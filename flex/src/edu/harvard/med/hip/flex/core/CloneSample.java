/*
 * CloneSample.java
 *
 * Created on April 24, 2003, 5:17 PM
 */

package edu.harvard.med.hip.flex.core;

/**
 *
 * @author  dzuo
 */
public class CloneSample extends Sample {
    protected int cloneid;
    
    public CloneSample(int id, int position, int containerid, int cloneid) {
        super(id, position, containerid);
        this.cloneid = cloneid;
    }
    
    public void setCloneid(int cloneid) {
        this.cloneid = cloneid;
    }
    
    public int getCloneid() {
        return cloneid;
    }
}
