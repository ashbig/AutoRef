/*
 * CloneStorage.java
 *
 * Created on June 11, 2003, 2:59 PM
 */

package edu.harvard.med.hip.flex.core;

/**
 *
 * @author  dzuo
 */
public class CloneStorage {
    protected int storageid;
    protected int sampleid;
    protected String storageType;
    protected String storageForm;
    protected int cloneid;
    protected int position;
    protected String label;
    protected int containerid;
    
    public int getStorageid() {return storageid;}
    public int getSampleid() {return sampleid;}
    public String getStorageType() {return storageType;}
    public String getStorageForm() {return storageForm;}
    public int getCloneid() {return cloneid;}
    public int getPosition() {return position;}
    public String getLabel() {return label;}
    public int getContainerid() {return containerid;}
    
    public void setPosition(int i) {this.position = i;}
    public void setLabel(String s) {this.label = s;}
    public void setContainerid(int i) {this.containerid = i;}
    
    /** Creates a new instance of CloneStorage */
    public CloneStorage() {
    }
    
    public CloneStorage(CloneStorage c) {
        if (c != null) {
            this.storageid = c.getStorageid();
            this.sampleid = c.getSampleid();
            this.storageType = c.getStorageType();
            this.storageForm = c.getStorageForm();
            this.cloneid = c.getCloneid();
            this.position = c.getPosition();
            this.label = c.getLabel();
            this.containerid = c.getContainerid();
        }
    }

    public CloneStorage(int storageid, int sampleid, String storageType, String storageForm, int cloneid) {
        this.storageid = storageid;
        this.sampleid = sampleid;
        this.storageType = storageType;
        this.storageForm = storageForm;
        this.cloneid = cloneid;
    }
    
    public CloneStorage(int sampleid, String storageType, String storageForm, int cloneid) {
        this.sampleid = sampleid;
        this.storageType = storageType;
        this.storageForm = storageForm;
        this.cloneid = cloneid;
    }
}
