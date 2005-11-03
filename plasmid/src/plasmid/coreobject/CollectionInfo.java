/*
 * CollectionInfo.java
 *
 * Created on November 3, 2005, 2:53 PM
 */

package plasmid.coreobject;

/**
 *
 * @author  DZuo
 */
public class CollectionInfo {
    public static final String DISTRIBUTION = "Distribution";
    
    private String name;
    private String description;
    private String protocolname;
    private String protocolfile;
    private double price;
    private String status;
    
    /** Creates a new instance of CollectionInfo */
    public CollectionInfo() {
    }
    
    public CollectionInfo(String name, String desc, String protocolname, String protocolfile, double price, String status) {
        this.name = name;
        this.description = desc;
        this.protocolname = protocolname;
        this.protocolfile = protocolfile;
        this.price = price;
        this.status = status;
    }
    
    public String getName() {return name;}
    public String getDescription() {return description;}
    public String getProtocolname() {return protocolname;}
    public String getProtocolfile() {return protocolfile;}
    public double getPrice() {return price;}
    public String getStatus() {return status;}
    
    public void setName(String s) {this.name = s;}
    public void setDescription(String s) {this.description = s;}
    public void setProtocolname(String s) {this.protocolname = s;}
    public void setProtocolfile(String s) {this.protocolfile = s;}
    public void setPrice(double d) {this.price = d;}
    public void setStatus(String s) {this.status = s;}
}
