/*
 * StaticTaskManagerFactory.java
 *
 * This is a static class which is used to create the concrete TaskManager classes.
 *
 * Created on July 31, 2001, 11:49 AM
 */

package edu.harvard.med.hip.flex.workflow;

import java.lang.reflect.Constructor;

/**
 *
 * @author  dzuo
 * @version
 */
public class StaticTaskManagerFactory {
    
    /**
     * Create the concrete TaskManager object based on the task name provided.
     *
     * @param taskName The class name of the concrete TaskManager object.
     * @return The concrete TaskManager object. Return null if fails.
     * @exception FlexWorkflowException
     */
    public static TaskManager createTaskManager(String taskName) throws FlexWorkflowException {
        try {
            Class classs = Class.forName("edu.harvard.med.hip.flex.workflow." + taskName);
            Constructor constructor = classs.getConstructor(null);
            
            return (TaskManager) constructor.newInstance(null);
        } catch (Exception e) {
            throw new FlexWorkflowException("Cannot create TaskManager object with "+taskName);
        }        
    }
}
