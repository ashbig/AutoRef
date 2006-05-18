//Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->
/*
 * ExportDataForPolymorphismFinder.java
 *
 * Created on August 1, 2005, 2:09 PM
 ** This class exports the  sequences from the database to the
 * FASTA format file. It calls a stand alone program to do the task,
 * only because the program uses the ACE package which only works
 * with under the web server environment.
 *
 */

package edu.harvard.med.hip.bec.action;

import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import  edu.harvard.med.hip.bec.programs.polymorphism_finder.*;
/**
 *
 * @author  htaycher
 */
public class ExportDataForPolymorphismFinderAction extends Action {
    
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
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException 
    {
        String output_dir = null;
        String param = request.getParameter("param");
       
        if (  param.equalsIgnoreCase("submit"))
        {
           PolymFinderDataSender.generateSequencingDataFiles(null);
        }
        else
        {
            ResultParser.parseResultFileAndSubmitInformation(output_dir);
        }
        return (mapping.findForward("success"));
    }
}