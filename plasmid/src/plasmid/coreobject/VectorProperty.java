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
    public static final String TYPE = "Type";
    
    private int vectorid;
    private String propertyType;
    private String displayValue;
    private String category;
    
    /** Creates a new instance of VectorProperty */
    public VectorProperty() {
    }

    public VectorProperty(int vectorid, String propertyType, String value) {
        this.vectorid=vectorid;
        this.propertyType=propertyType;
        this.displayValue = value;
    }
    
    public VectorProperty(int vectorid, String propertyType) {
        this.vectorid=vectorid;
        this.propertyType=propertyType;
    }
    
    public VectorProperty(String type, String value) {
        this.propertyType = type;
        this.displayValue = value;
    }
    
    public int getVectorid() {return vectorid;}
    public String getPropertyType() {return propertyType;}
    public String getDisplayValue() {return displayValue;}
    
    public void setVectorid(int id) {this.vectorid = id;}
    public void setPropertyType(String type) {this.propertyType = type;}
    public void setDisplayValue(String s) {this.displayValue = s;}

    /**
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }
}
