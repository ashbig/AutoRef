//Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->
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
 * @version    $Revision: 1.2 $ $Date: 2006-05-18 15:41:24 $
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

		
