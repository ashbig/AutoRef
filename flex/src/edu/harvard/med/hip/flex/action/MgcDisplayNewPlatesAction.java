/*
 * MgcViewNewPlates.java
 *
 * Created on May 14, 2002, 11:08 AM
 */

package edu.harvard.med.hip.flex.action;

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

import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.util.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.form.*;

/**
 *
 * @author  htaycher
 */
public class MgcDisplayNewPlatesAction extends ResearcherAction
{
    
    public ActionForward flexPerform (ActionMapping mapping,
                                        ActionForm form,
                                        HttpServletRequest request,
                                        HttpServletResponse response)
                                        throws ServletException, IOException
    {
        ActionErrors errors = new ActionErrors();
        String fileName =  ((MgcCloneInfoImportForm)form).getFileName();
        HttpSession session = request.getSession();
        ArrayList labels = new ArrayList(); 
   
        MgcContainerCollection mgcCol = new MgcContainerCollection();
       
        try {
            
            labels =  mgcCol.getNewPlates(fileName);
            request.setAttribute("filename", fileName);
            
            request.setAttribute("LABELS", labels);
            
            return (mapping.findForward("displayplates")) ;
             
        } catch (Exception e) {
            request.setAttribute(Action.EXCEPTION_KEY, e);
            return (mapping.findForward("error"));
        }
    }


}
