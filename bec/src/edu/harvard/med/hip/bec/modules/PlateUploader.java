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
    private int       m_array_type = -1;
    
    
    
   
    /** Creates a new instance of PlateUploader */
    public PlateUploader(ArrayList plate_names, int mode)
    {
        m_plate_names = plate_names;
        m_array_type = mode;
        
    }
    
    
    
    public ArrayList  upload()
    {
        ArrayList errors = new ArrayList();
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
            errors.add("Cannot open connection to database");
            return errors;
        }
        for (int count = 0 ; count < m_plate_names.size(); count++)
        {
            try
            {
                uploadPlate( (String) m_plate_names.get(count) , flex_connection, bec_connection);
            }
            catch(BecDatabaseException e)
            {
                errors.add(e.getMessage());
            }
        }
        try
        {
            flex_connection.close();
            flex_connection.close();
            return errors;
        }
        catch(Exception e)
        {
            errors.add("Cannot close connection to database");
            return errors;
        }
    }
    
    //function uploads data for one plate into BEC
    public void uploadPlate( String platename, Connection flex_connection, Connection bec_connection)throws BecDatabaseException
    {
        //hash for flex sequences
          Hashtable flex_sequence_ids = new Hashtable();
          // get all related sample info from flex
          ArrayList samples = getSampleInfoFromFLEX( platename,  flex_sequence_ids,  flex_connection);
          // check for sequence duplicates in BEC
          checkForSequencePresenceInBEC(flex_sequence_ids,bec_connection,flex_connection);
          //get new for BEC sequences from FLEX
           getMissingFlexSequences(flex_sequence_ids, flex_connection);
        
    }
    
    // finds construct ids for the plate samples
    private ArrayList getSampleInfoFromFLEX(String platename, Hashtable flex_sequence_ids, Connection flex_connection) throws BecDatabaseException
    {
        ArrayList samples = new ArrayList();
      
        int plate_id  = -1;
        SampleInfo sample ;
        String sql = "select   from view_flexsampleinfo " +
        " where label ='" + platename +"' ";
        ResultSet rs = null;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql,flex_connection);
            
            while(rs.next())
            {
                sample = new SampleInfo();
                sample.setId ( rs.getInt("sampleid") );
                sample.setPosition ( rs.getInt("position") );
                
                sample.setType ( rs.getString("sampletype") );
                //control
                if (sample.getType().indexOf("CONTROL_") == -1)
                {
                    sample.setConstructId ( rs.getInt("constructid") );
                    sample.setSequenceId (rs.getInt("sequenceid") );
                    sample.setFormat (rs.getString("formatd") );
                    
                    flex_sequence_ids.put(new Integer(sample.getSequenceId () ), null);
                }
                //empty sample
                if ( !sample.getType().equalsIgnoreCase("EMPTY") )
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
        String sql = "select flexsequenceid, sequenceid from view_flexbecsequenceid where flexsequenceid in (";
        StringBuffer stb = new StringBuffer();
        for (Enumeration e = flex_sequence_ids.keys(); e.hasMoreElements() ;)
        {
            stb.append( ( (Integer)e.nextElement()).intValue() );
            if ( e.hasMoreElements() )stb.append(",");
        }
        sql += stb.toString() +")";
        
        //get data from database
        ResultSet rs = null;
         try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql,bec_connection);
            
            while(rs.next())
            {
                flex_sequence_ids.put( new Integer(rs.getInt("flexsequenceid") ),
                                        new Integer(rs.getInt("sequenceid")));
            }
           
        } catch (Exception sqlE)
        {
            throw new BecDatabaseException("Error occured while getting bec sequence information"+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
    
    
    private void getMissingFlexSequences(Hashtable flex_sequences,
                                        Connection flex_connection)
                                        throws BecDatabaseException
    {
        for (Enumeration e = flex_sequences.keys(); e.hasMoreElements() ;)
        {
            Object key = e.nextElement();
            if ( flex_sequences.get(key) == null)
            { 
                RefSequence fl = getRefSequenceFormFlex( ((Integer) key).intValue(), flex_connection);
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
    
    
    
    
    
    
    
    
    
    
    
    
    /*
     
    private   Container findContainer(String plate_param, Connection conn) throws     BecDatabaseException
    {
        String sql = null;Container newBecContainer = null;
        if ( m_array_type == PLATE_NAMES)
        {
            sql = "select containerid , containertype , label" +
            "from containerheader  where label = '"+ plate_param+"'";
        }
        else if (m_array_type == PLATE_IDS)
        {
            sql = "select containerid , containertype , label" +
            "from containerheader  where containerid = '"+ plate_param+"'";
        }
        if (sql == null) return null; //not proper call was made
        ArrayList samples = null;
        ResultSet rs = null;
        try
        {
         //   rs = DatabaseTransaction.getInstance().executeQuery(sql, conn);
            while(rs.next())
            {
                int id = rs.getInt("CONTAINERID");
                String label = rs.getString("LABEL");
                newBecContainer = new Container( -1, Container.TYPE_SEQUENCING_CONTAINER,   label, Container.STATUS_SUBMITTED);
                newBecContainer.setSamples( restoreFLEXSample(id, conn) );
            }
            return newBecContainer;
        }
        catch (NullPointerException ex)
        {
            throw new BecDatabaseException("Error occured while initializing container with plate param: "+plate_param+"\n"+ex.getMessage());
        }
        catch (SQLException sqlE)
        {
            throw new BecDatabaseException("Error occured while initializing container from plate param: "+plate_param+"\n"+"\nSQL: "+sqlE);
        }
        finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
     
    }
     
     
    //get samples from FLEX
    private ArrayList restoreFLEXSample(int id, Connection conn) throws BecDatabaseException
    {
        String sql =  "select containerid , containertype , label" +
        "from containerheader  where label = '"+ id+"'";
     
        ArrayList samples = new ArrayList();
        ResultSet rs = null;
        try
        {
            //rs = DatabaseTransaction.getInstance().executeQuery(sql, conn);
            while(rs.next())
            {
     
            }
            return samples;
        }
        catch (Exception sqlE)
        {
            throw new BecDatabaseException("Error occured while initializing container from plate parameter: "+id+"\n"+"\nSQL: "+sqlE);
        }
        finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
     
    }
     
     
    //             prevent uploading of the same sequence several times
     
    //get all refseq from BEC so that we won't upload the same sequence several times
    private Hashtable   getAllRefSequences (Connection conn)throws BecDatabaseException
    {
        Hashtable refseq = new Hashtable();
         String sql = "select i.flexsequenceid as flexsequenceid, c.refsequenceid as becsequenceid "+
        "from  construct c, isolatetracking i " +
        "where c.constructid = i.constructid   ";
     
        CachedRowSet crs = null;
        int refid = -1; int becid = -1;
        try
        {
           // crs = DatabaseTransaction.getInstance().executeQuery(sql,m_bec_connection);
     
            while(crs.next())
            {
                refid = crs.getInt("refSEQUENCEID");
                becid = crs.getInt("becsequenceid");
                refseq.put( new Integer(refid), new UploadedRefSequence( refid, becid));
            }
            return refseq;
     
        } catch (Exception e)
        {
            throw new  BecDatabaseException("Can not get refsequences from database");
     
        } finally
        {
            DatabaseTransaction.closeResultSet(crs);
        }
    }
     */
    
   // constructid / sequenceid / constructtype / sample array / containerid
    // sample - sampleid / position / sampletype / cloneid
    class SampleInfo
    {
        private int i_sampleid  = -1;
        private int i_position = -1;
        private int i_cloneid = -1;
        private String i_type = null;
        
        private int i_constructid = -1;
        private int i_sequenceid = -1;
        private int i_format = -1;
        
        public SampleInfo(){}
        public int getId (){ return i_sampleid   ;}
        public int getPosition (){ return i_position  ;}
        public int getCloneId (){ return i_cloneid  ;}
        public String getType (){ return i_type  ;}

        public int getConstructId (){ return i_constructid  ;}
        public int getSequenceId (){ return i_sequenceid  ;}
        public int getFormat (){ return i_format  ;}
        
        public void setId (int v){  i_sampleid   = v;}
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
        
        public int  getFlexRefseqid()
        { return i_flex_ref_seq_id;}
        public int  getBecRefseqid()
        { return i_bec_ref_seq_id;}
    }
}
