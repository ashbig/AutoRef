/*
 * GetVectorPropertyTypesAction.java
 *
 * Created on January 27, 2006, 2:01 PM
 */

package plasmid.action;

import java.util.*;
import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import plasmid.database.DatabaseManager.DefTableManager;
import plasmid.database.DatabaseManager.VectorManager;
import plasmid.Constants;
import plasmid.form.*;

/**
 *
 * @author  DZuo
 */
public class GetVectorPropertyTypesAction extends Action {
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
        List species = DefTableManager.getVocabularies("species", "genusspecies");
        species.add(0, Constants.ALL);
        Map types = VectorManager.getAllVectorPerpertyTypes();
        
        ((VectorSearchForm)form).setTypes(types);
        ((VectorSearchForm)form).resetVectortypes(types);
        ((VectorSearchForm)form).resetLogicOperators(types.size());
        request.setAttribute("species", species);
        
        ViewCartForm f = (ViewCartForm)request.getSession().getAttribute("viewCartForm");
        if(f != null)
            f.setIsBatch(null);
        
        return (mapping.findForward("success"));
    }
}
