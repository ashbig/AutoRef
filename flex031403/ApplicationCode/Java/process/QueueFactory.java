/* $Id: QueueFactory.java,v 1.2 2001-04-25 18:37:59 dongmei_zuo Exp $ 
 *
 * File     : QueueFactory.java 
 * Date     : 04182001
 * Author	: Dongmei Zuo
 */

package flex.ApplicationCode.Java.process;

/**
 * QueueFactory defines the interface to make Queue object.
 */
public interface QueueFactory {
	/**
	 * Construct a new Queue object.
	 *
	 * @param s Protocol used to make the concrete Queue object.
	 * 
	 * @return A Queue object.
	 *
	 * @exception FlexProcessException
	 */
	public Queue makeQueue (String s) throws FlexProcessException;
}
