/**
 * $Id: FlexCoreException.java,v 1.2 2001-07-09 16:00:56 jmunoz Exp $
 *
 * File     	: FlexCoreException.java 
 * Date     	: 04162001
 * Author	: Dongmei Zuo
 */

package edu.harvard.med.hip.flex.core;

/**
 * This class builds the exception for a given error message.
 */
public class FlexCoreException extends Exception {
	public FlexCoreException(String s) {
		super("FlexCoreException: "+s);
	}
}
		