/*
 * FlexSeqStatusComparator.java
 *
 * Used for sorting the FlexSequence objects based on the flex status.
 *
 * Created on July 12, 2001, 3:05 PM
 */

package edu.harvard.med.hip.flex.util;

import java.util.*;
import edu.harvard.med.hip.flex.core.FlexSequence;

/**
 *
 * @author  dzuo
 * @version 
 */
public class FlexSeqStatusComparator implements Comparator {
    
    /** Creates new FlexSeqStatusComparator */
    public FlexSeqStatusComparator() {
    }
    
    public int compare(Object o1, Object o2) {
        String status1 = ((FlexSequence)o1).getFlexstatus();
        String status2 = ((FlexSequence)o2).getFlexstatus();
        int ret = 0;
        
        if(FlexSequence.OBTAINED.equals(status1)) {
            ret = -1;
        } else if (FlexSequence.INPROCESS.equals(status1)) {
            if(FlexSequence.OBTAINED.equals(status2)) {
                ret = 1;
            } else {
                ret = -1;
            }
        } else if(FlexSequence.PENDING.equals(status1)) {
            if(FlexSequence.OBTAINED.equals(status2) || FlexSequence.INPROCESS.equals(status2)) {
                ret = 1;
            } else {
                ret = -1;
            }
        } else if(FlexSequence.NEW.equals(status1)) {
            if(FlexSequence.FAILED.equals(status2) || FlexSequence.REJECTED.equals(status2)) {
                ret = -1;
            } else {
                ret = 1;
            } 
        } else if(FlexSequence.FAILED.equals(status1)) {
            if(FlexSequence.REJECTED.equals(status2)) {
                ret = -1;
            } else {
                ret = 1;
            }
        } else {
            ret = 1;
        }
        
        return ret;
    }

    public static void main(String [] args) {
        FlexSequence s1 = new FlexSequence(1, "OBTAINED", null, null, null, 1, 1, 1, 1, null);
        FlexSequence s2 = new FlexSequence(1, "INPROCESS", null, null, null, 1, 1, 1, 1, null);
        FlexSequence s3 = new FlexSequence(1, "PENDING", null, null, null, 1, 1, 1, 1, null);
        FlexSequence s4 = new FlexSequence(1, "REJECTED", null, null, null, 1, 1, 1, 1, null);
        FlexSequence s5 = new FlexSequence(1, "FAILED", null, null, null, 1, 1, 1, 1, null);
        FlexSequence s6 = new FlexSequence(1, "OBTAINED", null, null, null, 1, 1, 1, 1, null);

        Vector v = new Vector();
        v.addElement(s4);
        v.addElement(s2);
        v.addElement(s5);
        v.addElement(s1);
        v.addElement(s3);
        v.addElement(s6);
        
        System.out.println("Before sorting...");
        for(int i=0; i<v.size(); i++) {
            FlexSequence s = (FlexSequence)v.elementAt(i);
            System.out.println(s.getFlexstatus());
        }
        
        Collections.sort(v, new FlexSeqStatusComparator());
        
        System.out.println("After sorting...");
        for(int i=0; i<v.size(); i++) {
            FlexSequence s = (FlexSequence)v.elementAt(i);
            System.out.println(s.getFlexstatus());
        }
    }
}
