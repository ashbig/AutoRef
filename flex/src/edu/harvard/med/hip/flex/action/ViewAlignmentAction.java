/*
 * ViewAlignmentAction.java
 *
 * Created on June 6, 2001, 5:47 PM
 *
 * Display the blast alignment.
 */

package edu.harvard.med.hip.flex.action;

import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import java.util.LinkedList;
import java.sql.*;
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

import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.util.*;
import edu.harvard.med.hip.flex.form.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.user.*;
import edu.harvard.med.hip.flex.Constants;

/**
 *
 * @author  Dongmei Zuo
 * @version 
 */
public class ViewAlignmentAction extends FlexAction {

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
        java.util.Date d = new java.util.Date();
        java.text.SimpleDateFormat f = new java.text.SimpleDateFormat("MM_dd_yyyy");
        String fileName = request.getParameter("gi")+"_" + f.format(d)+".out";

        BufferedReader in = new BufferedReader(new FileReader("/tmp/"+fileName));
        StringBuffer sb = new StringBuffer();
        boolean start = false;
            
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            if(start) {
                sb.append(inputLine+"\n");
                continue;
            }
                
            if(inputLine.indexOf(">")==0) {
                sb.append(inputLine+"\n");
                start = true;
            }
        }
        in.close();
        request.setAttribute("alignment", sb.toString());
        return (mapping.findForward("success"));
    }
}
