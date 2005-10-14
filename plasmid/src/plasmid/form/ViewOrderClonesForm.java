/*
 * ViewOrderClonesForm.java
 *
 * Created on June 9, 2005, 3:55 PM
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
public class ViewOrderClonesForm extends ActionForm {
    private int orderid;
    
    /** Creates a new instance of ViewOrderClonesForm */
    public ViewOrderClonesForm() {
    }
    
    public void setOrderid(int id) {this.orderid = id;}
    
    public int getOrderid() {return orderid;}
}
