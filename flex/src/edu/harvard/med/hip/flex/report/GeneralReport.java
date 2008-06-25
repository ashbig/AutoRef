/*
 * GeneralReportRunner.java
 *
 * Created on May 20, 2008, 1:36 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.report;

import static edu.harvard.med.hip.flex.report.ReportConstants.REPORT_COLUMN;
import static edu.harvard.med.hip.flex.report.ReportConstants.ITEM_TYPE;
import static edu.harvard.med.hip.flex.report.ReportDefinition.REPORT_TYPE;

import edu.harvard.med.hip.flex.util.*;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.infoimport.coreobjectsforimport.*;

import java.sql.*;
import sun.jdbc.rowset.*;
import edu.harvard.med.hip.flex.database.*;


import java.util.*;
import java.io.*;
/**
 *
 * @author htaycher
 */
public class GeneralReport extends ReportDefinition
{
    private final REPORT_TYPE     REPORT_TYPE_FOR_THIS_REPORT = REPORT_TYPE.GENERAL;
    public static final REPORT_COLUMN[]     REPORT_COLUMNS = {
        REPORT_COLUMN.PLATE_LABEL  ,
        REPORT_COLUMN.USER_PLATE_LABEL  ,//if applicable
        REPORT_COLUMN.WELL_NUMBER  ,
        REPORT_COLUMN.WELL_NAME ,
        REPORT_COLUMN.SAMPLE_ID ,
        REPORT_COLUMN.SAMPLE_TYPE  ,
        REPORT_COLUMN.FLEXSEQUENCE_ID  ,
        REPORT_COLUMN.CDS_START  ,
        REPORT_COLUMN.CDS_STOP ,
        REPORT_COLUMN.CDSLENGTH ,
        REPORT_COLUMN.SPECIES ,
        REPORT_COLUMN.NAMES ,
        REPORT_COLUMN.CDS_TEXT ,
        REPORT_COLUMN.FS_SEQUENCE,
        REPORT_COLUMN.FLEXSEQUENCE_STATUS ,
        REPORT_COLUMN.CONSTRUCT_TYPE,
        REPORT_COLUMN.PROJECT_NAME ,
        REPORT_COLUMN.WORKFLOW_NAME ,
        REPORT_COLUMN.QUEUE_PROTOCOL_NAME,
        REPORT_COLUMN.CLONE_ID ,
        REPORT_COLUMN.CLONE_STATUS ,
        REPORT_COLUMN.CLONE_NAMES ,
        REPORT_COLUMN.CLONE_AUTHOR ,
        REPORT_COLUMN.CLONE_PUBLICATION ,
        REPORT_COLUMN.CLONE_SEQUENCE ,
        REPORT_COLUMN.CLONE_S_LINKER_5P ,
        REPORT_COLUMN.CLONE_S_LINKER_3P ,
        REPORT_COLUMN.CLONE_S_CDS_START ,
        REPORT_COLUMN.CLONE_S_CDS_STOP ,
        REPORT_COLUMN.CLONE_S_DISCREPANCY,
        REPORT_COLUMN.VECTOR ,
        REPORT_COLUMN.LINKER_3P_NAME ,
        REPORT_COLUMN.LINKER_3P_SEQUENCE ,
        REPORT_COLUMN.LINKER_5P_NAME ,
        REPORT_COLUMN.LINKER_5P_SEQUENCE ,
        REPORT_COLUMN.STRATEGY_NAME
    };
    
    private boolean         isCloningStrategy = false;
    private boolean         isCloneInfo = false;
    private boolean         isCloneNames = false;
    private boolean         isCloneAuthors = false;
    private boolean         isClonePublications = false;
    private boolean         isRefSequence = false;
    private boolean         isRefSequenceNames = false;
    private boolean         isRefSequenceText = false;
    private boolean         isProcessingStatus = false;
    private boolean         isPlateLabel = false;
    private boolean         isUserPlateLabel = false;
    private boolean         isCloneSequence   = false;
    private boolean         isCloneSequenceInfo   = false;
    private boolean         isConstructType=false;
    
    
    public GeneralReport()
    {
        m_report_type = REPORT_TYPE_FOR_THIS_REPORT;
    }
    
  
     protected  List<String[]>  getDataForReport(String sql_items,ITEM_TYPE items_type,
              ReportProperties fr)
     {
         List<String[]>  report_data = null;
         HashMap<Integer, CloningStrategy> cl_str = null;
         ArrayList<Container> containers;
         HashMap<String, ImportClone> clones;
         HashMap<String, ImportFlexSequence> refsequence_info = null;
         try
         {
            if (isCloningStrategy) cl_str=getCloningStrategies();
            containers = getContainersInfo(sql_items,items_type);
            clones =  getCloneInfo(sql_items,items_type);
             refsequence_info = getReferenceSequenceInfo(sql_items,items_type);
           
            //reformat data to List<String[]>
            REPORT_COLUMN[] user_column_in_order =  m_report_type.getUserSelectedReportColumnsSettingsDefinedOrder(
                  fr, m_report_type,   m_user_report_columns);
     
            report_data= buildReportOutput(user_column_in_order, cl_str, containers, clones, refsequence_info);
         }
         catch(Exception e)
         {
              this.getErrorMessages().add(e.getMessage());
         }
         return report_data;
     }
             
   
    
    protected void setUpReportCheckList()
    {
        for (REPORT_COLUMN column : m_user_report_columns)
        {
              switch (column )  
              {
                  case  VECTOR:
                    case LINKER_3P_NAME :
                    case LINKER_3P_SEQUENCE :
                    case LINKER_5P_NAME :
                    case LINKER_5P_SEQUENCE :
                    case STRATEGY_NAME:     {  isCloningStrategy = true;     break;                    }
                  case WELL_NUMBER : case WELL_NAME: 
                  case SAMPLE_ID: case SAMPLE_TYPE  :
                  
                  case PLATE_LABEL:        { isPlateLabel = true; break;}
                  case USER_PLATE_LABEL:                  {isUserPlateLabel = true ; break;}
                  case    CLONE_ID: case CLONE_STATUS:               {isCloneInfo= true ; break;}
                   case  CLONE_NAMES  :                  {isCloneInfo= true ;isCloneNames= true ; break;}
                  case  CLONE_AUTHOR    :                  {isCloneInfo= true ;isCloneAuthors= true ; break;}
                  case  CLONE_PUBLICATION    :              {isCloneInfo= true ;isClonePublications= true ; break;}
                    case    FLEXSEQUENCE_ID:
                    case CDS_START  :
                    case CDS_STOP :
                    case CDSLENGTH :
                    case SPECIES :
                    case FLEXSEQUENCE_STATUS: {isRefSequence= true ; break;}
                    case NAMES   :   {isRefSequence= true ;isRefSequenceNames= true ; break;}
                    case  CDS_TEXT  :    {isRefSequence= true ;isRefSequenceText= true ; break;}
                  case   PROJECT_NAME:
                  case         WORKFLOW_NAME :
                 // case         QUEUE_PROTOCOL_NAME:     { isPlateLabel = true; isProcessingStatus= true ; break;}
                  case         CLONE_SEQUENCE:                  {isCloneSequence= true ; break;}
                  case        CLONE_S_LINKER_5P :
                   case  CLONE_S_LINKER_3P :
                   case  CLONE_S_CDS_START :
                   case  CLONE_S_CDS_STOP:    
                  case CLONE_S_DISCREPANCY: {isCloneSequenceInfo= true ; break;}
                  case CONSTRUCT_TYPE:{isConstructType=true; break;}
                
              }
               
              
        }
    }
   
    private HashMap<Integer, CloningStrategy>      getCloningStrategies()throws Exception
    {
        List clon_strategies = CloningStrategy.getAllCloningStrategies(true);
        HashMap<Integer, CloningStrategy> result = new HashMap<Integer, CloningStrategy>(clon_strategies.size());
        for (Iterator<CloningStrategy> it = clon_strategies.iterator(); it.hasNext(); )
        {
            CloningStrategy item =  it.next();
            result.put( Integer.valueOf (item.getId()),item) ;
         } 
        return result;
    }
    private HashMap<String, ImportClone>          getCloneInfo(String sql_items,
            ITEM_TYPE item_type)throws Exception
    {
        HashMap<String, ImportClone>   clones = new HashMap<String, ImportClone>  ( );
           
        String sql ; ImportClone clone;
        if(isCloneInfo)
        {
            sql=getCloneIDsSQL(sql_items, item_type);
            ArrayList<String>  cloneid_sql =new ArrayList<String>();
            clones = getCloneBasicInfo( sql, cloneid_sql);
            if ( cloneid_sql != null)
            {
             for (String sql_item : cloneid_sql)
            {
                if ( isCloneNames)
                {
                     HashMap <String, ArrayList<PublicInfoItem> >       clone_names=
                                UtilSQL.getNamesFromDatabase("clonename" , "cloneid", sql_item);
                      for ( String clone_id : clone_names.keySet())
                     {
                         clone = clones.get(clone_id);
                         clone.addPublicInfoItems(clone_names.get(clone_id));
                      }
                }
                if (  isClonePublications  )
                {
                     ImportPublication pb;
                     sql= "select cloneid as item1, pubmedid  as item2, title as item3 from "
                        +     " clonepublication cp, publication p where cp.publicationid=p.publicationid and cloneid in ("+sql_item+")";
                     List <String[]> clone_publications=UtilSQL.getDataFromDatabase(sql, 3);
                     for ( String[] pb_record : clone_publications )
                     {
                         clone = clones.get(pb_record[0]);
                         pb = new ImportPublication();
                         pb.setPubMedID(pb_record[1]);
                         pb.setTitle(pb_record[2]);
                         clone.addPublication(  pb);
                         
                     }
                }
                if (  isCloneAuthors )
                {
                    ImportAuthor author;
                    sql= "select cloneid as item1, authortype  as item2, authorname as item3 from "
                        +     " cloneauthor cp, authorinfo p where cp.authorid=p.authorid and cloneid in ("+sql_item+")";
                    List <String[]> clone_authors=UtilSQL.getDataFromDatabase(sql, 3);
                     for ( String[] author_record : clone_authors )
                     {
                         clone = clones.get(author_record[0]);
                         author = new ImportAuthor();
                         author.setName(author_record[2]);
                         author.setType(author_record[1]);
                         clone.addAuthor(  author);
                         
                     }
                }

               if ( isCloneSequenceInfo ) getCloneSequenceInfo(clones, sql_item);
               if ( isCloneSequence ) getCloneSequenceText (clones, sql_item);
            }
            }
        }
        return clones;
    }
   
    
    private String    getCloneIDsSQL(String sql_items,ITEM_TYPE item_type)
    {
         switch (item_type)
         {
             case PLATE_LABELS:
                 return "select cloneid, clonetype, clonename, sequenceid, constructid, strategyid, status, comments from clones where cloneid in ("
                        + " select cloneid from sample where containerid in ("
                        +" select containerid from containerheader where label in (" +sql_items+")))";
            
             case CLONE_ID:
                 return "select cloneid, clonetype, clonename, sequenceid, constructid, strategyid, status, comments from clones where cloneid in ("+sql_items+")";
             case FLEXSEQUENCE_ID:
                 return "select cloneid, clonetype, clonename, sequenceid, constructid, strategyid, status, comments from clones where cloneid in ("
                         +" select cloneid from sample where constructid in ("
                         +" select constructid from designconstruct where sequenceid in ("+sql_items+")))";
            
             case USER_PLATE_LABELS:
                 return "select cloneid, clonetype, clonename, sequenceid, constructid, strategyid, status, comments from clones where cloneid in ("
                        + " select cloneid from sample where containerid in ("
                        +" select containerid from containerheader_name where nametype='USER_ID' and  namevalue in (" +sql_items+")))";
    
             default: return null;
         }
    }
    private  HashMap<String, ImportClone>       getCloneBasicInfo(String sql,
            ArrayList<String>  cloneid_sql )
            throws FlexDatabaseException
    {
        //"select cloneid, clonetype, clonename, mastercloneid,sequenceid, constructid, strategyid, status, comments from clones where cloneid in ("+sql_cl+")";
        HashMap<String, ImportClone>   clones = new HashMap<String, ImportClone>  ( );
        CachedRowSet rs =null;  PublicInfoItem p_info;String tmp;
        ImportClone cur_clone;ImportCloneSequence clone_sequence;
         StringBuffer ids = new StringBuffer( );
     
        try
        {
            rs = DatabaseTransaction.getInstance().executeQuery(sql);
            while(rs.next())
            {
                cur_clone = new ImportClone();
                cur_clone.setId(rs.getInt("cloneid"));
                ids.append(cur_clone.getId()+" ");
                cur_clone.setCloningStrategyId(rs.getInt("strategyid"));
                cur_clone.setType(rs.getString("clonetype"));
                cur_clone.setStatus(rs.getString("status"));
                tmp=rs.getString("clonename");
                if (tmp != null)
                {p_info = new  PublicInfoItem("CLONE_NAME",tmp);
                cur_clone.addPublicInfo(p_info);}
                tmp=rs.getString("comments");
                if (tmp != null)
                {p_info = new  PublicInfoItem("COMMENT",tmp);
                cur_clone.addPublicInfo(p_info);}
                tmp=String.valueOf(rs.getInt("constructid"));
                if (tmp != null)
                {   p_info = new  PublicInfoItem("CONSTRUCTID",tmp);
                    p_info.setIsSubmit(false);
                cur_clone.addPublicInfo(p_info);
                }
                tmp = String.valueOf(rs.getInt("sequenceid"));
                if (tmp != null)
                {  p_info = new  PublicInfoItem("REFSEQUENCEID",tmp);
                   p_info.setIsSubmit(false);
                cur_clone.addPublicInfo(p_info);}
                //p_info = new  PublicInfoItem("MASTERCLONEID",String.valueOf(rs.getInt("mastercloneid")));
                //cur_clone.addPublicInfo(p_info);
                
                clones.put(String.valueOf(cur_clone.getId()), cur_clone);
                
            }
            cloneid_sql.addAll(UtilSQL.prepareItemsListForSQL(ITEM_TYPE.CLONE_ID,  ids.toString()));
            DatabaseTransactionLocal.closeResultSet(rs);
            return clones;
       }
        catch (Exception sqlE)
        {
            System.out.println(sqlE.getMessage());
            throw new FlexDatabaseException("Error occured while getting clone information /SQL: "+sql);
        } finally
        {
            DatabaseTransactionLocal.closeResultSet(rs);
        }
        
    }
            
    private void            getCloneSequenceText(HashMap<String, ImportClone>  clones, 
            String sql_cl) throws FlexDatabaseException
    {
          String sql  = "select cloneid as id, cs.sequenceid, sequencetext from clonesequence cs,clonesequencing ss,  clonesequencetext st"
+" where cloneid in ("+sql_cl+") and cs.sequenceid = st.sequenceid and "
+"cs.sequencingid=ss.sequencingid order by cloneid, sequenceid, sequenceorder";
        HashMap<String, ImportCloneSequence> sequence_text=UtilSQL.getSequenceText(sql);
        ImportClone clone;ImportCloneSequence clone_sequence;
          for ( String clone_id : clones.keySet())
         {
             clone = clones.get(clone_id);
             clone_sequence=sequence_text.get(clone_id);
             if(clone_sequence != null)
             {
                 clone.setCloneSequence(clone_sequence);
             }
          }
        
        
       
    }
    private void            getCloneSequenceInfo(HashMap<String, ImportClone>  clones, 
            String sql_cl) throws FlexDatabaseException
    {
        
          String sql  = "select '' || cloneid as item1, '' || sequenceid as item2,"
  +" sequencetype as item3, linker5p as item4, linker3p as item5,'' || cdsstart as item6,"
 +" '' || cdslength as item7,genechange as item8 from clonesequence cs,clonesequencing ss "
+" where cloneid in ("+sql_cl+") and "
+"cs.sequencingid=ss.sequencingid order by cloneid, sequenceid";
         CachedRowSet rs =null; 
        ImportCloneSequence clone_sequence;
        ImportClone cur_clone;
        try
        {
             List<String[]> data = UtilSQL.getDataFromDatabase(sql, 8);
             for (String[] record : data)
             {
                 clone_sequence = new ImportCloneSequence();
               if ( record[5] != null)  clone_sequence.setCDSStart(Integer.parseInt(record[5]));
               if ( record[1] != null) clone_sequence.setSequenceID(Integer.parseInt(record[1]));
              if ( record[6] != null)  clone_sequence.setCDSStop(Integer.parseInt(record[6]) - clone_sequence.getCDSStart());
                clone_sequence.setMLinker5(record[3]);
                clone_sequence.setMutCDS(record[7]);
                clone_sequence.setMutLinker3(record[4]);
                cur_clone = clones.get(record[0]);
                cur_clone.setCloneSequence(clone_sequence);
             }
      
        }
        catch (Exception sqlE)
        {
            System.out.println(sqlE.getMessage());
            throw new FlexDatabaseException("Error occured while getting clone information /SQL: "+sql);
        } finally
        {
            DatabaseTransactionLocal.closeResultSet(rs);
        }
    }
    
    
    ////////////////////////////////////////////////////////
    private  HashMap<String, ImportFlexSequence>              
            getReferenceSequenceInfo(String sql_items,ITEM_TYPE items_type)
            throws FlexDatabaseException
    {
        HashMap<String, ImportFlexSequence>  ref_sequences = null;
        ImportFlexSequence refsequence;
        String sql;
        if (isRefSequence)
        {
            ArrayList<String>  refsequence_ids =new ArrayList<String> ();
             ref_sequences = getReferenceSequenceBasicInfo ( sql_items, items_type, refsequence_ids );
             if ( refsequence_ids != null)
             {
                 for (String sql_refseq_id_item: refsequence_ids )
                 {
                    if (isRefSequenceNames  )
                    {
                         HashMap <String, ArrayList<PublicInfoItem> >       refsequence_names=
                                        UtilSQL.getNamesFromDatabase("name" , "sequenceid", sql_refseq_id_item);
                          for ( String refsequence_id : refsequence_names.keySet())
                          {
                             refsequence = ref_sequences.get(refsequence_id);
                             refsequence.addPublicInfoItems(refsequence_names.get(refsequence_id));
                             
                          }
                    }
                    if (isRefSequenceText  )
                        {getReferenceSequenceText(ref_sequences, sql_refseq_id_item);}
                 }
             }
        }
        
        return ref_sequences;
    }
    
    /*
 private String    getRefSequenceIDSQL(String sql_items,ITEM_TYPE item_type)
    {
         switch (item_type)
         {
             case PLATE_LABELS:
                 return "select cloneid, clonetype, clonename, sequenceid, constructid, strategyid, status, comments from clones where cloneid in ("
                        + " select cloneid from sample where containerid in ("
                        +" select containerid from containerheader where label in (" +sql_items+")))";
            
             case CLONE_ID:
                 return "select cloneid, clonetype, clonename, sequenceid, constructid, strategyid, status, comments from clones where cloneid in ("+sql_items+")";
             case FLEXSEQUENCE_ID:
                 return "select cloneid, clonetype, clonename, sequenceid, constructid, strategyid, status, comments from clones where cloneid in ("
                         +" select cloneid from sample where constructid in ("
                         +" select constructid from designconstruct where sequenceid in ("+sql_items+")))";
            
             case USER_PLATE_LABELS:
                 return "select cloneid, clonetype, clonename, sequenceid, constructid, strategyid, status, comments from clones where cloneid in ("
                        + " select cloneid from sample where containerid in ("
                        +" select containerid from containerheader_name where nametype='USER_ID' and  namevalue in (" +sql_items+")))";
    
             default: return null;
         }
    }    
     * */
private HashMap<String, ImportFlexSequence> getReferenceSequenceBasicInfo(
        String sql_items,ITEM_TYPE items_type,
        ArrayList<String>  refsequence_ids)
        throws FlexDatabaseException
{
       String  sql = getRefSequenceSQL(sql_items, items_type);        
       HashMap<String, ImportFlexSequence>  sequences=new HashMap<String, ImportFlexSequence> ();
       List<String[]> data = UtilSQL.getDataFromDatabase(sql, 5);
       StringBuffer ids = new StringBuffer( );
       ImportFlexSequence  sequence;
       try
       {
           for (String[] record: data)   
           {
                sequence = new ImportFlexSequence();
                sequence.setCDSStart(Integer.parseInt(record[3]));
                sequence.setId(Integer.parseInt(record[0]));
                ids.append( record[0]   +" ");
                sequence.setCDSStop(Integer.parseInt(record[4]));
                sequence.setFlexStatus(record[1]);
                sequence.setSpesies(record[2]);
                sequences.put( record[0], sequence);
           }
          refsequence_ids.addAll( UtilSQL.prepareItemsListForSQL(ITEM_TYPE.FLEXSEQUENCE_ID,  ids.toString()));
    
          return sequences;
            
        }
        catch (Exception sqlE)
        {
            System.out.println(sqlE.getMessage());
            throw new FlexDatabaseException("Error occured while getting ref sequence information /SQL: "+sql);
        }
   
}


 private String    getRefSequenceSQL(String sql_items,ITEM_TYPE item_type)
    {
     
 
         switch (item_type)
         {
             case PLATE_LABELS:
                 return "select '' || sequenceid as item1, flexstatus as item2,"
+"genusspecies as item3,'' || cdsstart as item4,'' || cdsstop as item5 from flexsequence where sequenceid in ("
+" select distinct(sequenceid) from constructdesign where constructid in ("
 + " select distinct(constructid) from sample where containerid in ("
 +" select containerid from containerheader where label in (" +sql_items+"))))";
            
             case CLONE_ID:
                 return "select '' || sequenceid as item1, flexstatus as item2,"
+"genusspecies as item3,'' || cdsstart as item4,'' || cdsstop as item5 from flexsequence where sequenceid in ("
+" select distinct(sequenceid) from constructdesign where constructid in ("
 + " select distinct(constructid) from sample where cloneid in (" +  sql_items+")))";

             case FLEXSEQUENCE_ID:
return "select '' || sequenceid as item1, flexstatus as item2,"
+"genusspecies as item3,'' || cdsstart as item4,'' || cdsstop as item5 from flexsequence where sequenceid in ("+ sql_items+")";
  
             case USER_PLATE_LABELS:
            return "select '' || sequenceid as item1, flexstatus as item2,"
+"genusspecies as item3,'' || cdsstart as item4,'' || cdsstop as item5 from flexsequence where sequenceid in ("
+" select distinct(sequenceid) from constructdesign where constructid in ("
 + " select distinct(constructid) from sample where containerid in ("
+" select containerid from containerheader_name where nametype='USER_ID' and  namevalue in (" +sql_items+"))))";
    
             default: return null;
         }
    }
   
    private void            getReferenceSequenceText(HashMap<String, ImportFlexSequence> ref_sequences, String seq_ids)
            throws FlexDatabaseException
    {
   String sql  = "select sequenceid as id, sequenceid, sequencetext from sequencetext "
+" where sequenceid in ("+seq_ids+") order by sequenceid, sequenceorder";
        HashMap<String, ImportCloneSequence> sequence_text=UtilSQL.getSequenceText(sql);
        ImportFlexSequence ref_sequence;
        ImportCloneSequence sequence;
          for ( String ref_sequences_id : ref_sequences.keySet())
         {
             ref_sequence = ref_sequences.get(ref_sequences_id);
             sequence=sequence_text.get( String.valueOf(ref_sequence.getId()));
             if(sequence != null)
             {
                 ref_sequence.setSequenceText(sequence.getSequenceText());
             }
          }
    }
    /////////////////////////////////////////////////////////////
    
    private ArrayList<Container>               getContainersInfo(String item_ids,
            ITEM_TYPE itemtype)
            throws Exception
    {
        ArrayList<Container> result = new ArrayList<Container>();
        String sql = null;
        if (isPlateLabel)
        {
            if (itemtype == ITEM_TYPE.PLATE_LABELS)
            {
                sql=       " select  label as item1,'' || cs.containerid as item2,'' || sampleid as item3,"
                             +" sampletype as item4, '' || containerposition as item5, '' || constructid as item6,"
                             +"'' || cloneid as item7, containertype as item8 "
+" from containerheader cs, sample s where s.containerid=cs.containerid and   "
+"label in ("+item_ids+")order by cs.containerid, containerposition";
                 if (isUserPlateLabel)
                 {
                     sql=         " select  label as item1,'' || cs.containerid as item2,'' || sampleid as item3,"
                             +" sampletype as item4, '' || containerposition as item5, '' || constructid as item6,"
                             +"'' || cloneid as item7, containertype as item8 ,"
+"(select namevalue from containerheader_name where nametype='USER_ID' and containerid=cs.containerid) as item9 "
+" from containerheader cs, sample s where s.containerid=cs.containerid and   "
+"label in ("+item_ids+")order by cs.containerid, containerposition";
             
                 }
            }
            else if (itemtype == ITEM_TYPE.CLONE_ID)
            {
                 sql=   " select  label as item1,'' || cs.containerid as item2,'' || sampleid as item3,"
                             +" sampletype as item4, '' || containerposition as item5, '' || constructid as item6,"
                             +"'' || cloneid as item7, containertype as item8 "
+" from containerheader cs, sample s where s.containerid=cs.containerid and   "
+"cloneid in ("+item_ids+")order by cs.containerid, containerposition";
                 if (isUserPlateLabel)
                 {
                     sql=   " select  label as item1,'' || cs.containerid as item2,'' || sampleid as item3,"
                             +" sampletype as item4, '' || containerposition as item5, '' || constructid as item6,"
                             +"'' || cloneid as item7, containertype as item8 ,"
+"(select namevalue from containerheader_name where nametype='USER_ID' and containerid=cs.containerid) as item9 "
+" from containerheader cs, sample s where s.containerid=cs.containerid and   "
+"cloneid in ("+item_ids+")order by cs.containerid, containerposition";
             
                 }
            }
            else if (itemtype == ITEM_TYPE.FLEXSEQUENCE_ID)
            {
                sql=" select  label as item1,'' || cs.containerid as item2,'' || sampleid as item3,"
                             +" sampletype as item4, '' || containerposition as item5, '' || constructid as item6,"
                             +"'' || cloneid as item7, containertype as item8 "

+" from containerheader cs, sample s, constructdesign d where s.containerid=cs.containerid   and d.constructid=s.constructid and "
+"sequenceid in ("+item_ids+")order by cs.containerid, containerposition";       if (isUserPlateLabel)
                 {
                     sql=   " select  label as item1,'' || cs.containerid as item2,'' || sampleid as item3,"
                             +" sampletype as item4, '' || containerposition as item5, '' || constructid as item6,"
                             +"'' || cloneid as item7, containertype as item8 ,"
+"(select namevalue from containerheader_name where nametype='USER_ID' and containerid=cs.containerid) as item9 "
+" from containerheader cs, sample s, constructdesign d where s.containerid=cs.containerid   and d.constructid=s.constructid and "
+"sequenceid in ("+item_ids+")order by cs.containerid, containerposition";
                        
                 }
            }
            else if (itemtype == ITEM_TYPE.USER_PLATE_LABELS)
            {
sql="select label as item1, '' || c.containerid as item2, '' || sampleid as item3, sampletype as item4,'' ||  containerposition as item5,"
+"'' || constructid as itm6,'' || cloneid as item7, containertype as item8 "
+" from containerheader c, sample s , containerheader_name n where s.containerid=c.containerid and "
+"and c.containerid=n.containerid and nametype='USER_ID'  namevalue in ("+item_ids+") order by c.containerid, position";
             
             }
          
            if(isProcessingStatus){}
            if(isConstructType){}
            try
            {
                int prev_container_id = -1;int cur_container_id;
                Container container =null;Sample sample;PublicInfoItem p_info;
                int items_count = (isUserPlateLabel)? 9:8;
                List<String[]> cont_data = UtilSQL.getDataFromDatabase(sql, items_count);
                for (String[] record:cont_data)
                {
                    cur_container_id = Integer.parseInt(record[1]);
                    if (prev_container_id != cur_container_id)
                    {
                        container = new Container(cur_container_id,record[7],null, record[0]);
                        result.add(container);
                    }
                    sample = new Sample(Integer.parseInt(record[2]), Integer.parseInt(record[4]), cur_container_id);
                    if ( record[6] != null) sample.setCloneid(Integer.parseInt(record[6]));
                    if ( record[5] != null) sample.setConstructid(Integer.parseInt(record[5]));
                    sample.setType(record[3]);
                    if (isUserPlateLabel && record[8] != null)
                    {
                        container.setIsPublicInfo(true);
                        ArrayList v=new ArrayList();
                        p_info = new  PublicInfoItem("USER_ID",record[8]);
                        v.add(p_info);
                        container.setPublicInfo(v);
                    }
                    container.addSample(sample);
                    prev_container_id  = cur_container_id;
                }
                return result;
            }
            catch(Exception e)
            {
                this.getErrorMessages().add("Cannot get plate information");
                throw new Exception("Cannot get plate information");
            }
        }
        return result;
    }
    
    
    
    private     List<String[]>           buildReportOutput
        ( REPORT_COLUMN[] user_column_in_order ,
        HashMap<Integer, CloningStrategy> cl_str, 
        ArrayList<Container> containers, 
        HashMap<String, ImportClone> clones, 
        HashMap<String, ImportFlexSequence>  refsequence_info)
    {
        List<String[]> result = new ArrayList<String[]>();
        isCloningStrategy = (cl_str != null && cl_str.size() > 0) ;    
        isCloneInfo = ( clones != null && clones.size() > 0);
        isRefSequence = (refsequence_info !=null && refsequence_info.size() > 0);    
        isPlateLabel = (containers != null && containers.size() > 0);
  
        if ( isPlateLabel )
        {     result = buildPlateBasedReportOutput(user_column_in_order,cl_str,
                    containers,clones,refsequence_info);}
        else if (isCloneInfo)
         {     result = buildCloneBasedReportOutput(user_column_in_order,cl_str,
                clones,refsequence_info);}
        else if (isRefSequence)
        {     result = buildRefSequenceBasedReportOutput(user_column_in_order,
                refsequence_info);}
        else if ( isCloningStrategy )
            result = buildCloningStrategyReportOutput(user_column_in_order,cl_str);
       
      
        return result;
    }
       
    private     List<String[]>            buildCloningStrategyReportOutput
            (REPORT_COLUMN[] user_column_in_order ,
        HashMap<Integer, CloningStrategy> cl_str)
    {
        String[] record;int column_number=0;
         List<String[]> result = new ArrayList<String[]>();
       
        for ( CloningStrategy cl : cl_str.values())
        {
            record = buildRecord(  user_column_in_order ,null,null,null,null,null,cl);
            result.add(record);
        }
        return result;
    }
    
    private     List<String[]>            buildRefSequenceBasedReportOutput
            (REPORT_COLUMN[] user_column_in_order ,
        HashMap<String, ImportFlexSequence> ref_sequences)
    {
        String[] record;int column_number=0;String names;
        List<String[]> result = new ArrayList<String[]>();
       
        for ( ImportFlexSequence refsequence : ref_sequences.values())
        {
                record = buildRecord(  user_column_in_order ,null,null,null,null,refsequence,null);
                result.add(record);
        }
        return result;
    }

    private     List<String[]>            buildCloneBasedReportOutput
            (REPORT_COLUMN[] user_column_in_order ,
        HashMap<Integer, CloningStrategy> cl_str,
         HashMap<String, ImportClone> clones, 
        HashMap<String, ImportFlexSequence>  refsequence_info)
    {
        String[] record;int column_number=0;
         List<String[]> result = new ArrayList<String[]>();
        CloningStrategy cl=null; ImportFlexSequence refsequence=null;String names;
        String tmp;
        for ( ImportClone clone : clones.values())
        {
             if ( cl_str != null)
              { cl = cl_str.get(Integer.valueOf(clone.getCloningStrategyId()));}
              if ( refsequence_info!= null)
              { refsequence = refsequence_info.get( PublicInfoItem.getPublicInfoByName("REFSEQUENCEID",clone.getPublicInfo()).getValue());}
             record = buildRecord(  user_column_in_order ,null,null, null, clone,refsequence,cl);
             result.add(record);
        }
        return result;
    }

    private     List<String[]>            buildPlateBasedReportOutput
        ( REPORT_COLUMN[] user_column_in_order ,
        HashMap<Integer, CloningStrategy> cl_str, 
        ArrayList<Container> containers, 
        HashMap<String, ImportClone> clones, 
        HashMap<String, ImportFlexSequence>  refsequence_info)
    {
        String[] record; Sample sample;
         List<String[]> result = new ArrayList<String[]>();
       CloningStrategy cl=null; ImportFlexSequence refsequence=null;
       ImportClone clone=null;
         
        for ( Container container : containers)
        {
            for ( int count = 0; count < container.getSamples().size(); count++)
            {
                sample = (Sample)container.getSamples().get(count);
                if ( clones != null)
                { clone = clones.get(String.valueOf(sample.getCloneid()));  }
                if ( cl_str != null && clone != null)
                  { cl = cl_str.get(Integer.valueOf(clone.getCloningStrategyId()));}
                if ( refsequence_info!= null  && clone != null)
                  { refsequence = refsequence_info.get( PublicInfoItem.getPublicInfoByName("REFSEQUENCEID",clone.getPublicInfo()).getValue());}
   //System.out.println(sample.getId()) ;  
   String user_container_id = null;
   if ( container.getPublicInfo() != null)
   { user_container_id = PublicInfoItem.getPublicInfoByName("USER_ID" ,   container.getPublicInfo()).getValue();}
                 record = buildRecord(  user_column_in_order ,sample , container.getLabel(),
                     user_container_id,
                     clone,refsequence,cl);
                 result.add(record);
 //  System.out.println(sample.getId()) ;
            }
        }
          
        return result;
    }

    
    private String[]    buildRecord(REPORT_COLUMN[] user_column_in_order ,
            Sample sample, String container_label, String user_container_label,
            ImportClone clone,
            ImportFlexSequence  refsequence,    CloningStrategy cl)
    {
        int column_number=0;String tmp;String names = null;
        String[] record = new String[user_column_in_order.length];
                  
        for ( REPORT_COLUMN column  : user_column_in_order)
        {
          if ( column  == null) continue;// 1 indexed
         // System.out.println(column.getColumnDisplayTitle());
           switch(  column)
          {
              case VECTOR :{ if ( cl != null){ record[column_number]=cl.getClonevector().getName();}break;}
              case LINKER_3P_NAME :{if(cl != null){ record[column_number]=cl.getLinker3p().getName();}break;}
             case LINKER_3P_SEQUENCE :{if(cl != null){ record[column_number]=cl.getLinker3p().getSequence();}break;}
             case LINKER_5P_NAME :{if(cl != null){ record[column_number]=cl.getLinker5p().getName();}break;}
             case LINKER_5P_SEQUENCE :{if(cl != null){ record[column_number]=cl.getLinker5p().getSequence();}break;}
             case STRATEGY_NAME:{if(cl != null){ record[column_number]=cl.getName();}break;}

              case FLEXSEQUENCE_ID  : {if ( refsequence != null){  record[column_number]=String.valueOf(refsequence.getId());}break;}
            case CDS_START  : {if ( refsequence != null){ record[column_number]= String.valueOf(refsequence.getCDSStart());}break;}
            case CDS_STOP : {if ( refsequence != null){  record[column_number]=String.valueOf(refsequence.getCDSStop());} break;}
            case CDSLENGTH : {if ( refsequence != null){  record[column_number]=String.valueOf(refsequence.getCDSStop() - refsequence.getCDSStart());}break;}
              case SPECIES : {if ( refsequence != null){  record[column_number]=refsequence.getSpesies();}break;}
            case NAMES : 
            {
                if ( refsequence != null && refsequence.getPublicInfo()!= null)
                {
                    PublicInfoItem.sorPublicInfo(refsequence.getPublicInfo());
                    names=PublicInfoItem.toString(refsequence.getPublicInfo(),false,false);
                    record[column_number]=names; 
                }
                break;
            }
            case CDS_TEXT : {if ( refsequence != null && refsequence.getSequenceText() != null){ record[column_number]= refsequence.getSequenceText().substring(refsequence.getCDSStart()-1, refsequence.getCDSStop()-1);}break;}
            case FS_SEQUENCE: {if ( refsequence != null && refsequence.getSequenceText() != null){  record[column_number]=refsequence.getSequenceText();}break;}
            case FLEXSEQUENCE_STATUS :     {if ( refsequence != null){record[column_number]=refsequence.getFlexStatus();}break;}
            case CONSTRUCT_TYPE:
            {
               // record[column_number]="";     
                break;
            }
            case CLONE_ID :       {if (  clone  != null ) {  record[column_number]= String.valueOf( clone.getIdAsIs());}break;}
            case CLONE_STATUS :{ if(  clone  != null){  record[column_number]=clone.getStatus();}break;}
            case CLONE_NAMES :
            {
                if ( clone != null && clone.getPublicInfo()!= null)
                {
                    PublicInfoItem.sorPublicInfo(clone.getPublicInfo());
                    names=PublicInfoItem.toString(clone.getPublicInfo(),false,false);
                    record[column_number]=names;
                }
                break;
            }
            case CLONE_AUTHOR :
            {
                if ( clone != null && clone.getAuthors()!= null)
                {
                    Iterator  itauthor= clone.getAuthors().listIterator();
                    while(itauthor.hasNext())
                    {
                        names+= "|"+ ((ImportAuthor)itauthor.next()).toString();
                    }
                    record[column_number]=names;
                }
                break;
            }
            case CLONE_PUBLICATION :
            {
                if ( clone != null && clone.getPublications()!= null)
                {
                    Iterator  itpublication= clone.getPublications().listIterator();
                    while(itpublication.hasNext())
                    {
                        names+= "|"+ ((ImportPublication)itpublication.next()).toString();
                    }
                    record[column_number]=names;
                }
                      
                break;
            }
            case CLONE_SEQUENCE :{ if(  clone  != null && clone.getCloneSequence() != null){  record[column_number]=clone.getCloneSequence().getSequenceText();} break;}
            case CLONE_S_LINKER_5P :{ if (  clone  != null && clone.getCloneSequence() != null){ record[column_number]=clone.getCloneSequence().getMLinker5();}break;}
            case CLONE_S_LINKER_3P :{if(  clone  != null && clone.getCloneSequence()!= null){ record[column_number]=clone.getCloneSequence().getMutLinker3(); }break;}
            case CLONE_S_CDS_START :{if (  clone  != null && clone.getCloneSequence()!=null){ record[column_number]=String.valueOf(clone.getCloneSequence().getCDSStart()); }break;}
            case CLONE_S_CDS_STOP :{if (  clone  != null && clone.getCloneSequence() != null){ record[column_number]=String.valueOf(clone.getCloneSequence().getCDSStop()); }break;}
            case CLONE_S_DISCREPANCY:{if (  clone  != null && clone.getCloneSequence() != null){ record[column_number]= clone.getCloneSequence().getMutCDS();}break;}
            
              
            case PLATE_LABEL  :{ record[column_number]= container_label;break;}
            case USER_PLATE_LABEL  : {  record[column_number]=user_container_label;break;}
            case WELL_NUMBER  :{if ( sample != null){ record[column_number]=String.valueOf( sample.getPosition() );}break;}
            case WELL_NAME :
            {
                if ( sample != null){  record[column_number]= Algorithms.convertWellFromInttoA8_12(sample.getPosition()) ;} 
                break;
            }
            case SAMPLE_ID :{  record[column_number]=String.valueOf(sample.getId());break;}
            case SAMPLE_TYPE  :{  record[column_number]=sample.getType();break;}
          }
           column_number++;
           names="";
         
        }
          return record;
    }
   
}




