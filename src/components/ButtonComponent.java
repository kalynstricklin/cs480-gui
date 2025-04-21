package components;

import javax.swing.*;
import java.awt.*;

public class ButtonComponent extends JPanel {


    private JButton startButton;
    private JButton stopButton;

    public ButtonComponent() {


        startButton = new JButton("Start");
        stopButton = new JButton("Stop");

        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        setBackground(Color.WHITE);
        add(stopButton);
        add(startButton);
    }

    public JButton getStartButton() {
        return startButton;
    }

    public JButton getStopButton() {
        return stopButton;
    }

}
