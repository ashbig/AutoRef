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
import edu.harvard.med.hip.utility.*;

public class DiscrepancyFinder
{
   
    //store input & output blast files: do not allow to change
    private  String INPUT = null;
    public  String OUTPUT = "/output/needleoutput/";
    
    // private static final String LOG_FILE_NAME = "/needleoutput/logfile.txt";

    private  String LOG_FILE_NAME = "/output/needleoutput/logfile.txt";
    {
        if (ApplicationHostDeclaration.IS_BIGHEAD)
        {
            INPUT = Constants.getTemporaryFilesPath();
            OUTPUT = "d:\\output\\needleoutput\\";
            LOG_FILE_NAME = "d:\\output\\needleoutput\\logfile.txt";
            if (ApplicationHostDeclaration.IS_BIGHEAD_FOR_EXPRESSION_EVALUATION) 
            {
                 OUTPUT = "d:\\output\\eval_needleoutput\\";
            }
        }
        else
        {
            INPUT = Constants.getTemporaryFilesPath();
            OUTPUT = "c:\\needleoutput\\";
            LOG_FILE_NAME = "c:\\needleoutput\\logfile.txt";
        }
    }
    private SequencePair m_seqpair = null;
    private ArrayList    m_seqpairs = null;
    //one based; this parameters defined as start / stop of ref sequence,
    // so they need to be recalculated if linker is attached to the real ref sequence sequence
    private int             m_cds_start  = 0; //start of cds for query sequence if covered
      private int             m_cds_stop  = 0; //start of cds for query sequence if covered

     private ArrayList    m_aligmentFiles = null;
    private double      m_identitycutoff = Constants.MIN_IDENTITY_CUTOFF;
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
    public NeedleResult runNeedle(SequencePair pair) throws BecUtilException
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
     //   nw.setGapOpen(m_needle_gap_open);
    //    nw.setGapExtend(m_needle_gap_ext);

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
        int mut_start = -1;
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
             if ( (elements[count].getQueryChar()  == elements[count].getSubjectChar() )
                    ||
                    ( ( elements[count].getQueryChar()  != elements[count].getSubjectChar() ) &&
                        ( elements[count].getQueryChar()  != 'N' && elements[count].getSubjectChar() =='N' )))
               {
        // if either empty - get out - aligment finished - force last mutation to close up

                    // do nothing
                    if (isInMutation)//mutation finished
                    {
                         createDiscrepancyDescription( res, isDefineQuality,  elements,   
                         count,  mut_start,  exper_sequence_id );
                        mut_start = -1;
                        isInMutation =false;
                        number_of_rna_discrepancies++;
                        //forse get out if max number of discrepancies reached
                        if (m_max_number_of_mutations_to_detect < number_of_rna_discrepancies)
                            break;

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
                            createDiscrepancyDescription( res, isDefineQuality,  elements,   count,  mut_start,  exper_sequence_id);
                        }
                        break;
                    }
                     //forse to close if multipal substitution
                     boolean isCreateNewMutation = false;
                     if (isInMutation && elements[count].getSubjectChar() != elements[count].getQueryChar() )
                     {
                         //second substitution
                         if ( elements[count].getSubjectChar() != '-' && elements[count].getQueryChar() != '-')
                            isCreateNewMutation = true;
                         if ( count > 1 && ( elements[count].getSubjectChar() == '-' && elements[count-1].getSubjectChar() != '-'))
                             isCreateNewMutation = true;
                         if   ( count > 1 && ( elements[count].getQueryChar() == '-' && elements[count-1].getQueryChar() != '-'))
                             isCreateNewMutation=true ;
                         //insertion deletion going from 5'linker into start codon or from stop codon into 3linker
                         if ((mut_start != count && elements[count].getSubjectIndex() == m_refsequence_cds_start +1 )
                             || (mut_start != count && elements[count].getSubjectIndex() == m_refsequence_cds_stop +1 ))
                            isCreateNewMutation=true ;
                         if (isCreateNewMutation)
                         {
                             createDiscrepancyDescription( res,   isDefineQuality,  elements,   count,
                                                        mut_start,  exper_sequence_id);
                             mut_start = -1;
                             if ( m_max_number_of_mutations_to_detect < res.size()) break;

                         }
                     }
                    isInMutation = true;
                    if (mut_start == -1)    mut_start = count ;
                }    
           }

            ArrayList discrepancies = new ArrayList();
            for (int count = 0; count < res.size(); count++)
            {
                discrepancies.addAll( ((DiscrepancyDescription) res.get(count)).getAllDiscrepancies());
            }
            return discrepancies;
       }
        catch(Exception e)
        {
             e.printStackTrace();
            System.out.println(e.getMessage());
           throw new BecUtilException(e.getMessage());
        }


    }
/*
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
    
    */
     private void createDiscrepancyDescription(ArrayList discrepancy_descriptions,  
            boolean isDefineQuality, SequenceElement[] elements,
            int discr_end_pointer, int discr_start_pointer, int exper_sequence_id)
    {
        discr_end_pointer--;
        //determine
        String q_allel = ""; String s_allel =""; int quality = RNAMutation.QUALITY_NOTKNOWN;
        for (int count_element = discr_start_pointer; count_element <= discr_end_pointer ;count_element++)
        {
                if (!isWrongChar(elements[count_element].getQueryChar()) )q_allel += elements[count_element].getQueryChar();
                if (!isWrongChar(elements[count_element].getSubjectChar()) )s_allel += elements[count_element].getSubjectChar();
        }
//determine quality
        if (isDefineQuality )
        {
            quality = calculateDiscrepancyQuality(elements, discr_start_pointer, discr_end_pointer);
         }

        //create mutation
        if ((elements[discr_start_pointer].getSubjectIndex() <= m_refsequence_cds_start) 
            || (elements[discr_start_pointer].getSubjectIndex() > m_refsequence_cds_stop )
            || (elements[discr_start_pointer].getSubjectIndex() == m_refsequence_cds_stop && 
                    elements[discr_start_pointer].getSubjectChar() == '-'))
        {
                createLinkerDiscrepancyDescription(discrepancy_descriptions, 
                                                                elements, 
                                                                discr_start_pointer , 
                                                                m_refsequence_cds_start,  
                                                                m_refsequence_cds_stop,
                                                                exper_sequence_id,
                                                                q_allel, 
                                                                s_allel, 
                                                                quality );
               
        }
        else
        {
        
            createNotLinkerDiscrepancyDescription( discrepancy_descriptions, 
                                                    elements,  
                                                    discr_end_pointer, 
                                                    discr_start_pointer, 
                                                    m_refsequence_cds_start, 
                                                    exper_sequence_id,
                                                    q_allel,
                                                    s_allel,
                                                    quality);
      
//System.out.println("\t\t\t "+ cur_aa_mutation.toString());
           
        }
        
    }

     private int calculateDiscrepancyQuality(SequenceElement[] elements, int discr_start_pointer,int discr_end_pointer)
     {
         
            int base_count = 0;int quality = RNAMutation.QUALITY_NOTKNOWN;
            /*
            //count dicrepancy bases
            for (int count_element = discr_start_pointer ; count_element <= discr_end_pointer; count_element++)
            {
                if ( elements[count_element].getQueryChar() != '\u0000')
                {
                    quality += elements[count_element].getBaseScore();
                    base_count++;
                }
            }
             //count two downstream
            int count_end = 2;
            int element_pointer = discr_start_pointer - 1;
            while ( count_end > 0 && element_pointer >= 0)
            {
                if ( elements[element_pointer].getQueryChar() != '\u0000')
                {
                    quality += elements[element_pointer].getBaseScore();
                    base_count++;
                    count_end--;
                }
                element_pointer--;
            }
            
             //count two upstream
            count_end = 0;
            element_pointer = discr_end_pointer + 1;
            while ( count_end < 2 && element_pointer < elements.length)
            {
                if ( elements[element_pointer].getQueryChar() != '\u0000')
                {
                    quality += elements[element_pointer].getBaseScore();
                    base_count++;
                    count_end++;
                }
                element_pointer++;
            }
            
            
            quality = (int) quality / base_count;
            if ( quality < m_quality_cutoff)
                    return RNAMutation.QUALITY_LOW;
            else
                    return RNAMutation.QUALITY_HIGH;
             */
              //count dicrepancy bases
            for (int count_element = discr_start_pointer ; count_element <= discr_end_pointer; count_element++)
            {
                if ( elements[count_element].getQueryChar() != '\u0000')
                {
                    if ( elements[count_element].getBaseScore() < m_quality_cutoff)
                        return RNAMutation.QUALITY_LOW;
                }
            }
             //count n downstream
            int count_end = 4;
            int element_pointer = discr_start_pointer - 1;
            while ( count_end > 0 && element_pointer >= 0)
            {
                if ( elements[element_pointer].getQueryChar() != '\u0000')
                {
                     if ( elements[element_pointer].getBaseScore() < m_quality_cutoff)
                        return RNAMutation.QUALITY_LOW; 
                    count_end--;
                }
                element_pointer--;
            }
            
             //count n upstream
            count_end = 0;
            element_pointer = discr_end_pointer + 1;
            while ( count_end < 4 && element_pointer < elements.length)
            {
                if ( elements[element_pointer].getQueryChar() != '\u0000')
                {
                     if ( elements[element_pointer].getBaseScore() < m_quality_cutoff)
                        return RNAMutation.QUALITY_LOW; 
                    count_end++;
                }
                element_pointer++;
            }
            
            
           return RNAMutation.QUALITY_HIGH;
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
    
    private void createNotLinkerDiscrepancyDescription( 
                                                    ArrayList discrepancy_descriptions,
                                                    SequenceElement[]  elements,  
                                                    int discr_end_pointer, 
                                                    int discr_start_pointer, 
                                                    int refsequence_cds_start, 
                                                    int exper_sequence_id,
                                                    String q_allel,
                                                    String s_allel,
                                                    int quality)
    {
        DiscrepancyDescription discr_description =  new DiscrepancyDescription();
        String cor_mut = null;
        String cor_ori = null;
        ArrayList discrepancies = new ArrayList();
        String str_mut = null;
        String str_ori = null;
        int mutation_number = discrepancy_descriptions.size();
        //create rna discrepancy
        RNAMutation rna = createRNADiscrepancy( elements, 
                                                discr_end_pointer,
                                                mutation_number, 
                                                discr_start_pointer,
                                                refsequence_cds_start, 
                                                exper_sequence_id,
                                                q_allel,
                                                s_allel,
                                                quality);
        
        //check for ambiquaty, if this ab discrepancy create desc definition and exit
        
        
        //check that this mutation was not added yet
        if (isSameDiscrepancy (elements[discr_start_pointer].getSubjectIndex(),discrepancy_descriptions))
        {
             discr_description = (DiscrepancyDescription)discrepancy_descriptions.get(discrepancy_descriptions.size()-1);
             rna.setNumber( discr_description.getAADefinition().getNumber());
             discr_description.addRNADiscrepancy(rna);
       
        }
        else 
        {
           //if amb case where creation of aa not requered - skip it
          String[] codons_for_processing = {rna.getCodonOri(),rna.getCodonMut()};
          boolean isAmbiquousBase = RNAMutation.processForAmbiquoty(rna.getSubjectStr(), rna.getQueryStr() , codons_for_processing); 
          if ( (isAmbiquousBase && !codons_for_processing[1].equalsIgnoreCase( rna.getCodonMut()))
                || !isAmbiquousBase)
            {
                AAMutation aa = createAADiscrepancy( codons_for_processing[1], rna.getCodonOri(),  
                                                q_allel, s_allel,
                                                elements[discr_start_pointer].getSubjectIndex(),
                                                 quality ,   
                                                 mutation_number,   
                                                 exper_sequence_id);
                discrepancies.add( aa );
            }
            discrepancies.add( rna);
            discr_description.setDiscrepancies(discrepancies);
            discrepancy_descriptions.add(discr_description);
        }
        
    }
    
    //checks by position if the AA discrepancy we are going to create was already created 
    //on the previous run
    private boolean isSameDiscrepancy (int subjectIndex, ArrayList discrepancy_descriptions)
    {
        int position =( (int) (subjectIndex - m_refsequence_cds_start -1)/ 3) + 1;
        if ( discrepancy_descriptions == null || discrepancy_descriptions.size() < 1) return false;
        DiscrepancyDescription dd = (DiscrepancyDescription)discrepancy_descriptions.get(discrepancy_descriptions.size()-1);
        if ( dd.getDiscrepancyDefintionType() != DiscrepancyDescription.TYPE_AA ||
              (dd.getDiscrepancyDefintionType() == DiscrepancyDescription.TYPE_AA 
                    && dd.getAADefinition().getPosition() != position))
        {
            return false;
        }
        else 
            return true;
    }
    
//linker discrepancy  elements, 
                                                               
    private void createLinkerDiscrepancyDescription( 
                                     ArrayList discrepancy_descriptions,
                                        SequenceElement[] elements,
                                         int discr_start_pointer, 
                                        int refseq_cds_start,int refseq_cds_stop,
                                        int sequence_id,
                                        String q_allel,String s_allel,int quality  )
    {
        boolean is5linker = (elements[discr_start_pointer].getSubjectIndex() <= refseq_cds_start )? true : false;
        int refseq_cdspos = 0;
        if ( is5linker)
        {
           refseq_cdspos = refseq_cds_start;
        }
        else
        {
            refseq_cdspos = refseq_cds_stop;
        }
        LinkerMutation ld = createLinkerDiscrepancy( is5linker,
    					elements,
                                         discrepancy_descriptions.size(),  
                                         discr_start_pointer,  
                                         refseq_cdspos, sequence_id,
                                         q_allel, s_allel, quality );
         DiscrepancyDescription new_definition = new DiscrepancyDescription();
         ArrayList discr = new ArrayList();
         discr.add(ld);
         new_definition.setDiscrepancies( discr);
         if (new_definition != null)
            discrepancy_descriptions.add( new_definition);
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
                cur_linker_mutation.setExpPosition ( elements[mut_start].getQueryIndex());
               // System.out.println(cur_linker_mutation.toString());
                return cur_linker_mutation;
    }

    
    ///////////////////
    private RNAMutation createRNADiscrepancy( SequenceElement[] elements, int discr_end_pointer,
                    int mut_number,  int discr_start_pointer, 
                    int refsequence_cds_start, int exp_sequence_id,
                    String q_allel,String s_allel,int quality)
    {

         StringBuffer up = new StringBuffer(); ; StringBuffer dn = new StringBuffer();
         int codon_position = 0;
        //get downstream 
       
        int element_count = discr_start_pointer - 1 ;
        int added_base_count = 0;
                
        while ( added_base_count < RNAMutation.RNA_STREAM_RANGE && element_count >= 0)
        {
            if ( elements[element_count].getQueryChar() != '\u0000' && ! isWrongChar(elements[element_count].getQueryChar()) )
            {
                if ( elements[element_count].getQueryChar() == ' ') break;
                 dn.append( elements[element_count].getQueryChar()) ;
                 added_base_count++;
            }
            element_count--;
        }
        //get upstream
        element_count = discr_end_pointer + 1 ;        added_base_count = 0;
        while ( added_base_count < RNAMutation.RNA_STREAM_RANGE && element_count <  elements.length)
        {
            if ( elements[element_count].getQueryChar() != '\u0000' && ! isWrongChar(elements[element_count].getQueryChar()))
            {
                if ( elements[element_count].getQueryChar() == ' ') break;
                 dn.append( elements[element_count].getQueryChar()) ;
                 added_base_count++;
            }
            element_count++;
        }
        String cori = "";        String corm = ""; //codon mutant
        
        int current_base =0; int start_codon_pos = 0;
        if  ( !isWrongChar(elements[discr_start_pointer].getSubjectChar() ))
        {
            current_base  = elements[discr_start_pointer].getSubjectIndex();
            start_codon_pos = discr_start_pointer ;
       }
       else
       {
            current_base = elements[discr_end_pointer+1].getSubjectIndex();
            start_codon_pos = discr_start_pointer ;
        }

        switch ( (current_base - refsequence_cds_start) % 3 )
        {
            case 1:
            {
                 codon_position = 1;
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
                 codon_position = 2;
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
             codon_position = 3;
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
        
      
        cur_rna_mutation.setCodonPos( codon_position );
        cur_rna_mutation.setPosition ( elements[discr_start_pointer].getSubjectIndex()- refsequence_cds_start);// start of mutation (on object sequence)
        cur_rna_mutation.setExpPosition ( elements[discr_start_pointer].getQueryIndex());// start of mutation (on object sequence)
        cur_rna_mutation.setLength ( s_allel.length());
        cur_rna_mutation.setChangeMut ( q_allel);
        cur_rna_mutation.setChangeOri ( s_allel);
        cur_rna_mutation.setSequenceId ( exp_sequence_id) ;
        cur_rna_mutation.setNumber (mut_number) ;
        cur_rna_mutation.getChangeType ( ) ;
        cur_rna_mutation.setQuality( quality );
        
     //   System.out.println(cur_rna_mutation.toString());
        return cur_rna_mutation;

    }

    private AAMutation createAADiscrepancy(String cor_mut, String cor_ori, 
        String q_allel, String s_allel,
        int subjectIndex,
          int quality ,   int mutation_number,   int sequence_id)
    {

        AAMutation cur_aa_mutation = null;
        
         // for multibase substitution where one of the bases is N
            //resign it to be write base
        
        String atr =  SequenceManipulation.getTranslation( cor_mut, SequenceManipulation.ONE_LETTER_TRANSLATION_NO_SPACE);
        String am =  SequenceManipulation.getTranslation(cor_ori, SequenceManipulation.ONE_LETTER_TRANSLATION_NO_SPACE);
        cur_aa_mutation =  new AAMutation();
        cur_aa_mutation.setPosition ( ((int) (subjectIndex - m_refsequence_cds_start -1)/ 3) + 1 );// start of mutation (on object sequence)
        cur_aa_mutation.setLength ( (int) Math.ceil(cor_ori.length() / 3) );
        cur_aa_mutation.setChangeMut ( atr);
        cur_aa_mutation.setChangeOri ( am);
        cur_aa_mutation.setSequenceId ( sequence_id) ;
        cur_aa_mutation.setNumber ( mutation_number) ;
        cur_aa_mutation.getChangeType ( q_allel, s_allel ) ;
        cur_aa_mutation.setQuality( quality );
      //  System.out.println(cur_aa_mutation.toString());
        return cur_aa_mutation;
    }


    private  boolean isWrongChar(char ch)
    {
        if (ch == '-' || ch =='['  || ch ==' ' )
            return true;
        else
            return false;
    }
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

            

           NeedleResult res = new NeedleResult();
          String queryFile ="c:\\alt.txt";
            //  String queryFile = "c:\\needleATG.out";
             NeedleParser.parse(queryFile,res);

            // edu.harvard.med.hip.bec.coreobjects.endreads.Read read =  edu.harvard.med.hip.bec.coreobjects.endreads.Read.getReadById(1154);
            // int[] trimmed_scores = read.getTrimmedScoresAsArray();
             DiscrepancyFinder d =new DiscrepancyFinder(new ArrayList());
             
             String l5 = "caaattgatgagcaatgcttttttataatgccaactttgtacaaaaaagcaggcttccagctgaccaacc";
        //     String l3 = "CATGGCTATTCGGGG";
            String seqr="ATGCCCGCAATAAAAGAAGCAAAGCAATGAAAGGAACCAAAGAAGAGGACCACCAGGAGAAAGAAGGATCCTAACGCCCCTAAGAGGGGCTTGTCAGCTTATATGTTCTTTGCTAATGAAAACAGAGACATTGTCCGTTCCGAGAATCCTGACGTAACTTTTGGCCAAGTAGGCAGAATATTGGGTGAGAGGTGGAAGGCCTTAACTGCTGAAGAAAAGCAACCCTATGAATCTAAGGCTCAAGCAGACAAGAAGAGATACGAATCTGAAAAGGAATTGTACAATGCTACACGTGCTTGA";

             d.setRefSequenceCdsStart(l5.length());
             d.setRefSequenceCdsStop(l5.length() +seqr.length());
             
         //    int[]scores=null;//{10,12,13,10,12,13,10,12,13,10,12,13,10,12,13,10,12,13,10,12,13,10,12,13,10,12,13,24,56,43,45,10,24,56,43,45,10,10,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,10,45,10,24,56,43,45,10,10,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,10,45,10,24,56,43,45,10,10,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,10,45,10,24,56,43,45,10,10,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,10,45,10,24,56,43,45,10,10,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,10,45,10,24,56,43,45,10,10,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,10,45,10,24,56,43,45,10,10,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,10,45,10,24,56,43,45,10,10,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,10,10,10,20,20,20,20,20,20,24,56,43,45,10,12,13,24,56,43,45,10,24,56,43,45,10,10,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,10,10,10,20,20,20,20,20,20,24,56,43,45,10,10,10,30,30,30,30,30,34,34,24,56,43,45,10,10,10,23,10,10,10,30,30,30,10,12,13,24,56,43,45,10,24,56,43,45,10,10,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,10,10,10,20,20,20,20,20,20,24,56,43,45,10,10,10,30,30,30,30,30,34,34,24,56,43,45,10,10,10,23,10,12,13,24,56,43,45,10,24,56,43,45,10,10,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,10,10,10,20,20,20,20,20,20,24,56,43,45,10,10,10,30,30,30,30,30,34,34,24,56,43,45,10,10,10,23,10,12,13,24,56,43,45,10,24,56,43,45,10,10,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,10,10,10,20,20,20,20,20,20,24,56,43,45,10,10,10,30,30,30,30,30,34,34,24,56,43,45,10,10,10,23,10,12,13,24,56,43,45,10,24,56,43,45,10,10,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,10,10,10,20,20,20,20,20,20,24,56,43,45,10,10,10,30,30,30,30,30,34,34,24,56,43,45,10,10,10,23,10,12,13,24,56,43,45,10,24,56,43,45,10,10,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,20,20,20,24,56,43,45,10,10,10,20,20,20,20,20,20,24,56,43,45,10,10,10,30,30,30,30,30,34,34,24,56,43,45,10,10,10,23,30,30,34,34,24,56,43,45,10,10,10,23,10,21,10,10,21,30,40,40,50,30,30,10,12,13,24,56,43,45,34,34,23,10,21,10,10,21,30,40,40,50,30,30,10,12,13,24,56,43,45,34,34,23,10,21,10,10,21,30,40,40,50,30,30,10,12,13,24,56,43,45,34,34,23,10,21,10,10,21,30,40,40,50,30,30,10,12,13,24,56,43,45,34,34,23,10,21,10,10,21,30,40,40,50,30,30,10,12,13,24,56,43,45,34,34,23,10,21,10,10,21,30,40,40,50,30,30,10,12,13,24,56,43,45,34,34,23,10,21,10,10,21,30,40,40,50,30,30,10,12,13,24,56,43,45,34,34,23,10,21,10,10,21,30,40,40,50,30,30,10,12,13,24,56,43,45,34,34,23,10,21,10,10,21,30,40,40,50,30,30,10,12,13,40,40,50,30,30,10,12,13,40,40,50,30,30,10,12,13,40,40,50,30,30,10,12,13,24,56,43,45,34,34,23,10,21,10,10,21,30,40,40,50,30,30};
         d.setMaxNumberOfDiscrepancies(20);
       // String p5="caaattgatgagcaatgcttttttataatgccaactttgtacaaaaaagcaggcttccagctgaccacc";
         //System.out.println(p5.length());
       //  d.setRefSequenceCdsStart(   p5.length() );
       
             ArrayList   m = d.run_analysis( res,  null,1188, 1203,l5.length() +seqr.length()-2);
         
      System.out.println("-------------------------"+queryFile);
        /*   queryFile ="C:\\BEC\\testdf.txt";
            NeedleParser.parse(queryFile,res);
               m = d.run_analysis( res,  null,1188, 1203,l5.length() +seqr.length()-2);
         
       System.out.println("-------------------------"+queryFile);*
           queryFile ="C:\\BEC\\testdf1.txt";
            NeedleParser.parse(queryFile,res);
               m = d.run_analysis( res,  null,1188, 1203,l5.length() +seqr.length()-2);
               m = DiscrepancyDescription.assembleDiscrepancyDefinitions(m);
       System.out.println("-------------------------"+queryFile);
          queryFile ="C:\\BEC\\testdf2.txt";
            NeedleParser.parse(queryFile,res);
               m = d.run_analysis( res,  null,1188, 1203,l5.length() +seqr.length()-2);
                m = DiscrepancyDescription.assembleDiscrepancyDefinitions(m);
       System.out.println("-------------------------"+queryFile);
       /*
           queryFile ="C:\\BEC\\testdf3.txt";
            NeedleParser.parse(queryFile,res);
               m = d.run_analysis( res,  null,1188, 1203,l5.length() +seqr.length()-2);
       System.out.println("-------------------------"+queryFile);
           queryFile ="C:\\BEC\\testdf4.txt";
            NeedleParser.parse(queryFile,res);
               m = d.run_analysis( res,  null,1188, 1203,l5.length() +seqr.length()-2);
       System.out.println("-------------------------"+queryFile);
           queryFile ="C:\\BEC\\testdf5.txt";
            NeedleParser.parse(queryFile,res);
               m = d.run_analysis( res,  null,1188, 1203,l5.length() +seqr.length()-2);
       System.out.println("-------------------------"+queryFile);
           queryFile ="C:\\BEC\\testdf6.txt";
            NeedleParser.parse(queryFile,res);
               m = d.run_analysis( res,  null,1188, 1203,l5.length() +seqr.length()-2);
       System.out.println("-------------------------"+queryFile);*/
         DiscrepancyFinder df = new DiscrepancyFinder();
        df.setNeedleGapOpen(20.0);
        df.setNeedleGapExt(0.05);
        df.setIdentityCutoff(60.0);
        df.setMaxNumberOfDiscrepancies(20);
        df.setInputType(true);     
        
       
        String linker5_seq = "caaattgatgagcaatgcttttttataatgccaactttgtacaaaaaagcaggcttccagctgaccacc";
        String linker3_seq ="catggcaattcccggggatacccagctttcttgtacaaagttggcattataagaaagcattgcttatcaatttgttgc";
      Construct construct=new Construct(1480);
        BaseSequence construct_refseqence = construct.getRefSequenceForAnalysis( 
                                           "ATG","GGA", "TAA");
            
            
            
            int cdsstart = linker5_seq.length() ;
            int cdsstop =  linker5_seq.length() + construct_refseqence.getText().length() ;
           //create refsequence for analysis
              df.setRefSequenceCdsStart( cdsstart);
        df.setRefSequenceCdsStop( cdsstop );
            BaseSequence refsequence = new BaseSequence();
            refsequence.setId( construct.getRefSeqId());
           
            refsequence.setText( linker5_seq.toLowerCase()+ construct_refseqence.getText().toUpperCase() + linker3_seq.toLowerCase() );
     
        CloneSequence clonesequence = new CloneSequence(15295);
        df.setSequencePair(new SequencePair(clonesequence ,  refsequence));
        df.run();

     
        for(int d=0;d<clonesequence.getDiscrepancies().size();d++)
        {
            Mutation mut = (Mutation)clonesequence.getDiscrepancies().get(d);
            System.out.println(mut.toStringSeparateType() );
        }    

            
            /*
        DiscrepancyFinder df = new DiscrepancyFinder();
        df.setNeedleGapOpen(20.0);
        df.setNeedleGapExt(0.05);
        df.setIdentityCutoff(60.0);
        df.setMaxNumberOfDiscrepancies(20);
        df.setInputType(true);     
        
       
        String linker5_seq = "CCAACTTTGTACAAAAAAGCAGGCTCCGAAGGAGATACC";
        String linker3_seq ="GACCCAGCTTTCTTGTACAAAGTTGG";
          df.setRefSequenceCdsStart( linker5_seq.length());
        df.setRefSequenceCdsStop(  linker5_seq.length()+str.length() );
        str=linker5_seq.toLowerCase()+ str.toUpperCase() + linker3_seq.toLowerCase() ;
        BaseSequence refseq = new BaseSequence(123);
        CloneSequence clonesequence = new CloneSequence(1 );
        
        df.setSequencePair(new SequencePair(clonesequence ,  refseq));
        df.run();

     
        for(int d=0;d<clonesequence.getDiscrepancies().size();d++)
        {
            Mutation mut = (Mutation)clonesequence.getDiscrepancies().get(d);
            System.out.println(mut.toStringSeparateType() );
        }
/*         
clstr="CCAACTTTGTNCAAAAAAGCAGGCTCCGAAGGAGATACCATGAAACTTCGTCACCTGCCCCTCATCGCTGCCATCGGCCTGTTCTCCACCGTCACCCTGGCCGCCGGCTATACCGGCCCGGGCGCTACCCCGACCACCACCACGGTGAAGGCCGCGCTGGAAGCCGCCGACGACACCCCGGTGGTCCTCCAGGGCACCATCGTCAAGCGCATCAAGGGCGACATCTACGAGTTCCGCGATGCCACCGGCAGCATGAAGGTGGAGATCGACGACGAAGACTTCCCGCCGATGGAAATCAACGACAAGACCCGGGTCAAGCTGACCGGCGAAGTCGACCGCGACCTGGTCGGCCGCGAGATCGACGTCGAGTTCGTCGAAGTGATCAAGTAANACCCAGCTTTCTTGTACNAAGTTGG";
clonesequence.setText(clstr);  
clonesequence.setId(124);
df.setSequencePair(new SequencePair(clonesequence ,  refseq));
        df.run();

System.out.println("5 / 3 linker n subst" );
        for(int d=0;d<clonesequence.getDiscrepancies().size();d++)
        {
            Mutation mut = (Mutation)clonesequence.getDiscrepancies().get(d);
            System.out.println(mut.toStringSeparateType() );
        }
        
        
        
clstr="CCAACTTNTGTACAAAAAAGCAGGCTCCGAAGGAGATACCATGAAACTTCGTCACCTGCCCCTCATCGCTGCCATCGGCCTGTTCTCCACCGTCACCCTGGCCGCCGGCTATACCGGCCCGGGCGCTACCCCGACCACCACCACGGTGAAGGCCGCGCTGGAAGCCGCCGACGACACCCCGGTGGTCCTCCAGGGCACCATCGTCAAGCGCATCAAGGGCGACATCTACGAGTTCCGCGATGCCACCGGCAGCATGAAGGTGGAGATCGACGACGAAGACTTCCCGCCGATGGAAATCAACGACAAGACCCGGGTCAAGCTGACCGGCGAAGTCGACCGCGACCTGGTCGGCCGCGAGATCGACGTCGAGTTCGTCGAAGTGATCAAGTAAGNCCCAGCTNTTCTTGTACAAAGTTGG";
clonesequence.setText(clstr);  
clonesequence.setId(124);
df.setSequencePair(new SequencePair(clonesequence ,  refseq));
        df.run();

System.out.println("5 /3 insertion" );
        for(int d=0;d<clonesequence.getDiscrepancies().size();d++)
        {
            Mutation mut = (Mutation)clonesequence.getDiscrepancies().get(d);
            System.out.println(mut.toStringSeparateType() );
        }
        
        
        

clstr="CCNNCTTTGTACAAANNNAAAGCAGGCTCCGAAGGAGATACCATGAAACTTCGTCACCTGCCCCTCATCGCTGCCATCGGCCTGTTCTCCACCGTCACCCTGGCCGCCGGCTATACCGGCCCGGGCGCTACCCCGACCACCACCACGGTGAAGGCCGCGCTGGAAGCCGCCGACGACACCCCGGTGGTCCTCCAGGGCACCATCGTCAAGCGCATCAAGGGCGACATCTACGAGTTCCGCGATGCCACCGGCAGCATGAAGGTGGAGATCGACGACGAAGACTTCCCGCCGATGGAAATCAACGACAAGACCCGGGTCAAGCTGACCGGCGAAGTCGACCGCGACCTGGTCGGCCGCGAGATCGACGTCGAGTTCGTCGAAGTGATCAAGTAANACCCAGCNNTTTCTTGTACAAANNNGTTGG";
clonesequence.setText(clstr);  
clonesequence.setId(124);
df.setSequencePair(new SequencePair(clonesequence ,  refseq));
        df.run();

System.out.println("5/3 multipal ins and subst" );
        for(int d=0;d<clonesequence.getDiscrepancies().size();d++)
        {
            Mutation mut = (Mutation)clonesequence.getDiscrepancies().get(d);
            System.out.println(mut.toStringSeparateType() );
        }
        
      
        

clstr="CCAACTTTGTACAAAAAAGCAGGCTCCGAAGGAGATACCNTGAAANTTCNTCACCTGCCCCTCATCGCTGCCATCGGCCTGTTCTCCACCGTCACCCTGGCCGCCGGCTATACCGGCCCGGGCGCTACCCCGACCACCACCACGGTGAAGGCCGCGCTGGAAGCCGCCGACGACACCCCGGTGGTCCTCCAGGGCACCATCGTCAAGCGCATCAAGGGCGACATCTACGAGTTCCGCGATGCCACCGGCAGCATGAAGGTGGAGATCGACGACGAAGACTTCCCGCCGATGGAAATCAACGACAAGACCCGGGTCAAGCTGACCGGCGAAGTCGACCGCGACCTGGTCGGCCGCGAGATCGACGTCGAGTTCGTCGAAGTGATCAAGNNAGACCCAGCTTTCTTGTACAAAGTTGG";
clonesequence.setText(clstr);  
clonesequence.setId(124);
df.setSequencePair(new SequencePair(clonesequence ,  refseq));
        df.run();

System.out.println("Gene start / stop / gene n subst simple" );
        for(int d=0;d<clonesequence.getDiscrepancies().size();d++)
        {
            Mutation mut = (Mutation)clonesequence.getDiscrepancies().get(d);
            System.out.println(mut.toStringSeparateType() );
        }

   


clstr="CCAACTTTGTACAAAAAAGCAGGCTCCGAAGGAGATACCNCGAAANTTCNCCACNNGCCCCTCATCGCTGCCATCGGCCTGTTCTCCACCGTCACCCTGGCCGCCGGCTATACCGGCCCGGGCGCTACCCCGACCACCACCACGGTGAAGGCCGCGCTGGAAGCCGCCGACGACACCCCGGTGGTCCTCCAGGGCACCATCGTCAAGCGCATCAAGGGCGACATCTACGAGTTCCGCGATGCCACCGGCAGCATGAAGGTGGAGATCGACGACGAAGACTTCCCGCCGATGGAAATCAACGACAAGACCCGGGTCAAGCTGACCGGCGAAGTCGACCGCGACCTGGTCGGCCGCGAGATCGACGTCGAGTTCGTCGAAGTGATCAAGANAGACCCAGCTTTCTTGTACAAAGTTGG";
clonesequence.setText(clstr);  
clonesequence.setId(124);
df.setSequencePair(new SequencePair(clonesequence ,  refseq));
        df.run();

System.out.println("Gene start / stop / gene n subst doubl" );
        for(int d=0;d<clonesequence.getDiscrepancies().size();d++)
        {
            Mutation mut = (Mutation)clonesequence.getDiscrepancies().get(d);
            System.out.println(mut.toStringSeparateType() );
        }


 clstr="CCAACTTTGTACAAAAAAGCAGGCTCCGAAGGAGATACCATNGAAACTTCGTCACCTGCCCCTCNNNATCGCTGCCATCGGNCTGTTCTCCACCGTCACCCTGGCCGCCGGCTATACCGGCCCGGGCGCTACCCCGACCACCACCACGGTGAAGGCCGCGCTGGAAGCCGCCGACGACACCCCGGTGGTCCTCCAGGGCACCATCGTCAAGCGCATCAAGGGCGACATCTACGAGTTCCGCGATGCCACCGGCAGCATGAAGNGTGGAGATCGACGACGAAGACTTCCCGCCGATGGAAATCAACGACAAGACCCGGGTCAAGCTGACCGGCGAAGTCGACCGCGACCTGGTCGGCCGCGAGATCGACGTCGAGTTCGTCGAAGTGATCAAGTNAAGACCCAGCTTTCTTGTACAAAGTTGG";
clonesequence.setText(clstr);  
clonesequence.setId(124);
df.setSequencePair(new SequencePair(clonesequence ,  refseq));
        df.run();

System.out.println("Gene start / stop / gene n subst doubl and insert" );
        for(int d=0;d<clonesequence.getDiscrepancies().size();d++)
        {
            Mutation mut = (Mutation)clonesequence.getDiscrepancies().get(d);
            System.out.println(mut.toStringSeparateType() );
        }
        
        //5' & 3' substitutions
clstr="CCAACCTTGTACACCAAAGCAGGCTCCGAAGGAGATACCATGAAACTTCGTCACCTGCCCCTCATCGCTGCCATCGGCCTGTTCTCCACCGTCACCCTGGCCGCCGGCTATACCGGCCCGGGCGCTACCCCGACCACCACCACGGTGAAGGCCGCGCTGGAAGCCGCCGACGACACCCCGGTGGTCCTCCAGGGCACCATCGTCAAGCGCATCAAGGGCGACATCTACGAGTTCCGCGATGCCACCGGCAGCATGAAGGTGGAGATCGACGACGAAGACTTCCCGCCGATGGAAATCAACGACAAGACCCGGGTCAAGCTGACCGGCGAAGTCGACCGCGACCTGGTCGGCCGCGAGATCGACGTCGAGTTCGTCGAAGTGATCAAGTAAGACCCAGGTTTCTTCCACAAAGTTGG";
clonesequence.setText(clstr);  
clonesequence.setId(124);
df.setSequencePair(new SequencePair(clonesequence ,  refseq));
        df.run();
        System.out.println("5' & 3' substitutions" );
        for(int d=0;d<clonesequence.getDiscrepancies().size();d++)
        {
            Mutation mut = (Mutation)clonesequence.getDiscrepancies().get(d);
            System.out.println(mut.toStringSeparateType() );
        }
      

//5' & 3' deletions insertions

clstr="CCAACTTTGTACAAAAGCAGGCAATCCGAAGGAATACCATGAAACTTCGTCACCTGCCCCTCATCGCTGCCATCGGCCTGTTCTCCACCGTCACCCTGGCCGCCGGCTATACCGGCCCGGGCGCTACCCCGACCACCACCACGGTGAAGGCCGCGCTGGAAGCCGCCGACGACACCCCGGTGGTCCTCCAGGGCACCATCGTCAAGCGCATCAAGGGCGACATCTACGAGTTCCGCGATGCCACCGGCAGCATGAAGGTGGAGATCGACGACGAAGACTTCCCGCCGATGGAAATCAACGACAAGACCCGGGTCAAGCTGACCGGCGAAGTCGACCGCGACCTGGTCGGCCGCGAGATCGACGTCGAGTTCGTCGAAGTGATCAAGTAAGACAGCTTTCTGTACAAAGTTTGG";
clonesequence.setText(clstr);  
df.setSequencePair(new SequencePair(clonesequence ,  refseq));
        df.run();
        System.out.println("5' & 3' delet" );
        for(int d=0;d<clonesequence.getDiscrepancies().size();d++)
        {
            Mutation mut = (Mutation)clonesequence.getDiscrepancies().get(d);
            System.out.println(mut.toStringSeparateType() );
        }

//5' into dene deletion
clstr="CCAACTTTGTACAAAAAAGCAGGCTCCGAAGGAGATACTGAAACTTCGTCACCTGCCCCTCATCGCTGCCATCGGCCTGTTCTCCACCGTCACCCTGGCCGCCGGCTATACCGGCCCGGGCGCTACCCCGACCACCACCACGGTGAAGGCCGCGCTGGAAGCCGCCGACGACACCCCGGTGGTCCTCCAGGGCACCATCGTCAAGCGCATCAAGGGCGACATCTACGAGTTCCGCGATGCCACCGGCAGCATGAAGGTGGAGATCGACGACGAAGACTTCCCGCCGATGGAAATCAACGACAAGACCCGGGTCAAGCTGACCGGCGAAGTCGACCGCGACCTGGTCGGCCGCGAGATCGACGTCGAGTTCGTCGAAGTGATCAAGTAAGACCCAGCTTTCTTGTACAAAGTTGG";

clonesequence.setText(clstr);  
df.setSequencePair(new SequencePair(clonesequence ,  refseq));
        df.run();
        System.out.println("5' into gene" );
        for(int d=0;d<clonesequence.getDiscrepancies().size();d++)
        {
            Mutation mut = (Mutation)clonesequence.getDiscrepancies().get(d);
            System.out.println(mut.toStringSeparateType() );
        }

//discr in last base

clstr="CCAACTTTGTACAAAAAAGCAGGCTCCGAAGGAGATACTATGAAACTTCGTCACCTGCCCCTCATCGCTGCCATCGGCCTGTTCTCCACCGTCACCCTGGCCGCCGGCTATACCGGCCCGGGCGCTACCCCGACCACCACCACGGTGAAGGCCGCGCTGGAAGCCGCCGACGACACCCCGGTGGTCCTCCAGGGCACCATCGTCAAGCGCATCAAGGGCGACATCTACGAGTTCCGCGATGCCACCGGCAGCATGAAGGTGGAGATCGACGACGAAGACTTCCCGCCGATGGAAATCAACGACAAGACCCGGGTCAAGCTGACCGGCGAAGTCGACCGCGACCTGGTCGGCCGCGAGATCGACGTCGAGTTCGTCGAAGTGATCAAGTAAGACCCAGCTTTCTTGTACAAAGTTGG";

clonesequence.setText(clstr);  
df.setSequencePair(new SequencePair(clonesequence ,  refseq));
        df.run();
        System.out.println("last base" );
        for(int d=0;d<clonesequence.getDiscrepancies().size();d++)
        {
            Mutation mut = (Mutation)clonesequence.getDiscrepancies().get(d);
            System.out.println(mut.toStringSeparateType() );
        }


//deletion from stop codon into linker

clstr="CCAACTTTGTACAAAAAAGCAGGCTCCGAAGGAGATACCATGAAACTTCGTCACCTGCCCCTCATCGCTGCCATCGGCCTGTTCTCCACCGTCACCCTGGCCGCCGGCTATACCGGCCCGGGCGCTACCCCGACCACCACCACGGTGAAGGCCGCGCTGGAAGCCGCCGACGACACCCCGGTGGTCCTCCAGGGCACCATCGTCAAGCGCATCAAGGGCGACATCTACGAGTTCCGCGATGCCACCGGCAGCATGAAGGTGGAGATCGACGACGAAGACTTCCCGCCGATGGAAATCAACGACAAGACCCGGGTCAAGCTGACCGGCGAAGTCGACCGCGACCTGGTCGGCCGCGAGATCGACGTCGAGTTCGTCGAAGTGATCAAGTACCCAGCTTTCTTGTACAAAGTTGG";
clonesequence.setText(clstr);  
df.setSequencePair(new SequencePair(clonesequence ,  refseq));
        df.run();
        System.out.println("stop into linker" );
        for(int d=0;d<clonesequence.getDiscrepancies().size();d++)
        {
            Mutation mut = (Mutation)clonesequence.getDiscrepancies().get(d);
            System.out.println(mut.toStringSeparateType() );
        }

//deletion across n last codons and int linker

clstr="CCAACTTTGTACAAAAAAGCAGGCTCCGAAGGAGATACCATGAAACTTCGTCACCTGCCCCTCATCGCTGCCATCGGCCTGTTCTCCACCGTCACCCTGGCCGCCGGCTATACCGGCCCGGGCGCTACCCCGACCACCACCACGGTGAAGGCCGCGCTGGAAGCCGCCGACGACACCCCGGTGGTCCTCCAGGGCACCATCGTCAAGCGCATCAAGGGCGACATCTACGAGTTCCGCGATGCCACCGGCAGCATGAAGGTGGAGATCGACGACGAAGACTTCCCGCCGATGGAAATCAACGACAAGACCCGGGTCAAGCTGACCGGCGAAGTCGACCGCGACCTGGTCGGCCGCGAGATCGACGTCGAGTTCGTCGAAGTGATCCCCAGCTTTCTTGTACAAAGTTGG";
clonesequence.setText(clstr);  
df.setSequencePair(new SequencePair(clonesequence ,  refseq));
        df.run();
        System.out.println("n last codons" );
        for(int d=0;d<clonesequence.getDiscrepancies().size();d++)
        {
            Mutation mut = (Mutation)clonesequence.getDiscrepancies().get(d);
            System.out.println(mut.toStringSeparateType() );
        }

//discrepancy in the first base of st 3' linker
clstr="CCAACTTTGTACAAAAAAGCAGGCTCCGAAGGAGATACCATGAAACTTCGTCACCTGCCCCTCATCGCTGCCATCGGCCTGTTCTCCACCGTCACCCTGGCCGCCGGCTATACCGGCCCGGGCGCTACCCCGACCACCACCACGGTGAAGGCCGCGCTGGAAGCCGCCGACGACACCCCGGTGGTCCTCCAGGGCACCATCGTCAAGCGCATCAAGGGCGACATCTACGAGTTCCGCGATGCCACCGGCAGCATGAAGGTGGAGATCGACGACGAAGACTTCCCGCCGATGGAAATCAACGACAAGACCCGGGTCAAGCTGACCGGCGAAGTCGACCGCGACCTGGTCGGCCGCGAGATCGACGTCGAGTTCGTCGAAGTGATCAAGTAAAACCCAGCTTTCTTGTACAAAGTTGG";
clonesequence.setText(clstr);  
df.setSequencePair(new SequencePair(clonesequence ,  refseq));
        df.run();
        System.out.println("first 3'" );
        for(int d=0;d<clonesequence.getDiscrepancies().size();d++)
        {
            Mutation mut = (Mutation)clonesequence.getDiscrepancies().get(d);
            System.out.println(mut.toStringSeparateType() );
        }
      System.out.println("1	Start codon substitution");
clstr="CCAACTTTGTACAAAAAAGCAGGCTCCGAAGGAGATACCAAGAAACTTCGTCACCTGCCCCTCATCGCTGCCATCGGCCTGTTCTCCACCGTCACCCTGGCCGCCGGCTATACCGGCCCGGGCGCTACCCCGACCACCACCACGGTGAAGGCCGCGCTGGAAGCCGCCGACGACACCCCGGTGGTCCTCCAGGGCACCATCGTCAAGCGCATCAAGGGCGACATCTACGAGTTCCGCGATGCCACCGGCAGCATGAAGGTGGAGATCGACGACGAAGACTTCCCGCCGATGGAAATCAACGACAAGACCCGGGTCAAGCTGACCGGCGAAGTCGACCGCGACCTGGTCGGCCGCGAGATCGACGTCGAGTTCGTCGAAGTGATCAAGTAAGACCCAGCTTTCTTGTACAAAGTTGG";
clonesequence.setText(clstr);  
df.setSequencePair(new SequencePair(clonesequence ,  refseq));
df.run();
 for(int d=0;d<clonesequence.getDiscrepancies().size();d++)
	{
	    Mutation mut = (Mutation)clonesequence.getDiscrepancies().get(d);
	    System.out.println(mut.toStringSeparateType() );
}

System.out.println("2	Start codon deletion");
clstr="CCAACTTTGTACAAAAAAGCAGGCTCCGAAGGAGATACCAGAAACTTCGTCACCTGCCCCTCATCGCTGCCATCGGCCTGTTCTCCACCGTCACCCTGGCCGCCGGCTATACCGGCCCGGGCGCTACCCCGACCACCACCACGGTGAAGGCCGCGCTGGAAGCCGCCGACGACACCCCGGTGGTCCTCCAGGGCACCATCGTCAAGCGCATCAAGGGCGACATCTACGAGTTCCGCGATGCCACCGGCAGCATGAAGGTGGAGATCGACGACGAAGACTTCCCGCCGATGGAAATCAACGACAAGACCCGGGTCAAGCTGACCGGCGAAGTCGACCGCGACCTGGTCGGCCGCGAGATCGACGTCGAGTTCGTCGAAGTGATCAAGTAAGACCCAGCTTTCTTGTACAAAGTTGG";
clonesequence.setText(clstr);  
df.setSequencePair(new SequencePair(clonesequence ,  refseq));
df.run();
 for(int d=0;d<clonesequence.getDiscrepancies().size();d++)
	{
	    Mutation mut = (Mutation)clonesequence.getDiscrepancies().get(d);
	    System.out.println(mut.toStringSeparateType() );
}

System.out.println("3	Start codon + deletion");
clstr="CCAACTTTGTACAAAAAAGCAGGCTCCGAAGGAGATACCAAACTTCGTCACCTGCCCCTCATCGCTGCCATCGGCCTGTTCTCCACCGTCACCCTGGCCGCCGGCTATACCGGCCCGGGCGCTACCCCGACCACCACCACGGTGAAGGCCGCGCTGGAAGCCGCCGACGACACCCCGGTGGTCCTCCAGGGCACCATCGTCAAGCGCATCAAGGGCGACATCTACGAGTTCCGCGATGCCACCGGCAGCATGAAGGTGGAGATCGACGACGAAGACTTCCCGCCGATGGAAATCAACGACAAGACCCGGGTCAAGCTGACCGGCGAAGTCGACCGCGACCTGGTCGGCCGCGAGATCGACGTCGAGTTCGTCGAAGTGATCAAGTAAGACCCAGCTTTCTTGTACAAAGTTGG";
clonesequence.setText(clstr);  
df.setSequencePair(new SequencePair(clonesequence ,  refseq));
df.run();
 for(int d=0;d<clonesequence.getDiscrepancies().size();d++)
	{
	    Mutation mut = (Mutation)clonesequence.getDiscrepancies().get(d);
	    System.out.println(mut.toStringSeparateType() );
}

System.out.println("4	Start codon insertion");
clstr="CCAACTTTGTACAAAAAAGCAGGCTCCGAAGGAGATACCATAGAAACTTCGTCACCTGCCCCTCATCGCTGCCATCGGCCTGTTCTCCACCGTCACCCTGGCCGCCGGCTATACCGGCCCGGGCGCTACCCCGACCACCACCACGGTGAAGGCCGCGCTGGAAGCCGCCGACGACACCCCGGTGGTCCTCCAGGGCACCATCGTCAAGCGCATCAAGGGCGACATCTACGAGTTCCGCGATGCCACCGGCAGCATGAAGGTGGAGATCGACGACGAAGACTTCCCGCCGATGGAAATCAACGACAAGACCCGGGTCAAGCTGACCGGCGAAGTCGACCGCGACCTGGTCGGCCGCGAGATCGACGTCGAGTTCGTCGAAGTGATCAAGTAAGACCCAGCTTTCTTGTACAAAGTTGG";
clonesequence.setText(clstr);  
clonesequence.setId(111);  
df.setSequencePair(new SequencePair(clonesequence ,  refseq));
df.run();
 for(int d=0;d<clonesequence.getDiscrepancies().size();d++)
	{
	    Mutation mut = (Mutation)clonesequence.getDiscrepancies().get(d);
	    System.out.println(mut.toStringSeparateType() );
}
System.out.println("4	Start codon deletion");
clstr="CCAACTTTGTACAAAAAAGCAGGCTCCGAAGGAGATACCAGAAACTTCGTCACCTGCCCCTCATCGCTGCCATCGGCCTGTTCTCCACCGTCACCCTGGCCGCCGGCTATACCGGCCCGGGCGCTACCCCGACCACCACCACGGTGAAGGCCGCGCTGGAAGCCGCCGACGACACCCCGGTGGTCCTCCAGGGCACCATCGTCAAGCGCATCAAGGGCGACATCTACGAGTTCCGCGATGCCACCGGCAGCATGAAGGTGGAGATCGACGACGAAGACTTCCCGCCGATGGAAATCAACGACAAGACCCGGGTCAAGCTGACCGGCGAAGTCGACCGCGACCTGGTCGGCCGCGAGATCGACGTCGAGTTCGTCGAAGTGATCAAGTAAGACCCAGCTTTCTTGTACAAAGTTGG";
clonesequence.setText(clstr);  
clonesequence.setId(111);  
df.setSequencePair(new SequencePair(clonesequence ,  refseq));
df.run();
 for(int d=0;d<clonesequence.getDiscrepancies().size();d++)
	{
	    Mutation mut = (Mutation)clonesequence.getDiscrepancies().get(d);
	    System.out.println(mut.toStringSeparateType() );
}

System.out.println("5	Stop codon substitution into different stop codon");
clstr="CCAACTTTGTACAAAAAAGCAGGCTCCGAAGGAGATACCATGAAACTTCGTCACCTGCCCCTCATCGCTGCCATCGGCCTGTTCTCCACCGTCACCCTGGCCGCCGGCTATACCGGCCCGGGCGCTACCCCGACCACCACCACGGTGAAGGCCGCGCTGGAAGCCGCCGACGACACCCCGGTGGTCCTCCAGGGCACCATCGTCAAGCGCATCAAGGGCGACATCTACGAGTTCCGCGATGCCACCGGCAGCATGAAGGTGGAGATCGACGACGAAGACTTCCCGCCGATGGAAATCAACGACAAGACCCGGGTCAAGCTGACCGGCGAAGTCGACCGCGACCTGGTCGGCCGCGAGATCGACGTCGAGTTCGTCGAAGTGATCAAGTGAGACCCAGCTTTCTTGTACAAAGTTGG";
clonesequence.setText(clstr);  
df.setSequencePair(new SequencePair(clonesequence ,  refseq));
df.run();
 for(int d=0;d<clonesequence.getDiscrepancies().size();d++)
	{
	    Mutation mut = (Mutation)clonesequence.getDiscrepancies().get(d);
	    System.out.println(mut.toStringSeparateType() );
}


System.out.println("6	Stop codon deletion");
clstr="CCAACTTTGTACAAAAAAGCAGGCTCCGAAGGAGATACCATGAAACTTCGTCACCTGCCCCTCATCGCTGCCATCGGCCTGTTCTCCACCGTCACCCTGGCCGCCGGCTATACCGGCCCGGGCGCTACCCCGACCACCACCACGGTGAAGGCCGCGCTGGAAGCCGCCGACGACACCCCGGTGGTCCTCCAGGGCACCATCGTCAAGCGCATCAAGGGCGACATCTACGAGTTCCGCGATGCCACCGGCAGCATGAAGGTGGAGATCGACGACGAAGACTTCCCGCCGATGGAAATCAACGACAAGACCCGGGTCAAGCTGACCGGCGAAGTCGACCGCGACCTGGTCGGCCGCGAGATCGACGTCGAGTTCGTCGAAGTGATCAAGTAGACCCAGCTTTCTTGTACAAAGTTGG";
clonesequence.setText(clstr);  
df.setSequencePair(new SequencePair(clonesequence ,  refseq));
df.run();
 for(int d=0;d<clonesequence.getDiscrepancies().size();d++)
	{
	    Mutation mut = (Mutation)clonesequence.getDiscrepancies().get(d);
	    System.out.println(mut.toStringSeparateType() );
}

System.out.println("7	Stop codon substitution into not stop codon");
clstr="CCAACTTTGTACAAAAAAGCAGGCTCCGAAGGAGATACCATGAAACTTCGTCACCTGCCCCTCATCGCTGCCATCGGCCTGTTCTCCACCGTCACCCTGGCCGCCGGCTATACCGGCCCGGGCGCTACCCCGACCACCACCACGGTGAAGGCCGCGCTGGAAGCCGCCGACGACACCCCGGTGGTCCTCCAGGGCACCATCGTCAAGCGCATCAAGGGCGACATCTACGAGTTCCGCGATGCCACCGGCAGCATGAAGGTGGAGATCGACGACGAAGACTTCCCGCCGATGGAAATCAACGACAAGACCCGGGTCAAGCTGACCGGCGAAGTCGACCGCGACCTGGTCGGCCGCGAGATCGACGTCGAGTTCGTCGAAGTGATCAAGTCAGACCCAGCTTTCTTGTACAAAGTTGG";
clonesequence.setText(clstr);  
df.setSequencePair(new SequencePair(clonesequence ,  refseq));
df.run();
 for(int d=0;d<clonesequence.getDiscrepancies().size();d++)
	{
	    Mutation mut = (Mutation)clonesequence.getDiscrepancies().get(d);
	    System.out.println(mut.toStringSeparateType() );
}

System.out.println("8	Stop codon insertion");
clstr="CCAACTTTGTACAAAAAAGCAGGCTCCGAAGGAGATACCATGAAACTTCGTCACCTGCCCCTCATCGCTGCCATCGGCCTGTTCTCCACCGTCACCCTGGCCGCCGGCTATACCGGCCCGGGCGCTACCCCGACCACCACCACGGTGAAGGCCGCGCTGGAAGCCGCCGACGACACCCCGGTGGTCCTCCAGGGCACCATCGTCAAGCGCATCAAGGGCGACATCTACGAGTTCCGCGATGCCACCGGCAGCATGAAGGTGGAGATCGACGACGAAGACTTCCCGCCGATGGAAATCAACGACAAGACCCGGGTCAAGCTGACCGGCGAAGTCGACCGCGACCTGGTCGGCCGCGAGATCGACGTCGAGTTCGTCGAAGTGATCAAGTAAAGACCCAGCTTTCTTGTACAAAGTTGG";
clonesequence.setText(clstr);  
df.setSequencePair(new SequencePair(clonesequence ,  refseq));
df.run();
 for(int d=0;d<clonesequence.getDiscrepancies().size();d++)
	{
	    Mutation mut = (Mutation)clonesequence.getDiscrepancies().get(d);
	    System.out.println(mut.toStringSeparateType() );
}

System.out.println("9	Substitution");
clstr="CCAACTTTGTACAAAAAAGCAGGCTCCGAAGGAGATACCATGCAAGTGCGTCACAAAACCCTCATCGCTGCCATCGGCCTGTTCTCCACCGTCACCCTGGCCGCCGGCTATACCGGCCCGGGCGCTACCCCGACCACCACCACGGTGAAGGCCGCGCTGGAAGCCGCCGACGACACCCCGGTGGTCCTCCAGGGCACCATCGTCAAGCGCATCAAGGGCGACATCTACGAGTTCCGCGATGCCACCGGCAGCATGAAGGTGGAGATCGACGACGAAGACTTCCCGCCGATGGAAATCAACGACAAGACCCGGGTCAAGCTGACCGGCGAAGTCGACCGCGACCTGGTCGGCCGCGAGATCGACGTCGAGTTCGTCGAAGTGATCAAGTAAGACCCAGCTTTCTTGTACAAAGTTGG";
clonesequence.setText(clstr);  
df.setSequencePair(new SequencePair(clonesequence ,  refseq));
df.run();
 for(int d=0;d<clonesequence.getDiscrepancies().size();d++)
	{
	    Mutation mut = (Mutation)clonesequence.getDiscrepancies().get(d);
	    System.out.println(mut.toStringSeparateType() );
}

System.out.println("10	Inframe insertion (first codon base start)");
clstr="CCAACTTTGTACAAAAAAGCAGGCTCCGAAGGAGATACCATGAAACAAATTCGTCACCTGCCCCTCATCGCTGCCATCGGCCTGTTCTCCACCGTCACCCTGGCCGCCGGCTATACCGGCCCGGGCGCTACCCCGACCACCACCACGGTGAAGGCCGCGCTGGAAGCCGCCGACGACACCCCGGTGGTCCTCCAGGGCACCATCGTCAAGCGCATCAAGGGCGACATCTACGAGTTCCGCGATGCCACCGGCAGCATGAAGGTGGAGATCGACGACGAAGACTTCCCGCCGATGGAAATCAACGACAAGACCCGGGTCAAGCTGACCGGCGAAGTCGACCGCGACCTGGTCGGCCGCGAGATCGACGTCGAGTTCGTCGAAGTGATCAAGTAAGACCCAGCTTTCTTGTACAAAGTTGG";
clonesequence.setText(clstr);  
df.setSequencePair(new SequencePair(clonesequence ,  refseq));
df.run();
 for(int d=0;d<clonesequence.getDiscrepancies().size();d++)
	{
	    Mutation mut = (Mutation)clonesequence.getDiscrepancies().get(d);
	    System.out.println(mut.toStringSeparateType() );
}

System.out.println("11	Inframe deletion(first codon base start)");
clstr="CCAACTTTGTACAAAAAAGCAGGCTCCGAAGGAGATACCATGAAACGTCACCTGCCCCTCATCGCTGCCATCGGCCTGTTCTCCACCGTCACCCTGGCCGCCGGCTATACCGGCCCGGGCGCTACCCCGACCACCACCACGGTGAAGGCCGCGCTGGAAGCCGCCGACGACACCCCGGTGGTCCTCCAGGGCACCATCGTCAAGCGCATCAAGGGCGACATCTACGAGTTCCGCGATGCCACCGGCAGCATGAAGGTGGAGATCGACGACGAAGACTTCCCGCCGATGGAAATCAACGACAAGACCCGGGTCAAGCTGACCGGCGAAGTCGACCGCGACCTGGTCGGCCGCGAGATCGACGTCGAGTTCGTCGAAGTGATCAAGTAAGACCCAGCTTTCTTGTACAAAGTTGG";
clonesequence.setText(clstr);  
df.setSequencePair(new SequencePair(clonesequence ,  refseq));
df.run();
 for(int d=0;d<clonesequence.getDiscrepancies().size();d++)
	{
	    Mutation mut = (Mutation)clonesequence.getDiscrepancies().get(d);
	    System.out.println(mut.toStringSeparateType() );
}

System.out.println("12	Inframe insertion (second codon base start)");
clstr="CCAACTTTGTACAAAAAAGCAGGCTCCGAAGGAGATACCATGAAACTAAAAAATCGTCACCTGCCCCTCATCGCTGCCATCGGCCTGTTCTCCACCGTCACCCTGGCCGCCGGCTATACCGGCCCGGGCGCTACCCCGACCACCACCACGGTGAAGGCCGCGCTGGAAGCCGCCGACGACACCCCGGTGGTCCTCCAGGGCACCATCGTCAAGCGCATCAAGGGCGACATCTACGAGTTCCGCGATGCCACCGGCAGCATGAAGGTGGAGATCGACGACGAAGACTTCCCGCCGATGGAAATCAACGACAAGACCCGGGTCAAGCTGACCGGCGAAGTCGACCGCGACCTGGTCGGCCGCGAGATCGACGTCGAGTTCGTCGAAGTGATCAAGTAAGACCCAGCTTTCTTGTACAAAGTTGG";
clonesequence.setText(clstr);  
df.setSequencePair(new SequencePair(clonesequence ,  refseq));
df.run();
 for(int d=0;d<clonesequence.getDiscrepancies().size();d++)
	{
	    Mutation mut = (Mutation)clonesequence.getDiscrepancies().get(d);
	    System.out.println(mut.toStringSeparateType() );
}

/*
System.out.println("13	Inframe deletion (second codon base start)");
clstr="CCAACTTTGTACAAAAAAGCAGGCTCCGAAGGAGATACCATGAAACACCTGCCCCTCATCGCTGCCATCGGCCTGTTCTCCACCGTCACCCTGGCCGCCGGCTATACCGGCCCGGGCGCTACCCCGACCACCACCACGGTGAAGGCCGCGCTGGAAGCCGCCGACGACACCCCGGTGGTCCTCCAGGGCACCATCGTCAAGCGCATCAAGGGCGACATCTACGAGTTCCGCGATGCCACCGGCAGCATGAAGGTGGAGATCGACGACGAAGACTTCCCGCCGATGGAAATCAACGACAAGACCCGGGTCAAGCTGACCGGCGAAGTCGACCGCGACCTGGTCGGCCGCGAGATCGACGTCGAGTTCGTCGAAGTGATCAAGTAAGACCCAGCTTTCTTGTACAAAGTTGG";
clonesequence.setText(clstr);  
df.setSequencePair(new SequencePair(clonesequence ,  refseq));
df.run();
 for(int d=0;d<clonesequence.getDiscrepancies().size();d++)
	{
	    Mutation mut = (Mutation)clonesequence.getDiscrepancies().get(d);
	    System.out.println(mut.toStringSeparateType() );
}

System.out.println("14	Frameshift insertion (1 base)");
clstr="CCAACTTTGTACAAAAAAGCAGGCTCCGAAGGAGATACCATGAAAGCTTCGTCACCTGCCCCTCATCGCTGCCATCGGCCTGTTCTCCACCGTCACCCTGGCCGCCGGCTATACCGGCCCGGGCGCTACCCCGACCACCACCACGGTGAAGGCCGCGCTGGAAGCCGCCGACGACACCCCGGTGGTCCTCCAGGGCACCATCGTCAAGCGCATCAAGGGCGACATCTACGAGTTCCGCGATGCCACCGGCAGCATGAAGGTGGAGATCGACGACGAAGACTTCCCGCCGATGGAAATCAACGACAAGACCCGGGTCAAGCTGACCGGCGAAGTCGACCGCGACCTGGTCGGCCGCGAGATCGACGTCGAGTTCGTCGAAGTGATCAAGTAAGACCCAGCTTTCTTGTACAAAGTTGG";
clonesequence.setText(clstr);  
df.setSequencePair(new SequencePair(clonesequence ,  refseq));
df.run();
 for(int d=0;d<clonesequence.getDiscrepancies().size();d++)
	{
	    Mutation mut = (Mutation)clonesequence.getDiscrepancies().get(d);
	    System.out.println(mut.toStringSeparateType() );
}
 
System.out.println("15	Frameshift insertion (n base)");
clstr="CCAACTTTGTACAAAAAAGCAGGCTCCGAAGGAGATACCATGAAACTTCGTCACCTGCCCAAAACTCATCGCTGCCATCGGCCTGTTCTCCACCGTCACCCTGGCCGCCGGCTATACCGGCCCGGGCGCTACCCCGACCACCACCACGGTGAAGGCCGCGCTGGAAGCCGCCGACGACACCCCGGTGGTCCTCCAGGGCACCATCGTCAAGCGCATCAAGGGCGACATCTACGAGTTCCGCGATGCCACCGGCAGCATGAAGGTGGAGATCGACGACGAAGACTTCCCGCCGATGGAAATCAACGACAAGACCCGGGTCAAGCTGACCGGCGAAGTCGACCGCGACCTGGTCGGCCGCGAGATCGACGTCGAGTTCGTCGAAGTGATCAAGTAAGACCCAGCTTTCTTGTACAAAGTTGG";
clonesequence.setText(clstr);  
df.setSequencePair(new SequencePair(clonesequence ,  refseq));
df.run();
 for(int d=0;d<clonesequence.getDiscrepancies().size();d++)
	{
	    Mutation mut = (Mutation)clonesequence.getDiscrepancies().get(d);
	    System.out.println(mut.toStringSeparateType() );
}

System.out.println("16	Frameshift deletion (1 base)");
clstr="CCAACTTTGTACAAAAAAGCAGGCTCCGAAGGAGATACCATGAAACTCGTCACCTGCCCCTCATCGCTGCCATCGGCCTGTTCTCCACCGTCACCCTGGCCGCCGGCTATACCGGCCCGGGCGCTACCCCGACCACCACCACGGTGAAGGCCGCGCTGGAAGCCGCCGACGACACCCCGGTGGTCCTCCAGGGCACCATCGTCAAGCGCATCAAGGGCGACATCTACGAGTTCCGCGATGCCACCGGCAGCATGAAGGTGGAGATCGACGACGAAGACTTCCCGCCGATGGAAATCAACGACAAGACCCGGGTCAAGCTGACCGGCGAAGTCGACCGCGACCTGGTCGGCCGCGAGATCGACGTCGAGTTCGTCGAAGTGATCAAGTAAGACCCAGCTTTCTTGTACAAAGTTGG";
clonesequence.setText(clstr);  
df.setSequencePair(new SequencePair(clonesequence ,  refseq));
df.run();
 for(int d=0;d<clonesequence.getDiscrepancies().size();d++)
	{
	    Mutation mut = (Mutation)clonesequence.getDiscrepancies().get(d);
	    System.out.println(mut.toStringSeparateType() );
}
System.out.println("17	Frameshift deletion (n base)");
clstr="CCAACTTTGTACAAAAAAGCAGGCTCCGAAGGAGATACCATGAAACTTCGTCACCCCTCATCGCTGCCATCGGCCTGTTCTCCACCGTCACCCTGGCCGCCGGCTATACCGGCCCGGGCGCTACCCCGACCACCACCACGGTGAAGGCCGCGCTGGAAGCCGCCGACGACACCCCGGTGGTCCTCCAGGGCACCATCGTCAAGCGCATCAAGGGCGACATCTACGAGTTCCGCGATGCCACCGGCAGCATGAAGGTGGAGATCGACGACGAAGACTTCCCGCCGATGGAAATCAACGACAAGACCCGGGTCAAGCTGACCGGCGAAGTCGACCGCGACCTGGTCGGCCGCGAGATCGACGTCGAGTTCGTCGAAGTGATCAAGTAAGACCCAGCTTTCTTGTACAAAGTTGG";
clonesequence.setText(clstr);  
df.setSequencePair(new SequencePair(clonesequence ,  refseq));
df.run();
 for(int d=0;d<clonesequence.getDiscrepancies().size();d++)
	{
	    Mutation mut = (Mutation)clonesequence.getDiscrepancies().get(d);
	    System.out.println(mut.toStringSeparateType() );
}

System.out.println("18	Substitution and deletion in one codon");
clstr="CCAACTTTGTACAAAAAAGCAGGCTCCGAAGGAGATACCATGAAAGTCGTCACCTGCCCCTCATCGCTGCCATCGGCCTGTTCTCCACCGTCACCCTGGCCGCCGGCTATACCGGCCCGGGCGCTACCCCGACCACCACCACGGTGAAGGCCGCGCTGGAAGCCGCCGACGACACCCCGGTGGTCCTCCAGGGCACCATCGTCAAGCGCATCAAGGGCGACATCTACGAGTTCCGCGATGCCACCGGCAGCATGAAGGTGGAGATCGACGACGAAGACTTCCCGCCGATGGAAATCAACGACAAGACCCGGGTCAAGCTGACCGGCGAAGTCGACCGCGACCTGGTCGGCCGCGAGATCGACGTCGAGTTCGTCGAAGTGATCAAGTAAGACCCAGCTTTCTTGTACAAAGTTGG";
clonesequence.setText(clstr);  
df.setSequencePair(new SequencePair(clonesequence ,  refseq));
df.run();
 for(int d=0;d<clonesequence.getDiscrepancies().size();d++)
	{
	    Mutation mut = (Mutation)clonesequence.getDiscrepancies().get(d);
	    System.out.println(mut.toStringSeparateType() );
}

System.out.println("19	Substitution and insertion in one codon");
clstr="CCAACTTTGTACAAAAAAGCAGGCTCCGAAGGAGATACCATGAAACTAGAAAACGTCACCTGCCCCTCATCGCTGCCATCGGCCTGTTCTCCACCGTCACCCTGGCCGCCGGCTATACCGGCCCGGGCGCTACCCCGACCACCACCACGGTGAAGGCCGCGCTGGAAGCCGCCGACGACACCCCGGTGGTCCTCCAGGGCACCATCGTCAAGCGCATCAAGGGCGACATCTACGAGTTCCGCGATGCCACCGGCAGCATGAAGGTGGAGATCGACGACGAAGACTTCCCGCCGATGGAAATCAACGACAAGACCCGGGTCAAGCTGACCGGCGAAGTCGACCGCGACCTGGTCGGCCGCGAGATCGACGTCGAGTTCGTCGAAGTGATCAAGTAAGACCCAGCTTTCTTGTACAAAGTTGG";
clonesequence.setText(clstr);  
df.setSequencePair(new SequencePair(clonesequence ,  refseq));
df.run();
 for(int d=0;d<clonesequence.getDiscrepancies().size();d++)
	{
	    Mutation mut = (Mutation)clonesequence.getDiscrepancies().get(d);
	    System.out.println(mut.toStringSeparateType() );
}*/

        }catch(Exception e)
        {
            System.out.println(e.getMessage());}
          System.exit(0);
    }


    

}
