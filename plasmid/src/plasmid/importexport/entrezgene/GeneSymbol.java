/*
 * GeneSymbol.java
 *
 * Created on February 20, 2007, 11:10 AM
 */

package plasmid.importexport.entrezgene;

/**
 *
 * @author  DZuo
 */
public class GeneSymbol {
    private String symbol;
    private int geneid;
    
    /** Creates a new instance of GeneSymbol */
    public GeneSymbol() {
    }
    
    public GeneSymbol(String symbol, int geneid) {
        this.symbol = symbol;
        this.geneid = geneid;
    }
    
    public String getSymbol() {return symbol;}
    public int getGeneid() {return geneid;}
}
