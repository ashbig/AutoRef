/*
 * SequenceQueryAction.java
 *
 * Created on September 16, 2002, 2:56 PM
 */

package edu.harvard.med.hip.flex.action;

import java.util.*;
import java.sql.*;
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
import org.apache.struts.upload.*;

import edu.harvard.med.hip.flex.form.*;
import edu.harvard.med.hip.flex.query.*;

/**
 *
 * @author  dzuo
 * @version
 */
public class SequenceQueryAction extends CollaboratorAction {
    
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
        request.getSession().removeAttribute("output");
        
        ActionErrors errors = new ActionErrors();
        String querySelect = ((SequenceQueryForm)form).getQuerySelect();
        String searchType = ((SequenceQueryForm)form).getSearchType();
        String searchTermType = ((SequenceQueryForm)form).getSearchTermType();
        int project = ((SequenceQueryForm)form).getProject();
        int workflow = ((SequenceQueryForm)form).getWorkflow();
        String flexstatus = null;
        String plate = null;
        boolean isResultDisplay = false;
        
        if("queryGenes".equals(querySelect)) {
            flexstatus = ((SequenceQueryForm)form).getFlexstatus();
            plate = ((SequenceQueryForm)form).getPlate();
            isResultDisplay = ((SequenceQueryForm)form).getIsResultDisplay();
        }
        
        String searchTerm = null;
        FormFile filename = null;
        QueryParser parser = new QueryParser();
        ArrayList searchTermList = null;
        if("nonfile".equals(searchTermType)) {
            searchTerm = ((SequenceQueryForm)form).getSearchTerm();
        } else {
            filename = ((SequenceQueryForm)form).getFilename();
            if(filename == null) {
                errors.add("filename", new ActionError("error.query.nofile"));
                saveErrors(request,errors);
                return new ActionForward(mapping.getInput());
            }
            
            InputStream input;
            
            try {
                input = filename.getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(input));
                String line = null;
                searchTerm = "";
                while((line = in.readLine()) != null) {
                    searchTerm = searchTerm+line+"\n";
                }
            } catch (FileNotFoundException ex) {
                errors.add("filename", new ActionError("flex.infoimport.file", ex.getMessage()));
                saveErrors(request,errors);
                return new ActionForward(mapping.getInput());
            } catch (IOException ex) {
                errors.add("sequenceFile", new ActionError("flex.infoimport.file", ex.getMessage()));
                saveErrors(request,errors);
                return new ActionForward(mapping.getInput());
            }
        }
        
        if(searchTerm == null || searchTerm.trim().equals("")) {
            errors.add("searchTerm", new ActionError("error.query.noSearchTerm"));
            saveErrors(request,errors);
            return new ActionForward(mapping.getInput());
        }
        
        if(parser.parse(searchTerm)) {
            searchTermList = parser.getOutput();
        } else {
            request.setAttribute(Action.EXCEPTION_KEY, new Exception(parser.getMessage()));
            return mapping.findForward("error");
        }
        
        QueryManager manager = new QueryManager();
        ArrayList output = new ArrayList();
        
        if("queryClones".equals(querySelect)) {
            if(manager.queryClone(searchTermList, searchType, project)) {
                output = manager.getQueryInfoList();
                request.getSession().setAttribute("output", output);
                return (mapping.findForward("success_queryClone"));
            } else {
                request.setAttribute(Action.EXCEPTION_KEY, new Exception(manager.getMessage()));
                return mapping.findForward("error");
            }
        }
        
        if(manager.doQuery(searchTermList,searchType,flexstatus,project,workflow,plate)) {
            output = manager.getQueryInfoList();
            request.getSession().setAttribute("output", output);
            
            if(isResultDisplay)
                request.setAttribute("isResultDisplay", new Boolean(isResultDisplay));
            
            if("queryGenes".equals(querySelect)) {
                return (mapping.findForward("success_queryGenes"));
            } else {
                return (mapping.findForward("success_queryAllGenes"));
            }
        } else {
            request.setAttribute(Action.EXCEPTION_KEY, new Exception(manager.getMessage()));
            return mapping.findForward("error");
        }
    }
}

