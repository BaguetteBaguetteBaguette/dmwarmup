import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class CSVPrinter {

    /**
     * Method to escape the special characters and find a valid replacement for them
     *
     * @param data unformatted string to be formatted to remove special characters
     * @return formatted string without special characters
     * */
    private static String replaceSpecialChars(String data) {
        return data;
    }

    /**
     * This method converts an array into a string that will represent a row of a csv file
     *
     * @param data takes in a string array representing a row in a csv file
     * @return a CSV string that is the concatenated contents of the parameter data
     *
     * */
    private static String convertArrayToCSVString(String[] data) {
        StringBuffer sb = new StringBuffer("");
        for(String s: data) {
            sb.append(s);
            sb.append(",");
        }
        String s = sb.toString();
        s = s.substring(0, s.length() - 1);
        return s;
    }
    /**
     * This is the only method that is public on this class. Is responsible for taking a string array and printing
     * it into a csv file
     *
     * @param data the string array to be printed into the CSV file
     * */
    public static void write(String[] data) throws IOException {
        String dataString = convertArrayToCSVString(data);
//        dataString = replaceSpecialChars(dataString);
        File csv = new File("output.csv");
        if(csv.delete()) {
            System.out.println("Deleted file...remaking");
            csv.createNewFile();
        }else {
            csv.createNewFile();
            System.out.println("Created new file");
        }
        PrintWriter pw = new PrintWriter(csv);
        pw.write(dataString);
        pw.write("\n");
        pw.close();
    }
}
