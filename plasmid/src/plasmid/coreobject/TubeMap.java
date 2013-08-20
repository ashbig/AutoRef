/*
 * TubeMap.java
 *
 * Created on May 31, 2005, 2:39 PM
 */

package plasmid.coreobject;

import java.util.*;

/**
 *
 * @author  DZuo
 */
public class TubeMap {
    public static final String NOTUBE = "N/A";
    private String barcode;
    private Map mapping;
    
    /** Creates a new instance of TubeMap */
    public TubeMap() {
    }
    
    public TubeMap(String barcode, Map mapping) {
        this.barcode = barcode;
        this.mapping = mapping;
    }
    
    public String getBarcode() {return barcode;}
    public Map getMapping() {return mapping;}
    
    public void setBarcode(String s) {this.barcode = s;}
    public void setMapping(Map m) {this.mapping = m;}
}
