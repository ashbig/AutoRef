/*
 * FilereferenceTO.java
 *
 * Created on April 30, 2007, 1:56 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package transfer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Serializable;
import util.Constants;

/**
 *
 * @author dzuo
 */
public class FilereferenceTO extends ProcessobjectTO implements Serializable{
    public static final String TYPE_IMPORT = "Import file";
    public static final String TYPE_MAPPING = "Program file";
    public static final String TYPE_RESULT = "Result file";
    public static final String MAPFILEPATH = Constants.REPOSITORY+Constants.MAPFILE;
    public static final String RESULTFILEPATH = Constants.REPOSITORY+Constants.RESULTFILE;
    
    private int filereferenceid;
    private String filename;
    private String filepath;
    private String filetype;
    
    /** Creates a new instance of FilereferenceTO */
    public FilereferenceTO() {
    }

    public FilereferenceTO(String filename, String filepath, String filetype) {
        this.setFilename(filename);
        this.setFilepath(filepath);
        this.setFiletype(filetype);
    }
    
    public FilereferenceTO(int id, String filename, String filepath, String filetype) {
        this.setFilereferenceid(id);
        this.setFilename(filename);
        this.setFilepath(filepath);
        this.setFiletype(filetype);
    }
    
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
        this.objectname = filename;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public int getFilereferenceid() {
        return filereferenceid;
    }

    public void setFilereferenceid(int filereferenceid) {
        this.filereferenceid = filereferenceid;
        this.objectid = filereferenceid;
    }
    
    public String getFileContent() {
        try {
            String s = "";
            String line = null;
            //Sftp ftp = SftpHandler.getSftpConnection();
            //BufferedReader f = new BufferedReader(new InputStreamReader(ftp.getInputStream(getFilepath()+getFilename(), 0)));
            BufferedReader f = new BufferedReader(new FileReader(getFilepath()+getFilename()));
            while((line=f.readLine()) != null) {
                s += line+"\n";
            }
            f.close();
            //SftpHandler.disconnectSftp(ftp);
            return s;
        } catch (Exception ex) {
            System.out.println(ex);
            return "Error reading file "+filename;
        }
    }
}
