/*
 * RequestImporterLogger.java
 *
 * Created on October 26, 2001, 1:12 PM
 */

package edu.harvard.med.hip.flex.infoimport;

/**
 *
 * @author  dzuo
 * @version 
 */
public class RequestImporterLogger extends SequenceImporterLogger {
    private String username;
    
    /** Creates new RequestImporterLogger */
    public RequestImporterLogger(String sequenceid, int flexid, 
                                  boolean successful, String message,
                                  String username) {
        super(sequenceid, flexid, successful, message);
        this.username = username;
    }

    /**
     * Set the username to the given value.
     *
     * @param username The value to be set to.
     */
    public void setUsername(String username) {
        this.username = username;
    }
    
    /**
     * Return the username.
     * 
     * @return The username.
     */
    public String getUsername() {
        return username;
    }
}
