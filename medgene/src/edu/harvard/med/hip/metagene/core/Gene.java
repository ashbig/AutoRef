/*
 * Gene.java
 *
 * Created on January 18, 2002, 2:27 PM
 */

package edu.harvard.med.hip.metagene.core;

/**
 *
 * @author  dzuo
 * @version 
 */
public class Gene {
    private String name;
    private String symbol;
    private String searchType;
    private Vector nicknames;
    private Vector information;
    
    /** Creates new Gene */
    public Gene(String name, String symbol, String searchType, Vector nicknames, Vector information) {
        this.name = name;
        this.symbol = symbol;
        this.searchType = searchType;
        this.nicknames = nicknames;
        this.information = information;
    }
}
