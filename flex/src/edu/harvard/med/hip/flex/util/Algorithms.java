package edu.harvard.med.hip.flex.util;


import java.util.*;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.user.*;
import java.io.*;

public class Algorithms {
    
    private static final String filePath = "/tmp/";
    //private static final String filePath = "c:/";
    
    public static ArrayList rearangeSawToothPatternInFlexSequence(ArrayList sequences) {
        for (int i=0; i< sequences.size();i++)
            
            //sort array by cds length
            Collections.sort(sequences, new Comparator() {
                public int compare(Object o1, Object o2) {
                    return ((FlexSequence) o1).getCdslength() - ((FlexSequence) o2).getCdslength();
                }
                /** Note: this comparator imposes orderings that are
                 * inconsistent with equals. */
                public boolean equals(java.lang.Object obj)
                {      return false;  }
                // compare
            } );
            
            ArrayList result = Algorithms.rearrangeInSawToothPattern(sequences);
            return result;
    }
    
    
    public static ArrayList rearangeSawToothPatternInSequenceDescription(ArrayList sequences) {
        //sort array by cds length
        Collections.sort(sequences, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((SequenceDescription) o1).getCdsLength() - ((SequenceDescription) o2).getCdsLength();
            }
            /** Note: this comparator imposes orderings that are
             * inconsistent with equals. */
            public boolean equals(java.lang.Object obj)
            {      return false;  }
            // compare
        } );
        //get middle element
        
        ArrayList result = Algorithms.rearrangeInSawToothPattern(sequences);
        return result;
    }
    
    public static LinkedList rearangeSawToothPatternInOligoPattern(LinkedList sequences) {
        LinkedList result = new LinkedList();
        //sort array by cds length
        Collections.sort(sequences, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((OligoPattern) o1).getCDSLength() - ((OligoPattern) o2).getCDSLength();
            }
            /** Note: this comparator imposes orderings that are
             * inconsistent with equals. */
            public boolean equals(java.lang.Object obj)
            {      return false;  }
            // compare
        } );
        //get middle element
        
        int middle = sequences.size() / 2;
        for (int count = 0; count < middle; count++) {
            result.add(sequences.get(count));
            result.add(sequences.get(middle+count));
        }
        //ad last element
        if (result.size() < sequences.size()) result.add(sequences.get(sequences.size() -1));
        return result;
    }
    
    /**
     * This function takes the sorted list of sequences and rearranges it into saw-tooth pattern
     * @param sequences A sorted list based on CDS length
     * @return A list rearranged in saw-tooth pattern
     */
    public static ArrayList rearrangeInSawToothPattern(List sequences) {
        ArrayList result = new ArrayList();
        
        int middle = sequences.size() / 2;
        for (int count = 0; count < middle; count++) {
            result.add(sequences.get(count));
            result.add(sequences.get(middle+count));
        }
        //ad last element
        if (result.size() < sequences.size()) result.add(sequences.get(sequences.size() -1));
        
        return result;                
    }
    
    /**
     * This function takes the string and converts it into a reversed sequence.
     * @param seq  A String object
     * @return A String object
     */
    public static String reverseString(String str) {
        String copy = "";  // The reversed copy.
        //       from str.length() - 1 down to 0.
        for ( int i = str.length() - 1;  i >= 0;  i-- ) {
            // Append i-th char of str to copy.
            copy += str.charAt(i);
        }
        return copy;
    }
    public static String cleanWhiteSpaces(String str) {
        String copy = "";  // The reversed copy.
        char[] ar =str.toCharArray();
        //       from str.length() - 1 down to 0.
        for ( int i = 0; i < ar.length;  i++ ) {
            if (Character.isLetterOrDigit(ar[i]) ) copy += ar[i];
            
        }
        
        return copy;
    }
    
    public static String cleanChar(String str, char ch) {
        String copy = "";  // The reversed copy.
        char[] ar =str.toCharArray();
        //       from str.length() - 1 down to 0.
        for ( int i = 0; i < ar.length;  i++ ) {
            if (ar[i] != ch )
                copy += ar[i];
            
        }
        
        return copy;
    }
    
    
    public static String replaceChar(String str, char oldch, char newchar) {
        StringBuffer copy = new StringBuffer();  // The reversed copy.
        char[] ar =str.toCharArray();
        //       from str.length() - 1 down to 0.
        for ( int i = 0; i < ar.length;  i++ ) {
            if (ar[i] == oldch )
                copy.append( newchar );
            else
                copy.append( ar[i]);
        }
        
        return copy.toString();
    }
    
    public static ArrayList splitString(String value, String spliter) {
        ArrayList res = new ArrayList();
        StringTokenizer st = new StringTokenizer(value, spliter);
        while(st.hasMoreTokens()) {
            String val = st.nextToken().trim();
            res.add( val );
        }
        return res;
    }
    
    
    
    
    
    public static int numberOf(String str, char ch) {
        int res = 0;
        char[] ar =str.toCharArray();
        //       from str.length() - 1 down to 0.
        for ( int i = 0; i < ar.length;  i++ ) {
            if ( ar[i] == ch)
                res++;
            
        }
        
        return res;
    }
    
    //convert well nomenculature from A10 to int
    public static int convertWellFromA8_12toInt( String well) {
        int position = -1;
        well = well.toLowerCase();
        int row = (int)well.charAt(0);
        int column = Integer.parseInt(well.substring(1));
        int a_value = (int) 'a';
        int first_char_value = 0;
        int second_char_value = 0;
        int row_value = 0;
        
        
        row_value = row - a_value + 1;
        
        return (column - 1) * 8 +  row_value ;
        
        
    }
    //convert well nomenculature from A10 to int
    public static String convertWellFromInttoA8_12( int well) {
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
    
    
    public static void main(String args[]) {
        String inputFile = "C:\\Documents and Settings\\dzuo\\My Documents\\work\\production\\vcholera\\wellin.txt";
        String outputFile = "C:\\Documents and Settings\\dzuo\\My Documents\\work\\production\\vcholera\\wellout.txt";
        String line;
        
        try {
            PrintWriter output = new PrintWriter(new BufferedWriter(new FileWriter(outputFile)));
 
            BufferedReader in = new BufferedReader((new FileReader(inputFile)));
            //String ignore = in.readLine();
            
            while((line = in.readLine()) != null) {
                /**
                String s = Algorithms.cleanWhiteSpaces(line);
                int num = Algorithms.convertWellFromA8_12toInt(s);
                output.println(line+"\t"+num);
                */
                
                int num = Integer.parseInt(line);
                String s = Algorithms.convertWellFromInttoA8_12(num);
                String positionX = s.substring(0, 1);
                String positionY = s.substring(1);
                int y = Integer.parseInt(positionY);
                java.text.NumberFormat fmt = java.text.NumberFormat.getInstance();
                fmt.setMaximumIntegerDigits(2);
                fmt.setMinimumIntegerDigits(2);
                fmt.setGroupingUsed(false);
                String well = positionX+fmt.format(y);
                output.println(line+"\t"+well+"\t"+positionX+"\t"+y);
            }
            in.close();
            output.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        
        /**
        String s = "ff f f f f f f fd ffff ";
        s = Algorithms.cleanWhiteSpaces(s);
        System.out.println(s);
         **/
    }
}


