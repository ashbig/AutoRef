/**
 * $Id $
 *
 * File     	: FlexGuiException.java 
 * Date     	: 05132001
 * Author	: Dongmei Zuo
 */

package flex.ApplicationCode.Java.gui;

/**
 * This class builds the exception for a given error message.
 */
public class FlexGuiException extends Exception {
	public FlexGuiException(String s) {
		super("FlexGuiException: "+s);
	}
}
		