/*
 * NoFoundSet.java
 *
 * Created on November 4, 2003, 4:27 PM
 */

package edu.harvard.med.hip.flex.query.core;

import java.util.*;
import java.sql.*;
import edu.harvard.med.hip.flex.database.*;

/**
 *
 * @author  DZuo
 */
public class NoFoundSet {
    private List noFounds;
    
    /** Creates a new instance of NoFoundSet */
    public NoFoundSet(List noFounds) {
        this.noFounds = noFounds;
    }
    
    public void persist(Connection conn) throws FlexDatabaseException, SQLException {
        if(noFounds == null || noFounds.size() == 0)
            return;
        
        String sql = "insert into notfound(searchresultid, reason)"+
                    " values(?,?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        
        for(int i=0; i<noFounds.size(); i++) {
            NoFound nf = (NoFound)noFounds.get(i);
            String reason = nf.getReason();
            if(reason.length() > 100)
                reason = reason.substring(0, 100);
       
            stmt.setInt(1, nf.getSearchResultId());
            stmt.setString(2, reason);
            DatabaseTransaction.executeUpdate(stmt);
        }
        DatabaseTransaction.closeStatement(stmt);
    }
}
