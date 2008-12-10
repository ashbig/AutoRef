/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package plasmid.util;

import java.util.Comparator;
import plasmid.coreobject.Clone;

/**
 *
 * @author DZuo
 */
public class OriginalCloneidComparator implements Comparator {

    public OriginalCloneidComparator() {
    }
    
    public int compare(Object p1, Object p2) {
        String name1 = ((Clone)p1).getOriginalCloneid();
        String name2 = ((Clone)p2).getOriginalCloneid();
                
        if(name1 == null) {
            if(name2 == null) {
                return 0;
            } else {
                return 1;
            }
        } else {
            if(name2 == null) {
                return -1;
            } else {
                return (name1.compareTo(name2));
            }
        } 
    }
}
