/**
 * $Id: ContainerFactory.java,v 1.1 2003-03-07 17:44:51 dzuo Exp $
 *
 * File     	: ContainerFactory.java
 * Date     	: 04162001
 * Author	: Dongmei Zuo
 */

package edu.harvard.med.hip.flex.core;

/**
 * ContainerFactory defines the interface for all container makers.
 */
public interface ContainerFactory {
    /**
     * Constructs a Container object.
     *
     * @param type A string representation for container type.
     *
     * @return A Container object.
     * @exception FlexCoreException This exception is thrown when the
     * 		container type is not found.
     */
    public Container getContainer(String type) throws FlexCoreException;
}