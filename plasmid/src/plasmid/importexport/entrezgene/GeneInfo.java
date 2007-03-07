/*
 * GeneInfo.java
 *
 * Created on February 20, 2007, 11:02 AM
 */

package plasmid.importexport.entrezgene;

/**
 *
 * @author  DZuo
 */
public class GeneInfo {
    private int geneid;
    private int taxid;
    private String symbol;
    private String description;
    private String type;
    private String locustag;
    private String chromosome;
    private String maplocation;
    
    /** Creates a new instance of GeneInfo */
    public GeneInfo() {
    }
    
    public GeneInfo(int geneid, int taxid, String symbol, String description, String type,
    String locustag, String chromosome, String maplocation) {
        this.geneid = geneid;
        this.taxid = taxid;
        this.symbol = symbol;
        this.description = description;
        this.type = type;
        this.locustag = locustag;
        this.chromosome = chromosome;
        this.maplocation = maplocation;
    }
    
    public int getGeneid() {return geneid;}
    public int getTaxid() {return taxid;}
    public String getSymbol() {return symbol;}
    public String getDescription() {return description;}
    public String getType() {return type;}
    public String getLocustag() {return locustag;}
    public String getChromosome() {return chromosome;}
    public String getMaplocation() {return maplocation;}
}
