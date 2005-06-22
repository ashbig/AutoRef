/*
 * Utils.java
 *
 * Created on June 20, 2005, 1:37 PM
 */

package edu.harvard.med.hip.bec.programs.polymorphism_finder;

import java.util.*;
import java.io.*;
/**
 *
 * @author  htaycher
 */
public class Utils 
{
    
     private static Properties  m_properties = null;

    public static void         getSystemProperties(String properties_file_name) throws Exception
    {
         //try to get the properties file from the class path
        if ( m_properties == null)
        {
            InputStream iStream = null;
            Properties prop = null;
            try 
            {
                iStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(properties_file_name);
                if (m_properties == null) m_properties = new Properties();
                 m_properties.load(iStream);
                 m_properties.setProperty("LINE_SEPARATOR", System.getProperty("line.separator"));
            } 
            catch (Exception ioE) 
            {
             //   System.err.println("Unable to load properites file"+ ioE.getMessage());
                try {  iStream.close();   } catch(Throwable th){}
                throw new Exception ( "Unable to load property file "+properties_file_name );
            }
        }
        
    }
    
    public static String getSystemProperty(String key) 
    {
        String property = m_properties.getProperty(key);
        if ( property == null) return null;
        else         return property.trim();
    }
    public static void     setSystemProperty(String key, String value) 
    {
        if (key == null || key.length()==0 || value == null || value.length()==0)return;
        m_properties.setProperty(key, value);
        
    }
   
    
    
    ///------------------------------------------------------------------------------------
    //Print the sequence cds to a file in a fasta format.
    public static void makeQueryFileInFASTAFormat
                (String fileName,
                String text, 
                String id) throws Exception
    {
        try
        {
            PrintWriter pr = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
            pr.print( ">"+id);
            pr.println(edu.harvard.med.hip.bec.bioutil.SequenceManipulation.convertToFasta(text));
            pr.close();
        }catch (IOException e)
        {
            throw new Exception("Cannot make query file for "+fileName+"\n"+e.getMessage());
        }
    }
  
    
     public static String convertToFasta(String sequenceString)
    {
        
        StringBuffer seqBuff = new StringBuffer();
        
        for (int i=0; i < sequenceString.length(); i++)
        {
            if(i% 60 == 0)
            {
                seqBuff.append( Utils.getSystemProperty("LINE_SEPARATOR"));
            }
            
            seqBuff.append(sequenceString.charAt(i));
        }
        
        return seqBuff.toString();
    }
     
     
     //------------------------
     public static  ArrayList splitString(String value, String spliter)
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
     
     //Fields:
 // 0 - Query id     1 - Subject id   2 - % identity  3-alignment_length  4 - mismatches,
   //   5 - gap openings  6 - q. start 7 - q. end  8 - s. start   9 - s. end   10 - e-value,
   //   11 - bit score
//	gi|28363213|gb|CB241569.1|CB241569	99.21	253	0	1	1	251	1	253	6e-136	480.2

    public static ArrayList parseBlastTabularFormat(String foutput_name,
                        int requered_identity, 
                        int requered_aligment_length) throws Exception
    {
        String line = null;
        ArrayList hits = new ArrayList();
        ArrayList temp = null;
        BufferedReader  fin = null;
        try
        {
            fin = new BufferedReader(new FileReader(foutput_name));
            while ( (line = fin.readLine()) != null)
            {
                temp = splitString(line,"\t");
                if (temp == null || temp.size() < 9 || temp.size() > 10) // skip description for 9
                {
                    double hit_identity =  Double.parseDouble(  (String)temp.get(2) ) ;
                    int hit_stop = Integer.parseInt(  (String)temp.get(7));
                    int hit_start = Integer.parseInt(  (String)temp.get(6));
                    String hit_id = (String)temp.get(1);
                    if( hit_identity == 100.0 && (hit_stop - hit_start + 1)== requered_aligment_length)
                           hits.add(hit_id);
                }
            }
            
            fin.close();
            return hits;
        }
        catch(Exception e)
         {
             try{fin.close();}catch(Exception c){}
             throw new  Exception("Cannot parse blast output");
         }
    } 
     
    public static boolean runProgram(String cmd)
                    throws Exception
    {
        try
        {
            Runtime r = Runtime.getRuntime();
            r.traceMethodCalls(true);
            Process p = r.exec(cmd);
             BufferedInputStream berr = new BufferedInputStream(p.getErrorStream());
            BufferedInputStream binput = new BufferedInputStream(p.getInputStream());
            int x = 0;int y = 0;
            
            boolean    isFinished = false;
            boolean    isErrDone = false;
            boolean    isOutDone = false;
            byte[]      buff = new byte[255];
            
            while (!isFinished)
            {
                if (berr.available() == 0 && binput.available() == 0)
                {
                    try
                    {
                        p.exitValue();
                        isFinished = true;
                        break;
                    }
                    catch (IllegalThreadStateException e)
                    {
                        Thread.currentThread().sleep(100);
                    }
                    catch(Exception e)
                    {
                        throw new Exception("Cannot run program");
                    }
                }
                else
                {
                    berr.read(buff, 0, Math.min(255, berr.available()));
                    binput.read(buff, 0, Math.min(255, binput.available()));
                }
            }
           
            p.waitFor();
            if (p.exitValue() != 0)
            {
               // System.err.println("program call failed" + p.exitValue());
                return false;
            }
        }
        catch (Exception e)
        {
            throw new  Exception("Cannot run program");
        } 
        return true;
    }
}
