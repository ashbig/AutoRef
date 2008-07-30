/*
 * SamplepropertyTO.java
 *
 * Created on December 10, 2007, 12:50 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package transfer;

import java.io.Serializable;

/**
 *
 * @author dzuo
 */
public class SamplepropertyTO  implements Serializable{
    private int sampleid;
    private String type;
    private String value;
    private boolean isnew;
    
    /** Creates a new instance of SamplepropertyTO */
    public SamplepropertyTO() {
        setIsnew(false);
    }

    public SamplepropertyTO(int sampleid, String type, String value) {
        this.setSampleid(sampleid);
        this.setType(type);
        this.setValue(value);
        setIsnew(false);
    }
    
    public int getSampleid() {
        return sampleid;
    }

    public void setSampleid(int sampleid) {
        this.sampleid = sampleid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    public String getCulture() {
        if(ResultTO.TYPE_CULTURE.equals(getType()))
            return getValue();
        return null;
    }
    
    public String getDna() {
        if(ResultTO.TYPE_DNA.equals(getType()))
            return getValue();
        return null;
    }
    
    public boolean getIsculture() {
        if(ResultTO.TYPE_CULTURE.equals(getType()))
            return true;
        return false;
    }
    
    public boolean getIsdna() {
        if(ResultTO.TYPE_DNA.equals(getType()))
            return true;
        return false;
    }

    public boolean isIsnew() {
        return isnew;
    }

    public void setIsnew(boolean isnew) {
        this.isnew = isnew;
    }
}
