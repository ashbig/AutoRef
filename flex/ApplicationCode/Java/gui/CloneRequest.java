/*
 * $Id: CloneRequest.java,v 1.5 2001-05-12 20:44:55 dongmei_zuo Exp $
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
import flex.ApplicationCode.Java.blast.*;

/**
 * This class handles the customer request.
 */
public class CloneRequest {
	public static final String BLASTDBPATH="";

	private String searchString = null;
	private String checkOrder = null;
	private Hashtable searchResult = new Hashtable();;
	private Hashtable goodSequences = new Hashtable();
	private Hashtable badSequences = new Hashtable();
	private Hashtable sameSequences = new Hashtable();
	private Hashtable homologs = new Hashtable();
	private Hashtable blastResults = new Hashtable();
	
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
     * @exception FlexUtilException, FlexDatabaseException, ParseException.
     */	
	public void processSelection(HttpServletRequest request) throws FlexUtilException, FlexDatabaseException, ParseException {	
		goodSequences.clear();
		badSequences.clear();
		sameSequences.clear();
		homologs.clear();
		blastResults.clear();
		
		String [] selections = request.getParameterValues("checkOrder");			
		for(int i=0; i<selections.length; i++) {
			String gi = selections[i];
			FlexSequence sequence = (FlexSequence)searchResult.get(gi);
			
			//for the new sequences, need more info from genbank.
			if(sequence.getFlexstatus().equals("NEW")) {
				setSequenceInfo(sequence, gi);
				
				//if the sequence quality is questionable, put it aside.
				if("QUESTIONABLE".equals(sequence.getQuality())) {
					badSequences.put(gi, sequence);
				} else {			
					FlexSeqAnalyzer analyzer = new FlexSeqAnalyzer(sequence);
					if(analyzer.findSame()) {
						sameSequences.put(gi, analyzer.getSameSequence());
					} else {
						if(analyzer.findHomolog()) {
							homologs.put(gi, analyzer.getHomolog());
							blastResults.put(gi, analyzer.getBlastResults());
						} else {
							goodSequences.put(gi, sequence);
						}
					}
				}
			} else {
				goodSequences.put(gi, sequence);
			}         	
		}				
	}
	
	public Hashtable getGoodSequences() {
		return goodSequences;
	}

	public Hashtable getBadSequences() {
		return badSequences;
	}
		
	public Hashtable getSameSequences() {
		return sameSequences;
	}
	
	public Hashtable getHomologs() {
		return homologs;
	}

	public Hashtable getBlastResults() {
		return blastResults;
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
		LinkedList l = new LinkedList();
		Protocol p = new Protocol("approve sequences");
		
		String [] selections = request.getParameterValues("good");
		if(selections != null) {
		for(int i=0; i<selections.length; i++) {
			String gi = selections[i];
			FlexSequence sequence = (FlexSequence)goodSequences.get(gi);
			r.addSequence(sequence);			
			
			if("NEW".equals(sequence.getFlexstatus())) {
				QueueItem item = new QueueItem(sequence, p);
				l.addLast(item);
			}
		}
		}

		selections = request.getParameterValues("same");
		if(selections != null) {
		for(int i=0; i<selections.length; i++) {
			String gi = selections[i];
			Vector v = (Vector)sameSequences.get(gi);				
			FlexSequence sequence = (FlexSequence)v.elementAt(0);				
			r.addSequence(sequence);			
			
			if("NEW".equals(sequence.getFlexstatus())) {
				QueueItem item = new QueueItem(sequence, p);
				l.addLast(item);
			}
		}
		}

		selections = request.getParameterValues("homolog");
		if(selections != null) {
		for(int i=0; i<selections.length; i++) {
			String gi = selections[i];
			Hashtable h = (Hashtable)homologs.get(gi);			
			String [] gis = request.getParameterValues(gi);	
			
			for(int j=0; j<gis.length; j++) {
				FlexSequence sequence = (FlexSequence)h.get(gis[j]);			
				r.addSequence(sequence);			
			
				if("NEW".equals(sequence.getFlexstatus())) {
					QueueItem item = new QueueItem(sequence, p);
					l.addLast(item);
				}
			}
		}	
		}
				
		DatabaseTransaction t = DatabaseTransaction.getInstance();
		r.insert(t);

		SequenceProcessQueue queue = new SequenceProcessQueue();
		queue.addQueueItems(l, t);
				
		t.commit();
		t.disconnect();
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
	