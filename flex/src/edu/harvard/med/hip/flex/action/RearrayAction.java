/*
 * RearrayAction.java
 *
 * Created on April 16, 2003, 2:52 PM
 */

package edu.harvard.med.hip.flex.action;

import java.util.LinkedList;
import java.util.MissingResourceException;
import java.util.Vector;
import java.util.Enumeration;
import java.sql.*;
import java.io.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.*;
import org.apache.struts.upload.*;
import org.apache.struts.util.*;

import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.form.*;
import edu.harvard.med.hip.flex.process.Process;
import edu.harvard.med.hip.flex.workflow.*;
import edu.harvard.med.hip.flex.user.*;
import edu.harvard.med.hip.flex.Constants;

/**
 *
 * @author  dzuo
 */
public class RearrayAction extends AdminAction {
    public ActionForward flexPerform(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException {
        ActionErrors errors = new ActionErrors();
        
        // Get the workflow and project from the form and store in request.
        
        int workflowid = ((RearrayPlateForm)form).getWorkflowid();
        int projectid = ((RearrayPlateForm)form).getProjectid();
        FormFile rearrayFile = ((RearrayPlateForm)form).getRequestFile();
        boolean isSort = ((RearrayPlateForm)form).getIsSortBySawToothpatern();
        boolean isArrangeBySize = ((RearrayPlateForm)form).getIsArrangeBySize();
        boolean isControls = ((RearrayPlateForm)form).getIsControls() ;
        boolean isPutOnQueue = ((RearrayPlateForm)form).getIsPutOnQueue() ;
        boolean isFullPlate = ((RearrayPlateForm)form).getIsFullPlate();
        boolean isSmall = ((RearrayPlateForm)form).getSmall();
        boolean isMeddium = ((RearrayPlateForm)form).getMedium() ;
        boolean isLarge = ((RearrayPlateForm)form).getLarge();
        String location = ((RearrayPlateForm)form).getLocation();
        
        int  wells_on_plate = ((RearrayPlateForm)form).getWellsOnPlate();
        
        request.setAttribute("workflowid", new Integer(workflowid));
        request.setAttribute("projectid", new Integer(projectid));
        
        InputStream input = null;
        try {
            input = rearrayFile.getInputStream();
        }
        catch (FileNotFoundException ex) {
            errors.add("rearrayFile", new ActionError("flex.infoimport.file", ex.getMessage()));
            saveErrors(request,errors);
            return new ActionForward(mapping.getInput());
        }
        catch (IOException ex) {
            errors.add("rearrayFile", new ActionError("flex.infoimport.file", ex.getMessage()));
            saveErrors(request,errors);
            return new ActionForward(mapping.getInput());
        }
        
        Connection conn = null;
        try {
            String username = ((User)request.getSession().getAttribute(Constants.USER_KEY)).getUsername();
            AccessManager am = AccessManager.getInstance();
            String user_email = am.getEmail( username );
            
            Protocol pr = new Protocol(Protocol.REARRAY_TO_DNA_TEMPLATE);
            int protocolid = pr.getId();
            conn = DatabaseTransaction.getInstance().requestConnection();
            
            RearrayerForPlates rearrayer = new RearrayerForPlates(conn,projectid, workflowid,protocolid,input,user_email);
            
            rearrayer.setWellsNumbers(wells_on_plate);
            rearrayer.isArrangeBySize(isArrangeBySize);
            rearrayer.isControls(isControls);
            // rearrayer.setSampleType(sampletype);
            rearrayer.setSort(isSort);
            rearrayer.isSmall(isSmall);
            rearrayer.isMeddium(isMeddium);
            rearrayer.isLarge(isLarge);
            rearrayer.isFullPlates(isFullPlate);
            rearrayer.isPutOnQueue(isPutOnQueue);
            rearrayer.setLocation(location);
            
            if(workflowid == Workflow.CONVERT_FUSION_TO_CLOSE) {
                rearrayer.isConvert(true);
                rearrayer.isClose(true);
                rearrayer.isOligo(true);
            }
            
            if(workflowid == Workflow.CONVERT_CLOSE_TO_FUSION) {
                rearrayer.isConvert(true);
                rearrayer.isFusion(true);
                rearrayer.isOligo(true);
            }
            
            rearrayer.createNewPlates(0);
            
            //delete item from queue for current protocol;
            if (isPutOnQueue) {
                conn.commit();
                request.setAttribute("EnterSourcePlateAction.newContainers", rearrayer.getRearrayContainers());
                
                return mapping.findForward("success");
            }
            else {
                conn.rollback();
                return new ActionForward(mapping.getInput());
            }
        } catch (FlexDatabaseException fde) {
            System.out.println("FDE " + fde.getMessage());
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.database.error",fde));
            saveErrors(request,errors);
            DatabaseTransaction.rollback(conn);
            return mapping.findForward("error");
        }
        catch(Exception ex) {
            System.out.println("E " + ex.getMessage());
            errors.add(Action.EXCEPTION_KEY,
            new ActionError(Action.EXCEPTION_KEY,ex));
            saveErrors(request,errors);
            DatabaseTransaction.rollback(conn);
            return mapping.findForward("error");
        }
        finally {
            if (conn != null)
                DatabaseTransaction.closeConnection(conn);
        }
    }
}


