/*
 * AddResearcherAction.java
 *
 * Add a researcher to the database.
 *
 * Created on October 30, 2001, 10:37 AM
 */

package edu.harvard.med.hip.flex.action;

import edu.harvard.med.hip.flex.form.AddResearcherForm;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.process.Researcher;

import java.io.*;
import java.sql.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import org.apache.struts.upload.*;

/**
 *
 * @author  dzuo
 * @version
 */
public class AddResearcherAction extends AdminAction {
    
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
        String researcherName = ((AddResearcherForm)form).getResearcherName();
        String researcherBarcode = ((AddResearcherForm)form).getResearcherBarcode();
        
        // Check to see if researcher name already exists in the database.
        try {
            if(Researcher.nameExists(researcherName)) {
                errors.add("researcherName", new ActionError("error.researcherName.exists", researcherName));
                saveErrors(request,errors);
                return new ActionForward(mapping.getInput());
            }
        } catch (FlexDatabaseException ex) {
            return mapping.findForward("fail");
        }
        
        // Check to see if the barcode has been used.
        try {
            if(Researcher.isValid(researcherBarcode)) {
                errors.add("researcherBarcode", new ActionError("error.researcherBarcode.exists", researcherBarcode));
                saveErrors(request,errors);
                return new ActionForward(mapping.getInput());
            }
        } catch (FlexDatabaseException ex) {
            return mapping.findForward("fail");
        }                
                
        //add researcher to the database.
        DatabaseTransaction t = null;
        Connection conn = null;
        request.setAttribute("researcherName", researcherName);
        request.setAttribute("researcherBarcode", researcherBarcode);
        
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            if(Researcher.addResearcher(researcherName, researcherBarcode, conn)) {
                DatabaseTransaction.commit(conn);
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
