/*
 * DisplayBarcodeFileAction.java
 *
 * Created on November 14, 2001, 12:00 PM
 */

package edu.harvard.med.hip.flex.action;

import java.io.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.util.MessageResources;

import edu.harvard.med.hip.flex.form.GetResearcherBarcodeForm;

/**
 *
 * @author  dzuo
 * @version
 */
public class DisplayBarcodeFileAction extends ResearcherAction {
    
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
        String fileName = GetResearcherAction.BARCODEFILE;
        BufferedReader in = new BufferedReader(new FileReader(fileName));
        StringBuffer sb = new StringBuffer();
        
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            sb.append(inputLine+"\n");
        }
        in.close();
        request.setAttribute("barcodeFile", sb.toString());
        return (mapping.findForward("success"));
    }
}
