/*
 * ResultFileParser.java
 *
 * Created on December 7, 2007, 2:12 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package io;

import core.Fileresult;
import java.io.InputStream;
import java.util.List;

/**
 *
 * @author dzuo
 */
abstract public class ResultFileParser {
    
    /** Creates a new instance of ResultFileParser */
    public ResultFileParser() {
    }
    
    abstract public List<Fileresult> parseFile(InputStream input) throws NappaIOException;
}
