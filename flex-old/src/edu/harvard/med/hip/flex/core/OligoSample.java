/*
 * OligoSample.java
 *
 * Created on June 27, 2001, 2:34 PM
 */

package edu.harvard.med.hip.flex.core;
import java.util.*;
import java.math.BigDecimal;
import java.sql.*;
import javax.sql.*;

import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.util.*;

import sun.jdbc.rowset.*;
/**
 *
 * @author  Wendy
 * @version
 */
public class OligoSample {
    private int oligoId;
    private String sequence;
    private double tm;
    private String gatewaySequence;
    private int position;
    /**
     * Constructor.
     *
     * @param id The oligo id.
     *
     * @return A oligo object with id.
     * @exception FlexCoreException, FlexDatabaseException.
     */
    public OligoSample(int id) throws FlexCoreException, FlexDatabaseException {
        this.oligoId = id;
        
        String sql = "select o.oligoid, o.oligosequence, "+
        "o.tm, o.gatewaysequence,"+
        "s.containerposition \n"+
        "from oligo o, sample s \n"+
        "where o.oligoid = s.oligoid\n"+
        "and o.oligoid = "+id;
        
        ResultSet rs = null;
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next()) {
                sequence = rs.getString("OLIGOSEQUENCE");
              //  System.out.println("oligo sequence is: "+sequence);
                tm = rs.getDouble("TM");
                gatewaySequence = rs.getString("GATEWAYSEQUENCE");
                position = rs.getInt("CONTAINERPOSITION");
            }
        } catch (NullPointerException ex) {
            throw new FlexCoreException("Error occured while initializing OligoSample with id: "+id+"\n"+ex.getMessage());
        } catch (SQLException sqlE) {
            throw new FlexDatabaseException("Error occured while initializing OligoSample with id: "+id+"\n"+sqlE+"\nSQL: "+sql);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
    } //constructor
    
    /**
     * Return sequence as a string.
     *
     * @return A string representation of the oligo sequence.
     */
    public String getSequence() {
        return sequence;
    }
    
    /**
     * Return gatewaySequence as a String.
     *
     * @return A String representation for gatewaySequence.
     */
    public String getGatewayOligoSequence() {
        return gatewaySequence;
    }
    
    /**
     * Return the oligo sample position on plate
     * @return sample position
     */
    public int getPosition(){
        return position;
    }
    
    /**
     * Return oligo melting temp: Tm.
     *
     * @return A double which indicates the oligo Tm.
     */
    public double getTm() {
        return tm;
    }
    
    /**
     * Return oligo length.
     *
     * @return A integer which indicates the oligo length.
     */
    public int getOligoLength() {
        return sequence.length();
    }
    
    /**
     * Return oligo id.
     *
     * @return A integer which indicates the oligo length.
     */
    public int getOligoId() {
        return oligoId;
    }
    
    public static void main(String [] args) {
        Connection c = null;
        int oligoid = 1;
        // OligoSample os = null;
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            c = t.requestConnection();
            OligoSample os = new OligoSample(oligoid);
            
            System.out.print("Oligo ID: "+os.getOligoId()+ "\n");
            System.out.println("Oligo Sequence: "+os.getSequence());
            System.out.println("Oligo TM: "+ os.getTm());
            System.out.println("Oligo sample position: "+ os.getPosition());
            
        }
        catch(FlexCoreException ex){
            System.out.println(ex.getMessage());
        }catch (FlexDatabaseException exception) {
            System.out.println(exception.getMessage());
        }finally {
            DatabaseTransaction.closeConnection(c);
        }
    } //main
}
