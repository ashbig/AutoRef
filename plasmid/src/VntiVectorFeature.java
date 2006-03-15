/*
 * VntiVectorFeature.java
 *
 * Created on March 14, 2006, 3:18 PM
 */

/**
 *
 * @author  DZuo
 */
public class VntiVectorFeature {
    public static final String PROMOTER = "promoter";
    public static final String MISC_FEATURE = "misc_feature";
    public static final String CDS = "CDS";
    public static final String REP_ORIGIN = "rep_origin";
    public static final String MISC_MARKER = "misc_marker";
    
    protected String name;
    protected int start;
    protected int end;
    protected String label;
    protected boolean isComplement;
    
    /** Creates a new instance of VntiVectorFeature */
    public VntiVectorFeature() {
        isComplement = false;
    }
    
    public void setName(String s) {this.name = s;}
    public void setStart(int i) {this.start = i;}
    public void setEnd(int i) {this.end = i;}
    public void setLabel(String s) {this.label = s;}
    public void setIsComplement(boolean b) {this.isComplement = b;}
    
    public String getName() {return name;}
    public int getStart() {return start;}
    public int getEnd() {return end;}
    public String getLabel() {return label;}
    public boolean getIsComplement() {return isComplement;}
    
    public static String getName(int i) {
        if(i == 0)
            return MISC_FEATURE;
        if(i == 1)
            return PROMOTER;
        if(i == 2)
            return CDS;
        if(i == 3)
            return REP_ORIGIN;
        if(i == 4)
            return MISC_MARKER;
        return null;
    }
    
    public String getKey() {
        if(name.equals(PROMOTER))
            return "29";
        if(name.equals(MISC_FEATURE))
            return "21";
        if(name.equals(CDS))
            return "4";
        if(name.equals(REP_ORIGIN))
            return "33";
        if(name.equals(MISC_MARKER))
            return "22";
        return null;
    }
}
