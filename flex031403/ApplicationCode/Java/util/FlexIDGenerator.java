/**
 * $Id: FlexIDGenerator.java,v 1.1 2001-04-20 14:51:50 dongmei_zuo Exp $
 *
 * File     : FlexIDGenerator.java 
 * Date     : 04182001
 * Author	: Dongmei Zuo
 */

package flex.util;

import java.util.*;
import flex.database.*;

/**
 * This class gets the next primary key from the database
 * for any given table.
 */
public class FlexIDGenerator {
	/**
	 * This is a static method to get the primary key for the given table
	 * from the database.
	 *
	 * @param sequenceName The given sequence that the primary key is 
	 * 	    generated from.
	 * @return An integer representing the primary key.
	 */
	public static int getID (String sequenceName) {
		int id = 0;
		String sql = "select "+sequenceName+".nextval as id from dual";

		try {
			DatabaseTransaction t = DatabaseTransaction.getInstance();
			Vector results = t.executeSql(sql);
			Enumeration enum = results.elements();
			while (enum.hasMoreElements()) {
				Hashtable h = (Hashtable)enum.nextElement();
				id = ((Integer)h.get("id")).intValue();
			}		
		} catch (FlexDatabaseException e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
		return id;
	}
}