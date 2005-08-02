/*
 * Utils.java
 *
 * Created on June 20, 2005, 1:37 PM
 */

package src;

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
	
            pr.println(convertToFasta(text));
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
                seqBuff.append( "\n");//Utils.getSystemProperty("LINE_SEPARATOR"));
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
                        int requered_aligment_length
                        ) throws Exception
     {
        return parseBlastStandardFormat( foutput_name,
                         requered_identity,requered_aligment_length,false);
     }

      public static ArrayList parseBlastStandardFormat(String foutput_name,
                        int requered_identity,
                        int requered_aligment_length,
                        boolean mode_exit_after_first_identity_reading) throws Exception
    {
        String line = null;
        ArrayList hits = new ArrayList();
        ArrayList temp = null;
        BufferedReader  fin = null;
        String hit_id = null;
        int number_of_hits_per_id_to_checked = 0;
        boolean isFistHitForId = false;
        try
        {
 System.out.println("parsing blast output " + foutput_name);
            fin = new BufferedReader(new FileReader(foutput_name));
            while ( (line = fin.readLine()) != null)
            {
				
                if (line.indexOf(">gi") != -1)
                {//extract gi
                    hit_id = null;
                    temp = Utils.splitString(line, "|");
                    if ( temp == null || temp.size() < 2)throw new Exception("Cannot parse blast output");
                    if ( ((String)temp.get(0)).indexOf("gi") != -1)
                    {
                        hit_id = (String)temp.get(1);
                        isFistHitForId = true;
                    }
                }
                //take the best hit fo rthe id
                if ( line.indexOf("Identities") != -1 && hit_id != null )
                {
System.out.println(hit_id);
                    if ( hit_id == null && !isFistHitForId)
                        continue;

                     temp = Utils.splitString(line, " ");
                     if ( temp == null || temp .size() < 3) continue;
                  
                     temp = Utils.splitString( (String)temp.get(2), "/");
                    int hit_bases = Integer.parseInt(  (String)temp.get(0));
                    int subject_bases = Integer.parseInt(  (String)temp.get(1));
                    number_of_hits_per_id_to_checked++;
                    if( hit_id != null && hit_bases >=  requered_aligment_length  && ( hit_bases * 100 / subject_bases)  >= requered_identity)
                           hits.add(hit_id);
                    if ( mode_exit_after_first_identity_reading ){ fin.close(); return hits;}
                    isFistHitForId = false;
                }
            }

            fin.close();
System.out.println("result "+hits.size());
            return hits;
        }
        catch(Exception e)
         {
             try{fin.close();}catch(Exception c){}
             throw new  Exception("Cannot parse blast output");
         }
    }

    public static boolean runProgram(String cmd)
    {
        return runProgram( cmd, 1);
    }


     public static boolean runProgram(String cmd, int mode)
    {
        try
        {
            Process p = null;
System.out.println("start process " + cmd);

            Runtime r = Runtime.getRuntime();
          //  r.traceMethodCalls(true);
            if ( mode == 0 )
            {
                String[] command = { "/bin/csh", "-c", cmd };
                p = r.exec(command );
            }
            else if (mode == 1)
            {
                    p = r.exec(cmd);
            }
            
            BufferedInputStream bin = new BufferedInputStream(p.getErrorStream());
            int x;
            while ((x = bin.read()) != -1)   {  ;   }
            p.waitFor();
            if (p.exitValue() != 0)
            {
                System.err.println(" call failed");
                return false;
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            return false;
          //  throw new  Exception("Cannot run program");
        }       
        return true;
    }


    /*public static String convertWindowsFileNameIntoUnix(String filename)
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
*/

    public static void main(String[] args)
	    {
     System.out.println("AAA");System.exit(0);
 }
}
