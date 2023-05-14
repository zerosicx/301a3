import java.util.*;
import java.io.*;

public class KMPSearch {

    public static ArrayList<Character> uniqueChars = new ArrayList<>();
    public static ArrayList<Character> pattern = new ArrayList<>();
    public static ArrayList<ArrayList<Integer>> skipTable = new ArrayList<ArrayList<Integer>>() {
    };

    public static void main(String[] args) {
        if (args.length < 2 || args.length > 2) { // check if used correctly
            System.err.println("Usage: KMP search <KMPTable file> <file to be searched>");
        }

        // read and process the table file
        createTableArray(new File(args[0]));

        // process searh file and get location of match
        searchPatternInFile(new File(args[1]));
    }

    /**
     * Searches for the pattern in the given file and prints the first occurence of
     * the pattern per line in the structure:
     * [line index] [character index]
     * 
     * @param file the file to be searched
     */
    public static void searchPatternInFile(File file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            int lineIndex = 0;

            while ((line = reader.readLine()) != null) {
                char[] cArray = line.toCharArray();
                int matchIndex = 0;
                Character match;

                // search each character in line
                for (int i = 0; i < cArray.length; i++) {

                    // check if character is in pattern or not
                    if (pattern.indexOf(cArray[i]) >= 0) {
                        match = cArray[i];
                    } else {
                        match = '*';
                    }

                    if (match.equals(pattern.get(matchIndex))) { // check if current character matches pattern

                        if (matchIndex == pattern.size() - 1) { // pattern is found
                            System.out.println((lineIndex + 1) + " " + (i - matchIndex + 1));
                        } else { // not full pattern, match next character
                            matchIndex++; 
                        }

                    } else {
                        // get skip value to skip ahead
                        int num = getSkipValue(matchIndex, uniqueChars.indexOf(match));
                        if ((num + i) >= cArray.length - 1) { // skip too large, read next line
                            i = cArray.length;
                        } else { // skip ahead
                            i += num - 1;
                        }
                        matchIndex = 0; // reset match
                    }
                }
                lineIndex++;
            }
            reader.close();

        } catch (Exception e) { // catch errors
            System.err.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Creates the skip table array and generates the pattern and unique characters
     * array with the given skip table file
     * 
     * @param file The file containing the skip table
     */
    public static void createTableArray(File file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            int index = 0;

            while ((line = reader.readLine()) != null) { //read line by line
                line = line.trim().replaceAll(" ", ""); // clean up lines

                if (index == 0) { // get pattern and save to pattern array
                    char[] sArr = line.toCharArray();
                    for (int i = 0; i < sArr.length; i++) {
                        pattern.add(sArr[i]);
                    }

                } else { // get unique characters and skip values
                    uniqueChars.add(line.charAt(0)); // get unique character

                    ArrayList<Integer> row = new ArrayList<>();
                    char[] arr = line.toCharArray();

                    for (int i = 1; i < arr.length; i++) { //build skip rows for table
                        int skip = Integer.parseInt("" + arr[i]);
                        row.add(skip);
                    }
                    skipTable.add(row); // add to skip table

                }
                index++;
            }
            reader.close(); // close reader

        } catch (Exception e) { // catch errors
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * gets and returns the skip value with the given pattern and input character
     * 
     * @param row the index of match character in uniqueChars
     * @param col the index of match character in pattern
     */
    public static int getSkipValue(int row, int col) {
        int skip = skipTable.get(col).get(row);
        return skip;
    }
}
