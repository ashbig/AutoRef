/*
 * PlateComparator.java
 *
 * Created on April 7, 2003, 4:54 PM
 */

package edu.harvard.med.hip.flex.query;
import java.util.Comparator;

/**
 *
 * @author  hweng
 */
public class PlateComparator implements Comparator{
    
    public int compare(Object o1, Object o2){
        if( ((PlateSuccessInfo)o1).getLabel().compareToIgnoreCase(((PlateSuccessInfo)o2).getLabel()) > 0)
            return 1;
        else if( ((PlateSuccessInfo)o1).getLabel().compareToIgnoreCase(((PlateSuccessInfo)o2).getLabel()) < 0)
            return -1;
        else
            return 0;
        
    }
    
}
