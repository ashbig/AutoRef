/*
 * FastaFileGenerator.java
 *
 * Created on September 24, 2001, 2:33 PM
 */

package edu.harvard.med.hip.flex.export;

import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.util.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.util.Mailer;
import edu.harvard.med.hip.utility.Logger;
import edu.harvard.med.hip.utility.DatabaseManager;
import edu.harvard.med.hip.flex.workflow.Project;
import java.io.*;
import java.util.*;
import java.sql.*;
import java.math.BigDecimal;
import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;

/**
 *
 * @author  dzuo
 * @version
 */

/**
 * This class queries the flex database to get all the sequences and
 * generates the fasta format database file.
 */

public class FastaFileGenerator {
    public final static String BLAST_BASE_DIR=FlexProperties.getInstance().getProperty("flex.repository.basedir");
    public final static String BLAST_DB_DIR=FlexProperties.getInstance().getProperty("flex.repository.blast.relativedir");
    
    public static final String HUMANDB=BLAST_BASE_DIR+BLAST_DB_DIR+"Human/genes";
    public static final String YEASTDB=BLAST_BASE_DIR+BLAST_DB_DIR+"Yeast/genes";
    public static final String PSEUDOMONASDB=BLAST_BASE_DIR+BLAST_DB_DIR+"Pseudomonas/genes";
    public static final String MGCDB=BLAST_BASE_DIR+BLAST_DB_DIR+"MGC/genes";
    public static final String YPDB=BLAST_BASE_DIR+BLAST_DB_DIR+"YP/genes";
    public static final String BCDB=BLAST_BASE_DIR+BLAST_DB_DIR+"BC/genes";
    public static final String NIDDKDB=BLAST_BASE_DIR+BLAST_DB_DIR+"NIDDK/genes";
    public static final String CLONTECHDB=BLAST_BASE_DIR+BLAST_DB_DIR+"Clontech/genes";
    public static final String RZPDWALLDB=BLAST_BASE_DIR+BLAST_DB_DIR+"RZPD-WALL/genes";
    public static final String FTDB=BLAST_BASE_DIR+BLAST_DB_DIR+"FT/genes";
    public static final String KINASEDB=BLAST_BASE_DIR+BLAST_DB_DIR+"Kinase/genes";
    public static final String SEQVERIFIEDDB=BLAST_BASE_DIR+BLAST_DB_DIR+"Sequence_Verified/genes";
    public static final String VERIFIEDBCDB=BLAST_BASE_DIR+BLAST_DB_DIR+"Sequence_Verified_BC/genes";
    public static final String VERIFIEDKINASEDB=BLAST_BASE_DIR+BLAST_DB_DIR+"Sequence_Verified_Kinase/genes";
    public static final String VERIFIEDHUMANDB=BLAST_BASE_DIR+BLAST_DB_DIR+"Sequence_Verified_Human/genes";
    public static final String ALLDB=BLAST_BASE_DIR+BLAST_DB_DIR+"BlastDB/genes";
        
    public static final String LOGFILE=BLAST_BASE_DIR+BLAST_DB_DIR+"Log/blastdb.log";
    public static final String SEQUENCEIDFILE=BLAST_BASE_DIR+BLAST_DB_DIR+"Log/sequenceid.txt";
                            
    public static final String HUMAN = "'Homo sapiens'";
    public static final String YEAST = "'Saccharomyces cerevisiae'";
    public static final String PSEUDOMONAS = "'Pseudomonas aeruginosa'";
    public static final String YP = "'Yersinia pestis'";
    public static final String FT = "'Francisella tularensis'";
    
    public static final String SPECIES = "Species";
    public static final String MGCPROJECT = "MGC Project";
    public static final String PROJECT = "Project";
    public static final String VERIFIED = "Verified";
    public static final String VERIFIED_HUMAN = "Verified Human";
    public static final String VERIFIED_BC = "Verified BC";
    public static final String VERIFIED_KINASE = "Verified Kinase";
    public static final String ALL = "All";
    
    public static void generateFastaFiles() {
        Logger log = new Logger(LOGFILE);
        if(!log.start()) {
            sendEmail("Cannot open log file.");
            return;
        }
        
        int lastSequence = getLastSequence(log);
        if(lastSequence == -1) {
            logAndMail(log, "Error occured when reading last sequence file.");
            return;
        }
        log.logging("Last sequenceid: "+lastSequence);
        
        // Generate FASTA file for all human genes.
        log.logging("Generate human database");
        int maxid1 = generateFile(log, HUMANDB, HUMAN, lastSequence, SPECIES);
        if(maxid1 == -1) {
            logAndMail(log, "Error occured when generate human database file.");
            return;
        }
        
        // Generate FASTA file for all yeast genes.
        log.logging("Generate yeast database");
        int maxid2 = generateFile(log, YEASTDB, YEAST, lastSequence, SPECIES);
        if(maxid2 == -1) {
            logAndMail(log, "Error occured when generate yeast database file.");
            return;
        }
        
        // Generate FASTA file for all Pseudomonas genes.
        log.logging("Generate Pseudomonas database");
        int maxid3 = generateFile(log, PSEUDOMONASDB, PSEUDOMONAS, lastSequence, SPECIES);
        if(maxid3 == -1) {
            logAndMail(log, "Error occured when generate pseudomonas database file.");
            return;
        }
        
        // Generate FASTA file for all Yersinia pestis genes.
        log.logging("Generate Yersinia database");
        int maxid5 = generateFile(log, YPDB, YP, lastSequence, SPECIES);
        if(maxid5 == -1) {
            logAndMail(log, "Error occured when generate yersinia database file.");
            return;
        }
        
        // Generate FASTA file for all MGC clones.
        log.logging("Generate MGC database");
        int maxid4 = generateFile(log, MGCDB, MGCPROJECT, lastSequence, MGCPROJECT);
        if(maxid4 == -1) {
            logAndMail(log, "Error occured when generate MGC database file.");
            return;
        }
        
        log.logging("Generate BC database");
        int maxid6 = generateFile(log, BCDB, (new Integer(Project.BREASTCANCER)).toString(), lastSequence, PROJECT);
        if(maxid6 == -1) {
            logAndMail(log, "Error occured when generate Breast Cancer database file.");
            return;
        }
        
        log.logging("Generate NIDDK database");
        int maxid7 = generateFile(log, NIDDKDB, (new Integer(Project.NIDDK)).toString(), lastSequence, PROJECT);
        if(maxid7 == -1) {
            logAndMail(log, "Error occured when generate NIDDK database file.");
            return;
        }
        
        log.logging("Generate CLONTECH database");
        int maxid8 = generateFile(log, CLONTECHDB, (new Integer(Project.CLONTECH)).toString(), lastSequence, PROJECT);
        if(maxid8 == -1) {
            logAndMail(log, "Error occured when generate NIDDK database file.");
            return;
        }
        
        log.logging("Generate RZPDWALL database");
        int maxid9 = generateFile(log, RZPDWALLDB, (new Integer(Project.RZPD_WALL)).toString(), lastSequence, PROJECT);
        if(maxid9 == -1) {
            logAndMail(log, "Error occured when generate RZPD-Wall database file.");
            return;
        }
        
        log.logging("Generate FT database");
        int maxid10 = generateFile(log, FTDB, FT, lastSequence, SPECIES);
        if(maxid10 == -1) {
            logAndMail(log, "Error occured when generate FT database file.");
            return;
        }
        
        log.logging("Generate Kinase database");
        int maxid11 = generateFile(log, KINASEDB, (new Integer(Project.KINASE)).toString(), lastSequence, PROJECT);
        if(maxid11 == -1) {
            logAndMail(log, "Error occured when generate Kinase database file.");
            return;
        }
               
        log.logging("Generate sequence verified database");
        int maxid12 = generateFile(log, SEQVERIFIEDDB, null, lastSequence, VERIFIED);
        if(maxid12 == -1) {
            logAndMail(log, "Error occured when generate Sequence Verified database file.");
            return;
        }
                
        log.logging("Generate BC sequence verified database");
        int maxid13 = generateFile(log, VERIFIEDBCDB, null, lastSequence, VERIFIED_BC);
        if(maxid13 == -1) {
            logAndMail(log, "Error occured when generate Sequence Verified BC database file.");
            return;
        }
                
        log.logging("Generate kinase sequence verified database");
        int maxid14 = generateFile(log, VERIFIEDKINASEDB, null, lastSequence, VERIFIED_KINASE);
        if(maxid14 == -1) {
            logAndMail(log, "Error occured when generate Sequence Verified Kinase database file.");
            return;
        }
                 
        log.logging("Generate human sequence verified database");
        int maxid15 = generateFile(log, VERIFIEDHUMANDB, null, lastSequence, VERIFIED_HUMAN);
        if(maxid15 == -1) {
            logAndMail(log, "Error occured when generate Sequence Verified Human database file.");
            return;
        }
                 
        log.logging("Generate all sequence database");
        int maxid16 = generateFile(log, ALLDB, null, lastSequence, ALL);
        if(maxid16 == -1) {
            logAndMail(log, "Error occured when generate entire database file.");
            return;
        }
                                      
        int newLastSequence = maxid2;
        if(maxid1 > maxid2) {
            newLastSequence = maxid1;
        }
        
        if(maxid3 > newLastSequence) {
            newLastSequence = maxid3;
        }
        
        if(maxid4 > newLastSequence) {
            newLastSequence = maxid4;
        }
        
        if(maxid5 > newLastSequence) {
            newLastSequence = maxid5;
        }
         
        if(maxid6 > newLastSequence) {
            newLastSequence = maxid6;
        }
                  
        if(maxid7 > newLastSequence) {
            newLastSequence = maxid7;
        }
           
        if(maxid8 > newLastSequence) {
            newLastSequence = maxid8;
        }
           
        if(maxid9 > newLastSequence) {
            newLastSequence = maxid9;
        }
           
        if(maxid10 > newLastSequence) {
            newLastSequence = maxid10;
        }
           
        if(maxid11 > newLastSequence) {
            newLastSequence = maxid11;
        }
           
        if(maxid12 > newLastSequence) {
            newLastSequence = maxid12;
        }
           
        if(maxid13 > newLastSequence) {
            newLastSequence = maxid13;
        }
           
        if(maxid14 > newLastSequence) {
            newLastSequence = maxid14;
        }
           
        if(maxid15 > newLastSequence) {
            newLastSequence = maxid15;
        }
            
        if(maxid16 > newLastSequence) {
            newLastSequence = maxid16;
        }
     
        if(!writeLastSequence(newLastSequence, log)) {
            logAndMail(log, "Error occured while writting to sequence file");
            return;
        }
        
        log.end();
    }
    
    private static int getLastSequence(Logger log) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(SEQUENCEIDFILE));
            String line = in.readLine();
            in.close();
            return Integer.parseInt(line);
        }catch (Exception ex) {
            log.logging(ex.getMessage());
            return -1;
        }
    }
    
    private static int generateFile(Logger log, String db, String species, int seq, String criteria) {
        DatabaseTransaction t = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        PrintWriter pr = null;
        int maxid = seq;
        
        try {
            t = DatabaseTransaction.getInstance();
            pr = new PrintWriter(new BufferedWriter(new FileWriter(db, true)));
            String sql = getSql(species, seq, criteria);
            String sql2 = "select * from sequencetext where sequenceid=? order by sequenceorder";
            conn = t.requestConnection();
            stmt = conn.prepareStatement(sql2);

            rs = t.executeQuery(sql);
            while(rs.next()) {
                int id = rs.getInt(1);
                int cdsstart = rs.getInt(2);
                int cdsstop = rs.getInt(3);
                log.logging("Processing sequence: "+id);
                
                stmt.setInt(1, id);
                rs2 = DatabaseTransaction.executeQuery(stmt);  
                
                String sequencetext = "";                
                while(rs2.next()) {
                    sequencetext = sequencetext+rs2.getString("SEQUENCETEXT");
                }
                
                CDNASequence sequence = new CDNASequence(cdsstart, cdsstop, sequencetext);
                pr.print(">"+id);
                pr.println(sequence.getCDSFasta());
                log.logging("...OK");
                maxid = id;
            }
            pr.close();
            return maxid;
        }catch (IOException e) {
            log.logging(e.getMessage());
            return -1;
        }catch (SQLException sqlE) {
            log.logging(sqlE.getMessage());
            return -1;
        }catch (FlexDatabaseException ex) {
            log.logging(ex.getMessage());
            return -1;
        }catch (Exception ex) {
            log.logging(ex.getMessage());
            return -1;
        }finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeResultSet(rs2);
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeConnection(conn);
        }
    }
    
    public static boolean writeLastSequence(int newLastSequence, Logger log) {
        PrintWriter SEQFILE = null;
        try {
            SEQFILE = new PrintWriter(new BufferedWriter(new FileWriter(SEQUENCEIDFILE)));
            SEQFILE.println(newLastSequence);
            SEQFILE.close();
            return true;
        }catch (Exception e) {
            log.logging(e.getMessage());
            return false;
        }
    }
    
    private static void logAndMail(Logger log, String message) {
        log.logging(message);
        sendEmail(message);
        log.end();
    }
    
    private static void sendEmail(String message) {
        String to = "dongmei_zuo@hms.harvard.edu";
        String from = "flex@hms.harvard.edu";
        String subject = "FLEX sequence export failed.";
        
        try {
            Mailer.sendMessage(to, from, subject, message);
        } catch (MessagingException ex){
            System.out.println(ex);
        }
    }
    
    private static String getSql(String where, int seq, String criteria) {
        String sql = null;
        
        if(criteria.equals(SPECIES)) {
            sql = "select sequenceid, cdsstart, cdsstop " +
            "from flexsequence " +
            "where genusspecies = " +where + " " +
            "and sequenceid > "+seq + " " +
            "order by sequenceid";
        }
        
        if(criteria.equals(MGCPROJECT)) {
            sql = "select distinct f.sequenceid, f.cdsstart, f.cdsstop "+
            " from mgcclone m, flexsequence f "+
            " where f.sequenceid > "+seq+
            " and m.sequenceid=f.sequenceid"+
            " order by f.sequenceid";
        }
        
        if(criteria.equals(PROJECT)) {
            sql = "select distinct f.sequenceid, f.cdsstart, f.cdsstop"+
                  " from requestsequence r, flexsequence f"+
                  " where f.sequenceid > "+seq+
                  " and r.projectid="+where+
                  " and r.sequenceid=f.sequenceid"+
                  " order by f.sequenceid";
        }
        
        if(criteria.equals(VERIFIED)) {
            sql = "select sequenceid, cdsstart, cdsstop"+
                " from flexsequence"+
                " where sequenceid>"+seq+
                " and flexstatus='"+FlexSequence.OBTAINED+"'"+
                " order by sequenceid";
        }
        
        if(criteria.equals(VERIFIED_HUMAN)) {
            sql = "select sequenceid, cdsstart, cdsstop"+
                " from flexsequence"+
                " where sequenceid>"+seq+
                " and flexstatus='"+FlexSequence.OBTAINED+"'"+
                " and genusspecies="+HUMAN+
                " order by sequenceid";
        }
        
        if(criteria.equals(VERIFIED_BC)) {
            sql = "select distinct f.sequenceid, f.cdsstart, f.cdsstop"+
                " from flexsequence f, requestsequence r"+
                " where f.sequenceid=r.sequenceid"+
                " and f.sequenceid>"+seq+
                " and f.flexstatus='"+FlexSequence.OBTAINED+"'"+
                " and r.projectid="+Project.BREASTCANCER+
                " order by f.sequenceid";
        }
        
        if(criteria.equals(VERIFIED_KINASE)) {
            sql = "select distinct f.sequenceid, f.cdsstart, f.cdsstop"+
            " from flexsequence f, requestsequence r"+
            " where f.sequenceid=r.sequenceid"+
            " and f.sequenceid>"+seq+
            " and f.flexstatus='"+FlexSequence.OBTAINED+"'"+
            " and r.projectid="+Project.KINASE+
            " order by f.sequenceid";
        }
         
        if(criteria.equals(ALL)) {
           sql = "select sequenceid, cdsstart, cdsstop "+
            " from flexsequence "+
            " where sequenceid > "+seq+
            " order by sequenceid";
        }
               
        return sql;
    }
    
    public static void main(String [] args) {
        FastaFileGenerator.generateFastaFiles();
    }
}


