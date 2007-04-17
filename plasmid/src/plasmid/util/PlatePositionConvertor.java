/*
 * PlatePositionConvertor.java
 *
 * Created on May 23, 2005, 2:00 PM
 */

package plasmid.util;

import java.util.*;
import java.io.*;

/**
 *
 * @author  DZuo
 */
public class PlatePositionConvertor {
    
    /** Creates a new instance of PlatePositionConvertor */
    public PlatePositionConvertor() {
    }
        
    public static int convertWellFromA8_12toInt( String well) throws Exception {
        well.trim();
        int position = -1;
        well = well.toLowerCase();
        int row = (int)well.charAt(0);
        int column = Integer.parseInt(well.substring(1));
        int a_value = (int) 'a';
        int first_char_value = 0;
        int second_char_value = 0;
        int row_value = 0;
       
        if(column>12)
            throw new Exception("Well is not valid.");
        
        row_value = row - a_value + 1;
        
        if(row_value>8)
            throw new Exception("Well is not valid.");
        
        return (column - 1) * 8 +  row_value ;
    }
    
    //convert well nomenculature from A10 to int
    public static String convertWellFromInttoA8_12( int well) throws Exception {
        if(well>96)
            throw new Exception("Well is not valid.");
        
        String position = null;
        
        int a_value = (int) 'A';
        int row_value = well % 8;
        int column = (int) well / 8  +1 ;
        char rowname = (char) (a_value + row_value - 1);
        String column_string ="";
        
        if (row_value == 0 ) {
            if (column - 1 < 10)
                return "H" +  "0"+(column -1 );
            else
                return "H" +  (column -1);
        }
        
        else {
            if (column < 10)
                return "" +  rowname+"0"+column;
            else
                return "" +  rowname+column;
        }               
    }
    
    public static void main (String args[]) {
        String inputFile = "G:\\plasmid\\plate_outside_200704\\plate.txt";
        String outputFile = "G:\\plasmid\\plate_outside_200704\\plateout.txt";
        String line;
        
        try {
            PrintWriter output = new PrintWriter(new BufferedWriter(new FileWriter(outputFile)));
 
            BufferedReader in = new BufferedReader((new FileReader(inputFile)));
            
            while((line = in.readLine()) != null) {     
                int position = PlatePositionConvertor.convertWellFromA8_12toInt(line);
                output.println(line+"\t"+position);
            }
            in.close();
            output.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
