/*
 * CollectionInfo.java
 *
 * Created on November 3, 2005, 2:53 PM
 */

package plasmid.coreobject;

import java.util.*;
import plasmid.Constants;

/**
 *
 * @author  DZuo
 */
public class CollectionInfo {
    public static final String DISTRIBUTION = "Distribution";
    public static final String DISPLAYPRICE = " upon request";
    
    public static final String FT_221 = "HIP FLEXGene Francisella tularensis ORF collection (pDONR221 vector)";
    public static final String YP_221 = "HIP FLEXGene Yersinia pestis ORF collection (pDONR221 vector)";
    public static final String VC_221 = "HIP FLEXGene Vibrio cholerae ORF collection (pDONR221 vector)";
    public static final String VC_EXP = "HIP FLEXGene Vibrio cholerae ORF collection (cell-free expression vector)";
    public static final String BA_EXP = "HIP FLEXGene Bacillus anthracis ORF collection (cell-free expression vector)";
    public static final String BA_221 = "HIP FLEXGene Bacillus anthracis ORF collection (pDONR221 vector)";
    public static final String FT_EXP = "HIP FLEXGene Francisella tularensis ORF collection (yeast expression vector with tags)";
    public static final String FT_EXP1 = "HIP FLEXGene Francisella tularensis ORF collection (E. coli expression vector)";
    public static final String PA_201 = "HIP FLEXGene Pseudomonas aeruginosa ORF collection (pDONR201 vector)";
    public static final String VC_EXP2 = "HIP FLEXGene Vibrio cholerae ORF collection (bacterial expression with C-terminal GST tag)";
    public static final String YPT_221 = "HIP FLEXGene Yersinia pseudotuberculosis ORF collection (pDONR221 vector)";
    
    private String name;
    private String description;
    private double price;
    private double memberprice;
    private String status;
    private String restriction;
    private double commercialprice;
    
    private List protocols;
    private List authors;
    private List publications;
    private List clones;
    
    private int quantity;
    
    /** Creates a new instance of CollectionInfo */
    public CollectionInfo() {
    }
    
    public CollectionInfo(String name, String desc, double price, double memberprice, String status, String restriction, double commercialprice) {
        this.name = name;
        this.description = desc;
        this.price = price;
        this.memberprice = memberprice;
        this.status = status;
        this.restriction = restriction;
        this.commercialprice = commercialprice;
    }
    
    public String getName() {return name;}
    public String getDescription() {return description;}
    public double getPrice() {return price;}
    public double getMemberprice() {return memberprice;}
    public String getStatus() {return status;}
    public String getRestriction() {return restriction;}
    public int getQuantity() {return quantity;}
    
    public void setName(String s) {this.name = s;}
    public void setDescription(String s) {this.description = s;}
    public void setPrice(double d) {this.price = d;}
    public void setMemberprice(double d) {this.memberprice = d;}
    public void setStatus(String s) {this.status = s;}
    public void setRestriction(String s) {this.restriction = s;}
    public void setQuantity(int i) {this.quantity = i;}
    
    public List getProtocols() {return protocols;}
    public void setProtocols(List l) {this.protocols = l;}
    
    public List getAuthors() {return authors;}
    public void setAuthors(List l) {this.authors = l;}
    
    public List getPublications() {return publications;}
    public void setPublications(List l) {this.publications = l;}
    
    public List getClones() {return clones;}
    public void setClones(List l) {this.clones = l;}
    
    public String getDisplayPrice() {
        if(price<-1)
            return DISPLAYPRICE;
        if(price<0)
            return Constants.NA;
        return "$"+price;
    }
    
    public String getDisplayMemberPrice() {
        if(price<-1)
            return DISPLAYPRICE;
        if(price<0)
            return Constants.NA;
        return "$"+memberprice;
    }
    
    public String getDisplayCommercialPrice() {
        if(price<-1)
            return DISPLAYPRICE;
        if(price<0)
            return Constants.NA;
        return "$"+commercialprice;
    }

    public double getCommercialprice() {
        return commercialprice;
    }

    public void setCommercialprice(double commercialprice) {
        this.commercialprice = commercialprice;
    }
}
