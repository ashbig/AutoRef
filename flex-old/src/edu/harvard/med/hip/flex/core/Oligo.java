package edu.harvard.med.hip.flex.core;
import  edu.harvard.med.hip.flex.util.*;
import  edu.harvard.med.hip.flex.database.*;
import java.sql.*;

/**
 * This class represents an oligo object.
 * $Id: Oligo.java,v 1.2 2001-07-09 16:00:57 jmunoz Exp $
 * @File:	Oligo.java
 * @Date:	4/30/01
 * @author:	Wendy Mar
 *
 * modified 5/28/01 wmar:   added insert method
 * modified 6/28/01 wmar:   added constructor taking oligoid as parameter
 */
public class Oligo {
    private static final String GATEWAYTAG = "XXXX";
    private int id;
    private String type;
    private String sequence;
    private double Tm;
    private String gatewaySequence;
    
    /**
     * Constructor. Return an Oligo object.
     *
     * @param type An oligo type (five, three, threeopen).
     * @param sequence The oligo sequence text.
     * @param Tm  A double type.
     * @return An Oligo object.
     */
    public Oligo(String type, String sequence, double Tm) throws FlexDatabaseException {
        this.type = type;
        this.sequence = sequence;
        this.Tm = Tm;
        this.id = FlexIDGenerator.getID("oligoid");
    }
    
    /**
     * Constructor. Return an Oligo object.
     *
     * @param id The oligoID
     * @param type An oligo type (five, three, threeopen).
     * @param sequence The oligo sequence text.
     * @param Tm  A double type.
     * @return An Oligo object.
     */
    public Oligo(int id, String type, String sequence, double Tm) {
        this.id = id;
        this.type = type;
        this.sequence = sequence;
        this.Tm = Tm;
    }
    
    /**
     * Constructor.
     *
     * @param id The oligo id.
     *
     * @return A oligo object with id.
     * @exception FlexCoreException, FlexDatabaseException.
     */
    public Oligo(int id) throws FlexDatabaseException {
        this.id = id;
        
        String sql = "select o.oligoid, o.oligosequence, "+
        "o.tm, o.gatewaysequence\n"+
        "from oligo o\n"+
        "where o.oligoid = "+id; 
        
        ResultSet rs = null;
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next()) {
                sequence = rs.getString("OLIGOSEQUENCE");
                Tm = rs.getDouble("TM");
                gatewaySequence = rs.getString("GATEWAYSEQUENCE");
            }
        } catch (SQLException sqlE) {
            throw new FlexDatabaseException("Error occured while initializing Oligo with id: "+id+"\n"+sqlE+"\nSQL: "+sql);
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
        return (GATEWAYTAG + sequence);
    }
    
    /**
     * Return oligo type.
     *
     * @return A String representation for the oligo type.
     */
    public String getType() {
        return type;
    }
    
    /**
     * Return oligo melting temp: Tm.
     *
     * @return A double which indicates the oligo Tm.
     */
    public double getTm() {
        return Tm;
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
    public int getOligoID() {
        return id;
    }
    
    /**
     * insert oligos into Oligo table.
     *
     */
    public void insert(Connection conn) throws FlexDatabaseException {
        String gatewaySequence = this.getGatewayOligoSequence();
        Statement stmt = null;
        
        String sql = "INSERT INTO oligo\n" +
        "(oligoid, oligosequence, tm, gatewaysequence)\n" +
        " VALUES(" +id+ ",'" + sequence + "',"
         + Tm + ", '" + gatewaySequence + "')";
        
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException sqlE) {
            throw new FlexDatabaseException(sqlE+"\nSQL: "+sql);
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }        
    } //insertOligo
    
    public static void main(String [] args) {
        Connection c = null;
        int oligoid = 1;
        
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            c = t.requestConnection();
            Oligo o = new Oligo(oligoid);
            
            System.out.print("Oligo ID: "+o.getOligoID()+ "\n");
            System.out.println("Oligo Sequence: "+o.getSequence());
            System.out.println("Oligo TM: "+ o.getTm());
            
        }
        catch (FlexDatabaseException exception) {
            System.out.println(exception.getMessage());        
        }finally {
            DatabaseTransaction.closeConnection(c);
        }
    } //main
    
}
