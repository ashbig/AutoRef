/*
 * DefTableManager.java
 *
 * Created on April 1, 2005, 4:24 PM
 */

package plasmid.database.DatabaseManager;

import plasmid.Constants;
import plasmid.database.*;

import javax.sql.*;

/**
 *
 * @author  DZuo
 */
public class DefTableManager extends TableManager {
    
    /** Creates a new instance of TableManager */
    public DefTableManager() {
    }
    
    public int getMaxNumber(String table, String column, DatabaseTransaction dt) {
        String sql = "select max("+column+") from "+table;
                
        RowSet rs = null;
        int id = -1;
        try {
            rs = dt.executeQuery(sql);
            if(rs.next()) {
                id = rs.getInt(1);
            }
        } catch (Exception ex) {
            handleError(ex, "Error occured while querying "+table);
        } finally {
            dt.closeResultSet(rs);
        }
        
        return id;
    }
}
