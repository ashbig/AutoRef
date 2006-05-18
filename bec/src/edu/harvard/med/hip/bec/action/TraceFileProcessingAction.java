//Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->
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

public class TraceFileProcessingAction extends BecAction
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
             System.out.println("user "+user); 
     
            ProcessRunner runner = new TraceFileProcessingRunner();
            runner.setProcessType(forwardName);
            request.setAttribute(Constants.JSP_CURRENT_LOCATION,getPageLocation(forwardName));
               request.setAttribute(Constants.JSP_TITLE,getPageTitle(forwardName));
                
            switch (forwardName)
            {
                case Constants.PROCESS_CREATE_FILE_FOR_TRACEFILES_TRANSFER:
                {
                     String items = (String)request.getParameter("items");
                   //     request.setAttribute(Constants.ADDITIONAL_JSP,"Processing plates:\n"+items.toUpperCase().trim() +". Files will be send to you by e-mail.");
                 
                    request.setAttribute(Constants.ADDITIONAL_JSP,"Processing plates:\n"+items.trim() +". Files will be send to you by e-mail.");
                    ((TraceFileProcessingRunner)runner).setReadDirection((String)request.getParameter("read_direction"));
                    ((TraceFileProcessingRunner)runner).setReadType(Integer.parseInt((String)request.getParameter("read_type")));
                    runner.setInputData(Constants.ITEM_TYPE_PLATE_LABELS,items.toUpperCase().trim() );
                  
                    break;
                }
                case Constants.PROCESS_INITIATE_TRACEFILES_TRANSFER:
                {
                    request.setAttribute(Constants.ADDITIONAL_JSP,"Report will be send to you by e-mail.");
             
                    String inputdir = (String)request.getParameter("inputdir");
                    String outputdir = (String)request.getParameter("outputdir");
                    String delete = (String)request.getParameter("delete");
                    ((TraceFileProcessingRunner) runner).setInputDirectory(inputdir);
                    ((TraceFileProcessingRunner) runner).setOutputDirectory(outputdir);
                    ((TraceFileProcessingRunner) runner).setDelete(delete);
                    FormFile requestFile = ((SubmitDataFileForm)form).getFileName();
                    InputStream input = null;
                    try
                    {
                        input = requestFile.getInputStream();
                        ((TraceFileProcessingRunner) runner).setRenamingFile(input);
                        
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
                    request.setAttribute(Constants.ADDITIONAL_JSP,"Application will put renaming file into input directory. Report will be send to you by e-mail.");
                    String inputdir = (String)request.getParameter("inputdir");
                    ((TraceFileProcessingRunner) runner).setInputDirectory(inputdir);
                    ((TraceFileProcessingRunner) runner).setReadType(Integer.parseInt((String)request.getParameter("read_type")));
                    String temp = (String)request.getParameter("sequencing_facility");
                    ((TraceFileProcessingRunner) runner).setFormatName( temp);
    
                    //runner.setSequencingFacility(Integer.parseInt(temp);
                    FormFile requestFile = ((SubmitDataFileForm)form).getFileName();
                    InputStream input = null;
                    try
                    {
                        input = requestFile.getInputStream();
                        ((TraceFileProcessingRunner) runner).setRenamingFile(input);
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

    
    private String          getPageTitle(int forwardName)
    {
        switch (forwardName)
        {
                case Constants.PROCESS_CREATE_FILE_FOR_TRACEFILES_TRANSFER:
                 return "  Processing Request for Plate Upload";
                 
                    
                case Constants.PROCESS_INITIATE_TRACEFILES_TRANSFER:
                 return "Processing Request for Trace Files Transfer";
                   
                case Constants.PROCESS_CREATE_RENAMING_FILE_FOR_TRACEFILES_TRANSFER:
                return "Processing Request for Creating Trace Renaming File for Trace Files";
         default: return "";
        }
     }
     
    
    private String          getPageLocation(int forwardName)
    {
        switch (forwardName)
        {
                case Constants.PROCESS_CREATE_FILE_FOR_TRACEFILES_TRANSFER:
                 return " Home > Trace Files >  Processing Request for Plate Upload";
                 
                    
                case Constants.PROCESS_INITIATE_TRACEFILES_TRANSFER:
                 return "Home > Trace Files > Upload Trace Files ";
                   
                case Constants.PROCESS_CREATE_RENAMING_FILE_FOR_TRACEFILES_TRANSFER:
                return "Home > Trace Files >  Create Renaming File";
         default: return "";
        }
     }
                


    
}


