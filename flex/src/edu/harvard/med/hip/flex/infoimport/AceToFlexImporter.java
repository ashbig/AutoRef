/*
 * AceToFlexImporter.java
 *
 * Created on March 6, 2007, 2:37 PM
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
import edu.harvard.med.hip.flex.user.*;
import edu.harvard.med.hip.bec.coreobjects.endreads.*;
import edu.harvard.med.hip.bec.coreobjects.feature.*;
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
/**
 *
 * @author  htaycher
 */

/*
 *
 *sql for database update
 *insert into menuitem values(menuitemid.nextval, 'transferACEtoFLEX', 12,'ACE to FLEX data transfer','Process')
 update menuitem set displayorder=displayorder+1 where displayorder>
 insert into usergroupmenuitem  values ('Workflow Admin',
 (select menuitemid from menuitem where menuitem='transferACEtoFLEX'));
insert into usergroupmenuitem  values ('System Admin',
 (select menuitemid from menuitem where menuitem='transferACEtoFLEX'));
 
 */
public class AceToFlexImporter extends ImportRunner
{
    
   
    public String getTitle() {        return "Clone information transfer from ACE to FLEX.";    }
    
    public void run_process() 
    {
         Connection  flex_connection = null;
         Connection  ace_connection = null;
      
         ArrayList  clone_ids_for_sql =   prepareItemsListForSQL();
        // System.out.println(clone_ids_for_sql.size());
         if (clone_ids_for_sql == null || clone_ids_for_sql.size() < 1)
         {
             m_error_messages.add("No clone submitted for transfer.");
             return;
         }
            
        try
        {
            // get connection to ACE
       /*    
           ace_connection = DatabaseTransactionLocal.getInstance(
                    edu.harvard.med.hip.flex.util.FlexProperties.getInstance().getProperty("ACE_URL") , 
                    edu.harvard.med.hip.flex.util.FlexProperties.getInstance().getProperty("ACE_USERNAME"), 
                    edu.harvard.med.hip.flex.util.FlexProperties.getInstance().getProperty("ACE_PASSWORD")).requestConnection();
          
            /* */FlexProperties sysProps = StaticPropertyClassFactory.makePropertyClass("FlexProperties");
       
             ace_connection = DatabaseTransactionLocal.getInstance(
                    sysProps.getInstance().getProperty("ACE_URL") , 
                    sysProps.getInstance().getProperty("ACE_USERNAME"), 
                    sysProps.getInstance().getProperty("ACE_PASSWORD")).requestConnection();
       
            flex_connection = DatabaseTransaction.getInstance().requestConnection();        
        }
        catch(Exception e)
        {
            m_error_messages.add("Cannot get connection to ACE, check settings");
              System.out.println(e.getMessage());
        }
        try
        {
             for (int i_group_count = 0; i_group_count < clone_ids_for_sql.size(); i_group_count++)
             {
                 //get info from ACE 
                 ArrayList clone_data_for_submission = getDataFromAce((String)clone_ids_for_sql.get(i_group_count),ace_connection);
                 // submit into flex
                 importDataIntoFLEX( clone_data_for_submission, flex_connection);
               
             }
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            m_error_messages.add("Cannot transfer data from ACE.\n"+e.getMessage());
        }
         finally
         {
            sendEmails("Data transfer from ACE to FLEX process","Data transfer from ACE to FLEX process");
         }
      
           
    }
    
    private ArrayList        getDataFromAce(String clone_ids_for_sql,Connection ace_connection)throws Exception
    {
        ArrayList clones_data = new ArrayList();
        // make sure that only not 'IN PROCESS' clones are transfered
        String sql ="select iso.process_status as process_status,flexcloneid as cloneid, cs.sequenceid as clone_sequenceid,  "
       + "  cs.cdsstart as cdsstart, cs.cdsstop as cdsstop from flexinfo f, isolatetracking iso, " +
       " assembledsequence cs   where iso.isolatetrackingid=f.isolatetrackingid and cs.isolatetrackingid(+) = iso.isolatetrackingid and flexcloneid in ("
       +clone_ids_for_sql+")  and flexcloneid > 0 order by flexcloneid, cs.sequenceid";
        CachedRowSet rs = null;
        Hashtable sequence_hash = new Hashtable();
        CloneSequenceDataForFLEXSubmission clone_description = null;
        ArrayList clone_discreapncies = null; 
        StringBuffer sequence_ids = new StringBuffer();
         ArrayList sequence_text = null;
        Hashtable processed_clones = new Hashtable();
        int current_seq_id = 0;
        
         ArrayList seq_text_entry =  new ArrayList();
        try
        {
              // DatabaseTransactionLocal t = DatabaseTransactionLocal.getInstance();
            rs = DatabaseTransaction.executeQuery(sql,ace_connection);
            while(rs.next())
            {
                if (  rs.getInt("process_status") ==     IsolateTrackingEngine.FINAL_STATUS_NOT_APPLICABLE
       || rs.getInt("process_status") == IsolateTrackingEngine.FINAL_STATUS_INPROCESS)
                {
                    m_error_messages.add("Data for clone ID "+rs.getInt("cloneid")+" cannot be transfered, because clone final status "+ IsolateTrackingEngine.getCloneFinalStatusAsString( rs.getInt("process_status")));
                    continue;
                }
                
                if (  processed_clones.contains( new Integer(rs.getInt("cloneid")) ))
                {
                    continue;
                }
                clone_description = new CloneSequenceDataForFLEXSubmission();
                clone_description.setCloneID(  rs.getInt("cloneid"));
                if ( rs.getInt("clone_sequenceid") > 0 )
                {
                    clone_description.setACESequenceID( rs.getInt("clone_sequenceid"));
                    sequence_ids.append( clone_description.getACESequenceID() +",");
                    clone_description.setCDSStart(rs.getInt("cdsstart"));
                    clone_description.setCDSStop(rs.getInt("cdsstop")); 
                     // process discrepancies 
                     clone_discreapncies = getMutationsFromACE( ace_connection,  clone_description.getACESequenceID());
                     if ( clone_discreapncies != null && clone_discreapncies.size() > 0 )
                     {
                         clone_discreapncies = DiscrepancyDescription.assembleDiscrepancyDefinitions(clone_discreapncies);
                         String[] str_arr = new String[3];
                         String[] ob=  (String[] )  DiscrepancyDescription.detailedDiscrepancyreport(clone_discreapncies, str_arr );
                         clone_description.setMLinker5(ob[0]);
                         clone_description.setIsDiscrepancy(true);
                         clone_description.setMutCDS(ob[1]);
                         clone_description.setMutLinker3(ob[2]);

                     }
                }
                clone_description.setCloneFinalStatus(rs.getInt("process_status"));
                m_processed_items += clone_description.getCloneID()+" ";
                processed_clones.put(new Integer(clone_description.getCloneID()), clone_description);
                 
                
            }
            DatabaseTransactionLocal.closeResultSet(rs);
            // get clone sequence text
            sequence_hash = getSequences(sequence_ids.toString(), ace_connection);
            // fill in clone sequence text
            for (Enumeration en1 = processed_clones.elements() ; en1.hasMoreElements() ;)
            {
                clone_description = ( CloneSequenceDataForFLEXSubmission)en1.nextElement();
                sequence_text = (ArrayList)  sequence_hash.get(new Integer(clone_description.getACESequenceID()));
                if (sequence_text != null) clone_description.setSequenceText(sequence_text);
            }
          
            return new ArrayList( processed_clones.values());
        } catch (Exception sqlE)
        {
            System.out.println(sqlE.getMessage());
            throw new FlexDatabaseException("Error occured while getting clone information /SQL: "+sql);
        } finally
        {
            DatabaseTransactionLocal.closeResultSet(rs);
        }
        
    
    }
  
   private Hashtable        getSequences(String sequence_ids,Connection ace_connection)throws Exception
    {
        CachedRowSet rs = null;
        int current_seq_id = 0;String sql = null;
        Hashtable sequence_hash = new Hashtable();
        ArrayList sequence_text = new ArrayList();
        try
        {
            if (sequence_ids == null || sequence_ids.length() < 2) return sequence_hash;
            if ( sequence_ids.charAt(sequence_ids.length()-1) ==',') sequence_ids = sequence_ids.substring(0, sequence_ids.length()-1);
             sql = "select sequenceid, infotext from sequenceinfo where sequenceid in ("
            + sequence_ids +") and infotype = " + BaseSequence.SEQUENCE_INFO_TEXT +"  order by sequenceid, infoorder";
         
              // DatabaseTransactionLocal t = DatabaseTransactionLocal.getInstance();
            rs = DatabaseTransaction.executeQuery(sql,ace_connection);
            
            while(rs.next())
            {
                if ( current_seq_id > 0 && current_seq_id != rs.getInt("sequenceid"))
                {
                    sequence_hash.put(new Integer(current_seq_id), sequence_text);
                    sequence_text = new ArrayList();
                }
                sequence_text.add(rs.getString("infotext"));
                current_seq_id =rs.getInt("sequenceid");
            } 
            if ( ! sequence_text.toString().equals(""))
               sequence_hash.put(new Integer(current_seq_id), sequence_text);
            DatabaseTransactionLocal.closeResultSet(rs);   
          
            return sequence_hash;
        } catch (Exception sqlE)
        {
            System.out.println(sqlE.getMessage());
            throw new FlexDatabaseException("Error occured while getting clone information /SQL: "+sql);
        } finally
        {
            DatabaseTransactionLocal.closeResultSet(rs);
        }
        
    
    }
  
    private void             importDataIntoFLEX(ArrayList clone_data_for_submission, Connection conn) throws Exception
    {
        CloneSequenceDataForFLEXSubmission clone_data = null;
         int sequenceid =  -1; String cloneName = null;
        String sql_insert_cseq = "insert into clonesequence(sequenceid,sequencetype,resultexpect,"+
        " matchexpect,genechange,linker5p,linker3p,cdsstart,cdslength,sequencingid)"+
        " values(?,'FULL SEQUENCE',?,?,?,?,?,?,?,(select sequencingid from clonesequencing where cloneid=?))";
       
        String sql_insert_seq = "insert into clonesequencetext(sequenceid, sequenceorder, sequencetext)\n"+
        " values(?,?,?)";
         
        String sql_update_clonesequencing ="update CLONESEQUENCING set sequencingstatus ='COMPLETE' ,sequencingindate=sysdate where cloneid =?";
        String sql_update_clones_acc = "update clones set status='SEQUENCE VERIFIED'  where cloneid =?";
        
        String  sql_update_cloningprogress = "update CLONINGPROGRESS set statusid = 4 where constructid in "
                +"( select constructid from clones where cloneid =?)";
        String sql_update_flex_sequence = "update FLEXSEQUENCE set flexstatus='SEQUENCE VERIFIED' where  sequenceid in" 
        +"(select sequenceid from clones where cloneid =?)";
     
        String sql_update_clones_reject = "update clones set status='FAILED BY SEQUENCE VALIDATION'  where cloneid =?";
       
        PreparedStatement    stmt_insert_sequence_text = conn.prepareStatement(sql_insert_seq);
        PreparedStatement    stmt_insert_clone_sequence_details = conn.prepareStatement(sql_insert_cseq); 
        
        PreparedStatement    stmt_update_clonesequencing = conn.prepareStatement(sql_update_clonesequencing); 
        PreparedStatement    stmt_update_clones_acc = conn.prepareStatement(sql_update_clones_acc); 
        PreparedStatement    stmt_update_clones_reject = conn.prepareStatement(sql_update_clones_reject); 
        PreparedStatement    stmt_update_cloningprogress = conn.prepareStatement(sql_update_cloningprogress); 
        PreparedStatement    stmt_update_flex_sequence = conn.prepareStatement(sql_update_flex_sequence); 
        
        PreparedStatement    stmt_get_clone_name = conn.prepareStatement( "select cd.constructtype as constructtype, f.genusspecies as species "+
        " from clones c, obtainedmasterclone o, constructdesign cd, flexsequence f"+
        " where c.mastercloneid=o.mastercloneid and o.constructid=cd.constructid"+
        " and cd.sequenceid=f.sequenceid  and c.cloneid=?");
         PreparedStatement    stmt_update_clone_name = conn.prepareStatement( "update clones set clonename=? where cloneid=?");
       PreparedStatement    stmt_clone_update_status = conn.prepareStatement("select sequencingstatus from CLONESEQUENCING where cloneid=?"); 
        
         java.text.NumberFormat clone_name_format = getNameFormat();
         
        for (int count = 0; count < clone_data_for_submission.size(); count++)
        {
            try
            {
                clone_data = (CloneSequenceDataForFLEXSubmission) clone_data_for_submission.get(count);
             //    System.out.println("start update "+clone_data.getCloneID());
                    
                
                if ( isCloneDataHaveBeenTransfered(clone_data.getCloneID(),stmt_clone_update_status) )
                {
                    m_process_messages.add("No Transfer of clone data for Clone ID "+clone_data.getCloneID()+", because  of previous transfer.\n");
                    continue;
                }
                if ( clone_data.getCloneFinalStatus() == IsolateTrackingEngine.FINAL_STATUS_ACCEPTED && (
                        clone_data.getACESequenceID() < 0 || clone_data.getSequenceText() == null || clone_data.getSequenceText().size() < 0 ))
                {
                    m_process_messages.add("No Transfer of clone data for Clone ID "+clone_data.getCloneID()+", because of missing clone sequence.\n");
                    continue;
                }
                sequenceid = FlexIDGenerator.getID("CLONESEQUENCEID");
               
                stmt_insert_clone_sequence_details.setInt(1, sequenceid);
                
                stmt_insert_clone_sequence_details.setString(2,clone_data.getMutCDS());//resultexpect
                String matchexpect = clone_data.isDiscrepancies() ? "N": "Y";
                stmt_insert_clone_sequence_details.setString(3,matchexpect);//matchexpect
                stmt_insert_clone_sequence_details.setString(4,clone_data.getMutCDS());//genechange
                stmt_insert_clone_sequence_details.setString(5,clone_data.getMLinker5());//linker5p
                stmt_insert_clone_sequence_details.setString(6, clone_data.getMutLinker3());//linker3p
                stmt_insert_clone_sequence_details.setInt(7,clone_data.getCDSStart());//cdsstart
                stmt_insert_clone_sequence_details.setInt(8,clone_data.getCDSStop()-clone_data.getCDSStart());//cdslength,
                stmt_insert_clone_sequence_details.setInt(9,clone_data.getCloneID());//cdslength,
                 
                 DatabaseTransaction.executeUpdate(stmt_insert_clone_sequence_details);
                 
                 if ( clone_data.getACESequenceID() > 0 )
                 {
                    for (int s_count = 0; s_count < clone_data.getSequenceText().size(); s_count++)
                    {
                        stmt_insert_sequence_text.setInt(1, sequenceid);
                        stmt_insert_sequence_text.setInt(2, s_count+1 );
                        stmt_insert_sequence_text.setString(3,(String)clone_data.getSequenceText().get(s_count) );
                        DatabaseTransaction.executeUpdate(stmt_insert_sequence_text);

                    }
                 }
                 
                 //set different flags related to clone status
                 stmt_update_clonesequencing.setInt(1, clone_data.getCloneID()); 
                 DatabaseTransaction.executeUpdate(stmt_update_clonesequencing);
                 
                 if ( clone_data.getCloneFinalStatus() == IsolateTrackingEngine.FINAL_STATUS_ACCEPTED)
                 {
                     stmt_update_clones_acc.setInt(1, clone_data.getCloneID()); 
                     DatabaseTransaction.executeUpdate(stmt_update_clones_acc);
                     stmt_update_flex_sequence.setInt(1, clone_data.getCloneID()); 
                     DatabaseTransaction.executeUpdate(stmt_update_flex_sequence);
                     stmt_update_cloningprogress.setInt(1, clone_data.getCloneID()); 
                     DatabaseTransaction.executeUpdate(stmt_update_cloningprogress);
                     
                 }
                 else if (clone_data.getCloneFinalStatus() == IsolateTrackingEngine.FINAL_STATUS_REJECTED)
                 {
                     stmt_update_clones_reject.setInt(1, clone_data.getCloneID()); 
                     DatabaseTransaction.executeUpdate(stmt_update_clones_reject);
                 }
                 //update clone name
                 cloneName = buildCloneName(clone_data.getCloneID(),    stmt_get_clone_name,  clone_name_format);
                 stmt_update_clone_name.setString(1, cloneName);
                 stmt_update_clone_name.setInt(2, clone_data.getCloneID());
                 DatabaseTransaction.executeUpdate(stmt_update_clone_name);
                 
               //  System.out.println("to update "+clone_data.getCloneID());
                 conn.commit();
               // System.out.println("updated "+clone_data.getCloneID());
                 
                 m_process_messages.add("Clone data for Clone ID "+clone_data.getCloneID()+" were transfered into FLEX.\n");
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
                conn.rollback();
                m_error_messages.add("\nCannot submit data for clone ID " + clone_data.getCloneID());
            }
        }
        
    }
    
    
    private boolean isCloneDataHaveBeenTransfered(int cloneid, PreparedStatement stmt_get_data)throws Exception
    {
         ResultSet rs = null;
        
        try 
        {
             stmt_get_data.setInt(1, cloneid);
             rs = DatabaseTransaction.executeQuery(stmt_get_data);
             if(rs.next()) 
             {
                 if ( rs.getString("sequencingSTATUS").equals("COMPLETE")) return true;
             }
             return false;
        }
        catch(Exception e)
        {
            throw new Exception("\nCannot verify clone update status. Clone ID "+cloneid);
             }
    }
    private java.text.NumberFormat getNameFormat()
    {
        java.text.NumberFormat fmt = java.text.NumberFormat.getInstance();
        fmt.setMaximumIntegerDigits(6);
        fmt.setMinimumIntegerDigits(6);
        fmt.setGroupingUsed(false);
        return fmt;
    }
    
    private String buildCloneName(int cloneid, 
            PreparedStatement stmt_get_data, java.text.NumberFormat fmt) throws Exception
    {
        ResultSet rs = null;
        
        try 
        {
             stmt_get_data.setInt(1, cloneid);
             rs = DatabaseTransaction.executeQuery(stmt_get_data);
             if(rs.next()) 
             {
                    String constructtype = rs.getString("constructtype");
                    String species = rs.getString("species");
                    String name = null;
                    if("FUSION".equals(constructtype))
                    {
                        if(species.equals(FlexSequence.HUMAN)) return "FLH"+fmt.format(cloneid)+".01L";
                        else return "FLH"+fmt.format(cloneid)+".01F";
                    } 
                    else if("CLOSED".equals(constructtype)) return "FLH"+fmt.format(cloneid)+".01X";
                    else
                    {
                        throw new Exception("Invalid construct type: "+cloneid+": "+constructtype);
                    }
             }
             else 
            {
                throw new Exception("No construct found: "+cloneid);
            }
            
        }
        catch (Exception ex)
        {
            throw new Exception("Database error.");
        } 
    }
    
    private void             updateFLEXDatabase( String clone_ids_for_sql, Connection conn) throws Exception
    {
      
        String sql_update = null;
        DatabaseTransaction dt = DatabaseTransaction.getInstance();
                
       //  update CLONESEQUENCING table: sequencingstatus ='COMPLETE' ,sequencingdate=sysdate
        sql_update ="update CLONESEQUENCING set sequencingstatus ='COMPLETE' ,sequencingindate=sysdate where cloneid in ("+clone_ids_for_sql+")";
        dt.executeUpdate(sql_update, conn);
        //Update CLONES table: status='SEQUENCE VERIFIED'
        
        sql_update = "update clones set status='SEQUENCE VERIFIED'  where cloneid in ("+clone_ids_for_sql+")";
         dt.executeUpdate(sql_update, conn);
        
         //Update CLONINGPROGRESS table: status=4
        sql_update = "update CLONINGPROGRESS set statusid = 4 where constructid in "
                +"( select constructid from clones where cloneid in ("+clone_ids_for_sql+"))";
         dt.executeUpdate(sql_update, conn);
          //FLEXSEQUENCE table: flexstatus:'SEQUENCE VERIFIED'*/
        sql_update = "update FLEXSEQUENCE set flexstatus='SEQUENCE VERIFIED' where  flexsequenceid in" 
        +"(select sequenceid from clones where cloneid in ("+clone_ids_for_sql+"))";
         dt.executeUpdate(sql_update, conn);
        
        
    }
    //************************************************************
    //copy from Mutation class of ACE project to allow connection to FLEX specified ACE instance
    
    private  ArrayList   getMutationsFromACE(Connection ace_connection, int sequence_id ) throws FlexDatabaseException
    {
        int type =-1; 
        int mid = -1;
        
        ArrayList discrepancies = new ArrayList();
        String sql = "select  DISCREPANCYID  ,POSITION ,LENGTH ,CHANGEORI ,CHANGEMUT "
 +",TYPE ,SEQUENCEID ,POLYMFLAG ,POLYMID ,POLMDATE,CODONORI ,CODONMUT ,UPSTREAM ,DOWNSTREAM "
 +",CODONPOS ,CHANGETYPE ,DISCRNUMBER ,DISCRPOSITION  ,DISCRLENGTH  ,DISCQUALITY  from discrepancy where sequenceid = "+sequence_id +" order by discrnumber, type";

        ResultSet rs = null;
        try
        {
             
            rs = DatabaseTransaction.executeQuery(sql,ace_connection);
            
            while(rs.next())
            {
                mid = rs.getInt("DISCREPANCYID");
                type = rs.getInt("TYPE");
                if  (type == Mutation.AA)
                {
                    AAMutation cur_aa_mutation = new AAMutation();
                    cur_aa_mutation.setId(mid);
                    cur_aa_mutation.setPosition ( rs.getInt("POSITION") );// start of mutation (on object sequence)
                    cur_aa_mutation.setLength ( rs.getInt("LENGTH") );
                    cur_aa_mutation.setChangeMut ( rs.getString("CHANGEMUT"));
                    cur_aa_mutation.setChangeOri ( rs.getString("CHANGEORI"));
                    cur_aa_mutation.setSequenceId ( rs.getInt("SEQUENCEID") ) ;
                    cur_aa_mutation.setNumber ( rs.getInt("DISCRNUMBER")) ;
                    cur_aa_mutation.setChangeType ( rs.getInt("CHANGETYPE")) ;
                    cur_aa_mutation.setQuality( rs.getInt("DISCQUALITY") );
                    discrepancies.add(cur_aa_mutation);
                }
                else if (type == Mutation.LINKER_5P || type == Mutation.LINKER_3P )
                {
                    LinkerMutation cur_linker_mutation = new LinkerMutation(type == Mutation.LINKER_5P);
                    cur_linker_mutation.setType(type);
                    cur_linker_mutation.setId(mid);
                    cur_linker_mutation.setPosition ( rs.getInt("POSITION") );// start of mutation (on object sequence)
                    cur_linker_mutation.setLength ( rs.getInt("LENGTH") );
                    cur_linker_mutation.setChangeMut ( rs.getString("CHANGEMUT"));
                    cur_linker_mutation.setChangeOri ( rs.getString("CHANGEORI"));
                    cur_linker_mutation.setSequenceId ( rs.getInt("SEQUENCEID")) ;
                    cur_linker_mutation.setNumber ( rs.getInt("DISCRNUMBER")) ;
                    cur_linker_mutation.setChangeType ( rs.getInt("CHANGETYPE")) ;
                    cur_linker_mutation.setQuality( rs.getInt("DISCQUALITY") );
                    cur_linker_mutation.setExpPosition( rs.getInt("DISCRPOSITION") );
                    discrepancies.add( cur_linker_mutation );
                }
               else if(type == Mutation.RNA)
                {
                    RNAMutation cur_rna_mutation = new RNAMutation();
                    cur_rna_mutation.setId(mid);
                     cur_rna_mutation.setPolymFlag( rs.getInt("POLYMFLAG"));
                     cur_rna_mutation.setPolymDate(rs.getDate("POLMDATE"));
                     cur_rna_mutation.setPolymId( rs.getString("POLYMID"));
                    cur_rna_mutation.setUpstream(rs.getString("UPSTREAM"));
                    cur_rna_mutation.setDownStream(rs.getString("DOWNSTREAM"));
                    cur_rna_mutation.setCodonOri( rs.getString("CODONORI") );
                    cur_rna_mutation.setCodonMut(rs.getString("CODONMUT"));
                    cur_rna_mutation.setCodonPos( rs.getInt("CODONPOS") );
                     cur_rna_mutation.setPosition ( rs.getInt("POSITION") );// start of mutation (on object sequence)
                    cur_rna_mutation.setLength ( rs.getInt("LENGTH") );
                    cur_rna_mutation.setChangeMut ( rs.getString("CHANGEMUT"));
                    cur_rna_mutation.setChangeOri ( rs.getString("CHANGEORI"));
                    cur_rna_mutation.setSequenceId ( rs.getInt("SEQUENCEID")) ;
                    cur_rna_mutation.setNumber ( rs.getInt("DISCRNUMBER")) ;
                    cur_rna_mutation.setChangeType ( rs.getInt("CHANGETYPE")) ;
                    cur_rna_mutation.setQuality( rs.getInt("DISCQUALITY") );
                    cur_rna_mutation.setExpPosition( rs.getInt("DISCRPOSITION") );
                    discrepancies.add( cur_rna_mutation);
                }
            }
           return discrepancies;
        } catch (Exception sqlE)
        {
            throw new FlexDatabaseException("Error occured while getting discrepancies: \nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
       
    }
     
     //****************************************************
    
    class CloneSequenceDataForFLEXSubmission
    {
        private int                 i_cloneid = -1;
        private int                 i_ace_sequence_id = -1;
        private ArrayList           i_sequence_text = null;
        private int                 i_cds_start = -1;    
        private int                 i_cds_stop =-1;
        private String              i_mut_linker_5p = null;
        private String              i_mut_linker_3p = null;
        private String              i_mut_cds = null;
        private boolean             i_is_discrepancies = false;
        private int                 i_clone_final_status = IsolateTrackingEngine.FINAL_STATUS_INPROCESS;
        
        public  CloneSequenceDataForFLEXSubmission()
        {
            i_mut_linker_5p = "";
            i_mut_linker_3p = "";
            i_mut_cds = "";
        }
        public int                 getCloneID(){ return i_cloneid ;}
        public int                 getACESequenceID(){ return i_ace_sequence_id ;}
        public ArrayList               getSequenceText(){ return i_sequence_text ;}
        public int                 getCDSStart(){ return i_cds_start ;}   
        public int                 getCDSStop(){ return i_cds_stop ;}
        public String              getMLinker5(){ return i_mut_linker_5p;}
        public String              getMutLinker3(){ return i_mut_linker_3p ;}
        public String              getMutCDS(){ return i_mut_cds ;}
        public boolean              isDiscrepancies(){ return i_is_discrepancies;}
        public int                  getCloneFinalStatus(){ return i_clone_final_status;}
        
        public void                 setCloneID(int v){  i_cloneid = v ;}
        public void                 setACESequenceID(int v){  i_ace_sequence_id = v;}
        public  void                setSequenceText(ArrayList v){  i_sequence_text = v;}
        public void                 setCDSStart(int v){  i_cds_start = v;}   
        public void                 setCDSStop(int v){  i_cds_stop= v ;}
        public void                 setMLinker5(String v){  i_mut_linker_5p= v;}
        public void              setMutLinker3(String v){  i_mut_linker_3p= v ;}
        public void              setMutCDS(String v){  i_mut_cds = v;}
        public void                 setIsDiscrepancy(boolean v){ i_is_discrepancies = v;}
        public void                 setCloneFinalStatus(int v){i_clone_final_status=v;}
        
    }
    
    
    public static void main(String [] args)
    {
        try
        {
            
        ImportRunner runner = new AceToFlexImporter();
        runner.setInputData(ConstantsImport.ITEM_TYPE_CLONEID, " 268620 268622 268624   " );     //  runner.setContainerLabels(master_container_labels );
        runner.setProcessType( ConstantsImport.PROCESS_DATA_TRANSFER_ACE_TO_FLEX );
       // Spec spec_f = Spec.getSpecById(91, Spec.FULL_SEQ_SPEC_INT);
        User user = new User("htaycher", "hip_informatics@hms.harvard.edu", "");
    
        runner.setUser( user);
          runner.run();
        }catch(Exception e){}
        System.exit(0);
      }

     
    
}
