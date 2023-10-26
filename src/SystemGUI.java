/*
* This system's logic
* - Runs a method that detects the available root drives
* - Selecting a directory will update the list as well as storing the current directory to a variable
* - The return option in the list takes the stored directory and takes its parent directory
*/


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class SystemGUI {
    final static String system_name = "File Management System";
    static String currentDirectory;

    static JFrame mainFrame;
    static JPanel listPanel, buttonPanel;
    static JButton create, rename, delete, open;
    static JLabel fileInfo;
    static DefaultListModel<String> items;
    static JList<String> itemList;
    static JScrollPane scrollPane;
    static HashMap<String, String> filePaths;


    public static void main(String[] args) {
        // -- Button -- //
        int x_margin = 21;
        int y_margin = 16;

        int[] c_bounds = {15 + x_margin, 23 + y_margin, 100, 55};
        create = createButton("Create", c_bounds, "create", Process.buttonAction);

        int[] r_bounds = {120 + x_margin, 23 + y_margin, 100, 55};
        rename = createButton("Rename", r_bounds, "rename", Process.buttonAction);

        int[] d_bounds = {330 + x_margin, 23 + y_margin, 100, 55};
        delete = createButton("Delete", d_bounds, "delete", Process.buttonAction);

        int[] o_bounds = {225 + x_margin, 23 + y_margin, 100, 55};
        open = createButton("Open", o_bounds, "open", Process.buttonAction);

        // -- Button -- //


        // -- Label -- //

        fileInfo = new JLabel();
        fileInfo.setBounds(36, 3, 200, 30);
        fileInfo.setText("");

        // -- Label -- //


        // -- Scroll Pane and List -- //

        filePaths = new HashMap<>(); // This hashmap is used to store the name (key) of a directory as well as its path (value)
        items = new DefaultListModel<>(); // This is the component for the JList
        Process.detectDrives(items); // First detection of current root drives

        itemList = new JList<>(items);
        itemList.setFont(new Font("Arial", Font.BOLD, 20));
        itemList.addListSelectionListener(Process.selectionListener);
        itemList.addMouseListener(Process.mouseAction);

        scrollPane = new JScrollPane(itemList); // Used scroll pane so that it will add a scroll bar once the list gets out of bounds
        scrollPane.setBounds(10, 10, 465, 330);

        // -- Scroll Pane and List -- //


        // -- Panel -- //

        listPanel = new JPanel(); // Panel for the list part of the system
        listPanel.setPreferredSize(new Dimension(500, 350));
        listPanel.setBackground(Color.BLACK);
        listPanel.setLayout(null);
        listPanel.add(scrollPane);

        buttonPanel = new JPanel(); // Panel for the button part of the system
        buttonPanel.setPreferredSize(new Dimension(500, 350));
        buttonPanel.setBackground(Color.orange);
        buttonPanel.setLayout(null);
        JButton[] buttons = {create, rename, delete, open};
        addComponents(buttons, buttonPanel);
        buttonPanel.add(fileInfo);

        // -- Panel -- //


        // -- Frame -- //

        mainFrame = new JFrame(system_name); // The main frame of the system
        mainFrame.setSize(500, 500);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setResizable(false);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainFrame.add(listPanel, BorderLayout.NORTH);
        mainFrame.add(buttonPanel, BorderLayout.CENTER);

        mainFrame.setVisible(true);

        // -- Frame -- //
    }

    public static <T, U> void addComponents(T[] compArray, U container) {
        for(T component : compArray) {
            ((Container)container).add((Component)component);
        }
    }
    public static JButton createButton(String name, int[] bounds, String action, ActionListener listener) {
        JButton button = new JButton(name);
        button.setBounds(bounds[0], bounds[1], bounds[2], bounds[3]);
        button.setActionCommand(action);
        button.addActionListener(listener);
        return button;
    }
}
