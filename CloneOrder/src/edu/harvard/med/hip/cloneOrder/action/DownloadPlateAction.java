/*
 * DownloadPlateAction.java
 *
 * Created on June 16, 2003, 10:42 AM
 */

package edu.harvard.med.hip.cloneOrder.action;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;

/**
 *
 * @author  yanhuihu
 */
public class DownloadPlateAction extends Action  {
 
    public ActionForward perform(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException {
        
        
        ActionErrors errors = new ActionErrors();
        //response.setContentType("application/download");         
        response.setContentType("application/x-msexcel");
        response.setHeader("Content-Disposition", "attachment;filename=KinasePlate.xls"); 
        PrintWriter out = response.getWriter();
        InputStream in = null;        
        
        try {

            in = new BufferedInputStream(new FileInputStream("D://Clone Order//Kinase.xls") );
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
