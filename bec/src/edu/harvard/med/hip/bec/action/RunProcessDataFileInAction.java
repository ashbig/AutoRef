/*
 * RunProcessDataFileInAction.java
 *
 * Created on September 19, 2003, 10:44 AM
 */

package edu.harvard.med.hip.bec.action;
import java.util.*;

import java.sql.*;
import java.io.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;
import org.apache.struts.upload.*;

import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.modules.*;
import edu.harvard.med.hip.bec.action_runners.*;
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.coreobjects.endreads.*;
import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.coreobjects.oligo.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.form.*;
import edu.harvard.med.hip.bec.user.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.Constants;
import edu.harvard.med.hip.bec.sampletracking.mapping.*;
import edu.harvard.med.hip.bec.sampletracking.objects.*;

/**
 *
 * @author  HTaycher
 */
   

public class RunProcessDataFileInAction extends ResearcherAction
{
    
    
    public ActionForward becPerform(ActionMapping mapping,
                                    ActionForm form,
                                    HttpServletRequest request,
                                    HttpServletResponse response)
                                    throws ServletException, IOException
    {
        // place to store errors
        ActionErrors errors = new ActionErrors();
        Thread t = null;
         int forwardName = ((Seq_GetSpecForm)form).getForwardName();
         Connection conn = null;
         
        FormFile requestFile = ((SubmitDataFileForm)form).getFileName();
     
        //check if can get input stream
        InputStream input = null;
         try
        {
            input = requestFile.getInputStream();
        } catch (FileNotFoundException ex)
        {
            errors.add("fileName", new ActionError("bec.infoimport.file", ex.getMessage()));
            saveErrors(request,errors);
            return new ActionForward(mapping.getInput());
        } catch (IOException ex)
        {
            errors.add("fileName", new ActionError("bec.infoimport.file", ex.getMessage()));
            saveErrors(request,errors);
            return new ActionForward(mapping.getInput());
        }
       
        try
        {
            
            User user = (User)request.getSession().getAttribute(Constants.USER_KEY);
            DatabaseTransaction dt = DatabaseTransaction.getInstance();
            conn = dt.requestConnection();
        
    
            switch (forwardName)
            {
                case Constants.PROCESS_ADD_NEW_INTERNAL_PRIMER : // add new internal primer
                {
                }

                case Constants.PROCESS_VIEW_INTERNAL_PRIMERS : //view internal primers
                {
                }

                case Constants.PROCESS_APPROVE_INTERNAL_PRIMERS : //approve internal primers
                {
                }

                case Constants.PROCESS_RUN_PRIMER3: //run primer3
                {
                }

                case Constants.PROCESS_RUNPOLYMORPHISM_FINDER: //run polymorphism finder
                {
                }

                case Constants.PROCESS_RUN_DISCREPANCY_FINDER: //run discrepancy finder
                {
                }

                case Constants.PROCESS_SUBMIT_ASSEMBLED_SEQUENCE : 
                {
                }
           }
       
            return mapping.findForward("processing");
        }
        catch (Exception e)
        {
             try 
             {
                conn.rollback();
                System.out.println(e.getMessage());
                request.setAttribute(Action.EXCEPTION_KEY, e);
                return (mapping.findForward("error"));
             } catch (Exception e1) {           
                request.setAttribute(Action.EXCEPTION_KEY, e);
                return (mapping.findForward("error"));
            } 
        }
        finally
        {
            if(conn != null)
                DatabaseTransaction.closeConnection(conn);
        }
    }
}

    

