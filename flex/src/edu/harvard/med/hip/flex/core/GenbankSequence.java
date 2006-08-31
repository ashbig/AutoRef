/**
 * $Id: GenbankSequence.java,v 1.2 2006-08-31 19:25:49 dzuo Exp $
 *
 * File     : GenbankSequence.java 
 * Date     : 05022001
 * Author	: Dongmei Zuo
 */
 
package edu.harvard.med.hip.flex.core;

import edu.harvard.med.hip.flex.database.*;
import java.util.*;

/**
 * This class is built specificly for parsing the genbank search result.
 */
public class GenbankSequence {
	protected String accession;
	protected String gi;
	protected String description;
	
	/**
	 * Constructor.
	 *
	 * @param accession The genbank accession number.
	 * @param gi The GI number.
	 * @param description The gene description.
	 * @return The GenbankSequence object.
	 * @exception FlexCoreException.
	 */			 
	public GenbankSequence(String accession, String gi, String description) throws FlexCoreException {
		if(accession==null || gi==null || description==null)
			throw new FlexCoreException("Constructor cannot accept null value.");
			
		this.accession = accession;
		this.gi = gi;
		this.description = description;
	}
	
	/**
	 * Return genbank accession number.
	 *
	 * @return The genbank accession number.
	 */
	public String getAccession() {
		return accession;
	}
	
	/**
	 * Return the GI number.
	 *
	 * @return The GI number.
	 */
	public String getGi() {
		return gi;
	}
	
	/**
	 * Return the gene description.
	 *
	 * @return The gene description.
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Converts this sequence to FlexSequence object.
	 *
	 * @return A FlexSequence object.
	 * @exception FlexCoreException.
	 */
	public FlexSequence toFlexSequence() throws FlexCoreException, FlexDatabaseException {
		Vector v = new Vector();
		
		Hashtable h = new Hashtable();
		h.put("NAMETYPE", "GENBANK_ACCESSION");
		h.put("NAMEVALUE", accession);
		h.put("DESCRIPTION", description);
		v.addElement(h);
		
		h = new Hashtable();
		h.put("NAMETYPE", "GI");
		h.put("NAMEVALUE", gi);
		h.put("DESCRIPTION",description);
		v.addElement(h);
		
		FlexSequence sequence = new FlexSequence(v);
                sequence.setCurrentAccession(accession);
                sequence.setCurrentGi(gi);
                sequence.setCurrentDescription(description);
		
		return sequence;
	}
	
	//******************************************************************//
	//						Test										//
	//******************************************************************//
	
	public static void main(String [] args) {
		try {
			GenbankSequence sequence = new GenbankSequence("AB12345", "SD2222", "This is a test sequence");	
			System.out.println("Accession:\t"+sequence.getAccession());
			System.out.println("GI:\t"+sequence.getGi());
			System.out.println("Description:\t"+sequence.getDescription());
			
			FlexSequence flexSeq = sequence.toFlexSequence();
			Vector publicInfo = flexSeq.getPublicInfo();
			Enumeration enu = publicInfo.elements();
			while(enu.hasMoreElements()) {
				Hashtable h = (Hashtable)enu.nextElement();
				System.out.println("NAMETYPE:\t"+h.get("NAMETYPE"));
				System.out.println("NAMEVALUE:\t"+h.get("NAMEVALUE"));
				System.out.println("DESCRIPTION:\t"+h.get("DESCRIPTION"));
			}
		} catch (FlexCoreException e) {
			System.out.println(e.getMessage());
		} catch (FlexDatabaseException e) {
			System.out.println(e.getMessage());
		} finally {
                    System.exit(0);
                }
	}			
}