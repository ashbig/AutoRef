/*
 * PolymorphismFinderRunner.java
 *
 * Created on October 27, 2003, 5:12 PM
 */

package edu.harvard.med.hip.bec.action_runners;

import java.sql.*;
import java.io.*;


import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.programs.needle.*;
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.coreobjects.endreads.*;
import edu.harvard.med.hip.bec.coreobjects.feature.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.form.*;
import edu.harvard.med.hip.bec.user.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.Constants;
import edu.harvard.med.hip.bec.sampletracking.mapping.*;
import edu.harvard.med.hip.bec.sampletracking.objects.*;
import edu.harvard.med.hip.bec.programs.assembler.*;
import edu.harvard.med.hip.bec.util_objects.*;
import edu.harvard.med.hip.bec.ui_objects.*;
  import java.util.*;
  import edu.harvard.med.hip.utility.*;
 
/**
 *
 * @author  HTaycher
 */
public class PolymorphismFinderRunner extends ProcessRunner 
{
    
    
    private final static   String FILE_NAME_INPUT_DATA_FILE = "pl_input_data.txt";
    
    private int                     m_spec_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;    
    private PolymorphismSpec             m_spec = null;
    private String                  m_blastabledb_names = null;
   
    /** Creates a new instance of PolymorphismFinderRunner */
    public void         setSpecId(int v){m_spec_id = v;}
    public void         setBlastableDBNames(String v) { m_blastabledb_names = v;}
    public String getTitle()    {return "Request for polymorphism finder run.";    }
    
    public void run()
    {
        //create job_order file
        int process_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
        String sql = null;
        ArrayList  discrepancy_descriptions = new ArrayList();
        ArrayList  sql_groups_of_items = new ArrayList();
        if ( ! getPolymorphismFinderSpec()) return ;
        ArrayList blastable_dbs = Algorithms.splitString(m_blastabledb_names);
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
                sql = getQueryString( (String)sql_groups_of_items.get(count) );
                if ( sql != null && sql.length() > 0)
                    discrepancy_descriptions = getDiscrepancyDescriptions(sql, blastable_dbs);
                if ( discrepancy_descriptions != null && discrepancy_descriptions.size() > 0 ) 
                    writeDiscrepancyDiscriptions(discrepancy_descriptions);
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
 
     private String getQueryString( String sql_items )
    {
        
        // get isolatetrackingid
        String isolatetrackingids = null;
         switch ( m_items_type)
        {
            case  Constants.ITEM_TYPE_CLONEID :
            {
                 isolatetrackingids ="  (select isolatetrackingid from flexinfo where flexcloneid in ("+sql_items+"))";
                 break;
            }
            case  Constants.ITEM_TYPE_PLATE_LABELS :
            {
                 isolatetrackingids ="  (select isolatetrackingid from isolatetracking where sampleid in   "
                 +" (select sampleid from sample where containerid in "
                 +" (select containerid from  containerheader where label in ("+sql_items+"))))";
                break;// + " and a.analysisstatus in ("+sequence_analysis_status+")";
            }
            default : isolatetrackingids = null;
         }
         ArrayList remaining_istrids = new ArrayList();
         ArrayList sequence_ids = getCloneSequenceIds( isolatetrackingids , remaining_istrids);
         if ( remaining_istrids.size() > 0)  
         {
             sequence_ids.addAll(  getContigSequenceIds(  Algorithms.convertStringArrayToString(remaining_istrids, ","), new ArrayList())  );
         }
             
        return "select discrepancyid, upstream, changemut, downstream from discrepancy where type = "+Mutation.RNA +" and  sequenceid in ("
        + Algorithms.convertStringArrayToString(sequence_ids, ",") +")";
           
    }
     
     private ArrayList   getCloneSequenceIds(String isolatetrackingids, ArrayList remaining_istrids)
     {
        String sql = " select f.isolatetrackingid as isolatetrackingid, sequenceid from assembledsequence a, isolatetracking f "
        +" where a.isolatetrackingid(+)=f.isolatetrackingid and f.isolatetrackingid in "+isolatetrackingids+" order by f.isolatetrackingid, sequenceid DESC";
        
        
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
                    sequenceids.add( String.valueOf( rs.getInt("sequenceid")) );
                else
                    remaining_istrids.add( String.valueOf( current_istr ));
            }
            return sequenceids;
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
    
      private ArrayList   getContigSequenceIds(String isolatetrackingids, ArrayList remaining_istrids)
     {
        String sql = " select f.isolatetrackingid as isolatetrackingid, sequenceid, s.collectionid as collectionid from stretch_collection c, stretch s, isolatetracking f "
        +" where c.isolatetrackingid(+)=f.isolatetrackingid and s.collectionid=c.collectionid and c.type =" + StretchCollection.TYPE_COLLECTION_OF_GAPS_AND_CONTIGS +" and " 
        +" s.type = " +Stretch.GAP_TYPE_CONTIG +  " and f.isolatetrackingid in ("+isolatetrackingids+") order by isolatetrackingid, c.collectionid DESC";
        ArrayList sequenceids = new ArrayList();   DatabaseTransaction t = null;
        boolean isNewClone = true; int current_istr= 0 ; 
        boolean isNewCollection = true; int current_collection_id = 0;
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
                    sequenceids.add( String.valueOf( rs.getInt("sequenceid")));
                }
            }
            return sequenceids;            
              
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
    
    private ArrayList getDiscrepancyDescriptions(String sql, ArrayList blastable_dbs)
    {
        ArrayList discrepancy_descriptions = new ArrayList();
        String discrepancy_description = null;
        DatabaseTransaction t = null;
        String seq = null; String upstream = null; String downstream = null;
        int length = 0;
        ResultSet rs = null;
        try
        {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while(rs.next())
            {
                discrepancy_description = ">"+rs.getInt("discrepancyid");
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
                discrepancy_description += " "+ upstream + seq+ downstream;
                for ( int count = 0; count < blastable_dbs.size(); count++)
                {
                    discrepancy_descriptions.add(discrepancy_description + " " +(String)blastable_dbs.get(count));
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
        String job_filename = edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("POLYORPHISM_FINDER_DATA_DIRECTORY") + java.io.File.separator + FILE_NAME_INPUT_DATA_FILE;
        File job_file = new File(job_filename);
        boolean isAppend = job_file.exists();
            
        try
        {
            Algorithms.writeArrayIntoFile(discrepancy_descriptions,isAppend,job_filename,true);
        }
        catch(Exception e)
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
            runner.setInputData(Constants.ITEM_TYPE_CLONEID, "  201483    201485    201487    201489    201491    201493    201495    201497    201499    201501    201503   158965   200668    200669    200690    200693    200695    200702    200704    200706    200709    200717    200719	");
            runner.setUser(user);
            runner.setBlastableDBNames("db1 db2");
            runner.setSpecId(104);// for bec
         

             runner.run();
        }
        catch(Exception e){}



        System.exit(0);
     }

    
   
}
    
   
    

