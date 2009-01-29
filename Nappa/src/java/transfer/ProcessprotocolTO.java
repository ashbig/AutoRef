/*
 * ProcessprotocolTO.java
 *
 * Created on April 25, 2007, 1:33 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package transfer;

import java.io.Serializable;

/**
 *
 * @author dzuo
 */
public class ProcessprotocolTO implements Serializable {
    public static final String IMPORT_PLATES = "Import plates";
    public static final String TRANSFER_FROM_96_TO_384 = "Transfer samples from 96-well plate to 384-well plate";
    public static final String GROW_CULTURE = "Grow culture";
    public static final String GENERATE_GLYCEROL = "Generate glycerol stock";
    public static final String PRINT_SLIDES = "Print slides";
    public static final String DNA_PREP = "DNA Prep";
    public static final String CULTURE_RESULT = "Enter Culture Results";
    public static final String DNA_RESULT = "Enter DNA Results";
    
    public static final String SET_EXP = "Set up experiment";
    public static final String UPLOAD_VIGENE_RESULT = "Upload MicroVigene Results";
    
    private String name;
    private String type;
    private String description;
    
    /**
     * Creates a new instance of ProcessprotocolTO
     */
    public ProcessprotocolTO() {
    }

    public ProcessprotocolTO(String name, String type, String desc) {
        this.name = name;
        this.type = type;
        this.description = desc;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
}
