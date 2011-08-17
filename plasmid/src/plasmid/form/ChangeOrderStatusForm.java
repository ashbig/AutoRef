/*
 * ChangeOrderStatusForm.java
 *
 * Created on June 28, 2005, 7:40 AM
 */

package plasmid.form;

import javax.servlet.http.HttpServletRequest;
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
    private String orderListButton;
    private List oldStatus;
    private String inprocessOrderidString;
    private boolean isDownload;
    
    /** Creates a new instance of ChangeOrderStatusForm */
    public ChangeOrderStatusForm() {
    }
    
    public List getStatusList() {return status;}
    public List getOrderidList() {return orderid;}
    public String getOrderListButton() {return orderListButton;}
    
    public String getStatus(int i) {return (String)status.get(i);}
    public int getOrderid(int i) {return Integer.parseInt((String)orderid.get(i));}
    
    public void setStatus(int i, String s) {this.status.set(i, s);}
    public void setOrderid(int i, String id) {this.orderid.set(i, id);}
    public void setOrderListButton(String s) {this.orderListButton = s;}
    
    public void initiateLists(List orders) {
        status = new ArrayList();
        orderid = new ArrayList();
        oldStatus = new ArrayList();
        
        for(int i=0; i<orders.size(); i++) {
            CloneOrder order = (CloneOrder)orders.get(i);
            status.add(order.getStatus());
            oldStatus.add(order.getStatus());
            orderid.add((new Integer(order.getOrderid())).toString());
        }
    }    
        
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        orderListButton = null;
    }

    public List getOldStatus() {
        return oldStatus;
    }

    public String getInprocessOrderidString() {
        return inprocessOrderidString;
    }

    public void setInprocessOrderidString(String inprocessOrderidString) {
        this.inprocessOrderidString = inprocessOrderidString;
    }

    public boolean isIsDownload() {
        return isDownload;
    }

    public void setIsDownload(boolean isDownload) {
        this.isDownload = isDownload;
    }
}
