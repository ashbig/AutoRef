
package edu.harvard.med.hip.flex.seqprocess.engine;

import edu.harvard.med.hip.flex.seqprocess.core.sequence.*;
import edu.harvard.med.hip.flex.seqprocess.core.feature.*;
import edu.harvard.med.hip.flex.seqprocess.core.blast.*;
import edu.harvard.med.hip.flex.seqprocess.util.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.util.*;
import edu.harvard.med.hip.flex.blast.*;
import java.util.*;
import java.math.BigDecimal;
import java.sql.*;

import java.io.*;


/**
 * This class run blast, parse output, parse mutations
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
    private static final String OUTPUT = "/blastoutput/";
    
    private BaseSequence m_sequence = null;
    
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
    public int run_blastn( String dbFileName , int hits, File fname) throws  FlexUtilException,ParseException
    {
      
        String queryFile = makeQueryFile(m_sequence.getText(), "bn", m_sequence.getId());
        Blaster blaster = setBlaster(dbFileName, Blaster.BLAST_PROGRAM_BLASTN, queryFile, null,  hits);
        blaster.blast(queryFile+".in", queryFile+".out");
     
        BlastParser parser = new BlastParser(queryFile+".out", hits);
        parser.parseBlast();
        
        ArrayList homologs = parser.getHomologList();
        fname = new File(queryFile+".out");
        return homologs.size();
     }
    
   
    /**
     * run blastn
     */
    public BlastResult run_tblastx( String dbFileName, int hits ) throws  FlexUtilException,ParseException
    {
        boolean isHomolog = false;
        
        String queryFile = makeQueryFile(m_sequence.getText(), "bx",m_sequence.getId());
        Blaster blaster = setBlaster(dbFileName, Blaster.BLAST_PROGRAM_TBLASTX, queryFile, null, hits);
        blaster.blast(queryFile+".in", queryFile+".out");
        BlastParser parser = new BlastParser(queryFile+".out", hits);
        parser.parseBlast();
        return null;
    }
    
 
            
     
    
    public BlastResult run_bl2seq( TheoreticalSequence tr, String blastType,int hits) throws  FlexUtilException,ParseException
    {
        String query = m_sequence.getText();
        String subj = tr.getCodingSequence();
        Blaster blaster = null;
        String queryFile  = null;
        String subjFile =null;
        String prefix = null;
        
        if (blastType.equals(Blaster.BLAST_PROGRAM_TBLASTX))
        {
            prefix = "b2x";
            queryFile = makeQueryFile(query, prefix,m_sequence.getId());
            subjFile = makeQueryFile(subj, prefix,tr.getId())+".in";
            blaster = setBlaster(null, Blaster.BLAST_PROGRAM_TBLASTX, queryFile, subjFile, hits);
            
        }
        else if (blastType.equals(Blaster.BLAST_PROGRAM_BLASTX))
        {  
            prefix = "b2tp";
            queryFile = makeQueryFile(m_sequence.getText(), prefix,m_sequence.getId());
            subjFile = makeQueryFile(SequenceManipulation.getTranslation(tr.getCodingSequence(),SequenceManipulation.ONE_LETTER_TRANSLATION_NO_SPACE),prefix, tr.getId())+".in";
            blaster = setBlaster(null, Blaster.BLAST_PROGRAM_BLASTX, queryFile, subjFile, hits);
            blaster.setGapExtend(1);
            blaster.setGapOpen(11);
            
        }
        else if (blastType.equals(Blaster.BLAST_PROGRAM_BLASTP))
        {
            prefix = "b2p";
            queryFile = makeQueryFile(SequenceManipulation.getTranslation(m_sequence.getText(),SequenceManipulation.ONE_LETTER_TRANSLATION_NO_SPACE), prefix,m_sequence.getId());
            subjFile = makeQueryFile(SequenceManipulation.getTranslation(tr.getCodingSequence(),SequenceManipulation.ONE_LETTER_TRANSLATION_NO_SPACE),prefix, tr.getId())+".in";
            
            blaster = setBlaster(null, Blaster.BLAST_PROGRAM_BLASTP, queryFile, subjFile, hits);
           
        }
        else if (blastType.equals(Blaster.BLAST_PROGRAM_BLASTN))
        {
            prefix = "b2n";
             queryFile = makeQueryFile(m_sequence.getText(),prefix,m_sequence.getId());
             subjFile = makeQueryFile(tr.getCodingSequence(), prefix,tr.getId()) +".in";
             blaster = setBlaster(null, Blaster.BLAST_PROGRAM_BLASTN, queryFile, subjFile, hits);
              blaster.setGapExtend(0);
              blaster.setGapOpen(1);
        }
        String outputfile = OUTPUT+prefix+m_sequence.getId()+".out";
        blaster.setExecutable(Blaster.BLAST_EXEC_BLAST2SEQ);
        blaster.blast_bl2seq(queryFile+".in", outputfile);
        String cmdLine = "bl2seq -p" + blastType + " " + blaster.getExpect() + " " + blaster.getGapped() +          
           " -j " + subjFile + " -i " + queryFile+".in" +  " -o " + outputfile +
        " " + blaster.getFilter() + " " + blaster.getGapOpen() +" "+blaster.getGapExtend(); 
        System.out.println(cmdLine);
        return parseResult(outputfile, blastType, tr.getId(),cmdLine, hits);
    }
    
    public BlastResult parseResult(String fname, String bltype, int tr_id,String cmdLine, int num_hits)
        throws FlexUtilException
    {
     
        ArrayList hits = new ArrayList();
        BlastResult result = null;
        java.util.Date d = new java.util.Date();
        java.text.SimpleDateFormat f = new java.text.SimpleDateFormat("MM_dd_yyyy");
        String date = f.format(d);
        int sstrand = -1; int qstrand = -1; int qframe = -1; int sframe = -1;int gaps = -1;
        String strand = null;
        try
        {
             ArrayList parsed_hits = Blast2seqParser.parse(fname,num_hits);
             for (int hit_count = 0; hit_count < parsed_hits.size(); hit_count++)
             {
                 Hashtable hit_data = (Hashtable) parsed_hits.get(hit_count);
                 if ( hit_data.containsKey("qframe") )
                     qframe = Integer.parseInt( (String)hit_data.get("qframe")); 
                 else
                     qframe = 0;
                 
                 if ( hit_data.containsKey("gaps") )
                     gaps = Integer.parseInt( (String)hit_data.get("gaps")); 
                 else
                     gaps = 0;
                 
                 if ( hit_data.containsKey("sframe") )
                     sframe = Integer.parseInt( (String)hit_data.get("sframe")); 
                 else
                     sframe = 0;
                 
                 if ( hit_data.containsKey("sstrand") )
                 {
                     strand = (String)hit_data.get("sstrand");
                     if ( strand.equalsIgnoreCase("plus") || strand.equalsIgnoreCase("+"))
                         sstrand = 1; 
                     else
                         sstrand = -1;
                 }
                 else
                     sstrand = 0;
                 
                 if ( hit_data.containsKey("qstrand") )
                 {
                     strand = (String)hit_data.get("qstrand");
                     if ( strand.equalsIgnoreCase("plus") || strand.equalsIgnoreCase("+"))
                         qstrand = 1; 
                     else
                         qstrand = -1;
                 }
                 else
                     qstrand = 0;
                 
                  int score = Integer.parseInt( (String) hit_data.get("score"));
                  double identity =    Double.parseDouble( (String) hit_data.get("identity")) ;
                  String expect =   (String) hit_data.get("expect") ;
                 int qstart =    Integer.parseInt( (String)   hit_data.get("qstart") );
                  int  qstop=     Integer.parseInt( (String)  hit_data.get("qstop"));
                  int   sstart =  Integer.parseInt( (String)   hit_data.get("sstart")) ;
                  int sstop=    Integer.parseInt( (String)   hit_data.get("sstop") );
                  int length =    Integer.parseInt( (String)   hit_data.get("length") );
                  String query = (String)hit_data.get("query");
                  String sbj =      (String) hit_data.get("subject");
                  BlastHit hit = new BlastHit(
                     tr_id,
                     score,identity,expect,  
                      -1, qstrand ,  sstrand,
                      sframe, qframe,qstart,qstop,sstart,sstop,length,
                      0, -1,query,sbj  ,gaps  );
                 hits.add(hit);
             }
             result = new BlastResult( 
                m_sequence.getId() , 
                Blaster.BLAST_EXEC_BLAST2SEQ,// program from blast family (bl2seq, blastn, tblastx, ..) that was run
                tr_id , // subject sequence – sequence id used for bl2seq
                "" ,//– information about database used for search if applicable, empty for bl2seq)
                date ,//– when blast was run for blastable db tracking purposes: 
                0 , //– the threshold for accepted hits
                hits ,
                bltype,
                fname, 
                cmdLine
                );
        }
        catch(Exception e)
        {
            throw new  FlexUtilException("Can not parse blast file");
        }
        
        
        
        return result;
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
        blaster.setFilter("F");
       
        blaster.setGapped(true);
        
        return blaster;
    }
    //Print the sequence cds to a file in a fasta format.
    private  String makeQueryFile(String text, String prefics, int id) throws FlexUtilException
    {
        java.util.Date d = new java.util.Date();
        java.text.SimpleDateFormat f = new java.text.SimpleDateFormat("MM_dd_yyyy");
        String fileName = INPUT+prefics+id;//System.currentTimeMillis();
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
    
    
    //________________________________________________________________________
  
    //**********************************************************************//
    //                          Test                                        //
    //**********************************************************************//
    
    public static void main(String [] args)
    {
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            TheoreticalSequence sequence = new TheoreticalSequence(20282);
       
            String query="ATGAGCGGCGGCGGGCCTTGGCCTAGAGCGCTCCCAAGAAGTGGCTTACACGGACATCAAAGTGATTGGCAATGGCTCATTTGGGGTCGTGTACCAGGCACGGCTGGCAGAGACCAGGGAACTAGTCGCCATCAAGAAGGTTCTCCAGGACAAGAGGTTCAAGAACCGAGAGCTGCAGATCATGCGTAAGCTGGACCACTGCAATATTGTGAGGCTGAGATACTTTTTCTACTCCAGTGGCGAGAAGAAAGACGAGCTTTACCTAAATCTGGTGCTGGAATATGTGCCCGAGACAGTGTACCGGGTGGCCCGCCACTTCACCAAGGCCAAGTTGACCATCCCTATCCTCTATGTCAAGGTGTACATGTACCAGCTCTTCCGCAGCTTGGCCTACATCCACTCCCAGGGCGTGTGTCACCGCGACATCAAGCCCCAGAACCTGCTGGTGGACCCTGACACTGCTGTCCTCAAGCTCTGCGATTTTGGCAGTGCAAAGCAGTTGGTCCGAGGGGAGCCCAATGTCTCCTACATCTGTTCTCGCTACTACCGGGCCCCAGAGCTCATCTTTGGAGCCACTGATTACACCTCATCCATCGATGTTTGGTCAGCTGGCTGTGTACTGGCAGAGCTCCTCTTGGGCCAGCCCATCTTCCCTGGGGACAGTGGGGTGGACCAGCTGGTGGAGATCATCAAGGTGCTGGGAACACCAACCCGGGAACAAATCCGAGAGATGAACCCCAACTACACGGAGTTCAAGTTCCCTCAGATTAAAGCTCACCCCTGGACAAAGGTGTTCAAATCTCGAACGCCGCCAGAGGCCATCGCGCTCTGCTCTAGCCTGCTGGAGTACACCCCATCCTCAAGGCTCTCCCCACTAGAGGCCTGTGCGCACAGCTTCTTTGATGAACTGCGATGTCTGGGAACCCAGCTGCCTAACAACCGCCCACTTCCCCCTCTCTTCAACTTCAGTGCTGGTGAACTCTCCATCCAACCGTCTCTCAACGCCATTCTCATCCCTCCTCACTTGAGGTCCCCAGCGGGCACTACCACCCTCACCCCGTCCTCACAAGCTTTAACTGAGACTCCGACCAGCTCAGACTGGCAGTCGACCGATGCCACACCTACCCTCACTAACTCCTCCTT";
            
                 String subject="atggctgaccaactgactgaagagcagattgcagaattcaaagaagctttttcactattt"+
"gacaaagatggtgatggaactataacaacaaaggaattgggaactgtaatgagatctctt"+
"gggcagaatcccacagaagcagagttacaggacatgattaatgaagtagatgctgatggt"+
"aatggcacaattgacttccctgaatttctgacaatgatggcaagaaaaatgaaagacaca"+
"gacagtgaagaagaaattagagaagcattccgtgtgtttgataaggatggcaatggctat"+
"attagtgctgcagaacttcgccatgtgatgacaaaccttggagagaagttaacagatgaa"+
"gaagttgatgaaatgatcagggaagcagatattgatggtgatggtcaagtaaactatgaa"+
"gagtttgtacaaatgatgacagcaaagtga";

            FullSequence fl = new FullSequence(query,-1);
        //    sequence.setText(subject);
          //  sequence.setText(sequence.getText().substring(0,200) + sequence.getText().substring(209) );
            SequenceAnalyzer analyzer = new SequenceAnalyzer(fl);
             BlastResult res_n = analyzer.run_bl2seq(  sequence, Blaster.BLAST_PROGRAM_BLASTN,5);
            BlastResult res_p = analyzer.run_bl2seq( sequence, Blaster.BLAST_PROGRAM_BLASTX,5);
            BlastResult res_p1 = analyzer.run_bl2seq( sequence, Blaster.BLAST_PROGRAM_BLASTP,5);
            // BlastResult res_n = analyzer.parseResult("/tmp/b2n-1.out", Blaster.BLAST_PROGRAM_BLASTN, sequence.getId(),"", 3);
           // BlastResult res_p = analyzer.parseResult("/tmp/b2tp-1.out", Blaster.BLAST_PROGRAM_BLASTX,  sequence.getId(),"", 3);
            Mutation.run_mutation_analysis(res_n, res_p,(FullSequence)fl,sequence);
            
           // analyzer.run_bl2seq(  sequence, Blaster.BLAST_PROGRAM_TBLASTX,3);
           // analyzer.run_bl2seq(  sequence, Blaster.BLAST_PROGRAM_BLASTP,3);
           
        } catch (Exception e)
        {
            System.out.println(e);
        }
        System.exit(0);
    }
}