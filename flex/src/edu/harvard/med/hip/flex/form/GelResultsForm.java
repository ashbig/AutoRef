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
 * $Revision: 1.3 $
 * $Date: 2001-06-20 20:02:01 $
 * $Author: dongmei_zuo $
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
 * @author     $Author: dongmei_zuo $
 * @version    $Revision: 1.3 $ $Date: 2001-06-20 20:02:01 $
 */

public class GelResultsForm extends ContainerResultsForm {
    
    
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
     * Accessor for the gel image.
     *
     * @return the FormFile of the gel image.
     */
    public FormFile getGelImage() {
        return this.gelImage;
    }
    
    
    /**
     * Mutator for the gel image.
     *
     * @param gelImage the <code>FormFile</code> that represents the gel image.
     */
    public void setGelImage(FormFile gelImage) {
        this.gelImage = gelImage;
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
        if(this.gelImage == null || this.gelImage.getFileName() == null || 
            this.gelImage.getFileName().equals("") || this.gelImage.getFileSize() == 0 ) {
            errors.add("gelImage",
            new ActionError("error.gel.file.required"));
            
        }
        
        return errors;
        
    }
    
} // End class GelDetailsForm


/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */
