/*
 * PrintBarcodeAction.java
 *
 * Print the barcode.
 *
 * Created on June 26, 2001, 3:42 PM
 */

package edu.harvard.med.hip.flex.action;

import java.util.*;
import java.sql.*;
import java.io.IOException;
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
import edu.harvard.med.hip.flex.form.EnterOligoPlateLocationForm;
import edu.harvard.med.hip.flex.core.Container;
import edu.harvard.med.hip.flex.util.PrintLabel;

/**
 *
 * @author  wendy mar
 * @version
 */
public class PrintOligoPlateBarcodeAction extends ResearcherAction {
    /**
     * Process the specified HTTP request, and create the corresponding HTTP
     * response (or forward to another web component that will create it).
     * Return an <code>ActionForward</code> instance describing where and how
     * control should be forwarded, or <code>null</code> if the response has
     * already been completed.
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
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException {
        //Vector newContainers = (Vector)request.getSession().getAttribute("EnterSourcePlateAction.newContainers");
        LinkedList containerList = (LinkedList)request.getSession().getAttribute("containerList");
        ListIterator iter = containerList.listIterator();
        
        while (iter.hasNext()) {
            Container container = (Container)iter.next();
            System.out.println("Printing barcode: "+ container.getLabel());
            String label = PrintLabel.execute(container.getLabel());
            
        } //while
        
        return (mapping.findForward("success"));
    }
}

