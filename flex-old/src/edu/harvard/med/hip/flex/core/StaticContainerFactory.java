/**
 * $Id: StaticContainerFactory.java,v 1.2 2001-07-09 16:00:58 jmunoz Exp $
 *
 * File     	: StaticContainerFactory.java 
 * Date     	: 04162001
 * Author	: Dongmei Zuo
 */

package edu.harvard.med.hip.flex.core;

import java.lang.reflect.Constructor;

/**
 * Implements ContainerFactory interface to define the methods to 
 * construct the Container objects correponding to a specific type.
 */
public class StaticContainerFactory implements ContainerFactory {
	/**
	 * Constructs a Container object corresponding to a specific type.
	 *
	 * @param type A string representation for container type.
	 *
	 * @return A Container object corresponding to a specific type.
	 *
	 * @exception FlexCoreException This exception is thrown when the
	 * 		container type is not found.
	 */
	public Container getContainer (String type) throws FlexCoreException {
		try {
    			Class classs = Class.forName ("edu.harvard.med.hip.flex.core." + type);
      			Constructor constructor = classs.getConstructor (null);
      			return (Container)constructor.newInstance(null);
    		} catch (Exception e) {
      			throw new FlexCoreException ("Unable to load container " + type);
    		}
	}
}
