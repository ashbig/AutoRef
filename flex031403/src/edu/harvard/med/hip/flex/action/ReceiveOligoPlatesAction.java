/*
 * ReceiveOligoPlatesAction.java
 *
 * Created on June 18, 2001, 5:58 PM
 */

package edu.harvard.med.hip.flex.action;

import java.util.*;
import java.sql.*;
import java.io.*;
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
import edu.harvard.med.hip.flex.form.ReceiveOligoOrdersForm;
/**
 *
 * @author  Wendy
 * @version
 */
public class ReceiveOligoPlatesAction extends ResearcherAction {
    
    /** Creates new ReceiveOligoPlatesAction */
    public ReceiveOligoPlatesAction() {
    }
    
    /**
     * Does the real work for the perform method which must be overriden by the
     * Child classes.
     *
     * @param mapping The ActionMapping used to select this instance
     * @param actionForm The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */
    public ActionForward flexPerform(ActionMapping mapping, ActionForm form,
    HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
        ActionErrors errors = new ActionErrors();
        String barcode = ((ReceiveOligoOrdersForm)form).getResearcherBarcode();
        String oligoPlateIds = ((ReceiveOligoOrdersForm)form).getOligoPlateIds();
        String receiveDate = ((ReceiveOligoOrdersForm)form).getReceiveDate();
        Researcher researcher = null;
        
        // Validate the researcher barcode.
        try {
            researcher = new Researcher(barcode);
        } catch (FlexProcessException ex) {
            System.out.println(ex);
            errors.add("researcherBarcode", new ActionError("error.researcher.invalid.barcode", barcode));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        } catch (FlexDatabaseException ex) {
            System.out.println(ex);
            request.setAttribute(Action.EXCEPTION_KEY, ex);
            return (mapping.findForward("error"));
        }
        
        
        ReceiveOligoOrdersForm formProper = (ReceiveOligoOrdersForm) form;
        List ids = formProper.getOligoPlateList();
        request.getSession().setAttribute("plateList",ids);
  
        Connection conn = null;
        ListIterator iter = ids.listIterator();
        List containerList = null;
        int containerId = -1;
        System.out.println("Total labels entered: "+ ids.size());
        
        //insert receive plates process execution record
        //for each plate received.
        while (iter.hasNext()) {
            String label = (String)iter.next();
            System.out.println("The oligo plate label is: "+label);
            
            try{
                containerId = findContainerId(label);
                System.out.println("container ID is: " + containerId);
            } catch(FlexCoreException coreEx){
                System.out.println(coreEx);
                return (new ActionForward(mapping.getInput()));
            } catch(FlexDatabaseException dbEx){
                System.out.println(dbEx);
                //request.setAttribute(Action.EXCEPTION_KEY, ex);
                return (mapping.findForward("error"));
            }
    
            
            try {
                DatabaseTransaction t = DatabaseTransaction.getInstance();
                conn = t.requestConnection();
                Protocol protocol = new Protocol("receive oligo plates");
                // Create a process and process object.
                
                edu.harvard.med.hip.flex.process.Process process =
                new edu.harvard.med.hip.flex.process.Process(protocol,
                edu.harvard.med.hip.flex.process.Process.SUCCESS, researcher);
                System.out.println("process created");
                System.out.println("containerid is: "+ containerId);
                
                ContainerProcessObject ioContainer =
                new ContainerProcessObject(containerId,
                process.getExecutionid(),
                edu.harvard.med.hip.flex.process.ProcessObject.IO);
                System.out.println("process object created");
                
                // Insert the process and process objects into database.
                process.addProcessObject(ioContainer);
                process.insert(conn);
                
                // Remove the container from the queue.
                 LinkedList newItems = new LinkedList();
                 //   newItems.addLast(item);
                 //   ContainerProcessQueue queue = new ContainerProcessQueue();
                 //   queue.removeQueueItems(newItems, conn);
                
                 // Add the new container to the queue.
                 //   newItems.clear();
                 //   newItems.addLast(new QueueItem(newContainer, protocol));
                 //   queue.addQueueItems(newItems, conn);
                
                 // Commit the changes to the database.
                // DatabaseTransaction.commit(conn);
                
                 // Store the data in the session.
                 //    Location sLocation = new Location(sourceLocation);
                 //    request.getSession().setAttribute("dLocation", dLocation);
                
                 //    return (mapping.findForward("success"));
            } catch (Exception ex) {
                DatabaseTransaction.rollback(conn);
                request.setAttribute(Action.EXCEPTION_KEY, ex);
                return (mapping.findForward("error"));
            } finally {
                DatabaseTransaction.closeConnection(conn);
            }
            
        } //while
        return (mapping.findForward("success"));
        
        // return (mapping.findForward("error"));
    } //flexPerform
    
    
    // Validate the plate barcode with containerids in the queue.
    private boolean plateExist(int id) throws FlexDatabaseException {
        boolean isExist = false;
        ResultSet rs = null;
        
        String sql = "SELECT containerid FROM queue\n"
        + "WHERE containerid = '" + id + "'";
        
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            if (rs.next()) {
                isExist = true;
            }
        }catch (SQLException sqlex) {
            sqlex.printStackTrace();
            throw new FlexDatabaseException(sqlex+"\nSQL: "+sql);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        
        return isExist;
    } //PlateExist
    
    private int findContainerId(String label) throws FlexCoreException,
    FlexDatabaseException {
         int id = -1;
        String sql = "select c.containerid as containerid, "+
        "c.containertype as containertype, "+
        "c.label as label, "+
        "c.locationid as locationid, "+
        "c.platesetid as platesetid, "+
        "l.locationtype as locationtype, "+
        "l.locationdescription as description\n"+
        "from containerheader c, containerlocation l\n"+
        "where c.locationid = l.locationid\n"+
        "and c.label = '"+ label+"'";
        ResultSet rs = null;
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next()) {
                
                 id = rs.getInt("CONTAINERID");
                 System.out.println("container ID is: "+ id);
                //Container curContainer = new Container(id);
            }
        } catch (NullPointerException ex) {
            throw new FlexCoreException("Error occured while initializing container with label: "+label+"\n"+ex.getMessage());
        } catch (SQLException sqlE) {
            throw new FlexDatabaseException("Error occured while initializing container from labe: "+label+"\n"+"\nSQL: "+sqlE);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        return id;
    }
    
}
