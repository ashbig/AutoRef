/*
 * BlastHit.java
 *
 * Created on September 24, 2002, 1:27 PM
 */

package edu.harvard.med.hip.bec.programs.blast;



import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.util.*;
import java.sql.*;
import java.util.*;
import edu.harvard.med.hip.bec.core.feature.*;
/**
 *
 * @author  htaycher
 */
public class BlastAligment
{
    private int m_id = -1;
  
    private int m_hit_seqid = - 1;
    private int m_score = -1;
    private double m_identity = -1;
    private String m_expect = "";
    private double m_pvalue = -1;
    private int m_query_strand = 0;
    private int m_subject_strand= 0;
    private int m_blastid = -1;
  
    private int m_subject_frame= 0;
    private int m_query_frame= 0;

    private int m_query_start = -1;//The offset into the query sequence of the beginning of the match
    private int m_query_stop = -1;//-The offset in the query of the end of the match
    private int m_subject_start = -1;//-The offset in the subject of the beginning of the match
    private int m_subject_stop = -1;//-The offset in the subject of the end of the match
    private int m_subject_length = -1;
    private int m_pscore = -1;
    private int m_gaps = -1;
    private String m_query = null;
    private String m_subject = null;
   // private ArrayList m_mutations = null;
    
//-The length of the match

    
    /** Creates a new instance of BlastHit */
    public BlastAligment(int hit_seqid, int score, double identity, String expect, double pvalue, int quary_strand, int Subjstrand, int subjframe, int queryframe, int query_start, int query_stop, int subject_start, int subject_stop, int subject_length, int ps, int bl, String q, String s, int gaps)
    {
        m_hit_seqid = hit_seqid;
        m_score = score;
        m_identity = identity;
        m_expect = expect;
        m_pvalue = pvalue;
        m_query_strand = quary_strand;
        m_subject_strand= Subjstrand;
     
        m_subject_frame= subjframe;
        m_query_frame= queryframe;
        m_query_start = query_start;
        m_query_stop = query_stop;
        m_subject_start = subject_start;
        m_subject_stop = subject_stop;
        m_subject_length = subject_length;
        m_pscore = ps;
        m_blastid = bl;
        m_subject = s;
        m_query = q;
        m_gaps = gaps;
        
    }
    
    
    public BlastAligment(int id) throws BecDatabaseException
    {
        m_id = id;
        
        String sql = "select  sequenceid,score,identity,expect,pvalue,qstop,qstart,qstrand,qframe,"+
        "sstart,sstop,sframe,sstrand,pscore,blastid,gaps from blasthit where hitid= "+id;
        
        ResultSet rs = null;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next())
            {
                 m_hit_seqid = rs.getInt("sequenceid");
                 m_score = rs.getInt("score");
                 m_identity = rs.getDouble("identity");
                 m_expect = rs.getString("expect");
                 m_pvalue = rs.getDouble("pvalue");
                 m_query_strand = rs.getInt("qstrand");
                 m_subject_strand= rs.getInt("sstrand");
                 m_blastid = rs.getInt("blastid");

                 m_subject_frame= rs.getInt("sframe");
                 m_query_frame= rs.getInt("qframe");

                 m_query_start = rs.getInt("qstart");//The offset into the query sequence of the beginning of the match
                 m_query_stop = rs.getInt("qstop");//-The offset in the query of the end of the match
                 m_subject_start = rs.getInt("sstart");//-The offset in the subject of the beginning of the match
                 m_subject_stop = rs.getInt("sstop");//-The offset in the subject of the end of the match
                 m_gaps = rs.getInt("gaps");
                 m_pscore = rs.getInt("pscore");
               //  m_mutations = getAllMutations();
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
        // PreparedStatement pstmt = null;
       Statement pstmt = null;
        if (m_id == -1 ) m_id = BecIDGenerator.getID("blasthitid");
        String sql = "INSERT INTO blasthit (hitid,sequenceid,score,"
        +"identity,expect,pvalue,blastid,qstop,qstart,qstrand,qframe,"+
        "sstart,sstop,sframe,sstrand,pscore,gaps) VALUES (    "+
        
          m_id+","+    m_hit_seqid+","+
              m_score+","+
               m_identity+",'"+
                 m_expect+"',"+
              m_pvalue+","+
                 m_blastid+","+ 
                 m_query_stop+","+ 
                 m_query_start+","+
                 m_query_strand+","+
                 m_query_frame+","+
                 m_subject_start+","+
                m_subject_stop+","+
                m_subject_frame+","+
                 m_subject_strand+","+
                 m_pscore+","+
                 m_gaps+")";
        //(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try
        {/*
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, m_id);
             pstmt.setInt(2, m_hit_seqid);
              pstmt.setDouble(3,m_score);
               pstmt.setDouble(4,m_identity);
                pstmt.setString(5, m_expect);
                pstmt.setDouble(6,m_pvalue);
                pstmt.setInt(7, m_blastid); 
                pstmt.setInt(8, m_query_stop); 
                pstmt.setInt(9, m_query_start);
                pstmt.setInt(10, m_query_strand);
                pstmt.setInt(11, m_query_frame);
                pstmt.setInt(12, m_subject_start);
                pstmt.setInt(13,m_subject_stop);
                pstmt.setInt(14,m_subject_frame);
                pstmt.setInt(15, m_subject_strand);
                pstmt.setInt(16, m_pscore);
                pstmt.setInt(17, m_gaps);
                
            pstmt.executeUpdate(sql);
         */pstmt = conn.createStatement();
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
    public int getId()  { return m_id;}

     public int getSequenceId(){ return m_hit_seqid ;}
     public int getScore(){ return m_score;}
      public int getProgramScore(){ return m_pscore;}
      public int getGaps(){ return m_gaps;}
     public double getIdentity(){ return m_identity;}
     public String getExpectValue(){ return m_expect;}
     public double getPValue(){ return m_pvalue;}
     public int getQStrand(){ return m_query_strand;}
     public int getSStrand(){ return m_subject_strand;}

     public int getSFrame(){ return m_subject_frame;}
     public int getQFrame(){ return m_query_frame;}
    public int getQStart(){return m_query_start;}//The offset into the query sequence of the beginning of the match
    public int getQStop(){return m_query_stop  ;}//-The offset in the query of the end of the match
    public int getSStart(){return m_subject_start  ;}//-The offset in the subject of the beginning of the match
    public int getSStop(){return m_subject_stop  ;}//-The offset in the subject of the end of the match
    public int getLength(){return m_subject_length  ;}
    public String getQSequence(){ return m_query;}
    public String getSSequence(){ return m_subject;}
    
    
    public void setSequenceId(int v){  m_hit_seqid = v ;}
    public void setScore(int v){  m_score= v ;}
    public void setGaps(int v){  m_gaps= v ;}
    public void setProgramScore(int v){  m_pscore= v ;}
    public void setIdentity(int v){  m_identity= v ;}
    public void setExpectValue(String v){  m_expect= v ;}
    public void setPValue(double v){  m_pvalue= v ;}
    public void setQStrand(int v){  m_query_strand= v ;}
    public void setSStrand(int v){  m_subject_strand= v ;}
    public void setSFrame(int v){  m_subject_frame= v ;}
    public void setQFrame(int v){  m_query_frame= v ;}
    public void setQStart(int v){ m_query_start= v ;}//The offset voido the query sequence of the beginning of the match
    public void setQStop(int v){ m_query_stop  = v ;}//-The offset in the query of the end of the match
    public void setSStart(int v){ m_subject_start  = v ;}//-The offset in the subject of the beginning of the match
    public void setSStop(int v){ m_subject_stop  = v ;}//-The offset in the subject of the end of the match
    public void setLength(int v){ m_subject_length  = v ;}
    public void setBlastId(int v){ m_blastid = v ;}
     public void setQSequence(String s){  m_query = s;}
    public void setSSequence(String s){  m_subject = s;}
    /*
    private ArrayList   getAllMutations() throws BecDatabaseException
    {
         String sql = "select  position,length,subject,query,type "+
                    "mutationid,sequenceid, flag from mutation where hitid = "+ m_hit_seqid;
        ArrayList res = new ArrayList();
        ResultSet rs = null;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next())
            {
                int id = rs.getInt("mutationid");
                 int type = rs.getInt("type");
                 int position = rs.getInt("position");
                 int length = rs.getInt("length");
                 String  subject_str = rs.getString("subject");//primer type: 5p-pcr, 5p-universal, 5p-full_set_n …
                 String query_str = rs.getString("query");
                 int flag = rs.getInt("flag");
                 int sequenceid = rs.getInt("sequenceid");
               //res.add(new Mutation(flag, position, length,query_str, 
                                subject_str, m_hit_seqid,  sequenceid, id, type));
            }
        } catch (Exception sqlE)
        {
            throw new BecDatabaseException("Error occured while initializing blasthit with id: "+m_id+"\n"+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
        return res;
    }
     **/
}
