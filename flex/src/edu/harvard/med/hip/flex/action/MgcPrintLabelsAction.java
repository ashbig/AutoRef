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
            request.setAttribute("printedLabels", "No MGC plates have been selected");
             return mapping.findForward("success");
        }
        
        String str = "<h3> Barcodes for the following plates have been printed: </h3>";
        for (int i = 0; i < labels.length; i++)
        {
            PrintLabel.execute(labels[i]);
            str += labels[i] + "\t";
            if (i != 0 && i % 10 == 0) str +="\n";
        }
        request.setAttribute("printedLabels", str);
        return mapping.findForward("success");
    }    

} // End class PrintLabelAction


