/*
 * GeneComparator.java
 *
 * Created on May 2, 2002, 4:37 PM
 */

package edu.harvard.med.hip.metagene.core;

/**
 *
 * @author  hweng
 */


import java.util.Comparator;

public class GeneComparator implements Comparator{
    
    public int compare(Object o1, Object o2){
        if(((ChipGene)o1).getScore() >((ChipGene)o2).getScore())
            return -1;
        else if (((ChipGene)o1).getScore() <((ChipGene)o2).getScore()) 
            return 1;
        else {
            if (((ChipGene)o1).getGene_symbol().compareToIgnoreCase(((ChipGene)o2).getGene_symbol()) > 0) 
                return 1;
            else if (((ChipGene)o1).getGene_symbol().compareToIgnoreCase(((ChipGene)o2).getGene_symbol()) < 0)
                return -1;
            else {
                if (((ChipGene)o1).getLocus_id() > ((ChipGene)o2).getLocus_id()) 
                    return 1;
                else if (((ChipGene)o1).getLocus_id() > ((ChipGene)o2).getLocus_id())
                    return -1;
                return 0;
            }
        }
    }
}