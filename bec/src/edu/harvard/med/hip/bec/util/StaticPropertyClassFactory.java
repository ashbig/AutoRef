/*
 * StaticPropertyClassFactory.java
 *
 * Created on July 9, 2001, 6:06 PM
 */

package edu.harvard.med.hip.bec.util;

/**
 *
 * @author  dzuo
 * @version
 */

/**
 * Returns the instance of the subclass of SystemProperties class.
 */
public class StaticPropertyClassFactory {
    /**
     * Constructs a BecProperties object corresponding to a specific type.
     *
     * @param type A string representation for BecProperties type.
     *
     * @return A BecProperties object corresponding to a specific type.
     */
    public static BecProperties makePropertyClass(String type) {
        if("BecProperties".equals(type)) {
            return BecProperties.getInstance();
        } else if("ContainerTypeProperties".equals(type)) {
            return ContainerTypeProperties.getInstance();
        } else {
            return null;
        }
    }
}


