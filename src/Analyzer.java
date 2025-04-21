import listeners.PacketListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class Analyzer {

    ProcessBuilder tSharkProcessBuilder;
    ProcessBuilder pythonProcessBuilder;
    Process tSharkProcess;
    Process pythonProcess;
    List<PacketListener> suspiciousPacketListeners;
    List<PacketListener> newPacketListeners;
    List<String> tSharkCommand;
    List<String> pythonCommand;

    public Analyzer() {
        tSharkCommand = Arrays.asList("tshark", "-T", "fields", "-e", "frame.number", "-e", "frame.time_relative", "-e", "ip.src", "-e", "ip.dst", "-e", "frame.len", "-e", "_ws.col.info", "-E", "separator=\",\"");
        tSharkProcessBuilder = new ProcessBuilder(tSharkCommand);
        pythonCommand = Arrays.asList("python", "-m", "main.py");
        pythonProcessBuilder = new ProcessBuilder(pythonCommand);
    }

    public void setFileName(String filename) {
        if (tSharkCommand.get(tSharkCommand.size()-2).equals("-r")) {
            tSharkCommand.remove(tSharkCommand.size()-1);
            tSharkCommand.remove(tSharkCommand.size()-1);
        }

        if (filename != null && !filename.isEmpty())
            tSharkCommand.addAll(Arrays.asList( "-r", filename));
    }

    public void startTShark() throws IOException {
        tSharkProcess = tSharkProcessBuilder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(tSharkProcess.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
            notifyNewPacketListeners(line);
        }
    }

    public void stopTShark() {
        if(tSharkProcess != null)
            tSharkProcess.destroy();
    }

    public void startPredictionModel(String inputCsv) throws IOException {
        // TODO: Add input CSV flag to python command, or use realtime flag
        pythonProcess = pythonProcessBuilder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(pythonProcess.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
            notifySuspiciousPacketListeners(line);
        }
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
