/*
 * SeqContainerMapper.java
 *
 * Created on January 20, 2004, 3:37 PM
 */

package edu.harvard.med.hip.flex.process;

import java.util.Properties;
import java.io.*;
import java.util.Vector;
import java.util.Enumeration;

import edu.harvard.med.hip.flex.process.FlexProcessException;
import edu.harvard.med.hip.flex.database.FlexDatabaseException;
import edu.harvard.med.hip.flex.process.Result;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.util.*;
import edu.harvard.med.hip.flex.workflow.*;

/**
 *
 * @author  DZuo
 */
public class SeqContainerMapper extends OneToOneContainerMapper {
    public static final String FILEPATH = FlexProperties.getInstance().getProperty("tmp");
    protected int rows = 2;
    protected int row = 1;
    protected int threadid;
    protected String processcode = "AE";
    protected int space = 4;
    protected int index = 1;
    protected int ROW = 8;
    protected int COL = 12;
    protected boolean isCulture = false;
    protected boolean isMappingFile = false;
    protected PrintWriter output = null;
    
    public void setRows(int rows) {this.rows = rows;}
    public void setRow(int row) {this.row = row;}
    public void setThreadid(int threadid) {this.threadid = threadid;}
    public void setProcesscode(String processcode) {this.processcode = processcode;}
    public void setIsCulture(boolean b) {this.isCulture = b;}
    public void setIsMappingFile(boolean b) {this.isMappingFile = b;}
    
    /** Creates a new instance of SeqContainerMapper */
    public SeqContainerMapper() {
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
        String newContainerType = getContainerType(protocol.getProcessname());
        Vector newContainers = new Vector();
        
        String projectCode = getProjectCode(project, workflow);
        String newBarcode = Container.getLabel(projectCode, processcode, threadid, null);
        Location location = new Location(Location.CODE_FREEZER, null, null);
        Container newContainer = new Container(newContainerType, location, newBarcode, threadid);
        
        if(isMappingFile) {
            try {
                output = new PrintWriter(new BufferedWriter(new FileWriter(FILEPATH+newContainer.getLabel())));
            } catch (IOException ex) {
                throw new FlexDatabaseException(ex);
            }
        }
        
        for(int i=0; i<containers.size(); i++) {
            Container container = (Container)containers.get(i);
            getSamples(container);
            mappingSamples(container, newContainer, protocol);
        }
        
        newContainers.addElement(newContainer);
        
        if(output != null)
            output.close();
        
        return newContainers;
    }
    
    // Creates the new samples from the samples of the previous plate.
    protected void mappingSamples(Container container, Container newContainer, Protocol protocol) throws FlexDatabaseException {
        String oldLabel = container.getLabel();
        String newLabel = newContainer.getLabel();
        
        for(int n=0; n<rows; n++) {
            int startRow = n*space+row;
            int startPos = index;
            for(int i=startRow-1; i<(COL-1)*ROW+startRow; i=i+ROW) {
                Sample s = null;
                try {
                    s = container.getSample(i+1);
                } catch (FlexCoreException ex) {
                    System.out.println(ex);
                }
                
                String type = s.getType();
                if(isCulture && type.equals("ISOLATE"))
                    type = getCultureSampleType(container, s, protocol);
                
                if(s != null) {
                    Sample newSample = new Sample(type, startPos, newContainer.getId(), s.getConstructid(), s.getOligoid(), Sample.GOOD);
                    newSample.setCloneid(s.getCloneid());
                    newContainer.addSample(newSample);
                    sampleLineageSet.addElement(new SampleLineage(s.getId(), newSample.getId()));
                }
                
                if(isMappingFile) {
                    output.println(oldLabel+"\t"+(i+1)+"\t"+newLabel+"\t"+startPos);
                }
                
                startPos = startPos+ROW;
            }
            index++;
        }
    }
    
    protected String getProjectCode(Project project, Workflow workflow) {
        String projectCode = "";
        Workflow wf = project.getWorkflow(workflow);
        if(wf != null) {
            projectCode = ((ProjectWorkflow)wf).getCode();
        }
        return projectCode;
    }
    
    protected void getSamples(Container container) throws FlexDatabaseException {
        container.restoreSample();
    }
    
    public static void main(String [] args) {
        SeqContainerMapper mapper = new SeqContainerMapper();
        String containerType = mapper.getContainerType("Create rearrayed sequencing plates");
        
        if("96 WELL PLATE".equals(containerType))
            System.out.println("Testing static method getContainerType - OK");
        else
            System.out.println("Testing static method getContainerType - ERROR");
        
        Container c1 = new Container(7150, null, null, null);
        Container c2 = new Container(7151, null, null, null);
        Container c3 = new Container(7152, null, null, null);
        Container c4 = new Container(7153, null, null, null);
        Container c5 = new Container(7096, null, null, null);
        Container c6 = new Container(7097, null, null, null);
        Container c7 = new Container(7098, null, null, null);
        Container c8 = new Container(7099, null, null, null);
        Vector containers = new Vector();
        containers.add(c1);
        containers.add(c2);
        containers.add(c4);
        containers.add(c3);
        containers.add(c5);
        containers.add(c6);
        containers.add(c7);
        containers.add(c8);
        
        try {
            Project project = new Project(Project.YP);
            Workflow workflow = new Workflow(Workflow.GATEWAY_WORKFLOW);
            Protocol protocol = new Protocol(Protocol.CREATE_SEQ_PLATES);
            mapper.setThreadid(123);
            mapper.setRows(1);
            mapper.setRow(5);
            mapper.setProcesscode("EG");
            Vector newContainers = mapper.doMapping(containers, protocol, project, workflow);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
