/*
 * FindClonesAction.java
 *
 * Created on June 20, 2006, 10:57 AM
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

import plasmid.Constants;
import plasmid.form.FindClonesForm;
import plasmid.util.StringConvertor;
import plasmid.database.DatabaseManager.CloneManager;

/**
 *
 * @author  DZuo
 */
public class FindClonesAction extends InternalUserAction{
    
    /** Creates a new instance of FindClonesAction */
    public FindClonesAction() {
    }
    
    public ActionForward internalUserPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActionErrors errors = new ActionErrors();
        
        String cloneList = ((FindClonesForm)form).getCloneidList();
        StringConvertor sc = new StringConvertor();
        List clones = sc.convertFromStringToList(cloneList, " \n\t\b");
        
        if(clones == null || clones.size()==0) {
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.general", "Please enter the valid PlasmID Clone ID."));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
        
        CloneManager manager = new CloneManager(null);
        Map foundClones = manager.queryCloneSamples(clones);
        
        if(foundClones == null) {
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.general", "Error occurred in the database."));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
           
        request.setAttribute("cloneSamples", foundClones);
        return mapping.findForward("success");
    }   
}
