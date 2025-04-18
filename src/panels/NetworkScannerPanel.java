package panels;

import javax.swing.*;
import java.awt.*;

public class NetworkScannerPanel extends JPanel {


    private JTextField ipField;
    private JButton startButton;
    private JButton stopButton;

    public NetworkScannerPanel(){

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Local Network Scanner", SwingConstants.CENTER);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("SansSerif", Font.BOLD, 14));

        ipField = new JTextField(15);
        ipField.setMaximumSize(new Dimension(200, 30));
        ipField.setHorizontalAlignment(JTextField.CENTER);
        ipField.setEditable(false);

        startButton = new JButton("Start");
        startButton.addActionListener(e -> handleStartScan());
        stopButton = new JButton("Stop");
        stopButton.addActionListener(e -> handleStopScan());


        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(stopButton);
        buttonPanel.add(startButton);

        add(Box.createVerticalStrut(10));
        add(title);
        add(Box.createVerticalStrut(10));
        add(ipField);
        add(Box.createVerticalStrut(10));
        add(buttonPanel);
    }

    public void handleStartScan(){

    }
    public void handleStopScan(){

    }

    public String getIpAddress() {
        return ipField.getText();
    }

    public JButton getStartButton() {
        return startButton;
    }

    public JButton getStopButton() {
        return stopButton;
    }
}
