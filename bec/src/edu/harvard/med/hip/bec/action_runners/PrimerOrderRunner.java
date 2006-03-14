/*
 * PrimerOrderRunner.java
 *
 * Created on November 18, 2003, 12:57 PM
 */

package edu.harvard.med.hip.bec.action_runners;


import java.sql.*;
import java.io.*;

import edu.harvard.med.hip.bec.file.*;
import  edu.harvard.med.hip.bec.programs.primer3.*;
import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.programs.needle.*;
import edu.harvard.med.hip.bec.coreobjects.sequence.*;

import edu.harvard.med.hip.bec.coreobjects.endreads.*;
import edu.harvard.med.hip.bec.coreobjects.oligo.*;
import edu.harvard.med.hip.bec.coreobjects.feature.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.form.*;
import edu.harvard.med.hip.bec.user.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.Constants;
import edu.harvard.med.hip.bec.sampletracking.mapping.*;
import edu.harvard.med.hip.bec.sampletracking.objects.*;
import edu.harvard.med.hip.bec.programs.assembler.*;
import edu.harvard.med.hip.bec.util_objects.*;
import edu.harvard.med.hip.bec.ui_objects.*;
  import java.util.*;
  import edu.harvard.med.hip.utility.*;

/**
 *
 * @author  HTaycher
 */
public class PrimerOrderRunner extends ProcessRunner 
{
    public static final int          PLACEMENT_FORMAT_ALL_TOGETHER = 1;
    public static final int          PLACEMENT_FORMAT_N_PRIMER = 2;
    
    public static final int          OLIGO_SELECTION_FORMAT_STRETCH_COOLECTION_ONLY = 1;
    public static final int          OLIGO_SELECTION_FORMAT_REFSEQ_ONLY = 2;
    public static final int          OLIGO_SELECTION_FORMAT_STRETCH_COLLECTION_REFSEQ = 3;

    private static final int           CLONE_NOT_PROCESSED = 0;
    private static final int           CLONE_PROCESSED = 1;

    private boolean                 m_isTryMode = false;
    private int                     m_primer_placement_format = PLACEMENT_FORMAT_ALL_TOGETHER;
    private int                     m_primer_number = -1;
    private int                     m_wells_per_plate = 96;
    private int                     m_first_well = 1;
    private int                     m_last_well = m_wells_per_plate;
    private int                     m_primers_selection_rule = OLIGO_SELECTION_FORMAT_REFSEQ_ONLY;
    private boolean                m_is_full_plates = false;
      
    // for oligo order file (sent to company
    
    //-----------------------------------------------------------------
    
    //goal to provide file with n columns, column can be empty or be one out of 5 data types.
    // empty well filled by rules declared by two params (empty sequence..)
    public static final int           ONE_FILE_PER_ORDER = 0;
    public static final int           ONE_FILE_PER_PLATE = 1;
 public static final int           NO_ORDER_FILES = 2;

    private int                     m_primer_sequence = 0;
     private int                     m_primer_name= 0;

    private int                     m_primer_column= 0;
    private int                     m_primer_row= 0;
    private int                     m_plate_name= 0;
    private String                     m_empty_sequence= null;
    private String                     m_empty_sequence_name = null;
    private int                     m_number_of_files = NO_ORDER_FILES;
    
    private int[]                   i_print_array_for_order_file = null;
    
    private static final int        DATA_TYPE_PLATE_LABEL = 0;
    private static final int        DATA_TYPE_PRIMER_SEQUENCE = 1;
    private static final int        DATA_TYPE_PRIMER_NAME = 2;
    private static final int        DATA_TYPE_PRIMER_COLUMN = 3;
    private static final int        DATA_TYPE_PRIMER_ROW = 4;
    private static final int        DATA_TYPE_NO_DATA = 5;
      
     
    //--------------------
    
    
      //prepared statments
    private   PreparedStatement m_pst_insert_process_object = null;
    private  PreparedStatement m_pst_update_primer_status = null;
    private   PreparedStatement m_pst_insert_oligo_sample = null;
    private   PreparedStatement m_pst_insert_oligo_plate = null;
   
    public void         setIsTryMode(boolean isTryMode){m_isTryMode=isTryMode;}
    public void         setPrimerNumber(int v){ m_primer_number = v;}
    public void         setPrimerPlacementFormat(int v){ m_primer_placement_format = v;}
    public void         setNumberOfWellsOnPlate(int v){ m_wells_per_plate = v;}
    public void         isFullPlatesOnly(boolean v){ m_is_full_plates =v;}
    public void         setPrimersSelectionRule(int v){ m_primers_selection_rule = v;}
    
    public void         setFirstWell(int v){m_first_well = v;}
    public void         setLastWell(int v){ m_last_well = v;}
   
    //--------------------
     // for oligo order file (sent to company
    public void         setPrimerSequenceColumn (int v){m_primer_sequence = v;}
    public void         setPrimerNameColumn  (int v){m_primer_name= v;}

   public void         setPrimerColumnColumn (int v){m_primer_column= v;}
    public void         setPrimerowColumn  (int v){m_primer_row= v;}
    public void         setPlateNameColumn (int v){m_plate_name= v;}
    public void         setEmptySequenceDisplay  (String v){m_empty_sequence= v;}
    public void         setEmptySequenceName  (String v){m_empty_sequence_name = v;}
    public void         setNumberOfOrderFiles  (int v){m_number_of_files = v;}

    //--------------------
    
    
    
    public String       getTitle()    { return "Request for primer order";}
    
    public void run_process()
    {
        Connection conn = null;
         try
        {
            conn = DatabaseTransaction.getInstance().requestConnection();
            //prepare array for order file
            if (  m_number_of_files != NO_ORDER_FILES ) preparePrintArrayForOrderFile();
           // pst_get_clone_primers = conn.prepareStatement("");
              int process_id = Request.createProcessHistory( conn, ProcessDefinition.RUN_OLIGO_ORDER, new ArrayList(),m_user) ;
             createPreparedStatements( process_id, conn);
              ArrayList clone_ids = Algorithms.splitString( m_items);
            ArrayList  clone_description =     getCloneDescriptions(clone_ids);
            if ( clone_description == null || clone_description.size() <1 ) return;
          //  ArrayList sorted_clone_description = sortCloneDescriptions(clone_description);
             ArrayList sorted_clone_description = clone_description;
            processClones(sorted_clone_description,m_wells_per_plate,   conn);
           // if ( m_isTryMode ) DatabaseTransaction.rollback(conn);
        }
        catch(Exception e)
        {
            DatabaseTransaction.rollback(conn);
            m_error_messages.add(e.getMessage());
        }
        finally
        {
            if(conn != null)            DatabaseTransaction.closeConnection(conn);
            sendEMails( getTitle() );
        }
    }
    
    
    
    //---------------------------private ------------------------------
    // build int[] where size of array declared by max number of the column asked by user.
    // array member value declares data type
    
    private void preparePrintArrayForOrderFile()
    {
        // get number of columns
       int column_number =  Math.max(  m_primer_sequence, Math.max( m_primer_name, Math.max(   m_primer_column,  Math.max(    m_primer_row    ,  m_plate_name))));
       i_print_array_for_order_file = new int[column_number];
       for ( int col = 0; col < i_print_array_for_order_file.length; col++)
       {
            if ( col + 1 ==  m_plate_name){     i_print_array_for_order_file[col]= DATA_TYPE_PLATE_LABEL  ; continue;}
            if ( col + 1 == m_primer_sequence )     {  i_print_array_for_order_file[col]= DATA_TYPE_PRIMER_SEQUENCE ; continue;}
            if ( col + 1 == m_primer_name) {  i_print_array_for_order_file[col]=   DATA_TYPE_PRIMER_NAME  ; continue;}
            if ( col + 1 ==  m_primer_column)  {  i_print_array_for_order_file[col]= DATA_TYPE_PRIMER_COLUMN  ; continue;}
            if ( col + 1 == m_primer_row)    {  i_print_array_for_order_file[col]=  DATA_TYPE_PRIMER_ROW  ; continue;}
            i_print_array_for_order_file[col]=  DATA_TYPE_NO_DATA;
       }
    }
    
    private String  createOrderFileEntry(int oligosample_id , String plate_label, 
                                        int well , String oligo_sequence  )
    {
         StringBuffer temp = new StringBuffer();
         String well_name = null;
        for (int column_count = 0; column_count < i_print_array_for_order_file.length; column_count++)
        {
            if ( i_print_array_for_order_file[column_count]== DATA_TYPE_PLATE_LABEL  ){temp.append(plate_label + Constants.TAB_DELIMETER); continue;}
            if (   i_print_array_for_order_file[column_count]== DATA_TYPE_PRIMER_SEQUENCE ) {temp.append(oligo_sequence + Constants.TAB_DELIMETER); continue;}
            if (   i_print_array_for_order_file[column_count]==   DATA_TYPE_PRIMER_NAME  ){temp.append(oligosample_id + Constants.TAB_DELIMETER); continue;}
            well_name = edu.harvard.med.hip.bec.sampletracking.objects.Container.convertPositionFrom_int_to_alphanumeric(well );
            if (   i_print_array_for_order_file[column_count]== DATA_TYPE_PRIMER_COLUMN  ){temp.append(well_name.substring(0,1) + Constants.TAB_DELIMETER); continue;}
            if (   i_print_array_for_order_file[column_count]==  DATA_TYPE_PRIMER_ROW  ){temp.append(well_name.substring(1) + Constants.TAB_DELIMETER); continue;}
            temp.append( Constants.TAB_DELIMETER);
        }
        
        return temp.toString();
    }
    
    
    private void            fillEmptyWellsForOrderFile(ArrayList order_file_entries, String plate_label)
    {
        StringBuffer temp = null;
        String well_name = null;
        if ( i_print_array_for_order_file == null || i_print_array_for_order_file.length < 1) return;
        for (int well = 1 ; well < m_first_well ; well++)  
        {
            order_file_entries.add(well-1,  emptyWellEntryForOrderFile(  plate_label,  well ));
            
        }
        for (int well = order_file_entries.size() + 1 ; well <= m_wells_per_plate; well++)  
        {
           order_file_entries.add( emptyWellEntryForOrderFile(  plate_label,  well ));
        }
    }
    
     private String            emptyWellEntryForOrderFile( String plate_label, int well)
    {
        String well_name = null;
        StringBuffer temp = new StringBuffer();
        for (int column_count = 0; column_count < i_print_array_for_order_file.length; column_count++)
        {
            if ( i_print_array_for_order_file[column_count]== DATA_TYPE_PLATE_LABEL  ){temp.append(plate_label + Constants.TAB_DELIMETER); continue;}
            if (   i_print_array_for_order_file[column_count]== DATA_TYPE_PRIMER_SEQUENCE ) {temp.append(m_empty_sequence + Constants.TAB_DELIMETER); continue;}
            if (   i_print_array_for_order_file[column_count]==   DATA_TYPE_PRIMER_NAME  ){temp.append(m_empty_sequence_name + Constants.TAB_DELIMETER); continue;}
            well_name = edu.harvard.med.hip.bec.sampletracking.objects.Container.convertPositionFrom_int_to_alphanumeric(well );
            if (   i_print_array_for_order_file[column_count]== DATA_TYPE_PRIMER_COLUMN  ){temp.append(well_name.substring(0,1) + Constants.TAB_DELIMETER); continue;}
            if (   i_print_array_for_order_file[column_count]==  DATA_TYPE_PRIMER_ROW  ){temp.append(well_name.substring(1) + Constants.TAB_DELIMETER); continue;}
            temp.append( Constants.TAB_DELIMETER);
        }
        return temp.toString();
        
    }
    //----------|----------------------------|---------------------------|--------------------
    private ArrayList           getCloneDescriptions(ArrayList clone_ids)
    {
        ArrayList result = new ArrayList();
        CloneDescription clone_description = null;
        String sql = "select flexsequenceid, flexcloneid as cloneid,c.refsequenceid as becrefsequenceid, "
        +" flexsequencingplateid as flexcontainerid,position "
        +" from flexinfo f, sequencingconstruct c, isolatetracking i, sample s"
        +" where f.isolatetrackingid=i.isolatetrackingid and c.constructid=i.constructid and "
        +" i.sampleid=s.sampleid and flexcloneid  in ("+Algorithms.convertStringArrayToString(clone_ids,"," ) +")";
        ResultSet rs = null;
       try
        {
           rs = DatabaseTransaction.getInstance().executeQuery(sql);
            while(rs.next())
            {
                clone_description = new CloneDescription();
                clone_description.setFlexSequenceId  ( rs.getInt("flexsequenceid") );
                clone_description.setCloneId (  rs.getInt("cloneid") );
                clone_description.setBecRefSequenceId ( rs.getInt("becrefsequenceid") );
                clone_description.setContainerId( rs.getInt("flexcontainerid") );
                clone_description.setPosition( rs.getInt("position") );
                clone_description.setIsolateStatus(CLONE_NOT_PROCESSED);
                result.add(clone_description);
            }
           return result;
        }
        catch(Exception e)
        {
            m_error_messages.add("Cannot extract clone information from database. "+e.getMessage());
            return null;
        }
        
    }
    
    private ArrayList           sortCloneDescriptions(ArrayList clone_descriptions)
    {
        
        //sort array by containerid and position
            Collections.sort(clone_descriptions, new Comparator() 
            {
                public int compare(Object o1, Object o2) 
                {
                    CloneDescription cl1 = (CloneDescription)o1;
                    CloneDescription cl2 = (CloneDescription)o2;
                    if (cl1.getContainerId() != cl2.getContainerId())
                        return cl1.getContainerId() - cl2.getContainerId();
                    else
                        return cl1.getPosition() - cl2.getPosition();
                 
                }
                /** Note: this comparator imposes orderings that are
                 * inconsistent with equals. */
                public boolean equals(java.lang.Object obj)
                {      return false;  }
                // compare
            } );
        
        return clone_descriptions;
    }
    
    
    private void     processClones(ArrayList sorted_clone_description, int m_wells_per_plate,
     Connection conn) throws Exception
    {
        ArrayList primers = new ArrayList();
        ArrayList clone_primers = null;
        CloneDescription clone_description = null;
        for ( int clone_count = 0; clone_count < sorted_clone_description.size() ; clone_count++)
        {
            clone_description = (CloneDescription)sorted_clone_description.get(clone_count);
            clone_primers = getClonePrimers( clone_description );
            if ( !m_isTryMode )
                IsolateTrackingEngine.updateStatus(IsolateTrackingEngine.PROCESS_STATUS_READY_FOR_INTERNAL_READS, clone_description.getIsolateTrackingId(),  conn );
                
            if ( clone_primers != null && clone_primers.size() >0)
            {
                primers.addAll(clone_primers);
            }
            else
            {
                switch ( m_primers_selection_rule)
                {
                    case PrimerOrderRunner.OLIGO_SELECTION_FORMAT_REFSEQ_ONLY:
                    {
                         m_error_messages.add("There is no primers for reference sequence:  clone " + clone_description.getCloneId());
                         break;
                    }
                    case PrimerOrderRunner.OLIGO_SELECTION_FORMAT_STRETCH_COLLECTION_REFSEQ:
                    {
                         m_error_messages.add("There is no primers for clone " + clone_description.getCloneId());
                         break;
                    }
                    case PrimerOrderRunner.OLIGO_SELECTION_FORMAT_STRETCH_COOLECTION_ONLY:
                    {
                         m_error_messages.add("There is no primers for stretch collection: clone " + clone_description.getCloneId());
                         break;
                    }
                }
             }
        }
        if (primers.size() > 0)
                 createPlateFiles(primers,sorted_clone_description,     conn); 
    }
    
    private ArrayList           getClonePrimers(CloneDescription clone_description) throws Exception
    {
        ArrayList clone_primers = Oligo.getByCloneId(clone_description.getCloneId(), Oligo.STATUS_APPROVED , m_primers_selection_rule);
        //trick here : replace orientation by clone id
        ArrayList result = new ArrayList();
        for (int count = 0; count < clone_primers.size(); count++)
        {
            if ( m_primer_placement_format == PLACEMENT_FORMAT_N_PRIMER 
                && count != m_primer_number -1)
                    continue;
            ((Oligo) clone_primers.get(count)).setType( clone_description.getCloneId() );
            result.add(clone_primers.get(count));
        }
        
        return result;
    }
    
    
    
    // there is two arrays sorted
    // first with clone description , second with primers where type field was set to clone id
    // we will move simultaniously by both arrays checking when member of clone_descriptions should be changed
    // 3 files for each plate:
    // template rearray file : cloneid | org plate |org well | dest plate | dest well
    // oligo order file: Plate Name | WellId |WellIndex|CloneId|PrimerId|PrimerSequence|Tm|PrimerLength
    // naming file

    private void           createPlateFiles(ArrayList primers, ArrayList sorted_clone_description, 
             Connection conn)
    throws Exception
    {
       
      //  ArrayList naming_file_entries  = new ArrayList();
        ArrayList items_template  = new ArrayList();
        ArrayList items_oligo  = new ArrayList();
        ArrayList items_order_file  = new ArrayList();
       String  order_file_name = null;
        CloneDescription clone_description = null;
        Oligo oligo = null;String temp= null;
    //    NamingFileEntry naming_entry = null;
        String plate_label = null;
        int container_counter_try_mode = 1;
        
        OligoContainer container = null;
        if ( ! m_isTryMode )     
        {
            container = createOligoContainer();
            plate_label = container.getLabel();
        }
        else
        {
             plate_label = "OPLATE"+Constants.formatIntegerToString( container_counter_try_mode++, 6);
        }
       
        int primer_counter = 0;int well_counter = m_first_well;
        for (int clone_counter = 0; clone_counter < sorted_clone_description.size(); clone_counter++)
        {
            clone_description = (CloneDescription)sorted_clone_description.get(clone_counter);
            while(true)
            {
                 oligo = (Oligo) primers.get(primer_counter  );
                 if ( oligo.getType() != clone_description.getCloneId())
                            break;
                 //create history and update oligo status
                 int oligosample_id = 0;
                if ( ! m_isTryMode)
                {
                     oligosample_id = createOligoRecord( oligo.getId(), container.getId(), well_counter ,clone_description.getCloneId());
                }
                 temp= oligosample_id +"\t"+ plate_label+"\t"+ (well_counter )
                        +"\t"+edu.harvard.med.hip.bec.sampletracking.objects.Container.convertPositionFrom_int_to_alphanumeric(well_counter )+
                        "\t"+clone_description.getCloneId() +"\t"+oligo.getId()
                        +"\t"+oligo.getSequence() +"\t"+oligo.getTm() +"\t"+oligo.getSequence().length();
                items_oligo.add(temp);
                
                if ( m_number_of_files != PrimerOrderRunner.NO_ORDER_FILES )
                    items_order_file.add( createOrderFileEntry(oligosample_id , plate_label,well_counter , oligo.getSequence()  ) );
             /*  naming_entry = new  NamingFileEntry(
                    clone_description.getCloneId()
                    , NamingFileEntry.getOrientation(oligo.getOrientation() ),
                    clone_description.getContainerId(),
                    Algorithms.convertWellFromInttoA8_12( well_counter ), 
                    clone_description.getFlexSequenceId(),
                    Integer.parseInt( oligo.getName().substring(1)) ) ;
                
                naming_file_entries.add( naming_entry); */
              /*  temp = clone_description.getCloneId()+"\t"
                     + clone_description.getContainerId() +"\t"
                     + clone_description.getPosition() +"\t"
                     + plate_label +"\t"
                     +   (well_counter );*/
                 temp = clone_description.getCloneId()+"\t"
                       + plate_label +"\t"
                       +   (well_counter );
                items_template.add(temp);
                
                String time_stamp_oligo_plate = "";
                if (well_counter == m_last_well   || primer_counter == primers.size() - 1)
                {
                    if ( m_isTryMode )
                    {
                        time_stamp_oligo_plate = String.valueOf( System.currentTimeMillis() );
                    }
                    m_file_list_reports.add( FileOperations.writeFile(items_template,  "Clone Id\tOrg plate\tOrg well\tDestination plate\tDestination well\n",  Constants.getTemporaryFilesPath() + plate_label +"_template"+time_stamp_oligo_plate+".txt"));
                    m_file_list_reports.add( FileOperations.writeFile(items_oligo ,"Oligo Sample Id\tPlate Name\tWellId\tWellIndex\tCloneId\tPrimerId\tPrimerSequence\tTm\tPrimerLength\n",  Constants.getTemporaryFilesPath() + plate_label +"_oligo"+time_stamp_oligo_plate+".txt"));
 //-----                  m_file_list_reports.add( FileOperations.writeFile(items_order_file ,"Oligo Sample Id\tPlate Name\tWellId\tWellIndex\tCloneId\tPrimerId\tPrimerSequence\tTm\tPrimerLength\n",  Constants.getTemporaryFilesPath() + plate_label +"_oligo.txt"));
                    if ( m_number_of_files == PrimerOrderRunner.ONE_FILE_PER_ORDER)  
                    {
                        fillEmptyWellsForOrderFile(items_order_file,plate_label);
                         if ( order_file_name == null ) 
                         {
                             order_file_name = Constants.getTemporaryFilesPath() + "order_file_oligo_starting"+ plate_label +time_stamp_oligo_plate +".txt";
                             m_file_list_reports.add( new File(order_file_name)  );
                         }
                        FileOperations.writeFile(items_order_file ,"",  order_file_name , true);
                     
                    }
                    else if (m_number_of_files == PrimerOrderRunner.ONE_FILE_PER_PLATE )
                    {
                        fillEmptyWellsForOrderFile(items_order_file, plate_label );
                         m_file_list_reports.add( FileOperations.writeFile(items_order_file ,"",  Constants.getTemporaryFilesPath() + plate_label +"_order_file"+time_stamp_oligo_plate+".txt"));
                    }
                    if (! m_isTryMode ) 
                            conn.commit();
                     if ( primer_counter == primers.size() - 1)
                        return ;
                    
                    if ( ! m_isTryMode )     
                    {
                        container = createOligoContainer();
                        plate_label = container.getLabel();
                    }
                    else
                    {
                         plate_label = "OPLATE"+Constants.formatIntegerToString( container_counter_try_mode++, 6);
                    }
                 //    naming_file_entries  = new ArrayList();
                     items_template  = new ArrayList();
                     items_oligo  = new ArrayList();
                     items_order_file = new ArrayList();
                    well_counter = m_first_well - 1 ;
                
                }
                 primer_counter++;well_counter++;
            }//primer cycle;
            
        }//clone cycle
      
        return ;
        
    }
    
    
    private void createPreparedStatements(int process_id, Connection conn)throws Exception
    {
        m_pst_insert_process_object = conn.prepareStatement("insert into process_object (processid,objectid,objecttype) values("+process_id+",?,"+Constants.PROCESS_OBJECT_TYPE_CONTAINER+")");
        //m_pst_update_primer_status = conn.prepareStatement("update geneoligo set status = "+Oligo.STATUS_ORDERED +" where oligoid = ? ");
        m_pst_insert_oligo_plate = conn.prepareStatement("insert into oligocontainer (OLIGOCONTAINERID ,LABEL ,USERID  ,ORDERDATE,STATUS) values(?,?,?,sysdate,?)");    
        m_pst_insert_oligo_sample= conn.prepareStatement("insert into OLIGOSAMPLE  (OLIGOSAMPLEID ,OLIGOID  ,CLONEID  ,POSITION  ,OLIGOCONTAINERID) values(?,?,?,?,?)");    
    }
    
    
    private int createOligoRecord(int oligo_id, int plate_id, int position, int cloneid) throws Exception
    {
        //create history and update oligo status
       // m_pst_update_primer_status.setInt(1,oligo_id);
       // DatabaseTransaction.executeUpdate(m_pst_update_primer_status);
   
        int oligosample_id =  BecIDGenerator.getID("oligoid");
        m_pst_insert_oligo_sample.setInt(1,oligosample_id);
        m_pst_insert_oligo_sample.setInt(2,oligo_id);
         m_pst_insert_oligo_sample.setInt(3,cloneid);
          m_pst_insert_oligo_sample.setInt(4,position);
           m_pst_insert_oligo_sample.setInt(5,plate_id);
        DatabaseTransaction.executeUpdate(m_pst_insert_oligo_sample);
        return oligosample_id;
    }
    
    private OligoContainer createOligoContainer() throws Exception
    {
         
        String plate_name = "OPLATE"+Constants.formatIntegerToString( BecIDGenerator.getID("THREDID"), 6);
        OligoContainer container = new OligoContainer();
        container.setLabel(  plate_name.toUpperCase());
        container.setStatus( OligoContainer.STATUS_ORDER_SENT);  
          //create oligo plate record
        m_pst_insert_oligo_plate.setInt(1,container.getId());
        m_pst_insert_oligo_plate.setString(2,container.getLabel() );
        m_pst_insert_oligo_plate.setInt(3, m_user.getId());
         m_pst_insert_oligo_plate.setInt(4, container.getStatus());
        DatabaseTransaction.executeUpdate(m_pst_insert_oligo_plate);
        m_pst_insert_process_object.setInt(1,container.getId());
        DatabaseTransaction.executeUpdate(m_pst_insert_process_object);
        return container;
        
         
    }
    
    
    
    public static void main(String args[]) 
     
    {
       // InputStream input = new InputStream();
        PrimerOrderRunner input = null;
        User user  = null;
        try
        {
                BecProperties sysProps =  BecProperties.getInstance( BecProperties.PATH);
        sysProps.verifyApplicationSettings();
     edu.harvard.med.hip.bec.DatabaseToApplicationDataLoader.loadDefinitionsFromDatabase();
    input = new PrimerOrderRunner();
            user = AccessManager.getInstance().getUser("htaycher123","htaycher");
       //     input.setItems(" 799	       800	       801	       818	       819	       820	       821	       822	       823	       824	       825	       838	       839	       840	       841	       842	       843	       844	       845	       850	       851	       852	       853	       882	       883	       884	       885	       890	       891	       892	       893	       897	       898	       899	       900	       905	       906	       907	       908	       933	       934	       935	       936	       941	       942	       943	       944	       948	       949	       950	       951	       956	       957	       958	       959	      1003	      1004	     35284	     35285	     35286	     35287	     35288	     35289");
            
       //     input.setItemsType( Constants.ITEM_TYPE_CLONEID);
            input.setUser(user);
            input.setInputData(Constants.ITEM_TYPE_CLONEID,"  141384	 172316	 172638	 172476	 172662	 172782	 172599	 172607	");
            
            
            //input.setPrimerNumber();
            input.setPrimerPlacementFormat(PrimerOrderRunner.PLACEMENT_FORMAT_ALL_TOGETHER );
            input.setNumberOfWellsOnPlate(96 );
            input.setPrimersSelectionRule( PrimerOrderRunner.OLIGO_SELECTION_FORMAT_STRETCH_COOLECTION_ONLY);
            input.isFullPlatesOnly(false);
            input.setIsTryMode(true);
            input.setFirstWell(4);
            input.setLastWell(90);
    
            
            
    
            input.setPrimerSequenceColumn (8);
            input.setPrimerNameColumn  ( 1);

            input.setPrimerColumnColumn (2 );
            input.setPrimerowColumn  (3 );
            input.setPlateNameColumn (6 );
            input.setEmptySequenceDisplay  (".");
            input.setEmptySequenceName  (" ");
            input.setNumberOfOrderFiles  (PrimerOrderRunner.NO_ORDER_FILES );

            input.run();
        }
        catch(Exception e){}
     
        
       
        System.exit(0);
     } 
    
   
    
}
