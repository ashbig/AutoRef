/*
 * SubmitDataFileForm.java
 *
 * Created on June 19, 2003, 12:50 PM
 */

package edu.harvard.med.hip.bec.form;

import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import org.apache.struts.upload.*;


import edu.harvard.med.hip.bec.database.*;


/**
 *
 * @author  htaycher
 */
public class SubmitDataFileForm extends Seq_GetSpecForm

{
   
    protected FormFile      m_file = null;
   
   
    public void         setFileName(FormFile requestFile)   {      m_file = requestFile;    }
    public FormFile     getFileName()                          {        return m_file;    }
   
    
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
    HttpServletRequest request)
    {
      
        
        ActionErrors errors = new ActionErrors();
        if ((m_file.getFileName() == null) || (m_file.getFileName().trim().length() < 1))
            errors.add("submitionFile", new ActionError("submition file required"));
        
        
        return errors;
    }
    
    


}
