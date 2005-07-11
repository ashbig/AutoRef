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
            Properties prop = null;FileInputStream pin = null;
            try 
            {
                 pin = new java.io.FileInputStream(properties_file_name);
                if (m_properties == null) m_properties = new Properties();
                m_properties.load(pin);
                 m_properties.setProperty("LINE_SEPARATOR", System.getProperty("line.separator"));
            } 
            catch (Exception ioE) 
            {
             //   System.err.println("Unable to load properites file"+ ioE.getMessage());
                try {  pin.close();   } catch(Throwable th){}
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
     
    /*
     *>gi|68293809|emb|CR999924.1| CR999924 RZPD no.9016 Homo sapiens cDNA clone RZPDp9016H213 5',
           mRNA sequence
          Length = 662

 Score = 60.0 bits (30), Expect = 4e-010
 Identities = 33/34 (97%)
 Strand = Plus / Plus

                              
     **/
    
      public static ArrayList parseBlastStandardFormat(String foutput_name,
                        int requered_identity, 
                        int requered_aligment_length) throws Exception
    {
        return   parseBlastStandardFormat( foutput_name, requered_identity, 
                         requered_aligment_length,      1) ;
    
      }
    
      public static ArrayList parseBlastStandardFormat(String foutput_name,
                        int requered_identity, 
                        int requered_aligment_length,
                        int number_of_hits_per_id_to_check) throws Exception
    {
        String line = null;
        ArrayList hits = new ArrayList();
        ArrayList temp = null;
        BufferedReader  fin = null;
        String hit_id = null;
        int number_of_hits_per_id_to_checked = 0;
        try
        {
            fin = new BufferedReader(new FileReader(foutput_name));
            while ( (line = fin.readLine()) != null)
            {
                if (line.indexOf(">gi") != -1)
                {//extract gi
                    hit_id = null;
                    temp = Utils.splitString(line, "|");
                    if ( temp == null || temp.size() < 2)throw new Exception("Cannot parse blast output");
                    if ( ((String)temp.get(0)).indexOf("gi") != -1)
                        hit_id = (String)temp.get(1);
                }
                if ( line.indexOf("Identities") != -1 && hit_id != null )
                {
                    if ( hit_id == null || (number_of_hits_per_id_to_checked >= number_of_hits_per_id_to_check && 
                         hit_id.equalsIgnoreCase( (String) hits.get(hits.size() - 1))))
                        continue;
                     temp = Utils.splitString(line, " ");
                     temp = Utils.splitString( (String)temp.get(2), "/");
                    int hit_bases = Integer.parseInt(  (String)temp.get(0));
                    int subject_bases = Integer.parseInt(  (String)temp.get(1));
                    number_of_hits_per_id_to_checked++;
                    if( hit_id != null && hit_bases >=  requered_aligment_length  && ( hit_bases * 100 / subject_bases)  >= requered_identity)
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
                 return false;
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            throw new  Exception("Cannot run program");
        } 
        return true;
    }
    
    
     public static boolean runProgram(String cmd, String file_name)
                    throws Exception
    {
        try
        {
            Runtime r = Runtime.getRuntime();
            r.traceMethodCalls(true);
             Process p = r.exec(cmd);
             BufferedInputStream berr = new BufferedInputStream(p.getErrorStream());
            BufferedInputStream binput = new BufferedInputStream(p.getInputStream());
            FileOutputStream bo = new FileOutputStream(file_name);
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
                    bo.write(buff);bo.flush();
                }
            }
           bo.close();
            p.waitFor();
            if (p.exitValue() != 0)
            {
                   return false;
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            throw new  Exception("Cannot run program");
        } 
        return true;
    }

     
    public static String convertWindowsFileNameIntoUnix(String filename)
    {
        String res = null;
        char[] arr = filename.toCharArray();
        StringBuffer converted_file_name = new StringBuffer();
        for (int count = 0; count < arr.length; count++)
        {
            if ( count == 0 && arr[count] != '/')converted_file_name.append('/');
            if ( arr[count] == ':') continue;
            if ( arr[count] == File.separatorChar ) { converted_file_name.append('/'); continue;}
            converted_file_name.append(arr[count]);
        }
           return converted_file_name.toString();
    }
}
