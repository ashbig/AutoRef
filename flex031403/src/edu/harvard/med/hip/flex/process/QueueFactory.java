/* $Id: QueueFactory.java,v 1.1 2001-05-23 15:40:06 dongmei_zuo Exp $ 
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
