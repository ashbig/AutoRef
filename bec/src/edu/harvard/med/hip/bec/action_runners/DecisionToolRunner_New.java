/*
 * DecisionTool.java
 *
 * Created on March 11, 2004, 4:30 PM
 */

package edu.harvard.med.hip.bec.action_runners;



import java.sql.*;
import java.io.*;

import edu.harvard.med.hip.bec.coreobjects.endreads.*;
import edu.harvard.med.hip.bec.coreobjects.feature.*;
import  edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.file.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.user.*;
import edu.harvard.med.hip.bec.*;
import edu.harvard.med.hip.bec.programs.phred.*;
import edu.harvard.med.hip.bec.util_objects.*;
  import java.util.*;
  import edu.harvard.med.hip.utility.*;
  import edu.harvard.med.hip.bec.programs.assembler.*;
/**
 *
 * @author  HTaycher
 */
  public class DecisionToolRunner_New extends ProcessRunner
{

    private static final int    TOTAL_NUMBER_OF_BASES_TO_COVER_BY_END_READ = 200;

    private FullSeqSpec         m_spec = null;
    private int                 m_spec_id = -1;
    private int                 m_number_of_files = Constants.OUTPUT_TYPE_ONE_FILE;
    private String              m_user_comment = null;

    private  GroupDefinition[] m_group_definitions = null;
    private  SpeciesIdHelper[] m_species_id_definitions = null;
    //print options
    private boolean 				 m_is_plate_label = false;//    Plate Label</td>
private boolean 				 m_is_sample_type = false;//    Sample Type</td>
private boolean 				 m_is_position = false;//    Well</td>
private boolean 				 m_is_ref_sequence_id = false;//    Sequence ID</td>
private boolean 				 m_is_clone_seq_id = false;//    Clone Sequence Id</td>
private boolean 				 m_is_ref_cds_start = false;//    CDS Start</td>
private boolean 				 m_is_clone_sequence_assembly_status = false;//    Clone Sequence assembly attempt status    </td>
private boolean 				 m_is_ref_cds_stop = false;//    CDS Stop</td>
private boolean 				 m_is_clone_sequence_analysis_status = false;//Clone Sequence Analysis Status  </td>
private boolean 				 m_is_ref_cds_length = false;//    CDS Length</td>
private boolean 				 m_is_clone_sequence_cds_start = false;//Cds Start</td>
private boolean 				 m_is_ref_seq_text = false;//   Sequence Text</td>
private boolean 				 m_is_clone_sequence_cds_stop = false;// Cds Stop </td>
private boolean 				 m_is_ref_seq_cds = false;//    CDS</td>
private boolean 				 m_is_clone_sequence_text = false;//Clone Sequence  </td>
private boolean 				 m_is_clone_sequence_cds = false;//Clone Sequence  </td>


private boolean 				 m_is_ref_gene_symbol = false;// Gene Symbol</td>
private boolean 				 m_is_clone_sequence_disc_high = false;// Discrepancies High Quality (separated by type)</td>
private boolean 				 m_is_ref_gi = false;//    GI Number</td>
private boolean 				 m_is_clone_sequence_disc_low = false;
private boolean 				 m_is_ref_5_linker = false;//5' linker sequence    </td>
private boolean 				 m_is_clone_sequence_disc_det = false; //Detailed Discrepancy Report </td>
private boolean 				 m_is_ref_3_linker = false;//   3' linker sequence</td>
private boolean 				 m_is_ref_species_id = false;//Species specific ID</td>
private boolean 				 m_is_ref_locusid = false; //All available identifiers</td>

    public void                 setSpecId(int v){ m_spec_id = v;}
    public void                 setUserComment(String v){ m_user_comment = v;}
    public void                 setNumberOfOutputFiles(int v){ m_number_of_files= v;}
    public String               getTitle()    {return "Request for Decision tool run";    }
    public void                 setFields(
                        Object is_plate_label,//    Plate Label</td>
                        Object is_sample_type,//    Sample Type</td>
                        Object is_position,//    Well</td>
                        Object is_ref_sequence_id,//    Sequence ID</td>
                        Object is_clone_seq_id,//    Clone Sequence Id</td>
                        Object is_ref_cds_start,//    CDS Start</td>
                        Object is_clone_sequence_assembly_status,//    Clone Sequence assembly attempt status    </td>
                        Object is_ref_cds_stop,//    CDS Stop</td>
                        Object is_clone_sequence_analysis_status,//Clone Sequence Analysis Status  </td>
                        Object is_ref_cds_length,//    CDS Length</td>
                        Object is_clone_sequence_cds_start,//Cds Start</td>
                        Object is_ref_seq_text,//   Sequence Text</td>
                        Object is_clone_sequence_cds_stop,// Cds Stop </td>
                        Object is_ref_seq_cds,//    CDS</td>
                        Object is_clone_sequence_text,//Clone Sequence  </td>
                        Object is_clone_sequence_cds,
                        Object is_ref_gene_symbol,// Gene Symbol</td>
                        Object is_clone_sequence_disc_high,// Discrepancies High Quality (separated by type)</td>
                        Object is_ref_gi,//    GI Number</td>
                        Object is_clone_sequence_disc_low,
                        Object is_ref_5_linker,//5' linker sequence    </td>
                        Object is_clone_sequence_disc_det, //Detailed Discrepancy Report </td>
                        Object is_ref_3_linker,//   3' linker sequence</td>
                        Object is_ref_species_id,//Species specific ID</td>
                        Object is_ref_locusid//All available identifiers</td>
                        )
   {
          if(   is_plate_label != null ) { m_is_plate_label = true; }
        if(  is_sample_type != null ) { m_is_sample_type = true; }
        if(  is_position != null ) { m_is_position = true;  }

        if(  is_ref_sequence_id != null ) { m_is_ref_sequence_id = true; }
        if(  is_ref_cds_start != null ) { m_is_ref_cds_start = true;  }
        if(  is_ref_cds_stop != null ) { m_is_ref_cds_stop = true;  }
        if(  is_ref_cds_length != null ) { m_is_ref_cds_length = true; }
        if(  is_ref_gene_symbol != null ) { m_is_ref_gene_symbol = true;  }
        if(  is_ref_gi != null ) { m_is_ref_gi = true;   }
        if(  is_ref_locusid != null ) { m_is_ref_locusid  = true;   }
        if(  is_ref_species_id != null ){    m_is_ref_species_id = true;   }

        if(  is_clone_seq_id != null ) { m_is_clone_seq_id = true; }
        if(  is_clone_sequence_assembly_status != null ) { m_is_clone_sequence_assembly_status = true;      }
        if(  is_clone_sequence_analysis_status != null ) { m_is_clone_sequence_analysis_status = true;  }
        if(  is_clone_sequence_cds_start != null ) { m_is_clone_sequence_cds_start = true;}
        if(  is_clone_sequence_cds_stop != null ) { m_is_clone_sequence_cds_stop = true;  }
        if(  is_clone_sequence_disc_high != null ) { m_is_clone_sequence_disc_high = true;    }
        if(  is_clone_sequence_disc_low != null ) { m_is_clone_sequence_disc_low = true;}
        if(  is_clone_sequence_disc_det != null ) { m_is_clone_sequence_disc_det = true;     }

        if(  is_ref_5_linker != null ) { m_is_ref_5_linker = true;   }
        if(  is_ref_3_linker != null ) { m_is_ref_3_linker = true;    }

        if ( is_clone_sequence_cds != null ) { m_is_clone_sequence_cds = true;    }
        if(  is_ref_seq_cds != null ) { m_is_ref_seq_cds = true; }

        if(  is_clone_sequence_text != null ) { m_is_clone_sequence_cds = true;     }
        if(  is_ref_seq_text != null ) { m_is_ref_seq_text = true;   }

    }



    public DecisionToolRunner_New()
    {
        super();
        setGroupDefinitions();
        biuldSpeciesIdDefinitions();
    }

    private void      biuldSpeciesIdDefinitions  ()
    {
        int species = DatabaseToApplicationDataLoader.getSpecies().size();
        m_species_id_definitions = new SpeciesIdHelper[species];
        SpeciesDefinition sd = null;
        for (Enumeration e = DatabaseToApplicationDataLoader.getSpecies().elements() ; e.hasMoreElements() ;)
        {
             sd = (SpeciesDefinition) e.nextElement();
             if ( sd.getIdName() != null && sd.getIdName().trim().length() > 0)
                m_species_id_definitions[sd.getCode()-1] = new SpeciesIdHelper(sd.getCode(), sd.getIdName());
        }

    }

    private  void setGroupDefinitions()
    {
        m_group_definitions = new GroupDefinition[GroupDefinition.GROUP_NUMBER];
        GroupDefinition group_definition = null; int count = 0;
       //    "Accepted",// (Full sequence coverage and meet the acceptance criteria)
        group_definition = new GroupDefinition("Accepted","DecisionTool_Accepted", GroupDefinition.GROUP_TYPE_ACCEPTED);
        m_group_definitions[GroupDefinition.GROUP_TYPE_ACCEPTED] = group_definition;
	//"Rejected",// (Meet the rejection criteria with/without full sequence coverage)
        group_definition = new GroupDefinition("Rejected","DecisionTool_Rejected", GroupDefinition.GROUP_TYPE_REJECTED);
        m_group_definitions[GroupDefinition.GROUP_TYPE_REJECTED] = group_definition;
//	Need further analysis (7 groups):No trace file (no trace files stored for the clone on hard drive)
        group_definition = new GroupDefinition("No trace files: Need further analysis","DecisionTool_NFA_NoTraceFiles", GroupDefinition.GROUP_TYPE_NO_TRACE_FILES);
        m_group_definitions[GroupDefinition.GROUP_TYPE_NO_TRACE_FILES] = group_definition;
//	Need further analysis 	End reads not analyzed (trace files are available but not processed)
// group_definition = new GroupDefinition("End reads not analyzed: Need further analysis","DecisionTool_NFA_ER_NotAnalyzed", GroupDefinition.GROUP_TYPE_ER_NOT_ANALYZED);
  //      m_group_definitions[GroupDefinition.GROUP_TYPE_ER_NOT_ANALYZED] = group_definition;
//	Need further analysis 	Assembly not attempted
 group_definition = new GroupDefinition("Assembly not attempted: Need further analysis","DecisionTool_NFA_AssemblyNotAttempted", GroupDefinition.GROUP_TYPE_ASSEMBLY_NOT_ATTEMPTED);
        m_group_definitions[GroupDefinition.GROUP_TYPE_ASSEMBLY_NOT_ATTEMPTED] = group_definition;
//	Need further analysis 	Assembled not analyzed
 group_definition = new GroupDefinition("Clone data not analyzed: Need further analysis","DecisionTool_NFA_NotAnalized", GroupDefinition.GROUP_TYPE_NOT_ANALYZED);
        m_group_definitions[GroupDefinition.GROUP_TYPE_NOT_ANALYZED] = group_definition;
//	Need further analysis 	Lack full sequence coverage (This is dominant over Group 6 - that is, if a clone lacks full sequence coverage and also has LQDs, it should be included in this group)
group_definition = new GroupDefinition("No full sequence coverage: Need further analysis","DecisionTool_NFA_NoFullCoverage", GroupDefinition.GROUP_TYPE_NOT_FULL_COVERAGE);
        m_group_definitions[GroupDefinition.GROUP_TYPE_NOT_FULL_COVERAGE] = group_definition;
        //	Need further analysis 	Persistent low quality discrepancies (Clones that are neither "Rejected" nor "Accepted", are not included in any previous group and still contain LQDs)
group_definition = new GroupDefinition("Persistent LQ Discrepancy: Need further analysis","DecisionTool_NFA_PersistentLQD", GroupDefinition.GROUP_TYPE_LQ_DISCREPANCY);
        m_group_definitions[GroupDefinition.GROUP_TYPE_LQ_DISCREPANCY] = group_definition;
        //	Need further analysis 	Other (clones failed to meet either acceptance criteria or rejection criteria, are not in any other group
group_definition = new GroupDefinition("Other: Need further analysis","DecisionTool_NFA_Other", GroupDefinition.GROUP_TYPE_OTHER);
        m_group_definitions[GroupDefinition.GROUP_TYPE_OTHER] = group_definition;

 group_definition = new GroupDefinition("No match","DecisionTool_NO_MATCH", GroupDefinition.GROUP_TYPE_NO_MATCH);
        m_group_definitions[GroupDefinition.GROUP_TYPE_NO_MATCH] = group_definition;
group_definition = new GroupDefinition("Manul Review High Quality Discrepancies","DecisionTool_Manula_Review_HQD", GroupDefinition.GROUP_TYPE_MANUAL_REVIEW_HQD);
        m_group_definitions[GroupDefinition.GROUP_TYPE_MANUAL_REVIEW_HQD] = group_definition;


    }



    public void run()
    {
        // ArrayList file_list = new ArrayList();
        CloneDescription[] clones = new CloneDescription[100];
        String time_stamp = String.valueOf( System.currentTimeMillis() );
        String  summary_report_file_name =  Constants.getTemporaryFilesPath() +  "DecisionToolSummaryReport" + time_stamp +".txt";
        String  total_report_file_name =  Constants.getTemporaryFilesPath() +  "DecisionToolTotalReport" + time_stamp +".txt";
        ArrayList sql_groups_of_items = new ArrayList();
        ArrayList isolate_trackingid_list = new ArrayList();
        Hashtable linkers = null; Hashtable refsequences = null;
        try
        {
           m_spec = (FullSeqSpec)Spec.getSpecById(m_spec_id);
//returns string array: 100 isolates in each
           isolate_trackingid_list = getListOfIsolateTrackingId();

           EndReadsWrapperRunner erw = new EndReadsWrapperRunner();
           String trace_files_path = erw.getOuputBaseDir();

           for (int count = 0; count < isolate_trackingid_list.size(); count++)
           {
               //recet array
               for (int ii = 0; ii < clones.length; ii++){ clones[ii]= null;}
               try
               {
                   // get clone information for up to 100 clones, exception per block
                   clones =  getCloneData( (String) isolate_trackingid_list.get(count), clones);
                    // collect information to print; exception per item
                  refsequences = getRefSequencesAndCloneSequencetText( clones  );
                  linkers = getLinkers( clones, linkers);

                   //assign status to clone
                   clones = analyzeClones(clones, trace_files_path, m_spec,  refsequences,linkers);
                    //print
                   printReport( total_report_file_name, time_stamp, clones, count,
                             linkers,  refsequences);
               }
               catch(Exception e)
               {
                   m_error_messages.add("Cannot constract report for items (isolate tracking ids):"+(String) isolate_trackingid_list.get(count));
               }
           }
           printSummaryFile(summary_report_file_name, m_spec);
           attachFiles(summary_report_file_name, total_report_file_name, time_stamp);
        }
        catch(Exception ex)
        {
            m_error_messages.add(ex.getMessage());
        }
        finally
        {
            sendEMails( getTitle() );
        }

    }


    //------------------------------------
    private CloneDescription[]  analyzeClones(CloneDescription[] clones,
                                                String trace_files_path,
                                                FullSeqSpec spec,
                                                Hashtable refsequences,
                                                Hashtable linkers
                                                )
    {
        CloneDescription clone = null;
        for (int count = 0; count < clones.length; count ++)
        {
            clone = clones[count];if ( clone == null) break;
              //define group
              try
              {
                defineCloneGroup(clone, trace_files_path, spec, refsequences,linkers);
              }
              catch(Exception e)
              {
                  m_error_messages.add("Cannot process data for clone "+ clone.getCloneId());
              }
         }
        return clones;
    }

    private void            defineCloneGroup(CloneDescription clone ,
                                            String trace_files_path,
                                            FullSeqSpec spec,
                                            Hashtable refsequences,
                                            Hashtable linkers) throws Exception
    {
        //full sequence no discrepancies
        boolean is5end_finished = false;
        boolean is3end_finished = false;
        clone.setNextStepRecomendation("");

        if (clone.getCloneStatus() == IsolateTrackingEngine.PROCESS_STATUS_ER_ANALYZED_NO_MATCH
        || clone.getCloneStatus() == IsolateTrackingEngine.PROCESS_STATUS_CLONE_SEQUENCE_ANALYZED_NO_MATCH)
        {
            clone.setNextStepRecomendation("Run report 'show no match to the expected ORF'.");
            clone.setRank( GroupDefinition.GROUP_TYPE_NO_MATCH );
            return;
        }
   //-----------------------------------------------------------------------------------
        if (clone.getCloneSequenceId() > 0 &&  clone.getCloneSequenceAnalysisStatus() == BaseSequence.CLONE_SEQUENCE_STATUS_ANALIZED_NO_DISCREPANCIES)
        {
            is5end_finished =  isUpstreamLinkerCovered( clone.getCloneSequenceCdsStart() ,   clone.getCloningStrategyId(),   linkers);
            is3end_finished =  isDownstreamLinkerCovered(clone.getCloningStrategyId(), 
                        clone.getCloneSequenceCdsStop(), clone.getCloneSequence3LinkerStop(),  linkers);
  
            if ( is5end_finished && is3end_finished)
            {
                clone.setNextStepRecomendation("Clone finished");
                clone.setRank( GroupDefinition.GROUP_TYPE_ACCEPTED );
                return;
            }
            else if (! is5end_finished)
            {
                clone.setNextStepRecomendation( "Repeat ER: Forward;");
                clone.setRank( GroupDefinition.GROUP_TYPE_NOT_FULL_COVERAGE );
            }
            else if ( ! is3end_finished )
            {
                clone.setNextStepRecomendation(clone.getNextStepRecomendation()+" Repeat ER: Reverse;");
                clone.setRank( GroupDefinition.GROUP_TYPE_NOT_FULL_COVERAGE );
            }
            return;
        }
//--------------------------------------------------------------------------------
        //define 'no trace files'
        String trace_dir =   trace_files_path +File.separator +clone.getFlexSequenceId() + File.separator + clone.getCloneId(); //trace file directory
        //////*********************** for testing ***************************
      //  trace_dir = "Z:\\trace_files_root\\clone_samples" + File.separator +clone.getFlexSequenceId() + File.separator + clone.getCloneId(); //trace file directory"

        int numberOfFilesStatus = CloneAssembly.isAssemblerRunNeeded(trace_dir, 0);
        if ( numberOfFilesStatus ==  CloneAssembly.ASSEMBLY_RUN_STATUS_NO_TRACE_FILES_AVAILABLE
                && clone.getCloneSequenceId() < 0)
        {
                clone.setNextStepRecomendation("Get trace files;");
                clone.setRank(GroupDefinition.GROUP_TYPE_NO_TRACE_FILES);
                return;
        }
//------------------------------------------------------------------------------------------
        if ( numberOfFilesStatus ==  CloneAssembly.ASSEMBLY_RUN_STATUS_NOT_ALL_TRACE_FILES_INCLUDED)
        {
            clone.setNextStepRecomendation("Rerun assembler - not all trace files used;");
            clone.setRank(GroupDefinition.GROUP_TYPE_NOT_ANALYZED );
            return;
        }
 //------------------------------------------------------------------


        //analyze set of discrepancies
        getCloneDiscrepancies( clone, linkers ,  refsequences);

 //--------------------------------------
        if ( clone.getCloneAnalysisStatus() == CloneDescription.CLONE_ANALYSIS_STATUS_NOT_ANALYZED)
        {
            clone.setNextStepRecomendation("Run Discrepancy Finder.");
            clone.setRank(GroupDefinition.GROUP_TYPE_NOT_ANALYZED );
            return;
        }
 //-------------------------------------------------
        String additional_recommendation = "";RefSequence refsequence = null;

        int clone_quality =  DiscrepancyDescription.defineQuality( clone.getCloneDiscrepancies() ,m_spec );

  //---------------------------------------------------------
        if (clone_quality == BaseSequence.QUALITY_BAD)
        {
            clone.setNextStepRecomendation("Clone rejected;");
            clone.setRank( GroupDefinition.GROUP_TYPE_REJECTED );
            return;
        }
 //----------------------------------------------------------------

// find if end covered
        is5end_finished = clone.isForwardERNeeded( );
         if ( clone.getBecRefSequenceId()>0)
          {
             refsequence = (RefSequence)refsequences.get(new Integer(clone.getBecRefSequenceId()));
          }

         is3end_finished = clone.isReverseERNeeded();
           if ( is5end_finished) additional_recommendation = " Forward End read needed;";
           if (  is3end_finished) additional_recommendation += " Reverse End read needed;";

        boolean isSequenceExists = ( clone.getCloneSequenceId() > 0)      ;

        if ( !isSequenceExists )
        {
            if (  clone.getCloneAnalysisStatus() ==  CloneDescription.CLONE_ANALYSIS_STATUS_ANALYZED_CONTIGS)
            {
                additional_recommendation += "Run primers design; " ;
            }
            else
                additional_recommendation += "Run Gap Mapper;";
        } 
        if (clone_quality == BaseSequence.QUALITY_GOOD )
        {
            if ( isSequenceExists && ! is5end_finished && ! is3end_finished )
            {
                clone.setNextStepRecomendation("Clone finished");
                clone.setRank( GroupDefinition.GROUP_TYPE_ACCEPTED );
                return;
            }
            else
            {
                 clone.setNextStepRecomendation(clone.getNextStepRecomendation() + "Partial coverage;" + additional_recommendation);
                clone.setRank( GroupDefinition.GROUP_TYPE_NOT_FULL_COVERAGE );
                return;
            }
        }

        else if (clone_quality == BaseSequence.QUALITY_REVIEW)
        {
            if ( isSequenceExists )
            {
                //if at least one low quality discrepancy
                int number_of_discrepancies[] = DiscrepancyDescription.getTotalNumberOfDiscrepanciesByQuality  ( clone.getCloneDiscrepancies() );
                if ( number_of_discrepancies[1] > 0 )
                {
                    clone.setNextStepRecomendation(clone.getNextStepRecomendation() + "Run LQ Finder for clone sequence. " + number_of_discrepancies[1] + " LQ discrepancies;" + additional_recommendation);
                    clone.setRank( GroupDefinition.GROUP_TYPE_LQ_DISCREPANCY );
                    return;
                }
                else
                {
                    clone.setNextStepRecomendation(clone.getNextStepRecomendation() + "Only high quality discrepancies ( " + number_of_discrepancies[1] + ") ;" + additional_recommendation);
                    clone.setRank( GroupDefinition.GROUP_TYPE_MANUAL_REVIEW_HQD );
                    return;
                }
            }
            else // no sequence
            {
                clone.setNextStepRecomendation(clone.getNextStepRecomendation() + additional_recommendation);
                clone.setRank( GroupDefinition.GROUP_TYPE_NOT_FULL_COVERAGE );
                return;
            }


        }
        clone.setNextStepRecomendation(clone.getNextStepRecomendation() + "Review. " + additional_recommendation);
         clone.setRank( GroupDefinition.GROUP_TYPE_OTHER );
         return;
    }




    private boolean         isUpstreamLinkerCovered(int  clone_cds_start ,   int  clone_cloning_strategy_id,  Hashtable linkers)
    {
        BioLinker linker = (BioLinker)linkers.get(new Integer(clone_cloning_strategy_id));
        return    ( clone_cds_start >= linker.getSequence().length());
    }

    private boolean         isDownstreamLinkerCovered(int clone_cloning_strategy_id, 
                        int clone_cds_stop, int clone_linker3_stop, Hashtable linkers)
    {
         BioLinker linker = (BioLinker)linkers.get(new Integer(-clone_cloning_strategy_id));
         return    ( clone_linker3_stop - clone_cds_stop >= linker.getSequence().length() - 1 - 5);
    }


    //assumption: array of discrepancy discriptions is sorted by position
     private boolean         isUpstreamEndReadNeededBasedOnDiscrepancyPresence(ArrayList discrepancies,
                                              int clone_cloning_strategy_id, 
                                             Hashtable linkers)
    {
         if ( discrepancies == null || discrepancies.size() < 1) return false;
         Mutation discr = null;

         BioLinker linker = (BioLinker)linkers.get(new Integer(clone_cloning_strategy_id));
         int gene_region_length = TOTAL_NUMBER_OF_BASES_TO_COVER_BY_END_READ - linker.getSequence().length();

         DiscrepancyDescription dd = (DiscrepancyDescription)discrepancies.get(0);
        if( dd.getDiscrepancyDefintionType() == DiscrepancyDescription.TYPE_AA )
        {
           discr = (Mutation)dd.getRNACollection().get(dd.getRNACollection().size() - 1) ;
        }
        else   if( dd.getDiscrepancyDefintionType() ==  DiscrepancyDescription.TYPE_NOT_AA_LINKER
            || dd.getDiscrepancyDefintionType() ==  DiscrepancyDescription.TYPE_NOT_AA_AMBIQUOUS )
        {
            discr = dd.getRNADefinition();
        }

         if ( discr.getType() == Mutation.LINKER_5P && discr.getQuality() == Mutation.QUALITY_LOW  )return true;
         if ( discr.getPosition() <= gene_region_length && discr.getQuality() == Mutation.QUALITY_LOW ) return true;
         return false;

    }

      //assumption: array of discrepancy discriptions is sorted by position

    private boolean         isDownstreamEndReadNeededBasedOnDiscrepancyPresence(ArrayList discrepancies,
                                              int clone_cloning_strategy_id, Hashtable linkers,
                                              int refseq_length)
    {

        if ( discrepancies == null || discrepancies.size() < 1) return false;

          Mutation discr = null;

         BioLinker linker = (BioLinker)linkers.get(new Integer(-clone_cloning_strategy_id));
         int gene_region_length = refseq_length - ( TOTAL_NUMBER_OF_BASES_TO_COVER_BY_END_READ - linker.getSequence().length());
         DiscrepancyDescription  dd = (DiscrepancyDescription)discrepancies.get(discrepancies.size() - 1);

         if( dd.getDiscrepancyDefintionType() == DiscrepancyDescription.TYPE_AA )
        {
            discr = (Mutation)dd.getRNACollection().get(0) ;
        }
        else   if( dd.getDiscrepancyDefintionType() ==  DiscrepancyDescription.TYPE_NOT_AA_LINKER
        || dd.getDiscrepancyDefintionType() ==  DiscrepancyDescription.TYPE_NOT_AA_AMBIQUOUS        )
        {
            discr = dd.getRNADefinition();
        }

        if ( discr.getType() == Mutation.LINKER_3P && discr.getQuality() == Mutation.QUALITY_LOW ) return true;
        if ( discr.getPosition() > gene_region_length && discr.getQuality() == Mutation.QUALITY_LOW ) return true;
         return false;

    }






    private void            getCloneDiscrepancies(CloneDescription clone , Hashtable linkers,  Hashtable refsequences) throws Exception
    {
        //System.out.println("get clone discr");
        ArrayList clone_discrepancies = new ArrayList();
        ArrayList contigs = null;
        Read read = null;
        boolean is_cds_start_set = false;

        try
        {
            RefSequence refsequence = (RefSequence)refsequences.get(new Integer(clone.getBecRefSequenceId()));
            int  refsequence_cds_length = refsequence.getCdsStop() - refsequence.getCdsStart();
    
            if ( clone.getCloneSequenceId() > 0)
            {
                getCloneDiscrepanciesForCloneSequence(clone, linkers, refsequence_cds_length);
            }
            else
            {
                ArrayList temp = null;
                contigs = IsolateTrackingEngine.getStretches( clone.getIsolateTrackingId(), Stretch.GAP_TYPE_CONTIG  );
                if (contigs != null && contigs.size() > 0)
                {
                    getCloneDiscrepanciesForCloneStretchCollection(contigs,  clone ,  linkers, refsequence_cds_length);
                }
                else
                {
                   getCloneDiscrepanciesForER( clone ,  linkers,  refsequence_cds_length);
                }
            }
            
        }
        catch(Exception e)
        {
            m_error_messages.add("Cannot extract discrepancies for clone : "+clone.getCloneId() +" "+e.getMessage());
        }
    }
//-------------------------------------------------------------------------------------------
//builds clone discrepancies for clone with assembled sequence
    private void                getCloneDiscrepanciesForCloneSequence(CloneDescription clone , 
                Hashtable linkers, int refsequence_cds_length) throws Exception
    {
        ArrayList clone_discrepancies = new ArrayList();
       
       if ( clone.getCloneSequenceAnalysisStatus() == BaseSequence.CLONE_SEQUENCE_STATUS_ASSEMBLED )
        {
                clone.setCloneAnalysisStatus(CloneDescription.CLONE_ANALYSIS_STATUS_NOT_ANALYZED);
                return;
        }
        clone.setCloneAnalysisStatus( CloneDescription.CLONE_ANALYSIS_STATUS_ANALYZED_SEQUENCE);
        clone_discrepancies = Mutation.getDiscrepanciesBySequenceId(clone.getCloneSequenceId());
        clone_discrepancies = DiscrepancyDescription.assembleDiscrepancyDefinitions( clone_discrepancies);
        clone.setCloneDiscrepancies( clone_discrepancies );
        //determine need for end reads
        clone.setIsForwardERNeeded(
                ! isUpstreamLinkerCovered(clone.getCloneSequenceCdsStart() ,   clone.getCloningStrategyId(),  
                linkers));
        if ( ! clone.isForwardERNeeded())
        {
              clone.setIsForwardERNeeded(   isUpstreamEndReadNeededBasedOnDiscrepancyPresence(
                                     clone.getCloneDiscrepancies() ,
                                     clone.getCloningStrategyId(),   linkers) );
        }
        clone.setIsReverseERNeeded(! isDownstreamLinkerCovered(clone.getCloningStrategyId(), 
                clone.getCloneSequenceCdsStop(), clone.getCloneSequence3LinkerStop(),  linkers));
        if ( ! clone.isReverseERNeeded() )
        {
                clone.setIsReverseERNeeded( isDownstreamEndReadNeededBasedOnDiscrepancyPresence(
                                            clone.getCloneDiscrepancies(),
                                              clone.getCloningStrategyId(),  linkers,
                                              refsequence_cds_length) );
        }
        
                 
    }
    
    private void                getCloneDiscrepanciesForCloneStretchCollection(ArrayList contigs, 
                            CloneDescription clone , Hashtable linkers,int refsequence_cds_length)
                            throws Exception
    {
        ArrayList clone_discrepancies = new ArrayList();ArrayList temp = null;
        Stretch contig = null; boolean isERNeeded = false;
        contigs = Stretch.sortByPosition(contigs);
        
        clone.setCloneAnalysisStatus( CloneDescription.CLONE_ANALYSIS_STATUS_ANALYZED_CONTIGS);
        for (int count = 0; count < contigs.size(); count++)
        {
            contig = (Stretch) contigs.get(count);
            if ( contig.getAnalysisStatus() == -1)
            {
                clone.setCloneAnalysisStatus( CloneDescription.CLONE_ANALYSIS_STATUS_NOT_ANALYZED);
                return;
            }
            if ( contig.getSequence().getDiscrepancies() != null &&
                contig.getSequence().getDiscrepancies().size() > 0)
            {
                temp= contig.getSequence().getDiscrepancies();
                temp = DiscrepancyDescription.assembleDiscrepancyDefinitions( temp);
                clone_discrepancies.addAll( temp);

            }
            if ( count == 0  )// first contig set clone cds start and check for ER need
            {
                if ( clone.getCloneSequenceCdsStart() > 0  ) isERNeeded = true;
                else
                {
                    BioLinker linker = (BioLinker)linkers.get(new Integer(clone.getCloningStrategyId()));
                    isERNeeded = linker.getSequence().length() - 5 > Math.abs( contig.getCdsStart());
                    if (!isERNeeded )
                    {
                        isERNeeded = isUpstreamEndReadNeededBasedOnDiscrepancyPresence(
                                     temp ,
                                     clone.getCloningStrategyId(),   linkers) ;
                    }
                }
                clone.setIsForwardERNeeded( isERNeeded );
            }
            if ( count == contigs.size() - 1 )//last contig
            {
                BioLinker linker = (BioLinker)linkers.get(new Integer(-clone.getCloningStrategyId()));
                isERNeeded= ( contig.getCdsStop()  < (linker.getSequence().length() + refsequence_cds_length - 5));   
                if ( ! isERNeeded )
                {
                     isERNeeded = isDownstreamEndReadNeededBasedOnDiscrepancyPresence( temp,
                                              clone.getCloningStrategyId(),  linkers,
                                              refsequence_cds_length);
                }
                clone.setIsReverseERNeeded( isERNeeded );
            }
        }
        clone.setCloneDiscrepancies( clone_discrepancies );
      
    }
    
    
    private void                getCloneDiscrepanciesForER(
                            CloneDescription clone , Hashtable linkers,int refsequence_cds_length)
                            throws Exception
    {
        ArrayList reads = new ArrayList();Read read = null;
        ArrayList clone_discrepancies = new ArrayList();
        ArrayList read_1_discrepancies = null;ArrayList read_2_discrepancies = null;
        BioLinker linker = null;
                   
        clone.setCloneAnalysisStatus( CloneDescription.CLONE_ANALYSIS_STATUS_ANALYZED_READS);
        reads = Read.getReadByIsolateTrackingId( clone.getIsolateTrackingId() );
        if (reads != null && reads.size() > 0 )
        {
            for (int count_read = 0; count_read < reads.size(); count_read++)
            {
                read = (Read) reads.get(count_read);
                if ( read.getStatus() == Read.STATUS_NOT_ANALIZED )
                {
                    if ( read.getType() != Read.TYPE_ENDREAD_FORWARD_SHORT && read.getType() != Read.TYPE_ENDREAD_REVERSE_SHORT)
                    {
                        clone.setCloneAnalysisStatus( CloneDescription.CLONE_ANALYSIS_STATUS_NOT_ANALYZED);
                        return;
                    }
                }
                if ( read.getType() == Read.TYPE_ENDREAD_FORWARD )
                {
                    read_1_discrepancies = read.getSequence().getDiscrepancies();
                    //define need for forward read
                    linker = (BioLinker)linkers.get(new Integer(clone.getCloningStrategyId()));
                    clone.setIsForwardERNeeded( read.getCdsStart() >= linker.getSequence().length());
                    
                }
                if (read.getType() == Read.TYPE_ENDREAD_REVERSE )
                {
                    read_2_discrepancies= read.getSequence().getDiscrepancies();
                }
            }
            if ( ( read_1_discrepancies != null && read_1_discrepancies.size() > 0 ) &&
                ( read_2_discrepancies != null && read_2_discrepancies.size() > 0 ))
            {
                clone_discrepancies = DiscrepancyDescription.getDiscrepancyDescriptionsNoDuplicates(
                                                read_1_discrepancies,read_2_discrepancies);
            }
            else if ( read_1_discrepancies != null && read_1_discrepancies.size() > 0 )
            {
                clone_discrepancies = read_1_discrepancies;
                clone_discrepancies = DiscrepancyDescription.assembleDiscrepancyDefinitions( clone_discrepancies);

            }
            else if ( read_2_discrepancies != null && read_2_discrepancies.size() > 0 )
            {
                clone_discrepancies = read_2_discrepancies;
                clone_discrepancies = DiscrepancyDescription.assembleDiscrepancyDefinitions( clone_discrepancies);

            }
        }
        clone.setCloneDiscrepancies( clone_discrepancies );
        if ( ! clone.isForwardERNeeded())
        {
            clone.setIsForwardERNeeded(   isUpstreamEndReadNeededBasedOnDiscrepancyPresence(
                                     clone.getCloneDiscrepancies() ,
                                     clone.getCloningStrategyId(),   linkers) );
        }
        clone.setIsReverseERNeeded( isDownstreamEndReadNeededBasedOnDiscrepancyPresence( clone.getCloneDiscrepancies(),
                                              clone.getCloningStrategyId(),  linkers,
                                              refsequence_cds_length));
    }
    //-----------------------------------------------------------------------
    private CloneDescription[] getCloneData(String sql_items, CloneDescription[] clones)throws Exception
    {
        String  sql="select i.status as ISOLATESTATUS, assembly_status, i.CONSTRUCTID as CONSTRUCTID , cloningstrategyid, "
+" a.SEQUENCEID as CLONESEQUENCEID,  a.linker5start as clonesequence5start, a.linker3stop as clonesequence3stop, a.cdsstart as cloneseqcdsstart, a.cdsstop as clonesequencecdsstop, analysisSTATUS, "
+" FLEXSEQUENCEID,flexcloneid  as CLONEID,  POSITION,  SAMPLETYPE, LABEL, "
+"sc.refsequenceid as refsequenceid,    i.ISOLATETRACKINGID as ISOLATETRACKINGID "
+"  from flexinfo f,isolatetracking i, sample s, containerheader c,assembledsequence a ,"
+" sequencingconstruct sc where f.isolatetrackingid=i.isolatetrackingid and i.sampleid= "
+" s.sampleid  and sc.constructid(+)=i.constructid and   s.containerid=c.containerid and a.isolatetrackingid(+) "
+" =i.isolatetrackingid   and i.isolatetrackingid in ("+sql_items+") order by CLONEID, a.submissiondate desc";

        CloneDescription clone = null;
        Hashtable processed_isolatetracking_ids = new Hashtable();
        Integer current_clone_id = null;
        int count = 0;
        ResultSet rs = null;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);

            while(rs.next())
            {
                 clone = new CloneDescription();
                 clone.setCloneId (rs.getInt("CLONEID"));
                 if (clone.getCloneId() > 0 )
                 {
                    current_clone_id = new Integer(clone.getCloneId());
                    if (  processed_isolatetracking_ids.contains( current_clone_id ))
                    {
                        continue;
                    }
                 }
                 else
                     current_clone_id = null;

                 clone.setCloneAssemblyStatus   (rs.getInt("assembly_status"));
                 clone.setPlateName (rs.getString("LABEL"));
                 clone.setPosition (rs.getInt("POSITION"));
                 clone.setSampleType (rs.getString("SAMPLETYPE"));

                 clone.setCloneStatus (rs.getInt("ISOLATESTATUS"));
                 clone.setCloningStrategyId (rs.getInt("cloningstrategyid"));
                 clone.setCloneSequenceId (rs.getInt("CLONESEQUENCEID"));
                 clone.setCloneSequenceAnalysisStatus (rs.getInt("analysisSTATUS"));
              //   clone.setSequenceType (rs.getInt("SEQUENCETYPE"));
                 clone.setConstructId (rs.getInt("CONSTRUCTID"));
                 clone.setIsolateTrackingId (rs.getInt("ISOLATETRACKINGID"));
                 //clone.setRank(rs.getInt("RANK"));
                // clone.setSampleId(rs.getInt("SAMPLEID"));
                // clone.setScore(rs.getInt("SCORE"));
                 clone.setBecRefSequenceId(rs.getInt("REFSEQUENCEID"));
                 clone.setFlexSequenceId(rs.getInt("FLEXSEQUENCEID"));
                 clone.setCloneSequenceCdsStart(rs.getInt("cloneseqcdsstart"));
                 clone.setCloneSequenceCdsStop(rs.getInt("clonesequencecdsstop"));
                 clone.setCloneSequence5LinkerStart(rs.getInt("clonesequence5start"));
                clone.setCloneSequence3LinkerStop (rs.getInt("clonesequence3stop"));

                 if (  current_clone_id != null)
                 {
                     processed_isolatetracking_ids.put(current_clone_id, current_clone_id );
                 }
                 clones[count++] = clone;
            }
            return clones;
        }
        catch(Exception e)
        {
            m_error_messages.add("Cannot get data for clone "+clone.getCloneId ()+" "+e.getMessage() +"\n"+sql);
            throw new Exception();
        }


    }

    //collects information for printouts
    private Hashtable  getRefSequencesAndCloneSequencetText(CloneDescription[] clones)throws Exception
    {
        CloneDescription clone = null;
        RefSequence refsequence = null;
        Hashtable ref_sequences = new Hashtable();

        boolean generalinfo = true;//(  m_is_ref_cds_start ||  m_is_ref_cds_stop || m_is_ref_cds_length);
        boolean sequencetext = ( m_is_ref_seq_text ||  m_is_ref_seq_cds );
        boolean isIncludePublicInfo = (  m_is_ref_gene_symbol ||  m_is_ref_gi || m_is_ref_locusid);



      //  boolean m_is_ref_species_id ; m_is_ref_species_id

       boolean isCloneSequenceTextNeeded = ( m_is_clone_sequence_text|| m_is_clone_sequence_cds );//Clone Sequence  </td>

        for (int count = 0; count < clones.length; count++)
        {
            clone= clones[count];
            if ( clone == null) break;

        //collect sequence text
            if ( isCloneSequenceTextNeeded && clone.getCloneSequenceId() > 0 )
            {
                try
                {
                    clone.setCloneSequenceText( BaseSequence.getSequenceInfo(clone.getCloneSequenceId(), BaseSequence.SEQUENCE_INFO_TEXT));
                }
                catch(Exception e)
                {
                    m_error_messages.add("Cannot get clone sequence text for clone id "+clone.getCloneId() +"\n"+e.getMessage());
                    throw new Exception();
                }
            }

            //collect reference sequence information

            if ( ( isIncludePublicInfo ||       sequencetext ||             generalinfo )
                    && clone.getBecRefSequenceId() > 0  &&
                ! ref_sequences.containsKey(new Integer(clone.getBecRefSequenceId())))

            {
                try
                {
                    refsequence = new RefSequence(clone.getBecRefSequenceId(),  isIncludePublicInfo,       sequencetext,              generalinfo);
                    ref_sequences.put(new Integer(clone.getBecRefSequenceId()),refsequence);
                    if (  m_species_id_definitions[refsequence.getSpecies() - 1] != null && m_is_ref_species_id )
                        m_species_id_definitions[refsequence.getSpecies() - 1].incrementCount();
                }
                catch(Exception e)
                {
                    m_error_messages.add("Cannot get Refsequence for id "+clone.getBecRefSequenceId() +"\n"+e.getMessage());
                    throw new Exception();
                }
            }

        }
       return ref_sequences;
    }

     //collects information for printouts
    private Hashtable  getLinkers(CloneDescription[] clones, Hashtable linkers)throws Exception
    {
        CloneDescription clone = null;
        CloningStrategy cloning_strategy = null;
        if (linkers == null ) linkers = new Hashtable();

        boolean isLinkerInformationNeeded = true;//(  m_is_ref_5_linker||  m_is_ref_3_linker );


        for (int count = 0; count < clones.length; count++)
        {
            clone= clones[count];
            if ( clone == null) break;

                    //collect linker information
            //put linker 5 with startetgy id
            // linker 3 with - startegy id
             if ( isLinkerInformationNeeded && clone.getCloningStrategyId() > 0  &&
                ! linkers.containsKey(new Integer(clone.getCloningStrategyId())))

            {
                try
                {
                    cloning_strategy =  CloningStrategy.getById(clone.getCloningStrategyId());
                    linkers.put(new Integer(clone.getCloningStrategyId()),BioLinker.getLinkerById(cloning_strategy.getLinker5Id()) );
                    linkers.put(new Integer(-clone.getCloningStrategyId()),BioLinker.getLinkerById(cloning_strategy.getLinker3Id()));
                }
                catch(Exception e)
                {
                    m_error_messages.add("Cannot get linkers for clone id "+clone.getCloneId() +"\n"+e.getMessage());
                    throw new Exception();
                }
            }

        }
        return linkers;
    }



    private String    writeItem( CloneDescription clone, Hashtable refsequences, Hashtable linkers)
    {
        StringBuffer cloneinfo= new StringBuffer();
       RefSequence refsequence = null;
       String cds = null;
//System.out.println("clones writing " + clone.getCloneId());
try
{
      if ( clone.getBecRefSequenceId()>0)
      {
         refsequence = (RefSequence)refsequences.get(new Integer(clone.getBecRefSequenceId()));
      }


        cloneinfo.append(clone.getCloneId() + Constants.TAB_DELIMETER);
        cloneinfo.append( m_group_definitions[clone.getRank()].getGroupName() + Constants.TAB_DELIMETER);
        cloneinfo.append(clone.getNextStepRecomendation() + Constants.TAB_DELIMETER);
        if(    m_is_plate_label   ){ cloneinfo.append(clone.getPlateName  ()+ Constants.TAB_DELIMETER); }//   "Plate Label "
        if(     m_is_sample_type ){ cloneinfo.append(clone.getSampleType  ()+ Constants.TAB_DELIMETER); }//          "Sample Type "
        if(     m_is_position ){ cloneinfo.append(clone.getPosition  ()+ Constants.TAB_DELIMETER); }//          "Well "

    if (refsequence != null  )
    {
        if(     m_is_ref_sequence_id ){ cloneinfo.append(clone.getFlexSequenceId()+ Constants.TAB_DELIMETER); }//         "Sequence ID "
        if(     m_is_ref_cds_start ){ cloneinfo.append(refsequence.getCdsStart()  + Constants.TAB_DELIMETER); }//          "CDS Start "
        if(     m_is_ref_cds_stop ){ cloneinfo.append(refsequence.getCdsStop()+ Constants.TAB_DELIMETER); }//          "CDS Stop "
        if(     m_is_ref_cds_length ){ cloneinfo.append( (refsequence.getCdsStop() - refsequence.getCdsStart())+ Constants.TAB_DELIMETER); }//          "CDS Length "
        if(     m_is_ref_gene_symbol ){ cloneinfo.append( refsequence.getPublicInfoParameter("GENE_NAME") + Constants.TAB_DELIMETER); }//          "Gene Symbol "
        if(     m_is_ref_gi ){ cloneinfo.append(refsequence.getPublicInfoParameter("GI")+ Constants.TAB_DELIMETER); }//          "GI Number "
        if(     m_is_ref_locusid  ){ cloneinfo.append(refsequence.getPublicInfoParameter("LOCUS_ID")+Constants.TAB_DELIMETER ); }//        "All available identifiers "
        if(     m_is_ref_species_id )
        {
             for (int count = 0; count < m_species_id_definitions.length; count++)
            {
                 if (m_species_id_definitions[count] != null)
                      cloneinfo.append(refsequence.getPublicInfoParameter(m_species_id_definitions[count].getIdName()) + Constants.TAB_DELIMETER);

            }

         }//          "Species specific ID "
    }
    else
    {
        if(     m_is_ref_sequence_id ){ cloneinfo.append(Constants.TAB_DELIMETER ); }//         "Sequence ID "
        if(     m_is_ref_cds_start ){ cloneinfo.append(Constants.TAB_DELIMETER ); }//          "CDS Start "
        if(     m_is_ref_cds_stop ){ cloneinfo.append(Constants.TAB_DELIMETER ); }//          "CDS Stop "
        if(     m_is_ref_cds_length ){ cloneinfo.append(Constants.TAB_DELIMETER ); }//          "CDS Length "
        if(     m_is_ref_gene_symbol ){ cloneinfo.append(Constants.TAB_DELIMETER ); }//          "Gene Symbol "
        if(     m_is_ref_gi ){ cloneinfo.append(Constants.TAB_DELIMETER ); }//          "GI Number "
        if(     m_is_ref_locusid  ){ cloneinfo.append(Constants.TAB_DELIMETER ); }//        "All available identifiers "
        if(     m_is_ref_species_id ){ cloneinfo.append(Constants.TAB_DELIMETER ); }//          "Species specific ID "
    }


if(     m_is_clone_seq_id ){ cloneinfo.append(clone.getCloneSequenceId() + Constants.TAB_DELIMETER ); }//         "Clone Sequence Id "
if(     m_is_clone_sequence_assembly_status ){ cloneinfo.append(IsolateTrackingEngine.getAssemblyStatusAsString(clone.getCloneAssemblyStatus()) + Constants.TAB_DELIMETER ); }//          "Clone Sequence assembly attempt status "
if ( clone.getCloneSequenceId() > 0 )
{
    if(     m_is_clone_sequence_analysis_status ){ cloneinfo.append(BaseSequence.getSequenceAnalyzedStatusAsString(clone.getCloneSequenceAnalysisStatus())+ Constants.TAB_DELIMETER ); }//          "Clone Sequence Analysis Status "
    if(     m_is_clone_sequence_cds_start ){ cloneinfo.append(clone.getCloneSequenceCdsStart() + Constants.TAB_DELIMETER ); }//          "Cds Start "
    if(     m_is_clone_sequence_cds_stop ){ cloneinfo.append(clone.getCloneSequenceCdsStop  () + Constants.TAB_DELIMETER ); }//          " Cds Stop "

}
else
{
    if(     m_is_clone_sequence_analysis_status ){ cloneinfo.append("N/A"+ Constants.TAB_DELIMETER ); }//          "Clone Sequence Analysis Status "
    if(     m_is_clone_sequence_cds_start ){ cloneinfo.append("N/A" + Constants.TAB_DELIMETER ); }//          "Cds Start "
    if(     m_is_clone_sequence_cds_stop ){ cloneinfo.append("N/A" + Constants.TAB_DELIMETER ); }//          " Cds Stop "

}


int[][] discrepancy_count  = null;

if(  m_is_clone_sequence_disc_high || m_is_clone_sequence_disc_low )
{
    discrepancy_count  = DiscrepancyDescription.getDiscrepanciesSeparatedByType(clone.getCloneDiscrepancies(),true);
}
if(  m_is_clone_sequence_disc_high )
{
    cloneinfo.append( DiscrepancyDescription.discrepancySummaryReport( discrepancy_count,Mutation.REGION_LINKER_5P, true, false));
    cloneinfo.append( Constants.TAB_DELIMETER );
    cloneinfo.append( DiscrepancyDescription.discrepancySummaryReport( discrepancy_count,Mutation.REGION_CDS, true, false));
    cloneinfo.append( Constants.TAB_DELIMETER );
    cloneinfo.append( DiscrepancyDescription.discrepancySummaryReport( discrepancy_count,Mutation.REGION_LINKER_3P, true, false));
    cloneinfo.append(Constants.TAB_DELIMETER );
}
if(  m_is_clone_sequence_disc_low  )
{
    cloneinfo.append( DiscrepancyDescription.discrepancySummaryReport( discrepancy_count,Mutation.REGION_LINKER_5P, false, false));
    cloneinfo.append( Constants.TAB_DELIMETER );
    cloneinfo.append( DiscrepancyDescription.discrepancySummaryReport( discrepancy_count,Mutation.REGION_CDS, false, false));
    cloneinfo.append( Constants.TAB_DELIMETER );
    cloneinfo.append( DiscrepancyDescription.discrepancySummaryReport( discrepancy_count,Mutation.REGION_LINKER_3P, false, false));
    cloneinfo.append(Constants.TAB_DELIMETER );
}

if(     m_is_clone_sequence_disc_det )
{
    cds = (String)DiscrepancyDescription.detailedDiscrepancyreport( clone.getCloneDiscrepancies() , "type") ;
    if ( cds == null) cds =  Constants.TAB_DELIMETER+ Constants.TAB_DELIMETER;
    cloneinfo.append(cds    + Constants.TAB_DELIMETER );
}//           "Detailed Discrepancy Report "

    BioLinker linker = ( BioLinker ) linkers.get( new Integer(clone.getCloningStrategyId()));
    if(     linker != null )    {         cloneinfo.append(linker.getSequence()  + Constants.TAB_DELIMETER);    }//          "5' linker sequence "
    else    {        cloneinfo.append(Constants.TAB_DELIMETER);    }

    linker = ( BioLinker ) linkers.get( new Integer(-clone.getCloningStrategyId()));
    if(     linker != null )    {         cloneinfo.append(linker.getSequence()  + Constants.TAB_DELIMETER);    }//          "5' linker sequence "
    else    {        cloneinfo.append(Constants.TAB_DELIMETER);    }

    if ( clone.getCloneSequenceId() > 0 &&     m_is_clone_sequence_cds )
    {
        cds = clone.getCloneSequenceText().substring( clone.getCloneSequenceCdsStart(), clone.getCloneSequenceCdsStop() );
        cloneinfo.append(cds + Constants.TAB_DELIMETER );
    }//          "Clone Sequence "
    else  if ( clone.getCloneSequenceId() < 0 &&     m_is_clone_sequence_cds ){ cloneinfo.append("N/A"+ Constants.TAB_DELIMETER ); }//          "Clone Sequence "

    if (refsequence != null  &&  m_is_ref_seq_cds )    { cloneinfo.append(refsequence.getCodingSequence()+ Constants.TAB_DELIMETER); }
    else  if (refsequence == null  &&  m_is_ref_seq_cds )    { cloneinfo.append( Constants.TAB_DELIMETER); }//          "CDS "

    if ( clone.getCloneSequenceId() > 0 &&     m_is_clone_sequence_text )  { cloneinfo.append(  clone.getCloneSequenceText() + Constants.TAB_DELIMETER ); }//          "Clone Sequence "
    else  if ( clone.getCloneSequenceId() < 0 &&     m_is_clone_sequence_text ){ cloneinfo.append("N/A"+ Constants.TAB_DELIMETER ); }//          "Clone Sequence "

    if (refsequence != null  && m_is_ref_seq_text ){ cloneinfo.append(refsequence.getText()+ Constants.TAB_DELIMETER); }//          "Sequence Text "
    else  if (refsequence == null  &&  m_is_ref_seq_text )    { cloneinfo.append( Constants.TAB_DELIMETER); }//          "CDS "
 // System.out.println("clones "+cloneinfo.toString());
        return cloneinfo.toString();
}
catch(Exception e)
{
    m_error_messages .add("Cannot print report for clone: "+clone.getCloneId());
    return "";
}
    }

    private ArrayList getListOfIsolateTrackingId()throws BecDatabaseException
    {
        String query =  null;
        String isolatetracking_ids = "";
        try
        {
            switch (m_items_type)
            {
                case Constants.ITEM_TYPE_CLONEID:
                {
                     //get all plates
                    ArrayList cloneids =  prepareItemsListForSQL();
                    for (int clone_count = 0; clone_count < cloneids.size(); clone_count++)
                    {

                         query = " select isolatetrackingid as item from flexinfo where flexcloneid in "
                           +"("+cloneids.get(clone_count)+")";
                         isolatetracking_ids += getListOfItems( query);
                    }
                    break;

                }
                case Constants.ITEM_TYPE_PLATE_LABELS:
                {
                    //get all plates
                    ArrayList plate_labels =  prepareItemsListForSQL();
                    for (int plate_count = 0; plate_count < plate_labels.size(); plate_count++)
                    {

                         query = " select isolatetrackingid as item from isolatetracking where sampleid in "
                         +" (select sampleid from sample where containerid in "
                         +" (select containerid from containerheader where label in "
                         +"("+plate_labels.get(plate_count)+")))";
                         isolatetracking_ids += getListOfItems( query);
                    }
                    break;
                }
                case Constants.ITEM_TYPE_PROJECT_NAME:
                {
                    //get all plates
                   query = " select isolatetrackingid as item from isolatetracking where sampleid in "
                         +" (select sampleid from sample where containerid in "
                         +" (select containerid from containerheader where label like '" + m_items + "%'))";

                         isolatetracking_ids += getListOfItems( query);

                }
                break;
            }
            if ( isolatetracking_ids == null || isolatetracking_ids.trim().equals("")  )return null;
            else    return prepareItemsListForSQL(Constants.ITEM_TYPE_CLONEID, isolatetracking_ids, 20);

        }
        catch(Exception e)
        {
            throw new BecDatabaseException("Cannot get items for process." + e.getMessage());
        }
    }



    private String getListOfItems(String query) throws Exception
    {
        DatabaseTransaction t = null;
        String result = "";
        ResultSet rs = null;
        try
        {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery( query );
            while(rs.next())
            {
                result += rs.getInt( "ITEM" ) + " ";
            }
            return result;
        }
        catch (Exception E)
        {
            m_error_messages.add("Error occured while trying to get plate labels. "+E+"\nSQL: "+query);
            throw new Exception();
        }
        finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }

    }

      private void    printReport(String total_report_file_name,
                      String time_stamp,
                      CloneDescription[] clones, int write_cycle,
                    Hashtable  linkers, Hashtable ref_sequences
                    )throws Exception
      {
           FileWriter fr_total_report_file_name = null;
           FileWriter[] fr_array =null;
            CloneDescription clone = null;
            String title = getReportTitle();
            String item_report = null;
           try
           {
               if ( m_number_of_files == Constants.OUTPUT_TYPE_GROUP_PER_FILE)
               {
                   fr_array = getFileWriters(time_stamp, title, write_cycle);
        // System.out.println("fr_array "+fr_array.length);
        //  System.out.println("fclones "+clones.length);
                   for (int count = 0; count < clones.length; count++)
                   {
                        clone= clones[count];
                        if ( clone == null  ) break;
                        if (  clone.getRank() < 0 || clone.getRank() > m_group_definitions.length-1) continue;
                         System.out.println("clones "+clone.getCloneId()+" "+clone.getRank());
                        item_report =  writeItem(  clone,  ref_sequences,  linkers );
                         fr_array[clone.getRank()].write(item_report + Constants.LINE_SEPARATOR);

                        m_group_definitions[clone.getRank()].incrementCloneCount();
                   }
                   for (int count = 0; count < clones.length; count++)
                   {
                        fr_array[clone.getRank()].flush();
                        fr_array[clone.getRank()].close();
                   }

               }
               else
               {

                    fr_total_report_file_name =  new FileWriter(total_report_file_name, true);
                    if (write_cycle == 0) fr_total_report_file_name.write(title+Constants.LINE_SEPARATOR);
                    for (int count = 0; count < clones.length; count++)
                    {
                        clone= clones[count];
                        if ( clone == null) break;
                        if (  clone.getRank() < 0 || clone.getRank() > m_group_definitions.length-1) continue;
                      
                        item_report =  writeItem(  clone,  ref_sequences,  linkers ) ;
                        fr_total_report_file_name.write(item_report + Constants.LINE_SEPARATOR);
                         m_group_definitions[clone.getRank()].incrementCloneCount();
                    }
                    fr_total_report_file_name.flush();
                    fr_total_report_file_name.close();
                }

           }
           catch(Exception e)
           {
               try
               { if ( fr_total_report_file_name != null) fr_total_report_file_name.close();
                 for (int count = 0 ; count < fr_array.length; count++)
                 {
                     if ( fr_array[count] != null ) fr_array[count].close();
                 }
               }catch(Exception n){}
           }
      }


      private FileWriter[] getFileWriters(String time_stamp, String title, int write_cycle)throws Exception
      {
          FileWriter[] fr = new FileWriter[m_group_definitions.length];
          for ( int group_count = 0; group_count < m_group_definitions.length; group_count++)
          {

              fr[group_count] = new FileWriter(Constants.getTemporaryFilesPath() +  m_group_definitions[group_count].getFileName() + "_"+ time_stamp +".txt", true);
              if ( write_cycle == 0 )fr[group_count].write(title+Constants.LINE_SEPARATOR);
              fr[group_count].flush();

          }
          return fr;
      }
  /* a.	Summary file:
i.	User id
ii.	Date
iii.	User comments
iv.	Full description of used spec (name, id, parameters)
v.	Input data (list of cloneid, or list of plate labels or project name)
vi.	Summary of number of clones in each group
vii.	Total number of clones in this report*/

    private void printSummaryFile( String file_name, FullSeqSpec spec )
    {
        FileWriter fr = null;
        int total_clone_count = 0 ;
        try
        {
            fr =  new FileWriter(file_name );
            fr.write("User Id:"+ Constants.TAB_DELIMETER +  m_user.getUsername() + Constants.LINE_SEPARATOR);
            fr.write("Date: " + Constants.TAB_DELIMETER +  Constants.getCurrentDate() + Constants.LINE_SEPARATOR);
            fr.write("Comment: " + Constants.TAB_DELIMETER +   m_user_comment + Constants.LINE_SEPARATOR);
            fr.write(Constants.LINE_SEPARATOR + "User spec: "  + Constants.LINE_SEPARATOR);
            fr.write(Constants.LINE_SEPARATOR +  spec.printSpecDefinition(Constants. LINE_SEPARATOR) );

            fr.write(Constants.LINE_SEPARATOR + "Submitted Items:" + Constants.LINE_SEPARATOR);
            fr.write(m_items+ Constants.LINE_SEPARATOR+ Constants.LINE_SEPARATOR);
            fr.write("Report Summary:" + Constants.LINE_SEPARATOR);
            for (int group_count = 0; group_count < m_group_definitions.length; group_count++)
          {
              total_clone_count += m_group_definitions[group_count].getCloneCount();
            fr.write( m_group_definitions[group_count].getGroupName() + Constants.TAB_DELIMETER + m_group_definitions[group_count].getCloneCount()+ Constants.LINE_SEPARATOR);
          }
            fr.write(Constants.LINE_SEPARATOR + "Total clone number: "+ total_clone_count+ Constants.LINE_SEPARATOR);
            fr.flush();
            fr.close();
        }
        catch(Exception e)
        {
            m_error_messages.add("Cannot write summary report");
            try { fr.close();}catch(Exception n){}
        }
    }



    //put all report files into sending collection
  private void attachFiles(String summary_report_file_name, String total_report_file_name, String time_stamp)
  {
      m_file_list_reports.add(new File(summary_report_file_name));
      if ( m_number_of_files == Constants.OUTPUT_TYPE_ONE_FILE)
      {
          m_file_list_reports.add(new File(total_report_file_name));
      }
      else
      {
          File fr = null;
          for (int group_count = 0; group_count < m_group_definitions.length; group_count++)
          {
              fr = new File(Constants.getTemporaryFilesPath() + m_group_definitions[group_count].getFileName() + "_"+  time_stamp + ".txt");

              if ( fr.exists() && m_group_definitions[group_count].getCloneCount() > 0)
                m_file_list_reports.add( fr );
          }
      }
  }

  private String     getReportTitle()
   {
       SpeciesDefinition sd = null;
       StringBuffer title = new StringBuffer();
       title.append("Clone Id"+ Constants.TAB_DELIMETER +"Group"+ Constants.TAB_DELIMETER+" Next step"+ Constants.TAB_DELIMETER);
if(   m_is_plate_label ) title.append("Plate Label" + Constants.TAB_DELIMETER);
if(  m_is_sample_type  ) { title.append("Sample Type" + Constants.TAB_DELIMETER) ;}
if(  m_is_position ) {title.append("Position" + Constants.TAB_DELIMETER);}

if(  m_is_ref_sequence_id) title.append("Ref Sequence ID" + Constants.TAB_DELIMETER);
if(   m_is_ref_cds_start) title.append("Ref CDS Start" + Constants.TAB_DELIMETER);
if(   m_is_ref_cds_stop ) title.append("Ref CDS Stop" + Constants.TAB_DELIMETER);
if(   m_is_ref_cds_length ) title.append("Ref CDS Length" + Constants.TAB_DELIMETER);
if(  m_is_ref_gene_symbol ) title.append("Gene Symbol" + Constants.TAB_DELIMETER);
if(  m_is_ref_gi)title.append("GI Number" + Constants.TAB_DELIMETER);
if(  m_is_ref_locusid) title.append("Locus ID" + Constants.TAB_DELIMETER);

if(  m_is_ref_species_id  )
{
    for (int count = 0; count < m_species_id_definitions.length; count++)
    {
         if (m_species_id_definitions[count] == null || m_species_id_definitions[count].getCloneCount() == 0)
             m_species_id_definitions[count] = null;
         else title.append(m_species_id_definitions[count].getIdName() + Constants.TAB_DELIMETER);

    }

 }

if(   m_is_clone_seq_id )title.append("Clone Sequence Id" + Constants.TAB_DELIMETER);
if(  m_is_clone_sequence_assembly_status )title.append("Assembly attempt status - clone sequence" + Constants.TAB_DELIMETER);
if(   m_is_clone_sequence_analysis_status )title.append("Analysis Status - clone sequence" + Constants.TAB_DELIMETER);
if(  m_is_clone_sequence_cds_start)title.append("Clone Cds Start" + Constants.TAB_DELIMETER);
if( m_is_clone_sequence_cds_stop )title.append("Clone Cds Stop" + Constants.TAB_DELIMETER);
if( m_is_clone_sequence_disc_high)title.append( "5' High Quality Discrepancies " + Constants.TAB_DELIMETER + "CDS High Quality Discrepancies " + Constants.TAB_DELIMETER + "3' High Quality Discrepancies " + Constants.TAB_DELIMETER);
if(   m_is_clone_sequence_disc_low)title.append("5' Low Quality Discrepancies " + Constants.TAB_DELIMETER + "CDS Low Quality Discrepancies " + Constants.TAB_DELIMETER + "3' Low Quality Discrepancies " + Constants.TAB_DELIMETER);
if(   m_is_clone_sequence_disc_det )title.append("Detailed 5' Discrepancy Report" + Constants.TAB_DELIMETER + "Detailed CDS Discrepancy Report" + Constants.TAB_DELIMETER +"Detailed 3' Discrepancy Report" + Constants.TAB_DELIMETER);

if(  m_is_ref_5_linker)title.append("5' linker sequence" + Constants.TAB_DELIMETER);
if(   m_is_ref_3_linker)title.append("3' linker sequence" + Constants.TAB_DELIMETER);

if (  m_is_clone_sequence_cds )title.append("Clone Sequence Cds" + Constants.TAB_DELIMETER);
if( m_is_ref_seq_cds)title.append("CDS" + Constants.TAB_DELIMETER);

if(  m_is_clone_sequence_cds )title.append("Clone Sequence" + Constants.TAB_DELIMETER);
if(   m_is_ref_seq_text)title.append("Sequence Text" + Constants.TAB_DELIMETER);

       return title.toString();
    }

  //---------------------------------------------------------------------
  protected class SpeciesIdHelper
  {
      private int               m_code = -1;
      private String            m_id_name = null;
      private int               m_clone_count = 0;

      public SpeciesIdHelper(int i, String v)
      {
          m_code = i;
          m_id_name =v ;
      }
      public int            getCode(){ return m_code;}
      public String         getIdName(){ return m_id_name;}
      public int            getCloneCount(){ return m_clone_count;}

      public void           incrementCount(){ m_clone_count++;}


  }


  protected   class  GroupDefinition
  {
      public static final int		GROUP_TYPE_ACCEPTED = 0;//Accepted","DecisionTool_Accepted");
        public static final int		GROUP_TYPE_REJECTED = 1; //Rejected","DecisionTool_Rejected");
        public static final int		GROUP_TYPE_NO_TRACE_FILES = 2;//Need further analysis: No trace files","DecisionTool_NFA_NoTraceFiles");
      //  public static final int		GROUP_TYPE_ER_NOT_ANALYZED = 3;//Need further analysis:	End reads not analyzed","DecisionTool_NFA_ER_NotAnalyzed");
        public static final int		GROUP_TYPE_ASSEMBLY_NOT_ATTEMPTED = 3; //Need further analysis: Assembly not attempted","DecisionTool_NFA_AssemblyNotAttempted");
        public static final int		GROUP_TYPE_NOT_ANALYZED = 4; //Need further analysis: Assembled not analyzed","DecisionTool_NFA_Assembled_NotAnalized");
        public static final int		GROUP_TYPE_NOT_FULL_COVERAGE = 5; //5eed further analysis: No full sequence coverage","DecisionTool_NFA_NoFullCoverage");
        public static final int		GROUP_TYPE_LQ_DISCREPANCY = 6;//Need further analysis: Persistent LQ Discrepancy","DecisionTool_NFA_PersistentLQD");
        public static final int		GROUP_TYPE_OTHER = 7;//Need further analysis: Other","DecisionTool_NFA_Other");
     public static final int		GROUP_TYPE_NO_MATCH = 8;//Need further analysis: Other","DecisionTool_NFA_Other");
     public static final int		GROUP_TYPE_MANUAL_REVIEW_HQD = 9;//Need further analysis: Other","DecisionTool_NFA_Other");




        public static final int		GROUP_NUMBER = 10;

        private String i_group_name = null;
      private int    i_clone_count = 0;
      private int    i_clone_status = 0;
      private String i_file_name = null;

      public GroupDefinition(String s, String ss, int status)
      {
          i_group_name = s;
          i_file_name = ss;
          i_clone_status = status;
      }

       public String        getGroupName(){ return i_group_name ;}
       public int           getCloneCount(){ return i_clone_count ;}
       public String        getFileName(){ return i_file_name ;}
       private int          getCloneStatus(){ return i_clone_status;}

       public void           setCloneCount(int v){  i_clone_count = v;}
       public void           incrementCloneCount(){  i_clone_count++;}
  }


    public static void main(String args[])

    {
       // InputStream input = new InputStream();
        DecisionToolRunner_New runner = null;
        User user  = null;
        try
        {

            user = AccessManager.getInstance().getUser("htaycher123","htaycher");
             BecProperties sysProps =  BecProperties.getInstance( BecProperties.PATH);
            sysProps.verifyApplicationSettings();
            DatabaseToApplicationDataLoader.loadDefinitionsFromDatabase();

            runner = new DecisionToolRunner_New();
         //   runner.setInputData(Constants.ITEM_TYPE_CLONEID, "159321 159415 159237 159333 159423 159245 159435 159345 159349 159437 159441 159261 159353 159357 159445 159265 159269 159365 159273 159277 159369 159281 159285 159377 159289 159293 159385 159301 159397 159309 159401 159405 159317 159407 159411 172834 172842 172849 172857 172865 172869 172873 172881 172891 172893 172897 172926 172929 172940 172945 172949 172962 172966 172977 172981 172993 172997 173012 173013 173238 173249 173276 173419 173419 173437 173442 173470 173473 173477 173489 173607 173613 173619 173625 173630 173633 173642 173647 173649 173654 173662 173665 173669 173678 173682 173686 173689 ");

      //runner.setInputData(Constants.ITEM_TYPE_CLONEID, "172259 141149 141150 172169 135152 119982 119983 141618 141619 141620 141630 141631 141632 172223 172224 172246 172247 172248 141117 141118 141119 141151 134468 134469 134472 134475 134476 134479 134480 140012 140013 140014 140042 140043 140044 135071 135142 135143 135144 172767 172768 172769 135150 135151 172624 172625 172626 135252 135253 135396 141686 141704 141744 172167 172168 172260 172226 172227 172228 172363 172362 172691 172693 141687 141705 ");

      
     runner.setInputData(Constants.ITEM_TYPE_CLONEID, "     154816 154658 155181 155189");

      

            runner.setUser(user);
            runner.setNumberOfOutputFiles(Constants.OUTPUT_TYPE_GROUP_PER_FILE);
          //  runner.setSpecId(4);// for yp3
                   runner.setSpecId(33);// for bec
            runner.setUserComment(" test user comment");
            //runner.setNumberOfOutputFiles( );
             runner.setFields(
            "is_plate_label",//    Plate Label</td>
            "is_sample_type",//    Sample Type</td>
            "is_position",//    Well</td>
            "is_ref_sequence_id",//    Sequence ID</td>
            "is_clone_seq_id",//    Clone Sequence Id</td>
            "is_ref_cds_start",//    CDS Start</td>
            "is_clone_sequence_assembly_status",//    Clone Sequence assembly attempt status    </td>
            "is_ref_cds_stop",//    CDS Stop</td>
            "is_clone_sequence_analysis_status",//Clone Sequence Analysis Status  </td>
            "is_ref_cds_length",//    CDS Length</td>
            "is_clone_sequence_cds_start",//Cds Start</td>
           null,// "is_ref_seq_text",//   Sequence Text</td>
            "is_clone_sequence_cds_stop",// Cds Stop </td>
            null,//"is_ref_seq_cds",//    CDS</td>
           null,// "is_clone_sequence_text",//Clone Sequence  </td>
           null,// "is_clone_sequence_cds",
            "is_ref_gene_symbol",// Gene Symbol</td>
            "is_clone_sequence_disc_high",// Discrepancies High Quality (separated by type)</td>
            "is_ref_gi",//    GI Number</td>
            "is_clone_sequence_disc_low",
            "is_ref_5_linker",//5' linker sequence    </td>
            "is_clone_sequence_disc_det", //Detailed Discrepancy Report </td>
            "is_ref_3_linker",//   3' linker sequence</td>
            "is_ref_species_id",//Species specific ID</td>
            "is_ref_ids"//All available identifiers</td>
	);


             runner.run();
        }
        catch(Exception e){}



        System.exit(0);
     }


}
