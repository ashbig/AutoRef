/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plasmid.coreobject;

import java.io.BufferedReader;
import java.io.FileReader;
import plasmid.util.FlexProperties;

/**
 *
 * @author DZuo
 */
public class MTA {
    public static final String MTA_NONE = "None";
    public static final String MTAFILE_PATH = FlexProperties.getInstance().getProperty("mtafilepath");
    public static final String ISAGREE_Y = "Agree";
    public static final String ISAGREE_N = "Do Not Agree";
    
    private int id;
    private String name;
    private String textfilename;
    private String description;
    private String filename;
    private String filepath;
    private String textfile;
    private String isagree;

    public MTA(int id, String name, String text, String desc, String filename, String filepath) {
        this.id = id;
        this.name = name;
        this.textfilename = text;
        this.description = desc;
        this.filename = filename;
        this.filepath = filepath;
        this.isagree = ISAGREE_N;
    }

    public void readTextFromFile() throws Exception {
        BufferedReader in = new BufferedReader(new FileReader(MTAFILE_PATH + textfilename));
        String line = null;
        textfile = "";
        while ((line = in.readLine()) != null) {
            textfile += line + "\n";
        }
        in.close();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTextfilename() {
        return textfilename;
    }

    public void setTextfilename(String text) {
        this.textfilename = text;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getTextfile() throws Exception {
        if(textfile == null)
            readTextFromFile();
        return textfile;
    }

    public void setTextfile(String textfile) {
        this.textfile = textfile;
    }

    public String getIsagree() {
        return isagree;
    }

    public void setIsagree(String isagree) {
        this.isagree = isagree;
    }
}
