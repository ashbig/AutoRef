/*
 * Container.java
 *
 * Created on May 23, 2005, 1:22 PM
 */

package plasmid.coreobject;

import java.util.*;

/**
 *
 * @author  DZuo
 */
public class Container {
    public static final String PLATE_96 = "96 Well Plate";
    public static final String TUBE = "Tube";
    
    private int containerid;
    private String type;
    private String label;
    private String oricontainerid;
    private String location;
    
    private List samples;
    
    /** Creates a new instance of Container */
    public Container() {
    }
    
    public Container(int containerid, String type, String label, String oricontainerid, String location) {
        this.containerid = containerid;
        this.type = type;
        this.label = label;
        this.oricontainerid = oricontainerid;
        this.location = location;
    }
    
    public int getContainerid() {return containerid;}
    public String getType() {return type;}
    public String getLabel() {return label;}
    public String getOricontainerid() {return oricontainerid;}
    public String getLocation() {return location;}
    
    public void setContainerid(int id) {this.containerid = id;}
    public void setType(String s) {this.type = s;}
    public void setLabel(String s) {this.label = s;}
    public void setOricontainerid(String s) {this.oricontainerid = s;}
    public void setLocation(String s) {this.location = s;}
    
    public List getSamples() {return samples;}
    public void setSamples(List l) {this.samples = l;}
    
    public int getSize() {
        if(this.PLATE_96.equals(type))
            return 96;
        if(this.TUBE.equals(type))
            return 1;
        return 0;
    }
    
    public void initiateSamples() {
        this.samples = new ArrayList(this.getSize());
    }
    
    public void addSample(Sample s) {
        if(samples == null) 
            samples = new ArrayList(this.getSize());
        
        samples.add(s.getPosition()-1, s);
    }
    
    public Sample getSample(int position) {
        Sample s = null;
        try {
            s = (Sample)samples.get(position-1);
        } catch (Exception ex) {}
        
        return s;
    }
    
    public void setSample(Sample s) {
        if(getSample(s.getPosition()) == null)
            addSample(s);
        else
            samples.set(s.getPosition()-1, s);
    }
}
