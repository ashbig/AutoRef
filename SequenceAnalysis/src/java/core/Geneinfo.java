/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

/**
 *
 * @author Lab User
 */
public class Geneinfo {
    private int geneid;
    private String symbol;
    private String description;
    private String type;
    private String locustag;
    private String synonyms;
    private String chromosome;
    private String maplocation;
    private String symbolnom;
    private String namenom;
    private String nomstatus;
    private int taxid;

    public Geneinfo() {}
    
    /**
     * @return the geneid
     */
    public int getGeneid() {
        return geneid;
    }

    /**
     * @param geneid the geneid to set
     */
    public void setGeneid(int geneid) {
        this.geneid = geneid;
    }

    /**
     * @return the symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * @param symbol the symbol to set
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the locustag
     */
    public String getLocustag() {
        return locustag;
    }

    /**
     * @param locustag the locustag to set
     */
    public void setLocustag(String locustag) {
        this.locustag = locustag;
    }

    /**
     * @return the synonyms
     */
    public String getSynonyms() {
        return synonyms;
    }

    /**
     * @param synonyms the synonyms to set
     */
    public void setSynonyms(String synonyms) {
        this.synonyms = synonyms;
    }

    /**
     * @return the chromosome
     */
    public String getChromosome() {
        return chromosome;
    }

    /**
     * @param chromosome the chromosome to set
     */
    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }

    /**
     * @return the maplocation
     */
    public String getMaplocation() {
        return maplocation;
    }

    /**
     * @param maplocation the maplocation to set
     */
    public void setMaplocation(String maplocation) {
        this.maplocation = maplocation;
    }

    /**
     * @return the symbolnom
     */
    public String getSymbolnom() {
        return symbolnom;
    }

    /**
     * @param symbolnom the symbolnom to set
     */
    public void setSymbolnom(String symbolnom) {
        this.symbolnom = symbolnom;
    }

    /**
     * @return the namenom
     */
    public String getNamenom() {
        return namenom;
    }

    /**
     * @param namenom the namenom to set
     */
    public void setNamenom(String namenom) {
        this.namenom = namenom;
    }

    /**
     * @return the nomstatus
     */
    public String getNomstatus() {
        return nomstatus;
    }

    /**
     * @param nomstatus the nomstatus to set
     */
    public void setNomstatus(String nomstatus) {
        this.nomstatus = nomstatus;
    }

    /**
     * @return the taxid
     */
    public int getTaxid() {
        return taxid;
    }

    /**
     * @param taxid the taxid to set
     */
    public void setTaxid(int taxid) {
        this.taxid = taxid;
    }
}
