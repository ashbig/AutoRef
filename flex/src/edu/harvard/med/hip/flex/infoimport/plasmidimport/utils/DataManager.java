/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.infoimport.plasmidimport.utils;


import edu.harvard.med.hip.flex.util.*;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.infoimport.coreobjectsforimport.*;
import edu.harvard.med.hip.flex.report.*;
import static edu.harvard.med.hip.flex.infoimport.ConstantsImport.ITEM_TYPE;
import static edu.harvard.med.hip.flex.infoimport.plasmidimport.PlasmidImporterDefinitions.PLASMID_TRANSFER_CLONE_STATUS;

import java.sql.*;
import sun.jdbc.rowset.*;
import edu.harvard.med.hip.flex.database.*;


import java.util.*;
import java.io.*;
/**
 *
 * @author htaycher
 */
public class DataManager
{
    private HashMap<String,ImportAuthor> m_authors ;
    public DataManager(){}
    
    public HashMap<String,ImportAuthor> getAuthors(){return m_authors;}
    
    public HashMap<Integer, CloningStrategy>      getCloningStrategies()throws Exception
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
    public HashMap<String, ImportClone>          getCloneInfo(String sql_items,
            ITEM_TYPE item_type)throws Exception
    {
        HashMap<String, ImportClone>   clones = new HashMap<String, ImportClone>  ( );
           
        String sql ; ImportClone clone;
        sql=getCloneIDsSQL(sql_items, item_type);
        ArrayList<String>  cloneid_sql =new ArrayList<String>();
        clones = getCloneBasicInfo( sql, cloneid_sql);
        if ( cloneid_sql != null)
        {
             for (String sql_item : cloneid_sql)
            {
               //CloneNames)
               HashMap <String, ArrayList<PublicInfoItem> >       clone_names=
                                UtilSQL.getNamesFromDatabase("clonename" , "cloneid", sql_item);
              for ( String clone_id : clone_names.keySet())
             {
                 clone = clones.get(clone_id);
                 clone.addPublicInfoItems(clone_names.get(clone_id));
              }
               // isClonePublications  )
                ImportPublication pb;
                 sql= "select cloneid as item1, pubmedid  as item2, title as item3, p.publicationid as item4 from "
                    +     " clonepublication cp, publication p where cp.publicationid=p.publicationid and cloneid in ("+sql_item+")";
                 List <String[]> clone_publications=UtilSQL.getDataFromDatabase(sql, 3);
                 for ( String[] pb_record : clone_publications )
                 {
                     clone = clones.get(pb_record[0]);
                     pb = new ImportPublication();
                     pb.setPubMedID(pb_record[1]);
                     pb.setTitle(pb_record[2]);
                     pb.setId(Integer.valueOf(pb_record[3]));
                     clone.addPublication(  pb);

                 }
              //  isCloneAuthors )
               ImportAuthor author;
                sql= "select cloneid as item1, authortype  as item2, authorname as item3, "
 +" firstname as item4, lastname as item5, fax as item6,authoremail as item7,"
+" address as item8, www as item9,description as item10,organizationname as item11 "

+" from  cloneauthor cp, authorinfo p where cp.authorid=p.authorid and cloneid in ("+sql_item+")";
                  List <String[]> clone_authors=UtilSQL.getDataFromDatabase(sql, 11);
                     for ( String[] author_record : clone_authors )
                     {
                         clone = clones.get(author_record[0]);
                         author = new ImportAuthor();
                         author.setId(Integer.parseInt(author_record[1]));
                         author.setName(author_record[2]);
                         author.setType(author_record[1]);
                         author.setFNName(author_record[3]);
                         author.setFLName(author_record[4]);
                         author.setFax(author_record[5]);
                         author.setEMail(author_record[6]);
                         author.setAdress(author_record[7]);
                         author.setWWW(author_record[8]);
                         author.setDescription(author_record[9]);
                         author.setOrgName(author_record[10]);
                         
                         if ( m_authors.get(author_record[1])!= null)
                             m_authors.put(author_record[1], author);
                         clone.addAuthorIDAsString(  author_record[1]);
                         
                     }
                
               // getCloneSequenceInfo(clones, sql_item);
                //getCloneSequenceText (clones, sql_item);
                     getCloneSequenceInfo(clones);
             }
        }
        return clones;
    }
   
    
    private String    getCloneIDsSQL(String sql_items,ITEM_TYPE item_type)
    {
         switch (item_type)
         {
             case ITEM_TYPE_PLATE_LABELS:
                 return "select cloneid, isreadyforplasmid,mastercloneid,clonetype, clonename, c.sequenceid as sequenceid, constructtype, cs.constructid as constructid, strategyid, status, comments "
                         +" from clones c, constructdesign cs where  c.constructid=cs.constructid and cloneid in ("
                        + " select cloneid from sample where containerid in ("
                        +" select containerid from containerheader where   label in (" +sql_items+")))";
            
             case ITEM_TYPE_CLONEID:
                 return "select cloneid, isreadyforplasmid, mastercloneid,clonetype, clonename, c.sequenceid, constructtype, "
+" cs.constructid as constructid, strategyid, status, comments from clones c, "
+" constructdesign cs where c.constructid=cs.constructid and cloneid in("+sql_items+")";
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
                cur_clone.setMasterCloneId(rs.getInt("mastercloneid"));
                int isreadyforplasmid=rs.getInt("isreadyforplasmid");
                cur_clone.setTransferStatus(PLASMID_TRANSFER_CLONE_STATUS.getValueByIntValue(isreadyforplasmid));
                
                tmp=rs.getString("constructtype");
                if (tmp != null)
                {p_info = new  PublicInfoItem("FORMAT",tmp);
                cur_clone.addPublicInfo(p_info);}
                
                tmp=rs.getString("clonename");
                if (tmp != null)
                {p_info = new  PublicInfoItem("CLONEHIPNAME",tmp);
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
            cloneid_sql.addAll(UtilSQL.prepareItemsListForSQL
                    (edu.harvard.med.hip.flex.report.ReportConstants.ITEM_TYPE.CLONE_ID,  ids.toString()));
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
          String sql  = "select cloneid as id, cs.sequenceid, sequencetext "
+" from clones cc, clonesequence cs,  clonesequencetext st "
+" where cc.mastercloneid=cs.mastercloneid and "
 +" mastercloneid in ( select mastercloneid from clones where cloneid in ( "+sql_cl+")) and cs.sequenceid = st.sequenceid and "
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
+" where mastercloneid in ( select mastercloneid from clones where cloneid in  ("+sql_cl+")) and "
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
              if ( record[6] != null)  clone_sequence.setCDSStop(Integer.parseInt(record[6]) + clone_sequence.getCDSStart());
                clone_sequence.setMLinker5(record[3]);
                clone_sequence.setMutCDS(record[7]);
                clone_sequence.setMutLinker3(record[4]);
                cur_clone = clones.get(record[0]);
                cur_clone.setCloneSequence(clone_sequence);
                cur_clone.setStatus("SEQUENCE VERIFIED");
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
    
     private void            getCloneSequenceInfo(HashMap<String, ImportClone>  clones) throws FlexDatabaseException
    {
        
          String sql_seq_info  = "select  sequenceid , sequencetype , linker5p , linker3p , cdsstart ,"
 +" cdslength ,genechange  from clonesequence where mastercloneid =?";
          
        String sql_seq_text  = "select sequencetext  from  clonesequencetext where sequenceid=? order  by sequenceorder";
        ImportCloneSequence clone_sequence;ResultSet rs =null;
        ResultSet rs_stext =null;StringBuffer stext ;
        Connection con=null;PreparedStatement stm_seq_info=null;PreparedStatement stm_seq_text =null;
        try
        {
            con = DatabaseTransaction.getInstance().requestConnection();
             stm_seq_info = con.prepareStatement(sql_seq_info);
             stm_seq_text = con.prepareStatement(sql_seq_text);
            for (ImportClone cur_clone:  clones.values())
            {
                stm_seq_info.setInt(1,cur_clone.getMaterCloneId());
                rs = stm_seq_info.executeQuery();
               
                if (rs.next())
                {
                    clone_sequence = new ImportCloneSequence();
                   clone_sequence.setCDSStart(rs.getInt("cdsstart"));
                   clone_sequence.setSequenceID(rs.getInt("sequenceid"));
                   clone_sequence.setCDSStop(rs.getInt("cdslength") + clone_sequence.getCDSStart());
                    clone_sequence.setMLinker5(rs.getString("linker5p"));
                    clone_sequence.setMutCDS(rs.getString("genechange"));
                    clone_sequence.setMutLinker3(rs.getString("linker3p"));
                    cur_clone.setCloneSequence(clone_sequence);
                    cur_clone.setStatus("SEQUENCE VERIFIED");
                    
                    stext = new StringBuffer();
                     stm_seq_text.setInt(1,clone_sequence.getSequenceID());
                     rs_stext = stm_seq_text.executeQuery();
                     while(rs_stext.next())
                     {
                         stext.append(rs_stext.getString("sequencetext"));
                     }
                    clone_sequence.setSequenceText(stext.toString());
                }
            }
        }
        catch (Exception sqlE)
        {
            System.out.println(sqlE.getMessage());
            throw new FlexDatabaseException("Error occured while getting clone sequence information /SQL: "+sqlE.getMessage());
        } finally
        {
            
            DatabaseTransaction.closeStatement(stm_seq_info);
            DatabaseTransaction.closeStatement(stm_seq_text);
            DatabaseTransaction.closeConnection(con);
        }
    }
    
    
    ////////////////////////////////////////////////////////
    public  HashMap<String, ImportFlexSequence>              
            getReferenceSequenceInfo(String sql_items,ITEM_TYPE items_type)
            throws FlexDatabaseException
    {
        HashMap<String, ImportFlexSequence>  ref_sequences = null;
        ImportFlexSequence refsequence;
        String sql;
        ArrayList<String>  refsequence_ids =new ArrayList<String> ();
        ref_sequences = getReferenceSequenceBasicInfo ( sql_items, items_type, refsequence_ids );
        if ( refsequence_ids != null)
         {
             for (String sql_refseq_id_item: refsequence_ids )
             {
                //isRefSequenceNames  )
                HashMap <String, ArrayList<PublicInfoItem> >       refsequence_names=
                                    UtilSQL.getNamesFromDatabase("name" , "sequenceid", sql_refseq_id_item);
              for ( String refsequence_id : refsequence_names.keySet())
              {
                 refsequence = ref_sequences.get(refsequence_id);
                 refsequence.addPublicInfoItems(refsequence_names.get(refsequence_id));

              }
               //isRefSequenceText  )
              getReferenceSequenceText(ref_sequences, sql_refseq_id_item);
             }
         }
        return ref_sequences;
    }
    
   
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
          refsequence_ids.addAll( UtilSQL.prepareItemsListForSQL
                  (edu.harvard.med.hip.flex.report.ReportConstants.ITEM_TYPE.FLEXSEQUENCE_ID,  ids.toString()));
    
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
             case ITEM_TYPE_PLATE_LABELS:
                 return "select '' || sequenceid as item1, flexstatus as item2,"
+"genusspecies as item3,'' || cdsstart as item4,'' || cdsstop as item5 from flexsequence where sequenceid in ("
+" select distinct(sequenceid) from constructdesign where constructid in ("
 + " select distinct(constructid) from sample where containerid in ("
 +" select containerid from containerheader where label in (" +sql_items+"))))";
            
             case ITEM_TYPE_CLONEID:
                 return "select '' || sequenceid as item1, flexstatus as item2,"
+"genusspecies as item3,'' || cdsstart as item4,'' || cdsstop as item5 from flexsequence where sequenceid in ("
+" select distinct(sequenceid) from constructdesign where constructid in ("
 + " select distinct(constructid) from sample where cloneid in (" +  sql_items+")))";

            
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
    
    public ArrayList<Container>               getContainersInfo(String item_ids,
            ITEM_TYPE itemtype)
            throws Exception
    {
        ArrayList<Container> result = new ArrayList<Container>();
        String sql = null;
        if (itemtype == ITEM_TYPE.ITEM_TYPE_PLATE_LABELS)
        {
                sql=       " select  label as item1,'' || cs.containerid as item2,'' || sampleid as item3,"
                             +" sampletype as item4, '' || containerposition as item5, '' || constructid as item6,"
                             +"'' || cloneid as item7, containertype as item8 "
+" from containerheader cs, sample s where s.containerid=cs.containerid and   "
+"label in ("+item_ids+")order by cs.containerid, containerposition";
              
        }
            else if (itemtype == ITEM_TYPE.ITEM_TYPE_CLONEID)
            {
                 sql=   " select  label as item1,'' || cs.containerid as item2,'' || sampleid as item3,"
                             +" sampletype as item4, '' || containerposition as item5, '' || constructid as item6,"
                             +"'' || cloneid as item7, containertype as item8 "
+" from containerheader cs, sample s where s.containerid=cs.containerid and   "
+"cloneid in ("+item_ids+")order by cs.containerid, containerposition";
                
            }
             try
            {
                int prev_container_id = -1;int cur_container_id;
                Container container =null;Sample sample;PublicInfoItem p_info;
                List<String[]> cont_data = UtilSQL.getDataFromDatabase(sql, 8);
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
                   
                    container.addSample(sample);
                    prev_container_id  = cur_container_id;
                }
                return result;
            }
            catch(Exception e)
            {
                throw new Exception("Cannot get plate information");
            }
        }
        
    }
    
    
