package References;

public class ExistingName {
    public static void main(String[] args) {
        String fileName = "test.another. elmaoi..lamaw.test.txt";
        String[] fileNameArray;
        int fileNameArrayLength;
        String newFileName = "";

        fileNameArray = fileName.split("\\.");
        fileNameArrayLength = fileNameArray.length;
        System.out.println(fileNameArrayLength);

        for(int i=0; i<fileNameArrayLength; i++) {
            if(i<fileNameArrayLength-2) {
                newFileName += fileNameArray[i] + ".";
            } else {
                newFileName += fileNameArray[i];
            }
            if(i == fileNameArrayLength-2) {
                newFileName += "(1).";
            }
            System.out.println(newFileName);
        }
    }
}
