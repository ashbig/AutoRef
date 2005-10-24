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
    
    /** Creates a new instance of StaticMappingCalculatorFactory */
    public StaticMappingCalculatorFactory() {
    }
    
    public static MappingCalculator generateMappingCalculator(String type, List src, List dest, String sType) {
        if(DIRECT_MAPPING.equals(type))
            return new DirectMappingCalculator(src, dest, sType);
        
        return null;
    }
}
