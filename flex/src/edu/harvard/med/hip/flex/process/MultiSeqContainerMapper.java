/*
 * MultiSeqContainerMapper.java
 *
 * Created on January 27, 2004, 10:42 AM
 */

package edu.harvard.med.hip.flex.process;

import java.util.*;
import edu.harvard.med.hip.flex.workflow.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.util.FlexIDGenerator;

/**
 *
 * @author  DZuo
 */
public class MultiSeqContainerMapper {
    public static final String AE = "ae";
    public static final String BF = "bf";
    public static final String BOTH = "both";
    
    private String row = BOTH;
    private Vector sampleLineageSet = null;
    private boolean isCulture = false;
    private boolean isMappingFile = false;
    
    public void setRow(String row) {this.row = row;}
    public void setIsCulture(boolean b) {isCulture=b;}
    public void setIsMappingFile(boolean b) {isMappingFile = b;}
    
    /** Creates a new instance of MultiSeqContainerMapper */
    public MultiSeqContainerMapper() {
        sampleLineageSet = new Vector();
    }
    
    /**
     * Creates new containers based on given containers and protocol.
     *
     * @param containers Source containers for mapping.
     * @param protocol The protocol for destination containers.
     * @return The new containers.
     * @exception FlexDatabaseException.
     */
    public Vector doMapping(Vector containers, Protocol protocol, Project project,
    Workflow workflow) throws FlexDatabaseException {
        int threadid;
        try {
            threadid = FlexIDGenerator.getID("threadid");
        } catch (Exception ex) {
            throw new FlexDatabaseException(ex);
        }
        if("ae".equals(row)) {
            return mapAE(project, workflow, protocol, containers, threadid);
        } else if("bf".equals(row)) {
            return mapBF(project, workflow, protocol, containers, threadid);
        } else {
            Vector newContainers = mapAE(project, workflow, protocol, containers, threadid);
            newContainers.addAll(mapBF(project, workflow, protocol, containers, threadid));
            return newContainers;
        }
    }
        
    /** Return the sample lineage.
     *
     * @return The sample lineage as a Vector.
     *
     */
    public Vector getSampleLineageSet() {
        return sampleLineageSet;
    }
    
    public Vector mapAE(Project project, Workflow workflow, Protocol protocol, Vector containers, int threadid) throws FlexDatabaseException {
        SeqContainerMapper mapper = new SeqContainerMapper();
        mapper.setRows(2);
        mapper.setRow(1);
        mapper.setProcesscode("AE");
        mapper.setThreadid(threadid);
        mapper.setIsCulture(isCulture);
        mapper.setIsMappingFile(isMappingFile);
        Vector newContainers = mapper.doMapping(containers, protocol, project, workflow);
        sampleLineageSet.addAll(mapper.getSampleLineageSet());
        return newContainers;
    }
    
    public Vector mapBF(Project project, Workflow workflow, Protocol protocol, Vector containers, int threadid) throws FlexDatabaseException {
        SeqContainerMapper mapper = new SeqContainerMapper();
        mapper.setRows(2);
        mapper.setRow(2);
        mapper.setProcesscode("BF");
        mapper.setThreadid(threadid);
        mapper.setIsCulture(isCulture);
        mapper.setIsMappingFile(isMappingFile);
        Vector newContainers = mapper.doMapping(containers, protocol, project, workflow);
        sampleLineageSet.addAll(mapper.getSampleLineageSet());
        return newContainers;
    }
}
