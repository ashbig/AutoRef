/* $Id: StaticQueueFactory.java,v 1.2 2001-04-25 18:37:59 dongmei_zuo Exp $ 
 *
 * File     : StaticQueueFactory.java 
 * Date     : 04182001
 * Author	: Dongmei Zuo
 */

package flex.ApplicationCode.Java.process;

import java.lang.reflect.Constructor;

/**
 * Implements QueueFactory interface to define the methods to 
 * construct the Queue objects correponding to a specific type.
 */
public class StaticQueueFactory implements QueueFactory {
	/**
	 * Constructs a Queue object corresponding to a specific type.
	 *
	 * @param type A string representation for queue type.
	 *
	 * @return A Queue object corresponding to a specific type.
	 *
	 * @exception FlexProcessException This exception is thrown when the
	 * 		Queue type is not found.
	 */
	public Queue makeQueue (String type) throws FlexProcessException {
		try {
    			Class classs = Class.forName ("flex.ApplicationCode.Java.process." + type);
      		Constructor constructor = classs.getConstructor (null);
      
      		return (Queue) constructor.newInstance (null);
    		} catch (Exception e) {
      		throw new FlexProcessException ("Unable to load queue " + type);
    		}
	}
}


