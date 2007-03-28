/*
 * GenerateMultipleWorklistForm.java
 *
 * Created on March 27, 2007, 11:49 AM
 */

package plasmid.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import java.util.*;
import plasmid.coreobject.Process;

/**
 *
 * @author  DZuo
 */
public class GenerateMultipleWorklistForm extends ActionForm {
    private String processname;
    private List protocols;
    private String protocol;
    private String srcContainerList;
    private String destContainerListWorking;
    private String destContainerListArchive;
    private String destContainerListBiobank;
    private int volumnWorking;
    private int volumnArchive;
    private int volumnBiobank;
    private String worklistname;
    
    /** Creates a new instance of GenerateWorklistForm */
    public GenerateMultipleWorklistForm() {
    }
        
    public String getProcessname() { return processname;}
    public List getProtocols() {return protocols;}
    public String getProtocol() {return protocol;}
    public String getSrcContainerList() {return srcContainerList;}
    public String getDestContainerListWorking() {return destContainerListWorking;}
    public String getDestContainerListArchive() {return destContainerListArchive;}
    public String getDestContainerListBiobank() {return destContainerListBiobank;}
    public int getVolumnWorking() {return volumnWorking;}
    public int getVolumnArchive() {return volumnArchive;}
    public int getVolumnBiobank() {return volumnBiobank;}
    public String getWorklistname() {return worklistname;}
    
    public void setProcessname(String s) {this.processname = s;}
    public void setProtocols(List l) {this.protocols = l;}
    public void setProtocol(String s) {this.protocol = s;}
    public void setSrcContainerList(String s) {this.srcContainerList = s;}
    public void setDestContainerListWorking(String s) {this.destContainerListWorking = s;}
    public void setDestContainerListArchive(String s) {this.destContainerListArchive = s;}
    public void setDestContainerListBiobank(String s) {this.destContainerListBiobank = s;}
    public void setVolumnWorking(int i) {this.volumnWorking = i;}
    public void setVolumnArchive(int i) {this.volumnArchive = i;}
    public void setVolumnBiobank(int i) {this.volumnBiobank = i;}
    public void setWorklistname(String s) {this.worklistname = s;}
    
    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        srcContainerList = null;
        destContainerListWorking = null;
        destContainerListArchive = null;
        destContainerListBiobank = null;
        volumnWorking = 3;
        volumnArchive = 3;
        volumnBiobank = 3;
    }  
    
    /**
     * Validate the properties that have been set from this HTTP request,
     * and return an <code>ActionErrors</code> object that encapsulates any
     * validation errors that have been found.  If no errors are found, return
     * <code>null</code> or an <code>ActionErrors</code> object with no
     * recorded error messages.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public ActionErrors validate(ActionMapping mapping,
                                 HttpServletRequest request) {

        ActionErrors errors = new ActionErrors();
        if ((srcContainerList == null) || (srcContainerList.trim().length() < 1))
            errors.add("srcContainerList", new ActionError("error.container.required"));
        if ((destContainerListWorking == null) || (destContainerListWorking.trim().length() < 1))
            errors.add("destContainerListWorking", new ActionError("error.container.required"));
        if ((destContainerListArchive == null) || (destContainerListArchive.trim().length() < 1))
            errors.add("destContainerListArchive", new ActionError("error.container.required"));
        if ((destContainerListBiobank == null) || (destContainerListBiobank.trim().length() < 1))
            errors.add("destContainerListBiobank", new ActionError("error.container.required"));
        if (volumnWorking <= 0)
            errors.add("volumnWorking", new ActionError("error.volumn.invalid", (new Integer(volumnWorking)).toString()));
        if (volumnArchive <= 0)
            errors.add("volumnArchive", new ActionError("error.volumn.invalid", (new Integer(volumnArchive)).toString()));
        if (volumnBiobank <= 0)
            errors.add("volumnBiobank", new ActionError("error.volumn.invalid", (new Integer(volumnBiobank)).toString()));
            
        return errors;
    }
}
