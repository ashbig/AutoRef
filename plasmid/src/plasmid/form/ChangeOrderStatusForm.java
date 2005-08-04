/*
 * ChangeOrderStatusForm.java
 *
 * Created on June 28, 2005, 7:40 AM
 */

package plasmid.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import java.util.*;
import plasmid.coreobject.CloneOrder;

/**
 *
 * @author  DZuo
 */
public class ChangeOrderStatusForm extends ActionForm {
    private List status;
    private List orderid;
    
    /** Creates a new instance of ChangeOrderStatusForm */
    public ChangeOrderStatusForm() {
    }
    
    public List getStatusList() {return status;}
    public List getOrderidList() {return orderid;}
    
    public String getStatus(int i) {return (String)status.get(i);}
    public int getOrderid(int i) {return Integer.parseInt((String)orderid.get(i));}
    
    public void setStatus(int i, String s) {this.status.set(i, s);}
    public void setOrderid(int i, String id) {this.orderid.set(i, id);}
    
    public void initiateLists(List orders) {
        status = new ArrayList();
        orderid = new ArrayList();
        
        for(int i=0; i<orders.size(); i++) {
            CloneOrder order = (CloneOrder)orders.get(i);
            status.add(order.getStatus());
            orderid.add((new Integer(order.getOrderid())).toString());
        }
    }
}