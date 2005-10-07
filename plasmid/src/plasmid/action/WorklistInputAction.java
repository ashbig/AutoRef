/*
 * WorklistInputAction.java
 *
 * Created on October 5, 2005, 10:26 AM
 */

package plasmid.action;

import java.util.*;
import java.io.*;
import java.sql.*;
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
import plasmid.database.DatabaseManager.ProcessManager;
import plasmid.Constants;
import plasmid.coreobject.Process;
import plasmid.coreobject.*;
import plasmid.util.StringConvertor;
import plasmid.process.*;

/**
 *
 * @author  DZuo
 */
public class WorklistInputAction extends InternalUserAction{
    
    /** Creates a new instance of WorklistInputAction */
    public WorklistInputAction() {
    }
    
    public ActionForward internalUserPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActionErrors errors = new ActionErrors();
        
        String processname = ((GenerateWorklistForm)form).getProcessname();
        String protocol = ((GenerateWorklistForm)form).getProtocol();
        String srcContainerList = ((GenerateWorklistForm)form).getSrcContainerList().trim();
        String destContainerList = ((GenerateWorklistForm)form).getDestContainerList().trim();
        int volumn = ((GenerateWorklistForm)form).getVolumn();
        
        StringConvertor sc = new StringConvertor();
        List srcLabels = sc.convertFromStringToList(srcContainerList, "\n\t ");
        List destLabels = sc.convertFromStringToList(destContainerList, "\n\t ");
        
        if(srcLabels.size()==0) {
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.general", "Source plate labels are invalid."));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
        if(destLabels.size()==0) {
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.general", "Destination plate labels are invalid."));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
        
        ContainerProcessManager manager = new ContainerProcessManager();
        try {
            List srcContainers = manager.getContainers(srcLabels, true);
            if(srcContainers == null) {
                errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.general", "Cannot get containers from database."));
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
            }
            
            //System.out.println(srcLabels);
            //System.out.println("src containers: "+srcContainers);
            List notFound = manager.getNofoundContainers(srcLabels, srcContainers);
            if(notFound.size()>0) {
                String s = sc.convertFromListToString(notFound);
                errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.container.notfound", s));
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
            }
            
            List destContainers = manager.getContainers(destLabels, false);
            if(destContainers == null) {
                errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.general", "Cannot get containers from database."));
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
            }
            
            //System.out.println(destLabels);
            //System.out.println("dest containers: "+destContainers);
            List notFoundDest = manager.getNofoundContainers(destLabels, destContainers);
            if(notFoundDest.size()>0) {
                String s = sc.convertFromListToString(notFoundDest);
                errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.container.notfound", s));
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
            }
                
            List notEmptyDestContainers = manager.checkEmptyContainers(destContainers);
            if(notEmptyDestContainers.size()>0) {
                String s = sc.convertFromListToString(notEmptyDestContainers);
                errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.container.notempty", s));
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
            }
            
            MappingCalculator calculator = StaticMappingCalculatorFactory.generateMappingCalculator(StaticMappingCalculatorFactory.DIRECT_MAPPING, srcContainers, destContainers, Sample.getSampleType(processname));
            if(!calculator.isMappingValid()) {
                errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.general", "Container types are not compatable for mapping."));
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
            }
         
            String fileWorklist = "worklist.txt";
            String fileWorklistRobot = "worklist_robot.txt";
            List worklist = calculator.calculateMapping();
            WorklistGenerator generator = new WorklistGenerator(worklist);
            generator.printFullWorklist(Constants.WORKLIST_FILE_PATH+"full_worklist.txt");
            generator.printWorklist(Constants.WORKLIST_FILE_PATH+fileWorklist);
            generator.printWorklistForRobot(Constants.WORKLIST_FILE_PATH+fileWorklistRobot, volumn, volumn, true);
            
            List filenames = new ArrayList();
            filenames.add(fileWorklist);
            filenames.add(fileWorklistRobot);
            request.setAttribute("filenames", filenames);
        } catch (Exception ex) {
            if(Constants.DEBUG) {
                System.out.println(ex);
            }
            
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.database"));
            saveErrors(request, errors);
            return mapping.findForward("error");
        }
        
        return mapping.findForward("success");
    }
}
