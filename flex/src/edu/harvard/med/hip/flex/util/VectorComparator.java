/*
 * VectorComparator.java
 *
 * Created on November 28, 2005, 11:07 AM
 */

package edu.harvard.med.hip.flex.util;

import java.util.*;

/**
 *
 * @author  DZuo
 */
public class VectorComparator implements Comparator {
    
    /** Creates a new instance of VectorComparator */
    public VectorComparator() {
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
        String v1 = (String)o1;
        String v2 = (String)o2;
        
        if(("pDONR201".equals(v1) || "pDONR221".equals(v1) || "pGW_NcoI-XhoI".equals(v1))
        && ("pDONR201".equals(v2) || "pDONR221".equals(v2) || "pGW_NcoI-XhoI".equals(v2)))
            return 0;
        else
            return v1.compareTo(v2);        
    }
    
    public static void main(String args[]) {
        Set s = new TreeSet(new VectorComparator());
        s.add("pDONR201");
        s.add("pDONR221");
        s.add("pGW_NcoI-XhoI");
       //s.add("pDONRDual");
        System.out.println(s.size());
    }
}
