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
import edu.harvard.med.hip.bec.coreobjects.endreads.*;
/**
 *
 * @author  htaycher
 */
public class Contig
{
    private String              m_sequence = null;
    private String              m_scores = null;
    private String              m_name = null;
    private int                 m_num_of_reads_in_contig = 0;
    private int                 m_cds_start = 0; //0 based indexes
    private int                 m_cds_stop = 0;
    /** Creates a new instance of CloneAssembly */
    public Contig()
    {
    }
    
    
     public String              getSequence(){ return  m_sequence ;}
    public String              getScores(){ return m_scores ;}
     public String              getName(){ return m_name ;}
     public int                 getNumberOfReadsInContig(){ return m_num_of_reads_in_contig;}
      public int                getCdsStart(){ return  m_cds_start ;}
    public int                 getCdsStop(){ return m_cds_stop ;}
     
     public void            setNumberOfReadsInContig(int v){  m_num_of_reads_in_contig = v;}
     
    public void              setName(String s){   m_name =s ;} 
    public void              setSequence(String s){   m_sequence =s ;}
    public void              setScores(String s){ m_scores =s ;}
    
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
        nw.setGapOpen(20);
        nw.setGapExtend(0.05);
        nw.setOutputFileDir("/tmp_assembly/");
        NeedleResult res_needle =  nw.runNeedle();
        
        //parse needle output
         res_needle.recalculateIdentity();
        if (res_needle.getIdentity() < 50)
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
            if (count == 547)
            {
                System.out.println(count);
        }
    
        }
        return elements;
    }
    /*
     public   SequenceElement[] prepareSequenceElementsTest(char[] sequence_query_n,
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
          //  System.out.println(count+" "+sequence_subject_n[count]+" "+sequence_query_n[count]);
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
            else if (count == length-1 || s_index == refseq_length ) 
            {
                elements[3] = new SequenceElement(  q_index  , s_index , -1, sequence_query_n[count],sequence_subject_n[count]);
                break;
            }
    
        }
        return elements;
    }
    */
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
           String queryFile ="c:\\tmp_assembly\\needle990_96.out";// "c:\\needleoutput\\needle10339_419.out";
            //  String queryFile = "c:\\needleATG.out";
             NeedleParser.parse(queryFile,res);
               
       
        
        char[] sequence_query_n = res.getQuery().toUpperCase().toCharArray();
        char[] sequence_subject_n = res.getSubject().toUpperCase().toCharArray();
        //prepare sequence elements
        Contig c = new Contig();
       // SequenceElement[] start_stop = c.prepareSequenceElementsTest(sequence_query_n,sequence_subject_n, 70,  460,534 );
      //  int r = c.setCoverageStatus(start_stop);
      //  System.out.print( start_stop.length);
        
        }catch(Exception e){}
    }
}
