/*
 * FlexSeqBatchRetriever.java
 *
 * Created on March 18, 2003, 4:32 PM
 */

package edu.harvard.med.hip.flex.query.handler;

import java.sql.*;
import java.util.*;
import javax.sql.*;

import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.query.core.*;
import edu.harvard.med.hip.flex.query.QueryException;

/**
 *
 * @author  dzuo
 */
public class FlexSeqBatchRetriever extends SeqBatchRetriever {       
    public FlexSeqBatchRetriever(List giList) {
        super(giList);
    }
    
    /**
     * Retrive the sequences from FLEXGene database. Populate foundList
     * and noFoundList. Return a list of GIs that are found in the database.
     *
     * @return A list of GIs that are found in the database.
     * @exception QueryException
     * @exception FlexDatabaseException
     * @exception SQLException
     */
    public List retrieveSequence() throws QueryException, FlexDatabaseException, SQLException {
        if(giList == null) {
            throw new QueryException("No query list provided.");
        }
        
        List foundGi = new ArrayList();
        String sql = "select * from girecord where gi=?";
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        Connection conn = t.requestConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = null;
        
        for(int i=0; i<giList.size(); i++) {
            String element = (String)giList.get(i);
            stmt.setInt(1, Integer.parseInt(element));
            rs = DatabaseTransaction.executeQuery(stmt);
            
            if (rs.next()) {
                String type = rs.getString("TYPE");
                int gi = rs.getInt("GI");
                String sequenceFile = rs.getString("SEQUENCEFILE");
                GiRecord giRecord = new GiRecord(gi, sequenceFile);
                giRecord.setType(type);
                foundList.add(giRecord);
                foundGi.add(element);
            } else {
                NoFound nf = new NoFound(element, null);
                noFoundList.add(nf);
            }
        }
        
        DatabaseTransaction.closeResultSet(rs);
        return foundGi;
    }
}
