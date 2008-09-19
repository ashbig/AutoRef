/*
 * ProgrammappingTO.java
 *
 * Created on May 1, 2007, 10:18 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package transfer;

import core.*;

/**
 *
 * @author dzuo
 */
public class ProgrammappingTO { 
    public static final int PLATE384_ROW = 16;
    public static final int PLATE384_COL = 24;
  
    private int mapid;
    private String programname;
    private String srcplate;
    private int srcpos;
    private String srcwellx;
    private String srcwelly;
    private String destplate;
    private int destpos;
    private String destwellx;
    private String destwelly;
    private int destblocknum;
    private int destblockrow;
    private int destblockcol;
    private int destblockwellx;
    private int destblockwelly;
    private int destblockposx;
    private int destblockposy;
    
    /** Creates a new instance of ProgrammappingTO */
    public ProgrammappingTO() {
    }

    public ProgrammappingTO(int mapid, String name, String srcplate, int srcpos, String srcwellx, String srcwelly, String destplate, int destpos,String destwellx,
            String destwelly, int destblocknum, int destblockrow, int destblockcol, int destblockposx, int destblockposy, int destblockwellx, int destblockwelly) {
        setMapid(mapid);
        setProgramname(name);
        setSrcplate(srcplate);
        setSrcpos(srcpos);
        setSrcwellx(srcwellx);
        setSrcwelly(srcwelly);
        setDestplate(destplate);
        setDestpos(destpos);
        setDestwellx(destwellx);
        setDestwelly(destwelly);
        setDestblocknum(destblocknum);
        setDestblockrow(destblockrow);
        setDestblockcol(destblockcol);
        setDestblockposx(destblockposx);
        setDestblockposy(destblockposy);
        setDestblockwellx(destblockwellx);
        setDestblockwelly(destblockwelly);
    }
    public String getSrcplate() {
        return srcplate;
    }

    public void setSrcplate(String srcplate) {
        this.srcplate = srcplate;
    }

    public int getSrcpos() {
        return srcpos;
    }

    public void setSrcpos(int srcpos) {
        this.srcpos = srcpos;
    }

    public String getSrcwellx() {
        return srcwellx;
    }

    public void setSrcwellx(String srcwellx) {
        this.srcwellx = srcwellx;
    }

    public String getSrcwelly() {
        return srcwelly;
    }

    public void setSrcwelly(String srcwelly) {
        this.srcwelly = srcwelly;
    }

    public String getDestplate() {
        return destplate;
    }

    public void setDestplate(String destplate) {
        this.destplate = destplate;
    }

    public int getDestpos() {
        return destpos;
    }

    public void setDestpos(int destpos) {
        this.destpos = destpos;
    }

    public String getDestwellx() {
        return destwellx;
    }

    public void setDestwellx(String destwellx) {
        this.destwellx = destwellx;
    }

    public String getDestwelly() {
        return destwelly;
    }

    public void setDestwelly(String destwelly) {
        this.destwelly = destwelly;
    }

    public int getDestblocknum() {
        return destblocknum;
    }

    public void setDestblocknum(int destblocknum) {
        this.destblocknum = destblocknum;
    }

    public int getDestblockrow() {
        return destblockrow;
    }

    public void setDestblockrow(int destblockrow) {
        this.destblockrow = destblockrow;
    }

    public int getDestblockcol() {
        return destblockcol;
    }

    public void setDestblockcol(int destblockcol) {
        this.destblockcol = destblockcol;
    }

    public int getDestblockwellx() {
        return destblockwellx;
    }

    public void setDestblockwellx(int destblockwellx) {
        this.destblockwellx = destblockwellx;
    }

    public int getDestblockwelly() {
        return destblockwelly;
    }

    public void setDestblockwelly(int destblockwelly) {
        this.destblockwelly = destblockwelly;
    }

    public void calculateDestAbsolutePositions(int blockrownum, int blockcolnum, int rownum) {
        int x = destblockwellx+(destblockrow-1)*blockrownum;
        int y = destblockwelly+(destblockcol-1)*blockcolnum;
        this.destwellx = ""+x;
        this.destwelly = ""+y;
        this.destpos = Well.convertWellToHPos(x, y, rownum*blockrownum);
    }

    public int getMapid() {
        return mapid;
    }

    public void setMapid(int mapid) {
        this.mapid = mapid;
    }

    public String getProgramname() {
        return programname;
    }

    public void setProgramname(String programname) {
        this.programname = programname;
    }

    public int getDestblockposx() {
        return destblockposx;
    }

    public void setDestblockposx(int destblockposx) {
        this.destblockposx = destblockposx;
    }

    public int getDestblockposy() {
        return destblockposy;
    }

    public void setDestblockposy(int destblockposy) {
        this.destblockposy = destblockposy;
    }
}
