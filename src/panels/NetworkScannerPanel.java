package panels;

import components.ButtonComponent;

import javax.swing.*;
import java.awt.*;

public class NetworkScannerPanel extends JPanel {


    private JTextField ipField;
    ButtonComponent buttonComponent;

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


        buttonComponent = new ButtonComponent();


        add(Box.createVerticalStrut(10));
        add(title);
        add(Box.createVerticalStrut(10));
        add(ipField);
        add(Box.createVerticalStrut(10));
        add(buttonComponent);
    }



    public String getIpAddress() {
        return ipField.getText();
    }

}
