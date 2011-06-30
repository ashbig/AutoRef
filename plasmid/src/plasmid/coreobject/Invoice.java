/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plasmid.coreobject;

/**
 *
 * @author Dongmei
 */
public class Invoice {

    public static final String PAYMENTSTATUS_UNPAID = "Unpaid";
    public static final String PAYMENTSTATUS_PAID = "Paid";
    public static final String PAYMENTSTATUS_PARTIAL = "Partially paid";
    public static final String PAYMENTTYPE_CREDITCARD = "Credit Card";
    public static final String INVOICE_PREFIX = "DFHCC_";
    private int invoiceid;
    private String invoicenum;
    private String invoicedate;
    private double price;
    private double adjustment;
    private double payment;
    private String paymentstatus;
    private String paymenttype;
    private String accountnum;
    private String comments;
    private String reasonforadj;
    private String updatedby;
    private String updatedon;
    private int orderid;

    public Invoice(String invoicenum, String invoicedate, double price, double adjustment, double payment,
            String paymentstatus, String paymenttype, int orderid, String reason, String account) {
        this.invoicenum = invoicenum;
        this.invoicedate = invoicedate;
        this.price = price;
        this.adjustment = adjustment;
        this.payment = payment;
        this.paymentstatus = paymentstatus;
        this.paymenttype = paymenttype;
        this.orderid = orderid;
        this.reasonforadj = reason;
        this.accountnum = account;
    }

    public int getInvoiceid() {
        return invoiceid;
    }

    public void setInvoiceid(int invoiceid) {
        this.invoiceid = invoiceid;
    }

    public String getInvoicenum() {
        return invoicenum;
    }

    public void setInvoicenum(String invoicenum) {
        this.invoicenum = invoicenum;
    }

    public String getInvoicedate() {
        return invoicedate;
    }

    public void setInvoicedate(String invoicedate) {
        this.invoicedate = invoicedate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getAdjustment() {
        return adjustment;
    }

    public void setAdjustment(double adjustment) {
        this.adjustment = adjustment;
    }

    public double getPayment() {
        return payment;
    }

    public void setPayment(double payment) {
        this.payment = payment;
    }

    public String getPaymentstatus() {
        return paymentstatus;
    }

    public void setPaymentstatus(String paymentstatus) {
        this.paymentstatus = paymentstatus;
    }

    public String getPaymenttype() {
        return paymenttype;
    }

    public void setPaymenttype(String paymenttype) {
        this.paymenttype = paymenttype;
    }

    public String getAccountnum() {
        return accountnum;
    }

    public void setAccountnum(String accountnum) {
        this.accountnum = accountnum;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getUpdatedby() {
        return updatedby;
    }

    public void setUpdatedby(String updatedby) {
        this.updatedby = updatedby;
    }

    public String getUpdatedon() {
        return updatedon;
    }

    public void setUpdatedon(String updatedon) {
        this.updatedon = updatedon;
    }

    public int getOrderid() {
        return orderid;
    }

    public void setOrderid(int orderid) {
        this.orderid = orderid;
    }

    public String getReasonforadj() {
        return reasonforadj;
    }

    public void setReasonforadj(String reasonforadj) {
        this.reasonforadj = reasonforadj;
    }

    public String getAdjustmentString() {
        if (adjustment > 0.0) {
            return "$" + adjustment;
        } else {
            return "($" + Math.abs(adjustment) + ")";
        }
    }

    public double getDue() {
        return price + adjustment - payment;
    }

    public String getDueString() {
        double due = getDue();
        if (due > 0.0) {
            return "$" + due;
        } else {
            return "($" + Math.abs(due) + ")";
        }
    }
}