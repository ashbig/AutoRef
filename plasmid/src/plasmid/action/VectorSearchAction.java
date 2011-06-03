/*
 * VectorSearchAction.java
 *
 * Created on February 2, 2006, 3:47 PM
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

import plasmid.form.VectorSearchForm;
import plasmid.coreobject.*;
import plasmid.Constants;
import plasmid.process.QueryProcessManager;
import plasmid.query.handler.GeneQueryHandler;

/**
 *
 * @author  DZuo
 */
public class VectorSearchAction extends Action {

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
        String species = ((VectorSearchForm) form).getSpecies();

        Map types = ((VectorSearchForm) form).getTypes();

        QueryProcessManager manager = new QueryProcessManager();
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        List operators = manager.getVectorQueryOperators(types, (VectorSearchForm) form);
        Set vectorids = manager.getVectoridFromQueryOperators(operators, Constants.AND);
        List clones = manager.queryClonesByVector(user, vectorids, species);

        if (clones == null) {
            return (mapping.findForward("error"));
        }

        if (clones.size() > GeneQueryHandler.MAXCLONECOUNT) {
            return (mapping.findForward("summary"));
        }

        List vectors = manager.getVectorNamesFromClones(clones);
        ((VectorSearchForm) form).setVectornames(vectors);
        ((VectorSearchForm) form).setVectors(vectors);
        ((VectorSearchForm) form).setClones(clones);
        ((VectorSearchForm) form).resetVectornameBooleanValues(vectors);
        request.setAttribute("numberOfClones", new Integer(clones.size()));

        return (mapping.findForward("success"));
    }
}
