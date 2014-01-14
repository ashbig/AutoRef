/*
 * ConfirmResultsAction.java
 *
 * Created on November 30, 2005, 2:19 PM
 */

package plasmid.action;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import plasmid.form.EnterResultsForm;
import plasmid.Constants;
import plasmid.coreobject.*;
import plasmid.process.*;
import plasmid.coreobject.Process;

import java.util.*;
import java.io.*;

/**
 *
 * @author  DZuo
 */
public class ConfirmResultsAction extends InternalUserAction{
    
    /** Creates a new instance of ConfirmResultsAction */
    public ConfirmResultsAction() {
    }
    
    public ActionForward internalUserPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActionErrors errors = new ActionErrors();
        
        Container container = ((EnterResultsForm)form).getContainer();
        String resultType = ((EnterResultsForm)form).getResultType();
        List results = new ArrayList();
        List samples = container.getSamples();
        for(int i=0; i<samples.size(); i++) {
            Sample s = (Sample)samples.get(i);
            String result = ((EnterResultsForm)form).getResult(i);
            Result r = new Result(-1, s.getSampleid(), -1, resultType, result);
            results.add(r);
        }
        
        String processname = null;
        if(Result.CULTURE.equals(resultType)) {
            processname = Process.ENTER_CULTURE_RESULT;
        }
        
        User user = (User)request.getSession().getAttribute(Constants.USER_KEY);
        
        List containers = new ArrayList();
        containers.add(container);
        
        ProcessExecution process = new ProcessExecution(-1, ProcessExecution.COMPLETE, null, processname, user.getUsername(), null);
        process.setIoObjects(containers);
        process.setResults(results);
        
        ResultProcessManager manager = new ResultProcessManager();
        if(!manager.persistData(process, results)) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.general", "Error occured while inserting data into database"));
            saveErrors(request,errors);
            return mapping.findForward("error");
        }
             
        request.setAttribute("message",  "The results have been successfully entered for container: "+container.getLabel());
        request.removeAttribute("enterResultsForm");
        return mapping.findForward("success");        
    }
}
