/*
 * CloneInfoTargetPlateWellComparator.java
 *
 * Created on June 7, 2006, 2:39 PM
 */

package plasmid.util;

import java.util.*;
import plasmid.query.coreobject.*;
import plasmid.util.PlatePositionConvertor;
import plasmid.Constants;

/**
 *
 * @author  DZuo
 */
public class CloneInfoTargetPlateWellComparator implements Comparator {
    
    /** Creates a new instance of CloneInfoTargetPlateWellComparator */
    public CloneInfoTargetPlateWellComparator() {
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
        String well1 = ((CloneInfo)o1).getTargetWell();
        String well2 = ((CloneInfo)o2).getTargetWell();
        
        String name1 = ((CloneInfo)o1).getTargetPlate();
        String name2 = ((CloneInfo)o2).getTargetPlate();
                
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
                if(name1.equals(name2)) {
                    if(well1 == null) {
                        if(well2 == null) {
                            return 0;
                        } else {
                            return 1;
                        }
                    } else {
                        if(well2 == null) {
                            return -1;
                        } else {
                            int p1 = 0;
                            int p2 = 0;
                            try {
                                p1 = PlatePositionConvertor.convertWellFromA8_12toInt(well1);
                                p2 = PlatePositionConvertor.convertWellFromA8_12toInt(well2);
                            } catch (Exception ex) {
                                if(Constants.DEBUG) {
                                    System.out.println(ex);
                                }
                            }
                            
                            if(p1<p2)
                                return -1;
                            else if(p1>p2)
                                return 1;
                            else
                                return 0;
                        }
                    }
                } else {
                    return (name1.compareTo(name2));
                }
            }
        } 
    }
}
