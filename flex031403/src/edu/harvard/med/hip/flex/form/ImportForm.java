/*
 * ImportForm.java
 *
 * Created on October 22, 2001, 4:49 PM
 */

package edu.harvard.med.hip.flex.form;

import org.apache.struts.upload.*;
import org.apache.struts.action.*;

/**
 *
 * @author  dzuo
 * @version 
 */
public class ImportForm extends ActionForm {
    private FormFile sequenceFile;
    private FormFile nameFile;
    
    /**
     * Set the sequence file to the given value.
     *
     * @param sequenceFile The value to be set to.
     */
    public void setSequenceFile(FormFile sequenceFile) {
        this.sequenceFile = sequenceFile;
    }
    
    /**
     * Return the sequence file.
     *
     * @return The sequence file.
     */
    public FormFile getSequenceFile() {
        return sequenceFile;
    }
    
    /**
     * Set the name file to the given value.
     *
     * @param nameFile The value to be set to.
     */
    public void setNameFile(FormFile nameFile) {
        this.nameFile = nameFile;
    }
    
    /**
     * Return the name file.
     *
     * @return The name file.
     */
    public FormFile getNameFile() {
        return nameFile;
    }    
}