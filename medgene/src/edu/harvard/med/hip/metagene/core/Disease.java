/*
 * Disease.java
 *
 * Created on December 11, 2001, 2:46 PM
 */

package edu.harvard.med.hip.metagene.core;

/**
 *
 * @author  dzuo
 * @version 
 */
public class Disease {
    private int id;
    private String term;
    private String date;
    
    /** Creates new Disease */
    public Disease(int id, String term, String date) {
        this.id = id;
        this.term = term;
        this.date = date;
    }

    /**
     * Return the disease id.
     *
     * @return The disease id.
     */
    public int getId() {
        return id;
    }
    
    /**
     * Return the disease term.
     *
     * @return The disease term.
     */
    public String getTerm() {
        return term;
    }
}
