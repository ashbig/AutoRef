/*
 * ImportSample.java
 *
 * Created on June 22, 2007, 3:53 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.infoimport.coreobjectsforimport;

import java.io.*;
import java.util.*;
import java.sql.*;
import java.util.*;
//import javax.crypto.NullCipher;
import sun.jdbc.rowset.*;

import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.util.*;
/**
 *
 * @author htaycher
 */
public class ImportSample
{
  
    public static final String      SAMPLE_POSITION = "POSITION";
    public static final String      SAMPLE_CONSTRUCT_TYPE = "CONSTRUCT_TYPE";
    public static final String      SAMPLE_CONSTRUCT_SIZE ="CONSTRUCT_SIZE";
    
    // for clones tables only
    public static final String      SAMPLE_CLONE_STATUS ="CLONE_STATUS";
    public static final String      SAMPLE_CLONE_TYPE ="CLONE_TYPE";
    public static final String      SAMPLE_CLONING_STRATEGYID="CLONING_STRATEGYID";
     public static final String      SAMPLE_FIVE_PRIME_LINKER="FIVE_PRIME_LINKER";
    public static final String      SAMPLE_THREE_PRIME_LINKER="THREE_PRIME_LINKER";
    public static final String      SAMPLE_VECTOR="VECTOR";
   
 /*   
    public static final String      CLONE_STATUS_SEQUENCE_VERIFIED = "SEQUENCE VERIFIED";
public static final String      CLONE_STATUS_UNSEQUENCED = "UNSEQUENCED";
public static final String      CLONE_STATUS_IN_PROCESS = "IN PROCESS";
public static final String      CLONE_STATUS_SUCESSFUL = "SUCESSFUL";
public static final String      CLONE_STATUS_FAIL = "FAIL";
public static final String      CLONE_STATUS_FAILED_BY_SEQUENCE_VALIDATION ="FAILED BY SEQUENCE VALIDATION";
*/

       
    private int         m_id = -1;
    private int         m_construct_id =-1;
    private ArrayList   m_additional_info = null;
    private String      m_type = Sample.ISOLATE;
    private int         m_containerid = -1;
    private int         m_position = -1;
    private String      m_status = Sample.GOOD;  
     private String         m_sequence_id = null;
     private String         m_construct_type = null;
     private String            m_construct_size_class = ImportConstruct.CONSTRUCT_SIZE_SMALL;
    private int             i_cloning_strategy_id = -1;
    private String             i_clone_type = CloneSample.MASTER;
    private String              i_clone_status = ImportClone.CLONE_STATUS_IN_PROCESS;
    private ImportClone         i_clone = null;
   
    
    /** Creates a new instance of ImportSample */
    public ImportSample(int position) 
    {
        m_position = position;
        m_additional_info = new ArrayList();
    }
    
    public ImportSample(int position, String type) 
    {
        m_position = position;
        m_type = type;
        m_additional_info = new ArrayList();
    }
     public ImportSample() 
    {
       m_additional_info = new ArrayList();
    }
    
    public int          getPosition(){ return m_position;}
    public String       getType(){ return m_type;}
    public int          getId() throws Exception{  if (m_id == -1)        m_id = FlexIDGenerator.getID("sampleid");return m_id;}
    public String       getSequenceId(){ return m_sequence_id;}
    public int          getConstructId() throws Exception{  if (m_construct_id == -1)        m_construct_id = FlexIDGenerator.getID("constructid"); return m_construct_id;}
    public String       getConstructType(   ){  return    m_construct_type ;}
      public String     getConstructSize(  ){   return        m_construct_size_class ;}
    
    public int          getCloningStrategyId(   ){  return    i_cloning_strategy_id;}
    public String       getCloneType(   ){  return    i_clone_type ;}
    public String       getCloneStatus(   ){  return     i_clone_status ;}
    public ImportClone  getClone(){ return i_clone;}
 
    
    public void         setClone(ImportClone v){ i_clone = v;}
    public void          setPosition(int v){  m_position = v;}
    public void         setConstructId(int v){    m_construct_id = v;}
    public void         setConstructType( String   v){      m_construct_type = v;}
     public void         setConstructSize( String v){           m_construct_size_class = v;}
    public void         setSequenceId(String v){ m_sequence_id = v;}
    public          void        addPublicInfo(PublicInfoItem v)
    {
        if (m_additional_info == null) m_additional_info = new ArrayList();
        if ( ! PublicInfoItem.contains( m_additional_info, v))
                m_additional_info.add(v);
    }
    public void         setContainerId(int v){         m_containerid = v;}
    public ArrayList    getPublicInfo() { return m_additional_info;}
    public void          setCloningStrategyId(int v   ){      i_cloning_strategy_id = v;}
    public void         setCloneType( String v  ){      i_clone_type  = v;}
    public void         setCloneStatus( String v  ){       i_clone_status  = v;}
    public void         addPublicInfoItems(ArrayList v)
    {
        PublicInfoItem p_info = null;
        for (int count = 0; count < v.size(); count++)
        {
            p_info = (PublicInfoItem) v.get(count);
            if ( ! PublicInfoItem.contains( m_additional_info, p_info))
                m_additional_info.add(p_info);
        }
    }
  
    
    
    
    public boolean      isSameSample(ImportSample sample)
    {
         if (m_position != sample.getPosition()) return false;
         if ( !m_sequence_id.equalsIgnoreCase( sample.getSequenceId()) )return false;
         //for (int count = 0; count  < m_additional_info.size(); count++)
        // {
             // if (!m_additional_info.containsAll(sample.getPublicInfo()))
             //     return false;
        
        // }
       
         return true;
    }
    
     public String toString() {return this.toString("\n");}
    
    public String toString(String delim)
    {
        StringBuffer seq = new StringBuffer();
        seq.append("ID: "+m_id +delim);
        seq.append("Status: "+m_status+delim);
        seq.append("Position: "+m_position+delim);
        seq.append("Construct ID: "+m_containerid+delim);
        seq.append("Sequence ID: "+m_sequence_id+delim);
        seq.append("Construct Type: "+m_construct_type+delim);
        
        for (int count =0; count < m_additional_info.size(); count++)
        {
            seq.append( (PublicInfoItem) m_additional_info.get(count)+delim);
        }
        return seq.toString();
     
    }
    
    
    public void insert(Connection conn, int containerid, ArrayList errors) throws Exception
     {
           int is_addition_info = PublicInfoItem.isAnyPublicInfoForSubmission(m_additional_info) ? 1:0;
             if (m_id == -1)        m_id = FlexIDGenerator.getID("sampleid");
         String sql = null;
           if ( m_type.equals( Sample.ISOLATE))
           {
               if(m_construct_id == -1) m_construct_id =  FlexIDGenerator.getID("constructid");
        
               sql = "insert into sample (sampleid, sampletype, containerid, containerposition,"
                  +"       constructid, status_gb, additionalinfo)"
                 + "values ("+ m_id +",'"+ m_type +"',"+containerid+","+m_position
                + ","+m_construct_id + ",'"+m_status+"',"+is_addition_info+")";
           }
           else 
           {
              sql = "insert into sample (sampleid, sampletype, containerid, containerposition,"
                  +"    status_gb, additionalinfo)"
                 + "values ("+ m_id +",'"+ m_type +"',"+containerid+","+m_position
                + ",'"+m_status+"',"+is_addition_info+")";
           }
       
        DatabaseTransaction.executeUpdate(sql,conn);
            
        sql = "insert into containercell (containerid, position, sampleid) " +
            "values(" + containerid + ","+m_position+","+m_id+")";
        DatabaseTransaction.executeUpdate(sql, conn);
         PublicInfoItem.insertPublicInfo(  conn, "SAMPLE_NAME", 
                m_additional_info, m_id, "SAMPLEID",                true, errors) ;
         
   
       
      }
       public void insertMGC(Connection conn,  int containerid, ArrayList errors) throws Exception
     {
           PublicInfoItem pi_temp = null;
           
           pi_temp =  PublicInfoItem.getPublicInfoByName("VECTOR",m_additional_info);
           if (pi_temp == null)  throw new Exception("No vector set for the ");
           String vector =  pi_temp.getValue();
        
           String mgc_status = ( m_sequence_id != null)? MgcSample.STATUS_AVAILABLE: MgcSample.STATUS_NO_SEQUENCE;
           
           String orientation = null;
           pi_temp =  PublicInfoItem.getPublicInfoByName("ORIENTATION",m_additional_info);
           if ( pi_temp != null)
           {
               orientation = pi_temp.getValue();
           }
           else
           {
               orientation = String.valueOf( MgcSample.ORIENTATION_NOTKNOWN );
           }
             
           String row =  null; String column = null;
           //if they were not defined
           if ( PublicInfoItem.getPublicInfoByName("ROW",m_additional_info)== null ||
                   PublicInfoItem.getPublicInfoByName("COLUMN",m_additional_info) == null )
           {
               String temp = Algorithms.convertWellFromInttoA8_12(m_position);
               column = temp.substring(1);
               row = String.valueOf( temp.charAt(0));
               pi_temp =  PublicInfoItem.getPublicInfoByName("COLUMN",m_additional_info);
               pi_temp =  PublicInfoItem.getPublicInfoByName("ROW",m_additional_info);
           }
           else
           {
               pi_temp =  PublicInfoItem.getPublicInfoByName("ROW",m_additional_info);
               row =  pi_temp.getValue();
               pi_temp =  PublicInfoItem.getPublicInfoByName("COLUMN",m_additional_info);
               column = pi_temp.getValue();
            }
           
         
            
             int is_addition_info = PublicInfoItem.isAnyPublicInfoForSubmission(m_additional_info )? 1:0;
             if (m_id == -1)        m_id = FlexIDGenerator.getID("sampleid");
      
           String sql = "insert into sample (sampleid, sampletype, containerid, containerposition,"
          +"        status_gb, additionalinfo)"
         + "values ("+ m_id +",'"+ m_type +"',"+containerid+","+m_position
        +  ",'"+m_status+"',"+is_addition_info+")";
       
           DatabaseTransaction.executeUpdate(sql,conn);

            sql = "insert into containercell (containerid, position, sampleid) " +
                "values(" + containerid + ","+m_position+","+m_id+")";
            DatabaseTransaction.executeUpdate(sql, conn);
             
           pi_temp =  PublicInfoItem.getPublicInfoByName(FlexSequence.MGC_ID,m_additional_info);
           String mgc_id = ( pi_temp == null) ? "-1" : pi_temp.getValue();
        
           pi_temp =  PublicInfoItem.getPublicInfoByName(FlexSequence.IMAGE_ID,m_additional_info);
           String image_id = ( pi_temp == null) ? "-1" : pi_temp.getValue();
         
           sql = "insert into mgcclone (mgccloneid, mgcid, imageid, vector, orgrow, orgcol, "
            + " sequenceid, status, orientation)  values ("+ m_id + "," + mgc_id +","+ image_id +
            ",'"+ vector + "','"+ row+"',"+ column + "," + m_sequence_id + ",'" + mgc_status + "',"+ orientation +")" ;
           DatabaseTransaction.executeUpdate(sql, conn);
           
       }
       
       
       public static void populateObtainedMasterprogressTables_2(Connection conn, Collection samples             ) throws Exception
       {
            ResultSet rs = null;   
            String clone_type= null; String clone_status = null;
            int cloning_strategy = -1;
            PublicInfoItem  p_info = null;
            ImportSample sample = null;
               String sample_storage_form = null;              
             String sample_storage_type = null;
             int field_count = 0;int clone_id = 0;
             
  String sqlQuery_populateCloningprogressTable = "select statusid, constructid from cloningprogress"+
        " where constructid in (select constructid from sample where sampleid=?)";
  String sql_populateCloningprogressTable = "insert into cloningprogress select distinct constructid, ? "+
        " from sample where sampleid=?";
  String sqlUpdate_populateCloningprogressTable = "update cloningprogress set statusid= ?"+ConstructInfo.CLONE_OBTAINED_ID+
        " where constructid=?";
  String sql_populateObtainedmastercloneTable = "insert into obtainedmasterclone"+
        " select mastercloneid.nextval, s.sampleid, s.containerid,"+
        " c.label, s.containerposition, s.sampletype, s.constructid"+
        " from sample s, containerheader c where s.containerid=c.containerid  and s.sampleid=?";
  
  //first ? - clonesid.nextval
String sql_populateClonesTable = "insert into clones (CLONEID,CLONENAME,"+
        " CLONETYPE,MASTERCLONEID,SEQUENCEID,STRATEGYID,COMMENTS,STATUS,CONSTRUCTID)"+
        " select ?, null, ?, o.mastercloneid,"+
        " c.sequenceid, ?, null, ?, c.constructid"+
        " from obtainedmasterclone o, constructdesign c, sample s"+
        " where o.sampleid=s.sampleid  and s.constructid=c.constructid and s.sampleid=?";
            
String sql_populateStorageTable = "insert into clonestorage"+
        " select storageid.nextval, o.sampleid, ?,"+
        " ?, c.cloneid, o.containerid, o.containerlabel, o.containerposition"+
        " from obtainedmasterclone o, clones c  where o.mastercloneid=c.mastercloneid  and o.sampleid=?";
 //insert into clonestorage  select storageid.nextval, o.sampleid, 'Original Storage',"+
     //   " 'GLYCEROL', c.cloneid, o.containerid, o.containerlabel, o.containerposition"+
        
String sql_updateSampleTable = "update sample set cloneid= ? where sampleid=? and cloneid is null";

 String sql_updateSequenceTable = "update flexsequence set flexstatus='"+FlexSequence.CLONE_OBTAINED+"'"+
        " where sequenceid in (select distinct sequenceid"+
        " from constructdesign where constructid in ("+
        " select distinct constructid from sample where sampleid=?))"+
        " and (flexstatus in('"+FlexSequence.INPROCESS+"','"+FlexSequence.FAILED+"','"
        +FlexSequence.FAILED_CLONING+"','"+FlexSequence.PENDING+"','"+FlexSequence.REJECTED+"'))";
            
PreparedStatement stmt_populateCloningprogressTable = conn.prepareStatement(sql_populateCloningprogressTable);
PreparedStatement stmtQuery_populateCloningprogressTable = conn.prepareStatement(sqlQuery_populateCloningprogressTable);
PreparedStatement stmtUpdate_populateCloningprogressTable = conn.prepareStatement(sqlUpdate_populateCloningprogressTable);
PreparedStatement stmt_populateObtainedmastercloneTable = conn.prepareStatement(sql_populateObtainedmastercloneTable);        
PreparedStatement stmt_populateClonesTable = conn.prepareStatement(sql_populateClonesTable);
PreparedStatement stmt_populateStorageTable = conn.prepareStatement(sql_populateStorageTable);
PreparedStatement stmt_updateSampleTable = conn.prepareStatement(sql_updateSampleTable);
PreparedStatement stmt_updateSequenceTable = conn.prepareStatement(sql_updateSequenceTable);
        Iterator iter =   samples.iterator();
        try
        {
            while ( iter.hasNext())
            {

            //populateCloningprogressTable 
                sample = (ImportSample)iter.next();
                clone_status = sample.getCloneStatus();
                clone_type = sample.getCloneType();
                cloning_strategy = sample.getCloningStrategyId();
                clone_id = FlexIDGenerator.getID("clonesid");

                if ( clone_type == null || clone_status == null || cloning_strategy ==-1) 
                {
                    throw new Exception("Cannot submit sample into clones table (clone type, clone status or cloning strategy is not valid: sample info "+sample.toString());
                }
                if ( !ImportClone.isValidCLoneStatus(clone_status))
                    throw new Exception("Cannot submit sample into clones table clone status is not valid: sample info "+sample.toString());

                stmtQuery_populateCloningprogressTable.setInt(1, sample.getId());
                rs = DatabaseTransaction.executeQuery(stmtQuery_populateCloningprogressTable);
                if(rs.next()) 
                {
                    int statusid=rs.getInt(1);
                    int constructid = rs.getInt(2);
                    if(statusid == ConstructInfo.SEQUENCE_REJECTED_ID || statusid == ConstructInfo.FAILED_CLONING_ID) 
                    {
                        stmtUpdate_populateCloningprogressTable.setInt(1, constructid);
                        DatabaseTransaction.executeUpdate(stmtUpdate_populateCloningprogressTable);
                    }
               } 
                else
                {
                    stmt_populateCloningprogressTable.setInt(1,ImportClone. mapCloneStatusToConstructStatus(clone_status) );
                    stmt_populateCloningprogressTable.setInt(2, sample.getId());
                    DatabaseTransaction.executeUpdate(stmt_populateCloningprogressTable);
                }
                stmt_populateObtainedmastercloneTable.setInt(1, sample.getId());
                DatabaseTransaction.executeUpdate(stmt_populateObtainedmastercloneTable);


                field_count = 1;
                stmt_populateClonesTable.setInt(field_count++, clone_id);
                stmt_populateClonesTable.setString(field_count++, clone_type);
                stmt_populateClonesTable.setInt(field_count++, cloning_strategy);
                stmt_populateClonesTable.setString(field_count++, clone_status);
                stmt_populateClonesTable.setInt(field_count++, sample.getId());
                DatabaseTransaction.executeUpdate(stmt_populateClonesTable);

                
                p_info =  PublicInfoItem.getPublicInfoByName("CLONE_STORAGE_FORM", sample.getPublicInfo());
                if (p_info == null) throw new Exception();
                sample_storage_form = p_info.getValue();
                
                for (int t_count = 0; t_count < 10; t_count++)
                {
                    p_info =  PublicInfoItem.getPublicInfoByName("CLONE_STORAGE_TYPE_"+ t_count, sample.getPublicInfo());
                    if (p_info == null )
                    {
                        if ( t_count == 0 )
                            throw new Exception();
                        else 
                            break;
                    }
                    sample_storage_type = p_info.getValue();
                    stmt_populateStorageTable.setString(1, sample_storage_type);// type of storage
                    stmt_populateStorageTable.setString(2, sample_storage_form);// form
                    stmt_populateStorageTable.setInt(3, sample.getId());
                    DatabaseTransaction.executeUpdate(stmt_populateStorageTable);
                }

                //change to insure that executed only for onese
                field_count = 1;
                stmt_updateSampleTable.setInt(field_count++, clone_id);
                stmt_updateSampleTable.setInt(field_count++, sample.getId());
                DatabaseTransaction.executeUpdate(stmt_updateSampleTable);
                
                stmt_updateSequenceTable.setInt(1, sample.getId());
                DatabaseTransaction.executeUpdate(stmt_updateSequenceTable);
            }
        }
        catch(Exception e)
        {
            throw new Exception ("Cannot feel in cloning tables, check submiision files."+e.getMessage());
        }
        finally
        {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmtQuery_populateCloningprogressTable);
            DatabaseTransaction.closeStatement(stmtUpdate_populateCloningprogressTable);
            DatabaseTransaction.closeStatement(stmt_populateCloningprogressTable);
            DatabaseTransaction.closeStatement(stmt_populateObtainedmastercloneTable);
            DatabaseTransaction.closeStatement(stmt_populateClonesTable);
            DatabaseTransaction.closeStatement(stmt_populateStorageTable);
            DatabaseTransaction.closeStatement(stmt_updateSampleTable);
            DatabaseTransaction.closeStatement(stmt_updateSequenceTable);
        }
    }

  
   
    public static void populateObtainedMasterprogressTables(Connection conn, Collection samples             ) throws Exception
       {
            ResultSet rs = null;   
            PublicInfoItem  p_info = null;
            ImportSample sample = null;ImportClone clone = null;
               String clone_storage_form = null;              
             String clone_storage_type = null;
             int field_count = 0;
             
  String sqlQuery_populateCloningprogressTable = "select statusid, constructid from cloningprogress"+
        " where constructid in (select constructid from sample where sampleid=?)";
  String sql_populateCloningprogressTable = "insert into cloningprogress select distinct constructid, ? "+
        " from sample where sampleid=?";
  String sqlUpdate_populateCloningprogressTable = "update cloningprogress set statusid= ?"+ConstructInfo.CLONE_OBTAINED_ID+
        " where constructid=?";
  String sql_populateObtainedmastercloneTable = "insert into obtainedmasterclone"+
        " select mastercloneid.nextval, s.sampleid, s.containerid,"+
        " c.label, s.containerposition, s.sampletype, s.constructid"+
        " from sample s, containerheader c where s.containerid=c.containerid  and s.sampleid=?";
  
  //first ? - clonesid.nextval
String sql_populateClonesTable = "insert into clones (CLONEID,CLONENAME,"+
        " CLONETYPE,MASTERCLONEID,SEQUENCEID,STRATEGYID,COMMENTS,STATUS,CONSTRUCTID)"+
        " select ?, null, ?, o.mastercloneid,"+
        " c.sequenceid, ?, null, ?, c.constructid"+
        " from obtainedmasterclone o, constructdesign c, sample s"+
        " where o.sampleid=s.sampleid  and s.constructid=c.constructid and s.sampleid=?";
            
String sql_populateStorageTable = "insert into clonestorage"+
        " select storageid.nextval, o.sampleid, ?,"+
        " ?, c.cloneid, o.containerid, o.containerlabel, o.containerposition"+
        " from obtainedmasterclone o, clones c  where o.mastercloneid=c.mastercloneid  and o.sampleid=?";
 //insert into clonestorage  select storageid.nextval, o.sampleid, 'Original Storage',"+
     //   " 'GLYCEROL', c.cloneid, o.containerid, o.containerlabel, o.containerposition"+
        
String sql_updateSampleTable = "update sample set cloneid= ? where sampleid=? and cloneid is null";

 String sql_updateSequenceTable = "update flexsequence set flexstatus='"+FlexSequence.CLONE_OBTAINED+"'"+
        " where sequenceid in (select distinct sequenceid"+
        " from constructdesign where constructid in ("+
        " select distinct constructid from sample where sampleid=?))"+
        " and (flexstatus in('"+FlexSequence.INPROCESS+"','"+FlexSequence.FAILED+"','"
        +FlexSequence.FAILED_CLONING+"','"+FlexSequence.PENDING+"','"+FlexSequence.REJECTED+"'))";
            
PreparedStatement stmt_populateCloningprogressTable = conn.prepareStatement(sql_populateCloningprogressTable);
PreparedStatement stmtQuery_populateCloningprogressTable = conn.prepareStatement(sqlQuery_populateCloningprogressTable);
PreparedStatement stmtUpdate_populateCloningprogressTable = conn.prepareStatement(sqlUpdate_populateCloningprogressTable);
PreparedStatement stmt_populateObtainedmastercloneTable = conn.prepareStatement(sql_populateObtainedmastercloneTable);        
PreparedStatement stmt_populateClonesTable = conn.prepareStatement(sql_populateClonesTable);
PreparedStatement stmt_populateStorageTable = conn.prepareStatement(sql_populateStorageTable);
PreparedStatement stmt_updateSampleTable = conn.prepareStatement(sql_updateSampleTable);
PreparedStatement stmt_updateSequenceTable = conn.prepareStatement(sql_updateSequenceTable);
        Iterator iter =   samples.iterator();
        try
        {
            while ( iter.hasNext())
            {

            //populateCloningprogressTable 
                sample = (ImportSample)iter.next();
                clone = sample.getClone();
                if ( clone == null) continue;
                //clone_status = sample.getCloneStatus();
                //clone_type = sample.getCloneType();
                //cloning_strategy = sample.getCloningStrategyId();
                clone.setId( FlexIDGenerator.getID("clonesid"));

                if ( clone.getType() == null || clone.getStatus() == null || clone.getCloningStrategyId() ==-1) 
                {
                    throw new Exception("Cannot submit sample into clones table (clone type, clone status or cloning strategy is not valid: sample info "+sample.toString());
                }
                if ( !clone.isValidCLoneStatus())
                    throw new Exception("Cannot submit sample into clones table clone status is not valid: sample info "+sample.toString());

                stmtQuery_populateCloningprogressTable.setInt(1, sample.getId());
                rs = DatabaseTransaction.executeQuery(stmtQuery_populateCloningprogressTable);
                if(rs.next()) 
                {
                    int statusid=rs.getInt(1);
                    int constructid = rs.getInt(2);
                    if(statusid == ConstructInfo.SEQUENCE_REJECTED_ID || statusid == ConstructInfo.FAILED_CLONING_ID) 
                    {
                        stmtUpdate_populateCloningprogressTable.setInt(1, constructid);
                        DatabaseTransaction.executeUpdate(stmtUpdate_populateCloningprogressTable);
                    }
               } 
                else
                {
                    stmt_populateCloningprogressTable.setInt(1, clone.mapCloneStatusToConstructStatus() );
                    stmt_populateCloningprogressTable.setInt(2, sample.getId());
                    DatabaseTransaction.executeUpdate(stmt_populateCloningprogressTable);
                }
                stmt_populateObtainedmastercloneTable.setInt(1, sample.getId());
                DatabaseTransaction.executeUpdate(stmt_populateObtainedmastercloneTable);


                field_count = 1;
                stmt_populateClonesTable.setInt(field_count++, clone.getId());
                stmt_populateClonesTable.setString(field_count++, clone.getType());
                stmt_populateClonesTable.setInt(field_count++, clone.getCloningStrategyId());
                stmt_populateClonesTable.setString(field_count++, clone.getStatus());
                stmt_populateClonesTable.setInt(field_count++, sample.getId());
                DatabaseTransaction.executeUpdate(stmt_populateClonesTable);

                
                p_info =  PublicInfoItem.getPublicInfoByName("CLONE_STORAGE_FORM", clone.getPublicInfo());
                if (p_info == null) throw new Exception();
                clone_storage_form = p_info.getValue();
                
                for (int t_count = 0; t_count < 10; t_count++)
                {
                    p_info =  PublicInfoItem.getPublicInfoByName("CLONE_STORAGE_TYPE_"+ t_count, clone.getPublicInfo());
                    if (p_info == null )
                    {
                        if ( t_count == 0 )
                            throw new Exception();
                        else 
                            break;
                    }
                    clone_storage_type = p_info.getValue();
                    stmt_populateStorageTable.setString(1, clone_storage_type);// type of storage
                    stmt_populateStorageTable.setString(2, clone_storage_form);// form
                    stmt_populateStorageTable.setInt(3, sample.getId());
                    DatabaseTransaction.executeUpdate(stmt_populateStorageTable);
                }

                //change to insure that executed only for onese
                field_count = 1;
                stmt_updateSampleTable.setInt(field_count++, clone.getId());
                stmt_updateSampleTable.setInt(field_count++, sample.getId());
                DatabaseTransaction.executeUpdate(stmt_updateSampleTable);
                
                stmt_updateSequenceTable.setInt(1, sample.getId());
                DatabaseTransaction.executeUpdate(stmt_updateSequenceTable);
                
                
                // submit public info for clone 
                 PublicInfoItem.insertPublicInfo(  conn, "cloneNAME", 
                  clone.getPublicInfo(), clone.getId(), "CLONEID",                true, new ArrayList()) ;
                 clone.insertAuthor(conn);
            }
        }
        catch(Exception e)
        {
            throw new Exception ("Cannot feel in cloning tables, check submiision files."+e.getMessage());
        }
        finally
        {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmtQuery_populateCloningprogressTable);
            DatabaseTransaction.closeStatement(stmtUpdate_populateCloningprogressTable);
            DatabaseTransaction.closeStatement(stmt_populateCloningprogressTable);
            DatabaseTransaction.closeStatement(stmt_populateObtainedmastercloneTable);
            DatabaseTransaction.closeStatement(stmt_populateClonesTable);
            DatabaseTransaction.closeStatement(stmt_populateStorageTable);
            DatabaseTransaction.closeStatement(stmt_updateSampleTable);
            DatabaseTransaction.closeStatement(stmt_updateSequenceTable);
        }
    }

  
  
   
   
    public int          getCloningStrategyID(Hashtable cloning_strategies)
            throws Exception
     {
         int[] result = {-1,-1,-1};
         String vector_name = null;String linker_5p_name = null;String linker_3p_name = null;
         PublicInfoItem temp = null;
         
         temp = PublicInfoItem.getPublicInfoByName(ImportSample.SAMPLE_VECTOR,m_additional_info);
         if ( temp != null) vector_name = temp.getValue();
         temp = PublicInfoItem.getPublicInfoByName(ImportSample.SAMPLE_FIVE_PRIME_LINKER,m_additional_info);
         if ( temp != null)linker_5p_name = temp.getValue();
         temp = PublicInfoItem.getPublicInfoByName(ImportSample.SAMPLE_THREE_PRIME_LINKER,m_additional_info);
         if ( temp != null) linker_3p_name = temp.getValue();
        
        if (vector_name == null || linker_5p_name == null || linker_3p_name == null)
            return -1;
         
         String strategy_key =  vector_name+"_"+linker_5p_name +"_"+ linker_3p_name;
         // try to get cloning strategies for this parameters
         if ( cloning_strategies.get(strategy_key) != null)
         {
             return ((Integer)cloning_strategies.get(strategy_key)).intValue();
         }
         else
         {
             int clstr_id = CloningStrategy.findStrategyByVectorAndLinker( vector_name,  linker_5p_name, linker_3p_name);
             if (clstr_id == -1) return -1;
             
             cloning_strategies.put(strategy_key, new Integer(clstr_id));
             return clstr_id;
         }
       
     }
}
