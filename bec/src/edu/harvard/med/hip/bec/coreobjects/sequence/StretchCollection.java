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
import edu.harvard.med.hip.bec.bioutil.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.ui_objects.*;
import edu.harvard.med.hip.bec.modules.*;
import edu.harvard.med.hip.bec.*;
import edu.harvard.med.hip.bec.coreobjects.feature.*;
import edu.harvard.med.hip.bec.action_runners.*;
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
    private int         m_spec_id = -1;
    private CloneSequence   m_clone_sequence = null;
    private int         m_clone_id = -1;
    private String      m_submission_date  = null;
    private ArrayList   m_stretches = null;
    
    
    private String      i_html_description = null;
    /** Creates a new instance of GapCollection */
    public StretchCollection() {}
    
    public int         getId (){ return m_id  ; }
    public int         getType (){ return m_type  ; }
    public int         getRefSequenceId (){ return m_refsequence_id  ; }
    public int         getIsolatetrackingId (){ return m_isolatetracking_id  ; }
    public int         getCloneSequenceId (){ return m_clone_sequence_id  ; }
    public int         getCloneId (){ return m_clone_id  ; }
    public int         getSpecId (){ return m_spec_id ; }
    public ArrayList    getStretches(){ return m_stretches;}
    public String       getHTMLDescription(){ return i_html_description; }
    
    public void         setId ( int v){ m_id  = v ; }
    public void         setType ( int v){ m_type  = v ; }
    public void         setRefSequenceId ( int v){ m_refsequence_id  = v ; }
    public void         setIsolatetrackingId ( int v){ m_isolatetracking_id  = v ; }
    public void         setCloneSequenceId ( int v){ m_clone_sequence_id  = v ; }
    public void         setCloneId ( int v){ m_clone_id  = v ; }
    public void         setSpecId (int v){  m_spec_id = v; }
    public void         setStretches(ArrayList v){  m_stretches = v;}
    public void         addStretch(Stretch g){ if (m_stretches == null) m_stretches = new ArrayList(); m_stretches.add(g);}
    public void         addStretches(ArrayList ar){ if (m_stretches == null) m_stretches = new ArrayList(); m_stretches.addAll(ar);}
    
    
    public void         setHTMLDescription(String s){ i_html_description = s;}
    
    
    
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
                sql = "INSERT INTO STRETCH_COLLECTION ( COLLECTIONID  ,REFSEQUENCEID  , CONFIGID,  "
                +" SUBMISSIONDATE  ,TYPE  ,ISOLATETRACKINGID ) VALUES(" +m_id+ ","  +
                m_refsequence_id + "," + m_spec_id + ",sysdate,"  +m_type+ ","  +m_isolatetracking_id+ ")" ;
         
            else
                sql = "INSERT INTO STRETCH_COLLECTION ( COLLECTIONID  ,REFSEQUENCEID  , CONFIGID, CLONESEQUENCEID "
            +"  ,SUBMISSIONDATE  ,TYPE  ,ISOLATETRACKINGID ) VALUES(" +m_id+ ","  +
            m_refsequence_id +  "," + m_spec_id + ","  +m_clone_sequence_id+ ",sysdate,"  +m_type+ ","  +m_isolatetracking_id+ ")" ;
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
     
     public static Object getByCloneId(int clone_id, boolean isSequenceIncluded, boolean isFirstLastOnly)throws Exception
     {
         StretchCollection strcol = null;
          String sql = " select  COLLECTIONID  ,REFSEQUENCEID   , CONFIGID, CLONESEQUENCEID   ,SUBMISSIONDATE   ,TYPE "
        +" ,ISOLATETRACKINGID   from STRETCH_COLLECTION   where ISOLATETRACKINGID IN (SELECT ISOLATETRACKINGID "
        +" FROM FLEXINFO WHERE FLEXCLONEID =" +clone_id+    " ) order by COLLECTIONID desc";
         ArrayList col = getByRule( sql,  isSequenceIncluded);
         if (col != null && col.size() > 0 ) 
         {
             if ( isFirstLastOnly)
             {
                 strcol = (StretchCollection) col.get(0);
                 strcol.setCloneId(clone_id);
                 return strcol;
             }
             else
             {
                 ArrayList result = new ArrayList();
                 for (int index = 0; index < col.size(); index ++)
                 {
                     strcol = (StretchCollection) col.get(index);
                     strcol.setCloneId(clone_id);
                     result.add(strcol);
                 }
                 return result;
             
             }
         }
         return null;
     }
     public static StretchCollection     getLastByCloneId(int cloneid)throws Exception
     {
         return (StretchCollection) getByCloneId(cloneid, true, true);
     }
      public static ArrayList     getAllByCloneId(int cloneid)throws Exception
     {
         return (ArrayList) getByCloneId(cloneid, true, false);
     }
      
      
      //-------------------------------
     public static StretchCollection     getByCloneSequenceId(int clone_sequence_id, boolean isSequenceIncluded)throws Exception
     {
           String sql = " select  COLLECTIONID  ,REFSEQUENCEID   , CONFIGID, CLONESEQUENCEID   ,SUBMISSIONDATE   ,TYPE "
        +" ,ISOLATETRACKINGID   from STRETCH_COLLECTION   where clonesequenceid ="+clone_sequence_id +" order by COLLECTIONID desc";
         ArrayList col = getByRule( sql,  isSequenceIncluded);
         if (col != null && col.size() > 0 ) return (StretchCollection) col.get(0);
         return null;
     }
    
     
     public static int     getCloneIdByStretchCollectionId(int id)throws Exception
     {
        ResultSet rs = null; int cloneid = 0;
        String sql = "SELECT FLEXCLONEID from flexinfo where isolatetrackingid in (select isolatetrackingid from stretch_collection where collectionid="+id+")";
        try
        {
            rs = DatabaseTransaction.getInstance().executeQuery(sql);
            if (rs.next())
            {
               cloneid = rs.getInt("FLEXCLONEID");
            }
            return cloneid;
        } catch (Exception sqlE)
        {
            throw new BecDatabaseException("Error occured while extracting cloneid for stretch collection id "+id+"\nSQL: "+sql);
        } finally        {            if ( rs != null)DatabaseTransaction.closeResultSet(rs);        }


     }
    
     public static StretchCollection     getByCloneSequenceIdAndSpecId(int clone_sequence_id, int spec_id)throws Exception
     {
           String sql = " select  COLLECTIONID  ,REFSEQUENCEID   , CONFIGID, CLONESEQUENCEID   ,SUBMISSIONDATE   ,TYPE "
        +" ,ISOLATETRACKINGID   from STRETCH_COLLECTION   where clonesequenceid ="+clone_sequence_id +"and  CONFIGID= "+spec_id+" order by COLLECTIONID desc";
         ArrayList col = getByRule( sql,  false);
         if (col != null && col.size() > 0 ) return (StretchCollection) col.get(0);
         return null;
     }
     public static StretchCollection getByIsolateTrackingId(int is_id, boolean isSequenceIncluded)throws Exception
     {
          String sql = " select  COLLECTIONID  ,REFSEQUENCEID   ,CONFIGID, CLONESEQUENCEID   ,SUBMISSIONDATE   ,TYPE "
        +" ,ISOLATETRACKINGID   from STRETCH_COLLECTION   where ISOLATETRACKINGID ="+is_id +" order by COLLECTIONID desc";
         ArrayList col = getByRule( sql,  isSequenceIncluded);
         if (col != null && col.size() > 0 ) return (StretchCollection) col.get(0);
         return null;
     }
     public static StretchCollection     getById(int collection_id, boolean isSequenceIncluded) throws Exception
     {
         String sql = " select  COLLECTIONID  ,REFSEQUENCEID   ,CONFIGID, CLONESEQUENCEID   ,SUBMISSIONDATE   ,TYPE "
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
     
     //get collection of BaseSequence that are subsequences of refsequence
     // used for primer design
     // sequence id - cds start for the refsequence subsequence
     public  ArrayList getStretchBoundaries(int cloneid, 
                int cds_length, 
                int type_of_lqr_coverage , 
                boolean isStartFromFirstDiscrepancy)throws Exception
     {
      
         Stretch stretch = null;
         ArrayList boundaries = new ArrayList();
          ArrayList discrepancies = null;
         int[] element = null; 
         int stretch_start = 0;          int stretch_end =0;
         boolean isInclude = true;
         AnalyzedScoredSequence sequence = null;
         for (  int count_stretches = 0; count_stretches < m_stretches.size(); count_stretches++)
         {
             
             stretch = (Stretch) m_stretches.get(count_stretches);
             if ( stretch.getCdsStop() < 0 || stretch.getCdsStart() > cds_length  ) continue; // for gaps in 5' linker
             // only gaps from gaps & lqr collection
             if ( stretch.getType() == Stretch.GAP_TYPE_GAP ) 
             {
                 boundaries.add( constructBoundaryElementFromStretch( stretch,  cds_length) );
             }
             //lqr collection
             else if ( stretch.getType() == Stretch.GAP_TYPE_LOW_QUALITY )
             {
                 switch ( type_of_lqr_coverage)
                 {
                     case PrimerDesignerRunner.LQR_COVERAGE_TYPE_NONE : break;
                     case PrimerDesignerRunner.LQR_COVERAGE_TYPE_ANY_LQR:
                     {
                         boundaries.add( constructBoundaryElementFromStretch( stretch,  cds_length) );
                         break;
                     }
                    case PrimerDesignerRunner.LQR_COVERAGE_TYPE_LQR_WITH_DISCREPANCY :
                     {
                         sequence = getSequenceForStretch( cloneid, sequence, stretch );
                         discrepancies = getDiscrepanciesInRegion(stretch, sequence , cds_length, cloneid);
                         if ( discrepancies != null && discrepancies.size() > 0)
                              boundaries.add( constructBoundaryElementFromStretch( stretch,  cds_length) );
                         break;
                         
                     }
                    case PrimerDesignerRunner.LQR_COVERAGE_TYPE_LQR_DISCREPANCY_REGIONS:
                     {
                         sequence = getSequenceForStretch( cloneid, sequence, stretch );
                         discrepancies = getDiscrepanciesInRegion(stretch, sequence , cds_length, cloneid);
                         if ( discrepancies != null && discrepancies.size() > 0)
                         {
                            int first_discr_start = ((Mutation)discrepancies.get(0)).getPosition(); 
                            int last_discr_end = ((Mutation)discrepancies.get( discrepancies.size() -1 )).getPosition() +
                                ((Mutation)discrepancies.get( discrepancies.size() -1 )).getLength(); ; 
                            boundaries.add( constructBoundaryElementFromStretch( first_discr_start,  last_discr_end, cds_length) );
                         }
                         break;
                     }
                 }
             }
         }
         return boundaries;
    }
 
         
         
     //function prepares str collection for LQR display
     public static  void prepareStretchCollectionForDisplay
                        (StretchCollection lqr_for_clone,
                        CloneSequence clone_sequence, int cds_length)
     {
         if (lqr_for_clone == null ) return;
         Stretch lqr = null;
         StringBuffer stretch_collection_html_description = new StringBuffer();
         StringBuffer stretch_html_description = null;
         String header = " <th>Name </th><th>Cds Region</th><th>Sequence Region</th>"
         +" <th>Sequence</th><th>Discrepancy Report </th>";
         ArrayList discrepancies = null;
         String discrepancy_report_button_text = null;
         for (int count = 0; count < lqr_for_clone.getStretches().size(); count++)
         {
             lqr = (Stretch) lqr_for_clone.getStretches().get(count);
             ScoredSequence scored_sequence =  clone_sequence.getSubSequence( lqr.getSequenceStart() - 1, lqr.getSequenceStop() );
             String stretch_html_fomated_sequence = scored_sequence.toHTMLStringNoRuler(40) ;
             String stretch_name = lqr.getStretchTypeAsString(lqr.getType()) + " "+ (count + 1);
             StringBuffer html_description = new StringBuffer();
             html_description.append("<TD>"+stretch_name +"</TD>");
             
             html_description.append("<TD>"+ (  lqr.getCdsStart() )
                    +" - "+ ( lqr.getCdsStop()  )  +"</TD>");
              html_description.append("<TD>"+lqr.getSequenceStart() +" - "+ lqr.getSequenceStop() +"</TD>");
              html_description.append("<TD> <PRE> <font size='-2'>"+ stretch_html_fomated_sequence +"</font></pre></TD>");
             
              discrepancies = clone_sequence.getDiscrepanciesInRegion( lqr.getSequenceStart() , lqr.getSequenceStop(), clone_sequence.getCdsStart(), cds_length);
              if ( discrepancies == null || discrepancies.size() == 0 )
              {
                  discrepancy_report_button_text = "&nbsp";
              }
              else
              {
                  String discr_ids = "";
                  
                  for(int discr_count = 0; discr_count < discrepancies.size(); discr_count++)
                  {
                      discr_ids += ((Mutation)discrepancies.get(discr_count)).getId() + "_";
                  }
                  discrepancy_report_button_text = "<input type=BUTTON value=\"Discrepancy Report\"  onClick=\"window.open('"+edu.harvard.med.hip.utility.ApplicationHostDeclaration.JSP_REDIRECTION +"Seq_GetItem.do?forwardName="+Constants.ANALYZEDSEQUENCE_DISCREPANCY_REPORT_DEFINITION_INT +"&amp;"
                  +"DISCRIDS="+discr_ids+"','D"+clone_sequence.getId() +"','width=500,height=400,menubar=no,location=no,scrollbars=yes,resizable=yes');return false;\">";
              }
                  
              html_description.append("<TD>"+discrepancy_report_button_text +"</TD>");
             
             stretch_collection_html_description.append( "<TR>"  + html_description.toString() + "</TR>" );
         }
         
         lqr_for_clone.setHTMLDescription(header + stretch_collection_html_description.toString() );
     }
                           
    
     //--------------------------------
     //get sequence for stretch 
     private AnalyzedScoredSequence  getSequenceForStretch( int cloneid,
                    AnalyzedScoredSequence sequence, Stretch stretch ) throws Exception
     {
         
         try
         {
               if (m_type == StretchCollection.TYPE_COLLECTION_OF_LQR )
               {
                   if ( sequence != null)                return sequence;
                   sequence = (AnalyzedScoredSequence) CloneSequence.getOneByCloneId(cloneid);
               }
               else if ( m_type == StretchCollection.TYPE_COLLECTION_OF_GAPS_AND_CONTIGS )
               {
                   if ( sequence != null && ( sequence.getId() == stretch.getSequenceId())) return sequence;
                   sequence = new AnalyzedScoredSequence( stretch.getSequenceId() );
               }
               return sequence;
         }
         catch(Exception e){ throw new BecDatabaseException ("Cannot get sequence for LQR. Clone Id: "+ cloneid);}
    }
       
         
    private   ArrayList getDiscrepanciesInRegion(Stretch stretch, 
                AnalyzedScoredSequence sequence , int cds_length, int cloneid )
                throws Exception
     {
         ArrayList discrepancies = null;
                       
         try
         {
               discrepancies = Mutation.getDiscrepanciesBySequenceType(sequence.getDiscrepancies() , Mutation.RNA);
               discrepancies = AnalyzedScoredSequence.getRNADiscrepanciesInRegion( discrepancies,stretch.getSequenceStart(), stretch.getSequenceStop() );
        
               return discrepancies;
         }
         catch(Exception e){ throw new BecDatabaseException ("Cannot get discrepancies for LQR. Clone Id: "+ cloneid + " Stretch id: " + stretch.getId() );}
    }
       
                       
         
    private int[] constructBoundaryElementFromStretch(Stretch stretch, int cds_length)
    {
        return constructBoundaryElementFromStretch(stretch.getCdsStart(), stretch.getCdsStop(),  cds_length);
        
    }
    private int[] constructBoundaryElementFromStretch(int start, int end, int cds_length)
    {
         int[] element = new int[2];
         element[0] =  (start < 0 ) ? 0 :start;
         element[1] = ( end > cds_length ) ? cds_length  : end ;
         return element;
    }    
    private ArrayList getBySequenceIdType(int  sequence_id , int stretch_type ) 
    {
        ArrayList stretches = new ArrayList();
        Stretch stretch = null;
        for (int count = 0; count < m_stretches.size(); count++)
        {
            stretch = (Stretch) m_stretches.get(count);
            if ( stretch.getSequenceId() == sequence_id && stretch.getType() == stretch_type)
            {
                stretches.add(stretch);
            }
        }
        return stretches;
    }
    
     private static ArrayList getByRule(String sql, boolean isSequenceIncluded)throws BecDatabaseException
    {
        ArrayList res = new ArrayList();
        ResultSet rs = null;ResultSet rs1 = null;
         StretchCollection sc = null;
         ArrayList stretches = null;
         String sql1 = "SELECT FLEXCLONEID from flexinfo where isolatetrackingid = ";
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
                sc.setSpecId ( rs.getInt("CONFIGID"));
                stretches= Stretch.getByStretchCollectionId(sc.getId()  ,  isSequenceIncluded);
                sc.setStretches(stretches);
                rs1 = t.executeQuery(sql1 + sc.getIsolatetrackingId()); 
                if (rs1.next()) sc.setCloneId(rs1.getInt("FLEXCLONEID"));
                res.add(sc);
            }
            return res;
        } catch (Exception sqlE)
        {
            throw new BecDatabaseException("Error occured while extracting stretch collection"+sqlE+"\nSQL: "+sql);
        } finally
        {
            if ( rs1 != null) DatabaseTransaction.closeResultSet(rs1);
            if ( rs != null)DatabaseTransaction.closeResultSet(rs);
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
        ArrayList lqrs = new ArrayList();
        Hashtable lqr_sequence_ids = new Hashtable();
        ArrayList stretch_collection = Stretch.sortByPosition(strcol.getStretches() );
        
        for (int count = 0; count < stretch_collection.size(); count++)
        {
          stretch = (Stretch) stretch_collection.get(count);
          if (stretch.getType() == Stretch.GAP_TYPE_LOW_QUALITY && stretch.getSequenceId() != -1)
            lqr_sequence_ids.put(new Integer(stretch.getSequenceId()), "");
        }
         for (int count = 0; count < stretch_collection.size(); count++)
        {
            stretch = (Stretch) stretch_collection.get(count);
            if (stretch.getType() == Stretch.GAP_TYPE_LOW_QUALITY && stretch.getSequenceId() != -1)
                 continue;
            uiread = new UIRead();
            uiread.setId (stretch.getId());
            uiread.setType (stretch.getType());
            uiread.setTrimStart ( stretch.getCdsStart() );
            uiread.setTrimStop (stretch.getCdsStop() );
            if ( stretch.getType() == Stretch.GAP_TYPE_CONTIG && lqr_sequence_ids.get(new Integer(stretch.getSequenceId()))  != null)
                uiread.setIsProperty(true);
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
   
    
     public static void main(String [] args)
     {
         try
         {
             /*
     ArrayList items = Algorithms.splitString("  1986 1916 2349  ");
                     StretchCollection lqr_for_clone = null; 
                     int cloneid = 0;
                     CloneSequence clone_sequence = null;
                     //we trick system here writing html table 
                     Hashtable display_items = new Hashtable();
                     for (int index = 0; index < items.size();index++)
                     {
                            cloneid = Integer.parseInt( (String) items.get(index));
                            clone_sequence = CloneSequence.getOneByCloneId(cloneid);
                            if ( clone_sequence != null)
                            {
                                lqr_for_clone = StretchCollection.getByCloneSequenceId(clone_sequence.getId() , false);
                                if ( lqr_for_clone != null) 
                                    StretchCollection.prepareStretchCollectionForDisplay(lqr_for_clone,clone_sequence);
                         
                            }
                             display_items.put( items.get(index), lqr_for_clone);
 
                     }     
              
         String stic = "23880_23881_23882_23883_23884_23885_23886_23887_23888_23889_";
         stic = Algorithms.replaceChar( stic, '_', ',');
          if ( stic.charAt(stic.length() - 1) == ',') stic = stic.substring(0, stic.length() - 1);
          
          ArrayList discrepancies = Mutation.getByIds(stic);
                      
         String sa =  Mutation.toHTMLString(discrepancies);
         */
     StretchCollection   strcol = StretchCollection.getLastByCloneId(3558);
                            if ( strcol != null)
                            {
                                strcol.setStretches( StretchCollection.createListOfUIContigs(strcol,Constants.ITEM_TYPE_CLONEID));
                              }   }
         catch(Exception e){System.out.println(e.getMessage());}
     }
                           
}
