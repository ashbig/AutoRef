/*
 * ReagentImporter.java
 *
 * Created on July 17, 2007, 11:51 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package process;

import core.ReagentInfo;
import core.Well;
import dao.DaoException;
import dao.ReagentDAO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import transfer.ContainercellTO;
import transfer.ContainerheaderTO;
import transfer.ReagentTO;
import transfer.SampleTO;
import transfer.TransferException;

/**
 *
 * @author dzuo
 */
public class ReagentImporter implements Serializable {
    private List<ReagentInfo> reagents;
    private List<ContainerheaderTO> containers;
    private List<ReagentTO> reagentList;
    //private ContainertypeTO containertype;
    private String labware;
    private String sampletype;
    private String sampleform;
    private String location;
    
    /**
     * Creates a new instance of ReagentImporter
     */
    public ReagentImporter() {
    }
    
    public ReagentImporter(List<ReagentInfo> reagents) {
        this.setReagents(reagents);
    }
    
    public void destroyReagentList() {
        this.getReagents().clear();
        this.setReagents(null);
    }
    
    public void populateContainers() throws ProcessException, DaoException {
        for(ContainerheaderTO c:containers) {
            c.initSamples();
            c.setStatus(ContainerheaderTO.getSTATUS_GOOD());
        }
        
        ReagentDAO dao = makeReagentDAO();
        setReagentList(new ArrayList<ReagentTO>());
        Map reagentMap = dao.getReagents(getReagents());
        Set keys = reagentMap.keySet();
        Iterator iter = keys.iterator();
        
        while(iter.hasNext()) {
            ReagentInfo r = (ReagentInfo)iter.next();
            ReagentTO reagent = (ReagentTO)reagentMap.get(r);
            if(reagent == null) {
                reagent = findReagent(getReagentList(), r);
                if(reagent == null) {
                    reagent = createReagent(r);
                    getReagentList().add(reagent);
                }
            }
            ContainerheaderTO containerTo = findContainer(r.getPlate());
            if(containerTo == null) {
                throw new ProcessException("Cannot find container in the system: "+r.getPlate());
            }
            
            int pos = Well.convertWellToVPos(r.getWell(), containerTo.getContainertype().getNumofrow());
            SampleTO sample = new SampleTO(0, SampleTO.getNAME_GENERAL(), null, 0, 0, null, getSampletype(), getSampleform(), SampleTO.getSTATUS_GOOD(), 0, pos);
            Well well = Well.convertVPosToWell(pos, containerTo.getContainertype().getNumofrow());
            ContainercellTO cell = new ContainercellTO(pos, well.getX(), well.getY(), getCellType());
            sample.setCell(cell);
            sample.addReagent(reagent);
            addSampleToContainer(sample, containerTo);
        }
    }
    
    public List getReagents() {
        return reagents;
    }
    
    public void setReagents(List reagents) {
        this.reagents = reagents;
    }
    
    public ReagentDAO makeReagentDAO() {
        return new ReagentDAO();
    }
    
    public ReagentTO createReagent(ReagentInfo r) {
        return new ReagentTO(r.getName(),r.getType(),r.getDesc());
    }
    
    public String getCellType() {
        return ContainercellTO.TYPE_REAGENT;
    }
    
    public ReagentTO findReagent(List l, ReagentInfo r) {
        for(int i=0; i<l.size(); i++) {
            ReagentTO reagent = (ReagentTO)l.get(i);
            if(reagent.getName().equals(((ReagentInfo)r).getName())) {
                return reagent;
            }
        }
        return null;
    }
    
    public void addSampleToContainer(SampleTO sample, ContainerheaderTO c) throws ProcessException {
        try {
            c.setSample(sample, sample.getPosition());
        } catch (TransferException ex) {
            throw new ProcessException("Cannot add sample to container due to the following error.\n"+ex.getMessage());
        }
    }
    
    public ContainerheaderTO findContainer(String plate) {
        for(ContainerheaderTO container:containers) {
            if(plate.equals(container.getBarcode()))
                return container;
        }
        
        return null;
    }
    
    public List<ContainerheaderTO> getContainers() {
        return containers;
    }
    
    public void setContainers(List<ContainerheaderTO> containers) {
        this.containers = containers;
    }
    
    public List<ReagentTO> getReagentList() {
        return reagentList;
    }
    
    public void setReagentList(List<ReagentTO> reagentList) {
        this.reagentList = reagentList;
    }
    
    public String getLabware() {
        return labware;
    }
    
    public void setLabware(String labware) {
        this.labware = labware;
    }
    
    public String getSampletype() {
        return sampletype;
    }
    
    public void setSampletype(String sampletype) {
        this.sampletype = sampletype;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public String getSampleform() {
        return sampleform;
    }
    
    public void setSampleform(String sampleform) {
        this.sampleform = sampleform;
    }
}
