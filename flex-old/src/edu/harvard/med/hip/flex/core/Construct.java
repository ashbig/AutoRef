/**
 * $Id: Construct.java,v 1.1 2001-07-06 19:28:41 jmunoz Exp $
 * This class maps to the ConstructDesign table in the database.
 * @File Construct.java
 * @date 4/28/01
 * @author Wendy Mar, Dongmei Zuo
 *
 * modified 6/1/01, wmar
 * added setPairId and setPlatesetId methods.
 * modified constructor.
 *
 * modified 6/30/01:    wmar
 * added new constructor
 */

package edu.harvard.med.hip.flex.core;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.util.*;
import java.sql.*;

public class Construct {
    private static final int SIZELOWER = 2000;
    private static final int SIZEHIGHER = 4000;
    
    private int id;
    private Sequence sequence;
    private Oligo fivepOligo;
    private Oligo threepOligo;
    private String type;
    private String sizeClass;
    private int pairId;
    private int platesetId = -1;
    
    private int seqId;
    private int oligoId_5p;
    private int oligoId_3p;
      
    /**
     * Constructor.
     *
     * @param sequence The Sequence object for this construct.
     * @param five The five prime oligo object for this construct.
     * @param three The three prime oligo object for this construct
     * @param type The construct type: open or close.
     *
     * @return A Construct object.
     */
    public Construct(Sequence sequence, Oligo fivep, Oligo threep, String type, int pairId, int platesetId) throws FlexDatabaseException {
        this.sequence = sequence;
        this.fivepOligo = fivep;
        this.threepOligo = threep;
        this.type = type;
        this.pairId = pairId;
        this.platesetId = platesetId;
        
        this.id = FlexIDGenerator.getID("constructid");
        setSizeClass();
    }
    
    /**
     * Constructor.
     *
     * @param sequence The Sequence object for this construct.
     * @param five The five prime oligo object for this construct.
     * @param three The three prime oligo object for this construct
     * @param type The construct type: open or close.
     *
     * @return A Construct object.
     */
    public Construct(Sequence sequence, Oligo fivep, Oligo threep, String type, int pairId) throws FlexDatabaseException {
        this.sequence = sequence;
        this.fivepOligo = fivep;
        this.threepOligo = threep;
        this.type = type;
        this.pairId = pairId;
        
        this.id = FlexIDGenerator.getID("constructid");
        setSizeClass();
    }
    
    /**
     * Constructor.
     *
     * @param id The constructID
     * @param sequence The Sequence object for this construct.
     * @param five The five prime oligo object for this construct.
     * @param three The three prime oligo object for this construct
     * @param type The construct type: open or close.
     * @param pairId The pairID for two open-close construct pairs
     * @param platesetId The platesetID for a group of three oligo plates
     *
     * @return A Construct object.
     */
    public Construct(int id, int seqId, int oligoId_5p, int oligoId_3p, String type, int pairId, int platesetId) {
        this.id = id;
        this.seqId = seqId;
        this.oligoId_5p = oligoId_5p;
        this.oligoId_3p = oligoId_3p;
        this.type = type;
        this.pairId = pairId;
        this.platesetId = platesetId;
        setSizeClass();
    }

        /**
     * Constructor.
     *
     * @param id The constructID
     * @param sequence The Sequence object for this construct.
     * @param five The five prime oligo object for this construct.
     * @param three The three prime oligo object for this construct
     * @param type The construct type: open or close.
     * @param size The size class.
     * @param pairId The pairID for two open-close construct pairs
     * @param platesetId The platesetID for a group of three oligo plates
     *
     * @return A Construct object.
     */
    public Construct(int id, int seqId, int oligoId_5p, int oligoId_3p, String type, String size, int pairId, int platesetId) {
        this.id = id;
        this.seqId = seqId;
        this.oligoId_5p = oligoId_5p;
        this.oligoId_3p = oligoId_3p;
        this.type = type;
        this.sizeClass = size;
        this.pairId = pairId;
        this.platesetId = platesetId;
    }
    
    /**
     * Return the constructID.
     *
     * @return the constructID.
     */
    public int getId() {
        return id;
    }
    
    /**
     * Return the fivepOligo object.
     *
     * @return An Oligo object.
     */
    public Oligo getFivepOligo() {
        return fivepOligo;
    }
    
    /**
     * Return the threepOligo object.
     *
     * @return An Oligo object.
     */
    public Oligo getThreepOligo() {
        return threepOligo;
    }
    
    /**
     * Return fivepOligo id
     *
     * @return An integer.
     */
    public int getFivepOligoid() {
        return fivepOligo.getOligoID();
    }
    
    /**
     * Return threepOligo id
     *
     * @return An integer.
     */
    public int getThreepOligoid() {
        return threepOligo.getOligoID();
    }
    
    /**
     * Return the Sequence object.
     *
     * @return A sequence object.
     */
    public Sequence getSequence() {
        return sequence;
    }
    
    /**
     * Return the Sequence object.
     *
     * @return A sequence object.
     */
    public int getSequenceId() {
        return sequence.getSeqID();
    }
    
    /**
     * Return the construct type.
     *
     * @return A String.
     */
    public String getType() {
        return type;
    }
    
    /**
     * Return the construct size class.
     *
     * @return Return the SizeClass as String.
     */
    public String getSizeClass() {
        return sizeClass;
    }
    
    /**
     * Return the pairid.
     *
     * @return Return the pairid.
     */
    public int getPairId() {
        return pairId;
    }
    
    /**
     * set the construct pairId
     *
     * @param The construct pair ID
     */
    public void setPairId(int pairId) {
        this.pairId = pairId;
    }
    
    /**
     * set the oligo platesetId
     *
     * @param The platesetId
     */
    public void setPlatesetId(int platesetId) {
        this.platesetId = platesetId;
    }
    
    /**
     * Return true if the given construct has the same pairid with
     * this object. Return false otherwise.
     *
     * @param c The given Construct object.
     *
     * @return A boolean value indicating whether the given construct
     *  	     has the same pairid with this object.
     */
    public boolean isPair(Construct c) {
        return (this.pairId == c.getPairId());
    }
    
    //****************************************************************//
    //											//
    // 		Private utility functions			  //
    //											//
    //****************************************************************//
    
    /**
     * Calculate the size class based on the criteria. Called by the constructor.
     */
    private void setSizeClass() {
        if (sequence != null) {
            if (sequence.getCDSLength() < SIZELOWER) {
                sizeClass = "Small";
            } else if (sequence.getCDSLength() < SIZEHIGHER) {
                sizeClass = "Medium";
            } else {
                sizeClass = "Large";
            }
        } else {
            //	throws new FlexException("Sequence object not defined.")
        }
    }
    
    /**
     * insert a Construct record into ConstructDesign table.
     *
     * @param t The DatabaseTransaction object.
     */
    public void insert(Connection conn) throws FlexDatabaseException {
        
        int seqID = sequence.getSeqID();
        int oligofivep = fivepOligo.getOligoID();
        int oligothreep = threepOligo.getOligoID();
        
        String platesetIdString = null;
        if (platesetId < 0)
        {
            platesetIdString = "null";
        }
        else
        {
            platesetIdString = String.valueOf(platesetId);
        }
        String sql = "INSERT INTO ConstructDesign\n" +               
        "(constructid, sequenceid, oligoid_5p, oligoid_3p," +
        " constructtype, constructsizeclass, constructpairid, platesetid)\n" +
        " VALUES(" +id+ "," +seqID+ "," +oligofivep+ "," +oligothreep+ "," +
        "'" + type + "', " + "'" + sizeClass + "'," 
        + pairId + "," + platesetIdString + ")";
        
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException sqlE) {
            throw new FlexDatabaseException(sqlE+"\nSQL: "+sql);
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }              
    } //insertConstruct
    
    public static void main(String [] args) {
        Connection c = null;
        int Id = 20000; // for testing...
        int seqId = 94;
        int oligo5p = 10;
        int oligo3s = 11;
        
        try {           
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            c = t.requestConnection();
            
            Construct cons = new Construct(Id,seqId,oligo5p,oligo3s,"CLOSED","small",100,1);
            System.out.println("inserting...");
            cons.insert(c);
            System.out.println("Construct ID: " + cons.getId());
           // System.out.println("sequence ID: "+ cons.getSequenceId());
           // System.out.println("CDS: "+seq.getCDSLength());
           // System.out.println("Sequence length: " + seq.getSeqLength());
           // System.out.println("Start pos: " + seq.getStart());
            
        } catch (FlexDatabaseException exception) {
            System.out.println(exception.getMessage());
        } finally {
            DatabaseTransaction.closeConnection(c);
        }
    } //main
}
