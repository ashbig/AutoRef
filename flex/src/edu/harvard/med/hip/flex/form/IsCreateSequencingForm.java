/*
 * IsCreateSequencingForm.java
 *
 * Created on January 26, 2004, 1:54 PM
 */

package edu.harvard.med.hip.flex.form;

/**
 *
 * @author  DZuo
 */
public class IsCreateSequencingForm extends ProjectWorkflowForm {
    protected String isSeqPlates;
    
    /** Creates a new instance of IsCreateSequencingForm */
    public IsCreateSequencingForm() {
    }
    
    public String getIsSeqPlates() {return isSeqPlates;}
    public void setIsSeqPlates(String s) {this.isSeqPlates = s;}
}
