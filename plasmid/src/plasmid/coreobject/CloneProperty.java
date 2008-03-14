/*
 * CloneProperty.java
 *
 * Created on May 12, 2005, 3:48 PM
 */

package plasmid.coreobject;

/**
 *
 * @author  DZuo
 */
public class CloneProperty {
    public static final String PROTEIN_EXPRESSED = "ProteinExpressed";
    public static final String SOLUBLE_PROTEIN = "SolubleProtein";
    public static final String PROTEIN_PURIFIED = "ProteinPurified";
    
    public static final int CAT_PSI = 1;
    
    public static final String NOT_TESTED = "Not_Tested";
    public static final String NOT_APP = "Not_Applicable";
    public static final String NOT_FOUND = "Tested_Not_Found";
    public static final String PROTEIN_CONFIRMED = "Protein_Confirmed";
    public static final String NOT_SOLUBLE = "Tested_Not_Soluble";
    public static final String SOLUBLE = "Protein_Soluble";
    public static final String NOT_PURIFIED = "Tested_Not_Purified";
    public static final String PURIFIED = "Protein_Purified";
    
    private int cloneid;
    private String type;
    private String value;
    private String extrainfo;
    
    /** Creates a new instance of CloneProperty */
    public CloneProperty() {
    }
    
    public CloneProperty(int cloneid, String type, String value, String extrainfo) {
        this.cloneid = cloneid;
        this.type = type;
        this.value = value;
        this.extrainfo = extrainfo;
    }
    
    public int getCloneid() {return cloneid;}
    public String getType() {return type;}
    public String getValue() {return value;}
    public String getExtrainfo() {return extrainfo;}
    
    public void setCloneid(int id) {this.cloneid = id;}
    public void setType(String s) {this.type = s;}
    public void setValue(String s) {this.value = s;}
    public void setExtrainfo(String s) {this.extrainfo = s;}
}
