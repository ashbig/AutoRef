/*
 * PlateUploader.java
 *
 * Created on April 9, 2003, 2:28 PM
 *class takes in list of plate names from FLEX and
 *trnsfers their info into BEC
 */

package edu.harvard.med.hip.bec.modules;


import edu.harvard.med.hip.bec.coreobjects.sequence.*;

import edu.harvard.med.hip.bec.database.*;
import  edu.harvard.med.hip.bec.util.*;
import  edu.harvard.med.hip.bec.bioutil.*;
import edu.harvard.med.hip.bec.Constants;
import edu.harvard.med.hip.bec.sampletracking.objects.*;
import edu.harvard.med.hip.bec.coreobjects.endreads.*;
import sun.jdbc.rowset.*;
import java.util.*;
import java.sql.Date;
import java.io.*;
import java.math.BigDecimal;
import java.sql.*;




/**
 *
 * @author  htaycher
 */
public class PlateUploader
{
    public static final int PLATE_NAMES = 1;
    public static final int PLATE_IDS = 2;
    
    private ArrayList m_plate_names = null;
    private int       m_array_type = -1;
    
    
    
    private Connection  m_flex_connection = null;
    private Connection  m_bec_connection = null;
    /** Creates a new instance of PlateUploader */
    public PlateUploader(ArrayList plate_names, int mode)
    {
        m_plate_names = plate_names;
        m_array_type = mode;
        
    }
    
    
    
    public ArrayList  upload()
    {
        ArrayList errors = new ArrayList();
        //get all reference sequences from bec: to prevent multipal upload of the same sequence
        //Hashtable refsequences = getAllRefSequences(m_bec_connection);
        try
        {
            m_flex_connection = DatabaseTransaction.getInstance(DatabaseTransaction.FLEX_url , DatabaseTransaction.FLEX_username, DatabaseTransaction.FLEX_password).requestConnection();
            m_bec_connection = DatabaseTransaction.getInstance(DatabaseTransaction.BEC_url, DatabaseTransaction.BEC_username, DatabaseTransaction.BEC_password).requestConnection();
        }
        catch(Exception e)
        {
            errors.add("Cannot open connection to database");
            return errors;
        }
        for (int count = 0 ; count < m_plate_names.size(); count++)
        {
            try
            {
                uploadPlate( (String) m_plate_names.get(count) );
            }
            catch(BecDatabaseException e)
            {
                errors.add(e.getMessage());
            }
        }
        try
        {
            m_flex_connection.close();
            m_flex_connection.close();
            return errors;
        }
        catch(Exception e)
        {
            errors.add("Cannot close connection to database");
            return errors;
        }
    }
    
    //function uploads data for one plate into BEC
    public void uploadPlate( String platename)throws BecDatabaseException
    {
       ArrayList isolate_tracking_engines = new ArrayList();
       ArrayList construct_tracking_engines = new ArrayList();
       ArrayList sequences = new ArrayList();
        //get container from flex
        Container newBecContainer = findContainer(platename, m_flex_connection);
        //get constructs info: attach construct to the
        
        //get sequences
        
    }
    
    
    private   Container findContainer(String plate_param, Connection conn) throws     BecDatabaseException
    {
        String sql = null;Container newBecContainer = null;
        if ( m_array_type == PLATE_NAMES)
        {
            sql = "select containerid , containertype , label" +
            "from containerheader  where label = '"+ plate_param+"'";
        }
        else if (m_array_type == PLATE_IDS)
        {
            sql = "select containerid , containertype , label" +
            "from containerheader  where containerid = '"+ plate_param+"'";
        }
        if (sql == null) return null; //not proper call was made
        ArrayList samples = null;
        ResultSet rs = null;
        try
        {
            rs = DatabaseTransaction.getInstance().executeQuery(sql, conn);
            while(rs.next())
            {
                int id = rs.getInt("CONTAINERID");
                String label = rs.getString("LABEL");
                newBecContainer = new Container( -1, Container.TYPE_SEQUENCING_CONTAINER,   label, Container.STATUS_SUBMITTED);
                newBecContainer.setSamples( restoreFLEXSample(id, conn) );
            }
            return newBecContainer;
        }
        catch (NullPointerException ex)
        {
            throw new BecDatabaseException("Error occured while initializing container with plate param: "+plate_param+"\n"+ex.getMessage());
        }
        catch (SQLException sqlE)
        {
            throw new BecDatabaseException("Error occured while initializing container from plate param: "+plate_param+"\n"+"\nSQL: "+sqlE);
        }
        finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
        
    }
    
    
    //get samples from FLEX
    private ArrayList restoreFLEXSample(int id, Connection conn) throws BecDatabaseException
    {
        String sql =  "select containerid , containertype , label" +
        "from containerheader  where label = '"+ id+"'";
        
        ArrayList samples = new ArrayList();
        ResultSet rs = null;
        try
        {
            rs = DatabaseTransaction.getInstance().executeQuery(sql, conn);
            while(rs.next())
            {
                
            }
            return samples;
        }
        catch (Exception sqlE)
        {
            throw new BecDatabaseException("Error occured while initializing container from plate parameter: "+id+"\n"+"\nSQL: "+sqlE);
        }
        finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
        
    }
 
    
    //             prevent uploading of the same sequence several times    
    
    //get all refseq from BEC so that we won't upload the same sequence several times
    private Hashtable   getAllRefSequences (Connection conn)throws BecDatabaseException
    {
        Hashtable refseq = new Hashtable();
         String sql = "select i.flexsequenceid as flexsequenceid, c.refsequenceid as becsequenceid "+
        "from  construct c, isolatetracking i " +
        "where c.constructid = i.constructid   ";
       
        CachedRowSet crs = null;
        int refid = -1; int becid = -1;
        try
        {
            crs = DatabaseTransaction.getInstance().executeQuery(sql,m_bec_connection);
            
            while(crs.next())
            {
                refid = crs.getInt("refSEQUENCEID");
                becid = crs.getInt("becsequenceid");
                refseq.put( new Integer(refid), new UploadedRefSequence( refid, becid));
            }
            return refseq;
            
        } catch (Exception e)
        {
            throw new  BecDatabaseException("Can not get refsequences from database");
            
        } finally
        {
            DatabaseTransaction.closeResultSet(crs);
        }
    }
    
    class UploadedRefSequence
    {
        private int i_flex_ref_seq_id = -1;
        private int i_bec_ref_seq_id = -1;
        
        public UploadedRefSequence(int flexid, int becid)
        {
            i_flex_ref_seq_id = flexid;
            i_bec_ref_seq_id = becid;
        }
        
        public int  getFlexRefseqid(){ return i_flex_ref_seq_id;}
        public int  getBecRefseqid(){ return i_bec_ref_seq_id;}
    }
}
