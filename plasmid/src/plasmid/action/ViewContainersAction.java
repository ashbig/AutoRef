/*
 * ViewContainersAction.java
 *
 * Created on April 17, 2006, 1:25 PM
 */

package plasmid.action;

import java.util.*;
import java.io.*;
import java.sql.*;
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

import plasmid.coreobject.*;
import plasmid.form.ViewContainersForm;
import plasmid.util.StringConvertor;
import plasmid.process.ContainerProcessManager;

/**
 *
 * @author  DZuo
 */
public class ViewContainersAction extends Action {
    /**
     * Process the specified HTTP request, and create the corresponding HTTP
     * response (or forward to another web component that will create it).
     * Return an <code>ActionForward</code> instance describing where and how
     * control should be forwarded, or <code>null</code> if the response has
     * already been completed.
     *
     * @param mapping The ActionMapping used to select this instance
     * @param actionForm The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */
    public ActionForward perform(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException {
        
        // get the parameters specified by the customer
        ActionErrors errors = new ActionErrors();
        String labelString = ((ViewContainersForm)form).getLabelString();
        
        StringConvertor sv = new StringConvertor();
        List labels = sv.convertFromStringToList(labelString, " \t\n");
        
        if(labels == null || labels.size() == 0) {
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.container.required"));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
        
       // List newLabels = new ArrayList();
       // newLabels.add((String)labels.get(0));
        ContainerProcessManager manager = new ContainerProcessManager();
        List containers = manager.getContainersWithCloneInfo(labels);
        
        if(containers == null) {            
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("Cannot get containers from database."));
            saveErrors(request, errors);
            return mapping.findForward("error");
        }
        
        if(containers.size() == 0) {
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.container.required"));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
        
        List growths = manager.getGrowthConditions(containers);
        
        request.setAttribute("containers", containers);
        request.setAttribute("growths", growths);
        return (mapping.findForward("success"));
    }
}

