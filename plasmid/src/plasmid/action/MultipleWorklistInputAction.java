/*
 * MultipleWorklistInputAction.java
 *
 * Created on March 27, 2007, 11:56 AM
 */

package plasmid.action;

import java.util.*;
import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import plasmid.form.GenerateMultipleWorklistForm;
import plasmid.database.DatabaseManager.DefTableManager;
import plasmid.Constants;
import plasmid.coreobject.*;
import plasmid.util.StringConvertor;
import plasmid.process.*;
import plasmid.util.Mailer;

/**
 *
 * @author  DZuo
 */
public class MultipleWorklistInputAction extends InternalUserAction{
    
    /** Creates a new instance of WorklistInputAction */
    public MultipleWorklistInputAction() {
    }
    
    public ActionForward internalUserPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActionErrors errors = new ActionErrors();
        
        String processname = ((GenerateMultipleWorklistForm)form).getProcessname();
        String protocol = ((GenerateMultipleWorklistForm)form).getProtocol();
        String srcContainerList = ((GenerateMultipleWorklistForm)form).getSrcContainerList().trim();
        String destContainerListWorking = ((GenerateMultipleWorklistForm)form).getDestContainerListWorking().trim();
        String destContainerListArchive = ((GenerateMultipleWorklistForm)form).getDestContainerListArchive().trim();
        String destContainerListBiobank = ((GenerateMultipleWorklistForm)form).getDestContainerListBiobank().trim();
        int volumnWorking = ((GenerateMultipleWorklistForm)form).getVolumnWorking();
        int volumnArchive = ((GenerateMultipleWorklistForm)form).getVolumnArchive();
        int volumnBiobank = ((GenerateMultipleWorklistForm)form).getVolumnBiobank();
        boolean isBiobank = ((GenerateMultipleWorklistForm)form).getIsBiobank();
        List volums = new ArrayList();
        volums.add(new Integer(volumnWorking));
        volums.add(new Integer(volumnArchive));
        int dVolum = volumnWorking+volumnArchive;
        if(isBiobank) {
            volums.add(new Integer(volumnBiobank));
            dVolum += volumnBiobank;
        }
        String glyceroltype = ((GenerateMultipleWorklistForm)form).getGlyceroltype();
        int volumnGlycerol = ((GenerateMultipleWorklistForm)form).getVolumnGlycerol();
  
        User user = (User)request.getSession().getAttribute(Constants.USER_KEY);
        
        StringConvertor sc = new StringConvertor();
        List srcLabels = sc.convertFromStringToList(srcContainerList, "\n\t");
        List destLabelsWorking = sc.convertFromStringToList(destContainerListWorking, "\n\t");
        List destLabelsArchive = sc.convertFromStringToList(destContainerListArchive, "\n\t");
        List destLabelsBiobank = sc.convertFromStringToList(destContainerListBiobank, "\n\t");
        
        if(srcLabels.size()==0) {
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.general", "Source plate labels are invalid."));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
        if(destLabelsWorking.size()==0) {
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.general", "Destination plate labels (working copy) are invalid."));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
        if(destLabelsArchive.size()==0) {
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.general", "Destination plate labels (archive copy) are invalid."));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
        if(isBiobank && destLabelsBiobank.size()==0) {
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.general", "Destination plate labels (BioBank copy) are invalid."));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
        
        List destLabels = new ArrayList();
        destLabels.add(destLabelsWorking);
        destLabels.add(destLabelsArchive);
        if(isBiobank) {
            destLabels.add(destLabelsBiobank);
        }
        
        ContainerProcessManager manager = new ContainerProcessManager();
        
        try {
            List newSrcLabels = null;
            List mapList = new ArrayList();
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
            
            List destContainers = new ArrayList();
            List newDestLabels = new ArrayList();
            List newDestContainers = new ArrayList();
            for(int i=0; i<destLabels.size(); i++) {
                List labels = (List)destLabels.get(i);
                List l = manager.getContainers(labels, false);
                if(l == null) {
                    errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("error.general", "Cannot get containers from database."));
                    saveErrors(request, errors);
                    return (new ActionForward(mapping.getInput()));
                }
                destContainers.add(l);
                newDestLabels.addAll(labels);
                newDestContainers.addAll(l);
            }
            
            //System.out.println(destLabels);
            //System.out.println("dest containers: "+destContainers);
            List notFoundDest = manager.getNofoundContainers(newDestLabels, newDestContainers);
            if(notFoundDest.size()>0) {
                String s = sc.convertFromListToString(notFoundDest);
                errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.container.notfound", s));
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
            }
            
            List emptySrcContainers = manager.checkEmptyContainers(srcContainers, Container.EMPTY);
            if(emptySrcContainers.size()>0) {
                String s = sc.convertFromListToString(emptySrcContainers);
                errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.container.empty", s));
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
            }
            
            List notEmptyDestContainers = manager.checkEmptyContainers(newDestContainers, Container.FILLED);
            if(notEmptyDestContainers.size()>0) {
                String s = sc.convertFromListToString(notEmptyDestContainers);
                errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.container.notempty", s));
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
            }
            
            List worklist = new ArrayList();
            for(int i=0; i<destContainers.size(); i++) {
                List containers = (ArrayList)destContainers.get(i);
                MappingCalculator calculator = StaticMappingCalculatorFactory.generateMappingCalculator(StaticMappingCalculatorFactory.DIRECT_MAPPING, srcContainers, containers, Sample.getSampleType(processname));
                if(!calculator.isMappingValid()) {
                    errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("error.general", "Container types are not compatable for mapping."));
                    saveErrors(request, errors);
                    return (new ActionForward(mapping.getInput()));
                }
                worklist.add(calculator.calculateMapping());
            }
            
            int worklistid = DefTableManager.getNextid("WORKLISTID");
            if(worklistid < 0) {
                errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.general", "Cannot get next value from sequence WORKLISTID."));
                saveErrors(request, errors);
                return mapping.findForward("error");
            }
            
            //Sftp ftp = SftpHandler.getSftpConnection();
            String fileWorklist = Constants.WORKLIST+"_"+worklistid+".txt";
            String fileWorklistRobot = Constants.WORKLISTROBOT+"_"+worklistid+".gwl";
            String worklistname = Constants.FULLWORKLIST+"_"+worklistid+".txt";
            MultipleWorklistGenerator generator = new MultipleWorklistGenerator(worklist, false);
            generator.setGlycerollabel("Glycerol");
            generator.setGlyceroltype(glyceroltype);
            generator.setGlycerolvolume(volumnGlycerol);
            generator.setVolumes(volums);
            generator.printFullWorklist(Constants.WORKLIST_FILE_PATH+worklistname);
            generator.printWorklist(Constants.USER_WORKLIST_FILE_PATH+fileWorklist);
            generator.printWorklistForRobot(Constants.USER_WORKLIST_FILE_PATH+fileWorklistRobot, -1, -1, true);
            //File localFileWorklist = ftp.download("/tmp/"+fileWorklist, Constants.USER_WORKLIST_FILE_PATH+fileWorklist);
            //File localFileWorklistRobot = ftp.download("/tmp/"+fileWorklistRobot, Constants.USER_WORKLIST_FILE_PATH+fileWorklistRobot);
            //SftpHandler.disconnectSftp(ftp);
            
            List filenames = new ArrayList();
            filenames.add(fileWorklist);
            filenames.add(fileWorklistRobot);
            
            String istube = "N";
            WorklistInfo info = new WorklistInfo(worklistid, worklistname, user.getFirstname()+" "+user.getLastname(), processname,protocol,WorklistInfo.NOTCOMMIT,istube);
            if(!manager.persistWorklistInfo(info)) {
                errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.general", "Cannot insert data into WORKLISTINFO with worklistid: "+worklistid));
                saveErrors(request, errors);
                return mapping.findForward("error");
            }
            
            Collection fileCol = new LinkedList();
            String to = user.getEmail();
            String subject = "Worklist";
            String text = "The attached files are your worklists.";
            fileCol.add(Constants.USER_WORKLIST_FILE_PATH+fileWorklist);
            fileCol.add(Constants.USER_WORKLIST_FILE_PATH+fileWorklistRobot);
            Mailer.sendMessage(to,Constants.EMAIL_FROM,null,subject,text,fileCol);
            
            request.setAttribute("filenames", filenames);
            request.setAttribute("srcLabels", srcLabels);
            request.setAttribute("destLabels", destLabels);
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
