import java.io.File;

public class FileAndFolderList {
    public static void main(String[] args) {
        displayList("D:/");
    }
    public static void displayList(String path) {
        File directory = new File(path);
        File[] list = directory.listFiles();
        if(list != null) {
            for(File curr : list) {
                if(curr.isDirectory()) {
                    System.out.printf("Folder: %s\n", curr.getName());
                } else if(curr.isFile()){
                    System.out.printf("File: %s\n", curr.getName());
                } else {
                    System.out.printf("Unknown: %s\n", curr.getName());
                }
            }
        }
    }
}
