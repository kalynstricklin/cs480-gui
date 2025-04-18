import listeners.ScanListener;
import listeners.StateListener;
import models.SimulatorState;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Simulator {

    String filepath;

    boolean continueProcessing;
    Thread writerThread;

    String currentMessage;
    List<ScanListener> scanListeners;
    List<StateListener> stateListeners;

    // File stuff
    File inputFile;

    public Simulator(String filepath) throws IOException {
        this.filepath = filepath;

//        writerThread = new Thread(
//        () -> {
//            try {
//                System.out.println("Waiting for client connection...");
//                setState(SimulatorState.WAITING);
//
//
//                System.out.println("Client connected");
//                setState(SimulatorState.CONNECTED);
//
//                List<String> lines = Files.readAllLines(Paths.get(filepath), StandardCharsets.UTF_8);
//
//                for (String line : lines) {
//                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
//
//                    if(line.length() > 30) {
//                        // Get rid of date at end of line
//                        currentMessage = line.substring(0, line.length()-13) + "\r\n";
//                    } else {
//                        currentMessage = line + "\r\n";
//                    }
//
//
//                    System.out.print(currentMessage);
//                    out.print(currentMessage);
//                    out.flush();
//
//                    for(ScanListener listener : scanListeners) {
//                        listener.onNewScan(currentMessage);
//                    }
//                }
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            } finally {
//                setState(SimulatorState.ENDED);
//            }
//        });
    }

    public void init() {

        System.out.println("Server started");

        // Start server
        continueProcessing = true;

        // Get file from filepath
        inputFile = new File(filepath);
        if(!inputFile.exists()) {
            setState(SimulatorState.ERROR);
        }

        scanListeners = new ArrayList<>();
        stateListeners = new ArrayList<>();
    }

    public void start() {
//        this.writerThread.start();
    }


    public void stop() {
        synchronized (this) {
            continueProcessing = false;

        }
    }

    public void addScanListener(ScanListener listener) {
        scanListeners.add(listener);
    }

    public void addStateListener(StateListener listener) {
        stateListeners.add(listener);
    }

    private void setState(SimulatorState state) {
        for(StateListener listener : stateListeners) {
            listener.onStateChange(state);
        }
    }

}
