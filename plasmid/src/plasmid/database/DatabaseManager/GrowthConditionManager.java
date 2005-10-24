/*
 * GrowthConditionManager.java
 *
 * Created on April 14, 2005, 11:46 AM
 */

package plasmid.database.DatabaseManager;

import java.sql.*;
import java.util.*;

import plasmid.coreobject.*;
import plasmid.database.*;
import plasmid.Constants;

/**
 *
 * @author  DZuo
 */
public class GrowthConditionManager extends TableManager {
    
    /** Creates a new instance of GrowthConditionManager */
    public GrowthConditionManager(Connection conn) {
       super(conn);
    }
    
    public boolean insertGrowthConditions(List conditions) {
        if(conditions == null)
            return true;
        
        String sql = "insert into growthcondition"+
        " (growthid,name,hosttype,antibioticselection,growthcondition,comments)"+
        " values(?,?,?,?,?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            for(int i=0; i<conditions.size(); i++) {
                GrowthCondition g = (GrowthCondition)conditions.get(i);
                stmt.setInt(1, g.getGrowthid());
                stmt.setString(2, g.getName());
                stmt.setString(3, g.getHosttype());
                stmt.setString(4, g.getSelection());
                stmt.setString(5, g.getCondition());
                stmt.setString(6, g.getComments());
                
                DatabaseTransaction.executeUpdate(stmt);
            }
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into GROWTHCONDITION table");
            return false;
        }
        return true;
    }
    
    public int getGrowthid(String name) {
        String sql = "select growthid from growthcondition where name=?";
        int id = 0;
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            ResultSet rs = DatabaseTransaction.executeQuery(stmt);
            if(rs.next()) {
                id = rs.getInt(1);
            }
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            if(Constants.DEBUG) {
                System.out.println(ex);
            }
        }
        
        return id;
    }
}
