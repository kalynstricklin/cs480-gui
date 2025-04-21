import listeners.PacketListener;
import listeners.WriteListener;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Analyzer {

    ProcessBuilder tSharkProcessBuilder;
    ProcessBuilder pythonProcessBuilder;
    Process tSharkProcess;
    Process pythonProcess;
    List<PacketListener> suspiciousPacketListeners;
    List<PacketListener> newPacketListeners;
    List<WriteListener> writeListeners;
    final List<String> pythonCommand;
    final List<String> tSharkCommand = Arrays.asList("tshark","-l", "-T", "fields", "-e", "frame.number", "-e", "frame.time_relative", "-e", "ip.src", "-e", "ip.dst", "-e", "_ws.col.Protocol", "-e", "frame.len", "-e", "_ws.col.info", "-E", "separator=\",\"");
    PrintWriter writer;
    File projectPath;

    public Analyzer() {
        newPacketListeners = new ArrayList<>();
        suspiciousPacketListeners = new ArrayList<>();
        writeListeners = new ArrayList<>();
        tSharkProcessBuilder = new ProcessBuilder(tSharkCommand);
        projectPath = new File(".").getAbsoluteFile().getParentFile().getParentFile().getParentFile();
        pythonCommand = Arrays.asList(projectPath.getAbsolutePath() + "/.venv/Scripts/python.exe", "-m", "app");
    }

    public String startTSharkBatch(String inputFile, String outputFile) throws IOException {
        List<String> command = new ArrayList<>(tSharkCommand);
        command.add("-r");
        command.add(inputFile);
        tSharkProcessBuilder = new ProcessBuilder(command);
        tSharkProcess = tSharkProcessBuilder.start();

        File file = new File(outputFile);
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write("No.,Time,Source,Destination,Protocol,Length,Info\n");
        BufferedReader reader = new BufferedReader(new InputStreamReader(tSharkProcess.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            fileWriter.write(line.replace("\\,", ".") + "\n");
        }
        fileWriter.flush();
        fileWriter.close();

        System.out.println("Completed tshark process");
        return file.getAbsolutePath();
    }

    public void startTSharkRealtime() throws IOException {
        System.out.println("Starting realtime tshark process");
        List<String> command = new ArrayList<>(tSharkCommand);
        command.add(2, "-i");
        //TODO Change to correct interface #
        command.add(3, "9");
        tSharkProcessBuilder = new ProcessBuilder(command);
        tSharkProcess = tSharkProcessBuilder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(tSharkProcess.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            notifyNewPacketListeners(line);
        }
        System.out.println("Completed realtime tshark process");
    }

    public void stopTShark() {
        if(tSharkProcess != null)
            tSharkProcess.destroy();
    }

    public String startPredictionModelBatch(String inputFile) throws IOException {
        System.out.println("Starting batch prediction model");
        List<String> command = new ArrayList<>(pythonCommand);
        command.add("--file");
        command.add(inputFile);
        pythonProcessBuilder = new ProcessBuilder(command);
        pythonProcessBuilder.directory(new File(projectPath.getAbsolutePath() + "/backend"));
        pythonProcess = pythonProcessBuilder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(pythonProcess.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
            if(line.startsWith("Predictions saved to "))
                return projectPath.getAbsolutePath() + "/backend/" + line.substring("Predictions saved to ".length());
        }
        return "";
    }

    public void startPredictionModelRealtime() throws IOException {
        System.out.println("Starting realtime prediction model process");
        List<String> command = new ArrayList<>(pythonCommand);
        command.add("--realtime");
        pythonProcessBuilder = new ProcessBuilder(command);
        pythonProcessBuilder.directory(new File(projectPath.getAbsolutePath() + "/backend"));
        pythonProcess = pythonProcessBuilder.start();
        writer = new PrintWriter(pythonProcess.getOutputStream(), true);

        new Thread(() -> {
            String err;
            while(true) {
                try {
                    if ((err = pythonProcess.errorReader().readLine()) != null)
                        System.out.println(err);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(pythonProcess.getInputStream()));
        String line;
        String latestPacket = "";
        while ((line = reader.readLine()) != null) {
            if(line.startsWith("["))
                latestPacket = line;
            if(line.contains("Prediction: 1"))
                notifySuspiciousPacketListeners(latestPacket);
        }
        System.out.println("Completed realtime prediction model process");
    }

    public void writePacketToPredictionModel(String packet) {
        if(writer != null)
            writer.println(packet);
    }

    public void stopPredictionModel() {
        if(pythonProcess != null)
            pythonProcess.destroy();
    }

    public void addSuspiciousPacketListener(PacketListener listener) {
        suspiciousPacketListeners.add(listener);
    }

    public void addNewPacketListener(PacketListener listener) {
        newPacketListeners.add(listener);
    }

    private void notifySuspiciousPacketListeners(String suspiciousPacket) {
        for (PacketListener listener : suspiciousPacketListeners)
            listener.onNewPacket(suspiciousPacket);
    }

    private void notifyNewPacketListeners(String newPacket) {
        for (PacketListener listener : newPacketListeners)
            listener.onNewPacket(newPacket);
    }

}
