/*
 * ThreadedRearrayedSeqPlatesHandler.java
 *
 * Created on January 28, 2004, 1:49 PM
 */

package edu.harvard.med.hip.flex.util;

import java.util.*;
import java.lang.Thread;
import edu.harvard.med.hip.flex.core.*;

/**
 *
 * @author  DZuo
 */
public class ThreadedRearrayedSeqPlatesHandler extends RearrayedSeqPlatesHandler implements Runnable {
    private String researcherBarcode;
    private List containers;
    private List glycerolContainers;
    private int strategyid;
    private String cloneType;
    
    /** Creates a new instance of ThreadedRearrayedSeqPlatesHandler */
    public ThreadedRearrayedSeqPlatesHandler() {
        super();
    }
    
    public ThreadedRearrayedSeqPlatesHandler(List containers, String barcode, boolean b) {
        super(b);
        this.containers = containers;
        this.researcherBarcode = barcode;
    }
    
    public ThreadedRearrayedSeqPlatesHandler(List containers, String barcode, boolean b, SummaryTablePopulator populator, List glycerolContainers, int strategyid, String cloneType) {
        super(b, populator);
        this.containers = containers;
        this.researcherBarcode = barcode;
        this.glycerolContainers = glycerolContainers;
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
        if(isFile) {
            emailFile(containers, researcherBarcode);
        }
        processSummaryTable(glycerolContainers, strategyid, cloneType);
        processSeqPlates(containers, researcherBarcode);
        insertSeqPlatesIntoClonestorage(containers);
    }
 
       public static void main(String args[]) {
           Container c1 = new Container(7893, null, null, "SAE000727");
           Container c2 = new Container(7894, null, null, "SBF000727");
           List containers = new ArrayList();
           containers.add(c1);
           containers.add(c2);
           
           List containerids = new ArrayList();
           containerids.add(new Integer(7893));
           containerids.add(new Integer(7894));
           
           SummaryTablePopulator populator = new SummaryTablePopulator();
           ThreadedRearrayedSeqPlatesHandler handler = new ThreadedRearrayedSeqPlatesHandler(containers, "dzuo", true, populator, containerids, 1, CloneInfo.MASTER_CLONE);
           try {
               new Thread(handler).start();
           } catch (Exception ex) {
               System.out.println(ex);
           }
       }
}
