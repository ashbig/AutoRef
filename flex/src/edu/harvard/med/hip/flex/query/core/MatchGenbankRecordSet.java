/*
 * MatchGenbankRecordSet.java
 *
 * Created on November 4, 2003, 10:40 AM
 */

package edu.harvard.med.hip.flex.query.core;

import java.util.*;
import java.sql.*;

import edu.harvard.med.hip.flex.database.*;

/**
 *
 * @author  DZuo
 */
public class MatchGenbankRecordSet {
    private List matchGenbankRecords;
    
    /** Creates a new instance of MatchGenbankRecordSet */
    public MatchGenbankRecordSet(List matchGenbankRecords) {
        this.matchGenbankRecords = matchGenbankRecords;
    }
    
    public void persist(Connection conn) throws FlexDatabaseException, SQLException {
        if(matchGenbankRecords == null || matchGenbankRecords.size() == 0)
            return;
        
        String sql = "insert into matchgenbankrecord(matchgenbankid, genbankaccession, gi, searchmethod, searchresultid, locusid, unigene)"+
                    " values(?,?,?,?,?,?,?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        for(int i=0; i<matchGenbankRecords.size(); i++) {
            MatchGenbankRecord mgr = (MatchGenbankRecord)matchGenbankRecords.get(i);
            stmt.setInt(1, mgr.getMatchGenbankId());
            stmt.setString(2, mgr.getGanbankAccession());
            stmt.setString(3, mgr.getGi());
            stmt.setString(4, mgr.getSearchMethod());
            stmt.setInt(5, mgr.getSearchResultid());
            stmt.setString(6, mgr.getLocusid());
            stmt.setString(7, mgr.getUnigene());
            DatabaseTransaction.executeUpdate(stmt);
        }
        DatabaseTransaction.closeStatement(stmt);
    }  
}
