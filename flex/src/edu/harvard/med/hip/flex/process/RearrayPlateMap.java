/*
 * RearrayPlateMap.java
 *
 * The class stores all the information for each rearrayed sample.
 *
 * Created on May 15, 2003, 9:48 AM
 */

package edu.harvard.med.hip.flex.process;

import java.util.Comparator;

/**
 *
 * @author  dzuo
 */
public class RearrayPlateMap {
    protected RearrayInputSample inputSample = null;
    protected int sourcePlateid = -1;
    protected String sourcePlateLabel = null;
    protected int sourceWell = -1;
    protected int destPlateid = -1;
    protected String destPlateLabel = null;
    protected int destWell = -1;
    
    protected int sequenceid = -1;
    protected int cdsstart = -1;
    protected int cdsstop = -1;
    protected int oligoid5p = -1;
    protected int oligoid3p = -1;
    protected int cdslength =-1;
    protected int oligoid3pReversed = -1;
    protected int constructid = -1;
    protected int sampleid = -1; 
    protected String sampletype = null;
    
    protected String constructtype = null;
    
    /** Creates a new instance of PlateMap */
    public RearrayPlateMap() {
    }
    
    public RearrayPlateMap(int sampleid, int constructid, String constructtype, int oligoid5p,
    int oligoid3p, int sequenceid, int cdsstart, int cdsstop, int cdslength, int containerid, String label) {
        this.sampleid=sampleid;
        this.constructid = constructid;
        this.constructtype = constructtype;
        this.oligoid5p = oligoid5p;
        this.oligoid3p = oligoid3p;
        this.sequenceid = sequenceid;
        this.cdsstart = cdsstart;
        this.cdsstop = cdsstop;
        this.cdslength = cdslength;
        this.sourcePlateid=containerid;
        this.sourcePlateLabel = label;
    }
    
    public void setSourceWell(int well) {this.sourceWell = well;}
    public void setRearrayInputSample(RearrayInputSample s) {this.inputSample = s;}
    
    public void setSequenceid(int id) {this.sequenceid = id;}
    public void setCdsstart(int cdsstart) {this.cdsstart = cdsstart;}
    public void setCdsstop(int cdsstop) {this.cdsstop = cdsstop;}
    public void setOligo5p(int id) {this.oligoid5p = id;}
    public void setOligo3p(int id) {this.oligoid3p = id;}
    public void setOligo3pReversed(int id) {this.oligoid3pReversed = id;}
    public void setDestPlateLabel(String label) {this.destPlateLabel = label;}
    public void setDestWell(int id) {this.destWell = id;}
    public void setSampletype(String sampletype) {this.sampletype = sampletype;}
    
    public int getSourcePlateid() {return sourcePlateid;}
    public String getSourcePlateLabel() {return sourcePlateLabel;}
    public int getSourceWell() {return sourceWell;}
    public int getCdslength() {return cdslength;}
    public String getConstructtype() {return constructtype;}
    public String getDestPlateLabel() {return destPlateLabel;}
    public int getSampleid() {return sampleid;}
    public int getDestWell() {return destWell;}
    public int getSequenceid() {return sequenceid;}
    public int getCdsstart() {return cdsstart;}
    public int getCdsstop() {return cdsstop;}
    public int getOligoid5p() {return oligoid5p;}
    public int getOligoid3p() {return oligoid3p;}
    public int getOligoid3pReversed() {return oligoid3pReversed;}
    public int getConstructid() {return constructid;}
    public String getSampletype() {return sampletype;}
    public RearrayInputSample getRearrayInputSample() {return inputSample;}
}
