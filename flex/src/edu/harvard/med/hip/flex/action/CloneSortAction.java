/*
 * CloneSortAction.java
 *
 * Created on June 19, 2003, 1:04 PM
 */

package edu.harvard.med.hip.flex.action;

import java.util.*;
import java.util.Hashtable;
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

import edu.harvard.med.hip.flex.form.BrowseForm;
import edu.harvard.med.hip.flex.query.BrowseFlexManager;
import edu.harvard.med.hip.flex.action.*;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.core.CloneInfo.RefseqidComparator;
import edu.harvard.med.hip.flex.user.*;
import edu.harvard.med.hip.flex.Constants;

/**
 *
 * @author  dzuo
 */
public class CloneSortAction extends Action {
    
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
        String sortby = ((BrowseForm)form).getSortby();
        int pageindex = ((BrowseForm)form).getPageindex();
        int pagerecord = ((BrowseForm)form).getPagerecord();
        int isSort = ((BrowseForm)form).getIsSort();
        String prevButton = ((BrowseForm)form).getPrevButton();
        List info = (List)request.getSession().getAttribute("cloneInfo");
        
        ActionErrors errors = new ActionErrors();
        if(info == null) {
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.session.expire"));
            saveErrors(request, errors);
            return mapping.findForward("login");
        }
        
        if(isSort != 0) {
            if("flexseqid".equals(sortby)) {
                Collections.sort(info, new CloneInfo.RefseqidComparator());
            }
            
            if("genesymbol".equals(sortby)) {
                Collections.sort(info, new CloneInfo.GenesymbolComparator());
            }
            
            if("genbank".equals(sortby)) {
                Collections.sort(info, new CloneInfo.GenbankComparator());
            }
            
            if("clonename".equals(sortby)) {
                Collections.sort(info, new CloneInfo.ClonenameComparator());
            }
            
            if("constructtype".equals(sortby)) {
                Collections.sort(info, new CloneInfo.ConstructtypeComparator());
            }
            
            if("cloneAcc".equals(sortby)) {
                Collections.sort(info, new CloneInfo.CloneAccComparator());
            }
        }
        
        int prev = 0;
        int next = 0;
        
        if("Prev".equals(prevButton))
            pageindex = pageindex-1;
        
        if("Next".equals(prevButton))
            pageindex = pageindex+1;
        
        if((info.size()-(pageindex)*pagerecord) > 0) {
            next = 1;
        }
        
        if(pageindex > 1) {
            prev = 1;
        }
        
        List currentInfo = null;
        if(pageindex*pagerecord < info.size()) {
            currentInfo = info.subList((pageindex-1)*pagerecord, pageindex*pagerecord);
        } else {
            currentInfo = info.subList((pageindex-1)*pagerecord, info.size());
        }
        
        request.setAttribute("prev", new Integer(prev));
        request.setAttribute("next", new Integer(next));
        request.setAttribute("pageindex", new Integer(pageindex));
        request.setAttribute("pagerecord", new Integer(pagerecord));
        request.setAttribute("currentInfo", currentInfo);
        request.setAttribute("isSort", new Integer(isSort));
        request.setAttribute("totalClones", new Integer(info.size()));
        
        User user = (User)request.getSession().getAttribute(Constants.USER_KEY);
        
        if(user == null) {
            request.setAttribute(Constants.ISDISPLAY, new Integer(0));
        } else {
            boolean retValue = AccessManager.getInstance().isUserAuthorize(user, Constants.RESEARCHER_GROUP);
            if(retValue) {
                request.setAttribute(Constants.ISDISPLAY, new Integer(1));
            } else {
                request.setAttribute(Constants.ISDISPLAY, new Integer(0));
            }
        }
        
        return (mapping.findForward("success"));
    }
}
