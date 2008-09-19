/*
 * ContainerheaderTO.java
 *
 * Created on April 26, 2007, 3:24 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package transfer;

import core.Well;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author dzuo
 */
public class ContainerheaderTO extends ProcessobjectTO implements Serializable {
    private static final String STATUS_GOOD = "Full";
    private static final String STATUS_EMPTY = "Empty";
    
    private static final String LABWARE_SLIDE = "Slide";
    private static final String LABWARE_PLATE96 = "96 Well Plate";
    private static final String LABWARE_PLATE384 = "384 Well Plate";
    private static final String FORMAT_REGULAR = "Regular";
    private static final String LOCATION_FREEZER = "Freezer";
    
    private int containerid;
    private String barcode;
    private String type;
    private String format;
    private String status;
    private String location;
    private String labware;
    private int threadid;
    private String category;
    
    private int numofspots;
    private int numofrow;
    private int numofcol;
    private int numofrowinblock;
    private int numofcolinblock;
    
    private List<SampleTO> samples;
    private ContainertypeTO containertype;
    private Collection<ContainerpropertyTO> properties;
    private Collection<SlideTO> slides;
    
    /** Creates a new instance of ContainerheaderTO */
    public ContainerheaderTO() {
        this.setSamples(new ArrayList<SampleTO>());
        this.setSlides(new ArrayList<SlideTO>());
        this.properties = new ArrayList<ContainerpropertyTO>();
    }
    
    public ContainerheaderTO(int id, String barcode, String type, String format, String status, String location, String labware, int threadid, String category) {
        this.setContainerid(id);
        this.setBarcode(barcode);
        this.setType(type);
        this.setFormat(format);
        this.setStatus(status);
        this.setLocation(location);
        this.setLabware(labware);
        this.setThreadid(threadid);
        this.setCategory(category);
        this.setSamples(new ArrayList<SampleTO>());
        this.setSlides(new ArrayList<SlideTO>());
        this.properties = new ArrayList<ContainerpropertyTO>();
    }
    
    public ContainerheaderTO(int id, String barcode, ContainertypeTO type, String format, String status, String location, String labware, int threadid, String category) {
        this.setContainerid(id);
        this.setBarcode(barcode);
        this.setType(type.getType());
        this.setContainertype(type);
        this.setFormat(format);
        this.setStatus(status);
        this.setLocation(location);
        this.setLabware(labware);
        this.setThreadid(threadid);
        this.setCategory(category);
        this.setSamples(new ArrayList<SampleTO>());
        this.setSlides(new ArrayList<SlideTO>());
        this.properties = new ArrayList<ContainerpropertyTO>();
    }
    
    /**
     * public void populateCells(String celltype) {
     * if(celltype == null)
     * celltype = ContainercellTO.getTYPE_CONTROL();
     *
     * for(int i=0; i<getCapacity(); i++) {
     * Well w = Well.convertPosToWell(i+1, getColnum());
     * ContainercellTO cell = new ContainercellTO(i+1, w.getX(), w.getY(), celltype);
     * getCells().add(cell);
     * }
     * }
     */
    public int getCapacity() {
        return getContainertype().getNumofrow()*getContainertype().getNumofcol();
    }
    
    public static String getLABWARE_PLATE384() {
        return LABWARE_PLATE384;
    }
    
    public int getRownum() {
        return getContainertype().getNumofrow();
    }
    
    public void initSamples() {
        setSamples(new ArrayList<SampleTO>());
        for(int i=0; i<getCapacity(); i++) {
            int pos = i+1;
            SampleTO sample = new SampleTO(0, SampleTO.getNAME_EMPTY(), null, 0, 0, null, SampleTO.getTYPE_EMPTY(), SampleTO.getFORM_EMPTY(), SampleTO.getSTATUS_GOOD(), 0, pos);
            Well well = Well.convertVPosToWell(pos, getContainertype().getNumofrow());
            ContainercellTO cell = new ContainercellTO(pos, well.getX(), well.getY(), ContainercellTO.TYPE_EMPTY);
            sample.setCell(cell);
            getSamples().add(sample);
        }
    }
    
    public SampleTO getSample(int pos) {
        for(SampleTO sample:samples) {
            if(sample.getPosition()==pos)
                return sample;
        }
        return null;
    }
    
    public void addSample(SampleTO s) throws TransferException {
        if(getSamples() == null)
            this.setSamples(new ArrayList<SampleTO>());
        
        ((ArrayList)getSamples()).add(s);
    }
    
    public void setSample(SampleTO s, int pos) throws TransferException {
        if(getSamples() == null) {
            throw new TransferException("Sample list is not initialized.");
        }
        
        try {
            ((ArrayList)getSamples()).set(pos-1, s);
        } catch (IndexOutOfBoundsException ex) {
            throw new TransferException("Invalid position: "+pos);
        }
    }
    
    public static String getSTATUS_GOOD() {
        return STATUS_GOOD;
    }
    
    public static String getLABWARE_SLIDE() {
        return LABWARE_SLIDE;
    }
    
    public static String getLABWARE_PLATE96() {
        return LABWARE_PLATE96;
    }
    
    public int getContainerid() {
        return containerid;
    }
    
    public void setContainerid(int containerid) {
        this.containerid = containerid;
        this.objectid = containerid;
    }
    
    public String getBarcode() {
        return barcode;
    }
    
    public void setBarcode(String barcode) {
        this.barcode = barcode;
        this.objectname = barcode;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getFormat() {
        return format;
    }
    
    public void setFormat(String format) {
        this.format = format;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public int getThreadid() {
        return threadid;
    }
    
    public void setThreadid(int threadid) {
        this.threadid = threadid;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getLabware() {
        return labware;
    }
    
    public void setLabware(String labware) {
        this.labware = labware;
    }
    
    public static String getFORMAT_REGULAR() {
        return FORMAT_REGULAR;
    }
    
    public List<SampleTO> getSamples() {
        return samples;
    }
    
    public void setSamples(List<SampleTO> samples) {
        this.samples = samples;
    }
    
    public ContainertypeTO getContainertype() {
        return containertype;
    }
    
    public void setContainertype(ContainertypeTO containertype) {
        this.containertype = containertype;
    }
    
    public static String getLOCATION_FREEZER() {
        return LOCATION_FREEZER;
    }
    
    public static String getSTATUS_EMPTY() {
        return STATUS_EMPTY;
    }
    
    public Collection<ContainerpropertyTO> getProperties() {
        return properties;
    }
    
    public void setProperties(Collection<ContainerpropertyTO> properties) {
        this.properties = properties;
    }
    
    public Collection<SlideTO> getSlides() {
        return slides;
    }
    
    public void setSlides(Collection<SlideTO> slides) {
        this.slides = slides;
    }
    
    public void addSlide(SlideTO s) {
        this.getSlides().add(s);
    }

    public int getNumofrow() {
        return numofrow;
    }

    public void setNumofrow(int numofrow) {
        this.numofrow = numofrow;
    }

    public int getNumofcol() {
        return numofcol;
    }

    public void setNumofcol(int numofcol) {
        this.numofcol = numofcol;
    }

    public int getNumofrowinblock() {
        return numofrowinblock;
    }

    public void setNumofrowinblock(int numofrowinblock) {
        this.numofrowinblock = numofrowinblock;
    }

    public int getNumofcolinblock() {
        return numofcolinblock;
    }

    public void setNumofcolinblock(int numofcolinblock) {
        this.numofcolinblock = numofcolinblock;
    }

    public int getNumofspots() {
        return numofspots;
    }

    public void setNumofspots(int numofspots) {
        this.numofspots = numofspots;
    }
}
