/*
 * ContainerProcessAction.java
 *
 * Created on June 17, 2003, 4:10 PM
 */

package edu.harvard.med.hip.bec.action;

/**
 *
 * @author  htaycher
 */

 


import java.util.*;

import java.sql.*;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.util.MessageResources;

import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.coreobjects.oligo.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.form.*;
import edu.harvard.med.hip.bec.user.*;
import edu.harvard.med.hip.bec.Constants;
import edu.harvard.med.hip.bec.sampletracking.objects.*;

public class ContainerProcessAction extends ResearcherAction
{
    
    
    public ActionForward becPerform(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException
    {
        ActionErrors errors = new ActionErrors();
        
        int forwardName = ((Seq_GetSpecForm)form).getForwardName();
       
        String label = (String)request.getParameter("CONTAINER_BARCODE");
        String title = (String)request.getParameter(Constants.JSP_TITLE);
         request.setAttribute(Constants.JSP_TITLE,title);
       
        Container container = null;
        try
        {
            label =label.toUpperCase().trim();
     
            //check if container exists
            if (label == null && label.equals("") )
            {
                errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.container.querry.parameter", 
                    "Unable to find in databse container with label "+label));
                saveErrors(request,errors);
                
                return new ActionForward(mapping.getInput());
               
            }
            
            container = Container.findContainerDescriptionFromLabel(label);
            System.out.println("container "+ container.getId() +" "+label);
            if ( container == null)
            {
                 errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.container.querry.parameter", 
                    "Unable to find container in databse  with label "+label));
                saveErrors(request,errors);
               
                return new ActionForward(mapping.getInput());
            }
          
            switch  (forwardName)
            {
                
                case Constants.CONTAINER_PROCESS_HISTORY:
                {
                    ArrayList pr_history = Container.getProcessHistoryItems( label);
                    request.setAttribute(Constants.CONTAINER_BARCODE_KEY,label) ;
                     request.setAttribute(Constants.CONTAINER_KEY,new Integer( container.getId()));
                   // request.getAttribute("cloning_strategy", container.get) ;
                    request.setAttribute("process_items",pr_history);
                    return (mapping.findForward("display_container_history"));
                }
                case Constants.CONTAINER_DEFINITION_INT:
                {
                    
                    
                    request.setAttribute("container",container);
                    return (mapping.findForward("display_container_details"));
                }
                case Constants.CONTAINER_RESULTS_VIEW:
                {
                   
                    String result_type = (String)request.getParameter("show_action");
                    if ( result_type.equalsIgnoreCase("FER"))
                    { System.out.println("A");
                    }
                    else if (result_type.equalsIgnoreCase("RER"))//reverse
                    {System.out.println("AS");
                    }
                    else if (result_type.equalsIgnoreCase("IR"))
                    {
                    System.out.println("SA");
                  
                       
                    }
                     request.setAttribute("container",container);
                     return (mapping.findForward("display_container_details"));
                 
                } 
              
            }
            
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            request.setAttribute(Action.EXCEPTION_KEY, e);
            return (mapping.findForward("error"));
        }
        return (mapping.findForward("error"));
    }





    
}
