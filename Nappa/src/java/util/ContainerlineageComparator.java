/*
 * ContainerlineageComparator.java
 *
 * Created on October 16, 2007, 11:36 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package util;

import java.util.Comparator;
import transfer.ContainerlineageTO;

/**
 *
 * @author dzuo
 */
public class ContainerlineageComparator implements Comparator {
    
    /** Creates a new instance of ContainerlineageComparator */
    public ContainerlineageComparator() {
    }
    
    public int compare(Object o1, Object o2) {
        ContainerlineageTO c1 = (ContainerlineageTO)o1;
        ContainerlineageTO c2 = (ContainerlineageTO)o2;
        
        if(c1.getFrom().equals(c2.getFrom()) && c1.getTo().equals(c2.getTo()))
            return 0;
        else
            return 1;
    }
}
