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
import edu.harvard.med.hip.bec.coreobjects.endreads.*;
import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.programs.needle.*;

import edu.harvard.med.hip.bec.coreobjects.feature.*;
//import edu.harvard.med.hip.bec.engine.*;
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

    //store input & output blast files: do not allow to change
    private static final String INPUT = "/tmp/";
    public static final String OUTPUT = "/needleoutput/";
    private static final String LOG_FILE_NAME = "/kinaze/logfile.txt";

    private SequencePair m_seqpair = null;
    private ArrayList    m_seqpairs = null;
    //one based; this parameters defined as start / stop of ref sequence,
    // so they need to be recalculated if linker is attached to the real ref sequence sequence
    private int             m_cds_start  = 0; //start of cds for query sequence if covered
      private int             m_cds_stop  = 0; //start of cds for query sequence if covered

     private ArrayList    m_aligmentFiles = null;
    private double      m_identitycutoff = 60.0;
    private boolean      m_isRunCompliment = false;
    private String      m_input = INPUT;
    private String      m_output = OUTPUT;


    private static final int IDENTITY_100 = 0;
    private static final int IDENTITY_60 = 1;
     private static final int IDENTITY_100_60 = 2;


     private boolean m_debug = false;

     private boolean  m_endreads_analysis = false;

     //quality definition
     private int      m_max_number_of_mutations_to_detect = 20;
     private int      m_quality_cutoff = 20;
     private boolean      m_isDefineQuality  = false;

     //needle parameters
     private double     m_needle_gap_open = 10.0;
     private double     m_needle_gap_ext = 0.5;


     //linkers definition
      private int      m_refsequence_cds_start = 0;
    private int      m_refsequence_cds_stop = 0;

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



    public String      getInputDirectory(){ return m_input;}
    public String      getOutputDirectory(){ return m_output;}
    public ArrayList   getAligmentFileNames(){ return m_aligmentFiles;}
     public void      setInputDirectory(String v){  m_input = v;}
  //  public void      setOutputDirectory(String v){  m_output = v;}

    public void addSequencePair(SequencePair pair)    {        m_seqpairs.add(  pair);    }
    public void setSequencePair(SequencePair pair)    {        m_seqpair =  pair;    }
    // parameter requeres run query on compliment of query sequence
    // needle is not capable on converting sequence for finding better aligment
    public void setIsRunCompliment(boolean b){m_isRunCompliment = b;}
    public void setDebug(boolean b){ m_debug =b;}

    public void setInputType(boolean v){m_endreads_analysis = v;}
    public void setIdentityCutoff(double d)    { m_identitycutoff = d;}
    public void setMaxNumberOfDiscrepancies(int v){ m_max_number_of_mutations_to_detect = v;}
    public void setQualityCutOff(int v){ m_quality_cutoff = v; m_isDefineQuality = true;}

    //set needle parameters
    public void setNeedleGapOpen(double v){ m_needle_gap_open = v;}
    public void setNeedleGapExt(double v){     m_needle_gap_ext = v;}

     public void setRefSequenceCdsStart(int    v){  m_refsequence_cds_start = v;}
    public void setRefSequenceCdsStop(int    v){  m_refsequence_cds_stop = v;}

    public int     getCdsStart(){ return        m_cds_start  ;} //start of cds for query sequence if covered
    public int     getCdsStop(){ return        m_cds_stop ;}
    //main calling function for polymorphism finder
    public void run()throws BecDatabaseException, BecUtilException
    {
        m_aligmentFiles = new ArrayList();
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


    //**************************** private methods **************************


    //main function run full analysis for the experimental sequence
    
    
    
    
    
    
    private void analizeUsingNeedle(SequencePair pair)throws  BecUtilException
    {
        NeedleResult res_needle = null;
        res_needle =  runNeedle( pair);
        ArrayList mutations = new ArrayList();
        m_aligmentFiles.add(res_needle.getFileName());
                //run blast n
        res_needle.recalculateIdentity();
        if ( res_needle.getIdentity() <= m_identitycutoff)
        {
            pair.getQuerySequence().setStatus(BaseSequence.CLONE_SEQUENCE_STATUS_NOMATCH);

        }
        else// not 100 on nucleotide level
        {
            int[] quality_scores_query = pair.getQuerySequence().getScoresAsArray();
            if (m_isRunCompliment )
            {
                 quality_scores_query = SequenceManipulation.complimentScores( quality_scores_query );
            }
            mutations = run_analysis(res_needle, quality_scores_query, pair.getQuerySequence().getId(), pair.getRefSequence().getId(),  pair.getRefSequence().getText().length());
            pair.getQuerySequence().setDiscrepancies(mutations);
            if ( mutations != null && mutations.size() > 0)
                 pair.getQuerySequence().setStatus(BaseSequence.CLONE_SEQUENCE_STATUS_ANALIZED_YES_DISCREPANCIES);
            else
                pair.getQuerySequence().setStatus(BaseSequence.CLONE_SEQUENCE_STATUS_ANALIZED_NO_DISCREPANCIES);
        }

    }

   
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
        nw.setGapOpen(m_needle_gap_open);
        nw.setGapExtend(m_needle_gap_ext);

        nw.setOutputFileDir(m_output);

        NeedleResult res_needle =  nw.runNeedle();
        return res_needle;

    }

     //function runs needle and parse output

    public ArrayList  run_analysis(NeedleResult res_needle, int[] quality_scores_query,
    	int exper_sequence_id, int refseq_id, int refsequence_length)
    throws BecUtilException
    {
        ArrayList res = new ArrayList();

        int length = 0;
        //if no linker info is provided
        if (   m_refsequence_cds_stop == 0)
        {
              m_refsequence_cds_stop = refsequence_length + m_refsequence_cds_start;
        }
        //check output of needle
        if (res_needle.getQuery() == null || res_needle.getSubject() == null) return null;
        char[] sequence_query_n = res_needle.getQuery().toUpperCase().toCharArray();
        char[] sequence_subject_n = res_needle.getSubject().toUpperCase().toCharArray();
        //prepare sequence elements
        SequenceElement[] elements = prepareSequenceElements(sequence_query_n,sequence_subject_n,quality_scores_query);
        int  number_of_rna_discrepancies = 0;

        boolean isInMutation = false;
        boolean isAlignmentStarted = false;
        boolean isDefineQuality = ( quality_scores_query != null && quality_scores_query.length > 0);
        AAMutation cur_aa_mutation = null;        RNAMutation cur_rna_mutation = null;
       
        int mut_number = 0;int mut_start = -1;
        try{
        for (int count = 0; count < elements.length; count++ )
        {
   //check if alignment already started
               if (!isAlignmentStarted)
               {
                   if (elements[count].getQueryChar() != ' ' &&  elements[count].getSubjectChar() !=' ')
                   {
                       isAlignmentStarted = true;
                   }
                   else
                   {
                         continue;
                   }
               }//end if for aligment start
       // inside alignment
               if ( elements[count].getQueryChar()  == elements[count].getSubjectChar()  ||
                    ( elements[count].getQueryChar()  != elements[count].getSubjectChar()  &&
                    ( elements[count].getQueryChar()  == 'N' || sequence_query_n[count] == 'n'
                    || elements[count].getSubjectChar()  =='N' || elements[count].getSubjectChar() =='n' )))
               {
        // if either empty - get out - aligment finished - force last mutation to close up

                    // do nothing
                    if (isInMutation)//mutation finished
                    {
                         createMutations( res, isDefineQuality,  elements,   
                         count,  mut_start,  exper_sequence_id );
                        mut_start = -1;
                        isInMutation =false;
                        number_of_rna_discrepancies++;
                        //forse get out if max number of discrepancies reached
                        if (m_max_number_of_mutations_to_detect < number_of_rna_discrepancies)
                            return res;

                    }
                }
               else //not equal
                {
 //System.out.print(" "+elements[count].getSubjectChar() +" "+ elements[count].getQueryChar()+";");
                     if (elements[count].getSubjectChar() == ' ' || elements[count].getQueryChar() == ' ' )
                    {
                        //forse to close last mutation
                        if (isInMutation)
                        {
                            createMutations( res,  
                             isDefineQuality,  elements,   count,  mut_start,  exper_sequence_id);
                        }
                        break;
                    }
                     //forse to close if multipal substitution
                     boolean isCreateNewMutation = false;
                     if (isInMutation && elements[count].getSubjectChar() != elements[count].getQueryChar() )
                     {
                         if ( elements[count].getSubjectChar() != '-' && elements[count].getQueryChar() != '-')
                            isCreateNewMutation = true;//second substitution
                         if ( count > 1 && ( elements[count].getSubjectChar() == '-' && elements[count-1].getSubjectChar() != '-'))
                             isCreateNewMutation = true;
                         if   ( count > 1 && ( elements[count].getQueryChar() == '-' && elements[count-1].getQueryChar() != '-'))
                             isCreateNewMutation=true
                             ;
                         if (isCreateNewMutation)
                         {
                             createMutations( res,   isDefineQuality,  elements,   count,
                             mut_start,  exper_sequence_id);
                             mut_start = -1;
                             number_of_rna_discrepancies ++;
                             if ( m_max_number_of_mutations_to_detect < number_of_rna_discrepancies) return res;

                         }
                     }
                    isInMutation = true;
                    if (mut_start == -1)    mut_start = count ;

                 }    

            }
            return res;
       }
        catch(Exception e)
        {
             e.printStackTrace();
            System.out.println(e.getMessage());
            throw new BecUtilException(e.getMessage());
        }


    }

    private void createMutations(ArrayList res,  
            boolean isDefineQuality, SequenceElement[] elements,
            int count, int mut_start, int exper_sequence_id)
    {
        AAMutation cur_aa_mutation = null;
        RNAMutation cur_rna_mutation = null;
        LinkerMutation cur_linker_mutation = null;
        int  mut_number = 0;
        //determine mutation number
        if (res != null && res.size() > 0)
        {
            mut_number = ((Mutation) res.get(res.size() - 1)).getNumber();
        }
        mut_number++;
        //determine
        String q_allel = ""; String s_allel =""; int quality = RNAMutation.QUALITY_NOTKNOWN;
        for (int count_element = mut_start; count_element < count ;count_element++)
        {
                if (!isWrongChar(elements[count_element].getQueryChar()) )q_allel += elements[count_element].getQueryChar();
                if (!isWrongChar(elements[count_element].getSubjectChar()) )s_allel += elements[count_element].getSubjectChar();
        }
//determine quality
        if (isDefineQuality )
        {
                int base_count = 0;
                for (int count_element = mut_start - 2 ; count_element < s_allel.length() + 2 + mut_start; count_element++)
                {
                        if (count_element < 0) continue;
                        if (count_element > elements.length -1 ) break;
                        quality += elements[count_element].getBaseScore();
                        base_count++;
                }
                quality = (int) quality / base_count;
                if ( quality < 20)
                        quality = RNAMutation.QUALITY_LOW;
                else
                        quality = RNAMutation.QUALITY_HIGH;
        }

        //create mutation
        if (elements[mut_start].getSubjectIndex() <= m_refsequence_cds_start )
        {
                cur_linker_mutation = createLinkerDiscrepancy(true,
                                 elements,  mut_number,   mut_start , m_refsequence_cds_start,  exper_sequence_id,
                                 q_allel, s_allel, quality );
                res.add(cur_linker_mutation);

                return;
        }
        if (elements[mut_start].getSubjectIndex() > m_refsequence_cds_stop )
        {
                cur_linker_mutation = createLinkerDiscrepancy(false,
                                       elements, mut_number, mut_start, m_refsequence_cds_stop, exper_sequence_id,
                                       q_allel, s_allel, quality );
                res.add(cur_linker_mutation);
                return;
        }


        cur_rna_mutation = createRNADiscrepancy( elements,  count, mut_number,
                                                mut_start, m_refsequence_cds_start, exper_sequence_id,
                                                q_allel,s_allel,quality);
        res.add(cur_rna_mutation);
// System.out.println("\t\t\t New Mutation\n\n\t\t\t "+cur_rna_mutation.toString());
       
        if (    cur_rna_mutation.getType() != Mutation.TYPE_RNA_SILENT )
        {
           cur_aa_mutation= createAADiscrepancy( cur_rna_mutation.getCodonMut(),
                        cur_rna_mutation.getCodonOri(),
                        elements[count], cur_rna_mutation.getQuality(),
                        mut_number ,exper_sequence_id);



//System.out.println("\t\t\t "+ cur_aa_mutation.toString());
            res.add(cur_aa_mutation);
        }

    }

    private SequenceElement[] prepareSequenceElements(char[] sequence_query_n,
                                        char[] sequence_subject_n,
                                        int[] quality_scores)
    {
        int length = ( sequence_query_n.length  >= sequence_subject_n.length ) ?
                sequence_subject_n.length   :   sequence_query_n.length  ;
        SequenceElement[] elements = new SequenceElement[length];
        SequenceElement element = null;
        int q_index = 0;
        int s_index = 0;
        int q_score = 0;
        for (int count = 0; count < length; count++)
        {
            if ( sequence_subject_n[count] !=' ' && sequence_subject_n[count] != '-')
            {
                s_index++;
            }
            if (sequence_query_n[count] != ' ' && sequence_query_n[count] != '-')
            {
                q_index ++;
                if (quality_scores != null && quality_scores.length != 0)
                {
                    q_score = quality_scores[q_index - 1];
                }
            }

            element = new SequenceElement(  q_index  , s_index , q_score, sequence_query_n[count],sequence_subject_n[count]);
            elements[count]=element;

            //set up cds start && stop
            if (s_index >0 && q_index > 0 && m_cds_start == 0)
            {
                m_cds_start = q_index;
            }
            if (m_cds_stop == 0 && s_index > 1 && 
            ( 
             (sequence_subject_n[count] == ' ' && sequence_query_n[count] == ' ')
            || (sequence_subject_n[count] == ' ' && sequence_query_n[count] != ' ')
            ||  ( m_cds_start > 0 && sequence_subject_n[count] != ' ' && sequence_query_n[count] == ' ')
            || ( count == sequence_query_n.length -1 )))
                
            {
                m_cds_stop = q_index;
            }
        }
        return elements;
    }

    private LinkerMutation createLinkerDiscrepancy(boolean is5linker,
    					SequenceElement[] elements,
                                        int mut_number,  int mut_start, int refseq_cdspos,int sequence_id,
                                        String q_allel,String s_allel,int quality )
    {
		LinkerMutation cur_linker_mutation = null;
		cur_linker_mutation = new LinkerMutation(is5linker);
                int position =elements[mut_start].getSubjectIndex();
                if (is5linker)
                    position = position - refseq_cdspos;
                else
                    position = position -refseq_cdspos  ;
		cur_linker_mutation.setPosition ( position);// start of mutation (on object sequence)
		cur_linker_mutation.setLength ( s_allel.length());
		cur_linker_mutation.setChangeMut ( q_allel);
		cur_linker_mutation.setChangeOri ( s_allel);
		cur_linker_mutation.setSequenceId ( sequence_id) ;
		cur_linker_mutation.setNumber (mut_number) ;
		cur_linker_mutation.getChangeType ( ) ;
                cur_linker_mutation.setQuality( quality );
                return cur_linker_mutation;
    }

    private RNAMutation createRNADiscrepancy( SequenceElement[] elements, int current_element_count,
                    int mut_number,  int mut_start, int m_refsequence_cds_start, int sequence_id,
                    String q_allel,String s_allel,int quality)
    {

         StringBuffer up = new StringBuffer(); ; StringBuffer dn = new StringBuffer();
         if ( mut_start > 10 )
         {
            for (int count = mut_start - 1; count > mut_start - RNAMutation.RNA_STREAM_RANGE;)
            {

                if ( ! isWrongChar( elements[count].getQueryChar() ))
                {
                     dn.append( elements[count].getQueryChar()) ;
                }
                count--;
                if (count == 0 || elements[count].getQueryChar() == ' ') break;
            }
         }
         if ( current_element_count < elements.length + 10 )
         {
            for (int count = current_element_count - 1; count < current_element_count + RNAMutation.RNA_STREAM_RANGE;)
            {
                  if ( elements[count].getQueryChar() == ' ') break;
                if ( ! isWrongChar( elements[count].getQueryChar() ))
                {
                     up.append( elements[count].getQueryChar()) ;
                }
                  count++;
                    if (count == (elements.length -1)  ) break;
            }
         }

   //     int codon_start_mutation = (int) Math.ceil( s_position / 3);

        String cori = "";
        String corm = ""; //codon mutant
        int current_base =0; int start_codon_pos = 0;
        if  ( !isWrongChar(elements[mut_start].getSubjectChar() ))
        {
            current_base  = elements[mut_start].getSubjectIndex();
            start_codon_pos = mut_start;
       }
       else
       {
            current_base = elements[current_element_count].getSubjectIndex();
            start_codon_pos = mut_start;
        }

        switch (current_base % 3 )
        {
            case 1:
            {
                while (cori.length() < 3)
                {
                    if (start_codon_pos > elements.length-1) break;
                        cori+=elements[start_codon_pos].getSubjectChar();
                   corm +=elements[start_codon_pos].getQueryChar();
                    start_codon_pos++;
                }

                   break;
            }
            case 2 :
            {

                int keep_codon_start_search_position = start_codon_pos;
                while (cori.length() < 2)
                {
                    if (start_codon_pos > elements.length) break;
                    cori+=elements[start_codon_pos].getSubjectChar();
                    corm +=elements[start_codon_pos].getQueryChar();
                    start_codon_pos++;
                }
                while (cori.length() < 3)
                {
                    if ( keep_codon_start_search_position - 1 < 0)break;
                        cori=elements[--keep_codon_start_search_position].getSubjectChar()+cori;
                    corm =elements[keep_codon_start_search_position].getQueryChar()+corm;
                }
                break;
            }

          case 0:
        {

            while (cori.length() < 3)
            {
             //   if ( !isWrongChar(elements[start_codon_pos].getSubjectChar()))
             //   {
                if (start_codon_pos < 0)break;
                    cori=elements[start_codon_pos].getSubjectChar()+cori;
              //  }
                corm =elements[start_codon_pos].getQueryChar()+corm;
                start_codon_pos--;
            }
          }
        }


        RNAMutation cur_rna_mutation = new RNAMutation();
        cur_rna_mutation.setPolymFlag(RNAMutation.FLAG_POLYM_NOKNOWN);
        cur_rna_mutation.setUpstream(up.toString());
        cur_rna_mutation.setDownStream(dn.toString());
        cur_rna_mutation.setCodonOri( cori );
        cur_rna_mutation.setCodonMut(corm);
        cur_rna_mutation.setCodonPos( elements[mut_start].getSubjectIndex()  % 3 );
        cur_rna_mutation.setPosition ( elements[mut_start].getSubjectIndex()- m_refsequence_cds_start);// start of mutation (on object sequence)
        cur_rna_mutation.setLength ( s_allel.length());
        cur_rna_mutation.setChangeMut ( q_allel);
        cur_rna_mutation.setChangeOri ( s_allel);
        cur_rna_mutation.setSequenceId ( sequence_id) ;
        cur_rna_mutation.setNumber (mut_number) ;
        cur_rna_mutation.getChangeType ( ) ;
        cur_rna_mutation.setQuality( quality );
        return cur_rna_mutation;

    }

    private AAMutation createAADiscrepancy(String cor_mut, String cor_ori,   SequenceElement element,
          int quality ,   int mutation_number,   int sequence_id)
    {

        AAMutation cur_aa_mutation = null;


        String atr =  SequenceManipulation.getTranslation( cor_mut, SequenceManipulation.ONE_LETTER_TRANSLATION_NO_SPACE);
        String am =  SequenceManipulation.getTranslation(cor_ori, SequenceManipulation.ONE_LETTER_TRANSLATION_NO_SPACE);

        cur_aa_mutation =  new AAMutation();
        cur_aa_mutation.setPosition ( (int)Math.floor( (element.getSubjectIndex() - m_refsequence_cds_start)/ 3) + 1 );// start of mutation (on object sequence)
        cur_aa_mutation.setLength ( (int) Math.ceil(cor_ori.length() / 3) );
        cur_aa_mutation.setChangeMut ( atr);
        cur_aa_mutation.setChangeOri ( am);
        cur_aa_mutation.setSequenceId ( sequence_id) ;
        cur_aa_mutation.setNumber ( mutation_number) ;
        cur_aa_mutation.getChangeType ( ) ;
        cur_aa_mutation.setQuality( quality );
        return cur_aa_mutation;

    }


    private  boolean isWrongChar(char ch)
    {
        if (ch == '-' || ch =='['  || ch ==' ')
            return true;
        else
            return false;
    }
/*

    class SequenceElement
    {
        private int i_query_index = -1;
        private int i_subject_index = -1;
        private int i_base_score = 0;
        private char i_query_base ;
        private char i_subject_base;

        public SequenceElement( int query_index  ,int subject_index ,int base_score,char query_base,char subject_base)
        {
            i_query_index = query_index;
            i_subject_index = subject_index;
            i_base_score = base_score;
            i_query_base = query_base;
            i_subject_base = subject_base;

        }
         public SequenceElement(){}

         public int getQueryIndex (){ return i_query_index    ; }
        public int getSubjectIndex (){ return i_subject_index    ; }
        public int getBaseScore (){ return i_base_score    ; }
        public char getQueryChar (){ return i_query_base   ; }
        public char getSubjectChar (){ return i_subject_base   ; }

         public void setQueryIndex (int v){  i_query_index    = v; }
        public void setSubjectIndex (int v){  i_subject_index    = v; }
        public void setBaseScore (int v){  i_base_score    = v; }
        public void setQueryChar (char v){  i_query_base   = v; }
        public void setSubjectChar (char v){  i_subject_base   = v; }
    }
 *
 **/
  //******************************************
    public static void main(String args[])
    {

      
       try
        {
           /*
            NeedleResult res = new NeedleResult();

            ArrayList files = new ArrayList();
            BufferedReader reader = new BufferedReader(new FileReader("c:\\kinez-test.txt"));

            while((line = reader.readLine()) != null)
            {
                StringTokenizer st = new StringTokenizer(line, "\t");
                String [] info = new String[3];
                int i = 0;

                while(st.hasMoreTokens())
                {
                    info[i] = st.nextToken();
                    i++;
                }
                cloneseq = info[1];
                if (cloneseq.startsWith("'") )cloneseq.substring(1);

                refseq=info[2];
                refseq = refseq.substring(0, refseq.length()-3);
                refseqid = Integer.parseInt(info[0]);
                clonesequence = new AnalyzedScoredSequence(cloneseq,   refseqid);
                clonesequence.setId(-refseqid);
                refsequence = new BaseSequence(refseq,BaseSequence.BASE_SEQUENCE);
                refsequence.setId(refseqid);
                pair = new SequencePair(clonesequence ,  refsequence );
                pairs.add(pair);
                 DiscrepancyFinder d =new DiscrepancyFinder(pair);
                   d.setInputDirectory("/tmp/");

                 d.run();
                   files.add( d.getAligmentFileNames() );
                   File n = new File( (String)files.get(0));
                  if ( clonesequence.getDiscrepancies() == null ||
                                clonesequence.getDiscrepancies().size() == 0)
                 {
                     System.out.println("\t\t No discrepancies have been detected\n\n");
                 }
                 else
                 {
                           //write down mutations
                    int discrepancy_number = 1;Mutation discr=null;
                     for (int count = 0; count < clonesequence.getDiscrepancies().size(); count++)
                     {
                         discr = (Mutation) clonesequence.getDiscrepancies().get(count);
                         if ( discrepancy_number != discr.getNumber())
                         {
                            System.out.println("\n\t\t New Discrepancy ");
                            discrepancy_number = discr.getNumber();
                         }
                        System.out.println( discr.toString() );

                     }
                 }

            }

            reader.close();

            */

           NeedleResult res = new NeedleResult();
          String queryFile ="c:\\needleoutput\\needle14137_583.out";
            //  String queryFile = "c:\\needleATG.out";
             NeedleParser.parse(queryFile,res);

            // edu.harvard.med.hip.bec.coreobjects.endreads.Read read =  edu.harvard.med.hip.bec.coreobjects.endreads.Read.getReadById(1154);
            // int[] trimmed_scores = read.getTrimmedScoresAsArray();
             DiscrepancyFinder d =new DiscrepancyFinder(new ArrayList());
             
       ///      String l5 = "CTTCCGGCTGACCATC";
        //     String l3 = "CATGGCTATTCGGGG";
        //     String seqr="ATGATTGTTAAAGTGAAGACACTGACTGGGAAGGAGATCTCTGTTGAGCTGAAGGAATCAGATCTCGTATATCACATCAAGGAACTTTTGGAGGAAAAAGAAGGGATTCCACCATCTCAACAAAGACTTATATTCCAGGGAAAACAAATGTATGTTCATTCAGTGGTTAA--ATCTTTTAAGTTTATTTACGTTTTTGAGACCTTACTAACGACCAGGATAGTGATGATAAATTAACAGTAACGGATGCACATCTAGTAGAGGGAATGCAACTCCACTTGGTATTAACACTAAGAGGTGGTAACTAG";
             

         //    d.setRefSequenceCdsStart(l5.length());
         //    d.setRefSequenceCdsStop(l5.length() +seqr.length()-2);
             
         //    int[]scores=null;//{10,12,13,10,12,13,10,12,13,10,12,13,10,12,13,10,12,13,10,12,13,10,12,13,10,12,13,24,56,43,45,10,24,56,43,45,10,10,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,10,45,10,24,56,43,45,10,10,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,10,45,10,24,56,43,45,10,10,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,10,45,10,24,56,43,45,10,10,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,10,45,10,24,56,43,45,10,10,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,10,45,10,24,56,43,45,10,10,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,10,45,10,24,56,43,45,10,10,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,10,45,10,24,56,43,45,10,10,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,10,10,10,20,20,20,20,20,20,24,56,43,45,10,12,13,24,56,43,45,10,24,56,43,45,10,10,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,10,10,10,20,20,20,20,20,20,24,56,43,45,10,10,10,30,30,30,30,30,34,34,24,56,43,45,10,10,10,23,10,10,10,30,30,30,10,12,13,24,56,43,45,10,24,56,43,45,10,10,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,10,10,10,20,20,20,20,20,20,24,56,43,45,10,10,10,30,30,30,30,30,34,34,24,56,43,45,10,10,10,23,10,12,13,24,56,43,45,10,24,56,43,45,10,10,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,10,10,10,20,20,20,20,20,20,24,56,43,45,10,10,10,30,30,30,30,30,34,34,24,56,43,45,10,10,10,23,10,12,13,24,56,43,45,10,24,56,43,45,10,10,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,10,10,10,20,20,20,20,20,20,24,56,43,45,10,10,10,30,30,30,30,30,34,34,24,56,43,45,10,10,10,23,10,12,13,24,56,43,45,10,24,56,43,45,10,10,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,10,10,10,20,20,20,20,20,20,24,56,43,45,10,10,10,30,30,30,30,30,34,34,24,56,43,45,10,10,10,23,10,12,13,24,56,43,45,10,24,56,43,45,10,10,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,10,10,10,20,20,20,20,20,20,24,56,43,45,10,10,10,30,30,30,30,30,34,34,24,56,43,45,10,10,10,23,30,30,34,34,24,56,43,45,10,10,10,23,10,21,10,10,21,30,40,40,50,30,30,10,12,13,24,56,43,45,34,34,23,10,21,10,10,21,30,40,40,50,30,30,10,12,13,24,56,43,45,34,34,23,10,21,10,10,21,30,40,40,50,30,30,10,12,13,24,56,43,45,34,34,23,10,21,10,10,21,30,40,40,50,30,30,10,12,13,24,56,43,45,34,34,23,10,21,10,10,21,30,40,40,50,30,30,10,12,13,24,56,43,45,34,34,23,10,21,10,10,21,30,40,40,50,30,30,10,12,13,24,56,43,45,34,34,23,10,21,10,10,21,30,40,40,50,30,30,10,12,13,24,56,43,45,34,34,23,10,21,10,10,21,30,40,40,50,30,30,10,12,13,24,56,43,45,34,34,23,10,21,10,10,21,30,40,40,50,30,30,10,12,13,40,40,50,30,30,10,12,13,40,40,50,30,30,10,12,13,40,40,50,30,30,10,12,13,24,56,43,45,34,34,23,10,21,10,10,21,30,40,40,50,30,30};
         d.setMaxNumberOfDiscrepancies(20);
         d.setRefSequenceCdsStart(   69 );
         d.setRefSequenceCdsStop(  1315);
             ArrayList   m = d.run_analysis( res,  null,1188, 1203,450);
         
          System.out.println(d.getCdsStart()+" "+d.getCdsStop());


            /*
             refsequence = new RefSequence(374);
            Read read =  Read.getReadById(2529);

        pair = new SequencePair(read.getSequence() ,  (BaseSequence)refsequence );
        pairs.add(pair);
        DiscrepancyFinder d =new DiscrepancyFinder(pair);
        d.run();
             **/
        }catch(Exception e)
        {
            System.out.println(e.getMessage());}
          System.exit(0);
    }


    

}
