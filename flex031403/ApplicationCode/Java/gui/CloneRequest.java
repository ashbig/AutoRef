/*
 * $Id: CloneRequest.java,v 1.1 2001-05-08 09:38:43 dongmei_zuo Exp $
 *
 * File     : CloneRequest.java 
 * Date     : 05042001
 * Author	: Dongmei Zuo
 */

package flex.ApplicationCode.Java.gui;

import java.util.*;
import flex.ApplicationCode.Java.database.*;
import flex.ApplicationCode.Java.core.*;
import flex.ApplicationCode.Java.util.*;

/**
 * This class handles the customer request.
 */
public class CloneRequest {
	private String searchString = null;
	private String checkOrder = null;
	
	/**
	 * Return searchString field.
	 *
	 * @return The searchString field.
	 */
	public String getSearchString() {
		return searchString;
	}
	
	/**
	 * Set the searchString field to the given value.
	 *
	 * @param searchString The value to be set to.
	 */
	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	/**
	 * Return checkOrder field.
	 *
	 * @return The checkOrder field.
	 */	
	public String getCheckOrder() {
		return checkOrder;
	}

	/**
	 * Set the checkOrder field to the given value.
	 *
	 * @param checkOrder The value to be set to.
	 */	
	public void setCheckOrder(String checkOrder) {
		this.checkOrder = checkOrder;
	}
	
	/**
	 * Performs genbank search and gets the flex status.
	 *
	 * @return A list of FlexSequence objects.
	 * @exception FlexCoreException, FlexUtilException, FlexDatabaseException.
	 */
	public Vector getSearchResult() throws FlexCoreException, FlexUtilException, FlexDatabaseException {
		Vector v = new Vector();
		
		GenbankGeneFinder finder = new GenbankGeneFinder();
		Vector sequences = finder.search(searchString);
		Enumeration e = sequences.elements();
		while(e.hasMoreElements()) {
			GenbankSequence sequence = (GenbankSequence)e.nextElement();
			String accession = sequence.getAccession();
			String gi = sequence.getGi();
			String desc = sequence.getDescription();
			FlexSequence newSequence = sequence.toFlexSequence();
			v.addElement(newSequence);
		}
		
		return v;
	}
}	
	