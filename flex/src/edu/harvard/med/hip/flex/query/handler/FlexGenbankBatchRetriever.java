/*
 * FlexGenbankBatchRetriever.java
 *
 * Created on October 21, 2003, 5:42 PM
 */

package edu.harvard.med.hip.flex.query.handler;

import java.sql.*;
import java.util.*;

import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.query.core.*;

/**
 *
 * @author  DZuo
 */
public class FlexGenbankBatchRetriever extends GenbankBatchRetriever {
    
    /** Creates a new instance of FlexGenbankBatchRetriever */
    public FlexGenbankBatchRetriever() {
    }
    
    public Hashtable retrieveGenbank(List genbanks) throws Exception {        
        String sql = "select * from sequencerecord where accession=?";
        return doRetrieve(genbanks, sql);
    }
  
    public Hashtable retrieveRelatedGenbank(List genbanks) throws Exception {        
        String sql = "select * from sequencerecord where locusid = "+
                    " (select locusid from sequencerecord where accession=?)";
        return doRetrieve(genbanks, sql);
    }    
      
    public Hashtable retrieveRelatedCodingGenbank(List genbanks) throws Exception {        
        String sql = "select * from sequencerecord where locusid = "+
                    " (select locusid from sequencerecord where accession=?)"+
                    " and type in ('m', 'e')";
        return doRetrieve(genbanks, sql);
    } 
    
    protected Hashtable doRetrieve(List genbanks, String sql) throws Exception {
        Hashtable found = new Hashtable();
        if(genbanks == null || genbanks.size() == 0) {
            return found;
        }
        
        DatabaseTransaction t = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        t = DatabaseTransaction.getInstance();
        conn = t.requestConnection();
        stmt = conn.prepareStatement(sql);
        
        for(int i=0; i<genbanks.size(); i++) {
            String genbank = (String)genbanks.get(i);
            stmt.setString(1, genbank);
            rs = DatabaseTransaction.executeQuery(stmt);
            List matchs = new ArrayList();
            while(rs.next()) {
                String accession = rs.getString(1);
                int gi = rs.getInt(2);
                int locus = rs.getInt(3);
                String type = rs.getString(4);
                SequenceRecord sr = new SequenceRecord(accession, gi, locus, type);
                matchs.add(sr);
            }
            found.put(genbank, matchs);
        }
        
        DatabaseTransaction.closeResultSet(rs);
        DatabaseTransaction.closeStatement(stmt);
        DatabaseTransaction.closeConnection(conn);
        
        return found;
    }    
}
