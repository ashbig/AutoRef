/*
 * $Id: QueueItem.java,v 1.2 2001-07-09 16:02:39 jmunoz Exp $
 *
 * File     : QueueItem.java 
 * Date     : 04162001
 * Author	: Dongmei Zuo, Wendy Mar
 */ 

package edu.harvard.med.hip.flex.process;

/**
 * This class represents an item on a queue.
 */
public class QueueItem {
    private Object item;
    private String date;
    private Protocol protocol;
    
    /**
     * Constructor.
     *
     * @param item The item object to be set to.
     * @param protocol The protocol to be set to.
     * @param date The date to be set to.
     *
     * @return A QueueItem object.
     */
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
     *
     * @return A QueueItem object.
     */
    public QueueItem(Object item, Protocol protocol) {
	this.item = item;
	this.protocol = protocol;
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
}


