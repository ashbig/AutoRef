/*
 * ContainerViewBean.java
 *
 * Created on October 10, 2007, 11:45 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package bean;

import core.Block;
import core.Well;
import dao.ContainerDAO;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import transfer.ContainerheaderTO;
import transfer.ContainertypeTO;
import transfer.ReagentTO;
import transfer.SampleTO;
import transfer.SlideTO;
import transfer.SlidecellTO;
import util.StringConvertor;

/**
 *
 * @author dzuo
 */
public class ContainerViewBean {
    private int containerid;
    private String labels;
    private Collection<ContainerheaderTO> containers;
    private ContainerheaderTO containerheader;
    private Collection<SampleTO> samples;
    private SlideTO slide;
    private boolean displayDetail;
    private boolean displayBlock;
    private ReagentTO reagent;
    private DataModel plateModel;
    private DataModel headerModel;
    private DataModel blockModel;
    private DataModel blockHeaderModel;
    private double culturecut;
    private double dnacut;
    
    private int blocknum;
    
    /** Creates a new instance of ContainerViewBean */
    public ContainerViewBean() {
        reset();
    }
    
    public void reset() {
        setReagent(null);
        setDisplayDetail(false);
        setDisplayBlock(false);
        setCulturecut(SampleTO.getCULTURE_THREASHOLD());
        setDnacut(SampleTO.getDNA_THREASHOLD());
    }
    
    public ContainerheaderTO getContainerheader() {
        return containerheader;
    }
    
    public void setContainerheader(ContainerheaderTO containerheader) {
        this.containerheader = containerheader;
    }
    
    public void setDisplayDetail(boolean displayDetail) {
        this.displayDetail = displayDetail;
    }
    
    public boolean isDisplayDetail() {
        return displayDetail;
    }
    
    public ReagentTO getReagent() {
        return reagent;
    }
    
    public void setReagent(ReagentTO reagent) {
        this.reagent = reagent;
    }
    
    public String findContainers() {
        try {
            List containerLabels = StringConvertor.convertFromStringToList(getLabels(), "\n\t\b ");
            containers = ContainerDAO.checkContainers(containerLabels, false);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        
        return "listcontainers";
    }
    
    public void showReagent() {
        setDisplayDetail(false);
        FacesContext context = FacesContext.getCurrentInstance();
        Map map = context.getExternalContext().getRequestParameterMap();
        int reagentid = Integer.parseInt((String)map.get("reagentid"));
        for(SampleTO s:getSamples()) {
            for(ReagentTO r:s.getReagents()) {
                if(r.getReagentid() == reagentid) {
                    setReagent(r);
                    setDisplayDetail(true);
                }
            }
        }
    }
    
    public boolean isClone() {
        if(getReagent() == null)
            return false;
        if(ReagentTO.getTYPE_CLONE().equals(getReagent().getType()))
            return true;
        
        return false;
    }
    
    public String viewContainerOrSlide() {
        String containertype = null;
        
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            Map map = context.getExternalContext().getRequestParameterMap();
            containertype = (String)map.get("containertype");
        } catch (Exception ex) {
            System.out.println(ex);
        }
        
        if(ContainertypeTO.TYPE_SLIDE.equals(containertype))
            return viewSlide();
        else
            return viewContainer();
    }
    
    public String viewContainer() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            Map requestMap = context.getExternalContext().getRequestParameterMap();
            containerid = Integer.parseInt((String)requestMap.get("containerid"));
            ContainerheaderTO c = ContainerDAO.getContainer(containerid, true, true, true, true);
            setContainerheader(c);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        
        return viewContainerNoID();
    }
    
    public String viewContainerNoID() {
        reset();
        try {
            ContainerheaderTO c = getContainerheader();
            List containertable = TableModelBean.convertContainerToDatamodel(c);
            setPlateModel(new ListDataModel(containertable));
            
            List l = new ArrayList();
            for(int i=0; i<c.getContainertype().getNumofcol(); i++) {
                l.add(new Integer(i+1));
            }
            setHeaderModel(new ListDataModel(l));
            
            setSamples(c.getSamples());
        } catch (Exception ex) {
            System.out.println(ex);
        }
        
        return "viewContainer";
    }
    
    public String viewSlide() {
        reset();
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            Map map = context.getExternalContext().getRequestParameterMap();
            int slideid = Integer.parseInt((String)map.get("containerid"));
            SlideTO c = ContainerDAO.getSlideWithBlocks(slideid);
            setSlide(c);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        
        return viewSlideNoID();
    }
    
    public String viewSlideNoID() {
        reset();
        try {
            SlideTO c = getSlide();
            convertSlideToDatamodel(c);
            
            setBlocknum(1);
            int containerid = getSlide().getContainer().getContainerid();
            List<SampleTO> samples = ContainerDAO.getSamplesForBlock(containerid,blocknum,true,true);
            setSamples(samples);
            convertSamplesToDatamodel(samples);
            setDisplayBlock(true);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        
        return "viewSlide";
    }
    
    public void changeCulturecut() {
        ContainerheaderTO c = getContainerheader();
        Collection<SampleTO> samples = c.getSamples();
        for(SampleTO s:samples) {
            s.setCulturecut(getCulturecut());
        }
    }
    
    public void changeDnacut() {
        ContainerheaderTO c = getContainerheader();
        Collection<SampleTO> samples = c.getSamples();
        for(SampleTO s:samples) {
            s.setDnacut(getDnacut());
        }
    }
    
    public void changeCulturecutForSamples() {
        for(SampleTO s:getSamples()) {
            s.setCulturecut(getCulturecut());
        }
    }
    
    public void changeDnacutForSamples() {
        for(SampleTO s:getSamples()) {
            s.setDnacut(getDnacut());
        }
    }
    
    public void viewBlock() {
        reset();
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            Map map = context.getExternalContext().getRequestParameterMap();
            setBlocknum(Integer.parseInt((String)map.get("blocknum")));
            int containerid = getSlide().getContainer().getContainerid();
            List<SampleTO> samples = ContainerDAO.getSamplesForBlock(containerid,blocknum,true,true);
            setSamples(samples);
            convertSamplesToDatamodel(samples);
            setDisplayBlock(true);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
    
    public SampleTO getRowLabel() {
        SampleTO rowLabel = null;
        
        if(getPlateModel().isRowAvailable()) {
            
            List list = (List)getPlateModel().getRowData();
            
            rowLabel = (SampleTO)list.get(0);
            
        }
        
        return rowLabel;
    }
    
    public int getSlideRowLabel() {
        int num = 0;
        if(getPlateModel().isRowAvailable()) {
            List list = (List)getPlateModel().getRowData();
            num = ((Block)list.get(0)).getX();
        }
        return num;
    }
    
    public int getBlockRowLabel() {
        int num = 0;
        if(getBlockModel().isRowAvailable()) {
            List list = (List)getBlockModel().getRowData();
            num = ((SlidecellTO)((SampleTO)list.get(0)).getCell()).getBlockwellx();
        }
        return num;
    }
    
    public SampleTO getPlateValue() {
        SampleTO plateValue = null;
        
        if(getPlateModel().isRowAvailable() && getHeaderModel().isRowAvailable()) {
            
            List list = (List)getPlateModel().getRowData();
            
            plateValue = (SampleTO)list.get(getHeaderModel().getRowIndex());
            
        }
        return plateValue;
    }
    
    public Block getSlideValue() {
        Block b = null;
        
        if(getPlateModel().isRowAvailable() && getHeaderModel().isRowAvailable()) {
            List list = (List)getPlateModel().getRowData();
            b = (Block)list.get(getHeaderModel().getRowIndex());
        }
        return b;
    }
    
    public SampleTO getBlockValue() {
        SampleTO plateValue = null;
        
        if(getBlockModel().isRowAvailable() && getBlockHeaderModel().isRowAvailable()) {
            
            List list = (List)getBlockModel().getRowData();
            
            plateValue = (SampleTO)list.get(getBlockHeaderModel().getRowIndex());
            
        }
        return plateValue;
    }
    /**
    public List convertContainerToDatamodel(ContainerheaderTO c) {
        List mappingsInTable = new ArrayList<List>();
        for(int i=0; i<c.getRownum(); i++) {
            List<SampleTO> cols = new ArrayList<SampleTO>();
            for(int j=0; j<c.getContainertype().getNumofcol(); j++) {
                SampleTO s = c.getSample(Well.convertWellToVPos(i+1, j+1, c.getRownum()));
                cols.add(s);
            }
            mappingsInTable.add(cols);
        }
        return mappingsInTable;
    }
    */
    public void convertSlideToDatamodel(SlideTO slide) {
        List mappingsInTable = new ArrayList<List>();
        List headerList = new ArrayList();
        List<Block> blocks = slide.getBlocks();
        int row = 1;
        List<Block> cols = new ArrayList<Block>();
        boolean addHeader = true;
        for(Block b:blocks) {
            if(b.getX() != row) {
                mappingsInTable.add(cols);
                cols = new ArrayList<Block>();
                addHeader = false;
            }
            if(addHeader) {
                headerList.add(b.getY());
            }
            cols.add(b);
            row = b.getX();
        }
        mappingsInTable.add(cols);
        setPlateModel(new ListDataModel(mappingsInTable));
        setHeaderModel(new ListDataModel(headerList));
    }
    
    public void convertSamplesToDatamodel(List<SampleTO> samples) {
        List mappingsInTable = new ArrayList<List>();
        List headerList = new ArrayList();
        int row = 1;
        List<SampleTO> cols = new ArrayList<SampleTO>();
        boolean addHeader = true;
        for(SampleTO sample:samples) {
            if(((SlidecellTO)sample.getCell()).getBlockwellx() != row) {
                mappingsInTable.add(cols);
                cols = new ArrayList<SampleTO>();
                addHeader = false;
            }
            if(addHeader) {
                headerList.add(((SlidecellTO)sample.getCell()).getBlockwelly());
            }
            cols.add(sample);
            row = ((SlidecellTO)sample.getCell()).getBlockwellx();
        }
        mappingsInTable.add(cols);
        setBlockModel(new ListDataModel(mappingsInTable));
        setBlockHeaderModel(new ListDataModel(headerList));
    }
    
    public DataModel getPlateModel() {
        return plateModel;
    }
    
    public void setPlateModel(DataModel plateModel) {
        this.plateModel = plateModel;
    }
    
    public DataModel getHeaderModel() {
        return headerModel;
    }
    
    public void setHeaderModel(DataModel headerModel) {
        this.headerModel = headerModel;
    }
    
    public String getLabels() {
        return labels;
    }
    
    public void setLabels(String labels) {
        this.labels = labels;
    }
    
    public Collection<ContainerheaderTO> getContainers() {
        return containers;
    }
    
    public void setContainers(Collection<ContainerheaderTO> containers) {
        this.containers = containers;
    }
    
    public SlideTO getSlide() {
        return slide;
    }
    
    public void setSlide(SlideTO slide) {
        this.slide = slide;
    }
    
    public DataModel getBlockModel() {
        return blockModel;
    }
    
    public void setBlockModel(DataModel blockModel) {
        this.blockModel = blockModel;
    }
    
    public DataModel getBlockHeaderModel() {
        return blockHeaderModel;
    }
    
    public void setBlockHeaderModel(DataModel blockHeaderModel) {
        this.blockHeaderModel = blockHeaderModel;
    }
    
    public Collection<SampleTO> getSamples() {
        return samples;
    }
    
    public void setSamples(Collection<SampleTO> samples) {
        this.samples = samples;
    }
    
    public int getBlocknum() {
        return blocknum;
    }
    
    public void setBlocknum(int blocknum) {
        this.blocknum = blocknum;
    }
    
    public boolean isDisplayBlock() {
        return displayBlock;
    }
    
    public void setDisplayBlock(boolean displayBlock) {
        this.displayBlock = displayBlock;
    }

    public double getCulturecut() {
        return culturecut;
    }

    public void setCulturecut(double culturecut) {
        this.culturecut = culturecut;
    }

    public double getDnacut() {
        return dnacut;
    }

    public void setDnacut(double dnacut) {
        this.dnacut = dnacut;
    }

    public int getContainerid() {
        return containerid;
    }

    public void setContainerid(int containerid) {
        this.containerid = containerid;
    }
}
