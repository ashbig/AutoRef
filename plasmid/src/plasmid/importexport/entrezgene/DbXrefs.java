/*
 * DbXrefs.java
 *
 * Created on February 20, 2007, 11:37 AM
 */

package plasmid.importexport.entrezgene;

/**
 *
 * @author  DZuo
 */
public class DbXrefs {
    private int geneid;
    private String db;
    private String value;
    
    /** Creates a new instance of DbXrefs */
    public DbXrefs() {
    }
    
    public DbXrefs(int geneid, String db, String value) {
        this.geneid = geneid;
        this.db = db;
        this.value = value;
    }
    
    public int getGeneid() {return geneid;}
    public String getDb() {return db;}
    public String getValue() {return value;}
}
