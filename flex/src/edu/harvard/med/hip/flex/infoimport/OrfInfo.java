/*
 * OrfInfo.java
 *
 * Created on January 23, 2007, 10:50 AM
 */

package edu.harvard.med.hip.flex.infoimport;

/**
 *
 * @author  DZuo
 */
public class OrfInfo {
    public static final String NULLVALUE = "NULL";
    public static final String WITHSTOP = "with";
    public static final String WITHOUTSTOP = "without";
    
    private String imageid;
    private String collectionName;
    private String plate;
    private String row;
    private String col;
    private String library;
    private String cloneAcc;
    private String parentImageid;
    private String refAcc;
    private String symbol;
    private String species;
    private String vector;
    private String linker5p;
    private String linker3p;
    private String insertDigest;
    private String stop;
    
    /** Creates a new instance of OrfInfo */
    public OrfInfo() {
    }
    
    public OrfInfo(String imageid, String collectionName, String plate, String row, String col,
    String library, String cloneAcc, String parentImageid, String refAcc, String symbol,
    String species, String vector, String linker5p, String linker3p, String insertDigest, String stop) {
        this.imageid = imageid;
        this.collectionName = collectionName;
        this.plate = plate;
        this.row = row;
        this.col = col;
        this.library = library;
        this.cloneAcc = cloneAcc;
        this.parentImageid = parentImageid;
        this.refAcc = refAcc;
        this.symbol = symbol;
        this.species = species;
        this.vector = vector;
        this.linker5p = linker5p;
        this.linker3p = linker3p;
        this.insertDigest = insertDigest;
        this.stop = stop;
    }
    
    public String getImageid() {return imageid;}
    public String getCollectionName() {return collectionName;}
    public String getPlate() {return plate;}
    public String getRow() {return row;}
    public String getCol() {return col;}
    public String getLibrary() {return library;}
    public String getCloneAcc() {return cloneAcc;}
    public String getParentImageid() {return parentImageid;}
    public String getRefAcc() {return refAcc;}
    public String getSymbol() {return symbol;}
    public String getSpecies() {return species;}
    public String getVector() {return vector;}
    public String getLinker5p() {return linker5p;}
    public String getLinker3p() {return linker3p;}
    public String getInsertDigest() {return insertDigest;}
    public String getStop() {return stop;}
    
    public void setImageid(String s) {this.imageid = s;}
    public void setCollectionName(String s) {this.collectionName = s;}
    public void setPlate(String s) {this.plate = s;}
    public void setRow(String s) {this.row = s;}
    public void setCol(String s) {this.col = s;}
    public void setLibrary(String s) {this.library =s;}
    public void setCloneAcc(String s) {this.cloneAcc = s;}
    public void setParentImageid(String s) {this.parentImageid = s;}
    public void setRefAcc(String s) {this.refAcc = s;}
    public void setSymbol(String s) {this.symbol = s;}
    public void setSpecies(String s) {this.species = s;}
    public void setVector(String s) {this.vector = s;}
    public void setLinker5p(String s) {this.linker5p = s;}
    public void setLinker3p(String s) {this.linker3p = s;}
    public void setInsertDigest(String s) {this.insertDigest = s;}
    public void setStop(String s) {this.stop = s;}
   
}
