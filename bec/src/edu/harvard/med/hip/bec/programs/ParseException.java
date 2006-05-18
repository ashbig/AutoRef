//Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->
/*
 * ParceException.java
 *
 * Created on June 16, 2003, 3:20 PM
 */

package edu.harvard.med.hip.bec.programs;

/**
 *
 * @author  htaycher
 */
public class ParseException extends Exception
{
    
     /**
     * Constructor that takes in the message for the exception.
     *
     * @param s message to use for exception
     */
	public ParseException(String s) {

		super("ParceException: "+s);

	} // end constructor

    /**
     * Constructor that copies the given exception's message for this 
     * exception's message.
     */
        public ParseException(Exception e) {
    
        this(e.getMessage());
    
    } // end constructor
    
}
