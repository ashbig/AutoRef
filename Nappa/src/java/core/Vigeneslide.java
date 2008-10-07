/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package core;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DZuo
 */
public class Vigeneslide {
    private String date;
    private int mainRow;
    private int mainCol;
    private int subRow;
    private int subCol;
    
    private List<Spotinfo> spots;

    public Vigeneslide() {
        setSpots(new ArrayList<Spotinfo>());
    }
    
    public Vigeneslide(String date, int mainrow, int maincol, int subrow, int subcol) {
        setDate(date);
        setMainRow(mainrow);
        setMainCol(maincol);
        setSubRow(subrow);
        setSubCol(subcol);
        setSpots(new ArrayList<Spotinfo>());
    }
    
    public void addSpotinfo(Spotinfo info) {
        this.spots.add(info);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
