/*
 * BlastHitSet.java
 *
 * Created on November 4, 2003, 4:16 PM
 */

package edu.harvard.med.hip.flex.query.core;

import java.util.*;
import java.sql.*;
import edu.harvard.med.hip.flex.database.*;

/**
 *
 * @author  DZuo
 */
public class BlastHitSet {
    private List blastHits;
    
    /** Creates a new instance of BlastHitSet */
    public BlastHitSet(List blastHits) {
        this.blastHits = blastHits;
    }
    
    public void persist(Connection conn) throws FlexDatabaseException, SQLException {
        String sql = "insert into blasthit(matchflexid, querylength, subjectlength)"+
                    " values(?,?,?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        for (int i=0; i<blastHits.size(); i++) {
            BlastHit blastHit = (BlastHit)blastHits.get(i);
            stmt.setInt(1, blastHit.getMatchFlexId());
            stmt.setInt(2, blastHit.getQueryLength());
            stmt.setInt(3, blastHit.getSubjectLength());
            DatabaseTransaction.executeUpdate(stmt);
        }
        DatabaseTransaction.closeStatement(stmt);
    }
}
