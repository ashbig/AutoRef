/**
 * $Id: FlexUtilException.java,v 1.1 2001-07-06 19:28:57 jmunoz Exp $
 *
 * File     	: FlexUtilException.java 
 * Date     	: 04162001
 * Author	: Dongmei Zuo
 */

package edu.harvard.med.hip.flex.util;

/**
 * This class builds the exception for a given error message.
 */
public class FlexUtilException extends Exception {
	public FlexUtilException(String s) {
		super("FlexUtilException: "+s);
	}
}
		