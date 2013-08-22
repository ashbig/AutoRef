/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plasmid.coreobject;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Lab User
 */
public class Geneinfo implements Serializable {
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
    private String species;
    private String term;
    private List<Gene2Refseq> seqs;
    private int cloneCount;

    public Geneinfo() {}
    
    public Geneinfo(int geneid, String symbol, String description, String type, String locustag, String synonyms,
            String chromosome, String maplocation, String symbolnom, String namenom, String nomstatus, int taxid) {
        this.geneid = geneid;
        this.symbol = symbol;
        this.description = description;
        this.type = type;
        this.locustag = locustag;
        this.synonyms = synonyms;
        this.chromosome = chromosome;
        this.maplocation = maplocation;
        this.symbolnom = symbolnom;
        this.namenom = namenom;
        this.nomstatus = nomstatus;
        this.taxid = taxid;
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

    /**
     * @return the term
     */
    public String getTerm() {
        return term;
    }

    /**
     * @param term the term to set
     */
    public void setTerm(String term) {
        this.term = term;
    }

    /**
     * @return the seqs
     */
    public List<Gene2Refseq> getSeqs() {
        return seqs;
    }

    /**
     * @param seqs the seqs to set
     */
    public void setSeqs(List<Gene2Refseq> seqs) {
        this.seqs = seqs;
    }

    /**
     * @return the species
     */
    public String getSpecies() {
        return species;
    }

    /**
     * @param species the species to set
     */
    public void setSpecies(String species) {
        this.species = species;
    }

    /**
     * @return the cloneCount
     */
    public int getCloneCount() {
        return cloneCount;
    }

    /**
     * @param cloneCount the cloneCount to set
     */
    public void setCloneCount(int cloneCount) {
        this.cloneCount = cloneCount;
    }
}
