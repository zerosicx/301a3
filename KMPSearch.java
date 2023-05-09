import java.util.*;
import java.io.*;

public class KMPSearch {

    public static File tableFile;
    public static File searchFile;
    public static String pattern;
    public static String uniqueChars;
    
    public static void main(String[] args){
        if(args.length <2 || args.length >2){ //check if used correctly
            System.err.println("Usage: KMP search <KMPTable file> <file to be searched>");
        }

        tableFile = new File(args[0]);
        searchFile = new File(args[1]);

        //read and process the table file
        createTableArray(tableFile);

    }


    public static void createTableArray(File file){
        try{
            BufferedReader reader =new BufferedReader( new FileReader(file));
            String line = reader.readLine();
            
            int index = 0;
            while(reader.readLine() != null){
                line = line.replaceAll(" ", ""); //clean up lines

                if(index==0){
                    pattern = line.substring(2);// removes padding
                    System.out.println("PATTERN: (" + pattern + ")");
                } else {
                    //get unique character
                    System.out.println("LINE: (" + line + ")");
                }
                line = reader.readLine();//read next line
                index++;
            }

        }catch(Exception e){

        }
    }


    /**
     * gets and returns the skip value with the given pattern and input character
     * 
     * @param pattern   the expected character
     * @param input     the actual character
     */
    public static int getSkipValue(char pattern, char input){
        int skip = 1;

        return skip;
    }
}
