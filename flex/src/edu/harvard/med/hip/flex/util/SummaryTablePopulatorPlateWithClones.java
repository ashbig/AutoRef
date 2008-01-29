/*
 * SummaryTablePopulatorPlateWithClones.java
 *
 * Created on January 24, 2008, 3:43 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.util;

import java.util.*;
import java.io.*;
import java.sql.*;
import javax.mail.*;
import javax.mail.internet.*;

import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.util.Mailer;
import edu.harvard.med.hip.flex.core.*;

import edu.harvard.med.hip.flex.special_projects.UpdateClonename;
import edu.harvard.med.hip.flex.process.SeqContainerMapper;
import edu.harvard.med.hip.flex.user.*;
import edu.harvard.med.hip.flex.database.*;

/**
 *
 * @author htaycher
 */
public class SummaryTablePopulatorPlateWithClones implements Runnable 
{
    private  List       m_seq_containers = null;
    private String      m_researcherbarcode = null;
    private boolean     m_isMappingFileRequested = false;
    private  List       m_gl_containers = null;
    private  String     m_storage_type_to_add = null;
    private  String     m_storage_form = null;
    private  List       m_original_containers = null;
    /** Creates a new instance of SummaryTablePopulatorPlateWithClones */
    public SummaryTablePopulatorPlateWithClones() {
    }
    public void setSequencingContainers( List seqContainers){ m_seq_containers = seqContainers;}
    public void setResercherBarcode(String researcherbarcode){ m_researcherbarcode =  researcherbarcode;}
    public void isMappingFile(boolean isMappingFileRequested){m_isMappingFileRequested = isMappingFileRequested;}
    public void setGLysterolContainers(List gl_containers){ m_gl_containers = gl_containers;}
    public void setStorageType(String storage_type_to_add){ m_storage_type_to_add = storage_type_to_add;}
    public void setStorageForm(String storage_form){ m_storage_form = storage_form;}
    public void setOriginalContainers(List original_containers){ m_original_containers = original_containers;}
   
    
     public void run()
     {
         if (m_isMappingFileRequested)
             emailFile(m_seq_containers,m_researcherbarcode) ;// copy from RearrayedSeqPlatesHandler
         
         Connection conn= null;
         try
         {
             
             conn = DatabaseTransaction.getInstance().requestConnection();
             List samples = getSamples(m_gl_containers);
              int[] cloneIDs = getFailedSamples( m_gl_containers , conn);
           
             if ( m_seq_containers != null &&  m_seq_containers.size()> 0 )
             {
                 List seq_plates_samples = getSamples(m_seq_containers);
                 updateEmptySamples(seq_plates_samples, cloneIDs, conn , Sample.EMPTY);
                addStorage(seq_plates_samples, m_storage_type_to_add, m_storage_form,  conn);
                 
             }
             else
                  addStorage(samples, m_storage_type_to_add, m_storage_form,  conn);
             updateClonesTable( samples,  conn,"UNSEQUENCED", Sample.ISOLATE);// updates status for good ones
             // get clone id for fail clones
             updateClonesTable( cloneIDs,  conn,"FAIL CLONING" );// updates status for good ones
             processFailedConstructs(m_gl_containers,  conn) ;
             conn.commit();
             
         }
         catch(Exception e)
         {
             //send email rollback
             if ( conn != null)DatabaseTransaction.rollback(conn);
         }
         finally
         {
             if ( conn != null) DatabaseTransaction.closeConnection(conn);
         }
         
         
     }
     
     public void emailFile(List containers, String researcherBarcode)
     {
        User user = User.getUserFromBarcode(m_researcherbarcode);
        String filepath = SeqContainerMapper.FILEPATH;
        Iterator iter = containers.iterator();
        Container cont = null;
        Collection fileCol = new ArrayList();
        while( iter.hasNext())
        {
            cont = (Container) iter.next();
            fileCol.add(new File(SeqContainerMapper.FILEPATH + cont.getLabel()));
        }
        try {
            Mailer.sendMessage(user.getUserEmail(),
                    FlexProperties.getInstance().getProperty("HIP_FROM_EMAIL_ADDRESS"),
                    FlexProperties.getInstance().getProperty("HIP_CC_EMAIL_ADDRESS"),
                    "Files for rearrayed sequencing plates","Attached are your files for rearrayed sequencing plates", fileCol);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
     
     private List getSamples(List containers)throws Exception
     {
         List samples = new ArrayList();
         Container cont = null;Sample sample = null;
         Iterator iter = containers.iterator();
         while (iter.hasNext())
         {
             cont = (Container) iter.next();
             for ( int count = 0; count < cont.getSamples().size(); count++)
             {
                 sample = (Sample) cont.getSamples().get(count);
                 sample.setGenbank(cont.getLabel());
                 samples.add(sample);
             }
         }
         
         return samples;
     }
     private void   addStorage(List samples, String storageType, String storageForm, Connection conn) throws Exception
     {
        String sql = "insert into clonestorage (storageid, storagetype, storageform, storagesampleid, "
                +" cloneid , storagecontainerid, storagecontainerlabel,storagecontainerposition) "
                +" values ( storageid.nextval, '" + storageType +"','" + storageForm
                +"', ?,?,?,?,?)";
        PreparedStatement stmt = null;
        Sample sample = null;int ret = 1;
        try
        {
            stmt = conn.prepareStatement(sql);
            

            for (int sample_count = 0; sample_count < samples.size(); sample_count++)
            {
                sample = (Sample) samples.get(sample_count);
                if ( sample.getType().equals(Sample.ISOLATE) && sample.getCloneid() > 0)
                {
                    System.out.println("adding storage for "+sample.getCloneid());
                    stmt.setInt(ret++, sample.getId());
                    stmt.setInt(ret++, sample.getCloneid());
                    stmt.setInt(ret++, sample.getContainerid());
                    stmt.setString(ret++, sample.getGenbank());// around containerid will be put here
                    stmt.setInt(ret++, sample.getPosition());// around containerid will be put here
                    ret = 1;
                    DatabaseTransaction.executeUpdate(stmt);
                }
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
            if ( conn != null) DatabaseTransaction.rollback(conn);
            throw new Exception ("Cannot add cloning storage: "+ e.getMessage());
        }
        finally
        {
            DatabaseTransaction.closeStatement(stmt);
        }
        
  
     }
     
     private void   updateEmptySamples(List seq_container_samples, int[] cloneIDs, Connection conn , String sample_type) throws Exception
     {
         Sample sample = null;
         ArrayList failed_cloneids = new ArrayList();
         for (int count = 0; count < cloneIDs.length; count ++)
         {
             failed_cloneids.add(new Integer(cloneIDs[count]));
         }
         Iterator iter =  seq_container_samples.iterator();
         String sql=" update sample set sampletype= '"+sample_type+"', cloneid = NULL where sampleid = ?";
         PreparedStatement stmt = null;
         try
        {
            stmt = conn.prepareStatement(sql);
            while (iter.hasNext())
             {
                 sample = (Sample) iter.next();
                 if ( failed_cloneids.contains( new Integer( sample.getCloneid())))
                 {
                        stmt.setInt(1, sample.getId());
                        stmt.executeUpdate();
                 }
             }
        }
        catch(Exception e)
        {
            System.out.println(e);
            DatabaseTransaction.rollback(conn);
            throw new Exception ("Cannot update sequencing container : "+ e.getMessage());
        }
        finally
        {
            DatabaseTransaction.closeStatement(stmt);
        }
     }
                
     //update status for not grow clones   FAIL TO GROW or UNSEQUENCED
     // for those that not empty
     private void   updateClonesTable(List samples, Connection conn, String status, String check_sample_type) throws Exception
     {
         String sql=" update clones set STATUS= '"+status+"' where cloneid = ?";
        PreparedStatement stmt = null;
        Sample sample = null;int ret = 1;
        try
        {
            stmt = conn.prepareStatement(sql);
            for (int sample_count = 0; sample_count < samples.size(); sample_count++)
            {
                sample = (Sample) samples.get(sample_count);
                if ( sample.getType().equals(check_sample_type) && sample.getCloneid() > 0)
                {
                           System.out.println("update clone status "+sample.getCloneid()+" "+status);
             
                    stmt.setInt(1, sample.getCloneid() );
                    DatabaseTransaction.executeUpdate(stmt);
                }
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
            DatabaseTransaction.rollback(conn);
            throw new Exception ("Cannot add cloning storage: "+ e.getMessage());
        }
        finally
        {
            DatabaseTransaction.closeStatement(stmt);
        }
        
          }
   
     
     private void   updateClonesTable(int[] cloneIDs, Connection conn, String status) throws Exception
     {
         String sql=" update clones set STATUS= '"+status+"' where cloneid = ?";
        PreparedStatement stmt = null;
        int cloneID = 0;
        try
        {
            stmt = conn.prepareStatement(sql);
            for (int counter = 0; counter <  cloneIDs.length; counter++)
            {
                cloneID = cloneIDs[counter];
                if ( cloneID > 0)
                {
     System.out.println("update clone status "+cloneID+" "+status);
             
                    stmt.setInt(1, cloneID);
                    DatabaseTransaction.executeUpdate(stmt);
                }
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
            DatabaseTransaction.rollback(conn);
            throw new Exception ("Cannot update clone status: "+ e.getMessage());
        }
        finally
        {
            DatabaseTransaction.closeStatement(stmt);
        }
        
          }
   
    public void processFailedConstructs(List containers, Connection conn) throws Exception
    {
        String sql_get_constructid = "select distinct constructid from sample"+
        " where sampletype='"+ Sample.EMPTY +"' and containerid=?"+
        " and constructid not in ( select distinct constructid from sample "+
        " where sampletype='"+ Sample.ISOLATE +"'  and containerid=?)";
     //   String sql_get_flexsequenceid = "select sequenceid, flexstatus from flexsequence"+
    //    " where sequenceid in ( select distinct sequenceid from constructdesign where constructid=?)";
        
        String sql_update_cloning_progress = "update cloningprogress set statusid = "+ConstructInfo.FAILED_CLONING_ID+" where constructid = ?";
        String sql_update_flexsequence = "update flexsequence set flexstatus='"+FlexSequence.FAILED_CLONING
                +"' where sequenceid in ( select distinct sequenceid from constructdesign where constructid=?) and flexstatus ='"
    +             FlexSequence.CLONE_OBTAINED +"'";
  
        PreparedStatement stmt_get_constructid = null;
        PreparedStatement stmt_get_flexsequenceid = null;
        PreparedStatement stmt_update_cloning_progress = null;
        PreparedStatement stmt_update_flexsequence = null;
        ResultSet rs = null;
         
        try {
            stmt_get_constructid = conn.prepareStatement(sql_get_constructid);
         //   stmt_get_flexsequenceid = conn.prepareStatement(sql_get_flexsequenceid);
            stmt_update_cloning_progress = conn.prepareStatement(sql_update_cloning_progress);
            stmt_update_flexsequence = conn.prepareStatement(sql_update_flexsequence);
            
            for(int cont_count =0; cont_count <containers.size(); cont_count++) 
            {
                int containerid = ((Container)containers.get(cont_count)).getId() ;
                stmt_get_constructid.setInt(1, containerid);
                stmt_get_constructid.setInt(2, containerid);
                rs = DatabaseTransaction.executeQuery(stmt_get_constructid);
                while(rs.next()) 
                {
                    int constructid = rs.getInt(1);
                   
                    stmt_update_flexsequence.setInt(1, constructid);
                    DatabaseTransaction.executeUpdate(stmt_update_flexsequence);
                    
                     // cloningprogress
                    stmt_update_cloning_progress.setInt(1, constructid);
                    DatabaseTransaction.executeUpdate(stmt_update_cloning_progress);
      System.out.println("update construct status "+constructid);
                            
                             
                   
                }
            }
        } catch (Exception ex) 
        {
            DatabaseTransaction.rollback(conn);
            throw new Exception ("Cannot update flexsequence and cloningprogress tables "+ ex.getMessage());
          } 
        finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt_get_constructid);
            DatabaseTransaction.closeStatement(stmt_update_flexsequence);
            DatabaseTransaction.closeStatement(stmt_update_cloning_progress);
           
        }
    }
    
    
    private int[]  getFailedSamples(List containers , Connection conn) throws Exception
    {
        List samples = new ArrayList();
         Container cont = null;Sample sample = null;
         Iterator iter = containers.iterator();
         // get empty samples
         while (iter.hasNext())
         {
             cont = (Container) iter.next();
             for ( int count = 0; count < cont.getSamples().size(); count++)
             {
                 sample = (Sample) cont.getSamples().get(count);
                 if (sample.getType().intern() == Sample.EMPTY)
                 {                 samples.add(sample);}
             }
         }
         //for each empty sample find last sample where cloneid > 0
         iter = samples.iterator();
         int[] cloneids = new int[samples.size()]; 
         int cloneid = 0; int counter=0;
         while (iter.hasNext())
         {
            sample = (Sample) iter.next();
            cloneid = getCloneIDForEmptySample(sample.getId(), conn);
            cloneids[counter++] = cloneid;
         }
         return cloneids;
    }
    
    private int         getCloneIDForEmptySample(int sampleid, Connection conn )throws Exception
    {
        int cloneid = 0;
         String sql_get_next_sample = "select cloneid, sampleid from sample where sampleid in "+
        " (select sampleid_from from samplelineage where sampleid_to=?"+
        " and   sampleid_from <> sampleid_to)";
        PreparedStatement stmt_get_next_sample = null;
        ResultSet rs = null;
        int id_value = -sampleid ; 
        try 
        {
            stmt_get_next_sample = conn.prepareStatement(sql_get_next_sample);
            while (id_value < 0)
            {
                id_value = getCloneID(stmt_get_next_sample, Math.abs(id_value) );
                if ( id_value > 0  )
                    return id_value;
                if (id_value == 0) 
                    throw new Exception ("Cannot find cloneid for EMPTY sample " + sampleid);// first sample
            }
        }
        catch (Exception ex) 
        {
            DatabaseTransaction.rollback(conn);
            throw new Exception ("Cannot get sample history "+ ex.getMessage());
         } 
        finally
        {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt_get_next_sample);
              
        }
        return cloneid;
    }
    
    
    private int getCloneID(PreparedStatement stmt_get_next_sample, int sampleid_from) throws Exception
    {
        int resultid = 0;
        stmt_get_next_sample.setInt(1, sampleid_from);
        ResultSet rs = stmt_get_next_sample.executeQuery();
        if (rs.next())
        {
            int sampleid = rs.getInt("Sampleid");
            int cloneid = rs.getInt("cloneid");
            if ( cloneid > 0 ) return cloneid;
            else if ( cloneid == 0 && sampleid > 0 ) return -sampleid;
            else return 0;
        }
        return 0;
    }
     public static void main(String args[]) 
     {
          SummaryTablePopulatorPlateWithClones populator = new SummaryTablePopulatorPlateWithClones();
           ArrayList seqContainers = new ArrayList();
           ArrayList newContainers = new ArrayList();
         try
         {
             FlexProperties prop = FlexProperties.getInstance();
             edu.harvard.med.hip.flex.core.Container cont = 
                     new  edu.harvard.med.hip.flex.core.Container(20840);
         cont.restoreSample();
            newContainers.add(cont);
            cont =       new  edu.harvard.med.hip.flex.core.Container(20841);
           cont.restoreSample();;seqContainers.add(cont);
            cont =       new  edu.harvard.med.hip.flex.core.Container(20842);
           cont.restoreSample();seqContainers.add(cont);
            cont =       new  edu.harvard.med.hip.flex.core.Container(20843);
           cont.restoreSample();seqContainers.add(cont);
            cont =       new  edu.harvard.med.hip.flex.core.Container(20844);
           cont.restoreSample();seqContainers.add(cont);
            cont =       new  edu.harvard.med.hip.flex.core.Container(20845);
           cont.restoreSample();seqContainers.add(cont);
            cont =       new  edu.harvard.med.hip.flex.core.Container(20846);
           cont.restoreSample();seqContainers.add(cont);
            cont =       new  edu.harvard.med.hip.flex.core.Container(20847);
          cont.restoreSample(); seqContainers.add(cont);
            cont =       new  edu.harvard.med.hip.flex.core.Container(20848);
           cont.restoreSample();seqContainers.add(cont);
           boolean isMappingFileRequested = true;
          populator.setSequencingContainers(seqContainers);
                populator.setResercherBarcode("9784");
                populator.isMappingFile(isMappingFileRequested);
                populator.setGLysterolContainers(newContainers);
                populator.setStorageType(StorageType.WORKING);
                populator.setStorageForm(StorageForm.GLYCEROL);
                java.lang.Thread new_thread = new java.lang.Thread(populator);    
                new_thread.start();
         }catch(Exception e){}
     }
}
