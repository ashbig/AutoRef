/**
 * $Id: Container.java,v 1.1 2003-03-27 17:30:28 Elena Exp $
 *
 * File     	: Container.java

 */
package edu.harvard.med.hip.bec.sampletracking;

import java.util.*;
import java.math.BigDecimal;
import java.sql.*;
import javax.sql.*;

import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.file.*;

import sun.jdbc.rowset.*;

/**
 * Generic representation of all types of containers.
 */
public class Container
{
    private int         m_id = -1;
    private String      m_type = null;
    private Location    m_location = null;
    private int         m_locationid = -1;
    private String      m_label = null;
    private ArrayList   m_samples = new ArrayList();
    private int         m_threadid = -1;
    private int         m_last_step = -1;
    /**
     * Constructor.
     *
     * @param id The container id.
     *
     * @return A Container object with id.
     * @exception BecUtilException, BecDatabaseException.
     */
    public Container(int id) throws BecUtilException, BecDatabaseException
    {
        m_id = id;
        
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
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            crs = t.executeQuery(sql);
            
            if(crs.size()==0)
            {
                throw new BecUtilException("Cannot initialize Container with id: "+id);
            }
            
            
            while(crs.next())
            {
                
                m_type = crs.getString("CONTAINERTYPE");
                int m_locationid = crs.getInt("LOCATIONID");
               
                m_location = new Location(m_locationid, crs.getString("LOCATIONTYPE"), crs.getString("LOCATIONDESCRIPTION"));
                m_label = crs.getString("LABEL");
                m_threadid = crs.getInt("THREADID");
                m_last_step = crs.getInt("");
            }
        } catch (NullPointerException ex)
        {
            throw new BecUtilException("Error occured while initializing container with id: "+id+"\n"+ex.getMessage());
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException("Error occured while initializing container with id: "+id+"\n"+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(crs);
        }
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
     * @exception BecDatabaseException.
     */
    public Container(int id, String type, Location location, String label, 
                    int status, int threadid) throws BecDatabaseException
    {
        m_type = type;
        m_location = location;
        m_locationid = m_location.getId();
        m_label = label;
        m_threadid = threadid;
        if (id == BecIDGenerator.BEC_OBJECT_ID_NOTSET)
            m_id = BecIDGenerator.getID("containerid");
        else
            m_id = id;
    }
    
    public Container(int id, String type, int location, String label, 
                    int status, int threadid) throws BecDatabaseException
    {
        m_type = type;
        
        m_locationid = location;
        m_label = label;
        m_threadid = threadid;
        if (id == BecIDGenerator.BEC_OBJECT_ID_NOTSET)
            m_id = BecIDGenerator.getID("containerid");
        else
            m_id = id;
    }
    
    /**
     * Finds all containers with the specified label.
     *
     * @param label The container label.
     *
     * @return A list of Container object with the given label.
     * @exception BecUtilException, BecDatabaseException.
     */
    public static List findContainers(String label) throws BecUtilException,
    BecDatabaseException
    {
        
        List containerList = new LinkedList();
        
        String sql = "select c.containerid as containerid, "+
        "c.containertype as containertype, "+
        "c.label as label, "+
        "c.locationid as locationid, "+
        "c.threadid as threadid, "+
        "l.locationtype as locationtype, "+
        "l.locationdescription as description\n"+
        "from containerheader c, containerlocation l\n"+
        "where c.locationid = l.locationid\n"+
        "and c.label = '"+ label+"'";
        ResultSet rs = null;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next())
            {
                
                int id = rs.getInt("CONTAINERID");
                Container curContainer = new Container(id);
                curContainer.restoreSample();
                containerList.add(curContainer);
            }
        } catch (NullPointerException ex)
        {
            throw new BecUtilException("Error occured while initializing container with label: "+label+"\n"+ex.getMessage());
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException("Error occured while initializing container from labe: "+label+"\n"+"\nSQL: "+sqlE);
        } finally
        {
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
     * @exception BecUtilException, BecDatabaseException.
     */
    public static List findContainersFromView(String label) throws BecUtilException,
    BecDatabaseException
    {
        
        List containerList = new LinkedList();
        
        String sql = "select containerid, containertype, label, locationid, "+
        "threadid, locationtype,  description "+
        "from CONTAINER_BY_LABEL where label = '"+ label+"'";
        ResultSet rs = null;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next())
            {
                
                int id = rs.getInt("CONTAINERID");
                Container curContainer = new Container(id);
                curContainer.restoreSample();
                containerList.add(curContainer);
            }
        } catch (NullPointerException ex)
        {
            throw new BecUtilException("Error occured while initializing container with label: "+label+"\n"+ex.getMessage());
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException("Error occured while initializing container from labe: "+label+"\n"+"\nSQL: "+sqlE);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
        return containerList;
    }
    
    /**
     * Return the container id.
     *
     * @return The container id.
     */
    public int getId()    {        return m_id;    }
    
    /**
     * Return the label of this container.
     *
     * @return The label string.
     */
    public String getLabel()    {        return m_label;    }
    
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
    
    /*
    public static String getLabel(String projectCode, String processcode, int threadid, String subthreadid)
    {
        java.text.NumberFormat fmt = java.text.NumberFormat.getInstance();
        fmt.setMaximumIntegerDigits(6);
        fmt.setMinimumIntegerDigits(6);
        fmt.setGroupingUsed(false);
        
        if((subthreadid == null) || (subthreadid.trim().length() == 0))
            return (projectCode+processcode+fmt.format(threadid));
        else
            return (projectCode+processcode+fmt.format(threadid)+"-"+subthreadid);
    }
    */
    /**
     * Return the location of the container.
     *
     * @return The location of the container.
     */
    public Location getLocation()  throws BecDatabaseException  ,BecUtilException
    {       
        if (m_location == null) 
            m_location = new Location(m_locationid); 
        return m_location;   
    }
     public int    getLocationId()    {        return m_locationid;    }
    /**
     * Set the container location to the given value.
     *
     * @param location The location to be set to.
     */
    public void setLocation(Location location)    {        m_location = location;    }
    
    /**
     * Return the threadid.
     *
     * @return The thread id.
     */
    public int getThreadid()    {        return m_threadid;    }
    
    public ArrayList getContainerHistory()    {        return null;    }
    public static ArrayList getContainerHistory(int containerid)    {        return null;    }
    
    /**
     * Set the threadid.
     *
     * @param threadid The value to be set to.
     */
    public void setThreadid(int threadid)    {        m_threadid = threadid;    }
    
    /**
     * Set the container label
     * @param label The label to be set to.
     */
    public void setLabel(String label)    {        m_label = label;    }
    
    /**
     * Return the type of the container.
     *
     * @return The container type.
     */
    public String getType()    {        return m_type;    }
    
    /**
     * Return a list of Sample objects.
     *
     * @return A list of Sample objects.
     */
    public ArrayList getSamples()    {        return m_samples;    }
    
    /**
     * Get the Sample object at a certain position.
     *
     * @param position The position that the sample located. Based on 1.
     *
     * @return A Sample object.
     *
     * @exception The BecUtilException when the samples is null.
     */
    public Sample getSample(int position) throws BecUtilException
    {
        if (m_samples != null)
        {
            return (Sample)m_samples.get(position-1);
        }
        else
        {
            throw new BecUtilException("samples not exists at position: "+position);
        }
    }
    
    /**
     * Add a Sample object to a list of Sample objects.
     *
     * @param sample A Sample object to be added to the list.
     */
    public void addSample(Sample sample)    {        m_samples.add(sample);    }
    
   
    /**
     * Get the data from Sample table.
     *
     * @exception BecDatabaseException.
     */
    public void restoreSample() throws BecDatabaseException
    {
        /*
        samples.removeAllElements();
        
        String sql = "select * from sample where containerid="+id+" order by CONTAINERPOSITION";
        
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        CachedRowSet crs = t.executeQuery(sql);
        ResultSet rs = null;
        try
        {
            while(crs.next())
            {
                
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
                
                String status = crs.getString("STATUS_GB");
                
                Sample s = new Sample(sampleid, sampletype, containerposition, id, constructid, oligoid, status);
                int cdslength = -1;
                
                if(!Sample.CONTROL_POSITIVE.equals(sampletype)&&!Sample.CONTROL_NEGATIVE.equals(sampletype))
                {
                    String newSql = "select distinct f.cdslength as cdslength from flexsequence f, "+
                    "constructdesign c, sample s where f.sequenceid = "+
                    "c.sequenceid and (s.constructid = c.constructid "+
                    "or s.oligoid=c.oligoid_5p) and s.sampleid = "+sampleid;
                    rs = t.executeQuery(newSql);
                    if(rs.next())
                    {
                        cdslength = rs.getInt("CDSLENGTH");
                    }
                }
                
                s.setCdslength(cdslength);
                m_addSample(s);
            }
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException("Error occured while initializing sample\n"+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeResultSet(crs);
        }
         **/
    }
    /**
     * Insert the container record into database.
     *
     * @param conn <code>Connection</code> to use for insert.
     * @exception BecDatabaseException.
     */
    public void insert(Connection conn) throws BecDatabaseException
    {
        String sql = 	"insert into containerheader " +
        "(containerid, containertype, locationid, label, threadid) "+
        "values ("+m_id+",'"+m_type+"',"+m_location.getId()+",'"+m_label+"',"+m_threadid+")";
        
        DatabaseTransaction.executeUpdate(sql,conn);
        
        //foreach sample, insert record into containercell and sample table
        for (int ind = 0; ind < m_samples.size(); ind++)
       
        {
            Sample s = (Sample)m_samples.get(ind);
            s.insert(conn);
        }
    }
    
   
    
    
    /**
     * Two containers are equal when there ids are set and are equal
     *
     * @param obj The object to compare to
     *
     * @return true when equal, false otherwise
     */
    public boolean equals(Container container)
    {
        boolean retValue= false;
        if (container !=null)
        {
           
            if(container.getId() >0 && container.getId() == m_id)
            {
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
     * @exception The BecDatabaseException.
     */
    public void updateLocation(int locationid, Connection c) throws BecDatabaseException
    {
        String sql = "update containerheader "+
        " set locationid="+locationid+
        " where containerid="+m_id;
        
        DatabaseTransaction.executeUpdate(sql, c);
    }
    
    /**
     * Update the location record to the database.
     *
     * @param locationid The new locationid to be updated to.
     * @param containerid The container id for container to be updated to.
     * @param c The connection used for database update.
     * @exception The BecDatabaseException.
     */
    public static void updateLocation(int locationid ,int containerid, Connection c) throws BecDatabaseException
    {
        String sql = "update containerheader "+
        " set locationid="+locationid+
        " where containerid="+containerid;
        
        DatabaseTransaction.executeUpdate(sql, c);
    }
  
    
    /**
     * Update the threadid record to the database.
     *
     * @param threadid The threadid to be updated to.
     * @param c The connection used for database update
     * @exception The BecDatabaseException.
     */
    
    /*
    public void updateThreadid(int threadid, Connection c) throws BecDatabaseException
    {
        String sql = "UPDATE containerheader\n"+
        " SET threadid="+threadid+
        " WHERE containerid="+m_id;
        
        DatabaseTransaction.executeUpdate(sql, c);
    } //updateThreadid
    */
   
    /**
     * Static method to find the next container from the previous one
     * for certain protocol.
     *
     * @param mgc The rearrayed MGC container.
     * @return The DNA template plate. Return null if not found.
     */
    
    /*
    public static Container findNextContainerFromPrevious(Container mgc, String protocolName)
    {
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
        try
        {
            t = DatabaseTransaction.getInstance();
            crs = t.executeQuery(sql);
            if(crs.next())
            {
                int containerid = crs.getInt("CONTAINERID");
                c = new Container(containerid);
            }
        } catch (Exception ex)
        {
            System.out.println(ex);
        } finally
        {
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
    
    /*
    public static Container findNextContainerFromPrevious(int id, String protocolName)
    {
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
        try
        {
            t = DatabaseTransaction.getInstance();
            crs = t.executeQuery(sql);
            if(crs.next())
            {
                int containerid = crs.getInt("CONTAINERID");
                c = new Container(containerid);
            }
            
            
        } catch (Exception ex)
        {
            System.out.println(ex);
        } finally
        {
            DatabaseTransaction.closeResultSet(crs);
        }
        
        return c;
    }
    
    /**
     * Static method to find the DNA template plate from the rearrayed
     * MGC container.
     *
     * @param mgc The rearrayed MGC container.
     * @return The DNA template plate. Return null if not found.
     */
    /**    public static Container findTemplateFromRearrayedMgc(Container mgc) {
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
                    " and p.processname='"+Protocol.CREATE_DNA_FROM_REARRAYED_CULTURE+"'"+
                    " ) and inputoutputflag = '"+ProcessObject.INPUT+"'"+
                    " and containerid = "+mgc.getId()+
                    " )";
 
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
     */
    
    //**************************************************************//
    //				Test				//
    //**************************************************************//
    
    // These test cases also include tests for Sample class.
    public static void main(String args[]) throws Exception
    {
        try
        {
            System.out.println(System.currentTimeMillis());
            Container.findContainers("MAB000206-F");
            System.out.println(System.currentTimeMillis());
            Container.findContainersFromView("MAB000206-F");
            System.out.println(System.currentTimeMillis());
        }
        catch(Exception e)
        {
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
         
        }catch(BecDatabaseException ex){
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
         
         
            ArrayList samples = plate.getSamples();
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
            ArrayList s1 = p1.getSamples();
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
        } catch (BecDatabaseException exception) {
            System.out.println(exception.getMessage());
        } catch (BecUtilException e) {
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
