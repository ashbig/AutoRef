package edu.harvard.med.hip.flex.util;


import java.util.*;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.user.*;
import java.io.*;

public class Algorithms{
    
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
        int middle = (int)Math.ceil((double)sequences.size() / 2) - 1;
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
        int middle = (int)Math.ceil((double)sequences.size() / 2)- 1;
        for (int count = 0; count < middle; count++)
        {
            result.add(sequences.get(count));
            result.add(sequences.get(middle+count));
        }
        //ad last element 
        if (result.size() < sequences.size()) result.add(sequences.get(sequences.size() -1));
        return result;
    }







public static void main(String args[])
    {
      
        
}
}


