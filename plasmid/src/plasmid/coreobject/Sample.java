/*
 * Sample.java
 *
 * Created on May 23, 2005, 1:27 PM
 */

package plasmid.coreobject;

import plasmid.util.*;

/**
 *
 * @author  DZuo
 */
public class Sample {
    public static final String GOOD = "G";
    public static final String BAD = "B";
    public static final String ARCHIVE_GLYCEROL = "Archive Glycerol";
    public static final String WORKING_GLYCEROL = "Working Glycerol";
    public static final String EMPTY = "Empty";
    public static final String CULTURE = "CULTURE";
    public static final String GLYCEROL = "GLYCEROL";
    public static final String AGAR = "AGAR";
    public static final String TRANSFORMATION = "TRANSFORMATION";
    
    private int sampleid;
    private String type;
    private String status;
    private int cloneid;
    private int position;
    private String positionx;
    private String positiony;
    private int containerid;
    private String containerlabel;
    private String result;
    private String containerType;
    
    /** Creates a new instance of Sample */
    public Sample() {
    }
    
    public Sample(int sampleid, String type, String status, int cloneid, int position, String x, String y, int containerid, String containerlabel) {
        this.sampleid = sampleid;
        this.type = type;
        this.status = status;
        this.cloneid = cloneid;
        this.position = position;
        this.positionx = x;
        this.positiony = y;
        this.containerid = containerid;
        this.containerlabel = containerlabel;
    }
    
    public int getSampleid() {return sampleid;}
    public String getType() {return type;}
    public String getStatus() {return status;}
    public int getCloneid() {return cloneid;}
    public int getPosition() {return position;}
    public String getPositionx() {return positionx;}
    public String getPositiony() {return positiony; }
    public int getContainerid() {return containerid;}
    public String getContainerlabel() {return containerlabel;}
    public String getResult() {return result;}
    public String getContainerType() {return containerType;}
    public String getWell() {return positionx+positiony;}
    
    public void setSampleid(int id) {this.sampleid = id;}
    public void setType(String s) {this.type = s;}
    public void setStatus(String s) {this.status = s;}
    public void setCloneid(int id) {this.cloneid = id;}
    public void setPosition(int s) {this.position = s;}
    public void setPositionx(String s) {this.positionx = s;}
    public void setPositiony(String s) {this.positiony = s;}
    public void setContainerid(int i) {this.containerid = i;}
    public void setContainerlabel(String s) {this.containerlabel = s;}
    public void setResult(String s) {this.result = s;}
    public void setContainerType(String s) {this.containerType = s;}
    
    public void setPositions(int p) {
        String st = PlatePositionConvertor.convertWellFromInttoA8_12(p);
        String positionX = st.substring(0, 1);
        String positionY = st.substring(1);
        int y = Integer.parseInt(positionY);
        java.text.NumberFormat fmt = java.text.NumberFormat.getInstance();
        fmt.setMaximumIntegerDigits(2);
        fmt.setMinimumIntegerDigits(2);
        fmt.setGroupingUsed(false);
        
        setPosition(p);
        setPositionx(positionX);
        setPositiony(fmt.format(y));
    }
    
    public static String getSampleType(String processname) {
        if(Process.CULTURE.equals(processname))
            return Sample.CULTURE;
        if(Process.GENERATE_GLYCEROL.equals(processname))
            return Sample.GLYCEROL;
        if(Process.PLATING.equals(processname))
            return Sample.AGAR;
        if(Process.TRANSFORMATION.equals(processname))
            return Sample.TRANSFORMATION;
        return null;
    }    
    
    public static boolean isResultPass(String sampletype, String result) {
        if(Sample.CULTURE.equals(sampletype)) {
            if(Result.GROW.equals(result) || Result.WEAKGROW.equals(result))
                return true;
            else
                return false;
        }
        return true;
    }
}
