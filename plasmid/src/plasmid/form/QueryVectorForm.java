/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package plasmid.form;

import org.apache.struts.action.ActionForm;

/**
 *
 * @author DZuo
 */
public class QueryVectorForm extends ActionForm {
    private boolean PSI;

    public boolean isPSI() {
        return PSI;
    }

    public void setPSI(boolean PSI) {
        this.PSI = PSI;
    }
}
