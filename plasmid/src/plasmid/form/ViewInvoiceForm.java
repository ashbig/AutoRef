/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package plasmid.form;

import org.apache.struts.action.ActionForm;

/**
 *
 * @author Dongmei
 */
public class ViewInvoiceForm extends ActionForm {
    private int invoiceid;
    private int orderid;
    private int isdownload;

    public int getInvoiceid() {
        return invoiceid;
    }

    public void setInvoiceid(int invoiceid) {
        this.invoiceid = invoiceid;
    }

    public int getOrderid() {
        return orderid;
    }

    public void setOrderid(int orderid) {
        this.orderid = orderid;
    }

    public int getIsdownload() {
        return isdownload;
    }

    public void setIsdownload(int isdownload) {
        this.isdownload = isdownload;
    }
}
