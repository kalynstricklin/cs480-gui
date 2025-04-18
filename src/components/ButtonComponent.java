package components;

import javax.swing.*;
import java.awt.*;

public class ButtonComponent extends JPanel {


    private JButton startButton;
    private JButton stopButton;

    public ButtonComponent(){


        startButton = new JButton("Start");
        startButton.addActionListener(e -> handleStartScan());
        stopButton = new JButton("Stop");
        stopButton.addActionListener(e -> handleStopScan());

        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        setBackground(Color.WHITE);
        add(stopButton);
        add(startButton);
    }


    public void handleStartScan(){

    }
    public void handleStopScan(){

    }

    public JButton getStartButton(){
        return startButton;
    }

    public JButton getStopButton() {
        return stopButton;
    }
}
