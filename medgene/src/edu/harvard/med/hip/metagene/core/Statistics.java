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
    public static final String PRODUCT_OF_INCIDENCE = "Product of incidence";
    public static final String PROBABILITY = "Probability";
    public static final String CHI_SQUARE = "Chi square analysis";
    public static final String FISCHER = "Fischer exact test";
    public static final String RELATIVE_RISK_OF_GENE = "Relative risk of gene";
    public static final String RELATIVE_RISK_OF_DISEASE = "Relative risk of disease";
    public static final int FISCHERID = 4;
    
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
        DBManager manager = new DBManager();
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
        
        manager.disconnect(conn);
        return stats;
    }
}
