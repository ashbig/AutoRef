/*
 * PrintBarcodeAction.java
 *
 * Print the barcode.
 *
 * Created on June 26, 2001, 3:42 PM
 */

package edu.harvard.med.hip.flex.action;

import java.util.LinkedList;
import java.util.MissingResourceException;
import java.util.Vector;
import java.util.Enumeration;
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
import edu.harvard.med.hip.flex.form.CreateProcessPlateForm;
import edu.harvard.med.hip.flex.core.Container;
import edu.harvard.med.hip.flex.util.PrintLabel;
import edu.harvard.med.hip.flex.database.*;

/**
 *
 * @author  dzuo
 * @version
 */
public class PrintBarcodeAction extends ResearcherAction
{
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
    throws ServletException, IOException
    {
        Vector newContainers = (Vector)request.getSession().getAttribute("EnterSourcePlateAction.newContainers");
        for(int i=0; i<newContainers.size(); i++)
        {
            Container c = (Container)newContainers.elementAt(i);
            String status = PrintLabel.execute(c.getLabel());
            //System.out.println("Printing barcode: "+status);
        }
        
        // for mgc plates: update location
         if (request.getParameter("update_location") != null)
        {
            Connection conn = null;
            Vector containers = (Vector)request.getSession().getAttribute("EnterSourcePlateAction.newContainers");
            try
            {
                DatabaseTransaction t = DatabaseTransaction.getInstance();
                conn = t.requestConnection();
                
                // update the location of the containers.
                for(int count =0; count < containers.size(); count++)
                {
                    Container container = (Container)containers.elementAt(count);
                    container.updateLocation(container.getLocation().getId(), conn);
                }
                // Commit the changes to the database.
                DatabaseTransaction.commit(conn);
            }
            catch (Exception ex)
            {
                System.out.println(ex.getMessage());
                DatabaseTransaction.rollback(conn);
                request.setAttribute(Action.EXCEPTION_KEY, ex);
                return (mapping.findForward("error"));
            } 
            finally
            {
                DatabaseTransaction.closeConnection(conn);
            }
        }
        
        return (mapping.findForward("success"));
    }
}
