/*
 * StaticQueryHandlerFactory.java
 *
 * Created on April 26, 2005, 11:52 AM
 */

package plasmid.query.handler;

import java.util.List;
import plasmid.coreobject.CloneNameType;
import plasmid.coreobject.CloneProperty;

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
        else if(GeneQueryHandler.PA.equals(type) || GeneQueryHandler.SGD.equals(type) 
        || GeneQueryHandler.VCNUMBER.equals(type) || GeneQueryHandler.FTNUMBER.equals(type)
        || GeneQueryHandler.FBID.equals(type) || GeneQueryHandler.WBGENEID.equals(type)
        || GeneQueryHandler.TAIRID.equals(type))
            return new PAQueryHandler(terms);
        else if(GeneQueryHandler.PRO_GENBANK.equals(type))
            return new PAGenbankQueryHandler(terms);
        else if(GeneQueryHandler.PRO_GI.equals(type))
            return new PAGIQueryHandler(terms);
        else if(GeneQueryHandler.PLASMIDCLONEID.equals(type))
            return new PlasmidCloneidQueryHandler(terms);
        else if(GeneQueryHandler.OTHERCLONEID.equals(type)) 
            return new CloneNameQueryHandler(terms);
        else if(GeneQueryHandler.GENETEXTCONTAIN.equals(type))
            return new GeneTextQueryHandler(terms);
        else if(GeneQueryHandler.GENETEXT.equals(type))
            return new GeneTextQueryMatchHandler(terms);
        else if(GeneQueryHandler.VECTORNAMECONTAIN.equals(type))
            return new VectorNameQueryHandler(terms);
        else if(GeneQueryHandler.VECTORNAMETEXT.equals(type))
            return new VectorNameMatchQueryHandler(terms);
        else if(GeneQueryHandler.VECTORFEATURETEXT.equals(type))
            return new VectorFeatureMatchQueryHandler(terms);
        else if(GeneQueryHandler.VECTORFEATURECONTAIN.equals(type))
            return new VectorFeatureQueryHandler(terms);
        else if(GeneQueryHandler.AUTHORTEXT.equals(type))
            return new AuthorNameMatchQueryHandler(terms);
        else if(GeneQueryHandler.AUTHORCONTAIN.equals(type))
            return new AuthorNameQueryHandler(terms);
        else if(GeneQueryHandler.PMIDMATCH.equals(type))
            return new PmidQueryHandler(terms);
        else if(CloneProperty.PROTEIN_EXPRESSED.equals(type)
                ||CloneProperty.SOLUBLE_PROTEIN.equals(type) 
                ||CloneProperty.PROTEIN_PURIFIED.equals(type))
            return new ClonePropertyQueryHandler(type, terms);
        else if(CloneNameType.PDB_ID.equals(type)
                ||CloneNameType.TARGETDB_ID.equals(type))
            return new CloneNameWithTypeQueryHandler(type, terms);
        else if(CloneNameType.PDB_ID_ALL.equals(type))
            return new CloneNameWithTypeWildcardQueryHandler(CloneNameType.PDB_ID, terms);
        else {
            RefseqNameQueryHandler h = new RefseqNameQueryHandler(terms);
            h.setNametype(type);
            return h;
        }
    }
}
