/*
 * ThreadedRearrayedSeqPlatesHandler.java
 *
 * Created on January 28, 2004, 1:49 PM
 */

package edu.harvard.med.hip.flex.util;

import java.util.*;

/**
 *
 * @author  DZuo
 */
public class ThreadedRearrayedSeqPlatesHandler extends RearrayedSeqPlatesHandler implements Runnable {
    private String researcherBarcode;
    private List containers;
    
    /** Creates a new instance of ThreadedRearrayedSeqPlatesHandler */
    public ThreadedRearrayedSeqPlatesHandler() {
        super();
    }
    
    public ThreadedRearrayedSeqPlatesHandler(List containers, String barcode, boolean b) {
        super(b);
        this.containers = containers;
        this.researcherBarcode = barcode;
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
        processSeqPlates(containers, researcherBarcode);
    }
    
}
