/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

/**
 *
 * @author dongmei
 */
public class Gene {
    public static String TYPE_PROTEIN_CODING = "protein-coding";
    
    private int geneid;
    private String symbol;
    private String locustag;
    private String synonyms;
    private String dbxrefs;
    private String chromosome;
    private String maplocation;
    private String description;
    private String type;
    private String nomensymbol;
    private String nomenname;
    private String nomenstatus;
    private String designations;
    private int modificationdate;

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
     * @return the dbxrefs
     */
    public String getDbxrefs() {
        return dbxrefs;
    }

    /**
     * @param dbxrefs the dbxrefs to set
     */
    public void setDbxrefs(String dbxrefs) {
        this.dbxrefs = dbxrefs;
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
     * @return the nomensymbol
     */
    public String getNomensymbol() {
        return nomensymbol;
    }

    /**
     * @param nomensymbol the nomensymbol to set
     */
    public void setNomensymbol(String nomensymbol) {
        this.nomensymbol = nomensymbol;
    }

    /**
     * @return the nomenname
     */
    public String getNomenname() {
        return nomenname;
    }

    /**
     * @param nomenname the nomenname to set
     */
    public void setNomenname(String nomenname) {
        this.nomenname = nomenname;
    }

    /**
     * @return the nomenstatus
     */
    public String getNomenstatus() {
        return nomenstatus;
    }

    /**
     * @param nomenstatus the nomenstatus to set
     */
    public void setNomenstatus(String nomenstatus) {
        this.nomenstatus = nomenstatus;
    }

    /**
     * @return the designations
     */
    public String getDesignations() {
        return designations;
    }

    /**
     * @param designations the designations to set
     */
    public void setDesignations(String designations) {
        this.designations = designations;
    }

    /**
     * @return the modificationdate
     */
    public int getModificationdate() {
        return modificationdate;
    }

    /**
     * @param modificationdate the modificationdate to set
     */
    public void setModificationdate(int modificationdate) {
        this.modificationdate = modificationdate;
    }

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
}
