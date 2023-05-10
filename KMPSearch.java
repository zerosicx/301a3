import java.util.*;
import java.io.*;

public class KMPSearch {

    public static File tableFile;
    public static File searchFile;
    public static String pattern;
    public static String uniqueChars = "";
    public static ArrayList<int[]> skipTable = new ArrayList<int[]>(){};
    
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
                line = line.trim(); //clean up lines

                if(index==0){
                    pattern = line.substring(5).replaceAll(" ", "");// removes padding
                    System.out.println("PATTERN: (" + pattern + ")");
                } else {

                    String[] arr = line.split(" ");

                    int[] row = new int[arr.length];
                    for(int i=0; i<arr.length; i++){
                        if(i==0){
                            uniqueChars += arr[i];
                        }else{
                            int num = (Integer.parseInt(arr[1].trim().replace(" ", "")));
                            row[i-1] = num;
                        }
                    }
                    skipTable.add(row);
                }
                line = reader.readLine();//read next line
                index++;
            }
            reader.close();
            System.out.println("PATTERN: (" + pattern + ")");
            System.out.println("uChars: (" + uniqueChars + ")");
            System.out.println("Table: " + skipTable);


        }catch(Exception e){
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * gets and returns the skip value with the given pattern and input character
     * 
     * @param match   the expected character
     * @param input     the actual character
     */
    public static int getSkipValue(char match, char input){
        int skip = 0;

        return skip;
    }
}
