/*
 * File : PendingRequestsForm.java
 * Classes : PendingRequestsForm
 *
 * Description :
 *
 * Form to accept or reject sequences in the queue.
 *
 *
 * Author : Juan Munoz (jmunoz@3rdmill.com)
 * 
 * See COPYRIGHT file for copyright information
 *
 *
 * The following information is used by CVS
 * $Revision: 1.1 $
 * $Date: 2001-07-20 14:23:12 $
 * $Author: jmunoz $
 *
 ******************************************************************************
 *
 * Revision history (Started on July 18, 2001) :
 *
 *    Add entries here when updating the code. Remember to date and insert
 *    your 3 letters initials.
 *
 *    Jul-18-2001 : JMM - Class created. 
 *
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
*/


package edu.harvard.med.hip.flex.form;

import org.apache.struts.action.*;

import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.process.*;


/**
 * Form for accepting rejecting sequences in the queue.
 *
 * @author     $Author: jmunoz $
 * @version    $Revision: 1.1 $ $Date: 2001-07-20 14:23:12 $
 */

public class PendingRequestsForm extends ActionForm  {
    // list of statuses to track.
    public List statusList= null;
    
    /**
     * makes the form represent this queue
     * 
     * @param queueItems The list of queue items this form will be 
     *  responsible for.
     */
    public void setQueueItems(List queueItems) {
        // create a new list to hold the statuses
        statusList = new LinkedList();
        
        /** go through each item in the queue and put its current status in the
         * form
         */
        Iterator queueIter = queueItems.iterator();
        while(queueIter.hasNext()) {
            QueueItem curItem = (QueueItem)queueIter.next();
            FlexSequence curSequence = (FlexSequence)curItem.getItem();
            statusList.add(curSequence.getFlexstatus());
        }
    }
    
    
    /**
     * Gets the status of the flex sequence
     *
     * @param i The index in the queue to get.
     *
     * @return The status of the sequence
     */
    public String getStatus(int i) {
        return (String)statusList.get(i);
    }
    
    /**
     * Sets the status of the flex sequence.
     *
     * @param i The index of the status to set.
     * @param status The status to set to.
     */
    public void setStatus (int i, String status) {
        statusList.set(i,status);
    }
    
    
    /**
     * Gets the number of items this form represents.
     *
     * @return size of the number of sequence in the UI.
     */
    public int getQueueSize() {
        return this.statusList.size();
    }
    
    public void reset(ActionMapping mapping, HttpServletRequest request) {
    }
    
} // End class PendingRequestsForm


/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
*/
