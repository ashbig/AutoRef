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

/**
 *
 * @author  dzuo
 * @version 
 */
public class FlowRecord {
    private String current;
    private Vector next;

    /** Creates new FlowRecord 
     * 
     * @param current The current protocol name.
     * @param next A collection of next protocol names.
     *
     * @return The FlowRecord object.
     */
    public FlowRecord(String current, Vector next) {
        this.current = current;
        this.next = next;
    }
    
    /**
     * Return true if the given protocol name if equal to the
     * current protocol name; false otherwise.
     *
     * @param protocol The protocol name to be compared to.
     * @return True if the given name is equal to the current
     *  protocol name; false otherwise.
     */
    public boolean isEqual(String protocol) {
        return (current.equals(protocol));
    }
    
    /**
     * Return the next protocol names as a Vector.
     *
     * @return The next protocol names.
     */
    public Vector getNext() {
        return next;
    }
}
