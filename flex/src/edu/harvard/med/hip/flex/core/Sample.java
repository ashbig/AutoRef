/**
 * $Id: Sample.java,v 1.5 2003-06-11 18:12:05 dzuo Exp $
 *
 * File     	: Sample.java
 * Date     	: 04162001
 * Author	: Dongmei Zuo
 *
 * Revision	: 04-26-2001	[dzuo]
 *		  Modified the constructor methods so that it always
 *		  has every field initialized.
 *
 * Modified     : 06-03-2001 [wmar]
 *                added one constructor for oligo samples.
 *                changed datatype of position from String to integer
 *                according to the new schema.
 *
 * Modified     : 01-16-2003 [hweng]
 *                added new attributes and new constructor
 *
 */

package edu.harvard.med.hip.flex.core;

import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.util.*;
import edu.harvard.med.hip.flex.process.Protocol;

import java.math.BigDecimal;
import java.util.*;
import java.sql.*;
import javax.sql.*;

/**
 * Generic representation of all kinds of samples.
 */
public class Sample {
    public final static String GOOD = "G";
    public final static String BAD = "B";
    public final static String OLIGO_5P = "OLIGO_5P";
    public final static String OLIGO_3C = "OLIGO_3C";
    public final static String OLIGO_3F = "OLIGO_3F";
    public final static String PCR = "PCR";
    public final static String BP = "BP";
    public final static String FILTER = "FILTER";
    public final static String TRANSFORMATION = "TRANSFORMATION";
    public final static String AGAR = "AGAR";
    public final static String ISOLATE = "ISOLATE";
    public final static String DNA = "DNA";
    public final static String CULTURE = "CULTURE";
    public final static String EMPTY = "EMPTY";
    public final static String GEL = "GEL";
    public final static String CR = "CR";
    public final static String CONTROL_POSITIVE = "CONTROL_POSITIVE";
    public final static String CONTROL_NEGATIVE = "CONTROL_NEGATIVE";
    
    
    protected int id = -1;
    protected String type;
    protected int containerid = -1;
    protected int position;
    protected int constructid = -1;
    protected int oligoid = -1;
    protected String status;
    protected String result;
    protected String label;
    
    protected int cdslength = -1;
    protected int sequenceid = -1;
    
    protected int cloneid = 0;
    
    public void setCloneid(int cloneid) {this.cloneid = cloneid;}
    public int getCloneid() {return cloneid;}

    public Sample() {}
        
    /**
     * Constructor.
     *
     * @param type The type of the sample.
     * @param position The position of the sample on the container.
     * @param containerid The container id that the sample is on.
     *
     * @return A Sample object.
     *
     * @exception FlexDatabaseException.
     */
    public Sample(String type, int position, int containerid) throws FlexDatabaseException {
        this.type = type;
        this.position = position;
        this.containerid = containerid;
        this.id = FlexIDGenerator.getID("sampleid");
    }
    
    public Sample(int id, int position, int containerid) {
        this.id = id;
        this.position = position;
        this.containerid = containerid;
    }
    /**
     * Constructor.
     *
     * @param id The primary key of the sample table.
     * @param type The type of the sample.
     * @param position The position of the sample on the container.
     * @param containerid The container id that the sample is on.
     * @param constructid The construct id that the sample is connected to.
     * @param oligoid The oligo id of the sample (if the sample is oligo sample).
     * @param status The status of the sample.
     *
     * @return A Sample object.
     */
    public Sample(int id, String type, int position, int containerid, int constructid, int oligoid, String status) {
        this.id = id;
        this.type = type;
        this.position = position;
        this.containerid = containerid;
        this.constructid = constructid;
        this.oligoid = oligoid;
        this.status = status;
    }
    
    /**
     * Constructor.
     *
     * @param type The type of the sample.
     * @param position The position of the sample on the container.
     * @param containerid The container id that the sample is on.
     * @param oligoid The oligo id of the sample (if the sample is oligo sample).
     * @param status The status of the sample.
     *
     * @return A Sample object.
     */
    public Sample(String type, int position, int containerid, int oligoid, String status) throws FlexDatabaseException {
        //this.id = id;
        this.type = type;
        this.position = position;
        this.containerid = containerid;
        this.oligoid = oligoid;
        this.status = status;
        
        this.id = FlexIDGenerator.getID("sampleid");
    }
    
    /**
     * Constructor.
     *
     * @param type The type of the sample.
     * @param position The position of the sample on the container.
     * @param containerid The container id that the sample is on.
     * @param constructid The construct id of the sample.
     * @param oligoid The oligo id of the sample (if the sample is oligo sample).
     * @param status The status of the sample.
     *
     * @return A Sample object.
     */
    public Sample(String type, int position, int containerid, int constructid, int oligoid, String status) throws FlexDatabaseException {
        //this.id = id;
        this.type = type;
        this.position = position;
        this.containerid = containerid;
        this.constructid = constructid;
        this.oligoid = oligoid;
        this.status = status;
        
        this.id = FlexIDGenerator.getID("sampleid");
    }
    
    
    public Sample(int sampleid, String type, String result, String label, int pos){
        this.id = sampleid;
        this.type = type;
        this.result = result;
        this.label = label;
        this.position = pos;
    }
        
    
    
    
    /**
     * Constructor.
     *
     * @param id The primary key of the sample table.
     *
     * @return A Sample object.
     * @exception FlexCoreException, FlexDatabaseException.
     */
    public Sample(int id) throws FlexCoreException, FlexDatabaseException {
        this.id = id;
        
        String sql = "select * from sample where sampleid = "+id;
        RowSet rs = null;
        ResultSet newRs = null;
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next()) {
                
                type = rs.getString("SAMPLETYPE");
                containerid = rs.getInt("CONTAINERID");
                position = rs.getInt("CONTAINERPOSITION");
                
                Object construct = rs.getObject("CONSTRUCTID");
                if(construct != null)
                    constructid = ((BigDecimal)construct).intValue();
                else
                    constructid = -1;
                
                Object oligo = rs.getObject("OLIGOID");
                if(oligo != null)
                    oligoid = ((BigDecimal)oligo).intValue();
                else
                    oligoid = -1;
                
                status = rs.getString("STATUS_GB");
                cloneid = rs.getInt("CLONEID");
                
                String newSql = "select distinct f.cdslength as cdslength from flexsequence f, "+
                "constructdesign c, sample s where f.sequenceid = "+
                "c.sequenceid and (s.constructid = c.constructid "+
                "or s.oligoid=c.oligoid_5p) and s.sampleid = "+id;
                newRs = t.executeQuery(newSql);
                if(newRs.next()) {
                    cdslength = newRs.getInt("CDSLENGTH");
                }
            }
        } catch (NullPointerException e) {
            throw new FlexCoreException("Error occured while initializing sample with id: "+id+"\n"+e.getMessage());
        } catch (SQLException sqlE) {
            throw new FlexDatabaseException("Error occured while initializing sample with id: "+id+"\n"+sqlE+"\nSQL: "+sql);
        } finally {
            DatabaseTransaction.closeResultSet(newRs);
            DatabaseTransaction.closeResultSet(rs);
        }
    }
    
    /**
     * Return the sample id.
     *
     * @return The sample id.
     */
    public int getId() {
        return id;
    }
    
    /**
     * Return the position of this sample.
     *
     * @return The position of this sample.
     */
    public int getPosition() {
        return position;
    }
    
    /**
     * Return the sample type.
     *
     * @return The sample type.
     */
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public void setPosition(int position) {
        this.position = position;
    }
    
    /**
     * Return the sample type.
     *
     * @param processname The process name related to this sample.
     * @return The sample type.
     */
    public static String getType(String processname) {
        String type = null;
        
        if(Protocol.GENERATE_PCR_PLATES.equals(processname) || Protocol.GENERATE_STEP2_PCR_PLATES.equals(processname))
            type = PCR;
        
        if(Protocol.GENERATE_FILTER_PLATES.equals(processname))
            type = FILTER;
        
        if(Protocol.GENERATE_BP_REACTION_PLATES.equals(processname))
            type = BP;
        
        if(Protocol.PERFORM_TRANSFORMATION.equals(processname))
            type = TRANSFORMATION;
        
        if(Protocol.GENERATE_DNA_PLATES.equals(processname))
            type = DNA;
        
        if(Protocol.GENERATE_GLYCEROL_PLATES.equals(processname))
            type = ISOLATE;
        
        if(Protocol.GENERATE_AGAR_PLATES.equals(processname))
            type = AGAR;
        
        if(Protocol.GENERATE_CULTURE_BLOCKS_FOR_ISOLATES.equals(processname))
            type = ISOLATE;
        
        if(Protocol.RUN_PCR_GEL.equals(processname))
            type = GEL;

        if(Protocol.GENERATE_CAPTURE_REACTION.equals(processname))
            type = CR;
        
        if(Protocol.GENERATE_GRID_PLATE.equals(processname))
            type = AGAR;
        
        if(Protocol.PICK_COLONY.equals(processname))
            type = ISOLATE;
        
        if(Protocol.CREATE_CULTURE_FROM_MGC.equals(processname))
            type = ISOLATE;
        
        if(Protocol.CREATE_GLYCEROL_FROM_CULTURE.equals(processname))
            type = ISOLATE;
        
        if(Protocol.MGC_DESIGN_CONSTRUCTS.equals(processname))
            type = DNA;

        if(Protocol.CREATE_GLYCEROL_FROM_REARRAYED_CULTURE.equals(processname))
            type = ISOLATE;

        if(Protocol.CREATE_DNA_FROM_REARRAYED_CULTURE.equals(processname))
            type = DNA;

        if(Protocol.CREATE_DNA_FROM_MGC_CULTURE.equals(processname))
            type = DNA;
        
        if(Protocol.REARRAY_PCR_PLATES.equals(processname))
            type = PCR;
        
        if(Protocol.REARRAY_CULTURE_PLATES_BASED_ON_SEQUENCING_RESULTS.equals(processname))
            type = ISOLATE;
        if(Protocol.GENERATE_SEQUENCING_GLYCEROL_PLATES.equals(processname))
            type = ISOLATE;
                
        if(Protocol.REARRAY_TO_DNA_TEMPLATE.equals(processname))
            type = DNA;
            
        if(Protocol.REARRAY_GLYCEROL.equals(processname))
            type = ISOLATE;
        
        if(Protocol.REARRAY_ARCHIVE_DNA.equals(processname))
            type = DNA;
        
        if(Protocol.REARRAY_ARCHIVE_GLYCEROL.equals(processname))
            type = ISOLATE;
        
        if(Protocol.REARRAY_DIST_DNA.equals(processname))
            type = DNA;
        
        if(Protocol.REARRAY_DIST_GLYCEROL.equals(processname))
            type = ISOLATE;
        
        if(Protocol.REARRAY_SEQ_DNA.equals(processname))
            type = DNA;
        
        if(Protocol.REARRAY_SEQ_GLYCEROL.equals(processname))
            type = ISOLATE;
        
        return type;
    }
    
    /**
     * Return the oligo id.
     *
     * @return The oligo id.
     */
    public int getOligoid() {
        return oligoid;
    }
    
    /**
     * Gets the construct object for this Sample
     *
     * @return Construct for this sample
     *
     * @exception FlexDatabaseException when a database error occurs.
     */
    public Construct getConstruct() throws FlexDatabaseException{
        return Construct.findConstruct(this.getConstructid());
    }
    
    /**
     * Return the construct id.
     *
     * @return The construct id.
     */
    public int getConstructid() {
/*
        if(constructid != -1) {
            return constructid;
        }
 
        if(oligoid ==-1) {
            return constructid;
        }
 
        ResultSet rs = null;
 
        try {
            String sql3p = "select c.constructid as id "+
                         "from constructdesign c, sample s "+
                         "where c.oligoid_3p = s.oligoid "+
                         "and s.oligoid="+oligoid;
            String sql5p = "select c.constructid as id "+
                         "from constructdesign c, sample s "+
                         "where c.oligoid_5p = s.oligoid "+
                         "and s.oligoid="+oligoid;
 
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql3p);
 
            if(rs.next()) {
                int id = rs.getInt("ID");
                return id;
            } else {
                rs = t.executeQuery(sql5p);
                if(rs.next()) {
                    int id = rs.getInt("ID");
                    return id;
                } else {
                    return constructid;
                }
            }
        }catch (SQLException sqlE) {
        } catch (FlexDatabaseException ex) {
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
 */
        return constructid;
    }
    
    /**
     * Return the status.
     *
     * @return The status.
     */
    public String getStatus() {
        return status;
    }
    
    /**
     * Return the cdslength.
     *
     * @return cdslength.
     */
    public int getCdslength() {
        return cdslength;
    }
    
    public int getSequenceid() {
        return sequenceid;
    }
    
    public void setSequenceid(int sequenceid) {
        this.sequenceid = sequenceid;
    }
    
    /**
     * Gets the sequence this sample is from.
     *
     * @return the FlexSequence this sample is from.
     */
    public FlexSequence getFlexSequence() throws FlexDatabaseException {
        String sql =
        "select DISTINCT fs.sequenceid " +
        "from  flexsequence fs, constructdesign cd, sample s, oligo o " +
        "where "+
        "s.sampleid=" + this.id + " AND cd.sequenceid=fs.sequenceid AND " +
        "(s.constructid = cd.constructid " +
        "OR (s.oligoid=o.oligoid AND "+
        "(o.oligoid=cd.oligoid_5p OR o.oligoid=cd.oligoid_3p)))";
        ResultSet rs = DatabaseTransaction.getInstance().executeQuery(sql);
        FlexSequence seq = null;
        try {
            while(rs.next()) {
                seq =  new FlexSequence(rs.getInt("SEQUENCEID"));
            }
        } catch (SQLException sqlE) {
            throw new FlexDatabaseException(sqlE);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        return seq;
    }
    
    
    /**
     * Return the container id.
     *
     * @return The container id.
     */
    public int getContainerid() {
        return containerid;
    }
    
    /**
     * Get the container this sample is in
     *
     * @return <Container> this sample is in
     */
    public Container getContainer()
    throws FlexDatabaseException, FlexCoreException {
        Container container = new Container(getContainerid());
        return container;
    }
    
    
    public String getLabel(){
        return this.label;
    }
    
    public String getResult(){
        return this.result;
    }
    
   
    
    /**
     * Set the construct id to the given value.
     *
     * @param id The value to be set to.
     */
    public void setConstructid(int id){
        this.constructid = id;
    }
    
    /**
     * Set the oligo id to the given value.
     *
     * @param oligoid The value to be set to.
     */
    public void setOligoid(int oligoid) {
        this.oligoid = oligoid;
    }
    
    /**
     * Set the status to the given value.
     *
     * @param status The value to be set to.
     */
    public void setStatus(String status) {
        this.status = status;
    }
    
    /**
     * Set the cdslength to the given value.
     *
     * @param cdslength The value to be set to.
     */
    public void setCdslength(int cdslength) {
        this.cdslength = cdslength;
    }
    
    /**
     * Return true if the sample is empty; false otherwise.
     *
     * @return True if the sample is empty; false otherwise.
     */
    public boolean isEmpty(){
        return (type.equals("EMPTY"));
    }
    
    /**
     * Return  true if the sample is control sample; false otherwise.
     *
     * @return True if the sample is control sample; false otherwise.
     */
    public boolean isControl() {
        return (type.equals("CONTROL"));
    }
    
    /**
     * Set the sample result to the given result.
     *
     * @param result The result that the sample result will be set to.
     */
    public void setResult(String result) {
        this.result = result;
    }
    
    /**
     * Returns true if the given sample is the same; returns false otherwise.
     *
     * @param sample The given sample for comparison.
     *
     * @return True if the given sample is the same, false otherwise.
     */
    public boolean isSame(Sample sample) {
        return (this.id == sample.id);
    }
    
    /**
     * Convert the data to a String so that it can be printed out.
     */
    public String toString() {
        return new Integer(this.getId()).toString();
    }
    
    /**
     * Updates the database with the values in the object.
     *
     * @param conn The connection used to do the datbase transaction.
     */
    public void update(Connection conn) throws FlexDatabaseException {
        
        DatabaseTransaction dt = DatabaseTransaction.getInstance();
        String sql =
        "update Sample " +
        "set STATUS_GB = '"+ this.getStatus() +"' " +
        ", SAMPLETYPE = '" + this.getType() + "' " +
        "where sampleid= " + this.getId();
        dt.executeUpdate(sql, conn);
        
        
    }
    
    
    public void updatePosition(Connection conn) throws FlexDatabaseException
    {
         DatabaseTransaction dt = DatabaseTransaction.getInstance();
        String sql =  "update containercell set position = "+ this.position +" "+
        "where sampleid= " + this.id ;
        dt.executeUpdate(sql, conn);
        sql =  "update sample set containerposition = "+ this.position +" "+
        "where sampleid= " + this.id ;
        dt.executeUpdate(sql, conn);
    }
    
    /**
     * Insert the sample record into database.
     *
     * @param t DatabaseTransaction object.
     * @exception FlexDatabaseException.
     */
    public void insert(Connection conn) throws FlexDatabaseException {
        String sql = "insert into sample\n"+
        "(sampleid, sampletype, containerid, containerposition";
        String valuesql = "values ("+id +",'"+ type +"',"+containerid+","+position;
        
        if(constructid != -1) {
            sql = sql + ",constructid";
            valuesql = valuesql + ","+constructid;
        }
        
        if(oligoid != -1) {
            sql = sql + ",oligoid";
            valuesql = valuesql + ","+oligoid;
        }
        
        if(status != null) {
            sql = sql + ",status_gb";
            valuesql = valuesql + ",'"+status+"'";
        }
        
        if(cloneid != 0) {
            sql = sql + ",cloneid";
            valuesql = valuesql + ","+cloneid;
        }
        
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            
            sql = sql + ")\n"+valuesql + ")";
            stmt.executeUpdate(sql);
            
            sql = "insert into containercell " +
            "(containerid, position, sampleid) " +
            "values(" + containerid + ","+position+","+id+")";
            stmt.executeUpdate(sql);
        } catch (SQLException sqlE) {
            throw new FlexDatabaseException(sqlE+"\nSQL: "+sql);
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }
    }
    
    
    /**
     * Two samples are equal if they have throws same id
     */
    public boolean equals(Object obj) {
        boolean retVal = false;
        if(obj instanceof Sample) {
            Sample sample = (Sample)obj;
            retVal = sample.getId() > 0 && sample.getId() == this.getId();
        } else {
            retVal= false;
        }
        return retVal;
    }
    //******************************************************//
    //			Testing				//
    //******************************************************//
    public static void main(String args[]) {
        Sample sa = new Sample(1, "PCR", 1, 100, 1000, 1, "t");
        Sample sb = new Sample(1, "PCR", 2, 100, 1001, 1, "t");
        Sample sc = new Sample(2, "GEL", 3, 100, 1000, 1, "t");
        
        if(sa.isSame(sb)) {
            System.out.println("Sample sa is the same as sample sb - OK");
        } else {
            System.out.println("Sample sa is different from sample sb - ERROR");
        }
        
        if(sa.isSame(sc)) {
            System.out.println("Sample sa is the same as sample sb - ERROR");
        } else {
            System.out.println("Sample sa is different from sample sb - OK");
        }
    }
}
