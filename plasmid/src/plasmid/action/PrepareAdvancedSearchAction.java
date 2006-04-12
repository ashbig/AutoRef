/*
 * PrepareAdvancedSearchAction.java
 *
 * Created on March 17, 2006, 11:00 AM
 */

package plasmid.action;

import java.util.*;
import java.io.*;
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

import plasmid.database.DatabaseManager.DefTableManager;
import plasmid.form.AdvancedSearchForm;
import plasmid.Constants;

/**
 *
 * @author  DZuo
 */
public class PrepareAdvancedSearchAction extends PlasmidAction {
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
    public ActionForward plasmidPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       
        ActionErrors errors = new ActionErrors();
        List species = DefTableManager.getVocabularies("species", "genusspecies");
        species.add(0, Constants.ALL); 
        
        ((AdvancedSearchForm)form).resetParams();
        request.setAttribute("species", species);
        
        return (mapping.findForward("success"));
    }
    
}