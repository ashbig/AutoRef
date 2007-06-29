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
/* sql changes to FLEX database
 *
CREATE TABLE CONTAINERHEADER_NAMETYPE
 (NAMETYPE VARCHAR2(50) NOT NULL
 ,DISPLAYTITLE VARCHAR2(200)
 );

CREATE TABLE SAMPLE_NAMETYPE
 (NAMETYPE VARCHAR2(50) NOT NULL
 ,DISPLAYTITLE VARCHAR2(200)
 );

CREATE TABLE SAMPLE_NAME
 (SAMPLEID NUMBER(10,0) NOT NULL
 ,NAMETYPE VARCHAR2(50) NOT NULL
 ,NAMEVALUE VARCHAR2(500) NOT NULL
 ,NAMEURL VARCHAR2(500)
 ,DESCRIPTION VARCHAR2(500)
 );

alter TABLE CONTAINERHEADER
 add ADDITIONALINFO NUMBER(1) DEFAULT 0 NOT NULL;

CREATE TABLE CONTAINERHEADER_NAME
 (CONTAINERID NUMBER(10,0) NOT NULL
 ,NAMETYPE VARCHAR2(50) NOT NULL
 ,NAMEVALUE VARCHAR2(200) NOT NULL
 ,NAMEURL VARCHAR2(500)
 ,DESCRIPTION VARCHAR2(240)
 );

alter TABLE SAMPLE
 add ADDITIONALINFO NUMBER(1) DEFAULT 0 NOT NULL;

 

ALTER TABLE CONTAINERHEADER_NAMETYPE
 ADD (CONSTRAINT OCE_PK PRIMARY KEY 
  (NAMETYPE));

ALTER TABLE SAMPLE_NAMETYPE
 ADD (CONSTRAINT SNT_PK PRIMARY KEY 
  (NAMETYPE));

ALTER TABLE SAMPLE_NAME
 ADD (CONSTRAINT SAE_PK PRIMARY KEY 
  (SAMPLEID
  ,NAMETYPE));

ALTER TABLE CONTAINERHEADER_NAME
 ADD (CONSTRAINT OCN_PK PRIMARY KEY 
  (CONTAINERID
  ,NAMETYPE));

ALTER TABLE CONTAINERHEADER
 ADD (CONSTRAINT AVCON_1180642153_ADDIT_000 CHECK (ADDITIONALINFO IN (0, 1)))
;  


ALTER TABLE SAMPLE
 ADD (CONSTRAINT AVCON_1180642153_ADDIT_001 CHECK (ADDITIONALINFO IN (0, 1)))
;

ALTER TABLE SAMPLE_NAME ADD (CONSTRAINT
 SAE_SNT_FK FOREIGN KEY 
  (NAMETYPE) REFERENCES SAMPLE_NAMETYPE
  (NAMETYPE))
;
ALTER TABLE SAMPLE_NAME ADD (CONSTRAINT
 SAE_SAMPLE_FK FOREIGN KEY 
  (SAMPLEID) REFERENCES SAMPLE
  (SAMPLEID) ON DELETE CASCADE);

ALTER TABLE CONTAINERHEADER_NAME ADD (CONSTRAINT
 OCN_OCE_FK FOREIGN KEY 
  (NAMETYPE) REFERENCES CONTAINERHEADER_NAMETYPE
  (NAMETYPE))


;
ALTER TABLE CONTAINERHEADER_NAME ADD (CONSTRAINT
 OCN_CHEADER_FK FOREIGN KEY 
  (CONTAINERID) REFERENCES CONTAINERHEADER
  (CONTAINERID) ON DELETE CASCADE);

  
 *
 *alter constructdesign 
 *add protocol - UPLOAD_CONTAINERS_FROM_FILE
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
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.workflow.*;
import edu.harvard.med.hip.flex.infoimport.file_mapping.*;
import edu.harvard.med.hip.flex.infoimport.coreobjectsforimport.*;

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
    private     int     m_project_id = -1;
    private     int     m_workflow_id = -1;
    private     boolean m_isOutOnFirstError = false;
      private int             m_number_of_samples_per_container = 96;
  private boolean         m_is_check_flex_sequences_against_FLEX_database = false;
    private   String        m_mgc_file_name = null;   
    private   String        m_plate_type = null;
    
    public  void       setProjectId(int v)   {     m_project_id = v;}
    public    void      setWorkFlowId(int v ){    m_workflow_id = v;}
    public void            isCheckInFLEXDatabase(boolean v){ m_is_check_flex_sequences_against_FLEX_database = v;}
    public void            setNumberOfWellsInContainer(int v){m_number_of_samples_per_container = v;}
   public void              setMGCFileName(String v){ m_mgc_file_name = v;}
   public void              setPlateType(String v){ m_plate_type = v;}
   
   
    public String getTitle() {        return "Upload of information for third-party containers into FLEX.";    }
    
    public void run_process() 
    {
        Connection  conn = null;
        HashMap containers = null;
        HashMap flex_sequences = null;
        FileStructure[]             file_structures = null;
        try
        {
            // read in data mapping schema
            file_structures = readDatMappingSchema();
  // array list of containers read from 1 file         
            readDataFromFiles(file_structures , containers, flex_sequences);
            fillFlexSequenceData(flex_sequences);
            conn = DatabaseTransaction.getInstance().requestConnection();   
            boolean isVerifed = verifyObjectsMapToNamingTables(conn, flex_sequences, containers);
            insertObjects(conn, flex_sequences,  containers);
            DatabaseTransaction.commit(conn);
        }
        catch(Exception e)
        {
             System.out.println(e.getMessage());
            m_error_messages.add("Cannot upload new plates from files.\n"+e.getMessage());
        }
        finally
         {
            sendEmails("New plates upload into FLEX","New plates upload into FLEX");
         }
    
    }
    
   
    private void    readDataFromFiles( FileStructure[]  file_structures ,
            HashMap containers, HashMap flex_sequences)
    {
        
    }
    
  
         
      private void        checkFlexSequencesInFLEXDatabase(HashMap flex_sequences)
      {
          
      }
       
       private void             reassignSequenceIds(HashMap old_new_sequence_id, HashMap containers)
       {
           
       }
    // assume that regardless of file type, one record per sample.
    private ArrayList  readContainerDataFromFilesIntoObjects(HashMap constructs_map, 
            FileStructure[]             file_structures ) throws Exception
    {
        ArrayList containers = null;
        //get right file
        FileToRead fr = null;
        FileStructure      mapping_file_structure = null; 
        for (int count = 0; count < m_file_input_data.size(); count++)
        {
            fr = (FileToRead) m_file_input_data.get(count);
            if ( fr.getFileType() == FileStructure.FILE_TYPE_ONE_FILE_SUBMISSION 
                        || fr.getFileType() == FileStructure.FILE_TYPE_PLATE_MAPPING)
            {
                break;
            }
        }
        // find file_structure for the file
        mapping_file_structure = (file_structures[FileStructure.FILE_TYPE_ONE_FILE_SUBMISSION] != null )?
            file_structures[FileStructure.FILE_TYPE_ONE_FILE_SUBMISSION] :
            file_structures[FileStructure.FILE_TYPE_PLATE_MAPPING];
        
              //read file 
        fr.readFile(true, true, true);
 
        if ( containers == null || containers.size() == 0)
        {
            m_error_messages.add("Error: no containers have been read - abort processing");
            throw new Exception();
        }
        return containers;
    }
    
    private HashMap         readFlexSequenceDataFromFilesIntoObjects(HashMap cloneid_flexsequence_map) throws Exception
    {
         HashMap flex_sequences = null;
        
        if ( flex_sequences == null || flex_sequences.size() == 0)
        {
            m_error_messages.add("Error: no flex sequences have been read - abort processing");
            throw new Exception();
        }
        return flex_sequences;
    }
    
 
      
    
   

    
    ////////////////////////////////////////////////////////////////////////////
    private void          readFlexSequenceDataFromEntrez(HashMap flex_sequences)
       {
           String key = null;
           Object sequence = null;
            java.net.URL url =   null; 
            String urlString = null;ImportFlexSequence sw=  null;
           InputSource in = null;
           
           Iterator iter = flex_sequences.keySet().iterator();
           EntrezParser SAXHandler = new EntrezParser();
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
               m_error_messages.add("Cannot get  set parser feuture "+e.getMessage());
           }
         
           
           while (iter.hasNext())
           {
               key = (String)iter.next();
               sequence = flex_sequences.get(key);
               //possible values: ImportFlexSequence - sequence build from file
               // Integer: is flexsequnce exsits in db and was verified
               //string - instraction to extract sequence from PubMed
               if (sequence instanceof String)
               {
                   urlString = (String) sequence + key;
                   try
                   {
                       url = new java.net.URL(urlString);
                       in = new InputSource( new InputStreamReader(    url.openStream()));
                       parser.parse(in);
                       sw=          SAXHandler.getImportSequence(0);
                   }
                   catch(Exception e)
                   {
                       m_error_messages.add("Cannot get  sequence from internet"+e.getMessage());
                   }
                   if ( sw.isVerified() )
                       flex_sequences.put(key, sw);
               }
           }
       }
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
    
    
      private void        fillFlexSequenceData(HashMap flex_sequences)
    {
        if (m_is_check_flex_sequences_against_FLEX_database)
            checkFlexSequencesInFLEXDatabase(flex_sequences);
        readFlexSequenceDataFromEntrez(flex_sequences);
    }
      
    private void insertObjects(Connection conn, HashMap flex_sequences, 
             HashMap containers)
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
        Protocol current_protocol = null;
        Project  current_project = null;
        Workflow current_workflow = null;
        ImportContainer container = null;
        ImportFlexSequence flex_sequence = null;
         try
        {
            current_protocol = new Protocol(  Protocol.UPLOAD_CONTAINERS_FROM_FILE);
            current_project = new Project(m_project_id);
            current_workflow = new Workflow(m_workflow_id);
            
        }
         catch(FlexDatabaseException ex)
        {
            throw new Exception ("Can not get next protocol." + ex.getMessage());
        }
         // insert flex_sequences 
        Iterator iter = flex_sequences.keySet().iterator (  ) ; 
        HashMap  old_new_sequence_id = new HashMap();
        String flex_sequence_key = null;
         while ( iter.hasNext (  )  ) 
         {  
             flex_sequence_key = (String) iter.next();
             flex_sequence =  (ImportFlexSequence)flex_sequences.get(flex_sequence_key);
             flex_sequence.insert(conn, m_error_messages);
             old_new_sequence_id.put( flex_sequence_key, new Integer(flex_sequence.getId()));
         }
        reassignSequenceIds(old_new_sequence_id, containers);
        iter = containers.entrySet().iterator (  ) ; 
         while ( iter.hasNext (  )  ) 
         { 
             container =  (ImportContainer)iter.next();
             if (current_project.getId() == Project.MGC_PROJECT)
                 container.insertMGC(conn, m_error_messages,m_mgc_file_name);
             else
                container.insert(conn, m_error_messages);
         }
       
         putForProcessing( conn,  containers.values(),   current_protocol,     current_project ,             current_workflow);
         DatabaseTransaction.commit(conn);      
    }
    
   
    
       // verify 3 name tables;
    // verify species name
    private   boolean verifyObjectsMapToNamingTables(Connection conn, HashMap flex_sequences, HashMap containers)
           
    {
        ArrayList flexsequence_names = new ArrayList();
        ArrayList container_names  = new ArrayList();
        ArrayList sample_names = new ArrayList();
        ImportFlexSequence sequence = null;
        Iterator iter = flex_sequences.entrySet().iterator();
        PublicInfoItem p_item = null;
        ImportSample sample = null; ImportContainer container = null;
      
         while( iter.hasNext())
         {
            sequence = (ImportFlexSequence ) iter.next();
            for (int p_count = 0; p_count < sequence.getPublicInfo(). size(); p_count++)
            {
                p_item = (PublicInfoItem ) sequence.getPublicInfo().get(p_count);
                if ( flexsequence_names.contains( p_item.getName()))
                     flexsequence_names.add( p_item.getName());
            }
         }
        iter = containers.entrySet().iterator();
        while( iter.hasNext())
         {
            container = (ImportContainer) iter.next();
            for (int p_count = 0; p_count < container.getPublicInfo().size(); p_count++)
            {
                p_item = (PublicInfoItem ) container.getPublicInfo().get(p_count);
                if ( container_names.contains( p_item.getName()))
                     container_names.add( p_item.getName());
            }
            for (int s_count = 0; s_count <  container.getSamples().size(); s_count++)
            {
                sample = (ImportSample) container.getSamples().get(s_count);
                for (int p_count = 0; p_count < sample.getPublicInfo().size(); p_count++)
                {
                    p_item = (PublicInfoItem ) sample.getPublicInfo().get(p_count);
                    if ( sample_names.contains( p_item.getName()))
                         sample_names.add( p_item.getName());
                }
            }
            
        }
        boolean isAllFlexsequenceNamesExists = checkNamesInDatabase(flexsequence_names, ConstantsImport.s_flex_names);
        boolean isAllContainerNamesExists = checkNamesInDatabase(container_names, ConstantsImport.s_container_names);
        boolean isAllSampleExists = checkNamesInDatabase(sample_names, ConstantsImport.s_sample_names);
       
        return (isAllFlexsequenceNamesExists && isAllContainerNamesExists && isAllSampleExists);
    }
    
    private boolean checkNamesInDatabase(ArrayList cur_names, Hashtable names )
    {
        for (int i_count = 0; i_count < cur_names.size(); i_count++)
        {
            if (names.get( (String)cur_names.get((i_count))) == null)
                return false;
        }
        return true;
    
    }
    
  
    /* Function queries for all mgc containers needed for request
     *put on Queue all containers and sequences
     */
    private void putForProcessing(Connection conn, Collection containers,
             Protocol current_protocol,   Project  current_project ,
            Workflow current_workflow) throws Exception
    {
    
        putOnQueue( conn,  containers,  current_protocol,     current_project ,   current_workflow   );
        createProcessRecord( conn,  containers,  current_protocol,     current_project ,   current_workflow   );
    
    }
    
    private void createProcessRecord( Connection conn, Collection containers,
            Protocol current_protocol,   Project  current_project ,
            Workflow current_workflow) throws Exception
    {
        ContainerProcessObject container_process_object = null;
         boolean isSubmitted = true;
        int container_id = -1;
        
        Researcher researcher = new Researcher();
        int userId = researcher.getId("SYSTEM");
        researcher = new Researcher(userId);
        
        edu.harvard.med.hip.flex.process.Process process = 
            new edu.harvard.med.hip.flex.process.Process(
                current_protocol, "??????",researcher, current_project, current_workflow);
        int execution_id = process.getExecutionid();
        try
        {
            Iterator iter = containers.iterator();
            while ( iter.hasNext())
            {
                container_id = ((ImportContainer) iter.next()).getId();
                container_process_object = new ContainerProcessObject(container_id,execution_id,"O");
                container_process_object.insert(conn);
            }
        }
        catch(Exception e)
        {
            isSubmitted = false;
            if ( m_isOutOnFirstError ) throw new Exception ("Can not put containers on queue for protocol " + current_protocol +e.getMessage());
            else m_error_messages.add ("Can not put containers on queue for protocol " + current_protocol +e.getMessage());
        }
        if ( ! isSubmitted) throw new Exception ("Can not put containers on queue for protocol " + current_protocol );
  
    }
    private void putOnQueue(Connection conn, Collection containers,
            Protocol current_protocol,   Project  current_project ,
            Workflow current_workflow) throws Exception
    {
        QueueItem queueItem = null;
        LinkedList queueItems = new LinkedList();
        boolean isSubmitted = true;
        ContainerProcessQueue containerQueue = new ContainerProcessQueue();
        ImportContainer container = null;
        
        Iterator iter = containers.iterator();
        while ( iter.hasNext())
        {
            container = (ImportContainer) iter.next();
            queueItem = new QueueItem(container,
                        current_protocol, current_project, current_workflow);
            queueItems.add(queueItem);
        }
        try
        {
            containerQueue.addQueueItems(queueItems, conn);
        }
        catch(Exception e)
        {
            isSubmitted = false;
            if ( m_isOutOnFirstError ) throw new Exception ("Can not put containers on queue for protocol " + current_protocol +e.getMessage());
            else m_error_messages.add ("Can not put containers on queue for protocol " + current_protocol +e.getMessage());
        }
        if ( ! isSubmitted) throw new Exception ("Can not put containers on queue for protocol " + current_protocol );
   
    }
}
