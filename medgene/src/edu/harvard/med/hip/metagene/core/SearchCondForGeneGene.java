/*
 * SearchCondForGeneGene.java
 *
 * Created on April 8, 2002, 6:41 PM
 */

package edu.harvard.med.hip.metagene.core;

/**
 *
 * @author  hweng
 */
public class SearchCondForGeneGene {
    
    private Gene source;
    private int number;
    private String stat_type;
    
    public SearchCondForGeneGene(Gene g, int n, String type){
        source = g;
        number = n;
        stat_type = type;
    }
    
    public Gene getSource(){
        return source;
    }
    
    public int getNumber(){
        return number;
    }
    
    public String getStat_type(){
        return stat_type;
    }
    
}
