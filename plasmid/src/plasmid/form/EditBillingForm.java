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
public class EditBillingForm extends ActionForm {
    private int orderid;
    private String billingto;
    private String billingorganization;
    private String billingaddressline1;
    private String billingaddressline2;
    private String billingcity;
    private String billingstate;
    private String billingzipcode;
    private String billingcountry;
    private String billingphone;
    private String billingfax;
    private String billingemail;

    public int getOrderid() {
        return orderid;
    }

    public void setOrderid(int orderid) {
        this.orderid = orderid;
    }

    public String getBillingto() {
        return billingto;
    }

    public void setBillingto(String billingto) {
        this.billingto = billingto;
    }

    public String getBillingorganization() {
        return billingorganization;
    }

    public void setBillingorganization(String billingorganization) {
        this.billingorganization = billingorganization;
    }

    public String getBillingaddressline1() {
        return billingaddressline1;
    }

    public void setBillingaddressline1(String billingaddressline1) {
        this.billingaddressline1 = billingaddressline1;
    }

    public String getBillingaddressline2() {
        return billingaddressline2;
    }

    public void setBillingaddressline2(String billingaddressline2) {
        this.billingaddressline2 = billingaddressline2;
    }

    public String getBillingcity() {
        return billingcity;
    }

    public void setBillingcity(String billingcity) {
        this.billingcity = billingcity;
    }

    public String getBillingstate() {
        return billingstate;
    }

    public void setBillingstate(String billingstate) {
        this.billingstate = billingstate;
    }

    public String getBillingzipcode() {
        return billingzipcode;
    }

    public void setBillingzipcode(String billingzipcode) {
        this.billingzipcode = billingzipcode;
    }

    public String getBillingcountry() {
        return billingcountry;
    }

    public void setBillingcountry(String billingcountry) {
        this.billingcountry = billingcountry;
    }

    public String getBillingphone() {
        return billingphone;
    }

    public void setBillingphone(String billingphone) {
        this.billingphone = billingphone;
    }

    public String getBillingfax() {
        return billingfax;
    }

    public void setBillingfax(String billingfax) {
        this.billingfax = billingfax;
    }

    public String getBillingemail() {
        return billingemail;
    }

    public void setBillingemail(String billingemail) {
        this.billingemail = billingemail;
    }
}
