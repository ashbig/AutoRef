/*
 * $Id: QueueItem.java,v 1.2 2001-08-17 20:37:48 dzuo Exp $
 *
 * File     : QueueItem.java 
 * Date     : 04162001
 * Author	: Dongmei Zuo, Wendy Mar
 */ 

package edu.harvard.med.hip.flex.process;

import edu.harvard.med.hip.flex.workflow.*;

/**
 * This class represents an item on a queue.
 */
public class QueueItem {
    private Object item;
    private String date;
    private Protocol protocol;
    private Project project;
    private Workflow workflow;
    
    /**
     * Constructor.
     *
     * @param item The item object to be set to.
     * @param protocol The protocol to be set to.
     * @param date The date to be set to.
     * @param project The project to be set to.
     * @param workflow The workflow to be set to.
     *
     * @return A QueueItem object.
     */
    public QueueItem(Object item, Protocol protocol, String date, Project project, Workflow workflow) {
	this.item = item;
	this.date = date;
	this.protocol = protocol;
        this.project = project;
        this.workflow = workflow;
    }
    
    public QueueItem(Object item, Protocol protocol) {
	this.item = item;
	this.protocol = protocol;
    }    

    public QueueItem(Object item, Protocol protocol, String date) {
	this.item = item;
	this.date = date;
	this.protocol = protocol;
    }    
    
    /**
     * Constructor.
     *
     * @param item The item object to be set to.
     * @param protocol The protocol to be set to.
     * @param project The project to be set to.
     * @param workflow The workflow to be set to.
     *
     * @return A QueueItem object.
     */
    public QueueItem(Object item, Protocol protocol, Project project, Workflow workflow) {
	this.item = item;
	this.protocol = protocol;
        this.project = project;
        this.workflow = workflow;
    }
    
    /**
     * Return the item field of this object.
     *
     * @return The item field of this object.
     */
    public Object getItem() {
	return item;
    }
    
    /**
     * Return the protocol of this object.
     *
     * @return The protocol field of this object.
     */
    public Protocol getProtocol() {
	return protocol;
    }
    
    /**
     * Sets the protocol for this object
     *
     * @param protocol The protocol this object should reference
     */
    public void setProtocol(Protocol protocol) {
	this.protocol = protocol;
    }
    /**
     * Return the date field of this object.
     */
    public String getDate() {
	return date;
    }
    
    /**
     * Return the project associated with this queue item.
     * 
     * @return The project associated with this queue item.
     */
    public Project getProject() {
        return project;
    }
    
    /**
     * Set the project to be the given value.
     *
     * @param project The value to be set to.
     */
    public void setProject(Project project) {
        this.project = project;
    }
    
    /**
     * Return the workflow associated with this queue item.
     *
     * @return The workflow associated with this queue item.
     */
    public Workflow getWorkflow() {
        return workflow;
    }
    
    /**
     * Set the workflow to be the given value.
     *
     * @param workflow The value to be set to.
     */
    public void setWorkflow(Workflow workflow) {
        this.workflow = workflow;
    }
}


