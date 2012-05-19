/*
 * UploadResultFileAction.java
 *
 * Created on October 19, 2005, 10:49 AM
 */

package plasmid.action;

import java.util.*;
import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.*;

import plasmid.form.EnterResultsForm;
import plasmid.coreobject.*;
import plasmid.process.*;

/**
 *
 * @author  DZuo
 */
public class UploadResultFileAction extends InternalUserAction{
    
    /** Creates a new instance of UploadResultFileAction */
    public UploadResultFileAction() {
    }
    
    public ActionForward internalUserPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActionErrors errors = new ActionErrors();
        
        String resultType = ((EnterResultsForm)form).getResultType();
        FormFile resultFile = ((EnterResultsForm)form).getResultFile();
        ResultProcessManager manager = new ResultProcessManager();
        ContainerProcessManager man = new ContainerProcessManager();
        
        // parse the log file.
        InputStream input = null;
        try {
            input = resultFile.getInputStream();
            
            if(Result.AGAR.equals(resultType)) {
                Map colonyInfo = manager.uploadAgarResults(input);
                
                if(colonyInfo.size() == 0) {
                    errors.add("resultFile", new ActionError("error.general", "File is empty"));
                    saveErrors(request,errors);
                    return new ActionForward(mapping.getInput());
                }
            } else if(Result.CULTURE.equals(resultType)) {
                String filename = resultFile.getFileName();
                String label = filename.substring(0, filename.indexOf("_"));
                List labels = new ArrayList();
                labels.add(label);
                
                List containers = man.getContainers(labels, true);
                if(containers == null) {
                    errors.add("resultFile", new ActionError("error.general", "Cannot get the containers for labels: "+labels));
                    saveErrors(request,errors);
                    return new ActionForward(mapping.getInput());
                }
                
                Container container = (Container)containers.get(0);
                List results = manager.uploadCultureResults(input, container);
                ((EnterResultsForm)form).initiate(container);
                for(int i=0; i<results.size(); i++) {
                    ((EnterResultsForm)form).setResult(i, (String)results.get(i));
                }
                ((EnterResultsForm)form).setLabel(label);
            } else {
                return mapping.findForward("fail");
            }
            
            return mapping.findForward("success");
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
            errors.add("resultFile", new ActionError("error.file.notfound", resultFile.getFileName()));
            saveErrors(request,errors);
            return new ActionForward(mapping.getInput());
        } catch (Exception ex) {
            System.out.println(ex);
            errors.add("resultFile", new ActionError("error.general", ex.getMessage()));
            saveErrors(request,errors);
            return new ActionForward(mapping.getInput());
        }
    }
}
