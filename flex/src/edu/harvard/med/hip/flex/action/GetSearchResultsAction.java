/*
 * GetSearchResultsAction.java
 *
 * Created on February 11, 2004, 2:36 PM
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

import edu.harvard.med.hip.flex.form.QueryFlexForm;
import edu.harvard.med.hip.flex.query.handler.*;
import edu.harvard.med.hip.flex.query.bean.*;

/**
 *
 * @author  DZuo
 */
public class GetSearchResultsAction extends FlexAction {
    
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
    public ActionForward flexPerform(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException {
        ActionErrors errors = new ActionErrors();
        
        int searchid = ((QueryFlexForm)form).getSearchid();
        String condition = ((QueryFlexForm)form).getCondition();
        int currentPage = ((QueryFlexForm)form).getCurrentPage();
        int pageSize = ((QueryFlexForm)form).getPageSize();
        String searchCriteria = ((QueryFlexForm)form).getSearchCriteria();
        String cloneCriteria = ((QueryFlexForm)form).getCloneCriteria();
        int lastFounds = ((QueryFlexForm)form).getLastFounds();
        
        QueryManager manager = new QueryManager();
        manager.setSearchCriteria(searchCriteria);
        manager.setCloneCriteria(cloneCriteria);
        
        List params = manager.getParams(searchid);
        request.setAttribute("params", params);
        
        if("found".equals(condition)) {
            int number = manager.getNumOfFounds(searchid);
            
            if(number == 0) {
                return (mapping.findForward("empty"));
            }
            
            String submitButton = ((QueryFlexForm)form).getSumbitButton();
            List selectedClones = ((QueryFlexForm)form).getSelectedClones();
            
            if(selectedClones == null || number != lastFounds) {
                selectedClones = new ArrayList();
            }
            
            List allClones = ((QueryFlexForm)form).getAllClones();
            for(int i=0; i<allClones.size(); i++) {
                String clone = (String)allClones.get(i);
                changeCloneList(selectedClones, clone, false);
            }
            
            List checkedClones = ((QueryFlexForm)form).getCheckedClones();
            for(int i=0; i<checkedClones.size(); i++) {
                String clone = (String)checkedClones.get(i);
                changeCloneList(selectedClones, clone, true);
            }
            
            request.setAttribute("selectedClones", selectedClones);
            request.setAttribute("lastFounds", new Integer(number));
            
            if("Export".equals(submitButton)) {
                return (mapping.findForward("export"));
            }
            
            if((currentPage-1)*pageSize >= number) {
                currentPage = 1;
            }
            
            List founds = manager.getFounds(searchid, (currentPage-1)*pageSize, (currentPage)*pageSize);
            
            if(founds == null) {
                String error = manager.getError();
                request.setAttribute("error", error);
                return (mapping.findForward("fail"));
            }
            
            if(founds.size() == 0) {
                return (mapping.findForward("empty"));
            }
            
            int pages = number/pageSize;
            if(number%pageSize>0) {
                pages += 1;
            }
            
            List allPages = new ArrayList();
            for(int i=0; i<pages; i++) {
                allPages.add(new PageInfo(i+1, i+1));
            }
            
            List pageSizes = new ArrayList();
            pageSizes.add(new PageSize(10, 10));
            pageSizes.add(new PageSize(25, 25));
            pageSizes.add(new PageSize(50, 50));
            pageSizes.add(new PageSize(100, 100));
            
            request.setAttribute("pageSizes", pageSizes);
            request.setAttribute("pageSize", new Integer(pageSize));
            request.setAttribute("currentPage", new Integer(currentPage));
            request.setAttribute("allPages", allPages);
            request.setAttribute("pages", new Integer(pages));
            request.setAttribute("results", founds);
            request.setAttribute("searchid", new Integer(searchid));
            request.setAttribute("condition", condition);
            
            if(QueryManager.DETAIL.equals(searchCriteria)) {
                return mapping.findForward("success_found_detail");
            }
            
            return (mapping.findForward("success_found"));
        } else if("nofound".equals(condition)) {
            List nofounds = manager.getNoFounds(searchid);
            
            if(nofounds == null) {
                String error = manager.getError();
                request.setAttribute("error", error);
                return (mapping.findForward("fail"));
            }
            
            if(nofounds.size() == 0) {
                return (mapping.findForward("empty"));
            }
            
            request.setAttribute("results", nofounds);
            return (mapping.findForward("success_nofound"));
        } else {
            return (mapping.findForward("error"));
        }
    }
    
    private void changeCloneList(List selectedClones, String clone, boolean isChecked) {
        for(int i=0; i<selectedClones.size(); i++) {
            String selectedClone = (String)selectedClones.get(i);
            if(clone.equals(selectedClone)) {
                if(!isChecked) {
                    selectedClones.remove(i);
                    return;
                }
                return;
            }
        }
        if(isChecked) {
            selectedClones.add(clone);
        }
    }
}
