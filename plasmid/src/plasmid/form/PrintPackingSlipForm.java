/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plasmid.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Lab User
 */
public class PrintPackingSlipForm extends ActionForm {
    private String[] shipped;

    
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        shipped = new String[0];
    }

    /**
     * @return the shipped
     */
    public String[] getShipped() {
        return shipped;
    }

    /**
     * @param shipped the shipped to set
     */
    public void setShipped(String[] shipped) {
        this.shipped = shipped;
    }
}