/*
 * HomologChipGeneComparator.java
 *
 * Created on July 10, 2002, 11:22 AM
 */

package edu.harvard.med.hip.metagene.core;

/**
 *
 * @author  hweng
 */
    
import java.util.Comparator;


public class HomologChipGeneComparator implements Comparator{
    
    public int compare(Object o1, Object o2){
        if(((HomologChipGene)o1).getScore() >((HomologChipGene)o2).getScore())
            return -1;
        else if (((HomologChipGene)o1).getScore() <((HomologChipGene)o2).getScore()) 
            return 1;
        else {
            if (((HomologChipGene)o1).getHomolog_id().compareToIgnoreCase(((HomologChipGene)o2).getHomolog_id()) > 0) 
                return 1;
            else if (((HomologChipGene)o1).getHomolog_id().compareToIgnoreCase(((HomologChipGene)o2).getHomolog_id()) < 0)
                return -1;
            else {
                if (((HomologChipGene)o1).getLocus_id() > ((HomologChipGene)o2).getLocus_id()) 
                    return 1;
                else if (((HomologChipGene)o1).getLocus_id() > ((HomologChipGene)o2).getLocus_id())
                    return -1;
                return 0;
            }
        }
    }
}
