/**
 * $Id: Container.java,v 1.9 2007-09-19 15:42:44 Elena Exp $
 *
 * File     	: Container.java
 * Date     	: 04162001
 * Author	: Dongmei Zuo
 *
 * Revision	: 04-26-2001 	[dzuo]
 *		  Modified constructor methods so that every field is initialized
 *		  for a new instance. Also added restoreSample() method.
 *
 * Revision     : 06-05-2001    [wmar]
 *                Modified sample related methods so that the sample position
 *                is of type integer instead of String
 *              : 06-25-2001    [jmunoz]
 *                Now returns samples ordered by their location
 *              :06-25-2001     [jmunoz]
 *                removed constructor that takes in a label, and added a
 *                find method to get a list of containers which have the
 *                specified label.
 *
 * Revision     : 07-01-2001    [wmar]
 *                added method updatePlatesetId(int platesetId, Connection c)
 *
 * Revision     : 07-03-2001    {wmar]
 *                added method getFileReferences() to retrieve all of the
 *                filereferences linked to a container.
 */

package edu.harvard.med.hip.flex.core;

import java.util.*;
import java.math.BigDecimal;
import java.sql.*;
import javax.sql.*;

import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.util.*;
import edu.harvard.med.hip.flex.file.*;
import edu.harvard.med.hip.flex.workflow.*;
import edu.harvard.med.hip.flex.process.ProcessObject;
import edu.harvard.med.hip.flex.process.Protocol;

import sun.jdbc.rowset.*;

/**
 * Generic representation of all types of containers.
 */
public class Container 
{
    protected int id;
    protected String type;
    protected Location location;
    protected String label;
    protected Vector samples = new Vector();
    protected int threadid = -1;
    protected ArrayList         m_additional_info = null;
    protected boolean           m_is_additional_info = false;
    
    /**
     * Constructor.
     *
     * @param id The container id.
     *
     * @return A Container object with id.
     * @exception FlexCoreException, FlexDatabaseException.
     */
    public Container(int id) throws FlexCoreException, FlexDatabaseException {
        this.id = id;
        
        String sql = "select c.containerid as containerid, "+
        "c.containertype as containertype, "+
        "c.label as label, "+
        "c.locationid as locationid, "+
        "c.threadid as threadid, "+
        "l.locationtype as locationtype, "+
        "l.locationdescription as description\n"+
        "from containerheader c, containerlocation l\n"+
        "where c.locationid = l.locationid\n"+
        "and c.containerid = "+id;
        
        CachedRowSet crs = null;
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            crs = t.executeQuery(sql);
            
            if(crs.size()==0) {
                throw new FlexCoreException("Cannot initialize Container with id: "+id);
            }
            
            
            while(crs.next()) {
                
                type = crs.getString("CONTAINERTYPE");
                int locationid = crs.getInt("LOCATIONID");
                
                String locationtype = crs.getString("LOCATIONTYPE");
                String description = crs.getString("DESCRIPTION");
                location = new Location(locationid, locationtype, description);
                label = crs.getString("LABEL");
                threadid = crs.getInt("THREADID");
            }
        } catch (NullPointerException ex) {
            throw new FlexCoreException("Error occured while initializing container with id: "+id+"\n"+ex.getMessage());
        } catch (SQLException sqlE) {
            throw new FlexDatabaseException("Error occured while initializing container with id: "+id+"\n"+sqlE+"\nSQL: "+sql);
        } finally {
            DatabaseTransaction.closeResultSet(crs);
        }
    }
    
    
    
    /**
     * Constructor.
     *
     * @param id The container id.
     * @param type The container type.
     * @param Location The location of the container.
     * @param label The container label.
     *
     * @return The Container object.
     */
    public Container(int id, String type, Location location, String label) {
        this.id = id;
        this.type = type;
        this.location = location;
        this.label = label;
    }
    
    /**
     * Constructor.
     *
     * @param type The container type.
     * @param Location The location of the container.
     * @param label The container label.
     *
     * @return The Container object.
     * @exception FlexDatabaseException.
     */
    public Container(String type, Location location, String label) throws FlexDatabaseException {
        this.type = type;
        this.location = location;
        this.label = label;
        id = FlexIDGenerator.getID("containerid");
    }
    
    /**
     * Constructor.
     *
     * @param type The container type.
     * @param Location The location of the container.
     * @param label The container label.
     * @param threadid The thread id of this container.
     *
     * @return The Container object.
     * @exception FlexDatabaseException.
     */
    public Container(String type, Location location, String label, int threadid) throws FlexDatabaseException {
        this.type = type;
        this.location = location;
        this.label = label;
        this.threadid = threadid;
        id = FlexIDGenerator.getID("containerid");
    }
    
    
    /**
     * Finds all containers with the specified label.
     *
     * @param label The container label.
     *
     * @return A list of Container object with the given label.
     * @exception FlexCoreException, FlexDatabaseException.
     */
    
    public static List findContainers(String label) throws FlexCoreException,
    FlexDatabaseException {
        return findContainers( label, 0);
    }
    public static List findContainers(String label, int mode) throws FlexCoreException,
    FlexDatabaseException {
        
        List containerList = new LinkedList();
        
        String sql = "select c.containerid as containerid, "+
        "c.containertype as containertype, "+
        "c.label as label, c.additionalinfo as isadditionalinfo, "+
        "c.locationid as locationid, "+
        "c.threadid as threadid, "+
        "l.locationtype as locationtype, "+
        "l.locationdescription as description\n"+
        "from containerheader c, containerlocation l\n"+
        "where c.locationid = l.locationid\n"+
        "and upper(c.label) = '"+ label.toUpperCase()+"'";
        ResultSet rs = null;
        boolean is_additional_info= false;
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next()) {
                
                int id = rs.getInt("CONTAINERID");
                String containerType = rs.getString("CONTAINERTYPE");
                String containerLabel = rs.getString("LABEL");
                int threadId = rs.getInt("THREADID");
                int locationid = rs.getInt("LOCATIONID");
                String locationtype = rs.getString("LOCATIONTYPE");
                String description = rs.getString("DESCRIPTION");
                if ( mode != 0 )
                {
                    is_additional_info = (rs.getInt("isadditionalinfo") == 1 );
                }
                Location location = new Location(locationid, locationtype, description);
                Container curContainer = new Container(id,containerType,location,containerLabel);
                 if ( mode != 0 && is_additional_info )
                 {
                    curContainer.restorAdditionalInfo();
                 }
                curContainer.setThreadid(threadId);
                curContainer.restoreSample();
                containerList.add(curContainer);
            }
        } catch (NullPointerException ex) {
            throw new FlexCoreException("Error occured while initializing container with label: "+label+"\n"+ex.getMessage());
        } catch (SQLException sqlE) {
            throw new FlexDatabaseException("Error occured while initializing container from labe: "+label+"\n"+"\nSQL: "+sqlE);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        return containerList;
    }
    
    /**
     * Finds all containers with the specified label.
     *
     * @param label The container label.
     *
     * @return A list of Container object with the given label.
     * @exception FlexCoreException, FlexDatabaseException.
     */
    public static List findContainersFromView(String label) throws FlexCoreException,
    FlexDatabaseException {
        
        List containerList = new LinkedList();
        
        String sql = "select containerid, containertype, label, locationid, "+
        "threadid, locationtype,  description "+
        "from CONTAINER_BY_LABEL where upper(label) = '"+ label.toUpperCase()+"'";
        ResultSet rs = null;
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next()) {
                
                int id = rs.getInt("CONTAINERID");
                Container curContainer = new Container(id);
                curContainer.restoreSample();
                containerList.add(curContainer);
            }
        } catch (NullPointerException ex) {
            throw new FlexCoreException("Error occured while initializing container with label: "+label+"\n"+ex.getMessage());
        } catch (SQLException sqlE) {
            throw new FlexDatabaseException("Error occured while initializing container from labe: "+label+"\n"+"\nSQL: "+sqlE);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        return containerList;
    }
    
    
    
    public boolean getIsPublicInfo() { return m_is_additional_info ;}
    public ArrayList getPublicInfo(){ return m_additional_info ;}
    public void setIsPublicInfo(boolean v) { this.m_is_additional_info = v;}
    public void setPublicInfo(ArrayList v){ this.m_additional_info = v;}
  
    /**
     * Return the container id.
     *
     * @return The container id.
     */
    public int getId() {
        return id;
    }
    
    /**
     * Return the label of this container.
     *
     * @return The label string.
     */
    public String getLabel() {
        return label;
    }
    
    /**
     * Static method to construct the label from process code,
     * plateset id, and subthread id.
     *
     * @param projectCode The code related to the project.
     * @param processcode The process code related to this container label.
     * @param threadid The thread id related to this container label.
     * @param subthreadid The subthreadid if there is any.
     * @return The label.
     */
    public static String getLabel(String projectCode, String processcode, int threadid, String subthreadid) {
        java.text.NumberFormat fmt = java.text.NumberFormat.getInstance();
        fmt.setMaximumIntegerDigits(6);
        fmt.setMinimumIntegerDigits(6);
        fmt.setGroupingUsed(false);
        
        if((subthreadid == null) || (subthreadid.trim().length() == 0))
            return (projectCode+processcode+fmt.format(threadid));
        else
            return (projectCode+processcode+fmt.format(threadid)+subthreadid);
    }
    
    /**
     * Return the location of the container.
     *
     * @return The location of the container.
     */
    public Location getLocation() {
        return location;
    }
    
    /**
     * Set the container location to the given value.
     *
     * @param location The location to be set to.
     */
    public void setLocation(Location location) {
        this.location = location;
    }
    
    /**
     * Return the threadid.
     *
     * @return The thread id.
     */
    public int getThreadid() {
        return threadid;
    }
    
    /**
     * Set the threadid.
     *
     * @param threadid The value to be set to.
     */
    public void setThreadid(int threadid) {
        this.threadid = threadid;
    }
    
    /**
     * Set the container label
     * @param label The label to be set to.
     */
    public void setLabel(String label){
        this.label = label;
    }
    
    /**
     * Return the type of the container.
     *
     * @return The container type.
     */
    public String getType() {
        return type;
    }
    
    /**
     * Return a list of Sample objects.
     *
     * @return A list of Sample objects.
     */
    public Vector getSamples(){
        return samples;
    }
    
    /**
     * Get the Sample object at a certain position.
     *
     * @param position The position that the sample located. Based on 1.
     *
     * @return A Sample object.
     *
     * @exception The FlexCoreException when the samples is null.
     */
    public Sample getSample(int position) throws FlexCoreException {
        if (samples != null) {
            for(int i=0; i<samples.size(); i++) {
                Sample s = (Sample)samples.get(i);
                if(s.getPosition() == position)
                    return s;
            }
            return null;
        }
        else {
            throw new FlexCoreException("samples not exists at position: "+position);
        }
    }
    
    /**
     * Add a Sample object to a list of Sample objects.
     *
     * @param sample A Sample object to be added to the list.
     */
    public void addSample(Sample sample){
        samples.addElement(sample);
    }
    
    /**
     * Set the specified sample result to the given result.
     *
     * @param id The sample id.
     * @param result The sample result that will be set to.
     */
    public void setSampleResult(int id, String result){
    }
    
    public void restoreSample() throws FlexDatabaseException {
        restoreSample(true);
    }
    
    /**
     * Get the data from Sample table.
     *
     * @exception FlexDatabaseException.
     */
    public void restoreSample(boolean isRestoreSequence) throws FlexDatabaseException {
        if(!isRestoreSequence) {
            restoreSampleWithoutSeq();
            return;
        }
        
        samples.removeAllElements();
        
        String sql = "select * from sample where containerid="+id+" order by CONTAINERPOSITION";
        
        String newSql = "select distinct f.sequenceid, f.cdslength as cdslength from flexsequence f, "+
        "constructdesign c, sample s where f.sequenceid = "+
        "c.sequenceid and (s.constructid = c.constructid "+
        "or s.oligoid=c.oligoid_5p) and s.sampleid = ?";
        
        String nameSql = "select distinct nametype, namevalue from name where sequenceid=?";
        
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        Connection c = t.requestConnection();
        PreparedStatement stmt = null;
        PreparedStatement nameStmt = null;
        CachedRowSet crs = t.executeQuery(sql);
        ResultSet rs = null;
        ResultSet nameRs = null;
        try {
            stmt = c.prepareStatement(newSql);
            nameStmt = c.prepareStatement(nameSql);
            
            while(crs.next()) {
                
                int sampleid = crs.getInt("SAMPLEID");
                String sampletype = crs.getString("SAMPLETYPE");
                int containerposition = crs.getInt("CONTAINERPOSITION");
                
                int constructid = -1;
                Object construct = crs.getObject("CONSTRUCTID");
                if(construct != null)
                    constructid = ((BigDecimal)construct).intValue();
                
                int oligoid = -1;
                Object oligo = crs.getObject("OLIGOID");
                if(oligo != null)
                    oligoid = ((BigDecimal)oligo).intValue();
                
                int cloneid = crs.getInt("CLONEID");
                
                String status = crs.getString("STATUS_GB");
                
                Sample s = new Sample(sampleid, sampletype, containerposition, id, constructid, oligoid, status);
                s.setCloneid(cloneid);
                
                int cdslength = -1;
                
                if(!Sample.CONTROL_POSITIVE.equals(sampletype)&&!Sample.CONTROL_NEGATIVE.equals(sampletype)) {
                    stmt.setInt(1, sampleid);
                    rs = DatabaseTransaction.executeQuery(stmt);
                    int seqid;
                    
                    if(rs.next()) {
                        seqid = rs.getInt("SEQUENCEID");
                        cdslength = rs.getInt("CDSLENGTH");
                        s.setSequenceid(seqid);
                        s.setCdslength(cdslength);
                        
                        nameStmt.setInt(1, seqid);
                        nameRs = DatabaseTransaction.executeQuery(nameStmt);
                        while(nameRs.next()) {
                            String nametype = nameRs.getString("NAMETYPE");
                            String namevalue = nameRs.getString("NAMEVALUE");
                            if(FlexSequence.GENBANK_ACCESSION.equals(nametype))
                                s.setGenbank(namevalue);
                            if(FlexSequence.GENE_SYMBOL.equals(nametype))
                                s.setGeneSymbol(namevalue);
                            if(FlexSequence.PANUMBER.equals(nametype))
                                s.setPa(namevalue);
                            if(FlexSequence.SGD.equals(nametype))
                                s.setSgd(namevalue);
                        }
                    }
                }
                
                this.addSample(s);
            }
        } catch (SQLException sqlE) {
            throw new FlexDatabaseException("Error occured while initializing sample\n"+sqlE+"\nSQL: "+sql);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeResultSet(crs);
            DatabaseTransaction.closeResultSet(nameRs);
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeStatement(nameStmt);
            DatabaseTransaction.closeConnection(c);
        }
    }
    
    /**
     * Get the data from Sample table.
     *
     * @exception FlexDatabaseException.
     */
    public void restoreSampleWithoutSeq() throws FlexDatabaseException {
        samples.removeAllElements();
        
        String sql = "select * from sample where containerid="+id+" order by CONTAINERPOSITION";
        
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        Connection c = t.requestConnection();
        CachedRowSet crs = t.executeQuery(sql);
        
        try {
            while(crs.next()) {
                int sampleid = crs.getInt("SAMPLEID");
                String sampletype = crs.getString("SAMPLETYPE");
                int containerposition = crs.getInt("CONTAINERPOSITION");
                
                int constructid = -1;
                Object construct = crs.getObject("CONSTRUCTID");
                if(construct != null)
                    constructid = ((BigDecimal)construct).intValue();
                
                int oligoid = -1;
                Object oligo = crs.getObject("OLIGOID");
                if(oligo != null)
                    oligoid = ((BigDecimal)oligo).intValue();
                
                int cloneid = crs.getInt("CLONEID");
                
                String status = crs.getString("STATUS_GB");
                
                Sample s = new Sample(sampleid, sampletype, containerposition, id, constructid, oligoid, status);
                s.setCloneid(cloneid);
                this.addSample(s);
            }
        } catch (SQLException ex) {
            throw new FlexDatabaseException(ex);
        } finally {
            DatabaseTransaction.closeResultSet(crs);
            DatabaseTransaction.closeConnection(c);
        }
    }
    
    
    private void    restorAdditionalInfo() throws FlexDatabaseException 
    {
        m_additional_info = new ArrayList();
       // m_is_additional_info
        
        String sql = "select containerid, nametype, namevalue, nameurl, description from containerheader_name where containerid="+id;
        
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        Connection c = t.requestConnection();
        CachedRowSet crs = t.executeQuery(sql);
        PublicInfoItem p_info= null;//new PublicInfoItem("USER_ID", m_label);
      
        try 
        {
            while(crs.next())
            {
                m_is_additional_info = true;
                p_info = new PublicInfoItem(  crs.getString("nametype"),crs.getString("namevalue"), crs.getString("description"),crs.getString("nameurl"));
                m_additional_info.add(p_info);
            }
        } catch (SQLException ex) {
            throw new FlexDatabaseException(ex);
        } finally {
            DatabaseTransaction.closeResultSet(crs);
            DatabaseTransaction.closeConnection(c);
        }
    }
    /**
     * Insert the container record into database.
     *
     * @param conn <code>Connection</code> to use for insert.
     * @exception FlexDatabaseException.
     */
    public void insert(Connection conn) throws FlexDatabaseException {
        String sql = 	"insert into containerheader " +
        "(containerid, containertype, locationid, label, threadid) "+
        "values ("+id+",'"+type+"',"+location.getId()+",'"+label+"',"+threadid+")";
        
        DatabaseTransaction.executeUpdate(sql,conn);
        
        //foreach sample, insert record into containercell and sample table
        Enumeration enu = samples.elements();
        while (enu.hasMoreElements()) {
            Sample s = (Sample)enu.nextElement();
            s.insert(conn);
        }
    }
    
    /**
     * Return true if two containers have same id, false otherwise.
     *
     * @param c A Container object to be compared.
     *
     * @return A boolean value.
     */
    public boolean isEqual(Container c) {
        return (id == c.getId());
    }
    
    
    
    
    /**
     * Return true if the container has the same label; false otherwise.
     *
     * @param label The given label to be compared.
     * @return True if the given label is the same as the container label,
     *	   return false otherwise.
     */
    public boolean isSame(String label){
        return (label.equalsIgnoreCase(this.label));
    }
    
    /**
     * Two containers are equal when there ids are set and are equal
     *
     * @param obj The object to compare to
     *
     * @return true when equal, false otherwise
     */
    public boolean equals(Object obj) {
        boolean retValue= false;
        if (obj !=null && obj instanceof Container) {
            Container container = (Container) obj;
            if(container.getId() >0 && container.getId() == this.getId()) {
                retValue = true;
            }
        }
        return retValue;
    }
    
    /**
     * Update the location record to the database.
     *
     * @param locationid The new locationid to be updated to.
     * @param c The connection used for database update.
     * @exception The FlexDatabaseException.
     */
    public void updateLocation(int locationid, Connection c) throws FlexDatabaseException {
        String sql = "update containerheader "+
        " set locationid="+locationid+
        " where containerid="+id;
        
        DatabaseTransaction.executeUpdate(sql, c);
    }
    
    /**
     * Update the location record to the database.
     *
     * @param locationid The new locationid to be updated to.
     * @param containerid The container id for container to be updated to.
     * @param c The connection used for database update.
     * @exception The FlexDatabaseException.
     */
    public static void updateLocation(int locationid ,int containerid, Connection c) throws FlexDatabaseException {
        String sql = "update containerheader "+
        " set locationid="+locationid+
        " where containerid="+containerid;
        
        DatabaseTransaction.executeUpdate(sql, c);
    }
    public static void updateLocation(String locationdesc ,int containerid, Connection c) throws FlexDatabaseException {
        String sql = "update containerheader "+
        " set locationtype="+locationdesc+
        " where containerid="+containerid;
        
        DatabaseTransaction.executeUpdate(sql, c);
    }
    
    /**
     * Update the threadid record to the database.
     *
     * @param threadid The threadid to be updated to.
     * @param c The connection used for database update
     * @exception The FlexDatabaseException.
     */
    public void updateThreadid(int threadid, Connection c) throws FlexDatabaseException {
        String sql = "UPDATE containerheader\n"+
        " SET threadid="+threadid+
        " WHERE containerid="+id;
        
        DatabaseTransaction.executeUpdate(sql, c);
    } //updateThreadid
    
    /**
     * Retrieves all of the filereferences linked to a container object.
     *
     * @return fileRefList The list of fileReference objects
     */
    public LinkedList getFileReferences() throws FlexDatabaseException{
        LinkedList fileRefList = FileReference.findFile(this);
        return fileRefList;
        
    } //getFileReferences
    
    /**
     * Static method to find the next container from the previous one
     * for certain protocol.
     *
     * @param mgc The rearrayed MGC container.
     * @return The DNA template plate. Return null if not found.
     */
    public static Container findNextContainerFromPrevious(Container mgc, String protocolName) {
        String sql = "select c.containerid as containerid"+
        " from containerheader c, processobject o"+
        " where c.containerid = o.containerid"+
        " and o.inputoutputflag = '"+ProcessObject.OUTPUT+"'"+
        " and o.executionid in ("+
        " select executionid from processobject"+
        " where executionid in ("+
        " select e.executionid"+
        " from processexecution e, processprotocol p"+
        " where e.protocolid = p.protocolid"+
        " and p.processname='"+ protocolName +"'"+
        " ) and inputoutputflag = '"+ProcessObject.INPUT+"'"+
        " and containerid = "+mgc.getId()+
        " )  and c.locationid <> " + Location.CODE_DESTROYED;
        
        DatabaseTransaction t = null;
        CachedRowSet crs = null;
        Container c = null;
        try {
            t = DatabaseTransaction.getInstance();
            crs = t.executeQuery(sql);
            if(crs.next()) {
                int containerid = crs.getInt("CONTAINERID");
                c = new Container(containerid);
            }
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            DatabaseTransaction.closeResultSet(crs);
        }
        
        return c;
    }
    
    
    /**
     * Static method to find the next container from the previous one
     * for certain protocol.
     *
     * @param id The  container id.
     * @return The DNA template plate. Return null if not found.
     */
    public static Container findNextContainerFromPrevious(int id, String protocolName) {
        String sql = "select c.containerid as containerid "+
        " from containerheader c, processobject o"+
        " where c.containerid = o.containerid"+
        " and o.inputoutputflag = '"+ProcessObject.OUTPUT+"'"+
        " and o.executionid in ("+
        " select executionid from processobject"+
        " where executionid in ("+
        " select e.executionid"+
        " from processexecution e, processprotocol p"+
        " where e.protocolid = p.protocolid"+
        " and p.processname='"+ protocolName +"'"+
        " ) and inputoutputflag = '"+ProcessObject.INPUT+"'"+
        " and containerid = "+ id +
        " ) and c.locationid <> " + Location.CODE_DESTROYED;
        
        DatabaseTransaction t = null;
        CachedRowSet crs = null;
        Container c = null;
        try {
            t = DatabaseTransaction.getInstance();
            crs = t.executeQuery(sql);
            if(crs.next()) {
                int containerid = crs.getInt("CONTAINERID");
                c = new Container(containerid);
            }
            
            
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            DatabaseTransaction.closeResultSet(crs);
        }
        
        return c;
    }
    
    /**
     * Static method to find whether the given plate label exists in the database.
     *
     * @param barcode The plate barcode.
     * @return true if the given barcode exists in the database; false otherwise.
     * @exception FlexDatabaseException
     * @exception SQLException
     */
    public static boolean plateExistsInDB(String barcode) throws FlexDatabaseException, SQLException {
        String sql = "select count(*) from containerheader where upper(label) = ?";
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        Connection conn = t.requestConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, barcode.toUpperCase());
        ResultSet rs = DatabaseTransaction.executeQuery(stmt);
        boolean ret = false;
        if(rs.next()) {
            int count = rs.getInt(1);
            if(count>0)
                ret = true;
        }
        DatabaseTransaction.closeResultSet(rs);
        DatabaseTransaction.closeStatement(stmt);
        DatabaseTransaction.closeConnection(conn);
        
        return ret;
    }
    
   
    /**
     * Static method to find the DNA template plate from the rearrayed
     * MGC container.
     *
     * @param mgc The rearrayed MGC container.
     * @return The DNA template plate. Return null if not found.
     */
    /**    public static Container findTemplateFromRearrayedMgc(Container mgc) {
     * String sql = "select c.containerid as containerid"+
     * " from containerheader c, processobject o"+
     * " where c.containerid = o.containerid"+
     * " and o.inputoutputflag = '"+ProcessObject.OUTPUT+"'"+
     * " and o.executionid in ("+
     * " select executionid from processobject"+
     * " where executionid in ("+
     * " select e.executionid"+
     * " from processexecution e, processprotocol p"+
     * " where e.protocolid = p.protocolid"+
     * " and p.processname='"+Protocol.CREATE_DNA_FROM_REARRAYED_CULTURE+"'"+
     * " ) and inputoutputflag = '"+ProcessObject.INPUT+"'"+
     * " and containerid = "+mgc.getId()+
     * " )";
     *
     * DatabaseTransaction t = null;
     * CachedRowSet crs = null;
     * Container c = null;
     * try {
     * t = DatabaseTransaction.getInstance();
     * crs = t.executeQuery(sql);
     * if(crs.next()) {
     * int containerid = crs.getInt("CONTAINERID");
     * c = new Container(containerid);
     * }
     * } catch (Exception ex) {
     * System.out.println(ex);
     * } finally {
     * DatabaseTransaction.closeResultSet(crs);
     * }
     *
     * return c;
     * }
     */
    
    //**************************************************************//
    //				Test				//
    //**************************************************************//
    
    // These test cases also include tests for Sample class.
    public static void main(String args[]) throws Exception {
        try {
            System.out.println(System.currentTimeMillis());
            List v = Container.findContainers("EDN003162", 1 );
            Container c = (Container) v.get(0);
            System.out.println(System.currentTimeMillis());
            Container.findContainers("FPL7",1);
             Container.findContainers("FPL7");
            System.out.println(System.currentTimeMillis());
        }
        catch(Exception e) {
        }
        /*
        System.out.println("\nCreate new container with label = CPL10.3");
        Container c = new Container("CPL10.3");
        c.restoreSample();
        System.out.println(c.getSamples());
        Iterator sampleIter = c.getSamples().iterator();
        while(sampleIter.hasNext()) {
            System.out.println("sample: " + sampleIter.next());
        }
         
        try{
            Container c = new Container(40); //containerid = 40
            LinkedList fList = c.getFileReferences();
            ListIterator iter = fList.listIterator();
            System.out.println("Get file references for container 40:");
            while (iter.hasNext()) {
                FileReference f = (FileReference) iter.next();
                System.out.print("FileReference ID: "+f.getId()+ "\n");
                System.out.println("FileType: "+f.getFileType());
                System.out.println("LocalPath: "+ f.getLocalPath());
                System.out.println("FileName: "+ f.getBaseName());
            } //while
         
        }catch(FlexDatabaseException ex){
            System.out.println(ex.getMessage());
        }
         
         
        Location l = new Location(1, "testlocation", "testlocation");
        RowSet rs = null;
        try {
            Container plate = new Container("test", l, "Test1");
         
            int containerid = plate.getId();
            for(int i=1; i<=5; i++) {
                Sample s = new Sample("PCR", i, containerid);
                s.setConstructid(100);
                plate.addSample(s);
            }
         
            System.out.println("Plate ID:\t" + plate.getId());
            Location location = plate.getLocation();
            System.out.println("Plate Location:\t" + location.getType());
            System.out.println("Plate Type:\t" + plate.getType());
            System.out.println("Plate Label:\t" + plate.getLabel());
         
         
            Vector samples = plate.getSamples();
            Enumeration enum = samples.elements();
            while (enum.hasMoreElements()){
                Sample sample = (Sample)enum.nextElement();
                System.out.println("\tSample Container Id:\t"+sample.getContainerid());
                System.out.println("\tSample ID:\t"+sample.getId());
                System.out.println("\tSample Type:\t"+sample.getType());
                System.out.println("\tSample Position:\t"+sample.getPosition());
                System.out.println("\tSample is Control:\t"+sample.isControl());
                System.out.println("\tSample is empty:\t"+sample.isEmpty());
                System.out.println("\tSample construct id:\t"+sample.getConstructid());
                System.out.println("\tSample olig id:\t"+sample.getOligoid());
                System.out.println("\tSample status:\t"+sample.getStatus());
            }
         
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            Connection conn = t.requestConnection();
         
            //t.executeUpdate("insert into containerlocation values (10, 'testlocation', 'testlocation')", conn);
            //t.executeUpdate("insert into containertype values ('test')",conn);
            //t.executeUpdate("insert into flexstatus values ('test')",conn);
            //t.executeUpdate("insert into species values ('human')",conn);
            //t.executeUpdate("insert into flexsequence values (1, 'test', 'human', 10, 100,null,null,null,null)",conn);
            //t.executeUpdate("insert into constructtype values ('open')",conn);
            //t.executeUpdate("insert into oligo values (10, 'ATCG', 30, null)",conn);
            //t.executeUpdate("insert into oligo values (11, 'ATCG', 30, null)",conn);
            //t.executeUpdate("insert into cdnalibrary values (1000, 'test')",conn);
            //t.executeUpdate("insert into constructdesign values (100, 1, 10, 11, 'open', 'small',null,null)",conn);
            //t.executeUpdate("insert into sampletype values ('PCR')",conn);
            plate.insert(conn);
         
            System.out.println("Insert into containerheader:");
            rs = t.executeQuery("select * from containerheader where containerid="+containerid);
         
         
            while(rs.next()) {
                for(int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    String column = rs.getMetaData().getColumnName(i);
                    System.out.println(column + "\t" + rs.getObject(column));
                }
         
            }
         
            System.out.println("Insert into sample:");
            rs = t.executeQuery("select * from sample where containerid="+containerid);
         
            while(rs.next()) {
                for(int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    String column = rs.getMetaData().getColumnName(i);
                    System.out.println(column + "Sample:\t" + rs.getObject(column));
                }
            }
         
         
            System.out.println("\nCreate new container with containerid = "+containerid);
            Container p1 = new Container(10);
            System.out.println("Plate ID:\t" + p1.getId());
            System.out.println("Plate Location:\t" + p1.getLocation().getType());
            System.out.println("Plate Type:\t" + p1.getType());
            System.out.println("Plate Label:\t" + p1.getLabel());
         
         
         
            System.out.println("\nSamples on p1 after restore:");
            p1.restoreSample();
            Vector s1 = p1.getSamples();
            enum = s1.elements();
            while (enum.hasMoreElements()){
                Sample sample = (Sample)enum.nextElement();
                System.out.println("\tSample Container Id:\t"+sample.getContainerid());
                System.out.println("\tSample ID:\t"+sample.getId());
                System.out.println("\tSample Type:\t"+sample.getType());
                System.out.println("\tSample Position:\t"+sample.getPosition());
                System.out.println("\tSample is Control:\t"+sample.isControl());
                System.out.println("\tSample is empty:\t"+sample.isEmpty());
                System.out.println("\tSample construct id:\t"+sample.getConstructid());
                System.out.println("\tSample olig id:\t"+sample.getOligoid());
                System.out.println("\tSample status:\t"+sample.getStatus());
            }
         
         
         
            Container containTest = new Container(1288);
            Iterator fileIter = containTest.getFileReferences().iterator();
            System.out.println("Files for 1288");
            while(fileIter.hasNext()) {
                System.out.println(fileIter.next());
            }
         
            conn.rollback();
            conn.close();
         
            //System.out.println(Container.getLabel("Bp", 12, "f"));
        } catch (FlexDatabaseException exception) {
            System.out.println(exception.getMessage());
        } catch (FlexCoreException e) {
            System.out.println(e.getMessage());
        } catch (SQLException sqlE) {
            System.err.println(sqlE.getMessage());
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            System.exit(0);
        }
         */
    }
}
