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
    public static final String ALL = "all";
    public static final String A = "a";
    public static final String B = "b";
    public static final String C = "c";
    public static final String D = "d";
    public static final String E = "e";
    public static final String F = "f";
    public static final String G = "g";
    public static final String H = "h";
    
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
        if(AE.equals(row)) {
            return mapRows(project, workflow, protocol, containers, threadid, "AE", 2, 1);
        } else if(BF.equals(row)) {
            return mapRows(project, workflow, protocol, containers, threadid, "BF", 2, 2);
        } else if(BOTH.equals(row)) {
            Vector newContainers = mapRows(project, workflow, protocol, containers, threadid, "AE", 2, 1);
            newContainers.addAll(mapRows(project, workflow, protocol, containers, threadid, "BF", 2, 2));
            return newContainers;
        } else if(A.equals(row)) {
            return mapRows(project, workflow, protocol, containers, threadid, "SA", 1, 1);
        } else if(B.equals(row)) {
            return mapRows(project, workflow, protocol, containers, threadid, "SB", 1, 2);
        } else if(C.equals(row)) {
            return mapRows(project, workflow, protocol, containers, threadid, "SC", 1, 3);
        } else if(D.equals(row)) {
            return mapRows(project, workflow, protocol, containers, threadid, "SD", 1, 4);
        } else if(E.equals(row)) {
            return mapRows(project, workflow, protocol, containers, threadid, "SE", 1, 5);
        } else if(F.equals(row)) {
            return mapRows(project, workflow, protocol, containers, threadid, "SF", 1, 6);
        } else if(G.equals(row)) {
            return mapRows(project, workflow, protocol, containers, threadid, "SG", 1, 7);
        } else if(H.equals(row)) {
            return mapRows(project, workflow, protocol, containers, threadid, "SH", 1, 8);
        } else {
            Vector newContainers = mapRows(project, workflow, protocol, containers, threadid, "SA", 1, 1);
            newContainers.addAll(mapRows(project, workflow, protocol, containers, threadid, "SB", 1, 2));
            newContainers.addAll(mapRows(project, workflow, protocol, containers, threadid, "SC", 1, 3));
            newContainers.addAll(mapRows(project, workflow, protocol, containers, threadid, "SD", 1, 4));
            newContainers.addAll(mapRows(project, workflow, protocol, containers, threadid, "SE", 1, 5));
            newContainers.addAll(mapRows(project, workflow, protocol, containers, threadid, "SF", 1, 6));
            newContainers.addAll(mapRows(project, workflow, protocol, containers, threadid, "SG", 1, 7));
            newContainers.addAll(mapRows(project, workflow, protocol, containers, threadid, "SH", 1, 8));
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
    
    public Vector mapRows(Project project, Workflow workflow, Protocol protocol, Vector containers, int threadid, String processCode, int numOfRows, int rowNum) throws FlexDatabaseException {
        SeqContainerMapper mapper = new SeqContainerMapper();
        mapper.setRows(numOfRows);
        mapper.setRow(rowNum);
        mapper.setProcesscode(processCode);
        mapper.setThreadid(threadid);
        mapper.setIsCulture(isCulture);
        mapper.setIsMappingFile(isMappingFile);
        Vector newContainers = mapper.doMapping(containers, protocol, project, workflow);
        sampleLineageSet.addAll(mapper.getSampleLineageSet());
        return newContainers;
    }
}
