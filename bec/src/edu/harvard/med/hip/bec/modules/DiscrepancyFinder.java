/*
 * DescrepancyFinder.java
 *
 * Created on March 24, 2003, 3:30 PM
 */

package edu.harvard.med.hip.bec.modules;

/**
 *
 * @author  htaycher
 */
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.programs.needle.*;
import edu.harvard.med.hip.bec.programs.blast.*;
import edu.harvard.med.hip.bec.export.*;
import edu.harvard.med.hip.bec.coreobjects.feature.*;
import edu.harvard.med.hip.bec.engine.*;
import edu.harvard.med.hip.bec.database.*;
import  edu.harvard.med.hip.bec.util.*;
import  edu.harvard.med.hip.bec.bioutil.*;
import edu.harvard.med.hip.bec.Constants;
import java.util.*;
import java.sql.Date;
import java.io.*;
import java.math.BigDecimal;

public class DiscrepancyFinder
{
    public static final int USE_BLAST = 0;
     public static final int USE_NEEDLE = 1;
    
    //store input & output blast files
    private static final String INPUT = "/tmp/";
    private static final String OUTPUT = "/blastoutput/";
    
    
    private SequencePair m_seqpair = null;
    private ArrayList    m_seqpairs = null;
    private double      m_identitycutoff = 60.0;
    private boolean      m_isRunCompliment = false;
    
    private static final int IDENTITY_100 = 0;
    private static final int IDENTITY_60 = 1;
     private static final int IDENTITY_100_60 = 2;
     
     
     private boolean m_debug = false;
     private boolean  m_endreads_analysis = false;
   
    
    /** Creates a new instance of DescrepancyFinder */
     public DiscrepancyFinder(SequencePair pair)
    {
        m_seqpair = pair;
    }
    
     public DiscrepancyFinder(ArrayList pairs)
    {
        m_seqpairs = pairs;
    }
    
     public DiscrepancyFinder()    {    }
    
    public void addSequencePair(SequencePair pair)    {        m_seqpairs.add(  pair);    }
    public void setSequencePair(SequencePair pair)    {        m_seqpair =  pair;    }
    // parameter requeres run query on compliment of query sequence
    // needle is not capable on converting sequence for finding better aligment
    public void setIsRunCompliment(boolean b){m_isRunCompliment = b;}
    public void setDebug(boolean b){ m_debug =b;}
   
    public void setInputType(boolean v){m_endreads_analysis = v;}
    public void setIdentityCutoff(double d)
    { m_identitycutoff = d;}
    
    //main calling function for polymorphism finder
    public void run()throws BecDatabaseException, BecUtilException
    {
        if (m_seqpairs != null)
        {
            for (int ind = 0; ind < m_seqpairs.size(); ind++)
            {
                SequencePair pr = (SequencePair) m_seqpairs.get(ind);
                analizeUsingNeedle(pr);
                
            }
        }
        else
        {
            analizeUsingNeedle(m_seqpair);
        }
        
    }
    
    
    
    //main function run full analysis for the experimental sequence
    private void analizeUsingNeedle(SequencePair pair)throws  BecUtilException
    {
        NeedleResult res_needle = null;
        res_needle =  runNeedle( pair);
        ArrayList mutations = new ArrayList();
        //run blast n
        if (res_needle.getIdentity() != 100.0)
        {
            if (!m_endreads_analysis && res_needle.getIdentity() <= m_identitycutoff)
            {
                pair.getQuerySequence().setStatus(BaseSequence.STATUS_NOMATCH);
               
            }
            else// not 100 on nucleotide level
            {
                mutations = run_analysis(res_needle, pair.getQuerySequence().getId(), pair.getRefSequence().getId() );
                pair.getQuerySequence().setDiscrepancies(mutations);
                pair.getQuerySequence().setStatus(BaseSequence.STATUS_ANALIZED_YES_DISCREPANCIES);
            }
        }
        else//identity 100%
        {
            pair.getQuerySequence().setStatus(BaseSequence.STATUS_ANALIZED_NO_DISCREPANCIES);
        }
        
        
    }
    
    /*
     //main function run full analysis for the experimental sequence
    private void analizeUsingBlast2Seq(SequencePair pair)throws  BecUtilException
    {
        BlastResult res_blast2seq = null;
       
        res_blast2seq =  runBlast( pair);
        if (res_blast2seq.getAligments() == null || res_blast2seq.getAligments().size() == 0)
        {
            return ;
        }
        BlastAligment bl_aligment = (BlastAligment)res_blast2seq.getAligments().get(0) ;
        ArrayList mutations = new ArrayList();
        //run blast n
        if (bl_aligment.getIdentity() != 100.0)
        {
            if (!m_endreads_analysis && bl_aligment.getIdentity() <= m_identitycutoff)
            {
                pair.getQuerySequence().setStatus(BaseSequence.STATUS_NOMATCH);
               
            }
            else// not 100 on nucleotide level
            {
               
                mutations = run_analysis(bl_aligment, pair.getQuerySequence().getId(), pair.getRefSequence().getId() );
                pair.getQuerySequence().setDiscrepancies(mutations);
                pair.getQuerySequence().setStatus(BaseSequence.STATUS_ANALIZED_YES_DISCREPANCIES);
            }
        }
        else//identity 100%
        {
            pair.getQuerySequence().setStatus(BaseSequence.STATUS_ANALIZED_NO_DISCREPANCIES);
        }
        
        
    }
     **/
    
    //function runs needle and parse output
    private NeedleResult runNeedle(SequencePair pair) throws BecUtilException
    {
        //run needle
        NeedleWrapper nw = new NeedleWrapper();
        nw.setQueryId(pair.getQuerySequence().getId());
        nw.setReferenceId(pair.getRefSequence().getId());
        if ( !m_isRunCompliment )
            nw.setQuerySeq(pair.getQuerySequence().getText());
        else
            nw.setQuerySeq( SequenceManipulation.getCompliment(pair.getQuerySequence().getText()));
        
        nw.setRefSeq(pair.getRefSequence().getText());
        nw.setGapOpen(10.0);
        nw.setGapExtend(0.5);
        nw.setOutputFileDir(OUTPUT);
        
        NeedleResult res_needle =  nw.runNeedle();
        return res_needle;
       
    }
    
     //function runs needle and parse output
    /*
    private BlastResult runBlast(SequencePair pair) throws BecUtilException
    {
        //run needle
        NeedleWrapper nw = new NeedleWrapper();
        nw.setQueryId(pair.getQuerySequence().getId());
        nw.setReferenceId(pair.getRefSequence().getId());
        nw.setRefSeq(pair.getRefSequence().getText());
        nw.setQuerySeq(pair.getQuerySequence().getText());
        
        nw.setGapOpen(10.0);
        nw.setGapExtend(0.5);
        nw.setOutputFileDir(OUTPUT);
        
        BlastResult res_blast = null;// nw.runNeedle();
        return res_blast;
       
    }
     **/
    
    /**
     //function runs needle and parse output
    private NeedleResult runNeedleTest(SequencePair pair) throws BecUtilException
    {
        //run needle
        NeedleWrapper nw = new NeedleWrapper();
        
        
        NeedleResult res_needle =  nw.runNeedleTest("c:\\EMBOSS-2.5.1\\emboss\\needle.out");
        return res_needle;
       
    }
    **/
    public ArrayList  run_analysis(NeedleResult res_needle,  int exper_sequence_id, int refseq_id)
    
    {
        ArrayList res = new ArrayList();
        
        int length = 0;
        //check output of needle
        if (res_needle.getQuery() == null || res_needle.getSubject() == null) return null;
        //skip what ever before subject sequence started
        
        char[] sequence_query_n = res_needle.getQuery().toCharArray();
        char[] sequence_subject_n = res_needle.getSubject().toCharArray();
        int subject_start = Algorithms.findFirstLetter(sequence_subject_n);
        return run_analysis( sequence_query_n,  sequence_subject_n,subject_start, exper_sequence_id, refseq_id);
     }
    
    
    /*
    public ArrayList  run_analysis(BlastAligment blast_aligmnet,  int exper_sequence_id, int refseq_id)
    
    {
        ArrayList res = new ArrayList();
        
        int length = 0;
        //check output of needle
        if (blast_aligmnet.getQSequence() == null || blast_aligmnet.getSSequence() == null) return null;
        //skip what ever before subject sequence started
        
        char[] sequence_query_n = blast_aligmnet.getQSequence().toCharArray();
        char[] sequence_subject_n = blast_aligmnet.getSSequence().toCharArray();
        int subject_start = blast_aligmnet.getSStart();//Algorithms.findFirstLetter(sequence_subject_n);
        return run_analysis( sequence_query_n,  sequence_subject_n,subject_start, exper_sequence_id, refseq_id);
     }
    
    */
    
    private ArrayList  run_analysis(char[] sequence_query_n, char[] sequence_subject_n,
                                    int subject_start,
                                    int exper_sequence_id, int refseq_id)
    
    {
        ArrayList res = new ArrayList();
        
        int length = 0;
    
        length = ( sequence_query_n.length - subject_start >= sequence_subject_n.length - subject_start) ?
                sequence_query_n.length -1 - subject_start : 
                    sequence_subject_n.length -1 - subject_start;
        
        
        boolean isInMutation = false;
        
        int mut_start = -1;        int mut_count = 0;
        int codon_number = 0 ; int codon_start_mutation = 0 ;
        String q_allel = "";        String s_allel = "";
        StringBuffer q_sequence = new StringBuffer();
        StringBuffer s_sequence = new StringBuffer();
        int q_position = 0; int s_position = 0;
        
        
        AAMutation cur_aa_mutation = null;        RNAMutation cur_rna_mutation = null;
        
        try
        {
            for (int count = subject_start; count < length; count++ )
            {
               if ( sequence_query_n[count] != sequence_subject_n[count ]
                        && sequence_query_n[count] ==' ' )
                {
                    s_position++;
                     if (s_position  % 3 == 1)  codon_number++;
                    continue;
                    //just loop
                }
                //preserve sequence and position
                if ( ! isWrongChar(sequence_query_n[count])  )
                {
                    q_sequence.append(sequence_query_n[count]);
                    q_position++;
                }
                if ( ! isWrongChar( sequence_subject_n[count] ))
                {
                    s_sequence.append(sequence_subject_n[count]);
                    s_position++;
                }
                if (s_position  % 3 == 1)  codon_number++;
                
                
                if ( sequence_query_n[count] == sequence_subject_n[count ])
                {
                      // if either empty - get out - aligment finished - force last mutation to close up
                    if (sequence_query_n[count] == ' ' || sequence_query_n[count] == ' ' )
                    {
                       isInMutation = true;
                    }
                    // do nothing
                    if (isInMutation)//mutation finished
                    {
                        
                        mut_count++;
                        //get upstream string
                        int upstream_start = ( (q_position - q_allel.length() - RNAMutation.RNA_STREAM_RANGE) > 0 ) ? q_position - q_allel.length() - RNAMutation.RNA_STREAM_RANGE: 0;
                        String up = q_sequence.toString().substring( upstream_start, q_position - q_allel.length() - 1);
                        //get downstream string
                        int pos = count; String dn = ""; int dn_length=0;
                       
                        while(true)
                        {
                            
                            if ( ! isWrongChar( sequence_query_n[pos] ))
                            {
                                 dn += sequence_query_n[pos] ;
                               
                                dn_length++;
                                if (dn_length == RNAMutation.RNA_STREAM_RANGE || pos == sequence_query_n.length - 1
                                 || sequence_query_n[pos+1] ==' ' )
                                    break;
                            }
                            pos++;
                        }
                         //System.out.println(count);
                        //  int codon_start = 3 * ( (int)Math.ceil( (count - s_allel.length() )/ 3 ) );
                        
                        String cori = new String(sequence_subject_n,subject_start + ( codon_start_mutation - 1) * 3  , 3);
                        String corm = new String(sequence_query_n, subject_start+(codon_start_mutation -1 ) * 3  , 3); //codon mutant
                        cori =  cori.replace( ' ','-');
                        corm = corm.replace( ' ','-');
                         
                        cur_rna_mutation = new RNAMutation();
                        
                        cur_rna_mutation.setPolymFlag(RNAMutation.FLAG_POLYM_NOKNOWN);
                       
                        cur_rna_mutation.setUpstream(up);
                       cur_rna_mutation.setDownStream(dn);
                       cur_rna_mutation.setCodonOri( cori );
                       cur_rna_mutation.setCodonMut(corm);
                        cur_rna_mutation.setCodonPos( s_position  % 3 +1);
                         cur_rna_mutation.setPosition ( mut_start);// start of mutation (on object sequence)
                        cur_rna_mutation.setLength ( s_allel.length());
                       
                        cur_rna_mutation.setChangeMut ( q_allel);
                        cur_rna_mutation.setChangeOri ( s_allel);
                        cur_rna_mutation.setSequenceId ( exper_sequence_id) ;
                        cur_rna_mutation.setNumber (mut_count) ;
                        
                        cur_rna_mutation.getChangeType ( ) ;
                       
                          
                        System.out.println("New Mutation\n\n"+cur_rna_mutation.toString());
                        res.add(cur_rna_mutation);
                        if (    cur_rna_mutation.getType() != Mutation.TYPE_RNA_SILENT )
                        {
                            String atr =  SequenceManipulation.getTranslation( corm, SequenceManipulation.ONE_LETTER_TRANSLATION_NO_SPACE);
                            String am =  SequenceManipulation.getTranslation(cori, SequenceManipulation.ONE_LETTER_TRANSLATION_NO_SPACE);
                             cur_aa_mutation =  new AAMutation();
                            cur_aa_mutation.setPosition ( codon_start_mutation);// start of mutation (on object sequence)
                            cur_aa_mutation.setLength ( (int) Math.ceil(cori.length() / 3) );
                            cur_aa_mutation.setChangeMut ( atr);
                            cur_aa_mutation.setChangeOri ( am);
                            cur_aa_mutation.setSequenceId ( exper_sequence_id) ;
                            cur_aa_mutation.setNumber ( mut_count) ;
                            cur_aa_mutation.getChangeType ( ) ;
                            res.add(cur_aa_mutation);
                            
                            System.out.println(cur_aa_mutation.toString());
                        }
                      
                        mut_start = -1;
                        s_allel="";
                        q_allel="";
                        isInMutation =false;
                        // force get out on end of aligment
                        if (sequence_query_n[count] == ' ' || sequence_query_n[count] == ' ' )
                        {
                           return res;
                        }
                    }
                }
               
                
                else //not equal
                {
                    isInMutation = true;
                    if (mut_start == -1)
                    {
                        mut_start = s_position ;
                        codon_start_mutation = codon_number;
                    }
                    if (isInMutation)
                    {
                        if ( ! isWrongChar( sequence_query_n[count] ) )
                            q_allel += sequence_query_n[count];
                        if ( ! isWrongChar(sequence_subject_n[count] ) )
                            s_allel += sequence_subject_n[count];
                    }
                    
                }
                
            }
        }catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
        
        return res;
    }
    
    
    
    
    private  boolean isWrongChar(char ch)
    {
        if (ch == '-' || ch =='['  || ch ==' ')
            return true;
        else
            return false;
    }
    
  //******************************************
    public static void main(String args[])
    {
        
        String seq = "TTTTTTTTTTTTTTGAATTTGATAATCCTCCTTTTATTcCATATTAAACTTTAAAATTTGTACCACATTATTAAAGTATTACTTTTACTCACAGTAGTATTATACATAGACTTAACACAATTTTTAAAAATGTGTTTACTTAAAACAATATAATTCTCCTTTACAAAAGCAACTTTATATAAAATGTTTGGCTTAAGACTGTCATTGCTATTATGCCTTTGAATGAAATTCCACTCTTTCGCCTCCATTGTCCAGAAACAGGCACATATCAGCTTGTTTTCTTTAATGAATATTCTGTAACAAGTTCCTGAAGTTTTCTAATTCTTTCACACTTGTAGAAATTCTTCCAAATGCGTTGAATAATGATACTATTTCTTGTCTGGTTAGATGGAATTCATAACTAGGTCCACTTTCTGGCATATTTGCTATCAATTTCTCAGAAAATAAGATCTTCAGAGCAGTGCCCAAACCCTGAGTCTGAAGCTTTCCCCACAGACGACATTTAAAACAACCAACACAATCCATAATT";
        // String seq="GCGGCCGCATAACTTCGTATAGCATACATTATACGAAGTTATCAGTCGACACCATGCGCGAGATCGTGCACATCCAGGCGGGCCAGTGCGGCAACCAGATCGGCGCCAAGTTTTGGGAGGTCATCAGTGATGAGCATGGGATTGACCCCACTGGCAGTTACCATGGAGACAGTGATTTGCAGCTGGAGAGAATCAATGTTTACTACAATGAAGCCACTGGTAACAAATATGTTCCTCGGGCCATCCTCGTGGATCTGGAGCCAGGCACGATGGATTCGGTTAGGTCTGGACCATTCGGCCAGATCTTCAGACCAGACAATTTCGTGTTTGGCCAGAGTGGAGCCGGGAATAACTGGGCCAAGGGCCACTACACAGAGGGAGCCGAGCTGGTCGACTCGGTCCTGGATGTGGTGAGGAAGGAGTCAGAGAGCTGTGACTGTCTCCAGGGCTTCCAGCTGACCCACTCTCTGGGGGGCGGCACGGGGTCCGGGATGGGCACCCTGCTCATCAGCAAGATCCGGGAAGAGTACCCAGACCGCATCATGAACACCTTCAGCGTCATGCCCTCACCCAAGGTGTCAGACACGGTGGTGGAGCCCTACAACGCCACCCTCTCGGTCCACCAGCTGGTGGAAAACACAGATGAAACCTACTGCATTGACAACGAGGCCCTGTATGACATCTGCTTCCGCACCCTGAAGCTGACCACCCCCACCTACGGGGACCTCAACCACCTGGTGTCGGCCACCATGAGCGGGGTCACCACCTGCCTGCGCTTCCCGGGCCAGCTGAACGCAGACCTGCGCAAGCTGGCGGTGAACATGGTGCCCTTCCCTCGCCTGCACTTCTTCATGCCCGGCTTCGCGCCCCTGACCAGCCGGGGCAGCCAGCAGTACCGGGCGCTCACGGTGCCCGAGCTCACCCAGCAGATGTTCGACTCCAAGAACATGATGGCCGCCTGCGACCCGCGCCACGGCCGCTACCTGACGGTGGCTGCCATCTTCCGGGGCCGCATGTCCATGAAGGAGGTGGACGAGCAGATGCTCAACGTGCAGAACAAGAACAGCAGCTACTTCGTGGAGTGGATCCCCAACAACGTGAAGACGGCCGTGTGCGACATCCCGCCCCGCGGCCTGAAGATGTCGGCCACCTTCATCGGCAACAGCACGGCCATCCAGGAGCTGTTCAAGCGCATCTCCGAGCAGTTCACGGCCATGTTCCGGCGCAAGGCCTTCCTGCACTGGTACACGGGCGAGGGCATGGACGAGATGGAGTTCACCGAGGCCGAGAGCAACATGAACGACCTGGTGTCCGAGTACCAGCAGTACCAGGACGCCACGGCCGACGAACAAGGGGAGTTCGAGGAGGAGGAGGGCGAGGACGAGGCTTTGGGAAGCTTTCTAGACCATTCGTTTGGCGCGCGGGCCC";
          String queryFile = "c:\\blastoutput\\needle1204_1188.out";
        try
        {
             NeedleResult res = new NeedleResult();
             DiscrepancyFinder d =new DiscrepancyFinder(new ArrayList());
            NeedleParser.parse(queryFile,res);
            ArrayList m = d.run_analysis( res,  1188, 1204);
            
            System.out.println("---------------------");
            queryFile = "c:\\blastoutput\\needle1203_1188.out";
             NeedleParser.parse(queryFile,res);
             m = d.run_analysis( res,  1188, 1203);
      
        }catch(Exception e)
        {
            System.out.println(e.getMessage());}
    }
  
}
