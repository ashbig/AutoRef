package edu.harvard.med.hip.bec.action_runners;


import java.sql.*;
import java.io.*;
import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.coreobjects.oligo.*;
import edu.harvard.med.hip.bec.coreobjects.feature.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.bioutil.*;
import edu.harvard.med.hip.bec.form.*;
import edu.harvard.med.hip.bec.user.*;
import edu.harvard.med.hip.bec.Constants;
import edu.harvard.med.hip.bec.sampletracking.objects.*;
import edu.harvard.med.hip.bec.util_objects.*;
import edu.harvard.med.hip.bec.ui_objects.*;
import edu.harvard.med.hip.bec.modules.*;
import java.util.*;

/**
 *
 * @author  HTaycher
 */
  public class GapMapperRunner extends ProcessRunner
{

    private int                             m_process_type = -1;
    private boolean                         m_isTryMode = false;
    private int                             m_spec_id = -1;
    private boolean                         m_isRunLQRFinderForContigs = false;
    private String                          m_vector_file_name = null;
    private int         m_quality_trimming_phd_score = 0;
    private int         m_quality_trimming_phd_first_base = 0;
    private int         m_quality_trimming_phd_last_base = 0;
      

    // only for LQR report how many bases will be covered by end reads
    private int				m_number_of_bases_covered_by_forward_er = 300;
    private int				m_number_of_bases_covered_by_reverse_er = 300;
   /* public static final int             STATUS_NO_READS_NEEDED = -1;
    public static final int             STATUS_NO_READS_NEEDED_OUT_OF_RANGE_FOR_DEFINITION = -2;
    public static final int             STATUS_NOT_DEFINED = 0;
    public static final int             STATUS_YES_READS_NEEDED = 1;
*/
    public void         setProcessType(int v){ m_process_type = v;}
    public void         setSpecId(int v){m_spec_id = v;}
    public void         setIsTryMode(boolean isTryMode){m_isTryMode=isTryMode;}
    public void         setIsRunLQR(boolean v){ m_isRunLQRFinderForContigs = v ;}

    public void         setVectorFileName(String v){m_vector_file_name = v;}
    public void         setQualityTrimmingScore (int v){ m_quality_trimming_phd_score = v;}
    public void         setQualityTrimmingLastBase (int v){ m_quality_trimming_phd_last_base = v;}
    public void         setQualityTrimmingFirstBase (int v){ m_quality_trimming_phd_first_base = v;}
      
    public void         setNumberOfBasesCoveredByForwardER(int v){m_number_of_bases_covered_by_forward_er = v ;}
    public void         setNumberOfBasesCoveredByReverseER(int v){m_number_of_bases_covered_by_reverse_er = v;}



    public String   getTitle()
    {
        switch ( m_process_type )
        {
            case Constants.PROCESS_FIND_GAPS:
                return "Request for Gap Mapper execution";
            case Constants.PROCESS_FIND_LQR_FOR_CLONE_SEQUENCE:
                return "Request for Low Quality Regions Finder execution for clone sequences";
            default:
                return "";
        }
    }

    public void run()
    {
         Connection conn = null;
         int process_id = -1;
         PreparedStatement pst_check_prvious_process_complition = null;
         PreparedStatement pst_insert_process_object = null;
         StretchCollection stretch_collection = null;
         SlidingWindowTrimmingSpec       trimming_spec = null;
         Hashtable clone_startagies = new Hashtable();
         CloningStrategy cloning_strategy = null;
         boolean isReportWasCreated = false;
         try
        {
            // get spec
            trimming_spec = (SlidingWindowTrimmingSpec)Spec.getSpecById(m_spec_id);
            conn = DatabaseTransaction.getInstance().requestConnection();
            ArrayList clone_ids = Algorithms.splitString( m_items);
            String report_file_name = Constants.getTemporaryFilesPath() + "GapMapper"+System.currentTimeMillis()+".txt";
            
            for (int count_clones = 0; count_clones < clone_ids.size(); count_clones++)
            {
                //we need to get linkers to prevent definition of lqrs outside intresting regions
                if ( m_process_type == Constants.PROCESS_FIND_LQR_FOR_CLONE_SEQUENCE)
                       cloning_strategy = getCloningStrategy((String) clone_ids.get(count_clones), clone_startagies);
                try// by stretch collection
                {
                    stretch_collection = findStretches(Integer.parseInt( (String) clone_ids.get(count_clones)), trimming_spec, cloning_strategy);
                        
                    if (stretch_collection != null && stretch_collection.getStretches() != null && stretch_collection.getStretches().size() > 0)
                    {
                        stretch_collection.setSpecId(m_spec_id);
    //define lqr for contgi sequences
                        if ( m_isRunLQRFinderForContigs && m_process_type == Constants.PROCESS_FIND_GAPS)
                                getLQRForDefinedContigs( stretch_collection, trimming_spec);

                        if ( ! m_isTryMode )
                        {
                             if ( process_id == -1)
                            {
                                process_id = createProcessRecord(  conn,  pst_check_prvious_process_complition, pst_insert_process_object) ;
                                pst_insert_process_object = conn.prepareStatement("insert into process_object (processid,objectid,objecttype) values("+process_id+",?,"+Constants.PROCESS_OBJECT_TYPE_STRETCH_COLLECTION+")");
                            }
                            stretch_collection.insert(conn);
                            pst_insert_process_object.setInt(1,stretch_collection.getId() );
                            DatabaseTransaction.executeUpdate(pst_insert_process_object);
                            conn.commit();
                        }
                        ArrayList print_items = SpecialReportsRunner.getReportForStretchCollection(
                            (String)clone_ids.get(count_clones), 
                            stretch_collection, 
                            m_number_of_bases_covered_by_forward_er ,
                            m_number_of_bases_covered_by_reverse_er,
                            true, true);
                        Algorithms.writeArrayIntoFile( print_items, true, report_file_name, false);
                        if ( print_items != null && print_items.size() > 0 && !isReportWasCreated)
                            isReportWasCreated = true;
                    }
                    
                }
                catch(Exception e)
                {
                    DatabaseTransaction.rollback(conn);
                    m_error_messages.add(e.getMessage());
                }
         }
         if ( isReportWasCreated )m_file_list_reports.add( new File(report_file_name));
        }
        catch(Exception e)
        {
            DatabaseTransaction.rollback(conn);
            m_error_messages.add(e.getMessage());
        }
        finally
        {
            if(conn != null)            DatabaseTransaction.closeConnection(conn);
            sendEMails( getTitle() );
        }
    }

    //-------------------------------
    private void              getLQRForDefinedContigs(StretchCollection stretch_collection,
                                SlidingWindowTrimmingSpec trimming_spec)
                                throws Exception
    {

           int[] stretch_boundaries = new int[2];
           Stretch lqr = null;Stretch contig =  null;
           ArrayList lqr_stretches = new ArrayList();
           for ( int contig_count = 0; contig_count < stretch_collection.getStretches().size(); contig_count++)
           {
               contig = (Stretch) stretch_collection.getStretches().get(contig_count);
               if ( contig.getType() == Stretch.GAP_TYPE_CONTIG )
               {
                   int sequenceid = BecIDGenerator.getID("sequenceid");
                   contig.getSequence().setId(sequenceid);
                   contig.setSequenceId(sequenceid);
                   ArrayList boundaries =  ScoredSequence.getLQR( contig.getSequence().getScoresAsArray() ,   trimming_spec, 0, contig.getSequence().getScoresAsArray().length);
                   for (int index = 0; index < boundaries.size(); index++)
                   {
                        stretch_boundaries = (int[]) boundaries.get(index);
                        lqr = new Stretch();
                        lqr.setType( Stretch.GAP_TYPE_LOW_QUALITY);
                        if ( contig.getCdsStart() == ScoredElement.DEFAULT_COORDINATE)
                        {
                            lqr.setCdsStart( -( contig.getSequence().getText().length() - contig.getCdsStop()) + stretch_boundaries[0] );
                            lqr.setCdsStop( -( contig.getSequence().getText().length() -contig.getCdsStop() ) + stretch_boundaries[1]  );
                        }
                        else
                        {
                            lqr.setCdsStart( contig.getCdsStart() + stretch_boundaries[0] - 1);
                            lqr.setCdsStop( contig.getCdsStart() + stretch_boundaries[1] - 1);
                        }
                        lqr.setSequenceStart(stretch_boundaries[0]);
                        lqr.setSequenceStop(stretch_boundaries[1]);
                        lqr.setSequenceId(sequenceid);
                        lqr_stretches.add(lqr);
                    }
                }
           }
           if ( lqr_stretches.size() > 0 )
           {
               stretch_collection.addStretches(lqr_stretches);
           }

    }
    private StretchCollection findStretches( int clone_id,
            SlidingWindowTrimmingSpec       trimming_spec,
            CloningStrategy cloning_strategy)throws Exception
    {
        StretchCollection stretch_collection = null;
        if ( m_process_type == Constants.PROCESS_FIND_LQR_FOR_CLONE_SEQUENCE)
        {
             stretch_collection = getLQRForClone( clone_id,   trimming_spec, cloning_strategy);
        }
        else if (m_process_type == Constants.PROCESS_FIND_GAPS)
        {
            GapMapper mapper = new GapMapper();
            mapper.setCloneId(clone_id);
            mapper.setQualityTrimmingScore (m_quality_trimming_phd_score);
            mapper.setQualityTrimmingLastBase(m_quality_trimming_phd_last_base);
            mapper.setQualityTrimmingFirstBase (m_quality_trimming_phd_first_base);

            mapper.setVectorFileName(m_vector_file_name );
       
            mapper.setTrimmingSpec( trimming_spec);
            mapper.setIsRunLQR(m_isRunLQRFinderForContigs);
            mapper.run();
            stretch_collection = mapper.getStretchCollection();
            if ( mapper.getErrorMessages() != null && mapper.getErrorMessages().size() > 0)
                m_error_messages.addAll( mapper.getErrorMessages());
        }
        /*
          for (int count =0; count < stretch_collection.getStretches().size(); count++)
          {
              Stretch s = (Stretch) stretch_collection.getStretches().get(count);
              System.out.println(count + " "+s.getCdsStart()+" "+s.getCdsStop() );
          }
         */
        return stretch_collection ;
    }

    private StretchCollection     getLQRForClone
            (int clone_id,
            SlidingWindowTrimmingSpec trimming_spec,
            CloningStrategy cloning_strategy)
            throws Exception
    {
            CloneDescription clone_description = CloneDescription.getCloneDescription( clone_id);
            Stretch lqr = null;
            CloneSequence clone_sequence = CloneSequence.getOneByCloneId(clone_id);
            StretchCollection  stretch_collection = null;
            if ( clone_sequence != null && clone_description != null) //sequence assembled , checking for lqr
            {
                //check weather this collection was already created
                if( !m_isTryMode && StretchCollection.getByCloneSequenceIdAndSpecId(clone_sequence.getId(), trimming_spec.getId()) != null )
                {
                     m_error_messages.add("LQR collection for clone id: "+clone_id +" already exists.");
                     return null;
                }
                 
               
                int start_of_lqr_definition_region =  clone_sequence.getCdsStart() - cloning_strategy.getLinker5().getSequence().length() - ((int)trimming_spec.getQWindowSize() / 2);
                start_of_lqr_definition_region = ( start_of_lqr_definition_region < 0) ? 0 : start_of_lqr_definition_region;
                int end_of_lqr_definition_region =  clone_sequence.getCdsStop() + cloning_strategy.getLinker3().getSequence().length()+ ((int)trimming_spec.getQWindowSize() / 2);
                end_of_lqr_definition_region = ( end_of_lqr_definition_region >= clone_sequence.getText().length() ) ?clone_sequence.getText().length() : end_of_lqr_definition_region;

               //  start_of_lqr_definition_region = 0;
               //  end_of_lqr_definition_region =clone_sequence.getText().length() ;
                ArrayList lqr_stretches = ScoredSequence.getLQR(clone_sequence,  trimming_spec,
                                        start_of_lqr_definition_region, end_of_lqr_definition_region);
                // reassigned coordinates
                for (int count = 0; count < lqr_stretches.size(); count++)
                {
                    lqr = (Stretch)lqr_stretches.get(count);
                    lqr.setSequenceStart( lqr.getCdsStart() );
                    lqr.setSequenceStop( lqr.getCdsStop() );
                    lqr.setCdsStart(lqr.getCdsStart()- clone_sequence.getCdsStart() );
                    lqr.setCdsStop(lqr.getCdsStop()- clone_sequence.getCdsStart() );
                }
                
                stretch_collection = new StretchCollection();
                stretch_collection.setType ( StretchCollection.TYPE_COLLECTION_OF_LQR);
                stretch_collection.setRefSequenceId (  clone_description.getBecRefSequenceId());
                stretch_collection.setIsolatetrackingId ( clone_description.getIsolateTrackingId());
                stretch_collection.setCloneId (clone_description.getCloneId());
                stretch_collection.setCloneSequenceId( clone_sequence.getId() );
                stretch_collection.setStretches( lqr_stretches);
            }
            else
            {
                m_error_messages.add("Cannot find clone sequence for clone id: "+clone_id);
            }
            return stretch_collection;
    }

    private int createProcessRecord( Connection conn,
        PreparedStatement pst_check_prvious_process_complition,
        PreparedStatement pst_insert_process_object) throws Exception
    {
        int process_id = -1;
        if ( m_process_type == Constants.PROCESS_FIND_LQR_FOR_CLONE_SEQUENCE)
            process_id = Request.createProcessHistory( conn, ProcessDefinition.RUN_GAP_MAPPER_FOR_LOWQUALITY_SEQUENCE, new ArrayList(),m_user) ;
        else if (m_process_type == Constants.PROCESS_FIND_GAPS)
                process_id = Request.createProcessHistory( conn, ProcessDefinition.RUN_GAP_MAPPER, new ArrayList(),m_user) ;
        return process_id;
    }

    private CloningStrategy getCloningStrategy(String clone_id, Hashtable clone_strategies)
    {
        CloningStrategy cloning_strategy = null;
        cloning_strategy = (CloningStrategy) clone_strategies.get(clone_id);
        BioLinker linker = null;
        try
        {
            if ( cloning_strategy == null )
            {
                cloning_strategy = CloningStrategy.getByCloneId(Integer.parseInt(clone_id));
                if ( cloning_strategy  != null )
                {
                    
                    linker = BioLinker.getLinkerById(cloning_strategy.getLinker5Id() );
                    if ( isYeastLinker( linker.getName() ))
                    {
                        linker =  BioLinker.getLinkerByName("5p minimum attB Yeast (phase 1 & 2) linker");
                        cloning_strategy.setLinker5Id(linker.getId());
                    }
                    cloning_strategy.setLinker5(linker);
                    linker = BioLinker.getLinkerById(cloning_strategy.getLinker3Id() );
                    if ( isYeastLinker(linker.getName() ))
                    {
                       linker =  BioLinker.getLinkerByName("3p minimum attB Yeast (phase 1 & 2) linker");
                        cloning_strategy.setLinker3Id(linker.getId());
                    }
                    cloning_strategy.setLinker3(linker);
                    clone_strategies.put(clone_id, cloning_strategy);
                }
            }
        }catch(Exception e){}
        return cloning_strategy;
    }

/*
            //PreparedStatement pst_get_flexsequenceid,PreparedStatement pst_get_flexsequence_length,
     private String getReportForStretchCollection(String clone_id, StretchCollection stretch_collection)throws Exception
    {
        Mutation discr = null;
       // System.out.println("A"+oligo_calculation.getSequenceId());
        StringBuffer buf = new StringBuffer();
        buf.append(Constants.LINE_SEPARATOR );
        buf.append("Clone Id: "+ clone_id+Constants.LINE_SEPARATOR);
        int refseq_cds_length = RefSequence.getCdsLength(stretch_collection.getRefSequenceId());
        buf.append("RefSequence Id: "+stretch_collection.getRefSequenceId() );
        buf.append("\tRefSequence Cds Length: "+refseq_cds_length );
        buf.append(Constants.LINE_SEPARATOR);
        
        ArrayList stretchs = Stretch.sortByPosition(stretch_collection.getStretches() );
        Stretch stretch = null;
        CloneSequence clone_sequence = null;
        int status_is_forward_er_needed = 0;// 0 - not defined, 1 - yes, -1 no, -2 no - out of range
        int status_is_reverse_er_needed = 0;// 0 - not defined, 1 - yes, -1 no -2 no - out of range
        int status_is_internal_needed = 0;// 0 - not defined, 1 - yes, -1 no -2 no - out of range
      
        if ( m_process_type == Constants.PROCESS_FIND_LQR_FOR_CLONE_SEQUENCE )
        {
                clone_sequence = CloneSequence.getOneByCloneId(Integer.parseInt( clone_id) );
                buf.append("Sequence Analysis status "+ BaseSequence.getSequenceAnalyzedStatusAsString(clone_sequence.getStatus())+Constants.LINE_SEPARATOR);
        }
        for (int index = 0; index < stretch_collection.getStretches().size();index ++)
        {
            stretch = (Stretch)stretch_collection.getStretches().get(index);

            if ( m_process_type == Constants.PROCESS_FIND_LQR_FOR_CLONE_SEQUENCE )
            {
                buf.append( stretch.getStretchTypeAsString( stretch.getType()) + " "+ (index + 1) + "\t");
                buf.append( "Cds Start " + ( stretch.getCdsStart()- clone_sequence.getCdsStart() ) );
                buf.append("\tCds Stop " + ( stretch.getCdsStop() -  clone_sequence.getCdsStart() ) );
                buf.append("\t Sequence Region "+stretch.getCdsStart() +" - "+ stretch.getCdsStop() );
                //define discrepancies in the region
                ArrayList lqr_discrepancies = clone_sequence.getDiscrepanciesInRegion( stretch.getCdsStart() , stretch.getCdsStop(), clone_sequence.getCdsStart() , refseq_cds_length);
                  if ( lqr_discrepancies == null || lqr_discrepancies.size() == 0 )
                  {
                      buf.append("\t No discrepancies found in the region ");
                  }
                  else
                  {
                       buf.append(Constants.LINE_SEPARATOR+" Discrepancies found in the region ");
                       buf.append(Constants.LINE_SEPARATOR+ Mutation.toString(lqr_discrepancies));
                  }
                
                if ( status_is_forward_er_needed == STATUS_NOT_DEFINED || status_is_forward_er_needed == STATUS_NO_READS_NEEDED )
                    status_is_forward_er_needed = isForwardReadRequered(stretch,lqr_discrepancies);
                if ( status_is_internal_needed == STATUS_NOT_DEFINED || status_is_internal_needed == STATUS_NO_READS_NEEDED)
                    status_is_internal_needed = isInternalReadsRequered(stretch,lqr_discrepancies, refseq_cds_length);
                if ( status_is_reverse_er_needed == STATUS_NOT_DEFINED || status_is_reverse_er_needed== STATUS_NO_READS_NEEDED)  
                    status_is_reverse_er_needed = isReverseReadRequered(stretch,lqr_discrepancies, refseq_cds_length);

            }
            else
            {
                buf.append( stretch.toString() );
            }
            buf.append(Constants.LINE_SEPARATOR );
           
        }
        //define need for end / internal  read
        if ( m_process_type == Constants.PROCESS_FIND_LQR_FOR_CLONE_SEQUENCE )
        {
                StringBuffer requered_reads_description= new StringBuffer();
                requered_reads_description.append("\t\t\t");
                requered_reads_description.append(translateToString(status_is_forward_er_needed, "ForwardEndRead - ") );
                requered_reads_description.append("\t");
                requered_reads_description.append(translateToString(status_is_internal_needed, "InternalRead(s) - ") );
                requered_reads_description.append("\t");
                requered_reads_description.append(translateToString(status_is_reverse_er_needed, "ReverseEndRead - ") );
                buf.append("Summary for Clone Id: "+ clone_id);
                buf.append(requered_reads_description.toString()+Constants.LINE_SEPARATOR);
        }
        return buf.toString();
    }

     // translates status of need to different reads into readable foramt
    private String translateToString(int status, String title) 
    {
        switch (status)
        {
            case STATUS_NO_READS_NEEDED : 
            case STATUS_NO_READS_NEEDED_OUT_OF_RANGE_FOR_DEFINITION: return title + "No";
            case STATUS_NOT_DEFINED: return title + "Not Defined";
            case STATUS_YES_READS_NEEDED: return title + "Yes";
            default: return "N/A";
        }
    }
    
   //define what kind of reads nneded 0 - not defined, 1 - yes, -1 no, -2 no - out of range
    private int  isForwardReadRequered(Stretch stretch, ArrayList lqr_discrepancies)
    {
        if ( stretch.getCdsStart() >= m_number_of_bases_covered_by_forward_er)
                return STATUS_NO_READS_NEEDED_OUT_OF_RANGE_FOR_DEFINITION;
        else if (stretch.getCdsStart() <= m_number_of_bases_covered_by_forward_er
            && ( lqr_discrepancies == null || lqr_discrepancies.size() == 0) )
                return STATUS_NO_READS_NEEDED;
        else if (stretch.getCdsStart() <= m_number_of_bases_covered_by_forward_er
            && ( lqr_discrepancies != null || lqr_discrepancies.size() > 0) )
                return STATUS_YES_READS_NEEDED;
        else 
            return STATUS_NOT_DEFINED;
    }
    private int   isInternalReadsRequered(Stretch stretch, ArrayList lqr_discrepancies, int refseq_cds_length)
    {
        if ( refseq_cds_length < m_number_of_bases_covered_by_forward_er 
          || refseq_cds_length < m_number_of_bases_covered_by_reverse_er 
            || !( stretch.getCdsStart() >  m_number_of_bases_covered_by_forward_er
                && stretch.getCdsStart() < refseq_cds_length - m_number_of_bases_covered_by_reverse_er))
                return STATUS_NO_READS_NEEDED_OUT_OF_RANGE_FOR_DEFINITION;
        
        else if ( stretch.getCdsStart() >  m_number_of_bases_covered_by_forward_er
                && stretch.getCdsStart() < refseq_cds_length - m_number_of_bases_covered_by_reverse_er)
        {
            if (  lqr_discrepancies == null || lqr_discrepancies.size() == 0) 
                return STATUS_NO_READS_NEEDED;
            if ( lqr_discrepancies != null || lqr_discrepancies.size() > 0) 
                return STATUS_YES_READS_NEEDED;
         }
        return STATUS_NOT_DEFINED;
    }
     
    private int   isReverseReadRequered(Stretch stretch, ArrayList lqr_discrepancies, int refseq_cds_length)
    {
         if ( refseq_cds_length <= m_number_of_bases_covered_by_forward_er 
            || stretch.getCdsStart() <= refseq_cds_length - m_number_of_bases_covered_by_reverse_er)
                return STATUS_NO_READS_NEEDED_OUT_OF_RANGE_FOR_DEFINITION;
        else if (stretch.getCdsStart() >= refseq_cds_length - m_number_of_bases_covered_by_reverse_er
            && ( lqr_discrepancies == null || lqr_discrepancies.size() == 0) )
                return STATUS_NO_READS_NEEDED;
        else if (stretch.getCdsStart() >= refseq_cds_length - m_number_of_bases_covered_by_reverse_er
            && ( lqr_discrepancies != null || lqr_discrepancies.size() > 0) )
                return STATUS_YES_READS_NEEDED;
        else 
            return STATUS_NOT_DEFINED;
    }

    */
    //to fix yeast long linker
    private boolean isYeastLinker(String name)
    {
        name=name.trim();
        if ( name.equalsIgnoreCase("5p retired extra long Yeast Linker")                                              
            || name.equalsIgnoreCase("3p retired extra long Yeast Linker")                                               
            || name.equalsIgnoreCase("5p retired Yeast Short Linker")                                                    
            || name.equalsIgnoreCase("3p retired Yeast Short Linker")  )                                                  
            //|| name.equalsIgnoreCase("5p minimum attB Yeast (phase 1 & 2) linker")                                       
           //|| name.equalsIgnoreCase("3p minimum attB Yeast (phase 1 & 2) linker"))
             return true;
    else 
        return false;

    }

     /*3558 3491 3515 884 6947 6858 3724 */

        public static void main(String [] args)
    {
        GapMapperRunner runner = new GapMapperRunner();
        try
        {
               BecProperties sysProps =  BecProperties.getInstance( BecProperties.PATH);
            sysProps.verifyApplicationSettings();
            edu.harvard.med.hip.bec.DatabaseToApplicationDataLoader.loadDefinitionsFromDatabase();
       
             runner.setUser( AccessManager.getInstance().getUser("htaycher123","htaycher"));
             runner.setInputData( Constants.ITEM_TYPE_CLONEID," 141619	");            
             runner.setProcessType(Constants.PROCESS_FIND_GAPS);
             runner.setIsTryMode(true);
             //SlidingWindowTrimmingSpec spec =   SlidingWindowTrimmingSpec.getDefaultSpec();
          //   spec.setTrimmingType( SlidingWindowTrimmingSpec.TRIM_TYPE_NONE);
             //spec.setQWindowSize( 10);
             runner.setSpecId(73);// 32 for yp3
           //  runner.setVectorFileName("vector_pDonor221_altered.txt");
             runner.setIsRunLQR(false);
             runner.setQualityTrimmingScore (0);
   runner.setQualityTrimmingLastBase (0);
   runner.setQualityTrimmingFirstBase (0);
  
             runner.run();
            /* spec.setType( SlidingWindowTrimmingSpec.TRIM_TYPE_MOVING_WINDOW);
             runner.setTrimmingSpec(spec);
             runner.run();
              spec =  new SlidingWindowTrimmingSpec();
             spec.setType( SlidingWindowTrimmingSpec.TRIM_TYPE_NONE);
             runner.setTrimmingSpec(spec);
             runner.run();
             */
        }catch(Exception e){}
        System.exit(0);

    }
}
