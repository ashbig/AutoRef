/**
 * $Id: FlexProcessException.java,v 1.2 2001-04-25 18:37:59 dongmei_zuo Exp $
 *
 * File     : FlexProcessException.java 
 * Date     : 04162001
 * Author	: Dongmei Zuo
 */

package flex.ApplicationCode.Java.process;

/**
 * This class builds the exception for a given error message.
 */
public class FlexProcessException extends Exception {
	public FlexProcessException(String s) {
		super(s);
	}
}
		