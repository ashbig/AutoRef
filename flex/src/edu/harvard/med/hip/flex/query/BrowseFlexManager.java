/*
 * BrowseFlexManager.java
 *
 * Created on June 18, 2003, 1:51 PM
 */

package edu.harvard.med.hip.flex.query;

import java.lang.*;
import java.util.*;
import java.sql.*;

import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.core.*;

/**
 *
 * @author  dzuo
 */
public class BrowseFlexManager {
    protected String species = null;
    protected int projectid;
    protected String cloneStatus;
    
    public void setSpecies(String species) {this.species = species;}
    public void setProjectid(int id) {this.projectid = id;}
    public void setCloneStatus(String status) {this.cloneStatus = status;}
    
    /** Creates a new instance of BrowseFlexManager */
    public BrowseFlexManager() {
    }
    
    public List queryClones() throws FlexDatabaseException, SQLException {
        List clones = new ArrayList();
        String sql = "select distinct cloneid"+
                    " from clones c, flexsequence f"+
                    " where c.sequenceid=f.sequenceid"+
                    " and c.status='"+cloneStatus+"'";
        
        if(projectid != 0) {
            sql = "select distinct cloneid"+
                    " from clones c, flexsequence f, requestsequence r"+
                    " where c.sequenceid=f.sequenceid"+
                    " and r.sequenceid=f.sequenceid"+
                    " and r.projectid="+projectid+
                    " and c.status='"+cloneStatus+"'";
        }
        
        if(species != null) {
            sql += " and f.genusspecies='"+species+"'";
        }
      
        DatabaseTransaction t = null;
        ResultSet rs = null;
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while(rs.next()) {
                int cloneid = rs.getInt(1);
                clones.add((new Integer(cloneid)).toString());
            }
            
            return clones;
        } catch (FlexDatabaseException ex) {
            throw ex;
        } catch (SQLException ex) {
            throw ex;
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
    } 
    
    public static void main(String args[]) {
        BrowseFlexManager manager = new BrowseFlexManager();
        manager.setSpecies(FlexSequence.HUMAN);
        manager.setCloneStatus(CloneInfo.SEQ_VERIFIED);
        manager.setProjectid(4);
        try {
            List clones = manager.queryClones();
            for(int i=0; i<clones.size();i++) {
                System.out.println(clones.get(i));
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
