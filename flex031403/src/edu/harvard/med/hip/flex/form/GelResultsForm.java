/*
 * File : GelDetailsForm.java
 * Classes : GelDetailsForm
 *
 * Description :
 *
 *      Form to capture input about a gel result.
 *
 *
 * Author : Juan Munoz (jmunoz@3rdmill.com)
 *
 * See COPYRIGHT file for copyright information
 *
 *
 * The following information is used by CVS
 * $Revision: 1.5 $
 * $Date: 2001-08-01 13:57:31 $
 * $Author: jmunoz $
 *
 ******************************************************************************
 *
 * Revision history (Started on June 20, 2001) :
 *
 *    Add entries here when updating the code. Remember to date and insert
 *    your 3 letters initials.
 *
 *    Jun-20-2001 : JMM - Class created.
 *
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */


package edu.harvard.med.hip.flex.form;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import org.apache.struts.upload.*;

import edu.harvard.med.hip.flex.core.*;

/**
 * Form for entering gel results.
 *
 *
 * @author     $Author: jmunoz $
 * @version    $Revision: 1.5 $ $Date: 2001-08-01 13:57:31 $
 */

public class GelResultsForm extends ContainerResultsForm implements FileForm {
    
    
    /**
     * The gel image file.
     */
    private FormFile gelImage;
    
    /**
     * Constructor that populates its self from a container
     * 
     * @param container the container to get info from
     */
    public GelResultsForm(Container container) {
        super(container);
    }
    
    /**
     * Default Constructor
     */
    public GelResultsForm() {
        super();
    }
    
   
    
    
    /**
     * Mutator for the gel image.
     *
     * @param gelImage the <code>FormFile</code> that represents the gel image.
     */
    public void setFormFile(FormFile formFile) {
        this.gelImage = formFile;
    }
    
    /**
     * Accessor for the gel image.
     *
     * @return the FormFile of the gel image.
     */
    public FormFile getFormFile() {
        return this.gelImage;
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
    public ActionErrors validate(ActionMapping mapping,
    HttpServletRequest request) {
        ActionErrors errors = super.validate(mapping,request);
        if(errors==null) {
            errors = new ActionErrors();
        }
       
        
        return errors;
        
    }
    
    
    
} // End class GelDetailsForm


/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */
