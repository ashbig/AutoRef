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

/**
 *
 * @author  DZuo
 */
public class GrowthConditionManager extends TableManager {
    private Connection conn;
    
    /** Creates a new instance of GrowthConditionManager */
    public GrowthConditionManager(Connection conn) {
        this.conn = conn;
    }
        
    public boolean insertGrowthConditions(List conditions) {
        if(conditions == null)
            return true;
        
        String sql = "inset into growthcondition"+
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
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into GROWTHCONDITION table");
            return false;
        }
        return true;
    }    
}
