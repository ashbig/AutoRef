/*
 * SelectProcessSucRate.java
 *
 * Created on January 29, 2003, 1:45 PM
 */

package edu.harvard.med.hip.flex.form;
import javax.servlet.http.*;
import javax.servlet.*;
import org.apache.struts.action.*;


/**
 *
 * @author  hweng
 */
public class ProcessForSucRateForm extends ProjectWorkflowProtocolForm{
    
    protected String cloneFormat;
    protected String initial_month_from;
    protected String initial_month_to;
    protected String initial_day_from;
    protected String initial_day_to;
    protected String initial_year_from;
    protected String initial_year_to;
    
    public String getCloneFormat(){
        return cloneFormat;
    }
    public String getInitial_month_to(){
        return initial_month_to;
    }
    public String getInitial_month_from(){
        return initial_month_from;
    }        
    public String getInitial_year_to(){
        return initial_year_to;
    }
    public String getInitial_year_from(){
        return initial_year_from;
    }                
    public String getInitial_day_to(){
        return initial_day_to;
    }    
    public String getInitial_day_from(){
        return initial_day_from;
    }    
    
    public void setCloneFormat(String format){
        this.cloneFormat = format;
    }
    public void setInitial_month_to(String initial_month_to){
        this.initial_month_to = initial_month_to;
    }
    public void setInitial_month_from(String initial_month_from){
        this.initial_month_from = initial_month_from;
    }        
    public void setInitial_year_to(String initial_year_to){
        this.initial_year_to = initial_year_to;
    }
    public void setInitial_year_from(String initial_year_from){
        this.initial_year_from = initial_year_from;
    }                
    public void setInitial_day_to(String initial_day_to){
        this.initial_day_to = initial_day_to;
    }    
    public void setInitial_day_from(String initial_day_from){
        this.initial_day_from = initial_day_from;
    }    
    
    /**
     * Validate the properties that have been set from this HTTP request,
     * and return an <code>ActionErrors</code> object that encapsulates any
     * validation errors that have been found.  If no errors are found, return
     * <code>null</code> or an <code>ActionErrors</code> object with no
     * recorded error messages.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

        ActionErrors errors = new ActionErrors();
        if( (initial_month_from.equalsIgnoreCase("month") || 
             initial_day_from.equalsIgnoreCase("day") ||
             initial_year_from.equalsIgnoreCase("year"))
             &&
            !(initial_month_from.equalsIgnoreCase("month") && 
             initial_day_from.equalsIgnoreCase("day") &&
             initial_year_from.equalsIgnoreCase("year"))      )
        {
             errors.add("date", new ActionError("error.date.required"));
             return errors;
        }
    
        if( (initial_month_to.equalsIgnoreCase("month") || 
             initial_day_to.equalsIgnoreCase("day") ||
             initial_year_to.equalsIgnoreCase("year"))
             &&
            !(initial_month_to.equalsIgnoreCase("month") && 
             initial_day_to.equalsIgnoreCase("day") &&
             initial_year_to.equalsIgnoreCase("year"))      )
        {
             errors.add("date", new ActionError("error.date.required"));
             return errors;
        }    

        return errors;
    }

    
}
