/*
 * Association.java
 *
 * Created on January 30, 2002, 6:23 PM
 */

package edu.harvard.med.hip.metagene.core;

import java.util.*;

/**
 *
 * @author  dzuo
 * @version 
 */

// gene-diseases association

public class Association {
    private Disease disease;
    private Gene gene;
    private GeneIndex geneIndex;
    private StatAnalysis stat;
    private AssociationData data;
    
    /** Creates new Association */
    public Association() {
    }
    
    public Association(Disease disease, GeneIndex geneIndex, StatAnalysis stat, AssociationData data) {
        this.disease = disease;
        this.geneIndex = geneIndex;
        this.stat = stat;
        this.data = data;
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
    
    public AssociationData getData() {
        return data;
    }
    
    public boolean geneInCommon(Association a) {
        if(this.getGene().getHipGeneId() == a.getGene().getHipGeneId())
            return true;
        else
            return false;
    }
    
    public static Vector mergeAssociations(Vector allAssociations) {
        Vector common = new Vector();
        
        Vector a1 = (Vector)allAssociations.elementAt(0);
        for(int i=1; i<allAssociations.size(); i++) {
            Vector a2 = (Vector)allAssociations.elementAt(i);
            common = findCommon(a1, a2);
            a1 = common;
        }
        
        return common;
    }
    
    public static Vector findCommon(Vector v1, Vector v2) {
        Vector v = new Vector();
        
        for(int i=0; i<v1.size(); i++) {
            Association a = (Association)v1.elementAt(i);
            if(found(a, v2)) {
                v.addElement(a);
            }
        }
        
        return v;
    }
    
    public static boolean found(Association a, Vector v) {
        for(int i=0; i<v.size(); i++) {
            Association b = (Association)v.elementAt(i);
            
            if(a.geneInCommon(b)) {
                return true;
            }
        }
        
        return false;
    }  
}
