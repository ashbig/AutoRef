/*
 * SelectProtocolAction.java
 *
 * Created on June 12, 2001, 4:10 PM
 */

package edu.harvard.med.hip.flex.action;

import java.util.LinkedList;
import java.util.Vector;
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

import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.form.*;
import edu.harvard.med.hip.flex.database.*;

/**
 *
 * @author  dzuo
 * @version 
 */
public class SelectProtocolAction extends FlexAction {

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
        int protocolid = ((CreateProcessPlateForm)form).getProtocol();
        Protocol protocol = new Protocol(protocolid);
        ContainerProcessQueue queue = new ContainerProcessQueue();
        try {
            LinkedList items = queue.getQueueItems(protocol);
            
            if(items.size() > 0) {
                request.setAttribute("queueItems", items);
            }

            // Get the location from the database.
            Vector locations = new Vector();
            String sql = "select * from containerlocation";
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            ResultSet rs = t.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("LOCATIONID");
                String type = rs.getString("LOCATIONTYPE");
                String description = rs.getString("LOCATIONDESCRIPTION");
                Location l = new Location(id, type, description);
                locations.addElement(l);
            }
            request.setAttribute("locations", locations);
            
            return (mapping.findForward("success"));
        } catch (FlexDatabaseException ex) {
            return (mapping.findForward("error"));
        } catch (SQLException ex) {
            return (mapping.findForward("error"));
        }
    }
}
