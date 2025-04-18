
import panels.NetworkScannerPanel;
import panels.PacketPanel;
import panels.UploadFilePanel;

import javax.swing.*;
import java.awt.*;


public class GUIFrame extends JFrame {

    JPanel mainPanel;
    JPanel controlPanel;

    NetworkScannerPanel networkScannerPanel;
    UploadFilePanel uploadFilePanel;
    PacketPanel packetPanel;

    public GUIFrame() {
        setTitle("Machine Learning Network Packet Analyzer");
        setSize(900,600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setBackground(Color.LIGHT_GRAY);

        mainPanel = new JPanel(new GridLayout(2, 1));

        controlPanel = new JPanel(new GridLayout(1, 2));

        mainPanel.setBackground(Color.WHITE);

        networkScannerPanel = new NetworkScannerPanel();
        uploadFilePanel = new UploadFilePanel();
        packetPanel = new PacketPanel();


        controlPanel.add(networkScannerPanel);
        controlPanel.add(uploadFilePanel);

        mainPanel.add(controlPanel);
        mainPanel.add(packetPanel);

        add(mainPanel);
        setVisible(true);
    }


}
