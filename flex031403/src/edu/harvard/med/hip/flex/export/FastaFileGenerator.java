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

//    public final static String BLAST_BASE_DIR="/kotel/data/FLEXRepository/";
//    public final static String BLAST_DB_DIR="Blast/";

    public static final String HUMANDB=BLAST_BASE_DIR+BLAST_DB_DIR+"Human/genes";
    public static final String YEASTDB=BLAST_BASE_DIR+BLAST_DB_DIR+"Yeast/genes";
    public static final String PSEUDOMONASDB=BLAST_BASE_DIR+BLAST_DB_DIR+"Pseudomonas/genes";
    public static final String LOGFILE=BLAST_BASE_DIR+BLAST_DB_DIR+"Log/blastdb.log";
    public static final String SEQUENCEIDFILE=BLAST_BASE_DIR+BLAST_DB_DIR+"Log/sequenceid.txt";
    
//    public static final String HUMANDB="/tmp/Human/genes";
//    public static final String YEASTDB="/tmp/Yeast/genes";
//    public static final String LOGFILE="/tmp/Log/blastdb.log";
//    public static final String SEQUENCEIDFILE="/tmp/Log/sequenceid.txt";
    
    public static final String HUMAN = "'Homo sapiens'";
    public static final String YEAST = "'Saccharomyces cerevisiae'";
    public static final String PSEUDOMONAS = "'Pseudomonas aeruginosa'";
    
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
        
        int maxid1 = generateFile(log, HUMANDB, HUMAN, lastSequence);
        if(maxid1 == -1) {
            logAndMail(log, "Error occured when generate human database file.");
            return;
        }
        
        int maxid2 = generateFile(log, YEASTDB, YEAST, lastSequence);
        if(maxid2 == -1) {
            logAndMail(log, "Error occured when generate yeast database file.");
            return;
        }

        int maxid3 = generateFile(log, PSEUDOMONASDB, PSEUDOMONAS, lastSequence);
        if(maxid3 == -1) {
            logAndMail(log, "Error occured when generate pseudomonas database file.");
            return;
        }
        
        int newLastSequence = maxid2;
        if(maxid1 > maxid2) {
            newLastSequence = maxid1;
        }
        
        if(maxid3 > newLastSequence) {
            newLastSequence = maxid3;
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
    
    private static int generateFile(Logger log, String db, String species, int seq) {
        DatabaseTransaction t = null;
        ResultSet rs = null;
        PrintWriter pr = null;
        int maxid = seq;
        
        try {
            t = DatabaseTransaction.getInstance();
            pr = new PrintWriter(new BufferedWriter(new FileWriter(db, true)));
            String sql = "select sequenceid " +
            "from flexsequence " +
            "where genusspecies = " +species + " " +
            "and sequenceid > "+seq + " " +
            "order by sequenceid";
        
            rs = t.executeQuery(sql);            
            while(rs.next()) {
                int id = rs.getInt("SEQUENCEID");
                log.logging("Processing sequence: "+id);
                FlexSequence sequence = new FlexSequence(id);
                pr.print(sequence.getFastaHeader());
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
        String to = "dzuo@hms.harvard.edu";
        String from = "flex@hms.harvard.edu";
        String subject = "FLEX sequence export failed.";
        
        try {
            Mailer.sendMessage(to, from, subject, message);
        } catch (MessagingException ex){
            System.out.println(ex);
        }
    }
    
    public static void main(String [] args) {
        FastaFileGenerator.generateFastaFiles();
    }
}


