/*
 * ResultProcessManager.java
 *
 * Created on October 11, 2005, 11:32 AM
 */

package plasmid.process;

import java.util.*;
import java.io.*;

import plasmid.coreobject.Container;
import plasmid.database.DatabaseManager.DefTableManager;
//import edu.harvard.med.hip.flex.process.ColonyPickLogFileParser;
//import edu.harvard.med.hip.flex.process.CultureResultConverter;

/**
 *
 * @author  DZuo
 */
public class ResultProcessManager {
    
    /** Creates a new instance of ResultProcessManager */
    public ResultProcessManager() {
    }
    
    /**
     * Return a list of result types from the database.
     *
     * @return A list of result types.
     */
    public List getResultTypes() {
        DefTableManager manager = new DefTableManager();
        return manager.getVocabularies("resulttype", "resulttype");
    }

    public Map uploadAgarResults(InputStream input) throws Exception {/**
        ColonyPickLogFileParser parser = new ColonyPickLogFileParser(input);
        if(!parser.parseFile()) {
            throw new Exception(parser.getErrorMessage());
        }
        
        return parser.getColonyInfo();*/
        return null;
    }
    
    public List uploadCultureResults(InputStream input, Container container) throws Exception {
        /**CultureResultConverter converter = new CultureResultConverter(input, container.getSamples().size());
        if(!converter.parseFile()) {
            throw new Exception(converter.getErrorMessage());
        }
        
        if(!converter.convertResults()) {
            throw new Exception("Colony picking log file is empty");
        }

        return converter.getResultList();*/
        return null;
    }
}
