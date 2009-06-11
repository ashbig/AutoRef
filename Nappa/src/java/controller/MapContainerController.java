/*
 * MapContainerController.java
 *
 * Created on October 25, 2007, 2:26 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package controller;

import core.Processobjectlineageinfo;
import dao.ContainerDAO;
import dao.DaoException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import process.Container96To96Mapper;
import process.ContainerManager;
import process.ContainerMapper;
import process.ProcessException;
import transfer.ContainerheaderTO;
import transfer.ProcessobjectTO;
import transfer.ProcessprotocolTO;
import transfer.ResearcherTO;
import transfer.SampleTO;

/**
 *
 * @author dzuo
 */
public class MapContainerController extends ProcessController {
    protected List srcLabels;
    protected List destLabels;
    protected String sampletype;
    protected String sampleform;
    //protected String samplename;
    protected ContainerMapper mapper;
    protected String location;
    protected boolean isNumber;
    
    
    /** Creates a new instance of MapContainerController */
    public MapContainerController() {
    }
    
    public MapContainerController(List slabels, List dlabels) {
        this.setSrcLabels(slabels);
        this.setDestLabels(dlabels);
    }

    public List getSrcLabels() {
        return srcLabels;
    }

    public void setSrcLabels(List srcLabels) {
        this.srcLabels = srcLabels;
    }

    public List getDestLabels() {
        return destLabels;
    }

    public void setDestLabels(List destLabels) {
        this.destLabels = destLabels;
    }

    public String getSampletype() {
        return sampletype;
    }

    public void setSampletype(String sampletype) {
        this.sampletype = sampletype;
    }

    public String getSampleform() {
        return sampleform;
    }

    public void setSampleform(String sampleform) {
        this.sampleform = sampleform;
    }

    public ContainerMapper getMapper() {
        return mapper;
    }

    public void setMapper(ContainerMapper mapper) {
        this.mapper = mapper;
    }

    public boolean getIsNumber() {
        return isNumber;
    }

    public void setIsNumber(boolean isNumber) {
        this.isNumber = isNumber;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    
    public void doSpecificProcess() throws ControllerException {
        setMapper(getContainerMapper());
        getMapper().setNewsampleform(getSampleform());
        getMapper().setNewsampletype(getSampletype());
        getMapper().setIsNumber(getIsNumber());
        getMapper().setLocation(getLocation());
        try {
            List missinglabels = new ArrayList();
            missinglabels.addAll(getDestLabels());
            Collection<ContainerheaderTO> containers = ContainerManager.checkContainers(missinglabels, ContainerheaderTO.getSTATUS_EMPTY(), true, false, false);
            if(missinglabels.size()>0) {
                throw new ControllerException("The following labels are not valid: "+missinglabels);
            }
            
            missinglabels = new ArrayList();
            missinglabels.addAll(getSrcLabels());
            Collection<ContainerheaderTO> srcContainers = ContainerManager.checkContainers(missinglabels, ContainerheaderTO.getSTATUS_GOOD(), false, true, true);
            if(missinglabels.size()>0) {
                throw new ControllerException("The following labels are not valid: "+missinglabels);
            }
            
            getMapper().setDestContainers(containers);
            getMapper().setSrcContainers(srcContainers);
            getMapper().mapContainers();
            
            setContainerLineages();
            getPe().setSlineages(getMapper().getLineages());
            addOtherObjects();
        } catch (ProcessException ex) {
            throw new ControllerException("Error occured while mapping containers.\n"+ex.getMessage());
        } catch (Exception ex) {
            throw new ControllerException("Error occured.\n"+ex.getMessage());
        }
    }
 
    public void setContainerLineages() {
            List<Processobjectlineageinfo> clineages = mapper.getClineages();
            int m=0;
            for(Processobjectlineageinfo info:clineages) {
                m++;
                List<ProcessobjectTO> srcs = info.getFrom();
                List<ProcessobjectTO> dests = info.getTo();
                int n=0;
                for(ProcessobjectTO c:srcs) {
                    n++;
                    c.setObjecttype(ProcessobjectTO.getTYPE_CONTAINERHEADER());
                    c.setIoflag(ProcessobjectTO.getIO_INPUT());
                    c.setLevel(m);
                    c.setOrder(n);
                    getPe().addProcessobject(c);
                }
                n=0;
                for(ProcessobjectTO c:dests) {
                    n++;
                    c.setObjecttype(ProcessobjectTO.getTYPE_CONTAINERHEADER());
                    c.setIoflag(ProcessobjectTO.getIO_OUTPUT());
                    c.setLevel(m);
                    c.setOrder(n);
                    getPe().addProcessobject(c);
                }
            }
    }
    
    public void persistSpecificProcess(Connection conn) throws ControllerException {
        Collection<ContainerheaderTO> srcContainers = getMapper().getSrcContainers();
        Collection<ContainerheaderTO> destContainers = getMapper().getDestContainers();
        
        try {
            List containers = new ArrayList<ContainerheaderTO>();
            ContainerDAO dao2 = new ContainerDAO(conn);
            dao2.addContainers(getMapper().getDestContainers(), true, true);
            
            persistOthers(conn);
        } catch (DaoException ex) {
            throw new ControllerException("DaoException: "+ex.getMessage());
        }
    }
    
    public void addOtherObjects() throws ControllerException {}
    
    public void persistOthers(Connection conn) throws ControllerException {}
    
    public ContainerMapper getContainerMapper() {
        return new Container96To96Mapper();
    }
    
    public static void main(String args[]) {
        List slabels = new ArrayList();
        slabels.add("HsxXG002541");
        slabels.add("HsxXG002542");
        slabels.add("HsxXG002543");
        slabels.add("HsxXG002544");
        
        List dlabels = new ArrayList();
        dlabels.add("HsxXG002541-1");
        dlabels.add("HsxXG002542-2");
        dlabels.add("HsxXG002543-3");
        dlabels.add("HsxXG002544-4");
        
        String sampletype = SampleTO.getTYPE_GLYCEROL();
        String sampleform = SampleTO.getFORM_GLYCEROL();
        String samplename = SampleTO.getNAME_GENERAL();
        ProcessprotocolTO protocol = new ProcessprotocolTO(ProcessprotocolTO.TRANSFER_FROM_96_TO_384, null, null);
        ResearcherTO researcher = new ResearcherTO("dzuo", null, null, null, "dzuo");
        String location = ContainerheaderTO.getLOCATION_FREEZER();
        
        MapContainerController controller = new MapContainerController(slabels, dlabels);
        controller.setIsNumber(false);
        controller.setSampleform(sampleform);
        controller.setSampletype(sampletype);
        controller.setProtocol(protocol);
        controller.setWho(researcher);
        controller.setLocation(location);
        
        try {
            controller.doProcess();
            controller.persistProcess();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
