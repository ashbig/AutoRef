/*
 * Clones.java
 *
 * Created on April 24, 2003, 4:48 PM
 */

package edu.harvard.med.hip.flex.core;

/**
 *
 * @author  dzuo
 */
public class Clones {
    protected int cloneid;
    protected String clonename;
    protected String clonetype;
    protected int mastercloneid;
    protected int sequenceid;
    protected int constructid;
    protected int strategyid;
    
    /** Creates a new instance of Clones */
    public Clones() {
    }
    
    public int getCloneid() {return cloneid;}
    public String getClonename() {return clonename;}
    public String getClonetype() {return clonetype;}
}
