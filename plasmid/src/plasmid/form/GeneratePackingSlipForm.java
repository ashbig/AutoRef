/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plasmid.form;

import org.apache.struts.action.ActionForm;

/**
 *
 * @author Lab User
 */
public class GeneratePackingSlipForm extends ActionForm {
    private int orderid;
    private String orderDate;
    private String ponumber;
    private String email;
    private String phone;

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
     * @return the orderDate
     */
    public String getOrderDate() {
        return orderDate;
    }

    /**
     * @param orderDate the orderDate to set
     */
    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    /**
     * @return the ponumber
     */
    public String getPonumber() {
        return ponumber;
    }

    /**
     * @param ponumber the ponumber to set
     */
    public void setPonumber(String ponumber) {
        this.ponumber = ponumber;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }
}
