package panels;

import components.ButtonComponent;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;

public class UploadFilePanel extends JPanel {

    private JTextField filePathField;
    private JButton uploadButton;
    private JTextField statusField;

    ButtonComponent buttonComponent;


    public UploadFilePanel() {

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Upload PCAP File", SwingConstants.CENTER);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        title.setFont(new Font("SansSerif", Font.BOLD, 18));

        // File selected
        filePathField = new JTextField(15);
        filePathField.setMaximumSize(new Dimension(200, 30));
        filePathField.setHorizontalAlignment(JTextField.CENTER);
        filePathField.setEditable(false);

        statusField = new JTextField(15);
        statusField.setMaximumSize(new Dimension(200, 30));
        statusField.setHorizontalAlignment(JTextField.CENTER);
        statusField.setEditable(false);
        statusField.setText("IDLE");

        uploadButton = new JButton("Upload File");
        uploadButton.addActionListener(e -> handleFileUpload());
        uploadButton.setBackground(Color.BLUE);
        uploadButton.setForeground(Color.WHITE);
        uploadButton.setFont(new Font("Arial", Font.BOLD, 14));

        JPanel uploadPanel = new JPanel();
        uploadPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        uploadPanel.setBackground(Color.WHITE);
        uploadPanel.add(filePathField);
        uploadPanel.add(uploadButton);

        buttonComponent = new ButtonComponent();

        add(title);
        add(Box.createVerticalStrut(10));
        add(uploadPanel);
        add(Box.createVerticalStrut(5));
        add(statusField);
        add(Box.createVerticalStrut(5));
        add(buttonComponent);

    }

    public JTextField getStatusField() {
        return statusField;
    }

    public void handleFileUpload(){
        JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        int result = fileChooser.showOpenDialog(this);

        if(result == JFileChooser.APPROVE_OPTION){
            File selectedFile = fileChooser.getSelectedFile();

            filePathField.setText(selectedFile.getAbsolutePath());
            System.out.println("Selected File: " + selectedFile.getAbsoluteFile());
        }
    }

    public String getFilePath() {
        return filePathField.getText();
    }

    public ButtonComponent getButtons() {
        return buttonComponent;
    }

}
