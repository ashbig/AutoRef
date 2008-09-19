/*
 * ContainerMapper.java
 *
 * Created on April 26, 2007, 3:20 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package process;

import Algorithm.AlgorithmException;
import Algorithm.MappingAlgorithm;
import core.Processobjectlineageinfo;
import transfer.ProgrammappingTO;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import transfer.ContainercellTO;
import transfer.ContainerheaderTO;
import transfer.SampleTO;
import transfer.SamplelineageTO;
import transfer.TransferException;

/**
 *
 * @author dzuo
 */
public abstract class ContainerMapper {
    protected Collection<ContainerheaderTO> srcContainers;
    protected Collection<ContainerheaderTO> destContainers;
    protected List<SamplelineageTO> lineages;
    protected List<Processobjectlineageinfo> clineages;
    
    protected MappingAlgorithm algorithm;
    protected String newsamplename;
    protected String newsampletype;
    protected String newsampleform;
    //protected ContainertypeTO containertype;
    protected boolean isNumber;
    protected String location;
    
    /** Creates a new instance of ContainerMapper */
    public ContainerMapper() {
        setSrcContainers(new ArrayList());
        setDestContainers(new ArrayList());
        setLineages(new ArrayList());
        setClineages(new ArrayList());
    }
    
    public Collection<ContainerheaderTO> getSrcContainers() {
        return srcContainers;
    }
    
    public void setSrcContainers(Collection<ContainerheaderTO> srcContainers) {
        this.srcContainers = srcContainers;
    }
    
    public Collection<ContainerheaderTO> getDestContainers() {
        return destContainers;
    }
    
    public void setDestContainers(Collection<ContainerheaderTO> destContainers) {
        this.destContainers = destContainers;
    }
    
    public void addSrcContainers(ContainerheaderTO c) {
        this.getSrcContainers().add(c);
    }
    
    public void addDestContainers(ContainerheaderTO c) {
        this.getDestContainers().add(c);
    }
    
    public MappingAlgorithm getAlgorithm() {
        return algorithm;
    }
    
    public void setAlgorithm(MappingAlgorithm algorithm) {
        this.algorithm = algorithm;
    }
    
    public abstract boolean isValid();
    
    public void mapContainers() throws ProcessException {
        try {
            getAlgorithm().setIsNumber(isIsNumber());
            getAlgorithm().doMapping();
            Collection<ProgrammappingTO> mappings = getAlgorithm().getMappings();
            doMapping(mappings, getAlgorithm().getSrclabels(), getAlgorithm().getDestlabels());
        } catch (AlgorithmException ex) {
            throw new ProcessException(ex.getMessage());
        }
    }
    
    //abstract public String getLabware();
    public ContainercellTO getContainercellmapTO(ProgrammappingTO m) {
        return new ContainercellTO(m.getDestpos(),m.getDestwellx(),m.getDestwelly(),ContainercellTO.TYPE_EMPTY);
    }
    /**
     * public List sortContainers(Collection<String> labels, Collection<String> maplabels, Collection<ContainerheaderTO> newDestContainers) throws ProcessException {
     * if(labels.size() < maplabels.size())
     * throw new ProcessException("Not enough labels.");
     *
     * List containers = new ArrayList<ContainerheaderTO>();
     * Iterator iter1 = labels.iterator();
     * Iterator iter2 = maplabels.iterator();
     * while(iter2.hasNext()) {
     * String maplabel = (String)iter2.next();
     * String label = (String)iter1.next();
     * ContainerheaderTO container = findContainer(newDestContainers, label);
     * if(container == null) {
     * throw new ProcessException("Cannot find container from system: "+label);
     * }
     * containers.add(container);
     * }
     * return containers;
     * }
     **/
    
    public void doMapping(Collection<ProgrammappingTO> mappings, Collection<String> mapsrclabels, Collection<String> mapdestlabels) throws ProcessException {
        if(destContainers.size()%mapdestlabels.size()!=0) {
            throw new ProcessException("Number of destination labels is wrong.");
        }
        
        if(!isValid()) {
            throw new ProcessException("Number of source and destination plates doesn't match.");
        }
        
        Iterator iter = destContainers.iterator();
        Iterator iter2 = getSrcContainers().iterator();
        List processSrcContainers = new ArrayList();
        List processDestContainers = new ArrayList();
        int i=0;
        int k=0;
        int n=0;
        while(iter.hasNext()) {
            processDestContainers.add((ContainerheaderTO)iter.next());
            i++;
            if(i%mapdestlabels.size()==0) {
                Iterator iter3 = mapsrclabels.iterator();
                while(iter3.hasNext()) {
                    Object o = iter3.next();
                    processSrcContainers.add((ContainerheaderTO)iter2.next());
                    n++;
                }
                
                for(ProgrammappingTO m:mappings) {
                    ContainerheaderTO containerFrom = findContainer(processSrcContainers, mapsrclabels, m.getSrcplate());
                    if(containerFrom == null)
                        throw new ProcessException("Cannot find container "+m.getSrcplate());
                    SampleTO sampleFrom = containerFrom.getSample(m.getSrcpos());
                    if(sampleFrom == null)
                        throw new ProcessException("Cannot find sample for container "+m.getSrcplate()+" at position "+m.getSrcpos());
                    
                    SampleTO sampleTo = new SampleTO(-1,getNewsamplename(),null,0,0,null,getNewsampletype(),getNewsampleform(), SampleTO.getSTATUS_GOOD(), 0, m.getDestpos());
                    ContainercellTO dest = getContainercellmapTO(m);
                    dest.setType(sampleFrom.getCell().getType());
                    sampleTo.setCell(dest);
                    sampleTo.setReagents(sampleFrom.getReagents());
                    if(!SampleTO.getTYPE_CULTURE().equals(getNewsampletype())) {
                        sampleTo.setProperties(sampleFrom.getProperties());
                    }
                    ContainerheaderTO containerTo = findContainer(processDestContainers, mapdestlabels, m.getDestplate());
                    if(containerTo == null)
                        throw new ProcessException("Cannot find container "+m.getDestplate());
                    sampleTo.setContainerheader(containerTo);
                    try {
                        containerTo.addSample(sampleTo);
                    } catch (TransferException ex) {
                        throw new ProcessException(ex.getMessage());
                    }
                    addOtherReagents(containerFrom, sampleTo, k, n);
                    SamplelineageTO lineage = new SamplelineageTO(sampleFrom, sampleTo);
                    getLineages().add(lineage);
                    addToContainerLineages(containerFrom, containerTo);
                }
                processDestContainers = new ArrayList();
                processSrcContainers = new ArrayList();
                k=n;
            }
        }
    }
    
    public void addOtherReagents(ContainerheaderTO container, SampleTO sample, int start, int end) {}
    
    public void addToContainerLineages(ContainerheaderTO containerFrom, ContainerheaderTO containerTo) {
        for(Processobjectlineageinfo cl:clineages) {
            if(cl.foundFromObject(containerFrom)) {
                if(cl.foundToObject(containerTo)) {
                    return;
                } else {
                    cl.addToTo(containerTo);
                    return;
                }
            } else {
                if(cl.foundToObject(containerTo)) {
                    cl.addToFrom(containerFrom);
                    return;
                }
            }
        }
        Processobjectlineageinfo info = new Processobjectlineageinfo();
        info.addToFrom(containerFrom);
        info.addToTo(containerTo);
        clineages.add(info);
    }
    
    protected ContainerheaderTO findContainer(Collection<ContainerheaderTO> containers, String label) {
        for(ContainerheaderTO c:containers) {
            if(c.getBarcode().equals(label))
                return c;
        }
        return null;
    }
    
    protected ContainerheaderTO findContainer(Collection<ContainerheaderTO> containers, Collection<String> labels, String label) {
        Iterator iter = labels.iterator();
        Iterator iter2 = containers.iterator();
        while(iter.hasNext()) {
            String s = (String)iter.next();
            ContainerheaderTO container = (ContainerheaderTO)iter2.next();
            if(s.equals(label)) {
                return container;
            }
        }
        
        return null;
    }
    
    public String getNewsampletype() {
        return newsampletype;
    }
    
    public void setNewsampletype(String newsampletype) {
        this.newsampletype = newsampletype;
    }
    
    public String getNewsamplename() {
        return newsamplename;
    }
    
    public void setNewsamplename(String newsamplename) {
        this.newsamplename = newsamplename;
    }
    
    public List<SamplelineageTO> getLineages() {
        return lineages;
    }
    
    public void setLineages(List<SamplelineageTO> lineages) {
        this.lineages = lineages;
    }
    
    public String getNewsampleform() {
        return newsampleform;
    }
    
    public void setNewsampleform(String newsampleform) {
        this.newsampleform = newsampleform;
    }
    
    public List<Processobjectlineageinfo> getClineages() {
        return clineages;
    }
    
    public void setClineages(List<Processobjectlineageinfo> clineages) {
        this.clineages = clineages;
    }
    
    public boolean getIsNumber() {
        return isIsNumber();
    }
    
    public void setIsNumber(boolean isNumber) {
        this.isNumber = isNumber;
    }
    
    public boolean isIsNumber() {
        return isNumber;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
}
