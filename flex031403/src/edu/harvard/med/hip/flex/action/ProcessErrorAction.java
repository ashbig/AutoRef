/*
 * File : ProcessErrorAction.java
 * Classes : ProcessErrorAction
 *
 * Description :
 *
 *  This Action is responsible for processing of errors in the FlexApplication.
 *
 *  Whenever an exception is caught, controll should be forwarded to this
 *  action.
 *
 *
 * Author : Juan Munoz (jmunoz@3rdmill.com)
 *
 * See COPYRIGHT file for copyright information
 *
 *
 * The following information is used by CVS
 * $Revision: 1.1 $
 * $Date: 2001-06-05 12:58:51 $
 * $Author: dongmei_zuo $
 *
 ******************************************************************************
 *
 * Revision history (Started on June 5, 2001) :
 *
 *    Add entries here when updating the code. Remember to date and insert
 *    your 3 letters initials.
 *
 *    Jun-05-2001 : JMM - Class Created
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


/**
 * The action for handling all flex error encountered.
 *
 * Whenever an exception is encountered, controll should be forwarded
 * to this action to process the exception.
 *
 * @author     $Author: dongmei_zuo $
 * @version    $Revision: 1.1 $ $Date: 2001-06-05 12:58:51 $
 */

public class ProcessErrorAction extends Action {
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
    public ActionForward perform(ActionMapping mapping,
    ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        ActionForward retForward = null;
        // get the errors to log
        ActionErrors errors =
        (ActionErrors)request.getAttribute(Action.ERROR_KEY);
        Throwable throwable =
        (Throwable)request.getAttribute(Action.EXCEPTION_KEY);
        
        // log each error
        Iterator propIter = errors.properties();
        while(propIter.hasNext()) {
            String curProp = (String)propIter.next();
            Iterator errorIter = errors.get(curProp);
            while(errorIter.hasNext()) {
                String errorString = (String)errorIter.next();
                // do loging instead
                System.out.println(curProp + ": " + errorString);
            }
        }
        
        // log the exception if any
        System.out.println("Unkown exception");
        System.out.println(throwable.getMessage());
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        System.out.println(sw);
        
        // log the environment
        // start with the application level env.
        ServletContext application = servlet.getServletContext();
        Enumeration appAttribEnum = application.getAttributeNames();
        System.out.println("Application variables");
        while(appAttribEnum.hasMoreElements()) {
            String key = (String)appAttribEnum.nextElement();
            String value = (String)application.getAttribute(key);
            System.out.println(key + " " + value);
        }
        
        // now log session variables
        HttpSession session = request.getSession();
        Enumeration sessionAttribEnum = session.getAttributeNames();
        System.out.println("Session Variables");
        while(sessionAttribEnum.hasMoreElements()) {
            String key = (String)sessionAttribEnum.nextElement();
            String value = (String)session.getAttribute(key);
            System.out.println(key + " " + value);
        }
        
        
        // now log request level env.
        Enumeration requestAttribEnum = request.getAttributeNames();
        System.out.println("request variables");
        while(requestAttribEnum.hasMoreElements()) {
            String key = (String)requestAttribEnum.nextElement();
            String value = (String)request.getAttribute(key);
            System.out.println(key + " " + value);
        }
        
        // now log request level parameters
        Enumeration requestParamEnum = request.getParameterNames();
        System.out.println("request parameters");
        while(requestParamEnum.hasMoreElements()) {
            String key = (String)requestParamEnum.nextElement();
            String value = (String)request.getParameter(key);
            System.out.println(key + " " + value);
        }
        
        retForward = mapping.findForward("displayErrors");
        return retForward;
    } // end perform()
    
    
    
} // End class ProcessError


/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */
