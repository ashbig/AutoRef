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
    
    
    private static final int           CLONE_NOT_PROCESSED = 0;
    private static final int           CLONE_PROCESSED = 1;
    
     private boolean                 m_isTryMode = false;
     private int                     m_primer_placement_format = PLACEMENT_FORMAT_ALL_TOGETHER;
     private int                     m_primer_number = -1;
     private int                     m_wells_per_plate = 96;
      private boolean                m_is_full_plates = false;
      
      //prepared statments
      PreparedStatement m_pst_insert_process_object = null;
      PreparedStatement m_pst_update_primer_status = null;
      PreparedStatement m_pst_insert_oligo_sample = null;
      PreparedStatement m_pst_insert_oligo_plate = null;
   
    public void         setIsTryMode(boolean isTryMode){m_isTryMode=isTryMode;}
    public void         setPrimerNumber(int v){ m_primer_number = v;}
    public void         setPrimerPlacementFormat(int v){ m_primer_placement_format = v;}
    public void         setNumberOfWellsOnPlate(int v){ m_wells_per_plate = v;}
    public void         isFullPlatesOnlye(boolean v){ m_is_full_plates =v;}
    
    public void run()
    {
        Connection conn = null;
         try
        {
            conn = DatabaseTransaction.getInstance().requestConnection();
           // pst_get_clone_primers = conn.prepareStatement("");
              int process_id = Request.createProcessHistory( conn, ProcessDefinition.RUN_OLIGO_ORDER, new ArrayList(),m_user) ;
             createPreparedStatements( process_id, conn);
              ArrayList clone_ids = Algorithms.splitString( m_items);
            ArrayList  clone_description =     getCloneDescriptions(clone_ids);
            if ( clone_description == null || clone_description.size() <1 ) return;
          //  ArrayList sorted_clone_description = sortCloneDescriptions(clone_description);
             ArrayList sorted_clone_description = clone_description;
            processClones(sorted_clone_description,m_wells_per_plate,   conn);
        }
        catch(Exception e)
        {
            DatabaseTransaction.rollback(conn);
            m_error_messages.add(e.getMessage());
        }
        finally
        {
            if(conn != null)            DatabaseTransaction.closeConnection(conn);
            sendEMails();
        }
    }
    
    
    
    //---------------------------private ------------------------------
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
                clone_description.setFlexCloneId (  rs.getInt("cloneid") );
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
            if ( clone_primers != null && clone_primers.size() >0)
            {
                primers.addAll(clone_primers);
                if (primers.size() >= m_wells_per_plate)
                     primers = createPlateFiles(primers, sorted_clone_description,   conn);
            }
            
        }
        if (primers.size() > 0)
                 primers = createPlateFiles(primers,sorted_clone_description,     conn); 
    }
    
    private ArrayList           getClonePrimers(CloneDescription clone_description) throws Exception
    {
        ArrayList clone_primers = Oligo.getByCloneId(clone_description.getFlexCloneId(), Oligo.STATUS_APPROVED );
        //trick here : replace orientation by clone id
        ArrayList result = new ArrayList();
        for (int count = 0; count < clone_primers.size(); count++)
        {
            if ( m_primer_placement_format == PLACEMENT_FORMAT_N_PRIMER 
                && count != m_primer_number -1)
                    continue;
            ((Oligo) clone_primers.get(count)).setType( clone_description.getFlexCloneId() );
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

    private ArrayList           createPlateFiles(ArrayList primers, ArrayList sorted_clone_description, 
             Connection conn)
    throws Exception
    {
        ArrayList primers_left = new ArrayList();
        ArrayList naming_file_entries  = new ArrayList();
        ArrayList items_template  = new ArrayList();
        ArrayList items_oligo  = new ArrayList();
        CloneDescription clone_description = null;
        Oligo oligo = null;String temp= null;
        NamingFileEntry naming_entry = null;
        int first_primer_cloneid = ( (Oligo) primers.get(0)).getType();boolean isInCycle = false;
        OligoContainer container = createOligoContainer();
        for (int clone_counter = 0; clone_counter < sorted_clone_description.size(); clone_counter++)
        {
            clone_description = (CloneDescription)sorted_clone_description.get(clone_counter);
            //loop while not get to the first clone that has primer in primers
            if ( !isInCycle && first_primer_cloneid != clone_description.getFlexCloneId())
            { 
                isInCycle = true; 
                continue;
            }
            for (int primer_counter = 0; primer_counter <  primers.size() ; primer_counter++)
            {
                 oligo = (Oligo) primers.get(primer_counter );
                 //create history and update oligo status
                createOligoRecords( oligo.getId(), container.getId(), primer_counter + 1,clone_description.getFlexCloneId());
               
                if ( oligo.getType() != clone_description.getFlexCloneId())
                            continue;
                temp=  container.getLabel()+"\t"+ (primer_counter + 1)
                        +"\t"+Algorithms.convertWellFromInttoA8_12(primer_counter )+
                        "\t"+clone_description.getFlexCloneId() +"\t"+oligo.getId()
                        +"\t"+oligo.getSequence() +"\t"+oligo.getTm() +"\t"+oligo.getSequence().length();
                items_oligo.add(temp);
               naming_entry = new  NamingFileEntry(
                    clone_description.getFlexCloneId()
                    , NamingFileEntry.getOrientation(oligo.getOrientation() ),
                    clone_description.getContainerId(),
                    Algorithms.convertWellFromInttoA8_12( primer_counter +1), 
                    clone_description.getFlexSequenceId(),
                    Integer.parseInt( oligo.getName().substring(1)) ) ;
                
                naming_file_entries.add( naming_entry);
                temp = clone_description.getFlexCloneId()+"\t"
                     + clone_description.getContainerId() +"\t"
                     + clone_description.getPosition() +"\t"
                     + container.getLabel() +"\t"
                     +   (primer_counter + 1);
                items_template.add(temp);
                
                if (primer_counter == m_wells_per_plate -1  || primer_counter == primers.size() -1)
                {
                    m_file_list_reports.add( NamingFileEntry.createNamingFile(naming_file_entries,FILE_PATH + container.getLabel()+"_naming_reads.txt"));
                    m_file_list_reports.add( FileOperations.writeFile(items_template,  "Clone Id\tOrg plate\tOrg well\tDestination plate\tDestination well\n",  FILE_PATH + container.getLabel() +"_template.txt"));
                    m_file_list_reports.add( FileOperations.writeFile(items_oligo ,"Plate Name\tWellId\tWellIndex\tCloneId\tPrimerId\tPrimerSequence\tTm\tPrimerLength\n",  FILE_PATH + container.getLabel() +"_oligo.txt"));
                             
                    conn.commit();
                    for (int counter = primer_counter + 1; counter < primers.size(); counter++)
                    {
                        primers_left.add( primers.get(counter));
                        return primers_left;
                    }
                }
            }//primer cycle;
            
        }//clone cycle
        return null;
        
    }
    
    
    private void createPreparedStatements(int process_id, Connection conn)throws Exception
    {
        m_pst_insert_process_object = conn.prepareStatement("insert into process_object (processid,objectid,objecttype) values("+process_id+",?,"+Constants.PROCESS_OBJECT_TYPE_CONTAINER+")");
        //m_pst_update_primer_status = conn.prepareStatement("update geneoligo set status = "+Oligo.STATUS_ORDERED +" where oligoid = ? ");
        m_pst_insert_oligo_plate = conn.prepareStatement("insert into oligocontainer (OLIGOCONTAINERID ,LABEL ,USERID  ,ORDERDATE,STATUS) values(?,?,?,sysdate,?)");    
        m_pst_insert_oligo_sample= conn.prepareStatement("insert into OLIGOSAMPLE  (OLIGOSAMPLEID ,OLIGOID  ,CLONEID  ,POSITION  ,OLIGOCONTAINERID) values(sampleid.nextval,?,?,?,?)");    
    }
    
    
    private void createOligoRecords(int oligo_id, int plate_id, int position, int cloneid) throws Exception
    {
        //create history and update oligo status
       // m_pst_update_primer_status.setInt(1,oligo_id);
       // DatabaseTransaction.executeUpdate(m_pst_update_primer_status);
       
        m_pst_insert_oligo_sample.setInt(1,oligo_id);
         m_pst_insert_oligo_sample.setInt(2,cloneid);
          m_pst_insert_oligo_sample.setInt(3,position);
           m_pst_insert_oligo_sample.setInt(4,plate_id);
        DatabaseTransaction.executeUpdate(m_pst_insert_oligo_sample);
    }
    
    private OligoContainer createOligoContainer() throws Exception
    {
         
        String plate_name = "OPoooo"+Constants.formatIntegerToString( BecIDGenerator.getID("THREDID"), 6);
        OligoContainer container = new OligoContainer();
        container.setLabel(  plate_name.toUpperCase());
        container.setStatus( OligoContainer.STATUS_ORDER_CREATED);  
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
            input = new PrimerOrderRunner();
            user = AccessManager.getInstance().getUser("htaycher1","htaycher");
            input.setItems("676\t678\t677\t719\t722\t721\t720\t956");
            input.setItemsType( Constants.ITEM_TYPE_CLONEID);
            input.setUser(user);
            //input.setPrimerNumber();
            input.setPrimerPlacementFormat(PrimerOrderRunner.PLACEMENT_FORMAT_ALL_TOGETHER );
            input.setNumberOfWellsOnPlate(96 );
            input.isFullPlatesOnlye(false);
            input.run();
        }
        catch(Exception e){}
     
        
       
        System.exit(0);
     } 
    
}
