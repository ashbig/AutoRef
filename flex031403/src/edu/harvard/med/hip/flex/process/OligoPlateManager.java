/*
 * OligoPlateManager.java
 * This class manages the oligo calculation, construct generation
 * and oligo plate generation
 * Created on May 31, 2001, 3:41 PM
 * @author  Wendy
 * @version 
 */

package edu.harvard.med.hip.flex.process;
import edu.harvard.med.hip.flex.database.*;
import java.sql.*;

/**
 *
 
 */
public class OligoPlateManager {
    
    /** Creates new OligoPlateManager */
    public OligoPlateManager()  {
    }

    /**
     * Check the total number of sequences in the Queue table which are
     * waiting for Design_Construct protocol
     * @return The total number of sequences.
     */
    public int totalSeqForConstructDesign () throws FlexDatabaseException {
        int count = 0;
        ResultSet rs = null;
        int cdsSizeGroup = 2000;
        
        // get the protocol id for "DESIGN_CONSTRUCT" protocol
	int protocolId = (ProcessProtocol.DESIGN_CONSTRUCT).getId();
        String sql = "SELECT COUNT(sequenceid) " + 
                     "FROM queue q, flexsequence s \n" +
                     "WHERE q.sequenceid = s.sequenceid \n" +
                     "AND protocolId =" + protocolId;
        
        try {
            rs = DatabaseTransaction.getInstance().executeQuery(sql);
            while (rs.next()) {
                count = rs.getInt(1);
            } // while
            
        } catch (SQLException sqlex) {
            throw new FlexDatabaseException(sqlex);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        return count;
    } // 
    
    //******************************************************//
    //			Test				//
    //******************************************************//
    public static void main(String [] args) {
       int totalQueue =0;
        try {
            OligoPlateManager om = new OligoPlateManager();
            totalQueue = om.totalSeqForConstructDesign();
            System.out.println("There are total of " + totalQueue + " sequences in the queue");
           
        } catch (FlexDatabaseException e) {
            System.out.println(e);
        } finally {
          //  DatabaseTransaction.closeConnection(conn);
        }
    }
    
}
