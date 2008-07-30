/*
 * SlidecellTO.java
 *
 * Created on March 22, 2007, 10:21 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package transfer;

import core.Well;
import java.io.Serializable;

/**
 *
 * @author dzuo
 */
public class SlidecellTO extends ContainercellTO implements Serializable {
    private int blocknum;
    private int blockx;
    private int blocky;
    private int blockposx;
    private int blockposy;
    private int blockwellx;
    private int blockwelly;

    /**
     * Creates a new instance of SlidecellTO
     */
    public SlidecellTO() {
    }
    
    public SlidecellTO(int pos, String x, String y, String type, int containerid, int sampleid,
            int blocknum,int blockx,int blocky,int blockposx,int blockposy,int blockwellx,int blockwelly) {
        super(pos,x,y,type,containerid,sampleid);
        this.blocknum = blocknum;
        this.blockx = blockx;
        this.blocky = blocky;
        this.blockposx = blockposx;
        this.blockposy = blockposy;
        this.blockwellx = blockwellx;
        this.blockwelly = blockwelly;
    }

    public SlidecellTO(int pos, String x, String y, String type, int blocknum,int blockx,int blocky,int blockposx,int blockposy,int blockwellx,int blockwelly) {
        super(pos,x,y,type);
        this.blocknum = blocknum;
        this.blockx = blockx;
        this.blocky = blocky;
        this.blockposx = blockposx;
        this.blockposy = blockposy;
        this.blockwellx = blockwellx;
        this.blockwelly = blockwelly;
    }
    
    public int getBlocknum() {
        return blocknum;
    }

    public void setBlocknum(int blocknum) {
        this.blocknum = blocknum;
    }

    public int getBlockx() {
        return blockx;
    }

    public void setBlockx(int blockx) {
        this.blockx = blockx;
    }

    public int getBlocky() {
        return blocky;
    }

    public void setBlocky(int blocky) {
        this.blocky = blocky;
    }

    public int getBlockposx() {
        return blockposx;
    }

    public void setBlockposx(int blockposx) {
        this.blockposx = blockposx;
    }

    public int getBlockposy() {
        return blockposy;
    }

    public void setBlockposy(int blockposy) {
        this.blockposy = blockposy;
    }

    public int getBlockwellx() {
        return blockwellx;
    }

    public void setBlockwellx(int blockwellx) {
        this.blockwellx = blockwellx;
    }

    public int getBlockwelly() {
        return blockwelly;
    }

    public void setBlockwelly(int blockwelly) {
        this.blockwelly = blockwelly;
    }
    
    public void calculateDestAbsolutePositions(int blockrownum, int blockcolnum, int rownum) {
        int x = blockwellx+(blockx-1)*blockrownum;
        int y = blockwelly+(blocky-1)*blockcolnum;
        this.setPosx(""+x);
        this.setPosy(""+y);
        this.setPos(Well.convertWellToVPos(x, y, rownum));
    }
}
