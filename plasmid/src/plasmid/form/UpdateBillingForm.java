/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plasmid.form;

import org.apache.struts.action.ActionForm;

/**
 *
 * @author dongmei
 */
public class UpdateBillingForm extends ActionForm {
    private int invoiceid;
    private int orderid;
    private String billingaddress;
    private String billingemail;

    /**
     * @return the orderid
     */
    public int getOrderid() {
        return orderid;
    }

    /**
     * @param orderid the orderid to set
     */
    public void setOrderid(int orderid) {
        this.orderid = orderid;
    }

    /**
     * @return the billingaddress
     */
    public String getBillingaddress() {
        return billingaddress;
    }

    /**
     * @param billingaddress the billingaddress to set
     */
    public void setBillingaddress(String billingaddress) {
        this.billingaddress = billingaddress;
    }

    /**
     * @return the billingemail
     */
    public String getBillingemail() {
        return billingemail;
    }

    /**
     * @param billingemail the billingemail to set
     */
    public void setBillingemail(String billingemail) {
        this.billingemail = billingemail;
    }

    /**
     * @return the invoiceid
     */
    public int getInvoiceid() {
        return invoiceid;
    }

    /**
     * @param invoiceid the invoiceid to set
     */
    public void setInvoiceid(int invoiceid) {
        this.invoiceid = invoiceid;
    }
}
