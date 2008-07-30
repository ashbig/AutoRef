/*
 * AbstractProgramMappingFileParser.java
 *
 * Created on March 2, 2007, 4:19 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package io;

import transfer.ProgrammappingTO;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author DZuo
 */
public abstract class AbstractProgramMappingFileParser implements ProgramMappingFileParser {
    protected Collection<String> srcContainers;
    protected Collection<String> destContainers;
    
    /**
     * Creates a new instance of AbstractProgramMappingFileParser
     */
    public AbstractProgramMappingFileParser() {
        setSrcContainers(new ArrayList<String>());
        setDestContainers(new ArrayList<String>());
    }
    
    //public abstract ContainerheadermapTO parseMappingFile(InputStream in, boolean isNumber) throws ProgramMappingFileParserException;
    public abstract List<ProgrammappingTO> parseMappingFile(InputStream in, boolean isNumber) throws ProgramMappingFileParserException;
    
    public abstract String getSrccontainertype();
    public abstract String getDestcontainertype();

    public void addToContainers(Collection<String> containers, String c) {
        boolean isnew = true;
        for(String container:containers) {
            if(container.equals(c)) {
                isnew = false;
            }
        }
        if(isnew)
            containers.add(c);
    }
    
    public void setSrcContainers(Collection<String> srcContainers) {
        this.srcContainers = srcContainers;
    }

    public Collection<String> getDestContainers() {
        return destContainers;
    }

    public void setDestContainers(Collection<String> destContainers) {
        this.destContainers = destContainers;
    }

    public Collection<String> getSrcContainers() {
        return srcContainers;
    }
}
