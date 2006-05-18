//Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->
/*
 * FileForm.java
 *
 * Created on August 1, 2001, 9:45 AM
 */

package edu.harvard.med.hip.bec.form;

import org.apache.struts.upload.*;
import org.apache.struts.action.*;


/**
 * This interface allows us to treat forms that upload files in a 
 * generic way for a container.
 *
 * @author  $Author: Elena $
 * @version $Revision: 1.2 $
 */
public interface FileForm {
    
    /**
     * get this container this form is about
     *
     * @return Container associated with this form
     */
  //  public Container getContainer();
    
    /**
     * associates a container with this form.
     *
     * @param container Container to associate with.
     */
  //  public void setContainer(Container container);
    
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
    
    /**
     * Gets the localpath for this file
     * 
     * @return localpath
     */
    public String getLocalPath();
    
    /**
     * Get the type of the file
     *
     * @return file type
     */
    public String getFileType();
}

