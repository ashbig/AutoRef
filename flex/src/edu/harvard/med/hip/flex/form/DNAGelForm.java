/*
 * File : DNAGelForm.java
 * Classes : DNAGelForm
 *
 * Description :
 *
 *  Allows you to enter results for a DNAGel.
 *
 *
 * Author : Juan Munoz (jmunoz@3rdmill.com)
 * 
 * See COPYRIGHT file for copyright information
 *
 *
 * The following information is used by CVS
 * $Revision: 1.1 $
 * $Date: 2001-08-01 16:22:58 $
 * $Author: jmunoz $
 *
 ******************************************************************************
 *
 * Revision history (Started on August 1, 2001) :
 *
 *    Add entries here when updating the code. Remember to date and insert
 *    your 3 letters initials.
 *
 *    Aug-01-2001 : JMM - Class Created
 *
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
*/


package edu.harvard.med.hip.flex.form;

import org.apache.struts.action.*;
import org.apache.struts.upload.*;

import edu.harvard.med.hip.flex.core.*;

/**
 * Holds the information about a DNA Gel result
 *
 *
 * @author     $Author: jmunoz $
 * @version    $Revision: 1.1 $ $Date: 2001-08-01 16:22:58 $
 */

public class DNAGelForm extends ResultForm implements FileForm{

    // the file for the dna gel image
    private FormFile dnaGel;
    
    
    /**
     * Constructor that takes in a container
     *
     * @param container Container this form is associated with
     */
    public DNAGelForm(Container container) {
       super(container);
    }
   
    /**
     * Accessor for the file.
     *
     * @return the FormFile of the gel image.
     */
    public FormFile getFormFile() {
        return this.dnaGel;
    }    

    /**
     * Mutator for the file.
     *
     * @param gelImage the <code>FormFile</code> that represents the gel image.
     */
    public void setFormFile(FormFile formFile) {
        this.dnaGel = formFile;
    }
    
  
    
  
    
} // End class DNAGelForm


/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
*/
