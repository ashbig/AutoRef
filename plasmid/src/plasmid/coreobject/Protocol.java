/*
 * Protocol.java
 *
 * Created on August 9, 2005, 1:36 PM
 */

package plasmid.coreobject;

/**
 *
 * @author  DZuo
 */
public class Protocol {
    private String name;
    private String filename;
    private String description;
    
    /** Creates a new instance of Protocol */
    public Protocol() {
    }
    
    public Protocol(String name, String filename, String description) {
        this.name = name;
        this.filename = filename;
        this.description = description;
    }
    
    public String getName() {return name;}
    public String getFilename() {return filename;}
    public String getDescription() {return description;}
    
    public void setName(String s) {this.name = s;}
    public void setFilename(String s) {this.filename = s;}
    public void setDescription(String s) {this.description = s;}
}
