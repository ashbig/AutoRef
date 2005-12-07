/*
 * GenerateContainersAction.java
 *
 * Created on November 2, 2005, 1:54 PM
 */

package plasmid.action;

import java.io.*;
import java.util.*;
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

import plasmid.form.GenerateWorklistForm;
import plasmid.Constants;
import plasmid.process.*;
import plasmid.coreobject.*;
import plasmid.util.Mailer;
import plasmid.database.DatabaseManager.ProcessManager;

/**
 *
 * @author  DZuo
 */
public class GenerateContainersAction extends Action {
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
    public ActionForward perform(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException {
        ActionErrors errors = new ActionErrors();
        String worklistname = ((GenerateWorklistForm)form).getWorklistname();
        ContainerProcessManager manager = new ContainerProcessManager();
        try {
            int begin = worklistname.indexOf("_");
            int end = worklistname.indexOf(".");
            int worklistid = Integer.parseInt(worklistname.substring(begin+1, end));
            WorklistInfo info = ProcessManager.getWorklistInfo(worklistid);
            if(info == null) {
                throw new Exception("Cannot get data from WORKLISTINFO with worklistid: "+worklistid);
            }
            String filename = info.getWorklistname();
            WorklistGenerator generator = new WorklistGenerator();
            generator.readWorklist(Constants.WORKLIST_FILE_PATH+filename);
            
            Set destContainerLabels = generator.getDestContainerLabels();
            List dContainers = manager.getContainers(new ArrayList(destContainerLabels), false);
            if(dContainers == null) {
                throw new Exception("Cannot get destination containers from database.");
            }
            List nonempty = manager.checkEmptyContainers(dContainers);
            if(nonempty.size()>0) {
                throw new Exception("Some destination containers have been filled.");
            }
            
            ContainerMapper mapper = new ContainerMapper(generator.getWorklist());
            List srcContainers = new ArrayList(generator.getSrcContainers());
            List destContainers = mapper.mapContainer();
            List lineages = mapper.getWorklist();
            List samples = new ArrayList();
            List tubes = new ArrayList();
            if(WorklistInfo.YES.equals(info.getTube())) {
                for(int i=0; i<destContainers.size(); i++) {
                    Container c = (Container)destContainers.get(i);
                    String label = c.getLabel();
                    Map m = manager.readTubeMappingFile(ContainerProcessManager.TUBEMAPFILEPATH+label+".trx");
                    
                    if(m == null) {
                        throw new Exception("Cannot read tube mapping file for container: "+label);
                    }
                    
                    List l = mapper.convertToTubes(c, m, true);
                    for(int n=0; n<l.size(); n++) {
                        Container c1 = (Container)l.get(n);
                        tubes.add(c1);
                        samples.addAll(c1.getSamples());
                    }
                }
                destContainers = tubes;
                manager.setContainerids(destContainers);
            } else {
                for(int i=0; i<destContainers.size(); i++) {
                    Container c = (Container)destContainers.get(i);
                    samples.addAll(c.getSamples());
                }
            }
            manager.setSampleids(samples);
            List newSampleLineages = manager.setSampleToidsForLineages(samples, lineages);
            if(newSampleLineages == null) {
                throw new Exception("Error occured while setting sampleids for sample lineages.");
            }
            ProcessExecution execution = new ProcessExecution(0, ProcessExecution.COMPLETE, null, info.getProcessname(), info.getResearchername(), info.getProtocolname());
            execution.setLineages(newSampleLineages);
            execution.setInputObjects(srcContainers);
            execution.setOutputObjects(destContainers);
            
            boolean b = false;            
            if(WorklistInfo.YES.equals(info.getTube())) {
                b = true;
            }
            if(!manager.persistData(destContainers,execution,info,b)) {
                throw new Exception("Error occured while inserting into database.");
            }
        } catch (Exception ex) {
            if(Constants.DEBUG) {
                System.out.println("Creating database records from worklist failed for worklist: "+worklistname);
                System.out.println(ex);
            }
            
            String to = Constants.EMAIL_FROM;
            String subject = "Creating database records from worklist failed.";
            String text = "Worklist id: "+worklistname+"\n\n";
            text += "Error message: "+ex;
            try {
                Mailer.sendMessage(to,Constants.EMAIL_FROM,subject,text);
            } catch (Exception ex1) {
                System.out.println(ex1);
            }
            return mapping.findForward("fail");
        }
        return mapping.findForward("success");
    }
}