import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;

public class KMPTable {

    public static String pattern;

    public static void main(String[] args) {
        // int[][] table = createSkipTable("xyxyz");
        if (args.length == 1) {
            pattern = args[0];
        } else {
            System.err.println("Usage: KMPTable <pattern>");
        }

        // System.out.println("Prefixes:");
        // ArrayList<String> prefixes = getPrefixes(pattern);
        // for (String s : prefixes) {
        // System.out.println(s);
        // }

        // System.out.println("Suffixes:");
        // ArrayList<String> suffixes = getSuffixes(pattern);
        // for (String s : suffixes) {
        // System.out.println(s);
        // }

        // System.out.println("Calculating the skip table: ");
        int[][] skipTable = createSkipTable();

        // Print the skip table
        printTable(skipTable);

        try {
            printTableToFile(skipTable);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Should create a skip table based on a pattern that is n x u + 1
     * n = pattern length
     * u = unique characters in pattern + 1 symbol to represent any other character
     * 
     * @return
     */
    public static int[][] createSkipTable() {
        // get unique characters in skip table by using a set

        ArrayList<Character> uniqueChars = new ArrayList<Character>();
        for (Character c : pattern.toCharArray()) {
            if (!uniqueChars.contains(c)) {
                uniqueChars.add(c);
            }
        }

        // represent all other types of characters
        uniqueChars.add('*');

        // create skip table
        int[][] skipTable = new int[uniqueChars.size()][pattern.length()];

        for (int j = 0; j < uniqueChars.size(); j++) {
            for (int i = 0; i < pattern.length(); i++) {
                // If they match, set the skip value to 0.
                if (pattern.charAt(i) == uniqueChars.get(j)) {
                    skipTable[j][i] = 0;
                }

                // Otherwise if they are NOT equal
                else {
                    // If uniqueChars == *, skip value is length of current pattern
                    if (uniqueChars.get(j) == '*') { // this should just be the else block tbh
                        skipTable[j][i] = i;
                    }

                    // Otherwise, the prefix arrays need to be computed
                    ArrayList<String> prefixes = getPrefixes(pattern.substring(0, i + 1));

                    // Eg. xyxyx when xyxyz
                    ArrayList<String> suffixes = getSuffixes(pattern.substring(0, i) + uniqueChars.get(j));

                    int skipVal = i + 1;

                    for (int k = 0; k < prefixes.size(); k++) {
                        String p = prefixes.get(k);
                        if (suffixes.contains(p)) {
                            // If the longest proper prefix is also a suffix, calculate the skip value
                            skipVal = (i + 1) - p.length();
                            // System.out.println(p);
                            // System.out.println("Longest prefix length: " + p.length() + " Current i: " +
                            // i
                            // + " skipValue: " + skipVal);
                            break;

                        }
                    }

                    skipTable[j][i] = skipVal;
                }
            }
        }

        return skipTable;
    }

    public static int getSkipValue() {
        return 1;
    }

    public static ArrayList<String> getPrefixes(String s) {
        ArrayList<String> prefixes = new ArrayList<String>();
        for (int i = 0; i < s.length() - 1; i++) {
            prefixes.add(s.substring(0, s.length() - 1 - i));
        }

        return prefixes;
    }

    public static ArrayList<String> getSuffixes(String s) {
        ArrayList<String> suffixes = new ArrayList<String>();
        for (int i = 0; i < s.length(); i++) {
            suffixes.add(s.substring(i, s.length()));
        }

        return suffixes;
    }

    public static void printTable(int[][] skipTable) {
        // The first line is the characters in the pattern space separated
        System.out.print("  "); // Making space for padding
        for (Character c : pattern.toCharArray()) {
            System.out.print(c + " ");
        }
        System.out.println();

        // Get unique characters to print out per line
        ArrayList<Character> uniqueChars = new ArrayList<Character>();
        for (Character c : pattern.toCharArray()) {
            if (!uniqueChars.contains(c)) {
                uniqueChars.add(c);
            }
        }

        // represent all other types of characters
        uniqueChars.add('*');

        for (int r = 0; r < skipTable.length; r++) { // should be 3
            // Print the unique character
            System.out.print(uniqueChars.get(r) + " ");

            for (int c = 0; c < skipTable[r].length; c++) {
                // Print skip value
                System.out.print(skipTable[r][c] + " ");
            }
            System.out.println();
        }
    }

    public static void printTableToFile(int[][] skipTable) throws Exception {
        // Create a new file
        PrintWriter pw = new PrintWriter(new File("table.kmp"));

        // The first line is the characters in the pattern space separated
        pw.print("  "); // Making space for padding
        for (Character c : pattern.toCharArray()) {
            pw.write(c + " ");
        }
        pw.println("");

        // Get unique characters to print out per line
        ArrayList<Character> uniqueChars = new ArrayList<Character>();
        for (Character c : pattern.toCharArray()) {
            if (!uniqueChars.contains(c)) {
                uniqueChars.add(c);
            }
        }

        // represent all other types of characters
        uniqueChars.add('*');

        for (int r = 0; r < skipTable.length; r++) { // should be 3
            // Print the unique character
            pw.print(uniqueChars.get(r) + " ");

            for (int c = 0; c < skipTable[r].length; c++) {
                // Print skip value
                pw.print(skipTable[r][c] + " ");
            }
            pw.println();
        }

        pw.close();

    }
}
