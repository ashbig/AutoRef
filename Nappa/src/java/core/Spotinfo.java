/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

/**
 *
 * @author DZuo
 */
public class Spotinfo {
    private int mainRow;
    private int mainCol;
    private int subRow;
    private int subCol;
    private String geneID;
    private double meannet;
    private double meantotal;
    private int meanbkg;
    private int bkgused;
    private int meandust;
    private int mediannet;
    private int mediantotal;
    private int medianbkg;
    private int volnet;
    private int voltotal;
    private int volbkg;
    private int voldust;
    private double cvspot;
    private double cvbkg;
    private int dustiness;
    private double spotskew;
    private double bkgskew;
    private double kurtosis;
    private int rank;
    private int type;
    private int xcenter;
    private int ycenter;
    private int areasignal;
    private int areaspot;
    private int areabkg;
    private int solidity;
    private double circularity;
    private double roundness;
    private double aspect;

    public Spotinfo() {}
    
    public Spotinfo(int MainRow, int MainCol, int SubRow, int SubCol, String GeneID, double meannet, double meantotal, int meanbkg, int bkgused, int meandust, int mediannet, int mediantotal, int medianbkg, int volnet, int voltotal, int volbkg, int voldust, double cvspot, double cvbkg, int dustiness, double spotskew, double bkgskew, double kurtosis, int rank, int type, int xcenter, int ycenter, int areasignal, int areaspot, int areabkg, int solidity, double circularity, double roundness, double aspect) {
        setMainRow(MainRow);
        setMainCol(MainCol);
        setSubRow(SubRow);
        setSubCol(SubCol);
        setGeneID(GeneID);
        setMeannet(meannet);
        setMeantotal(meantotal);
        setMeanbkg(meanbkg);
        setBkgused(bkgused);
        setMeandust(meandust);
        setMediannet(mediannet);
        setMediantotal(mediantotal);
        setMedianbkg(medianbkg);
        setVolnet(volnet);
        setVoltotal(voltotal);
        setVolbkg(volbkg);
        setVoldust(voldust);
        setCvspot(cvspot);
        setCvbkg(cvbkg);
        setDustiness(dustiness);
        setSpotskew(spotskew);
        setBkgskew(bkgskew);
        setKurtosis(kurtosis);
        setRank(rank);
        setType(type);
        setXcenter(xcenter);
        setYcenter(ycenter);
        setAreasignal(areasignal);
        setAreaspot(areaspot);
        setAreabkg(areabkg);
        setSolidity(solidity);
        setCircularity(circularity);
        setRoundness(roundness);
        setAspect(aspect);
    }

    public int getMainRow() {
        return mainRow;
    }

    public void setMainRow(int MainRow) {
        this.mainRow = MainRow;
    }

    public int getMainCol() {
        return mainCol;
    }

    public void setMainCol(int MainCol) {
        this.mainCol = MainCol;
    }

    public int getSubRow() {
        return subRow;
    }

    public void setSubRow(int SubRow) {
        this.subRow = SubRow;
    }

    public int getSubCol() {
        return subCol;
    }

    public void setSubCol(int SubCol) {
        this.subCol = SubCol;
    }

    public String getGeneID() {
        return geneID;
    }

    public void setGeneID(String GeneID) {
        this.geneID = GeneID;
    }

    public double getMeannet() {
        return meannet;
    }

    public void setMeannet(double meannet) {
        this.meannet = meannet;
    }

    public double getMeantotal() {
        return meantotal;
    }

    public void setMeantotal(double meantotal) {
        this.meantotal = meantotal;
    }

    public int getMeanbkg() {
        return meanbkg;
    }

    public void setMeanbkg(int meanbkg) {
        this.meanbkg = meanbkg;
    }

    public int getBkgused() {
        return bkgused;
    }

    public void setBkgused(int bkgused) {
        this.bkgused = bkgused;
    }

    public int getMeandust() {
        return meandust;
    }

    public void setMeandust(int meandust) {
        this.meandust = meandust;
    }

    public int getMediannet() {
        return mediannet;
    }

    public void setMediannet(int mediannet) {
        this.mediannet = mediannet;
    }

    public int getMediantotal() {
        return mediantotal;
    }

    public void setMediantotal(int mediantotal) {
        this.mediantotal = mediantotal;
    }

    public int getMedianbkg() {
        return medianbkg;
    }

    public void setMedianbkg(int medianbkg) {
        this.medianbkg = medianbkg;
    }

    public int getVolnet() {
        return volnet;
    }

    public void setVolnet(int volnet) {
        this.volnet = volnet;
    }

    public int getVoltotal() {
        return voltotal;
    }

    public void setVoltotal(int voltotal) {
        this.voltotal = voltotal;
    }

    public int getVolbkg() {
        return volbkg;
    }

    public void setVolbkg(int volbkg) {
        this.volbkg = volbkg;
    }

    public int getVoldust() {
        return voldust;
    }

    public void setVoldust(int voldust) {
        this.voldust = voldust;
    }

    public double getCvspot() {
        return cvspot;
    }

    public void setCvspot(double cvspot) {
        this.cvspot = cvspot;
    }

    public double getCvbkg() {
        return cvbkg;
    }

    public void setCvbkg(double cvbkg) {
        this.cvbkg = cvbkg;
    }

    public int getDustiness() {
        return dustiness;
    }

    public void setDustiness(int dustiness) {
        this.dustiness = dustiness;
    }

    public double getSpotskew() {
        return spotskew;
    }

    public void setSpotskew(double spotskew) {
        this.spotskew = spotskew;
    }

    public double getBkgskew() {
        return bkgskew;
    }

    public void setBkgskew(double bkgskew) {
        this.bkgskew = bkgskew;
    }

    public double getKurtosis() {
        return kurtosis;
    }

    public void setKurtosis(double kurtosis) {
        this.kurtosis = kurtosis;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getXcenter() {
        return xcenter;
    }

    public void setXcenter(int xcenter) {
        this.xcenter = xcenter;
    }

    public int getYcenter() {
        return ycenter;
    }

    public void setYcenter(int ycenter) {
        this.ycenter = ycenter;
    }

    public int getAreasignal() {
        return areasignal;
    }

    public void setAreasignal(int areasignal) {
        this.areasignal = areasignal;
    }

    public int getAreaspot() {
        return areaspot;
    }

    public void setAreaspot(int areaspot) {
        this.areaspot = areaspot;
    }

    public int getAreabkg() {
        return areabkg;
    }

    public void setAreabkg(int areabkg) {
        this.areabkg = areabkg;
    }

    public int getSolidity() {
        return solidity;
    }

    public void setSolidity(int solidity) {
        this.solidity = solidity;
    }

    public double getCircularity() {
        return circularity;
    }

    public void setCircularity(double circularity) {
        this.circularity = circularity;
    }

    public double getRoundness() {
        return roundness;
    }

    public void setRoundness(double roundness) {
        this.roundness = roundness;
    }

    public double getAspect() {
        return aspect;
    }

    public void setAspect(double aspect) {
        this.aspect = aspect;
    }
}
