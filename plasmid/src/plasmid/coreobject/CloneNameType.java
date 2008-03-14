/*
 * CloneNameType.java
 *
 * Created on April 14, 2005, 12:42 PM
 */

package plasmid.coreobject;

/**
 *
 * @author  DZuo
 */
public class CloneNameType {
    public static final String HIP_CLONE_ID = "HIP Clone ID";        
    public static final String HIP_MASTER_CLONE_ID = "HIP Master Clone ID";  
    public static final String ORIGINAL_CLONE_ID = "Original Clone ID"; 
    public static final String TARGETDB_ID = "TargetDB ID";
    public static final String PDB_ID = "PDB ID";
    public static final String PDB_ID_ALL = "PDB ID ALL";
    
    private String clonetype;
    private String nametype;
    private String use;
    
    /** Creates a new instance of CloneNameType */
    public CloneNameType() {
    }
    
    public CloneNameType(String clonetype, String nametype, String use) {
        this.clonetype = clonetype;
        this.nametype = nametype;
        this.use = use;
    }
    
    public String getClonetype() {return clonetype;}
    public String getNametype() {return nametype;}
    public String getUse() {return use;}
    
    public void setClonetype(String s) {this.clonetype = s;}
    public void setNametype(String s) {this.nametype = s;}
    public void setUse(String s) {this.use = s;}
}
