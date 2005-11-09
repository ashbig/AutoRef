/*
 * GetCollectionListAction.java
 *
 * Created on November 7, 2005, 9:35 AM
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

import plasmid.coreobject.*;
import plasmid.Constants;
import plasmid.database.DatabaseManager.*;
import plasmid.process.QueryProcessManager;

/**
 *
 * @author  DZuo
 */
public class GetCollectionListAction extends Action {
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
        
        User user = (User)request.getSession().getAttribute(Constants.USER_KEY);
        List restrictions = new ArrayList();
        restrictions.add(Clone.NO_RESTRICTION);
        if(user != null) {
            List ress = UserManager.getUserRestrictions(user);
            restrictions.addAll(ress);
        } 
        
        QueryProcessManager manager = new QueryProcessManager();
        List collections = manager.getCollections(CollectionInfo.DISTRIBUTION, restrictions);
        
        if(collections == null) {
            if(Constants.DEBUG)
                System.out.println("Cannot get collections from database");
            
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.general", "Cannot get collections from database."));
            saveErrors(request, errors);
            return (mapping.findForward("error"));
        }
        
        if(collections.size() == 0) {
            return (mapping.findForward("empty"));
        }
        
        request.setAttribute(Constants.COLLECTION, collections);        
        return (mapping.findForward("success"));
    }
}

