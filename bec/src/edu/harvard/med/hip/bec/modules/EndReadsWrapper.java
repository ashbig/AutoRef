    /*
 * EndReadsWrapper.java
 *
 * Created on April 18, 2003, 2:37 PM
 */

package edu.harvard.med.hip.bec.modules;

import edu.harvard.med.hip.bec.programs.phred.*;

import java.util.*;
import java.io.*;
import sun.jdbc.rowset.*;
import java.sql.*;
import org.apache.regexp.*;

import edu.harvard.med.hip.bec.engine.*;
import edu.harvard.med.hip.bec.database.*;
import  edu.harvard.med.hip.bec.bioutil.*;
import  edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.sampletracking.mapping.*;
import edu.harvard.med.hip.bec.coreobjects.endreads.*;
import edu.harvard.med.hip.bec.sampletracking.objects.*;

/**
 *
 * @author  htaycher
 */
public class EndReadsWrapper
{
     // outputBaseDir specify the base directory for trace file distribution
    private static final String OUTPUT_BASE_DR = "";
    //inputTraceDir specify the directory where the trace files get dumped from sequencer
    private static final String SEQUENCHER_BASE_DR = "";
    //errorDir specify where the error log is stored for trace files failed Phred run
    private static final String ERROR_BASE_DR = "";
    //errorDir specify where the error log is stored for trace files failed Phred run
    private static final String ERROR_BASE_WRONG_FORMAT_FILES = "";
    
    
    private String      m_outputBaseDir = null;
    private String      m_outputBaseDir_wrongformatfiles = null;
    private String      m_inputTraceDir = null;
    private String      m_errorDir = null;
    private String      m_empty_samples_directory = null;
    private ArrayList   m_error_messages = null;
    /** Creates a new instance of EndReadsWrapper */
    public EndReadsWrapper()
    {
         m_outputBaseDir =  OUTPUT_BASE_DR;
        m_inputTraceDir =  SEQUENCHER_BASE_DR;
        m_errorDir =  ERROR_BASE_DR;
    }
    
    public EndReadsWrapper(String outputBaseDir, String inputTraceDir, String errorDir, 
    String outputBaseDir_wrongformatfiles, String empty_samples_directory)
    {
        m_outputBaseDir =  outputBaseDir;
        m_inputTraceDir =  inputTraceDir;
        m_errorDir =  errorDir;
        m_outputBaseDir_wrongformatfiles = outputBaseDir_wrongformatfiles;
        m_empty_samples_directory = empty_samples_directory;
    }
    
    public ArrayList getErrorMessages(){ return m_error_messages;}
    
    /* outputBaseDir specify the base directory for trace file distribution
     * inputTraceDir specify the directory where the trace files get dumped from sequencer
     * errorDir specify where the error log is stored for trace files failed Phred run
     */
    
    public ArrayList run(Connection conn)throws BecDatabaseException
    {
        ArrayList reads = new ArrayList();
        m_error_messages = new ArrayList();
          //process only end reads that are exspected
        ArrayList expected_chromat_file_names = getExspectedChromatFileNames(conn);
        
        if (expected_chromat_file_names.size() == 0)
            return null;
        //distribute chromat files 
        TraceFilesDistributor tfb = new TraceFilesDistributor();
        tfb.setNameOfFilesToDistibute(expected_chromat_file_names);
        tfb.setIsInnerReads(false);
        ArrayList chromat_files_names = tfb.distributeChromatFiles(m_inputTraceDir, m_outputBaseDir,m_outputBaseDir_wrongformatfiles, m_empty_samples_directory);
        m_error_messages = tfb.getErrorMesages();
    
        return runPhredandParseOutput( chromat_files_names,  m_error_messages);
        //run phred and parse output
    }
    
    
    private ArrayList getExspectedChromatFileNames(Connection conn)throws BecDatabaseException
    {
        ArrayList res = new ArrayList();
        String sql = "select  FLEXSEQUENCINGPLATEID as plateid ,FLEXSEQUENCEID as sequenceid "
         +",FLEXCLONEID as cloneid,position,resulttype as orientation"
        +" from flexinfo f, isolatetracking iso, result r, sample s "
        +" where f.ISOLATETRACKINGID =iso.ISOLATETRACKINGID  and r.sampleid =s.sampleid"
        +" and iso.sampleid=s.sampleid and iso.sampleid in"
        +" (select sampleid from  result where resultvalueid is null and resulttype in (12, 13))";
        
        ResultSet rs = null;NamingFileEntry entry = null;
        String orientation_str = "";
        try
        {
           // DatabaseTransactionLocal t = DatabaseTransactionLocal.getInstance();
            rs = DatabaseTransaction.executeQuery(sql, conn);
            
            while(rs.next())
            {
                int clone_id = rs.getInt("cloneid");
                int plate_id = rs.getInt("plateid");
                int position = rs.getInt("position");
                int sequence_id = rs.getInt("sequenceid");
                int orientation = rs.getInt("orientation") ;
                if (orientation == Result.RESULT_TYPE_ENDREAD_REVERSE)
                    orientation_str="R";
                else if ( orientation == Result.RESULT_TYPE_ENDREAD_FORWARD)
                    orientation_str="F";
                else
                    continue;
                entry =new  NamingFileEntry(clone_id  , orientation_str,
                                plate_id,    Algorithms.convertWellFromInttoA8_12( position), 
                               sequence_id,   0);
               // System.out.println(entry.toString());
                res.add( entry.toString() );
            }
            return res;
        } catch (Exception sqlE)
        {
            throw new BecDatabaseException("Error occured while getting fil names: "+"\n"+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransactionLocal.closeResultSet(rs);
        }
    }
    
    private ArrayList runPhredandParseOutput(ArrayList file_names, ArrayList error_messages)
    {
        ArrayList reads = new ArrayList();
       
        PhredWrapper prwrapper = new PhredWrapper();
        prwrapper.setTrimType(PhredWrapper.TRIMMING_TYPE_PHRED_ALT);
        Read read = null;
        String traceFile_name ;
      
        for (int count = 0; count < file_names.size(); count++)
        {
            traceFile_name = (String) file_names.get(count);
            try
            {
                //create file structure and distribute trace file into chromat_dir
                read = prwrapper.run(new File(traceFile_name) );
               if (read != null)reads.add(read);
            }
            catch(Exception e)
            {
                e.printStackTrace();
                error_messages.add("Error occurred while running phred on " + traceFile_name);
            }
            
        } // for
        return reads;
        
    }//processPipeline
    
    public static void main(String args[])
    {
        String baseDir = "C:\\bio\\phred\\try";
        String traceDir = "C:\\bio\\phred\\out";
        String errorDir = "C:\\bio\\phred\\err";
        String dr ="c:\\bio\\phred\\err";
         String dr_empty ="c:\\bio\\phred\\empty";
      //  PipelineDriver task = new PipelineDriver();
        //task.processPipeline(baseDir,traceDir,errorDir);
        try{
            Connection  conn = DatabaseTransaction.getInstance().requestConnection();
            EndReadsWrapper ew = new EndReadsWrapper(traceDir,baseDir,errorDir, dr,dr_empty);
            ArrayList reads = ew.run(conn);
            System.out.println("Total read object: "+reads.size());
        
              Read read = null; int[] istr_info = new int[2];int resultid =-1;
              for (int count = 0; count < reads.size(); count++)
              {
                  read = (Read) reads.get(count);
                  istr_info = IsolateTrackingEngine.findIdandStatusFromFlexInfo(read.getFLEXPlate(), read.getFLEXWellid());
                  read.setIsolateTrackingId( istr_info[0]);
                  //get reasult id
                  if ( read.getType() == Read.TYPE_ENDREAD_REVERSE || read.getType() == Read.TYPE_ENDREAD_REVERSE_FAIL)
                  {
                      resultid = read.findResultIdFromFlexInfo(Result.RESULT_TYPE_ENDREAD_REVERSE);
                  }
                  if ( read.getType() == Read.TYPE_ENDREAD_FORWARD || read.getType() == Read.TYPE_ENDREAD_FORWARD_FAIL)
                  {
                      resultid = read.findResultIdFromFlexInfo(Result.RESULT_TYPE_ENDREAD_FORWARD);
                  }
                  //insert read data
                  if ( read.getType() == Read.TYPE_ENDREAD_FORWARD || read.getType() == Read.TYPE_ENDREAD_REVERSE)
                  {
                      read.setResultId(resultid);
                      read.insert(conn);
                      Result.updateResultValueId( resultid,read.getId(), conn);
                  }

                  Result.updateType( resultid,read.getType(), conn);
                  if (istr_info[1] == IsolateTrackingEngine.PROCESS_STATUS_ER_INITIATED)
                  {
                    IsolateTrackingEngine.updateStatus(IsolateTrackingEngine.PROCESS_STATUS_ER_PHRED_RUN, istr_info[0],  conn );
                  }
                  conn.commit();
                  String org_trace_file_name = read.getTraceFileName().substring( read.getTraceFileName().lastIndexOf(File.separator));
                  File org_trace_file = new File(baseDir+org_trace_file_name);
                  org_trace_file.delete();
             }
             
        }
        catch(Exception e){}
        System.exit(0);
    } // main
}
