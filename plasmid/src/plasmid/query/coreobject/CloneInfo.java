/*
 * CloneInfo.java
 *
 * Created on May 4, 2005, 4:35 PM
 */

package plasmid.query.coreobject;

import java.io.*;
import plasmid.coreobject.Clone;

/**
 *
 * @author  DZuo
 */
public class CloneInfo extends Clone implements Serializable{
    protected String term;     
    protected int quantity;
    protected String plate;
    protected String well;
    protected int position;
    protected boolean isAddedToCart;
    protected String targetPlate;
    protected String targetWell;
    
    //The below variables were created to store only plates OR only biobank tubes
    protected String latestPlate;
    protected String bbTube=null;
    protected String latestWell;
    protected int lposition;
    protected String primer;
    protected String samTube = null;
    
    public CloneInfo() {
        super();
    }
    
    public CloneInfo(int cloneid, String name, String type, String verified,
    String vermethod, String domain, String subdomain, String restriction, 
    String comments, int vectorid, String vectorname, String clonemap, String status,
    String specialtreatment, String source, String desc)  {
        super(cloneid,name, type,verified,vermethod,domain,subdomain, restriction,comments,vectorid, vectorname,clonemap,status,specialtreatment,source,desc);
    }
    
    public CloneInfo(String term, Clone c) {
        super(c.getCloneid(), c.getName(), c.getType(), c.getVerified(),c.getVermethod(), c.getDomain(),c.getSubdomain(),c.getRestriction(), c.getComments(),c.getVectorid(),c.getVectorname(), c.getClonemap(), c.getStatus(), c.getSpecialtreatment(), c.getSource(), c.getDescription());
        this.term = term;
        super.setAuthors(c.getAuthors());
        super.setGrowths(c.getGrowths());
        super.setHosts(c.getHosts());
        super.setInserts(c.getInserts());
        super.setNames(c.getNames());
        super.setPublications(c.getPublications());
        super.setSelections(c.getSelections());
        super.setRecommendedGrowthCondition(c.getRecommendedGrowthCondition());
        super.setOrderid(c.getOrderid());
    }
    
    public CloneInfo(Clone c) {
        super(c.getCloneid(), c.getName(), c.getType(), c.getVerified(),c.getVermethod(), c.getDomain(),c.getSubdomain(),c.getRestriction(), c.getComments(),c.getVectorid(),c.getVectorname(), c.getClonemap(), c.getStatus(), c.getSpecialtreatment(), c.getSource(), c.getDescription());
        super.setAuthors(c.getAuthors());
        super.setGrowths(c.getGrowths());
        super.setHosts(c.getHosts());
        super.setInserts(c.getInserts());
        super.setNames(c.getNames());
        super.setPublications(c.getPublications());
        super.setSelections(c.getSelections());
        super.setRecommendedGrowthCondition(c.getRecommendedGrowthCondition());
        super.setOrderid(c.getOrderid());
    }
        
    public String getTerm() {return term;}
    public int getQuantity() {return quantity;}
    public String getPlate() {return plate;}
    public String getWell() {return well;}
    public int getPosition() {return position;}
    public boolean getIsAddedToCart() {return isAddedToCart;}
    public String getTargetPlate() {return targetPlate;}
    public String getTargetWell() {return targetWell;}
    
    //getter methods to be called by DownloadCloneWorklistAction.java
    public String getLatestPlate() { return latestPlate; }
    public String getLatestWell() {return latestWell;}
    public String getBBTube() { return bbTube; }
    public String getPrimer() { return primer;};
    public String getSAMTube(){ return samTube;};

    
    public void setTerm(String term) {this.term = term;}
    public void setQuantity(int i) {this.quantity = i;}
    public void setPlate(String s) {this.plate = s;}
    public void setWell(String s) {this.well = s;}
    public void setWell(String x, String y) {this.well = x+y;}
    public void setPosition(int i) {this.position = i;}
    public void setIsAddedToCart(boolean b) {this.isAddedToCart = b;}    
    public void setTargetPlate(String s) {this.targetPlate = s;}
    public void setTargetWell(String s) {this.targetWell = s;}
    
    
    //setter methods to be called by DownloadCloneWorklistAction.java
    public void setLatestPlate( String lPlate ) { this.latestPlate = lPlate; }
    public void setLatestWell(String lWell) { this.latestWell =lWell;}
    public void setLatestWell(String x, String y) {this.latestWell = x+y;}
    public void setLatestPosition(int i) {this.lposition = i;}
    public void setBBTube( String bbTube) { this.bbTube = bbTube; }
    public void setPrimer ( String primer ) { this.primer = primer; }
    public void setSAMTube( String samTube) { this.samTube = samTube; }

    private void writeObject(java.io.ObjectOutputStream out)
     throws IOException {
         out.defaultWriteObject();        
    }
 
     private void readObject(java.io.ObjectInputStream in)
     throws IOException, ClassNotFoundException {
         in.defaultReadObject();
     }
}
