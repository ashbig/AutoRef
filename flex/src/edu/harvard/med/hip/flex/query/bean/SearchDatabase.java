/*
 * SearchDatabase.java
 *
 * Created on February 10, 2004, 11:15 AM
 */

package edu.harvard.med.hip.flex.query.bean;

import edu.harvard.med.hip.flex.util.FlexSeqAnalyzer;

/**
 *
 * @author  DZuo
 */
public class SearchDatabase {
    public static final String HUMAN = FlexSeqAnalyzer.HUMANDB;
    public static final String HUMAN_FINISHED = "human_finished";
    
    private String name;
    private String value;
    
    public void setName(String name) {this.name=name;}
    public void setValue(String value) {this.value = value;}
    
    public String getName() {return name;}
    public String getValue() {return value;}
    
    /** Creates a new instance of SearchDatabase */
    public SearchDatabase() {
    }
    
    public SearchDatabase(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
