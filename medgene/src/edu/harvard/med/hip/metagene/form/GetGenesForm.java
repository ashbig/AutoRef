/*
 * GetGenesForm.java
 *
 * Created on January 17, 2002, 2:55 PM
 */

package edu.harvard.med.hip.metagene.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author  dzuo
 * @version 
 */
public class GetGenesForm extends ActionForm {
    private int diseaseTerm;
    private int stat;
    private int number;

    /** Creates new GetGenesForm */
    public GetGenesForm() {
    }
    
    public int getDiseaseTerm() {
        return diseaseTerm;
    }
    
    public void setDiseaseTerm(int diseaseTerm) {
        this.diseaseTerm = diseaseTerm;
    }
    
    public int getStat() {
        return stat;
    }
    
    public void setStat(int stat) {
        this.stat = stat;
    }
    
    public int getNumber() {
        return number;
    }
    
    public void setNumber(int number) {
        this.number = number;
    }
}
