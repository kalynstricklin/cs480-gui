
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    static Analyzer analyzer;
    static GUIFrame gui;

    public static void main(String[] args) {
        gui = new GUIFrame();
        analyzer = new Analyzer();
        analyzer.addNewPacketListener(packet -> {
            analyzer.writePacketToPredictionModel(packet);
        });
        analyzer.addSuspiciousPacketListener(packet -> {
            if(packet.startsWith("["))
                gui.getPacketPanel().addPacketLine("Packet #" + packet.replace("[", "").split(",")[0].replace("'", "") + " : " + packet);
        });
        String csvOutput = "networkScanOutput.csv";

        // Upload file panel START button listener
        gui.getUploadFilePanel().getButtons().getStartButton().addActionListener(e -> {
            if(gui.getUploadFilePanel().getFilePath().isBlank() || gui.getUploadFilePanel().getFilePath().isEmpty()) {
                JOptionPane.showMessageDialog(gui, "Please select a file to upload");
                return;
            }

            new Thread(() -> {
                try {
                    // Run TShark batch scan
                    String tSharkOutputFilePath = analyzer.startTSharkBatch(gui.getUploadFilePanel().getFilePath(), csvOutput);
                    // Use TShark output to feed ML model
                    gui.getUploadFilePanel().getStatusField().setText("RUNNING PREDICTION MODEL. . .");
                    String predictionPath = analyzer.startPredictionModelBatch(tSharkOutputFilePath);
                    gui.getUploadFilePanel().getStatusField().setText("DONE! PRINTING RESULTS. . .");
                    System.out.println(predictionPath);
                    gui.getUploadFilePanel().getStatusField().setForeground(Color.GREEN);
                    BufferedReader reader = new BufferedReader(new FileReader(predictionPath));
                    String line;
                    while((line = reader.readLine()) != null) {
                        if(line.endsWith(",1"))
                            gui.getPacketPanel().addPacketLine("Packet #" + line.split(",")[0] + " : " + line);
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }).start();
                
            gui.getUploadFilePanel().getButtons().getStartButton().setEnabled(false);
            gui.getUploadFilePanel().getStatusField().setText("BATCHING PCAP DATA. . .");
            gui.getUploadFilePanel().getStatusField().setForeground(Color.YELLOW.darker());
        });

        // Network scanner START button listener
        gui.getNetworkScannerPanel().getButtons().getStartButton().addActionListener(e -> {
            new Thread(() -> {
                try {
                    analyzer.startTSharkRealtime();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }).start();

            new Thread(() -> {
                try {
                    analyzer.startPredictionModelRealtime();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }).start();

            gui.getNetworkScannerPanel().getButtons().getStartButton().setEnabled(false);
            gui.getNetworkScannerPanel().getField().setText("RUNNING");
            gui.getNetworkScannerPanel().getField().setForeground(Color.GREEN);
        });

        // Upload file panel STOP button listener
        gui.getUploadFilePanel().getButtons().getStopButton().addActionListener(e -> {
            analyzer.stopPredictionModel();
            analyzer.stopTShark();
            gui.getUploadFilePanel().getStatusField().setText("IDLE");
            gui.getUploadFilePanel().getStatusField().setForeground(Color.BLACK);
        });

        // Network scanner STOP button listener
        gui.getNetworkScannerPanel().getButtons().getStopButton().addActionListener(e -> {
            analyzer.stopTShark();
            analyzer.stopPredictionModel();
            gui.getNetworkScannerPanel().getButtons().getStopButton().setEnabled(true);
            gui.getNetworkScannerPanel().getField().setText("IDLE");
            gui.getNetworkScannerPanel().getField().setForeground(Color.BLACK);
        });
    }
}
