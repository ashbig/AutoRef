/*
 * GeneSymbol.java
 *
 * Created on July 18, 2003, 11:25 AM
 */

package plasmid.coreobject;

/**
 *
 * @author  dzuo
 */
public class GeneSymbol {
    private int symbolid;
    private String symbol;
    private String type;
    private int locusid;
    
    /** Creates a new instance of GeneSymbol */
    public GeneSymbol() {
    }
    
    public GeneSymbol(String symbol, String type) {
        this.symbol = symbol;
        this.type = type;
    }
    
    public int getSymbolid() {return symbolid;}
    public String getSymbol() {return symbol;}
    public String getType() {return type;}
    public int getLocusid() {return locusid;}
    
    public void setSymbolid(int symbolid) {this.symbolid = symbolid;}
    public void setSymbol(String symbol) {this.symbol = symbol;}
    public void setType(String type) {this.type = type;}
    public void setLocusid(int locusid) {this.locusid = locusid;}
}
