/*
 * BaseSequence.java
 * represent base sequence which is coding sequence for  flexsequence 
 *or just sequence text for any other
 * Created on September 20, 2002, 2:01 PM
 */

package edu.harvard.med.hip.flex.seqprocess.core.sequence;


import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.util.*;
import java.sql.*;
/**
 *
 * @author  htaycher
 */
public abstract class BaseSequence
{
    
    
    
    
    protected int m_id = -1;
    private String m_text = null;
    protected int m_type = -1;
    
    
    /** Creates a new instance of BaseSequence */
    public BaseSequence(String t, int tp)throws FlexDatabaseException
    {
       // m_id = ;
        m_text = t;
        m_type = tp;
    }
    
    public BaseSequence(int id) throws FlexDatabaseException
    {
        m_id = id;
        String sql = null;
        if (this instanceof FullSequence)
             sql = "select SEQUENCETEXT from seq_sequencetext where sequenceid="+id+" order by sequenceorder";
        if (this instanceof TheoreticalSequence)
             sql = "select SEQUENCETEXT from sequencetext where sequenceid="+id+" order by sequenceorder";
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        ResultSet rs = null;
        try
        {
            rs = t.executeQuery(sql);
            while(rs.next())
            {
                if (m_text == null)
                        m_text = rs.getString("SEQUENCETEXT");
                else
                    m_text +=rs.getString("SEQUENCETEXT");
            }
            
        } catch (SQLException sqlE)
        {
            throw new FlexDatabaseException("Error occured while restoring sequence with id "+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
    
    public  void insert(Connection conn)throws FlexDatabaseException
    {
        
        String text = "";
        String sql = "";
        Statement stmt =null;
        try
        {
            stmt = conn.createStatement();
            
            int i=0;
            //clean up text
            
            while(m_text.length()-4000*i>4000)
            {
                 text =m_text.substring(4000*(i), 4000*(i+1)).toUpperCase();
                 sql = "insert into seq_sequencetext(sequenceid, sequenceorder, sequencetext)\n"+
                "values("+m_id+","+(i+1)+",'"+text+"')";;
                stmt.executeUpdate(sql);
                
                i++;
            }
             text = m_text.substring(4000*(i));
             sql = "insert into seq_sequencetext(sequenceid, sequenceorder, sequencetext)\n"+
            "values("+m_id+","+(i+1)+",'"+text+"')";;
            stmt.executeUpdate(sql);
        }
        catch (Exception sqlE)
        {
            System.out.println(sqlE.getMessage());
            throw new FlexDatabaseException(sqlE+"\nSQL: "+sql);
        } finally
        {
          
            DatabaseTransaction.closeStatement(stmt);
        }
       
    
    }
    
    public String       getText(){ return m_text;}
    public void         setText(String s){  m_text = s;}
    public int          getId(){ return m_id;}
    public int          getType(){ return m_type;}
    public int          getLength(){ return m_text.length();}
    public String       getCodon(int num, int strand, int frame)
    {
        if (strand == 1 && frame == 1)
            return m_text.substring( (num - 1) * 3, 3);
        if (strand==1 && frame == 2)
            return m_text.substring( (num - 1) * 3 + 1, 3);
        if (strand==1 && frame == 3)
            return m_text.substring( (num - 1) * 3 + 1, 3);
        return null;
    }
    
    
}

