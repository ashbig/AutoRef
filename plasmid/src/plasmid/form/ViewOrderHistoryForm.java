/*
 * ViewOrderHistoryForm.java
 *
 * Created on June 8, 2005, 11:18 AM
 */

package plasmid.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import plasmid.coreobject.CloneOrder;

/**
 *
 * @author  DZuo
 */
public class ViewOrderHistoryForm extends ActionForm {
    private String status = CloneOrder.ALL;
    private int start = 0;
    
    /** Creates a new instance of ViewOrderHistoryForm */
    public ViewOrderHistoryForm() {
    }
      
    public String getStatus() {return status;}
    public void setStatus(String s) {this.status = s;}
    
    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
       // status = CloneOrder.ALL;
        status = CloneOrder.PENDING;
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
        //if(ponumber == null || ponumber.trim().length()<1)
        //    errors.add("ponumber", new ActionError("error.ponumber.required"));

        return errors;     
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }
}
