//Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->
/*
 * GapMapper.java
 ** @author  HTaycher
 * Created on May 3, 2004, 4:48 PM
 */

package edu.harvard.med.hip.bec.modules;
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.coreobjects.endreads.*;
import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.database.*;
import  edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.util_objects.*;
import  edu.harvard.med.hip.bec.bioutil.*;
import  edu.harvard.med.hip.bec.file.*;
import edu.harvard.med.hip.bec.Constants;
import edu.harvard.med.hip.bec.programs.assembler.*;
import edu.harvard.med.hip.bec.sampletracking.objects.*;
import edu.harvard.med.hip.bec.programs.phred.*;
import java.util.*;
import java.sql.*;
import java.io.*;

// work on clone base
public class GapMapper
{
   // private int                         m_clone_id = -1;
    private ArrayList                   m_error_messages = null;
   // private CloningStrategy             m_cloning_startegy = null;
    private StretchCollection           m_stretch_collection = null;
    private SlidingWindowTrimmingSpec       m_trimming_spec = null;
    private boolean                         m_isRunLQRFinderForContigs = false;
    private String                      m_vector_file_name = null;
    private int                         m_quality_trimming_phd_score = 0;
    private int                         m_quality_trimming_phd_first_base = 0;
    private int                         m_quality_trimming_phd_last_base = 0;
    private int                         m_use_lqreads_for_assembly = 0;
    private int                         m_delete_lqreads = 0;
    private CloneDescription            m_clone_description =    null;
    private CloningStrategy            m_cloning_strategy =    null;
   
    
   // private int                     m_min_contig_avg_score = 20;
    // private int                     m_min_contig_length = MIN_CONTIG_LENGTH;
    /** Creates a new instance of GapMapper */
    public GapMapper()
    {
        m_error_messages = new ArrayList();
    }
    
    //public void         setCloneId(int v){ m_clone_id = v;}
    public void         setCloneDescription(CloneDescription v ){ m_clone_description = v;}
    public void         setCloningStrategy( CloningStrategy v ){ m_cloning_strategy = v;}
    public void         setVectorFileName(String v){m_vector_file_name = v;}
    public void         setQualityTrimmingScore (int v){ m_quality_trimming_phd_score = v;}
    public void         setQualityTrimmingLastBase (int v){ m_quality_trimming_phd_last_base = v;}
    public void         setQualityTrimmingFirstBase (int v){ m_quality_trimming_phd_first_base = v;}
    public void         setIsDeleteLQReads(int v){ m_delete_lqreads = v;}
    public void         setIsUseLQReadsForAssembly(int v){  m_use_lqreads_for_assembly = v;}
     
    
    public void                 setIsRunLQR(boolean v){m_isRunLQRFinderForContigs = v;}
    public ArrayList            getErrorMessages(){ return m_error_messages; }
    public StretchCollection    getStretchCollection(){ return   m_stretch_collection ;}
    public void                 setTrimmingSpec(SlidingWindowTrimmingSpec   v){    m_trimming_spec = v;}
  
    public void                 run()
    {
        //get refsequence & linker information
        try
        {
           // int clone_id = m_clone_id;
            int refsequence_start = 0;
           /* CloneDescription clone_description = CloneDescription.getCloneDescription( clone_id);
            if ( clone_description== null) 
            {
                m_error_messages.add("Clone "+clone_id+" does not exist.");
                return ;
            }*/
             if ( m_clone_description.getCloneStatus() == IsolateTrackingEngine.PROCESS_STATUS_ER_ANALYZED_NO_MATCH ) 
            {
                m_error_messages.add("Clone "+m_clone_description.getCloneId()+" does not match exspected sequence: Gap Mapper run aborted.");
                return ;
            }
       //     if ( !isCallForGapMapperRelevant(clone_description) ) return;
                 
            //refsequence includes linker sequences 
            BaseSequence clone_refsequence  = getRefsequence();
            // build refsequence file
            PhredPhrap pp = new PhredPhrap();
            pp.setVectorFileName(m_vector_file_name);
            pp.setQualityTrimmingScore (m_quality_trimming_phd_score);
            pp.setQualityTrimmingLastBase(m_quality_trimming_phd_last_base);
            pp.setQualityTrimmingFirstBase (m_quality_trimming_phd_first_base);
            pp.setIsUseLQReadsForAssembly(  m_use_lqreads_for_assembly );
            pp.setIsDeleteLQReads( m_delete_lqreads);
   
                //delete all .phd files from previous processing
            FileOperations.deleteAllFilesFormDirectory(m_clone_description.getReadFilePath() + File.separator +"phd_dir");
      
            pp.createFakeTraceFile( clone_refsequence.getText(), clone_refsequence.getId(), 18,m_clone_description.getReadFilePath() , "refsequence");
            //run phrap assembler
            pp.run(m_clone_description.getReadFilePath() , "refseqassembly"+clone_refsequence.getId()+".fasta.screen.ace.1" );
            
            //parse phrap output extrcting exact read data
            PhredPhrapParser pparser = new PhredPhrapParser();
            //find gaps & contigs
            String refseq_read_name = m_clone_description.getReadFilePath() +File.separator+ "contig_dir"+File.separator+"refseqassembly"+clone_refsequence.getId()+".fasta.screen.ace.1";
            CloneAssembly clone_assembly = pparser.parseAllData( refseq_read_name );
  //it should be only one contig
            if ( clone_assembly == null || clone_assembly.getContigs()== null )
            {
                m_error_messages.add("Gap Mapper failed: no assembly was build for clone id "+m_clone_description.getCloneId());
                return;
            }
            else if ( clone_assembly != null && clone_assembly.getContigs().size() > 1)
            {
                m_error_messages.add("Gap Mapper failed: more than one contig("+ clone_assembly.getContigs().size() +" contigs) build for clone id "+m_clone_description.getCloneId());
                ArrayList not_assembled_reads = getNamesOfNotAssembledReads(m_clone_description, clone_assembly);
                if ( not_assembled_reads != null && not_assembled_reads.size() > 0 ) 
                    m_error_messages.addAll(not_assembled_reads);
                return;
            }
            int linker5_length = m_cloning_strategy.getLinker5().getSequence().length();
            int linker3_length = m_cloning_strategy.getLinker3().getSequence().length();
    
            ReadInAssembly read = null;
            boolean isContigComplement = false;
            for (int contig_count = 0; contig_count<   clone_assembly.getContigs().size(); contig_count++)
            {
                Contig contig =(Contig)clone_assembly.getContigs().get(contig_count);
                for (int read_count  = 0; read_count < contig.getReads().size();read_count++)
                {
                    read = (ReadInAssembly) contig.getReads().get(read_count);
                    if ( read.getQualityStart () == -1 && read.getQualityEnd() ==-1)
                        //exclude read
                    {
                        contig.setNumberOfReadsInContig(contig.getNumberOfReadsInContig() - 1);
                        contig.getReads().remove(contig_count);
                        contig_count++;
                        continue;
                    }
                    String scores = PhredPhrap.getScoresFromPhdFile(read.getName(), m_clone_description.getReadFilePath() + File.separator + "phd_dir");
                    int[] scores_as_array =  Algorithms.getConvertStringToIntArray( scores, " ") ;
                    //take into acount that phrap gives sense reading of the read in output while scores
                    // are in original direction
                    if ( read.getOrientation() == Constants.ORIENTATION_REVERSE)
                    {
                        scores_as_array = SequenceManipulation.complimentScores(scores_as_array);
                        if ( read.getName().indexOf("refsequence") != -1 )
                            isContigComplement = true;
                    }
                    if ( read.getName().toUpperCase().indexOf( "REFSEQUENCE") != -1)
                         refsequence_start = read.getQualityStart();//read.getAlignStart();//
                    read.setScores(scores_as_array);
                }
                
                ScoredElement[][]  bases = contig.prepareContigAligmentHorizontalStructure("refsequence", refsequence_start, linker5_length);
               // ArrayList gaps = Contig.findGaps(bases);
                ArrayList contigs = Contig.findContigs(bases, 
                                clone_refsequence.getText().length() - linker5_length - linker3_length,
                                linker5_length,
                                m_trimming_spec, isContigComplement);
                ArrayList gaps = Contig.findGaps(contigs, linker5_length, clone_refsequence.getText().length() );
                ArrayList stretches = contigs;
                stretches.addAll(gaps);
                
                if (( gaps != null && gaps.size() > 0) || (contigs != null  && contigs.size() > 0))
                {
                //return calculate gap positions to refseq coordinates - linkers?
                  // ArrayList stretches = recalculateStretches(gaps, contigs);
                    m_stretch_collection = new StretchCollection();
                    m_stretch_collection.setType ( StretchCollection.TYPE_COLLECTION_OF_GAPS_AND_CONTIGS);
                    m_stretch_collection.setRefSequenceId (  m_clone_description.getBecRefSequenceId());
                    m_stretch_collection.setIsolatetrackingId ( m_clone_description.getIsolateTrackingId());
                    m_stretch_collection.setCloneId (m_clone_description.getCloneId());
                    m_stretch_collection.setStretches( stretches);
                      
                }
            }
            
        }
        catch(Exception e)
        {
          //  System.out.println(e.getMessage());
            m_error_messages.add ("Cannot define contigs for clone "+ m_clone_description.getCloneId());
        }
    }
    
    //_______________________
    
    private  ArrayList      getNamesOfNotAssembledReads(CloneDescription clone_description, CloneAssembly clone_assembly)
    {
        ArrayList error_messages = new ArrayList();
        ReadInAssembly read = null; Contig contig = null;
        ArrayList file_names = new ArrayList ();
        for (int contig_count = 0; contig_count < clone_assembly.getContigs().size(); contig_count++)
        {
            contig = (Contig) clone_assembly.getContigs().get(contig_count);
            for (int read_count = 0; read_count < contig.getReads().size(); read_count++)
            {
                read = (ReadInAssembly) contig.getReads().get(read_count);
                if ( read.getName().indexOf("refsequence") != -1)
                {
                    file_names = new ArrayList();
                    break;
                }
                else
                {
                    file_names.add("Clone id: "+ clone_description.getCloneId() +"\t"+clone_description.getReadFilePath() +File.separator+PhredWrapper.CHROMAT_DIR_NAME +File.separator+read.getName());
                }
                
                if ( read_count == contig.getReads().size() - 1 && ( file_names!= null && file_names.size() > 0))
                {
                    error_messages.addAll(file_names);
                    file_names = new ArrayList();
                }
            }
        }
   
        return error_messages;
    }
               
   
    private boolean          isCallForGapMapperRelevant(CloneDescription clone_description)throws Exception
    {
        //check 1. there is no assembled sequence
        if (  CloneSequence.isAssembledSequenceExists(clone_description.getCloneId()))
        {
            m_error_messages.add("Call for GapMapper on Clone : "+clone_description.getCloneId()+" is irrelevant: assembled sequence exists");
            return false;
        }
        int assembly_run_status = CloneAssembly.isAssemblerRunNeeded(clone_description.getReadFilePath());
        switch(assembly_run_status)
        {
            case  CloneAssembly.ASSEMBLY_RUN_STATUS_NO_TRACE_FILES_AVAILABLE :
            {
                 m_error_messages.add("Call for GapMapper on Clone : "+clone_description.getCloneId()+" is irrelevant: no trace files exists for the clone.");
                return false;
            }
            case CloneAssembly.ASSEMBLY_RUN_STATUS_NOT_ALL_TRACE_FILES_INCLUDED :
            {
                 m_error_messages.add("Call for GapMapper on Clone : "+clone_description.getCloneId()+"can be irrelevant: assembler was not run on all existing chromat files?");
                return true;
            }
        }
        return true;  
    }
  
    private BaseSequence getRefsequence( )throws Exception
    {
        
       // BioLinker linker3 =  null;  BioLinker linker5 = null;
        BaseSequence clone_refsequence = null;
       
        /*m_cloning_startegy = CloningStrategy.getById( clone_description.getCloningStrategyId());
        if (m_cloning_startegy != null)
         {
             linker3 = BioLinker.getLinkerById( m_cloning_startegy.getLinker3Id() );
             m_cloning_startegy.setLinker3(linker3);
             linker5 = BioLinker.getLinkerById( m_cloning_startegy.getLinker5Id() );
             m_cloning_startegy.setLinker5(linker5);
         }*/
         RefSequence refsequence = new RefSequence( m_clone_description.getBecRefSequenceId());
        // if (refsequence != null && linker3 != null && linker5 != null) 
         if ( refsequence != null && m_cloning_strategy.getLinker3().getSequence()!= null &&
                 m_cloning_strategy.getLinker5().getSequence() != null )
         {
            clone_refsequence =  new BaseSequence(refsequence.getCodingSequence(), BaseSequence.BASE_SEQUENCE );
            clone_refsequence.setId(m_clone_description.getFlexSequenceId());
            clone_refsequence.setText( m_cloning_strategy.getLinker5().getSequence() + clone_refsequence.getText()+m_cloning_strategy.getLinker3().getSequence());
         }
         return clone_refsequence ;

    }
}
