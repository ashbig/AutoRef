/*
 * ProgramMappingFileParser.java
 *
 * Created on February 27, 2007, 2:11 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package io;

import transfer.ProgrammappingTO;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author DZuo
 */
public interface ProgramMappingFileParser {
    //ContainerheadermapTO parseMappingFile(InputStream in, boolean isNumber) throws ProgramMappingFileParserException;
    public List<ProgrammappingTO> parseMappingFile(InputStream in, boolean isNumber) throws ProgramMappingFileParserException;
    public Collection getDestContainers();
    public Collection getSrcContainers();
    public String getSrccontainertype();
    public String getDestcontainertype();
}
