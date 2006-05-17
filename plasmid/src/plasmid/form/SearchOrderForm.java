/*
 * SearchOrderForm.java
 *
 * Created on May 15, 2006, 1:24 PM
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
public class SearchOrderForm extends ActionForm {
    private String orderid;
    private String orderDateFrom;
    private String orderDateTo;
    private String shippingDateFrom;
    private String shippingDateTo;
    private String status;
    private String lastName;
    private String organization;
    private String sort;
    
    /** Creates a new instance of SearchOrderForm */
    public SearchOrderForm() {
    }
    
    public String getOrderid() {return orderid;}
    public String getOrderDateFrom() {return orderDateFrom;}
    public String getOrderDateTo() {return orderDateTo;}
    public String getShippingDateFrom() {return shippingDateFrom;}
    public String getShippingDateTo() {return shippingDateTo;}
    public String getStatus() {return status;}
    public String getLastName() {return lastName;}
    public String getOrganization() {return organization;}
    public String getSort() {return sort;}
    
    public void setOrderid(String s) {this.orderid = s;}
    public void setOrderDateFrom(String s) {this.orderDateFrom = s;}
    public void setOrderDateTo(String s) {this.orderDateTo = s;}
    public void setShippingDateFrom(String s) {this.shippingDateFrom = s;}
    public void setShippingDateTo(String s) {this.shippingDateTo = s;}
    public void setStatus(String s) {this.status = s;}
    public void setLastName(String s) {this.lastName = s;}
    public void setOrganization(String s) {this.organization = s;}
    public void setSort(String s) {this.sort = s;}
}
