/*
 * File : ThreadElement.java
 * Classes : ThreadElement
 *
 * Description :
 *
 *    Insert description here.
 *
 *
 * Author : Juan Munoz (jmunoz@3rdmill.com)
 *
 * See COPYRIGHT file for copyright information
 *
 *
 * The following information is used by CVS
 * $Revision: 1.2 $
 * $Date: 2001-07-09 16:00:58 $
 * $Author: jmunoz $
 *
 ******************************************************************************
 *
 * Revision history (Started on June 29, 2001) :
 *
 *    Add entries here when updating the code. Remember to date and insert
 *    your 3 letters initials.
 *
 *    Jun-28-2001 : JMM - Class created.
 *
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */


package edu.harvard.med.hip.flex.core;

import edu.harvard.med.hip.flex.process.Process;

/**
 * Describes an element in a thread.
 *
 * An element in a thread is an object such as a container or sequence
 * along with the process.
 *
 * @author     $Author: jmunoz $
 * @version    $Revision: 1.2 $ $Date: 2001-07-09 16:00:58 $
 */

public class ThreadElement {
    // Object in the thread
    private Object object;
    
    // The execution for the container in a thread
    private Process process;
    
    // The thread this element  belongs to.
    private Thread thread;
    
    
    /**
     * Constructor that takes in a thread, process and container object.
     *
     * @param thread The Thread this element belongs to.
     * @param process The Process execution that is associated
     *      with this container thread element.
     * @param container The Container this thread element deals with.
     */
    public ThreadElement(Thread thread, Process process,Object object) {
        this.object = object;
        this.thread = thread;
        this.process = process;
    }
    
    
    /**
     * Accessor for the object in the thread.
     *
     * @return The container.
     */
    public Object getObject() {
        return this.object;
    }
    
    /**
     * Accessor for the thread;
     *
     * @The thread associated with this element.
     */
    public Thread getThread() {
        return this.thread;
    }
    
    /**
     * Accessor for the process.
     *
     * @return the process associated with the element.
     */
    public Process getProcess() {
        return this.process;
    }
    
} // End class ThreadElement


/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */
