/*
 * $Id: FlexDatabaseException.java,v 1.2 2001-04-25 18:37:06 dongmei_zuo Exp $
 *
 * File     : FlexDatabaseException.java 
 * Date     : 04162001
 * Author	: Dongmei Zuo
 */

package flex.ApplicationCode.Java.database;

/**
 * This class builds the exception for a given error message.
 */
public class FlexDatabaseException extends Exception {
	public FlexDatabaseException(String s) {
		super(s);
	}
}
		