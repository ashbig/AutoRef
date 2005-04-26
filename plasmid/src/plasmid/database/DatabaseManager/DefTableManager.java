/*
 * DefTableManager.java
 *
 * Created on April 1, 2005, 4:24 PM
 */

package plasmid.database.DatabaseManager;

import plasmid.Constants;
import plasmid.database.*;

import java.sql.*;
import java.util.*;

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
                
        ResultSet rs = null;
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
        
        if(id == 0 )
            id = 1;
        
        return id;
    }
    
    public List getVocabularies(String table, DatabaseTransaction dt) {
        String sql = "select * from "+table;
        
        List l = new ArrayList();
        ResultSet rs = null;
        try {
            rs = dt.executeQuery(sql);
            while(rs.next()) {
                String s = rs.getString(1);
                l.add(s);
            }
        } catch (Exception ex) {
            handleError(ex, "Error occured while querying "+table);
        } finally {
            dt.closeResultSet(rs);
        }
        
        return l;
    }
}
