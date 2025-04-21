package panels;

import javax.swing.*;
import java.awt.*;


public class PacketPanel extends JPanel {

    private JTextArea packetOutputArea;

    private JLabel label;

    public PacketPanel(){

        setBackground(Color.WHITE);

        label = new JLabel("Suspicious Packets");
        packetOutputArea = new JTextArea(20, 80);
        packetOutputArea.setLineWrap(true);
        packetOutputArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(packetOutputArea);
        add(label);
        add(scrollPane);
    }

    public void addPacketLine(String line) {
        packetOutputArea.append(line + "\n");
        packetOutputArea.setCaretPosition(packetOutputArea.getDocument().getLength());
    }

}
