/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plasmid.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

/**
 *
 * @author dongmei
 */
public class UploadContainerForm extends ActionForm {
    
    private String containertype = null;
    private String location=null;
    private String sampletype = null;
    private FormFile file = null;
    
   public UploadContainerForm() {
       super();
       // TODO Auto-generated constructor stub
   }

    @Override
   public void reset(ActionMapping mapping, HttpServletRequest request) {
       reset();
   }
   
   public void reset() {
        setContainertype(null);
        setLocation(null);
        setSampletype(null);
        setFile(null);
   }
   
    @Override
   public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
       ActionErrors errors = new ActionErrors();
       return errors;
   }

    /**
     * @return the containertype
     */
    public String getContainertype() {
        return containertype;
    }

    /**
     * @param containertype the containertype to set
     */
    public void setContainertype(String containertype) {
        this.containertype = containertype;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return the sampletype
     */
    public String getSampletype() {
        return sampletype;
    }

    /**
     * @param sampletype the sampletype to set
     */
    public void setSampletype(String sampletype) {
        this.sampletype = sampletype;
    }

    /**
     * @return the file
     */
    public FormFile getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(FormFile file) {
        this.file = file;
    }
}
