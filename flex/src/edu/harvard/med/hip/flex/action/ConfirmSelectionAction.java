/*
 * ConfirmSelectionAction.java
 *
 * Created on June 6, 2001, 5:47 PM
 *
 * This class handles the sequences submitted by user and insert into database.
 */

package edu.harvard.med.hip.flex.action;

import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import java.util.LinkedList;
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
import edu.harvard.med.hip.flex.util.*;
import edu.harvard.med.hip.flex.form.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.user.*;
import edu.harvard.med.hip.flex.Constants;

/**
 *
 * @author  Dongmei Zuo
 * @version 
 */
public class ConfirmSelectionAction extends FlexAction {

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
        String [] selection = ((CustomerRequestForm)form).getSelection();
        if(selection == null) {         
            return (mapping.findForward("success"));
        }
        
        User user = (User)request.getSession().getAttribute(Constants.USER_KEY);
        String username = user.getUsername();
        Hashtable sequences = (Hashtable)request.getSession().getAttribute("sequences");
        Request r = new Request(username);
        LinkedList l = new LinkedList();        
        Connection conn = null;
        
        try {        
            Protocol p = new Protocol("approve sequences");

            for(int i=0; i<selection.length; i++) {
                String gi = selection[i];
                FlexSequence sequence = (FlexSequence)sequences.get(gi);
                r.addSequence(sequence);
                
                if("NEW".equals(sequence.getFlexstatus())) {
                    QueueItem item = new QueueItem(sequence, p);
                    l.addLast(item);
                }
            }

            DatabaseTransaction t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            r.insert(conn);
        
            SequenceProcessQueue queue = new SequenceProcessQueue();
            queue.addQueueItems(l, conn);

            conn.commit();
            return (mapping.findForward("success"));
        } catch (Exception sqlE) {
            try {
                conn.rollback();
                return (mapping.findForward("fail"));
            } catch (Exception e) {           
                return (mapping.findForward("fail"));
            } 
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
    }
    

}
