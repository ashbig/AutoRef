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
 * $Revision: 1.1 $
 * $Date: 2001-06-15 19:20:51 $
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

import javax.servlet.*;
import javax.servlet.http.*;

import edu.harvard.med.hip.flex.form.*;

import org.apache.struts.action.*;
/**
 * Action to enter details about a plate transfer into the database.
 *
 *
 * @author     $Author: dongmei_zuo $
 * @version    $Revision: 1.1 $ $Date: 2001-06-15 19:20:51 $
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
        TransformDetailsForm transForm = (TransformDetailsForm) form;
        for(int i = 0; transForm != null &&i <transForm.size() ;i++) {
            System.out.println(i+ ": status: " + transForm.getStatus(i));
            System.out.println(i+ ": result: " + transForm.getResult(i));
        }
        return null;
    } //end flexPerform
    
    
} // End class EnterTransformDetailsAction


/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
*/
