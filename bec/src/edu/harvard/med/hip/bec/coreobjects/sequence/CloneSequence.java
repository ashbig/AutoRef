/*
 * FuzzySequence.java
 * represent result of Phred : sequence and base scores plus full sequence score
 * Created on September 20, 2002, 2:19 PM
 */

package edu.harvard.med.hip.bec.coreobjects.sequence;


import java.sql.*;
import java.util.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.programs.blast.*;
import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.coreobjects.feature.*;
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.sampletracking.objects.*;
import edu.harvard.med.hip.bec.programs.assembler.*;
/**
 *
 * @author  htaycher
 */
public class CloneSequence extends AnalyzedScoredSequence
{
 
       
     //obtained/analyzed/mutations cleared/final
  
    private int        m_sequence_type = BaseSequence.CLONE_SEQUENCE_TYPE_ASSEMBLED ; //final\conseq\final editied
    private int         m_approved_by_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
   
    private int         m_result_id =  BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    private int         m_isolatetracking_id =  BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    //private int         m_analize_status = -1;
    private int         m_cds_start = -1;
    private int         m_cds_stop = -1;
    private int         m_linker5_start = -1;
    private int         m_linker3_stop = -1;
 
    private String     m_submission_date = null;
  
    
    private static int   MODE_ONE_LAST_BY_DATE_SEQUENCE = 0;
    private static int   MODE_ALL_SEQUENCES = 1;
    
    public CloneSequence( String text,  int refseqid)
    {
        super( Algorithms.cleanWhiteSpaces(text) , refseqid);
        m_type = CLONE_SEQUENCE;
        m_analize_status = CLONE_SEQUENCE_STATUS_ASSEMBLED;
       
    }
    public CloneSequence( String text, String scores,int refseqid, int status) 
    {
        this( text, scores ,refseqid);
        m_analize_status = status;
        m_type = CLONE_SEQUENCE;
    }
    public CloneSequence(int id) throws BecDatabaseException
    {  
        super( id);
         m_type = CLONE_SEQUENCE;
         String sql = "select SEQUENCEID ,LINKER5START ,LINKER3STOP,ANALYSISSTATUS ,SEQUENCETYPE ,REFSEQUENCEID,RESULTID ,ISOLATETRACKINGID "
 +"  ,APROVEDBYID   ,SUBMISSIONDATE ,CDSSTART   ,CDSSTOP    from ASSEMBLEDSEQUENCE where sequenceid="+id;
    
         DatabaseTransaction t = DatabaseTransaction.getInstance();
            ResultSet rs = null;
        try
        {
            rs = t.executeQuery(sql);
            while(rs.next())
            {
                m_sequence_type = rs.getInt("SEQUENCETYPE");// -1  rs.getInt(""); //final\conseq\final editied
                m_approved_by_id = rs.getInt("APROVEDBYID");// BecIDGenerator.BEC_OBJECT_ID_NOTSET rs.getInt("");
                m_refsequenceid = rs.getInt("REFSEQUENCEID");//  BecIDGenerator.BEC_OBJECT_ID_NOTSET rs.getInt("");
                m_result_id = rs.getInt("RESULTID");//  BecIDGenerator.BEC_OBJECT_ID_NOTSET rs.getInt("");
                m_isolatetracking_id = rs.getInt("ISOLATETRACKINGID");//  BecIDGenerator.BEC_OBJECT_ID_NOTSET rs.getInt("");
                m_analize_status = rs.getInt("ANALYSISSTATUS");// -1 rs.getInt("");
                m_cds_start = rs.getInt("CDSSTART");// -1 rs.getInt("");
                m_cds_stop = rs.getInt("CDSSTOP");// -1 rs.getInt("");
                m_linker5_start = rs.getInt("LINKER5START");
                m_linker3_stop = rs.getInt("LINKER3STOP");
            }
            m_text = BaseSequence.getSequenceInfo(id, BaseSequence.SEQUENCE_INFO_TEXT);
            m_id = id;
            m_type = CLONE_SEQUENCE;
            m_scores = BaseSequence.getSequenceInfo(id, BaseSequence.SEQUENCE_INFO_SCORE);
            
           
            getDiscrepancies();
        } catch (Exception sqlE)
        {
            throw new BecDatabaseException("Error occured while restoring sequence with id "+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
       
    }
    
    public static CloneSequence getOneByIsolateTrackingId(int is_id, String clone_sequence_analysis_status, String clone_sequence_type) throws BecDatabaseException
    {
        String sql = "select sequenceid from assembledsequence where isolatetrackingid = "+is_id;
        if (clone_sequence_type != null)
                sql += " and SEQUENCETYPE  in (    "+clone_sequence_type +")";
        if ( clone_sequence_analysis_status != null)
            sql +=" and ANALYSISSTATUS in ( "+clone_sequence_analysis_status +")";
        sql += " order by submissiondate desc";
        ArrayList sequences = getByRule(sql );
        if (sequences.size() > 0)
            return (CloneSequence) sequences.get(0);
        return null;
    }
    
    
     public static ArrayList getAllByIsolateTrackingId(int is_id, String clone_sequence_analysis_status, String clone_sequence_type) throws BecDatabaseException
    {
        String sql = "select sequenceid from assembledsequence where isolatetrackingid = "+is_id;
        if (clone_sequence_analysis_status != null)
                sql += " and SEQUENCETYPE  in (    "+clone_sequence_type +")";
        if ( clone_sequence_analysis_status != null)
            sql +=" and ANALYSISSTATUS in ( "+clone_sequence_analysis_status +")";
        sql += " order by submissiondate ";
        return getByRule(sql);
    }
    
     
   public String getCodingSequence()
   {
             return getText().substring(m_cds_start,m_cds_stop + 1);
    }
    
    public CloneSequence( String text, String score, int refseqid)
    {
        super( text,score,refseqid,CLONE_SEQUENCE_STATUS_ASSEMBLED);
        m_type =  CLONE_SEQUENCE;
   
    }
  
    
   
    
    
    public synchronized void insert (Connection conn) throws BecDatabaseException
    {
       String sql =null;
       Statement stmt = null;
        try
        {
            if (m_id ==  BecIDGenerator.BEC_OBJECT_ID_NOTSET)
                    m_id = BecIDGenerator.getID("sequenceid");
                    
              sql = 	"insert into ASSEMBLEDSEQUENCE "+
            " (SEQUENCEID ,LINKER5START ,LINKER3STOP, ANALYSISSTATUS ,SEQUENCETYPE ,REFSEQUENCEID,RESULTID ,ISOLATETRACKINGID "
  +" ,SUBMISSIONDATE ,CDSSTART   ,CDSSTOP   ) "+
            "values ("+m_id+ ","+ m_linker5_start + ","+ m_linker3_stop + ","+
            m_analize_status + ","  +m_sequence_type+","+m_refsequenceid+"," 
            +m_result_id+","+m_isolatetracking_id+",sysdate,"+m_cds_start+","+m_cds_stop +")";
             DatabaseTransaction.executeUpdate(sql,conn);
        
        //insert into sequencetext table.
            BaseSequence.insertSequence( conn, this.getText(),m_id, BaseSequence.SEQUENCE_INFO_TEXT);
            if (this.getScores() != null)
                BaseSequence.insertSequence( conn, this.getScores(),m_id, BaseSequence.SEQUENCE_INFO_SCORE);
          
        } catch (Exception sqlE)
        {
            throw new BecDatabaseException(sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeStatement(stmt);
        }
    }
    
    
     public static   int insertSequenceWithResult(
                         int sampleid,
                         int isolate_trackingid,
                         int refseq_id,
                         int result_type,
                         int seq_assembly_status,
                         int seq_type,
                         Contig contig,
                         int process_id,
                         Connection conn)throws BecDatabaseException
     {
         //create sequence
         try
         {
           
            Result result = new Result(BecIDGenerator.BEC_OBJECT_ID_NOTSET,     process_id,
                                        sampleid,       null,
                                        result_type,
                                        BecIDGenerator.BEC_OBJECT_ID_NOTSET );
            result.insert(conn, process_id );

            CloneSequence clone_seq = new CloneSequence( contig.getSequence(),  contig.getScores(), refseq_id);
            clone_seq.setResultId( result.getId()) ;//BecIDGenerator.BEC_OBJECT_ID_NOTSET= v;}
            clone_seq.setIsolatetrackingId ( isolate_trackingid );//BecIDGenerator.BEC_OBJECT_ID_NOTSET= v;}
            clone_seq.setCloneSequenceStatus (seq_assembly_status);
            clone_seq.setCloneSequenceType (seq_type); //final\conseq\final editied
            clone_seq.setCdsStart( contig.getCdsStart() );
            clone_seq.setCdsStop( contig.getCdsStop() );
            clone_seq.insert(conn);
            
            Result.updateResultValueId( result.getId(),clone_seq.getId(),  conn);
            
            return clone_seq.getId();
         }
         catch(Exception e)
         {
             throw new BecDatabaseException("Error while trying to insert clone sequence.");
         }
         //insert
     }

     
    public int          getCloneSequenceType (){ return m_sequence_type   ;} //final\conseq\final editied
    public int          getApprovedById (){ return m_approved_by_id ;} //BecIDGenerator.BEC_OBJECT_ID_NOTSET;}
    public int          getRefSequenceId (){ return m_refsequenceid;}   //BecIDGenerator.BEC_OBJECT_ID_NOTSET;}
    public int          getResultId (){ return m_result_id   ;}//BecIDGenerator.BEC_OBJECT_ID_NOTSET;}
    public int          getIsolatetrackingId (){ return m_isolatetracking_id  ;} //BecIDGenerator.BEC_OBJECT_ID_NOTSET;}
    public int          getCloneSequenceStatus (){ return m_analize_status  ;}
    public String       getSubmissionDate (){ return m_submission_date  ;}
    public int          getCdsStart(){ return m_cds_start ;}
    public int          getCdsStop(){ return m_cds_stop ;}
     public int          getLinker5Start(){ return m_linker5_start ;}
    public int          getLinker3Stop(){  return m_linker3_stop ;}

    public void         setCloneSequenceType (int v){  m_sequence_type   = v;} //final\conseq\final editied
    public void         setApprovedById (int v){  m_approved_by_id = v;} //BecIDGenerator.BEC_OBJECT_ID_NOTSET= v;}
    public void         setRefSequenceId (int v){  m_refsequenceid= v;}   //BecIDGenerator.BEC_OBJECT_ID_NOTSET= v;}
    public void         setResultId (int v){  m_result_id   = v;}//BecIDGenerator.BEC_OBJECT_ID_NOTSET= v;}
    public void         setIsolatetrackingId (int v){  m_isolatetracking_id  = v;} //BecIDGenerator.BEC_OBJECT_ID_NOTSET= v;}
    public void         setCloneSequenceStatus (int v){  m_analize_status  = v;}
    public void         setSubmissionDate (String v){  m_submission_date  = v;}
    public void          setCdsStart(int v){  m_cds_start = v;}
    public void          setCdsStop(int v){  m_cds_stop = v;}
     public void          setLinker5Start(int v){  m_linker5_start = v;}
    public void          setLinker3Stop(int v){  m_linker3_stop = v;}

   
  
     public void updateApprovedBy(Connection conn)    throws BecDatabaseException
    {
        Statement stmt =null;
        try
        {
            String sql = "UPDATE assembledSEQUENCE SET APPROVEDBYid = "
            + m_approved_by_id +" WHERE SEQUENCEID = "+m_id;
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException("Error occured while calling update method for sequence "+m_id+"\n"+sqlE);
        } finally
        {
            DatabaseTransaction.closeStatement(stmt);
        }
    }
     
     public static void updateLinker5Start(int id,  int info, Connection conn)    throws BecDatabaseException
    {
        String sql = "update assembledsequence  set LINKER5START="+info+      " where sequenceid="+id;
        
        DatabaseTransaction.executeUpdate(sql,conn);
     }
     public static void updateLinker3Stop(int id,  int info, Connection conn)    throws BecDatabaseException
    {
        String sql = "update assembledsequence  set LINKER3stop="+info+      " where sequenceid="+id;
        
        DatabaseTransaction.executeUpdate(sql,conn);
     }
     public static void updateCloneSequenceStatus(int id,  int status, Connection conn)    throws BecDatabaseException
    {
        String sql = "update assembledsequence  set ANALYSISSTATUS="+status+      " where sequenceid="+id;
        
        DatabaseTransaction.executeUpdate(sql,conn);
     }
   
    public int getRefsequenceCoveredLength()
    {
        int res = 0;
        int start = (m_cds_start >= m_linker5_start)? m_linker5_start : m_cds_start;
        int stop = (       m_cds_stop <=    m_linker3_stop ) ? m_linker3_stop :m_cds_stop;
        return stop - start ;
    }
    
   
      public String toHTMLString()
   {
       StringBuffer res = new StringBuffer();
       
       
       res.append("        0--------1---------2---------3---------4---------5---------6\n\n");
       String color_red = "<FONT COLOR=\"red\">";  
       String color_green = "<FONT COLOR=\"green\">";
       String color_blue="<FONT COLOR=\"blue\">";
       String color_orange="<FONT COLOR=\"orange\">";
       
    
       getScoresAsArray();
       int seqIndex=1;
       char[] text = m_text.toCharArray();
      
       for(int index = 0;index< text.length;index++)
        {
            //reformat for linker - cds
            text[index] = formatForLinkerCds(text[index],index);
            
            if(index == 0 ||  index % 60 == 0 )
            {
                res.append("\n");
                int pad_value = String.valueOf(index+1).length();
                String pad = "";
                for (int count = pad_value; count < 5; count++)
                {
                    pad +="0";
                }
                
                res.append(pad +(index+1) +" - ");
            }
            if ( m_scores_numbers != null)
            {
                if( m_scores_numbers[index] < 10)
                {
                    res.append(color_orange +text[index]+"</FONT>");
                }
                else if(m_scores_numbers[index] >=10 && m_scores_numbers[index] < 20)
                {
                    res.append(color_blue+text[index]+"</font>");
                }
                else if(m_scores_numbers[index] >=20 && m_scores_numbers[index] < 25)
                {
                    res.append(color_green+text[index]+"</font>");
                }
                else if ( m_scores_numbers[index] >= 25)
                {
                    res.append(color_red+text[index]+"</font>");
                }
            }
            else
            {
                res.append(text[index]);
            }
            
       
       }
       
       return res.toString();
   }
   
      
      //**********************************************************
     private static ArrayList getByRule( String sql)throws BecDatabaseException
     {
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        ResultSet rs = null;
        ArrayList sequences = new ArrayList();
        CloneSequence seq = null;
        try
        {
            rs = t.executeQuery(sql);
            while(rs.next())
            {
                seq = new CloneSequence( rs.getInt ("SEQUENCEID") );
                sequences.add(seq);
            }
            return sequences;
            
         }
         catch(Exception e)
         {
             throw new BecDatabaseException("Cannot extract sequence \nsql "+sql);
         }
     }
    private char formatForLinkerCds(char a, int pos)
    {
         if ( (pos >= m_linker5_start && pos < m_cds_start)
         || ( pos > m_cds_stop && pos <= m_linker3_stop))
        {
            return Character.toLowerCase(a);
        }
        else if (  pos >= m_cds_start &&  pos <= m_cds_stop )
        {
            return Character.toUpperCase(a);
        }
         return a;
    }
    //__________________________________________________________________________
    
         public static void main(String [] args)
    {
        try
        {
             CloneSequence cl = new CloneSequence(14800);
           
            System.out.println( cl.toHTMLString());
           // DatabaseTransaction t = DatabaseTransaction.getInstance();
           // TheoreticalSequence theoretical_sequence = TheoreticalSequence.findSequenceByGi(4503092);
            //int refseqid = theoretical_sequence.getId();
       
            //String query="ATGGAGCTACGTGTGGGGAACAAGTACCGCCTGGGACGGAAGATCGGGAGCGGGTCCTTCGGAGATATCTACCTGGGTGCCAACATCGCCTCTGGTGAGGAAGTCGCCATCAAGCTGGAGTGTGTGAAGACAAAGCACCCCCAGCTGCACATCGAGAGCAAGTTCTACAAGATGATGCAGGGTGGCGTGGGGATCCCGTCCATCAAGTGGTGCGGAGCTGAGGGCGACTACAACGTGATGGTCATGGAGCTGCTGGGGCCTAGCCTCGAGGACCTGTTCAACTTCTGTTCCCGCAAATTCAGCCTCAAGACGGTGCTGCTCTTGGCCGACCAGATGATCAGCCGCATCGAGTATATCCACTCCAAGAACTTCATCCACCGGGACGTCAAGCCCGACAACTTCCTCATGGGGCTGGGGAAGAAGGGCAACCTGGTCTACATCATCGACTTCGGCCTGGCCAAGAAGTACCGGGACGCCCGCACCCACCAGCACATTCCCTACCGGGAAAACAAGAACCTGACCGGCACGGCCCGCTACGCTTCCATCAACACGCACCTGGGCATTGAGCAAAGCCGTCGAGATGACCTGGAGAGCCTGGGCTACGTGCTCATGTACTTCAACCTGGGCTCCCTGCCCTGGCAGGGGCTCAAAGCAGCCACCAAGCGCCAGAAGTATGAACGGATCAGCGAGAAGAAGATGTCAACGCCCATCGAGGTCCTCTGCAAAGGCTATCCCTCCGAATTCTCAACATACCTCAACTTCTGCCGCTCCCTGCGGTTTGACGACAAGCCCGACTACTCTTACCTACGTCAGCTCTTCCGCAACCTCTTCCACCGGCAGGGCTTCTCCTATGACTACGTCTTTGACTGGAACATGCTGAAATTCGGTGCAGCCCGGAATCCCGAGGATGTGGACCGGGAGCGGCGAGAACACGAACGCGAGGAGAGGATGGGGCAGCTACGGGGGTCCGCGACCCGAGCCCTGCCCCCTGGCCCACCCACGGGGGCCACTGCCAACCGGCTCCGCAGTGCCGCCGAGCCCGTGGCTTCCACGCCAGCCTCCCGCATCCAGCCGGCTGGCAATACTTCTCCCAGAGCGATCTCGCGGGTCGACCGGGAGAGGAAGGTGAGTATGAGGCTGCACAGGGGTGCGCCCGCCAACGTCTCCTCCTCAGACCTCACTGGGCGGCAAGAGGTCTCCCGGATCCCAGCCTCACAGACAAGTGTGCCATTTGACCATCTCGGGAAGTTGG";
        
             //CloneSequence s = CloneSequence.getOneByIsolateTrackingId(988, "0,1,2","0,1,2");
             // ArrayList s1 = CloneSequence.getAllByIsolateTrackingId(988, "0,1,2","0,1,2");
             //System.out.print( s.getCodingSequence());
        } catch (Exception e)
        {
            System.out.println(e);
        }
        System.exit(0);
    }
}


