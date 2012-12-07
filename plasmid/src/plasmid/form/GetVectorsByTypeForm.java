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
public class GetVectorsByTypeForm extends ActionForm {
    private String type;
    
    /** Creates a new instance of ViewCartForm */
    public GetVectorsByTypeForm() {
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String s) {
        this.type = s;
    }
}

