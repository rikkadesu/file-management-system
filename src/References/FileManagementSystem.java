import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class FileManagementSystem extends JFrame implements ActionListener {
    private JList<String> fileList;
    private DefaultListModel<String> listModel;
    private JTextField fileNameField;
    private JButton createButton;
    private JButton deleteButton;

    public FileManagementSystem() {
        setTitle("File Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();
        fileList = new JList<>(listModel);
        panel.add(new JScrollPane(fileList), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout());
        fileNameField = new JTextField();
        inputPanel.add(fileNameField, BorderLayout.CENTER);

        createButton = new JButton("Create");
        createButton.addActionListener(this);
        inputPanel.add(createButton, BorderLayout.EAST);

        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(this);
        inputPanel.add(deleteButton, BorderLayout.WEST);

        panel.add(inputPanel, BorderLayout.SOUTH);

        add(panel);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == createButton) {
            String fileName = fileNameField.getText();
            if (!fileName.isEmpty()) {
                File file = new File(fileName);
                try {
                    if (file.createNewFile()) {
                        listModel.addElement(fileName);
                        fileNameField.setText("");
                    } else {
                        JOptionPane.showMessageDialog(this, "File already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error creating file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (e.getSource() == deleteButton) {
            int selectedIndex = fileList.getSelectedIndex();
            if (selectedIndex != -1) {
                String selectedFile = listModel.getElementAt(selectedIndex);
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete the file: " + selectedFile + "?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    File file = new File(selectedFile);
                    if (file.delete()) {
                        listModel.remove(selectedIndex);
                    } else {
                        JOptionPane.showMessageDialog(this, "Error deleting file!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }

    public void listFilesAndFolders(String directory) {
        File folder = new File(directory);
        File[] files = folder.listFiles();
        if (files != null) {
            listModel.clear();
            for (File file : files) {
                listModel.addElement(file.getName());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                FileManagementSystem fileSystem = new FileManagementSystem();
                fileSystem.listFilesAndFolders("D:/");
            }
        });
    }
}
