/*
 * ImportMgcCloneAction.java
 *
 * Created on 5/13/02
 */

package edu.harvard.med.hip.flex.action;

import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import org.apache.struts.upload.*;

import edu.harvard.med.hip.flex.form.*;
import edu.harvard.med.hip.flex.infoimport.MgcMasterListImporter;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.workflow.*;
import edu.harvard.med.hip.flex.user.*;
import edu.harvard.med.hip.flex.Constants;

/**
 *
 * @author  htaycher
 * @version
 */
public class ImportMgcCloneInfoAction extends AdminAction
{
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
   // private static boolean isBusy = false;
    
    public ActionForward flexPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        ActionErrors errors = new ActionErrors();
        /*
        synchronized (this)
        {
            if ( isBusy)
            {
                request.setAttribute("message","Application is busy. Somebody else is uploading the MGC master list. Please, come later.");
                return mapping.findForward("proccessing");
            }
            else
            {
                isBusy = true;
            }
        }
        */
        FormFile mgcCloneFile = ((MgcCloneInfoImportForm)form).getMgcCloneFile();
        String fileName =  ((MgcCloneInfoImportForm)form).getFileName();
        InputStream input = null;
        try
        {
            input = mgcCloneFile.getInputStream();
        } catch (FileNotFoundException ex)
        {
            errors.add("mgcCloneFile", new ActionError("flex.infoimport.file", ex.getMessage()));
            saveErrors(request,errors);
            return new ActionForward(mapping.getInput());
        } catch (IOException ex)
        {
            errors.add("mgcCloneFile", new ActionError("flex.infoimport.file", ex.getMessage()));
            saveErrors(request,errors);
            return new ActionForward(mapping.getInput());
        }
        
        String username = ((User)request.getSession().getAttribute(Constants.USER_KEY)).getUsername();
        ImportInformationRunner import_info = new ImportInformationRunner(input, fileName, username);
        Thread t = new Thread(import_info);
        t.start();
        request.setAttribute("message",
        "Information is uploading. It can take up to an hour based on number of clones. The e-mail notification will be sent to you upon completion.");
        return mapping.findForward("proccessing");
        
    }
    
    class ImportInformationRunner implements Runnable
    {
        private InputStream m_Input = null;
        private String      m_filename= null;
        private String      m_username = null;
        public ImportInformationRunner(InputStream in, String fn, String username)
        {
            m_Input = in;
            m_filename = fn;
            m_username = username;
        }
        public void run()
        {
            MgcMasterListImporter importer = new MgcMasterListImporter(m_username);
            importer.importMgcCloneInfoIntoDB(m_Input, m_filename) ;
           // isBusy = false;
        }
    }
    
}


