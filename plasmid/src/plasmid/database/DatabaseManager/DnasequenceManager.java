/*
 * DnasequenceManager.java
 *
 * Created on April 26, 2005, 9:53 AM
 */

package plasmid.database.DatabaseManager;

import java.sql.*;
import java.util.*;

import plasmid.coreobject.*;
import plasmid.database.*;
import plasmid.Constants;

/**
 *
 * @author  DZuo
 */
public class DnasequenceManager extends TableManager {
    private Connection conn;
    
    /** Creates a new instance of DnasequenceManager */
    public DnasequenceManager() {
    }
    
    public DnasequenceManager(Connection conn) {
        this.conn = conn;
    }
    
    public boolean insertDnasequences(List sequences) {
        if(sequences == null)
            return true;
        
        String sql = "insert into dnasequence"+
        " (sequenceid, type, referenceid, insertid)"+
        " values(?,?,?,?)";
        String sql2 = "insert into seqtext"+
        " (sequenceid, seqorder, seqtext)"+
        " values(?,?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            PreparedStatement stmt2 = conn.prepareStatement(sql2);
            
            for(int i=0; i<sequences.size(); i++) {
                Dnasequence seq = (Dnasequence)sequences.get(i);
                stmt.setInt(1, seq.getSequenceid());
                String type = seq.getType();
                stmt.setString(2, type);
                if(Dnasequence.REFERENCE.equals(type)) {
                    stmt.setInt(3, seq.getReferenceid());
                    stmt.setString(4, null);
                }
                else {
                    stmt.setString(3, null);
                    stmt.setInt(4, seq.getInsertid());
                }
                
                DatabaseTransaction.executeUpdate(stmt);
                
                String sequencetext = seq.getSequence();
                if(sequencetext != null) {
                    int n=0;
                    while(sequencetext.length()-4000*n>4000) {
                        String text = sequencetext.substring(4000*(n), 4000*(n+1)).toUpperCase();
                        stmt2.setInt(1, seq.getSequenceid());
                        stmt2.setInt(2, n+1);
                        stmt2.setString(3, text);
                        DatabaseTransaction.executeUpdate(stmt2);
                        
                        n++;
                    }
                    String text = sequencetext.substring(4000*(n));
                    stmt2.setInt(1, seq.getSequenceid());
                    stmt2.setInt(2, n+1);
                    stmt2.setString(3, text);
                    DatabaseTransaction.executeUpdate(stmt2);
                }
            }
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeStatement(stmt2);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into DNASEQUENCE and SEQTEXT table");
            return false;
        }
        return true;
    }
}
