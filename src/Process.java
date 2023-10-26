import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.Desktop;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class Process {
    public static ActionListener buttonAction = e -> {
        // cmd = Stores the command received
        String cmd = e.getActionCommand();
        switch(cmd) {
            case "create" -> {
                // if(SystemGUI.currentDirectory != null) = If the current directory is not null, folder and file creation is allowed
                if(SystemGUI.currentDirectory != null) {
                    String[] types = {"File", "Folder"};
                    int type = JOptionPane.showOptionDialog(SystemGUI.mainFrame, "Select type to create:", "Create...",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, types, 0);
                    if (type == 0) {
                        try {
                            // path = Asks for the filename to be created in the current location in the system
                            String path = JOptionPane.showInputDialog(SystemGUI.mainFrame, "Enter filename: ", "Create File",
                                    JOptionPane.PLAIN_MESSAGE);
                            if (path != null) {
                                // createFile(SystemGUI.currentDirectory + "\\" + path ) = Uses the current directory path
                                // and the file name entered to create a file on the current location
                                createFile(SystemGUI.currentDirectory + "\\" + path);
                            }
                        } catch (NullPointerException err) {
                            System.out.println("Operation cancelled. Method returned null.");
                        }
                    } else if (type == 1) {
                        try {
                            // path = Asks for the filename to be created in the current location in the system
                            String path = JOptionPane.showInputDialog(SystemGUI.mainFrame, "Enter folder name: ", "Create Folder",
                                    JOptionPane.PLAIN_MESSAGE);
                            if (path != null) {
                                // createFolder(SystemGUI.currentDirectory + "\\" + path ) = Uses the current directory path
                                // and the folder name entered to create a file on the current location
                                createFolder(SystemGUI.currentDirectory + "\\" + path);
                            }
                        } catch (NullPointerException err) {
                            System.out.println("Operation cancelled. Method returned null.");
                        }
                    }
                } else unAllowedItemCreation(SystemGUI.mainFrame, "Item");
            }
            case "rename" -> {
                if(SystemGUI.currentDirectory != null) {
                    String path = SystemGUI.itemList.getSelectedValue();
                    if (!path.equals(". . /")) {
                        String name = JOptionPane.showInputDialog(SystemGUI.mainFrame, "Enter new name: ");
                        if (name != null) renameFile(path, name);
                    }
                }
            }
            case "open" -> {
                try {
                    // if(SystemGUI.itemList.getSelectedValue() != null) = Used so that it will first check
                    // if there is an item selected. If none, the button will do nothing.
                    if(SystemGUI.itemList.getSelectedValue() != null) {
                        // path = Will take the string value of the selected item in the list then it will take
                        // the directory path of that item that is stored in the hashmap.
                        String path = SystemGUI.filePaths.get(SystemGUI.itemList.getSelectedValue());
                        if(isDirectory(path)) {
                            // Will only update the system if it's a folder/ directory
                            updateList(path, SystemGUI.items);
                        } else {
                            // Else it will try to read its contents or open another app
                            if(isTextFile(path)) new MyFileReader(path);
                            else {
                                try {
                                    Desktop.getDesktop().open(new File(path));
                                } catch (IOException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }
                        }
                    }
                } catch(NullPointerException err) {
                    //System.out.println("No selected file or folder. Method returned null.");
                    detectDrives(SystemGUI.items);
                }
            }
            case "delete" -> {
                try {
                    // path = Will take the string value of the selected item in the list then it will take
                    // the directory path of that item that is stored in the hashmap.
                    String path = SystemGUI.filePaths.get(SystemGUI.itemList.getSelectedValue());
                    // Uses a custom method to attempt to delete the file
                    if(path != null) deleteFile(path, SystemGUI.itemList.getSelectedValue());
                } catch(NullPointerException err) {
                    System.out.println("No selected file or folder. Method returned null.");
                }
            }
        }
    };

    // This enables double-clicking as a way to open folders and files
    // Almost same logic with the "open" button
    public static MouseAdapter mouseAction = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                int selectedIndex = SystemGUI.itemList.getSelectedIndex();
                if (selectedIndex != -1) {
                    String path = SystemGUI.filePaths.get(SystemGUI.itemList.getSelectedValue());
                    if(isDirectory(path)) {
                        // Will only update the system if it's a folder/ directory
                        updateList(path, SystemGUI.items);
                    } else {
                        // Else it will try to read its contents or open another app
                        if(isTextFile(path)) new MyFileReader(path);
                        else {
                            try {
                                Desktop.getDesktop().open(new File(path));
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    }
                }
            }
        }
    };

    public static ListSelectionListener selectionListener = e -> {
        if (!e.getValueIsAdjusting()) {
            // Get the selected item from the list
            String selectedItem = SystemGUI.itemList.getSelectedValue();
            String path = SystemGUI.filePaths.get(selectedItem);
            if(path != null) {
                // This part will show what type of the item we selected
                String type = isDirectory(path) ? "Folder" : "File";
                SystemGUI.fileInfo.setText("Type: " + type);
            } else SystemGUI.fileInfo.setText("");
        }
    };


    public static void createFile(String path) {
        File file = new File(path);
        try {
            // On this first if, it will check if the file is already existing.
            // If it does, it will create the file with an added string into its name "(1)"
            // depending on the last scanned existing file.
            if(file.exists()) {
                int i = 1;
                while(true) {
                    String repeat;
                    repeat = updateExistingFileName(path, i);
                    File newFile = new File(repeat);
                    i++;
                    if(!newFile.exists()) {
                        if(newFile.createNewFile()) {
                            messageCreateItemSuccess(SystemGUI.mainFrame, "File");
                            updateList(SystemGUI.currentDirectory, SystemGUI.items);
                            break;
                        } else { messageCreateItemFailed(SystemGUI.mainFrame, "File"); }
                    }
                }
            } else if(file.createNewFile()) {
                // If not existing, the system will immediately create the file and then update the list
                messageCreateItemSuccess(SystemGUI.mainFrame, "File");
                updateList(SystemGUI.currentDirectory, SystemGUI.items);
            } else {
                messageCreateItemFailed(SystemGUI.mainFrame, "File");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createFolder(String path) {
        File folder = new File(path);
        // On this first if, it will check if the folder is already existing.
        // If it does, it will create the folder with an added string into its name "(1)"
        // depending on the last scanned existing folder.
        if(folder.exists()) {
            int i = 1;
            while(true) {
                String repeat = String.format("(" + i + ")");
                File newFolder = new File(path+repeat);
                i++;
                if(!newFolder.exists()) {
                    if(newFolder.mkdir()) {
                        messageCreateItemSuccess(SystemGUI.mainFrame, "Folder");
                        updateList(SystemGUI.currentDirectory, SystemGUI.items);
                        break;
                    } else {
                        messageCreateItemFailed(SystemGUI.mainFrame, "Folder");
                    }
                }
            }
        } else if(folder.mkdir()) {
            // If not existing, the system will immediately create the folder and then update the list
            messageCreateItemSuccess(SystemGUI.mainFrame, "Folder");
            updateList(SystemGUI.currentDirectory, SystemGUI.items);
        } else {
            messageCreateItemFailed(SystemGUI.mainFrame, "Folder");
        }
    }

    // This method takes into account the positioning on where to append the () number of copy
    // in file creation
    public static String updateExistingFileName(String path, int iterate) {
        int dotIndex = path.lastIndexOf(".");
        if(dotIndex == -1) {
            return path + "(" + iterate + ")";
        } else {
            String newName = path.substring(0, dotIndex);
            String extension = path.substring(dotIndex);
            return newName + "(" + iterate + ")" + extension;
        }
    }

    public static void deleteFile(String path, String filename) {
        File file = new File(path);
        // choice = Shows a confirmation dialog using JOptionPane before deleting the file
        int choice = JOptionPane.showConfirmDialog(SystemGUI.mainFrame, "Do you want to delete this file?", filename, JOptionPane.YES_NO_OPTION);
        if(choice == 0) {
            if (file.delete()) {
                // If confirmed, the system will attempt to delete the file or folder and update the list
                messageDeleteItemSuccess(SystemGUI.mainFrame, "File");
                File upDirectory = file.getParentFile();
                if (upDirectory != null) {
                    updateList(upDirectory.getAbsolutePath(), SystemGUI.items);
                }
            } else {
                messageDeleteItemFailed(SystemGUI.mainFrame, "File");
            }
        }
    }

    public static void renameFile(String path, String newName) {
        path = SystemGUI.filePaths.get(path);
        File file = new File(path);
        File newFile = new File(file.getParent(), newName); // Creates a new File object for the new name
        boolean success = file.renameTo(newFile); // So we can use that File object as an argument for this method

        // Check if the renaming was successful
        if (success) {
            messageRenameItemSuccess(SystemGUI.mainFrame, "Item");
            updateList(SystemGUI.currentDirectory, SystemGUI.items);
        } else {
            messageRenameItemFailed(SystemGUI.mainFrame, "Item");
        }
    }

    public static void updateList(String path, DefaultListModel<String> items) {
        // currentDirectory = Will store the given argument as the current location in the system
        SystemGUI.currentDirectory = path;
        //System.out.println(SystemGUI.currentDirectory); // Debugging purposes
        // filePaths.clear() = Will clear the existing information about the previous list
        SystemGUI.filePaths.clear();
        // items.clear() = Will clear the existing items in the list
        items.clear();
        try {
            File directory = new File(path);
            // upDirectory = Will take the logical information of the parent directory of the chosen folder
            File upDirectory = directory.getParentFile();
            if(upDirectory != null) {
                // This part adds the absolute path of the parent directory of the chosen folder
                // Giving us the ability to go back
                SystemGUI.filePaths.put(". . /", upDirectory.getAbsolutePath());
                items.addElement(". . /");
            } else {
                // This is added because the root returns null as they are not considered as parent directory
                // This will also return null if selected
                items.addElement(". . /");
            }

            // list = Stores an array of directories here using listFiles() method
            // and will be added in the list one by one
            File[] list = directory.listFiles();
            if(list != null) {
                for(File curr : list) {
                    // They will be added in the hashmap with their name and absolute path
                    SystemGUI.filePaths.put(curr.getName(), curr.getAbsolutePath());
                    items.addElement(curr.getName());
                }
            }
        } catch(NullPointerException e) {
            // If it ever receives a null, it might mean that it encounter the root
            // so this method will update the list with the available drives
            detectDrives(items);
        }
    }
    public static void detectDrives(DefaultListModel<String> items) {
        SystemGUI.filePaths.clear();
        // drives = Will store an array of File object representing the current drives that the device have.
        File[] drives = File.listRoots();
        //System.out.println(SystemGUI.currentDirectory); // Debugging purposes

        if (drives != null && drives.length > 0) {
            for (File drive : drives) {
                // They will be added in the hashmap with their name and absolute path
                SystemGUI.filePaths.put(drive.getAbsolutePath(), drive.getAbsolutePath());
                items.addElement(SystemGUI.filePaths.get(drive.getAbsolutePath()));
            }
        } else {
            items.addElement("No drives found.");
        }
    }
    public static boolean isDirectory(String path) {
        // This method checks if the item chosen is a folder
        try {
            File file = new File(path);
            return file.isDirectory();
        } catch (NullPointerException err) {
            return true;
        }
    }
    public static boolean isTextFile(String path) {
        File file = new File(path);
        String fileName = file.getName();
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);

        // Define text-related file extensions
        String[] textExtensions = {"txt", "java", "json", "c", "c++", "py"};

        for (String ext : textExtensions) {
            if (extension.equalsIgnoreCase(ext)) {
                return true;
            }
        }
        return false;
    }

    public static void unAllowedItemCreation(JFrame parentContainer, String type) {
        JOptionPane.showMessageDialog(parentContainer, type + " creation now allowed here >:(", "Something went wrong", JOptionPane.WARNING_MESSAGE);
    }

    public static void messageCreateItemSuccess(JFrame parentContainer, String type) {
        JOptionPane.showMessageDialog(parentContainer, type + " creation successful :)", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void messageCreateItemFailed(JFrame parentContainer, String type) {
        JOptionPane.showMessageDialog(parentContainer, type + " creation failed :(", "Something went wrong", JOptionPane.WARNING_MESSAGE);
    }

    public static void messageDeleteItemSuccess(JFrame parentContainer, String type) {
        JOptionPane.showMessageDialog(parentContainer, type + " deletion successful :D", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void messageDeleteItemFailed(JFrame parentContainer, String type) {
        JOptionPane.showMessageDialog(parentContainer, type + " deletion failed :<", "Something went wrong", JOptionPane.WARNING_MESSAGE);
    }

    public static void messageRenameItemSuccess(JFrame parentContainer, String type) {
        JOptionPane.showMessageDialog(parentContainer, type + " rename successful :D", "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    public static void messageRenameItemFailed(JFrame parentContainer, String type) {
        JOptionPane.showMessageDialog(parentContainer, type + " rename failed :<", "Something went wrong", JOptionPane.WARNING_MESSAGE);
    }
}
