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
    public static final String CG = "cg";
    public static final String DH = "dh";
    public static final String BOTH = "both";
    public static final String BOTH_CG_DH = "both_cg_dh";
    public static final String ALL = "all";
    public static final String ONE = "one";
    public static final String A = "a";
    public static final String B = "b";
    public static final String C = "c";
    public static final String D = "d";
    public static final String E = "e";
    public static final String F = "f";
    public static final String G = "g";
    public static final String H = "h";
    
    private String row = BOTH;
    private   boolean isA = false;
    private   boolean isB = false;
    private   boolean isC = false;
    private   boolean isD = false;
    private   boolean isE = false;
    private   boolean isF = false;
    private   boolean isG = false;
    private   boolean isH = false;
    private Vector sampleLineageSet = null;
    private boolean isCulture = false;
    private boolean isMappingFile = false;
    
    public void setRow(String row) {this.row = row;}
    public void setIsCulture(boolean b) {isCulture=b;}
    public void setIsMappingFile(boolean b) {isMappingFile = b;}
    public void setIsA(boolean b) {this.isA = b;}
    public void setIsB(boolean b) {this.isB = b;}
    public void setIsC(boolean b) {this.isC = b;}
    public void setIsD(boolean b) {this.isD = b;}
    public void setIsE(boolean b) {this.isE = b;}
    public void setIsF(boolean b) {this.isF = b;}
    public void setIsG(boolean b) {this.isG = b;}
    public void setIsH(boolean b) {this.isH = b;}
    
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
        } else if(CG.equals(row)) {
            return mapRows(project, workflow, protocol, containers, threadid, "CG", 2, 3);
        } else if(DH.equals(row)) {
            return mapRows(project, workflow, protocol, containers, threadid, "DH", 2, 4);
        } else if(BOTH.equals(row)) {
            Vector newContainers = mapRows(project, workflow, protocol, containers, threadid, "AE", 2, 1);
            newContainers.addAll(mapRows(project, workflow, protocol, containers, threadid, "BF", 2, 2));
            return newContainers;
        } else if(BOTH_CG_DH.equals(row)) {
            Vector newContainers = mapRows(project, workflow, protocol, containers, threadid, "CG", 2, 3);
            newContainers.addAll(mapRows(project, workflow, protocol, containers, threadid, "DH", 2, 4));
            return newContainers;
        } else if(ALL.equals(row)) {
            Vector newContainers = mapRows(project, workflow, protocol, containers, threadid, "AE", 2, 1);
            newContainers.addAll(mapRows(project, workflow, protocol, containers, threadid, "BF", 2, 2));
            newContainers.addAll(mapRows(project, workflow, protocol, containers, threadid, "CG", 2, 3));
            newContainers.addAll(mapRows(project, workflow, protocol, containers, threadid, "DH", 2, 4));
            return newContainers;
        } else if(ONE.equals(row)) {
            Vector newContainers = new Vector();
            
            if(isA) {
                newContainers.addAll(mapRows(project, workflow, protocol, containers, threadid, "SA", 1, 1));
            }
            if(isB) {
                newContainers.addAll(mapRows(project, workflow, protocol, containers, threadid, "SB", 1, 2));
            }
            if(isC) {
                newContainers.addAll(mapRows(project, workflow, protocol, containers, threadid, "SC", 1, 3));
            }
            if(isD) {
                newContainers.addAll(mapRows(project, workflow, protocol, containers, threadid, "SD", 1, 4));
            }
            if(isE) {
                newContainers.addAll(mapRows(project, workflow, protocol, containers, threadid, "SE", 1, 5));
            }
            if(isF) {
                newContainers.addAll(mapRows(project, workflow, protocol, containers, threadid, "SF", 1, 6));
            }
            if(isG) {
                newContainers.addAll(mapRows(project, workflow, protocol, containers, threadid, "SG", 1, 7));
            }
            if(isH) {
                newContainers.addAll(mapRows(project, workflow, protocol, containers, threadid, "SH", 1, 8));
            }
            
            return newContainers;
        } else {
            return null;
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
