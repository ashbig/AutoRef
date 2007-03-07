/*
 * EntrezGeneImporterFactory.java
 *
 * Created on February 20, 2007, 1:24 PM
 */

package plasmid.importexport.entrezgene;

/**
 *
 * @author  DZuo
 */
public class EntrezGeneImporterFactory {
    public static final String GENE = "Gene";
    public static final String SEQUENCE = "Sequence";
    
    /** Creates a new instance of EntrezGeneImporterFactory */
    public EntrezGeneImporterFactory() {
    }
    
    public static EntrezGeneImporter getEntrezGeneImporter(String type) {
        if(GENE.equals(type))
            return new GeneInfoImporter();
        if(SEQUENCE.equals(type))
            return new SequenceInfoImporter();
        return null;
    }
}
