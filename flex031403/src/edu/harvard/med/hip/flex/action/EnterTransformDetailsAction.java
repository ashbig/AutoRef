/*
 * File : EnterTransformDetailsAction.java
 * Classes : EnterTransformDetailsAction
 *
 * Description :
 *
 *      Action to enter details about a transform details into the database
 *
 *
 * Author : Juan Munoz (jmunoz@3rdmill.com)
 *
 * See COPYRIGHT file for copyright information
 *
 *
 * The following information is used by CVS
 * $Revision: 1.3 $
 * $Date: 2001-06-18 15:07:27 $
 * $Author: dongmei_zuo $
 *
 ******************************************************************************
 *
 * Revision history (Started on June 15, 2001) :
 *
 *    Add entries here when updating the code. Remember to date and insert
 *    your 3 letters initials.
 *
 *    Jun-15-2001 : JMM - Class created.
 *
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */


package edu.harvard.med.hip.flex.action;

import java.io.*;
import java.util.*;
import java.sql.*;

import javax.servlet.*;
import javax.servlet.http.*;

import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.form.*;

import org.apache.struts.action.*;
/**
 * Action to enter details about a plate transfer into the database.
 *
 *
 * @author     $Author: dongmei_zuo $
 * @version    $Revision: 1.3 $ $Date: 2001-06-18 15:07:27 $
 */

public class EnterTransformDetailsAction extends ResearcherAction {
    
    /**
     * Default Constructor
     */
    public EnterTransformDetailsAction() {
    }
    
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
    public ActionForward flexPerform(ActionMapping mapping, ActionForm form,
    HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        ActionForward retForward = null;
        TransformDetailsForm transForm = (TransformDetailsForm) form;
        Vector samples = transForm.getContainer().getSamples();
        
        Connection conn = null;
        
        
        
        try {
            // Crete the protocol for the transform plates
            Protocol transformProtocol = 
                new Protocol("enter transformation results");
            
            
            
            conn = DatabaseTransaction.getInstance().requestConnection();
            for(int i = 0; transForm != null &&i <transForm.size() ;i++) {
                Sample curSample = (Sample)samples.get(i);
                curSample.setStatus(transForm.getStatus(i));
                curSample.setResult(transForm.getResult(i));
                curSample.update(conn);
            }
            DatabaseTransaction.commit(conn);
        } catch (FlexDatabaseException fde) {
            retForward = mapping.findForward("error");
            request.setAttribute(Action.EXCEPTION_KEY, fde);
            DatabaseTransaction.rollback(conn);
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
        
        return retForward;
    } //end flexPerform
    
    
} // End class EnterTransformDetailsAction


/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */
