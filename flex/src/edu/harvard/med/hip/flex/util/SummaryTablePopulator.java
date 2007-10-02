/*
 * SummaryTablePopulator.java
 *
 * Created on November 24, 2003, 2:10 PM
 */

package edu.harvard.med.hip.flex.util;

import java.util.*;
import java.sql.*;
import javax.mail.*;
import javax.mail.internet.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.util.Mailer;
import edu.harvard.med.hip.flex.core.*;

/**
 *
 * @author  DZuo
 */
public class SummaryTablePopulator {
    int numConstructs;
    int numMasterclones;
    int numClones;
    int numClonestorage;
    int numSamples;
    int numSeqs;
    int numFailedConstructs;
    int numFailedSeqs;
    boolean isFailedConstructs;
    
    /** Creates a new instance of SummaryTablePopulator */
    public SummaryTablePopulator() {
    }
    
    public boolean populateObtainedMasterClonesWithSamples(List samples, int strategyid, String cloneType) {
        DatabaseTransaction t = null;
        Connection conn = null;
        
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            numConstructs = populateCloningprogressTable(samples, conn);
            numMasterclones = populateObtainedmastercloneTable(samples, conn);
            numClones = populateClonesTable(samples, strategyid, cloneType, conn);
            numClonestorage = populateOriginalStorageTable(samples, conn);
            numSamples = updateSampleTable(samples, conn);
            numSeqs = updateSequenceTable(samples, conn);
            
            DatabaseTransaction.commit(conn);
            
            return true;
        } catch (Exception ex) {
            System.out.println(ex);
            DatabaseTransaction.rollback(conn);
            
            return false;
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
    }
    
    public boolean populateObtainedMasterClonesWithContainers(List containers, int strategyid, String cloneType) {
        try {
            List samples = getSamples(containers);
            if(populateObtainedMasterClonesWithSamples(samples, strategyid, cloneType)) {
                populateFailedConstructs(containers);
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            System.out.println(ex);
            return false;
        }
    }
    
    public List getSamples(List containers) throws FlexDatabaseException, SQLException {
        String sql = "select sampleid from sample where containerid=? and sampletype='ISOLATE'";
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        Connection conn = t.requestConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        List samples = new ArrayList();
        
        for(int i=0; i<containers.size(); i++) {
            Integer container = (Integer)containers.get(i);
            int containerid = container.intValue();
            stmt.setInt(1, containerid);
            ResultSet rs = DatabaseTransaction.executeQuery(stmt);
            while(rs.next()) {
                int sampleid = rs.getInt(1);
                samples.add(new Integer(sampleid));
            }
            DatabaseTransaction.closeResultSet(rs);
        }
        
        DatabaseTransaction.closeStatement(stmt);
        DatabaseTransaction.closeConnection(conn);
        
        return samples;
    }
   
    public List getNonIsolateSamples(List containers) throws FlexDatabaseException, SQLException {
        String sql = "select sampleid from sample where containerid=? and sampletype<>'ISOLATE'";
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        Connection conn = t.requestConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        List samples = new ArrayList();
        
        for(int i=0; i<containers.size(); i++) {
            Integer container = (Integer)containers.get(i);
            int containerid = container.intValue();
            stmt.setInt(1, containerid);
            ResultSet rs = DatabaseTransaction.executeQuery(stmt);
            while(rs.next()) {
                int sampleid = rs.getInt(1);
                samples.add(new Integer(sampleid));
            }
            DatabaseTransaction.closeResultSet(rs);
        }
        
        DatabaseTransaction.closeStatement(stmt);
        DatabaseTransaction.closeConnection(conn);
        
        return samples;
    }
     
    public int populateCloningprogressTable(List samples, Connection conn) throws FlexDatabaseException, SQLException {
        String sqlQuery = "select statusid, constructid from cloningprogress"+
        " where constructid in"+
        " (select constructid from sample where sampleid=?)";
        String sql = "insert into cloningprogress"+
        " select distinct constructid, 1"+
        " from sample where sampleid=?";
        String sqlUpdate = "update cloningprogress"+
        " set statusid="+ConstructInfo.CLONE_OBTAINED_ID+
        " where constructid=?";
        
        PreparedStatement stmt = conn.prepareStatement(sql);
        PreparedStatement stmtQuery = conn.prepareStatement(sqlQuery);
        PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate);
        ResultSet rs = null;
        
        int ret = 0;
        
        for (int i=0; i<samples.size(); i++) {
            Integer sample = (Integer)samples.get(i);
            int sampleid = sample.intValue();
            stmtQuery.setInt(1, sampleid);
            rs = DatabaseTransaction.executeQuery(stmtQuery);
            if(rs.next()) {
                int statusid=rs.getInt(1);
                int constructid = rs.getInt(2);
                if(statusid == ConstructInfo.SEQUENCE_REJECTED_ID || statusid == ConstructInfo.FAILED_CLONING_ID) {
                    stmtUpdate.setInt(1, constructid);
                    DatabaseTransaction.executeUpdate(stmtUpdate);
                    ret ++;
                }
            } else {
                stmt.setInt(1, sampleid);
                int num = DatabaseTransaction.executeUpdate(stmt);
                ret += num;
            }
        }
        
        DatabaseTransaction.closeResultSet(rs);
        DatabaseTransaction.closeStatement(stmtQuery);
        DatabaseTransaction.closeStatement(stmtUpdate);
        DatabaseTransaction.closeStatement(stmt);
        
        return ret;
    }
    
    public int populateObtainedmastercloneTable(List samples, Connection conn) throws FlexDatabaseException, SQLException {
        String sql = "insert into obtainedmasterclone"+
        " select mastercloneid.nextval, s.sampleid, s.containerid,"+
        " c.label, s.containerposition, s.sampletype, s.constructid"+
        " from sample s, containerheader c"+
        " where s.containerid=c.containerid"+
        " and s.sampleid=?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        int ret = 0;
        
        for(int i=0; i<samples.size(); i++) {
            Integer sample = (Integer)samples.get(i);
            int sampleid = sample.intValue();
            stmt.setInt(1, sampleid);
            int num = DatabaseTransaction.executeUpdate(stmt);
            ret += num;
        }
        
        DatabaseTransaction.closeStatement(stmt);
        
        return ret;
    }
    
    public int populateClonesTable(List samples, int cloningStrategy, String cloneType, Connection conn) throws FlexDatabaseException, SQLException {
        String sql = "insert into clones(CLONEID,CLONENAME,"+
        " CLONETYPE,MASTERCLONEID,SEQUENCEID,STRATEGYID,COMMENTS,STATUS,CONSTRUCTID)"+
        " select clonesid.nextval, null, ?, o.mastercloneid,"+
        " c.sequenceid, ?, null, 'UNSEQUENCED', c.constructid"+
        " from obtainedmasterclone o, constructdesign c, sample s"+
        " where o.sampleid=s.sampleid"+
        " and s.constructid=c.constructid"+
        " and s.sampleid=?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, cloneType);
        stmt.setInt(2, cloningStrategy);
        int ret = 0;
        
        for(int i=0; i<samples.size(); i++) {
            Integer sample = (Integer)samples.get(i);
            int sampleid = sample.intValue();
            stmt.setInt(3, sampleid);
            int num = DatabaseTransaction.executeUpdate(stmt);
            ret += num;
        }
        
        DatabaseTransaction.closeStatement(stmt);
        
        return ret;
    }

    public int populateOriginalStorageTable(List samples, Connection conn) throws FlexDatabaseException, SQLException {
        String sql = "insert into clonestorage"+
        " select storageid.nextval, o.sampleid, 'Original Storage',"+
        " 'GLYCEROL', c.cloneid, o.containerid, o.containerlabel, o.containerposition"+
        " from obtainedmasterclone o, clones c"+
        " where o.mastercloneid=c.mastercloneid"+
        " and o.sampleid=?";

        PreparedStatement stmt = conn.prepareStatement(sql);
        int ret = 0;
        
        for (int i=0; i<samples.size(); i++) {
            Integer sample = (Integer)samples.get(i);
            int sampleid = sample.intValue();
            stmt.setInt(1, sampleid);
            int num = DatabaseTransaction.executeUpdate(stmt);
            ret += num;
        }
        
        DatabaseTransaction.closeStatement(stmt);
        
        return ret;
    }
    
    public int populateExpressionClones(List samples, int cloningStrategy, Connection conn) throws FlexDatabaseException, SQLException {
        String sql = "insert into clones (CLONEID,CLONENAME,"+
        " CLONETYPE,MASTERCLONEID,SEQUENCEID,STRATEGYID,COMMENTS,STATUS,CONSTRUCTID)"+
        " select ?, null, ?, cl.mastercloneid,"+
        " c.sequenceid, ?, null, 'UNSEQUENCED', c.constructid"+
        " from constructdesign c, sample s, clones cl"+
        " where s.constructid=c.constructid"+
        " and s.cloneid=cl.cloneid"+
        " and s.sampleid=?";
        String sql2 = "update sample set cloneid=? where sampleid=?";
        
        PreparedStatement stmt = conn.prepareStatement(sql);
        PreparedStatement stmt2 = conn.prepareStatement(sql2);
        stmt.setString(2, CloneInfo.EXPRESSION_CLONE);
        stmt.setInt(3, cloningStrategy);
        int ret = 0;
        
        for(int i=0; i<samples.size(); i++) {
            int cloneid = FlexIDGenerator.getID("clonesid");
            Integer sample = (Integer)samples.get(i);
            int sampleid = sample.intValue();
            stmt.setInt(1, cloneid);
            stmt.setInt(4, sampleid);
            int num = DatabaseTransaction.executeUpdate(stmt);
            ret += num;
            
            stmt2.setInt(1, cloneid);
            stmt2.setInt(2, sampleid);
            DatabaseTransaction.executeUpdate(stmt2);
        }
        
        DatabaseTransaction.closeStatement(stmt);
        DatabaseTransaction.closeStatement(stmt2);
        
        return ret;
    }
    
    public void resetExpressSampleCloneids(List samples, Connection conn) throws Exception {
        String sql = "update sample set cloneid=null where sampleid=?";
        PreparedStatement stmt = conn.prepareStatement(sql);
       for(int i=0; i<samples.size(); i++) {
            Integer sample = (Integer)samples.get(i);
            int sampleid = sample.intValue();
            stmt.setInt(1, sampleid);
            DatabaseTransaction.executeUpdate(stmt);
        }
        DatabaseTransaction.closeStatement(stmt);
    }
        
    public boolean populateExpressionClonesWithContainers(List containers, int strategyid) {
        DatabaseTransaction t = null;
        Connection conn = null;
        
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            List samples = getSamples(containers);
            List nonIsolateSamples = getNonIsolateSamples(containers);
            numClones = populateExpressionClones(samples, strategyid, conn);  
            resetExpressSampleCloneids(nonIsolateSamples,conn);
            numClonestorage = populateClonestorageTableWithContainerids(containers, StorageType.WORKING, StorageForm.GLYCEROL, conn);
            DatabaseTransaction.commit(conn);
            
            return true;
        } catch (Exception ex) {
            System.out.println(ex);
            DatabaseTransaction.rollback(conn);
            
            return false;
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
    }
    
    public int populateClonestorageTableWithContainers(List containers, String storageType, String storageForm, Connection conn) throws FlexDatabaseException, SQLException {
        String sql = "insert into clonestorage"+
        " select storageid.nextval, s.sampleid, '"+storageType+"',"+
        " '"+storageForm+"', s.cloneid, c.containerid, c.label, s.containerposition"+
        " from sample s, containerheader c"+
        " where s.containerid=c.containerid"+
        " and s.sampletype='ISOLATE'"+
        " and c.containerid=?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        int ret = 0;
        
        for (int i=0; i<containers.size(); i++) {
            Container container = (Container)containers.get(i);
            int containerid = container.getId();
            stmt.setInt(1, containerid);
            int num = DatabaseTransaction.executeUpdate(stmt);
            ret += num;
        }
        
        DatabaseTransaction.closeStatement(stmt);
        
        return ret;
    }
  
        
    public int populateClonestorageTableWithContainerids(List containers, String storageType, String storageForm, Connection conn) throws FlexDatabaseException, SQLException {
        String sql = "insert into clonestorage"+
        " select storageid.nextval, s.sampleid, '"+storageType+"',"+
        " '"+storageForm+"', s.cloneid, c.containerid, c.label, s.containerposition"+
        " from sample s, containerheader c"+
        " where s.containerid=c.containerid"+
        " and s.sampletype='ISOLATE'"+
        " and c.containerid=?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        int ret = 0;
        
        for (int i=0; i<containers.size(); i++) {
            Integer container = (Integer)containers.get(i);
            int containerid = container.intValue();
            stmt.setInt(1, containerid);
            int num = DatabaseTransaction.executeUpdate(stmt);
            ret += num;
        }
        
        DatabaseTransaction.closeStatement(stmt);
        
        return ret;
    }
    
    public int updateSampleTable(List samples, Connection conn) throws FlexDatabaseException, SQLException {
        String sql = "update sample set cloneid="+
        " (select cloneid from clonestorage"+
        " where storagesampleid=?)"+
        " where sampleid=? and cloneid is null";
        
        PreparedStatement stmt = conn.prepareStatement(sql);
        int ret = 0;
        
        for(int i=0; i<samples.size(); i++) {
            Integer sample = (Integer)samples.get(i);
            int sampleid = sample.intValue();
            
            stmt.setInt(1, sampleid);
            stmt.setInt(2, sampleid);
            int num = DatabaseTransaction.executeUpdate(stmt);
            ret += num;
        }
        
        DatabaseTransaction.closeStatement(stmt);
        
        return ret;
    }
    
    public int updateSequenceTable(List samples, Connection conn) throws FlexDatabaseException, SQLException {
        String sql = "update flexsequence set flexstatus='"+FlexSequence.CLONE_OBTAINED+"'"+
        " where sequenceid in (select distinct sequenceid"+
        " from constructdesign where constructid in ("+
        " select distinct constructid from sample where sampleid=?))"+
        " and (flexstatus='"+FlexSequence.INPROCESS+"'"+
        " or flexstatus='"+FlexSequence.FAILED+"'"+
        " or flexstatus='"+FlexSequence.FAILED_CLONING+"'"+
        " or flexstatus='"+FlexSequence.PENDING+"'"+
        " or flexstatus='"+FlexSequence.REJECTED+"')";
        
        PreparedStatement stmt = conn.prepareStatement(sql);
        int ret = 0;
        
        for(int i=0; i<samples.size(); i++) {
            Integer sample = (Integer)samples.get(i);
            int sampleid = sample.intValue();
            
            stmt.setInt(1, sampleid);
            int num = DatabaseTransaction.executeUpdate(stmt);
            ret += num;
        }
        
        DatabaseTransaction.closeStatement(stmt);
        
        return ret;
    }
    
    public void sendEmail(boolean isSuccessful, List ids) {
        String msg = "===========================================================\n";
        String to = "dongmei_zuo@hms.harvard.edu";
        String from = "dongmei_zuo@hms.harvard.edu";
        String cc = "dongmei_zuo@hms.harvard.edu";
        String subject = "Summary table populating - ";
        
        java.util.Date d = new java.util.Date();
        java.text.SimpleDateFormat f = new java.text.SimpleDateFormat("MM/dd/yyyy");
        msg = msg + f.format(d) + "\n";
        
        if(isSuccessful) {
            msg = msg + "Summary tables populated successfully.\n";
            msg = msg + "\tNumber of constructs inserted into cloningprogress table: "+numConstructs+"\n";
            msg = msg + "\tNumber of clones inserted into obtainedmasterclone table: "+numMasterclones+"\n";
            msg = msg + "\tNumber of clones inserted into clones table: "+numClones+"\n";
            msg = msg + "\tNumber of clones inserted inot clonestorage table: "+numClonestorage+"\n";
            msg = msg + "\tNumber of samples: "+numSamples+"\n";
            subject = subject + "successful";
        } else {
            msg = msg + "Summary tables populated unsuccessfully.\n";
            subject = subject + "failed";
        }
        
        msg = msg + "\n===========================================================\n";
        for(int i=0; i<ids.size(); i++) {
            msg = msg + "\t" + ids.get(i).toString()+"\n";
        }
        msg = msg + "\n===========================================================\n";
        
        if(isFailedConstructs) {
            msg = msg + "Populating failed constructs successful.\n";
            msg = msg + "\tNumber of failed constructs: "+numFailedConstructs+"\n";
            msg = msg + "\tNumber of failed sequences: "+numFailedSeqs+"\n";
        } else {
            msg = msg + "Populating failed constructs failed.\n";
        }
        
        try {
            Mailer.sendMessage(to, from, cc, subject, msg);
        } catch (MessagingException ex) {
            System.out.println(ex);
        }
    }
    
    public void populateFailedConstructs(List containers) {
        String sql = "select distinct constructid from sample"+
        " where sampletype='EMPTY'"+
        " and containerid=?"+
        " and constructid not in ("+
        " select distinct constructid from sample"+
        " where sampletype='ISOLATE'"+
        " and containerid=?)";
        String sql2 = "select count(*) from cloningprogress where constructid=?";
        String sql3 = "select sequenceid, flexstatus from flexsequence"+
        " where sequenceid in ("+
        " select distinct sequenceid from constructdesign"+
        " where constructid=?)";
        String sql4 = "insert into cloningprogress values(?, "+ConstructInfo.FAILED_CLONING_ID+")";
        String sql5 = "update flexsequence set flexstatus='"+FlexSequence.FAILED_CLONING+"' where sequenceid=?";
        
        DatabaseTransaction t = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        PreparedStatement stmt3 = null;
        PreparedStatement stmt4 = null;
        PreparedStatement stmt5 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;
        
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            stmt = conn.prepareStatement(sql);
            stmt2 = conn.prepareStatement(sql2);
            stmt3 = conn.prepareStatement(sql3);
            stmt4 = conn.prepareStatement(sql4);
            stmt5 = conn.prepareStatement(sql5);
            
            numFailedConstructs = 0;
            numFailedSeqs = 0;
            System.out.println(containers.size());
            for(int i=0; i<containers.size(); i++) {
                int containerid = ((Integer)containers.get(i)).intValue();
                System.out.println(i+1+": "+containerid);
                stmt.setInt(1, containerid);
                stmt.setInt(2, containerid);
                rs = DatabaseTransaction.executeQuery(stmt);
                while(rs.next()) {
                    int constructid = rs.getInt(1);
                    
                    stmt2.setInt(1, constructid);
                    rs2 = DatabaseTransaction.executeQuery(stmt2);
                    if(rs2.next()) {
                        int count = rs2.getInt(1);
                        if(count == 0) {
                            stmt4.setInt(1, constructid);
                            DatabaseTransaction.executeUpdate(stmt4);
                            numFailedConstructs++;
                            System.out.println("insert failed construct: "+constructid);
                            
                            stmt3.setInt(1, constructid);
                            rs3 = DatabaseTransaction.executeQuery(stmt3);
                            
                            if(rs3.next()) {
                                int sequenceid = rs3.getInt(1);
                                String status = rs3.getString(2);
                                
                                if(FlexSequence.INPROCESS.equals(status)) {
                                    stmt5.setInt(1, sequenceid);
                                    DatabaseTransaction.executeUpdate(stmt5);
                                    numFailedSeqs++;
                                    System.out.println("update failed sequence: "+sequenceid);
                                }
                            }
                        }
                    }
                }
            }
            DatabaseTransaction.commit(conn);
            isFailedConstructs = true;
        } catch (Exception ex) {
            DatabaseTransaction.rollback(conn);
            System.out.println(ex);
            isFailedConstructs = false;
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeResultSet(rs2);
            DatabaseTransaction.closeResultSet(rs3);
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeStatement(stmt2);
            DatabaseTransaction.closeStatement(stmt3);
            DatabaseTransaction.closeStatement(stmt4);
            DatabaseTransaction.closeStatement(stmt5);
            DatabaseTransaction.closeConnection(conn);
        }
    }
    
    public static void main(String args[]) {
        //List containers = getContainers();
        List containers = new ArrayList();
        containers.add(new Integer(20308));
        containers.add(new Integer(20309));
        containers.add(new Integer(20310));
        containers.add(new Integer(20311));
        
        //List samples = new ArrayList();
        //samples.add(new Integer(1410486));
        
        /**
         * List samples = getSamples();
         *
         * if(samples == null) {
         * System.out.println("failed");
         * System.exit(0);
         * }
         */
        
        //change cloning strategy accordingly.
        //int cloningStrategyid = 4;
        int cloningStrategyid = 8;
        //String cloneType = CloneInfo.EXPRESSION_CLONE;
        String cloneType = CloneInfo.MASTER_CLONE;
        
        SummaryTablePopulator populator = new SummaryTablePopulator();
        
        //if(populator.populateExpressionClonesWithContainers(containers, cloningStrategyid)) {
        if(populator.populateObtainedMasterClonesWithContainers(containers, cloningStrategyid, cloneType))  {
        //if(populator.populateObtainedMasterClonesWithSamples(samples, cloningStrategyid, cloneType))  {
            populator.sendEmail(true, containers);
        } else {
            populator.sendEmail(false, containers);
        }
        
        //populator.populateFailedConstructs(containers);
        System.exit(0);
    }
    
    public static List getContainers() {
        String sql="select distinct containerid from obtainedmasterclone where containerlabel like 'YRG%' or containerlabel like 'YMG%'";
        DatabaseTransaction t = null;
        ResultSet rs = null;
        List containers = new ArrayList();
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while(rs.next()) {
                int containerid = rs.getInt(1);
                containers.add(new Integer(containerid));
            }
            return containers;
        } catch (Exception ex) {
            System.out.println(ex);
            return null;
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
    
    public static List getSamples() {
        String sql="select sampleid_to from samplelineage"+
        " where sampleid_from in ("+
        " select sampleid from sample where containerid in (8299, 8300))";
        DatabaseTransaction t = null;
        ResultSet rs = null;
        List sampleids = new ArrayList();
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while(rs.next()) {
                int sampleid = rs.getInt(1);
                sampleids.add(new Integer(sampleid));
            }
            return sampleids;
        } catch (Exception ex) {
            System.out.println(ex);
            return null;
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
}


