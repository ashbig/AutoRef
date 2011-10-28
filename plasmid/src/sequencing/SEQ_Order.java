/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sequencing;

/**
 *
 * @author Dongmei
 */
public class SEQ_Order {
    public static final String ISHARVARD_Y = "Y";
    public static final String ISHARVARD_N = "N";
    public static final String PAYMENTMETHOD_CREDIT = "CreditCard";
    public static final String PAYMENTMETHOD_PO = "PO";
    
    private int orderid;
    private String orderdate;
    private String billingemail;
    private String billingaddress;
    private String pifirstname;
    private String pilastname;
    private String piemail;
    private String username;
    private String ponumber;
    private int samples;
    private double cost;
    private String paymentmethod;
    private String service;
    private String institution;
    private String affiliation;
    private String isharvard;
    private int userid;
    private int invoiceid;

    public SEQ_Order(int orderid, String orderdate, String billingemail, String billingaddress,
            String pifirstname, String pilastname, String piemail, String username, String ponumber,
            int samples, double cost, String paymentmethod, String service, String institution,
            String affiliation, String isharvard, int userid) {
        this.orderid = orderid;
        this.orderdate = orderdate;
        this.billingemail = billingemail;
        this.billingaddress = billingaddress;
        this.pifirstname = pifirstname;
        this.pilastname = pilastname;
        this.piemail = piemail;
        this.username = username;
        this.ponumber = ponumber;
        this.samples = samples;
        this.cost = cost;
        this.paymentmethod = paymentmethod;
        this.service = service;
        this.institution = institution;
        this.affiliation = affiliation;
        this.isharvard = isharvard;
        this.userid = userid;
    }
    
    public String getPiname() {
        return getPilastname()+", "+getPifirstname();
    }
    
    public int getOrderid() {
        return orderid;
    }

    public void setOrderid(int orderid) {
        this.orderid = orderid;
    }

    public String getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(String orderdate) {
        this.orderdate = orderdate;
    }

    public String getBillingemail() {
        return billingemail;
    }

    public void setBillingemail(String billingemail) {
        this.billingemail = billingemail;
    }

    public String getBillingaddress() {
        return billingaddress;
    }

    public void setBillingaddress(String billingaddress) {
        this.billingaddress = billingaddress;
    }

    public String getPifirstname() {
        return pifirstname;
    }

    public void setPifirstname(String pifirstname) {
        this.pifirstname = pifirstname;
    }

    public String getPilastname() {
        return pilastname;
    }

    public void setPilastname(String pilastname) {
        this.pilastname = pilastname;
    }

    public String getPiemail() {
        return piemail;
    }

    public void setPiemail(String piemail) {
        this.piemail = piemail;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPonumber() {
        return ponumber;
    }

    public void setPonumber(String ponumber) {
        this.ponumber = ponumber;
    }

    public int getSamples() {
        return samples;
    }

    public void setSamples(int samples) {
        this.samples = samples;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getPaymentmethod() {
        return paymentmethod;
    }

    public void setPaymentmethod(String paymentmethod) {
        this.paymentmethod = paymentmethod;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public String getIsharvard() {
        return isharvard;
    }

    public void setIsharvard(String isharvard) {
        this.isharvard = isharvard;
    }

    public int getInvoiceid() {
        return invoiceid;
    }

    public void setInvoiceid(int invoiceid) {
        this.invoiceid = invoiceid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }
}
