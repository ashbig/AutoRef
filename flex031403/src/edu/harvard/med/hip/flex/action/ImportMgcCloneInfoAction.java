/*
 * ImportMgcCloneAction.java
 *
 * Created on 5/13/02
 */

package edu.harvard.med.hip.flex.action;

import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import org.apache.struts.upload.*;

import edu.harvard.med.hip.flex.form.*;
import edu.harvard.med.hip.flex.infoimport.MgcMasterListImporter;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.workflow.*;

/**
 *
 * @author  htaycher
 * @version
 */
public class ImportMgcCloneInfoAction extends AdminAction {    
    /**
     * Does the real work for the perform method which must be overriden by the
     * Child classes.
     *
     * @param mapping The ActionMapping used to select this instance
     * @param actionForm The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */
    public ActionForward flexPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActionErrors errors = new ActionErrors();
        
        FormFile mgcCloneFile = ((MgcCloneInfoImportForm)form).getMgcCloneFile();
        String fileName =  ((MgcCloneInfoImportForm)form).getFileName();
        InputStream input = null;
        try {
            input = mgcCloneFile.getInputStream();
        } catch (FileNotFoundException ex) {
            errors.add("mgcCloneFile", new ActionError("flex.infoimport.file", ex.getMessage()));
            saveErrors(request,errors);
            return new ActionForward(mapping.getInput());
        } catch (IOException ex) {
            errors.add("mgcCloneFile", new ActionError("flex.infoimport.file", ex.getMessage()));
            saveErrors(request,errors);
            return new ActionForward(mapping.getInput());
        }
        
        MgcMasterListImporter importer = new MgcMasterListImporter();
        importer.importMgcCloneInfoIntoDB(input, fileName) ;
        return mapping.findForward("proccessing");
    }
}


