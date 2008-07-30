/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bean;

import core.Block;
import core.Well;
import dao.ContainerDAO;
import dao.ProgramDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import transfer.ContainerheaderTO;
import transfer.ProgramdefinitionTO;
import transfer.ProgrammappingTO;
import transfer.ProgramtypeTO;
import transfer.SampleTO;
import transfer.SlideTO;
import transfer.SlidecellTO;

/**
 *
 * @author dzuo
 */
public class ProgramViewBean {
    private ProgramdefinitionTO program;
    private DataModel plateModel;
    private DataModel headerModel;
    private DataModel blockModel;
    private DataModel blockHeaderModel;
    /**
    public String viewProgram() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            Map requestMap = context.getExternalContext().getRequestParameterMap();
            String programname = (String)requestMap.get("programname");
            ProgramDAO dao = new ProgramDAO();
            ProgramdefinitionTO p = dao.getProgram(programname);
            setProgram(p);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        
        return viewProgramNoID();
    }
    
    public String viewProgramNoID() {
        String programtype = getProgram().getType();
        
        if(ProgramtypeTO.TYPE_PLATE96TO384.equals(programtype))
            return viewContainerProgram();
        else if(ProgramtypeTO.TYPE_384TOSLIDE.equals(programtype))
            return viewSlideProgram();
        else {
            System.out.println("Invalid program type: "+programtype);
            return null;
        }
    }
    
    public String viewContainerProgram() {
        try {
            List containertable = convertContainerToDatamodel(getProgram().getMappings());
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
        
        return "viewContainerProgram";
    }
    
    public String viewSlideProgram() {
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
    
    public List convertContainerToDatamodel() {
        List mappingsInTable = new ArrayList<List>();
        int rownum = program.getDestRownum();
        int colnum = program.getDestColnum();
        
        for(int i=0; i<rownum; i++) {
            List<ProgrammappingTO> cols = new ArrayList<ProgrammappingTO>();
            for(int j=0; j<colnum; j++) {
                ProgrammappingTO s = program.getMappingByDestpos(Well.convertWellToVPos(i+1, j+1, rownum));
                cols.add(s);
            }
            mappingsInTable.add(cols);
        }
        return mappingsInTable;
    }
    
    public void convertSlideToDatamodel() {
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

    public ProgramdefinitionTO getProgram() {
        return program;
    }

    public void setProgram(ProgramdefinitionTO program) {
        this.program = program;
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
    */
}
