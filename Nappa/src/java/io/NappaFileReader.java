/*
 * NappaFileReader.java
 *
 * Created on April 27, 2007, 1:26 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package io;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 *
 * @author dzuo
 */
public class NappaFileReader {
    
    /**
     * Creates a new instance of NappaFileReader
     */
    public NappaFileReader() {
    }
    
    public InputStream readFile(String filename) throws NappaIOException {
        try {
            InputStream in = new FileInputStream(filename);
            return in;
        } catch (Exception ex) {
            throw new NappaIOException("Cannot read file: "+filename+"\n"+ex.getMessage());
        }
    }
    
    public void closeFile(InputStream in) throws Exception {
        in.close();
    }
}
