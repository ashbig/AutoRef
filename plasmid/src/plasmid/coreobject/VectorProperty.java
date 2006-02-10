/*
 * VectorProperty.java
 *
 * Created on April 13, 2005, 9:59 AM
 */

package plasmid.coreobject;

/**
 *
 * @author  DZuo
 */
public class VectorProperty {
    public static final String ASSAY="Assay";
    public static final String CLONING="Cloning System";
    public static final String EXPRESSION="Expression";
    
    private int vectorid;
    private String propertyType;
    
    /** Creates a new instance of VectorProperty */
    public VectorProperty() {
    }
    
    public VectorProperty(int vectorid, String propertyType) {
        this.vectorid=vectorid;
        this.propertyType=propertyType;
    }
    
    public int getVectorid() {return vectorid;}
    public String getPropertyType() {return propertyType;}
    
    public void setVectorid(int id) {this.vectorid = id;}
    public void setPropertyType(String type) {this.propertyType = type;}
}
