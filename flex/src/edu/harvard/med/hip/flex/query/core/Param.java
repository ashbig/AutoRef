/*
 * Param.java
 *
 * Created on August 5, 2003, 9:38 AM
 */

package edu.harvard.med.hip.flex.query.core;

/**
 *
 * @author  DZuo
 */
public class Param {
    public static final String BLASTDB = "Blast DB";
    public static final String BLASTHIT = "Blast Hit";
    public static final String BLASTPID = "Blast PID";
    public static final String BLASTLENGTH = "Blast Length";
    
    protected String name;
    protected String value;
    
    /** Creates a new instance of Param */
    public Param() {
    }
    
    public Param(String name, String value) {
        this.name = name;
        this.value = value;
    }
    
    public String getName() {return name;}
    public String getValue() {return value;}
    
}
