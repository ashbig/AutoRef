/*
 * CollectionInfo.java
 *
 * Created on November 3, 2005, 2:53 PM
 */

package plasmid.coreobject;

import java.util.*;

/**
 *
 * @author  DZuo
 */
public class CollectionInfo {
    public static final String DISTRIBUTION = "Distribution";
    
    private String name;
    private String description;
    private double price;
    private String status;
    private String restriction;
    
    private List protocols;
    private List authors;
    private List publications;
    
    /** Creates a new instance of CollectionInfo */
    public CollectionInfo() {
    }
    
    public CollectionInfo(String name, String desc, double price, String status, String restriction) {
        this.name = name;
        this.description = desc;
        this.price = price;
        this.status = status;
        this.restriction = restriction;
    }
    
    public String getName() {return name;}
    public String getDescription() {return description;}
    public double getPrice() {return price;}
    public String getStatus() {return status;}
    public String getRestriction() {return restriction;}
    
    public void setName(String s) {this.name = s;}
    public void setDescription(String s) {this.description = s;}
    public void setPrice(double d) {this.price = d;}
    public void setStatus(String s) {this.status = s;}
    public void setRestriction(String s) {this.restriction = s;}
    
    public List getProtocols() {return protocols;}
    public void setProtocols(List l) {this.protocols = l;}
    
    public List getAuthors() {return authors;}
    public void setAuthors(List l) {this.authors = l;}
    
    public List getPublications() {return publications;}
    public void setPublications(List l) {this.publications = l;}
}
