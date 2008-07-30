/*
 * SlidecellComparator.java
 *
 * Created on November 19, 2007, 3:28 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package util;

import java.util.Comparator;
import transfer.SampleTO;
import transfer.SlidecellTO;

/**
 *
 * @author dzuo
 */
public class SlidecellComparator implements Comparator {
    
    /** Creates a new instance of SlidecellComparator */
    public SlidecellComparator() {
    }
    
    public int compare(Object o1, Object o2) {
        SampleTO sample1 = (SampleTO)o1;
        SampleTO sample2 = (SampleTO)o2;
        
        SlidecellTO cell1 = (SlidecellTO)sample1.getCell();
        SlidecellTO cell2 = (SlidecellTO)sample2.getCell();
        
        if(cell1.getBlockx()<cell2.getBlockx())
            return -1;
        else if(cell1.getBlockx() > cell2.getBlockx())
            return 1;
        else if(cell1.getBlocky()<cell2.getBlocky())
            return -1;
        else if(cell1.getBlocky()>cell2.getBlocky())
            return 1;
        else if(cell1.getBlockwellx()<cell2.getBlockwellx())
            return -1;
        else if(cell1.getBlockwellx()>cell2.getBlockwellx())
            return 1;
        else if(cell1.getBlockwelly()<cell2.getBlockwelly())
            return -1;
        else if(cell1.getBlockwelly()>cell2.getBlockwelly())
            return 1;
        else
            return 0;
    }
}

