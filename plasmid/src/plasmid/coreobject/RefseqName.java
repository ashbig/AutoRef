/*
 * RefseqName.java
 *
 * Created on April 7, 2005, 3:06 PM
 */

package plasmid.coreobject;

/**
 *
 * @author  DZuo
 */
public class RefseqName {
    private int refseqid;
    private String nametype;
    private String namevalue;
    private String nameurl;
    
    /** Creates a new instance of RefseqName */
    public RefseqName() {
    }
    
    public RefseqName(int refseqid, String nametype, String namevalue, String nameurl) {
        this.refseqid = refseqid;
        this.nametype = nametype;
        this.namevalue = namevalue;
        this.nameurl = nameurl;
    }
    
    public int getRefseqid() {return refseqid;}
    public String getNametype() {return nametype;}
    public String getNamevalue() {return namevalue;}
    public String getNameurl() {return nameurl;}
    
    public void setRefseqid(int refseqid) {this.refseqid = refseqid;}
    public void setNametype(String s) {this.nametype = s;}
    public void setNamevalue(String s) {this.namevalue = s;}
    public void setNameurl(String s) {this.nameurl = s;}
}
