/*
 * BatchOrderClonePlateComparator.java
 *
 * Created on June 7, 2006, 1:46 PM
 */

package plasmid.util;

import java.util.*;
import plasmid.coreobject.*;

/**
 *
 * @author  DZuo
 */
public class BatchOrderClonePlateComparator implements Comparator {
    
    /** Creates a new instance of BatchOrderClonePlateComparator */
    public BatchOrderClonePlateComparator() {
    }
    
    /** Compares its two arguments for order.  Returns a negative integer,
     * zero, or a positive integer as the first argument is less than, equal
     * to, or greater than the second.<p>
     *
     * The implementor must ensure that <tt>sgn(compare(x, y)) ==
     * -sgn(compare(y, x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
     * implies that <tt>compare(x, y)</tt> must throw an exception if and only
     * if <tt>compare(y, x)</tt> throws an exception.)<p>
     *
     * The implementor must also ensure that the relation is transitive:
     * <tt>((compare(x, y)&gt;0) &amp;&amp; (compare(y, z)&gt;0))</tt> implies
     * <tt>compare(x, z)&gt;0</tt>.<p>
     *
     * Finally, the implementer must ensure that <tt>compare(x, y)==0</tt>
     * implies that <tt>sgn(compare(x, z))==sgn(compare(y, z))</tt> for all
     * <tt>z</tt>.<p>
     *
     * It is generally the case, but <i>not</i> strictly required that
     * <tt>(compare(x, y)==0) == (x.equals(y))</tt>.  Generally speaking,
     * any comparator that violates this condition should clearly indicate
     * this fact.  The recommended language is "Note: this comparator
     * imposes orderings that are inconsistent with equals."
     *
     * @param o1 the first object to be compared.
     * @param o2 the second object to be compared.
     * @return a negative integer, zero, or a positive integer as the
     * 	       first argument is less than, equal to, or greater than the
     * 	       second.
     * @throws ClassCastException if the arguments' types prevent them from
     * 	       being compared by this Comparator.
     *
     */
    public int compare(Object o1, Object o2) {
        String name1 = ((BatchOrder)o1).getPlate();
        String name2 = ((BatchOrder)o2).getPlate();
                
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
