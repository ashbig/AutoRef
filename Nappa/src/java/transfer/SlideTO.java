/*
 * SlideTO.java
 *
 * Created on November 15, 2007, 10:08 AM
 *
 * To change this template, choose Tools | Template Manager
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
public class SlideTO extends ProcessobjectTO  implements Serializable{
    private int slideid;
    private int printorder;
    private String barcode;
    private String surfacechem;
    private String program;
    private String startdate;
    private ContainerheaderTO container;
    
    private List<Block> blocks;
    
    /** Creates a new instance of SlideTO */
    public SlideTO() {
        blocks = new ArrayList<Block>();
    }
    
    public SlideTO(int order, String barcode, String surfacechem, String program, String startdate, ContainerheaderTO c) {
        this.setPrintorder(order);
        this.setBarcode(barcode);
        this.setSurfacechem(surfacechem);
        this.setProgram(program);
        this.setStartdate(startdate);
        this.setContainer(c);
        blocks = new ArrayList<Block>();
    }

    public SlideTO(int slideid, int order, String barcode, String chem, String program, String date) {
        this.setSlideid(slideid);
        this.setPrintorder(order);
        this.setBarcode(barcode);
        this.setSurfacechem(chem);
        this.setProgram(program);
        this.setStartdate(date);
        blocks = new ArrayList<Block>();
    }
    
    public int getSlideid() {
        return slideid;
    }

    public void setSlideid(int slideid) {
        this.slideid = slideid;
        setObjectid(slideid);
    }

    public String getSurfacechem() {
        return surfacechem;
    }

    public void setSurfacechem(String surfacechem) {
        this.surfacechem = surfacechem;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public ContainerheaderTO getContainer() {
        return container;
    }

    public void setContainer(ContainerheaderTO container) {
        this.container = container;
    }

    public int getPrintorder() {
        return printorder;
    }

    public void setPrintorder(int printorder) {
        this.printorder = printorder;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
        setObjectname(barcode);
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<Block> blocks) {
        this.blocks = blocks;
    }
    
    public void addBlock(Block b) {
        this.blocks.add(b);
    }
}
