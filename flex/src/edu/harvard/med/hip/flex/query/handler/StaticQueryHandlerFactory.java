/*
 * StaticQueryHandlerFactory.java
 *
 * Created on October 28, 2003, 4:21 PM
 */

package edu.harvard.med.hip.flex.query.handler;

import java.util.*;
import edu.harvard.med.hip.flex.query.core.*;

/**
 *
 * @author  DZuo
 */
public class StaticQueryHandlerFactory {
    
    /** Creates a new instance of StaticQueryHandlerFactory */
    public StaticQueryHandlerFactory() {
    }
    
    public static QueryHandler makeQueryHandler(String type, List params) {
        if(SearchRecord.GI.equals(type))
            return new GiBlastQueryHandler(params);
        if(SearchRecord.GENBANK.equals(type))
            return new GenbankBlastQueryHandler(params);
        
        return null;
    }
}
