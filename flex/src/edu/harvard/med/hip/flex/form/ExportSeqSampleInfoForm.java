/*
 * ExportSeqSampleInfoForm.java
 *
 * Created on March 23, 2003, 10:16 PM
 */

package edu.harvard.med.hip.flex.form;
import javax.servlet.http.*;
import javax.servlet.*;
import org.apache.struts.action.*;
/**
 *
 * @author  hweng
 */
public class ExportSeqSampleInfoForm extends ActionForm {
    
    private String sample_type;
    private String sample_status;
            
    public String getSample_type(){return sample_type;}
    public String getSample_status(){return sample_status;}
    public void setSample_type(String sample_type){this.sample_type = sample_type;}
    public void setSample_status(String sample_status){this.sample_status = sample_status;}
}
