/*
 * DatabaseException.java
 *
 * Created on March 7, 2003, 11:09 AM
 */

package edu.harvard.med.hip.bec.database;


/**
 * This class builds the exception for a given error message.
 *
 * @author     $Author: Elena $
 * @version    $Revision: 1.1 $ $Date: 2003-03-14 21:19:01 $
 */
public class BecDatabaseException extends Exception {

    /**
     * Constructor that takes in the message for the exception.
     *
     * @param s message to use for exception
     */
	public BecDatabaseException(String s) {

		super("BecDatabaseException: "+s);

	} // end constructor

    /**
     * Constructor that copies the given exception's message for this 
     * exception's message.
     */
        public BecDatabaseException(Exception e) {
    
        this(e.getMessage());
    
    } // end constructor
    
} // end class BecDatabaseException

		
