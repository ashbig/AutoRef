/*
 * MultipleWorklistGenerator.java
 *
 * Created on March 27, 2007, 2:04 PM
 */

package plasmid.process;

import java.io.*;
import java.util.*;
import plasmid.util.ContainerLabelComparator;
import plasmid.coreobject.*;

/**
 *
 * @author  DZuo
 */
public class MultipleWorklistGenerator extends WorklistGenerator {
    
    /** Creates a new instance of MultipleWorklistGenerator */
    public MultipleWorklistGenerator() {
        super();
    }
    
    public MultipleWorklistGenerator(List worklist) {
        super(worklist);
    }
    
    public MultipleWorklistGenerator(List worklist, boolean b) {
        super(worklist, b);
    }
        
    public void printWorklistForRobot(String filename, List avolumn, int dvolumn, boolean isWash) throws Exception {

    }
        
    protected void convertWorklist() {
        List l = new ArrayList();
        
        for(int i=0; i<worklist.size(); i++) {
            l.addAll((ArrayList)worklist.get(i)); 
        }
        
        worklist = l;
    }
}
