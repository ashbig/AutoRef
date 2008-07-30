/*
 * Plate384ToGalFileMapping.java
 *
 * Created on May 1, 2007, 10:55 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package Algorithm;

import io.Plate384ToSlideGalFileParser;
import io.ProgramMappingFileParser;
import java.io.InputStream;

/**
 *
 * @author dzuo
 */
public class Plate384ToGalFileMapping extends FileMapping {
    
    /** Creates a new instance of Plate384ToGalFileMapping */
    public Plate384ToGalFileMapping() {
    }
    
    public Plate384ToGalFileMapping(InputStream f) {
        super(f);
    }
    
    public ProgramMappingFileParser getMappingFileParser() {
        return new Plate384ToSlideGalFileParser();
    }
    
    public boolean getIsNumber() {
        return false;
    }
}
