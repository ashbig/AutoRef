/*
 * Statistics.java
 *
 * Created on January 17, 2002, 2:22 PM
 */

package edu.harvard.med.hip.metagene.core;

import java.util.*;
import java.sql.*;

import edu.harvard.med.hip.metagene.database.*;

/**
 *
 * @author  dzuo
 * @version
 */
public class Statistics {
    private int id;
    private String type;
    
    /** Creates new Statistics */
    public Statistics(int id, String type) {
        this.id = id;
        this.type = type;
    }
    
    public int getId() {
        return id;
    }
    
    public String getType() {
        return type;
    }
    
    public static Vector getAllStatistics() {
        DatabaseManager manager = new DatabaseManager();
        Connection conn = manager.connect();
        
        if (conn == null) {
            System.out.println("Cannot connect to the database.");
            return null;
        }
        
        String sql = "select * from statistic_type";
        Statement stmt = null;
        Vector stats = null;
        
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            stats = new Vector();
            
            while(rs.next()) {
                int id = rs.getInt(1);
                String type = rs.getString(2);
                Statistics stat = new Statistics(id, type);
                stats.addElement(stat);
            }
            
            stmt.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        
        manager.disconnect();
        return stats;
    }
}
