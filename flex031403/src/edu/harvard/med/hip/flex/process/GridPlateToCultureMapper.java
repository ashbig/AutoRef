/*
 * GridPlateToCultureMapper.java
 *
 * Created on November 6, 2001, 3:48 PM
 */

package edu.harvard.med.hip.flex.process;

import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.workflow.*;
import java.util.*;

/**
 *
 * @author  dzuo
 * @version
 */
public class GridPlateToCultureMapper extends AbstractAgarToCultureMapper {
//    public static final int AGARWELLNUM = 48;
    protected int NumOfDestPlates = 4;
    protected int startIndexes[] = {1, 5};
    protected int column = 12;
    
    /**
     * Constructor.
     *
     * @return The ContainerMapper object.
     * @exception FlexProcessException.
     */
    public GridPlateToCultureMapper() throws FlexProcessException {
        super();
    }
        
    protected String getSubThread(Container c, int index) {
        String processcode = c.getLabel().substring(1, 3);
        
        if(processcode.equals("AA")) {
            return Integer.toString(index);
        }
        if(processcode.equals("AB")) {
            return Integer.toString(index+4);
        }
        return null;
    }    
  
    protected int getNumOfDestPlates() {
        return NumOfDestPlates;
    }
    
    protected int getStartIndex(int index) {
        return startIndexes[index];
    }
    
    protected int getColumn() {
        return column;
    }
    
}
