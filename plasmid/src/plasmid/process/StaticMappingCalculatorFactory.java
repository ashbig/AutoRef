/*
 * StaticMappingCalculatorFactory.java
 *
 * Created on May 26, 2005, 11:27 AM
 */

package plasmid.process;

import java.util.*;

/**
 *
 * @author  DZuo
 */
public class StaticMappingCalculatorFactory {
    public static final String DIRECT_MAPPING = "Direct Mapping";
    public static final String PLATE96_TO_384 = "Plate 94 to 384";
    
    /** Creates a new instance of StaticMappingCalculatorFactory */
    public StaticMappingCalculatorFactory() {
    }
    
    public static MappingCalculator generateMappingCalculator(String type, List src, List dest, String sType) {
        if(DIRECT_MAPPING.equals(type))
            return new DirectMappingCalculator(src, dest, sType);
        if(PLATE96_TO_384.equals(type))
            return new Plate96To384MappingCalculator(src, dest, sType);
        return null;
    }
}
