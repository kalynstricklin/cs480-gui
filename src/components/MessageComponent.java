package components;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.*;

public class MessageComponent extends JPanel {

    JLabel messageField;

    public MessageComponent() {
        messageField = new JLabel();
        messageField.setFont(getFont().deriveFont(Font.PLAIN, 16));
        messageField.setText("Upload PCAP File or Start Local Network Scanner");
        messageField.setVisible(true);
        add(messageField);
    }

    public void setMessage(String message, Color color) {
        messageField.setText(message);
        messageField.setForeground(color);
    }

}
