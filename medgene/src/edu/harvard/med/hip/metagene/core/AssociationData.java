/*
 * AssociationData.java
 *
 * Created on February 28, 2002, 4:57 PM
 */

package edu.harvard.med.hip.metagene.core;

/**
 *
 * @author  dzuo
 * @version 
 */
public class AssociationData {
    private int singlehitDisease;
    private int singlehitGene;
    private int doublehit;
    private int doubleNegative;
    private String date;
    
    /** Creates new AssociationData */
    public AssociationData(int singlehitDisease, int singlehitGene, int doublehit, int doubleNegative, String date) {
        this.singlehitDisease = singlehitDisease;
        this.singlehitGene = singlehitGene;
        this.doublehit = doublehit;
        this.doubleNegative = doubleNegative;
        this.date = date;
    }

    public int getDoublehit() {
        return doublehit;
    }
}
