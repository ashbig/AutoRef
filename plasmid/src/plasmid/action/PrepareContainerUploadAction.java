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
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import plasmid.process.ContainerUploader;

/**
 *
 * @author dongmei
 */
public class PrepareContainerUploadAction extends InternalUserAction{
    
    /** Creates a new instance of WorklistInputAction */
    public PrepareContainerUploadAction() {
    }
    
    public ActionForward internalUserPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<String> containertypes = ContainerUploader.getContainertypes();
        List<String> sampletypes = ContainerUploader.getSampletypes();
        List<String> locations = ContainerUploader.getLocations();
        request.setAttribute("containertypes", containertypes);
        request.setAttribute("sampletypes", sampletypes);
        request.setAttribute("locations", locations);
        return mapping.findForward("success");
    }
}

