/*
 * GetDataForRefseqSearchAction.java
 *
 * Created on April 19, 2005, 2:20 PM
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

import plasmid.database.*;
import plasmid.database.DatabaseManager.*;
import plasmid.Constants;
import plasmid.form.RefseqSearchForm;

/**
 *
 * @author  DZuo
 */
public class GetDataForRefseqSearchAction extends Action {

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
        DatabaseTransaction dt = null;
        try {
            List species = DefTableManager.getVocabularies("Species", "genusspecies");
           // List refseqTypes = defTableManager.getVocabularies("refseqtype", "refseqtype", dt);
            
            if(species == null) {
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.database.error", "Cannot get species from database"));
                return (mapping.findForward("error"));
            }
            /**
            if(refseqTypes == null) {
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.database.error", "Cannot get reference sequence type from database"));
                return (mapping.findForward("error"));
            }  */
            request.setAttribute("allSpecies", species);
            //request.setAttribute("refseqTypes", refseqTypes);
            ((RefseqSearchForm)form).resetValues();
            return (mapping.findForward("success"));
        } catch (Throwable th) {
            if(Constants.DEBUG)
                System.out.println(th);
            
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.database.error","Database error occured."));
            return (mapping.findForward("error")); 
        }
    }       
}
