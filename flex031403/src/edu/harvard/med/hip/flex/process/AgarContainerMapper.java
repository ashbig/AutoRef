/*
 * AgarContainerMapper.java
 *
 * This class creates 16 agar plates from one transformation plate
 * and mapping the samples from transformation plate to agar plate.
 *
 * Created on July 3, 2001, 12:00 PM
 */

package edu.harvard.med.hip.flex.process;

import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.workflow.*;
import java.util.*;

/**
 *
 * @author  dzuo
 * @version 
 */
public class AgarContainerMapper extends AbstractAgarContainerMapper {
    protected String processCodes[] = {"AA", "AB", "AC", "AD", "AE", "AF", "AG", "AH", "AI", "AJ", "AK", "AL", "AM", "AN", "AO", "AP"};
    protected int well = 6;
    
    /**
     * Constructor.
     *
     * @return The ContainerMapper object.
     * @exception FlexProcessException.
     */
    public AgarContainerMapper() throws FlexProcessException {
        super();       
    }
    
    /**
     * Return the all the process codes in an array.
     *
     * @return All the process codes in an array.
     */
    public String[] getProcessCodes() {
        return processCodes;
    }
    
    /**
     * Return the well number.
     *
     * @return The well number.
     */
    public int getWell() {
        return well;
    }
}
