//Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->
/*
 * File : ProcessErrorAction.java
 * Classes : ProcessErrorAction
 *
 * Description :
 *
 *  This Action is responsible for processing of errors in the Application.
 *
 *  Whenever an exception is caught, controll should be forwarded to this
 *  action.
 *
 *
 
 *
 */



package edu.harvard.med.hip.bec.action;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import edu.harvard.med.hip.bec.Constants;

import org.apache.struts.action.*;



/**
 * The action for handling all bec error encountered.
 *
 * Whenever an exception is encountered, controll should be forwarded
 * to this action to process the exception.
 *
 * @author     $Author: Elena $
 * @version    $Revision: 1.2 $ $Date: 2006-05-18 15:40:00 $
 */

public class ProcessErrorAction extends Action
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
    public ActionForward perform(ActionMapping mapping,
    ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
        ActionForward retForward = null;
        // get the errors to log
        ActionErrors errors =
        (ActionErrors)request.getAttribute(Action.ERROR_KEY);
        
        // make sure errors isn't null
        if(errors == null)
        {
            errors = new ActionErrors();
        }
        
        // buffer to store the log message
        StringBuffer logMessage = new StringBuffer();
        
        Throwable throwable =
        (Throwable)request.getAttribute(Action.EXCEPTION_KEY);
        
        // log each error
        logMessage.append("The following errors have occured: \n");
        Iterator propIter = errors.properties();
        while(propIter.hasNext())
        {
            String curProp = (String)propIter.next();
            Iterator errorIter = errors.get(curProp);
            while(errorIter.hasNext())
            {
                Object errorObj = errorIter.next();
                
                logMessage.append(curProp + ": " + errorObj + "\n");
            }
        }
        
        // log the exception if any
        if(throwable != null)
        {
            logMessage.append("Unkown exception: ");
            logMessage.append(throwable.getMessage());
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            throwable.printStackTrace(pw);
            logMessage.append(sw + "\n");
        }
        
        
        logMessage.append("\n-----Environment-----\n");
        // log the connected user
        logMessage.append("\n-Connected User: " +
        request.getSession().getAttribute(Constants.USER_KEY) + "\n");
        // log the environment
        // start with the application level env.
        ServletContext application = servlet.getServletContext();
        Enumeration appAttribEnum = application.getAttributeNames();
        logMessage.append("\n-Application attributes\n");
        while(appAttribEnum.hasMoreElements())
        {
            String key = (String)appAttribEnum.nextElement();
            Object value = application.getAttribute(key);
            logMessage.append("\t" + key + " = " + value + "\n");
        }
        
        // now log session variables
        HttpSession session = request.getSession();
        Enumeration sessionAttribEnum = session.getAttributeNames();
        logMessage.append("\n-Session attributes\n");
        while(sessionAttribEnum.hasMoreElements())
        {
            String key = (String)sessionAttribEnum.nextElement();
            Object value = session.getAttribute(key);
            logMessage.append("\t"+key + " = " + value + "\n");
        }
        
        
        // now log request level env.
        Enumeration requestAttribEnum = request.getAttributeNames();
        logMessage.append("\n-Request attributes\n");
        while(requestAttribEnum.hasMoreElements())
        {
            String key = (String)requestAttribEnum.nextElement();
            Object value = request.getAttribute(key);
            logMessage.append("\t"+key + " = " + value + "\n");
        }
        
        // now log request level parameters
        Enumeration requestParamEnum = request.getParameterNames();
        logMessage.append("\n-Request parameters\n");
        while(requestParamEnum.hasMoreElements())
        {
            String key = (String)requestParamEnum.nextElement();
            String value = (String)request.getParameter(key);
            logMessage.append("\t"+key + " = " + value + "\n");
        }
        
        
        //System.out.println("#######Exception###########");
        servlet.log(logMessage.toString());
        //System.out.println("###########################");
        
        //servlet.log(logMessage);
        retForward = mapping.findForward("displayError");
        return retForward;
    } // end perform()
    
    
    
} // End class ProcessError


/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */
