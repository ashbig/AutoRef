/*
 * GlycerolAndSeqContainerMapper.java
 *
 * Created on January 26, 2004, 3:27 PM
 */

package edu.harvard.med.hip.flex.process;

import java.util.*;
import edu.harvard.med.hip.flex.workflow.*;
import edu.harvard.med.hip.flex.process.ProcessProtocol;
import edu.harvard.med.hip.flex.database.FlexDatabaseException;
import edu.harvard.med.hip.flex.core.Container;

/**
 *
 * @author  DZuo
 */
public class GlycerolAndSeqContainerMapper implements ContainerMapper {
    protected OneToOneContainerMapper glycerolContainerMapper;
    protected MultiSeqContainerMapper seqContainerMapper;
    
    /** Creates a new instance of GlycerolAndSeqContainerMapper */
    public GlycerolAndSeqContainerMapper() {
        glycerolContainerMapper = new OneToOneContainerMapper();
        seqContainerMapper = new MultiSeqContainerMapper();
        seqContainerMapper.setIsCulture(true);
    }
    
    public void setRow(String row) {seqContainerMapper.setRow(row);}
    public void setIsMappingFile(boolean b) {
        seqContainerMapper.setIsMappingFile(b);
    }
    
    /** Creates new containers based on given containers and protocol.
     *
     * @param containers Source containers for mapping.
     * @param protocol The protocol for destination containers.
     * @param project The project used for mapping.
     * @param workflow The workflow related to the mapping container.
     * @return The new containers.
     * @exception FlexDatabaseException.
     *
     */
    public Vector doMapping(Vector containers, Protocol protocol, Project project, Workflow workflow) throws FlexDatabaseException {
        Vector newContainers = glycerolContainerMapper.doMapping(containers, protocol, project, workflow);
        newContainers.addAll(seqContainerMapper.doMapping(containers, protocol, project, workflow));
        return newContainers;
    }
    
    /** Return the container type based on process name.
     *
     * @param processname The name of the process protocol.
     * @return The container type for given process name.
     *
     */
    public String getContainerType(String processname) {
        return glycerolContainerMapper.getContainerType(processname);
    }
    
    /** Return the sample lineage.
     *
     * @return The sample lineage as a Vector.
     *
     */
    public Vector getSampleLineageSet() {
        Vector rt = glycerolContainerMapper.getSampleLineageSet();
        rt.addAll(seqContainerMapper.getSampleLineageSet());
        return rt;
    }
    
    public static void main(String [] args) {
        GlycerolAndSeqContainerMapper mapper = new GlycerolAndSeqContainerMapper();
        String containerType = mapper.getContainerType("Create rearrayed sequencing plates");
        
        if("96 WELL PLATE".equals(containerType))
            System.out.println("Testing static method getContainerType - OK");
        else
            System.out.println("Testing static method getContainerType - ERROR");
        
        try {
            Container c1 = new Container(6823);
            Container c2 = new Container(6824);
            Container c3 = new Container(6825);
            Container c4 = new Container(6826);
            //Container c5 = new Container(7096, null, null, null);
            //Container c6 = new Container(7097, null, null, null);
            //Container c7 = new Container(7098, null, null, null);
            //Container c8 = new Container(7099, null, null, null);
            Vector containers = new Vector();
            containers.add(c1);
            containers.add(c2);
            containers.add(c4);
            containers.add(c3);
            //containers.add(c5);
            //containers.add(c6);
            //containers.add(c7);
            //containers.add(c8);
            
            Project project = new Project(Project.YP);
            Workflow workflow = new Workflow(Workflow.GATEWAY_WORKFLOW);
            Protocol protocol = new Protocol(Protocol.CREATE_GLYCEROL_FROM_CULTURE);
            mapper.setRow("bf");
            Vector newContainers = mapper.doMapping(containers, protocol, project, workflow);
            System.out.println("=======================new containers========================");
            for(int i=0; i<newContainers.size(); i++) {
                Container c = (Container)newContainers.get(i);
                System.out.println(c.getLabel());
            }
            Vector sl = mapper.getSampleLineageSet();
            System.out.println("num of lineages: "+sl.size());
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
