package edu.harvard.med.hip.flex.util;


import java.util.*;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.user.*;
import java.io.*;

public class Algorithms{
public static ArrayList rearangeSawToothPatternInFlexSequence(ArrayList sequences)
    {
        ArrayList result = new ArrayList();
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
        int middle = (int)Math.ceil((double)sequences.size() / 2);
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
        int middle = (int)Math.ceil((double)sequences.size() / 2);
        for (int count = 0; count < middle; count++)
        {
            result.add(sequences.get(count));
            result.add(sequences.get(middle+count));
        }
        //ad last element 
        if (result.size() < sequences.size()) result.add(sequences.get(sequences.size() -1));
        return result;
    }


public static File writeFile(Vector fileData, String file_name)
throws IOException
{
    File fl = new File(file_name);
    FileWriter fr = new FileWriter(fl);
    
    for (int count = 0; count < fileData.size(); count++)
    {
        fr.write((String)fileData.get(count));
    }
    fr.flush();
    fr.close();
 
    return fl;
}


//send e-mail to the user with all GI separated to three groups
public static void notifyUser(String user_name, String fileName, Vector messages) throws Exception
    {
        AccessManager am = AccessManager.getInstance();
        String to = am.getEmail( user_name );
        //String to = "etaycher@hms.harvard.edu";
        String from = "etaycher@hms.harvard.edu";
        String subject = "User Notification: Mgc clone master list "+fileName+" was uploaded to database";
        subject += "\nReport is attached.";
        String msgText = null;
        File fl = Algorithms.writeFile(messages,"Report")  ;
   
        Mailer.sendMessageWithAttachedFile( to,  from, null, subject, msgText, fl);
    }
}
