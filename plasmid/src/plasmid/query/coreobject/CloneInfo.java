/*
 * CloneInfo.java
 *
 * Created on May 4, 2005, 4:35 PM
 */

package plasmid.query.coreobject;

import plasmid.coreobject.Clone;

/**
 *
 * @author  DZuo
 */
public class CloneInfo extends Clone {
    protected String term;     
    protected int quantity;
    protected String plate;
    protected String well;
    protected int position;
    protected boolean isAddedToCart;
    
    public CloneInfo(int cloneid, String name, String type, String verified,
    String vermethod, String domain, String subdomain, String restriction, 
    String comments, int vectorid, String vectorname, String clonemap, String status)  {
        super(cloneid,name, type,verified,vermethod,domain,subdomain, restriction,comments,vectorid, vectorname,clonemap,status);
    }
    
    public CloneInfo(String term, Clone c) {
        super(c.getCloneid(), c.getName(), c.getType(), c.getVerified(),c.getVermethod(), c.getDomain(),c.getSubdomain(),c.getRestriction(), c.getComments(),c.getVectorid(),c.getVectorname(), c.getClonemap(), c.getStatus());
        this.term = term;
        super.setAuthors(c.getAuthors());
        super.setGrowths(c.getGrowths());
        super.setHosts(c.getHosts());
        super.setInserts(c.getInserts());
        super.setNames(c.getNames());
        super.setPublications(c.getPublications());
        super.setSelections(c.getSelections());
    }
    
    public CloneInfo(Clone c) {
        super(c.getCloneid(), c.getName(), c.getType(), c.getVerified(),c.getVermethod(), c.getDomain(),c.getSubdomain(),c.getRestriction(), c.getComments(),c.getVectorid(),c.getVectorname(), c.getClonemap(), c.getStatus());
        super.setAuthors(c.getAuthors());
        super.setGrowths(c.getGrowths());
        super.setHosts(c.getHosts());
        super.setInserts(c.getInserts());
        super.setNames(c.getNames());
        super.setPublications(c.getPublications());
        super.setSelections(c.getSelections());
    }
        
    public String getTerm() {return term;}
    public int getQuantity() {return quantity;}
    public String getPlate() {return plate;}
    public String getWell() {return well;}
    public int getPosition() {return position;}
    public boolean getIsAddedToCart() {return isAddedToCart;}
    
    public void setTerm(String term) {this.term = term;}
    public void setQuantity(int i) {this.quantity = i;}
    public void setPlate(String s) {this.plate = s;}
    public void setWell(String s) {this.well = s;}
    public void setWell(String x, String y) {this.well = x+y;}
    public void setPosition(int i) {this.position = i;}
    public void setIsAddedToCart(boolean b) {this.isAddedToCart = b;}
}
