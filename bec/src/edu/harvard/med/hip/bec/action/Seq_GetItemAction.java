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
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.ui_objects.*;
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
                 || forwardName == Constants.PROCESS_APROVE_ISOLATE_RANKER
                 )//rocessing from container label
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
                          //   System.out.println("Container "+ container+mapping.getInput());
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
               forwardName == Constants.CONSTRUCT_DEFINITION_REPORT ||
               forwardName == Constants.CLONE_SEQUENCE_DEFINITION_REPORT_INT ||
               forwardName == Constants.STRETCH_REPORT_INT 
               )
               {
                   String str_id = (String) request.getParameter("ID");
                   if ( str_id != null)  id = Integer.parseInt(str_id);
                       
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
                    cs.setVector(vector);
                    cs.setLinker3(linker3);
                    cs.setLinker5(linker5);
                    request.setAttribute("cloning_strategy", cs);
                   
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
                    ArrayList ui_clones = container.restoreUISamples(container);
                    //fill in clone info
                    UICloneSample clone = null; 
                    
            //get info for the most relevant sequence 
                    UICloneSample.setCloneSequences(ui_clones, null);
                    container.setSamples(ui_clones);
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
                        request.setAttribute("forwardName", new Integer(forwardName));
                        return (mapping.findForward("display_isolate_ranker_report"));
                    }
                    container.getCloningStrategyId();
                    request.setAttribute("container",container);
                   
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
                    //get sample 
                    Sample sample = new Sample(id);
                    sample.getRefSequenceId();
            //get clone sequences && end reads
                    int[] sequence_analysis_status = {
                        BaseSequence.CLONE_SEQUENCE_STATUS_ASSEMBLED ,
                        BaseSequence.CLONE_SEQUENCE_STATUS_ANALIZED_YES_DISCREPANCIES,
                        BaseSequence.CLONE_SEQUENCE_STATUS_ANALIZED_NO_DISCREPANCIES ,
                        BaseSequence.CLONE_SEQUENCE_STATUS_NOMATCH ,
                        BaseSequence.CLONE_SEQUENCE_STATUS_POLYMORPHISM_CLEARED ,
                        BaseSequence.CLONE_SEQUENCE_STATUS_ANALYSIS_CONFIRMED };
                    String clone_sequence_analysis_status = Algorithms.convertArrayToString(sequence_analysis_status, ",");
                    int[] sequence_type = {BaseSequence.CLONE_SEQUENCE_TYPE_ASSEMBLED, BaseSequence.CLONE_SEQUENCE_TYPE_FINAL  };
                    String clone_sequence_type = Algorithms.convertArrayToString(sequence_type, ",");
                    IsolateTrackingEngine istr = IsolateTrackingEngine.getIsolateTrackingEngineBySampleId(sample.getId(),
                                clone_sequence_analysis_status,
                                clone_sequence_type,2);
                //    sample.setIsolaterTrackingEngine( istr);
                    String discrepancy_report_for_endread = null;
                    ArrayList end_reads = createListOfUIReads(istr,discrepancy_report_for_endread, sample.getRefSequenceId());
                    ArrayList  clone_sequences = createListOfUICloneSequences(istr, sample.getRefSequenceId());
                    ArrayList contigs = null;
                    StretchCollection strcol = null;
                    if ( clone_sequences== null || clone_sequences.size() < 1)
                    {
                        strcol = StretchCollection.getByIsolateTrackingId(id);
                        contigs = StretchCollection.createListOfUIContigs(strcol,Constants.ITEM_TYPE_ISOLATETRASCKING_ID);
                    }
                    request.setAttribute("container_label", request.getParameter("container_label"));
                    request.setAttribute("sample", sample);
                    request.setAttribute("end_read", end_reads);
                    request.setAttribute("contigs", contigs);
                    request.setAttribute("clone_sequences",clone_sequences);
                    request.setAttribute("discrepancy_report_for_endread",discrepancy_report_for_endread);
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
                case Constants.STRETCH_REPORT_INT:
                {
                    Stretch stretch = Stretch.getById(id);
                    ArrayList discrepancies = null;
                    String discrepancy_report_html = null;
                    StringBuffer lqr_report_html = new StringBuffer();
                    if ( stretch.getType() == Stretch.GAP_TYPE_CONTIG || stretch.getType() == Stretch.GAP_TYPE_LOW_QUALITY)
                    {
                        discrepancies = stretch.getSequence().getDiscrepancies();
                        discrepancy_report_html = Mutation.HTMLReport( discrepancies, Mutation.LINKER_5P, true);
                         discrepancy_report_html += Mutation.HTMLReport( discrepancies, Mutation.RNA, true);
                         discrepancy_report_html += Mutation.HTMLReport( discrepancies, Mutation.LINKER_3P, true);
                         if (discrepancy_report_html.equals(""))
                            discrepancy_report_html="<tr><td colspan=3><strong>No discrepancies</strong></td></tr>";
                    }
                    //get assosiated lqr
                    
                  if ( stretch.getType() == Stretch.GAP_TYPE_CONTIG )
                  {
                      Stretch lqr=null;
                         
                      ArrayList lqrs = Stretch.getBySequenceIdType(stretch.getSequenceId(), Stretch.GAP_TYPE_LOW_QUALITY, false);
                      if ( lqrs != null && lqrs.size() > 0)
                      {
                          lqr_report_html.append( " <th>Name </th><th>Cds Region</th><th>Sequence Region</th>");
                          for (int count = 0; count < lqrs.size(); count++)
                          {
                             lqr = (Stretch) lqrs.get(count);
                              lqr_report_html.append("<tr><TD>"+lqr.getStretchTypeAsString(lqr.getType()) + " "+ (count + 1) +"</TD>");
                              lqr_report_html.append("<TD>"+ (  lqr.getCdsStart() )
                                    +" - "+ ( lqr.getCdsStop()  )  +"</TD>");
                              lqr_report_html.append("<TD>"+lqr.getSequenceStart() +" - "+ lqr.getSequenceStop() +"</TD></tr>");
                          }
                      }
                  }
                    request.setAttribute("stretch", stretch);
                    request.setAttribute("discrepancy_report",discrepancy_report_html);
                    request.setAttribute("lqr_report",lqr_report_html.toString());
                    return (mapping.findForward("display_stretch_report"));
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
                   // System.out.println("L");
                    if ( id == -1)//display subset of discrepancies
                    {
                        ArrayList discrepancies = new ArrayList();
                        String discr_ids = (String) request.getParameter("DISCRIDS");
                        discr_ids = Algorithms.replaceChar( discr_ids, '_', ',');
                        if ( discr_ids.charAt(discr_ids.length() - 1) == ',') discr_ids = discr_ids.substring(0, discr_ids.length() - 1);
                        discrepancies = Mutation.getByIds(discr_ids);
                        request.setAttribute("discrepancies",discrepancies);
                    }
                    else
                    {
                        AnalyzedScoredSequence sequence = new AnalyzedScoredSequence(id);
                        request.setAttribute("sequence",sequence);
                    }
                   // System.out.println("1L");
                    return (mapping.findForward("display_discrepancyreport"));
                }
                case Constants.CLONE_SEQUENCE_DEFINITION_REPORT_INT:
                {
                    CloneSequence clone_sequence = new CloneSequence(id);
                    request.setAttribute("clone_sequence",clone_sequence);
                    return (mapping.findForward("display_clone_sequence"));
                }
                case Constants.READSEQUENCE_NEEDLE_ALIGNMENT_INT:
                {
                  //find file
                    
                    UIUtils ut= new UIUtils();
                    String needle_output = null;
                   
            //        System.out.print("seq_id "+request.getParameter(BaseSequence.THEORETICAL_SEQUENCE_STR));   
                    if ( request.getParameter(BaseSequence.THEORETICAL_SEQUENCE_STR) != null)
                    {
                        int refseq_id = Integer.parseInt(request.getParameter(BaseSequence.THEORETICAL_SEQUENCE_STR));
                        
                        ut.setRefSequenceId(refseq_id);
                        request.setAttribute("refsequenceid" , request.getParameter(BaseSequence.THEORETICAL_SEQUENCE_STR));
                        int sequenceType = Integer.parseInt( (String)request.getParameter("TYPE")) ;
                        needle_output = ut.getHTMLtransformedNeedleAlignmentForTrimedRead(null,id, sequenceType);
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
                case Constants.AVAILABLE_CONTAINERS_INT:
                {
                    String title = "available Containers";
                   ArrayList labels = Container.findAllContainerLabels();
                     StringBuffer container_names= new StringBuffer();
                    int cur_column=1;String cur_label = null;String prev_label = null;
                    if (labels == null || labels.size() < 1)
                    {
                        container_names.append( "No containers are available.");
                    }
                    else
                    {
                         container_names.append("<script language='JavaScript' src='"+edu.harvard.med.hip.utility.ApplicationHostDeclaration.JSP_REDIRECTION+"scripts.js'></script> ");
                       
                        for (int index = 0; index < labels.size(); index ++)
                        {
                            cur_label = (String)labels.get(index);
                          //  System.out.println(cur_label);
                            if (index == 0) container_names.append("<tr>");
                            if ( cur_column == 6)
                            {
                                container_names.append("</tr><tr>");
                                cur_column =1;
                            }
                                
                            if ( prev_label == null || ( prev_label != null &&  cur_label.charAt(0) != prev_label.charAt(0)))
                            {
                                if (index != 0)
                                {
                                     container_names.append("</table>");
                                    container_names.append("</DIV>");
                                    container_names.append("</tr><tr>");
                                }
                                 
                                container_names.append("<tr><td><INPUT onclick='javascript:showhide(\""+cur_label.charAt(0) +"\", this.checked);' type=checkbox CHECKED value=1 name=show> "+getProjectName(cur_label.charAt(0)));
                                container_names.append("<DIV ID='"+cur_label.charAt(0) +"' STYLE='  position:relative;  clip:rect(0px 120px 120px 0px); '>");
                                container_names.append("<p><table border = 0>");
                                cur_column=1;
                            }
                            container_names.append( "<td><b>"+cur_label+"</b></td>");
                            cur_column ++;
                            prev_label = cur_label;
                        }
                        container_names.append("</table></table>");
                    }
                    request.setAttribute( Constants.ADDITIONAL_JSP, container_names.toString()) ;
                    request.setAttribute(Constants.JSP_TITLE,title);
                    return (mapping.findForward("display_info"));
                }
                case Constants.PROCESS_PROCESS_OLIGO_PLATE:
                case Constants.PROCESS_VIEW_OLIGO_PLATE:
                {
                     label = (String)request.getParameter(Constants.CONTAINER_BARCODE_KEY);
                     label =label.toUpperCase().trim();
         //  System.out.println(label +" getitem "+ forwardName);
                     ArrayList oligo_containers = OligoContainer.findContainersInfoFromLabel(label.toUpperCase().trim(), OligoContainer.MODE_NOTRESTORE_SAMPLES);
                     OligoContainer oligo_container = null;
                    if ( oligo_containers != null && oligo_containers.size() == 1)
                        oligo_container = (OligoContainer)oligo_containers.get(0);
                     if (oligo_container == null)
                    {
                        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.container.querry.parameter",  "Unable to find oligo container with label "+label));
                        saveErrors(request,errors);
                        return new ActionForward(mapping.getInput());
                    }
                     if (forwardName == Constants.PROCESS_VIEW_OLIGO_PLATE)
                     {
                         oligo_container.restoreSamples();
                     }
 //  System.out.println(label+oligo_container.getId());
                    request.setAttribute(Constants.JSP_TITLE,"process Oligo Container");
                    request.setAttribute("container",oligo_container);
                    request.setAttribute("forwardName", new Integer(forwardName));
                    return (mapping.findForward("display_oligo_container"));
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
    
    private String getProjectName(char project_code)
    {
        project_code = Character.toUpperCase(project_code);
        switch (project_code)
        {
            case         'A': return  "NIDDK Diabetis - Human";
            case         'B': return  " Breast Cancer"; 
            case         'C': return  " Clontech"; 
            case         'D': return  " NIDDK Diabetis - Mouse"; 
            case         'G': return  " Prostate Cancer"; 
            case         'H': return  " Human"; 
            case         'K': return  " Kinase"; 
            case         'M': return  " MGC"; 
            case         'P': return  " Pseudomonas"; 
            case         'S': return  " Yersinia pestis"; 
            case         'T': return  " Transcription Factor";
            case         'Y': return  " Yeast";
            case        'F': return " Francisella tularentsis";
            default: return String.valueOf(project_code);
        }

    }
    
    //for sample report: converts end reads list int o uireads
    private ArrayList  createListOfUIReads(IsolateTrackingEngine istr, String discrepancy_report_for_endread, int ref_seqid)
                        throws BecDatabaseException
    {
        ArrayList discrepancies = new ArrayList();
        ArrayList uireads = new ArrayList();
        UIRead uiread = null;Read read = null;
        DiscrepancyFinder nv = new DiscrepancyFinder();
        String  needle_file_name = null;
        File needle_file = null;
         for (int read_count = 0; read_count < istr.getEndReads().size(); read_count++)
        {
            read = (Read) istr.getEndReads().get(read_count);
            uiread = new UIRead();
            uiread.setId (read.getId());
            uiread.setSequenceId (read.getSequence().getId());
            uiread.setType (read.getType());
            //determine needle file exists
            needle_file_name = nv.getOutputDirectory()+"/needle"+uiread.getSequenceId ()+
                                    "_"+ref_seqid+".out";
            needle_file = new File(needle_file_name);
            if ( needle_file.exists() )
                uiread.setIsAlignmentExists (true);
            if ( read.getSequence().getDiscrepancies() != null && read.getSequence().getDiscrepancies().size() > 0)
            {
                uiread.setIsDiscrepancies ( true);
            }
            uireads.add(uiread);
            discrepancies.addAll( read.getSequence().getDiscrepancies() );
        }
        discrepancies = DiscrepancyDescription.getDiscrepancyNoDuplicates(discrepancies);
    
         String discrepancy_report_html = Mutation.HTMLReport( discrepancies, Mutation.LINKER_5P, true);
         discrepancy_report_html += Mutation.HTMLReport( discrepancies, Mutation.RNA, true);
         discrepancy_report_html += Mutation.HTMLReport( discrepancies, Mutation.LINKER_3P, true);
         if (discrepancy_report_html.equals(""))
            discrepancy_report_for_endread="<tr><td colspan=3><strong>No discrepancies</strong></td></tr>";
         else
             discrepancy_report_for_endread = discrepancy_report_html;
         return uireads;
    }
    
   
        
     //for sample report: converts end reads list int o uireads
    private ArrayList  createListOfUICloneSequences(IsolateTrackingEngine istr,int ref_seqid)
                         throws BecDatabaseException
    {
        ArrayList discrepancies = new ArrayList();
        ArrayList uiclonesequences = new ArrayList();
        String discrepancy_report_html = null;
        File  needle_file = null;
        UISequence uiclonesequence = null;CloneSequence clone_sequence = null;
         for (int seq_count = 0; seq_count < istr.getCloneSequences().size(); seq_count++)
        {
            clone_sequence = (CloneSequence) istr.getCloneSequences().get(seq_count);
            uiclonesequence = new UISequence();
            uiclonesequence.setId (clone_sequence.getId());
            uiclonesequence.setRefSequenceId (ref_seqid);
            uiclonesequence.setSequenceType (BaseSequence.CLONE_SEQUENCE);
            uiclonesequence.setAnalysisStatus( clone_sequence.getCloneSequenceStatus());
            uiclonesequence. setCloneSequenceType( clone_sequence.getCloneSequenceType());
//check wether file exists
             DiscrepancyFinder nv = new DiscrepancyFinder();
            String  needle_file_name = nv.getOutputDirectory()+"/needle"+uiclonesequence.getId()+
                                    "_"+ref_seqid+".out";
             needle_file = new File(needle_file_name);
            if ( needle_file.exists() )           
                uiclonesequence.setIsAlignmentExists (true);
            if ( clone_sequence.getDiscrepancies() != null && clone_sequence.getDiscrepancies().size() > 0)
            {
                uiclonesequence.setIsDiscrepancies ( true);
               
            }
            
            discrepancies = clone_sequence.getDiscrepancies() ;
            discrepancy_report_html = Mutation.HTMLReport( discrepancies, Mutation.LINKER_5P, true);
            discrepancy_report_html += Mutation.HTMLReport( discrepancies, Mutation.RNA, true);
             discrepancy_report_html += Mutation.HTMLReport( discrepancies, Mutation.LINKER_3P, true);
             if (discrepancy_report_html.equals(""))
                uiclonesequence.setDiscrepancyReport ("<tr><td colspan=3><strong>No discrepancies</strong></td></tr>");
             else
                  uiclonesequence.setDiscrepancyReport (discrepancy_report_html);
             uiclonesequences.add(uiclonesequence);
        }
       return uiclonesequences;
        
    }
                   
  public static void main(String args[])
    {
        try
        {
    //  ArrayList a = StretchCollection.createListOfUIContigs( 6206, Constants.ITEM_TYPE_ISOLATETRASCKING_ID) ;
    Hashtable stretch_collections = new Hashtable();
                     ArrayList items = Algorithms.splitString("123 6345 6947");
                     StretchCollection strcol = null; int cloneid = 0;
                     for (int index = 0; index < items.size();index++)
                     {
                         cloneid = Integer.parseInt( (String) items.get(index));
                       //  strcol = StretchCollection.getByCloneId(cloneid);
                         if ( strcol != null) 
                         {
                             strcol.setCloneId(cloneid);
                             strcol.setStretches( StretchCollection.createListOfUIContigs(strcol,Constants.ITEM_TYPE_CLONEID));
                              stretch_collections.put( (String) items.get(index), strcol);
                         }
                         else
                              stretch_collections.put( (String) items.get(index), "");
                        
                     }
        }
        catch(Exception e){}
      }
}
