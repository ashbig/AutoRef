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
import edu.harvard.med.hip.bec.coreobjects.feature.*;
import edu.harvard.med.hip.bec.file.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.user.*;
import edu.harvard.med.hip.bec.Constants;
import edu.harvard.med.hip.bec.sampletracking.mapping.*;
import edu.harvard.med.hip.bec.sampletracking.objects.*;
import edu.harvard.med.hip.bec.programs.phred.*;
import edu.harvard.med.hip.bec.util_objects.*;
import edu.harvard.med.hip.bec.ui_objects.*;
import edu.harvard.med.hip.bec.file.*;
import edu.harvard.med.hip.bec.programs.phred.*;


  import java.util.*;
  import edu.harvard.med.hip.utility.*;
/**
 *
 * @author  HTaycher
 */
public class SpecialReportsRunner extends ProcessRunner 
{
    public static final int             STATUS_NO_READS_NEEDED = -1;
    public static final int             STATUS_NO_READS_NEEDED_OUT_OF_RANGE_FOR_DEFINITION = -2;
    public static final int             STATUS_NOT_DEFINED = 0;
    public static final int             STATUS_YES_READS_NEEDED = 1;

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
            case Constants.PROCESS_CREATE_REPORT_TRACEFILES_QUALITY:
            {
                return "Request for Trace Files quality report.";
            }
            default: return "Not known type of report.";
        }  
    }
    
     
    /** Creates a new instance of TraceFileProcessingRunner */
    public void run()
    {
        String title = null;
        int pass_score = 0; int first_base =  0; int last_base = 0;    int min_length = 0;
         //get triming parameters
        
        try
        {
           String  report_file_name =    Constants.getTemporaryFilesPath() + "SpecialReport"+ System.currentTimeMillis()+ ".txt";
           ArrayList sql_groups_of_items =  prepareItemsListForSQL();
           for (int count = 0; count < sql_groups_of_items.size(); count++)
           {
               ArrayList print_items = new ArrayList();
               if ( count == 0 && title != null ) print_items.add( title + Constants.LINE_SEPARATOR);
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
                   case Constants.PROCESS_CREATE_REPORT_TRACEFILES_QUALITY:
                    {
                        
                        pass_score = Integer.parseInt( edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("PHRED_QUALITYDEF_SCORE_PASS") );
                        first_base =  Integer.parseInt(edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("PHRED_QUALITYDEF_FIRST_BASE") );
                        last_base =  Integer.parseInt(edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("PHRED_QUALITYDEF_LAST_BASE") ) ;
                        min_length = Integer.parseInt( edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("PHRED_QUALITYDEF_MIN_LENGTH"));
                    
                        print_items = getTraceFilesQualityData((String)sql_groups_of_items.get(count),
                                       pass_score , first_base ,last_base , min_length );
                        print_items.add(0, "CloneId"+Constants.TAB_DELIMETER + "Trace File Name"+Constants.TAB_DELIMETER +"Quality Status");
                        break;
                        
                    }
                }
                
                Algorithms.writeArrayIntoFile(print_items, true , report_file_name);
               // printReport(report_file_name, count, print_items, title);
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
                    forward_reads.add(read_description.getPlateLabel()+"\t"+edu.harvard.med.hip.bec.sampletracking.objects.Container.convertPositionFrom_int_to_alphanumeric(read_description.getPosition())+"\tF" );
                else
                {
                    forward_expected_reads.add(read_description);
                }
             }
             else
             {
                if( read_description.getReadId() < 1 )
                    reverse_reads.add(read_description.getPlateLabel()+"\t"+edu.harvard.med.hip.bec.sampletracking.objects.Container.convertPositionFrom_int_to_alphanumeric(read_description.getPosition())+"\tR" );
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
   
     
     //------------------------------------------------------------
     
     private ArrayList getTraceFilesQualityData(String sql_items,
             int  pass_score , int first_base ,
             int last_base ,int min_length) 
     {
         ArrayList print_items = new ArrayList();
         String sql = constructQueryStringForTraceFilesQualityCheck( sql_items);
        //get clone data
         if ( sql == null || sql.trim().length() == 0) return print_items;
         Hashtable clone_directories =  getCloneDirectories( sql);
         //run thr. clone directory
         if ( clone_directories == null || clone_directories.size() < 1 ) return print_items;
         print_items = checkTraceFilesQuality(clone_directories ,   pass_score , first_base ,last_base ,min_length );
         return print_items;
         
     }
     
     
     private ArrayList checkTraceFilesQuality(Hashtable clone_directories,
                   int pass_score , int first_base ,int last_base ,
                   int min_length) 
     {
         ArrayList print_items = new ArrayList();
         String cloneid = null; String clone_directory = null;
         for ( Enumeration cloneinfo = clone_directories.keys(); cloneinfo.hasMoreElements();)
         {
             try
             {
                 cloneid =  (String) cloneinfo.nextElement() ;
                 clone_directory = (String) clone_directories.get( cloneid );
                 print_items.addAll( processCloneTraceFiles(cloneid, clone_directory,  pass_score , first_base ,last_base ,min_length ) );
        
             }catch (Exception e)
             {
                 m_error_messages.add("Cannot get data for clone " + cloneid);
             }
         }
         return print_items;
     }
     
     
     private ArrayList     processCloneTraceFiles(String cloneid, String clone_directory,
                    int pass_score , int first_base ,int last_base ,
                    int min_length) throws Exception
     {
         ArrayList print_items = new ArrayList();
         boolean isGoodQualityTraceFile = false;
         File[] traceFiles = FileOperations.getSortArrayOfFiles( clone_directory + File.separator +PhredWrapper.CHROMAT_DIR_NAME );
         File[] phdFiles = FileOperations.getSortArrayOfFiles( clone_directory + File.separator +PhredWrapper.PHD_DIR_NAME );
         
         int trace_files_count = 0; int phd_files_count = 0;
         int trace_files_number = traceFiles.length; int phd_files_number = phdFiles.length;
         String trace_file_name_key = null; String phd_file_name_key = null;
         int compare_result = 0;
         if ( phdFiles == null || phdFiles.length==0)throw new Exception ("No .phd files exist for the clone");
         if ( traceFiles == null || traceFiles.length==0)throw new Exception ("No trace files exist for the clone");
         
         for (; trace_files_count < trace_files_number; )
         {
             phd_file_name_key = phdFiles[phd_files_count].getName();
             phd_file_name_key = phd_file_name_key.substring(0,phd_file_name_key.indexOf('.'));
             trace_file_name_key = traceFiles[trace_files_count].getName();
             trace_file_name_key = trace_file_name_key.substring(0,trace_file_name_key.indexOf('.'));
            
             compare_result = trace_file_name_key.compareToIgnoreCase(phd_file_name_key);
             if ( compare_result == 0)
             {
                 isGoodQualityTraceFile = isTraceFilePassQualityCheck( phdFiles[phd_files_count],  pass_score , first_base ,last_base ,min_length );
                  if ( isGoodQualityTraceFile )
                      print_items.add(cloneid + Constants.TAB_DELIMETER + traceFiles[trace_files_count].getAbsolutePath() + Constants.TAB_DELIMETER + "PASS");
                  else
                     print_items.add(cloneid + Constants.TAB_DELIMETER + traceFiles[trace_files_count].getAbsolutePath() + Constants.TAB_DELIMETER + "FAIL");
                     trace_files_count++;phd_files_count++;
             }
             else if ( compare_result < 0 )
             {
                 print_items.add(cloneid + Constants.TAB_DELIMETER + traceFiles[trace_files_count].getAbsolutePath()+ Constants.TAB_DELIMETER + "N/A");
                 trace_files_count++;
             }
             else if ( compare_result > 0 )
             {
                  phd_files_count++;
             }
           
          }
          return print_items;
     }
     
     
     private boolean            isTraceFilePassQualityCheck( File trace_file_phd, 
                    int pass_score , int first_base ,int last_base ,
                    int min_length )
     {
        BufferedReader input = null;
        boolean isInsideRead = false;
        String line = null; 
        boolean isBeforeDNAEnd = true; boolean isBeforeDNAStart = true;
        ArrayList elements = new ArrayList();
       PhredScoredElement element = null;
       
        try
        {
             input = new BufferedReader(new FileReader(trace_file_phd));
             while ((line = input.readLine()) != null)
            {
                if ( !isInsideRead && line.indexOf("BEGIN_DNA" ) != -1)
                {
                    isInsideRead = true; continue;
                }
                if ( line.indexOf("END_DNA" ) != -1 )
                    return  PhredScoredElement.isGoodQuality( elements,  first_base, 
                                                 last_base,  pass_score,  min_length);
                if ( isInsideRead )
                {
                   element = new PhredScoredElement(line) ;
                   if (element == null) throw new Exception();
                   elements.add(element);
                }

            }
        }
        catch(Exception e)
        {
            m_error_messages.add("Cannot process file "+ trace_file_phd.getAbsolutePath());
        }
        return false;
     }
        
     
           
     
     
     
     private String    constructQueryStringForTraceFilesQualityCheck(String sql_items)
     {
        
          switch ( m_items_type)
          {
              case Constants.ITEM_TYPE_PLATE_LABELS:
              {
                   return "select flexcloneid, flexsequenceid from flexinfo where isolatetrackingid in "
                   +" ( select isolatetrackingid from isolatetracking where sampleid in "
                   +" (select sampleid from sample where containerid in "
                   +" (select containerid from containerheader where label in (" + sql_items +")))) order by flexcloneid";
              }
              case Constants.ITEM_TYPE_CLONEID:
                  return " select flexcloneid, flexsequenceid from flexinfo where flexcloneid in (" + sql_items +") order by flexcloneid"; 
              default : return "";
          }
     }
     private Hashtable getCloneDirectories(String sql)
     {
        ResultSet rs = null;Hashtable read_directories = new Hashtable();
        String directory_root  = null;int cloneid = 0; int refseqid = 0;
        String directory = null;
       try
        {
            EndReadsWrapperRunner erw = new EndReadsWrapperRunner();
            directory_root= erw.getOuputBaseDir() + File.separator;
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while(rs.next())
            {
                refseqid = rs.getInt("flexsequenceid");
                cloneid = rs.getInt("flexcloneid");
                if ( cloneid < 1 || refseqid < 1) continue;
                directory = directory_root +refseqid + File.separator + cloneid;
                read_directories.put( String.valueOf(cloneid) , directory);
            }
            return read_directories;
        } catch (Exception sqlE)
        {
            m_error_messages.add("Error occured while exstracting inforamation for clones:\nSQL: "+sql);
            return read_directories;
        } finally
        {
            DatabaseTransactionLocal.closeResultSet(rs);
        }
     }
     //--------------------------------------------------------------
     private ArrayList createOrderListForInternalRepeats(String sql_items)throws Exception
     {
           ArrayList result = new ArrayList();
         return result;
     }
     
     /*
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
     */
     
     
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
        try
        {
            read_info = DatabaseTransaction.getInstance().requestConnection().prepareStatement(sql);
            scores_info = DatabaseTransaction.getInstance().requestConnection().prepareStatement(sql_scores);
           
            for (int count = 0; count < read_descriptions.size(); count++)
            {
                scores_bf = new StringBuffer();
                read_description = (ReadDescription)read_descriptions.get(count);
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
                     scores_info.setInt(1, read.getSequenceId());
                     rs_scores = scores_info.executeQuery();
                     while(rs_scores.next())
                     {
                        scores_bf.append( rs_scores.getString("infotext") );
                     }
                       
                    if ( isRepeatNeeded( read, Algorithms.getConvertStringToIntArray(scores_bf.toString()," ")) )
                        result.add(read_description.getPlateLabel()+"\t"+edu.harvard.med.hip.bec.sampletracking.objects.Container.convertPositionFrom_int_to_alphanumeric(read_description.getPosition())+"\tF" );
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
     
     
     
     
     
    public static ArrayList getReportForStretchCollection(String clone_id,
            StretchCollection stretch_collection,
            int number_of_bases_covered_by_forward_er ,
            int number_of_bases_covered_by_reverse_er,
            boolean isDescriptionOfFirstRNAOnly,
            boolean isCDSCoordinatesAlfaNummericNomenculature)throws Exception
    {
        Mutation discr = null;
        String item = null;
        ArrayList print_items = new ArrayList();
        StringBuffer buf = new StringBuffer();
       // System.out.println("A"+oligo_calculation.getSequenceId());
        print_items.add(Constants.LINE_SEPARATOR );
        print_items.add("Clone Id: "+ clone_id+Constants.LINE_SEPARATOR);
        int refseq_cds_length = RefSequence.getCdsLength(stretch_collection.getRefSequenceId());
        buf.append("RefSequence Id: "+stretch_collection.getRefSequenceId() );
        buf.append("\tRefSequence Cds Length: "+refseq_cds_length  + Constants.LINE_SEPARATOR);
        print_items.add(buf.toString());
        
        ArrayList stretchs = Stretch.sortByPosition(stretch_collection.getStretches() );
        Stretch stretch = null;
        CloneSequence clone_sequence = null;
        int status_is_forward_er_needed = 0;// 0 - not defined, 1 - yes, -1 no, -2 no - out of range
        int status_is_reverse_er_needed = 0;// 0 - not defined, 1 - yes, -1 no -2 no - out of range
        int status_is_internal_needed = 0;// 0 - not defined, 1 - yes, -1 no -2 no - out of range
      
        if ( stretch_collection.getType() == StretchCollection.TYPE_COLLECTION_OF_LQR )
        {
                clone_sequence = CloneSequence.getOneByCloneId(Integer.parseInt( clone_id) );
                print_items.add("Sequence Analysis status "+ BaseSequence.getSequenceAnalyzedStatusAsString(clone_sequence.getStatus())+Constants.LINE_SEPARATOR);
        }
        for (int index = 0; index < stretch_collection.getStretches().size();index ++)
        {
            stretch = (Stretch)stretch_collection.getStretches().get(index);

            if ( stretch_collection.getType() == StretchCollection.TYPE_COLLECTION_OF_LQR )
            {
                buf = new StringBuffer();
                buf.append( stretch.getStretchTypeAsString( stretch.getType()) + " "+ (index + 1) + "\t");
                if ( isCDSCoordinatesAlfaNummericNomenculature )
                {
                    buf.append( "Cds Start " + convertCDSCoordinatesAlfaNummericNomenculature( stretch.getCdsStart() , refseq_cds_length ) );
                    buf.append("\tCds Stop " + convertCDSCoordinatesAlfaNummericNomenculature( stretch.getCdsStop() , refseq_cds_length ) );
                }
                else
                {
                    buf.append( "Cds Start " + ( stretch.getCdsStart() ) );
                    buf.append("\tCds Stop " + ( stretch.getCdsStop() ) );
             
                }
                buf.append("\t Sequence Region "+stretch.getSequenceStart() +" - "+ stretch.getSequenceStop() );
                //define discrepancies in the region
                ArrayList lqr_discrepancies = clone_sequence.getDiscrepanciesInRegion( stretch.getSequenceStart() , stretch.getSequenceStop(), clone_sequence.getCdsStart() , refseq_cds_length);
                  if ( lqr_discrepancies == null || lqr_discrepancies.size() == 0 )
                  {
                      buf.append("\t No discrepancies found in the region ");
                      print_items.add( buf.toString() );
                  }
                  else
                  {
                       print_items.add( buf.toString() );
                       print_items.add(Constants.LINE_SEPARATOR+" Discrepancies found in the region ");
                       print_items.add(Constants.LINE_SEPARATOR+ Mutation.toString(lqr_discrepancies, isDescriptionOfFirstRNAOnly));
                  }
                
                if ( status_is_forward_er_needed == STATUS_NOT_DEFINED || status_is_forward_er_needed == STATUS_NO_READS_NEEDED )
                    status_is_forward_er_needed = isForwardReadRequered(stretch,lqr_discrepancies, number_of_bases_covered_by_forward_er);
                if ( status_is_internal_needed == STATUS_NOT_DEFINED || status_is_internal_needed == STATUS_NO_READS_NEEDED)
                    status_is_internal_needed = isInternalReadsRequered(stretch,lqr_discrepancies, refseq_cds_length, number_of_bases_covered_by_forward_er, number_of_bases_covered_by_reverse_er);
                if ( status_is_reverse_er_needed == STATUS_NOT_DEFINED || status_is_reverse_er_needed== STATUS_NO_READS_NEEDED)  
                    status_is_reverse_er_needed = isReverseReadRequered(stretch,lqr_discrepancies, refseq_cds_length,number_of_bases_covered_by_forward_er,number_of_bases_covered_by_reverse_er);

            }
            else
            {
                print_items.add( stretch.toString() );
            }
            print_items.add( Constants.LINE_SEPARATOR );
          
        }
        print_items.add( Constants.LINE_SEPARATOR );
        //define need for end / internal  read
        if ( stretch_collection.getType() == StretchCollection.TYPE_COLLECTION_OF_LQR )
        {
                StringBuffer requered_reads_description= new StringBuffer();
                requered_reads_description.append("\t\t\t");
                requered_reads_description.append(translateToString(status_is_forward_er_needed, "ForwardEndRead - ") );
                requered_reads_description.append("\t");
                requered_reads_description.append(translateToString(status_is_internal_needed, "InternalRead(s) - ") );
                requered_reads_description.append("\t");
                requered_reads_description.append(translateToString(status_is_reverse_er_needed, "ReverseEndRead - ") );
                print_items.add("Summary for Clone Id: "+ clone_id + "\t"+requered_reads_description.toString()+Constants.LINE_SEPARATOR);
        }
        return print_items;
    }
    
    /* format 
     * cloneid   cds_length   Analyzed    AssembledSeqId    DiskrId     DiskrType     DiscrPos   
     *  DiskrQuality   LQR_ID   LQR_Cds_start   LQR_Cds_Stop    
     *LQR_Sequence_start    LQR_Sequence_Stop   GapId   ContigId    Gap/Contig Cds Range
     **/
    public static ArrayList  getReportForStretchCollectionNewFormat(String clone_id,
            StretchCollection stretch_collection,
            boolean isDescriptionOfFirstRNAOnly,
            boolean isCDSCoordinatesAlfaNummericNomenculature)throws Exception
    {
        ArrayList print_items = new ArrayList();
        ReportItem report_item = null;
       
        StringBuffer buf = new StringBuffer();
        int clone_id_int = Integer.parseInt( clone_id);
        int refseq_cds_length = RefSequence.getCdsLength(stretch_collection.getRefSequenceId());
        int refseq_id = stretch_collection.getRefSequenceId() ;
        int analysis_status = -1;
        ArrayList stretchs = Stretch.sortByPosition(stretch_collection.getStretches() );
        Stretch stretch = null;
        CloneSequence clone_sequence = null;
        if ( stretch_collection.getType() == StretchCollection.TYPE_COLLECTION_OF_LQR )
        {
                clone_sequence = CloneSequence.getOneByCloneId(Integer.parseInt( clone_id) );
                clone_sequence.getStatus() ;
        }
        for (int index = 0; index < stretch_collection.getStretches().size();index ++)
        {
            stretch = (Stretch)stretch_collection.getStretches().get(index);

            if ( stretch_collection.getType() == StretchCollection.TYPE_COLLECTION_OF_LQR )
            {
                buf.append( stretch.getStretchTypeAsString( stretch.getType()) + " "+ (index + 1) + "\t");
                if ( isCDSCoordinatesAlfaNummericNomenculature )
                {
                    buf.append( "Cds Start " + convertCDSCoordinatesAlfaNummericNomenculature( stretch.getCdsStart() , refseq_cds_length ) );
                    buf.append("\tCds Stop " + convertCDSCoordinatesAlfaNummericNomenculature( stretch.getCdsStop() , refseq_cds_length ) );
                }
                else
                {
                    buf.append( "Cds Start " + ( stretch.getCdsStart() ) );
                    buf.append("\tCds Stop " + ( stretch.getCdsStop() ) );
             
                }
                buf.append("\t Sequence Region "+stretch.getSequenceStart() +" - "+ stretch.getSequenceStop() );
                //define discrepancies in the region
           //   ArrayList lqr_discrepancies = clone_sequence.getDiscrepanciesInRegion( stretch.getCdsStart() , stretch.getCdsStop(), clone_sequence.getCdsStart() , refseq_cds_length);
               ArrayList lqr_discrepancies = clone_sequence.getDiscrepanciesInRegion( stretch.getSequenceStart() , stretch.getSequenceStop(), clone_sequence.getCdsStart() , refseq_cds_length);
          
                if ( lqr_discrepancies != null || lqr_discrepancies.size() > 0 )
              {
// create new report item
                  print_items.addAll(
                        getReportItemsForDiscrepancies( lqr_discrepancies,
                        clone_id_int,  refseq_cds_length,  analysis_status,
                        clone_sequence.getId() ,  stretch )        );
              }
            }
        }
        return print_items;
    }
    
      
    
    //---------------------------------
    
    private static ArrayList  getReportItemsForDiscrepancies(ArrayList lqr_discrepancies,
        int clone_id, int refseq_cds_length, int analysis_status,
        int clone_sequence_id, Stretch stretch )
    {
        Mutation discr = null; DiscrepancyDescription discr_description = null;
        ReportItem report_item = null;
        ArrayList report_items = new ArrayList();
        for ( int d_count = 0; d_count < lqr_discrepancies.size(); d_count++)
          {
            discr_description = (DiscrepancyDescription) lqr_discrepancies.get(d_count);
            if ( discr_description.getDiscrepancyDefintionType() == DiscrepancyDescription.TYPE_AA)
            {
                discr = (Mutation) discr_description.getRNACollection().get(0); 
             }
            else if ( discr_description.getDiscrepancyDefintionType() != DiscrepancyDescription.TYPE_AA)
            {
                discr = (Mutation)discr_description.getRNADefinition();
             }
            report_item = new ReportItem();
            report_item.setCloneId  (clone_id );
            report_item.setRefSequenceCdsLength  ( refseq_cds_length );
            report_item.setAnalysisStatus  (analysis_status);
            report_item.setCloneSequenceId   ( clone_sequence_id );
            report_item.setDiscrId   ( discr.getId());
            report_item.setDiscrChangeType ( discr.getChangeType()); 
            report_item.setDiscrPosition  ( discr.getPosition() );
            report_item.setDiscrQuality  (discr.getQuality());
            report_item.setLqrId  ( stretch .getId() );
            report_item.setLqrCdsStart   ( stretch.getCdsStart());
            report_item.setLqrCdsStop  (stretch.getCdsStop());
        //    report_item.setLqrSequenceStart  ( stretch.g);
       //     report_item.setLqrSequenceStop  ();
            report_items.add(report_item);
          }
          return report_items;
    }
      // translates status of need to different reads into readable foramt
    private static String translateToString(int status, String title) 
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
    private static int  isForwardReadRequered(Stretch stretch, ArrayList lqr_discrepancies,
                int number_of_bases_covered_by_forward_er )
    {
        if ( stretch.getCdsStart() >= number_of_bases_covered_by_forward_er)
                return STATUS_NO_READS_NEEDED_OUT_OF_RANGE_FOR_DEFINITION;
        else if (stretch.getCdsStart() <= number_of_bases_covered_by_forward_er
            && ( lqr_discrepancies == null || lqr_discrepancies.size() == 0) )
                return STATUS_NO_READS_NEEDED;
        else if (stretch.getCdsStart() <= number_of_bases_covered_by_forward_er
            && ( lqr_discrepancies != null || lqr_discrepancies.size() > 0) )
                return STATUS_YES_READS_NEEDED;
        else 
            return STATUS_NOT_DEFINED;
    }
    
    private static int   isInternalReadsRequered(Stretch stretch, ArrayList lqr_discrepancies,
            int refseq_cds_length,
            int number_of_bases_covered_by_forward_er,
            int number_of_bases_covered_by_reverse_er)
    {
        if ( refseq_cds_length < number_of_bases_covered_by_forward_er 
          || refseq_cds_length < number_of_bases_covered_by_reverse_er 
            || !( stretch.getCdsStart() >  number_of_bases_covered_by_forward_er
                && stretch.getCdsStart() < refseq_cds_length - number_of_bases_covered_by_reverse_er))
                return STATUS_NO_READS_NEEDED_OUT_OF_RANGE_FOR_DEFINITION;
        
        else if ( stretch.getCdsStart() >  number_of_bases_covered_by_forward_er
                && stretch.getCdsStart() < refseq_cds_length - number_of_bases_covered_by_reverse_er)
        {
            if (  lqr_discrepancies == null || lqr_discrepancies.size() == 0) 
                return STATUS_NO_READS_NEEDED;
            if ( lqr_discrepancies != null || lqr_discrepancies.size() > 0) 
                return STATUS_YES_READS_NEEDED;
         }
        return STATUS_NOT_DEFINED;
    }
     
    private static int   isReverseReadRequered(Stretch stretch, ArrayList lqr_discrepancies,
                                    int refseq_cds_length,
                                    int number_of_bases_covered_by_forward_er,
                                    int number_of_bases_covered_by_reverse_er)
    {
         if ( refseq_cds_length <= number_of_bases_covered_by_forward_er 
            || stretch.getCdsStart() <= refseq_cds_length - number_of_bases_covered_by_reverse_er)
                return STATUS_NO_READS_NEEDED_OUT_OF_RANGE_FOR_DEFINITION;
        else if (stretch.getCdsStart() >= refseq_cds_length - number_of_bases_covered_by_reverse_er
            && ( lqr_discrepancies == null || lqr_discrepancies.size() == 0) )
                return STATUS_NO_READS_NEEDED;
        else if (stretch.getCdsStart() >= refseq_cds_length - number_of_bases_covered_by_reverse_er
            && ( lqr_discrepancies != null || lqr_discrepancies.size() > 0) )
                return STATUS_YES_READS_NEEDED;
        else 
            return STATUS_NOT_DEFINED;
    }

    
     private static String convertCDSCoordinatesAlfaNummericNomenculature( int position , int refseq_cds_length )
     {
         if (position < 0) return "L5_-"+position;
         if ( position > 0 && position <= refseq_cds_length ) return ""+position;
         if (position > refseq_cds_length) return "L3_"+(position-refseq_cds_length)+"-CDS";
         return "";
     }
   
           

     //------------------------------------------------ 
     
    public static void main(String args[]) 
     
    {   try
         {
              BecProperties sysProps =  BecProperties.getInstance( BecProperties.PATH);
        sysProps.verifyApplicationSettings();
      
          ProcessRunner runner = new SpecialReportsRunner();
            runner.setUser( AccessManager.getInstance().getUser("htaycher123","htaycher"));
            ((SpecialReportsRunner)runner).setReportType(Constants.PROCESS_CREATE_REPORT_TRACEFILES_QUALITY);
            runner.setInputData(Constants.ITEM_TYPE_PLATE_LABELS," IGS002109-1  ");
          runner.run();
             
         }catch(Exception e){}
         System.exit(0);
     }
    
   
}
