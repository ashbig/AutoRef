/*
 * StaticPrimerCalculatorFactory.java
 *
 * Created on November 20, 2003, 9:15 AM
 */

package edu.harvard.med.hip.flex.util;

import edu.harvard.med.hip.flex.workflow.Project;

/**
 *
 * @author  DZuo
 */
public class StaticPrimerCalculatorFactory {
    
    /** Creates a new instance of StaticPrimerCalculatorFactory */
    public StaticPrimerCalculatorFactory() {
    }
    
    public static NNPrimerCalculator makePrimerCalculator(Project project) {
        if(project.getId() == Project.PSEUDOMONAS)
            return new PMNNPrimerCalculator();
        
        return new NNPrimerCalculator();
    }
}
