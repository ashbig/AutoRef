/*
 * DisplayExpSampleForm.java
 *
 * Created on February 19, 2003, 4:40 PM
 */

package edu.harvard.med.hip.flex.form;
import org.apache.struts.action.*;
/**
 *
 * @author  hweng
 */
public class DisplayExpSampleForm extends ActionForm{
    
    protected int step;
    protected String sampleStatus;
    
    public int getStep(){
        return step;
    }
    public String getSampleStatus(){
        return sampleStatus;
    }
    public void setStep(int process){
        this.step = process;
    }
    public void setSampleStatus(String sampleStatus){
        this.sampleStatus = sampleStatus;
    }
        

}
