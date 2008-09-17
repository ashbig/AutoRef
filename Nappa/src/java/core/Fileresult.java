/*
 * Fileresult.java
 *
 * Created on December 5, 2007, 10:12 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package core;

/**
 *
 * @author dzuo
 */
public class Fileresult {
    private int row;
    private int col;
    private int pos;
    private String read;
    
    /** Creates a new instance of Fileresult */
    public Fileresult() {
    }
    
    public Fileresult(int row, int col, String read) {
        this.setRow(row);
        this.setCol(col);
        this.setRead(read);
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int rownum) {
        this.pos = Well.convertWellToVPos(row,col,rownum);
    }
}
