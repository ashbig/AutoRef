/*
 * tester_run_assembler.java
 *
 * Created on July 8, 2003, 2:03 PM
 */

package edu.harvard.med.hip.bec.action;

import java.sql.*;
import java.io.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.coreobjects.endreads.*;
import edu.harvard.med.hip.bec.coreobjects.oligo.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.form.*;
import edu.harvard.med.hip.bec.user.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.Constants;
import edu.harvard.med.hip.bec.sampletracking.mapping.*;
import edu.harvard.med.hip.bec.sampletracking.objects.*;
  import java.util.*;
/**
 *
 * @author  htaycher
 */
public class tester_run_assembler
{
    
    /** Creates a new instance of tester */
    public tester_run_assembler()
    {
         i_error_messages = new ArrayList();
    }
    
         // outputBaseDir specify the base directory for trace file distribution
        private static final String INPUT_BASE_DR = "";
    
        private ArrayList   i_master_container_ids = null;//get from form
        private String      i_phrap_params_file = null;
        private String      i_traceFilesBaseDir = null;
        private ArrayList   i_error_messages = null;
        private String      i_result_type = null;
        private User        i_user = null;
        
        
        
        public  void        setUser(User v){i_user=v;}
        public void         setResultType(String v){ i_result_type=v;}
        
        public void run()
        {
      // The database connection used for the transaction
            Connection conn = null;
            ArrayList files = new ArrayList();
            ArrayList sequences = new ArrayList();
            i_error_messages = new ArrayList();
            try
            {
                // conncection to use for transactions
                conn = DatabaseTransaction.getInstance().requestConnection();
      
          //process only sequences  that are exspected
                ArrayList expected_sequence_definition = getExspectedSequenceDescriptions(conn, i_result_type);
        
                if (expected_sequence_definition.size() == 0)
                    return ;
        //run assembler and return sequences
                sequences = runAssemblerandParseOutput( expected_sequence_definition,  i_error_messages, conn);
        // put sequences into db
                insertSequences(sequences,  i_error_messages,  conn);
            }
            
            catch(Exception ex)
            {
                i_error_messages.add(ex.getMessage());
                //send notification to the user
                
                DatabaseTransaction.rollback(conn);
            }
            finally
            {
                try
                {
         //send errors
                    if (i_error_messages.size()>0)
                    {
                         Mailer.sendMessage(i_user.getUserEmail(), "elena_taycher@hms.harvard.edu",
                        "elena_taycher@hms.harvard.edu", "Request for sequence assemblyu : error messages.", "Errors\n " ,i_error_messages);
                
                    }
                }
                catch(Exception e){}
                DatabaseTransaction.closeConnection(conn);
            }
            
        }
        
     public ArrayList getErrorMessages(){ return i_error_messages;}
     
     
     
     
     //-------------------------------------------------------
      private ArrayList getExspectedSequenceDescriptions(Connection conn, String result_type)throws BecDatabaseException
    {
        ArrayList res = new ArrayList();
        String sql = "select  FLEXSEQUENCEID ,FLEXCLONEID , r.resultid as resultid, refsequenceid, iso.isolatetrackingid as isolatetrackingid "
        +" from flexinfo f, isolatetracking iso, result r, sample s, sequencingconstruct  constr "
        +" where constr.constructid = iso.constructid and f.ISOLATETRACKINGID =iso.ISOLATETRACKINGID  and r.sampleid =s.sampleid"
        +" and iso.sampleid=s.sampleid and iso.sampleid in"
        +" (select sampleid from  result where resultvalueid is null and resulttype in ("+
        result_type +"))";
        
        ResultSet rs = null;
      SequenceDescription seq_desc = null;
        try
        {
           // DatabaseTransactionLocal t = DatabaseTransactionLocal.getInstance();
            rs = DatabaseTransaction.executeQuery(sql, conn);
            
            while(rs.next())
            {
                seq_desc = new SequenceDescription(rs.getInt("FLEXSEQUENCEID"),
                                 rs.getInt("FLEXCLONEID"), rs.getInt("resultid")
                                ,rs.getInt("refsequenceID"), rs.getInt("isolatetrackingid"));
               // System.out.println(entry.toString());
                res.add( seq_desc );
            }
            return res;
        } catch (Exception sqlE)
        {
            throw new BecDatabaseException("Error occured while getting inforamation for sequences that can be assembled from end reads:\nSQL: "+sql);
        } finally
        {
            DatabaseTransactionLocal.closeResultSet(rs);
        }
    }
     
     
     private   ArrayList runAssemblerandParseOutput(
                         ArrayList expected_sequence_definition, 
                         ArrayList m_error_messages, 
                         Connection conn)throws BecUtilException
     {
         ArrayList res = new ArrayList();
         
         return res;
     }
     
     
     
     private   void insertSequences(
                         ArrayList sequence_definition, 
                         ArrayList m_error_messages, 
                         Connection conn)throws BecUtilException
     {
     }
     
     protected class SequenceDescription
     {
         private int        i_flex_sequenceid = -1;
         private int        i_flex_cloneid = -1;
         private int        i_resultid = -1;
         private int        i_becsequenceid = -1;
         private int        i_isolatetrackingid = -1;
         
         public SequenceDescription(){}
         public SequenceDescription(int v1, int v2, int v3, int v4, int v5)
         {
             i_flex_sequenceid = v1;
             i_flex_cloneid = v2;
             i_resultid = v3;
              i_becsequenceid = v4;
             i_isolatetrackingid = v5;
         }
         
         public int        getFlexSequenceId (){ return i_flex_sequenceid   ;}
         public int        getFlexCloneId (){ return i_flex_cloneid   ;}
         public int        getResultId (){ return i_resultid   ;}
         public int        getBecSequenceId (){ return i_becsequenceid   ;}
         public int        getIsolateTrackingId (){ return i_isolatetrackingid   ;}
         
         public void        setFlexSequenceId  ( int v){  i_flex_sequenceid =v  ;}
         public void        setFlexCloneId ( int v){  i_flex_cloneid   =v;}
         public void        setResultId ( int v){  i_resultid =v  ;}
          public void        setBecSequenceId (int v){  i_becsequenceid =v  ;}
         public void          setIsolateTrackingId (int v){  i_isolatetrackingid =v  ;}
     }
     
     
   //***************************************  
    
      public static void main(String args[]) 
     
    {
       
        tester_run_assembler runner = new tester_run_assembler();
        
        User user  = null;
        try
        {
            user = AccessManager.getInstance().getUser("htaycher","htaycher");
        }
        catch(Exception e){}
        runner.setUser(user);
        runner.setResultType( String.valueOf(Result.RESULT_TYPE_ASSEMBLED_FROM_END_READS));
        runner.run();
        System.exit(0);
     }
    
}
