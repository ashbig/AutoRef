/*
 * CloneSample.java
 *
 * Created on April 24, 2003, 5:17 PM
 */

package edu.harvard.med.hip.flex.core;

import edu.harvard.med.hip.flex.database.FlexDatabaseException;

/**
 *
 * @author  dzuo
 */
public class CloneSample extends Sample {
    public static final String MASTER = "Master";
    public static final String EXPRESSION = "Expression";
    public static final String PASS = "PASS";
    public static final String FAIL = "FAIL";
    public static final String NOT_DONE = "NOT DONE";
    
    protected String clonename;
    protected String clonetype;
    protected int mastercloneid;
    protected int strategyid;
    protected String clonestatus;
    protected String comments;
    
    public String getClonename() {return clonename;}
    public String getClonetype() {return clonetype;}
    public int getMastercloneid() {return mastercloneid;}
    public int getSequenceid() {return sequenceid;}
    public int getConstructid() {return constructid;}
    public int getStrategyid() {return strategyid;}
    public String getClonestatus() {return clonestatus;}
    public String getComments() {return comments;}
    
    public void setClonename(String clonename) {this.clonename=clonename;}
    public void setClonetype(String s) {this.clonetype=s;}
    public void setMastercloneid(int i) {this.mastercloneid=i;}
    public void setSequenceid(int i) {this.sequenceid = i;}
    public void setConstructid(int i) {this.constructid = i;}
    public void setStrategyid(int i) {this.strategyid=i;}
    public void setClonestatus(String s) {this.clonestatus = s;}
    public void setComments(String s) {this.comments = s;}
    
    public CloneSample(int id, int position, int containerid, int cloneid) {
        super(id, position, containerid);
        this.cloneid = cloneid;
    }
   
    public CloneSample(String type, int position, int containerid, int constructid, int oligoid, String status, int cloneid) throws FlexDatabaseException {
        super(type,position,containerid,constructid,oligoid,status);
        this.cloneid = cloneid;
    }
    
    public CloneSample(int sampleid, String sampletype, int containerposition, int id, int constructid, int oligoid, String status, int cloneid) {
        super(sampleid,sampletype,containerposition,id,constructid,oligoid,status);
        this.cloneid = cloneid;
    }
}
