/*
 * Read.java
 *
 * Created on November 7, 2002, 11:53 AM
 */

package edu.harvard.med.hip.bec.coreobjects.endreads;

import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.modules.*;
import edu.harvard.med.hip.bec.*;
import java.sql.*;

import javax.sql.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.coreobjects.feature.*;
import edu.harvard.med.hip.bec.sampletracking.objects.*;
import edu.harvard.med.hip.bec.programs.phred.*;

import java.util.*;
import java.math.*;
/**
 *
 * @author  Administrator
 *class describes object read: result of one sequencing read
 */
public class Read
{
    
    
    public static final int TYPE_ENDREAD_REVERSE = -1;
    public static final int TYPE_ENDREAD_REVERSE_FAIL = -3;
    public static final int TYPE_ENDREAD_REVERSE_NO_MATCH = -4;
    public static final int TYPE_ENDREAD_REVERSE_SHORT = -5;
    
    public static final int TYPE_NOT_SET = 0;
    
    public static final int TYPE_ENDREAD_FORWARD_SHORT = 5;
    public static final int TYPE_ENDREAD_FORWARD_NO_MATCH = 4;
    public static final int TYPE_ENDREAD_FORWARD_FAIL = 3;
    public static final int TYPE_ENDREAD_FORWARD = 1;
    
    public static final int TYPE_INNER_REVERSE = -2;
    public static final int TYPE_INNER_FORWARD = 2;
    
    public static final int STATUS_ANALIZED = 1;     
    public static final int STATUS_NOT_ANALIZED = 0;  
    
    private int         m_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    private int         m_isolatetrackingid = BecIDGenerator.BEC_OBJECT_ID_NOTSET; 
  
    private int         m_readsequence_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    private AnalyzedScoredSequence m_readsequence = null;
    private int         m_clonesequence_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    private int         m_result_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    private int         m_type = TYPE_NOT_SET;     //end / reverse
    private String      m_abi_file_name = null;
    private String      m_abi_file_basename = null;
    private String      m_machine = null;
    private String      m_capilarity = null;
    private int         m_trimmingtype = PhredWrapper.TRIMMING_TYPE_NOT_TRIMMED;
    private int         m_score = Constants.SCORE_NOT_CALCULATED;
    private int         m_status = STATUS_NOT_ANALIZED;
    
    private int         m_trimmedstart = -1;
    private int         m_trimmedstop = -1;
    //1 based
    private int         m_cdsstart = 0;
    private int         m_cdsstop = 0;
    
    // *********************************************************************
    // these fields use only as intermidate data storage when read data
    // collected from Phredoutput : !~ they are not stored as part of READ DATA
    private int         int_cloneid = -1;
    private int         int_sequenceid = -1;
    private int         int_plateid = -1;
    private int         int_wellid = -1;
  
    public int         getFLEXCloneId (){ return int_cloneid    ;}
    public int         getFLEXSequenceid (){ return int_sequenceid    ;}
    public int         getFLEXPlate (){ return int_plateid    ;}
    public int         getFLEXWellid (){ return int_wellid    ;}
    public void        setFLEXReadInfo(int cloneid, int sequenceid, int plateid, int wellid)
    {
        int_cloneid = cloneid ;
        int_sequenceid = sequenceid;
        int_plateid = plateid;
        int_wellid = wellid;
    }
    //***********************************************************************
    
     /** Creates a new instance of Read */
    public Read( )throws BecDatabaseException
    {
       m_id = BecIDGenerator.getID("readid");
       
    }
    /*
    public Read(int id  )throws BecDatabaseException
    {
        
         m_id = id;
        String sql = "select  READID  ,SAMPLEID  ,ISOLATETRACKINGID  ,FUZZYSEQUENCEID"+ 
        ",READTYPE  ,ABIFILENAME  ,MACHINE  ,CAPILARITY  ,TRIMMINGTYPE  ,SCORE from read where readid="+id;
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        ResultSet rs = null;
        try
        {
            rs = t.executeQuery(sql);
            while(rs.next())
            {
                m_isolatetrackingid = rs.getInt("ISOLATETRACKINGID"); 
          
                m_scoredsequence_id = rs.getInt("FUZZYSEQUENCEID");
                m_readtype = rs.getInt("READTYPE");     //forward/reverse
                m_abi_file_name = rs.getString("ABIFILENAME");
                m_machine = rs.getString("MACHINE");
                m_capilarity = rs.getString("CAPILARITY");
                m_trimmingtype = rs.getInt("TRIMMINGTYPE");
                m_score = rs.getInt("SCORE");
            }
            m_scoredsequence = new ScoredSequence(m_scoredsequence_id);
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException("Error occured while restoring sequence with id "+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
    */
    
    public static ArrayList getReadByIsolateTrackingId(int istr_id  )throws BecDatabaseException
    {
        return getReadByRule("ISOLATETRACKINGID="+istr_id);
    }
    
    public static Read getReadById(int id  )throws BecDatabaseException
    {
        return (Read)getReadByRule("readID="+id).get(0);
    }
    public static Read getReadByReadSequenceId(int id  )throws BecDatabaseException
    {
        return (Read)getReadByRule("readsequenceID="+id).get(0);
    }
    public void insert(Connection conn) throws BecDatabaseException
    {
        Statement stmt = null;
        String sql =""; String values = "";
        try
        {
            DatabaseTransaction dt = DatabaseTransaction.getInstance();
            //insert sequence
            if ( m_readsequence != null)
            {
                m_readsequence.insert(conn);
                m_readsequence_id = m_readsequence.getId();
            }
            sql = "insert into readinfo (readid, readsequenceid, trimmedtype,trimmedstart,trimmedend,readtype, score,status ";
            values = " values(" + m_id + ","+ m_readsequence_id  +","+m_trimmingtype+","+m_trimmedstart+","+m_trimmedstop +","+m_type +","+m_score+","+m_status;
             
            if ( m_isolatetrackingid != BecIDGenerator.BEC_OBJECT_ID_NOTSET)
            {
                sql += ", isolatetrackingid"; values+= ","+ m_isolatetrackingid;
            }
            
            if( m_clonesequence_id != BecIDGenerator.BEC_OBJECT_ID_NOTSET)
            {
                sql += ", assembledsequenceid"; values+= ","+ m_clonesequence_id;
            }
            if (m_result_id != BecIDGenerator.BEC_OBJECT_ID_NOTSET)
            {
                sql += ", resultid"; values+= ","+ m_result_id;
            }
            
            sql = sql +")" + values +")";
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            
            
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException(sqlE);
        } finally
        {
            DatabaseTransaction.closeStatement(stmt);
        }
    }
    public void updateScore(Connection conn) throws BecDatabaseException
    {
        String sql = "update readinfo set score="+ m_score +   " where readid="+m_id;
        DatabaseTransaction.executeUpdate(sql, conn);
    }
   public void updateStatus(Connection conn) throws BecDatabaseException
    {
        String sql = "update readinfo set status="+ m_status +   " where readid="+m_id;
        DatabaseTransaction.executeUpdate(sql, conn);
    }
    public  void updateType(Connection conn) throws BecDatabaseException
    {
        String sql = "update readinfo set readtype="+ m_type +   " where readid="+m_id;
        DatabaseTransaction.executeUpdate(sql, conn);
    }
     public  void updateCdsStartStop(Connection conn) throws BecDatabaseException
    {
        String sql = "update readinfo set cdsstart="+ m_cdsstart +   ", cdsstop= "+m_cdsstop+" where readid="+m_id;
        DatabaseTransaction.executeUpdate(sql, conn);
    }
    public int         getId(){ return m_id  ;}
    public int         getIsolateTrackingId(){ return m_isolatetrackingid  ;} 
 
    
    public int getSequenceId(){ return m_readsequence_id  ;}
    public AnalyzedScoredSequence getSequence() throws BecDatabaseException
    { 
        if (m_readsequence == null)
            m_readsequence = new AnalyzedScoredSequence(m_readsequence_id);
        return m_readsequence;
    }
    
    public String      getTrimmedSequence()throws BecDatabaseException
    {
        if (m_readsequence == null)
            getSequence();
       return m_readsequence.getText().substring(m_trimmedstart , m_trimmedstop);
    }
    
    
    public int[]      getTrimmedScoresAsArray()throws BecDatabaseException
    {
        if (m_readsequence == null)            getSequence();
        if (m_readsequence.getScores() == null) return null;
        int[] scores = m_readsequence.getScoresAsArray();
        int[] res = new  int[m_trimmedstop-m_trimmedstart];
        int count = 0;
        for (int i = m_trimmedstart; i < m_trimmedstop; i++)
        {
            res[count++] = scores[i];
        }
       return res;
    }
    public int[]      getScoresAsArray()throws BecDatabaseException
    {
        if (m_readsequence == null)            getSequence();
        if (m_readsequence.getScores() == null) return null;
        int[] scores = m_readsequence.getScoresAsArray();
       
       return scores;
    }
     public String     getTrimmedScores()throws BecDatabaseException
    {
        if (m_readsequence == null)            getSequence();
        if (m_readsequence.getScores() == null) return null;
        int[] scores = m_readsequence.getScoresAsArray();
        String res="" ;
        int count = 0;
        for (int i = m_trimmedstart; i < m_trimmedstop; i++)
        {
            res += scores[i]+" ";
        }
       return res;
    }
    
    
    public int         getCloneSequenceId(){ return m_clonesequence_id  ;}
    public int         getResultId(){ return m_result_id  ;}
    public int         getType(){ return m_type  ;}     //end / reverse/inner/end
    public String      getTraceFileName(){ return m_abi_file_name  ;}
    public String      getTraceFileBaseName(){ return m_abi_file_basename ;}
    public String      getMachine(){ return m_machine  ;}
    public String      getCapilarity(){ return m_capilarity  ;}
    public int         getTrimType(){ return m_trimmingtype  ;}
    public int         getScore(){ return m_score  ;}
    public int         getTrimStart(){ return m_trimmedstart  ;}
    public int         getTrimEnd(){ return m_trimmedstop  ;}
     public int         getCdsStart(){ return m_cdsstart ;}
    public int         getCdsStop(){ return m_cdsstop ;}
    public String       getTypeAsString() {return getTypeAsString(m_type);}
    
    public static String       getTypeAsString(int type)
    {
        switch (type)
        {
            case  TYPE_ENDREAD_REVERSE : return "Reverse";
            case  TYPE_ENDREAD_REVERSE_FAIL : return "Reverse Fail";
            case  TYPE_ENDREAD_REVERSE_NO_MATCH : return "Reverse No Match";
            case  TYPE_ENDREAD_REVERSE_SHORT : return "Reverse Short";
            case  TYPE_NOT_SET : return "Not set";
            case  TYPE_ENDREAD_FORWARD_SHORT : return "Forward Short";
            case  TYPE_ENDREAD_FORWARD_NO_MATCH : return "Forward No Match";
            case  TYPE_ENDREAD_FORWARD_FAIL : return "Forward Fail";
            case  TYPE_ENDREAD_FORWARD : return "Forward";
            case  TYPE_INNER_REVERSE : return "Reverse Inner";
            case  TYPE_INNER_FORWARD : return "Forward Inner";
            default : return "";
        }
    }
    public int              getStatus(){ return m_status;}
    public String          getStatusAsString(int status)
    { 
        switch(status)
        {
            case  STATUS_ANALIZED : return "Read sequence analized";
            case STATUS_NOT_ANALIZED : return "Read sequence not analized";
            default : return "";
        }
    }
    
   public void         setId(int v){  m_id  = v;}
    public void         setIsolateTrackingId(int v){  m_isolatetrackingid  = v;} 


    public void setSequenceId(int v){  m_readsequence_id  = v;}
    
    public void         setCloneSequenceId(int v){  m_clonesequence_id  = v;}
    public void         setResultId(int v){  m_result_id  = v;}
    public void         setType(int v){  m_type  = v;}     //end / reverse
    public void          setTraceFileName(String v){  m_abi_file_name  = v;}
    public void          setTraceFileBaseName(String v){  m_abi_file_basename  = v;}
    public void          setMachine(String v){  m_machine  = v;}
    public void         setCapilarity(String v){  m_capilarity  = v;}
    public void         setTrimType(int v){  m_trimmingtype  = v;}
    public void         setScore(int v){  m_score  = v;}
    public void         setTrimStart(int v){  m_trimmedstart  = v;}
    public void         setTrimEnd(int v){  m_trimmedstop  = v;}
    public void         setCdsStart(int v){  m_cdsstart =v;}
    public void         setCdsStop(int v){ m_cdsstop = v ;}
    public void         setStatus(int v){ m_status = v;}
    public void         setSequence(String text, String scores, int refseqid)
    {
        m_readsequence = new AnalyzedScoredSequence(text,scores,refseqid);
    }
    public void         setSequence( int seqid) throws BecDatabaseException
    {
        m_readsequence = new AnalyzedScoredSequence(seqid);
    }
    
    
    public int findResultIdFromFlexInfo(int result_type) throws BecDatabaseException
    {
        Result result = null;
        //find sample for the clone described by read
        String sql = "select resultid, resulttype from result where "
        +" resulttype ="+result_type
        +" and sampleid = "
        +" ( select sample.sampleid from  sample, isolatetracking iso "
        +" where sample.sampleid = iso.sampleid and sample.position = "+int_wellid
        +" and iso.isolatetrackingid in (  select isolatetrackingid from flexinfo "
        +" where flexsequencingplateid = "+int_plateid+" ))";
        RowSet rs = null;
        int r_type = -1; int r_id = -1;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next())
            {
                r_type =  rs.getInt("resulttype");
                r_id =  rs.getInt("resultid");
              
            }
            return r_id;
        } catch (Exception sqlE)
        {
            throw new BecDatabaseException("Error occured while getting sample with id:\n"+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
        
    }
    
    
    
    
    
    public int  calculateScore(EndReadsSpec spec) throws BecDatabaseException,BecUtilException
    { 
        
        if (m_type == Read.TYPE_ENDREAD_FORWARD || m_type == Read.TYPE_ENDREAD_REVERSE)
        {
            m_score = calculateScoreBasedOnSpec(spec );
        }
        return m_score ;
    }
    
    
    
    //      ----------------- private methods ----------------
    
    private static ArrayList getReadByRule(String rule  )throws BecDatabaseException
    {
        
        
        ArrayList reads = new ArrayList();
        String sql = "select  READID  ,MACHINE  ,CAPILARITY  ,READSEQUENCEID ,TRIMMEDTYPE "
        +"  ,TRIMMEDSTART  ,TRIMMEDEND  ,READTYPE  ,ASSEMBLEDSEQUENCEID  ,RESULTID  ,SCORE "
        +" ,ISOLATETRACKINGID , cdsstart,cdsstop, status from READINFO where " + rule;
       
        ResultSet rs = null; Read read = null;
        try
        {
             DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while(rs.next())
            {
                read = new Read();
                read.setId( rs.getInt("READID") ); 
                read.setSequenceId( rs.getInt("READSEQUENCEID"));
                read.setCapilarity(rs.getString("CAPILARITY"));
                read.setType( rs.getInt("READTYPE") );     //forward/reverse
                read.setCloneSequenceId(rs.getInt("ASSEMBLEDSEQUENCEID") );
                read.setIsolateTrackingId(rs.getInt("ISOLATETRACKINGID") );
                read.setMachine(rs.getString("MACHINE"));
                read.setResultId( rs.getInt("RESULTID"));
                read.setScore(rs.getInt("SCORE"));
                read.setTrimEnd( rs.getInt("TRIMMEDEND"));
                read.setTrimStart( rs.getInt("TRIMMEDSTART"));
                read.setSequence ( read.getSequenceId() );
                read.setCdsStart( rs.getInt("cdsstart"));
                read.setCdsStop( rs.getInt("cdsstop"));
                read.setStatus(rs.getInt("status"));
                reads.add(read);
            }
            return reads;
           
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException("Error occured while restoring sequence with id "+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
    
    
    private int calculateScoreBasedOnSpec(EndReadsSpec spec)throws BecDatabaseException,BecUtilException
    {
     //   ArrayList rna_discrepancies = m_readsequence.getDiscrepanciesByType(Mutation.RNA);
        //no discrepancies
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//####################################
        if (m_readsequence.getDiscrepancies() == null || m_readsequence.getDiscrepancies().size() == 0)
            return 0;
        
      
        int score = Constants.SCORE_NOT_CALCULATED; 
        int dtype = -1; int dquality = -1; int penalty = 0;
        
        ArrayList discrepancy_definitions = DiscrepancyDescription.assembleDiscrepancyDefinitions(
                                                m_readsequence.getDiscrepancies());
        score = DiscrepancyDescription.getPenalty( discrepancy_definitions, spec);
        int length_to_normalize = refsequenceCoveredLength();
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//####################################
        if ( length_to_normalize == 0)           return 0;
        return score;
    }
    
    
     //get not amb read
    public boolean isPassAmbiquoutyTest(FullSeqSpec cutoff_spec) throws BecDatabaseException
    {
    
       String read_sequence_text = getTrimmedSequence();
       return BaseSequence.isPassAmbiquoutyTest(cutoff_spec,read_sequence_text);
       
    }
    
    //define length of refsequence covered by read
    public int refsequenceCoveredLength()
    {
        int length = 0;
         if (m_cdsstart != 0 && m_cdsstop != 0)//whole sequence is covered by one read
        {
            length = Math.abs( m_cdsstop - m_cdsstart);
        }
        else if (m_cdsstart > 0 && m_cdsstop == 0)//forward read, sequence not covered
        {
            length = m_trimmedstop - m_cdsstart ;
        }
        else if (m_cdsstart == 0 && m_cdsstop > 0)//reverse read, not compliment,sequence not covered
        {
            length = m_cdsstop - m_trimmedstart;
        }
         else if (m_cdsstart < 0 && m_cdsstop == 0)//forward read, compliment,sequence not covered
        {
            length = Math.abs(m_cdsstart) - m_trimmedstart ;
        }
           else if (m_cdsstart == 0 && m_cdsstop < 0)//reverse read, compliment,sequence not covered
        {
            length = m_trimmedstop - Math.abs(m_cdsstop)   ;
        }
       return length;
    }
    
  
      public static void main(String args[])
    {
        Read r = null;
        try
        {
            //r = Read.getReadById(12071);
           
             // System.out.println(r.getSequence().getText().length() +" "+ r.getTrimStart() +" "+r.getTrimEnd());
             // System.out.println(r.getSequence().getText());
              //System.out.println(r.getSequence().getScores());
            //  System.out.println(r.getTrimmedSequence());
            //  System.out.println(r.getTrimmedScores());
               
   
 Read read =  Read.getReadById(5088);
 ScoredSequence c = read.getSequence();
 char[] a = c.getText().toCharArray();
 int[] b = c.getScoresAsArray();
 for (int count =0; count < c.getText().length();count++)
 {
     System.out.println(count+" "+a[count]+" "+b[count]);
 }
 /*
   int containerid = BaseSequence.getContainerId(3003, BaseSequence.READ_SEQUENCE);
   edu.harvard.med.hip.bec.coreobjects.oligo.Oligo[] oligos = Container.findEndReadsOligos(containerid);
   boolean  isCompliment = false;
                if (oligos[0] != null && (read.getType() ==  Read.TYPE_ENDREAD_FORWARD || 
                            read.getType() == Read.TYPE_ENDREAD_FORWARD_FAIL || 
                            read.getType() == Read.TYPE_ENDREAD_FORWARD_NO_MATCH || 
                            read.getType() == Read.TYPE_ENDREAD_FORWARD_SHORT))
                {
                       isCompliment = (oligos[0].getOrientation() == edu.harvard.med.hip.bec.coreobjects.oligo.Oligo.ORIENTATION_SENSE);
                }
                if (oligos[1] != null && (read.getType() ==  Read.TYPE_ENDREAD_REVERSE || 
                            read.getType() == Read.TYPE_ENDREAD_REVERSE_FAIL || 
                            read.getType() == Read.TYPE_ENDREAD_REVERSE_NO_MATCH || 
                            read.getType() == Read.TYPE_ENDREAD_REVERSE_SHORT))
                {
                      isCompliment =( oligos[1].getOrientation() == edu.harvard.med.hip.bec.coreobjects.oligo.Oligo.ORIENTATION_SENSE) ; 
                }
//r = Read.getReadById(19847);System.out.println(r.getId()+" "+r.getTrimmedSequence());System.out.println(r.getId()+" "+r.getTrimmedScores());
//r = Read.getReadById(19856);System.out.println(r.getId()+" "+r.getTrimmedSequence());System.out.println(r.getId()+" "+r.getTrimmedScores());
//r = Read.getReadById(19858);System.out.println(r.getId()+" "+r.getTrimmedSequence());System.out.println(r.getId()+" "+r.getTrimmedScores());
//r = Read.getReadById(19862);System.out.println(r.getId()+" "+r.getTrimmedSequence());System.out.println(r.getId()+" "+r.getTrimmedScores());
//r = Read.getReadById(20030);System.out.println(r.getId()+" "+r.getTrimmedSequence());System.out.println(r.getId()+" "+r.getTrimmedScores());
*/
        }
        catch(Exception e){}
         System.exit(0);
      }
     
}
