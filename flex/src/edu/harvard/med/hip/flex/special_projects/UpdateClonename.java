/*
 * UpdateClonename.java
 *
 * Created on June 20, 2003, 12:47 PM
 */

package edu.harvard.med.hip.flex.special_projects;

import java.util.*;
import java.sql.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.core.Container;

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
    
    public void updateDestCloneid(String containerids) {
        String sql = "update sample set cloneid=? where sampleid=? and cloneid is null";
        String sql2 = "select s1.cloneid, s2.sampleid"+
        " from sample s1, sample s2, samplelineage sl"+
        " where s1.sampleid=sl.sampleid_from"+
        " and sl.sampleid_to=s2.sampleid"+
        " and s2.sampletype='ISOLATE'"+
        " and s2.containerid in "+containerids;
        DatabaseTransaction t = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            stmt = conn.prepareStatement(sql);
            
            rs = t.executeQuery(sql2);
            System.out.println(sql2);
            while(rs.next()) {
                int cloneid = rs.getInt(1);
                int sampleid = rs.getInt(2);
                
                System.out.println(cloneid+"\t"+sampleid);
                
                stmt.setInt(1, cloneid);
                stmt.setInt(2, sampleid);
                DatabaseTransaction.executeUpdate(stmt);
            }
            DatabaseTransaction.commit(conn);
        } catch (Exception ex) {
            DatabaseTransaction.rollback(conn);
            System.out.println(ex);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
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
    
    public List updateAllRearrayPlateCloneid(List containers) {
        List failed = new ArrayList();
        for(int i=0; i<containers.size(); i++) {
            Container container = (Container)containers.get(i);
            try {
                updateRearrayPlateCloneid(container.getId());
            } catch (Exception ex) {
                failed.add(container);
            }
        }
        return failed;
    }
    
    public void updateRearrayPlateCloneid(int containerid) throws Exception {
        String sql = "select sampleid from sample where containerid="+containerid;
        String sql2 = "select sampleid_from from samplelineage where sampleid_to=?";
        String sql3 = "select cloneid from sample where sampleid in"+
                    " (select sampleid_to from samplelineage where sampleid_from=?"+
                    " and sampleid_to<>? and sampleid_from<>sampleid_to)"+
                    " order by cloneid desc";
        String sqlupdate = "update sample set cloneid=? where sampleid=?";
        
        DatabaseTransaction t = null;
        Connection conn = null;
        PreparedStatement stmt2 = null;
        PreparedStatement stmt3 = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            stmt2 = conn.prepareStatement(sql2);
            stmt3 = conn.prepareStatement(sql3);
            stmt = conn.prepareStatement(sqlupdate);
            rs = t.executeQuery(sql);
            
            while(rs.next()) {
                int sampleidSeq = rs.getInt(1);               
                //System.out.println("sampleidSeq:"+sampleidSeq);
                
                stmt2.setInt(1, sampleidSeq);
                int sampleidCulture = 0;
                rs2 = DatabaseTransaction.executeQuery(stmt2);
                if(rs2.next()) {
                    sampleidCulture = rs2.getInt(1);
                }
                DatabaseTransaction.closeResultSet(rs2);
                //System.out.println("sampleidCulture: "+sampleidCulture);
                
                stmt3.setInt(1, sampleidCulture);
                stmt3.setInt(2, sampleidSeq);
                rs3 = DatabaseTransaction.executeQuery(stmt3);
                int cloneid = 0;
                while(rs3.next()) {
                    cloneid = rs3.getInt(1);
                    if(cloneid > 0)
                        break;
                }
                DatabaseTransaction.closeResultSet(rs3);
                if(cloneid != 0) {
                    stmt.setInt(1, cloneid);
                    stmt.setInt(2, sampleidSeq);
                    //System.out.println("cloneid: "+cloneid);
                    DatabaseTransaction.executeUpdate(stmt);
                    //System.out.println("update: "+sampleidSeq+"\t"+cloneid);
                }
            }
            DatabaseTransaction.commit(conn);
        } catch (Exception ex) {
            DatabaseTransaction.rollback(conn);
            throw new Exception(ex);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeResultSet(rs2);
            DatabaseTransaction.closeResultSet(rs3);
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeStatement(stmt2);
            DatabaseTransaction.closeStatement(stmt3);
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
        /**   DatabaseTransaction t = DatabaseTransaction.getInstance();
         * String sql = "select c.cloneid from clones c"+
         * " where c.clonename is null"+
         * " and c.status='SEQUENCE VERIFIED'";
         * ResultSet rs = t.executeQuery(sql);
         * List clones = new ArrayList();
         * while(rs.next()) {
         * int cloneid = rs.getInt(1);
         * clones.add(new Integer(cloneid));
         * }
         *
         * UpdateClonename u = new UpdateClonename();
         * u.updateName(clones);
         */
        UpdateClonename u = new UpdateClonename();
        /**       List clones = u.readClones();
         * if(clones == null) {
         * System.out.println("Error");
         * System.exit(0);
         * }
         * System.out.println(clones.size());
         * u.updateCloneid(clones);
         */
        //u.updateDestCloneid("(7275,7276)");
        try {
            u.updateRearrayPlateCloneid(7389);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
