/*
 * GiRecordPopulator.java
 *
 * Created on November 6, 2003, 1:00 PM
 */

package edu.harvard.med.hip.flex.infoimport.locuslinkdb;

import java.io.*;
import java.util.*;
import java.sql.*;

import edu.harvard.med.hip.flex.util.FlexProperties;
import edu.harvard.med.hip.flex.util.FlexSeqAnalyzer;
import edu.harvard.med.hip.flex.query.core.GiRecord;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.query.handler.*;
import edu.harvard.med.hip.flex.core.CDNASequence;

/**
 *
 * @author  DZuo
 */
public class GiRecordPopulator {
    public final static String BLAST_FILE_DIR=FlexProperties.getInstance().getProperty("flex.repository.blastfiledir");
    
    private Collection records;
    
    public GiRecordPopulator() {}
    
    /** Creates a new instance of GIRecordPopulator */
    public GiRecordPopulator(Collection records) {
        this.records = records;
    }
    
    public void persistRecords() {
        if(records == null || records.size() == 0)
            return;
        
        List insertList = new ArrayList();
        Iterator iter = records.iterator();
        while(iter.hasNext()) {
            GiRecord giRecord = (GiRecord)iter.next();
            //System.out.println("GI:"+giRecord.getGi());
            //System.out.println("Genbank: "+giRecord.getGenbankAccession());
            //System.out.println("Sequence file: "+giRecord.getSequenceFile());
            //System.out.println("sequence: "+giRecord.getSequenceText());
            try {
                String file = writeFile(giRecord.getGi(), giRecord.getSequenceText());
                giRecord.setSequenceFile(file);
                insertList.add(giRecord);
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
        
        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            insertDB(insertList, conn);
            DatabaseTransaction.commit(conn);
        } catch (Exception ex) {
            DatabaseTransaction.rollback(conn);
            System.out.println(ex);
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
    }
    
    private String writeFile(String gi, String sf) throws Exception {
        String file = FlexSeqAnalyzer.BLAST_BASE_DIR+BLAST_FILE_DIR+gi;
        PrintWriter pr = new PrintWriter(new BufferedWriter(new FileWriter(file)));
        pr.print(">gi|"+gi);
        pr.println(CDNASequence.convertToFasta(sf));
        pr.close();
        
        return file;
    }
    
    private void insertDB(List records, Connection conn) throws Exception {
        String sql = "insert into girecord (gi, accession, cdsstart, cdsstop, sequencefile)"+
        " values(?,?,?,?,?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        
        for(int i=0; i<records.size(); i++) {
            GiRecord gr = (GiRecord)records.get(i);
            stmt.setString(1, gr.getGi());
            stmt.setString(2, gr.getGenbankAccession());
            stmt.setInt(3, gr.getCdsStart());
            stmt.setInt(4, gr.getCdsStop());
            stmt.setString(5, gr.getSequenceFile());
            DatabaseTransaction.executeUpdate(stmt);
        }
        
        DatabaseTransaction.closeStatement(stmt);
    }
    
    public void updateAccession() {
        String sql="update girecord set accession=("+
                    " select distinct accession from sequencerecord where gi=?) where gi=?";
        String sqlQuery="select gi from girecord where accession is null";
        
        DatabaseTransaction t = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            stmt = conn.prepareStatement(sql);
            rs = t.executeQuery(sqlQuery);
            while(rs.next()) {
                String gi = rs.getString(1);
                stmt.setString(1, gi);
                stmt.setString(2, gi);
                DatabaseTransaction.executeUpdate(stmt);
                System.out.println("update: "+gi);
            }
            
            DatabaseTransaction.commit(conn);
        } catch (Exception ex) {
            DatabaseTransaction.rollback(conn);
            System.out.println(ex);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeConnection(conn);
        }        
    }
    
    public static void main(String args[]) {
     /**   List giList = new ArrayList();
        giList.add("32450632");
        giList.add("21961206");
        giList.add("33869456");
        giList.add("33469967");
        giList.add("33469918");
        giList.add("33469916");
        giList.add("16936529");
        giList.add("37550355");
        giList.add("16923985");
        giList.add("34851998");
        
        SeqBatchRetriever retriever = new GenbankSeqBatchRetriever(giList);
        try {
            retriever.retrieveSequence();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        ThreadedGiRecordPopulator populator = new ThreadedGiRecordPopulator((Collection)(retriever.getFoundList().values()));
        populator.persistRecords();
      **/
        GiRecordPopulator populator = new GiRecordPopulator();
        populator.updateAccession();
    }
}
