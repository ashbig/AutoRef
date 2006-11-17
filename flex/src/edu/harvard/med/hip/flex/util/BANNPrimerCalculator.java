/*
 * BANNPrimerCalculator.java
 *
 * Created on October 13, 2006, 11:18 AM
 */

package edu.harvard.med.hip.flex.util;

/**
 *
 * @author  DZuo
 */
public class BANNPrimerCalculator extends NNPrimerCalculator {
    private static double PMDESIREDTM = 67.0;
    
    /** Creates a new instance of BANNPrimerCalculator */
    public BANNPrimerCalculator() {
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
