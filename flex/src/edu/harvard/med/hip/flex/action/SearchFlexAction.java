/*
 * SearchFlexAction.java
 *
 * Created on February 10, 2004, 12:37 PM
 */

package edu.harvard.med.hip.flex.action;

import java.io.*;
import java.util.*;
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
import org.apache.struts.upload.*;

import edu.harvard.med.hip.flex.query.core.*;
import edu.harvard.med.hip.flex.query.handler.*;
import edu.harvard.med.hip.flex.form.SearchFlexForm;
import edu.harvard.med.hip.flex.query.QueryParser;
import edu.harvard.med.hip.flex.Constants;
import edu.harvard.med.hip.flex.user.User;
import edu.harvard.med.hip.flex.database.*;

/**
 *
 * @author  DZuo
 */
public class SearchFlexAction extends FlexAction {
    
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
        
        String searchName = ((SearchFlexForm)form).getSearchName();
        String searchType = ((SearchFlexForm)form).getSearchType();
        String searchTermType = ((SearchFlexForm)form).getSearchTermType();
        int pid = ((SearchFlexForm)form).getPid();
        int length = ((SearchFlexForm)form).getLength();
        String searchDatabase = ((SearchFlexForm)form).getSearchDatabase();
        String searchTerm = null;
        FormFile filename = null;
        
        if("nonfile".equals(searchTermType)) {
            searchTerm = ((SearchFlexForm)form).getSearchTerm();
        } else {
            filename = ((SearchFlexForm)form).getFilename();
            if(filename == null) {
                errors.add("filename", new ActionError("error.query.nofile"));
                saveErrors(request,errors);
                return new ActionForward(mapping.getInput());
            }
            
            InputStream input;
            
            try {
                input = filename.getInputStream();
                searchTerm = QueryParser.convertFileToString(input);
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
        
        List searchTermList = null;
        try {
            searchTermList = QueryParser.parseStringToList(searchTerm);
        } catch (NoSuchElementException ex) {
            request.setAttribute(Action.EXCEPTION_KEY, new Exception(ex.getMessage()));
            return mapping.findForward("error");
        }
        
        User user = (User)request.getSession().getAttribute(Constants.USER_KEY);
        SearchRecord searchRecord = new SearchRecord(searchName, searchType, SearchRecord.INPROCESS, user.getUsername());
        List params = new ArrayList();
        params.add(new Param(Param.BLASTPID, (new Integer(pid)).toString()));
        params.add(new Param(Param.BLASTLENGTH, (new Integer(length)).toString()));
        params.add(new Param(Param.BLASTDB, searchDatabase));
        
        ThreadedSearchManager manager = new ThreadedSearchManager(searchRecord, params, searchTermList);
        new Thread(manager).start();   
                
        return (mapping.findForward("success"));
    }
}

