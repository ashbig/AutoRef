/*
 * SaveContainerInfoAction.java
 *
 * Created on February 5, 2002, 1:07 PM
 */

package edu.harvard.med.hip.flex.action;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;

import edu.harvard.med.hip.flex.form.SaveContainerInfoForm;
import edu.harvard.med.hip.flex.export.ContainerInfoExporter;

/**
 *
 * @author  dzuo
 * @version
 */
public class SaveContainerInfoAction extends ResearcherAction {
    
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
    public ActionForward flexPerform(ActionMapping mapping, ActionForm form,
    HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        ActionErrors errors = new ActionErrors();
        
        int id = ((SaveContainerInfoForm)form).getId();
        boolean sampleid = ((SaveContainerInfoForm)form).getSampleid();
        boolean type = ((SaveContainerInfoForm)form).getType();
        boolean position = ((SaveContainerInfoForm)form).getPosition();
        boolean status = ((SaveContainerInfoForm)form).getStatus();
        boolean sequenceid = ((SaveContainerInfoForm)form).getSequenceid();
        boolean cdsstart = ((SaveContainerInfoForm)form).getCdsstart();
        boolean cdsstop = ((SaveContainerInfoForm)form).getCdsstop();
        boolean cdslength = ((SaveContainerInfoForm)form).getCdslength();
        boolean gccontent = ((SaveContainerInfoForm)form).getGccontent();
        boolean sequencetext = ((SaveContainerInfoForm)form).getSequencetext();
        boolean cds = ((SaveContainerInfoForm)form).getCds();
        boolean isEmpty = ((SaveContainerInfoForm)form).getIsEmpty();
        
        response.setContentType("application/ms-excel");
        boolean ret = false;
        
        try {
            PrintWriter out = response.getWriter();
            ContainerInfoExporter exporter = new ContainerInfoExporter(sampleid, type, position, status, sequenceid, cdsstart, cdsstop, cdslength, gccontent, sequencetext, cds, isEmpty);
            ret = exporter.doExport(id, out);
            out.close();
        } catch (IOException ex) {
            return mapping.findForward("fail");
        }
        
        if(ret) {
            return mapping.findForward("success");
        } else {
            return mapping.findForward("fail");
        }
    }
}
