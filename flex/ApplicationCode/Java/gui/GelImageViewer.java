/**
 * $Id: GelImageViewer.java,v 1.3 2001-05-14 18:06:13 wenhong_mar Exp $
 *
 * File     	: GelImage.java 
 * Date     	: 05102001
 * Author	: Wendy Mar
 */

package flex.ApplicationCode.Java.gui;
//import javax.servlet.http.*;
import java.sql.*;
import flex.ApplicationCode.Java.database.*;
import flex.ApplicationCode.Java.core.*;
import flex.ApplicationCode.Java.util.*;


/**
 * This class represents a process.
 */
public class GelImageViewer {
	private int cloneID;
	private String plateID;	
	
	/**
	 * Constructor.
	 *
	 */
	public GelImageViewer() {
		
	}
		
	/**
	 * Constructor.
	 *
	 * @prarm processData The process date.
	 * @param subprotocol The subprotocol used in this process.
	 * @param extrainfo The extra information of the process.
	 * @return The Process object.
	 */
//	public GelImageViewer(int constructId) {
//		this.cloneID = constructId;
//	}
	
	public void setPlateID(String PlateID) {
		this.plateID = PlateID;	
	}	
	 
	public void setCloneID(int cloneID) {
		this.cloneID = cloneID;	
	}

	/**
	 * Get the gel image of for a cloneID (ConstructDesignID).
	 *
	 * @return result The gel plate label (GPL*).
	 */ 
	 public String findGel4Clone(int cloneID) throws FlexDatabaseException 
	 {
	 	DatabaseTransaction t = null;
		ResultSet rs = null;
		String gelName = null;
		String sql = "select f.basename " +
				 "from ConstructDesign c, Plateset p,filereference f " +
				 "where c.platesetid = p.platesetid " +
				 "and p.containerid_5p = f.containerid " +
				 "and constructid = " + cloneID ;
		
		try{
			t = DatabaseTransaction.getInstance();
			rs = t.getResultset(sql);
			rs.next();
			gelName = rs.getString(1);
			
		//	if (rs != null)
		//	rs.close();
		} catch (SQLException e) {
		 	throw new FlexDatabaseException(e.getMessage());
		}
		
		return gelName;
	 }
	
	 public String findPlate4Clone(String gelName) throws FlexDatabaseException 
	 {
	 	DatabaseTransaction t = null;
	 	Connection con = null;
	 	DBResults dbResults = null;
		ResultSet rs = null;
		String [] columnName;
  		int columnCount;
  
		String sql = 	 "select h.label, s.containerposition, s.constructid, s.sampleid " +
				 " from Sample s, ContainerHeader h " +
				 " where s.containerid = h.containerid " +
				 " and h.label = '" + gelName + "'" +
				 " order by s.containerposition";
				 
		
		try{
			t = DatabaseTransaction.getInstance();
			con = t.getConnection();
			rs = t.getResultset(sql);
			rs.next();
			
			ResultSetMetaData rsmd = rs.getMetaData();
        		columnCount = rsmd.getColumnCount();
        		columnName = new String [columnCount];
        		
        		// column index starts at 1
        		for (int i =1; i < columnCount+1; i++) {
          			columnName[i-1] = rsmd.getColumnName(i).trim();
        		} //for
        		
        		
        		dbResults = new DBResults(con, columnCount, columnName);
        		while (rs.next())
      			{
        			String[] row = new String[columnCount];
        			for (int i=1; i<columnCount+1; i++)
        			{
          				String entry = rs.getString(i);
          				if (entry != null) {
              					entry = entry.trim();
          				} //if
          				row[i-1] = entry;
        			} //for
        			dbResults.addRow(row);      
      			} //while

        		
        		 
		//	if (rs != null)
		//	rs.close();
		} catch (SQLException e) {
		 	throw new FlexDatabaseException(e.getMessage());
		}
		
		return (dbResults.toHTMLTable("GREY"));
	 }
	 
	 public String findGel4Plateset(String platesetID) throws FlexDatabaseException 
	 {
	 	DatabaseTransaction t = null;
		ResultSet rs = null;
		String gelName = null;
		String sql = "SELECT f.basename " +
				" FROM filereference f, containerheader h" +
				" WHERE f.containerid = h.containerid " +
				" AND h.label = '" + platesetID + "'";
		
		try{
			t = DatabaseTransaction.getInstance();
			rs = t.getResultset(sql);
			rs.next();
			gelName = rs.getString(1);
			
		//	if (rs != null)
		//	rs.close();
		} catch (SQLException e) {
		 	throw new FlexDatabaseException(e.getMessage());
		}
		
		return gelName;
	 }
	 
	 public static void main (String args[]) {
	 	GelImageViewer v = new GelImageViewer();
	 	String gelFile = null;
	 	try{
	 		gelFile= v.findGel4Clone(4221);
	 	} catch (FlexDatabaseException e){
	 		System.out.println(e);
	 	}
	 	System.out.println("The gel file name is: " + gelFile);	
	 } //main
	
}
