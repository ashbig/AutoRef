/*
 * $Id: FlexSeqAnalyzer.java,v 1.37 2004-01-29 18:16:46 dzuo Exp $
 *
 * File     : FlexSeqAnalyzer.java
 * Date     : 05102001
 * Author	: Dongmei Zuo
 */

package edu.harvard.med.hip.flex.util;

import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.blast.*;
import java.util.*;
import java.math.BigDecimal;
import java.sql.*;
import java.io.*;

/**
 * This class compares a sequence with existing flex sequences
 * in the database to find whether the given sequence is same or
 * homologous to any flex sequence.
 */
public class FlexSeqAnalyzer {
    public final static String BLAST_BASE_DIR=FlexProperties.getInstance().getProperty("flex.repository.basedir");
    public final static String BLAST_DB_DIR=FlexProperties.getInstance().getProperty("flex.repository.blast.relativedir");
    public static final String HUMANDB=BLAST_BASE_DIR+BLAST_DB_DIR+"Human/genes";
    public static final String YEASTDB=BLAST_BASE_DIR+BLAST_DB_DIR+"Yeast/genes";
    public static final String MGCDB=BLAST_BASE_DIR+BLAST_DB_DIR+"Mgc/genes";
    private static final String INPUT = FlexProperties.getInstance().getProperty("tmp");
    private static final String OUTPUT = FlexProperties.getInstance().getProperty("tmp");
    
    private FlexSequence sequence;
    private Vector sameSequence = new Vector();
    private Vector homolog = new Vector();
    private BlastResults blastResults = new BlastResults();
    
    private double requiredPercentIdentity = 0.95;
    private int requiredCdslengthLimit = 70;
    
    /**
     * Constructor.
     *
     * @param sequence The FlexSequence object.
     * @return A FlexSeqAnalyzer object.
     */
    public FlexSeqAnalyzer(FlexSequence sequence) {
        this.sequence = sequence;
    }
    
    /**
     * Return true if there exists exact sequence match in the database.
     * Return false otherwise.
     *
     * @return True if there exists exact sequence match in the database;
     *         false otherwise.
     * @exception FlexDatabaseException.
     */
    public boolean findSame() throws FlexDatabaseException {
        Vector v = new Vector();
        if(!sameGCContent(v)) {
            return false;
        }
        
        if(!sameSequenceText(v)) {
            return false;
        }
        
        return true;
    }
    
    /**
     * @param pid Percent Identity for blast.
     * @param cdslengthLimit The minimum CDS length required.
     * Return true if there is homolog in the flex database. Return false
     * otherwise.
     *
     * @return True if there is homolog in the flex database; false otherwise.
     * @exception FlexUtilException, FlexDatabaseException, ParseException.
     */
    public boolean findHomolog(double pid, int cdslengthLimit) throws FlexUtilException, FlexDatabaseException, ParseException {
        boolean isHomolog = false;
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        
        String queryFile = makeQueryFile();
        Blaster blaster = new Blaster();
        blaster.setHits(1);
        
        if("Homo sapiens".equals(sequence.getSpecies())) {
            blaster.setDBPath(HUMANDB);
        } else if ("Saccharomyces cerevisiae".equals(sequence.getSpecies())) {
            blaster.setDBPath(YEASTDB);
        } else {
            blaster.setDBPath(HUMANDB);
        }
        
        
        blaster.blast(queryFile+".in", queryFile+".out");
        BlastParser parser = new BlastParser(queryFile+".out");
        parser.parseBlast();
        ArrayList homologs = parser.getHomologList();
        
        BlastParser.HomologItem homologItem = (BlastParser.HomologItem)homologs.get(0);
        int homologid = Integer.parseInt((homologItem.getSubID()).trim());
        
        BlastParser.Alignment y = (BlastParser.Alignment)homologItem.getAlignItem(0);
        String identity = y.getIdentity();
        StringTokenizer st = new StringTokenizer(identity);
        int numerator = Integer.parseInt((st.nextToken(" /")).trim());
        int denomenator = Integer.parseInt((st.nextToken(" /")).trim());
        double percentIdentity = numerator/(double)denomenator;
        int start = y.getQryStart();
        int end = y.getQryEnd();
        int cdslength = sequence.getCdslength();
        double percentAlignment = (end-start+1)/(double)cdslength;
        String evalue = y.getEvalue();
        
        if(percentIdentity>=requiredPercentIdentity && numerator >= requiredCdslengthLimit) {
            FlexSequence s = new FlexSequence(homologid);
            s.restore(homologid);
            homolog.addElement(s);
            isHomolog = true;
        }
        
        if(isHomolog) {
            homolog.addElement(sequence);
            blastResults.setEvalue(evalue);
            blastResults.setIdentity(identity);
            blastResults.setCdslength(cdslength);
            blastResults.setPercentIdentity(percentIdentity);
            blastResults.setPercentAlignment(percentAlignment);
        }
        
        return isHomolog;
    }
    
    /**
     * Return true if there is homolog in the flex database. Return false
     * otherwise.
     *
     * @return True if there is homolog in the flex database; false otherwise.
     * @exception FlexUtilException, FlexDatabaseException, ParseException.
     */
    public boolean findHomolog() throws FlexUtilException, FlexDatabaseException, ParseException {
        return findHomolog(requiredPercentIdentity, requiredCdslengthLimit);
    }    
    
    
    //********************************************************************
    
    /**
     * Return the matched sequence id  from the flex database.
     *
     *@param dbFileName - file name for the blastable db
     * @return sequence id for the matched sequence from the flex database;
     * @exception FlexUtilException, FlexDatabaseException, ParseException.
     */
    public int findExactCdsMatch(String dbFileName) throws FlexUtilException, FlexDatabaseException, ParseException {
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        
        String queryFile = makeQueryFile();//set sequence for this
        Blaster blaster = new Blaster();
        blaster.setHits(1);
        

        //blaster.setDBPath(dbFileName);
        blaster.setDBPath(BLAST_BASE_DIR+BLAST_DB_DIR + dbFileName);        

        blaster.blast(queryFile+".in", queryFile+".out");
        BlastParser parser = new BlastParser(queryFile+".out");
        parser.parseBlast();
        ArrayList homologs = parser.getHomologList();
        
        BlastParser.HomologItem homologItem = (BlastParser.HomologItem)homologs.get(0);
        int homologid = Integer.parseInt((homologItem.getSubID()).trim());
        
        BlastParser.Alignment y = (BlastParser.Alignment)homologItem.getAlignItem(0);
        String identity = y.getIdentity();
        StringTokenizer st = new StringTokenizer(identity);
        int numerator = Integer.parseInt((st.nextToken(" /")).trim());
        int denomenator = Integer.parseInt((st.nextToken(" /")).trim());
        int start = y.getQryStart();
        int end = y.getQryEnd();
        int cdslength = sequence.getCdslength();
        
        if(numerator ==  denomenator && (end - start +1) == cdslength ) {
            return homologid;
        }
        
        return -1;
    }
    
    /**
     * @param pid Percent Identity for blast.
     * @param cdslengthLimit The minimum CDS length required.
     * Return true if there is homolog in the flex database. Return false
     * otherwise.
     *
     * @return True if there is homolog in the flex database; false otherwise.
     * @exception FlexUtilException, FlexDatabaseException, ParseException.
     */
    public int findExactMatchOrHomolog(double pid, int cdslengthLimit, String dbFileName) throws FlexUtilException, FlexDatabaseException, ParseException {
        boolean isHomolog = false;
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        
        String queryFile = makeQueryFile();
        Blaster blaster = new Blaster();
        blaster.setHits(1);
     //   blaster.setDBPath(BLAST_BASE_DIR+BLAST_DB_DIR + dbFileName);
        blaster.setDBPath( dbFileName);
                
        blaster.blast(queryFile+".in", queryFile+".out");
        BlastParser parser = new BlastParser(queryFile+".out");
        parser.parseBlast();
       
        ArrayList homologs = parser.getHomologList();
        
        BlastParser.HomologItem homologItem = (BlastParser.HomologItem)homologs.get(0);
        int homologid = Integer.parseInt((homologItem.getSubID()).trim());
        
        BlastParser.Alignment y = (BlastParser.Alignment)homologItem.getAlignItem(0);
        String identity = y.getIdentity();
        StringTokenizer st = new StringTokenizer(identity);
        
        int numerator = Integer.parseInt((st.nextToken(" /")).trim());
        int denomenator = Integer.parseInt((st.nextToken(" /")).trim());
        int start = y.getQryStart();
        int end = y.getQryEnd();
        int cdslength = sequence.getCdslength();
        
        //exact match
        if(numerator ==  denomenator && (end - start +1) == cdslength ) {
            return homologid;
        }
        if (  Math.abs(pid - 1.00) < 0.0000001 ) return -1;
        // not exact match
        double percentIdentity = numerator/(double)denomenator;
        double percentAlignment = (end-start+1)/(double)cdslength;
        String evalue = y.getEvalue();
        
        if (percentIdentity>=pid &&
                numerator >= cdslengthLimit) 
        {
            return homologid;
        }
               
        return -1;
    }
    
    
    
    //************************************************************
    /**
     * Return the same sequences as a Vector including this sequence.
     *
     * @return A Vector object containing all the same sequences including this one.
     */
    public Vector getSameSequence() {
        return sameSequence;
    }
    
    /**
     * Return the homologs as a vector including this sequence.
     *
     * @return A Vector object containing all the homologs including this one.
     */
    public Vector getHomolog() {
        return homolog;
    }
    
    /**
     * Return the blastResults for this sequence.
     *
     * @return The BlastResults object for this sequence.
     */
    public BlastResults getBlastResults() {
        return blastResults;
    }
    
    
    //**********************************************************************//
    //                          Private methods                             //
    //**********************************************************************//
    
    //Query the database to find the sequences that have the same GC content.
    private boolean sameGCContent(Vector v) throws FlexDatabaseException {
        String sql = "select sequenceid from flexsequence\n"+
        "where gccontent="+sequence.getGccontent();
        //+"\n"+
        //"and sequenceid <> "+sequence.getId();
        
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        ResultSet rs = t.executeQuery(sql);
        try {
            while(rs.next()) {
                int id = rs.getInt("SEQUENCEID");
                v.addElement(new Integer(id));
            }
        } catch(SQLException sqlE) {
            throw new FlexDatabaseException(sqlE+"\nSQL: "+sql);
        }
        if(v.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
    
    //Query the database to find exact sequence match.
    private boolean sameSequenceText(Vector v) throws FlexDatabaseException {
        boolean returnValue = false;
        
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        Connection c = t.requestConnection();
        String sql = "select s.sequenceid as id "+
        "from flexsequence s, sequencetext t "+
        "where s.sequenceid = t.sequenceid "+
        "and s.sequenceid = ? "+
        "and t.sequenceorder = ? "+
        "and t.sequencetext = ?";
        ResultSet rs = null;
        PreparedStatement stmt = null;
        try {
            stmt = c.prepareStatement(sql);
            
            String sequencetext = sequence.getSequencetext();
            
            Enumeration enum = v.elements();
            while(enum.hasMoreElements()) {
                int id = ((Integer)enum.nextElement()).intValue();
                stmt.setInt(1, id);
                
                boolean isSame = true;
                int sequenceid =-1;
                int order = 1;
                for(int i=0; i<sequencetext.length(); i=i+4000) {
                    String subseq = getSubSeq(sequencetext, i);
                    stmt.setInt(2, order);
                    stmt.setString(3, subseq);
                    
                    rs = stmt.executeQuery();
                    if (!rs.next()) {
                        isSame=false;
                        
                        break;
                    } else {
                        sequenceid = rs.getBigDecimal("ID").intValue();
                        
                    }
                    order++;
                }
                
                if(isSame) {
                    FlexSequence s = new FlexSequence(sequenceid);
                    s.restore(sequenceid);
                    sameSequence.addElement(s);
                    
                    returnValue = true;
                }
            }
            
            
            if(returnValue) {
                sameSequence.addElement(sequence);
            }
            
            return returnValue;
        } catch (SQLException e) {
            throw new FlexDatabaseException(e.getMessage()+"\nSQL: "+sql);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeConnection(c);
        }
    }
    
    //Return the substring of a string with 4000 char long.
    private String getSubSeq(String sequencetext, int i) {
        if(i+4000 < sequencetext.length()) {
            return sequencetext.substring(i, i+4000);
        } else {
            return sequencetext.substring(i);
        }
    }
    
    //Print the sequence cds to a file in a fasta format.
    private  String makeQueryFile() throws FlexUtilException {
        java.util.Date d = new java.util.Date();
        java.text.SimpleDateFormat f = new java.text.SimpleDateFormat("MM_dd_yyyy");
        String fileName = INPUT+sequence.getGi()+"_" + f.format(d);
        try {
            PrintWriter pr = new PrintWriter(new BufferedWriter(new FileWriter(fileName+".in")));
            pr.print(sequence.getFastaHeader());
            pr.println(sequence.getCDSFasta());
            pr.close();
            
            return fileName;
        }catch (IOException e) {
            throw new FlexUtilException("Cannot make query file for "+fileName+"\n"+e.getMessage());
        }
    }
    
    //**********************************************************************//
    //                          Test                                        //
    //**********************************************************************//
    
    public static void main(String [] args) {
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            FlexSequence sequence = new FlexSequence(24450);
            sequence.restore(24450);
            sequence.setId(-1);
            FlexSeqAnalyzer analyzer = new FlexSeqAnalyzer(sequence);
            analyzer.findExactMatchOrHomolog(90 ,  70 ,"c:\\MGC\\genes");
            
            /*
            if(analyzer.findSame()) {
                Vector sequences = analyzer.getSameSequence();
                Enumeration enum = sequences.elements();
                while(enum.hasMoreElements()) {
                    FlexSequence s = (FlexSequence)enum.nextElement();
                    //						s.restore(s.getId(), t);
                    System.out.println("\t"+s.getId());
                    System.out.println("\t"+s.getSequencetext());
                    System.out.println("\t"+s.getFlexstatus());
                }
            } else {
                System.out.println("Testing findSame() - ERROR");
            }
            
            if(analyzer.findHomolog()) {
                Vector homologs = analyzer.getHomolog();
                Enumeration enum = homologs.elements();
                while(enum.hasMoreElements()) {
                    Hashtable h = (Hashtable)enum.nextElement();
                    Enumeration ks = h.keys();
                    while(ks.hasMoreElements()) {
                        String k = (String)ks.nextElement();
                        FlexSequence s = (FlexSequence)h.get(k);
                        s.restore(s.getId());
                        System.out.println("\t"+s.getId());
                        System.out.println("\t"+s.getSequencetext());
                        System.out.println("\t"+s.getFlexstatus());
                    }
                }
             
            }**/
        } catch (FlexDatabaseException e) {
            System.out.println(e);
        } catch (FlexUtilException e) {
            System.out.println(e);
        }catch (ParseException e) {
            System.out.println(e);
        }
    }
}