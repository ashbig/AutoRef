/*
 * UpdateClonename.java
 *
 * Created on June 20, 2003, 12:47 PM
 */

package edu.harvard.med.hip.flex.special_projects;

import java.util.*;
import java.sql.*;
import edu.harvard.med.hip.flex.database.*;

/**
 *
 * @author  dzuo
 */
public class UpdateClonename {
    
    /** Creates a new instance of UpdateClonename */
    public UpdateClonename() {
    }
    
    public void updateName(List cloneids) throws Exception {
        String sql = "select cd.constructtype"+
        " from clones c, obtainedmasterclone o, constructdesign cd"+
        " where c.mastercloneid=o.mastercloneid"+
        " and o.constructid=cd.constructid"+
        " and c.cloneid=?";
        String sql2 = "update clones set clonename=? where cloneid=?";
        
        java.text.NumberFormat fmt = java.text.NumberFormat.getInstance();
        fmt.setMaximumIntegerDigits(6);
        fmt.setMinimumIntegerDigits(6);
        fmt.setGroupingUsed(false);
        
        DatabaseTransaction t = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        ResultSet rs = null;
        
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            stmt = conn.prepareStatement(sql);
            stmt2 = conn.prepareStatement(sql2);
            
            for(int i=0; i<cloneids.size(); i++) {
                int cloneid = ((Integer)cloneids.get(i)).intValue();
                stmt.setInt(1, cloneid);
                rs = DatabaseTransaction.executeQuery(stmt);
                if(rs.next()) {
                    String constructtype = rs.getString(1);
                    String name = null;
                    if("FUSION".equals(constructtype)) {
                        name="FLH"+fmt.format(cloneid)+".01L";
                    } else if("CLOSED".equals(constructtype)) {
                        name="FLH"+fmt.format(cloneid)+".01X";
                    } else {
                        throw new Exception("Invalid construct type: "+cloneid+": "+constructtype);
                    }
                    
                    stmt2.setString(1, name);
                    stmt2.setInt(2, cloneid);
                    DatabaseTransaction.executeUpdate(stmt2);
                    System.out.println(i+": "+cloneid+"\t"+name);
                } else {
                    throw new Exception("No construct found: "+cloneid);
                }
            }
            DatabaseTransaction.commit(conn);
        } catch (Exception ex) {
            DatabaseTransaction.rollback(conn);
            throw new Exception("Database error.");
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeStatement(stmt2);
            DatabaseTransaction.closeConnection(conn);
        }
    }
    
    public void updateCloneid(List clones) {
        String sql = "update sample set cloneid=? where sampleid=?";
        String sql2 = "select cloneid from sample where sampleid=?";
        
        DatabaseTransaction t = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        ResultSet rs = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            stmt = conn.prepareStatement(sql);
            stmt2 = conn.prepareStatement(sql2);
            
            for(int i=0; i<clones.size(); i++) {
                Clone c = (Clone)clones.get(i);
                int cloneid = c.getCloneid();
                int sampleid = c.getSampleid();
                
                stmt2.setInt(1, sampleid);
                rs = DatabaseTransaction.executeQuery(stmt2);
                if(rs.next()) {
                    int cloneid_old = rs.getInt(1);
                    
                    if(cloneid_old == 0) {                
                        stmt.setInt(1, cloneid);
                        stmt.setInt(2, sampleid);
                        DatabaseTransaction.executeUpdate(stmt);
                        System.out.println(i);
                    }
                }
            }
            DatabaseTransaction.commit(conn);
        } catch (Exception ex) {
            DatabaseTransaction.rollback(conn);
            System.out.println(ex);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt2);
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeConnection(conn);
        }
    }
    
    public List readClones() {
        /*
        String sql = "select s.cloneid, s.storagesampleid"+
                    " from clonestorage s, clones c"+
                    " where s.cloneid=c.cloneid"+
                    " and c.status='SEQUENCE VERIFIED'"+
                    " and c.clonename is null";
         **/
        String sql = "select s.cloneid, s.storagesampleid"+
                    " from clonestorage s, sample p"+
                    " where s.storagesampleid=p.sampleid"+
                    " and p.cloneid is null";
        DatabaseTransaction t = null;
        ResultSet rs = null;
        List clones = new ArrayList();
        
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while(rs.next()) {
                int cloneid = rs.getInt(1);
                int sampleid = rs.getInt(2);
                Clone c = new Clone(cloneid, sampleid);
                clones.add(c);
            }
            return clones;
        } catch (Exception ex) {
            System.out.println(ex);
            return null;
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
    
    public class Clone {
        private int cloneid;
        private int sampleid;
        
        public Clone(int cloneid, int sampleid) {
            this.cloneid = cloneid;
            this.sampleid = sampleid;
        }
        
        public int getCloneid() {return cloneid;}
        public int getSampleid() {return sampleid;}
    }
    
    public static void main(String args[]) throws Exception {
        /*
          DatabaseTransaction t = DatabaseTransaction.getInstance();
          String sql = "select c.cloneid from clonesequencing s, clones c"+
                        " where s.cloneid=c.cloneid"+
                        " and c.clonename is null"+
                        " and c.status='SEQUENCE VERIFIED'";
          ResultSet rs = t.executeQuery(sql);
          List clones = new ArrayList();
          while(rs.next()) {
          int cloneid = rs.getInt(1);
         clones.add(new Integer(cloneid));
         }
         
          UpdateClonename u = new UpdateClonename();
         u.updateName(clones);
        */ 
        
        UpdateClonename u = new UpdateClonename();
        List clones = u.readClones();
        if(clones == null) {
            System.out.println("Error");
            System.exit(0);
        }
        System.out.println(clones.size());
        u.updateCloneid(clones);
    }
}
