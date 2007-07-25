/*
 * ContainerTypeProperties.java
 *
 * Created on July 9, 2001, 6:24 PM
 */
package edu.harvard.med.hip.flex.infoimport.bioinfo;


import edu.harvard.med.hip.flex.util.*;

import java.io.*;



/**
 *
 * @author  dzuo
 * @version 
 */
public class SpeciesTranslationProperties extends FlexProperties 
{    
    // The name of the properties file holding the system config info.
    public final static String CONTAINER_TYPE_FILE ="config/bio_species_codon_translation.properties";

    private static SpeciesTranslationProperties spInstance = null;
    
    protected InputStream getInputStream() {
        return (Thread.currentThread().getContextClassLoader().getResourceAsStream(CONTAINER_TYPE_FILE));
    }
    
    /**
     * Gets the instance of systemproperties.
     *
     * @return the single SystemProperites instance.
     */
    public static FlexProperties getInstance() {
        if(spInstance == null) {
            spInstance = new SpeciesTranslationProperties();
        }
        return spInstance;
    } 
    
    
}
