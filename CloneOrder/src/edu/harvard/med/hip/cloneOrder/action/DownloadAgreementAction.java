/*
 * DownloadAgreementAction.java
 *
 * Created on March 28, 2003, 4:39 PM
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
public class DownloadAgreementAction extends Action {
    
    public ActionForward perform(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException {
        
        
        ActionErrors errors = new ActionErrors();
        //response.setContentType("application/download");         
        response.setContentType("application/x-msword");
        response.setHeader("Content-Disposition", "attachment;filename=Agreement.doc"); 
        PrintWriter out = response.getWriter();
        InputStream in = null;        
        
        try {

            in = new BufferedInputStream(new FileInputStream("c://Clone Order//FLEXGeneMTA.doc") );
            //in = new BufferedInputStream(new FileInputStream(".//Agreement.doc") );
            
            int ch;
            while ((ch = in.read()) !=-1) {
                out.print((char)ch);
            }                

        } catch (IOException ex) {            
            return mapping.findForward("fail");
        } finally{
            if (in != null) 
                in.close();  // very important
            
            out.close();
        }
   
        return null;
    }
        
}
