/*
 * BlastResult.java
 * represent one blast ourput file
 * Created on September 24, 2002, 1:27 PM
 */

package edu.harvard.med.hip.flex.seqprocess.core.blast;


import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.blast.*;
import edu.harvard.med.hip.flex.util.*;
import java.sql.*;
import java.util.*;
import edu.harvard.med.hip.flex.seqprocess.core.feature.*;
/**
 *
 * @author  htaycher
 */
public class BlastResult
{
    
     public static final String BLAST_N = "BLAST_N";
    public static final String BLAST_P = "BLAST_P";
    
    private int m_id = -1;
    private int m_query_sequenceid = -1;
    private String m_executable = null;// program from blast family (bl2seq, blastn, tblastx, ..) that was run
    private int    m_subject_sequenceid = -1;// subject sequence � sequence id used for bl2seq
    private String	m_blast_db = null;//� information about database used for search if applicable, empty for bl2seq)
    private String m_date = null;//� when blast was run for blastable db tracking purposes: 
    private int m_cutoff = -1;//� the threshold for accepted hits
    private ArrayList m_hits = null;
    private String m_type = null;
    private String m_file_name = null;
    private String m_params = null;
   
//Blast_hits[] � array of hits

    /** Creates a new instance of BlastResult */
    public BlastResult(int id) throws FlexDatabaseException
    {
        m_id = id;
        
        String sql = "select   type,queryid, executable,subjectid,blastdb,rundate,cutoff,filename ,params "+
        " from blastresult where blastid = "+id;
        
        ResultSet rs = null;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            if(rs.next())
            {
                m_id = id;
                m_query_sequenceid = rs.getInt ("queryid");
                m_executable = rs.getString ("executable");// program from blast family (bl2seq, blastn, tblastx, ..) that was run
                m_subject_sequenceid = rs.getInt ("subjectid");// subject sequence � sequence id used for bl2seq
                m_blast_db = rs.getString ("blastdb");//� information about database used for search if applicable, empty for bl2seq)
                m_date = rs.getString ("rundate");//� when blast was run for blastable db tracking purposes: 
                m_cutoff = rs.getInt ("cutoff");//� the threshold for accepted hits
                m_type = rs.getString ("type");
                m_file_name = rs.getString ("filename");
                //exstruct hits
                m_hits = getAllHits();
                m_params = rs.getString("params");
            }
        } catch (Exception sqlE)
        {
            throw new FlexDatabaseException("Error occured while initializing Oligo with id: "+id+"\n"+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
    public BlastResult()
    {
    }
    public BlastResult( int  ref_sequenceid , 
          String  algorithm ,// program from blast family (bl2seq, blastn, tblastx, ..) that was run
          int     subject_sequenceid , // subject sequence � sequence id used for bl2seq
          String	 blast_db ,//� information about database used for search if applicable, empty for bl2seq)
          String  date ,//� when blast was run for blastable db tracking purposes: 
          int  cutoff , //� the threshold for accepted hits
          ArrayList  hits ,
          String t,
          String fn, String pr)
    {
        m_query_sequenceid =  ref_sequenceid;
        m_executable = algorithm;// program from blast family (bl2seq, blastn, tblastx, ..) that was run
        m_subject_sequenceid =  subject_sequenceid;// subject sequence � sequence id used for bl2seq
        m_blast_db = blast_db ;//� information about database used for search if applicable, empty for bl2seq)
        m_date =  date;//� when blast was run for blastable db tracking purposes: 
        m_cutoff = cutoff ;//� the threshold for accepted hits
        m_hits =  hits;
        m_file_name = fn;
        m_type = t;
        m_params = pr;
        

    }
    
    public void insert(Connection conn) throws FlexDatabaseException
    {
        
        //!!! update blasthitid for each blasthit !!!!!!!!!!! 
          if (m_id == -1) m_id = FlexIDGenerator.getID("blastid");
       
        PreparedStatement pstmt = null;
        
        String sql = "INSERT INTO blastresult (blastid,executable,type,queryid,subjectid,blastdb,rundate,filename,"
        +"params,cutoff ) VALUES(?,?,?,?,?,?,sysdate,?,?,?)" ;
      
        try
        {
      
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, m_id);
            pstmt.setString(2, m_executable);
            pstmt.setString(3, m_type);
            pstmt.setInt(4, m_query_sequenceid);
            pstmt.setInt(5, m_subject_sequenceid);
            pstmt.setString(6, m_blast_db);
            pstmt.setString(7, m_file_name);
            pstmt.setString(8, m_params);
            pstmt.setInt(9, m_cutoff);
            DatabaseTransaction.executeUpdate(pstmt);
            for (int count = 0; count < m_hits.size(); count++)
            {
                BlastHit h = (BlastHit)m_hits.get(count);
                h.setBlastId(m_id);
                h.insert(conn);
            }
              
            
        } catch (Exception sqlE)
        {
            System.out.println(sqlE.getMessage());
            throw new FlexDatabaseException(sqlE+"\nSQL: "+sql);
        } finally
        {
          
            DatabaseTransaction.closeStatement(pstmt);
        }
    }
    
    //gettters 
    public  int         getId(){return m_id ;}
    public  String      getExecutable(){return m_executable ;}// program from blast family (bl2seq, blastn, tblastx, ..) that was run
    public  int         getSubjectSequenceId() {return m_subject_sequenceid ;}// subject sequence � sequence id used for bl2seq
    public  String	getDBName(){return m_blast_db ;}//� information about database used for search if applicable, empty for bl2seq)
    public  String      getDate(){return m_date ;}//� when blast was run for blastable db tracking purposes: 
    public  int         getCutoff(){return m_cutoff ;}//� the threshold for accepted hits
    public  ArrayList   getHits(){return m_hits ;}//Blast_hits[] � array of hits
    public  String	getFileName(){return m_file_name ;}
    public  String	getType(){return m_type ;}
    public  int         getQuerySequenceId() {return m_query_sequenceid ;}
    
    
   
    public  void        setBlastExecutable(String s){ m_executable =s ;}// program from blast family (bl2seq, blastn, tblastx, ..) that was run
    public  void         setSubjectSequenceId(int s) { m_subject_sequenceid =s;}// subject sequence � sequence id used for bl2seq
    public  void	setDBName(String s){ m_blast_db =s;}//� information about database used for search if applicable, empty for bl2seq)
    public  void        setDate(String s){ m_date=s ;}//� when blast was run for blastable db tracking purposes: 
    public  void         setCutoff(int s){ m_cutoff =s;}//� the threshold for accepted hits
    public  void        setHits(ArrayList s){ m_hits =s;}//Blast_hits[] � array of hits
    public  void	setFileName(String s){ m_file_name=s ;}
    public  void	setType(String s){ m_type =s;}
    public  void        setQuerySequenceId(int s) { m_query_sequenceid =s;}
    
    private ArrayList   getAllHits() throws FlexDatabaseException
    {
        
        ArrayList res = new ArrayList();
           
        String sql = "select  sequenceid,score,identity,expect,pvalue,qstop,qstart,qstrand,qframe,"+
        "sstart,sstop,sframe,sstrand,pscore,blastid,gaps from blasthit where blastid= "+m_id;
        
        ResultSet rs = null;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next())
            {
                int hit_seqid = rs.getInt("sequenceid");
                int score = rs.getInt("score");
                double identity = rs.getDouble("identity");
                String expect = rs.getString("expect");
                double pvalue = rs.getDouble("pvalue");
                int query_strand = rs.getInt("qstrand");
                int subject_strand= rs.getInt("sstrand");
                int subject_frame= rs.getInt("sframe");
                int query_frame= rs.getInt("qframe");
                int query_start = rs.getInt("qstart");//The offset into the query sequence of the beginning of the match
                int query_stop = rs.getInt("qstop");//-The offset in the query of the end of the match
                int subject_start = rs.getInt("sstart");//-The offset in the subject of the beginning of the match
                int subject_stop = rs.getInt("sstop");//-The offset in the subject of the end of the match
                int gaps = rs.getInt("gaps");
                int pscore = rs.getInt("pscore");
                BlastHit hit = new  BlastHit(  hit_seqid,
                          score ,    identity ,
                          expect ,    pvalue ,
                          query_strand ,    subject_strand,
                        subject_frame,    query_frame,
                          query_start ,  query_stop ,  
                            subject_start ,  subject_stop ,
                        0 ,  pscore, m_id,
                        null, null, gaps);
                res.add(hit);
            }
        } catch (SQLException sqlE)
        {
            throw new FlexDatabaseException("Error occured while initializing blasthit with id: "+m_id+"\n"+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
        return res;
    }
    
    
    
    //***********************************************
    private  void sortByScore()
    {
        Collections.sort(m_hits, new Comparator()
       {
            public int compare(Object cont1, Object cont2)
            {
                BlastHit p1 =(BlastHit) cont1;
                BlastHit p2 = (BlastHit) cont2;
                return  p1.getScore() - p2.getScore()  ;
           }
       });
    }
    
    private  void sortByIdentity()
    {
        Collections.sort(m_hits, new Comparator()
       {
            public int compare(Object cont1, Object cont2)
            {
                BlastHit p1 =(BlastHit) cont1;
                BlastHit p2 = (BlastHit) cont2;
                if (  p1.getIdentity() > p2.getIdentity())
                    return 1;
                else if  (  p1.getIdentity() < p2.getIdentity())
                    return -1;
                else
                    return 0;
           }
       });
    }
    
    
    
    public static void main(String [] args)
    {
        try
        {
        
            
        /*
         *    DatabaseTransaction t = DatabaseTransaction.getInstance();
            Connection conn =  t.requestConnection();
            ArrayList hits = new ArrayList();
        BlastHit hit = new BlastHit(
                    -1,
                     12,90,"e10-9",  
                      -1, 1 ,  1,
                      1, 1,1,1234,1,1234,1234,
                      0, -1,"lllll","lllll"  ,2  );
  
            hits.add(hit);
          
            BlastResult bl = new   BlastResult( 31461, 
             Blaster.BLAST_EXEC_BLAST2SEQ,-1,null,"",20,
             hits,Blaster.BLAST_PROGRAM_BLASTP,"/tmp", "-p bl");
            bl.insert(conn);  conn.commit();
         **/
            BlastResult b = new BlastResult(19);
          System.out.print(b.getId());            
        } catch (Exception e)
        {
            System.out.println(e);
        }
        System.exit(0);
    }
}
