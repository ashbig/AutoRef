/* $Id: ParamHashtable.java,v 1.2 2001-04-25 18:38:56 dongmei_zuo Exp $ 
 *
 * File     : ParamHashtable.java 
 * Date     : 04182001
 * Author	: Dongmei Zuo
 */

package flex.ApplicationCode.Java.util;

import java.util.*;

/**
 * This class only has one static method to construct a 
 * hash table for given parameter.
 */
public class ParamHashtable {
	/**
	 * Returns the Hashtable of the parameter.
	 *
	 * @param index The index value.
	 * @param type The type value.
	 * @param value The value for the key "value"
	 */
	public static Hashtable getParam (int index, String type, Object value) {
		Hashtable param = new Hashtable();
		param.put("index", new Integer(index));
		param.put("type", type);
		param.put("value", value);

		return param;
	}
}