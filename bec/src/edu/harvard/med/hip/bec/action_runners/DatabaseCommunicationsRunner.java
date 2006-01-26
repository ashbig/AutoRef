/*
 * DatabaseCommunicationsRunner.java
 *
 * Created on March 14, 2005, 11:03 AM
 */

package edu.harvard.med.hip.bec.action_runners;


import java.util.*;
import java.io.*;
import sun.jdbc.rowset.*;
import java.sql.*;

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import org.apache.xerces.parsers.SAXParser;
import javax.naming.*;
import javax.sql.*;


import sun.jdbc.rowset.*;

import edu.harvard.med.hip.bec.user.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.programs.parsers.*;
import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.programs.parsers.CloneCollectionElements.*;
import edu.harvard.med.hip.bec.sampletracking.objects.*;
import edu.harvard.med.hip.bec.coreobjects.endreads.*;
import  edu.harvard.med.hip.bec.util_objects.*;
import edu.harvard.med.hip.bec.programs.assembler.*;
/**
 *
 * @author  htaycher
 */
public class DatabaseCommunicationsRunner  extends ProcessRunner
{
    private InputStream                 m_input_stream = null;
     // first step in collection processing
    private int                         m_collection_processing_first_step = IsolateTrackingEngine.PROCESS_STATUS_SUBMITTED_FOR_ER;
    
    
    public void     setInputStream(InputStream i){ m_input_stream = i;}
    public String getTitle() 
    {  
        switch (m_process_type)
        {
            case -Constants.PROCESS_ADD_NEW_VECTOR  :
            {
               return "Request for new vector submission";
            }
           case -Constants.PROCESS_SUBMIT_REFERENCE_SEQUENCES  :
           {
                return "Request for reference sequence submission";
             
           }
           case -Constants.PROCESS_SUBMIT_CLONE_COLLECTION  : 
           {
               return "Request for clone collection submission";
           }
           case -Constants.PROCESS_SUBMIT_CLONE_SEQUENCES:
           {
               return "Request for clones sequence submission";
           }
            default : return "";
         }
    }
   
   public void run_process()
    {
         Connection  conn =null;
         try
         {
                conn = DatabaseTransaction.getInstance().requestConnection();
               switch (m_process_type)
                {
                    case -Constants.PROCESS_ADD_NEW_VECTOR  :
                    {
                        VectorInfoParser SAXHandler = new VectorInfoParser();
                        SAXParser parser = new SAXParser();
                        parser.setContentHandler(SAXHandler);
                        parser.setErrorHandler(SAXHandler);
                        parser.parse(new org.xml.sax.InputSource(m_input_stream));
                        ArrayList v= SAXHandler.getBioVectors();
                        for (int count = 0; count < v.size();count++)
                        {
                            ((BioVector)v.get(count)).insert(conn);
                        }
                        conn.commit();
                        break;
                    }
                   case -Constants.PROCESS_SUBMIT_REFERENCE_SEQUENCES  :
                   {
  
                        PreparedStatement pst_insert_temp_sequenceid = conn.prepareStatement("insert into temp_sequence_id (usersequenceid, appssequenceid) values(?,?)");
                        PreparedStatement pst_check_userid= conn.prepareStatement("select usersequenceid from  temp_sequence_id where usersequenceid= ?");
                
                        RefSequenceParser SAXHandler = new RefSequenceParser();
                        SAXParser parser = new SAXParser();
                        parser.setContentHandler(SAXHandler);
                        parser.setErrorHandler(SAXHandler);
                        parser.parse(new org.xml.sax.InputSource(m_input_stream));
                        ArrayList seq= SAXHandler.getRefSequences();
                        
                        int userid = -1;RefSequence refseq = null;
                        for (int count = 0; count < seq.size();count++)
                        {
                            refseq = (RefSequence)seq.get(count);
                            userid = refseq.getId();
                            if ( ! isReferenceSequenceExists( userid , pst_check_userid) )
                            {
                                refseq.setId(-1);
                                refseq.insert(conn);
                                //insert intermidate record
                                pst_insert_temp_sequenceid.setInt(1, userid);
                                pst_insert_temp_sequenceid.setInt(2, refseq.getId());
                                DatabaseTransaction.executeUpdate(pst_insert_temp_sequenceid);
                            }
                            else
                            {
                                m_error_messages.add("Reference Sequence with id: "+userid+" has not been uploaded. Please verify sequence id.");
                            }
                        }
                        conn.commit();
                        break;
                   }
                   case -Constants.PROCESS_SUBMIT_CLONE_COLLECTION  : 
                   {
                        uploadCloneCollections(conn);
                        break;
                   }
                    case -Constants.PROCESS_SUBMIT_CLONE_SEQUENCES:
                    {
                       uploadCloneSequences(conn);
                        break;
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
            DatabaseTransaction.closeConnection(conn);
        }
    }
    
   //-------------------------------------------------------------------
   private void uploadCloneSequences(Connection conn)
   {
       //parse FASTA file
       ArrayList sequences =  readSequencesFile();// array of basesequences
       if ( sequences == null || sequences.size() < 1 )return;
       ArrayList clone_descriptions =   getCloneDescriptionsForSequenceSubmission( sequences);
       if ( clone_descriptions == null || clone_descriptions.size() < 1  )return;
       CloneDescription clone_description = null;
       Hashtable clone_strategies = new Hashtable();
       BaseSequence clone_sequence = null;
       int sequences_count = 0; int description_count = 0;
       for ( int clone_count = 0; clone_count < clone_descriptions.size(); clone_count++)
       {
          
           clone_description = (CloneDescription)clone_descriptions.get(description_count);
           if (clone_description.getCloneFinalStatus() != IsolateTrackingEngine.FINAL_STATUS_INPROCESS)
           {
               m_error_messages.add("Cannot submit sequence for clone "+clone_description.getCloneId() +
               "Clone status set to 'nonactive'.");
               continue;
           }
           clone_sequence = (BaseSequence) sequences.get(sequences_count);
           
            int res = clone_description.getCloneId() - clone_sequence.getId();
            if( res == 0)
            {
                sequences_count++; description_count++;
            }
            else if ( res > 0 )
            {
               sequences_count++; continue;
            }
           
           
           try
           {
                // check sequence quality: reject any sequence that containes not N sequence amb.
               if ( !edu.harvard.med.hip.bec.bioutil.SequenceManipulation.isValidDNASequence (clone_sequence.getText()))
               {
                   m_error_messages.add( "Sequence for cloneid "+clone_description.getCloneId() +" is not valid cDNA sequence. Any ambiguity should be presented as 'N'.");
                   continue;
               }
                processSequence( conn,  clone_sequence, clone_description, clone_strategies);
           }
           catch(Exception e)
           {
               m_error_messages.add( "Cannot submit sequence for cloneid "+clone_description.getCloneId());
           }
       }
       
   }
   
   private ArrayList                    getCloneDescriptionsForSequenceSubmission(ArrayList sequences)
   {
        StringBuffer clones_to_process = new StringBuffer();
       ArrayList item = null;int cds_start = 0; int cds_end = 0;
       BaseSequence sequence = null;
       for ( int count = 0; count < sequences.size(); count ++)
       {
           sequence = (BaseSequence)sequences.get(count);
           clones_to_process.append( sequence.getId() ) ;
           if ( count < sequences.size() - 1)  clones_to_process.append(",");
       }
       String sql = "select flexcloneid, process_status, cloningstrategyid,  refsequenceid, iso.isolatetrackingid as isolatetrackingid  "
        + " from isolatetracking iso,  sequencingconstruct  constr , flexinfo f "
        +" where iso.PROCESS_STATUS = "+IsolateTrackingEngine.FINAL_STATUS_INPROCESS +"and constr.constructid = iso.constructid and f.isolatetrackingid=iso.isolatetrackingid "
        +" and f.flexcloneid in ("+clones_to_process.toString() +")   order by flexcloneid";
       ArrayList clone_descriptions = null;
       try
       {
           clone_descriptions = CloneDescription.getClonesDescriptions(  sql, null,  false, false, false,false, true);
       }
       catch(Exception e)           {               m_error_messages.add(e.getMessage());           }
      
       return clone_descriptions;
   }
  
   
   
   
   private ArrayList                    readSequencesFile()
   {
        ArrayList sequences =  null;
      
       try
       {
         sequences = edu.harvard.med.hip.bec.bioutil.BioFormatsFile.readFASTAFile(m_input_stream);
       }
       catch(Exception e)
       {
           m_error_messages.add("Cannot parse FASTA file");
           return sequences;
       }
       if ( sequences == null || sequences.size() < 0 ) return sequences;
       // sort array of sequenses 
       Collections.sort(sequences, new Comparator() 
       {
            public int compare(Object o1, Object o2) 
            {
                BaseSequence cl1 = (BaseSequence)o1;
                BaseSequence cl2 = (BaseSequence)o2;
                return cl1.getId() - cl2.getId();

            }
            public boolean equals(java.lang.Object obj)  {   return false;  }       } );
      return sequences;
   }
    
   
    
   
   private  void                  processSequence(Connection conn,   BaseSequence sequence,
                                    CloneDescription clone_description,
                                    Hashtable clone_strategies)throws Exception
    {
        
        // history 
        CloningStrategy cloning_strategy = null;
        cloning_strategy =(CloningStrategy) clone_strategies.get(new Integer(clone_description.getCloningStrategyId()));
        if ( cloning_strategy == null )
        {
            cloning_strategy = CloningStrategy.getById( clone_description.getCloningStrategyId() );
            if (cloning_strategy != null)
             {
                 cloning_strategy.setLinker3( BioLinker.getLinkerById( cloning_strategy.getLinker3Id() ));
                 cloning_strategy.setLinker5( BioLinker.getLinkerById( cloning_strategy.getLinker5Id() ));
                 clone_strategies.put(new Integer(clone_description.getCloningStrategyId() ),cloning_strategy);
            }
             
        }

        //get refsequence 
       RefSequence refsequence = new RefSequence( clone_description.getBecRefSequenceId());
       BioLinker linker5 = cloning_strategy.getLinker5();
        BioLinker linker3 = cloning_strategy.getLinker3();
       int cds_start = linker5.getSequence().length();
        int cds_stop = linker5.getSequence().length() +  refsequence.getCodingSequence().length();
        BaseSequence base_refsequence =  new BaseSequence(linker5.getSequence() + refsequence.getCodingSequence()+linker3.getSequence(), BaseSequence.BASE_SEQUENCE );
        base_refsequence.setId(refsequence.getId());
          
           //check coverage
          Contig contig = new Contig();
          contig.setSequence(sequence.getText().toUpperCase());
              
       int result = contig.checkForCoverage(clone_description.getCloneId(), cds_start,  cds_stop,  base_refsequence);
       submitSequence( conn,    sequence.getText(),  clone_description,  cds_start,  cds_stop,      result);  
    }
       
        
    private void            submitSequence(Connection conn,   String sequence,
                                CloneDescription clone_description, int cds_start, int cds_end,
                                int sequence_quality) throws Exception
    {
        try
        {
            CloneSequence cl_seq = new  CloneSequence( sequence, clone_description.getBecRefSequenceId());
            cl_seq.setCloneSequenceType ( BaseSequence.CLONE_SEQUENCE_TYPE_FINAL); //final\conseq\final editied
            cl_seq.setApprovedById ( m_user.getId() );//BecIDGenerator.BEC_OBJECT_ID_NOTSET= v;}
            cl_seq.setIsolatetrackingId (clone_description.getIsolateTrackingId()); //BecIDGenerator.BEC_OBJECT_ID_NOTSET= v;}
            cl_seq.setCloneSequenceStatus (BaseSequence.CLONE_SEQUENCE_STATUS_ASSEMBLED);
            cl_seq.setCdsStart(cds_start);
            cl_seq.setCdsStop(cds_end);
            cl_seq.insert(conn);

              //update isolatetracking 
             IsolateTrackingEngine.updateStatus(IsolateTrackingEngine.PROCESS_STATUS_CLONE_SEQUENCE_SUBMITED_FROM_OUTSIDE,
                                                   clone_description.getIsolateTrackingId(),  conn );
             IsolateTrackingEngine.updateAssemblyStatus(  sequence_quality,     clone_description.getIsolateTrackingId(),      conn);
             conn.commit();
        }
        catch(Exception e)
        {
            throw new BecDatabaseException("Can not insert sequence fro clone "+ clone_description.getCloneId());
        }
    }
    
    
    
    
    
    
    
    //--------------------------------------
   private void uploadCloneCollections(Connection conn)
   {
        ArrayList clone_collection= null;
        CloneCollectionParser SAXHandler = new CloneCollectionParser();
        SAXParser parser = new SAXParser();
        parser.setContentHandler(SAXHandler);
        parser.setErrorHandler(SAXHandler); 
        try
        {
            parser.parse(new org.xml.sax.InputSource(m_input_stream));
             clone_collection= SAXHandler.getCollections();
            
        }
        catch(Exception e)
        {
            m_error_messages.add("Cannot parse clone collection XML file. Please verify your data");
            return;
        }
               
        CloneCollection collection = null;
        PreparedStatement pst_check_refsequenceid = null;
        PreparedStatement pst_check_cloneid_duplicates = null;
        try
        {
          //  pst_check_cloneid_duplicates = conn.prepareStatement("select distinct flexcloneid from flexinfo where flexcloneid in (?)");
          //   pst_check_refsequenceid = conn.prepareStatement("select distinct usersequenceid from temp_sequence_id where usersequenceid in (?) order by usersequenceid");
        }
        catch(Exception e)
        {
            m_error_messages.add("Cannot create prepared statements to verify your data. Please contact development group");
            return;
        }
       ArrayList error_messages = null;
        for (int count = 0; count < clone_collection.size();count++)
        {    
            error_messages = null;
            collection = (CloneCollection) clone_collection.get(count);
            collection.verify(  error_messages);
            if ( error_messages != null && error_messages.size() > 0)
            {
                m_error_messages.addAll(error_messages);
                continue;
            }
            if ( isCloneCollectionNameExists(collection, conn)) continue;//pst_check_cloneid_duplicates ) ) continue;
           
            if ( isCloneDuplicationExists(collection, conn)) continue;//pst_check_cloneid_duplicates ) ) continue;
            // refsequence exists
            if ( !isReferenceSequencesExistsForCollection(collection,  conn)) continue;// , pst_check_refsequenceid)) continue;
            // cloning strategy  exists
            if ( !isCloningStrategyExistsForCollection(collection, conn )) continue;
            // submission 
            uploadCloneCollection( conn,  collection);
        }
   }
   
   private void                 uploadCloneCollection(Connection conn, CloneCollection collection)
   {
        int user_collection_id = collection.getId();
        Sample sample= null; 
        SampleForCloneCollection cc_sample = null;
        ConstructForCloneCollection cc_construct = null;
        Construct construct = null;
        Hashtable collection_construct_ids = new Hashtable();
        int current_constructid = -1;
       try
       {
      //create container 
            Container container = new Container( -1  , collection.getType(),  collection.getName(),  -1);
            //enter control samples
            for ( int sample_count = 0; sample_count < collection.getSamples().size(); sample_count++ )
            {
                cc_sample= (SampleForCloneCollection) collection.getSamples().get(sample_count);
                sample =  createControlSample( container.getId(), user_collection_id,  cc_sample);
                container.addSample(sample);
            }
            for ( int construct_count = 0; construct_count < collection.getConstructs().size(); construct_count++ )
            {
                cc_construct= (ConstructForCloneCollection ) collection.getConstructs().get(construct_count);
                current_constructid = uploadContsruct( cc_construct, collection_construct_ids, conn);
                for ( int sample_count = 0; sample_count < cc_construct.getSamples().size(); sample_count++ )
                {
                     cc_sample= (SampleForCloneCollection) cc_construct.getSamples().get(sample_count);
                     sample = createNotControlSample( cc_sample,  cc_construct, user_collection_id,
                                            current_constructid,container.getId());
                    
                    container.addSample(sample);
                }
        }
         //for ( int count = 0; count < container.getSamples().size(); count++)
        // {
        //     System.out.println( ((Sample)container.getSamples().get(count)).toString());
        // }
                  
        container.insert(conn);
        createProcessHistoryRecord( conn,  collection,  container);
  
        //conn.rollback();
         conn.commit();
       }
       catch(Exception e)
       {
           DatabaseTransaction.rollback(conn);
           m_error_messages.add("Cannot upload clone collection( name "+ collection.getName() + " id "+ collection.getId() +")");
       }
   }

   
   private int uploadContsruct(ConstructForCloneCollection cc_construct, 
                Hashtable collection_construct_ids, Connection conn) throws Exception
   {
        FlexInfo flex_info = null;         IsolateTrackingEngine istr = null;
        Sample sample= null; 
        SampleForCloneCollection cc_sample = null;
        Construct construct = null;
        int current_constructid = -1;
         // check if new construct in collection
        if (collection_construct_ids.containsKey(new Integer(cc_construct.getId())))
        {
           current_constructid = ((Integer) collection_construct_ids.get(new Integer(cc_construct.getId()))).intValue();
        }
                
        else
        {
            // check if construct exists in datatbase
            current_constructid = getConstructFromDatabase(  cc_construct);
            if ( current_constructid == -1)
            {
                        // create new construct 
                        construct = new Construct();
                        construct.setId(  BecIDGenerator.getID("constructid"));
                        construct.setRefSeqId( cc_construct.getRefSequenceId() );
                        construct.setFormat(cc_construct.getFormat());
                        construct.setCloningStrategyId( cc_construct.getCloningStrategyId() );
                        construct.insert(conn);
                        //add to hash eliminate creation of the same construct
                        collection_construct_ids.put(new Integer( cc_construct.getId()), new Integer(construct.getId() ));
                        current_constructid = construct.getId();
                }
            }
        return current_constructid;
                
   }
   
   private Sample     createNotControlSample(SampleForCloneCollection cc_sample, 
           ConstructForCloneCollection cc_construct,
            int user_collection_id,
            int current_constructid,
            int container_id) throws Exception
   {
        FlexInfo flex_info = new FlexInfo(  BecIDGenerator.getID("flexinfoid"),
                BecIDGenerator.BEC_OBJECT_ID_NOTSET ,
                cc_sample.getId () ,    cc_construct.getId(),
                user_collection_id, cc_construct.getRefSequenceId(), 
                cc_sample.getCloneId());
          //create  isolate tracking             
        IsolateTrackingEngine istr = new IsolateTrackingEngine();
        
        istr.setId( BecIDGenerator.getID("isolatetrackingid"));
        flex_info.setIsolateTrackingId ( istr.getId() );
        istr.setFlexInfo(flex_info);
        if (  cc_sample.getType().equalsIgnoreCase("EMPTY") )
        {
            istr.setStatus(IsolateTrackingEngine.PROCESS_STATUS_SUBMITTED_EMPTY);
            istr.setRank(IsolateTrackingEngine.RANK_NOT_APPLICABLE);
                istr.setFinalStatus( IsolateTrackingEngine.FINAL_STATUS_NOT_APPLICABLE);
        
        }
        else
        {
            istr.setStatus( m_collection_processing_first_step);
                istr.setFinalStatus( IsolateTrackingEngine.FINAL_STATUS_INPROCESS);
        
        }
        Sample sample = new Sample(  BecIDGenerator.getID("sampleid"),cc_sample.getType(), 
                             cc_sample.getPosition(), container_id);
          
        sample.setIsolaterTrackingEngine(istr);
        //link isolate tracking with sample
        istr.setSampleId( sample.getId() );
        istr.setConstructId( current_constructid );
        return sample;
   }
   
   private int getConstructFromDatabase( ConstructForCloneCollection cc_construct) throws BecDatabaseException
    {
        Construct construct = null;
          String sql ="select REFSEQUENCEID  ,FORMAT,cloningstrategyid,BECCONSTRUCTID,FLEXCONSTRUCTID "+
        " from VIEW_FLEXBECCONSTRUCT where FLEXCONSTRUCTID = "+ cc_construct.getId();
        ResultSet rs = null; 
        try
        {
            rs = DatabaseTransaction.getInstance().executeQuery(sql);
            while(rs.next())
            {
                  return rs.getInt("BECCONSTRUCTID");
             } 
            return -1;
            
        }
        catch (Exception sqlE)
        {
            throw new BecDatabaseException("Error occured while restoring construct with id "+cc_construct.getId()+"\n"+sqlE+"\nSQL: "+sql);
        } 
        finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
   }
       
  
   
 
   private Sample                 createControlSample(int container_id,
                        int user_collection_id, 
                        SampleForCloneCollection cc_sample)throws Exception
   {
         FlexInfo flex_info = null;        
         IsolateTrackingEngine istr = null;
         Sample sample= null; 
        
     
  //create container 
            flex_info = new FlexInfo( BecIDGenerator.getID("flexinfoid"), BecIDGenerator.BEC_OBJECT_ID_NOTSET ,
            cc_sample.getId () , -1,  user_collection_id,  -1, 0);
            //create  isolate tracking             
            istr = new IsolateTrackingEngine();
            istr.setId( BecIDGenerator.getID("isolatetrackingid"));
            istr.setStatus(IsolateTrackingEngine.PROCESS_STATUS_SUBMITTED_EMPTY);
            istr.setRank(IsolateTrackingEngine.RANK_NOT_APPLICABLE);
            istr.setFinalStatus( IsolateTrackingEngine.FINAL_STATUS_NOT_APPLICABLE);
            istr.setFlexInfo(flex_info);
            //link flex info with isolate tracking 
            flex_info.setIsolateTrackingId ( istr.getId() );
            sample = new Sample( BecIDGenerator.getID("sampleid"), cc_sample.getType(), 
                                 cc_sample.getPosition(), container_id);

            sample.setIsolaterTrackingEngine(istr);
            //link isolate tracking with sample
            istr.setSampleId( sample.getId() );
            return sample;
        
   }


private boolean isCloneCollectionNameExists(CloneCollection collection, Connection conn)
   {
        try
        {
           // String sql =  "select  *  from containerheader where label ="+ collection.getName();
            String sql =  "select  *  from containerheader where (Upper(label) like Upper('"+ collection.getName()+"')";

            CachedRowSet rs =  DatabaseTransaction.executeQuery(sql, conn);
            if( rs.next() )       
            {
                  m_error_messages.add(" Clone collection (name "+ collection.getName() +" id "+ collection.getId() +
                    " cannot be uploaded into ACE: clone collection with this name already exists. Please verify your data");
         
                   return false;
            }
            return true;
        }
        catch(Exception e)
        {
            m_error_messages.add(" Clone collection (name "+ collection.getName() +" id "+ collection.getId() +
                    " cannot be uploaded into ACE: cannot verify collection name.");
            return false;
        }
   }
   

   private boolean              isCloningStrategyExistsForCollection(CloneCollection collection, Connection conn)
   {
        ArrayList constructs = collection.getConstructs();
        ConstructForCloneCollection construct = null;
        ArrayList cloning_strategy_id = new ArrayList();
        int cloning_strategy_ids_count = 0; int db_count = 0;
        StringBuffer ids = new StringBuffer();
        try
        {
           for (int count = 0; count < constructs.size(); count++)
           {
               construct = (ConstructForCloneCollection) constructs.get(count);
               if ( !cloning_strategy_id.contains( new Integer(construct.getCloningStrategyId()) ))
               {
                   cloning_strategy_id.add( new Integer(construct.getCloningStrategyId()));
                   cloning_strategy_ids_count++;
                   ids.append( construct.getCloningStrategyId() +",");
               }
               
            }
             
            String sql =  ids.toString();
            sql = sql.substring(0, sql.length() - 1);
            sql = "select  strategyid from cloningstrategy where strategyid in ("+sql+")";
            CachedRowSet rs =  DatabaseTransaction.executeQuery(sql, conn);
            while( rs.next() )       
            {
                  db_count ++;
            }
            if (   cloning_strategy_ids_count != db_count  )
            {
                 m_error_messages.add(" Clone collection (name "+ collection.getName() +" id "+ collection.getId() +
                    " cannot be uploaded into ACE: problem with cloning strategy id. Please verify your data");
                return false;
            }
            return true;
        }
        catch(Exception e)
        {
            m_error_messages.add(" Clone collection (name "+ collection.getName() +" id "+ collection.getId() +
                    " cannot be uploaded into ACE: cannot verify cloning strategy ids in ACE. Please verify your data");
            return false;
        }
   }
   
   
   private boolean          isReferenceSequencesExistsForCollection(CloneCollection collection, 
                           Connection conn)// PreparedStatement pst_check_refsequenceid)
   {
        ArrayList constructs = collection.getConstructs();
        ConstructForCloneCollection construct = null;
        int[] refsequenceids = new int[constructs.size()];
        StringBuffer ids = new StringBuffer();
        try
        {
           for (int count = 0; count < constructs.size(); count++)
           {
               construct = (ConstructForCloneCollection) constructs.get(count);
               refsequenceids[count] = construct.getRefSequenceId();
               ids.append( construct.getRefSequenceId());
               if ( count != constructs.size() - 1) ids.append(",");
           }
            Arrays.sort( refsequenceids);
            String sql =  "select distinct usersequenceid from temp_sequence_id where usersequenceid in ("+ids.toString()+") order by usersequenceid";
            //pst_check_refsequenceid.setString(1, sql.toString());
            //CachedRowSet rs =  DatabaseTransaction.executeQuery(pst_check_refsequenceid);
          CachedRowSet rs =  DatabaseTransaction.executeQuery(sql, conn);
          
            int id = -1;int count = 0;
            
            while( rs.next() )       
            {
                id = rs.getInt("usersequenceid");
                if ( id != refsequenceids[count++]) 
                {
                     m_error_messages.add(" Clone collection (name "+ collection.getName() +" id "+ collection.getId() +
                    " cannot be uploaded into ACE:  reference sequences do not exists in ACE. Please verify your data");
                      return false;
                }
            }
            if ( id == -1 ) return false;
            return true;
        }
        catch(Exception e)
        {
            m_error_messages.add(" Clone collection (name "+ collection.getName() +" id "+ collection.getId() +
                    " cannot be uploaded into ACE: cannot verify reference sequences in ACE. Please verify your data");
            return false;
        }
   }
   private boolean          isCloneDuplicationExists(CloneCollection collection, Connection conn)//PreparedStatement pst_check_cloneid_duplicates)
   {
        ArrayList constructs = collection.getConstructs();
        String result = null;
        ConstructForCloneCollection construct = null;SampleForCloneCollection sample = null;
        StringBuffer ids = new StringBuffer();String sql = null;
       for (int count = 0; count < constructs.size(); count++)
       {
           construct = (ConstructForCloneCollection) constructs.get(count);
           for (int scount = 0; scount < construct.getSamples().size(); scount++)
           {
                sample = (SampleForCloneCollection) construct.getSamples().get(scount);
                ids.append( sample.getCloneId());
                ids.append(",");
           }
       }
        sql = ids.toString();
        sql = sql.substring(0, sql.length() - 1);
        try
        {
         //   pst_check_cloneid_duplicates.setString(1, sql );
            sql="select distinct flexcloneid from flexinfo where flexcloneid in ("+sql+")";
            CachedRowSet rs =  DatabaseTransaction. executeQuery(sql, conn);
          //  CachedRowSet rs =  DatabaseTransaction.executeQuery(pst_check_cloneid_duplicates);
           
            while( rs.next() )       
            {
                result += " "+rs.getInt("flexcloneid");
            }
            if ( result != null )
            {  m_error_messages.add(" Clone collection (name "+ collection.getName() +" id "+ collection.getId() +
                    " cannot be uploaded into ACE: clone id duplication found in ACE(check "+result+" clone ids). Please verify your data");
                return true;
            }
            else return false;
        }
        catch(Exception e)
        {
            m_error_messages.add(" Clone collection (name "+ collection.getName() +" id "+ collection.getId() +
                    " cannot be uploaded into ACE: cannot verify clone id duplication in ACE. Please verify your data");
            return true;
        }

   }
   
   
   private void createProcessHistoryRecord(Connection conn, CloneCollection collection,
   Container container)throws BecDatabaseException
   {
        ArrayList processes = new ArrayList();
        try
        {
            Request actionrequest = new Request(BecIDGenerator.BEC_OBJECT_ID_NOTSET,
                                        new java.util.Date(),
                                        m_user.getId(),
                                        processes,
                                        Constants.TYPE_OBJECTS);
              //create specs array for the process
            ArrayList specids = new ArrayList();
            isOneCloningStarategyForCollection( specids , collection);
                  //  specids.add(new Integer(m_reverse_primerid));
                    // Process object create
                ProcessExecution process = new ProcessExecution( BecIDGenerator.BEC_OBJECT_ID_NOTSET,
                                            ProcessDefinition.ProcessIdFromProcessName(ProcessDefinition.RUN_UPLOAD_PLATE),
                                            actionrequest.getId(),
                                            specids,
                                            Constants.TYPE_ID) ;
            processes.add(process);
            actionrequest.insert(conn);
            process.insertConnectorToSpec(conn, Spec.CLONINGSTRATEGY_SPEC_INT);

            String sql = "insert into process_object (processid,objectid,objecttype) values("+process.getId()+","+container.getId()+","+Constants.PROCESS_OBJECT_TYPE_CONTAINER+")";
            DatabaseTransaction.executeUpdate(sql, conn);
        }
        catch(Exception e)
        {
            throw new BecDatabaseException("Cannot create history record for the process.");
        }
      
   }
   
   private boolean  isOneCloningStarategyForCollection( ArrayList specids , CloneCollection collection)
   {
       int current_cloning_strategy_id = 0;
       ConstructForCloneCollection cc_construct = null;
        for ( int construct_count = 0; construct_count < collection.getConstructs().size(); construct_count++ )
        {
             cc_construct= (ConstructForCloneCollection ) collection.getConstructs().get(construct_count);
             if ( construct_count == 0) 
             {
                 current_cloning_strategy_id = cc_construct.getCloningStrategyId();
                 specids.add( new Integer(current_cloning_strategy_id));
                 continue;
             }
             if ( current_cloning_strategy_id != cc_construct.getCloningStrategyId()) 
             {
                 specids= new ArrayList();
                 return false;
             }
        }
        return true;
       
   }
   //-------------------------
   private boolean          isReferenceSequenceExists(  int userid , PreparedStatement pst_check_userid)
   {
       try
       {
           pst_check_userid.setInt(1, userid);
           CachedRowSet rs =  DatabaseTransaction.executeQuery(pst_check_userid);
           if ( rs.next() )            return true;
           return false;
       }
       catch(Exception e)
       {
           m_error_messages.add("Cannot verify reference sequence id " + userid);
            return false;
       }
   }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        User user  = null;
        try
        {

            user = AccessManager.getInstance().getUser("htaycher123","htaycher");
            BecProperties sysProps =  BecProperties.getInstance( BecProperties.PATH);
            sysProps.verifyApplicationSettings();
            DatabaseToApplicationDataLoader.loadDefinitionsFromDatabase();
            DatabaseCommunicationsRunner runner = new DatabaseCommunicationsRunner();
            runner.setUser(user);
            File f = new File("C:\\test.txt");
            InputStream input = new FileInputStream(f);
            runner.setInputStream(input);
            runner.setProcessType( -Constants.PROCESS_SUBMIT_CLONE_SEQUENCES);
            runner.run();
      
        }
        catch(Exception e){}
        System.exit(0);
    }
    
    
    
}
