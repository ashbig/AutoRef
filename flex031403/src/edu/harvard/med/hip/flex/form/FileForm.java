/*
 * FileForm.java
 *
 * Created on August 1, 2001, 9:45 AM
 */

package edu.harvard.med.hip.flex.form;

import org.apache.struts.upload.*;
import org.apache.struts.action.*;


/**
 * This interface allows us to treat forms that upload files in a 
 * generic way.
 *
 * @author  $Author: jmunoz $
 * @version $Revision: 1.1 $
 */
public interface FileForm {
    
    /**
     * Accessor for the file.
     *
     * @return the FormFile of the gel image.
     */
    public FormFile getFormFile();
    
    /**
     * Mutator for the file.
     *
     * @param gelImage the <code>FormFile</code> that represents the gel image.
     */
    public void setFormFile(FormFile formFile);
}

