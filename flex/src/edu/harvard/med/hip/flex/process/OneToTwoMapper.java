/*
 * OneToTwoMapper.java
 *
 * This class maps one 96 well plate to two 48 well plates. 
 *
 * Created on November 6, 2001, 3:36 PM
 */

package edu.harvard.med.hip.flex.process;

/**
 *
 * @author  dzuo
 * @version 
 */
public class OneToTwoMapper extends AbstractAgarContainerMapper {
    protected String processCodes[] = {"AA", "AB"};
    protected int well = 48;
    
    /**
     * Constructor.
     *
     * @return The ContainerMapper object.
     * @exception FlexProcessException.
     */
    public OneToTwoMapper() throws FlexProcessException {
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
