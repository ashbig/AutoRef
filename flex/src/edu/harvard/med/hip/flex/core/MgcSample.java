/**
 * $Id: MgcSample.java,v 1.2 2005-03-01 16:38:52 Elena Exp $
 *
 * File     	: MgcClone.java
 * Date     	: 05052002
 * Author	: Helen Taycher
 *
 * Revision	:
*/
package edu.harvard.med.hip.flex.core;

import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.util.*;
import edu.harvard.med.hip.flex.process.Protocol;

import java.math.BigDecimal;
import java.util.*;
import java.sql.*;
import javax.sql.*;
import sun.jdbc.rowset.*;


public class MgcSample extends Sample{
    
    public final static String STATUS_AVAILABLE = "AVAILABLE";
    public final static String STATUS_NO_GROW = "NO GROW";
     public final static String STATUS_NO_SEQUENCE = "NO SEQUENCE";
      public final static String STATUS_BAD_SEQUENCE = "BAD SEQUENCE";
    private int m_MgcId;
    private int m_ImageId;
    private String m_Vector;
    private int m_SequenceId = -1;
    private String m_Row;
    private int m_Column;
    
    private String m_Status;    
    
    
    /**
     * Constructor.
     *
     * @param id The primary key of the sample & mgcsample tables.
     * @param type The type of the mgc sample.
     * @param position The position of the mgc sample on the container.
     * @param containerid The container id that mgc the sample is on.
     *
     * @param status The status of the sample.
     *
     * @return A MgcSample object.
     */
    public MgcSample(int id,  int position, int containerid, 
                    int mgcId, int imageId, String vector, String row, int column,
                    int seqId, String clone_status) 
    {
        super( id,  Sample.ISOLATE,  position, containerid, -1, -1,  Sample.GOOD) ;
        m_Column = column;
        m_Row = row;
        m_MgcId = mgcId;
        m_SequenceId = seqId;
        m_Vector = vector;
        m_ImageId = imageId;
        m_Status = clone_status;
    }
    
    /**
     * Constructor.
     *
     * @param id The primary key of the sample & mgcsample tables.
    
     * @param position The position of the mgc sample on the container.
     * @param containerid The container id that the mgc sample is on.
    
     * @param image id - The image id of the mgc clone
     * @param mgc id - The mgc id of the mgc clone
     * @param sequenceid - The sequence id of the mgc clone
     * @param vector - The vector of the mgc clone
     * @param row/column - The row / Column of the mgc clone in the units of 
     * original master list file
     *
     * @return A MgcSample object.
     */
    public MgcSample(int id,  int containerid,  
                    int mgcId, int imageId, String vector, String row, int column,
                    String clone_status) throws FlexDatabaseException
    {
        super( id,  Sample.ISOLATE,  -1,  containerid, -1,-1,  Sample.GOOD) ;
        int a_value = (int) 'a';
        int first_char_value = 0;
        int second_char_value = 0;
        int row_value = 0;
        m_MgcId = mgcId;
        m_ImageId = imageId;
        m_Vector = vector;
        m_Row = row;
        m_Column = column;
        m_Status = clone_status;
        
        
        first_char_value = (int)row.charAt(0) - a_value + 1;
        if ( row.length() > 1) 
        {
            second_char_value = (int)row.charAt(1) - a_value + 1;
            row_value = first_char_value * 27 + second_char_value;
        }
        else
        {
            row_value = first_char_value;
        }
        this.position = (column - 1) * 8 +  row_value ;
  
    }
    public MgcSample(int id,  int containerid,  
                     int imageId, String vector, String row, int column,
                    String clone_status) throws FlexDatabaseException
    {
        super( id,  Sample.ISOLATE,  -1,  containerid, -1,-1,  Sample.GOOD) ;
        int a_value = (int) 'a';
        int first_char_value = 0;
        int second_char_value = 0;
        int row_value = 0;
      //  m_MgcId = mgcId;
        m_ImageId = imageId;
        m_Vector = vector;
        m_Row = row;
        m_Column = column;
        m_Status = clone_status;
        
        
        first_char_value = (int)row.charAt(0) - a_value + 1;
        if ( row.length() > 1) 
        {
            second_char_value = (int)row.charAt(1) - a_value + 1;
            row_value = first_char_value * 27 + second_char_value;
        }
        else
        {
            row_value = first_char_value;
        }
        this.position = (column - 1) * 8 +  row_value ;
  
    }
    
    /**
     * Constructor.
     *
     * @param id The mgc clone id.
     *
     * @return A Mgcclone object with id.
     * @exception FlexCoreException, FlexDatabaseException.
     */
    public MgcSample(int id) throws FlexCoreException, FlexDatabaseException 
    {
        super( id) ;
        
        this.cdslength = -1;
        String sql = "select * from mgcclone where mgccloneid=" + id ;
        
        CachedRowSet crs = null;
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            crs = t.executeQuery(sql);
            
            if(crs.size()==0) 
            {
                throw new FlexCoreException("Cannot initialize MgcClone with id: "+id);
            }
            
            
            while(crs.next()) {
                m_MgcId = crs.getInt("mgcid");
                m_ImageId = crs.getInt("IMAGEID");
                m_Vector = crs.getString("VECTOR");
                m_Row = crs.getString("ORGROW");
                m_Column = crs.getInt("ORGCOL");
                m_Status = crs.getString("STATUS");
                m_SequenceId = crs.getInt("SEQUENCEID");
            }
        } catch (NullPointerException ex) {
            throw new FlexCoreException("Error occured while initializing mgc clone with id: "+id+"\n"+ex.getMessage());
        } catch (SQLException sqlE) {
            throw new FlexDatabaseException("Error occured while initializing mgc clone with id: "+id+"\n"+sqlE+"\nSQL: "+sql);
         } catch (Exception q) {
             System.out.println(q.getMessage());
        } finally {
            DatabaseTransaction.closeResultSet(crs);
        }
    }
    
    public String toString() 
    {
        return "Mgc clone: MgcId - " + Integer.toString(m_MgcId) +
                ", ImageId - " + Integer.toString(m_ImageId) + 
                " Vector - " + m_Vector +
                " Row - " + m_Row +
               " Col - " + m_Column +
               " Id - " + this.id + " type - " + this.type;
    }
    
    
    /**
     *Inserts mgc clone information into database
     */
    public void insert(Connection conn) throws FlexDatabaseException
    {
        if (id == -1) id = FlexIDGenerator.getID("sampleid");
        //hack
       
        String sql = "insert into sample \n"+
        "(sampleid, sampletype, containerid, containerposition ";
        String valuesql = "values (" + id + ",'"+ type +"'," + containerid+","+position;
              
        if(status != null) {
            sql = sql + ",status_gb";
            valuesql = valuesql + ",'"+status+"'";
        }
        sql = sql + ")\n"+valuesql + ")";
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            sql = "insert into containercell (containerid, position, sampleid) " +
            "values(" + containerid + ","+position+","+id+")";
            stmt.executeUpdate(sql);
            sql = "insert into mgcclone (mgccloneid, mgcid, imageid, vector, orgrow, orgcol, sequenceid, status) ";
            if (m_SequenceId > 0)
            {
            valuesql = "values ("+ id + "," + m_MgcId +","+ m_ImageId +",'"+ m_Vector + "','"+ m_Row+"',"+ m_Column + "," +
                                    m_SequenceId + ",'" + m_Status + "')" ;
            }
            else
            {
                valuesql = "values ("+ id + "," + m_MgcId +","+ m_ImageId +",'"+ m_Vector + "','"+ m_Row+"',"+ m_Column + ",null,'" + m_Status + "')" ;
            }
            stmt.executeUpdate(sql + valuesql);                         
        } catch (SQLException sqlE) {
            throw new FlexDatabaseException(sqlE+"\nSQL: "+sql);
            
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }
      
     
    }
    
     public int getPosition( String row, int column) 
    {
        
        int a_value = (int) 'a';
        int first_char_value = 0;
        int second_char_value = 0;
        int row_value = 0;
        row = row.toLowerCase();       
        first_char_value = (int)row.charAt(0) - a_value + 1;
        if ( row.length() > 1) 
        {
            second_char_value = (int)row.charAt(1) - a_value + 1;
            row_value = first_char_value * 27 + second_char_value;
        }
        else
        {
            row_value = first_char_value;
        }
        this.position = (column - 1) * 8 +  row_value ;
        return this.position;
  
    }
    
     
     /**
     *Updates sequence id of the clone (available/ not available
     **/ 
  
      /**
     *Updates sequence id of the clone (available/ not available
     **/ 
    public void updateSequence(Connection conn) throws FlexDatabaseException
    {
         DatabaseTransaction dt = DatabaseTransaction.getInstance();
        String sql =
        "update MgcClone " +
        "set sequenceid = '"+ m_SequenceId +"' "+
        "where mgccloneid= " + this.id ;
        dt.executeUpdate(sql, conn);
      
    }
    
    
     /**
     *Updates mgc clone  of the clone (available/ not available
     **/ 
    public void updateCloneStatus(Connection conn) throws FlexDatabaseException
    {
         DatabaseTransaction dt = DatabaseTransaction.getInstance();
        String sql =
        "update MgcClone " +
        "set status = '"+ m_Status +"' "+
        "where mgccloneid= " + this.id ;
        dt.executeUpdate(sql, conn);
      
    }
    /**
     * Gets the sequence for mgc_sample.
     *
     * @return the FlexSequence for the mgc sample.
     */
    public FlexSequence getFlexSequence() throws FlexDatabaseException {
       
        return new FlexSequence(m_SequenceId);
    }
    
    /**
     * Return the Mgc id.
     *
     * @return The Mgc id.
     */
    public int getMgcId(){ return m_MgcId; }
    /**
     * Return the image  id.
     *
     * @return The image id.
     */
    public int getImageId(){ return m_ImageId; }
    /**
     * Return the Vector .
     *
     * @return The vector .
     */
    public String getVector(){ return m_Vector; }
    /**
     * Return the row.
     *
     * @return The row.
     */
    public String getRow(){ return m_Row; }
    /**
     * Return the status.
     *
     * @return The status.
     */
     public String getStatus() { return m_Status ;    }
    /**
     * Return the column.
     *
     * @return The column.
     */
    public int getColumn(){ return m_Column; }
    /**
     * Return the sequence id.
     *
     * @return The sequence id.
     */
    public int getSequenceId(){ return m_SequenceId; }
    /**
     * Set the sequence id to the given value.
     * @param sequence id The value to be set to.
     */
    public void setSequenceId(int Sequence_id) { m_SequenceId = Sequence_id;   }
    /**
     * Set the Mgc_id to the given value.
     * @param Mgc id The value to be set to.
     */
    public void setMgcId(int mgc_id) { m_MgcId = mgc_id;   }
    /**
     * Set the image id to the given value.
     * @param image id The value to be set to.
     */
    public void setImageId(int image_id) { m_ImageId = image_id;   }
    /**
     * Set the vector to the given value.
     * @param vector The value to be set to.
     */
    public void setVecot(String vector) { m_Vector = vector;   }
     /**
     * Set the status to the given value.
     * @param status The value to be set to.
     */
    public void setStatus(String status) { m_Status = status;   }
    /**
     * Set the sequence id to the given value.
     * @param sequence id The value to be set to.
     */
    public void setRow(String row) { m_Row = row;   }
    /**
     * Set the column to the given value.
     * @param column The value to be set to.
     */
    public void setColumn(int column) { m_Column = column;   }
    
     //******************************************************//
    //			Testing				//
    //******************************************************//
    public static void main(String args[])
    {
       
        
        try{
            /*
            int id = FlexIDGenerator.getID("sampleid");
             MgcSample sa = new MgcSample(-1, -1, 10000,  10000,"abc", "a",1, MgcSample.STATUS_AVAILABLE);
             System.out.println(sa.getPosition()); 
             sa = new MgcSample(-1, -1, 10000,  10000,"abc", "a",12, MgcSample.STATUS_AVAILABLE);
              System.out.println(sa.getPosition()); 
                sa = new MgcSample(-1, -1, 10000,  10000,"abc", "f",1, MgcSample.STATUS_AVAILABLE);
                 System.out.println(sa.getPosition()); 
             sa = new MgcSample(-1, -1, 10000,  10000,"abc", "f",12, MgcSample.STATUS_AVAILABLE);
              System.out.println(sa.getPosition()); 
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            Connection c = t.requestConnection();
            sa.insert(c);
             **/
            MgcSample aa = new MgcSample(354673);
           // System.out.println(sa.toString());
        
        }catch(Exception e){};
            
   
       
    }
    
   
    
}
