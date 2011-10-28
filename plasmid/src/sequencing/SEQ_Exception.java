/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sequencing;

/**
 *
 * @author Dongmei
 */
public class SEQ_Exception extends Exception {

    /**
     * Constructor that takes in the message for the exception.
     *
     * @param s message to use for exception
     */
	public SEQ_Exception(String s) {

		super("SEQ_Exception: "+s);

	} // end constructor

    /**
     * Constructor that copies the given exception's message for this 
     * exception's message.
     */
    public SEQ_Exception(Exception e) {
    
        this(e.getMessage());
    
    } // end constructor
}
