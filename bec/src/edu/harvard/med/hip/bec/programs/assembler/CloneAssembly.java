/*
 * CloneAssembly.java
 *
 * Created on August 4, 2003, 5:33 PM
 */

package edu.harvard.med.hip.bec.programs.assembler;


import java.util.*;
import edu.harvard.med.hip.bec.coreobjects.endreads.*;
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
/**
 *
 * @author  HTaycher
 */
public class CloneAssembly
{
    public static int       STATUS_ASSEMBLY_NOT_KNOWN = -1000;
     
    private ArrayList       m_contigs = null;
    private int             m_number_of_reads = -1;
    
    private int             m_contig_coverage = STATUS_ASSEMBLY_NOT_KNOWN;
    /** Creates a new instance of CloneAssembly */
    public CloneAssembly()
    {
    }
    
    public void   addContig(Contig c)
    {
        if (m_contigs == null)
            m_contigs = new ArrayList();
        m_contigs.add(c);
    }
    public void             setNumOfReads(int v){m_number_of_reads = v;}
    public void             setContigs(ArrayList a){  m_contigs = a;}
    public void             setStatus(int v){     m_contig_coverage =v ;}
    
    public int              getNumberOfReads(){ return m_number_of_reads;}
    public ArrayList        getContigs(){ return m_contigs;}
    public int              getStatus(){ return             m_contig_coverage ;}
    
   
    
    
}
