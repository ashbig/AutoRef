/*
 * GetResultTypesAction.java
 *
 * Created on October 11, 2005, 11:30 AM
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

import plasmid.process.ResultProcessManager;
import plasmid.Constants;

/**
 *
 * @author  DZuo
 */
public class GetResultTypesAction extends InternalUserAction{
    
    /** Creates a new instance of GetResultTypesAction */
    public GetResultTypesAction() {
    }
    
    public ActionForward internalUserPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActionErrors errors = new ActionErrors();
        
        ResultProcessManager manager = new ResultProcessManager();
        List resulttypes = manager.getResultTypes();
        request.setAttribute(Constants.RESULTTYPES, resulttypes);
        
        return mapping.findForward("success");
    }   
}
