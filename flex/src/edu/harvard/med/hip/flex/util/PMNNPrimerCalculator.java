/*
 * PMNNPrimerCalculator.java
 *
 * Created on March 7, 2002, 2:23 PM
 */

package edu.harvard.med.hip.flex.util;

/**
 *
 * @author  dzuo
 * @version
 */
public class PMNNPrimerCalculator extends NNPrimerCalculator {
    private static double PMDESIREDTM = 65.0;
    
    /** Creates new PMNNPrimerCalculator */
    public PMNNPrimerCalculator() {
        super();
    }
    
    public double getDesiredTM() {
        return PMDESIREDTM;
    }
    
    public int adjustPosition(int pos) {
        if(pos < 22)
            return 22;
        
        return pos;
    }
}
