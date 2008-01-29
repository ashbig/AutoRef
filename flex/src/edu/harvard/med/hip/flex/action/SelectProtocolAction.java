 /*
  * SelectProtocolAction.java
  *
  * This class gets all the queue items for a selected protocol.
  *
  * Created on June 12, 2001, 4:10 PM
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
import org.apache.struts.action.*;

import org.apache.struts.util.MessageResources;

import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.util.*;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.form.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.workflow.*;
import edu.harvard.med.hip.flex.Constants;

/**
 *
 * @author  dzuo
 * @version
 */
public class SelectProtocolAction extends FlexAction {
    
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
        
        String processname = ((PickColonyForm)form).getProcessname();
        int workflowid = ((PickColonyForm)form).getWorkflowid();
        int projectid = ((PickColonyForm)form).getProjectid();
        String projectname = ((PickColonyForm)form).getProjectname();
        
        request.setAttribute("workflowid", new Integer(workflowid));
        request.setAttribute("projectid", new Integer(projectid));
        request.setAttribute("projectname", projectname);
        request.setAttribute(Constants.PROTOCOL_NAME_KEY, processname);
        request.setAttribute("processname", processname);
        
        
        ProcessQueue queue = null;
        LinkedList items = null;
        
        try {
            
            
            Project project = new Project(projectid);
            Workflow workflow = new Workflow(workflowid);
            request.setAttribute("workflowname", workflow.getName());
            
            Protocol protocol = new Protocol(processname);
            // System.out.println(protocol.getId() +"_"+ project.getId()+"_"+ workflow.getId());
            
            if(Protocol.GENERATE_PCR_PLATES.equals(processname) ||
            Protocol.DILUTE_OLIGO_PLATE.equals(processname)) {
                queue = new PlatesetProcessQueue();
                items = queue.getQueueItems(protocol, project, workflow);
                storeInSession(request, items, protocol);
                return (mapping.findForward("success_pcr"));
                
            } else if(Protocol.CREATE_SEQ_PLATES.equals(processname)) {
                request.getSession().removeAttribute("SelectProtocolAction.protocol");
                request.getSession().setAttribute("SelectProtocolAction.protocol", protocol);
                return (mapping.findForward("success_sequencing"));
            } else if (Protocol.MGC_DESIGN_CONSTRUCTS.equals(processname)) {
                SequenceOligoQueue seqQueue = new SequenceOligoQueue();
                LinkedList seqList = seqQueue.getQueueItems(protocol, project, workflow);
                
                
                int numOfSeqs = seqList.size();
                
                //  checks for duplicates on queue
                LinkedList tmp =new LinkedList();
                LinkedList duplicates = new LinkedList();
                LinkedList seqNoDuplicates = new LinkedList();
                for (int seq_count = 0; seq_count < seqList.size(); seq_count++) {
                    int id =  ((Sequence)seqList.get(seq_count)).getId()  ;
                    if ( tmp.contains( new Integer(id) ) )
                        duplicates.add(seqList.get(seq_count));
                    else {
                        tmp.add(new Integer(id));
                        seqNoDuplicates.add(seqList.get(seq_count));
                    }
                }
                
                
                
                if (duplicates.size() != 0)//there are duplicates
                {
                    request.setAttribute("duplicatedSequences", duplicates);
                    request.setAttribute("number_of_duplicates",  new Integer(duplicates.size() ));
                    request.setAttribute("sequences_count", new Integer(numOfSeqs));
                    request.getSession().setAttribute("not_duplicated_sequences", seqNoDuplicates);
                    return (mapping.findForward("success_mgc_process_duplicate_plates"));
                }
                
                
                ArrayList plates = new ArrayList();
                ArrayList  plates_no_marker = new ArrayList();
                //get total number of genes in queue
                if (numOfSeqs != 0) {
                    try {
                        Rearrayer ra = new Rearrayer(new ArrayList(seqList), 94);
                        ra.setRearrayByMarker(true);
                        plates = ra.getPlates();
                        ra.setRearrayByMarker(false);
                        plates_no_marker = ra.getPlates();
                        
                    }
                    catch(Exception e) {
                        request.setAttribute(Action.EXCEPTION_KEY, e);
                        System.out.println(e.getMessage());
                        return (mapping.findForward("error"));
                    }
                }
                request.setAttribute("platesInfo_no_marker", plates_no_marker);
                request.setAttribute("full_plates_no_marker", new Integer( plates_no_marker.size() ));
                request.setAttribute("platesInfo_marker", plates);
                request.setAttribute("full_plates_marker", new Integer( plates.size() ));
                // request.setAttribute("platesInfo", plates);
                // request.setAttribute("full_plates", new Integer( plates.size() ));
                request.setAttribute("sequences_count", new Integer(numOfSeqs));
                return (mapping.findForward("success_mgc_process_full_plates"));
            }
            else if (Protocol.REARRAY_PLATES_BASED_ON_SEQUENCING_RESULTS.equals(processname)) {
                //check if all plates recieved
                
                SequenceOligoQueue seqQueue = new SequenceOligoQueue();
                Protocol protocol_prev = new Protocol(Protocol.RECEIVE_SEQUENCING_RESULTS);
                items = seqQueue.getQueueItems(protocol_prev, project, workflow);
                if (items.size() > 0) {
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.notallplates.recieved"));
                    saveErrors(request, errors);
                    return new ActionForward(mapping.getInput());
                }
                
                return (mapping.findForward("success_rearray"));
                
            }
            else if (Protocol.REARRAY_PCR_PLATES.equals(processname)) {
                
                 return (mapping.findForward("success_rearray"));
                
            }
            else if (Protocol.UPLOAD_CONTAINERS_FROM_FILE.equals(processname))
            {
                 request.setAttribute("processid", new Integer(protocol.getId()));
                 return (mapping.findForward("success_upload_containers_from_file"));
                
            }
            else {
                queue = new ContainerProcessQueue();
                
            }
            items = queue.getQueueItems(protocol, project, workflow);
            
            storeInSession(request, items, protocol);
            
            if(Protocol.GENERATE_CULTURE_BLOCKS_FOR_ISOLATES.equals(processname)) {
                
                return (mapping.findForward("success_culture"));
            }
            else if(Protocol.PICK_COLONY.equals(processname)) {
                return (mapping.findForward("success_pick_colony"));
            }
            else if(Protocol.ENTER_MGC_CULTURE_RESULTS.equals(processname)) {
                request.setAttribute(Constants.PROTOCOL_NAME_KEY, processname);
                return (mapping.findForward("success_enter_result"));
            }
            
            else if(Protocol.RECEIVE_SEQUENCING_RESULTS.equals(processname) ) {
                
                request.setAttribute("protocolid", new Integer(protocol.getId()));
                DateFormatter date = new DateFormatter();
                String currentDate = date.getDateString();
                request.setAttribute("receiveDate", currentDate);
                return (mapping.findForward("success_recieve_plates"));
            } 
            else if(
                    Protocol.GENERATE_GLYCEROL_PLATES.equals(processname) ||
                    Protocol.GENERATE_GLYCEROL_PLATES_FROM_PLATES_WITH_CLONES.equals(processname) )
            {
                return (mapping.findForward("success_glycerol"));
            } else {
                return (mapping.findForward("success"));
            }
        } catch (FlexDatabaseException ex) {
            System.out.println(ex.getMessage());
            request.setAttribute(Action.EXCEPTION_KEY, ex);
            return (mapping.findForward("error"));
        }
    }
    
    public void storeInSession(HttpServletRequest request, LinkedList items, Protocol protocol) {
        //remove the attributes from the session.
        if(request.getSession().getAttribute("SelectProtocolAction.queueItems") != null)
            request.getSession().removeAttribute("SelectProtocolAction.queueItems");
        
        request.getSession().removeAttribute("SelectProtocolAction.protocol");
        
        if(items.size() > 0) {
            request.getSession().setAttribute("SelectProtocolAction.queueItems", items);
        }
        
        request.getSession().setAttribute("SelectProtocolAction.protocol", protocol);
    }
}
