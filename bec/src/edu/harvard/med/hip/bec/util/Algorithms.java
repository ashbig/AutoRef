package edu.harvard.med.hip.bec.util;


import java.util.*;


import java.io.*;

public class Algorithms
{
    
    // private static final String filePath = "/tmp/";
    private static final String filePath = "c:/";
    
    
    /**
	 * This function takes the string and converts it into a reversed sequence.
	 * @param seq  A String object
	 * @return A String object
	 */
    public static String reverseString(String str)
    {
        String copy = "";  // The reversed copy.
        //       from str.length() - 1 down to 0.
        for ( int i = str.length() - 1;  i >= 0;  i-- )
        {
            // Append i-th char of str to copy.
            copy += str.charAt(i);
        }
        return copy;
    }
    public static String cleanWhiteSpaces(String str)
    {
        String copy = "";  // The reversed copy.
        char[] ar =str.toCharArray();
        //       from str.length() - 1 down to 0.
        for ( int i = 0; i < ar.length;  i++ )
        {
            if (Character.isLetterOrDigit(ar[i]) ) copy += ar[i];
          
        }
        
        return copy;
    }
    
   
    public static int findFirstLetter(String str)
    {
  
        char[] ar =str.toCharArray();
        return findFirstLetter(ar);
    }
     public static int findFirstLetter(char[] ar)
    {
       int count = 0;
        for ( ; count < ar.length;  count++ )
        {
               if (Character.isLetter(ar[count]) ) break;
        }
        return count;
    }
    
    public static String cleanChar(String str, char ch)
    {
        String copy = "";  // The reversed copy.
        char[] ar =str.toCharArray();
        //       from str.length() - 1 down to 0.
        for ( int i = 0; i < ar.length;  i++ )
        {
            if (ar[i] != ch ) 
                copy += ar[i];
          
        }
        
        return copy;
    }
   
    
    
    public static String replaceChar(String str, char oldch, char newchar)
    {
        StringBuffer copy = new StringBuffer();  // The reversed copy.
        char[] ar =str.toCharArray();
        //       from str.length() - 1 down to 0.
        for ( int i = 0; i < ar.length;  i++ )
        {
            if (ar[i] == oldch ) 
                copy.append( newchar );
            else
                copy.append( ar[i]);
        }
        
        return copy.toString();
    }
    
    
    
    public static String replaceString(String str, String oldstr, String newstr)
    {
        StringBuffer copy = new StringBuffer();  // The reversed copy.
        int curindex = 0;
        for (int count=0;count < str.length();count++)
        {
            curindex=str.indexOf(oldstr);
            if (curindex == -1 || curindex + 1 > str.length()) break;
            str = str.substring(0,curindex)+newstr +str.substring(curindex+oldstr.length());
            count=curindex+1;
        }
   
        return str;
    }
    
     public static ArrayList splitString(String value)
    {
        return  splitString( value, null);
    
    }
    public static ArrayList splitString(String value, String spliter)
    {
        ArrayList res = new ArrayList();
        StringTokenizer st  = null;
        if (spliter == null)
            st = new StringTokenizer(value);
        else
            st = new StringTokenizer(value, spliter);
        while(st.hasMoreTokens())
        {
            String val = st.nextToken().trim();
            res.add( val );
        }
        return res;
    }
    
    public static int[] getConvertStringToIntArray(String scores, String delim)
    {
        if ( scores == null ) return null;
        ArrayList arr_scores = Algorithms.splitString(scores,delim);
        int[] scores_numbers = new int[arr_scores.size()];
        for (int count = 0; count < arr_scores.size(); count++)
        {
            scores_numbers[count] =  (Integer.parseInt( (String) arr_scores.get(count)));
        }
        
        return scores_numbers;
    }
   
     // used to create quevery where parameter in range
    public static  String convertArrayToString(int[] arr, String delim)
    {
        String res = "";
        for (int count = 0; count < arr.length;count++)
        {
            res += arr[count];
            if (count != arr.length - 1 ) res += delim;
        }
        return res;
    }
    public static  String convertStringArrayToString(ArrayList arr, String delim)
    {
        String res = "";
        for (int count = 0; count < arr.size();count++)
        {
            res += (String)arr.get(count);
            if (count != arr.size()- 1 ) res += delim;
        }
        return res;
    }
    public static int numberOf(String str, char ch)
    {
        int res = 0;
        char[] ar =str.toCharArray();
        //       from str.length() - 1 down to 0.
        for ( int i = 0; i < ar.length;  i++ )
        {
            if ( ar[i] == ch)
                res++;
          
        }
        
        return res;
    }
    //array manipulation
    // mode define type of array members
  
    public static ArrayList findArrayOverlap(ArrayList array1, ArrayList array2)
    {
        ArrayList res = new ArrayList();
        ArrayList small_array = null;ArrayList large_array = null;
        if (array1.size() <= array2.size())
        {
            small_array = array1;
            large_array = array2;
        }
        else
        {
            large_array = array1;
             small_array = array2;
        }
        for (int ind = 0; ind <  small_array.size(); ind++)
        {
            if (  large_array.contains( small_array.get(ind)))
            {
                res.add(small_array.get(ind));
            }
        }
        return res;
    }
    
    //convert well nomenculature from A10 to int
    public static int convertWellFromA8_12toInt( String well) 
    {
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
    public static String convertWellFromInttoA8_12( int well) 
    {
        String position = null;
                
        int a_value = (int) 'A';
        int row_value = well % 8;
        int column = (int) well / 8  +1 ;   
        char rowname = (char) (a_value + row_value - 1);
        String column_string ="";
        
        if (row_value == 0 )
        {
             if (column - 1 < 10)
                return "H" +  "0"+(column -1 );
            else
                return "H" +  (column -1);
        }
     
        else
        {
            if (column < 10)
                return "" +  rowname+"0"+column;
            else
                return "" +  rowname+column;
        }
        
  
    }
    
    
    public static String convertWindowsFileNameIntoUnix(String filename)
    {
        String res = null;
        res = cleanChar(filename,':');
        if (res.charAt(0) != '/')
            res = "/"+res;
        res = replaceChar(res, File.separatorChar, '/');
        return res;
    }
    public static void main(String args[])
    {
       
       System.out.println(convertWellFromA8_12toInt("A1"));
       System.exit(0);
    }
    
    
}


