/*
 * BaseSequence.java
 * represent base sequence which is coding sequence for  becsequence 
 *or just sequence text for any other
 * Created on September 20, 2002, 2:01 PM
 */

package edu.harvard.med.hip.bec.coreobjects.sequence;


import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.util.*;
import java.sql.*;
import java.util.*;
/**
 *
 * @author  htaycher
 */
public  class BaseSequence
{
    
    public static final int BASE_SEQUENCE = 0;
    public static final int SCORED_SEQUENCE = 1;
    public static final int FULL_SEQUENCE = 2;
    public static final int THEORETICAL_SEQUENCE = 3;
    public static final int ANALIZED_SCORED_SEQUENCE = 4;
    public static final int CLONE_SEQUENCE = 5;
  
    public static final int READ_SEQUENCE = 6;//for display only
   
    
    //sequence info type
    public static final int SEQUENCE_INFO_TEXT = 0;
    public static final int SEQUENCE_INFO_SCORE = 1;
    
    //sequence analysis status
    public static final int STATUS_OBTAINED = 0;
    public static final int STATUS_ANALIZED_YES_DISCREPANCIES = 1;
    public static final int STATUS_ANALIZED_NO_DISCREPANCIES = 3;
    public static final int STATUS_NOMATCH = 4;
    public static final int STATUS_POLYMORPHISM_CLEARED = 2;
    public static final int STATUS_FINISHED = 5;
    
    //sequence assembly status
     public static final int ASSEMBLY_STATUS_ASSESMBLED = 0; 
    public static final int ASSEMBLY_STATUS_EDITED = 1; 
    public static final int ASSEMBLY_STATUS_FINAL = 2; 
    /*
     public static final int QUALITY_STORAGE = 2;
    public static final int QUALITY_RECOMENDED_STORAGE =1;
    public static final int QUALITY_BAD = -1;
    public static final int QUALITY_RECOMENDED_BAD = -2;
    public static final int QUALITY_NOT_DEFINED = 0;
    */
    protected int m_id =  BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    protected String m_text = null;
    protected int m_type = -1;
    
    
    /** Creates a new instance of BaseSequence */
    public BaseSequence( )    {}
    public BaseSequence( String t, int tp) 
    {
       // m_id = ;
      
        m_text = t;
        m_type = tp;
    }
    
    public static  String getSequenceInfo( int id, int info_type)throws BecDatabaseException
    {
       
        String sql = null; String text = null;
        sql = "select infotext from sequenceinfo where sequenceid="+id+" and infotype = " + info_type +"  order by infoorder";
       
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        ResultSet rs = null;
        try
        {
            rs = t.executeQuery(sql);
            while(rs.next())
            {
                if (text == null)
                        text = rs.getString("infotext");
                else
                    text +=rs.getString("infotext");
            }
            return text;
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException("Error occured while restoring sequence with id "+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
    
    public static  void insertSequence(Connection conn, String seq, int id, int info_type)throws BecDatabaseException
    {
        
        String text = "";
        String sql = "";
        Statement stmt =null;
        
        try
        {
            stmt = conn.createStatement();
            
            int i=0;
            //clean up text
            
            while(seq.length()-4000*i>4000)
            {
                 text =seq.substring(4000*(i), 4000*(i+1)).toUpperCase();
                 sql = "insert into sequenceinfo(sequenceid, infoorder, infotext, infotype)\n"+
                "values("+id+","+(i+1)+",'"+text+"', "+ info_type +")";
                stmt.executeUpdate(sql);
                
                i++;
            }
             text = seq.substring(4000*(i));
             sql = "insert into sequenceinfo(sequenceid, infoorder, infotext, infotype)\n"+
            "values("+id+","+(i+1)+",'"+text+"',"+ info_type +")";
            stmt.executeUpdate(sql);
        }
        catch (Exception sqlE)
        {
            System.out.println(sqlE.getMessage());
            throw new BecDatabaseException(sqlE+"\nSQL: "+sql);
        } finally
        {
          
            DatabaseTransaction.closeStatement(stmt);
        }
       
    
    }
     
    public String       getText(){ return m_text;}
    public void         setText(String s){  m_text = s;}
    public int          getId(){ return m_id;}
    
    //debuging only
    public void          setId(int i){  m_id = i;}
    
    
    
    public int          getType(){ return m_type;}
    public void         setType(int t){  m_type = t;}
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
    
    //function returns int[0,1] where 
    //0-total number of ambiques bases in sequence
    // 1 - the length of the longest stretch of ambiques bases in the sequence
    public static  int[] analizeSequenceAmbiquty(String seq_text)
    {
        int[] res = {0,0};
        char[] chars = seq_text.toCharArray();
        int total_amb_chars = 0;
        int stretch_curent = 0;
        int stretch_preserved = 0;
        for (int count = 0; count < chars[count]; count++)
        {
            if (chars[count] == 'N' || chars[count] == 'n')
            {
                total_amb_chars++;
                stretch_curent++;
            }
            else
            {
                if ( stretch_curent > stretch_preserved)
                {
                    stretch_preserved = stretch_curent;
                }
                stretch_curent = 0;
                
            }
        }
        res[0] = total_amb_chars;
        res[1] = (stretch_preserved >= stretch_curent) ? stretch_preserved : stretch_curent;
        return res;
     }
    public static ArrayList getAllSequencesWithStatus(int status, int seqtype) throws BecDatabaseException
    {
       
        ArrayList res = new ArrayList();

        /*
        String sql = "select sequenceid from fullsequence where FLAG_STATUS="+ status;
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        ResultSet rs = null;
        try
        {
            rs = t.executeQuery(sql);
            while(rs.next())
            {
                FullSequence fs = new FullSequence( rs.getInt("sequenceid"));
                res.add(fs);
            }
            return res;
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException("Error occured while restoring sequence with id "+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
         **/
        return res;
    }
}

