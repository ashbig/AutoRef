/*
 * ThreadedExpressionSummaryTablePopulator.java
 *
 * Created on March 16, 2005, 10:08 AM
 */

package edu.harvard.med.hip.flex.util;

import java.util.*;

/**
 *
 * @author  DZuo
 */
public class ThreadedExpressionSummaryTablePopulator extends SummaryTablePopulator implements Runnable {
    List containers;
    int strategyid;
    
    /** Creates a new instance of ThreadedExpressionSummaryTablePopulator */
    public ThreadedExpressionSummaryTablePopulator(List containers, int strategyid) {
        super();
        this.containers = containers;
        this.strategyid = strategyid;
    }
    
    /** When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see     java.lang.Thread#run()
     *
     */
    public void run() {       
        if(populateExpressionClonesWithContainers(containers, strategyid)) {
            sendEmail(true, containers);
        } else {
            sendEmail(false, containers);
        }
    }
    
}
