/*
 * BlastAlignmentSet.java
 *
 * Created on November 4, 2003, 4:20 PM
 */

package edu.harvard.med.hip.flex.query.core;

import java.util.*;
import java.sql.*;
import edu.harvard.med.hip.flex.database.*;

/**
 *
 * @author  DZuo
 */
public class BlastAlignmentSet {
    private List alignments;
    
    /** Creates a new instance of BlastAlignmentSet */
    public BlastAlignmentSet(List alignments) {
        this.alignments = alignments;
    }
    
    public void persist(Connection conn) throws FlexDatabaseException, SQLException {
        if(alignments == null || alignments.size() == 0)
            return;

        String sql = "insert into blastalignment(blastalignmentid, evalue,identity, querystart, queryend, substart, subend, score, strand, matchflexid)"+
                    " values (?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        
        for (int i=0; i<alignments.size(); i++) {
            BlastAlignment alignment = (BlastAlignment)alignments.get(i);
            stmt.setInt(1, alignment.getId());
            stmt.setString(2, alignment.getEvalue());
            stmt.setString(3, alignment.getIdentity());
            stmt.setInt(4, alignment.getQueryStart());
            stmt.setInt(5, alignment.getQueryEnd());
            stmt.setInt(6, alignment.getSubStart());
            stmt.setInt(7, alignment.getSubEnd());
            stmt.setString(8, alignment.getScore());
            stmt.setString(9, alignment.getStrand());
            stmt.setInt(10, alignment.getMatchFlexId());
            DatabaseTransaction.executeUpdate(stmt);
        }
        DatabaseTransaction.closeStatement(stmt);
    }
}
