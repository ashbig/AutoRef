/*
 * ThreadedCloneStorageManager.java
 *
 * Created on March 16, 2004, 1:22 PM
 */

package edu.harvard.med.hip.flex.process;

import java.util.*;

/**
 *
 * @author  DZuo
 */
public class ThreadedCloneStorageManager extends CloneStorageManager implements Runnable {
    private List containers;
    private String storageType;
    private String storageForm;
    
    /** Creates a new instance of ThreadedCloneStorageManager */
    public ThreadedCloneStorageManager(List containers, String storageType, String storageForm) {
        super();
        this.containers = containers;
        this.storageType = storageType;
        this.storageForm = storageForm;
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
        if(addCloneStorage(containers, storageType, storageForm)) {
            sendEmail(true, containers);
        } else {
            sendEmail(false, containers);
        }
    }    
}
