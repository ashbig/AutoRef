/*
 * FLEXtoPLASMIDImporter.java
 *
 * Created on April 8, 2008, 10:18 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.infoimport.plasmidimport;

import java.io.*;
import java.util.*;
import java.sql.*;



import edu.harvard.med.hip.flex.infoimport.*;
import edu.harvard.med.hip.flex.infoimport.coreobjectsforimport.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.util.*;
import edu.harvard.med.hip.flex.user.*;
import  edu.harvard.med.hip.flex.report.*;
import edu.harvard.med.hip.flex.infoimport.file_mapping.*;
import edu.harvard.med.hip.flex.infoimport.plasmidimport.filemanipulation.*;
import static edu.harvard.med.hip.flex.RearrayConstants.PLATE_TYPE;
import static edu.harvard.med.hip.flex.RearrayConstants.SAMPLE_TYPE;
import static edu.harvard.med.hip.flex.infoimport.ConstantsImport.ITEM_TYPE;
import static edu.harvard.med.hip.flex.infoimport.plasmidimport.PlasmidImporterDefinitions.PLASMID_TRANSFER_CLONE_STATUS;
import edu.harvard.med.hip.flex.infoimport.plasmidimport.databasemanipulation.*;
import static edu.harvard.med.hip.flex.infoimport.plasmidimport.PlasmidImporterDefinitions.IMPORT_ACTIONS;
import  edu.harvard.med.hip.flex.infoimport.plasmidimport.utils.*;
/**
 *
 * @author htaycher
 */
public class FLEXtoPLASMIDImporter  extends ImportRunner
{
     private     HashMap          i_containers = null;
    private     HashMap          i_flex_sequences = null;
    private     HashMap          i_authors = null;
    private     HashMap         i_publications= null;
    
    private SAMPLE_TYPE              m_sample_type=SAMPLE_TYPE.GLYCEROL;
    private PLATE_TYPE              m_plate_type=PLATE_TYPE.PLATE_96_WELL;
    private   PLASMID_TRANSFER_CLONE_STATUS          m_clone_status=PLASMID_TRANSFER_CLONE_STATUS.NOT_READY_FOR_TRANSFER;
    private String m_hoststrain1 ;
private String m_hoststrainIsInUse1;
private String m_hoststrainDescription1;
private String m_hoststrain2 ;
private String m_hoststrainIsInUse2;
private String m_hoststrainDescription2;
private String m_restriction;
private String  m_marker;
private String  m_growthCondition1;
private boolean m_isRecomendedGC1;
private String  m_growthCondition2;
private boolean m_isRecomendedGC2;
private String[] m_plasmid_collections=null;
private String  m_hosttype=null;

private HashMap<String, HashMap<String,FlexPlasmidMap>>         m_flex_plasmid_maps;
private HashMap<String, String>         m_flex_plasmid_submission_map;


private boolean i_isPublicationFile=false;
private boolean i_isClonePublication = false;
private boolean i_isRefseqName=false;
private boolean i_isCloneProperty=false;
private boolean i_isCloneHost=false;
                

private enum CLONE_INSERT_INDEX_MODE
{
    MODE_CLONE_INSERT_SEQUENCE,
    MODE_CLONE_INSERT_PROPERTY
}
    
    // this eventually should be set up by user thr. interface
    // in plasmid import routine : hardcoded
    
  private String              m_plate_status = plasmid.coreobject.Container.FILLED;
    private String              m_container_location=edu.harvard.med.hip.flex.core.Location.FREEZER;
    
    private boolean isDebug = false;
    /** Creates a new instance of FLEXtoPLASMIDImporter */
    public FLEXtoPLASMIDImporter() {
    }
    public void     setSampleType(String v)throws Exception{m_sample_type=SAMPLE_TYPE.valueOf(v);}
    public void     setPlateType(String v)throws Exception {              m_plate_type=PLATE_TYPE.valueOf(v);}
    public void     setCloneStatus(String v)throws Exception {m_clone_status=PLASMID_TRANSFER_CLONE_STATUS.valueOf(v);}
    
    public String getTitle() {        return "Clone information transfer from FLEX to PLAMID.";    }
   
    
public void     setHoststrain1(String v){   m_hoststrain1 =v ;}
public void     setHoststrainIsInUse1(String v){  m_hoststrainIsInUse1 = v;}
public void     setHoststrainDescription1(String v){  m_hoststrainDescription1 = v;}
public void     setHoststrain2(String v){  m_hoststrain2 =   v ;}
public void     setHoststrainIsInUse2(String v){  m_hoststrainIsInUse2 = v;}
public void     setHoststrainDescription2(String v){  m_hoststrainDescription2 =   v;}
public void     setRestriction(String v){  m_restriction=v;}
public void     setMarker(String v){  m_marker=v;}
public void     setGrowthCondition1(String v){  m_growthCondition1=v;}
public void     setIsRecomendedGC1(boolean v){   m_isRecomendedGC1=v;}
public void     setGrowthCondition2(String v){   m_growthCondition2=v;}
public void     setIsRecomendedGC2(boolean v){   m_isRecomendedGC2=v;}
public void     setCollections(String[] v){   m_plasmid_collections=v;}
public void     setHosttype(String v){ m_hosttype = v;}
public void     setDebugStatus(boolean n){ isDebug = n;}

   public void run_process() 
    {
         Connection conn=null;
         try
        {
                 conn = DatabaseTransaction.getInstance().requestConnection();
             switch(m_process_type)
             {
                 case   TRANSFER_FLEX_TO_PLASMID_CREATE_FILES:
             {
                 
                 m_flex_plasmid_maps = loadMapFLEX_To_PLASMID_Maps(conn);
                 m_flex_plasmid_submission_map=readSubmissionSpecificParameters(m_instream_input_files_data_schema);
                 writeSubmissionFiles(conn);
                 break;
             }
             case TRANSFER_FLEX_TO_PLASMID_IMPORT:
             {
                  FlexProperties sysProps = StaticPropertyClassFactory.makePropertyClass("FlexProperties");
        
                 uploadDataFromSubmissionFilesIntoPlasmID( sysProps );
                 break;
             }
             case  TRANSFER_FLEX_TO_PLASMID_DIRECT_IMPORT:
             {
                 m_flex_plasmid_maps = loadMapFLEX_To_PLASMID_Maps(conn);
                 writeSubmissionFiles(conn);
                  FlexProperties sysProps = StaticPropertyClassFactory.makePropertyClass("FlexProperties");
        
                 uploadDataFromSubmissionFilesIntoPlasmID(sysProps  );
                 break;
             }
                
             
             }
        }
        catch(Exception e)
        {
            m_error_messages.add(e.getMessage());
            DatabaseTransaction.rollback(conn);
        }
         finally
         {
            DatabaseTransaction.closeConnection(conn) ;
            sendEmails("Data transfer from FLEX to PLASMID","Data transfer from FLEX to PLASMID");
         }
    }
     
    
    //*********************************************************************
    public  HashMap<String, HashMap<String,FlexPlasmidMap>> 
            loadMapFLEX_To_PLASMID_Maps(Connection conn) throws Exception
    {
        HashMap<String, HashMap<String,FlexPlasmidMap>> temp = new  HashMap<String, HashMap<String,FlexPlasmidMap>>  ();
        loadMapByType(temp,conn, IMPORT_ACTIONS.CONNECT_PLASMID_FLEX_VECTOR_NAMES_SUBMITTED);
        loadMapByType(temp,conn, IMPORT_ACTIONS.CONNECT_PLASMID_FLEX_AUTHOR_SUBMITTED);
        loadMapByType(temp,conn,IMPORT_ACTIONS.CONNECT_PLASMID_FLEX_AUTHOR_TYPE_SUBMITTED);
        loadMapByType(temp,conn,  IMPORT_ACTIONS.CONNECT_PLASMID_FLEX_SPECIES_SUBMITTED);
        loadMapByType(temp,conn,  IMPORT_ACTIONS.CONNECT_PLASMID_FLEX_NAMETYPE_SUBMITTED);
       loadMapByType(temp,conn,  IMPORT_ACTIONS.CONNECT_PLASMID_FLEX_CLONE_NAMETYPE_SUBMITTED);
        loadMapByType(temp,conn,  IMPORT_ACTIONS.CONNECT_PLASMID_FLEX_CLONEPROPERTY_NAMETYPE_SUBMITTED);
        return temp;
    }
   
    private  void        loadMapByType(HashMap<String, HashMap<String,FlexPlasmidMap>> temp,
            Connection conn, IMPORT_ACTIONS cur_map)throws Exception
    {
        HashMap<String,FlexPlasmidMap> cur_map_hash = new HashMap<String,FlexPlasmidMap>();
        FlexPlasmidMap map = new FlexPlasmidMap();
        try
        {
            List<FlexPlasmidMap>  items = map.getAllMapItems(conn, cur_map,true);
            if (items != null && items.size()>0)
            {
                for (FlexPlasmidMap item: items){cur_map_hash.put(item.getFlexName(), item);}
                temp.put(cur_map.toString(),cur_map_hash);
            }
        }
        catch(Exception e)
        {
            m_error_messages.add(e.getMessage());
            throw new Exception ("Can not load map: "+cur_map.toString()+e.getMessage());
        }
        
    }
    
    
    public  HashMap<String,String>          readSubmissionSpecificParameters
            (InputStream submission_schema)
            throws Exception
    {
        HashMap<String,String> map = new HashMap<String,String>();
        String line;BufferedReader in;String[] temp = new String[2]; 
        
       try
       {
           in = new BufferedReader(new InputStreamReader(submission_schema));
            while((line = in.readLine()) != null) 
            {
                if(line.startsWith("#") || line.trim()=="" ){continue;}
                else
                {
                    temp=line.trim().split("=");
                    if ( temp.length ==2)
                    {map.put(temp[0].trim(),temp[1].trim());}
                }
                
            }      
            in.close();   in.close();         submission_schema.close();
            return map;
       }
        catch(Exception e)
        {
            m_error_messages.add(e.getMessage());
            throw new Exception ("Can not read submission properties map");
        }
    }
    
    
   private Connection     getPlasmidConnection(FlexProperties sysProps) throws Exception
    {
        Connection conn;
        try
        {
            conn = DatabaseTransactionLocal.getInstance(
               sysProps.getInstance().getProperty("PLASMID_URL") , 
                        sysProps.getInstance().getProperty("PLASMID_USERNAME"), 
                        sysProps.getInstance().getProperty("PLASMID_PASSWORD")).requestConnection();
                        return conn;
        }
        catch(Exception e)
        {
            m_error_messages.add(e.getMessage());
            throw new Exception ("Can not establish connection to PLASMID database.");
        }
    }
            
   
   private void            uploadDataFromSubmissionFilesIntoPlasmID(FlexProperties sysProps)
        throws Exception
    {
        Connection plasmid_connection = null;
        FileStructure[]             file_structures = null;
      
        try
        {
            plasmid_connection = getPlasmidConnection(sysProps);
             // read in data mapping schema
            file_structures = readDataMappingSchema();
  // array list of containers read from 1 file         
          //  readDataFromFiles(file_structures );
           
          //  ImportContainer.printContainers( i_containers,i_flex_sequences);
            synchronized (this)
            {
              //   checkContainerLabelsForDublicates();
                if (i_containers == null || i_containers.size() == 0 || i_flex_sequences==null
                        || i_flex_sequences.size() == 0)
                    return;

                boolean isVerifed =true;;// = verifyObjectsMapToNamingTables(plasmid_connection );
                if ( ! isVerifed )     {  m_error_messages.add("Cannot verify ");    throw new Exception();     }
        //        insertObjects(plasmid_connection);
            }
            // prepare final messages
            ImportContainer container = null;
            Iterator iter = i_containers.values().iterator();
            while(iter.hasNext())
            {
                container = (ImportContainer) iter.next();
                m_process_messages.add("Container "+ container.getLabel()+" have been transfered from FLEX to PLASMID.");
            }
             //conn.rollback();
            DatabaseTransaction.commit(plasmid_connection);
        }
        catch(Exception e)
        {
            DatabaseTransaction.rollback(plasmid_connection);
             System.out.println(e.getMessage());
            m_error_messages.add("Can not upload new plates from files.\n"+e.getMessage());
        }
        finally
         {
            if ( plasmid_connection != null) DatabaseTransaction.closeConnection(plasmid_connection);
            sendEmails("New plates upload from FLEX to PLASMID","New plates upload  from FLEX to PLASMID");
         }
    }
    
    
    private void            writeSubmissionFiles(Connection  flex_connection) throws Exception
    {
        ArrayList<String>  clone_ids_for_sql =   prepareItemsListForSQL();
        // System.out.println(clone_ids_for_sql.size());
        if (clone_ids_for_sql == null || clone_ids_for_sql.size() < 1)
        {
             m_error_messages.add("No items submitted for transfer.");
             return;
        }
        String time_stamp = String.valueOf( System.currentTimeMillis());
             
        DataManager gr = new DataManager();
            
        HashMap<Integer, CloningStrategy> cl_str = gr.getCloningStrategies();
        boolean isFirstWrite=true;  
        
       
        for (String sql_item : clone_ids_for_sql)
        {
             ArrayList<edu.harvard.med.hip.flex.core.Container> containers=gr.getContainersInfo(sql_item,m_items_type);
             HashMap<String, edu.harvard.med.hip.flex.infoimport.coreobjectsforimport.ImportClone> clones =  gr.getCloneInfo(sql_item,m_items_type);
             HashMap<String, edu.harvard.med.hip.flex.infoimport.coreobjectsforimport.ImportFlexSequence> refsequence_info = gr.getReferenceSequenceInfo(sql_item,m_items_type);
             HashMap<String,ImportAuthor> authors = gr.getAuthors();
             
             if ( clones == null || clones.size() ==0)
             {
                 m_error_messages.add("No clones have been selected based on your criteria. Items " +m_items);
                 throw new Exception ("No clones have been selected based on your criteria.");
             }
             writeSubmissionFiles( cl_str, containers , 
                    clones, refsequence_info, authors,time_stamp, isFirstWrite);
             isFirstWrite=false;
        }
        //clean up files that were not filled in
        String file_name;FileForTransferDefinition fd;
         for ( PlasmIDFileType cur_file : PlasmIDFileType.values())
         {
                 //get file description
                 if (!cur_file.isCreate()) continue;
                 fd =   PlasmidProperties.getInstance().getFilePropertiesByType(cur_file.toString());
                 file_name= FlexProperties.getInstance().getProperty("tmp")+File.separator+fd.getFileName()+time_stamp+".txt";
                 File fcurrent = new File(file_name);
                 if (cur_file==PlasmIDFileType.PUBLICATION && ! i_isPublicationFile)m_attached_files.remove(fcurrent);
                if (cur_file==PlasmIDFileType.CLONEPUBLICATION && ! i_isClonePublication)m_attached_files.remove(fcurrent);
                if (cur_file==PlasmIDFileType.REFSEQNAME && !i_isRefseqName)m_attached_files.remove(fcurrent);
                if (cur_file==PlasmIDFileType.CLONEPROPERTY && ! i_isCloneProperty )m_attached_files.remove(fcurrent); 
                 if ( cur_file==PlasmIDFileType.CLONEHOST && !i_isCloneHost)m_attached_files.remove(fcurrent); 
         }
    }
    
  //called in cycle
     private void writeSubmissionFiles( 
              HashMap<Integer, CloningStrategy> cl_str,
            ArrayList<edu.harvard.med.hip.flex.core.Container> containers,
             HashMap<String, edu.harvard.med.hip.flex.infoimport.coreobjectsforimport.ImportClone> clones ,
             HashMap<String, edu.harvard.med.hip.flex.infoimport.coreobjectsforimport.ImportFlexSequence> refsequence_info ,
              HashMap<String, ImportAuthor> authors ,
             
             String time_stamp, boolean isFirstWrite) throws Exception
     {
          //String[][] data_for_files = new String[][];
            //get all authors & build connections
         
         //get all publications & build connections
         FileForTransferDefinition fd;BufferedWriter  input;
         String file_name;
         FlexProperties plasmidProps =  PlasmidProperties.getInstance(  );
         FlexProperties sysProps =  FlexProperties.getInstance(  );
         ((PlasmidProperties)plasmidProps).processProperties();
         for ( PlasmIDFileType cur_file : PlasmIDFileType.values())
         {
             //get file description
             if (!cur_file.isCreate()) continue;
             System.out.println(cur_file.toString());
             fd =   PlasmidProperties.getInstance().getFilePropertiesByType(cur_file.toString());
             file_name= sysProps.getInstance().getProperty("tmp")+fd.getFileName()+time_stamp+".txt";
             File fcurrent = new File(file_name);
             if (isFirstWrite) m_attached_files.add(fcurrent);
             try{input=new BufferedWriter(new FileWriter(fcurrent));}
             catch(Exception e){throw new Exception("Can not open file "+file_name+" for writing.");}
             if (isFirstWrite)
             {
                 
                 input.write(fd.getHeader()+FileForTransferDefinition.LINE_SEPARATOR);
                 input.write(fd.getColumnNames()+FileForTransferDefinition.LINE_SEPARATOR);input.flush();
             }
             writeFileContent( cl_str, authors,containers,clones ,refsequence_info ,   input, fd.getFileType());
             input.close();
        }
         
     }
    
     
     private void   writeFileContent(
      HashMap<Integer, CloningStrategy> cl_str,
        HashMap<String,ImportAuthor> authors,
          
            ArrayList<edu.harvard.med.hip.flex.core.Container> containers,
             HashMap<String, edu.harvard.med.hip.flex.infoimport.coreobjectsforimport.ImportClone> clones ,
             HashMap<String, edu.harvard.med.hip.flex.infoimport.coreobjectsforimport.ImportFlexSequence> refsequence_info ,
            BufferedWriter  input, PlasmIDFileType file_type) throws Exception
     {
         String property;
         try
         {
             switch(file_type)
             {
                 case  AUTHOR :  {writeAuthorFile(input,authors );input.flush();break;}
                 case CLONEAUTHOR:{writeCloneAuthorFile(input,clones.values(), authors);input.flush();break;}
                 case      PUBLICATION: {  writePublicationFile(input, clones.values()); break;                 }
                 case       CLONEPUBLICATION:{writeClonePublicationFile(input,clones.values());input.flush();break;}
 
                 case       CLONE:{ writeCloneFile(input,cl_str, clones.values(), refsequence_info);break;}
                 case       PLATE:   {    writePlateFile( input,  containers);     break;     }
                 case       REFSEQ:{ writeRefSequenceFile(input,refsequence_info.values() );break;}
//case INSERTREFSEQ:{break;}???????
                 case       REFSEQNAME:{writeRefSequenceNamesFile(input,refsequence_info.values() );break;}
                 case       CLONESELECTION:
                 { 
                     property=m_hosttype+"\t"+m_marker;
                     writeCloneStringPropertyFile(input,property,clones.values());
                     break;
                 }
                 case       CLONEGROWTH:
                 { 
                     if ( m_growthCondition1 != null) 
                     {   
                         property = (m_isRecomendedGC1) ?"Y":"N";
                         property=m_growthCondition1+"\t"+property;
                        writeCloneStringPropertyFile(input,property,clones.values());
                     }
                      if ( m_growthCondition2 != null) 
                     {   property = (m_isRecomendedGC2) ?"Y":"N";
                         property=m_growthCondition2+"\t"+property;
                        writeCloneStringPropertyFile(input,property,clones.values());}
                     break;
                 }
                 case     CLONEHOST :
                 { 
                     if ( m_hoststrain1 != null) 
                     {   
                         i_isCloneHost=true;
                         m_hoststrainDescription1 = (m_hoststrainDescription1==null  )? "NA":m_hoststrainDescription1;
                         property=m_hoststrain1+"\t"+m_hoststrainIsInUse1+"\t"+m_hoststrainDescription1;
                          writeCloneStringPropertyFile(input,property,clones.values());}
                      if ( m_hoststrain2 != null) 
                     {     
                          i_isCloneHost=true;
                          m_hoststrainDescription2 = (m_hoststrainDescription2==null)? "NA":m_hoststrainDescription2;
                          property=m_hoststrain2+"\t"+m_hoststrainIsInUse2+"\t"+m_hoststrainDescription2;
                           writeCloneStringPropertyFile(input,property,clones.values());
                      }
                     break;
                 }
                case     CLONENAME :   
                {  
                    writeCloneNameFile(input,clones.values());break;  
                }
                 case     CLONECOLLECTION :
                 {
                     for (int cc = 0; cc <  m_plasmid_collections.length; cc++)
                     {    writeCloneStringPropertyFile(input,(String) m_plasmid_collections[cc],clones.values());}
                     break;
                 }
                case     CLONEINSERT :{writeInsertFile(input,cl_str, clones.values(), refsequence_info);break;}
                 case     CLONEPROPERTY :
                 {
                      ObjectType objtype=new ObjectType();
         
                     String   temp=objtype.getParamValue("CLONE_PROPERTY.COLLECTIONS", m_flex_plasmid_submission_map,  null,null, null);
                     if (temp.equals("Y"))
                     {
                         for (int cc = 0; cc <  m_plasmid_collections.length; cc++)
                         {   
                             if ( !i_isCloneProperty)i_isCloneProperty=true;
                             property="Collection\t"+(String) m_plasmid_collections[cc]+"\tNA";
                             writeCloneStringPropertyFile(input, 	 property,clones.values());
                         }
                     }
                     writeClonePropertiesFile(input, clones.values());
                    
                     break;
                 }
  case     INSERTPROPERTY :
  {writeInsertPropertyFile(input, clones.values(),refsequence_info);break;}
                //case     CLONEINSERTONLY :{break;}


             }
         }
         catch(Exception e)
         {
             m_error_messages.add(e.getMessage());
             throw new Exception("Can not write file "+file_type.getDisplayTile()+e.getMessage());
         }
         
     }
     
     private void   writePlateFile(BufferedWriter input, 
           ArrayList<edu.harvard.med.hip.flex.core.Container> containers)
           throws Exception
     {
         Sample sample;
         for(Container container: containers)
         {
             for (int cc=0; cc< container.getSamples().size();cc++)
             {
                sample = (Sample)  container.getSamples().get(cc);
                if ( sample.getCloneid()>1 )
                {input.write(  container.getLabel()+"\t"+sample.getPosition()+"\t"+sample.getCloneid()+FileForTransferDefinition.LINE_SEPARATOR); }
             }
         }
         input.flush();
     }
     private void   writePublicationFile(BufferedWriter input, 
             Collection<edu.harvard.med.hip.flex.infoimport.coreobjectsforimport.ImportClone> clones)
            throws Exception
     {
          ImportPublication publication;
        ArrayList<ImportPublication> temp=new ArrayList<ImportPublication>();

         for (ImportClone clone: clones)
         {
             if ( clone.getPublications()== null )continue;
             {
                 for ( int cc=0; cc< clone.getPublications().size();cc++ )
                 {    
                     publication = (ImportPublication)clone.getPublications().get(cc);
                     if(! temp.contains(publication)) {  temp.add(publication);}
                 }
             }
         }
         
         // if no publications - delete file
         for(ImportPublication pub : temp)
         {
             i_isPublicationFile=true;
            input.write( pub.getPubMedID()+"\t"+pub.getTitle()+"\t"+pub.getPubMedID()+FileForTransferDefinition.LINE_SEPARATOR); 
         }
         input.flush();
                     
     }
     private void    writeRefSequenceFile(BufferedWriter input,
             Collection<ImportFlexSequence> refsequence_info )throws Exception
     {
      /*
refseqid
type - queried from plasmid , species based (SPECIESREFSEQTYPE
GENUSSPECIES 	REFSEQTYPE)
name - flex.name.genesymbol 
description - flex.name.description
cdsstart - 
cdsstop
species
sequence  */
         PublicInfoItem p_item;
         HashMap<String,FlexPlasmidMap> speciesmap=
                 m_flex_plasmid_maps.get( IMPORT_ACTIONS.CONNECT_PLASMID_FLEX_SPECIES_SUBMITTED.toString());
         String temp;String species;
         ObjectType objtype=new ObjectType();
         for (ImportFlexSequence item: refsequence_info)
         {
             //id
            input.write(item.getId()+"\t");
            //type
             temp=objtype.getParamValue("REFSEQUENCE.TYPE", m_flex_plasmid_submission_map,  item,null, speciesmap);
            input.write(  temp+"\t");
             //name
            temp=objtype.getParamValue("REFSEQUENCE.NAME", m_flex_plasmid_submission_map,  item,null, speciesmap);
            input.write( temp+"\t");
            //description
            temp=objtype.getParamValue("REFSEQUENCE.DESCRIPTION", m_flex_plasmid_submission_map,  item,null, speciesmap);
            input.write( temp+"\t");
            //cds start
            input.write(item.getCDSStart()+"\t");
            //cds stop
            input.write(item.getCDSStop()+"\t");
            //species
            temp=speciesmap.get(item.getSpesies()).getPlasmidName();
            input.write(temp+ "\t");
            //sequence
            //getSequencetext().substring(getCdsstart()-1,getCdsstop())
            temp=objtype.getParamValue("REFSEQUENCE.SEQUENCE", m_flex_plasmid_submission_map,  item,null, speciesmap);
            input.write( temp+FileForTransferDefinition.LINE_SEPARATOR);
          input.flush();
         }
         
     }
     
     private void    writeRefSequenceNamesFile(BufferedWriter input,
             Collection<ImportFlexSequence> refsequences )throws Exception
     {
      /*
       * Reference Sequence Name
refid	nametype	namevalue	nameurl

 */         String temp; 
       HashMap<String,FlexPlasmidMap> nametypemap=
                 m_flex_plasmid_maps.get( IMPORT_ACTIONS.CONNECT_PLASMID_FLEX_NAMETYPE_SUBMITTED.toString());
       String key= null;FlexPlasmidMap map;
       HashMap  <String,String> curnames= getRelevantFileEntries("REFSEQUENCE_NAME.");
       
       ObjectType objtype=new ObjectType();
        for (ImportFlexSequence cur_refsequence: refsequences)
        {
            for ( String writeitem: curnames.keySet() ) 
            {
               if ( ! i_isRefseqName) i_isRefseqName= true; 
              
               
              
                 map =nametypemap.get(writeitem);
                 key= (map != null) ? map.getValue1():"NA";
                 
               if ( map != null)
               {
                    //record
                      temp=objtype.getParamValue(curnames.get(writeitem), m_flex_plasmid_submission_map,  cur_refsequence,null, null);
                    if (temp ==null || temp.equals("NA"))
                        continue;
                      input.write(cur_refsequence.getId()+"\t");//refseqid
                    input.write(nametypemap.get(writeitem).getPlasmidName()+"\t");//property name
                   
                      input.write(  temp+"\t");
                      key=(key==null)?"NA":key+temp;
                    input.write(key+FileForTransferDefinition.LINE_SEPARATOR);//url
                    if (isDebug)input.flush();
               }
               
            }
       }
        input.flush();
     }
     
     
     private void   writeInsertFile(BufferedWriter input,
            HashMap<Integer, CloningStrategy> cl_str,
            Collection<ImportClone> clones,
             HashMap<String, ImportFlexSequence> refsequence_info 
          )
            throws Exception
     {
        ImportFlexSequence refsequence;
        String submission_param;// m_flex_plasmid_submission_map.get("CLONE.NAMETYPE");
        HashMap<String,FlexPlasmidMap> speciesmap=
                 m_flex_plasmid_maps.get( IMPORT_ACTIONS.CONNECT_PLASMID_FLEX_SPECIES_SUBMITTED.toString());
        HashMap<String,FlexPlasmidMap> vectorname_map=
                 m_flex_plasmid_maps.get( IMPORT_ACTIONS.CONNECT_PLASMID_FLEX_VECTOR_NAMES_SUBMITTED.toString());
       boolean isCloneSequenceExist = false;
        String temp;// = speciesmap.get( item.getSpesies() ).getPlasmidName();
           ObjectType objtype=new ObjectType();int counter=1;
           CloningStrategy cloningstrategy;
         for (ImportClone clone: clones)
        {
             isCloneSequenceExist = (clone.getCloneSequence() != null);
             refsequence = refsequence_info.get( PublicInfoItem.getPublicInfoByName("REFSEQUENCEID",clone.getPublicInfo()).getValue());
             cloningstrategy= cl_str.get(Integer.valueOf(clone.getCloningStrategyId()));
             //insertid id
              input.write( clone.getMaterCloneId()+"\t"); 
              //	insertorder	
              input.write( "1\t");
              if(isDebug)input.flush();
              //sizeinbp
              
          /*    int seq_size= (isCloneSequenceExist)?
                  clone.getCloneSequence().getCDSStop()-clone.getCloneSequence().getCDSStart()+1
                  : 0;*/
              int seq_size=refsequence.getCDSStop()-refsequence.getCDSStart()+1;
              input.write(seq_size+"\t"); if(isDebug)input.flush();
              //species	
              temp=speciesmap.get(refsequence.getSpesies()).getPlasmidName();
              input.write(temp+ "\t"); if(isDebug)input.flush();
              //format	
              temp=PublicInfoItem.getPublicInfoByName("FORMAT",clone.getPublicInfo()).getValue();
              if ( temp==null)temp="NA";
              input.write(  temp+"\t"); if(isDebug)input.flush();
              //source	
            temp=objtype.getParamValue("CLONE_INSERT.SOURCE", m_flex_plasmid_submission_map,  refsequence,clone, null);
            input.write(  temp+"\t"); if(isDebug)input.flush();
              //cloneid	
              input.write( clone.getId()+"\t");
              //sequence
               if (isCloneSequenceExist )
               {
                   int[] cds_indexes =getCdsStartCDSStopCloneSequence(clone, CLONE_INSERT_INDEX_MODE.MODE_CLONE_INSERT_SEQUENCE);
                   /*
                   int cds_start = clone.getCloneSequence().getCDSStart();
                   int cds_stop=clone.getCloneSequence().getCDSStop();
                    temp=objtype.getParamValue("CLONE_INSERT.UPSTREAM_BASES", m_flex_plasmid_submission_map,  null,clone, null);
                   int upstream_bases= Integer.parseInt(temp);
                     temp=objtype.getParamValue("CLONE_INSERT.DOWNSTREAM_BASES", m_flex_plasmid_submission_map,  null,clone, null);
                    int downstream_bases =Integer.parseInt(temp);
                   int begin_index=cds_start;
                   int stop_index=cds_stop;
                   if ( upstream_bases != 0 )
                   {begin_index = (cds_start <= upstream_bases) ? cds_start : cds_start-upstream_bases;}
                   temp=clone.getCloneSequence().getSequenceText();
                   if ( downstream_bases!= 0)
                   {stop_index=(cds_stop+downstream_bases <= temp.length() ) ? cds_start+downstream_bases : temp.length();}  
                   */
                   temp=clone.getCloneSequence().getSequenceText().substring(cds_indexes[0], cds_indexes[1]);
                   input.write(  temp+"\t"); if(isDebug)input.flush();
               }
               else input.write(  "NA\t");
              
              //geneid	
            temp=objtype.getParamValue("CLONE_INSERT.GENEID", m_flex_plasmid_submission_map, refsequence,clone, null);
            input.write(  temp+"\t"); if(isDebug)input.flush();
             //name	
            temp=objtype.getParamValue("CLONE_INSERT.NAME", m_flex_plasmid_submission_map,  refsequence,clone, null);
            input.write(  temp+"\t"); if(isDebug)input.flush();
             //description
            temp=objtype.getParamValue("CLONE_INSERT.DESCRIPTION", m_flex_plasmid_submission_map,  refsequence,clone, null);
            input.write(  temp+"\t"); if(isDebug)input.flush();
          //targetseqid
               temp=objtype.getParamValue("CLONE_INSERT.TARGETSEQUENCEID", m_flex_plasmid_submission_map,  refsequence,clone, null);
            input.write(  temp+"\t"); if(isDebug)input.flush();
             //targetgenbank
               temp=objtype.getParamValue("CLONE_INSERT.TARGETGENEBANK", m_flex_plasmid_submission_map,  refsequence,clone, null);
            input.write(  temp+"\t"); if(isDebug)input.flush();
        
              //hasdiscrepancy	
             temp=null;
             if ( isCloneSequenceExist )
             {temp= clone.getCloneSequence().getMutCDS(); temp= (temp == null) ? "No":"Yes";}
             else temp="NA";
              input.write(  temp+"\t"); if(isDebug)input.flush();
              //hasmutation
              PublicInfoItem pitem = PublicInfoItem.getPublicInfoByName("MUTATION_NT",refsequence.getPublicInfo()) ;
              if ( pitem==null)
              {
                  pitem=PublicInfoItem.getPublicInfoByName("MUTATION_AA",refsequence.getPublicInfo());
              }
              temp=( pitem == null)? "No":"Yes";
              input.write(  temp+"\t"); if(isDebug)input.flush();
        
              //refseqid
            input.write(  refsequence.getId()+"\t");
            //annotation
              temp=objtype.getParamValue("CLONE_INSERT.ANNOTATION", m_flex_plasmid_submission_map,  refsequence,clone, null);
            input.write(  temp+FileForTransferDefinition.LINE_SEPARATOR); if(isDebug)input.flush();
            if (isDebug) input.flush();

         }
         input.flush();
     }
     
     
     private int[]      getCdsStartCDSStopCloneSequence(ImportClone clone, CLONE_INSERT_INDEX_MODE mode)throws Exception
     {
          String temp;// = speciesmap.get( item.getSpesies() ).getPlasmidName();
           ObjectType objtype=new ObjectType();
           int cds_start = clone.getCloneSequence().getCDSStart();
           int cds_stop=clone.getCloneSequence().getCDSStop();
                  
          temp=objtype.getParamValue("CLONE_INSERT.UPSTREAM_BASES", m_flex_plasmid_submission_map,  null,clone, null);
           int upstream_bases= Integer.parseInt(temp);
             temp=objtype.getParamValue("CLONE_INSERT.DOWNSTREAM_BASES", m_flex_plasmid_submission_map,  null,clone, null);
            int downstream_bases =Integer.parseInt(temp);
            int[] ind = new int[2];
           switch(mode)
           { 
               case MODE_CLONE_INSERT_SEQUENCE:
               {
                    int begin_index=cds_start;
                   int stop_index=cds_stop;
                   if ( upstream_bases != 0 )
                   {begin_index = (cds_start <= upstream_bases) ? cds_start : cds_start-upstream_bases;}
                   temp=clone.getCloneSequence().getSequenceText();
                   if ( downstream_bases!= 0)
                   {stop_index=(cds_stop+downstream_bases <= temp.length() ) ? cds_start+downstream_bases : temp.length();}  
                   
                    ind[0]=begin_index;ind[1]=--stop_index;
                     break;
                }
                case MODE_CLONE_INSERT_PROPERTY:
                {
                    if ( upstream_bases == 0 )
                    {ind[0]=1;}
                    else {ind[0]=  (cds_start <= upstream_bases) ? cds_start : upstream_bases;}
                  
                    if (downstream_bases==0){ind[1]=cds_stop-cds_start+1;}
                    else {ind[1]=ind[0]+cds_stop-cds_start+1;}
                    break;
                }
           }
           
           
           
           return ind;     
     }
    private void   writeCloneFile(BufferedWriter input,
            HashMap<Integer, CloningStrategy> cl_str,
            Collection<edu.harvard.med.hip.flex.infoimport.coreobjectsforimport.ImportClone> clones,
             HashMap<String, edu.harvard.med.hip.flex.infoimport.coreobjectsforimport.ImportFlexSequence> refsequence_info 
          )
            throws Exception
     {
      /*  /*
clonename - FLEX cloneid
        clonetype
        verified -"Y"	
        vermethod-"Sequence Verification"
        domain - species name (map for flex-plasmid)
        subdomain - NA
        restriction -plate level from UI
        clonemapfilename - NA
        comments - Batch based
        vectorname - vector name (map for flex-plasmid)
        status - 'NOT AVAILABLE'
        specialtreatment - Batch based
        source - Batch based
        description - Batch based
*/
       
        ImportFlexSequence refsequence;
        String submission_param;// m_flex_plasmid_submission_map.get("CLONE.NAMETYPE");
        HashMap<String,FlexPlasmidMap> speciesmap=
                 m_flex_plasmid_maps.get( IMPORT_ACTIONS.CONNECT_PLASMID_FLEX_SPECIES_SUBMITTED.toString());
        HashMap<String,FlexPlasmidMap> vectorname_map=
                 m_flex_plasmid_maps.get( IMPORT_ACTIONS.CONNECT_PLASMID_FLEX_VECTOR_NAMES_SUBMITTED.toString());
       
        String temp;// = speciesmap.get( item.getSpesies() ).getPlasmidName();
           ObjectType objtype=new ObjectType();
           CloningStrategy cloningstrategy;
         for (edu.harvard.med.hip.flex.infoimport.coreobjectsforimport.ImportClone clone: clones)
        {
             refsequence = refsequence_info.get( PublicInfoItem.getPublicInfoByName("REFSEQUENCEID",clone.getPublicInfo()).getValue());
             cloningstrategy= cl_str.get(Integer.valueOf(clone.getCloningStrategyId()));
             //clone id
              input.write( clone.getId()+"\t");
              
                //clone type
              temp=objtype.getParamValue("CLONE.CLONETYPE", m_flex_plasmid_submission_map,  refsequence,clone, speciesmap);
              input.write(  temp+"\t");
              //verified
              temp=objtype.getParamValue("CLONE.VERIFIED", m_flex_plasmid_submission_map, null, null, null);
              input.write(  temp+"\t");
              //vermethod
                temp=objtype.getParamValue("CLONE.VERMETHOD", m_flex_plasmid_submission_map, null, null, null);
              input.write(  temp+"\t");
              //domain
                temp=objtype.getParamValue("CLONE.DOMAIN", m_flex_plasmid_submission_map,  refsequence,clone, speciesmap);
              
                input.write(  temp+"\t");
            
              //subdomain
              temp=objtype.getParamValue("CLONE.SUBDOMAIN", m_flex_plasmid_submission_map, null, null, null);
              input.write(  temp+"\t");
           
             // restriction -plate level from UI
               input.write(  m_restriction+"\t");
        
             
              //clonemapfilename
              temp=objtype.getParamValue("CLONE.CLONEMAPFILENAME", m_flex_plasmid_submission_map, null, null, null);
              input.write(  temp+"\t");
           
             // comments - Batch based
              temp=objtype.getParamValue("CLONE.COMMENT", m_flex_plasmid_submission_map,  refsequence,clone, speciesmap);
              input.write(  temp+"\t");
      // vectorname - vector name (map for flex-plasmid)
              temp= cloningstrategy.getClonevector().getName();
              if (vectorname_map.get(temp)==null)
              {
                  m_error_messages.add("Vector not mapped "+vectorname_map.get(temp));
              }
              temp=vectorname_map.get(temp).getPlasmidName();
              input.write(temp+"\t");
                  
                      //status
              temp=objtype.getParamValue("CLONE.STATUS", m_flex_plasmid_submission_map, null, null, null);
              input.write(  temp+"\t");
           
            //specialtreatment
               temp=objtype.getParamValue("CLONE.SPECIALTREATMENT", m_flex_plasmid_submission_map, null, null, null);
              input.write(  temp+"\t");
         
            //source
               temp=objtype.getParamValue("CLONE.SOURCE", m_flex_plasmid_submission_map,  refsequence,clone, speciesmap);
              input.write(  temp+"\t");
             
              //description
              temp=objtype.getParamValue("CLONE.DESCRIPTION", m_flex_plasmid_submission_map,  refsequence,clone, speciesmap);
              input.write(  temp+FileForTransferDefinition.LINE_SEPARATOR);
             input.flush();
          }
        
     }
             
//cloneid	propertytype	propertyvalue	Extrainfo

     private void   writeClonePropertiesFile(BufferedWriter input,
            Collection<edu.harvard.med.hip.flex.infoimport.coreobjectsforimport.ImportClone> clones)
            throws Exception
     {
       String temp; 
       HashMap<String,FlexPlasmidMap> nametypemap=
                 m_flex_plasmid_maps.get( IMPORT_ACTIONS.CONNECT_PLASMID_FLEX_NAMETYPE.toString());
       String key; String value;
       String[] key_values;  
       HashMap  <String,String> curnames= getRelevantFileEntries("CLONE_PROPERTY.");
       //bbbb
        for ( String writeitem: curnames.keySet() ) 
            {
            if (writeitem.equals("COLLECTIONS"))curnames.remove(writeitem);
        }
       ObjectType objtype=new ObjectType();
        for (ImportClone cur_clone: clones)
        {
            for ( String writeitem: curnames.keySet() ) 
            {
                if (!i_isCloneProperty)i_isCloneProperty=true;
                input.write(cur_clone.getId()+"\t");//refseqid
                input.write(nametypemap.get(writeitem).getPlasmidName()+"\t");//property name
                 temp=objtype.getParamValue(curnames.get(writeitem), m_flex_plasmid_submission_map, null, cur_clone, null);
                 input.write(  temp+"\t");
           
                input.write(nametypemap.get(writeitem).getValue1()+temp+FileForTransferDefinition.LINE_SEPARATOR);//url
            }
       }
       input.flush();
     }
     //write clone names file
     private void   writeCloneNameFile(BufferedWriter input,
            Collection<edu.harvard.med.hip.flex.infoimport.coreobjectsforimport.ImportClone> clones)
            throws Exception
    {
          /* Clone Name
//cloneid	nametype	namevalue	nameurl
9508	HIP Clone ID	9508	http://kotel.harvard.edu:8080/FLEX/ViewClone.do?cloneid=9508&isDisplay=1
116503	HIP Clone Name	FLH116503.01X	NA*/
         PublicInfoItem p_info;
         HashMap<String,FlexPlasmidMap> nametypemap=
                 m_flex_plasmid_maps.get( IMPORT_ACTIONS.CONNECT_PLASMID_FLEX_NAMETYPE.toString());
        String temp; ObjectType objtype=new ObjectType();
        String flex_link= FlexProperties.getInstance().getProperty("FLEX_LOCATION");//&isDisplay=1";
        try
        {
            HashMap  <String,String> curnames= getRelevantFileEntries("CLONE_NAME.");
       
            for (ImportClone clone: clones)
            {
                input.write( clone.getId()+"\tHIP Clone ID\t"+clone.getId()+"\t"+flex_link+clone.getId()+"&isDisplay=1"+FileForTransferDefinition.LINE_SEPARATOR);
                input.write( clone.getId()+"\tHIP Master Clone ID\t"+clone.getMaterCloneId()+"\tNA"+FileForTransferDefinition.LINE_SEPARATOR);

                temp=objtype.getParamValue("CLONE_NAME.ORIGINAL_CLONE_ID", m_flex_plasmid_submission_map,  null,clone, null);
                if (temp != null)
                {    input.write( clone.getId()+"\tOriginal Clone ID\t"+temp+"\tNA"+FileForTransferDefinition.LINE_SEPARATOR);}
                if(isDebug)input.flush();
                if ( curnames!= null)
                {
                 for ( String writeitem: curnames.keySet() ) 
                {
                    if (writeitem.equals("ORIGINAL_CLONE_ID"))continue;
                    input.write(clone.getId()+"\t");//refseqid
                    input.write(nametypemap.get(writeitem).getPlasmidName()+"\t");//property name
                     temp=objtype.getParamValue(curnames.get(writeitem), m_flex_plasmid_submission_map,  null, clone,null);
                     input.write(  temp+"\t");

                    input.write(nametypemap.get(writeitem).getValue1()+temp+FileForTransferDefinition.LINE_SEPARATOR);//url
                }}
            
            }
        input.flush();
        }catch(Exception e)
        {
            throw new Exception(e.getMessage());
        }

    }
     //gets all entries for specific file from batch submission map
     private HashMap<String,String>     getRelevantFileEntries(String file_name)throws Exception
     {
        HashMap<String,String> curnames =  new HashMap<String,String>();
        String key; String value;String[] key_values;
        for ( Iterator<String> iter = m_flex_plasmid_submission_map.keySet().iterator(); 
                iter.hasNext(); ) 
       {
            key= iter.next() ;
            if (key.startsWith(file_name))
            {
                 value = m_flex_plasmid_submission_map.get(key);
                 //expected format REFSEQUENCE_NAME.XX=REFSEQUENCE(or CLONE).NAME.YYY
                 key_values = key.split("\\.");
                 if ( key_values.length != 2)
                     throw new  Exception ("Check batch file: item: "+key+", expected format REFSEQUENCE_NAME.XX=REFSEQUENCE(or CLONE).NAME.YYY");
                 //curnames.put(key_values[1], value);
                  curnames.put(key_values[1], key);
                
            }
       }
        return curnames;
     }

    
     //write file in format cloneID\tproperty\n
    private void    writeCloneStringPropertyFile(BufferedWriter input,
            String property, Collection<edu.harvard.med.hip.flex.infoimport.coreobjectsforimport.ImportClone> clones)
            throws Exception
    {
        for (edu.harvard.med.hip.flex.infoimport.coreobjectsforimport.ImportClone clone: clones)
        {
            input.write( clone.getId()+"\t"+property+FileForTransferDefinition.LINE_SEPARATOR);
        }
        input.flush();
    }
     
     private boolean readAllFiles(String filepath,  ArrayList tables )
     
    {
        return true;
    }
    
  
    
    public  Connection        getPLASMIDConnection()throws Exception
    {
   //get all reference sequences from bec: to prevent multipal upload of the same sequence
        //Hashtable refsequences = getAllRefSequences(m_bec_connection);
         Connection  plasmid_connection = null; 
          try
        {
             
             String plasmid_url =    FlexProperties.getInstance().getProperty("PLASMID_URL") ;
             String plasmid_username =          FlexProperties.getInstance().getProperty("PLASMID_USERNAME"); 
             String plasmid_password =          FlexProperties.getInstance().getProperty("PLASMID_PASSWORD");
        //     System.out.println(plasmid_url+":"+plasmid_username+":"+plasmid_password);
             
             plasmid_connection = DatabaseTransactionLocal.getInstance(
                    plasmid_url ,    plasmid_username,  plasmid_password).requestConnection();
             return plasmid_connection;
        }
        catch(Exception e)
        {
            m_error_messages.add(e.getMessage());
            throw new Exception("Cannot open connection to PLASMID database "+e.getMessage());
       
        }
    }
    private void writeClonePublicationFile(BufferedWriter input,
            Collection<edu.harvard.med.hip.flex.infoimport.coreobjectsforimport.ImportClone> clones)
            throws Exception
    { 
        ImportPublication publication;
         for (edu.harvard.med.hip.flex.infoimport.coreobjectsforimport.ImportClone clone: clones)
         {
             if ( clone.getPublications() == null) continue;
             if (! i_isClonePublication) i_isClonePublication=true;
             for ( int cc=0; cc< clone.getPublications().size();cc++ )
             {    
                 publication = (ImportPublication)clone.getPublications().get(cc);
                 input.write( clone.getId()+"\t"+publication.getId()+FileForTransferDefinition.LINE_SEPARATOR); 
             }
         }
}
    //authorname	firstname	lastname	tel	fax	authoremail	address	www	description	
    private void writeCloneAuthorFile(BufferedWriter input,
            Collection<edu.harvard.med.hip.flex.infoimport.coreobjectsforimport.ImportClone> clones,
            HashMap<String,ImportAuthor> authors)
            throws Exception
    {                //308229	Joint Center for Structural Genomics	PSI Center
        ImportAuthor author; 
        ArrayList<String> author_from_batchfile=new ArrayList<String>();
        String tmp;String tmp1; ObjectType objtype=new ObjectType();
        boolean isAtLeastOneAuthor=false;
        for (int cc = 1; cc< 10; cc++)
        {
            tmp = objtype.getParamValue("CLONE_AUTHOR.AUTHOR"+cc, m_flex_plasmid_submission_map,  null,null, null,false);
            if (tmp==null ) break;
            tmp1=objtype.getParamValue("CLONE_AUTHOR.AUTHORTYPE"+cc, m_flex_plasmid_submission_map,  null,null, null,false);
            if (tmp1 == null) throw new Exception ("Error in batch submission file: parameter CLONEAUTHOR.AUTHORTYPE"+cc+" is not provided");
            author_from_batchfile.add(tmp +"\t" + tmp1);
        }
        HashMap<String,FlexPlasmidMap> author_map=
                 m_flex_plasmid_maps.get( IMPORT_ACTIONS.CONNECT_PLASMID_FLEX_AUTHOR_SUBMITTED.toString());
       HashMap<String,FlexPlasmidMap> author_type=
                 m_flex_plasmid_maps.get( IMPORT_ACTIONS.CONNECT_PLASMID_FLEX_AUTHOR_TYPE_SUBMITTED.toString());
       
            
        for (edu.harvard.med.hip.flex.infoimport.coreobjectsforimport.ImportClone clone: clones)
         {
            if (clone.getAuthors() != null && clone.getAuthors().size() > 0)
            {
                if (authors == null || authors.size() == 0)
                         throw new Exception ("Authors collection is empty");
                isAtLeastOneAuthor=true; 
             
                for ( int cc=0; cc< clone.getAuthors().size();cc++ )
                {    
                     author = authors.get(clone.getAuthors());
                     if (author == null)
                         throw new Exception ("Can not find author for clone "+clone.getId());
                     try
                     {
                       input.write( clone.getId()+"\t"+
                             author_map.get(author.getName()).getPlasmidName()+"\t"+
                             author_type.get(author.getType()).getPlasmidName()+FileForTransferDefinition.LINE_SEPARATOR); 
                 
                     }catch(Exception e)
                     { throw new Exception ("Author data are not mapped: " + author.getName() +", "+author.getType() );         }
              
                }
            }
             for ( String curauthor : author_from_batchfile )
             {    
                 isAtLeastOneAuthor=true;
                 input.write( clone.getId()+"\t"+curauthor+FileForTransferDefinition.LINE_SEPARATOR); 
             }
             if (!isAtLeastOneAuthor) 
                    throw new Exception("There is no information to write clone author file.");
        }
         
    }
    private void writeAuthorFile(BufferedWriter input,
            HashMap<String,ImportAuthor> authors)
            throws Exception
    {
         if ( authors== null || authors.size()==0)return;
          HashMap<String,FlexPlasmidMap> author_map=
                 m_flex_plasmid_maps.get( IMPORT_ACTIONS.CONNECT_PLASMID_FLEX_AUTHOR_SUBMITTED.toString());
       
         for (ImportAuthor author: authors.values())
         {
             input.write(author_map.get(author.getName()).getPlasmidName()+"\t");//property name
                  
             input.write( author.getFNName()+"\t"+
                         author.getFLName()+"\t"+author.getTel()+"\t"+
                         author.getFax()+"\t"+author.getEMail()+"\t"+
                         author.getAdress()+"\t"+author.getWWW()+"\t"+
                           author.getDescription() +FileForTransferDefinition.LINE_SEPARATOR); 
          
         }
    }
    
    
    private void   writeInsertPropertyFile(BufferedWriter input,
             Collection<edu.harvard.med.hip.flex.infoimport.coreobjectsforimport.ImportClone> clones,
             HashMap<String, edu.harvard.med.hip.flex.infoimport.coreobjectsforimport.ImportFlexSequence> refsequence_info 
          )
            throws Exception
     {
      /*   insertid	propertytype	propertyvalue	extrainfo

*/          String temp;ImportFlexSequence refsequence;
          for (ImportClone cur_clone: clones)
	  {
                //sequence
                if ( cur_clone.getCloneSequence() != null )
                {
                   int[] cds_indexes =getCdsStartCDSStopCloneSequence(cur_clone, CLONE_INSERT_INDEX_MODE.MODE_CLONE_INSERT_PROPERTY);
                   input.write(cur_clone.getMaterCloneId()+"\t");//refseqid
                   input.write("CDS Start\t");
                   input.write(cds_indexes[0]+"\tNA"+FileForTransferDefinition.LINE_SEPARATOR);

                    input.write(cur_clone.getMaterCloneId()+"\t");//refseqid
                    input.write("CDS Stop\t");
                    input.write(cds_indexes[1]+"\tNA"+FileForTransferDefinition.LINE_SEPARATOR);
                    if(isDebug) input.flush();
                    //hasdiscrepancy	
                    temp= cur_clone.getCloneSequence().getMutCDS();
                    if ( temp != null )
                    {
                        input.write(cur_clone.getMaterCloneId()+"\t");//refseqid
                       // input.write("Yes\t");
                       // input.write(temp+"\t");
                       // input.write("No"+FileForTransferDefinition.LINE_SEPARATOR);
                        input.write("Discrepancy\t");
                        input.write(temp+"\tNA"+FileForTransferDefinition.LINE_SEPARATOR);
                        if(isDebug) input.flush();
                     }
                  //  else
                    //    input.write("No"+FileForTransferDefinition.LINE_SEPARATOR);
                }
                //hasmutation
                
                 String refseqid = PublicInfoItem.getPublicInfoByName("REFSEQUENCEID",cur_clone.getPublicInfo()).getValue();
                 refsequence = refsequence_info.get( refseqid);
               
                PublicInfoItem item =   PublicInfoItem.getPublicInfoByName("MUTATION_NT",refsequence.getPublicInfo());
                if ( item != null )temp=item.getValue();
                else { item=PublicInfoItem.getPublicInfoByName("MUTATION_AA",refsequence.getPublicInfo());
                 temp = (item == null) ? null : item.getValue();}
                if (temp != null)
                {
                    input.write(cur_clone.getMaterCloneId()+"\t");//refseqid
                    input.write("Mutation\t");
                    input.write(temp+"\t");
                    input.write("NA"+FileForTransferDefinition.LINE_SEPARATOR);
                    if(isDebug) input.flush();
                }
                  
	}
       input.flush();
     }
            
      
   //----------------------------------------------------------------- 
    
    public static void main(String args[]) 
    {
         try
        {/*
             String name="C:\\Projects\\FLEX\\flex\\src\\edu\\harvard\\med\\hip\\flex\\infoimport\\plasmidimport\\BatchSubmissionParameters.txt";
            InputStream batchParams =  new FileInputStream( name);
             FLEXtoPLASMIDImporter runner =  new FLEXtoPLASMIDImporter();
            HashMap<String,ArrayList<String>>   tt=
                    runner.readSubmissionSpecificParameters(batchParams);
              FlexProperties pp =  FlexProperties.getInstance();
       
              FLEXtoPLASMIDImporter pi = new FLEXtoPLASMIDImporter();
               Connection plasmid_connection=  pi.getPLASMIDConnection();
                      
             
         HashMap<String, HashMap<String,FlexPlasmidMap>> maps=
                    loadMapFLEX_To_PLASMID_Maps(plasmid_connection);
        runner.setInputData(ConstantsImport.ITEM_TYPE.ITEM_TYPE_FILE_PATH, " 154245 ");//154431  154512  154516  154520  154454  154245  154532  154269");
            //  runner.setContainerLabels(master_container_labels );
        runner.setProcessType( ConstantsImport.PROCESS_NTYPE.TRANSFER_FLEX_TO_PLASMID_IMPORT );
       // Spec spec_f = Spec.getSpecById(91, Spec.FULL_SEQ_SPEC_INT);
        User user = new User("htaycher", "hip_informatics@hms.harvard.edu", "");
    
        runner.setUser( user);
          runner.run();
       */ }catch(Exception e){}
        System.exit(0);
    }
    
}
