/*
 * FileMapping.java
 *
 * Created on May 1, 2007, 10:46 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package Algorithm;

import io.AbstractProgramMappingFileParser;
import io.ProgramMappingFileParser;
import io.ProgramMappingFileParserException;
import java.io.InputStream;

/**
 *
 * @author dzuo
 */
public abstract class FileMapping extends MappingAlgorithm {
    private InputStream file;
    
    /** Creates a new instance of FileMapping */
    public FileMapping() {
    }
    
    public FileMapping(InputStream f) {
        super();
        this.setFile(f);
    }
    
    public InputStream getFile() {
        return file;
    }
    
    public void setFile(InputStream file) {
        this.file = file;
    }
    
    public void doMapping() throws AlgorithmException {
        try {
            ProgramMappingFileParser parser = getMappingFileParser();
            mappings = parser.parseMappingFile(getFile(), getIsNumber());
            this.setSrclabels(((AbstractProgramMappingFileParser)parser).getSrcContainers());
            this.setDestlabels(((AbstractProgramMappingFileParser)parser).getDestContainers());
        } catch (ProgramMappingFileParserException ex) {
            throw new AlgorithmException("Error occured while parsing the mapping file.");
        }
    }
    
    public abstract ProgramMappingFileParser getMappingFileParser();
}
