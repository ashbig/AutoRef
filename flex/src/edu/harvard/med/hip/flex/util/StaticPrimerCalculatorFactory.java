/*
 * StaticPrimerCalculatorFactory.java
 *
 * Created on November 20, 2003, 9:15 AM
 */

package edu.harvard.med.hip.flex.util;

import edu.harvard.med.hip.flex.workflow.Project;
import edu.harvard.med.hip.flex.workflow.Workflow;

/**
 *
 * @author  DZuo
 */
public class StaticPrimerCalculatorFactory {
    
    /** Creates a new instance of StaticPrimerCalculatorFactory */
    public StaticPrimerCalculatorFactory() {
    }
    
    public static NNPrimerCalculator makePrimerCalculator(Project project, Workflow workflow) {
        if(project.getId() == Project.PSEUDOMONAS)
            return new PMNNPrimerCalculator();
        
        if(workflow.getId() == Workflow.GATEWAY_LONG_PRIMER_WITH_EGEL)
            return new BANNPrimerCalculator();
        
        return new NNPrimerCalculator();
    }
}
