/*
 * File : Thread.java
 * Classes : Thread
 *
 * Description :
 *
 * Base class for all types of threads such as container and sequence.
 *
 *
 * Author : Juan Munoz (jmunoz@3rdmill.com)
 * 
 * See COPYRIGHT file for copyright information
 *
 *
 * The following information is used by CVS
 * $Revision: 1.2 $
 * $Date: 2001-07-09 16:00:58 $
 * $Author: jmunoz $
 *
 ******************************************************************************
 *
 * Revision history (Started on June 29, 2001) :
 *
 *    Add entries here when updating the code. Remember to date and insert
 *    your 3 letters initials.
 *
 *    MMM-DD-YYYY : JMM - Class created. 
 *
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
*/


package edu.harvard.med.hip.flex.core;

import java.util.*;

/**
 * Class description - JavaDoc 1 liner.
 *
 * Class description - Full description
 *
 * @author     $Author: jmunoz $
 * @version    $Revision: 1.2 $ $Date: 2001-07-09 16:00:58 $
 */

public abstract class Thread {
    
    // ordered list of thread elements
    protected List elementList;

    /**
     * Gets a list of thread elements.
     *
     * @return list of container thread elements.
     */
    public List getElements() {
        return this.elementList;
    }
    
    
} // End class Thread


/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
*/
