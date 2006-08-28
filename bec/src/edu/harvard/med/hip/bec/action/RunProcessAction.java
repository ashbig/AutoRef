//Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->
/*
 * RunProcess.java
 *
 * Created on August 26, 2003, 3:27 PM
 */

package edu.harvard.med.hip.bec.action;

import java.util.*;

import java.sql.*;
import java.io.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;
import edu.harvard.med.hip.bec.ui_objects.*;

import  edu.harvard.med.hip.bec.programs.assembler.*;
import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.modules.*;
import edu.harvard.med.hip.bec.action_runners.*;
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.coreobjects.endreads.*;
import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.coreobjects.oligo.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.form.*;
import edu.harvard.med.hip.bec.user.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.Constants;
import edu.harvard.med.hip.bec.UI_Constants;
import edu.harvard.med.hip.bec.sampletracking.mapping.*;
import edu.harvard.med.hip.bec.sampletracking.objects.*;
import edu.harvard.med.hip.bec.util_objects.*;
/**
 *
 * @author  HTaycher
 */
public class RunProcessAction extends BecAction
{
    
    
    public ActionForward becPerform(ActionMapping mapping,    ActionForm form,    HttpServletRequest request,
                        HttpServletResponse response)    throws ServletException, IOException
    {
        // place to store errors
        ActionErrors errors = new ActionErrors();
        Thread t = null;
        Connection conn = null;
         ProcessRunner runner = null;
                   
     //   ArrayList master_container_ids = null;
      //  ArrayList master_container_labels = null;
        int   forwardName = ((Seq_GetSpecForm)form).getForwardName();
      request.setAttribute(Constants.JSP_TITLE,getTitleForProcess(forwardName));
                    request.setAttribute(Constants.JSP_CURRENT_LOCATION,getPageLocation(forwardName));
                    
        try
        {
            User user = (User)request.getSession().getAttribute(Constants.USER_KEY);
            DatabaseTransaction dt = DatabaseTransaction.getInstance();
            conn = dt.requestConnection();
     
            switch (forwardName)
            {
                case Constants.PROCESS_UPLOAD_PLATES : //upload plates
                {
                    
                    String  container_labels = (String) request.getParameter("plate_names");//get from form
                    String     start_codon =  (String)request.getParameter("start_codon");//get from form
                    String     fusion_stop_codon =  (String)request.getParameter("fusion_stop_codon");//get from form
                    String    closed_stop_codon =  (String)request.getParameter("closed_stop_codon");//get from form
                    
                    int     vectorid = Integer.parseInt( (String)request.getParameter(Constants.VECTOR_ID_KEY));//get from form
                    int     linker3id = Integer.parseInt( (String) request.getParameter("3LINKERID"));//get from form
                    int     linker5id = Integer.parseInt( (String)request.getParameter("5LINKERID"));//get from form
                    int     put_plate_for_step = Integer.parseInt( (String) request.getParameter("nextstep")); //get from form
                    int     project_id = Integer.parseInt((String) request.getParameter(UI_Constants.PROJECT_ID)); //get from form
                    //validate input
                    if (container_labels == null || container_labels.trim().equals("") )
                    {
                        errors.add(ActionErrors.GLOBAL_ERROR,  new ActionError("error.container.querry.parameter", "Please enter plate labels"));
                        saveErrors(request,errors);
                        return new ActionForward(mapping.getInput());
                    }
                    
                    request.setAttribute(Constants.ADDITIONAL_JSP,"Processing plates:\n"+container_labels);
                    
                      
                     runner = new PlateUploadRunner();
                     runner.setInputData(Constants.ITEM_TYPE_PLATE_LABELS,  container_labels);
                    ((PlateUploadRunner)runner).setVectorId(vectorid );
                    ((PlateUploadRunner)runner).setLinker3Id(linker3id);
                    ((PlateUploadRunner)runner).setLinker5Id(linker5id);
                    ((PlateUploadRunner)runner).setStartCodon(start_codon);
                    ((PlateUploadRunner)runner).setFusionStopCodon(fusion_stop_codon);
                    ((PlateUploadRunner)runner).setClosedStopCodon(closed_stop_codon);
                    ((PlateUploadRunner)runner).setNextStep(put_plate_for_step);
                    ((PlateUploadRunner)runner).setPlateInfoType(PlateUploader.PLATE_NAMES);
                    ((PlateUploadRunner)runner).setProjectId(project_id);
                   
                    runner.setProcessType(forwardName);
                    runner.setUser(user);
                    t = new Thread(runner);                    t.start();
                    break;
                }
               
                case Constants.PROCESS_RUN_END_READS : //run sequencing for end reads
                {
                    String[] labels = request.getParameterValues("chkLabel");
                    String plate_names = Algorithms.convertArrayToString(labels, " ");
                
                     
                    int forward_primer_id = Integer.parseInt( (String) request.getParameter("5p_primerid"));
                    int reverse_primer_id = Integer.parseInt( (String) request.getParameter("3p_primerid"));
                    
                    request.setAttribute(Constants.ADDITIONAL_JSP,"Processing plates "+ plate_names);
                    
                     runner = new EndReadsRequestRunner();
                    runner.setInputData(Constants.ITEM_TYPE_PLATE_LABELS , plate_names );
                    if ( forward_primer_id != -1) ((EndReadsRequestRunner)runner).setForwardPrimerId( forward_primer_id );
                    if ( reverse_primer_id != -1)((EndReadsRequestRunner)runner).setRevercePrimerId(reverse_primer_id);
                    runner.setUser(user);
                    runner.setProcessType(forwardName);
                 
                    t = new Thread(runner);           t.start();
                    
                    return mapping.findForward("processing");
                    
                }
                
         /*       case Constants.PROCESS_RUN_ISOLATE_RUNKER : //run isolate runker
                {
                    String[] labels = request.getParameterValues("chkLabel");
                     String plate_names = Algorithms.convertArrayToString(labels, " ");
                
                    
                  //  master_container_ids = Container.findContainerIdsFromLabel(master_container_labels);
                    request.setAttribute(Constants.ADDITIONAL_JSP,"Processing plates "+ plate_names.toString());
                    
                    int bioeval_spec_id = Integer.parseInt( (String) request.getParameter(Spec.FULL_SEQ_SPEC));
                    int endread_spec_id = Integer.parseInt( (String) request.getParameter(Spec.END_READS_SPEC));
                     runner = new IsolateRankerRunner();
                    // ((IsolateRankerRunner)runner).setContainerLabels(master_container_labels );
                   // runner.setContainerIds(master_container_ids );
                    runner.setInputData(Constants.ITEM_TYPE_PLATE_LABELS, plate_names );
                     ((IsolateRankerRunner)runner).setCutoffValuesSpec( (FullSeqSpec)Spec.getSpecById(bioeval_spec_id, Spec.FULL_SEQ_SPEC_INT));
                     ((IsolateRankerRunner)runner).setPenaltyValuesSpec( (EndReadsSpec)Spec.getSpecById(endread_spec_id, Spec.END_READS_SPEC_INT));
                    runner.setUser(user);
                   runner.setProcessType(forwardName);
                 
                    t = new Thread(runner);           t.start();
                    break;
                }*/
                //primer design items
                case Constants.PROCESS_ADD_NEW_INTERNAL_PRIMER : // add new internal primer
                case Constants.PROCESS_APPROVE_INTERNAL_PRIMERS :
                case Constants.PROCESS_VIEW_INTERNAL_PRIMERS : //view internal primers
                {
                    String  item_ids = (String) request.getParameter("items");
                    item_ids = item_ids.trim();
                     item_ids = item_ids.toUpperCase().trim();
                //    item_ids = item_ids.trim();
                    int item_type = Integer.parseInt(request.getParameter("item_type"));
                    
                    item_ids = ProcessRunner.cleanUpItems(item_type, item_ids);
                     ArrayList oligo_calculations = new ArrayList();
                     ArrayList items = Algorithms.splitString(item_ids);
                     ArrayList oligo_calculations_per_item = new ArrayList();
                   
                     int type_of_coverage = Integer.parseInt(request.getParameter(PrimerDesignerRunner.STRETCH_PRIMERS_APNAME_SEQUENCE_COVERAGE_TYPE)  );
                     boolean isLatestStretchCollection = true;
                     if ( forwardName == Constants.PROCESS_VIEW_INTERNAL_PRIMERS )
                     {
                         isLatestStretchCollection = ( Constants.STRETCH_COLLECTION_REPORT_INT == Integer.parseInt(request.getParameter(PrimerDesignerRunner.STRETCH_PRIMERS_APNAME_COLLECTIONS_TYPE)));
                     }
                     for (int index = 0; index < items.size();index++)
                     {
                          oligo_calculations_per_item = OligoCalculation.getOligoCalculations((String)items.get(index),item_type, type_of_coverage, isLatestStretchCollection);
                         oligo_calculations.add( oligo_calculations_per_item);
                     }
                    
                    if ( forwardName == Constants.PROCESS_APPROVE_INTERNAL_PRIMERS)
                    {
                       request.setAttribute("forwardName",new Integer(-forwardName));
                       //store all approved primer's ids for disapproval
                       OligoCalculation ol = null;Oligo oligo = null;
                       ArrayList forwardAllApprovedPrimerIds = new ArrayList();
                       for (int index = 0; index < oligo_calculations.size();index++)
                     {
                        oligo_calculations_per_item = (ArrayList)oligo_calculations.get(index);
                        for (int ol_count = 0; ol_count < oligo_calculations_per_item.size();ol_count++)
                        {
                            ol = (OligoCalculation)oligo_calculations_per_item.get(ol_count);
                            for (int oligo_count = 0 ; oligo_count < ol.getOligos().size(); oligo_count++)
                            {
                                oligo = (Oligo)ol.getOligos().get(oligo_count);
                                if (oligo.getStatus() == Oligo.STATUS_APPROVED && !forwardAllApprovedPrimerIds.contains(String.valueOf ( oligo.getId())))
                                    forwardAllApprovedPrimerIds.add(String.valueOf ( oligo.getId()) );
                            }
                        }

                     }
                       request.setAttribute("forwardAllApprovedPrimerIds",Algorithms.convertStringArrayToString( forwardAllApprovedPrimerIds,Constants.DELIM_WHITE_SPACE));
                     
                    }
                    request.setAttribute("oligo_calculations",oligo_calculations);
                    request.setAttribute("items",items);
                    request.setAttribute("item_type",request.getParameter("item_type"));
                    return mapping.findForward("display_oligo_calculations");
                }
                
                case Constants.STRETCH_COLLECTION_REPORT_INT:
                case Constants.STRETCH_COLLECTION_REPORT_ALL_INT:
                {
                     String  item_ids = (String) request.getParameter("items");
                 //   item_ids = item_ids.trim();
                    item_ids = item_ids.toUpperCase().trim();
                    int item_type = Integer.parseInt(request.getParameter("item_type"));
                     item_ids = ProcessRunner.cleanUpItems(item_type, item_ids);
                   
                    
                     Hashtable stretch_collections = new Hashtable();
                     ArrayList items = Algorithms.splitString(item_ids);
                     StretchCollection strcol = null; int cloneid = 0;
                     if ( items != null )
                     {
                         for (int index = 0; index < items.size();index++)
                         {
                             cloneid = Integer.parseInt( (String) items.get(index));
                             if ( forwardName == Constants.STRETCH_COLLECTION_REPORT_ALL_INT)
                             {
                                 ArrayList str_colections = StretchCollection.getAllByCloneId(cloneid);
                                 ArrayList str_colections_of_right_type = null;
                                 if ( str_colections != null )
                                 {
                                     for ( int count = 0; count < str_colections.size(); count++)
                                     {
                                         strcol = (StretchCollection) str_colections.get(count);
                                         if ( strcol.getType() == StretchCollection.TYPE_COLLECTION_OF_GAPS_AND_CONTIGS) 
                                         {
                                             if ( str_colections_of_right_type == null) str_colections_of_right_type = new ArrayList();
                                             str_colections_of_right_type.add(strcol);
                                             strcol.setStretches( StretchCollection.createListOfUIContigs(strcol,Constants.ITEM_TYPE_CLONEID));
                                         }
                                       
                                     }
                                     if ( str_colections_of_right_type!= null)
                                         stretch_collections.put( (String) items.get(index), str_colections_of_right_type);
                                 }
                             }
                             
                             else
                             {
                                strcol = StretchCollection.getLastByCloneId(cloneid);
                                if ( strcol != null)
                                {
                                    strcol.setStretches( StretchCollection.createListOfUIContigs(strcol,Constants.ITEM_TYPE_CLONEID));
                                    if ( strcol.getType() == StretchCollection.TYPE_COLLECTION_OF_GAPS_AND_CONTIGS) 
                                    {
                                        stretch_collections.put( (String) items.get(index), strcol);
                                       
                                       
                                    }
                                }
                             }
                     }
                     }
                     request.setAttribute("stretch_collections",stretch_collections);
                    request.setAttribute("items",items);
                    request.setAttribute("item_type",request.getParameter("item_type"));
                    return mapping.findForward("display_stretch_collections");
                }
                case Constants.LQR_COLLECTION_REPORT_INT:
                {
                    String  item_ids = (String) request.getParameter("items");
                    item_ids = item_ids.toUpperCase().trim();
                    //item_ids = item_ids.trim();
                   
                    int item_type = Integer.parseInt(request.getParameter("item_type"));
                     item_ids = ProcessRunner.cleanUpItems(item_type, item_ids);
                   
                     ArrayList items = Algorithms.splitString(item_ids);
                     StretchCollection lqr_for_clone = null; 
                     int cloneid = 0;
                     CloneSequence clone_sequence = null;
                     //we trick system here writing html table 
                     Hashtable display_items = new Hashtable();
                     if ( items != null)
                     {
                         for (int index = 0; index < items.size();index++)
                         {

                                cloneid = Integer.parseInt( (String) items.get(index));
                                clone_sequence = CloneSequence.getOneByCloneId(cloneid);
                                if ( clone_sequence != null)
                                {
                                    lqr_for_clone = StretchCollection.getByCloneSequenceId(clone_sequence.getId() , false);
                                    if ( lqr_for_clone != null && lqr_for_clone.getType() == StretchCollection.TYPE_COLLECTION_OF_LQR) 
                                    {
                                        StretchCollection.prepareStretchCollectionForDisplay(lqr_for_clone,clone_sequence, clone_sequence.getCdsStop() - clone_sequence.getCdsStart());
                                        display_items.put( items.get(index), lqr_for_clone);
                                    }
                                }

                         }
                     }
                    request.setAttribute("lqr_clone_stretch_collections",display_items);
                     request.setAttribute("items",items);
                    request.setAttribute("item_type",request.getParameter("item_type"));
                    return mapping.findForward("display_lqr_collections");
                }
                
                // approve primer
                case -Constants.PROCESS_APPROVE_INTERNAL_PRIMERS :
                {
                   
                    String[] primers = request.getParameterValues("chkPrimer");
                    String old_approved_primers = (String) request.getParameter("forwardAllApprovedPrimerIds");
                 //   System.out.println(old_approved_primers);
                    ArrayList arr_old_approved_primers = null;
                   if (old_approved_primers != null) 
                        arr_old_approved_primers = Algorithms.splitString(old_approved_primers,Constants.DELIM_WHITE_SPACE);
                    String primer_ids_report = "";
                    String primer_ids_approved = "";
                    String primer_ids_failed = "";String primer_ids_disapproved ="";
                    int primer_id = -1;
                    //prepare statements
                    PreparedStatement pst_update_primer_status = null;
                    PreparedStatement pst_insert_process_object = null;
                    int process_id =  -1;
                    if (primers != null || arr_old_approved_primers != null)
                    {
                          process_id = Request.createProcessHistory( conn, ProcessDefinition.RUN_OLIGO_APPROVAL, new ArrayList(),user) ;
                         pst_update_primer_status = conn.prepareStatement("update geneoligo set status = ? where oligoid = ? ");
                          pst_insert_process_object = conn.prepareStatement("insert into process_object (processid,objectid,objecttype) values("+process_id+",?,"+Constants.PROCESS_OBJECT_TYPE_OLIGO_ID+")");
                    }
                    if(primers != null)
                    {
                        
                        for (int i = 0; i < primers.length; i ++)
                        {
                           // System.out.println("item "+primers[i]);
                            if (arr_old_approved_primers != null && arr_old_approved_primers.contains(primers[i]))
                            {
                                arr_old_approved_primers.remove(primers[i]);
                              //  System.out.println("remove "+primers[i]);
                                continue;
                            }
                            primer_id = Integer.parseInt( primers[i]);
                            primer_ids_report += primers[i] + "\n";
                            try
                            {
                              // System.out.println("approve "+primer_id);
                                pst_update_primer_status.setInt(2,primer_id);
                                pst_update_primer_status.setInt(1,Oligo.STATUS_APPROVED);
                                DatabaseTransaction.executeUpdate(pst_update_primer_status);
                                pst_insert_process_object.setInt(1,primer_id);
                                DatabaseTransaction.executeUpdate(pst_insert_process_object);
                                primer_ids_approved += primer_id + "\n";
                                conn.commit();
                            }
                            catch(Exception e)
                            {
                               primer_ids_failed += primer_id + "<P>";
                            }
                         }
                    }
          //disapprove unselected primers
                    for (int i = 0; i < arr_old_approved_primers.size(); i ++)
                    {
                       try
                       {
                            primer_id = Integer.parseInt( (String)arr_old_approved_primers.get(i));
                          //  System.out.println("disapprove "+primer_id);
                            pst_update_primer_status.setInt(2,primer_id);
                            pst_update_primer_status.setInt(1,Oligo.STATUS_DESIGNED);
                            DatabaseTransaction.executeUpdate(pst_update_primer_status);
                            pst_insert_process_object.setInt(1,primer_id);
                            DatabaseTransaction.executeUpdate(pst_insert_process_object);
                            primer_ids_disapproved += primer_id + "\n";
                       }
                       catch(Exception e)
                       {
                           primer_ids_failed += primer_id + "<P>";
                       }
                    }
                    String report = "Processing "+primer_ids_report +"  primers. <P>"+"Primer ids approved "+primer_ids_approved+". <P>"+
                    "Primer ids disapproved "+primer_ids_disapproved+".<P>Primer ids failed "+primer_ids_failed+"\n\n";
                    request.setAttribute(Constants.ADDITIONAL_JSP,report);
                    conn.commit();
                    break;
                }
                
             // three go togeteher : donot separate
                case Constants.PROCESS_PROCESS_OLIGO_PLATE:
               
                case Constants.PROCESS_RUN_PRIMER3: //run primer3
                case Constants.PROCESS_RUNPOLYMORPHISM_FINDER: //run polymorphism finder                
                case Constants.PROCESS_RUN_DISCREPANCY_FINDER: //run discrepancy finder
                case Constants.PROCESS_RUN_ASSEMBLER_FOR_ALL_READS:
                case Constants.PROCESS_ORDER_INTERNAL_PRIMERS:
                case Constants.PROCESS_NOMATCH_REPORT:
                case Constants.PROCESS_FIND_GAPS:
                case Constants.PROCESS_FIND_LQR_FOR_CLONE_SEQUENCE:
                case Constants.PROCESS_RUN_END_READS_WRAPPER:
                case Constants.PROCESS_RUN_ASSEMBLER_FOR_END_READS:    
                case Constants.PROCESS_CREATE_ORDER_LIST_FOR_ER_RESEQUENCING  :
                case Constants.PROCESS_CREATE_ORDER_LIST_FOR_INTERNAL_RESEQUENCING  :
                case Constants.PROCESS_CREATE_REPORT_TRACEFILES_QUALITY:
                case Constants.PROCESS_DELETE_PLATE :
                case Constants.PROCESS_DELETE_CLONE_READS://
                case Constants.PROCESS_DELETE_CLONE_FORWARD_READ ://
                case Constants.PROCESS_DELETE_CLONE_REVERSE_READ ://
                case Constants.PROCESS_DELETE_CLONE_SEQUENCE://
                case  Constants.PROCESS_GET_TRACE_FILE_NAMES :
                case Constants.PROCESS_CLEANUP_INTERMIDIATE_FILES_FROM_HARD_DRIVE:
                 case  Constants.PROCESS_SET_CLONE_FINAL_STATUS:
                case Constants.PROCESS_REANALYZE_CLONE_SEQUENCE:
                      case Constants.PROCESS_RUN_ISOLATE_RUNKER : //run isolate runker
               // case  Constants.PROCESS_DELETE_TRACE_FILES :
  
                {
                     switch (forwardName)
                    {
                        case Constants.PROCESS_DELETE_PLATE :
                        case Constants.PROCESS_DELETE_CLONE_READS://
                        case Constants.PROCESS_DELETE_CLONE_FORWARD_READ ://
                        case Constants.PROCESS_DELETE_CLONE_REVERSE_READ ://
                        case Constants.PROCESS_DELETE_CLONE_SEQUENCE://
                        case  Constants.PROCESS_GET_TRACE_FILE_NAMES :
                        case Constants.PROCESS_REANALYZE_CLONE_SEQUENCE:
                         case Constants.PROCESS_CLEANUP_INTERMIDIATE_FILES_FROM_HARD_DRIVE:
                        {
                              runner = new DeleteObjectRunner();
                             if (forwardName == Constants.PROCESS_REANALYZE_CLONE_SEQUENCE)
                            {
                                ((DeleteObjectRunner)runner).setDiscrepancyQualityCutOff(Integer.parseInt( (String)request.getParameter("FS_PHRED_CUT_OFF")));
                            }
                              break;
                          
                        }
                          case Constants.PROCESS_RUN_ISOLATE_RUNKER : //run isolate runker
                        {
                            int bioeval_spec_id = Integer.parseInt( (String) request.getParameter(Spec.FULL_SEQ_SPEC));
                            int endread_spec_id = Integer.parseInt( (String) request.getParameter(Spec.END_READS_SPEC));
                             runner = new IsolateRankerRunner();
                            ((IsolateRankerRunner)runner).setCutoffValuesSpec( (FullSeqSpec)Spec.getSpecById(bioeval_spec_id, Spec.FULL_SEQ_SPEC_INT));
                            ((IsolateRankerRunner)runner).setPenaltyValuesSpec( (EndReadsSpec)Spec.getSpecById(endread_spec_id, Spec.END_READS_SPEC_INT));
                    
                            break;
                        }
                        case Constants.PROCESS_CREATE_ORDER_LIST_FOR_ER_RESEQUENCING  :
                        case Constants.PROCESS_CREATE_ORDER_LIST_FOR_INTERNAL_RESEQUENCING  :
                            case Constants.PROCESS_CREATE_REPORT_TRACEFILES_QUALITY:
                        {
                              runner = new SpecialReportsRunner();
                              ((SpecialReportsRunner)runner).setReportType(forwardName);
                              break;//run end reads wrapper
                         }
                         case Constants.PROCESS_RUN_END_READS_WRAPPER:
                         {
                              runner = new EndReadsWrapperRunner();
                              break;//run end reads wrapper
                         }
                        case Constants.PROCESS_RUN_ASSEMBLER_FOR_END_READS:
                        {
                            //run assembly wrapper
                             runner = new AssemblyRunner();
                               ((AssemblyRunner)runner).setAssemblyMode(AssemblyRunner.END_READS_ASSEMBLY);
                             ((AssemblyRunner)runner).setResultType( String.valueOf(IsolateTrackingEngine.PROCESS_STATUS_ER_PHRED_RUN));
                              ((AssemblyRunner)runner).setVectorFileName( request.getParameter("isRunVectorTrimming"));
                                ((AssemblyRunner)runner).setQualityTrimmingScore ( Integer.parseInt( (String)request.getParameter(PhredPhrap.QUALITY_TRIMMING_SCORE)));
                                ((AssemblyRunner)runner).setQualityTrimmingLastBase ( Integer.parseInt( (String) request.getParameter(PhredPhrap.QUALITY_TRIMMING_LAST_BASE)));
                               ((AssemblyRunner)runner).setQualityTrimmingFirstBase ( Integer.parseInt( (String)request.getParameter(PhredPhrap.QUALITY_TRIMMING_FIRST_BASE)));
                                 ((AssemblyRunner)runner).setIsUseLQReadsForAssembly( request.getParameter(PhredPhrap.LQREADS_USE_FOR_ASSEMBLY) != null );
                              ((AssemblyRunner)runner).setIsDeleteLQReads( request.getParameter(PhredPhrap.LQREADS_DELETE) != null );
                               break;
                        }
                        
                         case Constants.PROCESS_RUN_PRIMER3: //run primer3
                         {
                              runner = new PrimerDesignerRunner();
                             int spec_id = Integer.parseInt( request.getParameter("PRIMER3_SPEC"));
                              ((PrimerDesignerRunner)runner).setSpecId(spec_id);
      if ( request.getParameter("isTryMode") != null )  
          ((PrimerDesignerRunner)runner).setIsTryMode( true );
                             // System.out.println(Integer.parseInt(request.getParameter("typeSequenceCoverage")) );
      ((PrimerDesignerRunner)runner).setTypeOfSequenceCoverage( Integer.parseInt(request.getParameter(PrimerDesignerRunner.STRETCH_PRIMERS_APNAME_SEQUENCE_COVERAGE_TYPE))  );
     ((PrimerDesignerRunner)runner).setIsLQRCoverageType( Integer.parseInt(request.getParameter(PrimerDesignerRunner.STRETCH_PRIMERS_APNAME_LQR_COVERAGE_TYPE)) );
     ((PrimerDesignerRunner)runner).setMinDistanceBetweenStretchesToBeCombined( Integer.parseInt(request.getParameter(PrimerDesignerRunner.STRETCH_PRIMERS_APNAME_MIN_DISTANCE_BETWEEN_STRETCHES)) ); 
                              break;
                         }
                        case Constants.PROCESS_ORDER_INTERNAL_PRIMERS:
                        {
                             runner = new PrimerOrderRunner();
                             if ( request.getParameter("oligo_placement_format")  != null)
                            (   (PrimerOrderRunner)runner).setPrimerPlacementFormat( Integer.parseInt(request.getParameter("oligo_placement_format") ));
                              if ( request.getParameter("primer_number") != null)
                                    ((PrimerOrderRunner)runner).setPrimerNumber( Integer.parseInt(request.getParameter("primer_number") ));
                             if (request.getParameter("oligo_grouping_rule") != null)
                                    ((PrimerOrderRunner)runner).setPrimersSelectionRule(Integer.parseInt(request.getParameter("oligo_grouping_rule") ));
                             if ( request.getParameter("first_well") != null)
                             {
                                String fwell = request.getParameter("first_well");
                                int  fposition = edu.harvard.med.hip.bec.sampletracking.objects.Container.convertPositionFrom_alphanumeric_to_int(fwell);
                                
                               ((PrimerOrderRunner)runner).setFirstWell( fposition );
                             }
                               
                             if ( request.getParameter("last_well") != null)
                             {
                                 String lwell = request.getParameter("last_well");
                                 int  lposition = edu.harvard.med.hip.bec.sampletracking.objects.Container.convertPositionFrom_alphanumeric_to_int(lwell);
                                ((PrimerOrderRunner)runner).setLastWell( lposition );
                             }
if ( request.getParameter("primer_sequence") != null)((PrimerOrderRunner)runner).setPrimerSequenceColumn (Integer.parseInt(request.getParameter("primer_sequence") ));
if ( request.getParameter("primer_name") != null)((PrimerOrderRunner)runner).setPrimerNameColumn  (Integer.parseInt(request.getParameter("primer_name") ));

if ( request.getParameter("primer_column") != null)((PrimerOrderRunner)runner).setPrimerColumnColumn (Integer.parseInt(request.getParameter("primer_column") ));
if ( request.getParameter("primer_row") != null)((PrimerOrderRunner)runner).setPrimerowColumn  (Integer.parseInt(request.getParameter("primer_row") ));
if ( request.getParameter("plate_name") != null)((PrimerOrderRunner)runner).setPlateNameColumn (Integer.parseInt(request.getParameter("plate_name") ));
((PrimerOrderRunner)runner).setEmptySequenceDisplay  (request.getParameter("empty_sequence" ));
((PrimerOrderRunner)runner).setEmptySequenceName  (request.getParameter("empty_sequence_name" ));
((PrimerOrderRunner)runner).setNumberOfOrderFiles  (Integer.parseInt(request.getParameter("number_of_files") ));
                             
                             if ( request.getParameter("isTryMode") != null )
                                 ((PrimerOrderRunner)runner).setIsTryMode(true  );
                            break;
                        }
                        case Constants.PROCESS_RUN_ASSEMBLER_FOR_ALL_READS:
                        {
                             runner = new AssemblyRunner();
                             ((AssemblyRunner)runner).setAssemblyMode(AssemblyRunner.FULL_SEQUENCE_ASSEMBLY);
                             ((AssemblyRunner)runner).setVectorFileName( request.getParameter("isRunVectorTrimming"));
                              ((AssemblyRunner)runner).setQualityTrimmingScore ( Integer.parseInt( (String)request.getParameter(PhredPhrap.QUALITY_TRIMMING_SCORE)));
                                ((AssemblyRunner)runner).setQualityTrimmingLastBase ( Integer.parseInt( (String) request.getParameter(PhredPhrap.QUALITY_TRIMMING_LAST_BASE)));
                               ((AssemblyRunner)runner).setQualityTrimmingFirstBase ( Integer.parseInt( (String)request.getParameter(PhredPhrap.QUALITY_TRIMMING_FIRST_BASE)));
                                ((AssemblyRunner)runner).setIsUseLQReadsForAssembly( request.getParameter(PhredPhrap.LQREADS_USE_FOR_ASSEMBLY) != null );
                              ((AssemblyRunner)runner).setIsDeleteLQReads( request.getParameter(PhredPhrap.LQREADS_DELETE) != null );
                              break;
                        }
                        case Constants.PROCESS_RUNPOLYMORPHISM_FINDER:
                         {
                              runner = new PolymorphismFinderRunner();
                              ((PolymorphismFinderRunner)runner).setSpecId(Integer.parseInt( (String)request.getParameter("POLYMORPHISM_SPEC")));
                              break;
                        }//run polymorphism finder
                        case Constants.PROCESS_RUN_DISCREPANCY_FINDER:
                        {
                             runner = new DiscrepancyFinderRunner();
                             ((DiscrepancyFinderRunner)runner).setDiscrepancyQualityCutOff(Integer.parseInt( (String)request.getParameter("FS_PHRED_CUT_OFF")));
                              break;
                        }//run discrepancy finder
                        
                        case Constants.PROCESS_NOMATCH_REPORT:
                        {
                             runner = new NoMatchReportRunner();
                             ((NoMatchReportRunner)runner).setBlastableDBName( (String) request.getParameter("DATABASE_NAME")) ;
                              ((NoMatchReportRunner)runner).setIdTypeToDisplay( (String) request.getParameter("ID_NAME")) ;
                              break;
                        }
                        
                        case Constants.PROCESS_FIND_GAPS:
                        case Constants.PROCESS_FIND_LQR_FOR_CLONE_SEQUENCE:
                        {
                              runner = new GapMapperRunner();
                              int spec_id = Integer.parseInt( request.getParameter("SLIDING_WINDOW_SPEC"));
                              ((GapMapperRunner)runner).setSpecId(spec_id);
                              if ( request.getParameter("isTryMode") != null )  ((GapMapperRunner)runner).setIsTryMode( true );
                               if ( forwardName == Constants.PROCESS_FIND_GAPS)
                              {
                                    ((GapMapperRunner)runner).setVectorFileName( request.getParameter("isRunVectorTrimming"));
                                    ((GapMapperRunner)runner).setQualityTrimmingScore ( Integer.parseInt( (String)request.getParameter(PhredPhrap.QUALITY_TRIMMING_SCORE)));
                                    ((GapMapperRunner)runner).setQualityTrimmingLastBase ( Integer.parseInt( (String) request.getParameter(PhredPhrap.QUALITY_TRIMMING_LAST_BASE)));
                                    ((GapMapperRunner)runner).setQualityTrimmingFirstBase ( Integer.parseInt( (String)request.getParameter(PhredPhrap.QUALITY_TRIMMING_FIRST_BASE)));
                                    ((GapMapperRunner)runner).setIsUseLQReadsForAssembly( request.getParameter(PhredPhrap.LQREADS_USE_FOR_ASSEMBLY) != null );
                                    ((GapMapperRunner)runner).setIsDeleteLQReads( request.getParameter(PhredPhrap.LQREADS_DELETE) != null );
                                     if ( request.getParameter("isRunLQR") != null )                 ((GapMapperRunner)runner).setIsRunLQR(true);
                               }
                                  
                                if ( forwardName == Constants.PROCESS_FIND_LQR_FOR_CLONE_SEQUENCE)
                               {
                                   ((GapMapperRunner)runner).setNumberOfBasesCoveredByForwardER( Integer.parseInt( (String)request.getParameter(UI_Constants.GAP_MAPPER_NUMBER_OF_BASES_COVERED_FORWARDER )));
                                   ((GapMapperRunner)runner).setNumberOfBasesCoveredByReverseER( Integer.parseInt( (String)request.getParameter(UI_Constants.GAP_MAPPER_NUMBER_OF_BASES_COVERED_REVERSEER)));

                                }
                             
                              break;
                        } 
                        case Constants.PROCESS_PROCESS_OLIGO_PLATE:
                        {
                            runner = new OligoPlateProcessor_Runner();
                            (( OligoPlateProcessor_Runner) runner).setOrderComment( (String)request.getParameter("order_comments"));
                            (( OligoPlateProcessor_Runner)runner).setSequencingComment( (String)request.getParameter("sequencing_comments") );
                            ((OligoPlateProcessor_Runner)runner).setPlateStatus(Integer.parseInt( (String)request.getParameter("status")));

                            break;
                        }
                        case  Constants.PROCESS_SET_CLONE_FINAL_STATUS:
                        {
                            runner = new CloneManipulationRunner();
                            (( CloneManipulationRunner) runner).setProcessType(forwardName);
                            (( CloneManipulationRunner) runner).setIsCreateDistributionFile(  request.getParameter("isCreateDistributionFile") != null );
                           (( CloneManipulationRunner) runner).setCloneFinalStatus( Integer.parseInt((String)request.getParameter("CLONE_FINAL_STATUS")));
                            break;
                        }
                        
                    }
                    
                    String  item_ids = (String) request.getParameter("items");
                    String items = item_ids.toUpperCase().trim();
                    if ( forwardName == Constants.PROCESS_RUN_ISOLATE_RUNKER ) //run isolate runker
                    {
                            request.setAttribute(Constants.ADDITIONAL_JSP,"Processing plates "+ items);
                    }   
                  //  String items = item_ids.trim();
                    int items_type =  Integer.parseInt(request.getParameter("item_type"));
                
                    runner.setInputData(items_type,items);
                    runner.setUser(user);
                       runner.setProcessType(forwardName);
                                              
                    t = new Thread(runner);                    t.start();
                    request.setAttribute(Constants.ADDITIONAL_JSP,"Processing items:<P>"+ runner.getItems());
                    break;
                   
                   // runner(  request,  forwardName,  user);
                }
                
                case Constants.PROCESS_SHOW_CLONE_HISTORY:
                {
                    String  item_ids = (String) request.getParameter("items");
                    item_ids=ProcessRunner.cleanUpItems(Constants.ITEM_TYPE_CLONEID, item_ids);
                    ArrayList process_items = edu.harvard.med.hip.bec.util_objects.ProcessHistory.getProcessHistory( Integer.parseInt(request.getParameter("item_type")), item_ids.toUpperCase().trim());
                     request.setAttribute("process_items",process_items);
                    request.setAttribute("item_type",request.getParameter("item_type"));
                    return mapping.findForward("display_item_history"); 
                }
                case Constants.PROCESS_VIEW_OLIGO_ORDER_BY_CLONEID:
                {
                    String  item_ids = (String) request.getParameter("items");
                    int items_type =  Integer.parseInt(request.getParameter("item_type"));
                
                    ArrayList clone_ids = Algorithms.splitString(ProcessRunner.cleanUpItems( items_type,  item_ids));
                    ArrayList item_descriptions = UI_GeneOligo.getByCloneId(clone_ids);
                    request.setAttribute("processing_items", item_descriptions);
                    return mapping.findForward("display_oligo_order_for_clone"); 
                }
                case Constants.PROCESS_RUN_DECISION_TOOL : //run decision tool
                {
                    //get spec
                    int bioeval_spec_id = Integer.parseInt( (String) request.getParameter(Spec.FULL_SEQ_SPEC));
                    runner = new DecisionToolRunner();
                    ((DecisionToolRunner)runner).setSpecId(bioeval_spec_id);
                    String  item_ids = (String) request.getParameter("items");
                    //String items = item_ids.trim();
                    String items = item_ids.toUpperCase().trim();
                    int items_type =  Integer.parseInt(request.getParameter("item_type"));
                    runner.setInputData(items_type,items);
                       runner.setProcessType(forwardName);
                 
                    runner.setUser(user);
                    runner.run();
                   
                    ArrayList clone_data = ((DecisionToolRunner)runner).getClones();
                    request.setAttribute("clone_data",clone_data );
                    return mapping.findForward("decision_tool_report");
                }
               
               
           


  //two go together : donot separate           
  /*              case Constants.PROCESS_ACTIVATE_CLONES :
                case Constants.PROCESS_PUT_CLONES_ON_HOLD :  //put clones on hold
                {
                    String chkStr[] = request.getParameterValues("chkClone");
                    int istr_id = -1; String sql = null;
                    if (chkStr != null)
                    {
                        for (int i = 0; i < chkStr.length; i++)
                        {
                            istr_id = Integer.parseInt(chkStr[i]);
                            sql = "update isolatetracking set status=-status where iso latetrackingid="+ istr_id;
                            DatabaseTransaction.executeUpdate(sql, conn);
                            
                        }
                        conn.commit();
                    }
                    request.setAttribute(Constants.JSP_TITLE,"Request for clones status change" );
                    request.setAttribute(Constants.ADDITIONAL_JSP,"For " +chkStr.length+ " clones from plate "+ request.getParameter("containerLabel") +" status have been changes" );
                    return mapping.findForward("processing");
                }
   **/
                case Constants.PROCESS_CREATE_REPORT:
                {
                    String  item_ids = (String) request.getParameter("items");
                    runner = new ReportRunner();
                   // String items = item_ids.trim();
                    String items = item_ids.toUpperCase().trim();
                    int items_type =  Integer.parseInt(request.getParameter("item_type"));
                    runner.setInputData(items_type,items);
                 
                    // value="2"//Clone Ids</strong//
                    ((ReportRunner)runner).setFields(
                        request.getParameter("clone_id"), //    Clone Id
                        request.getParameter("dir_name"), // Directory Name
                        request.getParameter("clone_final_status"), //      Sample Id
                        request.getParameter("plate_label"), //      Plate Label
                        request.getParameter("sample_type"), //      Sample Type
                        request.getParameter("position"), //      Sample Position
                        request.getParameter("ref_sequence_id"), //      Sequence ID
                        request.getParameter("clone_seq_id"), //      Clone Sequence Id
                        request.getParameter("ref_cds_start"), //      CDS Start
                        request.getParameter("clone_status"),//      Clone Sequence Analysis Status
                        request.getParameter("ref_cds_stop"), //      CDS Stop
                        request.getParameter("clone_discr_high"), //    Discrepancies High Quality (separated by type)
                        request.getParameter("ref_cds_length"), //      CDS Length
                        request.getParameter("clone_disc_low"), //   Discrepancies Low Quality (separated by type)
                        request.getParameter("ref_gc"), //     GC Content
                        request.getParameter("ref_seq_text"), //      Sequence Text
                        request.getParameter("ref_cds"), //     CDS
                        request.getParameter("ref_gi"), //      GI Number
                        request.getParameter("ref_gene_symbol"), //      Gene Symbol
                        request.getParameter("ref_species_id"), //      PA Number (for Pseudomonas project only)
                        request.getParameter("ref_ids"), //      SGA Number (for Yeast project only)
                        request.getParameter("rank") ,
                        request.getParameter("read_length"), //      end reads length
                        request.getParameter("score"),
                        request.getParameter("clone_seq_cds_start"),
                        request.getParameter("clone_seq_cds_stop"),
                        request.getParameter("clone_seq_text"),
                        request.getParameter("assembly_attempt_status"));
                    runner.setUser(user);
                         runner.setProcessType(forwardName);
                 
                    t = new Thread( runner);                    t.start();
                    request.setAttribute(Constants.ADDITIONAL_JSP,"Processing items:<P>"+ runner.getItems());
                    break;
                }
                 case Constants.PROCESS_RUN_DECISION_TOOL_NEW:
                {
                    String  item_ids = (String) request.getParameter("items");
                    runner = new DecisionToolRunner_New();
                    //String items = item_ids.trim();
                    String items = item_ids.trim();
                    int items_type =  Integer.parseInt(request.getParameter("item_type"));
                  
                    if ( items_type != Constants.ITEM_TYPE_PROJECT_NAME)
                    { items =    items.toUpperCase();}
                    runner.setInputData(items_type,items);
                    runner.setUser(user);
                    runner.setProcessType(forwardName);
                 
                    int bioeval_spec_id = Integer.parseInt( (String) request.getParameter(Spec.FULL_SEQ_SPEC));
                    ((DecisionToolRunner_New)runner).setSpecId(bioeval_spec_id);
                    ((DecisionToolRunner_New)runner).setUserComment(request.getParameter("user_comment"));
                    ((DecisionToolRunner_New)runner).setNumberOfOutputFiles(Integer.parseInt( (String) request.getParameter("output_format")));
                     // value="2"//Clone Ids</strong//
                    ((DecisionToolRunner_New)runner).setFields(
                       request.getParameter("clone_final_status" ),
                        request.getParameter("plate_label" ),//    Plate Label</td>
                        request.getParameter("sample_type" ),//    Sample Type</td>
                        request.getParameter("position" ),//    Well</td>
                        request.getParameter("ref_sequence_id" ),//    Sequence ID</td>
                        request.getParameter("clone_seq_id" ),//    Clone Sequence Id</td>
                        request.getParameter("ref_cds_start" ),//    CDS Start</td>
                        request.getParameter("clone_sequence_assembly_status" ),//    Clone Sequence assembly attempt status    </td>
                        request.getParameter("ref_cds_stop" ),//    CDS Stop</td>
                        request.getParameter("clone_sequence_analysis_status" ),//Clone Sequence Analysis Status  </td>
                        request.getParameter("ref_cds_length" ),//    CDS Length</td>
                        request.getParameter("clone_sequence_cds_start" ),//Cds Start</td>
                        request.getParameter("ref_seq_text" ),//   Sequence Text</td>
                        request.getParameter("clone_seq_text" ),// Cds Stop </td>
                        
                        request.getParameter("ref_seq_cds" ),//    CDS</td>
                        request.getParameter("clone_sequence_text" ),//Clone Sequence  </td>
                        request.getParameter("clone_sequence_cds"),
                        request.getParameter("ref_gene_symbol" ),// Gene Symbol</td>
                        request.getParameter("clone_sequence_disc_high" ),// Discrepancies High Quality (separated by type)</td>
                        request.getParameter("ref_gi" ),//    GI Number</td>
                        request.getParameter("clone_sequence_disc_low"),
                        request.getParameter("ref_5_linker" ),//5' linker sequence    </td>
                        request.getParameter("clone_sequence_disc_det"), //Detailed Discrepancy Report </td>
                        request.getParameter("ref_3_linker" ),//   3' linker sequence</td>
                        request.getParameter("ref_species_id" ),//Species specific ID</td>
                        request.getParameter("ref_ids" ),//All available identifiers</td>
                        request.getParameter("is_forward_er_uploaded" ),
                        request.getParameter("is_reverse_er_uploaded"),//	Reverse read uploaded
                        request.getParameter("is_ordered_internals_oligos"),//	Number of ordered internal primers
                        request.getParameter("is_internal_traces"),//	Number of internal trace files
                        request.getParameter("is_gaps_last_stretch_collection"),//	Number of gaps in last stretch collection;
                        request.getParameter("is_lqd_last_assembly")//	Number of lqr in last assembled clone sequence
                        );
                    
                    t = new Thread( runner);                    t.start();
                     request.setAttribute(Constants.ADDITIONAL_JSP,"Processing items:<P>"+ runner.getItems());
                    break;
                }
               
            }
            
            return mapping.findForward("processing");
        }
        
        
               
        catch (Exception e)
        {
            try
            {
                conn.rollback();
               // System.out.println(e.getMessage());
                request.setAttribute(Action.EXCEPTION_KEY, e);
                return (mapping.findForward("error"));
            } catch (Exception e1)
            {
                request.setAttribute(Action.EXCEPTION_KEY, e);
                return (mapping.findForward("error"));
            }
        }
        finally
        {
            if(conn != null)            DatabaseTransaction.closeConnection(conn);
        }
    }
    
    
    private String          getTitleForProcess(int forwardName)
    {
             switch (forwardName)
            {
                case Constants.PROCESS_UPLOAD_PLATES :  return "Request for Plates Upload";
                case Constants.PROCESS_RUN_END_READS :  return "Request for End Read Sequencing";
                case Constants.PROCESS_RUN_ISOLATE_RUNKER :  return "Request for Run Isolate Ranker " ;
                case Constants.PROCESS_APPROVE_INTERNAL_PRIMERS: return "Request for Approve Internal Primers";
                case Constants.PROCESS_VIEW_INTERNAL_PRIMERS: return "View Internal Primers";
                case Constants.PROCESS_ADD_NEW_INTERNAL_PRIMER : return "Add new Internal Primers";
                case Constants.STRETCH_COLLECTION_REPORT_INT:return "View Latest Contig Collection";
                
                 case Constants.STRETCH_COLLECTION_REPORT_ALL_INT:return "View All Contig Collections";
                case Constants.LQR_COLLECTION_REPORT_INT: return   "View Low Confidence Regions for clone sequences";
                
                 case -Constants.PROCESS_APPROVE_INTERNAL_PRIMERS : return "Request for Approve Internal Primers " ;
                case Constants.PROCESS_CREATE_ORDER_LIST_FOR_INTERNAL_RESEQUENCING  : return "Request for Order List for resequencing of Internal Reads";
                case Constants.PROCESS_RUN_END_READS_WRAPPER:return "Request for Check Quality and Distribute End Reads";
                case Constants.PROCESS_RUN_ASSEMBLER_FOR_END_READS:return "Request for Run Assembly from End Reads.";
                case Constants.PROCESS_RUN_PRIMER3: return"Request for Run Primer Designer";
                case Constants.PROCESS_ORDER_INTERNAL_PRIMERS:return "Request for Primer Order";
                case Constants.PROCESS_RUN_ASSEMBLER_FOR_ALL_READS:return "Request for Clone Sequence Assembly";
                case Constants.PROCESS_RUNPOLYMORPHISM_FINDER:return "Request for Run Polymorphism Finder";
                case Constants.PROCESS_RUN_DISCREPANCY_FINDER:return "Request for Run Discrepancy Finder";
                case Constants.PROCESS_FIND_GAPS: return "Request for Run Gap Mapper ";
                case Constants.PROCESS_FIND_LQR_FOR_CLONE_SEQUENCE: return"Request for Find Low Confidence  Regions in Clone Sequences";
                case Constants.PROCESS_PROCESS_OLIGO_PLATE:return "Request for Track Oligo Plate";
                case  Constants.PROCESS_SET_CLONE_FINAL_STATUS: return "Request for Set Clone Status";
                case Constants.PROCESS_SHOW_CLONE_HISTORY: return "Clone History";
                
                 case Constants.PROCESS_VIEW_OLIGO_ORDER_BY_CLONEID: return "Oligos Ordered for Clone";
                
                 
                 case Constants.PROCESS_RUN_DECISION_TOOL : return "Decision Tool Report" ;
                case Constants.PROCESS_CREATE_REPORT: return "Request for General Report";
                case Constants.PROCESS_RUN_DECISION_TOOL_NEW: return "Detailed Decision Tool Request";
                  case Constants.PROCESS_NOMATCH_REPORT:return "Mismatched Clones Report";
                 case Constants.PROCESS_CREATE_REPORT_TRACEFILES_QUALITY  :return "Request for Trace File Quality Report";
               case Constants.PROCESS_CREATE_ORDER_LIST_FOR_ER_RESEQUENCING  : return "Request for End Read Report"; 
               
               
                case Constants.PROCESS_DELETE_PLATE :return  "Request to Delete Plate ";
                case Constants.PROCESS_DELETE_CLONE_READS:return  "Request to Delete  Clone Forward and Reverse End Reads from Database"; 
                case Constants.PROCESS_DELETE_CLONE_FORWARD_READ :return  "Request to Delete  Clone Forward End Reads from Database";
                case Constants.PROCESS_DELETE_CLONE_REVERSE_READ :return  "Request to Delete Clone   Reverse End Reads from Database";
                case Constants.PROCESS_DELETE_CLONE_SEQUENCE:return  "Request to Delete Clone Sequence"; 
                case  Constants.PROCESS_DELETE_TRACE_FILES : return "Request to  Delete Trace Files from Hard Drive";
                case  Constants.PROCESS_MOVE_TRACE_FILES: return"Move Trace Files from Clone Directory into Temporary Directory";
                case Constants.PROCESS_REANALYZE_CLONE_SEQUENCE:return"Request to Reanalyze Clone Sequence"; 
                case  Constants.PROCESS_GET_TRACE_FILE_NAMES :return"Request to Get Trace File Names";
                 case Constants.PROCESS_CLEANUP_INTERMIDIATE_FILES_FROM_HARD_DRIVE: return"Request for Clean-up of hard drive";
                 default: return "";
            }
       
    }
   private String          getPageLocation(int forwardName)
    {
             switch (forwardName)
            {
 case Constants.PROCESS_UPLOAD_PLATES :  return "Home > Process > Upload Plates ";

 case Constants.PROCESS_RUN_END_READS :  return "Home > Process > Evaluate Clones -> Request End Reads Sequencing";
case Constants.PROCESS_RUN_ISOLATE_RUNKER :  return "Home > Process > Clone Evaluation > Run Isolate Ranker" ;
case Constants.PROCESS_APPROVE_INTERNAL_PRIMERS: return "Home > Process > Clone Evaluation > Approve Internal Primers";
case Constants.PROCESS_ADD_NEW_INTERNAL_PRIMER : return "Home > Process > Clone Evaluation > Add Internal Primers";
case -Constants.PROCESS_APPROVE_INTERNAL_PRIMERS : return "Home > Process > Clone Evaluation > Approve  Internal Primers" ;
case Constants.PROCESS_CREATE_ORDER_LIST_FOR_INTERNAL_RESEQUENCING  : return "Request for Order List for resequencing of Internal Reads";
case Constants.PROCESS_RUN_END_READS_WRAPPER:return "Home > Process > Clone Evaluation > Check Quality and Distribute End Reads";
case Constants.PROCESS_RUN_ASSEMBLER_FOR_END_READS:return "Home > Process > Clone Evaluation > Run Assembler for End Reads.";
case Constants.PROCESS_RUN_PRIMER3: return"Home > Process > Clone Evaluation > Run Primer Designer";
case Constants.PROCESS_ORDER_INTERNAL_PRIMERS:return "Home > Process > Clone Evaluation > Order Internal Primers";
case Constants.PROCESS_RUN_ASSEMBLER_FOR_ALL_READS:return "Home > Process > Clone Evaluation > Run Assembler";
case Constants.PROCESS_RUNPOLYMORPHISM_FINDER:return "Home > Process > Clone Evaluation > Run Polymorphism Finder";
case Constants.PROCESS_RUN_DISCREPANCY_FINDER:return "Home > Process > Clone Evaluation > Run Discrepancy Finder";
case Constants.PROCESS_FIND_GAPS: return "Home > Process > Clone Evaluation > Run Gap Mapper ";
case Constants.PROCESS_FIND_LQR_FOR_CLONE_SEQUENCE: return"Home > Process > Clone Evaluation > Find Low Confidence  Regions in Clone Sequences";
case Constants.PROCESS_PROCESS_OLIGO_PLATE:return "Home > Process > Clone Evaluation > Track Oligo Plate";
case  Constants.PROCESS_SET_CLONE_FINAL_STATUS: return "Home > Process > Evaluate Clones > Set Clone Status";
                
                 case Constants.PROCESS_SHOW_CLONE_HISTORY: return "View > Clone History";
                
case Constants.PROCESS_VIEW_OLIGO_ORDER_BY_CLONEID: return "Home > Process > View Results > View Oligo Order(s) for Clone(s)";
case Constants.PROCESS_VIEW_INTERNAL_PRIMERS: return "Home > Process > View Results > View Internal Primers";
case Constants.STRETCH_COLLECTION_REPORT_INT:return "Home > Process > View Results > View Latest Contig Collection";
case Constants.STRETCH_COLLECTION_REPORT_ALL_INT:return "Home > Process > View Results > View All Contig Collections";
case Constants.LQR_COLLECTION_REPORT_INT: return   "Home > Process > View Results > View Low Confidence  Regions for Clone Sequences";

case Constants.PROCESS_RUN_DECISION_TOOL : return "Home > Reports > Quick Decision Tool" ;
case Constants.PROCESS_CREATE_REPORT: return "Home > Reports > General Report";
case Constants.PROCESS_RUN_DECISION_TOOL_NEW: return "Home > Reports > Detailed Decision Tool";
case Constants.PROCESS_CREATE_REPORT_TRACEFILES_QUALITY  :return "Home > Reports > Trace File Quality";
case Constants.PROCESS_CREATE_ORDER_LIST_FOR_ER_RESEQUENCING  : return "Home > Reports > End Read Report"; 
case Constants.PROCESS_NOMATCH_REPORT:return "Home > Reports > Mismatched Clones ";
                 
                         case  Constants.PROCESS_DELETE_TRACE_FILES : return "Home > Process > Delete data  > Delete Trace Files from Hard Drive";
            case  Constants.PROCESS_MOVE_TRACE_FILES: return"Home > Process > Delete data > Move Trace Files from Clone Directory into Temporary Directory";
            case Constants.PROCESS_DELETE_PLATE :  return "Home > Process > Delete data > Delete Plate";
            case Constants.PROCESS_DELETE_CLONE_READS : return "Home > Process > Delete data > Delete Clones Forward and Reverse End Reads";
            case Constants.PROCESS_DELETE_CLONE_FORWARD_READ :return"Home > Process > Delete data > Delete Clones Forward End Reads";
            case Constants.PROCESS_DELETE_CLONE_REVERSE_READ : return"Home > Process > Delete data > Delete Clones Reverse End Reads";
            case Constants.PROCESS_DELETE_CLONE_SEQUENCE : return"Home > Process > Delete data > Delete Clone Sequence";
            case Constants.PROCESS_REANALYZE_CLONE_SEQUENCE:return"Home > Process > Delete data > Reanalyze Clone Sequence "; 
            case  Constants.PROCESS_GET_TRACE_FILE_NAMES :return"Home > Process > Delete data > Get Trace File Names";
                 case Constants.PROCESS_CLEANUP_INTERMIDIATE_FILES_FROM_HARD_DRIVE: return "Home > Process > Delete data > Clean-up hard drive";
                 default: return "";
            }
       
    }
}
