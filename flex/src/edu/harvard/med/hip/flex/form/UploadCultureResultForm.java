/*
 * UploadCultureResultForm.java
 *
 * Created on February 4, 2005, 3:35 PM
 */

package edu.harvard.med.hip.flex.form;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import org.apache.struts.upload.*;

import edu.harvard.med.hip.flex.process.Researcher;
import edu.harvard.med.hip.flex.file.*;

/**
 *
 * @author  DZuo
 */
public class UploadCultureResultForm extends GelResultsForm {
    private Researcher researcher;
    
    public Researcher getResearcher() {return researcher;}
    
    public void setResearcher(Researcher r) {this.researcher=r;}
    
    public ActionErrors validate(ActionMapping mapping,
    HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
       
        return errors;       
    }
    
    /**
     * Gets the localpath for this file
     *
     * @return localpath
     */
    public String getLocalPath() {
        return FileRepository.CULTURE_LOCAL_PATH;
    }    
    
    /**
     * Get the type of the file
     *
     * @return file type
     */
    public String getFileType() {
        return FileReference.CULTURE_TYPE;
    }    
} 


