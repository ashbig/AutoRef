/*
 * DisplayExpSampleByPageForm.java
 *
 * Created on March 23, 2003, 10:59 PM
 */

package edu.harvard.med.hip.flex.form;
import javax.servlet.http.*;
import javax.servlet.*;
import org.apache.struts.action.*;
/**
 *
 * @author hweng
 */
public class DisplayExpSampleByPageForm extends ActionForm {
       
    protected int curr_page;
    protected String sample_type;
    protected String sample_status;
    protected String display_format;
    protected int page_size;
    protected int total_pages;
    
    public int getCurr_page(){return curr_page;}
    public String getSample_type(){return sample_type;}
    public String getSample_status(){return sample_status;}
    public String getDisplay_format(){return display_format;}
    public int getPage_size(){return page_size;}
    public int getTotal_pages(){return total_pages;}
    
    
    public void setCurr_page(int curr_page){this.curr_page = curr_page;}
    public void setSample_type(String sample_type){this.sample_type = sample_type;}
    public void setSample_status(String sample_status){this.sample_status = sample_status;}
    public void setDisplay_format(String display_format){this.display_format = display_format;}
    public void setPage_size(int page_size){this.page_size = page_size;}
    public void setTotal_pages(int total_pages){this.total_pages = total_pages;}
    
    
}
