/*
 * GetNametypeAction.java
 *
 * Created on October 29, 2001, 5:17 PM
 */

package edu.harvard.med.hip.flex.action;

import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import org.apache.struts.upload.*;

import edu.harvard.med.hip.flex.core.Nametype;

/**
 *
 * @author  dzuo
 * @version 
 */
public class GetNametypeAction extends AdminAction {
    
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
        Vector nametypes = Nametype.getAllNametypes();
        request.setAttribute("nametypes", nametypes);
        return mapping.findForward("success");
    }
}

