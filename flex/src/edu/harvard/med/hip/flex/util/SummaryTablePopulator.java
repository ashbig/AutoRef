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
import edu.harvard.med.hip.flex.core.CloneInfo;

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
            numClonestorage = populateClonestorageTable(samples, conn);
            numSamples = updateSampleTable(samples, conn);
            
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
            return populateObtainedMasterClonesWithSamples(samples, strategyid, cloneType);           
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
    
    public int populateCloningprogressTable(List samples, Connection conn) throws FlexDatabaseException, SQLException {
        String sql = "insert into cloningprogress"+
        " select distinct constructid, 1"+
        " from sample where sampleid=?"+
        " and constructid not in"+
        " (select distinct constructid from cloningprogress)";
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
        String sql = "insert into clones"+
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
    
    public int populateClonestorageTable(List samples, Connection conn) throws FlexDatabaseException, SQLException {
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
    
    public void sendEmail(boolean isSuccessful, List ids) {
        String msg = "===========================================================\n";
        String to = "dzuo@hms.harvard.edu";
        String from = "dzuo@hms.harvard.edu";
        String cc = "dzuo@hms.harvard.edu";
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
        
        msg = msg + "===========================================================\n";
        for(int i=0; i<ids.size(); i++) {
            msg = msg + "\t" + ids.get(i).toString()+"\n";
        }
        
        try {
            Mailer.sendMessage(to, from, cc, subject, msg);
        } catch (MessagingException ex) {
            System.out.println(ex);
        }
    }
    
    public static void main(String args[]) {
        List containers = new ArrayList();
        containers.add(new Integer(7045));
      containers.add(new Integer(7046));
      containers.add(new Integer(7047));
      containers.add(new Integer(7048));
      containers.add(new Integer(7049));
      containers.add(new Integer(7050));
      containers.add(new Integer(7051));
      containers.add(new Integer(7052));
      containers.add(new Integer(7053));
      containers.add(new Integer(7054));
      containers.add(new Integer(7055));
      containers.add(new Integer(7056));
      containers.add(new Integer(7057));
      containers.add(new Integer(7058));
      containers.add(new Integer(7059));
      containers.add(new Integer(7060));
      containers.add(new Integer(7061));
      containers.add(new Integer(7062));
      containers.add(new Integer(7063));
      containers.add(new Integer(7064));
      containers.add(new Integer(7108));
      containers.add(new Integer(7109));
      containers.add(new Integer(7110));
      containers.add(new Integer(7111));
      containers.add(new Integer(7112));

        //change cloning strategy accordingly.
        int cloningStrategyid = 4;
        String cloneType = CloneInfo.MASTER_CLONE;
        
        SummaryTablePopulator populator = new SummaryTablePopulator();
        if(populator.populateObtainedMasterClonesWithContainers(containers, cloningStrategyid, cloneType))  {
            populator.sendEmail(true, containers);
        } else {
            populator.sendEmail(false, containers);
        }
        
        System.exit(0);
    }
}


