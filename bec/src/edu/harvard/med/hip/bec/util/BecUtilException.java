//Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->
/*
 * BecUtilException.java
 *
 * Created on March 7, 2003, 1:13 PM
 */

package edu.harvard.med.hip.bec.util;

/**
 *
 * @author  htaycher
 */
public class BecUtilException extends Exception
{
    
    
     /**
     * Constructor that takes in the message for the exception.
     *
     * @param s message to use for exception
     */
	public BecUtilException(String s) {

		super("BecUtilException: "+s);

	} // end constructor

    /**
     * Constructor that copies the given exception's message for this 
     * exception's message.
     */
        public BecUtilException(Exception e) {
    
        this(e.getMessage());
    
    } // end constructor
    
}
