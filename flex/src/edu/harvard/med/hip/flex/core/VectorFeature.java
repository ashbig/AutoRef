/*
 * VectorFeature.java
 *
 * Created on June 17, 2003, 1:38 PM
 */

package edu.harvard.med.hip.flex.core;

/**
 *
 * @author  dzuo
 */
public class VectorFeature {
    protected int id;
    protected String name;
    protected String description;
    protected String status;
    
    /** Creates a new instance of VectorFeature */
    public VectorFeature() {
    }
    
    public VectorFeature(int id, String name, String description, String status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }
    
    public int getId() {return id;}
    public String getName() {return name;}
    public String getDescription() {return description;}
    public String getStatus() {return status;}
}
