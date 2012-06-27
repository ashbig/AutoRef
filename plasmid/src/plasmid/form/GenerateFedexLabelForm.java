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
public class GenerateFedexLabelForm extends ActionForm {
    private String orderid;

    /**
     * @return the orderid
     */
    public String getOrderid() {
        return orderid;
    }

    /**
     * @param orderid the orderid to set
     */
    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }
    
}
