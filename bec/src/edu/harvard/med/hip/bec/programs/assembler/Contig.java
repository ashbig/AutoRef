/*
 * CloneAssembly.java
 *
 * Created on July 11, 2003, 12:18 PM
 */

package edu.harvard.med.hip.bec.programs.assembler;

import java.util.*;
import edu.harvard.med.hip.bec.coreobjects.endreads.*;
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import  edu.harvard.med.hip.bec.util.*;
import  edu.harvard.med.hip.bec.bioutil.*;
import edu.harvard.med.hip.bec.programs.needle.*;
import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.utility.*;
import edu.harvard.med.hip.bec.Constants;
import edu.harvard.med.hip.bec.modules.*;

public class Contig
{
   
    
    private   String m_needle_output_path = null;
    {
        if (ApplicationHostDeclaration.IS_BIGHEAD)
            m_needle_output_path = "d:\\output\\tmp_assembly\\";
        else
            m_needle_output_path = "c:\\tmp_assembly\\";
    }
    
    private String              m_sequence = null;
    private String              m_scores = null;
    private int[]               m_arr_scores = null;
    private int[]               m_including_star_arr_scores = null;
    private String              m_name = null;
    private int                 m_num_of_reads_in_contig = 0;
    private int                 m_number_of_bases = 0;
    private ArrayList           m_reads = null;
     
    private int                 m_cds_start = 0; //0 based indexes
    private int                 m_cds_stop = 0;
    /** Creates a new instance of CloneAssembly */
    public Contig()    {        m_reads = new ArrayList();    }
    
    public String              getSequence(){ return  m_sequence ;}
    public String              getScores(){ return m_scores ;}
    public int[]               getScoresAsIntArray()
    { 
        if ( m_arr_scores == null && m_scores != null && m_scores.length() != 0 )
            m_arr_scores = Algorithms.getConvertStringToIntArray(m_scores," ");
        return m_arr_scores ;
    }
    
    //scores given without * value 
    public int[]               getScoresAsIntArrayIncludingStars()
    { 
        if ( m_including_star_arr_scores != null) return m_including_star_arr_scores;
        if ( m_arr_scores == null && m_scores != null && m_scores.length() != 0 )
            m_arr_scores = Algorithms.getConvertStringToIntArray(m_scores," ");
        m_including_star_arr_scores = new int[ m_sequence.length()];
        char[] sequence = m_sequence.toCharArray();
        int score_number = 0;
        for (int count = 0 ; count < m_sequence.length(); count++)
        {
            if (sequence[count] != '*' )
                m_including_star_arr_scores [count] = m_arr_scores[score_number++];
            else
                m_including_star_arr_scores[count] = 0;
        }
        return m_including_star_arr_scores ;
    }
    
    public String              getName(){ return m_name ;}
    public int                 getNumberOfReadsInContig(){ return m_num_of_reads_in_contig;}
    public int                 getCdsStart(){ return  m_cds_start ;}
    public int                 getCdsStop(){ return m_cds_stop ;}
    public ArrayList            getReads(){ return m_reads;}
    public int                  getNumberOfBases(){ return m_number_of_bases;}
    
    
    public void                 addRead(ReadInAssembly v){ if( m_reads == null) m_reads = new ArrayList(); m_reads.add(v);}

    public void                 setNumberOfReadsInContig(int v){  m_num_of_reads_in_contig = v;}
    public void              setName(String s){   m_name =s ;} 
    public void              setSequence(String s){   m_sequence =s ;}
    public void              setScores(String s){ m_scores =s ;}
    public void             setReads(ArrayList v){ m_reads= v;}
    public void             setNumberOfBases(int v){ m_number_of_bases = v;}
    
     //checks first contig only
    public int checkForCoverage(int cloneid, int cds_start, int cds_stop, BaseSequence refsequence)
    throws BecUtilException
    {
         //run needle
        NeedleWrapper nw = new NeedleWrapper();
        nw.setQueryId(cloneid);
        nw.setReferenceId( refsequence.getId());
        nw.setQuerySeq(m_sequence);
        nw.setRefSeq(refsequence.getText());
     //   nw.setGapOpen(20);
     //   nw.setGapExtend(0.05);
        nw.setOutputFileDir(m_needle_output_path);
        NeedleResult res_needle =  nw.runNeedle();
        
        //parse needle output
         res_needle.recalculateIdentity();
        if (res_needle.getIdentity() < Constants.MIN_IDENTITY_CUTOFF)
        {
            return IsolateTrackingEngine.ASSEMBLY_STATUS_FAILED_NO_MATCH;
        }
        //no coverage
        if (res_needle.getQuery() == null || res_needle.getSubject() == null)
            return IsolateTrackingEngine.ASSEMBLY_STATUS_FAILED_CDS_NOT_COVERED;
        
        char[] sequence_query_n = res_needle.getQuery().toUpperCase().toCharArray();
        char[] sequence_subject_n = res_needle.getSubject().toUpperCase().toCharArray();
        //prepare sequence elements
        SequenceElement[] start_stop = prepareSequenceElements(sequence_query_n,sequence_subject_n, cds_start,  cds_stop, refsequence.getText().length() );
        return setCoverageStatus(start_stop);

    }
  //uses horizontal array  
     public static ArrayList           findGaps(ArrayList contigs, int linker5_length, int clone_refsequence_length )
     {
         contigs = Stretch.sortByPosition(contigs);
         Stretch gap = null;
         ArrayList gaps = new ArrayList();
         int[] cds_coord = new int[ contigs.size() * 2];
         int coord_index = 0;  
         for (int contig_number = 0; contig_number < contigs.size() ; contig_number++)
         {
             Stretch contig = (Stretch)contigs.get(contig_number);
            // check if first contig covers 5 prime
             if ( contig_number == 0 && contig.getCdsStart() != ScoredElement.DEFAULT_COORDINATE )
             {
                gap = new Stretch(Stretch.GAP_TYPE_GAP, Stretch.STATUS_DETERMINED, ScoredElement.DEFAULT_COORDINATE , contig.getCdsStart() - 1 , Constants.ORIENTATION_FORWARD);
                gaps.add( gap);
               
              }
             //create gap after last contig if needed
             if ( contig_number == contigs.size() - 1 && contig.getCdsStop() != ScoredElement.DEFAULT_COORDINATE )
             {
                gap = new Stretch(Stretch.GAP_TYPE_GAP, Stretch.STATUS_DETERMINED, contig.getCdsStop() + 1,ScoredElement.DEFAULT_COORDINATE ,  Constants.ORIENTATION_FORWARD);
                gaps.add( gap);
             
             }
             if ( contig_number < contigs.size() - 1)//last element
             {
                gap = new Stretch(Stretch.GAP_TYPE_GAP, Stretch.STATUS_DETERMINED, contig.getCdsStop() + 1 , ((Stretch)contigs.get(contig_number + 1)).getCdsStart() - 1, Constants.ORIENTATION_FORWARD);
                gaps.add( gap);
             }
             
         }
        
         return gaps;
         
     }
             /*
    public static ArrayList            findGaps(ScoredElement[][] bases_array)
    { 
        ArrayList gaps = new ArrayList();
        ScoredElement[] base_aligment = null;
        boolean isBaseReadCovered = false;
        boolean isInsideGap = false;
          Stretch gap = null;
        //find out how namy 'reads' - first is contig, second - reference sequence are in contig
        
        int number_of_reads = bases_array[0].length;
        int bases_in_contig = bases_array.length;
        int base_count = 0;
        for (; base_count < bases_in_contig; base_count++)
        {
              //no requered refsequence
             if ( bases_array[base_count][1].getBase() == '\u0000') continue;
             if ( bases_array[base_count][1].getBase() == '\u0000' && base_count != 0 ) break;
             isBaseReadCovered = false;
             for (int read_count = 2; read_count <  number_of_reads; read_count++)
             {
               
                if ( bases_array[base_count][read_count].getBase() != '\u0000')
                {
                    isBaseReadCovered = true;
                    break;
                }
                 
             }
             //close gap
             if ( isInsideGap && isBaseReadCovered)
             {
                 gap.setCdsStop (bases_array[base_count][1].getIndex());
                 gaps.add(gap);
                 gap = null;
                 isInsideGap = false;
             }
             //open gap
             else if ( !isBaseReadCovered && !isInsideGap )
             {
                 gap = new Stretch();
                 gap.setCdsStart (bases_array[base_count][1].getIndex() ); //(int vclone sequence coordinates for low quality)
                gap.setType ( Stretch.GAP_TYPE_GAP );
                gap.setStatus( Stretch.STATUS_DETERMINED );
                isInsideGap = true;
             }
        }
         //close last  gap
         if ( isInsideGap && gap!= null)
         {
             gap.setCdsStop (bases_array[base_count-1][1].getIndex() );
             gaps.add(gap);
         }
        return gaps;
    }
    
    */
    
    //uses horizontal array  returns array of stretch elemnts
    public static ArrayList            findContigs(ScoredElement[][] bases_array, 
            int refseq_length, int linker5_length, SlidingWindowTrimmingSpec spec)throws Exception
    { 
        ArrayList contigs = new ArrayList();
        ArrayList current_contig = null;//array of scored elements
        Stretch contig = null;
        ScoredElement current_base_of_contig = null;ScoredSequence sequence = null;
        boolean isBaseReadCovered = false;
        boolean isInsideContig = false;
        //find out how namy 'reads' - first is contig, second - reference sequence are in contig
        
        int number_of_reads = bases_array[0].length;
        int bases_in_contig = bases_array.length;
        int base_count = 0;
        try
        {
            for (; base_count < bases_in_contig; base_count++)
            {
                 isBaseReadCovered = false;
                 for (int read_count = 2; read_count <  number_of_reads; read_count++)
                 {
                    if ( bases_array[base_count][read_count].getBase() != '\u0000')
                    {
                        isBaseReadCovered = true;
                        break;
                    }

                 }
                 if ( isBaseReadCovered) 
                 {
                     //drop first two elements -> they are from assembled contig & refseq
                     ScoredElement[] real_bases = prepareBaseColumn( bases_array[base_count]);
                     if ( real_bases != null && real_bases.length > 0)
                     {
                         char refseq_base = bases_array[base_count][1].getBase();
                         current_base_of_contig = ScoredElement.createScoreElementFromListOfElements(real_bases,  bases_array[base_count][1].getIndex(), refseq_base);
                         if ( current_contig == null) current_contig = new ArrayList();
                         current_contig.add( current_base_of_contig );
                         
                     }
                  }
                 //close gap
                 if ( isInsideContig && !isBaseReadCovered)
                 {
                     if ( bases_array[base_count][1].getIndex() != ScoredElement.DEFAULT_COORDINATE)
                        contig.setCdsStop (bases_array[base_count][1].getIndex() -1);
                     else
                         contig.setCdsStop (bases_array[base_count][1].getIndex());
                    sequence = ScoredElement.trimStretch( current_contig,  contig,  spec);
                   
                     if ( sequence != null && sequence.getText().length() > spec.getMinContigLength())
                      {
                          contig.setSequence (new AnalyzedScoredSequence(sequence.getText(),sequence.getScores(), -1) );
                          contigs.add(contig);
                      }
                     contig = null;        
                     isInsideContig = false;
                     current_contig = new ArrayList();
                     sequence = null;
                 }
                 //open gap
                 else if ( isBaseReadCovered && !isInsideContig )
                 {
                     contig = new Stretch();
                     contig.setAnalysisStatus(BaseSequence.CLONE_SEQUENCE_STATUS_ASSEMBLED);
                     contig.setCdsStart (bases_array[base_count][1].getIndex() ); //(int vclone sequence coordinates for low quality)
                    contig.setType ( Stretch.GAP_TYPE_CONTIG );
                    contig.setStatus( Stretch.STATUS_DETERMINED );

                    isInsideContig = true;
                 }
            }
             //close last  gap
             if ( isInsideContig && contig!= null)
             {
                 contig.setCdsStop (bases_array[base_count][1].getIndex() -1);
                 sequence = ScoredElement.trimStretch( current_contig,  contig,  spec);
                  
                  if ( sequence != null && sequence.getText().length() > spec.getMinContigLength())
                  {
                      contig.setSequence (new AnalyzedScoredSequence(sequence.getText(),sequence.getScores(), -1) );
                      contigs.add(contig);
                  }
             }
            return contigs;
        }
        catch(Exception e)
        {
           // System.out.println(e.getMessage());
            throw new BecUtilException("Cannot build collection of contigs for the clone");
        }
    }
    
    
    
    public ScoredElement[][]         prepareContigAligmentVerticalStructure(String refseq_read_name)
    {
         ScoredElement[][]       bases_array     = new ScoredElement[m_num_of_reads_in_contig +1 ][m_sequence.length()];
         //fill in array
         for (int col_number = 0; col_number < m_num_of_reads_in_contig+1; col_number++)
         {
             for (int row_number = 0; row_number < m_sequence.length(); row_number++)
             {
                 bases_array[col_number][row_number] = new ScoredElement();
             }
         }
         
         
         
         char[] contig_array =   m_sequence.toCharArray();
         for (int count = 0; count <m_sequence.length();count++)
         {
                bases_array[0][count].setIndex( count);
                bases_array[0][count].setScore(getScoresAsIntArrayIncludingStars()[count]);
                bases_array[0][count].setBase(contig_array[count] );
         }
         
         int current_base = 0;int not_refseq_current_read = 2;      int base_count =0; int current_read = 0;
         ReadInAssembly read = null;

         for (int read_count =0; read_count < m_reads.size(); read_count++)
         {
             read = (ReadInAssembly)  m_reads.get(read_count);
             char[] sequence = read.getSequence().toCharArray();
             //forse refseq to be second read
             if ( read.getName().toUpperCase().indexOf(refseq_read_name.toUpperCase()) != -1)
                 current_read = 1;
             else 
                 current_read = not_refseq_current_read++;
              current_base = read.getQualityStart()-1;
              base_count =  read.getStart() + read.getQualityStart() -2;
              for (;  current_base  <  read.getQualityEnd()-1;)
             {
        //      System.out.println(read_count +" "+base_count+" "+current_base+" "+sequence[current_base]);
                 bases_array[current_read][base_count].setIndex( current_base);
                 bases_array[current_read][base_count].setScore(read.getScoresIncludingStar()[current_base]);
                 bases_array[current_read][base_count++].setBase( sequence[current_base++]);
         //sequence[current_base++];
             }
         }
     /*    
         for ( base_count = 0; base_count < m_sequence.length(); base_count++)
         {
                System.out.print(bases_array[0][base_count].getIndex()+" "+ bases_array[0][base_count].getBase() +" " + bases_array[0][base_count].getScore());
                System.out.print(" "+ bases_array[1][base_count].getIndex()+" "+ bases_array[1][base_count].getBase() +" " + bases_array[1][base_count].getScore());
                System.out.print(" "+ bases_array[2][base_count] .getIndex()+" "+ bases_array[2][base_count].getBase() +" " + bases_array[2][base_count].getScore());
                System.out.print( " "+bases_array[3][base_count] .getIndex()+" "+ bases_array[3][base_count].getBase() +" " + bases_array[3][base_count].getScore());
                 System.out.println("" );
         }
      */    

         return bases_array;
    }
    
    
    
     public ScoredElement[][]         prepareContigAligmentHorizontalStructure(String refseq_read_name, int linker_5_length)
    {
         ScoredElement[][]       bases_array     = new ScoredElement[m_sequence.length()][m_num_of_reads_in_contig +1 ];
         int refseq_index = -linker_5_length;
         //fill in array
          for (int row_number = 0; row_number < m_sequence.length(); row_number++)
          {
             for (int col_number = 0; col_number < m_num_of_reads_in_contig+1; col_number++)
             {
                 bases_array[row_number][col_number] = new ScoredElement();
             }
          }
         
         
         
         char[] contig_array =   m_sequence.toCharArray();
         for (int count = 0; count <m_sequence.length();count++)
         {
                bases_array[count][0].setIndex( count);
                bases_array[count][0].setScore(getScoresAsIntArrayIncludingStars()[count]);
                bases_array[count][0].setBase(contig_array[count] );
         }
         
         int current_base = 0;int not_refseq_current_read = 2;      int base_count =0; int current_read = 0;
         ReadInAssembly read = null;

         for (int read_count =0; read_count < m_reads.size(); read_count++)
         {
             read = (ReadInAssembly)  m_reads.get(read_count);
             char[] sequence = read.getSequence().toCharArray();
             //forse refseq to be second read
             if ( read.getName().toUpperCase().indexOf(refseq_read_name.toUpperCase()) != -1)
                 current_read = 1;
             else 
                 current_read = not_refseq_current_read++;
              current_base = read.getQualityStart()-1;
              base_count =  read.getStart() + read.getQualityStart() -2;
              for (;  current_base  <  read.getQualityEnd()-1;)
             {
              // System.out.println(read_count +" "+base_count+" "+current_base+" "+sequence[current_base]);
                 if ( current_read == 1 && sequence[current_base] != '*') // for indexing for sequence, * excluded
                 {
                       if ( refseq_index == 0) refseq_index =1 ;
                        bases_array[base_count][current_read].setIndex( refseq_index++);
                 }
             //    else
                   //  bases_array[base_count][current_read].setIndex( current_base);
                 bases_array[base_count][current_read].setScore(read.getScoresIncludingStar()[current_base]);
                 bases_array[base_count++][current_read].setBase( sequence[current_base++]);
         //sequence[current_base++];
             }
         }
         /*
         for ( base_count = 0; base_count < m_sequence.length(); base_count++)
         {
             for (int read_count = 0; read_count <m_num_of_reads_in_contig+1;read_count++)
             {
                System.out.print(bases_array[base_count][read_count].getIndex()+"\t'"+ bases_array[base_count][read_count].getBase() +"'\t" + bases_array[base_count][read_count].getScore()+"\t");
                 
             }
              System.out.println();
         }
*/
         return bases_array;
    }
   //------------------------------------------- 
   
    private  static ScoredElement[]  prepareBaseColumn( ScoredElement[] contig_column)
    {
        ArrayList real_bases_in_column = new ArrayList();
        for (int element = 2; element < contig_column.length;element++)
         {
             if ( contig_column[element].getBase() == '\u0000' ||contig_column[0].getBase()== '*') continue;
             real_bases_in_column.add( contig_column[element]);
         }
        
        ScoredElement[] real_bases = new ScoredElement[real_bases_in_column.size()];
         for (int element = 0; element < real_bases_in_column.size();element++)
         {
             real_bases[element] = (ScoredElement)real_bases_in_column.get(element);
         }
        return real_bases;
    }
    private SequenceElement[] prepareSequenceElements(char[] sequence_query_n,
                                        char[] sequence_subject_n  ,
                                        int cds_start, int cds_stop   ,int refseq_length    )
    {
        int length = ( sequence_query_n.length  >= sequence_subject_n.length ) ?
                sequence_subject_n.length   :   sequence_query_n.length  ;
        SequenceElement[] elements = new SequenceElement[4];
      
        int q_index = 0;
        int s_index = 0;
       
        for (int count = 0; count < length; count++)
        {
            if ( sequence_subject_n[count] !=' ' && sequence_subject_n[count] != '-')
            {
                s_index++;
            }
            if (sequence_query_n[count] != ' ' && sequence_query_n[count] != '-')
            {
                q_index ++;
               
            }

            if (s_index == 1)
            {
                 elements[0] = new SequenceElement(  q_index  , s_index , -1, sequence_query_n[count],sequence_subject_n[count]);
            }
            else if (s_index == cds_start + 1)//array 0 based
            {
                m_cds_start =q_index - 1;
                 elements[1] = new SequenceElement(  q_index  , s_index , -1, sequence_query_n[count],sequence_subject_n[count]);
            }
            else if (s_index ==  cds_stop )
            {
                m_cds_stop = q_index -1;
                elements[2] = new SequenceElement(  q_index  , s_index , -1, sequence_query_n[count],sequence_subject_n[count]);
            }
            else if (  s_index == refseq_length  ) 
            {
                elements[3] = new SequenceElement(  q_index  , s_index , -1, sequence_query_n[count],sequence_subject_n[count]);
                break;
            }
          
        }
        return elements;
    }
    protected int setCoverageStatus(SequenceElement[] start_stop)
    {
        int res = CloneAssembly.STATUS_ASSEMBLY_NOT_KNOWN;
        if (start_stop[1].getQueryChar() == ' ' ||  start_stop[2].getQueryChar() == ' '   )
        {
            res = IsolateTrackingEngine.ASSEMBLY_STATUS_FAILED_CDS_NOT_COVERED ;
        }
        
        else if  (start_stop[0].getQueryChar() == ' ' &&  start_stop[1].getQueryChar() != ' '
        && start_stop[2].getQueryChar() != ' ' &&  start_stop[3].getQueryChar() != ' ')
        {
            res = IsolateTrackingEngine.ASSEMBLY_STATUS_FAILED_LINKER5_NOT_COVERED ;
        }
        
        else if (start_stop[0].getQueryChar() != ' ' &&  start_stop[1].getQueryChar() != ' '
        && start_stop[2].getQueryChar() != ' ' &&  start_stop[3].getQueryChar() == ' '  )
        {
            res = IsolateTrackingEngine.ASSEMBLY_STATUS_FAILED_LINKER3_NOT_COVERED ;
        }
        else if( start_stop[0].getQueryChar() == ' ' && start_stop[1].getQueryChar() != ' ' &&
            start_stop[2].getQueryChar() != ' '  && start_stop[3].getQueryChar() == ' ')
        {
            res = IsolateTrackingEngine.ASSEMBLY_STATUS_FAILED_BOTH_LINKERS_NOT_COVERED ;
        }
        else  if (start_stop[0].getQueryChar() != ' ' && start_stop[0].getSubjectChar() != ' ' &&
            start_stop[3].getQueryChar() != ' '  && start_stop[3].getSubjectChar() != ' ' )
      {
         res= IsolateTrackingEngine.ASSEMBLY_STATUS_PASS ;
      }
        return res;
    }
    
    
    public static void main(String[] a)
    {
        try{
            NeedleResult res = new NeedleResult();
           String queryFile ="c:\\Document.txt";// "c:\\needleoutput\\needle10339_419.out";
            //  String queryFile = "c:\\needleATG.out";
             NeedleParser.parse(queryFile,res);
       char[] sequence_query_n = res.getQuery().toUpperCase().toCharArray();
        char[] sequence_subject_n = res.getSubject().toUpperCase().toCharArray();
        //prepare sequence elements
        Contig c = new Contig();
     //   SequenceElement[] start_stop = c.prepareSequenceElementsTest(sequence_query_n,sequence_subject_n, 70,  460,534 );
        //int r = c.setCoverageStatus(start_stop);
      //  System.out.print( start_stop.length);
         }catch(Exception e){}
    }
}
