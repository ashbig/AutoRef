/*
 * ProtocolComparator.java
 *
 * This class implements the Comparator interface to define the orders of the Protocol object.
 * Created on November 7, 2001, 2:24 PM
 */

package edu.harvard.med.hip.flex.process;

import java.util.Comparator;

/**
 *
 * @author  dzuo
 * @version 
 */
public class ProtocolComparator implements Comparator {

    /** Creates new ProtocolComparator */
    public ProtocolComparator() {
    }
  
    public int compare(Object p1, Object p2) {
        int id1 = ((Protocol)p1).getId();
        int id2 = ((Protocol)p2).getId();
        
        if(id1 < id2) 
            return -1;
        
        if(id1 == id2)
            return 0;
        
        if(id1 > id2)
            return 1;
        
        return -1;
    }    
}
