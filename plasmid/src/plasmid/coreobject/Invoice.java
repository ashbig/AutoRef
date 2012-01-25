/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plasmid.coreobject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import sequencing.SEQ_Order;

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
    private String piname;
    private String institution;
    private CloneOrder order;
    private boolean selected;
    
    //for sequencing invoice
    private String pilastname;
    private String isharvard;
    private List<SEQ_Order> seqorder;

    public Invoice() {
        seqorder = new ArrayList<SEQ_Order>();
    }
    
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
        seqorder = new ArrayList<SEQ_Order>();
    }
    
    public String getPiemail() {
        if(seqorder==null || seqorder.isEmpty())
            return "";
        SEQ_Order o = (SEQ_Order)seqorder.get(0);
        String s = o.getPiemail();
        if(s.equals("0"))
            return "";
        return s;
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

    public String getPriceString() {
        DecimalFormat df = new DecimalFormat("#.00");
        return "$" + Double.valueOf(df.format(price));
    }

    public String getPaymentString() {
        DecimalFormat df = new DecimalFormat("#.00");
        
        if(payment>0.0) {
            return "($" + Double.valueOf(df.format(payment)) + ")";
        }
        return "$"+Double.valueOf(df.format(payment));
    }

    public String getAdjustmentString() {
        DecimalFormat df = new DecimalFormat("#.00");
        
        if (adjustment >= 0.0) {
            return "$" + Double.valueOf(df.format(adjustment));
        } else {
            return "($" + Double.valueOf(df.format(Math.abs(adjustment))) + ")";
        }
    }

    public double getDue() {
        return price + adjustment - payment;
    }

    public String getDueString() {
        DecimalFormat df = new DecimalFormat("#.00");
        double due = getDue();
        if (due >= 0.0) {
            return "$" + Double.valueOf(df.format(due));
        } else {
            return "($" + Double.valueOf(df.format(Math.abs(due))) + ")";
        }
    }

    public String getPiname() {
        return piname;
    }

    public void setPiname(String piname) {
        this.piname = piname;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public CloneOrder getOrder() {
        return order;
    }

    public void setOrder(CloneOrder order) {
        this.order = order;
    }
    
    public static String getPaymentStatus(double price, double adjustment, double payment) {
        double due = price + adjustment - payment;
        if(due>0) {
            if(due<price || payment>0) {
                return Invoice.PAYMENTSTATUS_PARTIAL;
            } else {
                return Invoice.PAYMENTSTATUS_UNPAID;
            }
        } else {
            return Invoice.PAYMENTSTATUS_PAID;
        }
    }

    public List<SEQ_Order> getSeqorder() {
        return seqorder;
    }

    public void setSeqorder(List<SEQ_Order> seqorder) {
        this.seqorder = seqorder;
    }
    
    public void addSeqOrder(SEQ_Order order) {
        this.seqorder.add(order);
    }

    public String getPilastname() {
        return pilastname;
    }

    public void setPilastname(String pilastname) {
        this.pilastname = pilastname;
    }

    public String getIsharvard() {
        return isharvard;
    }

    public void setIsharvard(String isharvard) {
        this.isharvard = isharvard;
    }
}