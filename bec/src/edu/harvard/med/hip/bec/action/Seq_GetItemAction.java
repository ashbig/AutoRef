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
import edu.harvard.med.hip.bec.coreobjects.feature.*;
import edu.harvard.med.hip.bec.coreobjects.endreads.*;
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
                forwardName == Constants.CONTAINER_RESULTS_VIEW ||
                forwardName == Constants.CONTAINER_ISOLATE_RANKER_REPORT ||
                forwardName ==Constants.PROCESS_PUT_CLONES_ON_HOLD ||
                 forwardName == Constants.PROCESS_ACTIVATE_CLONES ||
                 forwardName == Constants.PROCESS_CHECK_READS_AVAILABILITY 
                 || forwardName == Constants.PROCESS_APROVE_ISOLATE_RANKER)//rocessing from container label
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
               forwardName == Constants.CLONING_STRATEGY_DEFINITION_INT ||
               forwardName == Constants.SAMPLE_ISOLATE_RANKER_REPORT ||
               forwardName == Constants.READ_REPORT_INT ||
               forwardName == Constants.CONSTRUCT_DEFINITION_REPORT
               )
               {
                    id = Integer.parseInt( (String) request.getParameter("ID"));
                }
            switch  (forwardName)
            {
                case Constants.VECTOR_DEFINITION_INT:
                {
                    BioVector vector = BioVector.getVectorById(id);
                    ArrayList vectors = new ArrayList();
                    vectors.add(vector);
                    request.setAttribute("vectors", vectors);
                    return (mapping.findForward("display_vector"));
                }
                case Constants.LINKER_DEFINITION_INT:
                {
                    BioLinker linker = BioLinker.getLinkerById(id);
                    ArrayList linkers = new ArrayList();
                    linkers.add(linker);
                    request.setAttribute("linkers", linkers);
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
                   
                    ArrayList pr_history = Container.getProcessHistoryItems( label);
                    container.getCloningStrategyId();
                    request.setAttribute("container",container) ;
                   
                    request.setAttribute("process_items",pr_history);
                    return (mapping.findForward("display_container_history"));
                }
               
                case Constants.CONTAINER_DEFINITION_INT:
                {
                    
                    container.restoreSampleIsolate(false,false);
                    container.getCloningStrategyId();
                    request.setAttribute("container",container);
                    return (mapping.findForward("display_container_details"));
                }
                case Constants.PROCESS_APROVE_ISOLATE_RANKER:
                {
                    container.restoreSampleIsolateNoFlexInfo();
                    request.setAttribute("container",container);
                    request.setAttribute("forwardName", new Integer(forwardName));
                    request.setAttribute("rows", new Integer(8));
                    request.setAttribute("cols", new Integer(12));

                    return (mapping.findForward("display_isolate_ranker_report"));
                }
                 case Constants.CONTAINER_RESULTS_VIEW:
                {
                   
                    String result_type = (String)request.getParameter("show_action");
                    if ( result_type.equalsIgnoreCase("FER"))
                    { 
                   
                        int[] result_types = {Result.RESULT_TYPE_ENDREAD_FORWARD,Result.RESULT_TYPE_ENDREAD_FORWARD_PASS, Result.RESULT_TYPE_ENDREAD_FORWARD_FAIL};
                        container.restoreSampleWithResultId(result_types,true);
                      
                    }
                    else if (result_type.equalsIgnoreCase("RER"))//reverse
                    {
                        
                         int[] result_types = {Result.RESULT_TYPE_ENDREAD_REVERSE,Result.RESULT_TYPE_ENDREAD_REVERSE_PASS, Result.RESULT_TYPE_ENDREAD_REVERSE_FAIL};
                        container.restoreSampleWithResultId(result_types,true);
                    }
                    else if (result_type.equalsIgnoreCase("IR"))
                    {
                        container.restoreSampleIsolateNoFlexInfo();
                        container.getCloningStrategyId();
                        request.setAttribute("container",container);
                        request.setAttribute("rows", new Integer(8));
                        request.setAttribute("cols", new Integer(12));

                        return (mapping.findForward("display_isolate_ranker_report"));
                    }
                    container.getCloningStrategyId();
                    request.setAttribute("container",container);
                     System.out.println("send "+System.currentTimeMillis());
                    return (mapping.findForward("display_container_results_er"));
                  
                       
                }
                 /*
                case Constants.CONTAINER_ISOLATE_RANKER_REPORT:
                {
                    
                    container.restoreSampleIsolateNoFlexInfo();
                    container.getCloningStrategyId();
                    request.setAttribute("container",container);
                    request.setAttribute("rows", new Integer(8));
                    request.setAttribute("cols", new Integer(12));
                   
                    return (mapping.findForward("display_isolate_ranker_report"));
                }
                */
                case Constants.SAMPLE_ISOLATE_RANKER_REPORT:
                {
                    Sample sample = new Sample(id);
                    sample.getRefSequenceId();
            
                    sample.setIsolaterTrackingEngine( IsolateTrackingEngine.getIsolateTrackingEngineBySampleId(sample.getId()));
                    ArrayList discrepancies = new ArrayList();
                    CloneSequence   clonesequence   = null;
                    Read read  = null;
                    if (sample.getIsolateTrackingEngine().getCloneSequence() != null)
                    {
                        clonesequence = sample.getIsolateTrackingEngine().getCloneSequence();
                        discrepancies = clonesequence.getDiscrepancies();
                    }
                    else
                    {
                        for (int read_count = 0; read_count < sample.getIsolateTrackingEngine().getEndReads().size(); read_count++)
                        {
                            read = (Read) sample.getIsolateTrackingEngine().getEndReads().get(read_count);
                            discrepancies.addAll( read.getSequence().getDiscrepancies() );
                        }
                        discrepancies = DiscrepancyDescription.getDiscrepancyNoDuplicates(discrepancies);
                    }
                     String discrepancy_report_html = Mutation.HTMLReport( discrepancies, Mutation.LINKER_5P, true);
                     discrepancy_report_html += Mutation.HTMLReport( discrepancies, Mutation.RNA, true);
                     discrepancy_report_html += Mutation.HTMLReport( discrepancies, Mutation.LINKER_3P, true);
                     if (discrepancy_report_html.equals(""))
                        discrepancy_report_html="<tr><td colspan=3><strong>No discrepancies</strong></td></tr>";
                   
                    request.setAttribute("container_label", request.getParameter("container_label"));
                    request.setAttribute("sample", sample);
                    
                    request.setAttribute("discrepancy_report",discrepancy_report_html);
                    return (mapping.findForward("display_sample_isolate_ranker_report"));
                }
                case Constants.CONSTRUCT_DEFINITION_REPORT:
                {
                    Construct construct = new Construct(id);
                    ArrayList clones_data = Construct.getClonesData(id);
                    request.setAttribute("clones_data",clones_data);
                    request.setAttribute("construct",construct);
                     request.setAttribute("forwardName", new Integer(Constants.PROCESS_APROVE_ISOLATE_RANKER));
                    return (mapping.findForward("construct_report"));
                }
                case Constants.READ_REPORT_INT:
                {
                    Read read = Read.getReadById(id);
                    read.getSequence();
                    ArrayList discrepancies = read.getSequence().getDiscrepancies();
                    
                    String discrepancy_report_html = Mutation.HTMLReport( discrepancies, Mutation.LINKER_5P, true);
                     discrepancy_report_html += Mutation.HTMLReport( discrepancies, Mutation.RNA, true);
                     discrepancy_report_html += Mutation.HTMLReport( discrepancies, Mutation.LINKER_3P, true);
                     if (discrepancy_report_html.equals(""))
                        discrepancy_report_html="<tr><td colspan=3><strong>No discrepancies</strong></td></tr>";
                    request.setAttribute("read", read);
                    request.setAttribute("discrepancy_report",discrepancy_report_html);
                    return (mapping.findForward("display_read_report"));
                    
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
                    String needle_output = null;
                   
                    System.out.print("seq_id "+request.getParameter(BaseSequence.THEORETICAL_SEQUENCE_STR));   
                    if ( request.getParameter(BaseSequence.THEORETICAL_SEQUENCE_STR) != null)
                    {
                        int refseq_id = Integer.parseInt(request.getParameter(BaseSequence.THEORETICAL_SEQUENCE_STR));
                       
                        ut.setRefSequenceId(refseq_id);
                        request.setAttribute("refsequenceid" , request.getParameter(BaseSequence.THEORETICAL_SEQUENCE_STR));
                        needle_output = ut.getHTMLtransformedNeedleAlignmentForTrimedRead(null,id);
                    }
                    else
                    {
                        needle_output = ut.getHTMLtransformedNeedleAlignment(BaseSequence.READ_SEQUENCE,id);
                        RefSequence refsequence = ut.getRefSequence();
                        request.setAttribute("refsequenceid" , new Integer(refsequence.getId()));
                        if (request.getParameter("trimstart") != null)
                        {
                            request.setAttribute("trimstart",request.getParameter("trimstart"));
                        }
                        if (request.getParameter("trimend") != null)
                        {
                            request.setAttribute("trimend",request.getParameter("trimend"));
                        }
                    }
                    request.setAttribute( "expsequenceid",new Integer(id));
                    request.setAttribute( "expsequencetype", new Integer(BaseSequence.READ_SEQUENCE));
                    
                    request.setAttribute("alignment",needle_output);
                    return (mapping.findForward("display_needle_alignment"));
                }
                case   Constants.PROCESS_PUT_CLONES_ON_HOLD :
                 case Constants.PROCESS_ACTIVATE_CLONES:
                {
                 //show label scan form  
                   
                    String title = null;
                    if ( forwardName == Constants.PROCESS_PUT_CLONES_ON_HOLD )
                    {
                        
                        title =  "put Active Clones on Hold";
                    }
                    else if( forwardName == Constants.PROCESS_ACTIVATE_CLONES )
                    {
                        
                        title =  "activate Clones";
                    }
                    
                    container.restoreSampleIsolate(false,true);
                    request.setAttribute("container",container);
                    request.setAttribute("forwardName", new Integer(forwardName));
                
                    request.setAttribute(Constants.JSP_TITLE,title);
                    return (mapping.findForward("show_activate_list"));
                }
                case Constants.AVAILABLE_LINKERS_DEFINITION_INT:
                {
                    
                    ArrayList linkers = BioLinker.getAllLinkers();
                    
                    request.setAttribute("linkers", linkers);
                    request.setAttribute("forwardName", new Integer(forwardName));
                    return (mapping.findForward("display_linker"));
                }
                case Constants.AVAILABLE_VECTORS_DEFINITION_INT:
                {
                    
                    request.setAttribute("forwardName", new Integer(forwardName));
                    ArrayList vectors = BioVector.getAllVectors();
                    request.setAttribute("vectors", vectors);
                    return (mapping.findForward("display_vector"));
                    
                }     
                //check clones end reads / sequence availability
                //initiated from SelectProcess
                case Constants.PROCESS_CHECK_READS_AVAILABILITY:
                {
                    container.restoreSampleIsolateNoFlexInfo();
                    Sample s = null;Result r = null;
                    for (int is_count = 0; is_count < container.getSamples().size(); is_count++)
                    {
                        s = (Sample) container.getSamples().get(is_count);
                        r = Result.getResultBySampleId(s.getId(), ""+Result.RESULT_TYPE_ENDREAD_FORWARD +","+Result.RESULT_TYPE_ENDREAD_FORWARD_PASS+","+Result.RESULT_TYPE_ENDREAD_FORWARD_FAIL);
                        if (r != null) s.addResult(r);
                        r = Result.getResultBySampleId(s.getId(), ""+Result.RESULT_TYPE_ENDREAD_REVERSE +","+Result.RESULT_TYPE_ENDREAD_REVERSE_PASS+","+Result.RESULT_TYPE_ENDREAD_REVERSE_FAIL);
                        if (r != null) s.addResult(r);

                    }
                     request.setAttribute("container",container);
                    String title = "clone Data for container " + label;
                    request.setAttribute(Constants.JSP_TITLE,title);
                    return (mapping.findForward("show_clone_status_list"));
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
