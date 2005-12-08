/*
 * GenerateContainers.java
 *
 * Created on November 14, 2005, 10:34 AM
 */

/**
 *
 * @author  DZuo
 */

import java.io.*;
import java.net.*;

public class GenerateContainers {
    public static final String TECAN_LOG_FILE_PATH = "G:\\plasmid\\test\\tecan\\";
    //public static final String TECAN_LOG_FILE_PATH = "C:\\Gemini\\test\\";
    
    //public static final String URL = "http://128.103.32.179/PLASMID/";
    public static final String URL = "http://128.103.32.228/PLID/";
    //public static final String URL = "http://128.103.32.228/PLASMID/";
    
    private String worklistName;
    
    /** Creates a new instance of GenerateContainers */
    public GenerateContainers() {
    }
    
    public String getWorklistName() {
        return worklistName;
    }
    
    public void setWorklistName(String s) {
        this.worklistName = s;
    }
    
    public String getLastLogFile(String pathname) throws Exception {
        File dir = new File(pathname);
        File[] files = dir.listFiles();
        long lastTime = 0;
        File lastFile = null;
        for(int i=0; i<files.length; i++) {
            File f = files[i];
            long time = f.lastModified();
            if(time>lastTime) {
                lastTime = time;
                lastFile = f;
            }
        }
        
        if(lastFile == null) {
            throw new Exception("Cannot find the log file by pathname: "+pathname);
        }
        
        return lastFile.getName();
    }
    
    public void parseTecanLogFile(String logfile) throws Exception {
        BufferedReader in = new BufferedReader(new FileReader(TECAN_LOG_FILE_PATH+logfile));
        
        String line;
        while((line = in.readLine()) != null) {
            if(line.indexOf("Load Worklist") < 0) {
                continue;
            }
            
            int begin = line.lastIndexOf("\\");
            int end = line.lastIndexOf("\"");
            worklistName = line.substring(begin+1, end);
        }
        in.close();
        System.out.println("Current worklist file: "+worklistName);
    }
    
    public boolean generateContainers() throws Exception {
        String urlString = URL+"GenerateContainers.do?worklistname="+worklistName;
        URL url = new URL(urlString);
        InputStream output = url.openStream();    
        BufferedReader reader = new BufferedReader(new InputStreamReader(output));
        String line = null;
        while((line = reader.readLine()) != null) {
            if(line.trim().equals("1"))
                return true;
            else
                return false;
        }
        reader.close();
        
        return false;
    }
    
    public static void main(String args[]) {
        String logfile = "LOG018091.LOG";
        
        System.out.println("Starting...");
        GenerateContainers g = new GenerateContainers();
        try {
            System.out.println("Getting the last modified log file in directory: "+TECAN_LOG_FILE_PATH);
            logfile = g.getLastLogFile(TECAN_LOG_FILE_PATH);
            System.out.println("Parsing log file: "+logfile);
            g.parseTecanLogFile(logfile);
            System.out.println("Parsing log file successful.");
            System.out.println("Generating containers in database...");
            if(g.generateContainers()) {
                System.out.println("Generating containers in database successful.");
                System.out.println("Successful!");
                System.out.println("Please close the window.");
            } else {
                throw new Exception("Error occured while generating containers in the database.");
            }
        } catch (Exception ex) {
            System.out.println("Error occured");
            System.out.println(ex);
            System.out.println("Aborted!");
            System.out.println("Please contact Admin for help.");
        }
    }
}