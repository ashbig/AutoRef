/*
 * GetResearcherAction.java
 *
 * Created on June 26, 2001, 10:27 AM
 */

package edu.harvard.med.hip.flex.action;

import java.util.*;
import java.sql.*;
import java.io.*;
import java.lang.Thread;
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

import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.form.GetResearcherBarcodeForm;
import edu.harvard.med.hip.flex.process.Process;
import edu.harvard.med.hip.flex.workflow.*;
import static edu.harvard.med.hip.flex.workflow.Workflow.WORKFLOW_TYPE;
import edu.harvard.med.hip.flex.util.*;
import edu.harvard.med.hip.flex.special_projects.*;

/**
 *
 * @author  dzuo
 * @version
 */
public class GetResearcherAction extends ResearcherAction{
    public final static String BLAST_BASE_DIR=FlexProperties.getInstance().getProperty("flex.repository.basedir");
    public final static String BARCODEFILE = BLAST_BASE_DIR+"barcode/barcode.txt";
    //public final static String BARCODEFILE = "G:\\dev\\barcode.txt";
    
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
    public synchronized ActionForward flexPerform(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException {
        ActionErrors errors = new ActionErrors();
        String barcode = ((GetResearcherBarcodeForm)form).getResearcherBarcode();
        int workflowid = ((GetResearcherBarcodeForm)form).getWorkflowid();
        int projectid = ((GetResearcherBarcodeForm)form).getProjectid();
        int writeBarcode = ((GetResearcherBarcodeForm)form).getWriteBarcode();
        
        Researcher researcher = null;
        
        // Validate the researcher barcode.
        try {
            researcher = new Researcher(barcode);
        } catch (FlexProcessException ex) {
            request.setAttribute("workflowid", new Integer(workflowid));
            request.setAttribute("projectid", new Integer(projectid));
            
            errors.add("researcherBarcode", new ActionError("error.researcher.invalid.barcode", barcode));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        } catch (FlexDatabaseException ex) {
            request.setAttribute(Action.EXCEPTION_KEY, ex);
            return (mapping.findForward("error"));
        }
        
        Vector newContainers = (Vector)request.getSession().getAttribute("EnterSourcePlateAction.newContainers");
        Vector oldContainers = (Vector)request.getSession().getAttribute("EnterSourcePlateAction.oldContainers");
        LinkedList items = (LinkedList)request.getSession().getAttribute("EnterSourcePlateAction.items");
        Protocol protocol = (Protocol)request.getSession().getAttribute("SelectProtocolAction.protocol");
        Vector sampleLineageSet = (Vector)request.getSession().getAttribute("EnterSourcePlateAction.sampleLineageSet");
        SubProtocol subprotocol = (SubProtocol)request.getSession().getAttribute("EnterSourcePlateAction.subprotocol");
        String executionStatus = edu.harvard.med.hip.flex.process.Process.SUCCESS;
 // System.out.println("nc "+newContainers.size());      
        if(workflowid == Workflow.EXPRESSION_WORKFLOW) {
            if(newContainers == null || oldContainers == null ||
            protocol == null || sampleLineageSet == null) {
                return (mapping.findForward("fail"));
            }
        } else {
            if(newContainers == null || oldContainers == null ||
            items == null || protocol == null || sampleLineageSet == null ||
            subprotocol == null) {
                return (mapping.findForward("fail"));
            }
        }
        
        Connection conn = null;
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            
            // Insert the new containers and samples into database.
            for(int i=0; i<newContainers.size(); i++) {
                Container newContainer = (Container)newContainers.elementAt(i);
  //System.out.println("nc "+newContainer.getLabel());      
                newContainer.insert(conn);
            }
            
            if(Protocol.CREATE_CULTURE_FROM_MGC.equals(protocol.getProcessname())) {
                Container c = (Container)oldContainers.elementAt(0);
                MgcContainer mgcContainer = MgcContainer.findMGCContainerWithThread(c.getLabel().substring(3));
                if(mgcContainer == null) {
                    request.setAttribute("workflowid", new Integer(workflowid));
                    request.setAttribute("projectid", new Integer(projectid));
                    DatabaseTransaction.rollback(conn);
                    errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("error.mgc.notfound"));
                    saveErrors(request, errors);
                    return (new ActionForward(mapping.getInput()));
                }
                
                mgcContainer.updateCultureAndGlycerolAndDnaContainer(((Container)newContainers.elementAt(0)).getId(), ((Container)newContainers.elementAt(1)).getId(), ((Container)newContainers.elementAt(2)).getId(), conn);
                
                /**
                 * if("MGS".equals(c.getLabel().substring(0, 3))) {
                 * ((Container)oldContainers.elementAt(0)).setLocation(new Location(Location.DESTROYED));
                 * }
                 */
            }
            
            // update the location of the old container.
            for(int i=0; i<oldContainers.size(); i++) {
                Container oldContainer = (Container)oldContainers.elementAt(i);
                oldContainer.updateLocation(oldContainer.getLocation().getId(), conn);
            }
            
            Workflow workflow = new Workflow(workflowid);
            Project project = new Project(projectid);
            WorkflowManager manager = new WorkflowManager(project, workflow, "ProcessPlateManager");
            
            if(Protocol.CREATE_CULTURE_FROM_MGC.equals(protocol.getProcessname())) {
                Vector in = new Vector();
                Vector out = new Vector();
                in.addElement((Container)newContainers.elementAt(0));
                out.addElement((Container)newContainers.elementAt(1));
                
                // insert process record for creating culture from MGC
                manager.createProcessRecord(executionStatus, protocol, researcher,
                subprotocol, oldContainers, in, null, sampleLineageSet, conn);
                
                // insert process record for creating glycerol from culture
                Protocol p = new Protocol(Protocol.CREATE_GLYCEROL_FROM_CULTURE);
                Vector sls = (Vector)request.getSession().getAttribute("EnterSourcePlateAction.sls");
                manager.createProcessRecord(executionStatus, p, researcher,
                subprotocol, in, out, null, sls, conn);
                
                // insert process record for creating DNA from culture
                out.clear();
                out.addElement((Container)newContainers.elementAt(2));
                p = new Protocol(Protocol.CREATE_DNA_FROM_MGC_CULTURE);
                Vector sls1 = (Vector)request.getSession().getAttribute("EnterSourcePlateAction.sls1");
                manager.createProcessRecord(executionStatus, p, researcher,
                subprotocol, in, out, null, sls1, conn);
                
                manager.processQueue(items, in, protocol, conn);
            } else {
                manager.createProcessRecord(executionStatus, protocol, researcher,
                subprotocol, oldContainers, newContainers,
                null, sampleLineageSet, conn);
                manager.processQueue(items, newContainers, protocol, conn);
            }
            
            // Print the barcode
            for(int i=0; i<newContainers.size(); i++) {
                Container newContainer = (Container)newContainers.elementAt(i);
                String status = PrintLabel.execute(newContainer.getLabel());
     //           System.out.println("Printing barcode: "+status);
            }
 //System.out.println("nc "+BARCODEFILE);      
             
            //Print the barcode to the file.
            if(writeBarcode == 1) {
                PrintWriter pr = new PrintWriter(new BufferedWriter(new FileWriter(BARCODEFILE)));
                
                for(int i=0; i<oldContainers.size(); i++) {
                    Container oldContainer = (Container)oldContainers.elementAt(i);
                    pr.println(oldContainer.getLabel());
                }
                
                for(int i=0; i<newContainers.size(); i++) {
                    Container newContainer = (Container)newContainers.elementAt(i);
                    pr.println(newContainer.getLabel());
                }
                
                pr.close();
                request.setAttribute("writeBarcode", new Integer(1));
            }
            
            // Commit the changes to the database.
        //     why here???    DatabaseTransaction.commit(conn);
            //DatabaseTransaction.rollback(conn);
           //insert into summary table if it is the glycerol stock plate.
            if(Protocol.GENERATE_GLYCEROL_PLATES.equals(protocol.getProcessname()))
            {
                int strategyid = CloningStrategy.getStrategyid(project.getId(), workflow.getId());  
//System.out.println(strategyid);         
                List containerids = new ArrayList();
                List seqContainers = new ArrayList();
                for(int i=0; i<newContainers.size(); i++) {
                    Container newContainer = (Container)newContainers.elementAt(i);
                    if(newContainer.getLabel().substring(1,3).equals("GS")) {
                        containerids.add(new Integer(newContainer.getId()));
                    } else {
                        seqContainers.add(newContainer);
                    }
                }
                
                String isMappingFile = ((GetResearcherBarcodeForm)form).getIsMappingFile();
                boolean b = false;
                if("Yes".equals(isMappingFile)) {
                    b = true;
                }
                if(workflow.getWorkflowType()== WORKFLOW_TYPE.TRANSFER_TO_EXPRESSION) 
                {
                  /*  List newContainerids = new ArrayList();
                    for(int i=0; i<newContainers.size(); i++) {
                        Container newContainer = (Container)newContainers.elementAt(i);
                        newContainerids.add(new Integer(newContainer.getId()));
                    }
                   * */
                    //change class to allow processing of clones in different vectors into the same expression vector
                    //ThreadedExpressionSummaryTablePopulator p = new ThreadedExpressionSummaryTablePopulator(newContainerids, strategyid);
                   
                    String key = "-1"+ProjectWorkflowProtocolInfo.PWP_SEPARATOR+
                                workflow.getId()+ProjectWorkflowProtocolInfo.PWP_SEPARATOR+
                                "-1"+ProjectWorkflowProtocolInfo.PWP_SEPARATOR +"VECTOR_NAME";
                    String vector_name = ProjectWorkflowProtocolInfo.getInstance().getPWPProperties().get(key);
                    ThreadedExpressionSummaryTablePopulator p = 
                           new ThreadedExpressionSummaryTablePopulator(
                           containerids, seqContainers,  vector_name,
                          StorageForm.GLYCEROL, StorageType.WORKING,  CloneInfo.EXPRESSION_CLONE);

                    new Thread(p).start();
                } 
                else 
                {
                    SummaryTablePopulator populator = new SummaryTablePopulator();
                    ThreadedRearrayedSeqPlatesHandler handler = new ThreadedRearrayedSeqPlatesHandler(seqContainers, researcher.getBarcode(), b, populator, containerids, strategyid, CloneInfo.MASTER_CLONE);
                    new Thread(handler).start();
                }
            }
            
            
             if(Protocol.GENERATE_GLYCEROL_PLATES_FROM_PLATES_WITH_CLONES.equals(protocol.getProcessname()))
             {
                List containerids = new ArrayList();
                List seqContainers = new ArrayList();
                for(int i=0; i<newContainers.size(); i++) 
                {
                    Container newContainer = (Container)newContainers.elementAt(i);
                    
                    if(newContainer.getLabel().substring(1,3).equals("GS")) {
                        containerids.add(new Integer(newContainer.getId()));
                    } else {
                        seqContainers.add(newContainer);
                    }
                }
                
                String isMappingFile = ((GetResearcherBarcodeForm)form).getIsMappingFile();
                boolean isMappingFileRequested = ("Yes".equals(isMappingFile)) ?  true: false;
                
                SummaryTablePopulatorPlateWithClones populator = new SummaryTablePopulatorPlateWithClones();
                populator.setSequencingContainers(seqContainers);
                populator.setResercherBarcode(researcher.getBarcode());
                populator.isMappingFile(isMappingFileRequested);
                populator.setGLysterolContainers(newContainers);
                populator.setStorageType(StorageType.WORKING);
                populator.setStorageForm(StorageForm.GLYCEROL);
                Thread new_thread = new Thread(populator);    
                new_thread.start();
                   
                
            }
            
            if(Protocol.CREATE_TRANSFECTION.equals(protocol.getProcessname())) {
                List fileCol = (ArrayList)request.getSession().getAttribute("PerimeterRearrayInputAction.files");
                List emails = (ArrayList)request.getSession().getAttribute("PerimeterRearrayInputAction.emails");
                Mailer.sendMessages("dongmei_zuo@hms.harvard.edu","dongmei_zuo@hms.harvard.edu", emails, "Cell Culture Perimeter Rearray", "Attached are your rearray file and worklist.", fileCol);
            }
            
            // Remove everything from the session.
            request.getSession().removeAttribute("SelectProtocolAction.queueItems");
            request.getSession().removeAttribute("SelectProtocolAction.protocol");
            request.getSession().removeAttribute("EnterSourcePlateAction.oldContainer");
            request.getSession().removeAttribute("EnterSourcePlateAction.oldContainers");
            request.getSession().removeAttribute("EnterSourcePlateAction.locations");
            request.getSession().removeAttribute("EnterSourcePlateAction.items");
            request.getSession().removeAttribute("EnterSourcePlateAction.sampleLineageSet");
            request.getSession().removeAttribute("EnterSourcePlateAction.subprotocol");
            request.getSession().removeAttribute("EnterSourcePlateAction.sls");
            request.getSession().removeAttribute("EnterSourcePlateAction.sls1");
            request.getSession().removeAttribute("PerimeterRearrayInputAction.files");
            request.getSession().removeAttribute("PerimeterRearrayInputAction.emails");
            
            
            
               // Commit the changes to the database.
            DatabaseTransaction.commit(conn);
         
            return (mapping.findForward("success"));
        } catch (Exception ex) {
            DatabaseTransaction.rollback(conn);
            request.setAttribute(Action.EXCEPTION_KEY, ex);
            return (mapping.findForward("error"));
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
    }
}
