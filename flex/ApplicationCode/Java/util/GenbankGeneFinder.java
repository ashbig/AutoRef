/**
 * $Id: GenbankGeneFinder.java,v 1.1 2001-05-08 09:30:13 dongmei_zuo Exp $
 *
 * File     	: GenbankGeneFinder.java 
 * Date     	: 05052001
 * Author	: Dongmei Zuo
 */
 
package flex.ApplicationCode.Java.util;

import java.net.*;
import java.io.*;
import java.lang.*;
import java.util.*;
import flex.ApplicationCode.Java.core.GenbankSequence;

/**
 * GeneBankGeneFinder is an html parser class that performs genbank
 * search and result parsing. The search is performed by sending the http 
 * request to Genbank website. This will give us the benefit of using
 * Genbank's search engine.
 */
public class GenbankGeneFinder {
	/**
	 * Constructor.
	 */
	public GenbankGeneFinder() {}

	/**
	 * Perform the public database searching.
	 *
	 * @param query A query string used for search.
	 *
	 * @return A list of GenbankSequence object.
	 */	
	public Vector search(String query) throws FlexUtilException {
		try {
			Vector v = new Vector();
			String urlString = "http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=Search&db=Nucleotide&term="+query.replace(' ', '+');
			URL url = new URL(urlString);	
			BufferedReader in = new BufferedReader(
						new InputStreamReader(
						url.openStream()));
	
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				if(inputLine.indexOf("<dd>") == 0) {					
					StringTokenizer st = new StringTokenizer(inputLine);
					if (st.hasMoreTokens()) {
						String desc = st.nextToken("<dd>");
						String giIgnore = st.nextToken("|");
						String gi = st.nextToken("|");
						String gbIgnore = st.nextToken("|");
						String gb = st.nextToken("|");
						GenbankSequence sequence = new GenbankSequence(gb, gi, desc);
						v.addElement(sequence);
					}
				}
			}
			in.close();
			return v;
		} catch (Exception e) {
			throw new FlexUtilException (e.getMessage());
		}
    }
   
    //**************************************************************//
    //							Test								// 
    //**************************************************************//
    public static void main(String [] args) {
    	GenbankGeneFinder finder = new GenbankGeneFinder();
    	try {
	    	Vector v = finder.search("human kinase");
    		Enumeration enum = v.elements();
    		while(enum.hasMoreElements()) {
    			GenbankSequence sequence = (GenbankSequence)enum.nextElement();
    			System.out.println("Accession: "+sequence.getAccession());
    			System.out.println("GI: "+sequence.getGi());
    			System.out.println("Description: "+sequence.getDescription());
				System.out.println();
			}
		} catch (FlexUtilException e) {
			System.out.println(e);
		}
    }
}

