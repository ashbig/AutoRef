/*
 * ImportSequenceAction.java
 *
 * Created on October 22, 2001, 4:55 PM
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
import edu.harvard.med.hip.flex.infoimport.*;
import edu.harvard.med.hip.flex.database.*;

/**
 *
 * @author  dzuo
 * @version
 */
public class ImportSequenceAction extends WorkflowAction {
    
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
        
        FormFile sequenceFile = ((ImportForm)form).getSequenceFile();
        FormFile nameFile = ((ImportForm)form).getNameFile();
        InputStream sequenceInput;
        InputStream nameInput;
        
        try {
            sequenceInput = sequenceFile.getInputStream();
            nameInput = nameFile.getInputStream();
        } catch (FileNotFoundException ex) {
            errors.add("sequenceFile", new ActionError("flex.infoimport.file", ex.getMessage()));
            saveErrors(request,errors);
            return new ActionForward(mapping.getInput());
        } catch (IOException ex) {
            errors.add("sequenceFile", new ActionError("flex.infoimport.file", ex.getMessage()));
            saveErrors(request,errors);
            return new ActionForward(mapping.getInput());            
        }
        
        SequenceImporter importer = new SequenceImporter();
        DatabaseTransaction t = null;
        Connection conn = null;
        
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            
            if(importer.performImport(sequenceInput, nameInput, conn)) {
                DatabaseTransaction.commit(conn);
                Vector results = importer.getResults();
                request.setAttribute("ImportSequenceAction.totalCount", new Integer(importer.getTotalCount()));
                request.setAttribute("ImportSequenceAction.successfulCount", new Integer(importer.getSuccessfulCount()));
                request.setAttribute("ImportSequenceAction.failCount", new Integer(importer.getFailedCount()));
                
                if(importer.getFailedCount() != 0) {
                    request.setAttribute("ImportSequenceAction.importResult", results);
                }
                
                return mapping.findForward("success");
            } else {
                DatabaseTransaction.rollback(conn);
                return mapping.findForward("fail");
            }
        } catch (FlexDatabaseException ex) {
            DatabaseTransaction.rollback(conn);
            return mapping.findForward("fail");
        } catch (Exception ex) {
            DatabaseTransaction.rollback(conn);
            request.setAttribute(Action.EXCEPTION_KEY, ex);
            return mapping.findForward("error");         
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }        
    } 
}
