package panels;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;

public class UploadFilePanel extends JPanel {

    private JTextField filePathField;
    private JButton uploadButton;

    public UploadFilePanel(){
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Upload PCAP File", SwingConstants.CENTER);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("SansSerif", Font.BOLD, 14));

        // File selected
        filePathField = new JTextField(15);
        filePathField.setMaximumSize(new Dimension(200, 30));
        filePathField.setHorizontalAlignment(JTextField.CENTER);
        filePathField.setEditable(false);

        uploadButton = new JButton("Upload File");
        uploadButton.addActionListener(e -> handleFileUpload());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(filePathField);
        buttonPanel.add(uploadButton);
        add(Box.createVerticalStrut(10));
        add(title);
        add(Box.createVerticalStrut(10));
        add(buttonPanel);

    }

    public void handleFileUpload(){
        JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        int result = fileChooser.showOpenDialog(this);

        if(result == JFileChooser.APPROVE_OPTION){
            File selectedFile = fileChooser.getSelectedFile();

            filePathField.setText(selectedFile.getAbsolutePath());
            System.out.println("Selected File: " + selectedFile.getAbsoluteFile());

            // run tshark :-D
        }

    }

}
