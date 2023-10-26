package References;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileHandling {
    public static void main(String[] args) {
//        createFolder("testFolder");
//        sleep(3000);
//        createFile("testFolder/test.txt");
        deleteFile("testFolder/test.txt");
        sleep(3000);
        deleteFolder("testFolder");
    }
    public static void createFile(String path) {
        File file = new File(path);
        try {
            if(file.createNewFile()) {
            System.out.println("File created successfully :D");
            } else {
                System.out.println("Already exists...");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void createFolder(String path) {
        File file = new File(path);
        if(file.mkdir()) {
            System.out.println("Folder created successfully :)");
        } else {
            System.out.println("Already exists dummy -_-");
        }
    }
    public static void deleteFile(String path) {
        File file = new File(path);
        if(file.exists()) {
            if (file.delete()) {
                System.out.println("File deleted successfully :D");
            } else {
                System.out.println("Deletion failed :(");
            }
        } else {
            System.out.println("File does not exist :3");
        }
    }
    public static void deleteFolder(String path) {
        File file = new File(path);
        if(file.exists()) {
            if (file.delete()) {
                System.out.println("Folder deleted successfully :D");
            } else {
                System.out.println("Deletion failed :(");
            }
        } else {
            System.out.println("Folder does not exist :3");
        }
    }
    public static void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
