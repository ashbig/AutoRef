/*
 * FastaFileGenerator.java
 *
 * Created on September 24, 2001, 2:33 PM
 */

package edu.harvard.med.hip.bec.export;


import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.util.Mailer;
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.bioutil.*;
import edu.harvard.med.hip.utility.Logger;
import edu.harvard.med.hip.utility.DatabaseManager;
import edu.harvard.med.hip.bec.programs.blast.*;
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
 * This class queries the bec database to get all the sequences and
 * generates the fasta format database file.
 */

public class FastaFileGenerator
{
   
    /*
     * public final static String BLAST_BASE_DIR=BecProperties.getInstance().getProperty("bec.repository.basedir");
    public final static String BLAST_DB_DIR=BecProperties.getInstance().getProperty("bec.repository.blast.relativedir");
    
    public static final String HUMANDB=BLAST_BASE_DIR+BLAST_DB_DIR+"Human/genes";
    public static final String YEASTDB=BLAST_BASE_DIR+BLAST_DB_DIR+"Yeast/genes";
    public static final String PSEUDOMONASDB=BLAST_BASE_DIR+BLAST_DB_DIR+"Pseudomonas/genes";
    public static final String MGCDB=BLAST_BASE_DIR+BLAST_DB_DIR+"MGC/genes";
    public static final String LOGFILE=BLAST_BASE_DIR+BLAST_DB_DIR+"Log/blastdb.log";
    public static final String SEQUENCEIDFILE=BLAST_BASE_DIR+BLAST_DB_DIR+"Log/sequenceid.txt";
     */
  
    public static final String LOGFILE="/tmp/blastdb.log";
    public static final String SEQUENCEIDFILE="/tmp/sequenceid.txt";
    public static final String SPECIES = "Species";
    
    public static void generateFastaFiles() throws Exception
    {
        Logger log = new Logger(LOGFILE);
        if(!log.start())
        {
            Mailer.sendMessage("elena_taycher@hms.harvard.edu", "elena_taycher@hms.harvard.edu", null, "error",
            "Cannot open log file.");
            return;
        }
        
        int lastSequence = 0;//getLastSequence(log);
        if(lastSequence == -1)
        {
            logAndMail(log, "Error occured when reading last sequence file.");
            return;
        }
        log.logging("Last sequenceid: "+lastSequence);
        
        /*
        // Generate FASTA file for all human genes.
        int maxid1 = generateFile(log, HUMANDB, HUMAN, lastSequence, SPECIES);
        if(maxid1 == -1) {
            logAndMail(log, "Error occured when generate human database file.");
            return;
        }
         
        // Generate FASTA file for all yeast genes.
        int maxid2 = generateFile(log, YEASTDB, YEAST, lastSequence, SPECIES);
        if(maxid2 == -1) {
            logAndMail(log, "Error occured when generate yeast database file.");
            return;
        }
         
        // Generate FASTA file for all Pseudomonas genes.
        int maxid3 = generateFile(log, PSEUDOMONASDB, PSEUDOMONAS, lastSequence, SPECIES);
        if(maxid3 == -1) {
            logAndMail(log, "Error occured when generate pseudomonas database file.");
            return;
        }
         
        // Generate FASTA file for all MGC clones.
        int maxid4 = generateFile(log, MGCDB, MGCPROJECT, lastSequence, MGCPROJECT);
        if(maxid4 == -1)
        {
            logAndMail(log, "Error occured when generate pseudomonas database file.");
            return;
        }
         **/
        
        /*
        int newLastSequence = maxid2;
        if(maxid1 > maxid2) {
            newLastSequence = maxid1;
        }
         
        if(maxid3 > newLastSequence) {
            newLastSequence = maxid3;
        }
       
        
        int newLastSequence = 0;
        if(maxid4 > newLastSequence)
        {
            newLastSequence = maxid4;
        }
          */
         // Generate FASTA file for all yeast genes.
        int maxid2 = generateFile(log, BlastWrapper.YEASTDB, RefSequence.SPECIES_YEAST, lastSequence, SPECIES);
        if(maxid2 == -1) {
            logAndMail(log, "Error occured when generate yeast database file.");
            return;
        }
        /*
        if(!writeLastSequence(newLastSequence, log))
        {
            logAndMail(log, "Error occured while writting to sequence file");
            return;
        }
        */
        log.end();
    }
    
    private static int getLastSequence(Logger log)
    {
        BufferedReader in = null;
        try
        {
            in = new BufferedReader(new FileReader(SEQUENCEIDFILE));
            String line = in.readLine();
            in.close();
            return Integer.parseInt(line);
        }catch (Exception ex)
        {
            log.logging(ex.getMessage());
            return -1;
        }
    }
    
    private static int generateFile(Logger log, String db, int species, int seq, String criteria)
    {
        
        
        DatabaseTransaction t = null;
        ResultSet rs = null;
        PrintWriter pr = null;
        int maxid = seq;
        
        try
        {
            t = DatabaseTransaction.getInstance();
            pr = new PrintWriter(new BufferedWriter(new FileWriter(db, true)));
            String sql = getSql(species, seq, criteria);
            
            rs = t.executeQuery(sql);
            while(rs.next())
            {
                int id = rs.getInt("SEQUENCEID");
                log.logging("Processing sequence: "+id);
                RefSequence sequence = new RefSequence(id);
                pr.print(sequence.getFastaHeader());
                pr.println(SequenceManipulation.convertToFasta(sequence.getCodingSequence()));
                log.logging("...OK");
                maxid = id;
            }
            pr.close();
            return maxid;
        }catch (IOException e)
        {
            log.logging(e.getMessage());
            return -1;
        }catch (SQLException sqlE)
        {
            log.logging(sqlE.getMessage());
            return -1;
        }catch (BecDatabaseException ex)
        {
            log.logging(ex.getMessage());
            return -1;
        }catch (Exception ex)
        {
            log.logging(ex.getMessage());
            return -1;
        }finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
         
       
    }
    
    public static boolean writeLastSequence(int newLastSequence, Logger log)
    {
        PrintWriter SEQFILE = null;
        try
        {
            SEQFILE = new PrintWriter(new BufferedWriter(new FileWriter(SEQUENCEIDFILE)));
            SEQFILE.println(newLastSequence);
            SEQFILE.close();
            return true;
        }catch (Exception e)
        {
            log.logging(e.getMessage());
            return false;
        }
    }
    
    private static void logAndMail(Logger log, String message)throws Exception
    {
        log.logging(message);
         Mailer.sendMessage("elena_taycher@hms.harvard.edu", "elena_taycher@hms.harvard.edu", null, "error",
            message);
      
        log.end();
    }
    
   /* private static void sendEmail(String message)
    {
        String to = "dzuo@hms.harvard.edu";
        String from = "bec@hms.harvard.edu";
        String subject = "FLEX sequence export failed.";
        
        try
        {
            Mailer.sendMessage(to, from, subject, message);
        } catch (MessagingException ex)
        {
            System.out.println(ex);
        }
    }
    **/
    
    private static String getSql(int where, int seq, String criteria)
    {
        String sql = null;
        
        if(criteria == SPECIES)
        {
            sql = "select sequenceid from refsequence where genusspecies = " +where + "  order by sequenceid";
        }
        return sql;
    }
    
    public static void main(String [] args)
    {
        try{
        FastaFileGenerator.generateFastaFiles();
        }
        catch(Exception e){}
    }
}


