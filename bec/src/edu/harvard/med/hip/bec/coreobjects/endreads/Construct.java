/*
 * AgarTracking.java
 *
 * Created on September 24, 2002, 4:14 PM
 */

package edu.harvard.med.hip.bec.coreobjects.endreads;

import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.util.*;
import java.sql.*;
import javax.sql.*;
import java.util.*;
import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.ui_objects.*;
/**
 *
 * @author  htaycher
 */
public class Construct
{
    public static final int     FORMAT_OPEN =0;
    public static final int     FORMAT_CLOSE = 1;
    
    
    private int             m_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;//engine id
   
    private ArrayList       m_isolate_ids = null;// – refers to four Isolate objects
    private ArrayList       m_isolates = null;
    private int             m_current_index = -1;//– id of isolate that is currently processing for full sequencing
    private int             m_refsequence_id = -1;//– can be extracted from the original sample
    private RefSequence     m_refsequence = null;
    private BaseSequence    m_refsequence_for_analysis = null;
    private int             m_format = FORMAT_OPEN;//sequence format
    
    private int             m_cloning_strategy_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;	
    private CloningStrategy m_cloning_strategy = null;
  //  private int             m_vector_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;	
   // private BioVector       m_vector = null;
  //  private int             m_linker3_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
  //  private BioLinker       m_linker3 = null;
  //  private BioLinker       m_linker5 = null;
  //  private int             m_linker5_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    
    /** Creates a new instance of AgarTracking */
    public Construct()throws BecDatabaseException
    {
        
    }
    /** Creates a new instance of AgarTracking */
    public Construct(int id) throws BecDatabaseException
    {
       
     //   String sql ="select REFSEQUENCEID  ,FORMAT,VECTORID,LINKER3ID,LINKER5ID,constructID  from sequencingCONSTRUCT ";
        String sql ="select REFSEQUENCEID  ,FORMAT,cloningstrategyid,constructID , currentindex from sequencingCONSTRUCT where constructid = "+id;
        ResultSet rs = null;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance(); 
            rs = t.executeQuery(sql);
            while(rs.next())
            {
                  m_id = rs.getInt("CONSTRUCTID");
                  m_current_index = rs.getInt("CURRENTINDEX");
                  m_refsequence_id = rs.getInt("REFSEQUENCEID");
                  m_format = rs.getInt("FORMAT");
                  m_cloning_strategy_id = rs.getInt("cloningstrategyid");
                 // m_linker3_id =  rs.getInt("LINKER3ID");
                //  m_linker5_id = rs.getInt("LINKER5ID");
                  
            } 
            
        }
        catch (Exception sqlE)
        {
            throw new BecDatabaseException("Error occured while restoring construct with id "+id+"\n"+sqlE+"\nSQL: "+sql);
        } 
        finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
    
    
   // public Construct(int id, int curind, int seqid, int format, int vectid, int l3id, int l5id)
    public Construct(int id, int curind, int seqid, int format, int strategyid)
    {
        m_id = id;
        m_current_index = curind;//– id of isolate that is currently processing for full sequencing
        m_refsequence_id = seqid;//– can be extracted from the original sample
        m_format = format;
        m_cloning_strategy_id = strategyid;
       // m_vector_id = vectid;	
      //  m_linker3_id = l3id;
       // m_linker5_id = l5id;
    }
    
    public void insert(Connection conn) throws BecDatabaseException
    {
        if ( m_id == BecIDGenerator.BEC_OBJECT_ID_NOTSET)
            m_id = BecIDGenerator.getID("constructid");
      //  String sql = "insert into sequencingconstruct (constructid, REFSEQUENCEID  ,FORMAT,VECTORID,LINKER3ID,LINKER5ID ";
      //  String valuesql = "values ("+m_id +","+ m_refsequence_id +","+m_format+","+m_vector_id + ","+m_linker3_id+","+m_linker5_id;
        
          String sql = "insert into sequencingconstruct (constructid, REFSEQUENCEID  ,FORMAT,cloningstrategyid ";
        String valuesql = "values ("+m_id +","+ m_refsequence_id +","+m_format+","+m_cloning_strategy_id;
        
        if(m_current_index != -1)
        {
            sql += ",currentindex ";
            valuesql +=  "," + m_current_index;
        }
        
        sql = sql +")"+ valuesql+")";
        Statement stmt = null;
        try
        {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
           
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException(sqlE+"\nSQL: "+sql);
        } 
        finally
        {
            DatabaseTransaction.closeStatement(stmt);
        }
    }
    
    public int                  getId(){ return  m_id;}//engine id
   
    public ArrayList            getIsolateTrackingIds(){ return  m_isolate_ids ;}// – refers to four Isolate objects
    public ArrayList            getIsolateTrackings(){ return  m_isolates ;}
    public int                  getCurrentIsolateId(){ return  m_current_index ;}//– id of isolate that is currently processing for full sequencing
    public int                  getRefSeqId(){ return  m_refsequence_id;}//– can be extracted from the original sample
    public RefSequence        getRefSequence()throws BecDatabaseException
    { 
        if(m_refsequence == null) 
            m_refsequence = new RefSequence(m_refsequence_id); 
        return m_refsequence;
    }
   
    public int                  getFormat(){ return  m_format;}//sequence format
    public String               getFormatAsString()
    {
        if (m_format ==   FORMAT_OPEN )
            return "Fusion";
        else
            return "Closed";
    }
   
    
    public int                  getCloningStrategyId(){ return m_cloning_strategy_id;}
    public CloningStrategy      getCloningStrategy()throws BecDatabaseException
    { 
        if( m_cloning_strategy == null)
            m_cloning_strategy =  CloningStrategy.getById(m_cloning_strategy_id);
        return m_cloning_strategy;
    }
    public int                  getLinker3Id()throws BecDatabaseException
    { 
         if( m_cloning_strategy == null)
            m_cloning_strategy =  CloningStrategy.getById(m_cloning_strategy_id);
        return  m_cloning_strategy.getLinker3Id() ;
    }	
    public int                  getVectorId()throws BecDatabaseException
    { 
        if( m_cloning_strategy == null)
            m_cloning_strategy =  CloningStrategy.getById(m_cloning_strategy_id);
        return  m_cloning_strategy.getVectorId() ;
    }	
    public int                  getLinker5Id()throws BecDatabaseException
    {
         if( m_cloning_strategy == null)
            m_cloning_strategy =  CloningStrategy.getById(m_cloning_strategy_id);
        return  m_cloning_strategy.getLinker5Id() ;
    }	
   // public BioLinker            getLinker3(){ return  m_linker3 ;}	
   // public BioVector            getVector(){ return  m_vector ;}	
   // public BioLinker            getLinker5(){ return  m_linker5 ;}	
     
    public void                  setId(int v){    m_id= v;}//engine id
    public void                  addIsolateTracking(IsolateTrackingEngine is){  if (m_isolates==null) m_isolates = new ArrayList(); m_isolates.add(is) ;}
    public void                  setCurrentIndex(int v){    m_current_index = v;}//– id of isolate that is currently processing for full sequencing
    public void                  setRefSeqId(int v){    m_refsequence_id= v;}//– can be extracted from the original sample
    public void                  setFormat(int v){    m_format= v;}//sequence format
    public void                  setCloningStrategyId(int i){  m_cloning_strategy_id = i;}
  //  public void                  setLinker3Id(int v){    m_linker3_id = v;}	
  //  public void                  setVectorId(int v){    m_vector_id = v;}	
   // public void                  setLinker5Id(int v){    m_linker5_id = v;}	
 
     public static  BaseSequence         getRefSequenceForAnalysis(String startCodon,
                            String fusionStopCodon,String closedStopCodon,
                            String codingsequence, int format)
                            throws BecDatabaseException
    { 
        
          if ( !startCodon.equalsIgnoreCase("NON"))
         {
             codingsequence = startCodon + codingsequence.substring(3);
         }
        // seq = seq.substring(3, seq.length()-3);
         if (format == FORMAT_OPEN)
         {
             codingsequence =  codingsequence.substring( 0,codingsequence.length()-3 )+ fusionStopCodon;
         }
         else if (format == FORMAT_CLOSE && !closedStopCodon.equalsIgnoreCase("NON"))
         {
              codingsequence = codingsequence.substring( 0,codingsequence.length()-3) + closedStopCodon;
         }
             
         BaseSequence refsequence_for_analysis =  new BaseSequence(codingsequence, BaseSequence.BASE_SEQUENCE );
         return refsequence_for_analysis;
     }
     
    public BaseSequence         getRefSequenceForAnalysis(String startCodon,
                            String fusionStopCodon,String closedStopCodon)
                            throws BecDatabaseException
      { 
         if(m_refsequence == null) 
            m_refsequence = new RefSequence(m_refsequence_id);
         String codingsequence = m_refsequence.getCodingSequence();
          m_refsequence_for_analysis =  getRefSequenceForAnalysis( startCodon,
                             fusionStopCodon, closedStopCodon,
                             codingsequence, m_format);
         return m_refsequence_for_analysis;
         /*
         if ( !startCodon.equalsIgnoreCase("NONE"))
         {
             seq = startCodon + seq.substring(3);
         }
        // seq = seq.substring(3, seq.length()-3);
         if (m_format == FORMAT_OPEN)
         {
             seq =  seq.substring( seq.length()-3 )+ fusionStopCodon;
         }
         else if (m_format == FORMAT_CLOSE && !closedStopCodon.equalsIgnoreCase("NONE"))
         {
              seq = seq.substring( seq.length()-3) + closedStopCodon;
         }
             
         m_refsequence_for_analysis =  new BaseSequence(seq, BaseSequence.BASE_SEQUENCE );
         return m_refsequence_for_analysis;
          **/
     }
    /*
    public void calculateRank()
    {
        //sort isolate tracking by score
      
        Collections.sort(m_isolates, new Comparator()
        {
            public int compare(Object o1, Object o2)
            {
                IsolateTrackingEngine is1 = (IsolateTrackingEngine) o1;
                IsolateTrackingEngine is2 = (IsolateTrackingEngine) o2;
                return -(   is1.getScore() - is2.getScore()  );
            }
            // Note: this comparator imposes orderings that are inconsistent with equals. 
            public boolean equals(java.lang.Object obj)
            {      return false;  }
            // compare
        } );
        //assign rank
        int rank = 1;
        for (int count = 0; count < m_isolates.size(); count++)
        {
            IsolateTrackingEngine is = (IsolateTrackingEngine) m_isolates.get(count);
            if (is.getRank() != IsolateTrackingEngine.RANK_BLACK && is.getRank() != IsolateTrackingEngine.RANK_NOT_APPLICABLE &&
                is.getStatus() != IsolateTrackingEngine.PROCESS_STATUS_SUBMITTED_EMPTY)
            {
               is.setRank(rank++);
               if ( is.getRank() == 1)
               {
                   m_current_index = is.getSampleId();
               }
            }
            
        }
    }
    */
    
     public void calculateRank(int refsequence_length, FullSeqSpec cutoff_spec)
                throws BecDatabaseException
    {
      
        
        double expected_score = calculateExpectedScore();
        //assign score to isolte
// S1 = (RS1 * RL1 + ExpectedScore * (CDSLenght - RL1)) / CDSLenght;

//(RL1 -- overlap length, S1 - isolate score)
         IsolateTrackingEngine it = null;
         int cds_coverage = 0;
         for (int isolate_count = 0; isolate_count < m_isolates.size(); isolate_count++)
        {
            it = (IsolateTrackingEngine)m_isolates.get(isolate_count);
            
            if ( it.getRank() != IsolateTrackingEngine.RANK_BLACK)
            {
                cds_coverage =  it.getCdsLengthCovered();
                if ( cds_coverage == 0)
                    cds_coverage = it.getCdsLengthCovered( refsequence_length,  cutoff_spec);
                it.setScore( (int) (1000 * (it.getScore() * cds_coverage + expected_score * ( refsequence_length - cds_coverage)) / refsequence_length ));
            }
        }
        //sort isolate tracking by score
      
        Collections.sort(m_isolates, new Comparator()
        {
            public int compare(Object o1, Object o2)
            {
                IsolateTrackingEngine is1 = (IsolateTrackingEngine) o1;
                IsolateTrackingEngine is2 = (IsolateTrackingEngine) o2;
                return -(   is1.getScore() - is2.getScore()  );
            }
            /** Note: this comparator imposes orderings that are
             * inconsistent with equals. */
            public boolean equals(java.lang.Object obj)
            {      return false;  }
            // compare
        } );
        //assign rank
        int rank = 1;
        
        
        for (int count = 0; count < m_isolates.size(); count++)
        {
            IsolateTrackingEngine is = (IsolateTrackingEngine) m_isolates.get(count);
            if (is.getRank() != IsolateTrackingEngine.RANK_BLACK && is.getRank() != IsolateTrackingEngine.RANK_NOT_APPLICABLE &&
                is.getStatus() != IsolateTrackingEngine.PROCESS_STATUS_SUBMITTED_EMPTY)
            {
               is.setRank(rank++);
               if ( is.getRank() == 1)
               {
                   m_current_index = is.getSampleId();
               }
            }
            
        }
    }
    
   
     private double calculateExpectedScore()
     {
          /*
        ExpectedScore = ((RS1 + RS2 + RS3 + RS4) / 4) - small number;

(RS -- read(s) score, sum(penalties) / overlap_length)

S1 = (RS1 * RL1 + ExpectedScore * (CDSLenght - RL1)) / CDSLenght;

(RL1 -- overlap length, S1 - isolate score)
*/
        double expectedScore = -0.05;int isolate_number = 0;
        int isolate_score = 0;IsolateTrackingEngine it = null;
        for (int isolate_count = 0; isolate_count < m_isolates.size(); isolate_count++)
        {
            it = (IsolateTrackingEngine)m_isolates.get(isolate_count);
            if ( it.getRank() != IsolateTrackingEngine.RANK_BLACK)
            {
                isolate_number++;
                isolate_score+=it.getScore();
            }
        }
        if (isolate_number == 0) return expectedScore;
        expectedScore += isolate_score / isolate_number;
        return expectedScore;
     }
     
    public void updateCurrentIndex(int ind)throws BecDatabaseException
    {
        m_current_index = ind;
    }
    
    public void updateCurrentIndex(int ind, Connection conn)throws BecDatabaseException
    {
        m_current_index = ind;
         String sql = "update sequencingconstruct "+
        " set currentindex="+ind+" where constructid="+ m_id;
        
        DatabaseTransaction.executeUpdate(sql, conn);
    }
    
    //function used for UI of construct report
    // change isolate ranking
    public static ArrayList getClonesData(int construct_id)throws BecDatabaseException
    {
        ArrayList clones_data = new ArrayList();
       
        UICloneSample clone_sample = null;
         String sql = "select label, position, iso.status as status,rank,isolatetrackingid  "
+" from isolatetracking iso,  sample s, containerheader c "
+" where  s.sampleid=iso.sampleid and s.containerid=c.containerid and constructid="+construct_id+" order by position";
        RowSet rs = null;
       
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next())
            {
                clone_sample = new UICloneSample();
                clone_sample.setPlateLabel(rs.getString("label"));
                clone_sample.setPosition(rs.getInt("position"));
                clone_sample.setRank(rs.getInt("rank"));
                clone_sample.setCloneStatus(rs.getInt("status"));
                clone_sample.setIsolateTrackingId(rs.getInt("isolatetrackingid"));
                clones_data.add(clone_sample);
            }
            return clones_data;
        } catch (Exception e)
        {
            throw new BecDatabaseException("Error occured while getting clones data for construct with id:\n"+construct_id+"\nSQL: "+e.getMessage());
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
    
    
    //function gets all constructs that corespond to set of plates
    //with at least one isolate with status isolate_status
    public static ArrayList getConstructsFromPlates( ArrayList plate_ids, String clone_sequence_status, String clone_sequence_type, int mode)throws BecDatabaseException
    {
       
        String int_plateids = "";
        
        for (int i = 0; i < plate_ids.size() ;i++)
        {
            int_plateids += ((Integer)plate_ids.get(i)).intValue();
            if (i != plate_ids.size() - 1) int_plateids+=",";
        }
        return getConstructsFromPlates(  int_plateids, clone_sequence_status,  clone_sequence_type, mode );
    }
     
     //function gets all constructs that corespond to set of plates
    //with at least one isolate with status isolate_status
     public static ArrayList getConstructsFromPlate( int plate_id, String clone_sequence_status, String clone_sequence_type, int mode)throws BecDatabaseException
    {
        return getConstructsFromPlates(  String.valueOf(plate_id),  clone_sequence_status,  clone_sequence_type,  mode);
    }
    
     private static ArrayList getConstructsFromPlates( String plate_ids,String clone_sequence_status,String clone_sequence_type, int mode)throws BecDatabaseException
     {
         ArrayList constructs = new ArrayList();
        Hashtable constructs_hash = new Hashtable();
        /*
           String sql = "select const.constructid as constructid, refsequenceid,format,linker3id,"
         +" linker5id,vectorid,iso.isolatetrackingid as isolatetrackingid,status, score, rank, sampleid "
        +" from isolatetracking iso, sequencingconstruct const"
        +"  where iso.constructid=const.constructid and iso.isolatetrackingid in "
         +" (  select isolatetrackingid from isolatetracking where sampleid in "
        +" (select sampleid from sample where containerid "
        +" in ( "+plate_ids+" ) )) order by  const.constructid ";
         **/
          String sql = "select const.constructid as constructid, refsequenceid,format,cloningstrategyid,"
         +" iso.isolatetrackingid as isolatetrackingid,status, ASSEMBLY_STATUS,score, rank, sampleid "
        +" from isolatetracking iso, sequencingconstruct const"
        +"  where iso.constructid=const.constructid and iso.isolatetrackingid in "
         +" (  select isolatetrackingid from isolatetracking where sampleid in "
        +" (select sampleid from sample where containerid "
        +" in ( "+plate_ids+" ) )) order by  const.constructid ";
        RowSet rs = null;
       
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next())
            {
                //create isolate tracking 
                IsolateTrackingEngine istr = new IsolateTrackingEngine();
                istr.setRank(rs.getInt("rank") ) ;// results of the end read analysis
                istr.setScore(rs.getInt("score") );// results of the end read analysis
                istr.setStatus(rs.getInt("status") );
                istr.setSampleId(rs.getInt("sampleid") );
                istr.setId( rs.getInt("isolatetrackingid") );
                istr.setAssemblyStatus( rs.getInt("ASSEMBLY_STATUS"));
                istr.setConstructId( rs.getInt("constructid"));// identifies the agar; several (four) isolates will have the same id
                
                
                // exstruct reads if not empty sample
                if (istr.getStatus() != IsolateTrackingEngine.PROCESS_STATUS_SUBMITTED_EMPTY
                    && istr.getStatus() != IsolateTrackingEngine.PROCESS_STATUS_ER_NO_READS)
                {
                    //check whether assembled sequence is available
                    if (istr.getAssemblyStatus() == IsolateTrackingEngine.ASSEMBLY_STATUS_FAILED_LINKER5_NOT_COVERED 
                    || istr.getAssemblyStatus() == IsolateTrackingEngine.ASSEMBLY_STATUS_FAILED_LINKER3_NOT_COVERED 
                    ||istr.getAssemblyStatus() == IsolateTrackingEngine.ASSEMBLY_STATUS_FAILED_BOTH_LINKERS_NOT_COVERED
                    || istr.getAssemblyStatus() == IsolateTrackingEngine.ASSEMBLY_STATUS_PASS
                    || istr.getAssemblyStatus() == IsolateTrackingEngine.ASSEMBLY_STATUS_SUBMITTED_BY_SEQUENCING_FACILITY
                    )
                    {
                        if (mode == 1)
                        {
                            CloneSequence cl = CloneSequence.getOneByIsolateTrackingId(istr.getId(), clone_sequence_status, clone_sequence_type);
                            istr.setCloneSequence(cl);
                        }
                        else if (mode > 1)
                        {
                            ArrayList clonesequences = CloneSequence.getAllByIsolateTrackingId(istr.getId(), clone_sequence_status, clone_sequence_type);
                            istr.setCloneSequences(clonesequences); 
                        }
                    }
                    else
                    {
                        ArrayList reads = Read.getReadByIsolateTrackingId( istr.getId() );
                        istr.setEndReads(reads);
                    }
                }
                //check if construct exists already
                Integer construct_key =  new Integer( rs.getInt("constructid"));
                if ( constructs_hash.containsKey( construct_key))
                {
                    Construct ct = (Construct)constructs_hash.get(construct_key);
                    ct.addIsolateTracking(istr);
                }    
                else //creat new construct
                {
                    /*
                    Construct new_construct = new Construct(construct_key.intValue(), -1, 
                                                      rs.getInt("refsequenceid"), rs.getInt("format"), 
                                                      rs.getInt("vectorid"), rs.getInt("linker3id"),
                                                      rs.getInt("linker5id") ); 
                     **/
                    Construct new_construct = new Construct(construct_key.intValue(), -1, 
                                                      rs.getInt("refsequenceid"), rs.getInt("format"), 
                                                      rs.getInt("cloningstrategyid") ); 
                
                    new_construct.addIsolateTracking(istr);
                    constructs.add(new_construct);
                    constructs_hash.put(construct_key,new_construct);
                }
             
            }
            return constructs;
        } catch (Exception sqlE)
        {
            throw new BecDatabaseException("Error occured while getting sample with id:\n"+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
    
    //------------------------- private ----------------------  
     
     
   public static void main(String args[]) 
     
    {
        try{
        BaseSequence bc =     getRefSequenceForAnalysis("NONE",
                           "GGG","NONE",
                            "aaaaaaactgtgtgtgtgggggttttcccccc", 0);
       // ArrayList master_container_ids = new ArrayList();
      //  master_container_ids.add(new Integer(16));  
      //  ArrayList co =Construct.getConstructsFromPlates(master_container_ids,"0","0",1);
        System.exit(0);
        }catch(Exception e){}
   }
}
