/*
 * File : FlexDatabaseException.java
 * Classes : FlexDatabaseException
 *
 * Description :
 *
 *    The exception thrown when something goes wrong with the database.
 *
 *
 * Author : Juan Munoz (jmunoz@3rdmill.com)
 *
 * See COPYRIGHT file for copyright information
 *
 *
 * The following information is used by CVS
 * $Revision: 1.2 $
 * $Date: 2001-07-09 16:01:16 $
 * $Author: jmunoz $
 *
 ******************************************************************************
 *
 * Revision history (Started on May 22, 2001) :
 *
 *    Add entries here when updating the code. Remember to date and insert
 *    your 3 letters initials.
 *
 *    May-22-2001 : JMM - Class created.
 *
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */

package edu.harvard.med.hip.flex.database;

/**
 * This class builds the exception for a given error message.
 *
 * @author     $Author: jmunoz $
 * @version    $Revision: 1.2 $ $Date: 2001-07-09 16:01:16 $
 */
public class FlexDatabaseException extends Exception {

    /**
     * Constructor that takes in the message for the exception.
     *
     * @param s message to use for exception
     */
	public FlexDatabaseException(String s) {

		super("FlexDatabaseException: "+s);

	} // end constructor

    /**
     * Constructor that copies the given exception's message for this 
     * exception's message.
     */
    public FlexDatabaseException(Exception e) {
    
        this(e.getMessage());
    
    } // end constructor
    
} // end class FlexDatabaseException

		