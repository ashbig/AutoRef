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
     
    
    private String              m_renaming_file_name = "rename";
    private int                m_process_name = -1;
    private String             m_read_direction = null;
    private String             m_read_type = null;
    private String             m_file_extension = null;
    private String             m_inputdir = null;
    private String             m_outputdir = null;
    private boolean            m_isDelete = false;
    private InputStream         m_input = null;
    private int                 m_sequencing_facility = -1;
    
    
    private String              OUTPUT_DIR = null;
    private String              INPUT_DIR = null;
    {
        if (ApplicationHostDeclaration.IS_BIGHEAD)
        {
             INPUT_DIR = "F:\\Sequences for BEC\\files_to_transfer";
             OUTPUT_DIR = "d:\\trace_files_dump";
              if (ApplicationHostDeclaration.IS_BIGHEAD_FOR_EXPRESSION_EVALUATION) 
             {
                  OUTPUT_DIR = "d:\\eval_trace_files_dump";
             }
        }
        else
        {
            OUTPUT_DIR = "C:\\bio\\original_files";
        }
    }
    
    public void setProcessType(int process_name){m_process_name=process_name;}
    public void setReadDirection(String read_direction)    { m_read_direction = read_direction;}
    public void setReadType(String read_type)    { m_read_type = read_type;    }//m_read_type= read_type;}
    public void setFileExtension(String ext){ m_file_extension = ext;}
    public void     setSequencingFacility(int v){m_sequencing_facility = v;}
     public String getTitle()    { return "Request for trace files manipulation.";    }
    
     
    public void setInputDirectory(String inputdir)
    {
        if (ApplicationHostDeclaration.IS_BIGHEAD ) m_inputdir = INPUT_DIR;
        else         m_inputdir = inputdir;
    }
    public void setOutputDirectory(String outputdir)
    { 
        if (ApplicationHostDeclaration.IS_BIGHEAD) m_outputdir = OUTPUT_DIR;
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
         try
        {
            switch (m_process_name)
            {
                case Constants.PROCESS_CREATE_FILE_FOR_TRACEFILES_TRANSFER:
                {
                    runCreateRenamingFile(); break;
                }
                case Constants.PROCESS_INITIATE_TRACEFILES_TRANSFER:
                {
                    runFileRenaming();break;
                }
                case Constants.PROCESS_CREATE_RENAMING_FILE_FOR_TRACEFILES_TRANSFER:
                {
                    createRenamingFile();break;
                }
            }
        }
        catch(Exception e)
        {
            m_error_messages.add(e.getMessage());
        }
       finally
        {
            sendEMails( getTitle() );
       }
    
     }
     
     
     //-------------------------------------------
     private void       createRenamingFile()throws Exception
     {
          String renaming_file_name =   m_inputdir + File.separator +m_renaming_file_name + System.currentTimeMillis() + ".txt" ;
         
          //print input mail file
         // File mappint_file = new File(m_input);
     //    m_file_list_reports.add(mapping_file);
          //  if (fl.exists()) fl.delete();
         //read mapping file hash key - sequencing facility name, item - hip name
         Hashtable plate_map_names =  readPlateMapping();
         ////read directory && parse file names
         //returns  arrays  of file entries, sort by plate and well
        ArrayList sequencing_facility_file_items = parseTraceFileNames();
         //get plates that we will rename: one renaming file can include a 
         //lot of plate names that are not in the folder
         ArrayList plate_names_for_processing =  getPlateNames( sequencing_facility_file_items, plate_map_names);
   
         for (int plate_count = 0; plate_count < plate_names_for_processing.size(); plate_count++)
         {
            ArrayList hip_clone_items =  getCloneDescriptions((String)plate_names_for_processing.get(plate_count));
            ArrayList renaming_file_entries = getRenamingFileEnties(sequencing_facility_file_items, 
                                                                 hip_clone_items,
                                                                    plate_map_names);
            Algorithms.writeArrayIntoFile( renaming_file_entries, true, renaming_file_name);
           
         }
          File renaming_file =   new File( renaming_file_name );
          m_file_list_reports.add( renaming_file );
         
     }
     
     
     private ArrayList       getPlateNames(ArrayList sequencing_facility_file_items, Hashtable plate_map_names)
     {
         String plate_names = "";String plate_name = null;String plate_name_last = "";
         ArrayList plate_names_for_processing = new ArrayList();
         String current_plate = null;
         int number_of_plates = 1;
         for (int plate_count = 0; plate_count < sequencing_facility_file_items.size(); plate_count++)
         {
             plate_name = ((SequencingFacilityFileName)sequencing_facility_file_items.get(plate_count)).getPlateName();
             if ( !plate_name_last.equalsIgnoreCase(plate_name))
             {
                 
                 current_plate = (String)plate_map_names.get(plate_name) ;
                 if (current_plate != null) 
                 {
                     plate_names += "'"+(String)plate_map_names.get(plate_name) +"',";
                     number_of_plates++;
                 }
             }
             if ( !plate_names.equals("") && (number_of_plates % 5==0 || plate_count == sequencing_facility_file_items.size()-1))// by 5 plates 
             {
                  plate_names = plate_names.substring(0, plate_names.length() -1);
                  plate_names_for_processing.add(plate_names);
                  plate_names="";
                  number_of_plates = 1;
             }
             
             plate_name_last = plate_name ;
         }
         return plate_names_for_processing;
     }
     
     private Hashtable       readPlateMapping() throws Exception
     {
           //only for windows
        String sequencing_facility_plate = null;String hip_name = null;
        
        m_additional_info =  Constants.LINE_SEPARATOR + "Plate mapping information" + Constants.LINE_SEPARATOR;
        
        String line = null; ArrayList plate_names = null;
        Hashtable plate_map = new Hashtable();        BufferedReader fin=null;
        try
        {
            fin = new BufferedReader(new InputStreamReader(m_input));
            while ((line = fin.readLine()) != null)
           {
                m_additional_info += line + Constants.LINE_SEPARATOR;
                plate_names = Algorithms.splitString(line, "\t");
                sequencing_facility_plate =  (String)plate_names.get(0);
                hip_name =(String)plate_names.get(1);
               plate_map.put( sequencing_facility_plate, hip_name);
            }
            fin.close();
            return plate_map;
        }
        catch(Exception e){
            throw new BecDatabaseException("Cannot open or read mapping file: "+e.getMessage());
        }
      
     }
     
     private ArrayList parseTraceFileNames()throws Exception
     {
         String file_name = null;SequencingFacilityFileName br=null;
         ArrayList file_names = new ArrayList();
        try
        {
            File sourceDir = new File(m_inputdir); //trace file directory          
            File [] trace_files = sourceDir.listFiles();
            for (int i = 0 ; i< trace_files.length;i++)
            {
                file_name = trace_files[i].getName();
                br= new SequencingFacilityFileName(file_name, m_sequencing_facility);
                if ( br.isWriteFormat() ) 
                {
                    if ( m_read_type.equals( Constants.READ_TYPE_ENDREAD_STR)
                        || m_sequencing_facility == SequencingFacilityFileName.SEQUENCING_FACILITY_HTMBC
                          && (  br.getOrientation().equalsIgnoreCase("F") || br.getOrientation().equalsIgnoreCase("R")))
                    {
                        file_names.add(br);
                    }
                    else
                        file_names.add(br);
                }
                else
                    m_error_messages.add("File "+file_name+" has wrong format");
            }
             file_names = SequencingFacilityFileName.sortByPlateWell(file_names);
            return file_names;
        }
        catch(Exception e)
        {
            throw new BecDatabaseException ("Cannot process files in input directory "+file_name);
        }
         
        
     }
     
   
      private ArrayList  getRenamingFileEnties(ArrayList sequencing_facility_file_items, 
                                                ArrayList           hip_clone_items,
                                                Hashtable           plate_map_names)
      {
          ArrayList renaming_file_entries = new ArrayList();;
          Hashtable file_entries = new Hashtable();
          SequencingFacilityFileName original_file_info = null;
          NamingFileEntry hip_file_entry = null;
          String file_key = null;String file_name = null;
          //put one array in hash
           for (int count = 0 ; count < hip_clone_items.size(); count++)
          {
                hip_file_entry = (NamingFileEntry) hip_clone_items.get(count);
              file_key = hip_file_entry.getPlateLabel() + "_"+hip_file_entry.getWellNumber();
              file_entries.put(file_key, hip_file_entry );
          }

          for (int count = 0; count < sequencing_facility_file_items.size(); count++)
          {
              original_file_info = (SequencingFacilityFileName)sequencing_facility_file_items.get(count);
              file_key = ((String)plate_map_names.get(original_file_info.getPlateName())) + "_"+original_file_info.getWellNumber();
            
              hip_file_entry = (NamingFileEntry)file_entries.get(file_key);
              if (hip_file_entry == null) continue;
              hip_file_entry.setOrientation(original_file_info.getOrientation());
              file_name = createHipFileName( hip_file_entry);
              if ( original_file_info.getVersion() != null) file_name+="."+original_file_info.getVersion();
             file_name+="."+original_file_info.getExtension();
             renaming_file_entries.add(original_file_info.getFileName()+"\t"+file_name);
          }
         
          return renaming_file_entries;
      }
    
   /*  
     private void printRenamingFile(ArrayList renaming_file_entries)throws Exception
     {
         File fl = null;FileWriter fr = null;
         try
         {
            
            fr =  new FileWriter(m_inputdir + File.separator +m_renaming_file_name, true);
            for (int count = 0; count < renaming_file_entries.size(); count++)
            {
                    fr.write((String) renaming_file_entries.get(count)+"\n");
            }
            fr.flush();
            fr.close();
           
        }
         catch(Exception e )
         {
             throw new BecDatabaseException("Can not write renaming file");
         }
     }
    **/
     
     //********************************************
     private void       runFileRenaming() throws Exception
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
        catch(Exception e)
        { 
            throw new BecDatabaseException ("Cannot open renaming file: "+e.getMessage());
        }
        try
        {
            while ((line = fin.readLine()) != null)
           {
                file_names = Algorithms.splitString(line, "\t");
                filename_org = m_inputdir + File.separator + (String)file_names.get(0);
                filename_dest= m_outputdir + File.separator + (String)file_names.get(1);
             //  System.out.println(filename_org+" "+filename_dest);
                try
                {
                    dest_file = new File(filename_dest);
                    org_file= new File(filename_org);
               
                    if (org_file.exists() && !dest_file.exists())
                    {
                       FileOperations.moveFile(org_file, dest_file, true, false);
                        if ( m_isDelete && org_file.exists())//copy
                        {
                            org_file.delete();
                        }
                    }
                }
                catch(Exception e1)
                {
             
                    m_error_messages.add( "Cannot rename file "+filename_org+" into "+filename_dest  );
                }
            }
            fin.close();
        }
        catch(Exception e)
        {
            throw new BecDatabaseException("Cannot read renaming file:" + e.getMessage());}
        
   
     }
     
  
     private void       runCreateRenamingFile()
     {
         ArrayList file_list = new ArrayList();
         /*
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
                //get clone descriptions for plates
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
          **/
         
     }
     private ArrayList  getCloneDescriptions(String label)throws Exception
     {
         Container container = null;
         ArrayList clone_descriptions = new ArrayList();
         String sql = null;
         Connection conn = null;
         try
         {
             if (m_read_type.equals( Constants.READ_TYPE_INTERNAL_STR ))
             {
                  conn = DatabaseTransactionLocal.getInstance(DatabaseTransactionLocal.FLEX_url , DatabaseTransactionLocal.FLEX_username, DatabaseTransactionLocal.FLEX_password).requestConnection();
                  sql="select label, sequenceid as flexsequenceid, cloneid as flexcloneid,containerposition as position, "
                  +" sampletype, s.containerid as containerid from constructdesign d, sample s, containerheader c "
                  +" where label in ("+label+") and c.containerid=s.containerid and s.constructid=d.constructid(+)";
             }
             else if (  m_read_type.equals( Constants.READ_TYPE_ENDREAD_STR))
             {
                 conn = DatabaseTransaction.getInstance().requestConnection();
                     sql = "select position, flexcloneid , flexsequencingplateid as containerid, flexsequenceid , label "
             +" from flexinfo f, isolatetracking i, sample s , containerheader c where f.isolatetrackingid = i.isolatetrackingid "
             +" and i.sampleid = s.sampleid and  c.containerid = s.containerid   and label in ("+label+")";

             }
         }
         catch(Exception e){throw new BecDatabaseException ("Cannot establish connection to the database");}
         clone_descriptions = getCloneDescriptions( sql, label,conn);
         return clone_descriptions;
         
     }
     
     private ArrayList getCloneDescriptions(String sql, String label,Connection conn) throws BecDatabaseException
     {
        ArrayList clone_descriptions = new ArrayList();
        NamingFileEntry entry = null;
        ResultSet rs = null;
        try
        {
           // DatabaseTransactionLocal t = DatabaseTransactionLocal.getInstance();
            rs = DatabaseTransactionLocal.executeQuery(sql,conn);
            
            while(rs.next())
            {
              
                entry = new  NamingFileEntry()    ;
                entry.setPlateLabel(rs.getString("LABEL"));
                entry.setWellNumber(rs.getInt("position"));
                entry.setPlateId (rs.getInt("containerid") );
                entry.setWellId (  Algorithms.convertWellFromInttoA8_12( entry.getWellNumber()));
                entry.setCloneID ( rs.getInt("flexcloneid") );
                entry.setSequenceId ( rs.getInt("flexsequenceid") );
              //  createHipFileName( entry);
                clone_descriptions.add(entry);
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
     /*
     private File createFile(ArrayList clone_descriptions, String label)throws Exception
     {
         ArrayList file_entries = new ArrayList();
         TraceFilesOrderFile entry = null;
         CloneDescription clone = null;
         for (int count = 0; count < clone_descriptions.size(); count++)
         {
             clone = (CloneDescription) clone_descriptions.get(count);
             entry = new  TraceFilesOrderFile()    ;
             entry.setCloneId ( clone.getCloneId()) ;
            entry.setFlexSequenceId (clone.getFlexSequenceId()) ;
            entry.setHipPlateLabel (label);
            entry.setHipPlateId ( clone.getContainerId());
            entry.setHipWellId (Algorithms.convertWellFromInttoA8_12(clone.getPosition())) ;
            entry.setHipFilename ( createHipFileName(entry) );
            entry.setReadDirection (m_read_direction) ;//forward /reverse
            entry.setReadType (m_read_type) ;//internal/end read
            file_entries.add(entry);

         }
         File order_file = TraceFilesOrderFile.createTraceFilesOrderFile(file_entries);
         return order_file;
     }
    */
     
     private String createHipFileName(NamingFileEntry entry) 
     {
         String fn = "";
         if (  m_read_type.equals( Constants.READ_TYPE_ENDREAD_STR) )
         {
             entry.setReadNum(0);
           fn=entry.getNamingFileEntyInfo() ;
         }
         else if (  m_read_type.equals( Constants.READ_TYPE_INTERNAL_STR) )
         {
              entry.setReadNum(1);
              fn = entry.getNamingFileEntyInfo();
         }
         return fn;
     }
     
     
     
    public static void main(String args[]) 
     
    {   try
         {
TraceFileProcessingRunner runner = new TraceFileProcessingRunner();
runner.setProcessType(Constants.PROCESS_INITIATE_TRACEFILES_TRANSFER);
runner.setReadType(Constants.READ_TYPE_ENDREAD_STR);//m_read_type= read_type;}
runner.setSequencingFacility(SequencingFacilityFileName.SEQUENCING_FACILITY_HTMBC);
runner.setInputDirectory("E:\\Sequences for BEC\\files_to_transfer");
runner.setOutputDirectory( "C:\\bio\\original_files");
runner.setRenamingFile(new  FileInputStream("E:\\Sequences for BEC\\files_to_transfer\\rename1099341664500.txt"));
     runner.setUser( AccessManager.getInstance().getUser("htaycher123","htaycher"));
       runner.setDelete("YES");
           
         //         runner.setReadDirection("F");
             //       runner.setReadType("I");
             //       String items="ysg000766";
              //       runner.setItems(items );
             //       runner.setItemsType(Constants.ITEM_TYPE_PLATE_LABELS);
          //  runner.setFileExtension(".ab1");
                
          
            // runner.setInputDirectory("E:\\Sequences for BEC\\YEAST\\1b Internal Seq\\col16-30");
            //        runner.setOutputDirectory("E:\\Sequences for BEC\\YEAST\\1b Internal Seq\\rename1");
           //         runner.setDelete("NO");
           //         FileInputStream input =new  FileInputStream("c:\\renametest.txt");
                   
          //              runner.setRenamingFile(input);
                        
                        
          //                runner.setProcessType(Constants.PROCESS_INITIATE_TRACEFILES_TRANSFER);
         //        runner.setUser(  AccessManager.getInstance().getUser("htaycher345","htaycher"));
    runner.run();
         }catch(Exception e){}
         System.exit(0);
     }
    
   
}
