/*
 * ExpressionResultHistoryForm.java
 *
 * Created on September 11, 2003, 3:01 PM
 */

package edu.harvard.med.hip.flex.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author  DZuo
 */
public class ExpressionResultHistoryForm extends ActionForm {
    private int sampleid;
    private String resulttype;
    
    /** Creates a new instance of ExpressionResultHistoryForm */
    public ExpressionResultHistoryForm() {
    }
    
    public int getSampleid() {return sampleid;}
    public String getResulttype() {return resulttype;}
    
    public void setSampleid(int sampleid) {this.sampleid=sampleid;}
    public void setResulttype(String resulttype) {this.resulttype=resulttype;}
}
