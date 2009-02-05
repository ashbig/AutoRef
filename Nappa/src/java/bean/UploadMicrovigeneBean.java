/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import controller.UploadVigeneResultController;
import dao.ResearcherDAO;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.myfaces.custom.fileupload.UploadedFile;
import transfer.ProcessexecutionTO;
import transfer.ProcessprotocolTO;
import transfer.VariableTO;

/**
 *
 * @author DZuo
 */
public class UploadMicrovigeneBean implements Serializable {

    private String label;
    private UploadedFile file;
    private UploadedFile imagefile;
    private String type;
    private String value;
    private String extra;
    private List<VariableTO> variables;
    private String message;

    public UploadMicrovigeneBean() {
        variables = new ArrayList<VariableTO>();
        setMessage("");
    }

    public void addVariable(VariableTO v) {
        this.variables.add(v);
    }

    public String runUpload() {
        setMessage("Upload successful.");
        UploadVigeneResultController controller = new UploadVigeneResultController();
        try {
            InputStream file1 = file.getInputStream();
            InputStream file2 = file.getInputStream();
            String filename = file.getName();

            InputStream file3 = imagefile.getInputStream();
            String imagefilename = imagefile.getName();
            
            controller.setFile(file1);
            controller.setFilecopy(file2);
            controller.setFilename(filename);
            controller.setImagefile(file3);
            controller.setImagefilename(imagefilename);
            controller.setLabel(label);

            String researcherid = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getUserPrincipal().getName();
            controller.setWho(ResearcherDAO.getResearcher(researcherid));
            controller.setProtocol(new ProcessprotocolTO(ProcessprotocolTO.UPLOAD_VIGENE_RESULT, null, null));
            controller.setOutcome(ProcessexecutionTO.getOUTCOME_SUCCESS());
            
            controller.doProcess();
            controller.persistProcess();

            file1.close();
            file2.close();
            file3.close();
        } catch (Exception ex) {
            setMessage("Upload failed.\n"+ex.getMessage());
            ex.printStackTrace();
        }

        setLabel(null);
        return null;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public UploadedFile getImagefile() {
        return imagefile;
    }

    public void setImagefile(UploadedFile imagefile) {
        this.imagefile = imagefile;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public List<VariableTO> getVariables() {
        return variables;
    }

    public void setVariables(List<VariableTO> variables) {
        this.variables = variables;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
