/*
 * LogoutAction.java
 *
 * Created on March 29, 2003, 6:57 PM
 */

package edu.harvard.med.hip.cloneOrder.action;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;
/**
 *
 * @author  hweng
 */
public class LogoutAction extends Action{
    
    public ActionForward perform(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException {
        
        request.getSession().invalidate();
        return mapping.findForward("success");
    }    

    
}
