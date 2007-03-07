/*
 * SequenceInfoImporter.java
 *
 * Created on February 20, 2007, 1:30 PM
 */

package plasmid.importexport.entrezgene;

import java.util.*;
import java.sql.*;
import java.io.*;

import plasmid.database.*;

/**
 *
 * @author  DZuo
 */
public class SequenceInfoImporter extends EntrezGeneImporter {
    
    /** Creates a new instance of SequenceInfoImporter */
    public SequenceInfoImporter() {
    }
    
    
    public void importInfoToDb(List input, Connection conn) throws Exception {
        if(input == null)
            throw new Exception("Sequence list is null");
        
        EntrezGeneParser parser = new EntrezGeneParser();
        parser.parseGene2accession(input, EntrezGeneParser.HUMAN);
        List seqs = parser.getSequences();
        
        String sql = "insert into sequencerecord_tmp(accession,gi,paccession,pgi,type,geneid)"+
        " values(?,?,?,?,?,?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        
        for(int i=0; i<seqs.size(); i++) {
            SequenceInfo seq = (SequenceInfo)seqs.get(i);
            if(foundSeq(seq, conn))
                continue;
            
            stmt.setString(1, seq.getAccession());
            stmt.setString(2, seq.getGi());
            stmt.setString(3, seq.getPaccession());
            stmt.setString(4, seq.getPgi());
            stmt.setString(5, seq.getType());
            stmt.setInt(6, seq.getGeneid());
            System.out.println("Insert accession="+seq.getAccession()+
            ",gi="+seq.getGi()+",paccession:"+seq.getPaccession()+",pgi"+seq.getPgi()+
            " for gene: "+seq.getGeneid());
            DatabaseTransaction.executeUpdate(stmt);
        }
        DatabaseTransaction.closeStatement(stmt);
    }
    
    public boolean foundSeq(SequenceInfo seq, Connection conn) throws Exception {
        String sql = "select count(*) from sequencerecord_tmp"+
        " where geneid=? and accession=? and paccession=?";
        
        PreparedStatement stmt1 = null;
        ResultSet rs = null;
        
        try {
            stmt1 = conn.prepareStatement(sql);
            stmt1.setInt(1, seq.getGeneid());
            stmt1.setString(2, seq.getAccession());
            stmt1.setString(3, seq.getPaccession());
            rs = DatabaseTransaction.executeQuery(stmt1);
            if(rs.next()) {
                int count = rs.getInt(1);
                if(count > 0) {
                    return true;
                }
            } 
        } catch (Exception ex) {
            throw ex;
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            if(stmt1 != null)
                DatabaseTransaction.closeStatement(stmt1);
        }
        return false;
    }
}
