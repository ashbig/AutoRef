/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import transfer.MicrovigeneresultTO;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DZuo
 */
public class Vigeneslide {

    private String date;
    private String version;
    private int roiRowInFile;
    private int roiColInFile;
    private int mainRowInFile;
    private int mainColInFile;
    private int subRowInFile;
    private int subColInFile;
    private int mainRow;
    private int mainCol;
    private int subRow;
    private int subCol;
    private List<MicrovigeneresultTO> spots;

    public Vigeneslide() {
        setSpots(new ArrayList<MicrovigeneresultTO>());
    }

    public Vigeneslide(String version, String date, int roirow, int roicol, int mainrow, int maincol, int subrow, int subcol) {
        setVersion(version);
        setDate(date);
        setRoiRowInFile(roirow);
        setRoiColInFile(roicol);
        setMainRowInFile(mainrow);
        setMainColInFile(maincol);
        setSubRowInFile(subrow);
        setSubColInFile(subcol);

        if (roirow != 1 || roicol != 1) {
            subrow = mainrow;
            subcol = maincol;
            mainrow = roirow;
            maincol = roicol;
        }

        setMainRow(mainrow);
        setMainCol(maincol);
        setSubRow(subrow);
        setSubCol(subcol);
        setSpots(new ArrayList<MicrovigeneresultTO>());
    }

    public void addSpotinfo(MicrovigeneresultTO info) {
        this.spots.add(info);
    }

    public int calculatePosition(int spotmainrow, int spotmaincol, int spotsubrow, int spotsubcol) {
        return (spotmainrow - 1) * subRow * subCol * mainCol + (spotmaincol - 1) + (spotsubrow - 1) * subCol * mainCol + (spotmaincol - 1) * subCol + spotsubcol;
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

    public List<MicrovigeneresultTO> getSpots() {
        return spots;
    }

    public void setSpots(List<MicrovigeneresultTO> spots) {
        this.spots = spots;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getRoiRowInFile() {
        return roiRowInFile;
    }

    public void setRoiRowInFile(int roiRowInFile) {
        this.roiRowInFile = roiRowInFile;
    }

    public int getRoiColInFile() {
        return roiColInFile;
    }

    public void setRoiColInFile(int roiColInFile) {
        this.roiColInFile = roiColInFile;
    }

    public int getMainRowInFile() {
        return mainRowInFile;
    }

    public void setMainRowInFile(int mainRowInFile) {
        this.mainRowInFile = mainRowInFile;
    }

    public int getMainColInFile() {
        return mainColInFile;
    }

    public void setMainColInFile(int mainColInFile) {
        this.mainColInFile = mainColInFile;
    }

    public int getSubRowInFile() {
        return subRowInFile;
    }

    public void setSubRowInFile(int subRowInFile) {
        this.subRowInFile = subRowInFile;
    }

    public int getSubColInFile() {
        return subColInFile;
    }

    public void setSubColInFile(int subColInFile) {
        this.subColInFile = subColInFile;
    }
}
