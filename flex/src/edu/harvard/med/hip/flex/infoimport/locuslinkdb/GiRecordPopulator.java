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

/**
 *
 * @author  DZuo
 */
public class GiRecordPopulator {
        public final static String BLAST_FILE_DIR=FlexProperties.getInstance().getProperty("flex.repository.blastfiledir");
    
        private List records;
        
    /** Creates a new instance of GIRecordPopulator */
    public GiRecordPopulator(List records) {
        this.records = records;
    }
        
    public void persistRecords() {
        if(records == null || records.size() == 0)
            return;
        
        List insertList = new ArrayList();
        for(int i=0; i<records.size(); i++) {
            GiRecord giRecord = (GiRecord)records.get(i);
            try {
                String file = writeFile(giRecord.getGi(), giRecord.getSequenceFile());
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
            System.out.println(ex);
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
    }
    
    private String writeFile(int gi, String sf) throws Exception {
        String file = FlexSeqAnalyzer.BLAST_BASE_DIR+BLAST_FILE_DIR+gi;
        PrintWriter pr = new PrintWriter(new BufferedWriter(new FileWriter(file)));
        pr.println(sf);
        pr.close();
        
        return file;
    }
    
    private void insertDB(List records, Connection conn) throws Exception {
        String sql = "insert into girecord (gi, accession, cdsstart, cdsstop, sequencefile)"+
        " values(?,?,?,?,?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        
        for(int i=0; i<records.size(); i++) {
            GiRecord gr = (GiRecord)records.get(i);
            stmt.setInt(1, gr.getGi());
            stmt.setString(2, gr.getGenbankAccession());
            stmt.setInt(3, gr.getCdsStart());
            stmt.setInt(4, gr.getCdsStop());
            stmt.setString(5, gr.getSequenceFile());
            DatabaseTransaction.executeUpdate(stmt);
        }
        
        DatabaseTransaction.closeStatement(stmt);
    }
}
