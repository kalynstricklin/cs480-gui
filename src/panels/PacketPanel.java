package panels;

import javax.swing.*;
import java.awt.*;


public class PacketPanel extends JPanel {

    private JTextArea packetOutputArea;

    private JLabel label;

    public PacketPanel(){

        setBackground(Color.WHITE);

        label = new JLabel("Predicted Packets");
        packetOutputArea = new JTextArea(20, 80);
        packetOutputArea.setLineWrap(true);
        packetOutputArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(packetOutputArea);
        add(label);
        add(scrollPane);
    }
}
