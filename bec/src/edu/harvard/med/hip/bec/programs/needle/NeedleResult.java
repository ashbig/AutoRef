/*
 * NeedleResult.java
 *
 * Created on November 27, 2002, 5:34 PM
 */

package edu.harvard.med.hip.bec.programs.needle;

import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.util.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import edu.harvard.med.hip.bec.coreobjects.feature.*;
/**
 *
 * @author  htaycher
 */
public class NeedleResult
{
    
   private static final String file_name = "c:\\blastoutput\\needleout.txt";

    private int m_id = -1;
    
    private int m_query_seqid = - 1;
    private int m_subject_seqid = - 1;
    
    //params
    private double         m_gapopen_penalty = -1;
    private double         m_gap_ext_penalty = -1;
    
    //results
    private double       m_identity = -1;
    private double       m_similarity = -1;
    private double      m_gaps = -1;
    private double       m_score = -1;
    
    private String      m_filename = null;
    private String m_date = null;
    private String m_query = null;
    private String m_subject = null;
   // private ArrayList m_mutations = null;
    
//-The length of the match

    
    /** Creates a new instance of BlastHit */
    public NeedleResult( ){}
    
    
    public NeedleResult(int id) throws BecDatabaseException
    {
        m_id = id;
        
        String sql = "select  identity,score,similarity,gapopen,gapext,rundate,queryid,subjectid,filename "+
        " from needleresult where id= "+id;
        
        ResultSet rs = null;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next())
            {
                 m_query_seqid = rs.getInt("queryid");
                 m_score = rs.getFloat("score");
                 m_identity = rs.getFloat("identity");
                 m_similarity = rs.getFloat("similarity");
                 m_gapopen_penalty = rs.getFloat("gapopen");
                 m_gap_ext_penalty = rs.getFloat("gapext");
                 m_subject_seqid= rs.getInt("subjectid");
                 m_filename = rs.getString("filename");
                 m_date = rs.getString ("rundate");
               
            }
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException("Error occured while initializing Oligo with id: "+id+"\n"+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
         
    }
   
    
    
    public void insert(Connection conn) throws BecDatabaseException
    {
        
        PreparedStatement pstmt = null;
       //Statement pstmt = null;
        if (m_id == -1 ) m_id = BecIDGenerator.getID("blasthitid");
        String sql = "INSERT INTO needleresult (identity,score,similarity, "+
        " gapopen,gapext,rundate,queryid,subjectid,filename) VALUES ( needleid.nextval,"+
        "?,?,?,?,?,sysdate,?,?,?)";
        try
        {
            pstmt = conn.prepareStatement(sql);
            pstmt.setDouble(1, m_identity);
             pstmt.setDouble(2, m_score);
              pstmt.setDouble(3,m_similarity);
               pstmt.setDouble(4,m_gapopen_penalty);
                pstmt.setDouble(5, m_gap_ext_penalty);
                pstmt.setInt(6, m_query_seqid); 
                pstmt.setInt(7, m_subject_seqid); 
               
                pstmt.setString(8, m_filename);
                
            pstmt.executeUpdate(sql);
         //pstmt = conn.createStatement();
              pstmt.executeUpdate(sql);
        } catch (Exception sqlE)
        {
            System.out.println(sqlE);
            System.out.println(sql);
            throw new BecDatabaseException(sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeStatement(pstmt);
        }
        
    }
    
 
     
    //public void setMutations(ArrayList s){ m_mutations =s;}
   public int getId (){ return m_id  ;}
  
    public int getQuerySequenceId(){ return m_query_seqid   ;}
    public int getSubjectSequenceId (){ return m_subject_seqid   ;}
    
    //params
    public double         getGapOpen (){ return m_gapopen_penalty  ;}
    public double         getGapExtend (){ return m_gap_ext_penalty  ;}
    
    //results
    public double        getIdentity (){ return m_identity  ;}
    public double        getSimilarity (){ return m_similarity  ;}
    public double        getGaps (){ return m_gaps  ;}
    public double        getScore (){ return m_score  ;}
    public String       getFileName (){ return m_filename  ;}
    public String       getQuery (){ return m_query  ;}
    public String       getSubject (){ return m_subject  ;}
   
    
    public void         setId (int s){   m_id  = s;}
    public void         setQuerySequenceId(int s){   m_query_seqid   = s;}
    public void         setSubjectSequenceId (int s){   m_subject_seqid   = s;}
    public void         setGapOpen (double s){   m_gapopen_penalty  = s;}
    public void         setGapExtend (double s){   m_gap_ext_penalty  = s;}
    public void         setIdentity (double s){   m_identity  = s;}
    public void         setSimilarity (double s){   m_similarity  = s;}
    public void         setGaps (double s){   m_gaps  = s;}
    public void         setScore (double s){   m_score  = s;}
    public void         setFileName (String s){   m_filename  = s;}
    public void         setQuery (String s){   m_query  = s;}
    public void         setSubject (String s){   m_subject  = s;}
    
    
    public void recalculateIdentity()
    {
        char[] arr_query =  m_query.toCharArray();
        char[] arr_subject =  m_subject.toCharArray();
        int query_base_count = 0;
        int match_count = 0;
        boolean isStart = false;
        int length = ( arr_query.length  >= arr_subject.length ) ?
                arr_subject.length -1 : arr_query.length -1 ;
       
        for (int count = 0; count < length;count++)
        {
            if (!isStart && (arr_query[count] != ' ' && arr_subject[count] != ' '))isStart = true;
            if (!isStart) continue;
            if (isStart && arr_query[count] == arr_subject[count]) match_count++;
            if (isStart && arr_query[count] == ' ' || arr_subject[count] == ' ')
            {
                break;
            }
            if (isStart) query_base_count++;
          //  System.out.println(arr_query[count]+" "+arr_subject[count] +" "+match_count+" "+query_base_count);
        }
        //if ( arr_query.length  > arr_subject.length) query_base_count+= (arr_query.length  - arr_subject.length);
       
                /*
                try{
            FileWriter fl = new FileWriter(file_name,true);
            fl.write(""+m_query_seqid+"\t"+   m_subject_seqid+"\t"+ m_identity +"\t"+ ( 100 * match_count) / query_base_count
            +"\t"+m_score+"\t"+m_similarity+"\t"+m_gaps +"\n");
            fl.flush();
            fl.close();
            }catch(Exception e)
            {System.out.println(e.getMessage());}
System.out.println(""+m_query_seqid+"\t"+   m_subject_seqid+"\t"+ m_identity +"\t"+ ( 100 * match_count) / query_base_count
            +"\t"+m_score+"\t"+m_similarity+"\t"+m_gaps );
        */
                //should never be executed
        if (query_base_count == 0)
        {
             m_identity = 10;
        }
        m_identity = 100 * match_count / query_base_count;
       // System.out.println(m_identity);
    }
    
    
    
    //******************************************
    public static void main(String args[])
    {
        
         NeedleResult res = new NeedleResult();
       try
        {
        
          String queryFile ="c:\\test_needle.txt";// "c:\\needleoutput\\needle10339_419.out";
            //  String queryFile = "c:\\needleATG.out";
             NeedleParser.parse(queryFile,res);
             res.recalculateIdentity();
       
        }catch(Exception e)
        {
            System.out.println(e.getMessage());}
          System.exit(0);
    }
}
