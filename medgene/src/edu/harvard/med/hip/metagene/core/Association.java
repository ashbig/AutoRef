/*
 * Association.java
 *
 * Created on January 30, 2002, 6:23 PM
 */

package edu.harvard.med.hip.metagene.core;

/**
 *
 * @author  dzuo
 * @version 
 */
public class Association {
    private Disease disease;
    private Gene gene;
    private GeneIndex geneIndex;
    private StatAnalysis stat;

    /** Creates new Association */
    public Association() {
    }
    
    public Association(Disease disease, GeneIndex geneIndex, StatAnalysis stat) {
        this.disease = disease;
        this.geneIndex = geneIndex;
        this.stat = stat;
    }
    
    public Association(Disease disease, Gene gene, StatAnalysis stat) {
        this.disease = disease;
        this.gene = gene;
        this.stat = stat;
    }

    public Disease getDisease() {
        return disease;
    }
    
    public Gene getGene() {
        return gene;
    }

    public void setGene(Gene gene) {
        this.gene = gene;
    }
    
    public GeneIndex getGeneIndex() {
        return geneIndex;
    }
    
    public StatAnalysis getStat() {
        return stat;
    }
}
