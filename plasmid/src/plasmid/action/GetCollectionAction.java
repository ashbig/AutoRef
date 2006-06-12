/*
 * GetCollectionAction.java
 *
 * Created on November 7, 2005, 10:51 AM
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
import plasmid.form.*;

/**
 *
 * @author  DZuo
 */
public class GetCollectionAction extends Action {
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
        
        String collectionName = ((RefseqSearchForm)form).getCollectionName();
        
        QueryProcessManager manager = new QueryProcessManager();
        int numOfClones = manager.getClonenumInCollection(collectionName);
        
        User user = (User)request.getSession().getAttribute(Constants.USER_KEY);
        List restrictions = new ArrayList();
        restrictions.add(Clone.NO_RESTRICTION);
        if(user != null) {
            List ress = UserManager.getUserRestrictions(user);
            restrictions.addAll(ress);
        }
        
        int pagesize = Constants.PAGESIZE;
        int page = 1;
        request.setAttribute("displayPage", "indirect");
        request.setAttribute("pagesize", new Integer(pagesize));
        request.setAttribute("page",  new Integer(page));
        
        CollectionInfo info = null;
        if(numOfClones>Constants.DOWNLOADTHRESHOLD) {
            info = manager.getCollection(collectionName, CollectionInfo.DISTRIBUTION, restrictions, false);
            ((RefseqSearchForm)form).setIsDownload(Constants.BOOLEAN_ISDOWNLOAD_YES);
        } else {
            info = manager.getCollection(collectionName, CollectionInfo.DISTRIBUTION, restrictions, true);
            ((RefseqSearchForm)form).setIsDownload(Constants.BOOLEAN_ISDOWNLOAD_NO);
        }
        
        ((RefseqSearchForm)form).setNumOfClone(numOfClones);
        
        if(info == null) {
            if(Constants.DEBUG)
                System.out.println("Cannot get collection with name: "+collectionName);
            
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.general", "Cannot get collection with name: "+collectionName));
            saveErrors(request, errors);
            return (mapping.findForward("error"));
        }
        
        request.getSession().setAttribute(Constants.SINGLECOLLECTION, info);
        request.getSession().setAttribute("found", info.getClones());
        
        return (mapping.findForward("success"));
    }
}


