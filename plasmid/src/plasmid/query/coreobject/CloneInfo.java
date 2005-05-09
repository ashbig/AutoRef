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
    
    public void setTerm(String term) {this.term = term;}
    public void setQuantity(int i) {this.quantity = i;}
}
