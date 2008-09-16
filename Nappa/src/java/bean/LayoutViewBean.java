/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import core.Block;
import core.Slidecontainerlineageinfo;
import core.Well;
import dao.ReagentDAO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import transfer.ContainercellTO;
import transfer.SlidecelllineageTO;
import transfer.LayoutcontainerTO;
import transfer.ReagentTO;
import transfer.SlidecellTO;
import transfer.SlidelayoutTO;
import util.SlidecellMapComparator;

/**
 *
 * @author dzuo
 */
public class LayoutViewBean {

    protected SlidelayoutTO layout;
    protected int blocknum;
    protected List<SlidecelllineageTO> cells;
    protected String message;
    protected boolean editcontrol;
    protected List<SelectItem> controlreagents;
    private LayoutcontainerTO container;
    protected DataModel plateModel;
    protected DataModel headerModel;
    protected DataModel blockModel;
    protected DataModel blockHeaderModel;

    public LayoutViewBean() {
    }

    public String viewContainerDetail() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            Map map = context.getExternalContext().getRequestParameterMap();
            String containername = (String) map.get("name");
            int level = Integer.parseInt((String) map.get("level"));
            String io = (String) map.get("io");
            LayoutcontainerTO c = findContainer(containername, level, io);

            if (c == null) {
                throw new Exception("Cannot find container: " + containername);
            }

            setContainer(c);
            setCells(c.getCells());
            List containertable = convertContainerToDatamodel(c);
            setPlateModel(new ListDataModel(containertable));

            List l = new ArrayList();
            for (int i = 0; i < c.getColnum(); i++) {
                l.add(new Integer(i + 1));
            }
            setHeaderModel(new ListDataModel(l));
        } catch (Exception ex) {
            ex.printStackTrace();
            setMessage(ex.getMessage());
            return null;
        }
        return "viewcontainerdetail";
    }

    public String viewSlideDetail() {
        try {
            LayoutcontainerTO c = getLayout().getSlide();
            convertSlideToDatamodel(c);

            setBlocknum(1);
            List<SlidecelllineageTO> samples = c.getCellsForBlock(blocknum);
            setCells(samples);
            convertCellsToDatamodel(samples);
        } catch (Exception ex) {
            System.out.println(ex);
        }

        return "viewslidedetail";
    }

    public LayoutcontainerTO findContainer(String name, int level, String io) {
        for (Slidecontainerlineageinfo info : getLayout().getLineageinfo()) {
            List<LayoutcontainerTO> from = info.getFrom();
            for (LayoutcontainerTO c : from) {
                if (c.getName().equals(name) && c.getLevel() == level && c.getIoflag().equals((io))) {
                    return c;
                }
            }
            List<LayoutcontainerTO> to = info.getTo();
            for (LayoutcontainerTO c : to) {
                if (c.getName().equals(name) && c.getLevel() == level && c.getIoflag().equals((io))) {
                    return c;
                }
            }
        }
        return null;
    }

    public List convertContainerToDatamodel(LayoutcontainerTO c) throws Exception {
        List mappingsInTable = new ArrayList<List>();
        for (int i = 0; i < c.getRownum(); i++) {
            List<SlidecelllineageTO> cols = new ArrayList<SlidecelllineageTO>();
            for (int j = 0; j < c.getColnum(); j++) {
                int pos = Well.convertWellToVPos(i + 1, j + 1, c.getRownum());
                SlidecelllineageTO s = c.getCell(pos);
                if (s == null) {
                    s = new SlidecelllineageTO(new ContainercellTO(pos, "" + (i + 1), "" + (j + 1), ContainercellTO.TYPE_EMPTY, 0, 0));
                }
                cols.add(s);
            }
            mappingsInTable.add(cols);
        }
        return mappingsInTable;
    }

    public SlidecelllineageTO getRowLabel() {
        SlidecelllineageTO rowLabel = null;

        if (getPlateModel().isRowAvailable()) {

            List list = (List) getPlateModel().getRowData();

            rowLabel = (SlidecelllineageTO) list.get(0);

        }

        return rowLabel;
    }

    public SlidecelllineageTO getPlateValue() {
        SlidecelllineageTO plateValue = null;

        if (getPlateModel().isRowAvailable() && getHeaderModel().isRowAvailable()) {

            List list = (List) getPlateModel().getRowData();

            plateValue = (SlidecelllineageTO) list.get(getHeaderModel().getRowIndex());

        }
        return plateValue;
    }

    public void convertSlideToDatamodel(LayoutcontainerTO layout) {
        List mappingsInTable = new ArrayList<List>();
        List headerList = new ArrayList();
        List<Block> blocks = layout.getBlocks();
        int row = 1;
        List<Block> cols = new ArrayList<Block>();
        boolean addHeader = true;
        for (Block b : blocks) {
            if (b.getX() != row) {
                mappingsInTable.add(cols);
                cols = new ArrayList<Block>();
                addHeader = false;
            }
            if (addHeader) {
                headerList.add(b.getY());
            }
            cols.add(b);
            row = b.getX();
        }
        mappingsInTable.add(cols);
        setPlateModel(new ListDataModel(mappingsInTable));
        setHeaderModel(new ListDataModel(headerList));
    }

    public int getSlideRowLabel() {
        int num = 0;
        if (getPlateModel().isRowAvailable()) {
            List list = (List) getPlateModel().getRowData();
            num = ((Block) list.get(0)).getX();
        }
        return num;
    }

    public void viewBlock() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            Map map = context.getExternalContext().getRequestParameterMap();
            setBlocknum(Integer.parseInt((String) map.get("blocknum")));
            List<SlidecelllineageTO> cs = getLayout().getSlide().getCellsForBlock(blocknum);
            setCells(cs);
            convertCellsToDatamodel(cs);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void convertCellsToDatamodel(List<SlidecelllineageTO> samples) {
        List mappingsInTable = new ArrayList<List>();
        List headerList = new ArrayList();
        getCellsInDatamodel(samples, mappingsInTable, headerList);
        setBlockModel(new ListDataModel(mappingsInTable));
        setBlockHeaderModel(new ListDataModel(headerList));
    }

    public void getCellsInDatamodel(List<SlidecelllineageTO> samples, List<List> mappingsInTable, List headerList) {
        int row = 1;
        List<SlidecelllineageTO> cols = new ArrayList<SlidecelllineageTO>();
        boolean addHeader = true;
        for (SlidecelllineageTO sample : samples) {
            if (((SlidecellTO) sample.getCell()).getBlockwellx() != row) {
                mappingsInTable.add(cols);
                cols = new ArrayList<SlidecelllineageTO>();
                addHeader = false;
            }
            if (addHeader) {
                headerList.add(((SlidecellTO) sample.getCell()).getBlockwelly());
            }
            cols.add(sample);
            row = ((SlidecellTO) sample.getCell()).getBlockwellx();
        }
        mappingsInTable.add(cols);
    }

    public Block getSlideValue() {
        Block b = null;

        if (getPlateModel().isRowAvailable() && getHeaderModel().isRowAvailable()) {
            List list = (List) getPlateModel().getRowData();
            b = (Block) list.get(getHeaderModel().getRowIndex());
        }
        return b;
    }

    public SlidecelllineageTO getBlockValue() {
        SlidecelllineageTO plateValue = null;

        if (getBlockModel().isRowAvailable() && getBlockHeaderModel().isRowAvailable()) {

            List list = (List) getBlockModel().getRowData();

            plateValue = (SlidecelllineageTO) list.get(getBlockHeaderModel().getRowIndex());

        }
        return plateValue;
    }

    public int getBlockRowLabel() {
        int num = 0;
        if (getBlockModel().isRowAvailable()) {
            List list = (List) getBlockModel().getRowData();
            num = ((SlidecellTO) ((SlidecelllineageTO) list.get(0)).getCell()).getBlockwellx();
        }
        return num;
    }

    private void loadControlReagents() {
        try {
            List<ReagentTO> reagents = ReagentDAO.getReagents(ReagentTO.getTYPE_CONTROL());
            controlreagents = new ArrayList<SelectItem>();
            controlreagents.add(new SelectItem(ReagentTO.NON_SPOTS, ReagentTO.NON_SPOTS));
            for (ReagentTO r : reagents) {
                SelectItem i = new SelectItem(r.getName(), r.getName());
                controlreagents.add(i);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            setMessage("Cannot get controls from database.");
        }
    }

    public void saveControls() {
        for (SlidecelllineageTO cell : cells) {
            String control = cell.getCell().getControlreagent();
            List<ContainercellTO> pre = cell.getPre();
            for (ContainercellTO c : pre) {
                c.setControlreagent(control);
            }
            List<ContainercellTO> post = cell.getPost();
            for (ContainercellTO c : post) {
                c.setControlreagent(control);
            }
        }
    }

    public void saveSlideControls() {
        List<SlidecelllineageTO> slidecells = getLayout().getSlide().getCells();
        for (SlidecelllineageTO cell : slidecells) {
            String control = cell.getCell().getControlreagent();
            List<ContainercellTO> pre = cell.getPre();
            for (ContainercellTO c : pre) {
                c.setControlreagent(control);
            }
            List<ContainercellTO> post = cell.getPost();
            for (ContainercellTO c : post) {
                c.setControlreagent(control);
            }
        }
    }

    public void downloadMap() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletResponse response = (HttpServletResponse) context.getResponse();
        response.setContentType("Application/x-msexcel");
        response.setHeader("Content-Disposition", "inline;filename=slide.xls");

        LayoutcontainerTO slide = getLayout().getSlide();
        List<SlidecelllineageTO> cells = slide.getCells();
        Collections.sort(cells, new SlidecellMapComparator());
        int index = 1;
        try {
            ServletOutputStream out = response.getOutputStream();
            for (SlidecelllineageTO sample : cells) {
                SlidecellTO cell = (SlidecellTO) sample.getCell();
                if (Integer.parseInt(cell.getPosx()) != index) {
                    out.println();
                    index++;
                }
                out.print(cell.getBlocknum() + "," + cell.getPosx() + "," + cell.getPosy() + "," + cell.getPos() +","+ cell.getType());
                if(cell.getControlreagent() != null) {
                    out.print(","+ cell.getControlreagent());
                }
                out.print("\t");
            }
            out.flush();
            out.close();
            FacesContext.getCurrentInstance().responseComplete();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getBlocknum() {
        return blocknum;
    }

    public void setBlocknum(int blocknum) {
        this.blocknum = blocknum;
    }

    public List<SlidecelllineageTO> getCells() {
        return cells;
    }

    public void setCells(List<SlidecelllineageTO> cells) {
        this.cells = cells;
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

    public SlidelayoutTO getLayout() {
        return layout;
    }

    public void setLayout(SlidelayoutTO layout) {
        this.layout = layout;
    }

    public boolean isEditcontrol() {
        return editcontrol;
    }

    public void setEditcontrol(boolean editcontrol) {
        this.editcontrol = editcontrol;
    }

    public List<SelectItem> getControlreagents() {
        if (controlreagents == null) {
            loadControlReagents();
        }
        return controlreagents;
    }

    public void setControlreagents(List<SelectItem> controlreagents) {
        this.controlreagents = controlreagents;
    }

    public LayoutcontainerTO getContainer() {
        return container;
    }

    public void setContainer(LayoutcontainerTO container) {
        this.container = container;
    }
}
