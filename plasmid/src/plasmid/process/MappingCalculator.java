/*
 * MappingCalculator.java
 *
 * Created on May 26, 2005, 11:10 AM
 */

package plasmid.process;

import java.util.*;

/**
 *
 * @author  DZuo
 */
public abstract class MappingCalculator {
    protected List srcContainers;
    protected List destContainers;
    protected String destSampleType;
    
    /** Creates a new instance of MappingCalculator */
    public MappingCalculator() {
    }
    
    public MappingCalculator(List src, List dest, String destSampleType) {
        this.srcContainers = src;
        this.destContainers = dest;
        this.destSampleType = destSampleType;
    }
    
    public List getSrcContainers() {return srcContainers;}
    public List getDestContainers() {return destContainers;}
    public String getDestSampleType() {return destSampleType;}
    
    public void setSrcContainers(List l) {this.srcContainers = l;}
    public void setDestContainers(List l) {this.destContainers = l;}
    public void setDestSampleType(String s) {this.destSampleType = s;}
    
    abstract public boolean isMappingValid();
    abstract public List calculateMapping();
}
