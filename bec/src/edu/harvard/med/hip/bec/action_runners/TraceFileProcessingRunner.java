/*
 * TraceFileProcessingRunner.java
 *
 * Created on February 24, 2004, 4:27 PM
 */

package edu.harvard.med.hip.bec.action_runners;
import java.sql.*;
import java.io.*;

import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.file.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.user.*;
import edu.harvard.med.hip.bec.Constants;
import edu.harvard.med.hip.bec.sampletracking.mapping.*;
import edu.harvard.med.hip.bec.sampletracking.objects.*;
import edu.harvard.med.hip.bec.util_objects.*;
import edu.harvard.med.hip.bec.ui_objects.*;
  import java.util.*;
  import edu.harvard.med.hip.utility.*;
/**
 *
 * @author  HTaycher
 */
public class TraceFileProcessingRunner extends ProcessRunner 
{
    
    private int                m_process_name = -1;
    private String             m_read_direction = null;
    private String             m_read_type = null;
    private String             m_file_extension = null;
    private String             m_inputdir = null;
    private String             m_outputdir = null;
    private boolean            m_isDelete = false;
    private InputStream         m_input = null;
    
    private String              OUTPUT_DIR = null;
    private String              INPUT_DIR = null;
    {
        if (ApplicationHostDeclaration.IS_BIGHEAD)
        {
             INPUT_DIR = "F:\\Sequences for BEC\\files_to_transfer";
             OUTPUT_DIR = "d:\\trace_files_dump";
        }
    }
    
    public void setProcessType(int process_name){m_process_name=process_name;}
    public void setReadDirection(String read_direction)    { m_read_direction = read_direction;}
    public void setReadType(String read_type)    { m_read_type = read_type;    }//m_read_type= read_type;}
    public void setFileExtension(String ext){ m_file_extension = ext;}
    public void setInputDirectory(String inputdir)
    {
        if (ApplicationHostDeclaration.IS_BIGHEAD) m_inputdir = INPUT_DIR;
        else         m_inputdir = inputdir;
    }
    public void setOutputDirectory(String outputdir)
    { 
        if (ApplicationHostDeclaration.IS_BIGHEAD) m_inputdir = OUTPUT_DIR;
        else        m_outputdir = outputdir;
    }
    public void setDelete(String delete)
    {
        if (delete.equalsIgnoreCase("NO") ) m_isDelete = false;
        else if (delete.equalsIgnoreCase("YES") ) m_isDelete = true;
    }
      
    public void setRenamingFile(InputStream input){ m_input = input ;}
    
    
    
    /** Creates a new instance of TraceFileProcessingRunner */
     public void run()
    {
        System.out.println(m_process_name);
        switch (m_process_name)
        {
            case Constants.PROCESS_CREATE_FILE_FOR_TRACEFILES_TRANSFER:
            {
                runCreateRenamingFile();
            }
            case Constants.PROCESS_INITIATE_TRACEFILES_TRANSFER:
            {
                runFileRenaming();
            }
        }
        sendEMails();
    
     }
     
     
     //-------------------------------------------
     private void       runFileRenaming()
     {
        //only for windows
        File dest_file = null; File org_file = null;
        String filename_org = null;String filename_dest= null;
        ArrayList file_names = null;String line = null;
        BufferedReader fin=null;
       try
        {
            fin = new BufferedReader(new InputStreamReader(m_input));
         }
        catch(Exception e){m_error_messages.add("Cannot open renaming file: "+e.getMessage());}
        try
        {
            while ((line = fin.readLine()) != null)
           {
                file_names = Algorithms.splitString(line, "\t");
                filename_org = m_inputdir + File.separator + (String)file_names.get(0);
                filename_dest= m_outputdir + File.separator + (String)file_names.get(1);
               
                try
                {
                    dest_file = new File(filename_dest);
                    org_file= new File(filename_org);
               
                    if (org_file.exists() && !dest_file.exists())
                    {
                        if ( m_isDelete )//copy
                            FileOperations.moveFile(org_file, dest_file, true, true);
                       else
                           FileOperations.moveFile(org_file, dest_file, true, false);
                    }
                }
                catch(Exception e1)
                {
                    m_error_messages.add( "Cannot rename file "+filename_org+" into "+filename_dest  );
                }
            }
            fin.close();
        }catch(Exception e){m_error_messages.add( e.getMessage());}
        
   
     }
     private void       runCreateRenamingFile()
     {
         ArrayList file_list = new ArrayList();
        ArrayList container_labels = Algorithms.splitString(m_items);
                   
        ArrayList clone_descriptions  = null;
        Container container = null; String label = null;
        File order_file = null;
         for (int count = 0; count < container_labels.size(); count++)
         {
             try
             {
                label = (String)container_labels.get(count);
                label = label.toUpperCase();
                clone_descriptions = getCloneDescriptions(label);
                if ( clone_descriptions != null && clone_descriptions.size() > 0)
                {
                    order_file = createFile(clone_descriptions, label);
                    m_file_list_reports.add(order_file);
                }
             }
             catch(Exception e)
             {
               m_error_messages.add(e.getMessage());
             }
        }
     }
     private ArrayList  getCloneDescriptions(String label)throws Exception
     {
         Container container = null;
         ArrayList clone_descriptions = new ArrayList();
         String sql = null;
         Connection conn = null;
         if (m_read_type.equals( Constants.READ_TYPE_INTERNAL_STR ))
         {
              conn = DatabaseTransactionLocal.getInstance(DatabaseTransactionLocal.FLEX_url , DatabaseTransactionLocal.FLEX_username, DatabaseTransactionLocal.FLEX_password).requestConnection();
              sql="select sequenceid as flexsequenceid,cloneid as flexcloneid,containerposition as position, "
              +" sampletype, s.containerid as containerid from constructdesign d, sample s, containerheader c "
              +" where label='"+label+"' and c.containerid=s.containerid and s.constructid=d.constructid(+)";
         }
         else if (  m_read_type.equals( Constants.READ_TYPE_ENDREAD_STR))
         {
             conn = DatabaseTransaction.getInstance().requestConnection();
             sql="select flexsequenceid,flexcloneid,position, sampletype, s.containerid as containerid "
+" from flexinfo f, sample s, containerheader c,isolatetracking i where label='"+label
+"' and c.containerid=s.containerid and s.sampleid=i.sampleid and i.isolatetrackingid=f.isolatetrackingid";
         }
         clone_descriptions = getCloneDescriptions( sql, label,conn);
         return clone_descriptions;
         
     }
     
     private ArrayList getCloneDescriptions(String sql, String label,Connection conn) throws BecDatabaseException
     {
        ArrayList clone_descriptions = new ArrayList();
        CloneDescription clone = null;
        ResultSet rs = null;
        try
        {
           // DatabaseTransactionLocal t = DatabaseTransactionLocal.getInstance();
            rs = DatabaseTransactionLocal.executeQuery(sql,conn);
            
            while(rs.next())
            {
                clone = new CloneDescription();
                clone.setFlexSequenceId  ( rs.getInt("flexsequenceid") );
                if ( clone.getFlexSequenceId() == -1) clone.setFlexSequenceId(0);
                clone.setFlexCloneId ( rs.getInt("flexcloneid") );
                clone.setPosition(rs.getInt("position") );
                clone.setContainerId(rs.getInt("containerid"));
               // sample.setType ( rs.getString("sampletype") );
                clone_descriptions.add(clone);
            }
            return clone_descriptions;
                
        } catch (Exception sqlE)
        {
            throw new BecDatabaseException("Error occured while getting information for the plate: "+label+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransactionLocal.closeResultSet(rs);
        }
     }
     private File createFile(ArrayList clone_descriptions, String label)throws Exception
     {
         ArrayList file_entries = new ArrayList();
         TraceFilesOrderFile entry = null;
         CloneDescription clone = null;
         for (int count = 0; count < clone_descriptions.size(); count++)
         {
             clone = (CloneDescription) clone_descriptions.get(count);
             entry = new  TraceFilesOrderFile()    ;
             entry.setCloneId ( clone.getFlexCloneId()) ;
            entry.setFlexSequenceId (clone.getFlexSequenceId()) ;
            entry.setHipPlateLabel (label);
            entry.setHipPlateId ( clone.getContainerId());
            entry.setHipWellId (Algorithms.convertWellFromInttoA8_12(clone.getPosition())) ;
            entry.setHipFilename ( createHipFileName(clone) );
            entry.setReadDirection (m_read_direction) ;//forward /reverse
            entry.setReadType (m_read_type) ;//internal/end read
            file_entries.add(entry);

         }
         File order_file = TraceFilesOrderFile.createTraceFilesOrderFile(file_entries);
         return order_file;
     }
    
     
     private String createHipFileName(CloneDescription clone) 
     {
         String fn = "";
         if (  m_read_type.equals( Constants.READ_TYPE_ENDREAD_STR) )
         {
             NamingFileEntry entry =new  NamingFileEntry(clone.getFlexCloneId()
                        , m_read_direction,
                        clone.getContainerId(),
                        Algorithms.convertWellFromInttoA8_12(clone.getPosition()), 
                        clone.getFlexSequenceId(),
                        0);
             fn = entry.getNamingFileEntyInfo()+m_file_extension;
         }
         else if (  m_read_type.equals( Constants.READ_TYPE_INTERNAL_STR) )
         {
             NamingFileEntry entry =new  NamingFileEntry(clone.getFlexCloneId()
                        , m_read_direction,
                        clone.getContainerId(),
                        Algorithms.convertWellFromInttoA8_12(clone.getPosition()), 
                        clone.getFlexSequenceId(),
                        1);
             fn = entry.getNamingFileEntyInfo()+m_file_extension;
         }
         return fn;
     }
     
     
     
    public static void main(String args[]) 
     
    {   try
         {
          TraceFileProcessingRunner runner = new TraceFileProcessingRunner();
           
         //         runner.setReadDirection("F");
             //       runner.setReadType("I");
             //       String items="ysg000766";
              //       runner.setItems(items );
             //       runner.setItemsType(Constants.ITEM_TYPE_PLATE_LABELS);
          //  runner.setFileExtension(".ab1");
                
          
             runner.setInputDirectory("E:\\Sequences for BEC\\YEAST\\1b Internal Seq\\col16-30");
                    runner.setOutputDirectory("E:\\Sequences for BEC\\YEAST\\1b Internal Seq\\rename1");
                    runner.setDelete("NO");
                    FileInputStream input =new  FileInputStream("c:\\renametest.txt");
                   
                        runner.setRenamingFile(input);
                        
                        
                          runner.setProcessType(Constants.PROCESS_INITIATE_TRACEFILES_TRANSFER);
                 runner.setUser(  AccessManager.getInstance().getUser("htaycher345","htaycher"));
    runner.run();
         }catch(Exception e){}
         System.exit(0);
     }
}
