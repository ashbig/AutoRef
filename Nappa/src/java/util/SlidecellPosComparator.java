/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

import java.util.Comparator;
import transfer.SampleTO;
import transfer.SlidecellTO;

/**
 *
 * @author DZuo
 */
public class SlidecellPosComparator implements Comparator {
    
    /** Creates a new instance of SlidecellComparator */
    public SlidecellPosComparator() {
    }
    
    public int compare(Object o1, Object o2) {
        SampleTO sample1 = (SampleTO)o1;
        SampleTO sample2 = (SampleTO)o2;
        
        SlidecellTO cell1 = (SlidecellTO)sample1.getCell();
        SlidecellTO cell2 = (SlidecellTO)sample2.getCell();
        
        if(cell1.getPos()<cell2.getPos())
            return -1;
        else if(cell1.getPos() > cell2.getPos())
            return 1;
        else
            return 0;
    }
}

