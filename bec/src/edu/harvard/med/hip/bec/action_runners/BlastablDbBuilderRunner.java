/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.harvard.med.hip.bec.action_runners;

/**
 *
 * @author htaycher
 */


import java.sql.*;
import java.io.*;


import edu.harvard.med.hip.bec.coreobjects.endreads.*;
import edu.harvard.med.hip.bec.export.*;
import edu.harvard.med.hip.bec.programs.blast.*;
import edu.harvard.med.hip.bec.bioutil.*;
import  edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.file.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.user.*;
import edu.harvard.med.hip.bec.Constants;
import edu.harvard.med.hip.bec.util_objects.*;
import edu.harvard.med.hip.bec.ui_objects.*;
  import java.util.*;
  import edu.harvard.med.hip.utility.*;
/**
 *
 * @author  HTaycher
 */

public class BlastablDbBuilderRunner extends ProcessRunner 
{
  
     public static final String FLEX_DB = "FLEX";
    public static final String ACE_DB = "ACE";
    
    private String      m_blastable_dbname = null;
  private String m_vector_library_to_add=null;
  private boolean        m_isACEDatabase=true;
  
      public void         setVectorLibraryName(String v){System.out.println(v); m_vector_library_to_add = v;}
 public void        setIsACEDatabase(boolean v){ m_isACEDatabase = v;}
     public void         setBlastableDBName(String v){System.out.println(v); m_blastable_dbname = v;}
    public String       getTitle()    { return "Request to rebuild blastable database for mismatch report.";    }
   
    public void run_process()
    {
        Connection conn=null;
         try
         {
             if (m_isACEDatabase)
                conn = DatabaseTransaction.getInstance().requestConnection();
             else 
                 conn=getFLEXConnection();
              String blast_location= BecProperties.getInstance().getProperty("BLAST_EXE_COMMON_PATH");
              String vector_sequences_file = null;
              if (m_vector_library_to_add.trim().length()>0)
              {vector_sequences_file=BecProperties.getInstance().getProperty("VECTOR_LIBRARIES_ROOT")
                     + File.separator+m_vector_library_to_add;}
              //get seq_id by 100
              ArrayList<String> sequence_ids = getAllSequenceIDNoDublicate(conn);
              String temp_blastable_dbname=m_blastable_dbname+".txt";
              for (String sequence_ids_as_sql:sequence_ids)
              {
                 writeBlastableDBForSetOfSequences( sequence_ids_as_sql,
                    temp_blastable_dbname,   conn) ;
              }
              //reasign db
              File md = new File(m_blastable_dbname);
              if (md.exists())md.delete();
              File tmd = new File(temp_blastable_dbname);
              tmd.renameTo(new File(m_blastable_dbname));
              
              if (vector_sequences_file != null)
              { addVectorSequences(m_blastable_dbname, vector_sequences_file);}
                formatDB(m_blastable_dbname, blast_location);
              
           }
        catch(Exception ex)
        {
            m_error_messages.add(ex.getMessage());
        }
        finally
        {
            if ( conn !=null) DatabaseTransaction.closeConnection(conn);
            sendEMails( getTitle() );
        }
     
    }
    
    
    //-----------------------------------
    
    private Connection    getFLEXConnection()throws Exception
    {
         String flex_url =    BecProperties.getInstance().getProperty("FLEX_URL") ;
             String flex_username =          BecProperties.getInstance().getProperty("FLEX_USERNAME"); 
             String flex_password =          BecProperties.getInstance().getProperty("FLEX_PASSWORD");
         try
         {
             return DatabaseTransactionLocal.getInstance(
         
                    flex_url ,    flex_username,  flex_password).requestConnection();
         }
         catch(Exception e)
         { m_error_messages.add("Cannot open connection to FLEX database");
           throw new Exception("Cannot open connection to FLEX database");}
           
    }
    private  ArrayList<String>   getAllSequenceIDNoDublicate
            (Connection conn)
            throws BecDatabaseException
    {
        ArrayList<String>  seqids = new ArrayList<String> ();
        String tmp_sql=null;
        ResultSet rs = null;int counter=0;StringBuffer ids=new StringBuffer();
        String sql = null;
        if(m_isACEDatabase)
            sql="select DISTINCT  refsequenceid  from sequencingconstruct where constructid in " 
+"(select distinct constructid from isolatetracking where sampleid in "
+"(select  sampleid from   sample where containerid in  "
+"(select containerid from containerheader where label in ( ";
        else
            sql="select  DISTINCT  sequenceid as refsequenceid from constructdesign where constructid in "
+" (select  constructid from   sample where containerid in "
+" (select containerid from containerheader where label in ( ";
        ArrayList sql_groups_of_items=  prepareItemsListForSQL(2);
        try
        {
             for (int count = 0; count < sql_groups_of_items.size(); count++)
            {
                tmp_sql= (m_isACEDatabase) ?
                    sql+(String)sql_groups_of_items.get(count)+"))))"
                    : sql+(String)sql_groups_of_items.get(count)+" )))";
                ids = new StringBuffer();
                rs = DatabaseTransaction.executeQuery(tmp_sql , conn);
                  while(rs.next())
                  {
                     ids.append(rs.getInt("refsequenceid"));
                     ids.append(",");
                  }
                  String tmp = ids.toString();
                  if (tmp.trim().length()>0 && tmp.charAt(tmp.length()-1)==',')
                      tmp=tmp.substring(0,tmp.length()-1);
                  if (tmp.trim().length()!=0 )
                      seqids.add(tmp);
             }
             
             return seqids;
             
        }
        catch(Exception e)
        {
            m_error_messages.add("Error occured "+e.getMessage()+" "+sql);
            throw new BecDatabaseException("Error occured "+e.getMessage()+" "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
        
    }
    private void    writeBlastableDBForSetOfSequences( String sequence_ids,
            String location, 
            Connection conn) throws Exception
    {
        ArrayList<BaseCDSSequence>  sequences=  getSequenceInformation(  sequence_ids,
                  conn);
        
        File fl = new File(location);
        if ( !fl.getParentFile().exists())
            throw new Exception ("Directory "+fl.getParent()+ "(location of desirable database) does not exist");
        BufferedWriter fout = new BufferedWriter(new FileWriter(fl,true));
    
        for (BaseCDSSequence seq: sequences)
        {
            try
            {
                seq.writeToFile(fout, false);
            }
            catch (Exception e)
            {
                m_error_messages.add(e.getMessage());
            }
        }
        fout.flush();
        fout.close();
        
    }
    
    
    
    
    private ArrayList<BaseCDSSequence>    getSequenceInformation(String sequence_ids,
            Connection conn) throws Exception
    {
        ResultSet rs = null;  int seq_id=0;
         int cur_sequence_id =0 ; boolean isNewSequence = false;
         BaseCDSSequence cur_sequence=new BaseCDSSequence();
        StringBuffer sequence= new StringBuffer();
        ArrayList<BaseCDSSequence> arr = new ArrayList<BaseCDSSequence>();
        arr.add(cur_sequence);
        String sql=null;
         try
        {
             // by 100 sequences
            if(BecProperties.getInstance().isInternalHipVersion())
            { 
                if (m_isACEDatabase)
                {sql="select flexsequenceid  as sequenceid,f.sequenceid, cdsstart, cdsstop, infotext "
+"  from refsequence f, sequenceinfo i ,view_flexbecsequenceid v "
+" where f.sequenceid=i.sequenceid and f.sequenceid=v.becsequenceid and f.sequenceid in ("
            +sequence_ids+") order by f.sequenceid,infoorder";}
                else
                {sql="select f.sequenceid as sequenceid, cdsstart, cdsstop, sequencetext as infotext "
+"  from flexsequence f, sequencetext i  "
+" where f.sequenceid=i.sequenceid   and f.sequenceid in ("
            +sequence_ids+") order by f.sequenceid,sequenceorder";}
            }
            else
            {
                 sql="select f.sequenceid as sequenceid, cdsstart, cdsstop, infotext "
+"  from refsequence f, sequenceinfo i "
+" where f.sequenceid=i.sequenceid and f.sequenceid in ("
            +sequence_ids+") order by f.sequenceid,infoorder";
            }
                  rs = DatabaseTransaction.executeQuery(sql , conn);
                  while(rs.next())
                  {
                      if (seq_id !=0 && seq_id !=  rs.getInt("sequenceid"))
                          isNewSequence=true;
                      if (isNewSequence)
                      {
                              seq_id=0;
                              cur_sequence.setText(sequence.toString());
                            sequence = new StringBuffer();
                            isNewSequence=false;
                            cur_sequence=new BaseCDSSequence();
                            arr.add(cur_sequence);
                      }
                      if ( seq_id == 0)
                      {
                          cur_sequence.setId( rs.getInt("sequenceid"));
                          seq_id=cur_sequence.getId();
                          cur_sequence.setCdsStart( rs.getInt("cdsstart"));
                          cur_sequence.setCdsStop( rs.getInt("cdsstop"));
                      }
                      sequence.append( rs.getString("infotext"));
                      
                   }
                   cur_sequence.setText(sequence.toString());
                  return arr;
           
        } 
        catch (Exception e)
        {
               m_error_messages.add("Error occured "+e.getMessage()+" "+sql);
         
            throw new BecDatabaseException("Error occured "+e.getMessage()+" "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
    
    }
     
    
     
   
     public void    addVectorSequences(String location, String vector_sequences_file) throws Exception
    {
         
        String cmd = "cmd /c cat "+vector_sequences_file+" >> "+location  ;
        System.out.println(cmd);
        edu.harvard.med.hip.bec.programs.SystemProcessRunner.runOSCall(cmd);
     
    }
    public  void  formatDB(String location, String blast_location) throws Exception
    {
        
        String cmd = "cmd /c "+ blast_location +"\\formatdb -pF -oT -i "+ location;
        edu.harvard.med.hip.bec.programs.SystemProcessRunner.runOSCall(cmd);
        
    }
    
    
     
     public static void main(String args[])
{
    ProcessRunner runner = new BlastablDbBuilderRunner();
      try
    {
            BecProperties sysProps =  BecProperties.getInstance( BecProperties.APPLICATION_PROPERTIES);
            sysProps.verifyApplicationSettings();
            edu.harvard.med.hip.bec.DatabaseToApplicationDataLoader.loadDefinitionsFromDatabase();
            runner.setUser( AccessManager.getInstance().getUser("htaycher123","htaycher"));

            runner.setInputData(Constants.ITEM_TYPE_PLATE_LABELS, "FAE001173  FTXXG002477-3.014-1 FTXXG002559-1.014-1 FTXXG002559-2.014-1  ");
            ((BlastablDbBuilderRunner)runner).setBlastableDBName("c:\\blast_db\\VC\\genes");
            ((BlastablDbBuilderRunner)runner).setVectorLibraryName("pDONR201_cleaned.txt");
            ((BlastablDbBuilderRunner)runner).setIsACEDatabase(false);
            runner.setProcessType(Constants.PROCESS_REBUILD_BLASTABLE_DB);
            runner.run();

    }
      catch(Exception e){}
    }
    
}
