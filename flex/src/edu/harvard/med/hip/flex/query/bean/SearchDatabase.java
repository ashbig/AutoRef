/*
 * SearchDatabase.java
 *
 * Created on February 10, 2004, 11:15 AM
 */

package edu.harvard.med.hip.flex.query.bean;

import edu.harvard.med.hip.flex.export.FastaFileGenerator;

/**
 *
 * @author  DZuo
 */
public class SearchDatabase {    
    public static final String HUMAN = "All human genes";
    public static final String YEASTDB="Yeast genes";
    public static final String PSEUDOMONASDB="Pseudomonas genes";
    public static final String MGCDB="MGC Clones";
    public static final String YPDB="Yersinia pestis genes";
    public static final String BCDB="Breast cancer related genes";
    public static final String NIDDKDB="NIDDK mouse genes";
    public static final String CLONTECHDB="Clontech requested genes";
    public static final String RZPDWALLDB="RZPD-WALL genes";
    public static final String FTDB="Francisella tularensis";
    public static final String KINASEDB="Kinase related genes";
    public static final String VERIFIEDDB = "All sequence verified genes";
    public static final String VERIFIEDBCDB="Sequence verified breast cancer genes";
    public static final String VERIFIEDKINASEDB="Sequence verified kinase genes";
    public static final String VERIFIEDHUMANDB="Sequence verified human genes";
    public static final String ALLDB = "All genes";
    
    private String name;
    private String value;
    private String db;
    
    public void setName(String name) {this.name=name;}
    public void setValue(String value) {this.value = value;}
    public void setDb(String db) {this.db = db;}
    
    public String getName() {return name;}
    public String getValue() {return value;}
    public String getDb() {return db;}
    
    /** Creates a new instance of SearchDatabase */
    public SearchDatabase() {
    }
    
    public SearchDatabase(String name, String value) {
        this.name = name;
        this.value = value;
        this.db = db;
    }
    
    public SearchDatabase(String name, String value, String db) {
        this.name = name;
        this.value = value;
        this.db = db;
    }
    
    public static String getDbByName(String name) {
        if(SearchDatabase.HUMAN.equals(name)) {
            return FastaFileGenerator.HUMANDB;
        }
        if(SearchDatabase.ALLDB.equals(name)) {
            return FastaFileGenerator.ALLDB;
        }
        if(SearchDatabase.BCDB.equals(name)) {
            return FastaFileGenerator.BCDB;
        }
        if(SearchDatabase.CLONTECHDB.equals(name)) {
            return FastaFileGenerator.CLONTECHDB;
        }
        if(SearchDatabase.FTDB.equals(name)) {
            return FastaFileGenerator.FTDB;
        }
        if(SearchDatabase.KINASEDB.equals(name)) {
            return FastaFileGenerator.KINASEDB;
        }
        if(SearchDatabase.MGCDB.equals(name)) {
            return FastaFileGenerator.MGCDB;
        }
        if(SearchDatabase.NIDDKDB.equals(name)) {
            return FastaFileGenerator.NIDDKDB;
        }
        if(SearchDatabase.PSEUDOMONASDB.equals(name)) {
            return FastaFileGenerator.PSEUDOMONASDB;
        }
        if(SearchDatabase.RZPDWALLDB.equals(name)) {
            return FastaFileGenerator.RZPDWALLDB;
        }
        if(SearchDatabase.VERIFIEDDB.equals(name)) {
            return FastaFileGenerator.SEQVERIFIEDDB;
        }
        if(SearchDatabase.VERIFIEDBCDB.equals(name)) {
            return FastaFileGenerator.VERIFIEDBCDB;
        }
        if(SearchDatabase.VERIFIEDHUMANDB.equals(name)) {
            return FastaFileGenerator.VERIFIEDHUMANDB;
        }
        if(SearchDatabase.VERIFIEDKINASEDB.equals(name)) {
            return FastaFileGenerator.VERIFIEDKINASEDB;
        }
        if(SearchDatabase.YEASTDB.equals(name)) {
            return FastaFileGenerator.YEASTDB;
        }
        if(SearchDatabase.YPDB.equals(name)) {
            return FastaFileGenerator.YPDB;
        }
        
        return null;
    }
}
