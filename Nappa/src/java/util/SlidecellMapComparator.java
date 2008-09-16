/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

import java.util.Comparator;
import transfer.SlidecellTO;
import transfer.SlidecelllineageTO;

/**
 *
 * @author DZuo
 */
public class SlidecellMapComparator implements Comparator {
    
    /** Creates a new instance of SlidecellComparator */
    public SlidecellMapComparator() {
    }
    
    public int compare(Object o1, Object o2) {
        SlidecelllineageTO sample1 = (SlidecelllineageTO)o1;
        SlidecelllineageTO sample2 = (SlidecelllineageTO)o2;
        
        SlidecellTO cell1 = (SlidecellTO)sample1.getCell();
        SlidecellTO cell2 = (SlidecellTO)sample2.getCell();
        
        if(Integer.parseInt(cell1.getPosx())<Integer.parseInt(cell2.getPosx()))
            return -1;
        else if(Integer.parseInt(cell1.getPosx())>Integer.parseInt(cell2.getPosx()))
            return 1;
        else
            return 0;
    }
}