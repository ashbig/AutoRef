/*
 * Logger.java
 *
 * Created on September 26, 2001, 2:05 PM
 *
 * This class handles the general log messages.
 */

package edu.harvard.med.hip.utility;

import java.io.*;
import java.util.*;

/**
 *
 * @author  dzuo
 * @version
 */
public class Logger {
    private String fileName;
    private PrintWriter LOG;
    
    /** Creates new logger */
    public Logger() {
    }
    
    /**
     * Constructor.
     *
     * @param fileName The name of the log file.
     * @return A Logger object.
     */
    public Logger(String fileName) {
        this.fileName = fileName;
    }
    
    /**
     * Start the log by opening the log file for append.
     *
     * @return True if successful; false if failed.
     */
    public boolean start() {
        try {
            LOG = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)));
            java.util.Date today = new java.util.Date();
            LOG.println("===========================================================");
            LOG.println("Begin: "+today);
            
            return true;
        } catch (Exception ex) {
            System.out.println(ex);
            return false;
        }        
    }
    
    /**
     * Write the message to the log file.
     *
     * @param message The log message to be written.
     */
    public void logging(String message) {
        LOG.println(message);
    }
    
    /**
     * Close the log stream.
     */
    public void end() {
        if(LOG == null) 
            return;
        
        java.util.Date today = new java.util.Date();
        LOG.println("End: "+today);
        LOG.println("===========================================================");
        
        LOG.close();
    }
    
    public static void main(String args[]) {
        String file = "/tmp/testlog.txt";
        Logger log = new Logger(file);
        
        if(!log.start()) {
            System.out.println("Cannot instantiate Logger object.");
            return;
        }
        
        log.logging("This is the test.");
        log.end();
    }
}
