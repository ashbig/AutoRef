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
    private String              m_vector_name = null;
    
    /** Creates a new instance of VectorFeature */
    public VectorFeature() {
    }
    
    public VectorFeature(int id, String name, String description, String status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }
    
    public VectorFeature(VectorFeature v) {
        if(v != null) {
            this.id = v.getId();
            this.name = v.getName();
            this.description = v.getDescription();
            this.status = v.getStatus();
        }
    }
    
    public int getId() {return id;}
    public String getName() {return name;}
    public String getDescription() {return description;}
    public String getStatus() {return status;}
    public String   getVectorName(){ return m_vector_name;}
    
    public void         setName(String v) { name=v;}
    public void         setDescription(String v) { description= v;}
    public void         setStatus(String v) { status = v;}
    public void         setVectorName(String v){ m_vector_name = v;}
    
    public String       toString()
    {
       return name+" "+ description+" "+ status+" "+ m_vector_name;
    }
}
