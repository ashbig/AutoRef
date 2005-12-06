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
    public static final String TUBE_96 = "Tube 96 Well";
    public static final String COSTAR_FLT = "Costar flt bttm/MP16-24";
    public static final String COSTAR_RD = "Costar rd bttm/MP16-24";
    public static final String GREINER = "GreinerVbttmPPonMP16";
    public static final String PCR_ON_MP16 = "PCR on MP16 landscape";
    public static final String RESERVOIR_MP16 = "Reservoir on MP16";
    public static final String RESERVOIR_PYR = "Reservoir Pyr bttm";
    public static final String RK_RIPLATE_DW = "RK riplate dw/MP16-24";
    public static final String MICRONIC96TUBEMP16 = "Micronic96TubeMP16";
    
    public static final String EMPTY = "EMPTY";
    public static final String FILLED = "FILLED";
    
    public static final String UNAVAILABLE = "UNAVAILABLE";
    public static final String WORKBENCH = "WORKBENCH";
    
    private int containerid;
    private String type;
    private String label;
    private String oricontainerid;
    private String location;
    private int capacity;
    private String status;
    
    private List samples;
    
    /** Creates a new instance of Container */
    public Container() {
    }
    
    public Container(int containerid, String type, String label, String oricontainerid, String location, int capacity, String status) {
        this.containerid = containerid;
        this.type = type;
        this.label = label;
        this.oricontainerid = oricontainerid;
        this.location = location;
        this.capacity = capacity;
        this.status = status;
        samples = new ArrayList();
    }
    
    public int getContainerid() {return containerid;}
    public String getType() {return type;}
    public String getLabel() {return label;}
    public String getOricontainerid() {return oricontainerid;}
    public String getLocation() {return location;}
    public int getCapacity() {return capacity;}
    public String getStatus() {return status;}
    
    public void setContainerid(int id) {this.containerid = id;}
    public void setType(String s) {this.type = s;}
    public void setLabel(String s) {this.label = s;}
    public void setOricontainerid(String s) {this.oricontainerid = s;}
    public void setLocation(String s) {this.location = s;}
    public void setCapacity(int i) {this.capacity = i;}
    public void setStatus(String s) {this.status = s;}
    
    public List getSamples() {return samples;}
    public void setSamples(List l) {this.samples = l;}
    
    public static int getCapacity(String type) {
        if(COSTAR_FLT.equals(type))
            return 96;
        if(COSTAR_RD.equals(type))
            return 96;
        if(GREINER.equals(type))
            return 96;
        if(PCR_ON_MP16.equals(type))
            return 96;
        if(RESERVOIR_MP16.equals(type))
            return 1;
        if(RESERVOIR_PYR.equals(type))
            return 96;
        if(RK_RIPLATE_DW.equals(type))
            return 96;
        if(TUBE.equals(type))
            return 1;
        if(MICRONIC96TUBEMP16.equals(type))
            return 96;
        return 0;
    }
    
    public int getSize() {
        if(samples == null)
            return 0;
        return samples.size();
    }
    
    public void initiateSamples() {
        this.samples = new ArrayList(Container.getCapacity(this.getType()));
    }
    
    public void addSample(Sample s) {
        if(samples == null)
            samples = new ArrayList();
        
        samples.add(s);
    }
    
    public Sample getSample(int position) {
        if(samples == null)
            return null;
        
        Sample s = null;
        for(int i=0; i<samples.size(); i++) {
            s = (Sample)samples.get(i);
            if(s.getPosition() == position)
                break;
        }
        
        return s;
    }
    
    public void setSample(Sample s) {
        if(getSample(s.getPosition()) == null)
            addSample(s);
        else
            samples.set(s.getPosition()-1, s);
    }
}
