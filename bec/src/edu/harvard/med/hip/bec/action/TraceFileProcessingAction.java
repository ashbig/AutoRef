/*
 * TraceFileProcessingActio.java
 *
 * Created on February 24, 2004, 2:20 PM
 */

package edu.harvard.med.hip.bec.action;

/**
 *
 * @author  HTaycher
 */

    
   import java.util.*;

import java.sql.*;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;
import org.apache.struts.upload.*;


import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.coreobjects.oligo.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.form.*;
import edu.harvard.med.hip.bec.user.*;
import edu.harvard.med.hip.bec.action_runners.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.Constants;
import edu.harvard.med.hip.bec.sampletracking.objects.*;

public class TraceFileProcessingAction extends ResearcherAction
{
    
    
    public ActionForward becPerform(ActionMapping mapping,    ActionForm form,
                HttpServletRequest request,    HttpServletResponse response)
                throws ServletException, IOException
    {
        ActionErrors errors = new ActionErrors();
        
        int forwardName = ((SubmitDataFileForm)form).getForwardName();
        Thread t = null;
        try
        {
            User user = (User)request.getSession().getAttribute(Constants.USER_KEY);
            TraceFileProcessingRunner runner = new TraceFileProcessingRunner();
            runner.setProcessType(forwardName);
            switch (forwardName)
            {
                case Constants.PROCESS_CREATE_FILE_FOR_TRACEFILES_TRANSFER:
                {
                     String items = (String)request.getParameter("items");
                    request.setAttribute(Constants.JSP_TITLE,"processing Request for Plates Upload");
                    request.setAttribute(Constants.ADDITIONAL_JSP,"Processing plates:\n"+items.toUpperCase().trim() +". Files will be send to you by e-mail.");
                    runner.setReadDirection((String)request.getParameter("read_direction"));
                    runner.setReadType((String)request.getParameter("read_type"));
                    //runner.setItems(items.toUpperCase().trim() );
                   // runner.setItemsType(Constants.ITEM_TYPE_PLATE_LABELS);
                    runner.setInputData(Constants.ITEM_TYPE_PLATE_LABELS,items.toUpperCase().trim() );
                    break;
                }
                case Constants.PROCESS_INITIATE_TRACEFILES_TRANSFER:
                {
                    request.setAttribute(Constants.JSP_TITLE,"processing Request for Trace Files Transfer");
                    request.setAttribute(Constants.ADDITIONAL_JSP,"Report will be send to you by e-mail.");
             
                    String inputdir = (String)request.getParameter("inputdir");
                    String outputdir = (String)request.getParameter("outputdir");
                    String delete = (String)request.getParameter("delete");
                     runner.setInputDirectory(inputdir);
                    runner.setOutputDirectory(outputdir);
                    runner.setDelete(delete);
                    FormFile requestFile = ((SubmitDataFileForm)form).getFileName();
                    InputStream input = null;
                    try
                    {
                        input = requestFile.getInputStream();
                        runner.setRenamingFile(input);
                        
                    }
                    catch (Exception ex)
                    {
                        errors.add("fileName", new ActionError("bec.infoimport.file", ex.getMessage()));
                        saveErrors(request,errors);
                        return new ActionForward(mapping.getInput());
                    } 
                    break;
                }
                case Constants.PROCESS_CREATE_RENAMING_FILE_FOR_TRACEFILES_TRANSFER:
                {
                    request.setAttribute(Constants.JSP_TITLE,"processing Request for Creating Renaming File for Trace Files");
                    request.setAttribute(Constants.ADDITIONAL_JSP,"Application will put renaming file into input directory. Report will be send to you by e-mail.");
                    String inputdir = (String)request.getParameter("inputdir");
                    runner.setInputDirectory(inputdir);
                    runner.setReadType((String)request.getParameter("read_type"));
                    runner.setSequencingFacility(Integer.parseInt((String)request.getParameter("sequencing_facility")));
                    FormFile requestFile = ((SubmitDataFileForm)form).getFileName();
                    InputStream input = null;
                    try
                    {
                        input = requestFile.getInputStream();
                        runner.setRenamingFile(input);
                    }
                    catch (Exception ex)
                    {
                        errors.add("fileName", new ActionError("bec.infoimport.file", ex.getMessage()));
                        saveErrors(request,errors);
                        return new ActionForward(mapping.getInput());
                    } 
                    break;
                }
            }
                runner.setUser(user);
                t = new Thread(runner);                   
                t.start();
          return (mapping.findForward("processing"));
           
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            request.setAttribute(Action.EXCEPTION_KEY, e);
            return (mapping.findForward("error"));
        }
        
    }




    
}
