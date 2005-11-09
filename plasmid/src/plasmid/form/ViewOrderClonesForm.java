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
    private String type;
    private String collectionName = "";
    
    /** Creates a new instance of ViewOrderClonesForm */
    public ViewOrderClonesForm() {
    }
    
    public void setOrderid(int id) {this.orderid = id;}
    public void setType(String s) {this.type = s;}
    public void setCollectionName(String s) {this.collectionName = s;}
    
    public int getOrderid() {return orderid;}
    public String getType() {return type;}
    public String getCollectionName() {return collectionName;}
}
