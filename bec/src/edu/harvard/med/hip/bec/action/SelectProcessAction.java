/*
 * SelectProcessAction.java
 *
 * Created on March 11, 2003, 3:51 PM
 */

package edu.harvard.med.hip.bec.action;

import java.util.*;
import java.text.*;
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

import edu.harvard.med.hip.bec.core.spec.*;
import edu.harvard.med.hip.bec.core.sequence.*;
import edu.harvard.med.hip.bec.engine.*;

import edu.harvard.med.hip.bec.core.oligo.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.form.*;
import edu.harvard.med.hip.bec.user.*;
import edu.harvard.med.hip.bec.Constants;


/**
 *
 * @author  htaycher
 */
public class SelectProcessAction extends ResearcherAction
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
    public ActionForward becPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        
        ActionErrors errors = new ActionErrors();
        int forwardName = ((Seq_GetSpecForm)form).getForwardName();
        ArrayList specs = new ArrayList();
        
        
        
        try
        {
            
            request.setAttribute("forwardName", new Integer(forwardName));
            switch  (forwardName)
            {
                case 1://upload plates
                {
                    ArrayList biotails = BioTail.getAllTails();
                    ArrayList vectors = BioVector.getAllVectors();
                    
                    request.setAttribute("vectors", vectors);
                    request.setAttribute("biotails", biotails);
                    return (mapping.findForward("upload_plates"));
                }
                case 2://run sequencing for end reads
                {
                }
                case 3://run end reads wrapper
                case 12://run assembly wrapper
                {
                    RunProcess process = new RunProcess(forwardName, null);
                    process.run();
                }
                case 4://check reads
                {
                }
                case 5://run isolate runker
                {
                }
                case 6://view isolate ranker
                {
                }
                case 7: //send plate to seq
                {
                }
                case 8: case 11://recieve from seq
                {
                    String currentDate = getCurrentDate();
                    request.setAttribute("currentDate",currentDate);
                    return (mapping.findForward("recieve_orders"));
                }
                case 9://upload data from seq
                {
                }
                case 10://run primer3
                case 14: //run polymorphism finder
                case 13://run discrepancy finder
                {
                    ArrayList plates =  new ArrayList();
                    ArrayList specNames = new ArrayList();
                    ArrayList specLists = new ArrayList();
                    ArrayList specParamNames = new ArrayList();
                    
                    if (forwardName == 10)
                    {
                        ArrayList primer3_specs = Primer3Spec.getAllSpecs();
                        specNames.add("Primer designer");
                        specLists.add(primer3_specs);
                        specParamNames.add("primer3_specid");
                        request.setAttribute("title", "Design Primers");
                        //plates = getPlates();
                    }
                    if (forwardName == 14)//run polymorphism finder
                    {
                        ArrayList polym_specs = PolymorphismSpec.getAllSpecs();
                        specNames.add("Polymorphism finder");
                        specLists.add(polym_specs);
                        specParamNames.add("polym_specid");
                         request.setAttribute("title", "Run Polymorphism Finder");
                        //plates = getPlates();
                    }
                    if (forwardName == 13)//run discrepancy finder
                    {
                         request.setAttribute("title", "Run Discrepancy Finder");
                        //plates = getPlates();
                    }
                    
                    request.setAttribute("plates", plates);
                    request.setAttribute("specLists", specLists);
                    request.setAttribute("specNames", specNames);
                    request.setAttribute("specParamNames", specParamNames);
                    return (mapping.findForward("run_process"));
                }
                
                case 15://run decision tool
                {
                }
            }
            
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            request.setAttribute(Action.EXCEPTION_KEY, e);
            return (mapping.findForward("error"));
        }
        return (mapping.findForward("error"));
    }
    
    /**
     * get today's date in dd-mmm-yy format
     */
    private String getCurrentDate()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
        java.util.Date currentDate = new java.util.Date();
        return  formatter.format(currentDate);
        
    }
    
}
