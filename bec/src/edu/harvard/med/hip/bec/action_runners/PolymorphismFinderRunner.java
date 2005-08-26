/*
 * PolymorphismFinderRunner.java
 *
 * Created on October 27, 2003, 5:12 PM
 */

package edu.harvard.med.hip.bec.action_runners;

import java.sql.*;
import java.io.*;
 import java.util.*;

import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.coreobjects.feature.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.user.*;
import edu.harvard.med.hip.bec.Constants;
import edu.harvard.med.hip.bec.programs.polymorphism_finder.*;
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.util_objects.*;
 
 
/**
 *
 * @author  HTaycher
 */
public class PolymorphismFinderRunner extends ProcessRunner 
{
    
    
    
    private int                     m_spec_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;    
    private PolymorphismSpec             m_spec = null;
    
    /** Creates a new instance of PolymorphismFinderRunner */
    public void         setSpecId(int v){m_spec_id = v;}
    public String getTitle()    {return "Request for polymorphism finder run.";    }
    
    public void run()
    {
        //create job_order file
        int process_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
        String sql = null; String sql_where = null;
        ArrayList  discrepancy_descriptions = new ArrayList();
        ArrayList  sql_groups_of_items = new ArrayList();
        Hashtable sequenceid_isolatetrackingid = new Hashtable();
        ArrayList   not_processed_ids= new ArrayList();
        if ( ! getPolymorphismFinderSpec()) return ;
        ArrayList blastable_dbs = Algorithms.splitString(m_spec.getParameterByName("PL_DATABASE"));
        
        if ( blastable_dbs == null || blastable_dbs.size() < 1)
        {
            m_error_messages.add("No blastable DB set.");
            return;
        
        }
        try
        {
            sql_groups_of_items =  prepareItemsListForSQL();
            if ( sql_groups_of_items.size() < 1 )return;
            for (int count = 0; count < sql_groups_of_items.size(); count++)
            {
                sql_where = getWhereClause( (String)sql_groups_of_items.get(count) );
                sql = getSequenceIds( sql_where, sequenceid_isolatetrackingid );
                if ( sql != null && sql.length() > 0)
                    discrepancy_descriptions = getDiscrepancyDescriptions(sql, blastable_dbs, BecProperties.getInstance().getProperty("PF_PATH_DB"));
   
                
                if ( discrepancy_descriptions != null && discrepancy_descriptions.size() > 0 ) 
                {
                    Hashtable istr_refseqid = getIsolateTrackingIdORFSequencesIdMap(sequenceid_isolatetrackingid,discrepancy_descriptions);
                    updateDiscrepancyDescriptions( discrepancy_descriptions,
                             sequenceid_isolatetrackingid, istr_refseqid);
                    writeDiscrepancyDiscriptions(discrepancy_descriptions);
                    //index ORF file
                   // edu.harvard.med.hip.bec.export.IndexerForBlastDB.buildIndexForFASTAFile(  edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("POLYORPHISM_FINDER_DATA_DIRECTORY") + java.io.File.separator + FILE_NAME_ORFSEQUENCES_DATA_FILE ,
                   // edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("POLYORPHISM_FINDER_DATA_DIRECTORY") + java.io.File.separator + FILE_NAME_ORFSEQUENCES_INDEX_FILE  );
                }
            }

        }
        catch(Exception e)
        {
              m_error_messages.add(e.getMessage());
        }
        finally
        {
             sendEMails( getTitle() );
        }
   }
    
    
    
     private Hashtable getIsolateTrackingIdORFSequencesIdMap(Hashtable sequenceid_isolatetrackingid,
                                        ArrayList discrepancy_descriptions)throws Exception
     {
         //collect isolatetrackingids to get ORFS
         StringBuffer istr = new StringBuffer();
         String cur_istr = null;
         String prev_sequenceid = null;
         String current_sequenceid = null;
         for (int count = 0 ; count < discrepancy_descriptions.size(); count++)
         {
             current_sequenceid = ((String[])discrepancy_descriptions.get(count))[0];
             if ( count == 0 || !prev_sequenceid.equalsIgnoreCase(current_sequenceid))
             {
                 cur_istr = (String) sequenceid_isolatetrackingid.get(current_sequenceid);
                 if ( cur_istr == null)
                 {
                     m_error_messages.add("Cannot extract isolatetracking id for discrepancy "+((String[])discrepancy_descriptions.get(count))[1]);
                     throw new Exception();
                 }
                 istr.append( sequenceid_isolatetrackingid.get(current_sequenceid)+",");
             }
             prev_sequenceid = current_sequenceid;
             
         }
         String result = istr.toString().substring(0, istr.length()-1);
         return getIsolateTrackingIdORFSequencesIdMap( result);
            
     }
     
     private Hashtable      getIsolateTrackingIdORFSequencesIdMap(String sql)throws Exception
     {
        sql = " select distinct isolatetrackingid, refsequenceid  "
        +" from  isolatetracking i, sequencingconstruct c "
        +" where c.constructid =i.constructid and  i.isolatetrackingid in ("+sql+") order by isolatetrackingid DESC";
        Hashtable istrid_ORFid = new Hashtable();
        DatabaseTransaction t = null;
        ResultSet rs = null;
        StringBuffer sequence_text = new StringBuffer();
        int prev_istrid = 0; int prev_refsequenceid = 0;
        try
        {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while(rs.next())
            {
                if ( prev_istrid == 0 || prev_istrid != rs.getInt("isolatetrackingid") )
                {
                    istrid_ORFid.put( String.valueOf( prev_istrid), String.valueOf(prev_refsequenceid));
                    sequence_text = new StringBuffer();
                }
                prev_istrid = rs.getInt("isolatetrackingid");
                prev_refsequenceid = rs.getInt("refsequenceid");
            }
            //write last one
            if (  istrid_ORFid.get( String.valueOf( prev_istrid)) == null )
            {
                 istrid_ORFid.put( String.valueOf( prev_istrid), String.valueOf(prev_refsequenceid));
            }
            return istrid_ORFid;
         
        }
        catch (Exception E)
        {
            m_error_messages.add("Error occured while trying to get descriptions for descrepancy. "+E+"\nSQL: "+sql);
            throw new Exception();
        }
        finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
        
     }
     
     
     
    private void  updateDiscrepancyDescriptions(ArrayList discrepancy_descriptions,
                            Hashtable sequenceid_isolatetrackingid, Hashtable istr_refseqid)
                            throws Exception
    {
          String sequenceid = null; String istr_id = null;
          String ORF_id = null;
          for (int count = 0; count < discrepancy_descriptions.size(); count++)
          {
              sequenceid = ((String[])discrepancy_descriptions.get(count))[0];
              istr_id =    (String)sequenceid_isolatetrackingid.get(sequenceid);
              if ( istr_id == null) 
              {
                  m_error_messages.add("Cannot get isolatetracking id for discrepancy "+((String[])discrepancy_descriptions.get(count))[1]);
                  throw new Exception();
              }
              ORF_id =   (String) istr_refseqid.get(istr_id);
               if ( ORF_id == null) 
              {
                  m_error_messages.add("Cannot get reference sequence id for discrepancy "+((String[])discrepancy_descriptions.get(count))[1]);
                  throw new Exception();
              }
               ((String[])discrepancy_descriptions.get(count))[0] = ORF_id;
          }
    
    }
         
     private boolean getPolymorphismFinderSpec()
    {  //get primer spec
        try
        {
            m_spec = (PolymorphismSpec)Spec.getSpecById(m_spec_id);
            if (m_spec != null)            return true;
            return false;
        }
        catch(Exception e)
        {
            m_error_messages.add("Cannot extract Polymorphism Finder configuration with id "+m_spec_id);
            return false;
        }
    }
 
     
    private String getWhereString( String sql_items )
    {
        
        // get isolatetrackingid
         switch ( m_items_type)
        {
            case  Constants.ITEM_TYPE_CLONEID :
            {
                 return "  (select isolatetrackingid from flexinfo where flexcloneid in ("+sql_items+"))";
                     }
            case  Constants.ITEM_TYPE_PLATE_LABELS :
            {
                return"  (select isolatetrackingid from isolatetracking where sampleid in   "
                 +" (select sampleid from sample where containerid in "
                 +" (select containerid from  containerheader where label in ("+sql_items+"))))";
            }
            default : return null;
         }
      }
     private String getSequenceIds( String isolatetrackingids , Hashtable sequenceid_isolatetrackingid)
    {
         ArrayList remaining_istrids = new ArrayList();
         String sequence_ids = getCloneSequenceIds( isolatetrackingids , remaining_istrids, sequenceid_isolatetrackingid);
         if ( remaining_istrids.size() > 0)  
         {
             sequence_ids +=  getContigSequenceIds(  
                             Algorithms.convertStringArrayToString(remaining_istrids, ","),
                             new ArrayList()  , 
                             sequenceid_isolatetrackingid);
         }
        if ( sequence_ids.lastIndexOf(',') == sequence_ids.length()-1) 
            sequence_ids=sequence_ids.substring(0, sequence_ids.length()-1);   
        return sequence_ids;
           
    }
     
     private String   getCloneSequenceIds(String isolatetrackingids, 
                    ArrayList remaining_istrids,
                    Hashtable sequenceid_isolatetrackingid)
     {
        String sql = " select f.isolatetrackingid as isolatetrackingid, sequenceid from assembledsequence a, isolatetracking f "
        +" where a.isolatetrackingid(+)=f.isolatetrackingid and f.isolatetrackingid in "+isolatetrackingids+" order by f.isolatetrackingid, sequenceid DESC";
        String result = "";
        ArrayList sequenceids = new ArrayList();   DatabaseTransaction t = null;
        int current_istr =  0;
        ResultSet rs = null;
        try
        {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while(rs.next())
            {
                if (current_istr == rs.getInt("isolatetrackingid") )
                {
                    continue;
                }
                current_istr = rs.getInt("isolatetrackingid");
                if ( rs.getInt("sequenceid") != 0 ) 
                {
                    result += rs.getInt("sequenceid")+",";
                    sequenceid_isolatetrackingid.put( Integer.toString(rs.getInt("sequenceid")),  Integer.toString(current_istr));
                }
                else
                    remaining_istrids.add( String.valueOf( current_istr ));
            }
            return result;
        }
        catch (Exception E)
        {
            m_error_messages.add("Error occured while trying to get descriptions for descrepancy. "+E+"\nSQL: "+sql);
            
        }
        finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
        return null;
     }
    
      private String   getContigSequenceIds(String isolatetrackingids,
                    ArrayList remaining_istrids,
                    Hashtable sequenceid_isolatetrackingid)
     {
        String sql = " select f.isolatetrackingid as isolatetrackingid, sequenceid, s.collectionid as collectionid from stretch_collection c, stretch s, isolatetracking f "
        +" where c.isolatetrackingid(+)=f.isolatetrackingid and s.collectionid=c.collectionid and c.type =" + StretchCollection.TYPE_COLLECTION_OF_GAPS_AND_CONTIGS +" and " 
        +" s.type = " +Stretch.GAP_TYPE_CONTIG +  " and f.isolatetrackingid in ("+isolatetrackingids+") order by isolatetrackingid, c.collectionid DESC";
        String result ="";
        ArrayList sequenceids = new ArrayList();   DatabaseTransaction t = null;
        boolean isNewClone = true; int current_istr= 0 ; 
        boolean isNewCollection = true; int current_collection_id = 0;
        int [] istrid_seqid = new int[3];
        ResultSet rs = null;
        try
        {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while(rs.next())
            {
                if ( current_istr != rs.getInt("isolatetrackingid") && rs.getInt("collectionid") == 0 ) 
                {
                    remaining_istrids.add (String.valueOf(rs.getInt("isolatetrackingid")));
                    continue;
                } 
                   
                if ( current_istr != rs.getInt("isolatetrackingid") )
                { 
                  current_collection_id = rs.getInt("collectionid");
                }
                if ( current_collection_id == rs.getInt("collectionid")  )
                {
                    result += rs.getInt("sequenceid") +",";
                     sequenceid_isolatetrackingid.put(Integer.toString(rs.getInt("sequenceid")), Integer.toString(rs.getInt("isolatetrackingid")));
                }
            }
            return result;            
              
        }
        catch (Exception E)
        {
            m_error_messages.add("Error occured while trying to get descriptions for descrepancy. "+E+"\nSQL: "+sql);
            
        }
        finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
        return null;
     }
    
    
    
    private String getWhereClause( String sql_items )
    {
        
        // get isolatetrackingid
        String isolatetrackingids = null;
         switch ( m_items_type)
        {
            case  Constants.ITEM_TYPE_CLONEID :
            {
                 return "  (select isolatetrackingid from flexinfo where flexcloneid in ("+sql_items+"))";
            }
            case  Constants.ITEM_TYPE_PLATE_LABELS :
            {
                return "  (select isolatetrackingid from isolatetracking where sampleid in   "
                 +" (select sampleid from sample where containerid in "
                 +" (select containerid from  containerheader where label in ("+sql_items+"))))";
            }
            default : return  null;
         }
       
    }
     
    
    /*
     *verification.
Format for the job_order file:
>ORFID discrepancyid  sequence  database – the same discrepancy can be checked on several databases; 
     a lot of discrepancies can correspond to the same ORF, clone is irrelevant
*/
    private ArrayList getDiscrepancyDescriptions(String sql, 
            ArrayList blastable_dbs, String pl_db_path)
     {
        ArrayList discrepancy_descriptions = new ArrayList();
        String discrepancy_sequence = null;
        String discrepancy_description = null;
        DatabaseTransaction t = null;
        String seq = null; String upstream = null; String downstream = null;
        int length = 0;
        String[] discr_description = null;
        String verification_parameters = m_spec.getParameterByName("PL_MATCH_LENGTH")+" "+ m_spec.getParameterByName("PL_MATCH_IDENTITY");
        sql = "select sequenceid, discrepancyid, upstream, changemut, downstream from discrepancy where type = "+Mutation.RNA +"and polymflag = "+Mutation.FLAG_POLYM_NOKNOWN+" and  sequenceid in ("
        + sql +") order by sequenceid";
        
        ResultSet rs = null;
        try
        {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while(rs.next())
            {
                seq = rs.getString("changemut");
                if ( seq == null ) seq="";
                upstream = rs.getString("upstream");
                if (upstream != null)
                {
                    length = ( upstream.length() >= m_spec.getParameterByNameInt("PL_BASES")) ? m_spec.getParameterByNameInt("PL_BASES") : upstream.length() ;
                    upstream = upstream.substring( upstream.length() - length); 
                }
                else
                    upstream ="";
                
                downstream =  rs.getString("downstream");
                if ( downstream == null)
                    downstream="";
                else
                {
                    length = ( downstream.length() >= m_spec.getParameterByNameInt("PL_BASES")) ? m_spec.getParameterByNameInt("PL_BASES") : downstream.length() ;
                    downstream = downstream.substring(0, length);
                }
                discrepancy_sequence = upstream + seq+ downstream;
                 
                for ( int count = 0; count < blastable_dbs.size(); count++)
                {
                    discr_description = new String[2];
                    discr_description[0]=""+rs.getInt("sequenceid");
                    discr_description[1] = ""+rs.getInt("discrepancyid")+" " +discrepancy_sequence+" "+ pl_db_path+(String)blastable_dbs.get(count)+" "+ verification_parameters;
                    discrepancy_descriptions.add(discr_description );
                }
               
            }
            return discrepancy_descriptions;
        }
        catch (Exception E)
        {
            m_error_messages.add("Error occured while trying to get descriptions for descrepancy. "+E+"\nSQL: "+sql);
            
        }
        finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
        return null;
    }
               
    private void writeDiscrepancyDiscriptions(ArrayList discrepancy_descriptions)
    {
        String job_filename = BecProperties.getInstance().getProperty("POLYORPHISM_FINDER_DATA_DIRECTORY") + java.io.File.separator + BecProperties.getInstance().getProperty("FILE_NAME_INPUT_DATA_FILE");
        FileWriter fr = null;
        String[] ar = null;
         try
         {
            fr =  new FileWriter(job_filename, true);
            for (int count = 0; count < discrepancy_descriptions.size(); count++)
            {
                ar = (String[]) discrepancy_descriptions.get(count);
                    fr.write( ar[0] +" "+ ar[1]);
                    fr.write( Constants.LINE_SEPARATOR);
            }
            fr.flush();
            fr.close();
         }
         catch(Exception e )
         {
              m_error_messages.add("Cannot write job file for Polymorphism Finder");
         }
        
        
    }

     public static void main(String args[])

    {
       // InputStream input = new InputStream();
        PolymorphismFinderRunner runner = null;
        User user  = null;
        try
        {

            user = AccessManager.getInstance().getUser("htaycher123","htaycher");
             BecProperties sysProps =  BecProperties.getInstance( BecProperties.PATH);
            sysProps.verifyApplicationSettings();
            edu.harvard.med.hip.bec.DatabaseToApplicationDataLoader.loadDefinitionsFromDatabase();

            runner = new PolymorphismFinderRunner();
            runner.setInputData(Constants.ITEM_TYPE_CLONEID, "  163358	163458	163509	173104	159362	163010	163062	163124	172897	163270	163302	163398	173530	173096	163080	163493	173545	173665	173686	172940	173238	163232	173701	173753	173052	173392	173437	173856	159289	159294	173085	173477	173575	173856	159409	173384	172865	173268	173298	173814	173817	173952	173912	173022	173064		");
            runner.setUser(user);
        //    runner.setBlastableDBNames("db1 db2");
            runner.setSpecId(118);// for bec
         

             runner.run();
        }
        catch(Exception e){}



        System.exit(0);
     }

    
   
}
    
   
    

