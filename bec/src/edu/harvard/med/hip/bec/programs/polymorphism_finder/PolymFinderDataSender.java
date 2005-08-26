/*
 * PolymFinderDataSender.java
 *
 * Created on July 7, 2005, 3:35 PM
 */

package edu.harvard.med.hip.bec.programs.polymorphism_finder;


import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.programs.*;
import edu.harvard.med.hip.bec.export.*;
import edu.harvard.med.hip.bec.file.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.*;

import java.io.*;
import java.util.*;
import java.sql.*;
/**
 *
 * @author  htaycher
 */
public class PolymFinderDataSender
{
      public static String       LINE_SEPARATOR = System.getProperty("line.separator") ;
 
    
      public static void generateSequencingDataFiles(String cygwin_pass )
      {
          File flc =  null;
          try
          {
           
             String file_dir  = BecProperties.getInstance().getProperty("POLYORPHISM_FINDER_DATA_DIRECTORY") ;
             String file_name = BecProperties.getInstance().getProperty("FILE_NAME_INPUT_DATA_FILE");

             //check if file exists
              flc = new File(file_dir + java.io.File.separator + file_name); 
              if (! flc.exists()) return;
              cleanUpPolymorphismFinderDiscrepancyFile( file_dir,  file_name, cygwin_pass);
               writeORFFile();
             buildIndex(
                    file_dir + java.io.File.separator + BecProperties.getInstance().getProperty("FILE_NAME_ORFSEQUENCES_DATA_FILE") ,
                    file_dir + java.io.File.separator + BecProperties.getInstance().getProperty("FILE_NAME_ORFSEQUENCES_INDEX_FILE")  );
            }
          catch(Exception e)
          {
    
             try{Mailer.sendMessage      (  "hip_informatics@hms.harvard.edu", "hip_informatics@hms.harvard.edu",  null, "Polymorphism Finder Cron job report", "Cannot create ORF files "+ e.getMessage());
              }catch(Exception e1){}
      
          }
      }
    /**
     * @param args the command line arguments
     */
     public static void cleanUpPolymorphismFinderDiscrepancyFile(String file_dir, 
                    String file_name, String cygwin_pass)throws Exception
    {
        String cmd =  null;
        boolean result = false;
        String os_name = System.getProperty("os.name").toLowerCase();
         String tmpfile_name=file_dir+ java.io.File.separator +"tmp"+file_name;
        file_name = file_dir+ java.io.File.separator + file_name;
     
       if (  os_name.indexOf("win")> -1) //win
       {
           if ( cygwin_pass != null)
           {
                cmd = " -c \"/user/bin/sort --unique <"+file_name+" >"+tmpfile_name;
                result = SystemProcessRunner.runOSCall(  cmd);
           }
           if ( ! result ) { cleanUpWin( tmpfile_name,  file_name); result = true;}
       }
       else //unix
       {
               cmd = "sort --unique <"+file_name+" >"+tmpfile_name;
               result= SystemProcessRunner.runOSCall( cmd);
       }
         // copy back
         if ( result )
         {
             //  cmd = "copy "+tmpfile_name +" "+ file_name+" /y";
            //    result= SystemProcessRunner.runOSCall( cmd);
           try
             {
               File f1 = new File(tmpfile_name);
               File f2 = new File(file_name);
               FileOperations.copyFile(f1, f2, true);
               f1.delete();
             }        catch(Exception e){ throw new Exception("Cannot rename discrepancy file");}
             
         }
    }
     
  
    
    private static void cleanUpWin(String tmpfile_name, String file_name)
                         throws Exception
    {
        BufferedReader input = null;
        BufferedWriter output = null;
        String line = null;
        ArrayList items = new ArrayList();
        try
        {
            input = new BufferedReader(new FileReader(file_name));
            output = new BufferedWriter(new FileWriter(tmpfile_name));

            while ((line = input.readLine()) != null)
            {
                if ( !items.contains(line) )
                {
                    items.add(line);
                    output.write(line + LINE_SEPARATOR);
                }
            }
            input.close();
            output.flush();
            output.close();
         }
        catch(Exception e)
        {
             throw new Exception("Cannot write discrepancy file without duplications.");
        }
        finally
        {
            if ( input != null){try{ input.close();}catch(Exception e1){}}
            if ( output != null){try{ output.close();}catch(Exception e1){}}
        }
      
    }
    
    private static void    writeORFFile()throws Exception
    {
        String file_dir  = BecProperties.getInstance().getProperty("POLYORPHISM_FINDER_DATA_DIRECTORY") ;
        String file_name = BecProperties.getInstance().getProperty("FILE_NAME_INPUT_DATA_FILE");
         ArrayList ids = getUniqueORF_ids( file_dir + File.separator+file_name);
         writeORFFile(ids);
    }
    
    private static void             writeORFFile(ArrayList ids)throws Exception
    {
        //run 100 at a time
        StringBuffer orf_ids = new StringBuffer();
        for (int count = 0; count < ids.size(); count++)
        {
            if ( count >0 && count % 100 ==100 || count == ids.size()-1)
            {
                writeORFFileFor100Sequences(orf_ids.toString()+(String) ids.get(count));
                orf_ids = new StringBuffer();
                continue;
            }
            orf_ids.append( (String) ids.get(count) +",");
        }
       
    }
    
    private static void              writeORFFileFor100Sequences(String seq_ids)throws Exception
    {
        String sql = " select sequenceid  , infotext  from  sequenceinfo "
        +" where  sequenceid in ("+seq_ids+") order by sequenceid, infoorder";
        DatabaseTransaction t = null;
        ResultSet rs = null;
        StringBuffer sequence_text = new StringBuffer();
        int prev_seqid = 0;
        try
        {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while(rs.next())
            {
                if ( prev_seqid != 0 && prev_seqid != rs.getInt("sequenceid") )
                {
                     writeORFSequence(prev_seqid,  sequence_text.toString());
                    sequence_text = new StringBuffer();
                }
                prev_seqid = rs.getInt("sequenceid");
                sequence_text.append( rs.getString("infotext"));
            }
            //write last one
             writeORFSequence( prev_seqid,  sequence_text.toString());
           
        }
        catch (Exception E)
        {
             throw new Exception("Error occured while trying to get descriptions for descrepancy. "+E+"\nSQL: "+sql);
        }
        finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
    private static ArrayList        getUniqueORF_ids(String discr_file_name)throws Exception
    {
        BufferedReader input = null;
        ArrayList orf_ids = new ArrayList();
        String line = null; int count = 0;
        String orf_id = null;
        
        try
        {
           
           input = new BufferedReader(new FileReader(discr_file_name));
           while ((line = input.readLine()) != null)
           {
                if (  line.indexOf(" ") != -1) 
                {
                    orf_id = line.substring(0, line.indexOf(" "));
                    if ( orf_id != null && orf_id.trim().length()>0 && !orf_ids.contains(orf_id )) orf_ids.add(orf_id);
                }
           }
          return orf_ids;
        }
        catch(Exception e)
        {
            throw new Exception( "Cannot read discrepancy file "+e.getMessage());
        }
      
   
    }
    private static void       writeORFSequence(int refsequenceid, String sequence_text)throws Exception
    {
         FileWriter fr = null;
         String file_name = BecProperties.getInstance().getProperty("POLYORPHISM_FINDER_DATA_DIRECTORY") + java.io.File.separator + BecProperties.getInstance().getProperty("FILE_NAME_ORFSEQUENCES_DATA_FILE");
       
         try
         {
            fr =  new FileWriter(file_name, true);
            fr.write(">"+refsequenceid +Constants.LINE_SEPARATOR + sequence_text + Constants.LINE_SEPARATOR);
            fr.flush();
            fr.close();
         }
         catch(Exception e )
         {
             throw new Exception("Can not write reference sequence\n"+ e.getMessage());
         }
    
     }
     public static void buildIndex(String data_file_name, String index_file_name)
    {
        BufferedReader  fin = null;
          DataOutputStream dout = null; 
        String line = null; int id = -1; 
        ArrayList data = null;
        long current_pos = 0;
        boolean new_seq = false; long start =- 1; long end =-1;
        try
        {
            dout = new DataOutputStream( new FileOutputStream(index_file_name));
          
            File dbfile = new File(data_file_name);
            int separator  = dbfile.separator.length();
           
            fin = new BufferedReader(new FileReader(dbfile));
            while ( (line = fin.readLine()) != null)
            {
                
                if (line.startsWith(">"))
                {
                    if (start != -1)//skip first entry
                    {
                        end = current_pos - separator;// -LINE_DELIMITER_LENGTH;
                        writeIndexData(id, start,end,dout);
                      }
                    
                    data = Algorithms.splitString(line,"|");
                    id = Integer.parseInt( line.substring(1));
                    start = current_pos + line.length() +separator;//LINE_DELIMITER_LENGTH;
                }             
                
                current_pos += line.length() + separator;//LINE_DELIMITER_LENGTH;
                
            }
            //last record
            end = current_pos -1;
            writeIndexData(id, start,end,dout);
            fin.close();dout.close();
        }
        catch (Exception e)
        {
            System.out.println("Cannot build index file");
            try
            {fin.close();dout.close();} catch(Exception e1)
            {}
        }
    }
    
   private static void   writeIndexData(int id, long start,long end,DataOutputStream dout) throws Exception
   {
        dout.writeInt(id);
        dout.writeLong(start);
        dout.writeLong(end);
    //    dout.writeChar('\n');
}
    public static void main(String[] args)
    {
          BecProperties sysProps =  BecProperties.getInstance( BecProperties.PATH);
          sysProps.verifyApplicationSettings();
          File flc =  null;
          String cygwin_pass = null;   
           PolymFinderDataSender.generateSequencingDataFiles(null);
        /*  try
          {
              if ( args.length == 1)
            {
                flc = new File(args[0]); 
                if (flc.exists())cygwin_pass = args[0];
            }

             String file_dir  = BecProperties.getInstance().getProperty("POLYORPHISM_FINDER_DATA_DIRECTORY") ;
             String file_name = BecProperties.getInstance().getProperty("FILE_NAME_INPUT_DATA_FILE");

             //check if file exists
              flc = new File(file_dir + java.io.File.separator + file_name); 
              if (! flc.exists()) return;
              PolymFinderDataSender.cleanUpPolymorphismFinderDiscrepancyFile( file_dir,  file_name, cygwin_pass);
              PolymFinderDataSender.writeORFFile();
              PolymFinderDataSender.buildIndex(
                    file_dir + java.io.File.separator + BecProperties.getInstance().getProperty("FILE_NAME_ORFSEQUENCES_DATA_FILE") ,
                    file_dir + java.io.File.separator + BecProperties.getInstance().getProperty("FILE_NAME_ORFSEQUENCES_INDEX_FILE")  );
            }
          catch(Exception e)
          {
    
             try{Mailer.sendMessage      (  "hip_informatics@hms.harvard.edu", "hip_informatics@hms.harvard.edu",  null, "Polymorphism Finder Cron job report", "Cannot create ORF files "+ e.getMessage());
              }catch(Exception e1){}
      
          }
         **/
          System.exit(0);  
    }
    
}
