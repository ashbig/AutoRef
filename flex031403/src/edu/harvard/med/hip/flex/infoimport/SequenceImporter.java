/*
 * SequenceImporter.java
 *
 * This class imports the sequences from the file into the FLEXGene database.
 * The file contains all the information related to flexsequence, sequencetext
 * tables separated by delimiters.
 *
 * Created on October 24, 2001, 11:47 AM
 */

package edu.harvard.med.hip.flex.infoimport;

import java.io.*;
import java.util.*;
import java.sql.*;

import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.database.*;

/**
 *
 * @author  dzuo
 * @version
 */
public class SequenceImporter {
    public static final String DILIM = ":\t";
    public static final String STATUS = FlexSequence.NEW;
    
    private Hashtable publicInfo;
    private Hashtable sequences;
    private Vector results;
    
    /** Creates new ImportSequence
     *
     * @return The SequenceImporter object.
     */
    public SequenceImporter() {
        publicInfo = new Hashtable();
        sequences = new Hashtable();
        results = new Vector();
    }
    
    /**
     * Import the sequences into the database.
     *
     * @param sequences The InputStream object for sequences.
     * @param names The InputStream object for names.
     * @param conn The Connection object for insert.
     * @return true if the import successful; false otherwise.
     */
    public boolean performImport(InputStream sequenceInput, InputStream nameInput, Connection conn) {
        if(readPublicInfo(nameInput) && readSequences(sequenceInput)) {
            Enumeration enum = sequences.keys();
            while(enum.hasMoreElements()) {
                String k = (String)enum.nextElement();
                FlexSequence seq = (FlexSequence)sequences.get(k);
                try {
                    seq.insert(conn);
                    SequenceImporterLogger logger = new SequenceImporterLogger(k, seq.getId(), true, null);
                    results.addElement(logger);
                } catch (FlexDatabaseException ex) {
                    SequenceImporterLogger logger = new SequenceImporterLogger(k, seq.getId(), false, ex.getMessage());
                    results.addElement(logger);
                }
            }
            
            return true;
        } else {
            return false;
        }
    }
    
    
    /**
     * Read the public information from the input stream.
     * The input is in the following format:
     * sequenceid:nametype:namevalue:nameurl:description
     * sequenceid:nametype:namevalue:nameurl:description
     * ...
     *
     * @param input The InputStream object containing the public information.
     * @return true if successful; false otherwise.
     */
    public boolean readPublicInfo(InputStream input) {
        BufferedReader in = new BufferedReader(new InputStreamReader(input));
        String line = null;
        
        try {
            while((line = in.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, DILIM);
                String [] info = new String[5];
                int i = 0;
                Hashtable name = new Hashtable();
                
                try {
                    while(st.hasMoreTokens()) {
                        info[i] = st.nextToken();
                        i++;
                    }
                } catch (NoSuchElementException ex) {}
                
                name.put(FlexSequence.NAMETYPE, info[1]);
                name.put(FlexSequence.NAMEVALUE, info[2]);
                name.put(FlexSequence.NAMEURL, info[3]);
                name.put(FlexSequence.DESCRIPTION, info[4]);
                
                Vector foundInfo = null;
                if((foundInfo = (Vector)publicInfo.get(info[0])) != null) {
                    foundInfo.addElement(name);
                } else {
                    foundInfo = new Vector();
                    foundInfo.addElement(name);
                    publicInfo.put(info[0], foundInfo);
                }
            }
            
            return true;
        }catch (IOException ex) {
            return false;
        }
    }
    
    /**
     * Read the sequence information from the input stream.
     * The format of the input is as follows:
     * sequenceid:species:cdsstart:cdsstop:cdslength:gccontent:cdnasource:chromosome:sequencetext
     * ...
     *
     * @param input The InputStream object containing the sequence information.
     * @return true if successful; false otherwise.
     */
    public boolean readSequences(InputStream input) {
        BufferedReader in = new BufferedReader(new InputStreamReader(input));
        String line = null;
        
        try {
            while((line = in.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, DILIM);
                String [] info = new String[9];
                int i = 0;
                
                try {
                    while(st.hasMoreTokens()) {
                        info[i] = st.nextToken();
                        i++;
                    }
                    
                    Vector names = (Vector)publicInfo.get(info[0]);
                    FlexSequence seq = new FlexSequence(-1,STATUS,info[1],null,
                    info[8], Integer.parseInt(info[2]),
                    Integer.parseInt(info[3]),
                    Integer.parseInt(info[4]),
                    Integer.parseInt(info[5]), names,
                    info[6], info[7]);
                    sequences.put(info[0], seq);
                } catch (NoSuchElementException ex) {}
            }
            
            return true;
        } catch (IOException ex) {
            return false;
        }
    }
    
    /**
     * Return the results.
     *
     * @return results.
     */
    public Vector getResults() {
        return results;
    }
    
    public static void main(String args[]) {
        String sequenceFile = "C:\\TEMP\\FlexSequenceFile";
        String nameFile = "C:\\TEMP\\FlexNameFile";
        InputStream sequenceInput;
        InputStream nameInput;
        
        try {
            sequenceInput = new FileInputStream(sequenceFile);
            nameInput = new FileInputStream(nameFile);
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
            return;
        }
        
        SequenceImporter importer = new SequenceImporter();
        DatabaseTransaction t = null;
        Connection conn = null;
        
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            
            if(importer.performImport(sequenceInput, nameInput, conn)) {
                DatabaseTransaction.commit(conn);
                Vector results = importer.getResults();
                Enumeration enum = results.elements();
                while(enum.hasMoreElements()) {
                    SequenceImporterLogger logger = (SequenceImporterLogger)enum.nextElement();
                    if(logger.getSuccessful()) {
                        System.out.println("Import sequence "+logger.getSequenceid()+": successful");
                    } else {
                        System.out.println("Import sequence "+logger.getSequenceid()+": fail");
                        System.out.println("\t"+logger.getMessage());
                    }
                }
                System.out.println("Import finished.");
            } else {
                DatabaseTransaction.rollback(conn);
                System.out.println("Import aborted.");
            }
        } catch (FlexDatabaseException ex) {
            DatabaseTransaction.rollback(conn);
            System.out.println(ex);
        } finally {
            DatabaseTransaction.closeConnection(conn);
            System.exit(0);
        }
    }
}