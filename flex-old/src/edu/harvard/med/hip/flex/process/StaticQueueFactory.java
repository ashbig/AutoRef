/* $Id: StaticQueueFactory.java,v 1.2 2001-07-09 16:02:40 jmunoz Exp $ 
 *
 * File     : StaticQueueFactory.java 
 * Date     : 04182001
 * Author	: Dongmei Zuo
 */

package edu.harvard.med.hip.flex.process;

import java.lang.reflect.Constructor;

/**
 * Implements QueueFactory interface to define the methods to 
 * construct the ProcessQueue objects correponding to a specific type.
 */
public class StaticQueueFactory implements QueueFactory {
	/**
	 * Constructs a ProcessQueue object corresponding to a specific type.
	 *
	 * @param type A string representation for ProcessQueue type.
	 *
	 * @return A ProcessQueue object corresponding to a specific type.
	 *
	 * @exception FlexProcessException This exception is thrown when the
	 * 		ProcessQueue type is not found.
	 */
	public ProcessQueue makeQueue (String type) throws FlexProcessException {
		try {
    			Class classs = Class.forName ("edu.harvard.med.hip.flex.process." + type);
      			Constructor constructor = classs.getConstructor (null);
      
      			return (ProcessQueue) constructor.newInstance (null);
    		} catch (Exception e) {
      			throw new FlexProcessException ("Unable to load ProcessQueue " + type);
    		}
	}
}


