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
    public static final String SEARCH = "Search";
    public static final String DISPLAY = "Display";
    public static final String BOTH = "both";
    public static final String GENBANK = "GenBank Accession";
    public static final String GI = "GI";
    public static final String GENEID = "Gene ID";
    public static final String SYMBOL = "Gene Symbol";
    public static final String PA = "PA Number";
    public static final String PRO_GENBANK = "Protein GenBank Accession";
    public static final String PRO_GI = "Protein GI";
    public static final String SGD = "SGD Number";
    public static final String VCNUMBER = "Locus Tag (VC number)";
    public static final String FTNUMBER = "Locus Tag (FTT number)";
    public static final String FBID = "FlyBase ID";
    public static final String WBGENEID = "WB Gene ID";
    public static final String BANUMBER = "Locus Tag (BA number)";
    public static final String TAIR = "TAIR ID";
    public static final String LOCUS_TAG = "Locus Tag";
    
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
