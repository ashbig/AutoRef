/*
 * GetAllSearchRecordsAction.java
 *
 * Created on February 11, 2004, 1:32 PM
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

import edu.harvard.med.hip.flex.query.handler.QueryManager;
import edu.harvard.med.hip.flex.Constants;
import edu.harvard.med.hip.flex.user.*;
import edu.harvard.med.hip.flex.query.core.*;
import edu.harvard.med.hip.flex.query.bean.*;

/**
 *
 * @author  DZuo
 */
public class GetAllSearchRecordsAction extends FlexAction {
    
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
        User user = (User)request.getSession().getAttribute(Constants.USER_KEY);
        
        QueryManager manager = new QueryManager();
        List searchRecords = manager.getSearchRecords(user.getUsername());
     
        if(searchRecords != null && searchRecords.size() > 0) {
            List queryInfoList = new ArrayList();
            for(int i=0; i<searchRecords.size(); i++) {
                SearchRecord searchRecord = (SearchRecord)searchRecords.get(i);
                int searchid = searchRecord.getSearchid();
                int numOfResults = manager.getNumOfResults(searchid);
                int numOfFounds = manager.getNumOfFounds(searchid);
                int numOfNofounds = manager.getNumOfNoFounds(searchid);
                QueryInfo queryInfo = new QueryInfo(searchRecord, numOfResults, numOfFounds, numOfNofounds);
                queryInfoList.add(queryInfo);
            }
            request.setAttribute("queryInfoList", queryInfoList);
        }
        
        return (mapping.findForward("success")); 
    }
}
