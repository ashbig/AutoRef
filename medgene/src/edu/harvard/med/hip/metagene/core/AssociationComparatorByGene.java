/*
 * AssociationComparatorByGene.java
 *
 * Created on February 14, 2002, 4:51 PM
 */

package edu.harvard.med.hip.metagene.core;

import java.util.*;

/**
 *
 * @author  dzuo
 * @version 
 */
public class AssociationComparatorByGene implements Comparator {

    /** Creates new AssociationComparatorByGene */
    public AssociationComparatorByGene() {
    }
    
    public int compare(Object o1, Object o2) {
        Association a1 = (Association)o1;
        Association a2 = (Association)o2;
        
        if(a1.getGene().getHipGeneId()<a1.getGene().getHipGeneId())
            return -1;
        if(a1.getGene().getHipGeneId()>a1.getGene().getHipGeneId())
            return 1;
        return 0;
    }
}
