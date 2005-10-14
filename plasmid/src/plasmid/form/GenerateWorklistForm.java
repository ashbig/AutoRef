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
    
    /** Creates a new instance of GenerateWorklistForm */
    public GenerateWorklistForm() {
    }
        
    public String getProcessname() { return processname;}
    public List getProtocols() {return protocols;}
    public String getProtocol() {return protocol;}
    
    public void setProcessname(String s) {this.processname = s;}
    public void setProtocols(List l) {this.protocols = l;}
    public void setProtocol(String s) {this.protocol = s;}
    
    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        processname = Process.TRANSFORMATION;
    }  
}
