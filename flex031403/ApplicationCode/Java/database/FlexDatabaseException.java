/*
 * $Id: FlexDatabaseException.java,v 1.1 2001-04-20 14:49:47 dongmei_zuo Exp $
 *
 * File     : FlexDatabaseException.java 
 * Date     : 04162001
 * Author	: Dongmei Zuo
 */

package flex.database;

/**
 * This class builds the exception for a given error message.
 */
public class FlexDatabaseException extends Exception {
	public FlexDatabaseException(String s) {
		super(s);
	}
}
		