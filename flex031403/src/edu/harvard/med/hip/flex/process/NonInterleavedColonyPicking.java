/*
 * NonInterleavedColonyPicking.java
 *
 * Created on August 19, 2002, 5:05 PM
 */

package edu.harvard.med.hip.flex.process;

import java.util.*;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.workflow.*;

/**
 *
 * @author  dzuo
 * @version
 */
public class NonInterleavedColonyPicking extends GridPlateToCultureMapper {
    protected int platenumOnDest = 1;
    
    /**
     * Constructor.
     *
     * @return The ContainerMapper object.
     * @exception FlexProcessException.
     */
    public NonInterleavedColonyPicking() throws FlexProcessException {
        super();
    }
    
    protected int getPlatenumOnDest() {
        return platenumOnDest;
    }
    
    public Vector doMapping(Vector containers, Protocol protocol, Project project, Workflow workflow) throws FlexDatabaseException {
        Vector newContainers = new Vector();
        String newContainerType = getContainerType(protocol.getProcessname());
        String projectCode = getProjectCode(project, workflow);
        
        Enumeration enum = containers.elements();
        int subThreadIndex = 1;
        while(enum.hasMoreElements()) {
            Container c = (Container)enum.nextElement();
            c.restoreSample();
            
            
            
            int index = 0;
            while(c.getSamples().size()*COLONYNUM - index*getRow()*getColumn()/getPlatenumOnDest() > 0) {
                String subthread = getSubThread(c, subThreadIndex);
                String newBarcode = Container.getLabel(projectCode, protocol.getProcesscode(), c.getThreadid(), subthread);
                Container newContainer = new Container(newContainerType, null, newBarcode, c.getThreadid());
                mappingSamples(c, newContainer, protocol, 0, index);
                newContainers.addElement(newContainer);
                index++;
                subThreadIndex++;
            }
            
        }
        
        return newContainers;
    }
    
}
