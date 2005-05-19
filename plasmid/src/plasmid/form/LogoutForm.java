/*
 * LogoutForm.java
 *
 * Created on May 18, 2005, 4:02 PM
 */

package plasmid.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author  DZuo
 */
public class LogoutForm extends ActionForm {
    private String confirm;
    
    /** Creates a new instance of LogoutForm */
    public LogoutForm() {
    }
    
    public String getConfirm() {return confirm;}
    public void setConfirm(String s) {this.confirm = s;}
}
