/*
 * PlateUploader.java
 *
 * Created on April 9, 2003, 2:28 PM
 *class takes in list of plate names from FLEX and
 *trnsfers their info into BEC
 */

package edu.harvard.med.hip.bec.modules;


import edu.harvard.med.hip.bec.coreobjects.sequence.*;

import edu.harvard.med.hip.bec.database.*;
import  edu.harvard.med.hip.bec.util.*;
import  edu.harvard.med.hip.bec.bioutil.*;
import edu.harvard.med.hip.bec.Constants;
import edu.harvard.med.hip.bec.sampletracking.objects.*;
import edu.harvard.med.hip.bec.coreobjects.endreads.*;
import sun.jdbc.rowset.*;
import java.util.*;
import java.sql.Date;
import java.io.*;
import java.math.BigDecimal;
import java.sql.*;

import edu.harvard.med.hip.flex.core.FlexSequence;



/**
 *
 * @author  htaycher
 */
public class PlateUploader
{
    public static final int PLATE_NAMES = 1;
    public static final int PLATE_IDS = 2;
    
    private ArrayList m_plate_names = null;
    private int       m_plate_sabmitted_for = -1;//initial status for isolate ranking
    private int       m_vectorid = -1;
    private int       m_linker3_id = -1;
    private int       m_linker5_id = -1;
    private int       m_array_type = -1;
    
    private ArrayList m_error_messages = null;
    
    
   
    /** Creates a new instance of PlateUploader */
    public PlateUploader(ArrayList plate_names, int mode, int vectorid, 
                        int l3id, int l5id, int isltrstatus)
    {
        m_plate_names = plate_names;
        m_array_type = mode;
        m_plate_sabmitted_for = isltrstatus;//initial status for isolate ranking
        m_vectorid = vectorid;
        m_linker3_id = l3id;
        m_linker5_id = l5id;
        m_error_messages = new ArrayList();
    }
    
    
    public ArrayList getErrors(){ return m_error_messages;}
    
    public void  upload()
    {
     
        //get all reference sequences from bec: to prevent multipal upload of the same sequence
        //Hashtable refsequences = getAllRefSequences(m_bec_connection);
         Connection  flex_connection = null;
         Connection  bec_connection = null;
        try
        {
            // m_flex_connection = DatabaseTransaction.getInstance(DatabaseTransaction.FLEX_url , DatabaseTransaction.FLEX_username, DatabaseTransaction.FLEX_password).requestConnection();
            // m_bec_connection = DatabaseTransaction.getInstance(DatabaseTransaction.BEC_url, DatabaseTransaction.BEC_username, DatabaseTransaction.BEC_password).requestConnection();
        }
        catch(Exception e)
        {
            m_error_messages.add("Cannot open connection to database");
          
        }
        for (int count = 0 ; count < m_plate_names.size(); count++)
        {
            uploadPlate( (String) m_plate_names.get(count) , flex_connection, bec_connection);
           
        }
        try
        {
            flex_connection.close();
            flex_connection.close();
     
        }
        catch(Exception e)
        {
            m_error_messages.add("Cannot close connection to database");
           
        }
    }
    
    //function uploads data for one plate into BEC
    public   void uploadPlate( String platename, Connection flex_connection, 
    Connection bec_connection)
    {
        //hash for flex sequences
        try
        {
              Hashtable flex_sequence_ids = new Hashtable();
              // get all needed info from flex

              // get all related sample info from flex
              ArrayList samples = getSampleInfoFromFLEX( platename,  flex_sequence_ids,  flex_connection);
              // check for sequence duplicates in BEC
              checkForSequencePresenceInBEC(flex_sequence_ids,bec_connection,flex_connection);
              //get new for BEC sequences from FLEX
               getMissingFlexSequences(flex_sequence_ids, flex_connection,bec_connection);

               insertPlateInfo(platename, samples, flex_sequence_ids,bec_connection);

               //commit plate info into bec
               bec_connection.commit();
        }
        catch(Exception e)
        {
            m_error_messages.add(e.getMessage());
            try
            {
                bec_connection.rollback();
            }catch(Exception ee){}
        }
        
    }
    
    
    //*******************************************************************************
    
    // the following function get plate related info from FLEX
    
    // finds construct ids for the plate samples
    private ArrayList getSampleInfoFromFLEX(String platename, Hashtable flex_sequence_ids, Connection flex_connection) throws BecDatabaseException
    {
        ArrayList samples = new ArrayList();
      
        int plate_id  = -1;
        SampleInfo sample ;
        String sql = "select sampleid, sampletype, containerid,"+
        " containerposition as position, cd.constructtype as format, cd.constructid as constructid, cd.sequenceid as sequenceid, cloneid"+
        " from clonesequencing c, sample s, constructdesign cd"+
        " where s.containerid = "+
        " (select distinct sequencingcontainerid"+
        " from clonesequencing"+
        " where sequencingcontainerlabel='" + platename +"')"+
        " and c.sequencingsampleid(+)=s.sampleid"+
        " and s.constructid=cd.constructid(+)"+
        " order by containerposition";
 
        ResultSet rs = null;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql,flex_connection);
            
            while(rs.next())
            {
                sample = new SampleInfo();
                sample.setId ( rs.getInt("sampleid") );
                sample.setPlateId( rs.getInt("containerid") );
                sample.setPosition ( rs.getInt("position") );
                
                sample.setType ( rs.getString("sampletype") );
                //not control
                if ( !sample.isControl() )
                {
                    sample.setConstructId ( rs.getInt("constructid") );
                    sample.setSequenceId (rs.getInt("sequenceid") );
                    sample.setFormat (rs.getString("format") );
                    
                    flex_sequence_ids.put(new Integer(sample.getSequenceId () ), null);
                }
                //not empty sample
                if ( !sample.isEmpty() )
                {
                    sample.setCloneId (rs.getInt("cloneid"));
                }
                samples.add(sample);
            }
        } catch (Exception sqlE)
        {
            throw new BecDatabaseException("Error occured while getting construct ids for the plate: "+platename+"\n"+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
        return samples;
    }
    
    //function checks whether these sequences already in bec
    //if yes it stores coinsisteant BEC sequence id in hash
    private void checkForSequencePresenceInBEC(Hashtable flex_sequence_ids, 
                                                Connection bec_connection, 
                                                Connection flex_connection)
                                                throws BecDatabaseException
    {
        //construct query string
        StringBuffer stb = new StringBuffer();
        for (Enumeration e = flex_sequence_ids.keys(); e.hasMoreElements() ;)
        {
            stb.append( ( (Integer)e.nextElement()).intValue() );
            if ( e.hasMoreElements() )stb.append(",");
        }
       
          String sql = "select distinct flexsequenceid, refsequenceid "+
            " from flexinfo, isolatetracking iso, sequencingconstruct sc "+
            " where flexinfo.isolatetrackingid=iso.isolatetrackingid and iso.constructid=sc.constructid "+
            " and flexinfo.flexsequenceid in "+
            "(select  flexsequenceid from flexinfo where flexsequenceid in ("+stb.toString() +"))";
        
        //get data from database
        ResultSet rs = null;
         try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql,bec_connection);
            
            while(rs.next())
            {
                flex_sequence_ids.put( new Integer(rs.getInt("flexsequenceid") ),
                                        new Integer(rs.getInt("refsequenceid")));
            }
           
        } catch (Exception sqlE)
        {
            throw new BecDatabaseException("Error occured while getting bec sequence information"+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
    
    
    private synchronized void getMissingFlexSequences(Hashtable flex_sequences,
                                        Connection flex_connection,
                                        Connection bec_connection)
                                        throws Exception
    {
        for (Enumeration e = flex_sequences.keys(); e.hasMoreElements() ;)
        {
            Object key = e.nextElement();
            if ( flex_sequences.get(key) == null)
            { 
                RefSequence fl = getRefSequenceFormFlex( ((Integer) key).intValue(), flex_connection);
                fl.insert(bec_connection);
                bec_connection.commit();
                flex_sequences.put(key, fl);
            }
        }
    }
    
    
    
    private RefSequence getRefSequenceFormFlex(int id, Connection flex_connection)throws BecDatabaseException
    {
         
        String sql = null; String sequencetext = "";
        RefSequence ref_sequence ;ResultSet rs = null;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            //get sequence
            sql = "select * from sequencetext where sequenceid="+id+" order by sequenceorder";
            rs = t.executeQuery(sql, flex_connection);
            while(rs.next())
            {
                sequencetext += rs.getString("SEQUENCETEXT");
            }
            ref_sequence = new RefSequence(sequencetext);
            
            //get sequence characteristics
           sql = "select s.genusspecies as species,s.cdsstart as cdsstart,"+
            "s.cdsstop as cdsstop,s.gccontent as gccontent,"+
            "from flexsequence s where s.sequenceid="+id;
           rs = t.executeQuery(sql,flex_connection);
       
            while(rs.next())
            {
                String species = rs.getString("SPECIES");
                if (species.equalsIgnoreCase("Homo sapiens"))
                {
                    ref_sequence.setSpecies(RefSequence.SPECIES_HUMAN);
                }
                else if (species.equalsIgnoreCase("Saccharomyces cerevisiae"))
                {
                    ref_sequence.setSpecies(RefSequence.SPECIES_YEAST);
                }
                else if (species.equalsIgnoreCase("Pseudomonas aeruginosa"))
                {
                    ref_sequence.setSpecies(RefSequence.SPECIES_PSEUDOMONAS);
                }
                ref_sequence.setCdsStart(rs.getInt("CDSSTART") );
                ref_sequence.setCdsStop( rs.getInt("CDSSTOP") );
                ref_sequence.setGCcontent(rs.getInt("GCCONTENT"));
            }

        // public info stuff
                sql = "select nametype, namevalue,nameurl,description from name where sequenceid="+id;

                rs = t.executeQuery(sql,flex_connection);
                ArrayList public_info = new ArrayList();
                
                while(rs.next())
                {
                    public_info.add(new PublicInfoItem(rs.getString("nametype"),
                                                       rs.getString("namevalue"),
                                                       rs.getString("nameurl"),
                                                       rs.getString("description")));
                }
                if (public_info != null)
                    ref_sequence.setPublicInfo(public_info);
                
                return ref_sequence;
              
            } catch (Exception sqlE)
            {
                throw new BecDatabaseException("Error occured while restoring sequence with id "+id+"\n"+sqlE+"\nSQL: "+sql);
            } finally
            {
                DatabaseTransaction.closeResultSet(rs);
            }
    }
    
    
    //*******************************************************************************
    
    
    
    //*******************************************************************************
    //the following functions create new plate in bec
    
    
    private   synchronized void insertPlateInfo(String platename, ArrayList samples, 
    Hashtable flex_sequence_ids,Connection bec_connection)
     throws BecDatabaseException
    {
        //create container in bec
       
        Container container = new Container(BecIDGenerator.BEC_OBJECT_ID_NOTSET,
                                            Container.TYPE_SEQUENCING_CONTAINER,
                                            platename,  -1);
    
        //create samples and flex info and isolatetracking (optional)
         FlexInfo flex_info;
         IsolateTrackingEngine istr;
         Sample sample; SampleInfo sample_info; 
        for (int sample_count = 0; sample_count < samples.size(); sample_count++)
        {    
            sample_info = ( SampleInfo )samples.get(sample_count);
            //create flex info record
            flex_info = new FlexInfo();
            
            flex_info.setFlexSampleId ( sample_info.getId () );
            flex_info.setFlexConstructId ( sample_info.getConstructId());
            flex_info.setFlexPlateId ( sample_info.getPlateId() );
            flex_info.setFlexSequenceId ( sample_info. getSequenceId ());
            flex_info.setFlexCloneId ( sample_info.getCloneId() );
            
   //create  isolate tracking             
            istr = new IsolateTrackingEngine();
            istr.setStatus( m_plate_sabmitted_for);
            istr.setFlexInfo(flex_info);
    //link flex info with isolate tracking 
            flex_info.setIsolateTrackingId ( istr.getId() );
            
    // create construct 
            int constructid = getConstructId( sample_info.getConstructId(), 
                                    sample_info.getFormat(), 
                                    sample_info.getSequenceId(),
                                    flex_sequence_ids, 
                                    bec_connection);
            istr.setConstructId( constructid );
            
            sample = new Sample(BecIDGenerator.BEC_OBJECT_ID_NOTSET, 
                                        sample_info.getType(), 
                                        sample_info.getPosition(),   
                                        container.getId());
          
            sample.setIsolaterTracking(istr);
     //link isolate tracking with sample
            istr.setSampleId( sample.getId() );
            
            container.addSample(sample);
        }
         container.insert(bec_connection);
        
        //construct
    }
    
    
    
    
    
    
    
    
    private int  getConstructId( int flexconstructId, 
                                    int constructformat, 
                                    int flex_sequenceid,
                                    Hashtable flex_sequence_ids,
                                    Connection bec_connection)
                                     throws BecDatabaseException
    {
        Construct construct;
        int construct_id = -1;
        //check wether BEC database contains this construct: we relay to user
        construct = Construct.getConstructByFlexConstructId(flexconstructId);
        if (construct != null)
        {
            //check if construct has the same vector & linker info
            if (construct.getVectorId() == m_linker5_id && 
                construct.getLinker3Id()==  m_linker3_id &&
                construct.getLinker5Id() ==    m_linker5_id )
            {
               return construct.getId();
            }
        }
        // create new construct
        construct = new Construct();
        //get bec sequence id
        int sequence_id = -1;
        Object seq = flex_sequence_ids.get(new Integer(flex_sequenceid ));
        if ( seq instanceof Integer)
        {
            sequence_id = ((Integer)seq).intValue();
        }
        else if (seq instanceof RefSequence)
        {
            sequence_id = ((RefSequence) seq).getId();
        }
        construct.setRefSeqId( sequence_id );
        construct.setFormat(constructformat);
        construct.setLinker3Id(m_linker3_id);
        construct.setVectorId(m_linker5_id);
        construct.setLinker5Id(m_linker5_id);
        construct.insert(bec_connection);
        return construct_id;
    }
    
    
    
    
    
    //*******************************************************************************
    
    
    
    
    
    
    
   
    
   // constructid / sequenceid / constructtype / sample array / containerid
    // sample - sampleid / position / sampletype / cloneid
    class SampleInfo
    {
        private int i_sampleid  = -1;
        private int i_plateid = -1;
        private int i_position = -1;
        private int i_cloneid = -1;
        private String i_type = null;
        
        private int i_constructid = -1;
        private int i_sequenceid = -1;
        private int i_format = -1;
        
        public SampleInfo(){}
        
        public int getId (){ return i_sampleid   ;}
        public int getPlateId (){ return i_plateid;}
        public int getPosition (){ return i_position  ;}
        public int getCloneId (){ return i_cloneid  ;}
        public String getType (){ return i_type  ;}

        public int getConstructId (){ return i_constructid  ;}
        public int getSequenceId (){ return i_sequenceid  ;}
        public int getFormat (){ return i_format  ;}
        
        public void setId (int v){  i_sampleid   = v;}
        public void setPlateId (int v){  i_plateid = v;}
        public void setPosition (int v){  i_position  = v;}
        public void setCloneId (int v){  i_cloneid  = v;}
        public void setType (String v){  i_type  = v;}

        public void setConstructId (int v){  i_constructid  = v;}
        public void setSequenceId (int v){  i_sequenceid  = v;}
        public void setFormat (int v){  i_format  = v;}
        public void setFormat (String  v)
        {  
            if (v.equalsIgnoreCase("CLOSED") )
            {
                i_format = Construct.FORMAT_CLOSE;
            }
            else
                i_format = Construct.FORMAT_OPEN;
        }
        
        public boolean isEmpty()    {        return (i_type.equals("EMPTY"));    }
        public boolean isControl()    {        return (i_type.startsWith("CONTROL"));} 
    }
    
    class UploadedRefSequence
    {
        private int i_flex_ref_seq_id = -1;
        private int i_bec_ref_seq_id = -1;
        
        public UploadedRefSequence(int flexid, int becid)
        {
            i_flex_ref_seq_id = flexid;
            i_bec_ref_seq_id = becid;
        }
        
        public int  getFlexRefseqid()        { return i_flex_ref_seq_id;}
        public int  getBecRefseqid()        { return i_bec_ref_seq_id;}
    }
}
