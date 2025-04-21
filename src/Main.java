
import javax.swing.*;
import java.io.IOException;
import java.awt.event.WindowEvent;

public class Main {
    static Analyzer analyzer;
    static GUIFrame gui;

    public static void main(String[] args) {
        gui = new GUIFrame();
        analyzer = new Analyzer();
        String csvOutput = "networkScanOutput.csv";

        // Upload file panel START button listener
        gui.getUploadFilePanel().getButtons().getStartButton().addActionListener(e -> {
            if(gui.getUploadFilePanel().getFilePath().isBlank() || gui.getUploadFilePanel().getFilePath().isEmpty()) {
                JOptionPane.showMessageDialog(gui, "Please select a file to upload");
                return;
            }

            analyzer.setFileName(gui.getUploadFilePanel().getFilePath());
                new Thread(() -> {
                    try {
                        analyzer.startTShark();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }).start();
                
            gui.getUploadFilePanel().getButtons().getStartButton().setEnabled(false);
        });

        // Network scanner START button listener
        gui.getNetworkScannerPanel().getButtons().getStartButton().addActionListener(e -> {
            try {
                analyzer.startTShark();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            gui.getNetworkScannerPanel().getButtons().getStartButton().setEnabled(false);
        });

        // Upload file panel STOP button listener
        gui.getUploadFilePanel().getButtons().getStopButton().addActionListener(e -> {

        });

        // Network scanner STOP button listener
        gui.getNetworkScannerPanel().getButtons().getStopButton().addActionListener(e -> {
            analyzer.stopTShark();
            try {
                analyzer.startPredictionModel(csvOutput);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        GUIFrame gui = new GUIFrame();

    }
}
