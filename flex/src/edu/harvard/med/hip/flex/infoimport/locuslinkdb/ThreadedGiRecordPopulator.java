/*
 * GiRecordPopulator.java
 *
 * Created on November 6, 2003, 10:31 AM
 */

package edu.harvard.med.hip.flex.infoimport.locuslinkdb;

import java.io.*;
import java.util.*;
import java.sql.*;

import edu.harvard.med.hip.flex.util.FlexProperties;
import edu.harvard.med.hip.flex.util.FlexSeqAnalyzer;
import edu.harvard.med.hip.flex.query.core.GiRecord;
import edu.harvard.med.hip.flex.database.*;

/**
 *
 * @author  DZuo
 */
public class ThreadedGiRecordPopulator extends GiRecordPopulator implements Runnable {
    /** Creates a new instance of GIRecordPopulator */
    public ThreadedGiRecordPopulator(Collection records) {
        super(records);
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
        try {
            persistRecords();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
    
}
