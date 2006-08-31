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
        
        Enumeration enu = containers.elements();
        int subThreadIndex = 1;
        int threadid = -1;
        while(enu.hasMoreElements()) {
            Container c = (Container)enu.nextElement();
            c.restoreSample();
            
            //If next plate has different threadid from the previouse plate,
            //we'll reset the subthreadid to 1.
            if(threadid == -1) {
                threadid = c.getThreadid();
            } else {
                if(threadid != c.getThreadid()) {
                    subThreadIndex = 1;
                }
            }
            
            int index = 0;
            while(c.getSamples().size()*getColonynum() - index*getRow()*getColumn()/getPlatenumOnDest() > 0) {
                String subthread = "-"+Integer.toString(subThreadIndex);
                String threadId;
                String leftString = "";
                int l = c.getLabel().indexOf("-");
                if(l<0)
                    threadId = c.getLabel().substring(3);
                else {
                    threadId = c.getLabel().substring(3, l);
                    leftString = c.getLabel().substring(l);
                    if(leftString.equals("-F") || leftString.equals("-C"))
                        leftString = "";
                }
                String newBarcode = projectCode+protocol.getProcesscode()+threadId+leftString+subthread;
                Container newContainer = new Container(newContainerType, null, newBarcode, c.getThreadid());
                mappingSamples(c, newContainer, protocol, 1, index);
                newContainers.addElement(newContainer);
                index++;
                subThreadIndex++;
            }
        }
        
        return newContainers;
    }
}
