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
import edu.harvard.med.hip.bec.*;
import edu.harvard.med.hip.bec.sampletracking.objects.*;
import edu.harvard.med.hip.bec.programs.needle.*;
import edu.harvard.med.hip.bec.sampletracking.mapping.*;

public class Seq_GetItemAction extends BecAction
{
    
    
    public ActionForward becPerform(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException
    {
     
        ActionErrors errors = new ActionErrors();
        int forwardName = ((Seq_GetSpecForm)form).getForwardName();
         request.setAttribute("forwardName", new Integer(forwardName));
         request.setAttribute(Constants.JSP_CURRENT_LOCATION, getPageLocation(forwardName));
          request.setAttribute(Constants.JSP_TITLE, getPageTitle(forwardName));
                   
        String label = null;int id = -1;Container container = null;
         try
        {
            if ( forwardName == Constants.CONTAINER_PROCESS_HISTORY || forwardName 
                ==Constants.CONTAINER_DEFINITION_INT ||
                forwardName == Constants.CONTAINER_RESULTS_VIEW ||
                forwardName == Constants.CONTAINER_ISOLATE_RANKER_REPORT ||
             //   forwardName ==Constants.PROCESS_PUT_CLONES_ON_HOLD ||
              //   forwardName == Constants.PROCESS_ACTIVATE_CLONES ||
                 forwardName == Constants.PROCESS_CHECK_READS_AVAILABILITY 
               //  || forwardName == Constants.PROCESS_APROVE_ISOLATE_RANKER
                 
                 )//rocessing from container label
               {
                     
                    label = (String)request.getParameter(Constants.CONTAINER_BARCODE_KEY);
                    label =label.toUpperCase().trim();
                   //   label =label.trim();
                 
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
             //  forwardName == Constants.CONSTRUCT_DEFINITION_REPORT ||
               forwardName == Constants.CLONE_SEQUENCE_DEFINITION_REPORT_INT ||
               forwardName == Constants.STRETCH_REPORT_INT ||
               forwardName == Constants.STRETCH_COLLECTION_REPORT_INT 
               )
               {
                   String str_id = (String) request.getParameter("ID");
                   if ( str_id != null )  id = Integer.parseInt(str_id);
                       
                }
            switch  (forwardName)
            {
                case Constants.STRETCH_COLLECTION_REPORT_INT:
                {
                     Hashtable stretch_collections = new Hashtable();
                     StretchCollection strcol = null; int cloneid = 0;
                    strcol = StretchCollection.getById(id);
                    int clone_id = Integer.parseInt((String)request.getParameter("cloneid"));
                    if ( strcol != null)
                    {
                        strcol.setStretches( StretchCollection.createListOfUIContigs(strcol,Constants.ITEM_TYPE_CLONEID));
                        if ( strcol.getType() == StretchCollection.TYPE_COLLECTION_OF_GAPS_AND_CONTIGS) 
                        {
                            stretch_collections.put( String.valueOf(clone_id), strcol);
                         }
                    }
                    ArrayList items = new ArrayList(); items.add(String.valueOf(clone_id));
                    request.setAttribute("stretch_collections",stretch_collections);
                    request.setAttribute("items", items);
                    request.setAttribute("caller", Constants.NO_MENU_TO_SHOW);
                    request.setAttribute("item_type", String.valueOf(Constants.ITEM_TYPE_CLONEID));
                    return mapping.findForward("display_stretch_collections");
                }
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
                     //get info for the most relevant sequence 
                    UICloneSample.setCloneSequences(ui_clones, null);
                    container.setSamples(ui_clones);
                    container.getCloningStrategyId();
                    request.setAttribute("container",container);
                    return (mapping.findForward("display_container_details"));
                }
               /* case Constants.PROCESS_APROVE_ISOLATE_RANKER:
                {
                    container.restoreSampleIsolateNoFlexInfo();
                    request.setAttribute("container",container);
                    request.setAttribute("rows", new Integer(8));
                    request.setAttribute("cols", new Integer(12));

                    return (mapping.findForward("display_isolate_ranker_report"));
                }*/
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
                        ArrayList ui_clones = container.restoreUISamples(container);
                        container.setSamples(ui_clones);
                        container.getCloningStrategyId();
                        request.setAttribute("container",container);
                        request.setAttribute("rows", new Integer(8));
                        request.setAttribute("cols", new Integer(12));
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
                    UICloneSample sample = null;
                    ArrayList ar =  UICloneSample.getCloneInfo( String.valueOf(id),Constants.ITEM_TYPE_ISOLATETRASCKING_ID,false, true);
                    if (ar != null && ar.size() > 0)
                         sample = (UICloneSample) ar.get(0);
                        //get clone sequences && end reads
                    ArrayList end_reads = Read.getReadByIsolateTrackingId( id );
                    String discrepancy_report_for_endread = null;
                    end_reads = createListOfUIReads(end_reads,discrepancy_report_for_endread, sample.getRefSequenceId());
                   
                    ArrayList  clone_sequences = CloneSequence.getAllByIsolateTrackingId(id, null,  null);
                    clone_sequences = createListOfUICloneSequences(clone_sequences, sample.getRefSequenceId());
                    
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
              /*  case Constants.CONSTRUCT_DEFINITION_REPORT:
                {
                    Construct construct = new Construct(id);
                    ArrayList clones_data = Construct.getClonesData(id);
                    request.setAttribute("clones_data",clones_data);
                    request.setAttribute("construct",construct);
             //        request.setAttribute("forwardName", new Integer(Constants.PROCESS_APROVE_ISOLATE_RANKER));
                    return (mapping.findForward("construct_report"));
                }
               **/
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
                    ArrayList discrepancies = new ArrayList();
                    if ( id == -1)//display subset of discrepancies
                    {
                        String discr_ids = (String) request.getParameter("DISCRIDS");
                        discr_ids = Algorithms.replaceChar( discr_ids, '_', ',');
                        if ( discr_ids.charAt(discr_ids.length() - 1) == ',') discr_ids = discr_ids.substring(0, discr_ids.length() - 1);
                        discrepancies = Mutation.getByIds(discr_ids);
                        //request.setAttribute("discrepancies",discrepancies);
                    }
                    else
                    {
                        AnalyzedScoredSequence sequence = new AnalyzedScoredSequence(id);
                
                        if ( sequence != null) 
                            discrepancies = sequence.getDiscrepancies() ;
                         request.setAttribute("sequence_id",String.valueOf( sequence.getId()));
                       // request.setAttribute("sequence",sequence);
                    }
                    //request.setAttribute("discrepancies",discrepancies);
                    request.setAttribute("discrepancies",DiscrepancyDescription.assembleDiscrepancyDefinitions(discrepancies));
     
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
            /*    case   Constants.PROCESS_PUT_CLONES_ON_HOLD :
                 case Constants.PROCESS_ACTIVATE_CLONES:
                {
                 //show label scan form  
                   
                    if ( forwardName == Constants.PROCESS_PUT_CLONES_ON_HOLD )
                    {
                        
                        title =  "Put Active Clones on Hold";
                    }
                    else if( forwardName == Constants.PROCESS_ACTIVATE_CLONES )
                    {
                        
                        title =  "Activate Clones";
                    }
                    
                    container.restoreSampleIsolate(false,true);
                    request.setAttribute("container",container);
                    request.setAttribute(Constants.JSP_TITLE,title);
                    return (mapping.findForward("show_activate_list"));
                }
             **/
                case Constants.AVAILABLE_LINKERS_DEFINITION_INT:
                {
                    ArrayList linkers = BioLinker.getAllLinkers();
                    request.setAttribute("linkers", linkers);
                    return (mapping.findForward("display_linker"));
                }
                case Constants.AVAILABLE_VECTORS_DEFINITION_INT:
                {
                    ArrayList vectors = BioVector.getAllVectors();
                    request.setAttribute("vectors", vectors);
                    return (mapping.findForward("display_vector"));
                }    
                case Constants.AVAILABLE_SPECIFICATION_INT:
                {
                    request.setAttribute("primer3", Spec.getAllSpecsByType(Spec.PRIMER3_SPEC_INT, false));
                    request.setAttribute("comparespec", Spec.getAllSpecsByType(Spec.END_READS_SPEC_INT, false));
                    request.setAttribute("bioevaluation", Spec.getAllSpecsByType(Spec.FULL_SEQ_SPEC_INT, false));
                    request.setAttribute("polymerphism", Spec.getAllSpecsByType(Spec.POLYMORPHISM_SPEC_INT, false));
                    request.setAttribute("slidingwindow", Spec.getAllSpecsByType(Spec.TRIM_SLIDING_WINDOW_SPEC_INT, false));
                    return (mapping.findForward("choose_spec"));
                }
                case Spec.PRIMER3_SPEC_INT * Spec.SPEC_DELETE_SPEC:
                {
                    request.setAttribute("primer3", Primer3Spec.getAllNotUsedSpecs( ));
                    request.setAttribute(Constants.JSP_TITLE,"Select Specification to delete");
                    request.setAttribute("forwardName", new Integer(Spec.SPEC_DELETE_SPEC));
                
                    return (mapping.findForward("choose_spec"));
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
                    request.setAttribute(Constants.JSP_TITLE,"Clone Data for Plate " + label);
                    return (mapping.findForward("show_clone_status_list"));
                }
                case Constants.AVAILABLE_CONTAINERS_INT:
                {
                    ArrayList labels = Container.findAllContainerLabels();
                    //sort labels
                    Collections.sort(labels, new Comparator()
                    {
                        public int compare(Object o1, Object o2)
                        {
                            return ((String) o1).compareTo( (String) o2);
                        }
                        public boolean equals(java.lang.Object obj)               {      return false;  }
                    } );
                    request.setAttribute("labels",labels);
                     request.setAttribute( Constants.ADDITIONAL_JSP, "<script language='JavaScript' src='"+edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") +"scripts.js'></script> ") ;
                    return (mapping.findForward("display_info"));
                     
                }
                case Constants.PROCESS_VIEW_OLIGO_PLATE:
                {
                    //label for oligo plate created by application, always in apper case
                     label = (String)request.getParameter(Constants.CONTAINER_BARCODE_KEY);
                    label =label.toUpperCase().trim();
                     
                     ArrayList oligo_containers = OligoContainer.findContainersInfoFromLabel(label, OligoContainer.MODE_NOTRESTORE_SAMPLES);
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
                     //set title
                    request.setAttribute("container",oligo_container);
                    return (mapping.findForward("display_oligo_container"));
                }
             
            
            }
            
        }
        catch (Exception e)
        {
           // System.out.println(e.getMessage());
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
    
   
    
     
    //for sample report: converts end reads list int o uireads
    private ArrayList  createListOfUIReads(ArrayList reads, String discrepancy_report_for_endread, int ref_seqid)
                        throws BecDatabaseException
    {
        ArrayList discrepancies = new ArrayList();
        ArrayList uireads = new ArrayList();
        UIRead uiread = null;Read read = null;
        DiscrepancyFinder nv = new DiscrepancyFinder();
        String  needle_file_name = null;
        File needle_file = null;
         for (int read_count = 0; read_count < reads.size(); read_count++)
        {
            read = (Read) reads.get(read_count);
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
    private ArrayList  createListOfUICloneSequences(ArrayList clone_sequences,int ref_seqid)
                         throws BecDatabaseException
    {
        ArrayList discrepancies = new ArrayList();
        ArrayList uiclonesequences = new ArrayList();
        String discrepancy_report_html = null;
        File  needle_file = null;
        UISequence uiclonesequence = null;CloneSequence clone_sequence = null;
         for (int seq_count = 0; seq_count < clone_sequences.size(); seq_count++)
        {
            clone_sequence = (CloneSequence)clone_sequences.get(seq_count);
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
          
    
    private String          getPageTitle(int forwardId)
    {
        switch(forwardId )
        {
             //   forwardName ==Constants.PROCESS_PUT_CLONES_ON_HOLD : return " ";
            //   case Constants.PROCESS_ACTIVATE_CLONES : return " ";
            case Constants.PROCESS_CHECK_READS_AVAILABILITY : return "End Reads Availability";
            //  : return " ";case Constants.PROCESS_APROVE_ISOLATE_RANKER

            case Constants.SCOREDSEQUENCE_DEFINITION_INT : return " ";
            case Constants.REFSEQUENCE_DEFINITION_INT : return " ";
            case  Constants.ANALYZEDSEQUENCE_DISCREPANCY_REPORT_DEFINITION_INT : return " ";
            case Constants.READSEQUENCE_NEEDLE_ALIGNMENT_INT : return " ";
            case Constants.VECTOR_DEFINITION_INT : return " ";
            case Constants.LINKER_DEFINITION_INT : return " ";
            case Constants.CLONING_STRATEGY_DEFINITION_INT : return "Cloning Strategy ";
            case Constants.SAMPLE_ISOLATE_RANKER_REPORT : return " ";
            case Constants.READ_REPORT_INT : return " ";
            //  case Constants.CONSTRUCT_DEFINITION_REPORT : return " ";
            case Constants.CLONE_SEQUENCE_DEFINITION_REPORT_INT : return " ";
            case Constants.STRETCH_REPORT_INT : return " ";
            
            case Constants.AVAILABLE_VECTORS_DEFINITION_INT: return "Available Vectors";
            case Constants.AVAILABLE_LINKERS_DEFINITION_INT: return "Available Linkers";
             case Constants.AVAILABLE_SPECIFICATION_INT: return "Available Process Configurations";
            case Constants.AVAILABLE_CONTAINERS_INT: return "Available Plates";
           case Constants.CONTAINER_PROCESS_HISTORY : return "Plate History "; 
            case Constants.CONTAINER_DEFINITION_INT : return "Plate Description ";
            case Constants.CONTAINER_RESULTS_VIEW : return "Plate Results ";
            case Constants.CONTAINER_ISOLATE_RANKER_REPORT : return "Plate Results";
           
            case Constants.STRETCH_COLLECTION_REPORT_INT:                return "View Gaps and Contigs";

             case Constants.PROCESS_VIEW_OLIGO_PLATE:return "View Oligo Plate";
              case Spec.PRIMER3_SPEC_INT * Spec.SPEC_DELETE_SPEC: return "Select Specification to delete";

            default: return "";
         }
    }
    
    
     private String          getPageLocation(int forwardId)
    {
        switch(forwardId )
        {
             case Constants.AVAILABLE_VECTORS_DEFINITION_INT: return "Home > View > Vectors";
           case Constants.AVAILABLE_LINKERS_DEFINITION_INT: return "Home > View >  Linkers";
           case Constants.AVAILABLE_SPECIFICATION_INT: return "Home > View >  Process Configurations";
            case Constants.AVAILABLE_CONTAINERS_INT: return "Home > View > Plates";
            case Constants.CONTAINER_PROCESS_HISTORY : return "Home > View > Plate History "; 
            case Constants.CONTAINER_DEFINITION_INT : return "Home > View > Plate Description ";
            case Constants.CONTAINER_RESULTS_VIEW : return "Home > View > Plate Results";
            case Constants.CONTAINER_ISOLATE_RANKER_REPORT : return "Home > View > Plate Results";
            //   forwardName ==Constants.PROCESS_PUT_CLONES_ON_HOLD : return " ";
            //   case Constants.PROCESS_ACTIVATE_CLONES : return " ";
            case Constants.PROCESS_CHECK_READS_AVAILABILITY : return "Home > Process > View Process Results > View Available End Reads";
            //  : return " ";case Constants.PROCESS_APROVE_ISOLATE_RANKER

            case Constants.SCOREDSEQUENCE_DEFINITION_INT : return " ";
            case Constants.REFSEQUENCE_DEFINITION_INT : return " ";
            case  Constants.ANALYZEDSEQUENCE_DISCREPANCY_REPORT_DEFINITION_INT : return " ";
            case Constants.READSEQUENCE_NEEDLE_ALIGNMENT_INT : return " ";
            case Constants.VECTOR_DEFINITION_INT : return " ";
            case Constants.LINKER_DEFINITION_INT : return " ";
            case Constants.CLONING_STRATEGY_DEFINITION_INT : return "Home > View Cloning Strategy ";
            case Constants.SAMPLE_ISOLATE_RANKER_REPORT : return " ";
            case Constants.READ_REPORT_INT : return " ";
            //  case Constants.CONSTRUCT_DEFINITION_REPORT : return " ";
            case Constants.CLONE_SEQUENCE_DEFINITION_REPORT_INT : return " ";
            case Constants.STRETCH_REPORT_INT : return " ";
            case Constants.STRETCH_COLLECTION_REPORT_INT : return " ";
            
            case Constants.PROCESS_VIEW_OLIGO_PLATE:return "Home > Process > View Process Results > View Oligo Plate";
            default: return "";
         }
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
                                 