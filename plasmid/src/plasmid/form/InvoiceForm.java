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
public class InvoiceForm extends ActionForm {
    private int invoiceid;
    private String paymentstatus;
    private String accountnum;
    private double adjustment;
    private double payment;
    private String comments;
    private boolean returnToList;
    
    public InvoiceForm() {this.returnToList = false;}
    
    public int getInvoiceid() {
        return invoiceid;
    }

    public void setInvoiceid(int invoiceid) {
        this.invoiceid = invoiceid;
    }

    public String getPaymentstatus() {
        return paymentstatus;
    }

    public void setPaymentstatus(String paymentstatus) {
        this.paymentstatus = paymentstatus;
    }

    public String getAccountnum() {
        return accountnum;
    }

    public void setAccountnum(String accountnum) {
        this.accountnum = accountnum;
    }

    public double getPayment() {
        return payment;
    }

    public void setPayment(double payment) {
        this.payment = payment;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public boolean isReturnToList() {
        return returnToList;
    }

    public void setReturnToList(boolean returnToList) {
        this.returnToList = returnToList;
    }

    public double getAdjustment() {
        return adjustment;
    }

    public void setAdjustment(double adjustment) {
        this.adjustment = adjustment;
    }
}
