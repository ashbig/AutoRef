/*
 * CloneInfoComparator.java
 *
 * Created on March 26, 2003, 4:11 PM
 */

package edu.harvard.med.hip.cloneOrder.core;
import java.util.*;
/**
 *
 * @author  hweng
 */

public class CloneInfoComparator implements Comparator {

    
    public int compare(Object o1, Object o2) {
        CloneInfo c1 = (CloneInfo)o1;
        CloneInfo c2 = (CloneInfo)o2;        
        return (c1.getGeneSymbol().compareToIgnoreCase(c2.getGeneSymbol()));
    }
}
    

