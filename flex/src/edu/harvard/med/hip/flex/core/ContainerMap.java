/*
 * ContainerMap.java
 *
 * Created on November 28, 2005, 3:30 PM
 */

package edu.harvard.med.hip.flex.core;

import java.util.*;

/**
 *
 * @author  DZuo
 */
public class ContainerMap {
    private List src;
    private List dest;
    
    /** Creates a new instance of ContainerMap */
    public ContainerMap() {
    }
    
    public ContainerMap(List source, List destination) {
        this.src = new ArrayList();
        src.addAll(source);
        this.dest = new ArrayList();
        dest.addAll(destination);
    }
    
    public List getSrc() {return src;}
    public List getDest() {return dest;}
}
