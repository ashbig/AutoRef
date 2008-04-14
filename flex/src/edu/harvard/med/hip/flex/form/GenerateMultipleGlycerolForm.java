/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author DZuo
 */
public class GenerateMultipleGlycerolForm extends ActionForm {
    private String srclabels;
    private int num;
    private String researcherBarcode;
    
    public String getSrclabels() {
        return srclabels;
    }

    public void setSrclabels(String srclabels) {
        this.srclabels = srclabels;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
        
    public ActionErrors validate(ActionMapping mapping,
    HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if(srclabels == null || srclabels.trim().length() < 1) {
            errors.add("srclabels", new ActionError("error.plate.required"));
        }
        
        if(num<1) {
            errors.add("num", new ActionError("error.invalid.number", num));  
        }
        
        if(researcherBarcode == null || researcherBarcode.trim().length() < 1) {
            errors.add("researcherBarcode", new ActionError("error.researcherBarcode.required"));
        }
        return errors;        
    }

    public String getResearcherBarcode() {
        return researcherBarcode;
    }

    public void setResearcherBarcode(String researcherBarcode) {
        this.researcherBarcode = researcherBarcode;
    }
}
