/*
 * CreateProcessPlateAction.java
 *
 * Created on June 12, 2001, 1:49 PM
 *
 * This class gets all the process protocols related to generating process
 * plates from the beans.
 */

package edu.harvard.med.hip.flex.action;

import java.util.Hashtable;
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

import edu.harvard.med.hip.flex.database.*;

/**
 *
 * @author  dzuo
 * @version 
 */
public class CreateProcessPlateAction extends InternalFlexAction {

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
        String sql = "select * from processprotocol where processname like 'generate % plates'";
        Hashtable protocol = new Hashtable();
        try {        
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            ResultSet rs = t.executeQuery(sql);
            while (rs.next()) {
                int protocolid = rs.getInt("PROTOCOLID");
                String processname = rs.getString("PROCESSNAME");
                protocol.put(new Integer(protocolid), processname);
            }
            request.setAttribute("protocol", protocol);
            return (mapping.findForward("success"));
        } catch (FlexDatabaseException ex) {
             return (mapping.findForward("error"));
        } catch (SQLException ex) {
             return (mapping.findForward("error"));
        }
    }
}
