/*
 * SubProtocol.java
 *
 * Represents the subprotocol table in the database.
 *
 * Created on July 19, 2001, 1:48 PM
 */

package edu.harvard.med.hip.flex.process;

/**
 *
 * @author  dzuo
 * @version 
 */
public class SubProtocol {
    private String name;
    private String description;
    
    /**
     * Constructor.
     */
    public SubProtocol(String name) {
        this.name = name;
    }
    
    /** Creates new SubProtocol */
    public SubProtocol(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Return the protocol name.
     *
     * @return The protocol name.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Return the protocol description.
     *
     * @return The protocol description.
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Set the protocol name.
     *
     * @param name The value to be set to.
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Set the protocol description.
     *
     * @param description The value to be set to.
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
