/*
 * RefseqNameType.java
 *
 * Created on April 14, 2005, 3:36 PM
 */

package plasmid.coreobject;

/**
 *
 * @author  DZuo
 */
public class RefseqNameType {
    public static String SEARCH = "Search";
    public static String DISPLAY = "Display";
    public static String BOTH = "Both";
    
    private String species;
    private String refseqtype;
    private String nametype;
    private String use;
    
    /** Creates a new instance of RefseqNameType */
    public RefseqNameType() {
    }
    
    public RefseqNameType(String species, String refseqtype, String nametype, String use) {
        this.species = species;
        this.refseqtype = refseqtype;
        this.nametype = nametype;
        this.use = use;
    }
    
    public String getSpecies() {return species;}
    public String getRefseqtype() {return refseqtype;}
    public String getNametype() {return nametype;}
    public String getUse() {return use;}
    
    public void setSpecies(String s) {this.species = s;}
    public void setRefseqtype(String s) {this.refseqtype = s;}
    public void setNametype(String s) {this.nametype = s;}
    public void setUse(String s) {this.use = s;}
}
