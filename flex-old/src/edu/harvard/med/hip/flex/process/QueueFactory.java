/* $Id: QueueFactory.java,v 1.2 2001-07-09 16:02:39 jmunoz Exp $ 
 *
 * File     : QueueFactory.java 
 * Date     : 04182001
 * Author	: Dongmei Zuo
 */

package edu.harvard.med.hip.flex.process;

/**
 * QueueFactory defines the interface to make ProcessQueue object.
 */
public interface QueueFactory {
	/**
	 * Construct a new ProcessQueue object.
	 *
	 * @param s Protocol used to make the concrete ProcessQueue object.
	 * 
	 * @return A ProcessQueue object.
	 *
	 * @exception FlexProcessException
	 */
	public ProcessQueue makeQueue (String s) throws FlexProcessException;
}
