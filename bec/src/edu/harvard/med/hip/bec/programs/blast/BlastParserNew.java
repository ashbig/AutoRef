/*
 * BlastParserNew.java
 *
 * Created on March 14, 2003, 12:21 PM
 */

package edu.harvard.med.hip.bec.programs.blast;

import java.util.*;
import java.io.*;
import org.apache.regexp.*;

import edu.harvard.med.hip.bec.util.*;
/**
 *
 * @author  htaycher
 */
public class BlastParserNew
{
    private int m_blast_format = 0;
    private static BlastParserNew m_instance = null;
    
   
    /** Creates a new instance of BlastParserNew */
    private BlastParserNew(int format)
    {
        m_blast_format = format;
        try
        {
            // # Identity:    1120/1147 (97.6%)
            switch (m_blast_format)
            {
                case 8: case  9:
                {
                    //1	gi|28363213|gb|CB241569.1|CB241569	99.21	253	0	1	1	251	1	253	6e-136	480.2
                    //1	BX119401	100.00	14	0	0	3	16	1	14	  6.8	28.25
                    return;
                }
            }
            
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
    
     public static void parse(String foutput_name, BlastResult res, int format)
    {
        if (m_instance == null)  m_instance = new BlastParserNew(format);
        
        boolean isQuerySeq = true;
        boolean isRefSeq = false;
        
        Hashtable matches = new Hashtable(); 
        BlastResult result = new BlastResult();
        String line = null;
     
        ArrayList temp = null;
        BufferedReader  fin = null;
        try 
        {
            fin = new BufferedReader(new FileReader(foutput_name));
            while ((line = fin.readLine()) != null)
            {
               switch (format)
               {
                   case 8: case 9:
                   {
                        temp = Algorithms.splitString(line,"\t");
                        if (temp.size() > 1) // skip description for 9
                        {
                            result = new BlastResult();
                            
                            temp = Algorithms.splitString((String) temp.get(1),"|");
                        }
                   }
               }
              
             }
         }
         catch(Exception e)
         {
         }
    }
    
    
      //******************************************
    public static void main(String args[])
    {
        /*
        try{
            RE letters = new RE("Identities\\s*=\\s*(\\d+)\\/(\\d+)\\s*\\((\\d+) %\\)");
           
        }catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
     */
        
        String queryFile = "e:\\htaycher\\sequencing\\needle\\ex1.txt";
        BlastResult res = null;
        try
        {
             res = new BlastResult();
            
            BlastParserNew.parse(queryFile,res, 8);
        }catch(Exception e){}
        System.exit(0);
    }
}
      