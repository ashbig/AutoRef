/*
 * SamplePositionComparator.java
 *
 * Created on October 10, 2007, 4:19 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package util;

import java.util.Comparator;
import transfer.SampleTO;

/**
 *
 * @author dzuo
 */
public class SamplePositionComparator implements Comparator {
    
    /** Creates a new instance of SamplePositionComparator */
    public SamplePositionComparator() {
    }
    
    public int compare(Object o1, Object o2) {
        int p1 = ((SampleTO)o1).getPosition();
        int p2 = ((SampleTO)o2).getPosition();
        
        if(p1<p2)
            return -1;
        if(p1>p2)
            return 1;
        return 0;
    }
}
