/*
 * SequenceClone.java
 *
 * Created on April 24, 2003, 4:51 PM
 */

package edu.harvard.med.hip.flex.core;

/**
 *
 * @author  dzuo
 */
public class SequenceClone extends Clones {
    protected int sequencingid;
    protected String sequencingstatus;
    protected String sequencingfacility;
    protected String outdate;
    protected String indate;
    protected int sampleid;
    protected int containerid;
    protected String containerlabel;
    protected String position;
    
    /** Creates a new instance of SequenceClone */
    public SequenceClone() {
    }
    
    public int getSampleid() {return sampleid;}
    public int getContainerid() {return containerid;}
    public String getContainerlabel() {return containerlabel;}
    public String getPosition() {return position;}
    
}
