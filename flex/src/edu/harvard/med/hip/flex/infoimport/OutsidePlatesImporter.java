/*
 * 
 *http://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=nucleotide&id=5&retmode=xml

 gives back image id
http://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=nucleotide&term=4791512&retmode=xml
http://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=nucleotide&term=4791512&field=image&retmode=xml

gives back: 8  8  0   124383319  123997820  123997818  123983117  23270926  8666547  13980312  28930092  
http://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=CoreNucleotide&term=4791512[image]&retmode=html
where 

 OutsidePlatesImporter.java
 *
 * Created on May 31, 2007, 1:53 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */


package edu.harvard.med.hip.flex.infoimport;

import java.io.*;
import java.util.*;
import java.sql.*;
import java.util.*;
import sun.jdbc.rowset.*;

import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.util.*;
//import edu.harvard.med.hip.flex.util.objectcopy.*;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.workflow.*;
import edu.harvard.med.hip.flex.infoimport.file_mapping.*;
import edu.harvard.med.hip.flex.infoimport.coreobjectsforimport.*;
import edu.harvard.med.hip.flex.infoimport.bioinfo.*;

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import org.apache.xerces.parsers.SAXParser;

/**
 *
 * @author htaycher
 *
 * goal: submit plates from files
 * data inserted into:
 *  -   containerheader (flag additional info - 1)
 *   -   containerheader_name
 *  -   containercell
 *  -   sample (clone id - null, flag additional info - 1)
 *  -   sample name
 *  -   constructdesign
 *  -   flexsequence 
 *  -   name
 *  -   queue
 *
 * data pre- submition
 *   -   project
 *   -   workflow
 * 
 */
public class OutsidePlatesImporter extends ImportRunner
{
    
    public static final int   SUBMISSION_TYPE_ONE_FILE = 1;
    public static final int   SUBMISSION_TYPE_PSI = 2;
    public static final int   SUBMISSION_TYPE_REFSEQUENCE_LOCATION_FILES = 3;
    public static final int   SUBMISSION_TYPE_MGC = 4;
    public static final int   SUBMISSION_TYPE_NOTE_KNOWN = 5;
    
    private     int     m_project_id = -1;
    private     int     m_workflow_id = -1;
    private     int     m_protocol_id = -1;
    private     boolean m_isOutOnFirstError = false;
   private int             m_number_of_samples_per_container = 96;
  
private boolean         m_is_check_flex_sequences_against_FLEX_database = false;
private   String        m_mgc_file_name = null;   
private   String        m_plate_type = null;
private   int           m_plate_location = Location.CODE_FREEZER;
private boolean         m_is_fillin_clones_records = false;
private boolean         m_is_define_construct_type_by_nuclsequence = false;
private boolean         m_is_put_on_queue = false;
private boolean         m_is_get_flexsequence_from_ncbi = false;
private boolean         m_is_flexsequence_gi = false;
private boolean         m_is_insert_control_negative_for_empty_well = true;
private int             m_submission_type = SUBMISSION_TYPE_ONE_FILE;

private String          m_sample_biotype = "LI";// from processprotocol
    
private     HashMap          i_containers = null;
private     HashMap          i_flex_sequences = null;
private     HashMap          i_authors = null;
private     HashMap         i_publications= null;

public void              setPlatesLocation(int v){ m_plate_location = v;} 
public  void             setProjectId(int v)   {     m_project_id = v;}
public    void          setWorkFlowId(int v ){    m_workflow_id = v;}
public void             setProtocolId(int v){ m_protocol_id= v;}
public void             setSubmissionType(int v){ m_submission_type = v;}


public void            isCheckInFLEXDatabase(boolean v){ m_is_check_flex_sequences_against_FLEX_database = v;}
public void            setNumberOfWellsInContainer(int v){m_number_of_samples_per_container = v;}
public void              setMGCFileName(String v){ m_mgc_file_name = v;}
public void              setPlateType(String v){ m_plate_type = v;}
public void              isFillInClonesTables(boolean v){ m_is_fillin_clones_records= v;}
public void              isDefineContructTypeByNSequence(boolean v){ m_is_define_construct_type_by_nuclsequence = v;}
public void                 isPutOnQueue(boolean v){ m_is_put_on_queue = v;}   
public void              setSampleBioType(String v){ m_sample_biotype = v;}   
public void              isGetFLEXSequenceFromNCBI(boolean v){ m_is_get_flexsequence_from_ncbi= v;}
public void              isFLEXSequenceIDGI(boolean v) { m_is_flexsequence_gi = v;}   
public void             isInsertControlNegativeForEmptyWell(boolean v){ m_is_insert_control_negative_for_empty_well= v;}


public String getTitle() {        return "Upload of information for third-party containers into FLEX.";    }
    
    public void run_process() 
    {
        Connection  conn = null;
        FileStructure[]             file_structures = null;
        try
        {
            //load names
               ConstantsImport.fillInNames();
      
            // read in data mapping schema
            file_structures = readDataMappingSchema();
  // array list of containers read from 1 file         
           readDataFromFiles(file_structures );
            
            if ( m_is_insert_control_negative_for_empty_well)
                         fellInEmptyWells();
          //  ImportContainer.printContainers( i_containers,i_flex_sequences);
            synchronized (this)
            {
                // make sure that no container will be submitted for the second time: check by user 
                // container_id , mgc project is different from others  
                checkContainerLabelsForDublicates();
                if (i_containers == null || i_containers.size() == 0 || i_flex_sequences==null
                        || i_flex_sequences.size() == 0)
                    return;

                fillFlexSequenceData();
                conn = DatabaseTransaction.getInstance().requestConnection();   
                boolean isVerifed = verifyObjectsMapToNamingTables(conn );
                if ( ! isVerifed )
                {
                   throw new Exception();
                }
                insertObjects(conn);
            }
            // prepare final messages
            ImportContainer container = null;
            Iterator iter = i_containers.values().iterator();
            while(iter.hasNext())
            {
                container = (ImportContainer) iter.next();
                m_process_messages.add("Container "+PublicInfoItem.getPublicInfoByName(FileStructureColumn.PROPERTY_NAME_USER_ID, container.getPublicInfo()).getValue()+ " have been uploaded in FLEX. FLEX container label "+ container.getLabel());
            }
             //conn.rollback();
            DatabaseTransaction.commit(conn);
        }
        catch(Exception e)
        {
            DatabaseTransaction.rollback(conn);
             System.out.println(e.getMessage());
            m_error_messages.add("Can not upload new plates from files.\n"+e.getMessage());
        }
        finally
         {
            if ( conn != null) DatabaseTransaction.closeConnection(conn);
            sendEmails("New plates upload into FLEX","New plates upload into FLEX");
         }
    
    }
    
   
      //---------------------------------------------------------------------------------//
    //                          Private Methods
    //---------------------------------------------------------------------------------//
  private void fellInEmptyWells() throws Exception
  {
      ImportSample sample = null; ImportContainer cur_container  =null;
      int prev_sample_position = 0; 
       if (i_containers == null || i_containers.size() == 0)                return;
      Iterator  iter = i_containers.keySet().iterator (  ) ; 
        
         while ( iter.hasNext (  )  ) 
         { 
             String key = (String)iter.next();
             int mis_samples = 0;
             cur_container =  (ImportContainer)i_containers.get(key);
             prev_sample_position = 0; 
    
             cur_container.sortSamplesByPosition();
             ArrayList control_samples = new ArrayList(); 
             
             for ( int sample_count = 0; sample_count < cur_container.getSamples().size(); sample_count++)
             {
                  sample = (ImportSample)cur_container.getSamples().get(sample_count);
                //fill in samples                   
                  if ( prev_sample_position < sample.getPosition()-1  )
                  {
                      for ( mis_samples = prev_sample_position +1 ; mis_samples <  sample.getPosition() ; mis_samples++)
                      {
                          control_samples.add(new ImportSample(mis_samples, Sample.CONTROL_NEGATIVE));
                      }
                 }
                 prev_sample_position=sample.getPosition();
                  
             }
             for (int count = 0; count < control_samples.size(); count++)
             {
                 cur_container.addSample( (ImportSample) control_samples.get(count));
             }
        }
  } 
  
  
  
    private void checkContainerLabelsForDublicates() throws Exception
    {
        if (i_containers == null || i_containers.size() == 0)                return;
        Iterator iter = i_containers.keySet().iterator (  ) ; 
        StringBuffer labels = new StringBuffer();
        while ( iter.hasNext (  )  ) 
        { 
             labels.append( "'"+(String)iter.next() +"',");
        }
         String labels_ids = labels.toString();
        labels_ids = labels_ids.substring(0, labels_ids.length()-1);
         String sql = null ;         ResultSet rs = null; 
         if (m_submission_type == this.SUBMISSION_TYPE_MGC)
         {
             sql = "select oricontainer as label from mgccontainer where oricontainer in (" +
            labels_ids +")";
         }
         else
         {
             sql = "select namevalue as label from containerheader_name where nametype = 'USER_ID' and namevalue in ("+
            labels_ids +")";
         }
         try 
            {
                DatabaseTransaction t = DatabaseTransaction.getInstance();
                rs = t.executeQuery(sql);
                while(rs.next()) 
                {
                    i_containers.remove( rs.getString("label") );
                    m_error_messages.add("The container " + rs.getString("label")+ " will not be submitted: container with this name already has been submitted." );
               
                }
              
            }
            catch(Exception ee)
            {
                throw new Exception (ee.getMessage());
            }
         
    }
    private void    readDataFromFiles( FileStructure[]  file_structures) throws Exception
    {
         if ( m_submission_type == this.SUBMISSION_TYPE_PSI)
         {
             readDataForPSI(file_structures); 
         }
         else if ( m_submission_type == this.SUBMISSION_TYPE_REFSEQUENCE_LOCATION_FILES)
         {
             readDataFromTwoFilesJoinedByRefSequenceID(file_structures);
         }
        //    readDataFromFilesMultipalFileSubmission 
         else if ( m_submission_type == this.SUBMISSION_TYPE_ONE_FILE ||
                 m_submission_type == this.SUBMISSION_TYPE_MGC)
             readDataFromFilesOneFileSubmission(file_structures);
          
    }
    
    
        // the order of files is VERY IMPORTANT!!!!    
    private void    readDataForPSI( FileStructure[]  file_structures) throws Exception
    {
         DataFileReader freader = new DataFileReader();
         freader.setNumberOfWellsInContainer(m_number_of_samples_per_container);
         freader.isCreateCloneObjectPerSample(m_is_fillin_clones_records);
       // read in containers, samples, create clone persample
         freader.readFileIntoSetOfObjects( (InputStream)m_file_input_data.get(FileStructure.STR_FILE_TYPE_PLATE_MAPPING), true,
                FileStructure.FILE_TYPE_PLATE_MAPPING, 
               true, true,file_structures[ FileStructure.FILE_TYPE_PLATE_MAPPING]);//, null) ;
           i_containers = freader.getContainers();
         
         // read in target sequences
          freader.readFileIntoSetOfObjects( (InputStream)m_file_input_data.get(FileStructure.STR_FILE_TYPE_SEQUENCE_INFO), true,
                FileStructure.FILE_TYPE_SEQUENCE_INFO, 
               true, true,file_structures[ FileStructure.FILE_TYPE_SEQUENCE_INFO]);//, null) ;
          
           i_flex_sequences = freader.getFlexSequences();
           freader.emptySampleHashMap();
           
           freader.readFileIntoSetOfObjects( (InputStream)m_file_input_data.get(FileStructure.STR_FILE_TYPE_GENE_INFO), true,
                FileStructure.FILE_TYPE_GENE_INFO, 
               true, true,file_structures[ FileStructure.FILE_TYPE_GENE_INFO]);//, null) ;
            if ( file_structures[ FileStructure.FILE_TYPE_AUTHOR_INFO] != null &&
                   file_structures[ FileStructure.FILE_TYPE_AUTHOR_CONNECTION] != null)
            {
               freader.readFileIntoSetOfObjects( (InputStream)m_file_input_data.get(FileStructure.STR_FILE_TYPE_AUTHOR_INFO), true,
                    FileStructure.FILE_TYPE_AUTHOR_INFO, 
                    true, true,file_structures[ FileStructure.FILE_TYPE_AUTHOR_INFO]);//, null) ;
               i_authors = freader.getAuthors();
               freader.resetAdditionalInfo();     
               freader.readFileIntoSetOfObjects( (InputStream)m_file_input_data.get(FileStructure.STR_FILE_TYPE_AUTHOR_CONNECTION), true,
                    FileStructure.FILE_TYPE_AUTHOR_CONNECTION, 
                true, true,file_structures[ FileStructure.FILE_TYPE_AUTHOR_CONNECTION]);//, null) ;
                assignAuthorInformation( freader);
            }
           if ( file_structures[ FileStructure.FILE_TYPE_PUBLICATION_INFO] != null &&
                   file_structures[ FileStructure.FILE_TYPE_PUBLICATION_CONNECTION] != null
                   && m_file_input_data.get(FileStructure.STR_FILE_TYPE_PUBLICATION_INFO) != null
                   && m_file_input_data.get(FileStructure.STR_FILE_TYPE_PUBLICATION_CONNECTION) != null)
            {
               FileStructure fst = file_structures[ FileStructure.FILE_TYPE_PUBLICATION_INFO];
               freader.readFileIntoSetOfObjects( (InputStream)m_file_input_data.get(FileStructure.STR_FILE_TYPE_PUBLICATION_INFO), true,
                    FileStructure.FILE_TYPE_PUBLICATION_INFO, 
                    true, true, fst        );//, null) ;
               i_publications = freader.getPublications();
               freader.resetAdditionalInfo(); 
               fst = file_structures[ FileStructure.FILE_TYPE_PUBLICATION_CONNECTION];
               freader.readFileIntoSetOfObjects( (InputStream)m_file_input_data.get(FileStructure.STR_FILE_TYPE_PUBLICATION_CONNECTION), true,
                    FileStructure.FILE_TYPE_PUBLICATION_CONNECTION, 
                true, true,fst);//, null) ;
                assignPublicationInformation( freader);
            }
    }
    
     private void           assignPublicationInformation(DataFileReader freader)
             throws Exception
    {
        // read authors 
            HashMap publications = null; HashMap publications_connectors = null;
            DataConnectorObject primary_key_connector_object = null;
            DataConnectorObject f_key_connector_object = null;
            ArrayList f_keys = null; ArrayList publications_annotations_per_owner = null;
            ImportSample sample = null; ImportContainer cur_container  =null;
            ImportClone clone = null;
            
             publications= freader.getPublications();
             publications_connectors = freader.getAdditionalInfo();
               
             if ( publications == null && 
                     (publications_connectors!= null && publications_connectors.size() > 0))
                 throw new Exception("Please check publication files");
               // assign publications to objects owner can be container, sample, clone in the future
                if ( publications != null && publications_connectors != null  && publications_connectors.size() > 0)
                {
                    Iterator iter = publications_connectors.keySet().iterator();
                    while( iter.hasNext())
                    {
                        primary_key_connector_object = (DataConnectorObject)iter.next();
                        f_keys =(ArrayList) publications_connectors.get(primary_key_connector_object);
                        publications_annotations_per_owner = new ArrayList();
                        for (int count_key = 0; count_key < f_keys.size(); count_key++)
                        {
                            f_key_connector_object = (DataConnectorObject)f_keys.get(count_key);
                            ImportPublication publication = (ImportPublication) publications.get( f_key_connector_object.getId());
                            if (primary_key_connector_object.getType().intern() == FileStructureColumn.OBJECT_TYPE_SAMPLE)
                            {
                                sample = (ImportSample) freader.getSamples().get(primary_key_connector_object.getId());
                            }
                            else if (primary_key_connector_object.getType().intern() == FileStructureColumn.OBJECT_TYPE_CLONE)
                            {
                                clone = (ImportClone) freader.getClones().get(primary_key_connector_object.getId());
                                if (clone != null)clone.addPublication(publication);
                            }
                           }
                     }
                
            }
        //     System.out.println("a");
    }
    
    private void           assignAuthorInformation(DataFileReader freader)
    {
        // read authors 
            HashMap authors = null; HashMap author_connectors = null;
            DataConnectorObject primary_key_connector_object = null;
            DataConnectorObject f_key_connector_object = null;
            ArrayList f_keys = null; ArrayList author_annotations_per_owner = null;
            ImportSample sample = null; ImportContainer cur_container  =null;
            ImportClone clone = null;
            
             authors= freader.getAuthors();
             author_connectors = freader.getAdditionalInfo();
               
               // assign authors to objects owner can be container, sample, clone in the future
                if ( author_connectors != null  && author_connectors.size() > 0)
                {
                    Iterator iter = author_connectors.keySet().iterator();
                    while( iter.hasNext())
                    {
                        primary_key_connector_object = (DataConnectorObject)iter.next();
                        f_keys =(ArrayList) author_connectors.get(primary_key_connector_object);
                        author_annotations_per_owner = new ArrayList();
                        for (int count_key = 0; count_key < f_keys.size(); count_key++)
                        {
                            f_key_connector_object = (DataConnectorObject)f_keys.get(count_key);
                            ImportAuthor author = (ImportAuthor) authors.get( f_key_connector_object.getId());
                             if (primary_key_connector_object.getType().intern() == FileStructureColumn.OBJECT_TYPE_CONTAINER)
                            {
                                //container go by label
                                 cur_container = (ImportContainer) i_containers.get(primary_key_connector_object.getId());
                                 if ( cur_container != null)   cur_container.addAuthor( author );
                            }
                            else if (primary_key_connector_object.getType().intern() == FileStructureColumn.OBJECT_TYPE_SAMPLE)
                            {
                                sample = (ImportSample) freader.getSamples().get(primary_key_connector_object.getId());
                               // if (sample != null)sample.addAuthor(author);
                            }
                            else if (primary_key_connector_object.getType().intern() == FileStructureColumn.OBJECT_TYPE_CLONE)
                            {
                                clone = (ImportClone) freader.getClones().get(primary_key_connector_object.getId());
                                if (clone != null)clone.addAuthor(author);
                            }
                           }
                     }
                
            }
        //     System.out.println("a");
    }
    /*
    private HashMap    createSampleHashMap()
    {
        HashMap samples_hash= new HashMap();
        ImportSample sample = null; ImportContainer cur_container  =null;
        PublicInfoItem p_info = null;
            
        Iterator iter1 = i_containers.values().iterator();
        while(iter1.hasNext()){
             cur_container = (ImportContainer) iter1.next();
             for (int count_sample = 0; count_sample < cur_container.getSamples().size();count_sample++)
             {
                 sample = (ImportSample) cur_container.getSamples().get(count_sample);
                 p_info = PublicInfoItem.getPublicInfoByName( FileStructureColumn.PROPERTY_NAME_USER_ID, sample.getPublicInfo());
                 if ( p_info != null) 
                     samples_hash.put(p_info.getValue(), sample);
                  if ( p_info != null && p_info.getValue().equals(primary_key_connector_object.getId()))
                 {
                     sample.addPublicInfoItems(author_annotations_per_owner);
                 }
             }
        }
        return samples_hash;
    }
     **/
    /////////////////////////////////////////////
     private void    readDataFromFilesOneFileSubmission( FileStructure[]  file_structures) throws Exception
    {
         DataFileReader freader = new DataFileReader();
          
         freader.setNumberOfWellsInContainer(m_number_of_samples_per_container);
         freader.isCreateCloneObjectPerSample(m_is_fillin_clones_records);
         freader.readFileIntoSetOfObjects( 
                 (InputStream)m_file_input_data.get(FileStructure.STR_FILE_TYPE_ONE_FILE_SUBMISSION), true,
                FileStructure.FILE_TYPE_ONE_FILE_SUBMISSION, 
                true, true,file_structures[ FileStructure.FILE_TYPE_ONE_FILE_SUBMISSION]);//, null) ;
         i_containers = freader.getContainers();
         i_flex_sequences = freader.getFlexSequences();
         if ( freader.getErrorMesages().size() > 0 )
         {
             String error_messages = "";
             m_error_messages.addAll(freader.getErrorMesages());
          
             for (int count = 0; count < m_error_messages.size(); count++)
             {
                 error_messages +="\n" + (String) m_error_messages.get(count);
             }
             throw new Exception ("Cannot process files: "+error_messages);
         }
         
          
    }
    
    private void    readDataFromTwoFilesJoinedByRefSequenceID( FileStructure[]  file_structures) throws Exception
    {
         DataFileReader freader = new DataFileReader();
          
         freader.setNumberOfWellsInContainer(m_number_of_samples_per_container);
         freader.isCreateCloneObjectPerSample(m_is_fillin_clones_records);
         
           String fkey = FileStructure.STR_FILE_TYPE_REFERENCE_SEQUENCE_INFO;
          InputStream refseq_in_stream = (InputStream)m_file_input_data.get(fkey); 
          freader.readFileIntoSetOfObjects( 
                   refseq_in_stream, true,
                   FileStructure.FILE_TYPE_REFERENCE_SEQUENCE_INFO, 
                   true, true,
                   file_structures[ FileStructure.FILE_TYPE_REFERENCE_SEQUENCE_INFO]);//, null) ;
          
           i_flex_sequences = new HashMap(freader.getFlexSequences());
          
         
         
            freader.readFileIntoSetOfObjects( 
                       (InputStream)m_file_input_data.get(FileStructure.STR_FILE_TYPE_ONE_FILE_SUBMISSION), 
                        true,
                        FileStructure.FILE_TYPE_ONE_FILE_SUBMISSION, 
                        true, true,file_structures[ FileStructure.FILE_TYPE_ONE_FILE_SUBMISSION]);//, null) ;
            i_containers = freader.getContainers();
         
            if ( freader.getErrorMesages().size() > 0)
            {
                String error_messages = "";
                 m_error_messages.addAll(freader.getErrorMesages());

                 for (int count = 0; count < m_error_messages.size(); count++)
                 {
                     error_messages +="\n" + (String) m_error_messages.get(count);
                 }
                 throw new Exception ("Cannot process files: "+error_messages);
            }
    
    }
     // check by GI only    
      private void        checkFlexSequencesInFLEXDatabase() throws Exception
      {
          String sql = "select sequenceid, namevalue from name where nametype='GI' and namevalue in (";
          StringBuffer gis_for_sql = new StringBuffer();
          Hashtable gis = new Hashtable(100); // key - GI, value - originall key
          String gi = null;ImportFlexSequence sequence = null; String key = null;
          String send_sql = null;
          // get GI for extracted sequences
          Iterator iter = i_flex_sequences.keySet().iterator();
          int count = 0; 
          while(iter.hasNext())
          {
              key = (String)iter.next();
              if ( ! m_is_flexsequence_gi )
              {
                  sequence = (ImportFlexSequence) i_flex_sequences.get( key );
                  if ( sequence == null) continue;
                  gi = PublicInfoItem.getPublicInfoByName(FlexSequence.GI, sequence.getPublicInfo()).getValue();
              }
              else
              {
                  gi = key;
              }
              if (gi != null)
              {
                  gis_for_sql.append("'" + gi + "',");
                  gis.put(gi, key);
                  count++;
                  if ( count > 200)
                  {
                      send_sql =  gis_for_sql.toString();
                      send_sql =  send_sql.substring(0, send_sql.length() - 1);
                      send_sql = sql + send_sql +")";
                      // run check 
                      reassignFlexSequenceIds(gis, send_sql );
                      count = 0; gis_for_sql = new StringBuffer(); gis = new Hashtable();
                  }
              }
          }
         send_sql =  gis_for_sql.toString();
         send_sql =  send_sql.substring(0, send_sql.length() - 1);
         send_sql = sql + send_sql +")";
         // run check 
         reassignFlexSequenceIds(gis, send_sql );
         
      }
       
      private void              reassignFlexSequenceIds(Hashtable gis,
              String sql )throws Exception
      {
          ResultSet rs = null; 
          String sequence_id = null; String gi = null;
          String key = null;
           try 
            {
                DatabaseTransaction t = DatabaseTransaction.getInstance();
                rs = t.executeQuery(sql);
                while(rs.next()) 
                {
                    gi = rs.getString("namevalue");
                    sequence_id = rs.getString("sequenceid");
                    key = (String) gis.get(gi);
                    if (key != null)
                    {
                        i_flex_sequences.put(key, sequence_id);
                    }
                }
               
            }
            catch(Exception ee)
            {
                throw new Exception (ee.getMessage());
            }
      
      }
   private void             setFlexSequenceIdsForSamples() throws Exception
   {
        ImportContainer container =  null;
        String key = null;
        ImportSample  sample = null;
        PublicInfoItem p_info= null;
        String sequence_id = null;
         Iterator iter = null;
         boolean is_verified_seq_ids = true;
         Hashtable mgc_ids = null; String[] mgs_id_per_sequence = null;
         if ( m_submission_type == this.SUBMISSION_TYPE_MGC)
         {
             mgc_ids = getMGCSequenceIDs();
         }
         iter = i_containers.keySet().iterator (  ) ; 
        
         while ( iter.hasNext (  )  ) 
         { 
             key = (String)iter.next();
             container =  (ImportContainer)i_containers.get(key);
             for (int count = 0; count < container.getSamples().size(); count++)
             {
                sample =(ImportSample)container.getSamples().get(count) ;
                if (! sample.getType().equals(Sample.ISOLATE)) continue;
                sequence_id = (String) i_flex_sequences.get(sample.getSequenceId());
                if ( sequence_id == null)
                {
                 is_verified_seq_ids  =false;
                 m_error_messages.add("No sequence found for sequence "+ sample.getSequenceId());
                }
                sample.setSequenceId(sequence_id);
                    // for MGC project only we need to assign MGC_ID and IMAGE_ID to the samples
                
                if ( m_submission_type == this.SUBMISSION_TYPE_MGC)
                {
                     mgs_id_per_sequence = ( String[]) mgc_ids.get(sequence_id);
                     if ( mgs_id_per_sequence != null )
                     {
                         if ( mgs_id_per_sequence[0] != null )
                         {
                              p_info= new PublicInfoItem("MGC_ID", mgs_id_per_sequence[0] );
                              sample.addPublicInfo(p_info);
                         }
                         if ( mgs_id_per_sequence[1] != null )
                         {
                              p_info= new PublicInfoItem("IMAGE_ID", mgs_id_per_sequence[1] );
                              sample.addPublicInfo(  p_info);
                         }
                     }
                }
             }
            
         }
       if ( !is_verified_seq_ids ) 
           throw new Exception("Can not define sequence for sample");
   }
   
   // hash of MGC ids: key  - sequence id from database, element String[], where Str[0] - MGC ID,
   // str[1] - IMAGE ID
   private Hashtable getMGCSequenceIDs() throws Exception
   {
        Hashtable result = new Hashtable();
        String elem[] = null;
        if( i_flex_sequences.size() == 0) return result;
        Iterator iter = i_flex_sequences.values().iterator (  ) ; 
        HashMap  old_new_sequence_id = new HashMap();
        StringBuffer flex_sequence_keys = new StringBuffer();
        String value = null;
         while ( iter.hasNext (  )  ) 
         {  
             value =  (String) iter.next();
             if (value != null )flex_sequence_keys.append(  value +",");
         }
        String sequence_ids = flex_sequence_keys.toString();
        sequence_ids = sequence_ids.substring(0, sequence_ids.length()-1);
        String sql = "SELECT sequenceid, namevalue, nametype FROM name  "
+" where sequenceid in (" + sequence_ids +") and nametype in ('IMAGE_ID','MGC_ID') order by sequenceid " ;
         ResultSet rs = null; int prev_sequence_id = -1;
         try 
            {
                DatabaseTransaction t = DatabaseTransaction.getInstance();
                rs = t.executeQuery(sql);
                while(rs.next()) 
                {
                    if (prev_sequence_id != rs.getInt("sequenceid"))
                    {
                        prev_sequence_id = rs.getInt("sequenceid");
                         elem =  new String[2];
                         result.put(String.valueOf( prev_sequence_id), elem   );
                    }
                       
                    if ( rs.getString("nametype").intern() == FlexSequence.IMAGE_ID)
                    {
                        elem[1]=  rs.getString("namevalue");
                    }
                    else if ( rs.getString("nametype").intern() == FlexSequence.MGC_ID)
                    {
                        elem[0]=  rs.getString("namevalue");
                    }
                    
                    
               
                }
               return result;
            }
            catch(Exception ee)
            {
                throw new Exception (ee.getMessage());
            }
         
   }
    // assume that regardless of file type, one record per sample.
    private ArrayList  readContainerDataFromFilesIntoObjects(HashMap constructs_map, 
            FileStructure[]             file_structures ) throws Exception
    {
        return null;
    }
    
  /*  private HashMap         readFlexSequenceDataFromFilesIntoObjects(HashMap cloneid_flexsequence_map) throws Exception
    {
         HashMap flex_sequences = null;
        
        if ( flex_sequences == null || flex_sequences.size() == 0)
        {
            m_error_messages.add("Error: no flex sequences have been read - abort processing");
            throw new Exception();
        }
        return flex_sequences;
    }
    */
 
      
    
   

    
    ////////////////////////////////////////////////////////////////////////////
    private void          readFlexSequenceDataFromEntrez()
   {
         String key = null;
           Object sequence = null;
            java.net.URL url =   null; 
            String urlString = null;ImportFlexSequence sw=  null;
           InputSource in = null;
           
           Iterator iter = i_flex_sequences.keySet().iterator();
            edu.harvard.med.hip.flex.infoimport.ncbi_record.EntrezParser SAXHandler =
                    new  edu.harvard.med.hip.flex.infoimport.ncbi_record.EntrezParser();
           SAXParser parser = new SAXParser();
            parser.setContentHandler(SAXHandler);
            parser.setErrorHandler(SAXHandler);
            String featureURI = "http://xml.org/sax/features/string-interning";
           try
           {
                  parser.setFeature(featureURI, true);
           }
           catch(Exception e)
           {
               m_error_messages.add("Can not parse  file "+e.getMessage());
           }
         
           
           while (iter.hasNext())
           {
               key = (String)iter.next();
               sequence = i_flex_sequences.get(key);
               //possible values: ImportFlexSequence - sequence build from file
               // Integer: is flexsequnce exsits in db and was verified
               //string - instraction to extract sequence from PubMed
               if (sequence instanceof String)
               {
                   urlString = (String) sequence ;
                 //  System.out.println("started "+urlString);
                   try
                   {
                       url = new java.net.URL(urlString);
                       in = new InputSource( new InputStreamReader(    url.openStream()));
                       parser.parse(in);
                       sw=          SAXHandler.getImportSequence(0);
                       if ( sw.isVerified() )
                            i_flex_sequences.put(key, sw);
                       else
                            i_flex_sequences.put(key, null);
                      //  System.out.println("finished "+urlString);
                 
                   }
                   catch(Exception e)
                   {
                       m_error_messages.add("Can not get  sequence from NCBI "+e.getMessage());
                   }
                   
               }
           }
          
       }
    
    /*
     // read and parse XML for data mapping
   private  FileStructure[]        readDatMappingSchema() throws Exception
   {
        FileToRead fr = null;
        for (int count = 0; count < m_file_input_data.size(); count++)
        {
            fr = (FileToRead) m_file_input_data.get(count);
            if ( fr.getFileType() == FileStructure.FILE_TYPE_XML_DATAMAPPING_SCHEMA)
            {
                break;
            }
        }
        try
        {
             FileMapParser SAXHandler = new FileMapParser();
             SAXParser parser = new SAXParser();
             parser.setContentHandler(SAXHandler);
             parser.setErrorHandler(SAXHandler);
             InputSource in = new InputSource(fr.getInputStream());
              parser.setFeature("http://xml.org/sax/features/string-interning", true);
             parser.parse(in);
             return SAXHandler.getFileStructures();
        }
        catch(Exception e)
        {
            m_error_messages.add("Cannot read data schema XML file");
            throw new Exception("Cannot read data schema XML file");
        }
   }
    
    */
    private void        fillFlexSequenceData( )throws Exception
    {
         if (  m_is_get_flexsequence_from_ncbi)
             readFlexSequenceDataFromEntrez( );
      
        if (m_is_check_flex_sequences_against_FLEX_database)
            checkFlexSequencesInFLEXDatabase( );
        if ( m_is_define_construct_type_by_nuclsequence)
        {
            // set type of construct by last codon
             // and construct group
            setConstructTypeBySequenceStopCodon();
            
        }
    }
      
    private void         setConstructTypeBySequenceStopCodon() throws Exception
    {
         ImportContainer container =  null;
         String key = null;
         ImportSample  sample = null;
         Iterator iter = i_containers.values().iterator (  ) ; 
         ImportFlexSequence sequence = null;
         String codon_seq = null;
         String tr_table_name = null;String construct_type = null;
         TranslationTable tr_table = null;
         PublicInfoItem p_info  = null;
         while ( iter.hasNext (  )  ) 
         { 
             container =  (ImportContainer)iter.next();
             for (int count = 0; count < container.getSamples().size(); count++)
             {
                sample =(ImportSample)container.getSamples().get(count) ;
                if ( i_flex_sequences.get(sample.getSequenceId()) instanceof ImportFlexSequence )
                {
                    sequence = (ImportFlexSequence) i_flex_sequences.get(sample.getSequenceId());
                    
                    // use for vector sequences and other not CDS sequences
                    p_info =  PublicInfoItem.getPublicInfoByName(ImportFlexSequence.PROPERTY_NAME_IS_CHECK_CDS, sequence.getPublicInfo());
                    if (  p_info != null && p_info.getValue().intern() == ImportFlexSequence.PROPERTY_VALUE_NOTCHECK_CDS)
                    {
                        sample.setConstructType(Construct.FUSION);
                        sample.setConstructSize( ImportConstruct.defineConstructSize(sequence.getSequenceText().length()));
                        continue;
                    }
                
                    
                    codon_seq =sequence.getStopCodon();
                    try
                    {
                        edu.harvard.med.hip.flex.util.FlexProperties pr = SpeciesTranslationProperties.getInstance();
                        tr_table_name = pr.getProperty(sequence.getSpesies().replaceAll(" ","."));
                        Hashtable e = TranslationTable.getTranlationTables();
                        if ( tr_table_name!= null ) tr_table = ( TranslationTable) e.get(tr_table_name);
                        if ( tr_table == null)
                        {
                            tr_table  = ( TranslationTable) TranslationTable.getTranlationTables().get("1");
                        }
                        if ( tr_table != null)
                        {
                            construct_type = ( tr_table.isStopCodon( new Codon(codon_seq)) == Codon.PROPERTY_YES)? Construct.CLOSED: Construct.FUSION;
                            sample.setConstructType(construct_type);
                            sample.setConstructSize( ImportConstruct.defineConstructSize(sequence.getSequenceText().length()));
                        }
                    }
                    catch(Exception e)
                    {
                        int a = 1;
                    }
                }
                
             }
         }
    }
    private void insertObjects(Connection conn )
            throws Exception 
    {
        /* 1 - insert flex_sequences
         * 2 - insert containerheader
         * 3 - insert containerheader_name !!! add
         * 4 - insert sample & containercell
         * 5 - insert sample_name         !!!   add
         * 6 - insert constructdesign
         * 7 - insert queue
         * 8 - process object
         * 9 - process execution
         * 
         */
         //put  containers on queue
       
        ImportContainer container = null;
        ImportFlexSequence flex_sequence = null;
        
         // insert flex_sequences 
        Iterator iter = i_flex_sequences.keySet().iterator (  ) ; 
        HashMap  old_new_sequence_id = new HashMap();
        String flex_sequence_key = null;                                                                                                                                                                                                                                                               
         while ( iter.hasNext (  )  ) 
         {  
             flex_sequence_key = (String) iter.next();
          //   System.out.println(flex_sequence_key);
             if ( !( i_flex_sequences.get(flex_sequence_key) instanceof ImportFlexSequence)  )
                 continue;
             flex_sequence =  (ImportFlexSequence)i_flex_sequences.get(flex_sequence_key);
             flex_sequence.insert(conn, m_error_messages);
             i_flex_sequences.put( flex_sequence_key, String.valueOf(flex_sequence.getId()));
             //System.out.println(flex_sequence_key);
         }
         setFlexSequenceIdsForSamples( );
        iter = i_containers.keySet().iterator (  ) ; 
        String container_key = null;
       
        String project_code = getProjectCode(); 
         while ( iter.hasNext (  )  ) 
         { 
             container_key = (String)iter.next();
             container =  (ImportContainer)i_containers.get(container_key);
             container.setLocation(m_plate_location);
             container.setType(m_plate_type);
             if (m_submission_type == this.SUBMISSION_TYPE_MGC)
                 container.insertMGC(conn, m_error_messages,m_mgc_file_name);
             else
                container.insert(conn, m_error_messages, m_project_id, m_workflow_id,
                        project_code, m_sample_biotype);
         }
                createProcessRecords( conn,  i_containers.values(),  m_protocol_id,  m_project_id ,   m_workflow_id   );
       
    }
    
    private String getProjectCode() throws Exception
    {
        Project project = new Project(m_project_id);
        Workflow wf = project.getWorkflow(m_workflow_id);
        if(wf != null)
        {
            return ((ProjectWorkflow)wf).getCode();
        }
        return "";
    }
    
    private void    createProcessRecords( Connection conn,  
            Collection containers,  int current_protocol_id,   int current_project_id , 
            int current_workflow_id) throws Exception
    {
        if ( m_is_put_on_queue) putOnQueue( conn,  containers, current_protocol_id,  current_project_id ,   current_workflow_id   );
        int execution_id = createProcessExecutionRecord(  conn,  current_protocol_id,     current_project_id ,    current_workflow_id);
        createProcessObjectRecord( conn,   containers,  current_protocol_id,     current_project_id ,   current_workflow_id,  execution_id );
        if (m_is_fillin_clones_records)
            fillInClonesRecords(conn);
    }
   
    private  void fillInClonesRecords(Connection conn) throws Exception
    {
          Iterator iter = i_containers.values().iterator (  ) ; 
          ImportContainer container = null;
          
          while ( iter.hasNext (  )  ) 
         { 
             container =  (ImportContainer) iter.next();
             container.checkCloningStrategyIDAsigment();
             ImportSample.populateObtainedMasterprogressTables( conn, container.getSamples()) ;
         }
        
    }
       // verify 3 name tables;
    // verify species name
    private   boolean verifyObjectsMapToNamingTables(Connection conn)
           
    {
        ArrayList flexsequence_names = new ArrayList();
         ArrayList species_names = new ArrayList();
        ArrayList container_names  = new ArrayList();
        ArrayList sample_names = new ArrayList();
        ImportFlexSequence sequence = null;
        Iterator iter = i_flex_sequences.keySet().iterator();
        PublicInfoItem p_item = null;
        ImportSample sample = null; ImportContainer container = null;
        Object temp = null;
         while( iter.hasNext())
         {
            temp = i_flex_sequences.get( (String)iter.next());
            if (temp instanceof edu.harvard.med.hip.flex.infoimport.coreobjectsforimport.ImportFlexSequence)
            {
                sequence = (ImportFlexSequence ) temp;
                if ( !species_names.contains(sequence.getSpesies()))
                {
                    species_names.add(sequence.getSpesies());
                    if (sequence.getSpesies()==null)
                    {
                        System.out.println(sequence.getId());
                    }
                }
                for (int p_count = 0; p_count < sequence.getPublicInfo(). size(); p_count++)
                {
                    p_item = (PublicInfoItem ) sequence.getPublicInfo().get(p_count);
                    if ( !flexsequence_names.contains( p_item.getName()) && p_item.isSubmit())
                         flexsequence_names.add( p_item.getName());
                    
                }
            }
         }
        iter =i_containers.keySet().iterator();
        while( iter.hasNext())
         {
            container = (ImportContainer) i_containers.get( (String)iter.next());
            for (int p_count = 0; p_count < container.getPublicInfo().size(); p_count++)
            {
                p_item = (PublicInfoItem ) container.getPublicInfo().get(p_count);
                if (! container_names.contains( p_item.getName())&& p_item.isSubmit())
                     container_names.add( p_item.getName());
            }
            for (int s_count = 0; s_count <  container.getSamples().size(); s_count++)
            {
                sample = (ImportSample) container.getSamples().get(s_count);
                for (int p_count = 0; p_count < sample.getPublicInfo().size(); p_count++)
                {
                    p_item = (PublicInfoItem ) sample.getPublicInfo().get(p_count);
                    if ( ! sample_names.contains( p_item.getName())&& p_item.isSubmit())
                    {  sample_names.add( p_item.getName());
                      }
                }
            }
            
        }
        boolean isAllFlexsequenceNamesExists = checkNamesInDatabase(flexsequence_names, ConstantsImport.s_flex_names);
        if ( !isAllFlexsequenceNamesExists) m_error_messages.add("Check 'NAMETYPE' table."+ Algorithms.convertStringArrayToString(flexsequence_names,","));
        boolean isAllContainerNamesExists = checkNamesInDatabase(container_names, ConstantsImport.s_container_names);
          if ( !isAllContainerNamesExists) m_error_messages.add("Check 'CONTAINERHEADER_NAMETYPE' table. "+ Algorithms.convertStringArrayToString(container_names,","));
         boolean isAllSampleExists = checkNamesInDatabase(sample_names, ConstantsImport.s_sample_type_names);
         if ( !isAllSampleExists) m_error_messages.add("Check 'SAMPLE_NAMETYPE' table."+ Algorithms.convertStringArrayToString(sample_names,","));
       boolean isSpeciesExists = checkNamesInDatabase(species_names, ConstantsImport.s_species_names);
       if ( !isSpeciesExists ) m_error_messages.add("Check 'SPECIES' table."+ Algorithms.convertStringArrayToString(species_names,","));
        return (isAllFlexsequenceNamesExists && isAllContainerNamesExists && isAllSampleExists && isSpeciesExists);
    }
    
     
    
    
    private void  createProcessObjectRecord( Connection conn,  
            Collection containers,  int current_protocol_id,   int current_project_id , 
            int current_workflow_id,  int execution_id )    throws Exception
    {
        String sql = "insert into processobject (executionid, inputoutputflag, containerid) values ("
                  +execution_id+",'" + edu.harvard.med.hip.flex.process.ProcessObject.OUTPUT +"',?)" ;
        PreparedStatement stmt = null;
        ImportContainer container = null;
        Iterator iter = containers.iterator();
        try 
        {
            stmt = conn.prepareStatement(sql);
            while (iter.hasNext()) 
            {
                container = (ImportContainer) iter.next();
                stmt.setInt(1, container.getId());
                 DatabaseTransaction.executeUpdate(stmt);
            }
       
        }
        catch(Exception e)
        { 
            m_error_messages.add ("Can not create process object for container: "  +e.getMessage());
            throw new FlexDatabaseException(e+"\nSQL: "+sql);
        } 
        finally 
        {
            DatabaseTransaction.closeStatement(stmt);
        }
    }
    
    
    private int createProcessExecutionRecord( Connection conn, 
            int current_protocol_id,   int  current_project_id ,
            int current_workflow_id) throws Exception
    {
         
        int execution_id= FlexIDGenerator.getID("executionid");
         String sql = "insert into processexecution (executionid, protocolid, executionstatus," +
         " researcherid, processdate";
         String valueSql =" values("+execution_id+","+
         current_protocol_id + ",'" + edu.harvard.med.hip.flex.process.Process.SUCCESS + "', " + m_researcher.getId() + ", sysdate";
         
         
         if(current_project_id > 0) 
         {
             sql +=  ", projectid";
             valueSql +=  "," + current_project_id;
         }
         
         if(current_workflow_id > 0) 
         {
             sql  += ", workflowid";
             valueSql  += "," + current_workflow_id;
         }
         
         DatabaseTransaction.executeUpdate(sql+") "+valueSql+")", conn);
         return execution_id ;
    }
    
    
    private void putOnQueue(Connection conn, Collection containers,
            int current_protocol_id,   int  current_project_id ,
            int current_workflow_id) throws Exception
    {
        
        ImportContainer container = null;
        String sql = new String("insert into queue (protocolid, dateadded, containerid, projectid, workflowid)\n" +
        "values(?, sysdate, ?, ?, ?)");
        PreparedStatement stmt = null;
        Iterator iter = containers.iterator();
        try 
        {
             // find next protocol
            Workflow workflow = new Workflow(current_workflow_id);
            Protocol next_protocol = (Protocol) workflow.getNextProtocol( new Protocol(current_protocol_id)).get(0);
            current_protocol_id = next_protocol.getId();
//put on queue
           
            stmt = conn.prepareStatement(sql);
            while (iter.hasNext()) 
            {
                container = (ImportContainer) iter.next();
                stmt.setInt(1, current_protocol_id);
                stmt.setInt(2, container.getId());
                stmt.setInt(3, current_project_id);
                stmt.setInt(4, current_workflow_id);
                DatabaseTransaction.executeUpdate(stmt);
            }
        } 
        catch (SQLException sqlE) 
        {
            m_error_messages.add ("Can not put containers on queue for protocol: "  +sqlE.getMessage());
            throw new FlexDatabaseException(sqlE+"\nSQL: "+sql);
        } 
        finally 
        {
            DatabaseTransaction.closeStatement(stmt);
        }
    }
    
}
