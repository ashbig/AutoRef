/*
 * ProcessPlateManager.java
 *
 * Created on July 31, 2001, 12:46 PM
 */

package edu.harvard.med.hip.flex.workflow;

import java.util.*;
import java.sql.*;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.process.Process;
import edu.harvard.med.hip.flex.database.*;

/**
 *
 * @author  dzuo
 * @version
 */
public class ProcessPlateManager extends AbstractTaskManager {
    
    /** Creates new ProcessPlateManager */
    public ProcessPlateManager() {
        super();
    }
 
    /**
     * Make the queue for added items.
     *
     * @return The ProcessQueue object.
     */
    public ProcessQueue makeAddedItemsQueue() {
        return new ContainerProcessQueue();
    }
    
    /**
     * Make the queue for removed items.
     *
     * @return The ProcessQueue object.
     */
    public ProcessQueue makeRemovedItemQueue() {
        return new ContainerProcessQueue();
    }
    
    public void addInputObjects(Process process, List objects) {
        String ioFlag = edu.harvard.med.hip.flex.process.ProcessObject.INPUT;
        addProcessObjects(process, objects, ioFlag);
    }
    
    public void addOutputObjects(Process process, List objects) {
        String ioFlag = edu.harvard.med.hip.flex.process.ProcessObject.OUTPUT;
        addProcessObjects(process, objects, ioFlag);
    }
    
    public void addIOObjects(Process process, List objects) {
        String ioFlag = edu.harvard.med.hip.flex.process.ProcessObject.IO;
        addProcessObjects(process, objects, ioFlag);        
    }
    
    protected void addProcessObjects(Process process, List objects, String ioFlag) {
        Iterator iter = objects.iterator();
        while(iter.hasNext()) {
            Container c = (Container)iter.next();
            ContainerProcessObject o = new ContainerProcessObject(c.getId(), ioFlag);
            process.addProcessObject(o);
        }        
    }        
}
