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
    /** Creates a new instance of CloneAssembly */
    public Contig()
    {
    }
    
    
     public String              getSequence(){ return  m_sequence ;}
    public String              getScores(){ return m_scores ;}
     public String              getName(){ return m_name ;}
     public int                 getNumberOfReadsInContig(){ return m_num_of_reads_in_contig;}
     
     
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
        nw.setOutputFileDir("\\tmp\\needle"+cloneid+"_"+refsequence.getId() +".out");
        NeedleResult res_needle =  nw.runNeedle();
        
        //parse needle output
        
        //no coverage
        if (res_needle.getQuery() == null || res_needle.getSubject() == null)
            return IsolateTrackingEngine.PROCESS_STATUS_ASSEMBLY_FAILED_CDS_NOT_COVERED;
        
        char[] sequence_query_n = res_needle.getQuery().toUpperCase().toCharArray();
        char[] sequence_subject_n = res_needle.getSubject().toUpperCase().toCharArray();
        //prepare sequence elements
        SequenceElement[] start_stop = prepareSequenceElements(sequence_query_n,sequence_subject_n, cds_start,  cds_stop, refsequence.getText().length() -1);
        return setCoverageStatus(start_stop);

    }
    
    
    private SequenceElement[] prepareSequenceElements(char[] sequence_query_n,
                                        char[] sequence_subject_n  ,
                                        int cds_start, int cds_stop   ,int refseq_length    )
    {
        int length = ( sequence_query_n.length  >= sequence_subject_n.length ) ?
                sequence_subject_n.length -1  :   sequence_query_n.length -1 ;
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
            else if (s_index == cds_start)
                 elements[1] = new SequenceElement(  q_index  , s_index , -1, sequence_query_n[count],sequence_subject_n[count]);
            else if (s_index ==  cds_stop)
                 elements[2] = new SequenceElement(  q_index  , s_index , -1, sequence_query_n[count],sequence_subject_n[count]);
            else if (s_index == refseq_length) 
                elements[3] = new SequenceElement(  q_index  , s_index , -1, sequence_query_n[count],sequence_subject_n[count]);
    
        }
        return elements;
    }
    
    
    private int setCoverageStatus(SequenceElement[] start_stop)
    {
        if (start_stop[1].getQueryChar() == ' ' ||  start_stop[2].getQueryChar() == ' '   )
        {
            return IsolateTrackingEngine.PROCESS_STATUS_ASSEMBLY_FAILED_CDS_NOT_COVERED ;
        }
        if  (start_stop[0].getQueryChar() == ' ' &&  start_stop[1].getQueryChar() != ' '  )
        {
            return IsolateTrackingEngine.PROCESS_STATUS_ASSEMBLY_FAILED_LINKER5_NOT_COVERED ;
        }
         if (start_stop[2].getQueryChar() != ' ' &&  start_stop[3].getQueryChar() == ' '  )
        {
            return IsolateTrackingEngine.PROCESS_STATUS_ASSEMBLY_FAILED_LINKER3_NOT_COVERED ;
        }
      if (start_stop[0].getQueryChar() != ' ' && start_stop[0].getSubjectChar() != ' ' &&
        start_stop[3].getQueryChar() != ' '  && start_stop[3].getSubjectChar() != ' ' )
      {
        return IsolateTrackingEngine.PROCESS_STATUS_ASSEMBLY_PASS ;
      }
        return CloneAssembly.STATUS_ASSEMBLY_NOT_KNOWN;
    }
}
