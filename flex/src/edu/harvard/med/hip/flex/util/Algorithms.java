package edu.harvard.med.hip.flex.util;


import java.util.*;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.user.*;
import java.io.*;

public class Algorithms
{
    
    // private static final String filePath = "/tmp/";
    private static final String filePath = "c:/";
    
    public static ArrayList rearangeSawToothPatternInFlexSequence(ArrayList sequences)
    {
        ArrayList result = new ArrayList();
        for (int i=0; i< sequences.size();i++)
            
            //sort array by cds length
            Collections.sort(sequences, new Comparator()
            {
                public int compare(Object o1, Object o2)
                {
                    return ((FlexSequence) o1).getCdslength() - ((FlexSequence) o2).getCdslength();
                }
                /** Note: this comparator imposes orderings that are
                 * inconsistent with equals. */
                public boolean equals(java.lang.Object obj)
                {      return false;  }
                // compare
            } );
            //get middle element
            int middle = sequences.size() / 2;
            for (int count = 0; count < middle; count++)
            {
                result.add(sequences.get(count));
                result.add(sequences.get(middle+count));
            }
            //ad last element
            if (result.size() < sequences.size()) result.add(sequences.get(sequences.size() -1));
            
            return result;
    }
    
    
    public static ArrayList rearangeSawToothPatternInSequenceDescription(ArrayList sequences)
    {
        ArrayList result = new ArrayList();
        //sort array by cds length
        Collections.sort(sequences, new Comparator()
        {
            public int compare(Object o1, Object o2)
            {
                return ((SequenceDescription) o1).getCdsLength() - ((SequenceDescription) o2).getCdsLength();
            }
            /** Note: this comparator imposes orderings that are
             * inconsistent with equals. */
            public boolean equals(java.lang.Object obj)
            {      return false;  }
            // compare
        } );
        //get middle element
        
        int middle = sequences.size() / 2;
        for (int count = 0; count < middle; count++)
        {
            result.add(sequences.get(count));
            result.add(sequences.get(middle+count));
        }
        //ad last element
        if (result.size() < sequences.size()) result.add(sequences.get(sequences.size() -1));
        return result;
    }
    
    public static LinkedList rearangeSawToothPatternInOligoPattern(LinkedList sequences)
    {
        LinkedList result = new LinkedList();
        //sort array by cds length
        Collections.sort(sequences, new Comparator()
        {
            public int compare(Object o1, Object o2)
            {
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
        for (int count = 0; count < middle; count++)
        {
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
        
        
        String s = "ff f f f f f f fd ffff ";
        s = Algorithms.cleanWhiteSpaces(s);
        System.out.println(s);
    }
}


