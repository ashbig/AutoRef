/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plasmid.util;

import java.util.Comparator;
import plasmid.coreobject.OrderCloneValidation;

/**
 *
 * @author dongmei
 */
public class CloneValidationComparator implements Comparator {
    
    /** Creates a new instance of CloneInfoComparator */
    public CloneValidationComparator() {
    }
    
    public int compare(Object o1, Object o2) {
        int phred1 = ((OrderCloneValidation)o1).getPhred();
        int phred2 = ((OrderCloneValidation)o2).getPhred();
                
        if(phred1<phred2)
            return -1;
        else if(phred1==phred2)
            return 0;
        else
            return 1;
    }   
}
