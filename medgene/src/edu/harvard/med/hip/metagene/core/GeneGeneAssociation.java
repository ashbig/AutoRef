/*
 * GeneGeneAssociation.java
 *
 * Created on April 4, 2002, 11:30 AM
 */

package edu.harvard.med.hip.metagene.core;
import java.util.*;

/**
 *
 * @author  hweng
 */

public class GeneGeneAssociation {

    private Gene source_gene;
    private Gene target_gene;
    private StatAnalysis stat_analysis;
    private AssociationData asso_data;              //private int k;
    
    
    /** Creates a new instance of GeneGeneAssociation */
    public GeneGeneAssociation(Gene source_gene, Gene target_gene, 
                               StatAnalysis stat_analysis, AssociationData asso_data) {
        this.source_gene = source_gene;
        this.target_gene = target_gene;
        this.stat_analysis = stat_analysis;
        this.asso_data = asso_data;
        
        //this.k = k;
    }
    
    public Gene getSource_gene(){
        return source_gene;
    }
    
    public Gene getTarget_gene(){
        return target_gene;
    }
    
    public StatAnalysis getStat_analysis(){
        return stat_analysis;
    }
    
    public AssociationData getAsso_data(){
        return asso_data;
    }
 
    //public int getK(){return k;}
    
}
