/*
 * SequenceImporterLogger.java
 *
 * This class holds the information about the result of the importing.
 *
 * Created on October 24, 2001, 1:35 PM
 */

package edu.harvard.med.hip.flex.infoimport;

/**
 *
 * @author  dzuo
 * @version 
 */
public class SequenceImporterLogger {
    private String sequenceid;
    private int flexid;
    private boolean successful;
    private String message;
    
    /** Creates new SequenceImporterLogger */
    public SequenceImporterLogger(String sequenceid, int flexid, boolean successful, String message) {
        this.sequenceid = sequenceid;
        this.flexid = flexid;
        this.successful = successful;
        this.message = message;
    }

    /**
     * Accessor method.
     *
     * @return sequenceid.
     */
    public String getSequenceid() {
        return sequenceid;
    }
    
    /**
     * Accessor method.
     *
     * @reuturn flexid.
     */
    public int getFlexid() {
        return flexid;
    }
    
    /**
     * Accessor method.
     *
     * @return successful.
     */
    public boolean getSuccessful() {
        return successful;
    }
    
    /**
     * Accessor method.
     *
     * @return message.
     */
    public String getMessage() {
        return message;
    }     
}
