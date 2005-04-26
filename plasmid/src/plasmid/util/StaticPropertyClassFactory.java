/*
 * StaticPropertyClassFactory.java
 *
 * Created on July 9, 2001, 6:06 PM
 */

package plasmid.util;

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
     * Constructs a FlexProperties object corresponding to a specific type.
     *
     * @param type A string representation for FlexProperties type.
     *
     * @return A FlexProperties object corresponding to a specific type.
     */
    public static FlexProperties makePropertyClass(String type) {
        if("FlexProperties".equals(type)) {
            return FlexProperties.getInstance();
        } else {
            return null;
        }
    }
}


