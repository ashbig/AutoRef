/*
 * ExpressionCloneSample.java
 *
 * Created on August 7, 2003, 3:48 PM
 */

package edu.harvard.med.hip.flex.core;

import java.util.*;
import edu.harvard.med.hip.flex.database.FlexDatabaseException;

/**
 *
 * @author  DZuo
 */
public class ExpressionCloneSample extends CloneSample {
    protected String author;
    protected String startdate;
    protected String pcrresult;
    protected String floresult;
    protected String proteinresult;
    protected String restrictionresult;
    protected String colonyresult;
    protected List resulttypelist;

    public void setAuthor(String s) {this.author=s;}
    public void setStartdate(String s) {this.startdate=s;}
    public void setPcrresult(String s) {this.pcrresult=s;}
    public void setFloresult(String s) {this.floresult=s;}
    public void setProteinresult(String s) {this.proteinresult=s;}
    public void setRestrictionresult(String s) {this.restrictionresult=s;}
    public void setColonyresult(String s) {this.colonyresult=s;}
    public void setResulttypelist(List l) {this.resulttypelist=l;}
    
    public String getAuthor() {return author;}
    public String getStartdate() {return startdate;}
    public String getPcrresult() {return pcrresult;}
    public String getFloresult() {return floresult;}
    public String getProteinresult() {return proteinresult;}
    public String getRestrictionresult() {return restrictionresult;}
    public String getColonyresult() {return colonyresult;}
    public List getResulttype() {return resulttypelist;}
    
    public ExpressionCloneSample(String type, int position, int containerid, int constructid, int oligoid, String status, int cloneid) throws FlexDatabaseException {
        super(type,position,containerid,constructid,oligoid,status,cloneid);
    }    
        
    public ExpressionCloneSample(int sampleid, String sampletype, int containerposition, int id, int constructid, int oligoid, String status, int cloneid) {
        super(sampleid,sampletype,containerposition,id,constructid,oligoid,status,cloneid);
    }
}
