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

/**
 *
 * @author  dzuo
 */
public class FlexSeqBatchRetriever extends SeqBatchRetriever {
    
    /** Creates a new instance of FlexSeqBatchRetriever */
    public FlexSeqBatchRetriever() {
    }
    
    public FlexSeqBatchRetriever(List giList) {
        super(giList);
    }
        
    public boolean retrieveSequence() {
        if(giList == null || giList.size() == 0) {
            error = "No query list provided.";
            return false;
        }
        
        noFoundList.addAll(giList); 
        List foundGi = new ArrayList();
        String sql = "select * from girecord where gi=?";
        DatabaseTransaction t = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            stmt = conn.prepareStatement(sql);
            
            for(int i=0; i<noFoundList.size(); i++) {
                String element = (String)(noFoundList.get(i));
                stmt.setString(1, element);
                rs = DatabaseTransaction.executeQuery(stmt);
                
                if (rs.next()) {
                    String gi = rs.getString("GI");
                    String genbank = rs.getString("GENBANKACCESSION");
                    int genbankVersion = rs.getInt("GENBANKVERSION");
                    String sequenceFile = rs.getString("SEQUENCEFILE");
                    GiRecord giRecord = new GiRecord(gi, genbank, genbankVersion, sequenceFile);
                    foundList.add(giRecord);  
                    foundGi.add(element);
                }
            }
            
            noFoundList.removeAll(foundGi);
            
            return true;
        } catch (FlexDatabaseException ex) {
            error = ex.getMessage();
            return false;
        } catch (SQLException e) {
            error = e.getMessage();
            return false;
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
    }    
}
