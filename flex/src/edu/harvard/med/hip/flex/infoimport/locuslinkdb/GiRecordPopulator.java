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
                String file = writeFile(giRecord.getGi(), new StringBuffer(giRecord.getSequenceText()));
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
            
    public String writeFile(String gi, StringBuffer sf) throws Exception {
        String file = FlexSeqAnalyzer.BLAST_BASE_DIR+BLAST_FILE_DIR+gi;
        PrintWriter pr = new PrintWriter(new BufferedWriter(new FileWriter(file)));
        pr.println(sf.toString());
        pr.close();
        
        return file;
    }
    
    private void insertDB(List records, Connection conn) throws Exception {
        String sql = "insert into girecord (gi, accession, cdsstart, cdsstop, sequencefile, locusid, unigene)"+
        " values(?,?,?,?,?,?,?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        
        for(int i=0; i<records.size(); i++) {
            GiRecord gr = (GiRecord)records.get(i);
            stmt.setString(1, gr.getGi());
            stmt.setString(2, gr.getGenbankAccession());
            stmt.setInt(3, gr.getCdsStart());
            stmt.setInt(4, gr.getCdsStop());
            stmt.setString(5, gr.getSequenceFile());
            stmt.setString(6, gr.getLocusid());
            stmt.setString(7, gr.getUnigene());
            System.out.println("insert: "+gr.getGi());
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
    
    
    public void updateLocus() {
        String sqlUpdate="update girecord set locusid=? where gi=?";
        String sql = "select distinct locusid from sequencerecord where gi=?";
        String sqlQuery="select gi from girecord where locusid is null";
        
        DatabaseTransaction t = null;
        Connection conn = null;
        PreparedStatement stmtUpdate = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            stmt = conn.prepareStatement(sql);
            stmtUpdate = conn.prepareStatement(sqlUpdate);
            rs = t.executeQuery(sqlQuery);
            while(rs.next()) {
                String gi = rs.getString(1);
                stmt.setString(1, gi);
                
                rs1 = DatabaseTransaction.executeQuery(stmt);
                String locusid = null;
                while(rs1.next()) {
                    String currentLocusid = rs1.getString(1);
                    if(currentLocusid != null && currentLocusid.length() > 0) {
                        if(locusid == null) {
                            locusid = currentLocusid;
                        } else {
                            locusid = locusid + "," + currentLocusid;
                        }
                    }
                }
                
                stmtUpdate.setString(1, locusid);
                stmtUpdate.setString(2, gi);
                DatabaseTransaction.executeUpdate(stmtUpdate);
                
                System.out.println("update: "+gi+" - "+locusid);
            }
            
            DatabaseTransaction.commit(conn);
        } catch (Exception ex) {
            DatabaseTransaction.rollback(conn);
            System.out.println(ex);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeResultSet(rs1);
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeStatement(stmtUpdate);
            DatabaseTransaction.closeConnection(conn);
        }
    }
    
    public void updateUnigene() {
        String sqlUpdate="update girecord set unigene=? where gi=?";
        String sql = "select distinct unigeneid from generecord where locusid in (select distinct locusid from sequencerecord where gi=?)";
        String sqlQuery="select gi from girecord";
        //String sqlQuery="select gi from girecord where unigene is null";
        
        DatabaseTransaction t = null;
        Connection conn = null;
        PreparedStatement stmtUpdate = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            stmt = conn.prepareStatement(sql);
            stmtUpdate = conn.prepareStatement(sqlUpdate);
            rs = t.executeQuery(sqlQuery);
            int n=0;
            while(rs.next()) {
                String gi = rs.getString(1);
                stmt.setString(1, gi);
                
                rs1 = DatabaseTransaction.executeQuery(stmt);
                String unigeneid = null;
                while(rs1.next()) {
                    String currentUnigene = rs1.getString(1);
                    if(currentUnigene != null && currentUnigene.length() > 0) {
                        if(unigeneid == null) {
                            unigeneid = currentUnigene;
                        } else {
                            unigeneid = unigeneid + "," + currentUnigene;
                        }
                    }
                }
                
                if(unigeneid != null) {
                    stmtUpdate.setString(1, unigeneid);
                    stmtUpdate.setString(2, gi);
                    DatabaseTransaction.executeUpdate(stmtUpdate);
                    
                    System.out.println("update: "+gi+" - "+unigeneid);
                }
                
                n++;
                if(n==200) {
                    DatabaseTransaction.commit(conn);
                    n = 0;
                }
            }
            
            DatabaseTransaction.commit(conn);
        } catch (Exception ex) {
            DatabaseTransaction.commit(conn);
            System.out.println(ex);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeResultSet(rs1);
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeStatement(stmtUpdate);
            DatabaseTransaction.closeConnection(conn);
        }
    }
    
    public void parseFile(String file, Connection conn) throws Exception {
        List records = new ArrayList();
        
        BufferedReader in = new BufferedReader(new FileReader(file));
        String line = null;
        StringBuffer sb = new StringBuffer();
        String gi = null;
        String filename = null;
        
        int count = 0;
        while((line = in.readLine()) != null) {
            if(line.trim().equals("")) {
                filename = writeFile(gi, sb);
                sb = new StringBuffer();
                GiRecord gr = new GiRecord(gi, filename);
                records.add(gr);
                count++;
                
                if(count == 200) {
                    insertDB(records, conn);
                    DatabaseTransaction.commit(conn);
                    records = new ArrayList();
                    count = 0;
                }
                
                continue;
            }
            
            sb.append(line+"\n");
            
            if(line.indexOf(">") == 0) {
                StringTokenizer st = new StringTokenizer(line);
                String skip = st.nextToken("|");
                gi = st.nextToken("|");
            }
        }
        
        filename = writeFile(gi, sb);
        GiRecord gr = new GiRecord(gi, filename);
        records.add(gr);
        count++;
        insertDB(records, conn);
        DatabaseTransaction.commit(conn);
        
        in.close();
    }
    
    public static void main(String args[]) {
        /**   List giList = new ArrayList();
         * giList.add("32450632");
         * giList.add("21961206");
         * giList.add("33869456");
         * giList.add("33469967");
         * giList.add("33469918");
         * giList.add("33469916");
         * giList.add("16936529");
         * giList.add("37550355");
         * giList.add("16923985");
         * giList.add("34851998");
         *
         * SeqBatchRetriever retriever = new GenbankSeqBatchRetriever(giList);
         * try {
         * retriever.retrieveSequence();
         * } catch (Exception ex) {
         * System.out.println(ex);
         * }
         * ThreadedGiRecordPopulator populator = new ThreadedGiRecordPopulator((Collection)(retriever.getFoundList().values()));
         * populator.persistRecords();
         **/
        GiRecordPopulator populator = new GiRecordPopulator();
        populator.updateAccession();
        populator.updateLocus();
        populator.updateUnigene();
       /**         
        String file = "H:\\programs\\locus_gi_fasta";
        
        DatabaseTransaction t = null;
        Connection conn = null;
        
        GiRecordPopulator parser = new GiRecordPopulator();
        
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            parser.parseFile(file, conn);
        } catch (Exception ex) {
            DatabaseTransaction.rollback(conn);
            System.out.println(ex);
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }*/
    }
}
