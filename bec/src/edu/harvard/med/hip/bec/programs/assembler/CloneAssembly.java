//Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->
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
        return isAssemblerRunNeeded(clone_trace_files_location, 2);
    }
    public static int isAssemblerRunNeeded( String clone_trace_files_location , int requered_number_of_files)throws Exception
    {
            int assembly_run_status = -1;
            int numberOfTraceFiles = numberOfExistingTraceFiles( clone_trace_files_location +File.separator +PhredWrapper.CHROMAT_DIR_NAME, requered_number_of_files); //trace file directory
             if ( numberOfTraceFiles == 0 ) 
                    return ASSEMBLY_RUN_STATUS_NO_TRACE_FILES_AVAILABLE;
            File phd_dir = new File( clone_trace_files_location +File.separator +PhredWrapper.PHD_DIR_NAME); //trace file directory
            File [] phd_files = phd_dir.listFiles(); //trace file directory
            if (   numberOfTraceFiles > phd_files.length ) 
                return ASSEMBLY_RUN_STATUS_NOT_ALL_TRACE_FILES_INCLUDED;
            return assembly_run_status;
    }
     

    public static int numberOfExistingTraceFiles( String clone_trace_files_location, int requered_number_of_files )throws Exception
    {
            File trace_dir = new File( clone_trace_files_location ); //trace file directory
            File [] trace_files = trace_dir.listFiles(); //trace file directory
            if (   trace_files == null || trace_files.length < requered_number_of_files)
                 return 0;
            else
                return trace_files.length;
    }
    
    public  void sortContigsByLength()
    {
        m_contigs = sortContigsByLength(m_contigs);
    }
    public static ArrayList sortContigsByLength(ArrayList contigs)
    {
          //sort array by containerid and position
            Collections.sort(contigs, new Comparator() 
            {
                public int compare(Object o1, Object o2) 
                {
                    Contig cl1 = (Contig)o1;
                    Contig cl2 = (Contig)o2;
                    return cl2.getSequence().length() - cl1.getSequence().length();
                 
                }
                /** Note: this comparator imposes orderings that are
                 * inconsistent with equals. */
                public boolean equals(java.lang.Object obj)
                {      return false;  }
                // compare
            } );
        
        return contigs;
    }
    
    
}
