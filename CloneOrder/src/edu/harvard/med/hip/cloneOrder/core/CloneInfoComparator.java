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
        int result1 = c1.getPlateId().compareToIgnoreCase(c2.getPlateId());
        int result2 = c1.getWellIdPosition().compareToIgnoreCase(c2.getWellIdPosition());
        if (result1!=0){  
            return (c1.getPlateId().compareToIgnoreCase(c2.getPlateId()));}
        else{
            return (c1.getWellIdPosition().compareToIgnoreCase(c2.getWellIdPosition()));}
    }
}
    

