/*
 * SpecialReportsRunner.java
 *
 * Created on September 8, 2004, 2:33 PM
 */


package edu.harvard.med.hip.bec.action_runners;
import java.sql.*;
import java.io.*;

import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.coreobjects.endreads.*;
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.file.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.user.*;
import edu.harvard.med.hip.bec.Constants;
import edu.harvard.med.hip.bec.sampletracking.mapping.*;
import edu.harvard.med.hip.bec.sampletracking.objects.*;
import edu.harvard.med.hip.bec.util_objects.*;
import edu.harvard.med.hip.bec.ui_objects.*;
  import java.util.*;
  import edu.harvard.med.hip.utility.*;
/**
 *
 * @author  HTaycher
 */
public class SpecialReportsRunner extends ProcessRunner 
{
    private int                m_report_type = -1;
    private int                 m_sequencing_facility = SequencingFacilityFileName.SEQUENCING_FACILITY_HTMBC;
  
    
    public void     setReportType(int report_type){m_report_type=report_type;}
    public void     setSequencingFacility(int v){m_sequencing_facility = v;}
     
    public String getTitle()    
    {  
        switch (m_report_type)
        {
           case Constants.PROCESS_CREATE_ORDER_LIST_FOR_ER_RESEQUENCING  :
           {
               return "Request for sequencing facility order list for end reads report.";
           }
            case Constants.PROCESS_CREATE_ORDER_LIST_FOR_INTERNAL_RESEQUENCING  :
            {
               return "Request for sequencing facility order list for internal reads report.";
           }
            default: return "Not known type of report.";
        }  
    }
    
     
    /** Creates a new instance of TraceFileProcessingRunner */
    public void run()
    {
        String title = null;
        try
        {
           String  report_file_name =    Constants.getTemporaryFilesPath() + "SpecialReport"+ System.currentTimeMillis()+ ".txt";
           ArrayList sql_groups_of_items =  prepareItemsListForSQL();
           for (int count = 0; count < sql_groups_of_items.size(); count++)
           {
               ArrayList print_items = new ArrayList();
                switch (m_report_type)
                {
                   case Constants.PROCESS_CREATE_ORDER_LIST_FOR_ER_RESEQUENCING  :
                   {
                       print_items = createOrderListForERRepeats((String)sql_groups_of_items.get(count) );
                       title = "";
                       break;
                   }
                    case Constants.PROCESS_CREATE_ORDER_LIST_FOR_INTERNAL_RESEQUENCING  :
                    {
                       print_items = createOrderListForInternalRepeats((String)sql_groups_of_items.get(count));   
                       title = "";
                       break;
                   }
                }
                printReport(report_file_name, count, print_items, title);
           }
 
           m_file_list_reports.add(new File(report_file_name));   
        }
        catch(Exception e)
        {
            m_error_messages.add(e.getMessage());
        }
       finally
        {
            sendEMails( getTitle() );
       }
    
     }
     
     
     //-------------------------------------------
     private ArrayList createOrderListForERRepeats(String sql_items)throws Exception
     {
         ArrayList result = new ArrayList();
         ArrayList forward_expected_reads = new ArrayList();
         ArrayList reverse_expected_reads = new ArrayList();
         ArrayList forward_reads = new ArrayList();
         ArrayList reverse_reads = new ArrayList();
         
       String result_status =  Result.RESULT_TYPE_ENDREAD_FORWARD +"," +
    Result.RESULT_TYPE_ENDREAD_FORWARD_PASS+"," + Result.RESULT_TYPE_ENDREAD_FORWARD_FAIL+"," +
    Result.RESULT_TYPE_ENDREAD_REVERSE_FAIL +","   +     Result.RESULT_TYPE_ENDREAD_REVERSE_PASS +"," 
    + Result.RESULT_TYPE_ENDREAD_REVERSE ;  
    
         String sql = "select position, resulttype as readtype ,resultvalueid as readid,label from containerheader c, "
         +" sample s , result r where r.sampleid=s.sampleid and c.containerid=s.containerid "
         +" and label in ("+sql_items+")and resulttype in ("+result_status+") order by label,position";
         ArrayList expected_reads = getExspectedReadDescriptions(sql);
         ReadDescription read_description = null;
         for (int count = 0; count < expected_reads.size(); count++)
         {
             read_description = (ReadDescription) expected_reads.get(count);
             //first process empty reads
             if (read_description.getReadType() == Result.RESULT_TYPE_ENDREAD_FORWARD 
                       || read_description.getReadType() == Result.RESULT_TYPE_ENDREAD_FORWARD_PASS
                       || read_description.getReadType() ==  Result.RESULT_TYPE_ENDREAD_FORWARD_FAIL)
             {
                if( read_description.getReadId() < 1 )
                    forward_reads.add(read_description.getPlateLabel()+"\t"+Algorithms.convertWellFromInttoA8_12(read_description.getPosition())+"\tF" );
                else
                {
                    forward_expected_reads.add(read_description);
                }
             }
             else
             {
                if( read_description.getReadId() < 1 )
                    reverse_reads.add(read_description.getPlateLabel()+"\t"+Algorithms.convertWellFromInttoA8_12(read_description.getPosition())+"\tF" );
                else
                {
                    reverse_expected_reads.add(read_description );
                }
              }
             
         }
        forward_expected_reads = getDataForNotEmptyReads(forward_expected_reads);
        forward_reads.addAll( forward_expected_reads );
         reverse_expected_reads = getDataForNotEmptyReads(reverse_expected_reads);
        reverse_reads.addAll( reverse_expected_reads );
          
         result.addAll(forward_reads);
         result.addAll(reverse_reads);
         return result;
     }
     
     private ArrayList createOrderListForInternalRepeats(String sql_items)throws Exception
     {
           ArrayList result = new ArrayList();
         return result;
     }
     
     private void  printReport(String report_file_name,
                              int write_cycle, ArrayList print_items, String title)
  {
        FileWriter fr = null;
        try
        {
            fr =  new FileWriter(report_file_name, true);
             if (write_cycle == 0) fr.write(title+"\n");        
            for (int count = 0; count < print_items.size(); count++)
            {
                fr.write((String)print_items.get(count)+"\n");
            }
            fr.flush();
            fr.close();
         }
        catch(Exception e){ try { fr.close();}catch(Exception n){} }
     }
     
     
     
    private ArrayList getExspectedReadDescriptions( String sql)throws BecDatabaseException
    {
       ArrayList res = new ArrayList();
       ResultSet rs = null;
       ReadDescription read_desc = null;
       try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while(rs.next())
            {
                read_desc = new ReadDescription();
                read_desc.setPlateLabel(rs.getString("LABEL"));
                read_desc.setPosition( rs.getInt("position"));
                read_desc.setReadType(rs.getInt("readtype"));
                read_desc.setReadId(rs.getInt("readid"));
                res.add(read_desc);
            }
            return res;
        } catch (Exception sqlE)
        {
            throw new BecDatabaseException("Error occured while getting inforamation for end reads:\nSQL: "+sql);
        } finally
        {
            DatabaseTransactionLocal.closeResultSet(rs);
        }
    }


    private ArrayList           getDataForNotEmptyReads(ArrayList read_descriptions)throws BecDatabaseException
    {
        ArrayList result = new ArrayList ();ReadDescription read_description = null;
        PreparedStatement  read_info = null;PreparedStatement  scores_info = null;
        Read read = null;
        StringBuffer scores_bf = new StringBuffer();
        String sql = "select  READID  ,MACHINE  ,CAPILARITY  ,READSEQUENCEID ,TRIMMEDTYPE "
        +"  ,TRIMMEDSTART  ,TRIMMEDEND  ,READTYPE  ,ASSEMBLEDSEQUENCEID  ,RESULTID  ,SCORE "
        +" ,ISOLATETRACKINGID , cdsstart,cdsstop, status from READINFO where readid = ?";
        String sql_scores = "select infotext from sequenceinfo where sequenceid=? and infotype = " + BaseSequence.SEQUENCE_INFO_SCORE +"  order by infoorder";
      
        ResultSet rs = null;ResultSet rs_scores = null;
        System.out.println("start "+ System.currentTimeMillis() );
        try
        {
            read_info = DatabaseTransaction.getInstance().requestConnection().prepareStatement(sql);
            scores_info = DatabaseTransaction.getInstance().requestConnection().prepareStatement(sql_scores);
           
            for (int count = 0; count < read_descriptions.size(); count++)
            {
                scores_bf = new StringBuffer();
                read_description = (ReadDescription)read_descriptions.get(count);
                   System.out.println( read_description.getReadId()+" "+System.currentTimeMillis() );
                read_info.setInt(1, read_description.getReadId() );
                rs = read_info.executeQuery();
                if ( rs.next()) 
                {
                    read = new Read();
                    read.setId( rs.getInt("READID") ); 
                    read.setSequenceId( rs.getInt("READSEQUENCEID"));
                    read.setType( rs.getInt("READTYPE") );     //forward/reverse
                    read.setTrimEnd( rs.getInt("TRIMMEDEND"));
                    read.setTrimStart( rs.getInt("TRIMMEDSTART"));
                    read.setStatus(rs.getInt("status"));
                            System.out.println( read_description.getReadId()+" "+System.currentTimeMillis() );
                     scores_info.setInt(1, read.getSequenceId());
                     rs_scores = scores_info.executeQuery();
                     while(rs_scores.next())
                     {
                        scores_bf.append( rs_scores.getString("infotext") );
                     }
                       
                    if ( isRepeatNeeded( read, Algorithms.getConvertStringToIntArray(scores_bf.toString()," ")) )
                        result.add(read_description.getPlateLabel()+"\t"+Algorithms.convertWellFromInttoA8_12(read_description.getPosition())+"\tF" );
                }
           }
            return result;
        }
        catch(Exception e)
        {
            throw new BecDatabaseException( e.getMessage() );
        }
        finally
        {
            if ( rs != null ) DatabaseTransaction.closeResultSet(rs);
        }
        
    }
    
    private boolean             isRepeatNeeded(Read read, int[] scores)throws BecDatabaseException
    {
        if ( read.getType() == Read.TYPE_ENDREAD_REVERSE_SHORT || read.getType() == Read.TYPE_ENDREAD_FORWARD_SHORT)
            return true;
        if ( read.getTrimEnd() - read.getTrimStart() < 65 + 50)   return true;
        int bases_to_check = ( scores.length <= 300 ) ? scores.length : 300;
        int good_bases = 0;
        for ( int bases = 1; bases <= bases_to_check; bases++)
        {
            if ( scores[bases] >= 20) good_bases++;
        }
        if ( good_bases < 100  )      return true;
        return false;
    }

     
     protected class ReadDescription
     {
         
        private String		  i_plate_label  = null;
        private int		  i_position  = -1;
        private int		  i_read_type = -100 ;
        private int		  i_read_id  = -1;

        public ReadDescription(){}
        protected String            getPlateLabel( ){ return  i_plate_label  ;}
        protected int            getPosition( ){ return  i_position  ;}
        protected int            getReadType( ){ return  i_read_type  ;}
        protected int            getReadId( ){ return  i_read_id  ;}

        protected void            setPlateLabel(String v){ i_plate_label = v;}
        protected void            setPosition(int v){ i_position = v;}
        protected void            setReadType(int v){ i_read_type = v;}
        protected void            setReadId(int v){ i_read_id = v;}
     }
     
     
     //------------------------------------------------ 
     
    public static void main(String args[]) 
     
    {   try
         {
             
          SpecialReportsRunner runner = new SpecialReportsRunner();
           runner.setSequencingFacility(SequencingFacilityFileName.SEQUENCING_FACILITY_HTMBC);
           runner.setUser( AccessManager.getInstance().getUser("htaycher123","htaycher"));
            runner.setReportType(Constants.PROCESS_CREATE_ORDER_LIST_FOR_ER_RESEQUENCING);
            runner.setItems(" BSA000768   ");
            
            
            runner.setItemsType( Constants.ITEM_TYPE_PLATE_LABELS);
         runner.run();
             
         }catch(Exception e){}
         System.exit(0);
     }
    
   
}
