/*
 * DiseaseComparator.java
 *
 * Created on January 30, 2002, 3:35 PM
 */

package edu.harvard.med.hip.metagene.core;

import java.util.*;

/**
 *
 * @author  dzuo
 * @version 
 */
public class DiseaseComparator implements Comparator {

    /** Creates new DiseaseComparator */
    public DiseaseComparator() {
    }
    
    public int compare(Object o1, Object o2) {
        Disease d1 = (Disease)o1;
        Disease d2 = (Disease)o2;
        
        return (d1.getTerm().compareToIgnoreCase(d2.getTerm()));
    }
}
