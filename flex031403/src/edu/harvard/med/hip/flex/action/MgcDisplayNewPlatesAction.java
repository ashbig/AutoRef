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
        Hashtable labels = new Hashtable(); 
        DatabaseTransaction t = null;
        Connection conn = null;
        MgcContainerCollection mgcCol = new MgcContainerCollection();
       
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            
            labels =  mgcCol.getNewPlates(fileName,conn);
            request.setAttribute("filename", fileName);
            
            
            if (labels == null) return (mapping.findForward("no_labels_found"));
            /*
            //calculate number of pages
             int itemsPerPage =
                    Integer.parseInt(FlexProperties.getInstance().
                    getProperty("flex.approvesequences.pagesize"));
            
              // get the total number of items.
            int totalItems = labels.size();
            // find out how many pages we will need.
            int pageCount = (int)Math.ceil((double)totalItems/itemsPerPage);
            // make an array with all the page numbers
            ArrayList pages = new ArrayList(pageCount);
            
            for (int i = 1; i <= pageCount; i++) {
                pages.add(i-1,String.valueOf(i));
                
            }
           
            
            // session.setAttribute("PAGES", pages);
             
            // session.setAttribute("CURRENT_PAGE", new Integer(1) );
             */
             session.setAttribute("LABELS", labels);
             
             return (mapping.findForward("displaylabels")) ;
             
        } catch (Exception e) {
            request.setAttribute(Action.EXCEPTION_KEY, e);
            return (mapping.findForward("error"));
        }
    }


}
