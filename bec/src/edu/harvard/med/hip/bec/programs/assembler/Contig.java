/*
 * CloneAssembly.java
 *
 * Created on July 11, 2003, 12:18 PM
 */

package edu.harvard.med.hip.bec.programs.assembler;

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
    
}
