/*
 * EnterOligoPlateLocation.java
 *
 * Created on July 9, 2001, 5:38 PM
 */

package edu.harvard.med.hip.flex.action;

/**
 *
 * @author  Wendy
 * @version
 */

import java.util.*;
import java.sql.*;
import java.io.IOException;
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
import edu.harvard.med.hip.flex.form.EnterOligoPlateLocationForm;
import edu.harvard.med.hip.flex.process.Process;
import edu.harvard.med.hip.flex.workflow.*;


public class EnterOligoPlateLocationAction extends ResearcherAction {
    
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
        Connection conn = null;
        LinkedList containerList = (LinkedList)request.getSession().getAttribute("containerList");
        
        int current_platesetid = -1;
        int old_platesetid = -1;
        LinkedList platesetList = new LinkedList();
        
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            
            ListIterator iter = containerList.listIterator();
            Container container = (Container)iter.next();
            current_platesetid = container.getPlatesetid();
            Plateset plateset = new Plateset(current_platesetid);
            platesetList.add(plateset);
            System.out.println("platesetid is: "+ plateset.getId());
            
            //oligo plates received may belong to more than one plateset
            while (iter.hasNext()){
                // Set the location for the containers.
                Location oligoPlateLocation = new Location("FREEZER"); //to be modified
                container.setLocation(oligoPlateLocation);
                int locationid = oligoPlateLocation.getId();
                container.updateLocation(locationid, conn);
                System.out.println("update the location for container: "+container.getLabel());
                DatabaseTransaction.commit(conn);
                
                old_platesetid = current_platesetid;
                container = (Container)iter.next();
                current_platesetid = container.getPlatesetid();
                
                if (old_platesetid != current_platesetid){
                    plateset = new Plateset(current_platesetid);
                    platesetList.add(plateset);
                    System.out.println("next platesetid is: "+ plateset.getId());
                } //if
                
            } //while
            
        } catch (Exception ex) {
            DatabaseTransaction.rollback(conn);
            request.setAttribute(Action.EXCEPTION_KEY, ex);
            return (mapping.findForward("error"));
        }
        
        try{
            //check plateset to see whether all of the oligo plates
            //belong to the same plateset have been received
            ListIterator iter = platesetList.listIterator();
            System.out.println("Total plateset received: "+ platesetList.size());
            while (iter.hasNext()){
                Plateset plateset = (Plateset)iter.next();
                boolean complete = checkPlateset(plateset.getId());
                
                if (complete) {
                    System.out.println("inserting generate PCR plate queue...");
                    insertPCRQueue(plateset, conn);
                    removeReceiveOligoQueue(containerList, conn);
                    DatabaseTransaction.commit(conn);
                }
            } //while
        } catch(Exception ex){
            DatabaseTransaction.rollback(conn);
            request.setAttribute(Action.EXCEPTION_KEY, ex);
            return (mapping.findForward("error"));
        }
        
        
        return (mapping.findForward("success"));
    }
    
    private boolean checkPlateset(int platesetid) throws FlexDatabaseException {
        
        String sql = "SELECT containerid, locationid FROM containerheader\n" +
        "WHERE platesetid ="+ platesetid;
        ResultSet rs = null;
        int count = 0;
        boolean complete = true;
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            //check the location. Unavaiable means oligoplate not received yet
            while(rs.next()) {
                ++count;
                int locationid = rs.getInt("LOCATIONID");
                //to be modified. locationid for unavailable is 1
                if (locationid == 1){
                    complete = false;
                }  //if
            }//while
            if (count < 3) {
                complete = false;
            }
            //System.out.println("total places received: "+ count);
        } catch (SQLException sqlE) {
            throw new FlexDatabaseException("Error occured while check plateset: "+platesetid+"\n"+"\nSQL: "+sqlE);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        
        return complete;
    } //checkPlateset
    
    /**
     * remove receive oligo plates queue record
     */
    protected void removeReceiveOligoQueue(LinkedList items, Connection conn) throws FlexDatabaseException {
        if (items == null)
            return;
        
        Protocol protocol = new Protocol("receive oligo plates");
        
        String sql = "DELETE FROM queue\n" +
        "WHERE protocolid = "+ protocol.getId()  + "\n" +
        "AND containerid = ?";
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            Vector v = new Vector();
            ListIterator iter = items.listIterator();
            
            while (iter.hasNext()) {
                Container container = (Container) iter.next();
                int containerid = container.getId();
                stmt.setInt(1, containerid);
                DatabaseTransaction.executeUpdate(stmt);
            }
            
        } catch(SQLException sqlE) {
            throw new FlexDatabaseException("Error occured while deleting oligo plates from queue\n"+sqlE+"\nSQL: "+sql);
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }
    }
    
    /**
     * insert "generate PCR plates" queue record for each plate created
     */
    protected void insertPCRQueue(Plateset plateset, Connection conn) throws FlexDatabaseException {
        Protocol protocol = new Protocol("generate PCR plates");
        
        try{
            PlatesetProcessQueue platesetQueue = new PlatesetProcessQueue();
            LinkedList platesetQueueItemList = new LinkedList();
            QueueItem queueItem = new QueueItem(plateset, protocol);
            platesetQueueItemList.add(queueItem);
            
            platesetQueue.addQueueItems(platesetQueueItemList, conn);
        } catch(FlexDatabaseException sqlE) {
            throw new FlexDatabaseException("Error occured while inserting plateset into queue for PCR\n");
        }
    } //insertPCRQueue
    
}

