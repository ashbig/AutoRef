/*
 * ThreadedSummaryTablePopulator.java
 *
 * Created on November 24, 2003, 3:56 PM
 */

package edu.harvard.med.hip.flex.util;

import java.util.*;

/**
 *
 * @author  DZuo
 */
public class ThreadedSummaryTablePopulator extends SummaryTablePopulator implements Runnable {
    List containers;
    int strategyid;
    String cloneType;
    
    /** Creates a new instance of ThreadedSummaryTablePopulator */
    public ThreadedSummaryTablePopulator(List containers, int strategyid, String cloneType) {
        super();
        this.containers = containers;
        this.strategyid = strategyid;
        this.cloneType = cloneType;
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
        if(populateObtainedMasterClonesWithContainers(containers, strategyid, cloneType)) {
            sendEmail(true, containers);
        } else {
            sendEmail(false, containers);
        }
    }   
}
