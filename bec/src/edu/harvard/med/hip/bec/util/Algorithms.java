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
    
    public static ArrayList splitString(String value, String spliter)
    {
        ArrayList res = new ArrayList();
        StringTokenizer st = new StringTokenizer(value, spliter);
        while(st.hasMoreTokens())
        {
            String val = st.nextToken().trim();
            res.add( val );
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
    
    public static void main(String args[])
    {
        
        String s = "//1	gi|28363213|gb|CB241569.1|CB241569	99.21	253	0	1	1	251	1	253	6e-136	480.2";
        ArrayList ar = new ArrayList();
        ar = Algorithms.splitString(s,"\t");
        ar = Algorithms.splitString((String) ar.get(1),"|");
         s = "ff f f f f f f fd ffff ";
        s = Algorithms.cleanWhiteSpaces(s);
        System.out.println(s);
    }
}


