/**

 * $Id: FlexProcessException.java,v 1.1 2001-05-23 15:40:06 dongmei_zuo Exp $

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

		super(s);

	}

}

		