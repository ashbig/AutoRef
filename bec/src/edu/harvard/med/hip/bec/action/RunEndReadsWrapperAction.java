/*
 * RunEndReadsWrapper.java
 *
 * Created on April 7, 2003, 1:53 PM
 */

package edu.harvard.med.hip.bec.action;

import java.sql.*;
import java.io.*;
import java.util.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.coreobjects.oligo.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.form.*;
import edu.harvard.med.hip.bec.user.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.Constants;
import edu.harvard.med.hip.bec.sampletracking.mapping.*;
import edu.harvard.med.hip.bec.sampletracking.objects.*;
import edu.harvard.med.hip.bec.modules.*;
import edu.harvard.med.hip.bec.coreobjects.endreads.*;

/**
 *
 * @author  htaycher
 */
public class RunEndReadsWrapperAction extends ResearcherAction
{
    
    
    public ActionForward becPerform(ActionMapping mapping,
                        ActionForm form,
                        HttpServletRequest request,
                        HttpServletResponse response)
                        throws ServletException, IOException
    {
        // place to store errors
        ActionErrors errors = new ActionErrors();
        
        User user = (User)request.getSession().getAttribute(Constants.USER_KEY);
        String      traceFilesDir = "/tmp/";
         String     inputTraceDir = "/tmp/";
         String      errorDir = "/tmp/";
         String      outputBaseDir_WrongFormatFiles = "/tmp/";
         String      empty_samples_directory = "/tmp/";
         
        ActionRunner runner = new ActionRunner();
     //   runner.setContainerIds(master_container_ids );
       
        runner.setUser(user);
        runner.setOriginalTraceFilesDir (traceFilesDir);
        runner.setTargetTraceFilesDir (inputTraceDir);
        runner.setErrorMessagesDir (errorDir);
        runner.setWrongFormatFilesDir (outputBaseDir_WrongFormatFiles);
        runner.setEmptySamplesDir(empty_samples_directory);
        Thread t = new Thread(runner);
        t.start();
        return mapping.findForward("proccessing");
        
        
    }
    
    class ActionRunner implements Runnable
    {
        private ArrayList   i_master_container_ids = null;//get from form
        private User        i_user = null;
        private String      i_traceFilesDir = null;
        private String      i_inputTraceDir = null;
        private String      i_errorDir = null;
        private String      i_outputBaseDir_WrongFormatFiles = null;
        private String      i_empty_samples_directory = null;
        
        private ArrayList   i_error_messages = null;
        
        public ActionRunner()
        {
            i_error_messages = new ArrayList();
        }
        public void      setOriginalTraceFilesDir (String s){ i_traceFilesDir = s;}
        public void      setTargetTraceFilesDir (String s){ i_inputTraceDir = s;}
        public void      setErrorMessagesDir (String s){ i_errorDir = s;}
        public void      setWrongFormatFilesDir (String s){ i_outputBaseDir_WrongFormatFiles = s;}
        public void      setEmptySamplesDir (String s){ i_empty_samples_directory = s;}

        public void         setContainerIds(ArrayList v)        { i_master_container_ids = v;}
        public  void        setUser(User v){i_user=v;}
        
        public void run()
        {
            Connection conn = null;
             int resultid = -1;
            try
            {
                  EndReadsWrapper ew = new EndReadsWrapper(i_traceFilesDir,i_inputTraceDir,i_errorDir, i_outputBaseDir_WrongFormatFiles,i_empty_samples_directory);
                  ArrayList reads = ew.run();
                  // insert reads into db
                  Read read = null; 
                  for (int count = 0; count < reads.size(); count++)
                  {
                      read = (Read) reads.get(count);
                      if ( read.getType() == Read.TYPE_ENDREAD_REVERSE )
                      {
                            resultid = read.findResult(Result.RESULT_TYPE_ENDREAD_REVERSE);
                          read.insert(conn);
                          Result.updateResultValueId( resultid,read.getId(), conn);
                      }
                      if ( read.getType() == Read.TYPE_ENDREAD_FORWARD)
                      {
                          resultid = read.findResult(Result.RESULT_TYPE_ENDREAD_FORWARD);
                          read.insert(conn);
                          Result.updateResultValueId( resultid,read.getId(), conn);
                      }
                      Result.updateType( resultid,read.getType(), conn);
                      conn.commit();
                  }
                  
            }
            catch(Exception ex)
            {
                i_error_messages.add(ex.getMessage());
                DatabaseTransaction.rollback(conn);
            }
            finally
            {
                DatabaseTransaction.closeConnection(conn);
            }
        }
    }
}
