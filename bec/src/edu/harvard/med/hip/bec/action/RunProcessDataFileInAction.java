//Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->
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
   

public class RunProcessDataFileInAction extends BecAction
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
        Connection conn = null;
        FormFile requestFile = null; String sequence_ids = null;
        ArrayList sequence_id_items = new ArrayList();
        InputStream input = null;
        
        
         int forwardName = ((Seq_GetSpecForm)form).getForwardName();
         String sequence_id_type = (String)request.getAttribute("searchType");
         String searchSubmissionType = (String)request.getAttribute("searchSubmissionType");
         
         if ( searchSubmissionType.equalsIgnoreCase("nonfile"))
         {
            sequence_ids = (String)request.getAttribute("searchTerm");
            //parse plate names
            sequence_id_items = Algorithms.splitString(sequence_ids);
            
         }
         else if ( searchSubmissionType.equalsIgnoreCase("file"))
         {
             requestFile = ((SubmitDataFileForm)form).getFileName();
             if(requestFile == null)
             {
                errors.add("filename", new ActionError("error.query.nofile"));
                saveErrors(request,errors);
                return new ActionForward(mapping.getInput());
            }
             try
            {
                input = requestFile.getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(input));
                String line = null;
                while((line = in.readLine()) != null)
                {
                    line +=line+"\n";
                }
                sequence_id_items = Algorithms.splitString(line);
            } catch (Exception ex)
            {
                errors.add("fileName", new ActionError("bec.infoimport.file", ex.getMessage()));
                saveErrors(request,errors);
                return new ActionForward(mapping.getInput());
            }
        }
         //check if any sequence id was submitted
        if (sequence_id_items.size() < 1)
        {
            errors.add("error", new ActionError("error while submitting data ", "no sequence id submitted"));
            saveErrors(request,errors);
            return new ActionForward(mapping.getInput());
        }
         //start processing 
        try
        {
            User user = (User)request.getSession().getAttribute(Constants.USER_KEY);
            DatabaseTransaction dt = DatabaseTransaction.getInstance();
            conn = dt.requestConnection();
   
            switch (forwardName)
            {
               
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

    

