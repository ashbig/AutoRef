/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package plasmid.util;

import java.util.Comparator;
import java.util.List;
import plasmid.coreobject.Clone;
import plasmid.coreobject.DnaInsert;

/**
 *
 * @author DZuo
 */
public class InsertMutDisComparator implements Comparator {
    
    /** Creates a new instance of InsertMutationComparator */
    public InsertMutDisComparator() {
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
        List inserts1 = ((Clone)o1).getInserts();
        List inserts2 = ((Clone)o2).getInserts();
        
        String s1 = null;
        String s2 = null;
        try {
            s1 = ((DnaInsert)inserts1.get(0)).getHasmutdis();
            s2 = ((DnaInsert)inserts2.get(0)).getHasmutdis();
        } catch (Exception ex) {}
        
        if(s1 == null) {
            if(s2 == null) {
                return 0;
            } else {
                return 1;
            }
        } else {
            if(s2 == null) {
                return -1;
            } else {
                return (s1.compareTo(s2));
            }
        }
    }
    
}
