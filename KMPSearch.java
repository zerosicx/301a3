import java.util.*;
import java.io.*;

public class KMPSearch {

    public static ArrayList<Character> uniqueChars = new ArrayList<>();
    public static ArrayList<Character> pattern = new ArrayList<>();
    public static ArrayList<ArrayList<Integer>> skipTable = new ArrayList<ArrayList<Integer>>(){};
    
    public static void main(String[] args){
        if(args.length <2 || args.length >2){ //check if used correctly
            System.err.println("Usage: KMP search <KMPTable file> <file to be searched>");
        }

        //read and process the table file
        createTableArray(new File(args[0]));

        // process searh file and get location of match
        int[] results = searchPatternInFile(new File(args[1]));
        if (results.length==2){
            System.out.println("Match found at line index " + results[0] + " and character index " + results[1]);
        } else {
            System.out.println("No matches found for pattern");
        }
    }


    /**
     * Searches for the pattern in the given file
     * @param file  the file to be searched
     * @return [line index, character index]
     */
    public static int[] searchPatternInFile(File file){
        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            int lineIndex = 0;
            
            while( (line = reader.readLine()) != null){
                char[] cArray = line.toCharArray();
                int matchIndex = 0;
                Character match;
            
                // search each character in line
                for(int i=0; i<cArray.length; i++){

                    // check if character is in pattern or not
                    if(pattern.indexOf(cArray[i]) >= 0){
                        match = cArray[i];
                    }else{
                        match = '*';
                    }

                    // System.out.println("match char: " + match);
                    // System.out.println("pattern char: " + (pattern.get(matchIndex)));

                    if(match.equals(pattern.get(matchIndex))){ // check if current character matches pattern
                        if(matchIndex == pattern.size()-1){ // pattern is found
                            return new int[]{lineIndex, (i-matchIndex)};
                        }else {
                            matchIndex++; // not full pattern, match next character
                        }

                    }else{
                        // get skip value to skip ahead
                        int num = getSkipValue(matchIndex, uniqueChars.indexOf(match)); 
                        if((num+i) >= cArray.length-1){ // skip too large, read next line
                            i = cArray.length;
                        } else{ // skip ahead
                            i += num-1;
                        }
                        matchIndex = 0; // reset match                        
                    }
                }
                lineIndex++;
            }
            reader.close();

        } catch (Exception e){ // catch errors
            System.err.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }

        // no patterns found
        return null;
    }

    /**
     * Creates the skip table array and generates the pattern and unique characters array with the given skip table file
     * @param file  The file containing the skip table
     */
    public static void createTableArray(File file){
        try{
            BufferedReader reader =new BufferedReader( new FileReader(file));
            String line = reader.readLine();
            
            int index = 0;
            while(reader.readLine() != null){
                line = line.trim().replaceAll(" ", ""); //clean up lines

                if(index==0){ // get pattern
                    String s = line.substring(2).trim();// removes padding
                    Character[] sArr = s.chars().mapToObj(c -> (char) c).toArray(Character[]::new);
                    for (int i=0; i<sArr.length;i+=3){
                        pattern.add(sArr[i]);
                    }

                } else { // get unique characters and skip values
                    uniqueChars.add(line.charAt(0));

                    ArrayList<Integer> row = new ArrayList<>();
                    Character[] arr = line.chars().mapToObj(c -> (char) c).toArray(Character[]::new);

                    for (int i=3;i<arr.length;i+=3){
                        int skip = Integer.parseInt(arr[i].toString());
                        row.add(skip);
                    }
                    skipTable.add(row); // add to skip table

                }
                line = reader.readLine();//read next line
                index++;
            }
            reader.close();
            // System.out.println("PATTERN: (" + pattern + ")");
            // System.out.println("uChars: (" + uniqueChars + ")");
            // System.out.println("Table: " + skipTable);


        }catch(Exception e){ // catch errors
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * gets and returns the skip value with the given pattern and input character
     * 
     * @param row  the index of match character in uniqueChars
     * @param col  the index of match character in pattern
     */
    public static int getSkipValue(int row, int col){
        int skip = skipTable.get(col).get(row);
        return skip;
    }
}
