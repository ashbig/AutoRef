/*
 * NonInterleavedOneColonyPicking.java
 *
 * Created on March 14, 2005, 4:05 PM
 */

package edu.harvard.med.hip.flex.process;

import java.util.*;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.workflow.*;

/**
 *
 * @author  DZuo
 */
public class NonInterleavedOneColonyPicking extends NonInterleavedColonyPicking {
    
    /** Creates a new instance of NonInterleavedOneColonyPicking */
    public NonInterleavedOneColonyPicking() throws FlexProcessException {
        super();
    }
    
    public Vector doMapping(Vector containers, Protocol protocol, Project project, Workflow workflow) throws FlexDatabaseException {
        Vector newContainers = new Vector();
        String newContainerType = getContainerType(protocol.getProcessname());
        String projectCode = getProjectCode(project, workflow);
        
        Container container = (Container)containers.get(0);
        String leftString = "";
        int l = container.getLabel().indexOf("-");
        String threadid;
        if(l<0)
            threadid = container.getLabel().substring(3);
        else {
            threadid = container.getLabel().substring(3, l);
            leftString = container.getLabel().substring(l);
            if(leftString.equals("-F") || leftString.equals("-C"))
                leftString = "";
        }
        String newBarcode = projectCode+protocol.getProcesscode()+threadid+leftString+"-1";
        Container newContainer = new Container(newContainerType, null, newBarcode, container.getThreadid());
        int index = 1;
        Enumeration enum = containers.elements();
        while(enum.hasMoreElements()) {
            Container c = (Container)enum.nextElement();
            c.restoreSample();
            mappingSamples(c, newContainer, protocol, index, 0);
            index = 49;
        }
        
        newContainers.addElement(newContainer);
        return newContainers;
    }
}
