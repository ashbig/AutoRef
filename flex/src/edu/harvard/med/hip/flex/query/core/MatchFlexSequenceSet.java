/*
 * MatchFlexSequenceSet.java
 *
 * Created on November 4, 2003, 4:08 PM
 */

package edu.harvard.med.hip.flex.query.core;

import java.util.*;
import java.sql.*;
import edu.harvard.med.hip.flex.database.*;

/**
 *
 * @author  DZuo
 */
public class MatchFlexSequenceSet {
    private List matchFlexSequences;
    
    /** Creates a new instance of MatchFlexSequenceSet */
    public MatchFlexSequenceSet(List matchFlexSequences) {
        this.matchFlexSequences = matchFlexSequences;
    }
    
    public void persist(Connection conn) throws FlexDatabaseException, SQLException {
        String sql = "insert into matchflexsequence(matchflexid, ismatchbygi, matchgenbankid, flexsequenceid)"+
                    " values(?,?,?,?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        for(int i=0; i<matchFlexSequences.size(); i++) {
            MatchFlexSequence mfs = (MatchFlexSequence)matchFlexSequences.get(i);
            stmt.setInt(1, mfs.getMatchFlexId());
            stmt.setString(2, mfs.getIsMatchByGi());
            stmt.setInt(3, mfs.getMatchGenbankId());
            stmt.setInt(4, mfs.getMatchFlexId());
            DatabaseTransaction.executeUpdate(stmt);
        }
        DatabaseTransaction.closeStatement(stmt);
    }
}
