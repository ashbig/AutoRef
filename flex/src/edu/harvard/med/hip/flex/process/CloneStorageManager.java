/*
 * CloneStorageManager.java
 *
 * Created on March 16, 2004, 9:54 AM
 */

package edu.harvard.med.hip.flex.process;

import java.util.*;
import java.sql.*;
import javax.mail.*;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.util.*;

/**
 *
 * @author  DZuo
 */
public class CloneStorageManager {
    private String error;
    
    public String getError() {return error;}
    
    /** Creates a new instance of CloneStorageManager */
    public CloneStorageManager() {
    }
    
    public boolean addCloneStorage(List containers, String storageType, String storageForm) {
        String sql = "select storageid from clonestorage"+
        " where cloneid=?"+
        " and storageType='"+StorageType.WORKING+"'"+
        " and storageForm='"+storageForm+"'";
        String sqlUpdate = "update clonestorage"+
        " set storagesampleid=?,"+
        " storagecontainerid=?,"+
        " storagecontainerlabel=?,"+
        " storagecontainerposition=?"+
        " where storageid=?";
        String sqlInsert = "insert into clonestorage"+
        " (storageid,storagesampleid,storagecontainerid,"+
        " storagecontainerlabel,storagecontainerposition,"+
        " storagetype,storageform,cloneid)"+
        " values(storageid.nextval, ?,?,?,?,?,?,?)";
        System.out.println("Storage Type: "+storageType);
        System.out.println("Storage Form: "+storageForm);
        PreparedStatement stmt = null;
        PreparedStatement stmtUpdate = null;
        PreparedStatement stmtInsert = null;
        ResultSet rs = null;
        DatabaseTransaction t = null;
        Connection conn = null;
        
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            stmt = conn.prepareStatement(sql);
            stmtUpdate = conn.prepareStatement(sqlUpdate);
            stmtInsert = conn.prepareStatement(sqlInsert);
            
            for(int i=0; i<containers.size(); i++) {
                Container c = (Container)containers.get(i);
                int containerid = c.getId();
                String label = c.getLabel();
                Vector samples = c.getSamples();
                for(int j=0; j<samples.size(); j++) {
                    Sample s = (Sample)samples.get(j);
                    int sampleid = s.getId();
                    int position = s.getPosition();
                    int cloneid = s.getCloneid();
                    String sampleType = s.getType();
                    
                    if(sampleType.equals(Sample.CONTROL_POSITIVE) ||
                       sampleType.equals(Sample.CONTROL_NEGATIVE) ||
                       sampleType.equals(Sample.EMPTY)) 
                           continue;
                    
                    stmtInsert.setInt(1, sampleid);
                    stmtInsert.setInt(2, containerid);
                    stmtInsert.setString(3, label);
                    stmtInsert.setInt(4, position);
                    stmtInsert.setString(5, storageType);
                    stmtInsert.setString(6, storageForm);
                    stmtInsert.setInt(7, cloneid);
                    
                    if(StorageType.WORKING.equals(storageType)) {
                        stmt.setInt(1, cloneid);
                        rs = DatabaseTransaction.executeQuery(stmt);
                        if(rs.next()) {
                            int storageid = rs.getInt(1);
                            stmtUpdate.setInt(1, sampleid);
                            stmtUpdate.setInt(2, containerid);
                            stmtUpdate.setString(3, label);
                            stmtUpdate.setInt(4, position);
                            stmtUpdate.setInt(5, storageid);
                            DatabaseTransaction.executeUpdate(stmtUpdate);
                            System.out.println("update clone: "+cloneid);
                        } else {
                            DatabaseTransaction.executeUpdate(stmtInsert);
                            System.out.println("insert clone: "+cloneid);
                        }
                    } else {
                        DatabaseTransaction.executeUpdate(stmtInsert);
                        System.out.println("INSERT clone: "+cloneid);
                    }
                }
            }
            DatabaseTransaction.commit(conn);
            return true;
        } catch (Exception ex) {
            System.out.println(ex);
            DatabaseTransaction.rollback(conn);
            error = ex.getMessage();
            return false;
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeStatement(stmtUpdate);
            DatabaseTransaction.closeStatement(stmtInsert);
            DatabaseTransaction.closeConnection(conn);
        }
    }
        
    public void sendEmail(boolean isSuccessful, List ids) {
        String msg = "===========================================================\n";
        String to = "dongmei_zuo@hms.harvard.edu";
        String from = "dongmei_zuo@hms.harvard.edu";
        String cc = "dongmei_zuo@hms.harvard.edu";
        String subject = "CloneStorage table populating - ";
        
        java.util.Date d = new java.util.Date();
        java.text.SimpleDateFormat f = new java.text.SimpleDateFormat("MM/dd/yyyy");
        msg = msg + f.format(d) + "\n";
        
        if(isSuccessful) {
            msg = msg + "CloneStorage table populated successfully: "+error+"\n";
            subject = subject + "successful";
        } else {
            msg = msg + "CloneStorage table populated unsuccessfully.\n";
            subject = subject + "failed";
        }
        
        msg = msg + "===========================================================\n";
        for(int i=0; i<ids.size(); i++) {
            Container c = (Container)ids.get(i);
            msg = msg + "\t" + c.getId() +"\n";
        }
        
        try {
            Mailer.sendMessage(to, from, cc, subject, msg);
        } catch (MessagingException ex) {
            System.out.println(ex);
        }
    }
        
    public static void main(String args[]) {
        List containers = new ArrayList();
        try {
            Container c = new Container(8338);
            c.restoreSample();
            containers.add(c);
            CloneStorageManager manager = new CloneStorageManager();
            manager.addCloneStorage(containers, StorageType.WORKING, StorageForm.GLYCEROL);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}

