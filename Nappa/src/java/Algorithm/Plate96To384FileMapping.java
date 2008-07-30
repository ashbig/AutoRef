/*
 * Plate96To384FileMapping.java
 *
 * Created on July 12, 2007, 3:19 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package Algorithm;

import io.Plate96To384ProgramMappingFileParser;
import io.ProgramMappingFileParser;
import java.io.InputStream;

/**
 *
 * @author dzuo
 */
public class Plate96To384FileMapping extends FileMapping {
    
    /** Creates a new instance of Plate96To384FileMapping */
    public Plate96To384FileMapping() {
    }
    
     public Plate96To384FileMapping(InputStream f) {
        super(f);
    }
    
    public ProgramMappingFileParser getMappingFileParser() {
        return new Plate96To384ProgramMappingFileParser();
    }
}
