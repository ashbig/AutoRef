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
public class CloneAssembly
{
    private String              m_sequence = null;
    private String              m_scores = null;
    /** Creates a new instance of CloneAssembly */
    public CloneAssembly()
    {
    }
    
    
     public String              getSequence(){ return  m_sequence ;}
    public String              getScores(){ return m_scores ;}
    public void              setSequence(String s){   m_sequence =s ;}
    public void              setScores(String s){ m_scores =s ;}
    
}
