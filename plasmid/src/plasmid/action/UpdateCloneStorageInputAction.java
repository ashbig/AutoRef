/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package plasmid.action;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import plasmid.database.DatabaseManager.CloneManager;
import plasmid.form.UpdateCloneStorageForm;
import plasmid.util.StringConvertor;

/**
 *
 * @author Dongmei
 */
public class UpdateCloneStorageInputAction extends InternalUserAction{
    
    /** Creates a new instance of FindClonesAction */
    public UpdateCloneStorageInputAction() {
    }
    
    public ActionForward internalUserPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActionErrors errors = new ActionErrors();
        
        String cloneList = ((UpdateCloneStorageForm)form).getCloneidList();
        StringConvertor sc = new StringConvertor();
        List clones = sc.convertFromStringToList(cloneList, " \n\t\b");
        
        if(clones == null || clones.size()==0) {
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.general", "Please enter the valid PlasmID Clone ID."));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
        
        CloneManager manager = new CloneManager(null);
        List foundClones = manager.queryCloneSamples(clones);
        
        if(foundClones == null) {
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.general", "Error occurred in the database."));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
           
        ((UpdateCloneStorageForm)form).initiateLabels(foundClones.size());
        request.getSession().setAttribute("cloneSamples", foundClones);
        return mapping.findForward("success");
    }   
}
