/*
 * AssociationComparatorByScore.java
 *
 * Created on October 9, 2002, 2:38 PM
 */

package edu.harvard.med.hip.metagene.core;
import java.util.*;

/**
 *
 * @author  hweng
 */

public class AssociationComparatorByScore implements Comparator{
    
    public int compare(Object o1, Object o2) {
        Association a1 = (Association)o1;
        Association a2 = (Association)o2;
        
        if(a1.getStat().getScore()<a2.getStat().getScore())
            return 1;
        else if(a1.getStat().getScore()>a2.getStat().getScore())
            return -1;
        else{
            if(a1.getGene().getHipGeneId()<a2.getGene().getHipGeneId())
                return -1;
            else if(a1.getGene().getHipGeneId()>a2.getGene().getHipGeneId())
                return 1;
            else
                return 0;
        }
    }    
    
}
