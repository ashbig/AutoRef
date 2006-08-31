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
    protected int colonynum;
    
    public int getColonynum() {return colonynum;}
    public void setColonynum(int i) {this.colonynum = i;}
    
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
            //String newBarcode = Container.getLabel(projectCode, protocol.getProcesscode(), f1.getThreadid(), subthread);
            int l = f1.getLabel().indexOf("-");
            String leftString = "";
            String threadid;
            if(l<0)
                threadid = f1.getLabel().substring(3);
            else {
                threadid = f1.getLabel().substring(3, l);
                leftString = f1.getLabel().substring(l);
                if(leftString.equals("-F") || leftString.equals("-C"))
                    leftString = "";
            }
            
            String newBarcode = projectCode+protocol.getProcesscode()+threadid+leftString+subthread;
            Container newContainer = new Container(newContainerType, null, newBarcode, f1.getThreadid());
            
            int index = 0;
            Enumeration enu = containers.elements();
            boolean addNewContainer = false;
            while(enu.hasMoreElements()) {
                Container c = (Container)enu.nextElement();
                c.restoreSample();
                
                //addNewContainer = true;
                if(c.getSamples().size()*colonynum - i*getRow()*getColumn()/getPlatenumOnDest() > 0) {
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
        String type [] = new String[colonynum];
        Vector oldSamples = container.getSamples();
        
        int column = getColumn();
        int row = getRow();
        int platenumOnDest = getPlatenumOnDest();
        int n=platenum*row*column/platenumOnDest/colonynum;
        while(n*colonynum<(platenum+1)*row*column/platenumOnDest) {
            Sample s = null;
            try {
                s = container.getSample(n+1);
            } catch (FlexCoreException ex) {
                System.out.println(ex);
            }
            
            if(s != null) {
                type = getType(container, s, protocol);
                
                for(int i=0; i<colonynum; i++) {
                    Sample newSample = new Sample(type[i], index+i, newContainer.getId(), s.getConstructid(), s.getOligoid(), Sample.GOOD);
                    newSample.setCloneid(s.getCloneid());
                    newContainer.addSample(newSample);
                    sampleLineageSet.addElement(new SampleLineage(s.getId(), newSample.getId()));
                }
            }
            index = index+colonynum*getPlatenumOnDest();
            n++;
        }
    }
    
    protected String[] getType(Container container, Sample s, Protocol newProtocol) throws FlexDatabaseException {
        String type [] = new String[colonynum];
        
        if(Sample.CONTROL_POSITIVE.equals(s.getType())) {
            for(int i=0; i<colonynum; i++) {
                type[i] = Sample.CONTROL_POSITIVE;
            }
        } else if(Sample.CONTROL_NEGATIVE.equals(s.getType())) {
            for(int i=0; i<colonynum; i++) {
                type[i] = Sample.CONTROL_NEGATIVE;
            }
        } else if(Sample.EMPTY.equals(s.getType())) {
            for(int i=0; i<colonynum; i++) {
                type[i] = Sample.EMPTY;
            }
        } else {
            type = getAgarSampleType(container, s, newProtocol);
        }
        
        return type;
    }
    
    protected String[] getAgarSampleType(Container container, Sample s, Protocol newProtocol) throws FlexDatabaseException {
        Protocol protocol = new Protocol(Protocol.ENTER_AGAR_PLATE_RESULTS);
        String type [] = new String[colonynum];
        edu.harvard.med.hip.flex.process.Process p =
        edu.harvard.med.hip.flex.process.Process.findCompleteProcess(container, protocol);
        Result result = null;
        try {
            result = Result.findResult(s, p);
        } catch (NullPointerException ex) {}
        
        if(result == null) {
            for(int i=0; i<colonynum; i++) {
                type[i] = Sample.getType(newProtocol.getProcessname());
            }
            return type;
        }
        
        try {
            int colony = Integer.parseInt(result.getValue());
            
            if(colony >= colonynum) {
                for(int i=0; i<colonynum; i++) {
                    type[i] = Sample.getType(newProtocol.getProcessname());
                }
            } else {
                for(int i=0; i<colony; i++) {
                    type[i] = Sample.getType(newProtocol.getProcessname());
                }
                
                for(int i=colony; i<colonynum; i++) {
                    type[i] = Sample.EMPTY;
                }
            }
        } catch (NumberFormatException ex) {
            for(int i=0; i<colonynum; i++) {
                type[i] = Sample.EMPTY;
            }
        }
        
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
