/*
 * Disease.java
 *
 * Created on December 11, 2001, 2:46 PM
 */

package edu.harvard.med.hip.metagene.core;

import java.util.*;

/**
 *
 * @author  dzuo
 * @version 
 */
public class Disease {
    private int id;
    private String term;
    private String date;

    public Disease(int id) {
        this.id = id;
    }
    
    public Disease(String term) {
        this.term = term;
    }
    
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
