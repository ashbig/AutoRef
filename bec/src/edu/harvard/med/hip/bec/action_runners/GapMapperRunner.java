package edu.harvard.med.hip.bec.action_runners;


import java.sql.*;
import java.io.*;
import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.coreobjects.oligo.*;
import edu.harvard.med.hip.bec.coreobjects.feature.*;
import edu.harvard.med.hip.bec.database.*;
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
    
    public void         setProcessType(int v){ m_process_type = v;}
    public void         setSpecId(int v){m_spec_id = v;}
    public void         setIsTryMode(boolean isTryMode){m_isTryMode=isTryMode;}
    public void         setIsRunLQR(boolean v){ m_isRunLQRFinderForContigs = v ;}
    
    
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
          FileWriter reportFileWriter = null;
         PreparedStatement pst_check_prvious_process_complition = null;
         PreparedStatement pst_insert_process_object = null;
         StretchCollection stretch_collection = null;
         SlidingWindowTrimmingSpec       trimming_spec = null;
   
         try
        {
            // get spec
            trimming_spec = (SlidingWindowTrimmingSpec)Spec.getSpecById(m_spec_id);
            conn = DatabaseTransaction.getInstance().requestConnection();
            ArrayList clone_ids = Algorithms.splitString( m_items);
            for (int count_clones = 0; count_clones < clone_ids.size(); count_clones++)
            {
                try// by stretch collection
                {
                    stretch_collection = findStretches(Integer.parseInt( (String) clone_ids.get(count_clones)), trimming_spec);
                    stretch_collection.setSpecId(m_spec_id);
                          //define lqr for contgi sequences
                    if ( m_isRunLQRFinderForContigs && m_process_type == Constants.PROCESS_FIND_GAPS)
                            getLQRForDefinedContigs( stretch_collection, trimming_spec);
                    if (stretch_collection != null )
                    {
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
                        else 
                        {
                            if (reportFileWriter == null)
                            {
                                File reportFile = new File(Constants.getTemporaryFilesPath() + "primer3Report.txt");
                                m_file_list_reports.add(reportFile);
                                reportFileWriter =  new FileWriter(reportFile);
                            }
                            String stretch_collection_report = getReportForStretchCollection((String)clone_ids.get(count_clones), stretch_collection);
                            reportFileWriter.write( stretch_collection_report);
                            reportFileWriter.flush();
                        }
                    }
                }
                catch(Exception e)
                {
                    DatabaseTransaction.rollback(conn);
                    m_error_messages.add(e.getMessage());
                }
         }
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
                   ArrayList boundaries =  ScoredSequence.getLQR( contig.getSequence().getScoresAsArray() ,   trimming_spec);
                   for (int index = 0; index < boundaries.size(); index++)
                   {
                        stretch_boundaries = (int[]) boundaries.get(index);
                        lqr = new Stretch();
                        lqr.setType( Stretch.GAP_TYPE_LOW_QUALITY);
                        lqr.setCdsStart( stretch_boundaries[0]);
                        lqr.setCdsStop( stretch_boundaries[1] );
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
            SlidingWindowTrimmingSpec       trimming_spec)throws Exception
    {
        StretchCollection stretch_collection = null;
        if ( m_process_type == Constants.PROCESS_FIND_LQR_FOR_CLONE_SEQUENCE)
        {
             stretch_collection = getLQRForClone( clone_id,   trimming_spec);
        }
        else if (m_process_type == Constants.PROCESS_FIND_GAPS)
        {
            GapMapper mapper = new GapMapper();
            mapper.setCloneId(clone_id);
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
            (int clone_id, SlidingWindowTrimmingSpec trimming_spec)
            throws Exception
    {
            CloneDescription clone_description = CloneDescription.getCloneDescription( clone_id);
            CloneSequence clone_sequence = CloneSequence.getOneByCloneId(clone_id);
            StretchCollection  stretch_collection = null;
            if ( clone_sequence != null && clone_description != null) //sequence assembled , checking for lqr
            {
                //check weather this collection was already created
                if( StretchCollection.getByCloneSequenceIdAndSpecId(clone_sequence.getId(), trimming_spec.getId()) != null )
                {
                     m_error_messages.add("LQR collection for clone id: "+clone_id +" already exists.");
                     return null;
                }
                    
                ArrayList lqr_stretches = ScoredSequence.getLQR(clone_sequence,  trimming_spec);
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
    
     
            //PreparedStatement pst_get_flexsequenceid,PreparedStatement pst_get_flexsequence_length,
     private String getReportForStretchCollection(String clone_id, StretchCollection stretch_collection)throws Exception
    {
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
        if ( m_process_type == Constants.PROCESS_FIND_LQR_FOR_CLONE_SEQUENCE )
        {
                clone_sequence = CloneSequence.getOneByCloneId(Integer.parseInt( clone_id) );
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
            }
            else
                buf.append( stretch.toString() );
            buf.append(Constants.LINE_SEPARATOR );
            /*
            if ( stretch.getSequence() != null)
            {
                buf.append( stretch.getSequence().getText() );
                buf.append(Constants.LINE_SEPARATOR );
                buf.append( stretch.getSequence().getScores() );
                buf.append(Constants.LINE_SEPARATOR );
            }
             **/
        }
        return buf.toString();
    }
     
     
     /*3558 3491 3515 884 6947 6858 3724 */
     
        public static void main(String [] args)
    {
        GapMapperRunner runner = new GapMapperRunner();
        try
        {
             runner.setUser( AccessManager.getInstance().getUser("htaycher123","htaycher"));
             runner.setItems("  3558 ");
             runner.setItemsType( Constants.ITEM_TYPE_CLONEID);
             runner.setProcessType(Constants.PROCESS_FIND_GAPS);
             runner.setIsTryMode(true);
             //SlidingWindowTrimmingSpec spec =   SlidingWindowTrimmingSpec.getDefaultSpec();
          //   spec.setTrimmingType( SlidingWindowTrimmingSpec.TRIM_TYPE_NONE);
             //spec.setQWindowSize( 10);
             runner.setSpecId(25);
             runner.setIsRunLQR(true);
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
