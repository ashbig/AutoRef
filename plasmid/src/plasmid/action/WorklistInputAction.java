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
import plasmid.database.DatabaseManager.DefTableManager;
import plasmid.Constants;
import plasmid.coreobject.Process;
import plasmid.coreobject.*;
import plasmid.util.StringConvertor;
import plasmid.process.*;
import plasmid.util.Mailer;
import plasmid.util.SftpHandler;
import com.jscape.inet.sftp.*;

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
        boolean tube = ((GenerateWorklistForm)form).getTube();
        
        User user = (User)request.getSession().getAttribute(Constants.USER_KEY);
        
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
        boolean b = false;
        if(Process.CULTURE.equals(processname) && tube)
            b = true;
        
        try {
            List newSrcLabels = null;
            List mapList = new ArrayList();
            if(b) {
                newSrcLabels = srcLabels;
                srcLabels = new ArrayList();
                for(int i=0; i<newSrcLabels.size(); i++) {
                    String label = (String)newSrcLabels.get(i);
                    Map m = manager.readTubeMappingFile(ContainerProcessManager.TUBEMAPFILEPATH+label+".trx");
                    
                    if(m == null) {
                        throw new Exception("Cannot read tube mapping file for container: "+label);
                    }
                    mapList.add(m);
                    srcLabels.addAll(m.values());
                }
            }
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
            
            List emptySrcContainers = manager.checkEmptyContainers(srcContainers, Container.EMPTY);
            if(emptySrcContainers.size()>0) {
                String s = sc.convertFromListToString(emptySrcContainers);
                errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.container.empty", s));
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
            }
            
            List notEmptyDestContainers = manager.checkEmptyContainers(destContainers, Container.FILLED);
            if(notEmptyDestContainers.size()>0) {
                String s = sc.convertFromListToString(notEmptyDestContainers);
                errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.container.notempty", s));
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
            }
            
            if(b) {
                List newSrcContainers = new ArrayList();
                ContainerMapper mapper = new ContainerMapper();
                for(int i=0; i<newSrcLabels.size(); i++) {
                    String label = (String)newSrcLabels.get(i);
                    Map m = (Map)mapList.get(i);
                    Container c = mapper.convertToPlates(srcContainers, m);
                    c.setLabel(label);
                    newSrcContainers.add(c);
                }
                srcContainers = newSrcContainers;
            }
            
            MappingCalculator calculator = StaticMappingCalculatorFactory.generateMappingCalculator(StaticMappingCalculatorFactory.DIRECT_MAPPING, srcContainers, destContainers, Sample.getSampleType(processname));
            if(!calculator.isMappingValid()) {
                errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.general", "Container types are not compatable for mapping."));
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
            }
            
            int worklistid = DefTableManager.getNextid("WORKLISTID");
            if(worklistid < 0) {
                errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.general", "Cannot get next value from sequence WORKLISTID."));
                saveErrors(request, errors);
                return mapping.findForward("error");
            }
            
            boolean isPrintEmpty = true;
            if(Process.GENERATE_GLYCEROL.equals(processname) && tube)
                isPrintEmpty = false;
            
            String fileWorklist = Constants.WORKLIST+"_"+worklistid+".txt";
            String fileWorklistRobot = Constants.WORKLISTROBOT+"_"+worklistid+".gwl";
            String worklistname = Constants.FULLWORKLIST+"_"+worklistid+".txt";
            List worklist = calculator.calculateMapping();
            WorklistGenerator generator = new WorklistGenerator(worklist, isPrintEmpty);
            Sftp ftp = SftpHandler.getSftpConnection();
            generator.printFullWorklist(Constants.WORKLIST_FILE_PATH+worklistname, ftp);
            generator.printWorklist(Constants.USER_WORKLIST_FILE_PATH+fileWorklist, ftp);
            generator.printWorklistForRobot(Constants.USER_WORKLIST_FILE_PATH+fileWorklistRobot, volumn, volumn, true, ftp);
            
            File localFileWorklist = ftp.download(Constants.USER_WORKLIST_FILE_PATH+fileWorklist);
            File localFileWorklistRobot = ftp.download(Constants.USER_WORKLIST_FILE_PATH+fileWorklistRobot);
            SftpHandler.disconnectSftp(ftp);
            
            List filenames = new ArrayList();
            filenames.add(fileWorklist);
            filenames.add(fileWorklistRobot);
            
            String istube = "N";
            if(tube)
                istube = "Y";
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
            //fileCol.add(new File(Constants.USER_WORKLIST_FILE_PATH+fileWorklist));
            //fileCol.add(new File(Constants.USER_WORKLIST_FILE_PATH+fileWorklistRobot));
            fileCol.add(localFileWorklist);
            fileCol.add(localFileWorklistRobot);
            Mailer.sendMessage(to,Constants.EMAIL_FROM,null,subject,text,fileCol);
            
            if(b) {
                srcLabels = newSrcLabels;
            }
            
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
