/*
 * PrintLabel.java
 *
 * Created on July 2, 2001, 11:57 AM
 */

package edu.harvard.med.hip.flex.util;

/**
 *
 * @author  FLEX
 * @version
 */

import java.io.*;
import java.lang.Runtime;
import java.lang.Process;
import java.io.IOException;
import java.lang.InterruptedException;
import java.net.URL;

public class PrintLabel {
    /**
     * static variables
     **/
    static final String printerName = "zebra";
    static final int labelWidth = 40;
    //static final int labelHeight = 15;
    static final int labelHeight = 3;
    
    /**
     * Creates new PrintLabel
     */
    public PrintLabel() {
    }
    
    
    public static String execute(String label) {
        String urlString = "http://fv.med.harvard.edu:8080/zebra/PrintLabelServlet?label=";
        String msg = "";
        try {
            InputStream output = null;
            BufferedReader reader = null;
            
            URL url = new URL(urlString+label);
            output = url.openStream();
            reader = new BufferedReader(new InputStreamReader(output));
            msg = reader.readLine();
            reader.close();
        } catch (Exception ex) {
            msg += msg;
        }
        return msg;
    }
    
    /**
     * Testing
     **/
    public static void main(String[] args) {
        
        String status = PrintLabel.execute("dongmeizuolabeltest");
        System.out.println("Print Status: " + status);
    }
}

/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */
