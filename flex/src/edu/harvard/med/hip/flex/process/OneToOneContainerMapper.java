/*
 * OneToOneContainerMapper.java
 *
 * Created on June 19, 2001, 12:41 PM
 *
 * This class handles one to one container mapping.
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
 * @author  dzuo
 * @version
 */
public class OneToOneContainerMapper implements ContainerMapper {
    public static final String DAUGHTER_OLIGO_PLATE = "D";
    
    protected FlexProperties containerType = null;
    protected Vector sampleLineageSet = new Vector();
    
    /**
     * Constructor.
     *
     * @return The ContainerMapper object.
     */
    public OneToOneContainerMapper() {
        containerType = StaticPropertyClassFactory.makePropertyClass("ContainerTypeProperties");
    }
    
    /**
     * Return the sample lineage set.
     *
     * @return The sample lineage set.
     */
    public Vector getSampleLineageSet() {
        return sampleLineageSet;
    }
    
    /**
     * Returns the type based on the process name.
     *
     * @param processname The name of the processname.
     * @return The container type.
     */
    public String getContainerType(String processname) {
        return (containerType.getProperty(processname));
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
        
        Enumeration enum = containers.elements();
        while (enum.hasMoreElements()) {
            Container container = (Container)enum.nextElement();
            String newBarcode = null;
            
            //For diluting oligo plate, we need to get the new label in a different way.
            if(Protocol.DILUTE_OLIGO_PLATE.equals(protocol.getProcessname())) {
                newBarcode = projectCode+DAUGHTER_OLIGO_PLATE+container.getLabel().substring(2);
            } else if(Protocol.CREATE_CULTURE_FROM_MGC.equals(protocol.getProcessname()) ||
            Protocol.CREATE_GLYCEROL_FROM_CULTURE.equals(protocol.getProcessname()) ||
            Protocol.CREATE_DNA_FROM_MGC_CULTURE.equals(protocol.getProcessname())) {
                newBarcode = projectCode+protocol.getProcesscode()+container.getLabel().substring(3);
            } else {
                newBarcode = Container.getLabel(projectCode, protocol.getProcesscode(), container.getThreadid(), getSubThread(container));
            }
            
            Container newContainer = new Container(newContainerType, null, newBarcode, container.getThreadid());
            getSamples(container);
            mappingSamples(container, newContainer, protocol);
            newContainers.addElement(newContainer);
        }
        
        return newContainers;
    }
    
    // Parse the barcode to get the sub thread.
    protected String getSubThread(Container c) {
        String barcode = c.getLabel();
        int index = barcode.indexOf("-");
        
        if(index<0) {
            index = barcode.indexOf(".");
            if(index < 0) {
                return null;
            }
        }
        
        return (barcode.substring(index));
    }
    
    // Creates the new samples from the samples of the previous plate.
    protected void mappingSamples(Container container, Container newContainer, Protocol protocol) throws FlexDatabaseException {
        String type;
        Vector oldSamples = container.getSamples();
        for (int i=0; i<oldSamples.size(); i++) {
            Sample s = (Sample)oldSamples.get(i);
            /**
             * char [] c = s.getType().toCharArray();
             * switch(c[0]) {
             * case 'C':
             * case 'E':
             * case 'O':
             * if(c[1]=='R')
             * type = Sample.getType(protocol.getProcessname());
             * else
             * type = s.getType();
             * break;
             * case 'G':
             * type = getGelSampleType(container, s, protocol);
             * break;
             * case 'I':
             * type = getCultureSampleType(container, s, protocol);
             *
             * if(type == null)
             * type = Sample.getType(protocol.getProcessname());
             * break;
             * default:
             * type = Sample.getType(protocol.getProcessname());
             * }
             **/
            
            if(Sample.CONTROL_POSITIVE.equals(s.getType())) {
                type = Sample.CONTROL_POSITIVE;
            } else if(Sample.CONTROL_NEGATIVE.equals(s.getType())) {
                type = Sample.CONTROL_NEGATIVE;
            } else if(Sample.EMPTY.equals(s.getType())) {
                type = Sample.EMPTY;
            } else if(Sample.GEL.equals(s.getType())) {
                type = getGelSampleType(container, s, protocol);
            } else if(Sample.ISOLATE.equals(s.getType())) {
                type = getCultureSampleType(container, s, protocol);
                
                if(type == null) {
                    type = Sample.getType(protocol.getProcessname());
                }
            } else if(Protocol.DILUTE_OLIGO_PLATE.equals(protocol.getProcessname())) {
                type = s.getType();
            } else {
                type = Sample.getType(protocol.getProcessname());
            }
            
            Sample newSample = new Sample(type, s.getPosition(), newContainer.getId(), s.getConstructid(), s.getOligoid(), Sample.GOOD);
            newSample.setCloneid(s.getCloneid());
            newContainer.addSample(newSample);
            sampleLineageSet.addElement(new SampleLineage(s.getId(), newSample.getId()));
        }
    }
    
    protected String getGelSampleType(Container container, Sample s, Protocol newProtocol) throws FlexDatabaseException {
        Protocol protocol = new Protocol(Protocol.ENTER_PCR_GEL_RESULTS);
        String type = null;
        edu.harvard.med.hip.flex.process.Process p =
        edu.harvard.med.hip.flex.process.Process.findCompleteProcess(container, protocol);
        Result result = Result.findResult(s, p);
        if(Result.CORRECT.equals(result.getValue()) ||
        Result.MUL_W_CORRECT.equals(result.getValue()) ||
        Result.NO_BAND.equals(result.getValue())) {
            type = Sample.getType(newProtocol.getProcessname());
        } else {
            type = Sample.EMPTY;
        }
        
        return type;
    }
    
    protected String getCultureSampleType(Container container, Sample s, Protocol newProtocol) throws FlexDatabaseException {
        Protocol protocol = new Protocol(Protocol.ENTER_CULTURE_RESULTS);
        String type = null;
        edu.harvard.med.hip.flex.process.Process p =
        edu.harvard.med.hip.flex.process.Process.findCompleteProcess(container, protocol);
        
        if(p == null) {
            return null;
        }
        
        Result result = Result.findResult(s, p);
        if(Result.GROW.equals(result.getValue())) {
            type = Sample.getType(newProtocol.getProcessname());
        } else {
            type = Sample.EMPTY;
        }
        
        return type;
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
        container.restoreSampleWithoutSeq();
    }
    
    public static void main(String [] args) {
        ContainerMapper mapper = new OneToOneContainerMapper();
        String containerType = mapper.getContainerType("generate DNA plates");
        
        if("96 WELL PLATE".equals(containerType))
            System.out.println("Testing static method getContainerType - OK");
        else
            System.out.println("Testing static method getContainerType - ERROR");
    }
}
