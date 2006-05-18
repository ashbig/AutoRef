//Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->
/*
 * BioGap.java
 *
 * Created on April 16, 2003, 11:58 AM
 */

package edu.harvard.med.hip.bec.coreobjects.sequence;

import  edu.harvard.med.hip.bec.util.*; 
import  edu.harvard.med.hip.bec.*;
import  edu.harvard.med.hip.bec.database.*;
import java.sql.*;
import java.util.*;
import edu.harvard.med.hip.bec.bioutil.*;

/**
 *
 * @author  htaycher
 */
public class Stretch
{
    
    public static final int   GAP_TYPE_LOW_QUALITY = 0;
    public static final int   GAP_TYPE_GAP = 1;
    public static final int   GAP_TYPE_CONTIG = 2;
    
    public static final int STATUS_DETERMINED = 1;
    public static final int STATUS_APPROVED_FOR_PRIMER_DESIGN = 2;
    /*
     *·	Id
·	* Sequence (pointer to analized sequence for contigs)
·	CDS start (clone sequence coordinates for low quality)
·	CDS stop
·	Direction (forward/reverse) 
·	Type (low quality / gap / contig) 
·	Trim start (we can introduce trimming for contigs as well) 
·	Trim end
·	Analysis Status (not analyzed / analyzed no discrepancies / analyzed yes discrepancies)
·	Group id (for connection of contigs);
*/
    private int             m_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    private int             m_sequence_id = -1;//Sequence (pointer to analized sequence for contigs)
    private AnalyzedScoredSequence m_sequence = null;
    private int             m_cds_start = ScoredElement.DEFAULT_COORDINATE; //(clone sequence coordinates for low quality)
    private int             m_cds_stop = ScoredElement.DEFAULT_COORDINATE;
    private int             m_sequence_start = ScoredElement.DEFAULT_COORDINATE; //(clone sequence coordinates for low quality)
    private int             m_sequence_stop = ScoredElement.DEFAULT_COORDINATE;
    private int             m_orientation = Constants.ORIENTATION_FORWARD;
    private int             m_analysis_status = -1;//(not analyzed / analyzed no discrepancies / analyzed yes discrepancies)
    private int             m_collection_id = -1;//Group id (for connection of contigs);
    private int             m_type = -1;
    private int             m_status = STATUS_DETERMINED;
   
    private String          i_refsequence_stretch = null; // for not contigs
    private String          i_html_description = null; 
    
    
    /** Creates a new instance of BioGap */
    public Stretch()
    {
    }
    public Stretch(int type, int status, int cds_start, int cds_stop, int seq_start, int seq_stop,int orientation)
    {
        m_cds_start = cds_start; //(clone sequence coordinates for low quality)
        m_cds_stop = cds_stop;
        m_sequence_stop = seq_stop;
        m_sequence_start = seq_start;
        m_orientation = orientation;
        m_type = type;
        m_status = status;
    }
     public int              getId (){ return m_id ;}
    public int             getSequenceId (){ return m_sequence_id  ;}//Sequence (pointer to analized sequence for contigs)
    public AnalyzedScoredSequence getSequence () throws Exception
    { 
        if ( m_sequence == null && m_sequence_id > 0)
            m_sequence = new AnalyzedScoredSequence(m_sequence_id);
        return m_sequence  ;
    }
    public int             getCdsStart (){ return m_cds_start  ;} //(clone sequence coordinates for low quality)
    public int             getCdsStop (){ return m_cds_stop  ;}
    public int             getOrientation (){ return m_orientation ;}
    public int             getAnalysisStatus (){ return m_analysis_status  ;}//(not analyzed / analyzed no discrepancies / analyzed yes discrepancies)
    public int              getCollectionId (){ return m_collection_id  ;}//Group id (for connection of contigs);
    public int              getType (){ return m_type  ;}
    public int              getStatus(){ return m_status;}
    public String           getRefSequenceStretch(){ return i_refsequence_stretch ;}
    public String           getHTMLDescription(){ return i_html_description;}
    public int              getSequenceStart(){ return m_sequence_start ;} //(clone sequence coordinates for low quality)
    public int              getSequenceStop(){ return m_sequence_stop ;}
   
    
   public void              setId (int v){   m_id = v;}
    public void             setSequenceId (int v){   m_sequence_id  = v;}//Sequence (int vpointer to analized sequence for contigs)
    public void              setSequence (AnalyzedScoredSequence v){   m_sequence  = v;}
    public void             setCdsStart (int v){   m_cds_start  = v;} //(int vclone sequence coordinates for low quality)
    public void             setCdsStop (int v){   m_cds_stop  = v;}
    public void             setOrientation (int v){   m_orientation = v;}
    public void             setAnalysisStatus (int v){   m_analysis_status  = v;}//(int vnot analyzed / analyzed no discrepancies / analyzed yes discrepancies)
    public void              setCollectionId (int v){   m_collection_id  = v;}//Group id (int vfor connection of contigs)= v;
    public void              setType (int v){   m_type  = v;}
    public void              setStatus(int v){ m_status = v;}
    public void              setRefSequenceStretch(String s) { i_refsequence_stretch = s; }
    public void           setSequenceStart(int v){  m_sequence_start = v;} //(clone sequence coordinates for low quality)
    public void              setSequenceStop(int v){  m_sequence_stop = v;}
 
    
    public void             setHTMLDescription(String s){ i_html_description = s;}
    
    
    public static   String    getStretchTypeAsString( int  stretch_type)
    {
        switch (stretch_type)
        {
            case GAP_TYPE_LOW_QUALITY : return "LQR";
            case GAP_TYPE_GAP: return "Gap";
            case GAP_TYPE_CONTIG: return "Contig";
            default: return "Not known";
        }
    }
    
    public String toString()
    {
        String result = "Type " +" "+ getStretchTypeAsString(m_type)
        +" Cds Start "+m_cds_start +" Cds Stop "+ m_cds_stop ;
        if ( m_sequence_id != -1 ) 
        {
           
            result +=" Sequence Id: "+ m_sequence_id;
        }
         if ( m_type == GAP_TYPE_LOW_QUALITY)
        {
            result +=" Contig sequence start "+ m_sequence_start ;
            result +=" Contig sequence stop "+m_sequence_stop ;
        }
        return  result;
        
     }
    
    
     public void insert(Connection conn) throws BecDatabaseException
    {
        Statement stmt = null;
        String sql = null;
         try
        {
            if (m_id == BecIDGenerator.BEC_OBJECT_ID_NOTSET)
                     m_id = BecIDGenerator.getID("stretchid");
               if ( m_sequence != null)
            {
                m_sequence.insert(conn);
                m_sequence_id = m_sequence.getId();
            }
            sql = "INSERT INTO STRETCH  (STRETCHID ,SEQUENCEID ,CDSSTART  ,CDSEND,SEQUENCESTART,SEQUENCESTOP, ORIENTATION ,"
            +" STATUS ,TYPE ,ANALYSISSTATUS,COLLECTIONID) VALUES("
            +m_id+ ","  + m_sequence_id + "," +m_cds_start + ", " + m_cds_stop+"," +
            m_sequence_start+"," + m_sequence_stop +"," +
            m_orientation +","+ m_status + ","+ m_type +","+m_analysis_status +","+m_collection_id+")";
         
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (Exception sqlE)
        {

            throw new BecDatabaseException(sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeStatement(stmt);
        }
     }
     
     
     
     public static ArrayList sortByPosition(ArrayList stretches)
    {
        for (int i=0; i< stretches.size();i++)
            Collections.sort(stretches, new Comparator()
            {
                public int compare(Object o1, Object o2)
                {
                    return ((Stretch) o1).getCdsStart() - ((Stretch) o2).getCdsStart();
                }
                public boolean equals(java.lang.Object obj)    {      return false;  }
            } );
            return stretches;
    }
     public static ArrayList getByStretchCollectionId( int collection_id, boolean isSequenceIncluded) throws Exception
     {
         String sql = " select  STRETCHID  ,SEQUENCEID  ,CDSSTART  ,CDSEND , SEQUENCESTART,SEQUENCESTOP, STATUS "
         +"  ,ORIENTATION  ,TYPE  ,ANALYSISSTATUS  ,COLLECTIONID   from  STRETCH "
         +"  where COLLECTIONID ="+collection_id +" order by CDSSTART";
         return getByRule( sql, isSequenceIncluded);
     }
     
      public static ArrayList getBySequenceIdType( int sequenceid, int stretch_type, boolean isSequenceIncluded) throws Exception
     {
         String sql = " select  STRETCHID  ,SEQUENCEID  ,CDSSTART  ,CDSEND , SEQUENCESTART,SEQUENCESTOP,  STATUS "
         +"  ,ORIENTATION  ,TYPE  ,ANALYSISSTATUS  ,COLLECTIONID   from  STRETCH "
         +"  where sequenceid ="+sequenceid +" and type = " +stretch_type  +" order by CDSSTART";
         return getByRule( sql, isSequenceIncluded);
     }
     public static Stretch getById( int id) throws Exception
     {
         String sql = " select  STRETCHID  ,SEQUENCEID  ,CDSSTART  ,CDSEND , SEQUENCESTART,SEQUENCESTOP, STATUS "
         +"  ,ORIENTATION  ,TYPE  ,ANALYSISSTATUS  ,COLLECTIONID   from  STRETCH "
         +"  where StretchID ="+id +" order by CDSSTART";
         ArrayList stretches = getByRule( sql, true);
         if ( stretches == null || stretches.size() < 1) return null;
         else return (Stretch)stretches.get(0);
     }
      public static ArrayList getByStretchCollectionId( int collection_id) throws Exception
     {
         return getByStretchCollectionId( collection_id, true);
     }
     
     
     public int  refsequenceCoveredLength()
    {
        int length = 0;
         if (m_cds_start > 0 && m_cds_stop > 0)//part of gene and (?) 3 ptime covered
        {
            length =  m_cds_stop - m_cds_start;
        }
        else if (m_cds_start < 0 && m_cds_stop > 0)//linker5 coverage
        {
            length = m_cds_stop - m_cds_start ;
        }
       
       return length;
    }
        public static void updateContigAnalysisStatus(int id,  int status, Connection conn)    throws BecDatabaseException
        {
            String sql = "update stretch  set ANALYSISSTATUS="+status+      " where stretchid="+id;

            DatabaseTransaction.executeUpdate(sql,conn);
         }
      //--------------------------------
     private static ArrayList getByRule(String sql, boolean isSequenceIncluded)throws BecDatabaseException
    {
        ArrayList res = new ArrayList();
        ResultSet rs = null;
         Stretch sc = null;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while(rs.next())
            {
                sc = new  Stretch();
                sc.setId ( rs.getInt("STRETCHID"));
                sc.setType ( rs.getInt("TYPE"));
                sc.setSequenceId (rs.getInt("SEQUENCEID"));;//Sequence (int vpointer to analized sequence for contigs)
                if ( isSequenceIncluded && sc.getSequenceId () > 0)
                    sc.setSequence ( new AnalyzedScoredSequence(sc.getSequenceId ()) ) ;
                sc.setCdsStart (rs.getInt("CDSSTART"));//(int vclone sequence coordinates for low quality)
                sc.setCdsStop (rs.getInt("CDSEND"));
                sc.setOrientation (rs.getInt("ORIENTATION"));
                sc.setAnalysisStatus (rs.getInt("ANALYSISSTATUS"));//(int vnot analyzed / analyzed no discrepancies / analyzed yes discrepancies)
                sc.setCollectionId (rs.getInt("COLLECTIONID"));//Group id (int vfor connection of contigs)= v;
                sc.setStatus( rs.getInt("STATUS"));
                sc.setSequenceStart(rs.getInt("SEQUENCESTART"));
                sc.setSequenceStop(rs.getInt("SEQUENCESTOP"));
                res.add(sc);
            }
            return res;
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException("Error occured while extracting stretch collection"+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }


    }
  
     
}
