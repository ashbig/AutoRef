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
import edu.harvard.med.hip.bec.modules.*;
  import java.util.*;
  import edu.harvard.med.hip.utility.*;
/**
 *
 * @author  HTaycher
 */
  public class GapMapperRunner extends ProcessRunner 
{
    public static final int TYPE_COLLECTION_OF_LQR = 0;
    public static final int TYPE_COLLECTION_OF_GAPS = 1;
    
    
    
    private int             m_process_type = TYPE_COLLECTION_OF_LQR;
    private boolean                 m_isTryMode = false;
    
    //parameters for LQS definition
    private int             m_lq_window_size = BaseSequence.INTERNAL_QUALITY_WINDOW_SIZE; 
    private int             m_lq_number_of_lqbases = BaseSequence.INTERNAL_QUALITY_NUMBER_LOW_QUALITY_BASES ; 
    private int             m_lq_number_of_lqconsbases = BaseSequence.INTERNAL_QUALITY_NUMBER_LOW_QUALITY_BASES_CONQ ; 
    private int             m_lq_cutoff = 7; 
    
    //parameters for gap definition
    private int                     m_min_contig_length = 50;
    private int                     m_min_contig_avg_score = 20;
   
    public void         setProcessType(int v){ m_process_type = v;}
    public void         setIsTryMode(boolean isTryMode){m_isTryMode=isTryMode;}
    public void         setMinContigLength(int v){ m_min_contig_length  = v;}
    public void         setMinAvgContigScore(int v){ m_min_contig_avg_score = v;} 
                          
    
    
    
    public String   getTitle()    {return "Request for Gap Finder execution";    }   
    public void    setLQWindowSize (int v){m_lq_window_size = v ; } 
    public void    setLQNumberOfBases (int v){m_lq_number_of_lqbases = v ;} 
    public void    setLQConsNumberOfBases (int v){m_lq_number_of_lqconsbases = v  ;} 
    public void    setLQCutOff (int v){m_lq_cutoff = v ;} 

    
    public void run()
    {
         Connection conn = null;
         int process_id = -1;
          FileWriter reportFileWriter = null;
         PreparedStatement pst_check_prvious_process_complition = null;
         PreparedStatement pst_insert_process_object = null;
         StretchCollection stretch_collection = null;
         try
        {
            conn = DatabaseTransaction.getInstance().requestConnection();
            ArrayList clone_ids = Algorithms.splitString( m_items);
          
            for (int count_clones = 0; count_clones < clone_ids.size(); count_clones++)
            {
                try// by stretch collection
                {
                    stretch_collection = findStretches(Integer.parseInt( (String) clone_ids.get(count_clones)));
                    if (stretch_collection != null )
                    {
                        if ( ! m_isTryMode )
                        {
                             if ( process_id == -1) 
                            {
                                process_id = createProcessRecord(  conn,  pst_check_prvious_process_complition, pst_insert_process_object) ;
                            }
                            stretch_collection.insert(conn);
                             //insert process_object
                          //  pst_insert_process_object.setInt(1, stretch_collection.getId());
                          //  DatabaseTransaction.executeUpdate(pst_insert_process_object);
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
                            String stretch_collection_report = getReportForStretchCollection(stretch_collection);
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
    
    private StretchCollection findStretches( int clone_id)
    {
        StretchCollection stretch_collection = null;
        if ( m_process_type == TYPE_COLLECTION_OF_LQR)
        {
           // AnalizedScoredequence clone_sequence = getLastCloneSequence(clone_id);
        }
        else if (m_process_type == TYPE_COLLECTION_OF_GAPS)
        {
            GapMapper mapper = new GapMapper();
            mapper.setCloneId(clone_id);
           // mapper.setMinContigLength(m_min_contig_length);
            mapper.setMinAvgContigScore(m_min_contig_avg_score);
            mapper.run();
            stretch_collection = mapper.getStretchCollection();
            if ( mapper.getErrorMessages() != null && mapper.getErrorMessages().size() > 0)
                m_error_messages.addAll( mapper.getErrorMessages());
        }
          for (int count =0; count < stretch_collection.getStretches().size(); count++)
          {
              Stretch s = (Stretch) stretch_collection.getStretches().get(count);
              System.out.println(count + " "+s.getCdsStart()+" "+s.getCdsStop() );
          }
        return stretch_collection ;
    }
    private void   createPreparedStatements(int process_id, Connection conn, 
            PreparedStatement pst_check_privious_process_complition, 
            PreparedStatement pst_insert_process_object)throws Exception
          
    {
        pst_insert_process_object = conn.prepareStatement("insert into process_object (processid,objectid,objecttype) values("+process_id+",?,"+Constants.PROCESS_OBJECT_TYPE_STRETCH_COLLECTION+")");
     //   pst_check_privious_process_complition = conn.prepareStatement("insert into process_object (processid,objectid,objecttype) values("+process_id+",?,"+Constants.PROCESS_OBJECT_TYPE_CONTAINER+")");
 
    }
    
    
    private int createProcessRecord( Connection conn, 
        PreparedStatement pst_check_prvious_process_complition, 
        PreparedStatement pst_insert_process_object) throws Exception
    {
        int process_id = -1;
        if ( m_process_type == TYPE_COLLECTION_OF_LQR)
            process_id = Request.createProcessHistory( conn, ProcessDefinition.RUN_GAP_MAPPER_FOR_LOWQUALITY_SEQUENCE, new ArrayList(),m_user) ;
        else if (m_process_type == TYPE_COLLECTION_OF_GAPS)
                process_id = Request.createProcessHistory( conn, ProcessDefinition.RUN_GAP_MAPPER, new ArrayList(),m_user) ;
         createPreparedStatements( process_id, conn, pst_check_prvious_process_complition, pst_insert_process_object);
        return process_id;
    }
    
     
            //PreparedStatement pst_get_flexsequenceid,PreparedStatement pst_get_flexsequence_length,
     private String getReportForStretchCollection( StretchCollection oligo_calculation)throws Exception
    {
       // System.out.println("A"+oligo_calculation.getSequenceId());
        StringBuffer buf = new StringBuffer();
        /*
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
        buf.append("RefSequence Id: "+oligo_calculation.getSequenceId()+Constants.LINE_SEPARATOR);
        buf.append("RefSequence Length: "+refsequence_cdslength+Constants.LINE_SEPARATOR);
        buf.append("FLEX RefSequence Id: "+flexrefsequenceid+Constants.LINE_SEPARATOR);
        for (int oligo_index = 0; oligo_index < oligo_calculation.getOligos().size();oligo_index ++)
        {
            buf.append( ((Oligo)oligo_calculation.getOligos().get(oligo_index)).geneSpecificOligotoString()+Constants.LINE_SEPARATOR);
        }
         **/
        return buf.toString();
    }
     
     
     /*REF: FLEX Id	Clone ID
833	6345 *

2055	3214 *   ??????????????????
2394	3491
2417	3515   ???????????????????
2445	3558  ??????????????????
2657	884
2695	3724
2875	4090
5173	6596

5618	6858 *
5785	6947 *
3558 3491 3515 884 6947 6858 3724 */
     
        public static void main(String [] args)
    {
        GapMapperRunner runner = new GapMapperRunner();
        try
        {
             runner.setUser( AccessManager.getInstance().getUser("htaycher123","htaycher"));
             runner.setItems(" 3558 3491 3515 884 6947 6858");
             runner.setItemsType( Constants.ITEM_TYPE_CLONEID);
             runner.setProcessType(TYPE_COLLECTION_OF_GAPS);
             runner.setIsTryMode(false);
             runner.run();
        }catch(Exception e){}
        System.exit(0);
       
    }
}
