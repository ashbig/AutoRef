/*
 * FuzzySequence.java
 * represent result of Phred : sequence and base scores plus full sequence score
 * Created on September 20, 2002, 2:19 PM
 */

package edu.harvard.med.hip.flex.seqprocess.core.sequence;

import edu.harvard.med.hip.flex.seqprocess.spec.*;
import java.sql.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.util.*;
import java.util.*;

/**
 *
 * @author  htaycher
 */
public class FuzzySequence extends BaseSequence
{
    public static final int FUZZY_SEQUENCE = 1;
    
    private int m_totalscore = -1;
    private String m_scores = null;
    private int[] m_scores_numbers = null;
    private int m_start = -1;
    private int m_stop = -1;
    
    /** Creates a new instance of FuzzySequence */
    public FuzzySequence(int id)throws FlexDatabaseException
    {  
        super(id);
        m_type = FUZZY_SEQUENCE;
        //restore fuzzy sequence characteristics
      
        String sql = null;
        
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        ResultSet rs = null;
        try
        {
            sql = "select  scores,start, stop from fuzzysequence where sequenceid="+id+" order by scoreorder";
            rs = t.executeQuery(sql);
            while(rs.next())
            {
                m_scores += rs.getString("SCORES");
                
                m_start = rs.getInt("START");
                m_stop = rs.getInt("STOP");
            }
           
        } catch (SQLException sqlE)
        {
            throw new FlexDatabaseException("Error occured while restoring sequence with id "+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
        
    }
    
    public FuzzySequence(String text, String scores, int start, int stop)throws FlexDatabaseException
    {
         super(Algorithms.cleanWhiteSpaces(text), FUZZY_SEQUENCE);
        m_scores = scores;
        m_start = start;
        m_stop = stop;
    
    }
  
    
    public int          getTotalScore()
    { 
        if (m_totalscore == 0) 
        {
             m_totalscore = calculateTotalScore();
        }
        return m_totalscore;
    }
    public String       getTrimmedSequence(){ return this.getText().substring(m_start - 1,m_stop);}
    public String       getScores(){return m_scores;}
    public int          getStart(){ return m_start;}
    public int          getEnd(){ return m_stop;}
    
    public void insert(Connection conn) throws FlexDatabaseException
    {
        String sql = "";
        Statement stmt =null;
        if (this.getText() == null || this.getText().length() == 0) return;
        if(m_id == -1)    m_id = FlexIDGenerator.getID("sequenceid");
        int i = 0;String text = "";
        try
        {
            stmt = conn.createStatement();
            while(m_scores.length()-4000*i>4000)
            {
                 text =m_scores.substring(4000*(i), 4000*(i+1)).toUpperCase();
                 sql = "insert into fuzzysequence(sequenceid, scoreorder, scores, start,stop)"+
                "values("+m_id+","+(i+1)+",'"+text+"',"+m_start+","+m_stop+")";
                stmt.executeUpdate(sql);
                
                i++;
             }
             text = m_scores.substring(4000*(i));
              sql = "insert into fuzzysequence(sequenceid, scoreorder, scores, start,stop)"+
                "values("+m_id+","+(i+1)+",'"+text+"',"+m_start+","+m_stop+")";
            stmt.executeUpdate(sql);
     
           super.insert(conn);
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
    
    
    //function parse out scores string
    private int calculateTotalScore()
    {
        int res = 0;
        ArrayList scores = Algorithms.splitString(m_scores," ");
        for (int count = 0; count < scores.size(); count++)
        {
            res+=  ((Integer) scores.get(count)).intValue();
        }
        return res;
    }
    
    //function parse out scores string
    public int[] getScoresAsArray()
    {
        if (m_scores_numbers != null) return m_scores_numbers;       
        ArrayList scores = Algorithms.splitString(m_scores," ");
        m_scores_numbers = new int[scores.size()];
        for (int count = 0; count < scores.size(); count++)
        {
            m_scores_numbers[count] =  ((Integer) scores.get(count)).intValue();
        }
        
        return m_scores_numbers;
    }
    
    //if not phred trimming is used sequence trimmes itself based on spec
    public void trim(EndReadsSpec spec)
    { 
               
    }
    
}
