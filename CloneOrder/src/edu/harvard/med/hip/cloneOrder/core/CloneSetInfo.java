/*
 * CloneSetInfo.java
 *
 * Created on March 26, 2003, 6:29 PM
 */

package edu.harvard.med.hip.cloneOrder.core;
import edu.harvard.med.hip.cloneOrder.database.*;
import java.util.TreeSet;
import java.sql.*;

/**
 *
 * @author  hweng
 */
public class CloneSetInfo {
    
    protected int id;
    protected String name;
    
    /** Creates a new instance of CloneSetInfo */
    public CloneSetInfo() {
    }
    
    public CloneSetInfo(int id, String name){
        this.id = id;
        this.name = name;
    }
    
    public int getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    
    public TreeSet getClonesInSet(String cloneSetName){
                        
        DBManager manager = new DBManager();
        Connection conn = manager.connect();
        
        if (conn == null) {
            System.out.println("Cannot connect to the database.");
            return new TreeSet();
        }
        
        String sql = "SELECT ci.PlateID,ci.WellID_position, ci.flexid, ci.genesymbol, ci.genbankacc, ci.gi, ci.sugenid, ci.clonetype " +
                     "FROM clone_set_info AS csi, clone_set AS cs, clone_info AS ci " +
                     "WHERE csi.clonesetid=cs.clonesetid " +
                     "and cs.flexid=ci.flexid and csi.clonesetname = '" + cloneSetName + "'";

        TreeSet clones = new TreeSet(new CloneInfoComparator());
        
        try{
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);            
            while(rs.next()) {
                String plateId = rs.getString(1);
                String wellIdPosition = rs.getString(2);
                String flexId = rs.getString(3);
                String geneSymbol = rs.getString(4);
                String genbankAcc = rs.getString(5);
                int gi = rs.getInt(6);
                String sugenId = rs.getString(7);
                String cloneType = rs.getString(8);
                CloneInfo clone = new CloneInfo(plateId,wellIdPosition,flexId, geneSymbol, genbankAcc, gi, sugenId, cloneType);
                clones.add(clone);
            }
            rs.close();
            stmt.close();
        }catch (SQLException ex) {
            System.out.println(ex);
        }finally {
            manager.disconnect(conn);
            return clones;
        }
    }
        
}
