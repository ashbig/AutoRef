/*
 * StaticQueryHandlerFactory.java
 *
 * Created on April 26, 2005, 11:52 AM
 */

package plasmid.query.handler;

import java.util.List;

/**
 *
 * @author  DZuo
 */
public class StaticQueryHandlerFactory {
    
    /** Creates a new instance of StaticQueryHandlerFactory */
    public StaticQueryHandlerFactory() {
    }
    
    public static GeneQueryHandler makeGeneQueryHandler(String type, List terms) {
        if(GeneQueryHandler.GENBANK.equals(type))
            return new GenbankQueryHandler(terms);
        else if(GeneQueryHandler.GENEID.equals(type))
            return new GeneidQueryHandler(terms);
        else if(GeneQueryHandler.SYMBOL.equals(type))
            return new GeneSymbolQueryHandler(terms);
        else if(GeneQueryHandler.DIRECT_GENBANK.equals(type))
            return new DirectGenbankQueryHandler(terms);
        else if(GeneQueryHandler.GI.equals(type))
            return new GiQueryHandler(terms);
        else if(GeneQueryHandler.DIRECT_GI.equals(type))
            return new DirectGiQueryHandler(terms);
        else if(GeneQueryHandler.PA.equals(type) || GeneQueryHandler.SGD.equals(type))
            return new PAQueryHandler(terms);
        else if(GeneQueryHandler.PRO_GENBANK.equals(type))
            return new PAGenbankQueryHandler(terms);
        else if(GeneQueryHandler.PRO_GI.equals(type))
            return new PAGIQueryHandler(terms);
        else
            return null;
    }
}
