/*
 * $Id: CloneRequest.java,v 1.2 2001-05-11 19:15:57 dongmei_zuo Exp $
 *
 * File     : CloneRequest.java 
 * Date     : 05042001
 * Author	: Dongmei Zuo
 */

package flex.ApplicationCode.Java.gui;

import java.util.*;
import javax.servlet.http.*;
import flex.ApplicationCode.Java.database.*;
import flex.ApplicationCode.Java.core.*;
import flex.ApplicationCode.Java.util.*;
import flex.ApplicationCode.Java.process.*;
import flex.ApplicationCode.Java.User.*;

/**
 * This class handles the customer request.
 */
public class CloneRequest {
	public static final String BLASTDBPATH="";

	private String searchString = null;
	private String checkOrder = null;
	private Hashtable searchResult = new Hashtable();;
	private Hashtable selectedSequences = new Hashtable();
	
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

	public Vector getUserRequest(String username, String password) throws FlexDatabaseException {
		User user = new User(username, password);
		DatabaseTransaction t = DatabaseTransaction.getInstance();
		Vector request = user.getRequests(t);
		return request;
	}
		
	/**
	 * Performs genbank search and gets the flex status.
	 *
	 * @return A list of FlexSequence objects.
	 * @exception FlexCoreException, FlexUtilException, FlexDatabaseException.
	 */
	public Hashtable getSearchResult() throws FlexCoreException, FlexUtilException, FlexDatabaseException {
		searchResult.clear();
		
		GenbankGeneFinder finder = new GenbankGeneFinder();
		Vector sequences = finder.search(searchString);
		Enumeration e = sequences.elements();
		while(e.hasMoreElements()) {
			GenbankSequence sequence = (GenbankSequence)e.nextElement();
			String accession = sequence.getAccession();
			String gi = sequence.getGi();
			String desc = sequence.getDescription();
			FlexSequence newSequence = sequence.toFlexSequence();
			searchResult.put(gi, newSequence);
		}
		
		return searchResult;
	}

	/**
     * Process user selected sequences.
     *
     * @param request The http request.
     * @return A Hashtable of sequences that have all the information populated.
     * @exception FlexUtilException.
     */	
	public Hashtable processSelection(HttpServletRequest request) throws FlexUtilException {	
		selectedSequences.clear();;
		String [] selections = request.getParameterValues("checkOrder");
		for(int i=0; i<selections.length; i++) {
			String gi = selections[i];
			FlexSequence sequence = (FlexSequence)searchResult.get(gi);
			
			//for the new sequences, need more info from genbank.
			if(sequence.getFlexstatus().equals("NEW")) {
				setSequenceInfo(sequence, gi);
					
					//output the sequence text for blast search.
/*					String seqText = sequence.getSequencetext();
					if(!seqText.equals("")) {
						output.println(seqText);
					}
*/			}
            			
           	 selectedSequences.put(gi, sequence);
		}				
		return selectedSequences;
	}

	/**
	 * Insert the user requested sequences into database.
	 *
     * @param request The http request.
     * @param username The name of the requestor.
     * @exception FlexDatabaseException.
     */	
	public void insertRequest(HttpServletRequest request, String username) throws FlexDatabaseException {
		Request r = new Request(username);
		String [] selections = request.getParameterValues("checkOrder");
		for(int i=0; i<selections.length; i++) {
			String gi = selections[i];
			FlexSequence sequence = (FlexSequence)selectedSequences.get(gi);
			r.addSequence(sequence);
		}
		DatabaseTransaction t = DatabaseTransaction.getInstance();
		r.insert(t);
		t.commit();
	}

	//call the parser with sequence gid, and set sequence values.
	private void setSequenceInfo(FlexSequence sequence, String gi) throws FlexUtilException {
		GenbankGeneFinder finder = new GenbankGeneFinder();
		Hashtable h = finder.searchDetail(gi);
		sequence.setSpecies((String)h.get("species"));
		int start = ((Integer)h.get("start")).intValue();
		int stop = ((Integer)h.get("stop")).intValue();
		sequence.setCdsstart(start);
		sequence.setCdsstop(stop);
		sequence.setCdslength(stop-start+1);
		sequence.setSequencetext((String)h.get("sequencetext"));
		if(start==-1 || stop == -1) {
			sequence.setCdslength(0);
			sequence.setQuality("QUESTIONABLE");
		}
		else { 
			sequence.setQuality("GOOD");
		}
	}    		  
}	
	