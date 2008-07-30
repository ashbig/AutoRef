/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package transfer;

import core.Block;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dzuo
 */
public class LayoutcontainerTO extends ProgramcontainerTO implements Serializable {
    private boolean iscontrol;
    private String layoutname;
    private String sampletype;
    private int level;
    private List<SlidecelllineageTO> cells;
    private int containerid;

    public LayoutcontainerTO(String layoutname, String containername, String type, String sampletype, int order, int level, String ioflag) {
        super(null, containername, type, order, ioflag);
        this.setLayoutname(layoutname);
        this.setSampletype(sampletype);
        this.setLevel(level);
        this.setCells(new ArrayList<SlidecelllineageTO>());
    }
    
    public void addCell(SlidecelllineageTO cell) {
        this.getCells().add(cell);
    }

    public SlidecelllineageTO getCell(int pos) {
        for (SlidecelllineageTO c : cells) {
            ContainercellTO cell = c.getCell();
            if (cell.getPos() == pos) {
                return c;
            }
        }
        return null;
    }

    public List<SlidecelllineageTO> getCellsForBlock(int blocknum) {
        List<SlidecelllineageTO> l = new ArrayList<SlidecelllineageTO>();
        for (SlidecelllineageTO c : cells) {
            SlidecellTO cell = (SlidecellTO) c.getCell();
            if (cell.getBlocknum() == blocknum) {
                l.add(c);
            }
        }
        return l;
    }

    public List<Block> getBlocks() {
        List<Block> blocks = new ArrayList<Block>();
        for (SlidecelllineageTO l : cells) {
            SlidecellTO cell = (SlidecellTO)l.getCell();
            addToBlock(blocks, cell);
        }
        return blocks;
    }

    private void addToBlock(List<Block> blocks, SlidecellTO cell) {
        for (Block b : blocks) {
            if (b.getNum() == cell.getBlocknum()) {
                return;
            }
        }
        Block block = new Block(cell.getBlocknum(), cell.getBlockposx(), cell.getBlockposy());
        block.setX(cell.getBlockx());
        block.setY(cell.getBlocky());
        blocks.add(block);
    }

    public String getLayoutname() {
        return layoutname;
    }

    public void setLayoutname(String layoutname) {
        this.layoutname = layoutname;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getSampletype() {
        return sampletype;
    }

    public void setSampletype(String sampletype) {
        this.sampletype = sampletype;
        if(ReagentTO.getTYPE_CONTROL().equals(sampletype))
            this.iscontrol = true;
        else
            this.iscontrol = false;
    }

    public List<SlidecelllineageTO> getCells() {
        return cells;
    }

    public void setCells(List<SlidecelllineageTO> cells) {
        this.cells = cells;
    }

    public boolean isIscontrol() {
        return iscontrol;
    }

    public void setIscontrol(boolean iscontrol) {
        this.iscontrol = iscontrol;
        if(iscontrol) {
            this.sampletype = ReagentTO.getTYPE_CONTROL();
        } else {
            this.sampletype = ReagentTO.getTYPE_CLONE();
        }
    }

    public int getContainerid() {
        return containerid;
    }

    public void setContainerid(int containerid) {
        this.containerid = containerid;
    }
}
