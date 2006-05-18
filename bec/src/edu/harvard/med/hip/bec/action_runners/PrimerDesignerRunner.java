//Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->
/*
 * PrimerDesignerRunner.java
 *
 * Created on October 27, 2003, 5:11 PM
 */

package edu.harvard.med.hip.bec.action_runners;


import java.sql.*;
import java.io.*;

import  edu.harvard.med.hip.bec.programs.primer3.*;
import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.programs.needle.*;
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.coreobjects.oligo.*;
import edu.harvard.med.hip.bec.coreobjects.feature.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.form.*;
import edu.harvard.med.hip.bec.user.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.Constants;
import edu.harvard.med.hip.bec.sampletracking.mapping.*;
import edu.harvard.med.hip.bec.sampletracking.objects.*;
import edu.harvard.med.hip.bec.programs.assembler.*;
import edu.harvard.med.hip.bec.util_objects.*;
import edu.harvard.med.hip.bec.ui_objects.*;
  import java.util.*;
  import edu.harvard.med.hip.utility.*;
/**
 *
 * @author  HTaycher
 */
public class PrimerDesignerRunner extends ProcessRunner 
{
    public  static final int        COVERAGE_TYPE_REFERENCE_CDS = 0;
    public  static final int        COVERAGE_TYPE_GAP_LQR = 1;
   // public  static final int        COVERAGE_TYPE_GAP_LQR_DISCREPANCIES = 3;
   
     public  static final int        LQR_COVERAGE_TYPE_NONE = 0;
     public  static final int        LQR_COVERAGE_TYPE_ANY_LQR = 1;
     public  static final int        LQR_COVERAGE_TYPE_LQR_WITH_DISCREPANCY = 2;
     public  static final int        LQR_COVERAGE_TYPE_LQR_DISCREPANCY_REGIONS = 3;
   //  public  static final int        LQR_COVERAGE_TYPE_COVER_EACH_DISCREPANCY = 4;
   //  public  static final int        LQR_COVERAGE_TYPE_COVER_EACH_LOWQ_DISCREPANCY = 5;
   //  public  static final int        LQR_COVERAGE_TYPE_COVER_EACH_LQDISCREPANCY = 6;
    public  static final int        LQR_COVERAGE_TYPE_LQR_WITH_DISCREPANCY_LQDISCREPANCIES = 7;
     
      //public  static final int        LQR_COVERAGE_TYPE_COVER_EACH_LOWQ_DISCREPANCY_IN_LQR = 7;
    
 
    
     public  static final String    STRETCH_PRIMERS_APNAME_COLLECTIONS_TYPE = "typeCollectionType";
    
     public  static final String    STRETCH_PRIMERS_APNAME_SEQUENCE_COVERAGE_TYPE = "typeSequenceCoverage";
     public  static final String    STRETCH_PRIMERS_APNAME_LQR_COVERAGE_TYPE = "typeLQRCoverage";
     public  static final String    STRETCH_PRIMERS_APNAME_MIN_DISTANCE_BETWEEN_STRETCHES = "minDistanceBetweenStretchesToBeCombined"; 
   //-----------------------------------------------------------------------------  
    
    private int                     m_spec_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    private Primer3Spec             m_spec = null;
    private boolean                 m_isTryMode = false;
    private int                     m_type_of_sequence_coverage = COVERAGE_TYPE_REFERENCE_CDS;
    private int                     m_type_of_lqr_coverage = LQR_COVERAGE_TYPE_LQR_WITH_DISCREPANCY;//if gaps & lqr: cover only gaps or lqr as well
    private int                     m_min_distance_between_stretches = 0;
    
    private int                     m_min_distance_between_stretches_divider = 30;
    private int                     m_number_of_bases_to_start_stop_requier_er = 200;
   
    /** Creates a new instance of PolymorphismFinderRunner */
    public void         setSpecId(int v){m_spec_id = v;}
    public void         setIsTryMode(boolean isTryMode){m_isTryMode=isTryMode;}
    public void         setTypeOfSequenceCoverage(int v) { m_type_of_sequence_coverage = v;}
    public void         setIsLQRCoverageType(int v) { m_type_of_lqr_coverage = v;}
    public void         setMinDistanceBetweenStretchesToBeCombined(int v){ m_min_distance_between_stretches = v;} 
    
    public String       getTitle()   
    {  
        String title = "Request for primer designer execution.";
        if ( m_type_of_sequence_coverage == COVERAGE_TYPE_GAP_LQR )
        {
           title +=" Stretch collection coverage.";
        }
        else
            title += " Reference Sequence coverage.";
        return title;
    }
  
    
    
    public void run_process()
    {
         int id = -1; int process_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
         Connection conn = null;
         OligoCalculation oligo_calculation = null;
         String sql = "";
         Statement stmt = null;
         PreparedStatement pst_check_oligo_cloning = null;
         PreparedStatement pst_get_flexsequenceid = null;
          PreparedStatement pst_insert_process_object = null;
           PreparedStatement pst_get_flexsequence_length= null;
           PreparedStatement pst_check_oligo_cloning_for_stretches = null;
           FileWriter reportFileWriter = null;QueryItem item = null;
        try
        {
            conn = DatabaseTransaction.getInstance().requestConnection();
            pst_insert_process_object = conn.prepareStatement("insert into process_object (processid,objectid,objecttype) values(?,?,"+Constants.PROCESS_OBJECT_TYPE_OLIGOCALCULATION+")");
            pst_check_oligo_cloning = conn.prepareStatement("select sequenceid from oligo_calculation where stretchcollectionid is null and sequenceid = ? and primer3configid = "+m_spec_id);
            pst_check_oligo_cloning_for_stretches = conn.prepareStatement("select sequenceid from oligo_calculation where stretchcollectionid =?  and primer3configid = "+m_spec_id +
            " and STRETCHDEFPARAMS='" + buildStretchCollectionParamString()+"'");
          
            pst_get_flexsequenceid = conn.prepareStatement("select flexsequenceid from view_flexbecsequenceid where becsequenceid = ? ");
            pst_get_flexsequence_length = conn.prepareStatement("select (cdsstop-cdsstart) as cdslength from refsequence where sequenceid = ? ");
 
            //get primer spec
            if ( !getPrimer3Spec()  ) return;
            //run primer3 with specified spec
            Primer3Wrapper primer3 = new Primer3Wrapper();
            primer3.setSpec(m_spec);
            m_number_of_bases_to_start_stop_requier_er = (int)m_spec.getParameterByNameInt("P_SINGLE_READ_LENGTH") / 2;
            //gett refsequence ids or stretch collections ids
            ArrayList  query_items =     getObjectIds(-1);
            if ( query_items == null || query_items.size() <1 ) return;
            
            if (! m_isTryMode )
            {
                //create process
                ArrayList specs =new ArrayList();
                specs.add(m_spec);
                process_id = Request.createProcessHistory( conn, ProcessDefinition.RUN_DESIGN_OLIGO,specs,m_user) ;
                conn.commit();
            }
            if ( m_isTryMode )
            {
                File reportFile = new File(Constants.getTemporaryFilesPath() + "PrimerDesignerReport"+System.currentTimeMillis()+".txt");
                m_file_list_reports.add(reportFile);
                reportFileWriter =  new FileWriter(reportFile);
                
            }
            for (int index =  0;  index < query_items.size(); index++)
             {
                synchronized(this)
                {
                    try
                        {
                            Thread.currentThread().sleep(100);
                            item = (QueryItem) query_items.get(index);
                            ArrayList oligo_calculations =getOligoCalculations( item.getWorkingItem(), pst_check_oligo_cloning, pst_check_oligo_cloning_for_stretches,primer3);
                     
                            if ( primer3.getFailedSequences() != null && primer3.getFailedSequences().size() >0)
                                m_error_messages.addAll(primer3.getFailedSequences());
                            if ( oligo_calculations == null || oligo_calculations.size() < 1)
                            {
                                continue;
                            }
                            oligo_calculation = (OligoCalculation)oligo_calculations.get(0);
                            if ( ! m_isTryMode )
                            {
                                oligo_calculation.insert(conn);
                                     //insert process_object
                                pst_insert_process_object.setInt(1,process_id);
                                pst_insert_process_object.setInt(2, item.getWorkingItem());
                                DatabaseTransaction.executeUpdate(pst_insert_process_object);
                                conn.commit();
                               
                            }
                            else
                            {
                                String ol_report = getReportForOligoCalculation(pst_get_flexsequenceid, pst_get_flexsequence_length,oligo_calculation, item);
                                reportFileWriter.write( ol_report);
                                reportFileWriter.flush();
                            }
                           
                        }
                        catch(Exception e)
                        {
                            DatabaseTransaction.rollback(conn);
                            m_error_messages.add(e.getMessage());
                        }
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
            if ( m_isTryMode )
            {
                try {reportFileWriter.close();}catch(Exception e){ try { reportFileWriter.close();}catch(Exception n){} }
            }
        
            sendEMails( getTitle());
        }

            //request->process->process_config|| process_object(refsequence)
    }
    
    
    
    //------------------------------------------------------------------
    private boolean getPrimer3Spec()
    {  //get primer spec
        try
        {
            m_spec = (Primer3Spec)Spec.getSpecById(m_spec_id);
            if (m_spec != null)            return true;
            return false;
        }
        catch(Exception e)
        {
            m_error_messages.add("Cannot extract primer3 configuration with id "+m_spec_id);
            return false;
        }
    }
    
    
    private BaseSequence getRefsequence(int id) throws Exception
    {
        RefSequence refsequence = new RefSequence( id, false);
        
        String sequence_text = refsequence.getCodingSequence();//BaseSequence.getSequenceInfo(id, BaseSequence.SEQUENCE_INFO_TEXT);
        BaseSequence refsequence_for_calculations = new BaseSequence();
        refsequence_for_calculations.setText(sequence_text);
        refsequence_for_calculations.setId(id);
        return refsequence_for_calculations;
        
    }
    
    
    private String      getQueryStringToGetRefSequenceIds(int rownum)
    {
        ArrayList items = Algorithms.splitString( m_items);
        switch ( m_items_type)
        {
            case  Constants.ITEM_TYPE_CLONEID :
            {
                return "select  refsequenceid as working_id , refsequenceid as repeat_id,  flexcloneid as query_id "
+" from sequencingconstruct c, flexinfo f,  isolatetracking i "
+" where i.constructid = c.constructid and f.isolatetrackingid = i.isolatetrackingid and   "
+" flexcloneid in ("+Algorithms.convertStringArrayToString(items,"," )+")" ;
/*
                return "select distinct refsequenceid as id from sequencingconstruct where constructid in "
                +" (select constructid from isolatetracking where isolatetrackingid in "
                +" (select isolatetrackingid from flexinfo where flexcloneid in" +
                " ("+Algorithms.convertStringArrayToString(items,"," )+")))";
                */
            }
            case  Constants.ITEM_TYPE_PLATE_LABELS :
            {
                StringBuffer plate_names = new StringBuffer();
                for (int index = 0; index < items.size(); index++)
                {
                    plate_names.append( "'");
                    plate_names.append((String)items.get(index));
                    plate_names.append("'");
                    if ( index != items.size()-1 ) plate_names.append(",");
                }
                return "select  distinct refsequenceid as working_id , refsequenceid as repeat_id, label as query_id "
                +" from sequencingconstruct c, sample s, containerheader ch,  isolatetracking i "
                +" where i.constructid = c.constructid and i.sampleid=s.sampleid and   s.containerid=ch.containerid and "
+" label in ("+plate_names.toString()+")";

/*
                 return "select  refsequenceid as id from sequencingconstruct where constructid in "
                +"  (select constructid from isolatetracking where sampleid in "
                +"  (select sampleid from sample where containerid in (select containerid from "
                +" containerheader where label in ("+plate_names.toString()+"))))";
 **/
            }
            case  Constants.ITEM_TYPE_ACE_REF_SEQUENCE_ID :
            {
                return "select distinct refsequenceid as working_id , refsequenceid as repeat_id, refsequenceid as query_id "
                +" from sequencingconstruct where  refsequenceid in  ("+Algorithms.convertStringArrayToString(items,"," )+")";

              //  return "select distinct refsequenceid as id from assembledsequence where sequenceid in  ("+Algorithms.convertStringArrayToString(items,"," )+")";
            }
            case  Constants.ITEM_TYPE_FLEXSEQUENCE_ID :
            {
                return "select distinct refsequenceid as working_id , refsequenceid as repeat_id, flexsequenceid as query_id "
                +" from flexinfo f, isolatetracking i, sequencingconstruct c where f.isolatetrackingid=i.isolatetrackingid and c.constructid=i.constructid "
+" and  flexsequenceid in  ("+Algorithms.convertStringArrayToString(items,"," )+")";

/*
                return "select distinct refsequenceid as id from sequencingconstruct where constructid in "
                +" (select constructid from isolatetracking where isolatetrackingid in "
                +" (select isolatetrackingid from flexinfo where flexsequenceid in ("+Algorithms.convertStringArrayToString(items,"," )+")))";
          */
            }
        }
        return null;
    }
    
    private String      getQueryStringToGetStretchCollectionIds(int rownum)
    {
        ArrayList items = Algorithms.splitString( m_items);
        switch ( m_items_type)
        {
            case  Constants.ITEM_TYPE_CLONEID :
            {
                return "select flexcloneid as query_id, collectionid as working_id , f.isolatetrackingid as repeat_id"
                +" from stretch_collection s, flexinfo f where  f.isolatetrackingid =s.isolatetrackingid and "
                +" flexcloneid in (" +Algorithms.convertStringArrayToString(items,"," )+" ) order by f.isolatetrackingid, collectionid desc";
       /*
                return "select collectionid as working_id , isolatetrackingid from stretch_collection "
                +"  where isolatetrackingid in  (select isolatetrackingid from  flexinfo where flexcloneid in ("
                +Algorithms.convertStringArrayToString(items,"," )+")) order by isolatetrackingid, collectionid desc";
          */
            }
            case  Constants.ITEM_TYPE_PLATE_LABELS :
            {
                StringBuffer plate_names = new StringBuffer();
                for (int index = 0; index < items.size(); index++)
                {
                    plate_names.append( "'");
                    plate_names.append((String)items.get(index));
                    plate_names.append("'");
                    if ( index != items.size()-1 ) plate_names.append(",");
                }
                return  "select collectionid as working_id , label as query_id, i.isolatetrackingid  as repeat_id"
                +" from stretch_collection  s, sample ss, containerheader c, isolatetracking i "
                +" where i.isolatetrackingid = s.isolatetrackingid and i.sampleid =  ss.sampleid and "
                +" ss.containerid = c.containerid and  label in ( "+plate_names.toString()+") order by i.isolatetrackingid, collectionid desc ";
         
                /*
                 return "select collectionid as id , isolatetrackingid from stretch_collection "
                +"  where isolatetrackingid in   "
                +"  (select isolatetrackingid from isolatetracking where sampleid in "
                +"  (select sampleid from sample where containerid in (select containerid from "
                +" containerheader where label in ("+plate_names.toString()+")))) order by isolatetrackingid, collectionid desc";
            */
            }
        }
        return null;
    }
    /*
    private int         getRefSequenceId(String sql) throws Exception
    {
        
        int id = -1;
        //get sequence id
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        ResultSet rs = null;
        try
        {
            rs = t.executeQuery(sql);
            if(rs.next())
            {
                id = rs.getInt("refsequenceid");
            }
            return id;
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException("Error occured while restoring sequence with id "+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
        
    }
    
    */
    private ArrayList         getObjectIds(int rownum )
    {
        ArrayList object_ids = null; String sql = null;
        switch ( m_type_of_sequence_coverage )
        {
            case  COVERAGE_TYPE_REFERENCE_CDS : //refsequence
        
            {
                sql =  getQueryStringToGetRefSequenceIds(rownum);
                if (sql == null) return null;
                object_ids= getIds( sql ) ;
                break;
            }
            case COVERAGE_TYPE_GAP_LQR :
          //  case COVERAGE_TYPE_GAP_LQR_DISCREPANCIES:
            {
                sql = getQueryStringToGetStretchCollectionIds( rownum) ;
                if (sql == null) return null;
                object_ids= getIds( sql) ;
                break;
            }
            
        }
        
        return object_ids;
            
    }
    private ArrayList         getIds(String sql) 
    {
       
        ArrayList ids = new ArrayList();
        //get sequence id
        DatabaseTransaction t = null;int working_id = -1; String query_id = null;
        ResultSet rs = null;
        Integer repeat_key = null; int repeat_key_int = -1;
        Hashtable repeats = new Hashtable();
        try
        {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while(rs.next())
            {
                //change here to hashmap error
                repeat_key_int = rs.getInt("repeat_id");
                repeat_key = new Integer(repeat_key_int);
                if(  repeats.containsKey(repeat_key) ) 
                    continue;
                else
                {
                    repeats.put(repeat_key, repeat_key);
                    working_id = rs.getInt("working_id");
                    query_id =  String.valueOf( rs.getObject("query_id"));
                    ids.add( new QueryItem(working_id,query_id ) );
                }
            }
            
        } 
        catch (Exception sqlE)
        {
            m_error_messages.add("Error occured while restoring sequence with id "+sqlE+"\nSQL: "+sql);
        } 
        finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
        return ids;
    }
    
    private ArrayList           getOligoCalculations( int id, PreparedStatement pst_check_oligo_cloning,PreparedStatement pst_check_oligo_cloning_for_stretches,Primer3Wrapper primer3) throws Exception
    {
        if (  m_type_of_sequence_coverage == COVERAGE_TYPE_REFERENCE_CDS ) 
            return getOligoCalculationsForRefSequence( id,  pst_check_oligo_cloning,  primer3);
        else 
        {
          
            return getOligoCalculationsForStretchCollection(  id,  pst_check_oligo_cloning_for_stretches,  primer3);
        }
    }
    
    private ArrayList           getOligoCalculationsForRefSequence( int refsequenceid, PreparedStatement pst_check_oligo_cloning, Primer3Wrapper primer3)throws Exception
    {
        ArrayList oligo_calculations = null;
        if (  isPrimersDesigned(refsequenceid, pst_check_oligo_cloning) )
        {
            m_error_messages.add("Primers for refsequence with id "+refsequenceid +"and this spec already designed.");
            if ( !m_isTryMode  ) return null;
        }
    //get reference sequence to process
        BaseSequence refsequence = getRefsequence(refsequenceid);
        primer3.setSequence(  refsequence);
        oligo_calculations = primer3.run();
        if ( oligo_calculations == null || oligo_calculations.size() < 1)
        {
            m_error_messages.add("Cannot design primers for refsequence with id: "+refsequence.getId());

        }
        return oligo_calculations;
    }
    
     private ArrayList           getOligoCalculationsForStretchCollection
                ( int stretchcollectionid, 
                PreparedStatement pst_check_oligo_cloning_for_stretches, 
                Primer3Wrapper primer3)throws Exception
    {
        ArrayList oligo_calculations = null;
        if (  isPrimersDesigned(stretchcollectionid, pst_check_oligo_cloning_for_stretches) )
        {
            m_error_messages.add("Primers for stretch collection with id "+stretchcollectionid +"and this spec already designed.");
            if ( !m_isTryMode  ) return null;
        }
         //check if primer3 spec is OK: no 5/3' universal primers
        if ( !(m_spec.getParameterByNameInt("P_UPSTREAM_DISTANCE") == 0 &&
            m_spec.getParameterByNameInt("P_DOWNSTREAM_DISTANCE") == 0))
        {
            m_error_messages.add("Wrong Primer3 configuration choosen. Cannot design primers for stretch collection id: "+stretchcollectionid);
            return null;
        }
    //prepare data: get last stretch collection and refsequence
        StretchCollection stretch_collection = StretchCollection.getById(stretchcollectionid);
        BaseSequence refsequence = getRefsequence(stretch_collection.getRefSequenceId());
        int cds_length = refsequence.getText().length() - 1;
       
   //get bondaries of refsequence stretches to be resequenced
        // format collection of int[2]
       ArrayList boundaries = stretch_collection.getStretchBoundaries(stretch_collection.getCloneId(),
                    cds_length,  
                    m_type_of_lqr_coverage);

       if ( boundaries == null || boundaries.size() == 0) return oligo_calculations;
   // extend them to the right and to the left by spec.getParameterByNameInt("P_BUFFER_WINDOW_LEN")
       int window_size = m_spec.getParameterByNameInt("P_BUFFER_WINDOW_LEN")
                    +m_spec.getParameterByNameInt("P_EST_SEQ");
       int coverage_direction =  m_spec.getParameterByNameInt("P_NUMBER_OF_STRANDS");
       boundaries = extandBoundaries(stretchcollectionid,boundaries, window_size, cds_length, coverage_direction); 
       if ( boundaries.size() < 1) return null;
      
       //order pair by start position     
       boundaries = orderByStartPosition(boundaries);
   // colaps stretch that are too close to each other - less than spec.getParameterByNameInt("P_SINGLE_READ_LENGTH")/3 ?????
       int min_distance_between_stretches = 0;
       if ( m_min_distance_between_stretches == 0 )
            min_distance_between_stretches = m_min_distance_between_stretches;
       else
            min_distance_between_stretches = (int) m_spec.getParameterByNameInt("P_SINGLE_READ_LENGTH") / m_min_distance_between_stretches_divider ;
       boundaries = collapseNeighboringStretches(boundaries, min_distance_between_stretches);
       
   // get BaseSeqquence per each stretch, set id == cds start
        ArrayList base_sequences_for_design = chopRefSequence(refsequence, boundaries);
        /*
        System.out.println( stretch_collection.getCloneId());
        for ( int i = 0; i < base_sequences_for_design.size();i++)
        {
            BaseSequence b = (BaseSequence) base_sequences_for_design.get(i);
            System.out.println( b.getId() + " "+b.getText().length());
        }
           
        */
    // run primer3 designer on collection of different sequences
        primer3.setSequences(  base_sequences_for_design);
        oligo_calculations = primer3.run();
        if ( oligo_calculations == null || oligo_calculations.size() < 1)
        {
            m_error_messages.add("Cannot design primers for  stretch collection with id "+stretchcollectionid );
            return oligo_calculations;
        }   
      // recalculate start position of each primer based on cds start of the stretch
        oligo_calculations = recalcultePrimerStartPosition(refsequence.getId(),stretch_collection.getId(), oligo_calculations);
        
        return oligo_calculations;
    }
     
     
     
    private boolean             isPrimersDesigned
                            (int id, 
                            PreparedStatement pst_check_oligo)
                            throws Exception
    {
       
        ResultSet rs = null;
        boolean result = true;
        DatabaseTransaction t =null;
        try
        {
            t = DatabaseTransaction.getInstance();
            pst_check_oligo.setInt(1, id);
             rs = t.executeQuery(pst_check_oligo);
            if(rs.next())
            {
                return true;
            }
            else
            {
                return false;
            }
        } 
        catch (Exception sqlE)
        {
           throw new Exception("Error occured while restoring sequence with id "+id);
        } 
        finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
        
    }
    
    
    private String getReportForOligoCalculation(PreparedStatement pst_get_flexsequenceid,
        PreparedStatement pst_get_flexsequence_length, 
        OligoCalculation oligo_calculation, QueryItem item)throws Exception
    {
       // System.out.println("A"+oligo_calculation.getSequenceId());
        StringBuffer buf = new StringBuffer();
        int flexrefsequenceid = -1;
        pst_get_flexsequenceid.setInt(1,oligo_calculation.getSequenceId());
        ResultSet rs = DatabaseTransaction.getInstance().executeQuery(pst_get_flexsequenceid);
        if(rs.next())
        {
            flexrefsequenceid = rs.getInt("flexsequenceid");
            // System.out.println("AS "+flexrefsequenceid);
        }
        int refsequence_cdslength = -1;
        pst_get_flexsequence_length.setInt(1,oligo_calculation.getSequenceId());
        rs = DatabaseTransaction.getInstance().executeQuery(pst_get_flexsequence_length);
        if(rs.next())
        {
            refsequence_cdslength = rs.getInt("cdslength");
            // System.out.println("AS1 "+refsequence_cdslength);
        }
        
        buf.append(Constants.LINE_SEPARATOR );
        buf.append( Constants.getItemTypeAsString(m_items_type)+" "+ item.getQueryItem() +Constants.LINE_SEPARATOR);
        if ( m_type_of_sequence_coverage == COVERAGE_TYPE_GAP_LQR )
        {
            buf.append( "Stretch collection Id "+ item.getWorkingItem() +Constants.LINE_SEPARATOR);
        }
        buf.append("RefSequence Id: "+oligo_calculation.getSequenceId()+Constants.LINE_SEPARATOR);
        buf.append("RefSequence Length: "+refsequence_cdslength+Constants.LINE_SEPARATOR);
        buf.append("FLEX RefSequence Id: "+flexrefsequenceid+Constants.LINE_SEPARATOR);
        buf.append("Primer3 Spec Id: "+oligo_calculation.getPrimer3SpecId()+Constants.LINE_SEPARATOR);
        
        for (int oligo_index = 0; oligo_index < oligo_calculation.getOligos().size();oligo_index ++)
        {
            buf.append( ((Oligo)oligo_calculation.getOligos().get(oligo_index)).geneSpecificOligotoString()+Constants.LINE_SEPARATOR);
        }
        return buf.toString();
    }
    
    // functions for stretch collection calculations
    private ArrayList extandBoundaries(int stretchcollectionid, 
            ArrayList boundaries, int window_size, int refsequence_length, int coverage_direction)
            throws Exception
    {
        ArrayList result = new ArrayList();
        int[] stretch_boundaries = null;
        int cloneid = 0; int boundary = 0;
        for (int count = 0; count < boundaries.size(); count++)
        {
            stretch_boundaries = (int[])boundaries.get(count);
            // check weather make sence to create primer for the stretch
            if (stretch_boundaries[0] < m_number_of_bases_to_start_stop_requier_er  )
            {
                if ( cloneid == 0) cloneid = StretchCollection.getCloneIdByStretchCollectionId(stretchcollectionid);
                m_error_messages.add("Clone "+ cloneid +" needs Forward End read. First stretch has boundaries "+stretch_boundaries[0] + " - "+ stretch_boundaries[1] );
               // continue;
            }
            else
            {
                if ( coverage_direction == Primer3Wrapper.WALKING_TYPE_ONE_STRAND_REVERSE )
                {
                     boundary= stretch_boundaries[0];
                }
                else
                {
                    boundary= stretch_boundaries[0] - window_size;
                }
                boundary = (boundary < 0 ) ? 0 :boundary;
                stretch_boundaries[0] =  boundary;
            }
                
            
            if (stretch_boundaries[1] > refsequence_length - m_number_of_bases_to_start_stop_requier_er) 
            {
                if ( cloneid == 0) cloneid = StretchCollection.getCloneIdByStretchCollectionId(stretchcollectionid);
                m_error_messages.add("Clone "+cloneid+" needs Reverse End read. Last stretch has boundaries "+stretch_boundaries[0] + " - "+ stretch_boundaries[1] );
              //  continue;
            }
            else
            {
                if ( coverage_direction == Primer3Wrapper.WALKING_TYPE_ONE_STRAND_FORWARD )
                {
                    boundary = stretch_boundaries[1];
                }
                else
                {
                    boundary = stretch_boundaries[1] + window_size;
                }
                
                boundary  = ( boundary > refsequence_length ) ? refsequence_length  : boundary ;
                stretch_boundaries[1] =  boundary;
            }
            result.add(stretch_boundaries);
        }
        return result;
    }
    
    private ArrayList orderByStartPosition(ArrayList boundaries)
    {
        Collections.sort(boundaries, new Comparator()
        {
            public int compare(Object o1, Object o2)
            {
                int[] stretch_boundaries_1 = (int[]) o1;
                int[] stretch_boundaries_2 = (int[]) o2;
                return -(   stretch_boundaries_2[1] - stretch_boundaries_1[0]  );
            }
            // Note: this comparator imposes orderings that are inconsistent with equals. 
            public boolean equals(java.lang.Object obj) {      return false;  }
            // compare
        } );
        return boundaries;
    }
    
    private ArrayList collapseNeighboringStretches(ArrayList boundaries, int min_distance_beetwen_stretches)
    {
        ArrayList result = new ArrayList();
        int[] last_element = (int[]) boundaries.get(0);
        int[] current_element = null;
         for (int count = 1; count < boundaries.size(); count++)
        {
            current_element = (int[]) boundaries.get(count);
            if (  Math.abs(last_element[1] - current_element[0]) < min_distance_beetwen_stretches
                 || last_element[1] > current_element[0] )
             {
                 last_element[1]=current_element[1];
             }
            else
            {
                result.add(last_element);
                last_element = current_element;
            }
        }
        if ( result.size() == 0 || ((int[])result.get(result.size() -1 ))[0] != last_element[0] )
            result.add(last_element);
        return result;
    }
    
    private ArrayList  chopRefSequence(BaseSequence refsequence, ArrayList boundaries)
    {
        ArrayList base_sequences_for_design = new ArrayList();
        BaseSequence stretch_sequence = null;
        String sequence_text = null;
        for (int count = 0; count < boundaries.size(); count++)
        {
            int[] current_element = (int[]) boundaries.get(count);
            stretch_sequence = new BaseSequence();
            String refsequence_text = refsequence.getText().trim();
            int start = current_element[0]; int end = current_element[1];
            sequence_text = refsequence_text.substring(start, end );
            stretch_sequence.setText(sequence_text);
            stretch_sequence.setId(current_element[0]);
            base_sequences_for_design.add(stretch_sequence);
        }
        return base_sequences_for_design;
    }
   
    private ArrayList recalcultePrimerStartPosition(int refsequence_id, int stretch_collection_id, ArrayList oligo_calculations)
    {
        ArrayList result = new ArrayList();
        OligoCalculation olc = null; Oligo oligo = null;
        OligoCalculation olc_final = new OligoCalculation();
        olc_final.setPrimer3SpecId( m_spec_id );
        olc_final.setSequenceId(refsequence_id);
        olc_final.setStretchCollectionId(stretch_collection_id);
        String stretch_params =  buildStretchCollectionParamString();
        olc_final.setStretchOligoCalculationParams( stretch_params );
 
        int total_oligo_count = 1;
        for (int count=0; count< oligo_calculations.size();count++)
        {
            olc = (OligoCalculation) oligo_calculations.get(count);
            for(int oligo_count = 0; oligo_count < olc.getOligos().size(); oligo_count++)
            {
                oligo = (Oligo) olc.getOligos().get(oligo_count);
                oligo.setPosition( oligo.getPosition() + olc.getSequenceId());
                oligo.setName( oligo.getName().charAt(0) +""+ total_oligo_count);
                olc_final.addOligo(oligo);
                total_oligo_count++;
            }
        }
        result.add(olc_final);
        return result;
    }
    
    
    private String buildStretchCollectionParamString()
    {
       String stretch_params =  PrimerDesignerRunner.STRETCH_PRIMERS_APNAME_LQR_COVERAGE_TYPE+"="+ m_type_of_lqr_coverage + OligoCalculation.WRITE_PARAM_DELIM
        +PrimerDesignerRunner.STRETCH_PRIMERS_APNAME_MIN_DISTANCE_BETWEEN_STRETCHES+"="+  m_min_distance_between_stretches +OligoCalculation.WRITE_PARAM_DELIM;
       return stretch_params;
    }
    
    class QueryItem
    {
        private String i_query_item = null;
        private int     i_woring_item = -1;
        
        public QueryItem(){}
        public QueryItem(int v, String c){i_query_item = c;i_woring_item = v;}
        
        public int          getWorkingItem(){ return i_woring_item;}
        public String       getQueryItem(){ return i_query_item;}
         public void          setWorkingItem(int v){  i_woring_item = v;}
        public void       setQueryItem(String v){  i_query_item = v;}
    }
    
    ///////////////////////////////////////////////////////
     public static void main(String args[]) 
     
    {
       // InputStream input = new InputStream();
        PrimerDesignerRunner input = null;
        User user  = null;
        try
        {// 3558           775       776       884       638      6947 
            //  input.setItems("   1085 1269 ");
            BecProperties sysProps =  BecProperties.getInstance( BecProperties.PATH);
        sysProps.verifyApplicationSettings();
      input = new PrimerDesignerRunner();
            user = AccessManager.getInstance().getUser("htaycher123","htaycher");
          
           input.setInputData( Constants.ITEM_TYPE_CLONEID,"29951 134929 140264 ");
         //   input.setInputData( Constants.ITEM_TYPE_CLONEID,"145895");
            input.setUser(user);
            input.setSpecId(97);
            input.setIsTryMode(true);
            input.setTypeOfSequenceCoverage(PrimerDesignerRunner.COVERAGE_TYPE_GAP_LQR);
            input.setIsLQRCoverageType(PrimerDesignerRunner.LQR_COVERAGE_TYPE_LQR_DISCREPANCY_REGIONS);
            input.setMinDistanceBetweenStretchesToBeCombined(50);
   
            input.run();
          
           
        }
        catch(Exception e){}
     
        
         
        System.exit(0);
     }
    
     
}
