/*
 * GeneralAssociation.java
 *
 * Created on July 10, 2002, 4:12 PM
 */

package edu.harvard.med.hip.metagene.core;
import java.util.Vector;
/**
 *
 * @author  hweng
 */
public class GeneralAssociation {
    
    String gene_index;
    Vector associations;
    
    public GeneralAssociation(String gene_index, Vector associations){
        this.gene_index = gene_index;
        this.associations = associations;
    }
        
    
    public String getGene_index(){
        return gene_index;
    }
    public Vector getAssociations(){
        return associations;
    }
    public void setGene_index(String gene_index){
        this.gene_index = gene_index;
    }
    public void setAssociations(Vector associations){
        this.associations = associations;
    }
    
}
