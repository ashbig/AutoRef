/**
 * $Id: FlexCoreException.java,v 1.1 2003-03-07 17:44:52 dzuo Exp $
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
		