/*
 * CloneLinker.java
 *
 * Created on June 17, 2003, 1:44 PM
 */

package edu.harvard.med.hip.flex.core;

/**
 *
 * @author  dzuo
 */
public class CloneLinker {
    protected int id;
    protected String name;
    protected String sequence;
    
    /** Creates a new instance of CloneLinker */
    public CloneLinker() {
    }
    
    public CloneLinker(CloneLinker c) {
        if(c != null) {
            this.id = c.getId();
            this.name = c.getName();
            this.sequence = c.getSequence();
        }
    }
    
    public CloneLinker(int id) {
        this.id = id;
    }
    
    public CloneLinker(int id, String name, String sequence) {
        this.id = id;
        this.name = name;
        this.sequence = sequence;
    }
    
    public int getId() {return id;}
    public String getName() {return name;}
    public String getSequence() {return sequence;}
}
