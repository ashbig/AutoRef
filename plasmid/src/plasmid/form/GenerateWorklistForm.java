/*
 * GenerateWorklistForm.java
 *
 * Created on August 10, 2005, 9:54 AM
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
public class GenerateWorklistForm extends ActionForm {
    private String processname;
    private List protocols;
    private String protocol;
    private String srcContainerList;
    private String destContainerList;
    private int volumn;
    
    /** Creates a new instance of GenerateWorklistForm */
    public GenerateWorklistForm() {
    }
        
    public String getProcessname() { return processname;}
    public List getProtocols() {return protocols;}
    public String getProtocol() {return protocol;}
    public String getSrcContainerList() {return srcContainerList;}
    public String getDestContainerList() {return destContainerList;}
    public int getVolumn() {return volumn;}
    
    public void setProcessname(String s) {this.processname = s;}
    public void setProtocols(List l) {this.protocols = l;}
    public void setProtocol(String s) {this.protocol = s;}
    public void setSrcContainerList(String s) {this.srcContainerList = s;}
    public void setDestContainerList(String s) {this.destContainerList = s;}
    public void setVolumn(int i) {this.volumn = i;}
    
    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        processname = Process.TRANSFORMATION;
        srcContainerList = null;
        destContainerList = null;
        volumn = 0;
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
        if ((destContainerList == null) || (destContainerList.trim().length() < 1))
            errors.add("destContainerList", new ActionError("error.container.required"));
        if (volumn <= 0)
            errors.add("volumn", new ActionError("error.volumn.invalid", (new Integer(volumn)).toString()));
            
        return errors;
    }
}
