/**
 * $Id: FlexIDGenerator.java,v 1.2 2001-04-25 18:38:56 dongmei_zuo Exp $
 *
 * File     	: FlexIDGenerator.java 
 * Date     	: 04182001
 * Author	: Dongmei Zuo
 */

package flex.ApplicationCode.Java.util;

import java.util.*;
import java.math.BigDecimal;

import flex.ApplicationCode.Java.database.*;

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
	public static int getID (String sequenceName) throws FlexUtilException {
		int id = 0;
		String sql = "select "+sequenceName+".nextval as id from dual";

		try {
			DatabaseTransaction t = DatabaseTransaction.getInstance();
			Vector results = t.executeSql(sql);
			Enumeration enum = results.elements();
			while (enum.hasMoreElements()) {
				Hashtable h = (Hashtable)enum.nextElement();
				id = ((BigDecimal)h.get("ID")).intValue();
			}		
		} catch (FlexDatabaseException e) {
			throw new FlexUtilException(e.getMessage());
		}
		return id;
	}
	
	//******************************************************//
	//			Test				//
	//******************************************************//
	public static void main(String [] args) {
		try {
			int id = FlexIDGenerator.getID("sampleid");
			System.out.println(id);
		} catch (FlexUtilException e) {
			System.out.println(e);
		}		
	}
}