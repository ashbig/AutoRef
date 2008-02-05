/*
 * DownloadClonesAction.java
 *
 * Created on June 23, 2005, 4:10 PM
 */

package plasmid.action;

import java.util.*;
import java.util.Date;
import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
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

import plasmid.coreobject.*;
import plasmid.process.*;
import plasmid.form.DownloadClonesForm;
import plasmid.Constants;
import plasmid.query.coreobject.CloneInfo;
import plasmid.util.SftpHandler;
import com.jscape.inet.sftp.*;

/**
 *
 * @author  DZuo
 */
public class DownloadClonesAction extends UserAction {
    // test for CVS 
    /** Does the real work for the perform method which must be overriden by the
     * Child classes.
     *
     * @param mapping The ActionMapping used to select this instance
     * @param actionForm The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     *
     */
    public ActionForward userPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActionErrors errors = new ActionErrors();
        
        User user = (User)request.getSession().getAttribute(Constants.USER_KEY);
        
        boolean isWorkingStorage = false;
        if(User.INTERNAL.equals(user.getIsinternal())) {
            isWorkingStorage = true;
        }
        
        int orderid = ((DownloadClonesForm)form).getOrderid();
        String type = ((DownloadClonesForm)form).getType();
        String collectionName = ((DownloadClonesForm)form).getCollectionName();
        String button = ((DownloadClonesForm)form).getButton();
        String isBatch = ((DownloadClonesForm)form).getIsBatch();
        
        OrderProcessManager manager = new OrderProcessManager();
        List clones = null;
        if(Constants.ORDER_CLONE.equals(type)) {
            clones = manager.getOrderClones(orderid, user, isWorkingStorage, isBatch);
        }
        if(Constants.ORDER_COLLECTION.equals(type)) {
            clones = manager.getOrderClonesForCollection(collectionName, user, isWorkingStorage);
        }
        
        if(clones == null) {
            if(Constants.DEBUG)
                System.out.println("Cannot get order clones from database.");
            
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.general","Cannot get order clones from database."));
            return (mapping.findForward("error"));
        }
        
        if(Constants.BUTTON_CREATE_BIOBANK_WORKLIST.equals(button) && User.INTERNAL.equals(user.getIsinternal())) {
            List groups = null;
            if("Y".equals(isBatch)) 
                groups = manager.groupClonesByTargetPlate(clones);
            else
                groups = manager.groupClonesByGrowth(clones);
            
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date today = new Date();
            String filename = "order"+orderid+"_"+formatter.format(today);
            List files = new ArrayList();
            try {
                Sftp ftp = SftpHandler.getSftpConnection();
                for(int i=0; i<groups.size(); i++) {
                    List group = (List)groups.get(i);
                    String worklistfilename = filename+"_"+(i+1)+".txt";
                    manager.printBioTracyWorklist(group, Constants.BIOTRACY_WORKLIST_PATH, worklistfilename, isBatch, ftp);
                    files.add(worklistfilename);
                }
                String summaryfilename = filename+"_summary.xls";
                manager.printBioTracySummary(groups, Constants.BIOTRACY_WORKLIST_PATH+summaryfilename, filename, ftp);
                SftpHandler.disconnectSftp(ftp);
                files.add(summaryfilename);
            } catch (Exception ex) {
                if(Constants.DEBUG) {
                    System.out.println("Cannot print BioTracy worklist files.");
                    System.out.println(ex);
                }
                
                errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.general","Cannot print BioTracy worklist files."));
                return (mapping.findForward("error"));
            }
            
            request.setAttribute("files", files);
            return (mapping.findForward("success"));
        }
        
        response.setContentType("application/x-msexcel");
        response.setHeader("Content-Disposition", "attachment;filename=Clones_"+orderid+".xls");
        PrintWriter out = response.getWriter();
        manager.writeCloneList(clones, out, isWorkingStorage, false);
        out.close();
        return null;
    }
}

