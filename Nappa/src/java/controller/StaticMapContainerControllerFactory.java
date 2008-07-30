/*
 * StaticMapContainerControllerFactory.java
 *
 * Created on October 30, 2007, 3:08 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package controller;

import java.io.InputStream;
import java.util.List;
import transfer.ProcessprotocolTO;

/**
 *
 * @author dzuo
 */
public class StaticMapContainerControllerFactory {
    
    /** Creates a new instance of StaticMapContainerControllerFactory */
    public StaticMapContainerControllerFactory() {
    }
    
    public static MapContainerController makeMapContainerController(List src, List dest, String file, InputStream fileinput, InputStream fileinputcopy, String protocol) {
        if(ProcessprotocolTO.GROW_CULTURE.equals(protocol) ||
           ProcessprotocolTO.GENERATE_GLYCEROL.equals(protocol) ||
           ProcessprotocolTO.DNA_PREP.equals(protocol)) {
            return new MapContainerController(src, dest);
        }
        if(ProcessprotocolTO.TRANSFER_FROM_96_TO_384.equals(protocol)) {
            return new PrepDnaController(src, dest, file, fileinput, fileinputcopy);
        }
        if(ProcessprotocolTO.PRINT_SLIDES.equals(protocol)) {
            return new PrintSlideController(src, dest, file, fileinput, fileinputcopy);
        }
        return null;
    }
}
