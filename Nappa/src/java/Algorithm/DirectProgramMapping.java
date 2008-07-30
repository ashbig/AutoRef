/*
 * DirectProgramMapping.java
 *
 * Created on October 23, 2007, 4:09 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package Algorithm;

import transfer.ProgrammappingTO;
import core.Well;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author dzuo
 */
public class DirectProgramMapping extends MappingAlgorithm {
    private int rownum;
    private int colnum;
    
    /**
     * Creates a new instance of DirectProgramMapping
     */
    public DirectProgramMapping() {
        super();
        this.rownum = 8;
        this.colnum = 12;
    }
    
    public DirectProgramMapping(int row, int col) {
        super();
        this.rownum = row;
        this.colnum = col;
    }
    
    public void doMapping() throws AlgorithmException {
        mappings = new ArrayList<ProgrammappingTO>();
        String srcContainer = "src";
        String destContainer = "dest";
        
        for(int i=0; i<rownum*colnum; i++) {
            int srcPos = i+1;
            Well sWell = Well.convertVPosToWell(srcPos, rownum);
            ProgrammappingTO p = new ProgrammappingTO();
            p.setSrcplate(srcContainer);
            p.setSrcpos(srcPos);
            p.setSrcwellx(sWell.getX());
            p.setSrcwelly(sWell.getY());
            p.setDestplate(destContainer);
            p.setDestpos(srcPos);
            p.setDestwellx(sWell.getX());
            p.setDestwelly(sWell.getY());
            mappings.add(p);
        }
        this.srclabels.add(srcContainer);
        this.destlabels.add(destContainer);
    }
    
    public int getRownum() {
        return rownum;
    }
    
    public void setRownum(int rownum) {
        this.rownum = rownum;
    }
    
    public int getColnum() {
        return colnum;
    }
    
    public void setColnum(int colnum) {
        this.colnum = colnum;
    }
}