/*
 * ChipGene.java
 *
 * Created on May 2, 2002, 4:37 PM
 */

package edu.harvard.med.hip.metagene.core;

/**
 *
 * @author  hweng
 */
public class ChipGene {
      
    int gene_index_id;
    String gene_symbol;
    int locus_id;   
    double score;
    String gene_term;  // unofficial gene name
    
    /** Creates a new instance of ChipGene */
    public ChipGene(int gene_index_id, double score) {
        this.gene_index_id = gene_index_id;
        this.score = score;        
    }
    
    public ChipGene(String gene_symbol, int locus_id, double score){
        this.gene_symbol = gene_symbol;
        this.locus_id = locus_id;
        this.score = score;        
    }
    
    
    /**
     * get methods
     */
    public int getGene_index_id(){
        return gene_index_id;
    }
    public String getGene_symbol(){
        return gene_symbol;
    }
    public int getLocus_id(){
        return locus_id;
    }
    public double getScore(){
        return score;
    }
    public String getGene_term(){
        return gene_term;
    }
    /**
     * set methods
     */
    public void setScore(double s){
        score = s;
    }

}
