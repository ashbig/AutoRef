/*
 * SelectSucCriteriaForm.java
 *
 * Created on February 11, 2003, 3:08 PM
 */

package edu.harvard.med.hip.flex.form;
import javax.servlet.http.*;
import javax.servlet.*;
import org.apache.struts.action.*;
/**
 *
 * @author  hweng
 */
public class SuccessCriteriaForm extends ProcessForSucRateForm{
    
    protected String[] pcr_succ_criteria = {"Correct", "Multiple w/ Correct", "No visible band with cloning attempt", "Succeeded"};
    protected int agar_plate_succ_criteria = 1;
    protected int culture_plate_succ_criteria = 1;
    protected String initial_date_from;
    protected String initial_date_to;
    
    public String[] getPcr_succ_criteria(){
        return pcr_succ_criteria;
    }
    public int getAgar_plate_succ_criteria(){
        return agar_plate_succ_criteria;
    }
    public int getCulture_plate_succ_criteria(){
        return culture_plate_succ_criteria;
    }
    public String getInitial_date_from(){
        return initial_date_from;
    }
    public String getInitial_date_to(){
        return initial_date_to;
    }    
        
    public void setPcr_succ_criteria(String[] pcr_succ_criteria){
        this.pcr_succ_criteria = pcr_succ_criteria;
    }        
    public void setAgar_plate_succ_criteria(int n){
        this.agar_plate_succ_criteria = n;
    }
    public void setCulture_plate_succ_criteria(int n){
        this.culture_plate_succ_criteria = n;
    }
    public void setInitial_date_from(String initial_date_from){
        this.initial_date_from = initial_date_from;
    }
    public void setInitial_date_to(String initial_date_to){
        this.initial_date_to = initial_date_to;
    }    

    
    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        return errors;
    }
    
}
