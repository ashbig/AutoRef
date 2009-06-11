/*
 * MgcPrintLabelsAction.java
 *
 * Created on May 29, 2002, 6:00 PM
 *htaycher
 */



package edu.harvard.med.hip.flex.action;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;


import org.apache.struts.action.*;

import edu.harvard.med.hip.flex.form.*;
import edu.harvard.med.hip.flex.util.*;

/**
 * Action to print the collection of barcodes.
 *
 
 */

public class MgcPrintLabelsAction extends ResearcherAction{

    
    public ActionForward flexPerform(ActionMapping mapping,
    ActionForm form, HttpServletRequest request, 
    HttpServletResponse response) throws ServletException, IOException {
       
        
        String[] labels = ((MgcPrintNewLabelsForm)form).getChkPrint();
        
        if (labels == null || labels.length == 0) 
        {
            request.setAttribute("message", "No plates have been selected");
             return mapping.findForward("success");
        }
        
        request.setAttribute("message","Barcodes for the following plates have been printed:");
        ArrayList printedLabels = new ArrayList(); 
        for (int i = 0; i < labels.length; i++)
        {
            PrintLabel.execute(labels[i]);
            printedLabels.add( labels[i] );
        }
        request.setAttribute("printedLabels", printedLabels);
        return mapping.findForward("success");
    }    

} // End class PrintLabelAction


