/*
 * File : PrintLabelAction.java
 * Classes : PrintLabelAction
 *
 * Description :
 *
 * This action will print the label it is passed
 *
 *
 * Author : Juan Munoz (jmunoz@3rdmill.com)
 * 
 * See COPYRIGHT file for copyright information
 *
 *
 * The following information is used by CVS
 * $Revision: 1.1 $
 * $Date: 2001-07-26 22:07:13 $
 * $Author: jmunoz $
 *
 ******************************************************************************
 *
 * Revision history (Started on July 26, 2001) :
 *
 *    Add entries here when updating the code. Remember to date and insert
 *    your 3 letters initials.
 *
 *    Jul-27-2001 : JMM - Class created. 
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

import javax.servlet.*;
import javax.servlet.http.*;


import org.apache.struts.action.*;

import edu.harvard.med.hip.flex.form.*;
import edu.harvard.med.hip.flex.util.*;

/**
 * Action to print the barcode.
 *
 *
 * @author     $Author: jmunoz $
 * @version    $Revision: 1.1 $ $Date: 2001-07-26 22:07:13 $
 */

public class PrintLabelAction extends ResearcherAction{

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
    public ActionForward flexPerform(ActionMapping mapping,
    ActionForm form, HttpServletRequest request, 
    HttpServletResponse response) throws ServletException, IOException {
        PrintLabelForm printForm = (PrintLabelForm) form;
        String label = printForm.getLabel();
        PrintLabel.execute(label);
        return mapping.findForward("success");
    }    

} // End class PrintLabelAction


/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
*/
