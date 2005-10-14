/*
 * LabelPlatesForm.java
 *
 * Created on July 29, 2005, 11:09 AM
 */

package plasmid.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import plasmid.coreobject.Container;

/**
 *
 * @author  DZuo
 */
public class LabelPlatesForm extends ActionForm {
    private int number;
    private String suffix;
    private String type;
    
    /** Creates a new instance of LabelPlates */
    public LabelPlatesForm() {
    }
    
    public int getNumber() {return number;}
    public void setNumber(int n) {this.number = n;}
    
    public String getSuffix() {
        if("crarc".equals(suffix))
            return "CrArc";
        if("send".equals(suffix))
            return "Send";
        return null;
    }
    public void setSuffix(String s) {this.suffix = s;}
        
    public String getType() {return type;}
    public void setType(String s) {this.type = s;}
    
    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.number = 1;
        this.suffix = "none";
        this.type = Container.COSTAR_FLT;
    }  
}
