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
import org.apache.struts.upload.FormFile;
import plasmid.coreobject.Container;
import plasmid.form.UploadContainerForm;
import plasmid.process.ContainerUploader;
import plasmid.process.ProcessException;
import plasmid.util.StringConvertor;

/**
 *
 * @author dongmei
 */
public class UploadContainerAction extends InternalUserAction {

    /** Creates a new instance of WorklistInputAction */
    public UploadContainerAction() {
    }

    public ActionForward internalUserPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActionErrors errors = new ActionErrors();

        String containertype = ((UploadContainerForm) form).getContainertype();
        String sampletype = ((UploadContainerForm) form).getSampletype();
        String location = ((UploadContainerForm) form).getLocation();
        FormFile file = ((UploadContainerForm) form).getFile();

        List<String> containertypes = ContainerUploader.getContainertypes();
        List<String> sampletypes = ContainerUploader.getSampletypes();
        List<String> locations = ContainerUploader.getLocations();
        request.setAttribute("containertypes", containertypes);
        request.setAttribute("sampletypes", sampletypes);
        request.setAttribute("locations", locations);

        ContainerUploader uploader = new ContainerUploader();
        uploader.setContainertype(containertype);
        uploader.setSampletype(sampletype);
        uploader.setLocation(location);
        try {
            List<Container> containers = uploader.readContainerFile(file.getInputStream());
            List<String> nofound = uploader.retriveClones(containers, uploader.getClonenames());
            if (nofound.size() > 0) {
                StringConvertor sc = new StringConvertor();
                String s = sc.convertFromListToString(nofound);
                errors.add("file", new ActionError("error.general", "Cannot find the following clones: " + s));
                saveErrors(request, errors);
                return new ActionForward(mapping.getInput());
            }
            uploader.importContainers(containers);
        } catch (IOException ex) {
            errors.add("file", new ActionError("error.general", "Cannot read the file."));
            saveErrors(request, errors);
            return new ActionForward(mapping.getInput());
        } catch (ProcessException ex) {
            errors.add("file", new ActionError("error.general", ex.getMessage()));
            saveErrors(request, errors);
            return new ActionForward(mapping.getInput());
        } catch (Exception ex) {
            ex.printStackTrace();
            errors.add("file", new ActionError("error.general", "Error occured."));
            saveErrors(request, errors);
            return new ActionForward(mapping.getInput());
        }

        errors.add("file", new ActionError("message.general", "Containers are successfully uploaded to the database."));
        saveErrors(request, errors);
        return new ActionForward(mapping.getInput());
    }
}
