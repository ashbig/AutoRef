/**
 * $Id: FlexUtilException.java,v 1.1 2001-04-25 18:39:32 dongmei_zuo Exp $
 *
 * File     	: FlexUtilException.java 
 * Date     	: 04162001
 * Author	: Dongmei Zuo
 */

package flex.ApplicationCode.Java.util;

/**
 * This class builds the exception for a given error message.
 */
public class FlexUtilException extends Exception {
	public FlexUtilException(String s) {
		super("FlexUtilException: "+s);
	}
}
		