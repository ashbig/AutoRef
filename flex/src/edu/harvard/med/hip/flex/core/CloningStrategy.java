/*
 * CloningStrategy.java
 *
 * Created on June 17, 2003, 1:35 PM
 */

package edu.harvard.med.hip.flex.core;

/**
 *
 * @author  dzuo
 */
public class CloningStrategy {
    protected int id;
    protected String name;
    protected CloneVector clonevector;
    protected CloneLinker linker5p;
    protected CloneLinker linker3p;
    
    /** Creates a new instance of CloningStrategy */
    public CloningStrategy() {
    }
    
    public CloningStrategy(int id, String name, CloneVector clonevector, CloneLinker linker5p, CloneLinker linker3p) {
        this.id = id;
        this.name = name;
        this.clonevector = clonevector;
        this.linker5p = linker5p;
        this.linker3p = linker3p;
    }
    
    public int getId() {return id;}
    public String getName() {return name;}
    public CloneVector getClonevector() {return clonevector;}
    public CloneLinker getLinker5p() {return linker5p;}
    public CloneLinker getLinker3p() {return linker3p;}
}
