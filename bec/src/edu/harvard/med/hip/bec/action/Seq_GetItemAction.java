/*
 * Seq_GetItemAction.java
 *
 * Created on June 17, 2003, 12:54 PM
 */



package edu.harvard.med.hip.bec.action;

/**
 *
 * @author  htaycher
 */



import java.util.*;

import java.sql.*;
import java.io.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.coreobjects.oligo.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.form.*;
import edu.harvard.med.hip.bec.user.*;
import edu.harvard.med.hip.bec.modules.*;
import edu.harvard.med.hip.bec.Constants;
import edu.harvard.med.hip.bec.sampletracking.objects.*;
import edu.harvard.med.hip.bec.programs.needle.*;

public class Seq_GetItemAction extends ResearcherAction
{
    
    
    public ActionForward becPerform(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException
    {
     
        ActionErrors errors = new ActionErrors();
        int forwardName = ((Seq_GetSpecForm)form).getForwardName();
        String label = null;int id = -1;Container container = null;
       
        
        try
        {
            if ( forwardName == Constants.CONTAINER_PROCESS_HISTORY || forwardName 
                ==Constants.CONTAINER_DEFINITION_INT ||
                forwardName == Constants.CONTAINER_RESULTS_VIEW )//rocessing from container label
               {
                     
                    label = (String)request.getParameter(Constants.CONTAINER_BARCODE_KEY);
                    label =label.toUpperCase().trim();
                    container =  verifyLabel(label);
                                 
                    if ( container == null)
                    {
                        errors.add(ActionErrors.GLOBAL_ERROR,
                            new ActionError("error.container.querry.parameter", 
                                "Unable to find container with label "+label));
                            saveErrors(request,errors);
                             System.out.println("Container "+ container+mapping.getInput());
                            return new ActionForward(mapping.getInput());
                    }
               }
               else if (forwardName == Constants.SCOREDSEQUENCE_DEFINITION_INT || 
               forwardName == Constants.REFSEQUENCE_DEFINITION_INT ||
               forwardName ==  Constants.ANALYZEDSEQUENCE_DISCREPANCY_REPORT_DEFINITION_INT ||
               forwardName == Constants.READSEQUENCE_NEEDLE_ALIGNMENT_INT ||
               forwardName == Constants.VECTOR_DEFINITION_INT ||
               forwardName == Constants.LINKER_DEFINITION_INT ||
               forwardName == Constants.CLONING_STRATEGY_DEFINITION_INT
               )
               {
                    id = Integer.parseInt( (String) request.getParameter("ID"));
                 
               }
            switch  (forwardName)
            {
                case Constants.VECTOR_DEFINITION_INT:
                {
                    BioVector vector = BioVector.getVectorById(id);
                    request.setAttribute("vector", vector);
                    return (mapping.findForward("display_vector"));
                }
                case Constants.LINKER_DEFINITION_INT:
                {
                    BioLinker linker = BioLinker.getLinkerById(id);
                    request.setAttribute("linker", linker);
                    return (mapping.findForward("display_linker"));
                }
                case Constants.CLONING_STRATEGY_DEFINITION_INT:
                {
                    CloningStrategy cs =  CloningStrategy.getById(id);
                    BioVector vector = BioVector.getVectorById( cs.getVectorId());
                    BioLinker linker3 = BioLinker.getLinkerById( cs.getLinker3Id() );
                    BioLinker linker5 = BioLinker.getLinkerById( cs.getLinker5Id() );
                    request.setAttribute("linker3", linker3);
                    request.setAttribute("linker5", linker5);
                    request.setAttribute("vector", vector);
                    return (mapping.findForward("display_cloning_strategy"));
                }
                case Constants.CONTAINER_PROCESS_HISTORY:
                {
                    System.out.println("label "+label);
                    ArrayList pr_history = Container.getProcessHistoryItems( label);
                    container.getCloningStrategyId();
                    request.setAttribute("container",container) ;
                    System.out.println("Process historys "+pr_history);
                    request.setAttribute("process_items",pr_history);
                    return (mapping.findForward("display_container_history"));
                }
               
                case Constants.CONTAINER_DEFINITION_INT:
                {
                    
                    container.restoreSampleIsolate();
                    System.out.println("Container samples "+container.getSamples().size());
                    container.getCloningStrategyId();
                    request.setAttribute("container",container);
                    return (mapping.findForward("display_container_details"));
                }
                 case Constants.CONTAINER_RESULTS_VIEW:
                {
                   
                    String result_type = (String)request.getParameter("show_action");
                    if ( result_type.equalsIgnoreCase("FER"))
                    { 
                       System.out.println("start "+System.currentTimeMillis());
                        int[] result_types = {Result.RESULT_TYPE_ENDREAD_FORWARD,Result.RESULT_TYPE_ENDREAD_FORWARD_PASS, Result.RESULT_TYPE_ENDREAD_FORWARD_FAIL};
                        container.restoreSampleWithResultId(result_types,true);
                         System.out.println("end "+System.currentTimeMillis());
                    }
                    else if (result_type.equalsIgnoreCase("RER"))//reverse
                    {
                        
                         int[] result_types = {Result.RESULT_TYPE_ENDREAD_REVERSE,Result.RESULT_TYPE_ENDREAD_REVERSE_PASS, Result.RESULT_TYPE_ENDREAD_REVERSE_FAIL};
                        container.restoreSampleWithResultId(result_types,true);
                    }
                    else if (result_type.equalsIgnoreCase("IR"))
                    {
                       
                    }
                    container.getCloningStrategyId();
                    request.setAttribute("container",container);
                     System.out.println("send "+System.currentTimeMillis());
                    return (mapping.findForward("display_container_results_er"));
                  
                       
                }
                case Constants.REFSEQUENCE_DEFINITION_INT :
                {
                   
                    RefSequence refsequence = new RefSequence(id);
                    request.setAttribute("refsequence",refsequence);
                    return (mapping.findForward("display_refsequence_details"));
                }
                case Constants.SCOREDSEQUENCE_DEFINITION_INT:
                {
                  
                    ScoredSequence scoredsequence = new ScoredSequence(id);
                    if (request.getParameter("trimstart") != null)
                    {
                        request.setAttribute("trimstart",request.getParameter("trimstart"));
                    }
                    if (request.getParameter("trimend") != null)
                    {
                        request.setAttribute("trimend",request.getParameter("trimend"));
                    }
                    request.setAttribute("scoredsequence",scoredsequence);
                    return (mapping.findForward("display_scoredsequence_details"));
                }
              //  case Constants.CLONE_SEQUENCE_DEFINITION_INT :
                case   Constants.ANALYZEDSEQUENCE_DISCREPANCY_REPORT_DEFINITION_INT :
                {
                    AnalyzedScoredSequence sequence = new AnalyzedScoredSequence(id);
                    request.setAttribute("sequence",sequence);
                    return (mapping.findForward("display_discrepancyreport"));
                }
                case Constants.READSEQUENCE_NEEDLE_ALIGNMENT_INT:
                {
                  //find file
                    
                    Utils ut= new Utils();
                    String needle_output = ut.getHTMLtransformedNeedleAlignment(BaseSequence.READ_SEQUENCE,id);
                    RefSequence refsequence = ut.getRefSequence();
                    if (request.getParameter("trimstart") != null)
                    {
                        request.setAttribute("trimstart",request.getParameter("trimstart"));
                    }
                    if (request.getParameter("trimend") != null)
                    {
                        request.setAttribute("trimend",request.getParameter("trimend"));
                    }
                    request.setAttribute( "expsequenceid",new Integer(id));
                    request.setAttribute( "expsequencetype", new Integer(BaseSequence.READ_SEQUENCE));
                    request.setAttribute("refsequenceid" , new Integer(refsequence.getId()));
                    request.setAttribute("alignment",needle_output);
                    return (mapping.findForward("display_needle_alignment"));
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

    private Container verifyLabel(String label)throws
    Exception
    {
        
        //check if container exists
        if (label == null && label.equals("") )
        {
            return null;

        }
        Container container = Container.findContainerDescriptionFromLabel(label);
       
        return container;
    }
    
    
    
    
    
}
