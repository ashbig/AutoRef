/**

 * $Id: FlexProcessException.java,v 1.2 2001-06-11 14:22:43 dongmei_zuo Exp $

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

		