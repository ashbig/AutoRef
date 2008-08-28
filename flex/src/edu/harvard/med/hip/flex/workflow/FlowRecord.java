/*
 * FlowRecord.java
 *
 * The structure to hold the current protocol name and
 * the next protocol name(s).
 *
 * Created on June 25, 2001, 4:08 PM
 */

package edu.harvard.med.hip.flex.workflow;

import java.util.*;
import edu.harvard.med.hip.flex.process.*;

/**
 *
 * @author  dzuo
 * @version 
 */
public class FlowRecord {
    private Protocol current;
    private Vector next;

    /** Creates new FlowRecord 
     * 
     * @param current The current protocol object.
     * @param next A collection of next protocol objects.
     *
     * @return The FlowRecord object.
     */
    public FlowRecord(Protocol current, Vector next) {
        this.current = current;
        this.next = next;
    }
    
    /**
     * Return true if the given protocol is equal to the
     * current protocol; false otherwise.
     *
     * @param protocol The protocol to be compared to.
     * @return True if the given protocol is equal to the current
     *  protocol; false otherwise.
     */
    public boolean isEqual(Protocol protocol) {
        return (current.getId() == (protocol.getId()));
    }
    
    /**
     * Return true if the given protocol is equal to the
     * current protocol; false otherwise.
     *
     * @param protocol The protocol to be compared to.
     * @return True if the given protocol is equal to the current
     *  protocol; false otherwise.
     */
    public boolean equals(Protocol protocol) {
        return (current.getId() == (protocol.getId()));
    } 
    
    /**
     * Return the next protocols as a Vector.
     *
     * @return The next protocols as a Vector.
     */
    public Vector getNext() {
        return next;
    }
    
    /**
     * Accessor to the current protocol.
     *
     * @return current protocol.
     */
    public Protocol getCurrent() {
        return this.current;
    }   
    
    public String getHTMLView()
     {
        Iterator iter;Protocol prot;String temp=null;
        StringBuffer html_string = new StringBuffer();
        iter = next.iterator();
        html_string.append("<ul>");
        while(iter.hasNext())
        {
            prot = (Protocol)iter.next();
            if ( !prot.getProcessname().equals(temp ))
            {
                temp = prot.getProcessname();
                html_string.append("<li>"+prot.getProcessname()+"</li>");
            }
        }
       // html_string.append("</ul>");
        return html_string.toString();
    }
}
