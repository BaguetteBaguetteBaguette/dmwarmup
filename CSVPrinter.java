import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

public class CSVPrinter {

    /**
     * This variable will determine if it is the first time that this class is being referenced or not
     * */
    private static boolean firstTimeThrough = true;
    /**
     * Private constructor hides the implicit public one and prevents this object
     * from being instantiated
     */
    private CSVPrinter() {
    }

    /**
     * Method to escape the special characters and find a valid replacement for them
     *
     * @param data unformatted string to be formatted to remove special characters
     * @return formatted string without special characters
     */
    private static String replaceSpecialChars(String data) {
        data = data.replace("&amp;", "&");
        data = data.replace("&#189;", "½");
        data = data.replace("&#188;", "¼");
        data = data.replace("&#190;", "¾");
        data = data.replace("&#8217;", "'");
        data = data.replace("&#241;", "n");
        data = data.replace("&#34;", "\"");
        data = data.replace("&#8531;", "⅓");
        data = data.replace("&rsquo;", "'");
        data = data.replace("&#176;", "°");
        data = data.replace("&#8532;", "⅔");
        data = data.replace("&#8212;", "-");
        data = data.replace("&#8211;", "-");
        data = data.replace("&#233;", "e");
        data = data.replace("&#8539", "⅛");
        return data;
    }

    /**
     * This method converts an array into a string that will represent a row of a
     * csv file
     *
     * @param data takes in a string array representing a row in a csv file
     * @return a CSV string that is the concatenated contents of the parameter data
     *
     */
    private static String convertArrayToCSVString(String[] data) {
        StringBuilder sb = new StringBuilder("");
        for (String s : data) {
            sb.append(s);
            sb.append(",");
        }
        String s = sb.toString();
        s = s.substring(0, s.length() - 1);
        s = replaceSpecialChars(s);
        return s;
    }

    /**
     * This is the only method that is public on this class. Is responsible for
     * taking a string array and printingit into a csv file
     *
     * @param data the arraylist of string arrays to be printed into the CSV file
     */
    public static void write(ArrayList<String[]> data) throws IOException {
        File csv = new File("output.csv");
        PrintStream ps  = new PrintStream(new FileOutputStream(csv, true));
        if(firstTimeThrough) {
            boolean wasFileDeleted = csv.delete();
            boolean wasFileCreated = csv.createNewFile();
            if (wasFileDeleted && wasFileCreated) {
                System.out.println("File successfully deleted and recreated");
            }
            String[] columnNames = { "Author", "Path", "Name", "Servings", "Ingredients", "Instructions" };
            String dataString = convertArrayToCSVString(columnNames);
            ps.println(dataString);
            firstTimeThrough = false;
        }
        for(String[] strArr : data) {
            String dataString = convertArrayToCSVString(strArr);
            ps.println(dataString);
        }
        ps.close();
    }
}
