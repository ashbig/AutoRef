/*
 * GetGeneSequenceAction.java
 *
 * Created on March 27, 2003, 11:01 AM
 */

package edu.harvard.med.hip.cloneOrder.action;

import java.io.*;
import java.util.*;
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

import edu.harvard.med.hip.cloneOrder.form.*;
import edu.harvard.med.hip.cloneOrder.core.*;
import edu.harvard.med.hip.cloneOrder.database.*;
import edu.harvard.med.hip.cloneOrder.Constants;

/**
 *
 * @author  hweng
 */
public class GetGeneSequenceAction extends Action {
    
    public ActionForward perform(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException {
        
        // Validate the request parameters specified by the user
        ActionErrors errors = new ActionErrors();
        String flexid = ((GetGeneSequenceForm)form).getFlexid();
        CloneInfo clone = new CloneInfo();
        String sequence = clone.getSeqFromFlexID(flexid);

        request.setAttribute("flexid", flexid);
        request.setAttribute("sequence", sequence);
        return (mapping.findForward("success"));
    }

}
