/*
 * ExpressionCloneImporter.java
 *
 * Created on May 17, 2006, 12:54 PM
 */

package plasmid.importexport;

import java.util.*;
import java.sql.*;

import plasmid.coreobject.*;
import plasmid.database.DatabaseManager.*;
import plasmid.database.*;

/**
 *
 * @author  DZuo
 */
public class ExpressionCloneImporter {
    private static String flexurl = "jdbc:oracle:thin:@kotel:1532:flex";
    private static String flexusername = "flex_production";
    private static String flexpassword = "3monkeys";
    private static String plasmidurl = "jdbc:oracle:thin:@128.103.32.228:1521:plasmid";
    private static String plasmidusername = "plasmid_test";
    private static String plasmidpassword = "plasmID";
    
    /** Creates a new instance of ExpressionCloneImporter */
    public ExpressionCloneImporter() {
    }
    
    public List getFlexClones(String status, String vectorname) throws Exception {
        String sql = "select cloneid,clonename,mastercloneid,status,comments,vectorname,genusspecies"+
        " from clones c, cloningstrategy s, flexsequence f where c.strategyid=s.strategyid"+
        " c.sequenceid=f.sequenceid";
        
        if(status != null)
            sql += " and c.status='"+status+"'";
        if(vectorname != null)
            sql += " and s.vectorname='"+vectorname+"'";
        
        List clones = new ArrayList();
        DatabaseTransactionNoPool t = new DatabaseTransactionNoPool(flexurl,flexusername,flexpassword);
        ResultSet rs = t.executeQuery(sql);
        while(rs.next()) {
            int cloneid = rs.getInt(1);
            String clonename = rs.getString(2);
            int mastercloneid = rs.getInt(3);
            String s = rs.getString(4);
            String comments = rs.getString(5);
            String v = rs.getString(6);
            String species = rs.getString(7);
            FlexClone c = new FlexClone(cloneid, mastercloneid, v, clonename, s, comments, species);
            clones.add(c);
        }
        DatabaseTransactionNoPool.closeResultSet(rs);
        
        return clones;
    }
    
    public int getCloneAuthorid(String vectorname) {
        return 1;
    }
    
    public void insertClones(List clones, Connection conn) throws Exception {
        String sql = "select distinct cloneid from clonename"+
        " where nametype='"+CloneNameType.HIP_MASTER_CLONE_ID+"'"+
        " and namevalue=?";
        String sql2 = "select domain, comments from clone where cloneid=?";
        String sql3 = "select insertid from cloneinsert where cloneid=?";
        
        PreparedStatement stmt = conn.prepareStatement(sql);
        PreparedStatement stmt2 = conn.prepareStatement(sql2);
        PreparedStatement stmt3 = conn.prepareStatement(sql3);
        
        CloneManager m = new CloneManager(conn);
        List plasmidClones = new ArrayList();
        List inserts = new ArrayList();
        List names = new ArrayList();
        
        DefTableManager d = new DefTableManager();
        int id = d.getMaxNumber("clone", "cloneid");
        if(id == -1) {
            throw new Exception("Cannot get cloneid from clone table.");
        }
        
        for(int i=0; i<clones.size(); i++) {
            FlexClone c = (FlexClone)clones.get(i);
            int plasmidCloneid = 0;
            
            stmt.setString(1, (new Integer(c.getMastercloneid())).toString());
            ResultSet rs = DatabaseTransactionNoPool.executeQuery(stmt);
            if(rs.next()) {
                plasmidCloneid = rs.getInt(1);
            }
            if(plasmidCloneid == 0) {
                throw new Exception("Cannot get plasmid cloneid with master cloneid "+c.getMastercloneid());
            }
            DatabaseTransactionNoPool.closeResultSet(rs);
            
            stmt2.setInt(1, plasmidCloneid);
            rs = DatabaseTransactionNoPool.executeQuery(stmt2);
            Clone plasmidClone = null;
            if(rs.next()) {
                String domain = rs.getString(1);
                String comments = rs.getString(2);
                plasmidClone = new Clone(id,null,Clone.CDNA,null,null,domain,null,Clone.HIP_ONLY,comments,0,c.getVectorname(),null,Clone.AVAILABLE,null,Clone.SOURCE_HIP,null);
                
                VectorManager man = new VectorManager(conn);
                int vectorid = man.getVectorid(c.getVectorname());
                if(vectorid <= 0)
                    throw new Exception("Cannot get vectorid with vectorname="+c.getVectorname());
                plasmidClone.setVectorid(vectorid);
                
                java.text.NumberFormat fmt = java.text.NumberFormat.getInstance();
                fmt.setMaximumIntegerDigits(8);
                fmt.setMinimumIntegerDigits(8);
                fmt.setGroupingUsed(false);
                DefTableManager manager = new DefTableManager();
                String sp = manager.getVocabulary("species", "genusspecies", "code", plasmidClone.getDomain(), conn);
                String tp = manager.getVocabulary("clonetype", "clonetype", "code", plasmidClone.getType(), conn);
                if(sp == null) {
                    if(Clone.NOINSERT.equals(plasmidClone.getDomain())) {
                        sp = Clone.SPECIES_NOINSERT;
                    } else {
                        throw new Exception("Cannot find code for species: "+plasmidClone.getDomain());
                    }
                }
                if(tp == null) {
                    throw new Exception("Cannot find code for clonetype: "+plasmidClone.getType());
                }
                plasmidClone.setName(sp+tp+fmt.format(id));
                
                //set verification based on FlexClone status
                
                plasmidClones.add(plasmidClone);
                
                CloneName n1 = new CloneName(id, CloneNameType.HIP_CLONE_ID, (new Integer(c.getCloneid())).toString(), "http://kotel.harvard.edu:8080/FLEX/ViewClone.do?cloneid="+c.getCloneid()+"&isDisplay=1");
                CloneName n2 = new CloneName(id, CloneNameType.HIP_MASTER_CLONE_ID, (new Integer(c.getMastercloneid())).toString(), null);
                CloneName n3 = new CloneName(id, CloneNameType.ORIGINAL_CLONE_ID, c.getClonename(), null);
                names.add(n1);
                names.add(n2);
                names.add(n3);
                
                id++;
            } else {
                throw new Exception("Cannot query clone with cloneid="+plasmidCloneid);
            }
            DatabaseTransactionNoPool.closeResultSet(rs);
            
            stmt3.setInt(1, plasmidCloneid);
            rs = DatabaseTransactionNoPool.executeQuery(stmt3);
            while(rs.next()) {
                int insertid = rs.getInt(1);
                DnaInsert insert = new DnaInsert();
                insert.setInsertid(insertid);
                insert.setCloneid(plasmidClone.getCloneid());
                inserts.add(insert);
            }
            DatabaseTransactionNoPool.closeResultSet(rs);
        }
        DatabaseTransactionNoPool.closeStatement(stmt);
        DatabaseTransactionNoPool.closeStatement(stmt2);
        DatabaseTransactionNoPool.closeStatement(stmt3);
        
        if(!m.insertClones(plasmidClones)) {
            throw new Exception("Error occured while inserting into CLONE table.");
        }
        if(!m.insertCloneNames(names)) {
            throw new Exception("Error occured while inserting into CLONENAME table");
        }
        if(!m.insertCloneInsertsWithoutInsertInfo(inserts)) {
            throw new Exception("Error occured while inserting into CLONEINSERT table");
        }
    }
    
    public void updateFlexCloneStatus(List clones, Connection conn) throws Exception {
        String sql = "update clones set plasmid='Y' where cloneid=?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        for(int i=0; i<clones.size(); i++) {
            FlexClone c = (FlexClone)clones.get(i);
            stmt.setInt(1, c.getCloneid());
            DatabaseTransaction.executeUpdate(stmt);
        }
        DatabaseTransaction.closeStatement(stmt);
    }
    
    public static void main(String args[]) throws Exception {
        DatabaseTransactionNoPool t = new DatabaseTransactionNoPool(plasmidurl,plasmidusername,plasmidpassword);
        Connection conn = t.requestConnection();
        DatabaseTransactionNoPool flex = new DatabaseTransactionNoPool(flexurl, flexusername, flexpassword);
        Connection flexconn = flex.requestConnection();
        
        ExpressionCloneImporter imp = new ExpressionCloneImporter();
        try {
            List clones = imp.getFlexClones(null, null);
            imp.insertClones(clones, conn);
            imp.updateFlexCloneStatus(clones, flexconn);
            DatabaseTransactionNoPool.commit(conn);
            DatabaseTransactionNoPool.commit(flexconn);
        } catch (Exception ex) {
            System.out.println(ex);
            DatabaseTransactionNoPool.rollback(conn);
            DatabaseTransactionNoPool.rollback(flexconn);
        } finally {
            DatabaseTransactionNoPool.closeConnection(conn);
            DatabaseTransactionNoPool.closeConnection(flexconn);
        }
    }
}
