/*
 * ViewBlastOutputAction.java
 *
 * Created on February 12, 2004, 12:41 PM
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

import edu.harvard.med.hip.flex.form.QueryFlexForm;

/**
 *
 * @author  DZuo
 */
public class ViewBlastOutputAction extends FlexAction {
    
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
        String fileName = ((QueryFlexForm)form).getOutputFile();
        
        try {
            BufferedReader in = new BufferedReader(new FileReader(fileName));
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
        } catch (Exception ex) {
            request.setAttribute("error", ex.getMessage());
            return (mapping.findForward("fail"));
        }
    }
}
