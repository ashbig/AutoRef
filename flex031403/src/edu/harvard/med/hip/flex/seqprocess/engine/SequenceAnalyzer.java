
package edu.harvard.med.hip.flex.seqprocess.engine;

import edu.harvard.med.hip.flex.seqprocess.core.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.util.*;
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
public class SequenceAnalyzer
{
    public final static String BLAST_BASE_DIR=FlexProperties.getInstance().getProperty("flex.repository.basedir");
    public final static String BLAST_DB_DIR=FlexProperties.getInstance().getProperty("flex.repository.blast.relativedir");
    private static final String HUMANDB=BLAST_BASE_DIR+BLAST_DB_DIR+"Human/genes";
    private static final String YEASTDB=BLAST_BASE_DIR+BLAST_DB_DIR+"Yeast/genes";
    
    //private static final String HUMANDB="E:/flexDev/BlastDB/genes";
    //private static final String BLASTDB="E:/flexDev/BlastDB/genes";
    private static final String INPUT = "/tmp/";
    private static final String OUTPUT = "/tmp/";
    
    private BaseSequence m_sequence = null;
    
    private Vector m_homolog = new Vector();
    private BlastResult m_blastResult = null;
    
    
    
    /**
     * Constructor.
     *
     * @param sequence The FlexSequence object.
     * @return A FlexSeqAnalyzer object.
     */
    public SequenceAnalyzer(BaseSequence sequence)
    {
        m_sequence = sequence;
    }
    
    
    
    
    
    
    /**
     * run blastn
     */
    public BlastResult run_blastn( String dbFileName , int hits) throws  FlexUtilException,ParseException
    {
        boolean isHomolog = false;
        
        String queryFile = makeQueryFile(m_sequence.getText(), m_sequence.getId());
        Blaster blaster = setBlaster(dbFileName, Blaster.BLAST_PROGRAM_BLASTN, queryFile, null,  hits);
         blaster.blast(queryFile+".in", queryFile+".out");
        BlastParser parser = new BlastParser(queryFile+".out", hits);
        parser.parseBlast();
        parser.displayParsed();
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
     
        
       
     
        double percentIdentity = numerator/(double)denomenator;
        //double percentAlignment = (end-start+1)/(double)cdslength;
        String evalue = y.getEvalue();
        
        return null;
    }
    
     
    /**
     * run blastn
     */
    public BlastResult run_tblastx( String dbFileName, int hits ) throws  FlexUtilException,ParseException
    {
        boolean isHomolog = false;
        
        String queryFile = makeQueryFile(m_sequence.getText(), m_sequence.getId());
        Blaster blaster = setBlaster(dbFileName, Blaster.BLAST_PROGRAM_TBLASTX, queryFile, null, hits);
         blaster.blast(queryFile+".in", queryFile+".out");
        BlastParser parser = new BlastParser(queryFile+".out", hits);
        
        parser.parseBlast();
       return null;
    }
    
    public BlastResult run_bl2seq_n( TheoreticalSequence tr) throws  FlexUtilException,ParseException
    {
        boolean isHomolog = false;
        
        String queryFile = makeQueryFile(m_sequence.getText(), m_sequence.getId());
        String subjFile = makeQueryFile(tr.getCodingSequence(), tr.getId()) + ".in";
        
        Blaster blaster = setBlaster(null, Blaster.BLAST_PROGRAM_BLASTN, queryFile, subjFile, 0);
        blaster.setExecutable(Blaster.BLAST_EXEC_BLAST2SEQ);
        blaster.blast(queryFile+".in", queryFile+".out");
        BlastParser parser = new BlastParser(queryFile+".out");
        parser.parseBlast();
        parser.displayParsed();
        
        ArrayList hits= new ArrayList();
        java.util.Date d = new java.util.Date();
        
        java.text.SimpleDateFormat f = new java.text.SimpleDateFormat("MM_dd_yyyy");
       
        BlastResult result = new BlastResult(  m_sequence.getId() , 
          Blaster.BLAST_EXEC_BLAST2SEQ + "|" + Blaster.BLAST_PROGRAM_BLASTN ,// program from blast family (bl2seq, blastn, tblastx, ..) that was run
          tr.getId() , // subject sequence – sequence id used for bl2seq
          "" ,//– information about database used for search if applicable, empty for bl2seq)
          d.toString() ,//– when blast was run for blastable db tracking purposes: 
          0 , //– the threshold for accepted hits???
          hits );
        
        ArrayList homologs = parser.getHomologList();
        if (homologs.size() > 0)
        {
        BlastParser.HomologItem homologItem = (BlastParser.HomologItem)homologs.get(0);
        
        int homologid = Integer.parseInt((homologItem.getSubID()).trim());
        
        BlastParser.Alignment y = (BlastParser.Alignment)homologItem.getAlignItem(0);
        String identity = y.getIdentity();
        StringTokenizer st = new StringTokenizer(identity);
        
        int numerator = Integer.parseInt((st.nextToken(" /")).trim());
        int denomenator = Integer.parseInt((st.nextToken(" /")).trim());
        int start = y.getQryStart();
        int end = y.getQryEnd();
     
        
       
     
        double percentIdentity = numerator/(double)denomenator;
        //double percentAlignment = (end-start+1)/(double)cdslength;
        String evalue = y.getEvalue();
        
        BlastHit hit = new  BlastHit( tr.getId(),
                        int  score ,                        int  identity ,
                        double  expect ,                        double  pvalue ,
                        int  quary_strand ,                        int  Subjstrand,
                        int  subjframe,                        int  queryframe,
                        int  query_start ,                        int  query_stop ,                        int  subject_start ,//-The offset in the subject of the beginning of the match
                        int  subject_stop ,//-The offset in the subject of the end of the match
                        int  subject_length )
               hits.add(hit);
        }
        return result;
    }
    
     public BlastResult run_bl2seq_x( TheoreticalSequence tr) throws  FlexUtilException,ParseException
    {
        boolean isHomolog = false;
        
        String queryFile = makeQueryFile(m_sequence.getText(), m_sequence.getId());
        String subjFile = makeQueryFile(tr.getCodingSequence(), tr.getId())+".in";
        
        Blaster blaster = setBlaster(null, Blaster.BLAST_PROGRAM_TBLASTX, queryFile, subjFile, 0);
        blaster.setExecutable(Blaster.BLAST_EXEC_BLAST2SEQ);
      
        blaster.blast(queryFile+".in", queryFile+".out");
        BlastParser parser = new BlastParser(queryFile+".out");
        parser.parseBlast();
        parser.displayParsed();
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
     
        
       
     
        double percentIdentity = numerator/(double)denomenator;
        //double percentAlignment = (end-start+1)/(double)cdslength;
        String evalue = y.getEvalue();
        
        return null;
    }
    
    
    
    /**
     * Return the homologs as a vector including this sequence.
     *
     * @return A Vector object containing all the homologs including this one.
     */
    public Vector getHomolog()
    {
        return m_homolog;
    }
    
    /**
     * Return the blastResults for this sequence.
     *
     * @return The BlastResults object for this sequence.
     */
    public BlastResult getBlastResult()
    {
        return m_blastResult;
    }
    
    
    //**********************************************************************//
    //                          Private methods                             //
    //**********************************************************************//
    private Blaster setBlaster(String dbFileName, String program, 
                                String queryFile, String subjFile, int hits)
    {
        Blaster blaster = new Blaster();
        blaster.setProgram(program);
        blaster.setHits(hits);
        blaster.setExpect(10e-50);
        blaster.setSubjectFileName(subjFile);
        blaster.setDBPath( dbFileName);
       
        return blaster;
    }
    //Print the sequence cds to a file in a fasta format.
    private  String makeQueryFile(String text, int id) throws FlexUtilException
    {
        java.util.Date d = new java.util.Date();
        java.text.SimpleDateFormat f = new java.text.SimpleDateFormat("MM_dd_yyyy");
        String fileName = INPUT+System.currentTimeMillis();
        try
        {
            PrintWriter pr = new PrintWriter(new BufferedWriter(new FileWriter(fileName+".in")));
            pr.print( ">"+id);
            pr.println(edu.harvard.med.hip.flex.core.CDNASequence.convertToFasta(text));
            pr.close();
            
            return fileName;
        }catch (IOException e)
        {
            throw new FlexUtilException("Cannot make query file for "+fileName+"\n"+e.getMessage());
        }
    }
    
    //**********************************************************************//
    //                          Test                                        //
    //**********************************************************************//
    
    public static void main(String [] args)
    {
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            BaseSequence sequence = new TheoreticalSequence(24450);
            
            SequenceAnalyzer analyzer = new SequenceAnalyzer(sequence);
            analyzer.run_bl2seq_x( (TheoreticalSequence) sequence);
           // analyzer.run_blastn("c:\\MGC\\genes", 1);
            
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
        } catch (Exception e)
        {
            System.out.println(e);
        } 
        System.exit(0);
    }
}