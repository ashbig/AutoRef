/*
 * GeneIndexComparator.java
 *
 * Created on July 9, 2002, 2:46 PM
 */

package edu.harvard.med.hip.metagene.core;

/**
 *
 * @author  hweng
 */

import java.util.Comparator;

public class GeneIndexComparator implements Comparator{
    
    public int compare(Object o1, Object o2){ 
        if(((GeneIndex)o1).getIndex().compareToIgnoreCase(((GeneIndex)o2).getIndex()) > 0)
            return 1;
        else if (((GeneIndex)o1).getIndex().compareToIgnoreCase(((GeneIndex)o2).getIndex()) < 0)
            return -1;
        else
            return 0;
    }
}
