/*
 * GapMapper.java
 ** @author  HTaycher
 * Created on May 3, 2004, 4:48 PM
 */

package edu.harvard.med.hip.bec.modules;
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.database.*;
import  edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.util_objects.*;
import  edu.harvard.med.hip.bec.bioutil.*;
import edu.harvard.med.hip.bec.Constants;
import edu.harvard.med.hip.bec.programs.assembler.*;
import edu.harvard.med.hip.bec.sampletracking.objects.*;
import java.util.*;
import java.sql.*;
import java.io.*;

// work on clone base
public class GapMapper
{
    private int                         m_clone_id = -1;
    private ArrayList                   m_error_messages = null;
    private CloningStrategy             m_cloning_startegy = null;
    private StretchCollection           m_stretch_collection = null;
    private SlidingWindowTrimmingSpec       m_trimming_spec = null;
    private boolean                         m_isRunLQRFinderForContigs = false;
   
   
    
   // private int                     m_min_contig_avg_score = 20;
    // private int                     m_min_contig_length = MIN_CONTIG_LENGTH;
    /** Creates a new instance of GapMapper */
    public GapMapper()
    {
        m_error_messages = new ArrayList();
    }
    
    public void                 setCloneId(int v){ m_clone_id = v;}
    
    public void                 setIsRunLQR(boolean v){m_isRunLQRFinderForContigs = v;}
    public ArrayList            getErrorMessages(){ return m_error_messages; }
    public StretchCollection    getStretchCollection(){ return   m_stretch_collection ;}
   // public void                 setMinContigLength(int v){ m_min_contig_length = v;}
   // public void                 setMinAvgContigScore(int v){  m_min_contig_avg_score = v;}
    public void         setTrimmingSpec(SlidingWindowTrimmingSpec   v){    m_trimming_spec = v;}
  
    public void                 run()
    {
        //get refsequence & linker information
        try
        {
            int clone_id = m_clone_id;
            CloneDescription clone_description = CloneDescription.getCloneDescription( clone_id);
            if ( clone_description== null) 
            {
                m_error_messages.add("Clone "+clone_id+" does not exist.");
                return ;
            }
            if ( !isCallForGapMapperRelevant(clone_description) ) return;
                 
            //refsequence includes linker sequences 
            BaseSequence clone_refsequence  = getRefsequence(clone_description);
            // build refsequence file
            PhredPhrap pp = new PhredPhrap();
            pp.createFakeTraceFile( clone_refsequence.getText(), clone_refsequence.getId(), 18,clone_description.getReadFilePath() , "refsequence");
            //run phrap assembler
            pp.run(clone_description.getReadFilePath() , "refseqassembly"+clone_refsequence.getId()+".fasta.screen.ace.1" );
            
            //parse phrap output extrcting exact read data
            PhredPhrapParser pparser = new PhredPhrapParser();
            //find gaps & contigs
            CloneAssembly clone_assembly = pparser.parseAllData(clone_description.getReadFilePath() +File.separator+ "contig_dir"+File.separator+"refseqassembly"+clone_refsequence.getId()+".fasta.screen.ace.1");
  //it should be only one contig
            if ( clone_assembly == null || clone_assembly.getContigs()== null || clone_assembly.getContigs().size() != 1)
            {
                m_error_messages.add("Gap Mapper failed: cannot assemble reads with reference sequence for clone id "+clone_description.getCloneId());
                return;
            }
            ReadInAssembly read = null;
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
                    String scores = PhredPhrap.getScoresFromPhdFile(read.getName(), clone_description.getReadFilePath() + File.separator + "phd_dir");
                    int[] scores_as_array =  Algorithms.getConvertStringToIntArray( scores, " ") ;
                    //take into acount that phrap gives sense reading of the read in output while scores
                    // are in original direction
                    if ( read.getOrientation() == Constants.ORIENTATION_REVERSE)
                    {
                        int[] compliment_scores = new int[scores_as_array.length];
                        for (int ii =  scores_as_array.length ;  ii > 0; ii--)
                        {
                            compliment_scores[scores_as_array.length - ii ] = scores_as_array[ii - 1];
                        }
                        scores_as_array = compliment_scores;
                    }
                    read.setScores(scores_as_array);
                }
                int linker5_length = m_cloning_startegy.getLinker5().getSequence().length();
    
                ScoredElement[][]  bases = contig.prepareContigAligmentHorizontalStructure("refsequence", linker5_length);
               // ArrayList gaps = Contig.findGaps(bases);
                ArrayList contigs = Contig.findContigs(bases, clone_refsequence.getText().length(), linker5_length, m_trimming_spec);
               
                ArrayList gaps = Contig.findGaps(contigs, linker5_length, clone_refsequence.getText().length() );
                ArrayList stretches = contigs;
                stretches.addAll(gaps);
                
                if (gaps != null && contigs != null && gaps.size() > 0 && contigs.size() > 0)
                {
                //return calculate gap positions to refseq coordinates - linkers?
                  // ArrayList stretches = recalculateStretches(gaps, contigs);
                    m_stretch_collection = new StretchCollection();
                    m_stretch_collection.setType ( StretchCollection.TYPE_COLLECTION_OF_GAPS_AND_CONTIGS);
                    m_stretch_collection.setRefSequenceId (  clone_description.getBecRefSequenceId());
                    m_stretch_collection.setIsolatetrackingId ( clone_description.getIsolateTrackingId());
                    m_stretch_collection.setCloneId (clone_description.getCloneId());
                    m_stretch_collection.setStretches( stretches);
                      
                }
            }
            
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            m_error_messages.add ("Cannot define contigs for clone "+ m_clone_id);
        }
    }
    
    //_______________________
    
   
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
                 m_error_messages.add("Call for GapMapper on Clone : "+clone_description.getCloneId()+" is irrelevant: assembly was not run on all existing chromat files.");
                return false;
            }
        }
        return true;  
    }
  
    private BaseSequence getRefsequence(CloneDescription clone_description)throws Exception
    {
        
        BioLinker linker3 =  null;  BioLinker linker5 = null;
        BaseSequence clone_refsequence = null;
       
        m_cloning_startegy = Container.getCloningStrategy(clone_description.getContainerId());
        if (m_cloning_startegy != null)
         {
             linker3 = BioLinker.getLinkerById( m_cloning_startegy.getLinker3Id() );
             m_cloning_startegy.setLinker3(linker3);
             linker5 = BioLinker.getLinkerById( m_cloning_startegy.getLinker5Id() );
             m_cloning_startegy.setLinker5(linker5);
         }
         RefSequence refsequence = new RefSequence( clone_description.getBecRefSequenceId());
         if (refsequence != null && linker3 != null && linker5 != null)   
         {
            clone_refsequence =  new BaseSequence(refsequence.getCodingSequence(), BaseSequence.BASE_SEQUENCE );
            clone_refsequence.setId(clone_description.getFlexSequenceId());
            clone_refsequence.setText( linker5.getSequence() + clone_refsequence.getText()+linker3.getSequence());
         }
         return clone_refsequence ;

    }
}
