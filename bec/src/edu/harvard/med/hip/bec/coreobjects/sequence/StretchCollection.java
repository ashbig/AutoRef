/*
 * GapCollection.java
 *
 * Created on March 26, 2004, 3:56 PM
 */

package edu.harvard.med.hip.bec.coreobjects.sequence;
import java.util.*;
import java.io.*;
import java.sql.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.ui_objects.*;
import edu.harvard.med.hip.bec.modules.*;
import edu.harvard.med.hip.bec.*;
/**
 *
 * @author  HTaycher
 */
public class StretchCollection
{
    /*
     *i.	id
ii.	Refsequence id
iii.	Isolate tracking id 
iv.	date
v.	Clone sequence id for low quality gaps on assembled sequence
vi.	Type of definition (coverage low quality / no coverage )

     */
    public static final int TYPE_COLLECTION_OF_LQR = 0;
    public static final int TYPE_COLLECTION_OF_GAPS_AND_CONTIGS = 1;
    
    private int         m_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    private int         m_type = -1;
    private int         m_refsequence_id = -1;
    private int         m_isolatetracking_id = -1;
    private int         m_clone_sequence_id = -1;
    private CloneSequence   m_clone_sequence = null;
    private int         m_clone_id = -1;
    private String      m_submission_date  = null;
    private ArrayList   m_stretches = null;
    /** Creates a new instance of GapCollection */
    public StretchCollection()
    {
    }
    
    
    public int         getId (){ return m_id  ; }
    public int         getType (){ return m_type  ; }
    public int         getRefSequenceId (){ return m_refsequence_id  ; }
    public int         getIsolatetrackingId (){ return m_isolatetracking_id  ; }
    public int         getCloneSequenceId (){ return m_clone_sequence_id  ; }
    public int         getCloneId (){ return m_clone_id  ; }
    public ArrayList    getStretches(){ return m_stretches;}
    
    public void         setId ( int v){ m_id  = v ; }
    public void         setType ( int v){ m_type  = v ; }
    public void         setRefSequenceId ( int v){ m_refsequence_id  = v ; }
    public void         setIsolatetrackingId ( int v){ m_isolatetracking_id  = v ; }
    public void         setCloneSequenceId ( int v){ m_clone_sequence_id  = v ; }
    public void         setCloneId ( int v){ m_clone_id  = v ; }
    public void         setStretches(ArrayList v){  m_stretches = v;}
    public void         addStretche(Stretch g){ if (m_stretches == null) m_stretches = new ArrayList(); m_stretches.add(g);}
    
     public void insert(Connection conn) throws BecDatabaseException
    {
        if ( m_stretches == null || m_stretches.size() < 1) return;
        Statement stmt = null;Stretch stretch = null;
        String sql = null;
         try
        {
            if (m_id == BecIDGenerator.BEC_OBJECT_ID_NOTSET)
                     m_id = BecIDGenerator.getID("stretchid");
            if ( m_clone_sequence_id == -1)
                sql = "INSERT INTO STRETCH_COLLECTION ( COLLECTIONID  ,REFSEQUENCEID  , "
            +" SUBMISSIONDATE  ,TYPE  ,ISOLATETRACKINGID ) VALUES(" +m_id+ ","  +
            m_refsequence_id + ",sysdate,"  +m_type+ ","  +m_isolatetracking_id+ ")" ;
         
            else
                sql = "INSERT INTO STRETCH_COLLECTION ( COLLECTIONID  ,REFSEQUENCEID  ,CLONESEQUENCEID "
            +"  ,SUBMISSIONDATE  ,TYPE  ,ISOLATETRACKINGID ) VALUES(" +m_id+ ","  +
            m_refsequence_id + ","  +m_clone_sequence_id+ ",sysdate,"  +m_type+ ","  +m_isolatetracking_id+ ")" ;
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            for (int count = 0; count < m_stretches.size(); count++)
            {
                stretch = (Stretch) m_stretches.get(count);
                stretch.setCollectionId(m_id);
                stretch.insert(conn);
            }
             
        } catch (Exception sqlE)
        {

            throw new BecDatabaseException(sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeStatement(stmt);
        }
        
     }
     
     public static StretchCollection getByCloneId(int clone_id, boolean isSequenceIncluded)throws Exception
     {
          String sql = " select  COLLECTIONID  ,REFSEQUENCEID   ,CLONESEQUENCEID   ,SUBMISSIONDATE   ,TYPE "
        +" ,ISOLATETRACKINGID   from STRETCH_COLLECTION   where ISOLATETRACKINGID IN (SELECT ISOLATETRACKINGID "
        +" FROM FLEXINFO WHERE FLEXCLONEID =" +clone_id+    " ) order by COLLECTIONID desc";
         ArrayList col = getByRule( sql,  isSequenceIncluded);
         if (col != null && col.size() > 0 ) return (StretchCollection) col.get(0);
         return null;
     }
     public static StretchCollection     getByCloneId(int cloneid)throws Exception
     {
         return getByCloneId(cloneid, true);
     }
      
     public static ArrayList     getByCloneSequenceId(int clone_sequence_id, boolean isSequenceIncluded)
     {
         return null;
     }
    
     public static StretchCollection getByIsolateTrackingId(int is_id, boolean isSequenceIncluded)throws Exception
     {
          String sql = " select  COLLECTIONID  ,REFSEQUENCEID   ,CLONESEQUENCEID   ,SUBMISSIONDATE   ,TYPE "
        +" ,ISOLATETRACKINGID   from STRETCH_COLLECTION   where ISOLATETRACKINGID ="+is_id +" order by COLLECTIONID desc";
         ArrayList col = getByRule( sql,  isSequenceIncluded);
         if (col != null && col.size() > 0 ) return (StretchCollection) col.get(0);
         return null;
     }
     public static StretchCollection     getById(int collection_id, boolean isSequenceIncluded) throws Exception
     {
         String sql = " select  COLLECTIONID  ,REFSEQUENCEID   ,CLONESEQUENCEID   ,SUBMISSIONDATE   ,TYPE "
        +" ,ISOLATETRACKINGID   from STRETCH_COLLECTION   where COLLECTIONID ="+collection_id ;
         ArrayList col = getByRule( sql,  isSequenceIncluded);
         if (col != null && col.size() > 0 ) return (StretchCollection) col.get(0);
         return null;
     }
      public static StretchCollection getByIsolateTrackingId(int is_id)throws Exception
     {
        return getByIsolateTrackingId( is_id, true);
     }
     public static StretchCollection     getById(int collection_id) throws Exception
     {
          return getById(collection_id, true);
     }
     
     public  ArrayList getSequenceCollection(String refsequence_text,int m_min_distance_between_stretches, boolean isApprovedOnly)
     {
         ArrayList subsequences = new ArrayList();
         int subsequence_start = 0; int subsequence_end = 0;
         Stretch stretch = null;Stretch stretch_next = null;
         String subsequence = null;
         int count_stretches = 0;
         ArrayList stretches_to_process = getStretchesToProcess(isApprovedOnly);
         while(count_stretches < stretches_to_process.size())
         {
             stretch = (Stretch) stretches_to_process.get(count_stretches);
             stretch_next = stretch;
             for (; count_stretches < stretches_to_process.size(); count_stretches++)
             {
                 if (stretch_next.getCdsStop() - stretch.getCdsStop()  <= m_min_distance_between_stretches)
                 {
                     stretch_next = (Stretch) stretches_to_process.get(count_stretches);
                 }
                 else
                     break;
             }
             subsequence = refsequence_text.substring(stretch.getCdsStart(),           stretch_next.getCdsStop() );
             subsequences.add(subsequence);
         }
       
         return subsequences;
     }
 
     //--------------------------------
     
     private ArrayList  getStretchesToProcess(boolean isApprovedOnly)
     {
         ArrayList stretches_to_process = new ArrayList();
         if ( !isApprovedOnly) return m_stretches;
         for (int count = 0; count < m_stretches.size(); count ++)
         {
             if (( (Stretch) m_stretches.get(count)).getStatus() == Stretch.STATUS_APPROVED_FOR_PRIMER_DESIGN)
                 stretches_to_process.add(m_stretches.get(count));
         }
         return stretches_to_process;
     }
     private static ArrayList getByRule(String sql, boolean isSequenceIncluded)throws BecDatabaseException
    {
        ArrayList res = new ArrayList();
        ResultSet rs = null;
         StretchCollection sc = null;
         ArrayList stretches = null;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);

            while(rs.next())
            {
                sc = new  StretchCollection();
                sc.setId ( rs.getInt("COLLECTIONID"));
                sc.setType ( rs.getInt("TYPE"));
                sc.setRefSequenceId ( rs.getInt("REFSEQUENCEID") ); 
                sc.setIsolatetrackingId ( rs.getInt("ISOLATETRACKINGID"));
                sc.setCloneSequenceId ( rs.getInt("CLONESEQUENCEID"));
               // sc.setCloneId ( rs.getInt("CLONEID"));
                stretches= Stretch.getByStretchCollectionId(sc.getId()  ,  isSequenceIncluded);
                sc.setStretches(stretches);
               
                res.add(sc);
            }
            return res;
        } catch (Exception sqlE)
        {
            throw new BecDatabaseException("Error occured while extracting stretch collection"+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }


    }
     
      //for sample report: converts end reads list int o uireads
    
    public static ArrayList  createListOfUIContigs( StretchCollection strcol, int id_type) throws Exception
    {
        ArrayList uireads = new ArrayList();
        UIRead uiread = null;Stretch stretch = null;
        String  needle_file_name = null;
        File needle_file = null;    DiscrepancyFinder nv = new DiscrepancyFinder();
       
        if (strcol == null) return null;
        int number_of_gaps = 1; int number_of_contigs = 1;
        int number_of_lqs=1;
        ArrayList stretch_collection = Stretch.sortByPosition(strcol.getStretches() );
         for (int count = 0; count < stretch_collection.size(); count++)
        {
            stretch = (Stretch) stretch_collection.get(count);
            uiread = new UIRead();
            uiread.setId (stretch.getId());
            uiread.setType (stretch.getType());
            uiread.setTrimStart ( stretch.getCdsStart() );
            uiread.setTrimStop (stretch.getCdsStop() );
            //determine needle file exists
            switch (stretch.getType())
            {
                case Stretch.GAP_TYPE_CONTIG://stretch
                {
                    uiread.setSequenceId (stretch.getSequence().getId());

                    needle_file_name = nv.getOutputDirectory()+"/needle"+uiread.getSequenceId ()+
                                            "_"+strcol.getRefSequenceId()+".out";
                    needle_file = new File(needle_file_name);
                    if ( needle_file.exists() )
                        uiread.setIsAlignmentExists (true);
                    if ( stretch.getSequence().getDiscrepancies() != null && stretch.getSequence().getDiscrepancies().size() > 0)
                    {
                        uiread.setIsDiscrepancies ( true);
                    }
                     uiread.setName("Contig "+number_of_contigs);//name
                     number_of_contigs ++;
                    break;   
                }
                case Stretch.GAP_TYPE_GAP:
                {
                    uiread.setName("Gap "+number_of_gaps);//name
                    number_of_gaps ++;
                    uiread.setIsDiscrepancies ( false);
                    break;
                }
                case Stretch.GAP_TYPE_LOW_QUALITY:
                {
                    uiread.setName("LQS "+number_of_lqs);//name
                     number_of_lqs++;
                    uiread.setIsDiscrepancies ( false);
                    break;
                }
            }
            uireads.add(uiread);
           // discrepancies.addAll( stretch.getSequence().getDiscrepancies() );
        }
         /*
        discrepancies = DiscrepancyDescription.getDiscrepancyNoDuplicates(discrepancies);
   
         String discrepancy_report_html = Mutation.HTMLReport( discrepancies, Mutation.LINKER_5P, true);
         discrepancy_report_html += Mutation.HTMLReport( discrepancies, Mutation.RNA, true);
         discrepancy_report_html += Mutation.HTMLReport( discrepancies, Mutation.LINKER_3P, true);
         if (discrepancy_report_html.equals(""))
            discrepancy_report_for_endread="<tr><td colspan=3><strong>No discrepancies</strong></td></tr>";
         else
             discrepancy_report_for_endread = discrepancy_report_html;
     **/
         return uireads;
    }
   
}
