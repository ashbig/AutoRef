/**
 * $Id: Researcher.java,v 1.1 2001-04-26 22:26:52 dongmei_zuo Exp $
 *
 * File     	: Researcher.java 
 * Date     	: 04262001
 * Author	: Dongmei Zuo
 */

package flex.ApplicationCode.Java.process;

import java.util.*;
import java.math.BigDecimal;
import flex.ApplicationCode.Java.database.*;

/**
 * Represents the researcher object corresponding to researcher table.
 */
public class Researcher {
	private int id;
	private String name;
	private String barcode;
	private String isActive;

	/**
	 * Constructor.
	 *
	 * @param id The researcher id.
	 * @param name The researcher name.
	 * @param barcode The researcher barcode.
	 * @param isActive Whether the researcher is active or not.
	 * @return The Researcher object.
	 */
	public Researcher(int id, String name, String barcode, String isActive) {
		this.id = id;
		this.name = name;
		this.barcode = barcode;
		this.isActive = isActive;
	}
	
	/**
	 * Constructor.
	 *
	 * @param barcode The researcher's barcode.
	 * @return The Researcher object.
	 * @exception FlexDatabaseException, FlexProcessException.
	 */
	public Researcher(String barcode) throws FlexDatabaseException, FlexProcessException {
		this.barcode = barcode;
		String sql ="select * from researcher where researcherbarcode = '"+barcode+"'";
		DatabaseTransaction t = DatabaseTransaction.getInstance();
		Vector v = t.executeSql(sql);
		
		if(v.isEmpty()) {
			throw new FlexProcessException("Cannot initialize Researcher with barcode: "+barcode);
		}	
		
		Enumeration enum = v.elements();
		while(enum.hasMoreElements()) {
			Hashtable h = (Hashtable)enum.nextElement();
			id = ((BigDecimal)h.get("RESEARCHERID")).intValue();
			name = (String)h.get("RESEARCHERNAME");
			barcode = (String)h.get("RESEARCHERBARCODE");
			isActive = (String)h.get("ACTIVEFLAG_YN");
		}
	}
	
	/**
	 * Return the researcher id.
	 *
	 * @return The researcher id.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Return the researcher name.
	 *
	 * @return The researcher name.
	 */
	public String getName() {
		return name;
	}
			
	//******************************************************//
	//			Test				//
	//******************************************************//
	public static void main(String [] args) {
		try {
			String sql ="insert into researcher values (1, 'Tester', 'AB0000', 'Y')";
			DatabaseTransaction t = DatabaseTransaction.getInstance();
			t.executeSql(sql);
			Researcher r = new Researcher("AB0000");
			System.out.println(r.getId());
			System.out.println(r.getName());
			t.abort();
		} catch (FlexDatabaseException e) {
			System.out.println(e);
		} catch (FlexProcessException e) {
			System.out.println(e);
		}
	}

}
	 