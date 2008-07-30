/*
 * MappingAlgorithm.java
 *
 * Created on May 1, 2007, 10:43 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package Algorithm;

import transfer.ProgrammappingTO;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author dzuo
 */
public abstract class MappingAlgorithm {
    protected Collection<String> srclabels;
    protected Collection<String> destlabels;
    protected Collection<ProgrammappingTO> mappings;
    protected boolean isNumber;
    
    /** Creates a new instance of MappingAlgorithm */
    public MappingAlgorithm() {
        this.srclabels = new ArrayList();
        this.destlabels = new ArrayList();
    }

    public Collection<String> getDestlabels() {
        return destlabels;
    }

    public Collection<String> getSrclabels() {
        return srclabels;
    }

    public void setSrclabels(Collection<String> srclabels) {
        this.srclabels = srclabels;
    }
    
    abstract public void doMapping() throws AlgorithmException;

    public Collection<ProgrammappingTO> getMappings() {
        return mappings;
    }

    public void setMappings(Collection<ProgrammappingTO> mappings) {
        this.mappings = mappings;
    }

    public void setDestlabels(Collection<String> destlabels) {
        this.destlabels = destlabels;
    }

    public boolean getIsNumber() {
        return isNumber;
    }

    public void setIsNumber(boolean isNumber) {
        this.isNumber = isNumber;
    }
}
