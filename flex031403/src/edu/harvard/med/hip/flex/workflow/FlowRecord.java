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
}
