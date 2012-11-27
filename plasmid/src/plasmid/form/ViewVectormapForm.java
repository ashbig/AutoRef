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
public class ViewVectormapForm extends ActionForm {
    private String vectorname;

    /**
     * @return the vectorname
     */
    public String getVectorname() {
        return vectorname;
    }

    /**
     * @param vectorname the vectorname to set
     */
    public void setVectorname(String vectorname) {
        this.vectorname = vectorname;
    }
    
}
