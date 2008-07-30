/*
 * ReagentTOComparator.java
 *
 * Created on October 1, 2007, 11:20 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package transfer;

import java.util.Comparator;

/**
 *
 * @author dzuo
 */
public class ReagentTOComparator implements Comparator {
    
    /** Creates a new instance of ReagentTOComparator */
    public ReagentTOComparator() {
    }
    
    public int compare(Object o1, Object o2) {
        ReagentTO r1 = (ReagentTO)o1;
        ReagentTO r2 = (ReagentTO)o2;
        return r1.getName().compareTo(r2.getName());
    }
}
