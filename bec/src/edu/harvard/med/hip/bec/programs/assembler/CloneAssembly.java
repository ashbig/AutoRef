/*
 * CloneAssembly.java
 *
 * Created on August 4, 2003, 5:33 PM
 */

package edu.harvard.med.hip.bec.programs.assembler;


import java.util.*;
import java.io.*;
import edu.harvard.med.hip.bec.coreobjects.endreads.*;
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import  edu.harvard.med.hip.bec.programs.phred.*;
/**
 *
 * @author  HTaycher
 */
public class CloneAssembly
{
    public static int       STATUS_ASSEMBLY_NOT_KNOWN = -1000;
    public static final int   ASSEMBLY_RUN_STATUS_NO_TRACE_FILES_AVAILABLE = 0;
    public static final int   ASSEMBLY_RUN_STATUS_NOT_ALL_TRACE_FILES_INCLUDED = 1;
    
     
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
    
    public static int isAssemblerRunNeeded( String clone_trace_files_location )throws Exception
        {
            int assembly_run_status = -1;
            File trace_dir = new File( clone_trace_files_location +File.separator +PhredWrapper.CHROMAT_DIR_NAME); //trace file directory
            File [] trace_files = trace_dir.listFiles(); //trace file directory
            if (   trace_files.length == 0 )
                 return ASSEMBLY_RUN_STATUS_NO_TRACE_FILES_AVAILABLE;
            File phd_dir = new File( clone_trace_files_location +File.separator +PhredWrapper.PHD_DIR_NAME); //trace file directory
            File [] phd_files = phd_dir.listFiles(); //trace file directory
            if (   trace_files.length > phd_files.length ) 
                return ASSEMBLY_RUN_STATUS_NOT_ALL_TRACE_FILES_INCLUDED;
            return assembly_run_status;
    }
     

    
    
}
