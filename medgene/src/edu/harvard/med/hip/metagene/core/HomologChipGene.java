/*
 * HomologChipGene.java
 *
 * Created on June 27, 2002, 4:01 PM
 */

package edu.harvard.med.hip.metagene.core;

/**
 *
 * @author  hweng
 */
public class HomologChipGene extends ChipGene {
    
    String homolog_id;
    
    /** Creates a new instance of HomologChipGene  */
    public HomologChipGene(String gene_symbol, int locus_id, double score, String homolog_id){      
        super(gene_symbol, locus_id, score);
        this.homolog_id = homolog_id;
    }
          
    public String getHomolog_id(){
        return homolog_id;
    }
        
}