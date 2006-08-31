/**
 * $Id: MgcContainer.java,v 1.4 2006-08-31 19:25:49 dzuo Exp $
 *
 * File     	: MgcContainer.java
 * Date     	: 04162001
 * Author	: Helen Taycher
 *
 * Revision	:
 *
 *SQLWKS> desc mgccontainer
Column Name                    Null?    Type
------------------------------ -------- ----
MGCCONTAINERID                 NOT NULL NUMBER(10)
ORICONTAINER                   NOT NULL VARCHAR2(50)
FILENAME                                VARCHAR2(50)
GLYCEROLCONTAINERID                     NUMBER(10)
CULTURECONTAINERID                      NUMBER(10)
MARKER                         NOT NULL VARCHAR2(50)
DNACONTAINERID                          NUMBER(10)
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

import sun.jdbc.rowset.*;

public class MgcContainer extends Container {
    
    private String          m_OriginalContainerName;
    private String          m_FileName;
    private int             m_glycerolContainerid = -1;
    private int             m_culture_Containerid = -1;
    private int             m_dnaContainerid = -1;
    private String          m_marker;
    
    private static final String m_ContainerType = "96 WELL PLATE";
    
    /**
     * Constructor.
     *
     * @param id The container id.
     * @param file name The filename from which container was created .
     * @param Location The location of the container.
     * @param label The container label.
     *
     * @return The Container object.
     */
    public MgcContainer(int id, String fileName, Location location,
    String original_container, String label, String marker) {
        super( id, m_ContainerType,  location,  label);
        m_OriginalContainerName = original_container;
        m_FileName = fileName;
        m_marker = marker;
        
    }
    
    /**
     * Constructor.
     *
     * @param id The container id.
     * @param file name The filename from which container was created .
     * @param Location The location of the container.
     * @param label The container label.
     *
     * @return The Container object.
     */
    public MgcContainer(int id, String fileName, Location location,
    String original_container, String label, String marker, int culture, int glycerol, int dna) {
        super( id, m_ContainerType,  location,  label);
        m_OriginalContainerName = original_container;
        m_FileName = fileName;
        m_marker = marker;
        m_glycerolContainerid = glycerol;
        m_culture_Containerid = culture;
        m_dnaContainerid = dna;
    }
    
    /**
     * Constructor.
     *
     * @param id The container id.
     *
     * @return A Container object with id.
     * @exception FlexCoreException, FlexDatabaseException.
     */
    
    public MgcContainer(int id) throws FlexCoreException, FlexDatabaseException {
        super( id, "",null, "") ;
        this.threadid = -1;
        
        String sql = "select mc.filename as filename, mc.oricontainer as orgcontainer, mc.marker as marker, mc.glycerolcontainerid as gl, mc.dnacontainerid as dna, mc.CULTURECONTAINERID as cl , " +
        " c.containertype as type, c.locationid as locationid, c.label as label, " +
        "l.locationtype as locationtype, l.locationdescription as description "+
        "from containerlocation l, mgccontainer mc , containerheader c where ( mc.mgccontainerid =  c.containerid  and c.containerid = " + id + ") and " +
        " l.locationid = c.locationid ";
        
        
        
        CachedRowSet crs = null;
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            crs = t.executeQuery(sql);
            
            if(crs.size()==0) {
                throw new FlexCoreException("Cannot initialize MgcContainer with id: "+id);
            }
            
            
            while(crs.next()) {
                m_FileName = crs.getString("FILENAME");
                m_OriginalContainerName = crs.getString("ORGCONTAINER");
                m_glycerolContainerid = crs.getInt("GL");
                m_culture_Containerid = crs.getInt("CL");
                m_dnaContainerid = crs.getInt("DNA");
                m_marker = crs.getString("MARKER");
                this.type = crs.getString("TYPE");
                int locationid = crs.getInt("LOCATIONID");
                
                String locationtype = crs.getString("LOCATIONTYPE");
                String description = crs.getString("DESCRIPTION");
                location = new Location(locationid, locationtype, description);
                this.label = crs.getString("LABEL");
            }
        } catch (NullPointerException ex) {
            throw new FlexCoreException("Error occured while initializing mgc container with id: "+id+"\n"+ex.getMessage());
        } catch (SQLException sqlE) {
            throw new FlexDatabaseException("Error occured while initializing mgc container with id: "+id+"\n"+sqlE+"\nSQL: "+sql);
        } finally {
            DatabaseTransaction.closeResultSet(crs);
        }
    }
    
    
    /**
     * Static method to construct the label from containerId
     *
     * @param original container id
     * @return The label.
     */
    public static String getLabel( int container_number) {
        int count = Integer.toString(container_number).length();
        StringBuffer temp = new StringBuffer("MGC") ;
        for (int i = 0; i < 6 - count; i++) {
            temp.append("0");
        }
        
        return ( temp.toString() + container_number );
    }
    
    
    /**
     * Not allaod for MgcContainer
     * Static method of Container class to construct the label from process code,
     * plateset id, and subthread id.
     *
     * @param projectCode The code related to the project.
     * @param processcode The process code related to this container label.
     * @param threadid The thread id related to this container label.
     * @param subthreadid The subthreadid if there is any.
     * @return empty string.
     */
    public static String getLabel(String projectCode, String processcode, int threadid,
    String subthreadid) {
        return "";
    }
    
    /**
     * Add a MgcSample object to a list of Sample objects.
     * (alows to add only MgcSample, not Sample)
     * @param sample A MgcSample object to be added to the list.
     */
    public void addSample(Sample sample) {
        if ( sample instanceof MgcSample) samples.addElement(sample);
    }
    
    /**
     * Does nothing, mirror method from parent class.
     *
     * override to make not possible for Mgccontainer
     */
    public void updateThreadid(int threadid, Connection c)
    throws FlexDatabaseException
    { } //updateThreadid
    
    /**
     * Insert the container record into database.
     *
     * @param conn <code>Connection</code> to use for insert.
     * @exception FlexDatabaseException.
     */
    public void insert(Connection conn) throws FlexDatabaseException {
        String sql = null;
        if (this.id == -1)
        { this.id = FlexIDGenerator.getID("containerid");}
        sql = 	"insert into containerheader " +
        "(containerid, containertype, locationid, label, threadid) "+
        "values ("+this.id+",'"+this.type+"',"+this.location.getId()+",'"+this.label+"',"+ this.threadid +")";
        
        try {
            DatabaseTransaction.executeUpdate(sql,conn);
            
            String sql1 = "insert into mgccontainer " +
            "(mgccontainerid, filename, oricontainer, marker ,glycerolcontainerid,CULTURECONTAINERID, dnacontainerid) "+
            "values ("+ this.id + ",'" +m_FileName+ "','"+ m_OriginalContainerName + "','" + m_marker + "', " + m_glycerolContainerid+ ", " + m_culture_Containerid+","+m_dnaContainerid+")";
            DatabaseTransaction.executeUpdate(sql1,conn);
            //foreach sample, insert record into containercell and sample table
            Enumeration enu = samples.elements();
            
            while (enu.hasMoreElements()) {
                MgcSample s = (MgcSample)enu.nextElement();
                s.containerid = this.id;
                s.insert(conn);
                
            }
        }
        catch(Exception e)
        {throw new FlexDatabaseException("Error occured while inserting mgc container with id: "+this.id);}
        
        
    }
    
    
    
    
    /**
     * Return the filename from which data was originally exported from of this container.
     * @return The file name string.
     */
    public String getFileName()
    {        return m_FileName;    }
    /**
     * Return the original container name of this container.
     * @return The original container name string.
     */
    public String getOriginalContainer()
    {        return m_OriginalContainerName;    }
    
    
    
    
    /**
     * Set the original container label
     * @param originalcontainer label The label to be set to.
     */
    public void setOriginalContainer(String label)
    {  m_OriginalContainerName = label; }
    
    
    /**
     * Return the marker name of this container.
     * @return The omarker name string.
     */
    public String getMarker()    {        return m_marker;    }
    
    
    
    
    /**
     * Set the marker
     * @param marker The marker to be set to.
     */
    public void setMarker(String marker)
    {  m_marker = marker; }
    /**
     * Set the FileName value
     * @param label The FileName to be set to.
     */
    public void setFileName(String FileName)
    {  m_FileName = FileName; }
    
    /**
     * Return the glycerol container ID associated with the original MGC container.
     *
     * @return The glycerol container ID associated with the original MGC container.
     */
    public int getGlycerolContainerid() {        return m_glycerolContainerid;    }
    public int getCultureContainerid() {        return m_culture_Containerid;    }
    
    /**
     * Set the glycerol container ID to the given value.
     *
     * @param glycerolContainerid The value to be set to.
     */
    public void setGlycerolContainerid(int glycerolContainerid) {        this.m_glycerolContainerid = glycerolContainerid;    }
    public void setCultureContainerid(int cultureContainerid) {        this.m_culture_Containerid = cultureContainerid;    }
    
    /**
     * Return the DNA container ID associated with the original MGC container.
     *
     * @return The DNA container ID associated with the original MGC container.
     */
    public int getDnaContainerid() {        return m_dnaContainerid;    }
    public int setDnaContainerid() {        return m_dnaContainerid;    }
    
    /**
     * Find the MGC container from the MGC culture container. In the FLEXGene
     * database, this MGC culture container is directly generated from MGC
     * container.
     *
     * @param c The MGC culture container.
     * @return The original MGC container as a Container object.
     */
    public static MgcContainer findMGCContainerFromCulture(Container c) {
        String sql = "select containerid from processobject"+
        " where executionid in "+
        " (select executionid from processobject"+
        " where inputoutputflag='O'"+
        " and containerid="+c.getId()+
        " ) and inputoutputflag='I'";
        
        CachedRowSet crs = null;
        int containerid = -1;
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            crs = t.executeQuery(sql);
            
            while(crs.next()) {
                containerid = crs.getInt("CONTAINERID");
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DatabaseTransaction.closeResultSet(crs);
        }
        
        MgcContainer container = null;
        if(containerid != -1) {
            try {
                container = new MgcContainer(containerid);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        
        return container;
    }
    
    
    /**
     * Find the MGC container with the thread id.
     *
     * @param c The MGC culture container.
     * @return The original MGC container as a Container object.
     */
    public static MgcContainer findMGCContainerWithThread(String threadid) {
        String sql = "select c.containerid, mc.filename as filename, mc.oricontainer as orgcontainer, mc.marker as marker, mc.glycerolcontainerid as gl, mc.culturecontainerid as culture,mc.dnacontainerid as dna," +
        " c.containertype as type, c.locationid as locationid, c.label as label, " +
        "l.locationtype as locationtype, l.locationdescription as description "+
        "from containerlocation l, mgccontainer mc , containerheader c where ( mc.mgccontainerid =  c.containerid  and c.label ='MGC" + threadid + "') and " +
        " l.locationid = c.locationid ";
        
        String sql2 = "select c.containerid, mc.filename as filename, mc.oricontainer as orgcontainer, mc.marker as marker, mc.glycerolcontainerid as gl, mc.culturecontainerid as culture,mc.dnacontainerid as dna," +
        " c.containertype as type, c.locationid as locationid, c.label as label, " +
        "l.locationtype as locationtype, l.locationdescription as description "+
        "from containerlocation l, mgccontainer mc , containerheader c where ( mc.mgccontainerid =  c.containerid  and c.threadid= "+ threadid + ") and " +
        " l.locationid = c.locationid ";
        
        CachedRowSet crs = null;
        MgcContainer container = null;
        
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            crs = t.executeQuery(sql);
            
            if (crs.next()){
                int id = crs.getInt("CONTAINERID");
                String fileName = crs.getString("FILENAME");
                String originalContainerName = crs.getString("ORGCONTAINER");
                int glycerolContainerid = crs.getInt("GL");
                int cultureContainerid = crs.getInt("CULTURE");
                int dnaContainerid = crs.getInt("DNA");
                String marker = crs.getString("MARKER");
                String type = crs.getString("TYPE");
                int locationid = crs.getInt("LOCATIONID");
                
                String locationtype = crs.getString("LOCATIONTYPE");
                String description = crs.getString("DESCRIPTION");
                Location location = new Location(locationid, locationtype, description);
                String label = crs.getString("LABEL");
                
                container = new MgcContainer(id, fileName, location,originalContainerName, label, marker, cultureContainerid, glycerolContainerid, dnaContainerid);
            } else {
                crs = t.executeQuery(sql2);
                if(crs.next()) {
                    int id = crs.getInt("CONTAINERID");
                    String fileName = crs.getString("FILENAME");
                    String originalContainerName = crs.getString("ORGCONTAINER");
                    int glycerolContainerid = crs.getInt("GL");
                    int cultureContainerid = crs.getInt("CULTURE");
                    int dnaContainerid = crs.getInt("DNA");
                    String marker = crs.getString("MARKER");
                    String type = crs.getString("TYPE");
                    int locationid = crs.getInt("LOCATIONID");
                    
                    String locationtype = crs.getString("LOCATIONTYPE");
                    String description = crs.getString("DESCRIPTION");
                    Location location = new Location(locationid, locationtype, description);
                    String label = crs.getString("LABEL");
                    
                    container = new MgcContainer(id, fileName, location,originalContainerName, label, marker, cultureContainerid, glycerolContainerid, dnaContainerid);
                }
            }
        } catch (Exception sqlE) {
            System.out.println(sqlE);
        } finally {
            DatabaseTransaction.closeResultSet(crs);
        }
        
        return container;
    }
    
    /**
     * Find the MGC container that contains specified sequence (ei spes. sample).
     *
     * @param seq_id - sequence id.
     * @return The original MGC container as a Container object.
     */
    public static MgcContainer findMGCContainerFromSequenceID(int seq_id) {
        String sql = "select containerid from sample"+
        " where sampleid in "+
        " (select mgccloneid from mgcclone"+
        " where sequenceid = " + seq_id + ") order by containerid";
        
        CachedRowSet crs = null;
        int containerid = -1;
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            crs = t.executeQuery(sql);
            
            while(crs.next()) {
                containerid = crs.getInt("CONTAINERID");
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DatabaseTransaction.closeResultSet(crs);
        }
        
        MgcContainer container = null;
        if(containerid != -1) {
            try {
                container = new MgcContainer(containerid);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        
        return container;
    }
    
    
    /**
     * Update the GLYCEROLCONTAINERID field in MGCCONTAINER table.
     *
     * @param containerid The new glycerol container id that will be updated to the database.
     * @param conn The Connection object.
     * @exception FlexDatabaseException.
     */
    public void updateGlycerolContainer(int containerid, Connection conn) throws FlexDatabaseException {
        String sql = "update MGCCONTAINER set GLYCEROLCONTAINERID="+containerid+
        " where mgccontainerid="+this.id;
        DatabaseTransaction.executeUpdate(sql, conn);
    }
    
    public void updateCultureContainer(int containerid, Connection conn) throws FlexDatabaseException {
        String sql = "update MGCCONTAINER set CULTURECONTAINERID="+containerid+
        " where mgccontainerid="+this.id;
        DatabaseTransaction.executeUpdate(sql, conn);
    }
    
    /**
     * Update the GLYCEROLCONTAINERID field and CULTURECONTAINERID field
     * in MGCCONTAINER table.
     *
     * @param cultureid The new culture container id that will be updated to the database.
     * @param glycerolid The new glycerol container id that will be updated to the database.
     * @param conn The Connection object.
     * @exception FlexDatabaseException.
     */
    public void updateCultureAndGlycerolContainer(int cultureid, int glycerolid, Connection conn) throws FlexDatabaseException {
        String sql = "update MGCCONTAINER set GLYCEROLCONTAINERID="+glycerolid+
        " , CULTURECONTAINERID="+cultureid+
        " where mgccontainerid="+this.id;
        DatabaseTransaction.executeUpdate(sql, conn);
    }
    
    /**
     * Update the GLYCEROLCONTAINERID field and CULTURECONTAINERID field
     * in MGCCONTAINER table.
     *
     * @param cultureid The new culture container id that will be updated to the database.
     * @param glycerolid The new glycerol container id that will be updated to the database.
     * @param conn The Connection object.
     * @exception FlexDatabaseException.
     */
    public void updateCultureAndGlycerolAndDnaContainer(int cultureid, int glycerolid, int dnaid, Connection conn) throws FlexDatabaseException {
        String sql = "update MGCCONTAINER set GLYCEROLCONTAINERID="+glycerolid+
        " , CULTURECONTAINERID="+cultureid+
        " , DNACONTAINERID="+dnaid+
        " where mgccontainerid="+this.id;
        DatabaseTransaction.executeUpdate(sql, conn);
    }
    
    /**
     * Get the data from Sample  & MgcSample table.
     *
     * @exception FlexDatabaseException.
     */
    public void restoreSample() throws FlexDatabaseException {
        samples.removeAllElements();
        
        String sql =  "select s.sampleid as sampleid, s.sampletype as type,  "+
        " s.containerposition as position, s.status_gb as status_gb, "+
        " ms.mgcid as mgcid, ms.imageid as imageid, ms.vector as vector,"+
        "ms.orgcol as acol ,  ms.orgrow as arow, ms.sequenceid as seqid, ms.orientation as orientation,"+
        "ms.status as status  from sample  s, mgcclone  ms "+
        "where s.sampleid = ms.mgccloneid and s.containerid=" + this.id + " order by s.CONTAINERPOSITION "
        
        ;
        
        MgcSample s = null;
        DatabaseTransaction t = null;
        CachedRowSet crs = null;
        try {
            t = DatabaseTransaction.getInstance();
            crs = t.executeQuery(sql);
            while(crs.next()) {
                
                int sampleid = crs.getInt("SAMPLEID");
                String sampletype = crs.getString("TYPE");
                int position = crs.getInt("POSITION");
                String status_gb = crs.getString("STATUS_GB");
                int mgcid = crs.getInt("MGCID");
                int imageid = crs.getInt("IMAGEID");
                String vector = crs.getString("VECTOR");
                int seqId = crs.getInt("SEQID");
                int column = crs.getInt("ACOL");
                String row = crs.getString("AROW");
                String status = crs.getString("STATUS");
                int orientation = crs.getInt("ORIENTATION");
                /* s = new MgcSample(sampleid,  position, this.id,
                mgcid, imageid, vector,
                row, column, seqId, status);*/
               s = new MgcSample(sampleid,  position, this.id,
                mgcid, imageid, vector,
                row, column, seqId, status, orientation);
                
                int cdslength = -1;
                this.addSample(s);
            }
        } catch (SQLException sqlE) {
            throw new FlexDatabaseException("Error occured while initializing mgc sample\n");
        } finally {
            
            DatabaseTransaction.closeResultSet(crs);
        }
    }
    /**
     * Return true if containers contain the sequence.
     *
     * @param seq_id A sequence id to be checked on.
     *
     * @return A boolean value.
     */
    public boolean isSample(int seq_id) {
        
        for (int count = 0; count < samples.size(); count++) {
            MgcSample s = (MgcSample)samples.get(count);
            if (s.getSequenceId() == seq_id)
                return true;
            
        }
        return false;
    }
    /**
     * Finds all containers with the specified label.
     *
     * @param label The container label.
     *
     * @return A list of Container object with the given label.
     * @exception FlexCoreException, FlexDatabaseException.
     */
    public static List findContainers(String label)
    throws FlexCoreException,  FlexDatabaseException {
        
        List containerList = new ArrayList();
        
        String sql = "select c.containerid as containerid, "+
        "c.containertype as containertype, "+
        "c.label as label, "+
        "c.locationid as locationid, "+
        "l.locationtype as locationtype, "+
        "l.locationdescription as description, "+
        "mc.filename as filename, mc.oricontainer as orgcontainer, mc.marker as marker " +
        "from containerheader c, containerlocation l, mgccontainer mc "+
        "where ( c.locationid = l.locationid and mc.mgccontainerid = c.containerid) "+
        "and c.label = '"+ label+"'";
        
        ResultSet rs = null;
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next()) {
                Location curLocation = new Location(
                rs.getInt("LOCATIONID"),
                rs.getString("LOCATIONTYPE"),
                rs.getString("DESCRIPTION" ));
                
                MgcContainer curContainer = new MgcContainer(
                rs.getInt("CONTAINERID"),
                rs.getString("FILENAME"),
                curLocation,
                rs.getString("ORGCONTAINER"),
                rs.getString("LABEL"),
                rs.getString("MARKER"));
                
                curContainer.restoreSample();
                containerList.add(curContainer);
            }
        } catch (NullPointerException ex) {
            throw new FlexCoreException("Error occured while initializing mgc container with label: "+label+"\n"+ex.getMessage());
        } catch (SQLException sqlE) {
            throw new FlexDatabaseException("Error occured while initializing mgc container from labe: "+label+"\n");
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        return containerList;
    }
    
    
    public static List findContainersFormView(String label)
    throws FlexCoreException,  FlexDatabaseException {
        
        List containerList = new ArrayList();
        
        String sql = "select  containerid,  containertype, label, "+
        "locationid,locationtype, description,  filename,  orgcontainer,  marker " +
        "from MGC_CONTAINER_BY_LABEL "+
        "where and c.label = '"+ label+"'";
        
        ResultSet rs = null;
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next()) {
                Location curLocation = new Location(
                rs.getInt("LOCATIONID"),
                rs.getString("LOCATIONTYPE"),
                rs.getString("DESCRIPTION" ));
                
                MgcContainer curContainer = new MgcContainer(
                rs.getInt("CONTAINERID"),
                rs.getString("FILENAME"),
                curLocation,
                rs.getString("ORGCONTAINER"),
                rs.getString("LABEL"),
                rs.getString("MARKER"));
                
                curContainer.restoreSample();
                containerList.add(curContainer);
            }
        } catch (NullPointerException ex) {
            throw new FlexCoreException("Error occured while initializing mgc container with label: "+label+"\n"+ex.getMessage());
        } catch (SQLException sqlE) {
            throw new FlexDatabaseException("Error occured while initializing mgc container from labe: "+label+"\n");
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        return containerList;
    }
    
    
    public String toString() 
    {
            StringBuffer str = new StringBuffer();
            str.append("Container "+this.getOriginalContainer() + " Marker " + m_marker +"\n");
            MgcSample sample = null;
            for (int mcount =0; mcount < this.getSamples().size(); mcount++)
            {
                sample = (MgcSample)this.getSamples().get(mcount);
                str.append(sample.toString() +"\n");
            }
            return str.toString();
     }
    
    
    /************************TESTING*************************
     *1890*/
    public static void main(String args[]) {
        
        
        try{
            
            // MgcContainer aa = new MgcContainer(1890);
            // MgcContainer aa = MgcContainer.findMGCContainerFromSequenceID(37718);
            // aa.restoreSample();
            System.out.println(System.currentTimeMillis());
            Container.findContainers("MGC000001");
            System.out.println(System.currentTimeMillis());
            Container.findContainersFromView("MGC000001");
            System.out.println(System.currentTimeMillis());
            
        }catch(Exception e)
        {System.out.println("A");}
        
        
        
    }
}
