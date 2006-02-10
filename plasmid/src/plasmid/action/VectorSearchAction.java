/*
 * VectorSearchAction.java
 *
 * Created on February 2, 2006, 3:47 PM
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

import plasmid.form.VectorSearchForm;
import plasmid.coreobject.*;
import plasmid.Constants;
import plasmid.database.DatabaseManager.UserManager;
import plasmid.process.QueryProcessManager;

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
        String species = ((VectorSearchForm)form).getSpecies();
        if(Constants.ALL.equals(species))
            species = null;
        boolean [][] types = ((VectorSearchForm)form).getVectortype();
        List vectortypes = (List)request.getSession().getAttribute("types");
        List properties = new ArrayList();
        
        for(int i=0; i<types.length; i++) {
            for(int j=0; i<types[i].length; j++) {
                boolean b = types[i][j];
                if(b) {
                    properties.add((String)vectortypes.get(i));
                }
            }
        }
        
        QueryProcessManager manager = new QueryProcessManager();
        User user = (User)request.getSession().getAttribute(Constants.USER_KEY);
        List clones = manager.queryClonesByVector(user, properties, species, Clone.AVAILABLE);
        
        if(clones == null) {
            return (mapping.findForward("error"));
        }
        
        Set vectors = manager.getVectorNamesFromClones(clones);
        ((VectorSearchForm)form).setVectorname(vectors);
        ((VectorSearchForm)form).setClones(clones);
        request.setAttribute("numberOfClones", new Integer(clones.size()));
        request.setAttribute("vectors", vectors);
        request.setAttribute("species", species);
        
        return (mapping.findForward("success"));
    }
}
