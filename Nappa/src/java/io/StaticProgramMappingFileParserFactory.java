/*
 * StaticProgramMappingFileParserFactory.java
 *
 * Created on February 28, 2007, 2:07 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package io;

import transfer.ProgramtypeTO;

/**
 *
 * @author DZuo
 */
public class StaticProgramMappingFileParserFactory {
    /** Creates a new instance of StaticProgramMappingFileParserFactory */
    public StaticProgramMappingFileParserFactory() {
    }
    
    public static ProgramMappingFileParser getParser(String type) {
        if(ProgramtypeTO.TYPE_PLATE96TO384.equals(type))
            return new Plate96To384ProgramMappingFileParser();
        if(ProgramtypeTO.TYPE_384TOSLIDE.equals(type))
            return new Plate384ToSlideGalFileParser();
        return null;
    }
}
