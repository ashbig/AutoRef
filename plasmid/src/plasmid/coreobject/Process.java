/*
 * Process.java
 *
 * Created on August 9, 2005, 1:24 PM
 */

package plasmid.coreobject;

import java.util.*;

/**
 *
 * @author  DZuo
 */
public class Process {
    public static final String TRANSFORMATION = "Transformation";
    public static final String PLATING = "Plating";
    public static final String CULTURE = "Grow Culture";
    public static final String GENERATE_GLYCEROL = "Generate Glycerol Stock";
    public static final String ENTER_CULTURE_RESULT = "Enter Culture Results";
   
    private String processname;
    private String description;
    private List protocols;
    
    /** Creates a new instance of Process */
    public Process() {
    }
    
    public Process(String processname, String description, List protocols) {
        this.processname = processname;
        this.description = description;
        this.protocols = protocols;
    }
    
    public String getProcessname() {return processname;}
    public String getDescription() {return description;}
    public List getProtocols() {return protocols;}
    
    public void setProcessname(String s) {this.processname = s;}
    public void setDescription(String s) {this.description = s;}
    public void setProtocols(List l) {this.protocols = l;}
}
