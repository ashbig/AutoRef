/**

 * $Id: FlexProcessException.java,v 1.2 2001-07-09 16:02:38 jmunoz Exp $

 *

 * File     : FlexProcessException.java 

 * Date     : 04162001

 * Author	: Dongmei Zuo

 */



package edu.harvard.med.hip.flex.process;



/**

 * This class builds the exception for a given error message.

 */

public class FlexProcessException extends Exception {

	public FlexProcessException(String s) {

		super("FlexProcessException: "+s);

	}

}

		