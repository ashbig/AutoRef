/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bean;

import controller.UploadVigeneResultController;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.myfaces.custom.fileupload.UploadedFile;
import transfer.VariableTO;

/**
 *
 * @author DZuo
 */
public class UploadMicrovigeneBean {
    private String label;
    private UploadedFile file;
    private UploadedFile imagefile;
    private String type;
    private String value;
    private String extra;
    private List<VariableTO> variables;
           
    public UploadMicrovigeneBean() {
        variables = new ArrayList<VariableTO>();
    }
    
    public void addVariable(VariableTO v) {
        this.variables.add(v);
    }
    
    public String runUpload() {
        UploadVigeneResultController controller = new UploadVigeneResultController();
        try {
            InputStream file1 = file.getInputStream();
            InputStream file2 = file.getInputStream();
            String filename = file.getName();
            
            controller.setFile(file1);
            controller.setFile(file2);
            controller.setFilename(filename);
            controller.setLabel(label);
            
            controller.doProcess();
            controller.persistProcess();
            
            file1.close();
            file2.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } 
        
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
}
