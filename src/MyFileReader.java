import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class MyFileReader {
    JFrame readerFrame;
    JTextArea textArea;
    JButton save, close;
    JPanel textPanel, buttonPanel;
    JScrollPane scrollPane;

    String path;

    MyFileReader(String absPath) {
        // These commands will run as soon as this class is called
        path = absPath;
        setReaderFrame(path);
        readFile(readerFrame, path);
    }

    public void setReaderFrame(String path) {
        // -- Button -- //

        int b_width = 100, b_height = 50;

        save = new JButton("Save");
        save.setBounds(135, 5, b_width, b_height);
        save.setActionCommand("save");
        save.addActionListener(action);

        close = new JButton("Close");
        close.setBounds(240, 5, b_width, b_height);
        close.setActionCommand("close");
        close.addActionListener(action);

        // -- Button -- //


        // -- Text Area -- //

        int t_width = 465, t_height = 380;

        textArea = new JTextArea();
        scrollPane = new JScrollPane(textArea);
        textArea.setBounds(10, 10, t_width, t_height);
        scrollPane.setBounds(10, 10, t_width, t_height);

        // -- Text Area -- //


        // -- Panel -- //

        textPanel = new JPanel();
        textPanel.setBounds(0, 0, 500, 400);
        textPanel.setBackground(Color.GRAY);
        textPanel.setLayout(null);
        textPanel.add(scrollPane);

        buttonPanel = new JPanel();
        buttonPanel.setBounds(0, 400, 500, 100);
        buttonPanel.setBackground(new Color(0x00faf0));
        buttonPanel.setLayout(null);
        buttonPanel.add(save);
        buttonPanel.add(close);

        // -- Panel -- //


        // -- Frame -- //

        File file = new File(path);
        readerFrame = new JFrame(file.getName());
        readerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        readerFrame.setLocationRelativeTo(SystemGUI.mainFrame);
        readerFrame.setLayout(null);
        readerFrame.setSize(500, 500);
        readerFrame.setResizable(false);
        readerFrame.add(textPanel);
        readerFrame.add(buttonPanel);
        readerFrame.setVisible(true);

        // -- Frame -- //
    }

    public ActionListener action = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            switch(e.getActionCommand()) {
                case "save" -> writeFile(readerFrame, path);
                case "close" -> readerFrame.dispose();
            }
        }
    };

    public void readFile(JFrame parentContainer, String path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) { // Puts the contents of a file to a Buffered Reader object
            String line;
            StringBuilder content = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                // Contents of the Buffered Reader object will be stored to a String Builder object to format
                // it (to have the appropriate "new line" character)
                content.append(line).append("\n");
            }
            // String Builder object's content will be then set to the text area
            textArea.setText(content.toString());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(parentContainer, "Error reading file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            readerFrame.dispose();
        }
    }

    public void writeFile(JFrame parentContainer, String path) {
        String content = textArea.getText(); // Takes the contents of the text area
        System.out.println(content); // Debugging purposes
        try {
            FileWriter file = new FileWriter(path); // Opens the file for writing
            file.write(content); // Writes into the file (overwriting)
            file.close(); // Closes the file to save changes
            JOptionPane.showMessageDialog(parentContainer, "Saved successfully! :D");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
