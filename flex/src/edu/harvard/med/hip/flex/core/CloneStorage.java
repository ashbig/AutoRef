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
    
    public int getStorageid() {return storageid;}
    public int getSampleid() {return sampleid;}
    public String getStorageType() {return storageType;}
    public String getStorageForm() {return storageForm;}
    public int getCloneid() {return cloneid;}
    
    /** Creates a new instance of CloneStorage */
    public CloneStorage() {
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
