/*
 * AbstractAgarToCultureMapper.java
 *
 * Abstract class to map agar plate to culture plate.
 *
 * Created on November 8, 2001, 12:34 PM
 */

package edu.harvard.med.hip.flex.process;

import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.workflow.*;
import java.util.*;

/**
 *
 * @author  dzuo
 * @version
 */
public abstract class AbstractAgarToCultureMapper extends OneToOneContainerMapper {
    public static final int COLONYNUM = 4;
    
    /**
     * Constructor.
     *
     * @return The ContainerMapper object.
     * @exception FlexProcessException.
     */
    public AbstractAgarToCultureMapper() throws FlexProcessException {
        super();
    }
    
    /**
     * Creates new containers based on given containers and protocol.
     *
     * @param containers Source containers for mapping.
     * @param protocol The protocol for destination containers.
     * @return The new containers.
     * @exception FlexDatabaseException, FlexCoreException
     */
    public Vector doMapping(Vector containers, Protocol protocol, Project project, Workflow workflow) throws FlexDatabaseException {
        Vector newContainers = new Vector();
        String newContainerType = getContainerType(protocol.getProcessname());
        String projectCode = getProjectCode(project, workflow);
        Container f1 = (Container)containers.elementAt(0);
        
        for(int i=0; i<getNumOfDestPlates(); i++) {
            String subthread = getSubThread(f1, i+1);
            String newBarcode = Container.getLabel(projectCode, protocol.getProcesscode(), f1.getThreadid(), subthread);
            Container newContainer = new Container(newContainerType, null, newBarcode, f1.getThreadid());
            
            int index = 0;
            Enumeration enum = containers.elements();
            boolean addNewContainer = false;
            while(enum.hasMoreElements()) {
                Container c = (Container)enum.nextElement();
                c.restoreSample();
                
                //addNewContainer = true;
                if(c.getSamples().size()*COLONYNUM - i*getRow()*getColumn()/getPlatenumOnDest() > 0) {
                    addNewContainer = true;
                    mappingSamples(c, newContainer, protocol, getStartIndex(index), i);
                }
                index++;
            }
            
            if(addNewContainer)
                newContainers.addElement(newContainer);
        }
        
        return newContainers;
    }
    
    protected void mappingSamples(Container container, Container newContainer, Protocol protocol, int index, int platenum) throws FlexDatabaseException {
        String type [] = new String[COLONYNUM];
        Vector oldSamples = container.getSamples();
        
        int column = getColumn();
        int row = getRow();
        int platenumOnDest = getPlatenumOnDest();
        int n=platenum*row*column/platenumOnDest/COLONYNUM;
        while(n*COLONYNUM<(platenum+1)*row*column/platenumOnDest && n<oldSamples.size()) {
            Sample s = (Sample)oldSamples.elementAt(n);
            type = getType(container, s, protocol);
            
            for(int i=0; i<COLONYNUM; i++) {
                Sample newSample = new Sample(type[i], index+i, newContainer.getId(), s.getConstructid(), s.getOligoid(), Sample.GOOD);
                newContainer.addSample(newSample);
                sampleLineageSet.addElement(new SampleLineage(s.getId(), newSample.getId()));
            }
            index = index+COLONYNUM*getPlatenumOnDest();
            n++;
        }
    }
    
    protected String[] getType(Container container, Sample s, Protocol newProtocol) throws FlexDatabaseException {
        String type [] = new String[COLONYNUM];
        
        if(Sample.CONTROL_POSITIVE.equals(s.getType())) {
            for(int i=0; i<COLONYNUM; i++) {
                type[i] = Sample.CONTROL_POSITIVE;
            }
        } else if(Sample.CONTROL_NEGATIVE.equals(s.getType())) {
            for(int i=0; i<COLONYNUM; i++) {
                type[i] = Sample.CONTROL_NEGATIVE;
            }
        } else if(Sample.EMPTY.equals(s.getType())) {
            for(int i=0; i<COLONYNUM; i++) {
                type[i] = Sample.EMPTY;
            }
        } else {
            type = getAgarSampleType(container, s, newProtocol);
        }
        
        return type;
    }
    
    protected String[] getAgarSampleType(Container container, Sample s, Protocol newProtocol) throws FlexDatabaseException {
        Protocol protocol = new Protocol(Protocol.ENTER_AGAR_PLATE_RESULTS);
        String type [] = new String[COLONYNUM];
        edu.harvard.med.hip.flex.process.Process p =
        edu.harvard.med.hip.flex.process.Process.findCompleteProcess(container, protocol);
        Result result = Result.findResult(s, p);
        
        /**        if(Result.MORE.equals(result.getValue())) {
         * for(int i=0; i<COLONYNUM; i++) {
         * type[i] = Sample.getType(newProtocol.getProcessname());
         * }
         * } else {
         **/
        try {
            int colony = Integer.parseInt(result.getValue());
            
            if(colony == 0) {
                for(int i=0; i<COLONYNUM; i++) {
                    type[i] = Sample.EMPTY;
                }
            }
            if(colony == 1) {
                type[0] = Sample.getType(newProtocol.getProcessname());
                for(int i=1; i<COLONYNUM; i++) {
                    type[i] = Sample.EMPTY;
                }
            }
            if(colony == 2) {
                for(int i=0; i<2; i++) {
                    type[i] = Sample.getType(newProtocol.getProcessname());
                }
                for(int i=2; i<COLONYNUM; i++) {
                    type[i] = Sample.EMPTY;
                }
            }
            if(colony == 3) {
                for(int i=0; i<3; i++) {
                    type[i] = Sample.getType(newProtocol.getProcessname());
                }
                for(int i=3; i<COLONYNUM; i++) {
                    type[i] = Sample.EMPTY;
                }
            }
            if(colony >= 4) {
                for(int i=0; i<COLONYNUM; i++) {
                    type[i] = Sample.getType(newProtocol.getProcessname());
                }
            }
        } catch (NumberFormatException ex) {
            for(int i=0; i<COLONYNUM; i++) {
                type[i] = Sample.EMPTY;
            }
        }
        //    }
        
        return type;
    }
    
    // Return the subthreadid for culture plate from agar plate.
    abstract protected String getSubThread(Container c, int index);
    
    abstract protected int getNumOfDestPlates();
    
    abstract protected int getStartIndex(int index);
    
    abstract protected int getColumn();
    
    abstract protected int getRow();
    
    /**
     * Return the number of plates mapped to one destination plate.
     */
    abstract protected int getPlatenumOnDest();
}
