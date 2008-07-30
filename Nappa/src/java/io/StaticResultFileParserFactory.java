/*
 * StaticResultFileParserFactory.java
 *
 * Created on December 7, 2007, 2:14 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package io;

import transfer.ResultTO;

/**
 *
 * @author dzuo
 */
public class StaticResultFileParserFactory {
    
    /** Creates a new instance of StaticResultFileParserFactory */
    public StaticResultFileParserFactory() {
    }
    
    public static ResultFileParser getResultFileParser(String type) {
        if(ResultTO.TYPE_CULTURE.equals(type)) 
            return new CultureFileParser();
        if(ResultTO.TYPE_DNA.equals(type))
            return new DNAFileParser();
        return null;
    }
}
