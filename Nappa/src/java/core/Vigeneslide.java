/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package core;

import java.util.List;

/**
 *
 * @author DZuo
 */
public class Vigeneslide {
    private String version;
    private String date;
    private int roiRow;
    private int roiCol;
    private int mainRow;
    private int mainCol;
    private int subRow;
    private int subCol;
    
    private List<Spotinfo> spots;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getRoiRow() {
        return roiRow;
    }

    public void setRoiRow(int roiRow) {
        this.roiRow = roiRow;
    }

    public int getRoiCol() {
        return roiCol;
    }

    public void setRoiCol(int roiCol) {
        this.roiCol = roiCol;
    }

    public int getMainRow() {
        return mainRow;
    }

    public void setMainRow(int mainRow) {
        this.mainRow = mainRow;
    }

    public int getMainCol() {
        return mainCol;
    }

    public void setMainCol(int mainCol) {
        this.mainCol = mainCol;
    }

    public int getSubRow() {
        return subRow;
    }

    public void setSubRow(int subRow) {
        this.subRow = subRow;
    }

    public int getSubCol() {
        return subCol;
    }

    public void setSubCol(int subCol) {
        this.subCol = subCol;
    }

    public List<Spotinfo> getSpots() {
        return spots;
    }

    public void setSpots(List<Spotinfo> spots) {
        this.spots = spots;
    }
}
