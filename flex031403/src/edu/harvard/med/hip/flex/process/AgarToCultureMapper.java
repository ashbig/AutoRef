/*
 * AgarToCultureMapper.java
 *
 * Create a new Culture block from four agar plates.
 *
 * Created on July 3, 2001, 7:43 PM
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
public class AgarToCultureMapper extends AbstractAgarToCultureMapper {
    //    public static final int AGARWELLNUM = 6;
    protected int NumOfDestPlates = 1;
    protected int startIndexes[] = {1, 5, 49, 53};
    protected int column = 6;
    protected int row = 8;
    protected int platenumOnDest = 2;
    
    /**
     * Constructor.
     *
     * @return The ContainerMapper object.
     * @exception FlexProcessException.
     */
    public AgarToCultureMapper() throws FlexProcessException {
        super();
    }
    
    // Return the subthreadid for culture plate from agar plate.
    protected String getSubThread(Container c, int index) {
        String processcode = c.getLabel().substring(1, 3);
        
        if(processcode.equals("AA")) {
            return new String("1");
        }
        if(processcode.equals("AC")) {
            return new String("2");
        }
        if(processcode.equals("AE")) {
            return new String("3");
        }
        if(processcode.equals("AG")) {
            return new String("4");
        }
        if(processcode.equals("AI")) {
            return new String("5");
        }
        if(processcode.equals("AK")) {
            return new String("6");
        }
        if(processcode.equals("AM")) {
            return new String("7");
        }
        if(processcode.equals("AO")) {
            return new String("8");
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
  
    protected int getRow() {
        return row;
    }
    
    /**
     * Return the number of plates mapped to one destination plate.
     */
    protected int getPlatenumOnDest() {
        return platenumOnDest;
    }
}