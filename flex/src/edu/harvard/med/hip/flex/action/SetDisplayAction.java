/*
 * SetDisplayAction.java
 *
 * Created on August 15, 2003, 4:07 PM
 */

package edu.harvard.med.hip.flex.action;

import java.util.*;
import java.sql.*;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.util.MessageResources;

import edu.harvard.med.hip.flex.form.EnterResultForm;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.process.MasterToExpressionContainerMapper;
import edu.harvard.med.hip.flex.workflow.*;
import edu.harvard.med.hip.flex.core.CloningStrategy;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.process.Process;

/**
 *
 * @author  DZuo
 */
public class SetDisplayAction extends ResearcherAction {
    
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
    public ActionForward flexPerform(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException {
        ActionErrors errors = new ActionErrors();
        
        boolean well = ((EnterResultForm)form).getWell();
        boolean sampleid = ((EnterResultForm)form).getSampleid();
        boolean geneSymbol = ((EnterResultForm)form).getGeneSymbol();
        boolean pa = ((EnterResultForm)form).getPa();
        boolean sgd = ((EnterResultForm)form).getSgd();
        boolean masterClone = ((EnterResultForm)form).getMasterClone();
        boolean researcher = ((EnterResultForm)form).getResearcher();
        boolean restriction = ((EnterResultForm)form).getRestriction();
        boolean pcr = ((EnterResultForm)form).getPcr();
        boolean colony = ((EnterResultForm)form).getColony();
        boolean florescence = ((EnterResultForm)form).getFlorescence();
        boolean protein = ((EnterResultForm)form).getProtein();
        boolean status = ((EnterResultForm)form).getStatus();
        String newPlate = ((EnterResultForm)form).getNewPlate();
        
        if(well) {request.setAttribute("well", new Boolean(well));}
        if(sampleid) {request.setAttribute("sampleid", new Boolean(sampleid));}
        if(geneSymbol) {request.setAttribute("geneSymbol", new Boolean(geneSymbol));}
        if(pa) {request.setAttribute("pa", new Boolean(pa));}
        if(sgd) {request.setAttribute("sgd", new Boolean(sgd));}
        if(masterClone) {request.setAttribute("masterClone", new Boolean(masterClone));}
        if(researcher) {request.setAttribute("researcher", new Boolean(researcher));}
        if(restriction) {request.setAttribute("restriction", new Boolean(restriction));}
        if(pcr) {request.setAttribute("pcr", new Boolean(pcr));}
        if(colony) {request.setAttribute("colony", new Boolean(colony));}
        if(florescence) {request.setAttribute("florescence", new Boolean(florescence));}
        if(protein) {request.setAttribute("protein", new Boolean(protein));}
        if(status) {request.setAttribute("status", new Boolean(status));}
        request.setAttribute("newPlate", newPlate);
        
        Container container = (ExpressionCloneContainer)request.getSession().getAttribute("newExpressionPlate");
        
        if(container == null) {
            return (mapping.findForward("fail"));
        }
        
        ((EnterResultForm)form).setCloneValues(container.getSamples());
        
        return (mapping.findForward("success"));
    }
}
