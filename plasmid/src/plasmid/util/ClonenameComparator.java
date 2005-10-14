/*
 * ClonenameComparator.java
 *
 * Created on May 4, 2005, 3:44 PM
 */

package plasmid.util;

import java.util.*;

import plasmid.coreobject.Clone;

/**
 *
 * @author  DZuo
 */
public class ClonenameComparator implements Comparator {
    
    /** Creates a new instance of ClonenameComparator */
    public ClonenameComparator() {
    }
    
    public int compare(Object p1, Object p2) {
        String name1 = ((Clone)p1).getName();
        String name2 = ((Clone)p2).getName();
        return (name1.compareTo(name2));
    }
}
