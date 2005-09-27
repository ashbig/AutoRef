/**
 * $Id: Container.java,v 1.30 2005-09-27 18:57:41 Elena Exp $
 *
 * File     	: Container.java

 */
package edu.harvard.med.hip.bec.sampletracking.objects;

import java.util.*;
import java.math.BigDecimal;
import java.sql.*;
import javax.sql.*;

import edu.harvard.med.hip.bec.ui_objects.*;
import edu.harvard.med.hip.bec.util_objects.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.file.*;
import edu.harvard.med.hip.bec.user.*;
import  edu.harvard.med.hip.bec.coreobjects.endreads.*;
import  edu.harvard.med.hip.bec.coreobjects.sequence.*;
import  edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.coreobjects.oligo.*;
import sun.jdbc.rowset.*;
import edu.harvard.med.hip.bec.*;
import edu.harvard.med.hip.bec.sampletracking.mapping.*;

/**
 * Generic representation of all types of containers.
 */
public class Container
{   
    public static final String TYPE_SEQUENCING_CONTAINER = "SEQUENCING_CONTAINER";
    public static final String TYPE_OLIGO_CONTAINER = "OLIGO_CONTAINER";
    
    
    //container type
    public static final int PLATE_TYPE_96_A1_H12 = 1;
    public static final int PLATE_TYPE_96_A1_L8 = 2;
    public static final int PLATE_TYPE_384_A1_P24 = 3;

    
  
    
    
    public static final int CONTAINER_STATUS_FINISHED = 1;
    
    private int         m_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    private String      m_type = null;
    private String      m_label = null;
    private ArrayList   m_samples = new ArrayList();
    private int         m_status = -1;
    private int         m_cloning_strategy_id =  BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    /**
     * Constructor.
     *
     * @param id The container id.
     *
     * @return A Container object with id.
     * @exception BecUtilException, BecDatabaseException.
     */
    public Container(int id) throws BecUtilException, BecDatabaseException
    {
        m_id = id;
        
  /*      String sql = "select c.containerid as containerid, "+
        "c.containertype as containertype, "+
        "c.label as label, "+
        "c.locationid as locationid, "+
        "c.threadid as threadid, "+
        "l.locationtype as locationtype, "+
        "l.locationdescription as description\n"+
        "from containerheader c, containerlocation l\n"+
        "where c.locationid = l.locationid\n"+
        "and c.containerid = "+id;
        */
          String sql = "select  containerid,  containertype,  label, status "+
            "from containerheader where containerid = "+id;
        CachedRowSet crs = null;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            crs = t.executeQuery(sql);
            
            if(crs.size()==0)
            {
                throw new BecUtilException("Cannot initialize Container with id: "+id);
            }
            
            
            while(crs.next())
            {
                
                m_type = crs.getString("CONTAINERTYPE");
              //  int m_locationid = crs.getInt("LOCATIONID");
                m_label = crs.getString("LABEL");
          //      m_threadid = crs.getInt("THREADID");
                m_status = crs.getInt("STATUS");
            }
        } catch (NullPointerException ex)
        {
            throw new BecUtilException("Error occured while initializing container with id: "+id+"\n"+ex.getMessage());
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException("Error occured while initializing container with id: "+id+"\n"+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(crs);
        }
    }
    
  
    
    
    public Container(int id, String type, String label, 
                    int status) throws BecDatabaseException
    {
        m_type = type;
        m_status = status;
        m_label = label;
        if (id == BecIDGenerator.BEC_OBJECT_ID_NOTSET)
            m_id = BecIDGenerator.getID("containerid");
        else
            m_id = id;
    }
    
    /**
     * Finds all containers with the specified label.
     *
     * @param label The container label.
     *
     * @return A list of Container object with the given label.
     * @exception BecUtilException, BecDatabaseException.
     */
    public static ArrayList findContainersFromLabel(String label) throws BecUtilException,
    BecDatabaseException
    {
        
        ArrayList containerList = new ArrayList();
       
        String sql = "select  containerid, containertype,  label, status "+
        "from containerheader where label = '"+ label+"'";
        ResultSet rs = null;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next())
            {
                int id = rs.getInt("CONTAINERID");
                Container curContainer = new Container(
                                    rs.getInt("CONTAINERID"), 
                                    rs.getString("containertype") , 
                                    rs.getString("label"), 
                                        rs.getInt("status"));
             
                curContainer.restoreSample();
                containerList.add(curContainer);
            }
            return containerList;
        } catch (Exception ex)
        {
            throw new BecUtilException("Error occured while initializing container with label: "+label+"\n"+ex.getMessage());
        } 
        finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
        
    }
    
     /**
     * Finds all container ids with the specified label/s.
     *
     * @param labels The array of container labels.
     *
     * @return A list of Container object with the given label.
     * @exception BecUtilException, BecDatabaseException.
     */
    public static ArrayList findContainerIdsFromLabel(ArrayList labels) throws BecUtilException,
    BecDatabaseException
    {
        
        ArrayList containerIds = new ArrayList();
        if (labels == null || labels.size() < 1) return containerIds;
        String container_label = "";
        for (int count =0; count < labels.size(); count ++)
        {
            container_label += "'" + (String)labels.get(count) + "',";
        }
        container_label = container_label.substring(0, container_label.length() - 1);
        String sql = "select  containerid from containerheader where label in ("+ container_label+")";
        ResultSet rs = null;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next())
            {
                containerIds.add( new Integer(rs.getInt("CONTAINERID")));
              
            }
            return containerIds;
        } catch (Exception sqlE)
        {
            throw new BecDatabaseException("Error occured while getting container ids from labels: "+container_label+"\n: "+sqlE.getMessage());
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
      
    }
    
    
      public static Container findContainerDescriptionFromLabel(String label) throws    BecDatabaseException
    {
        
        ArrayList containerList = new ArrayList();
        
        String sql = "select  containerid, containertype,  label, status from containerheader where label = '"+ label+"'";
        ResultSet rs = null;
        Container container = null;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next())
            {
                container = new Container(
                                    rs.getInt("CONTAINERID"), 
                                    rs.getString("containertype") , 
                                    rs.getString("label"), 
                                        rs.getInt("status"));
                
            }
            return container;
        } catch (NullPointerException ex)
        {
            throw new BecDatabaseException("Error occured while initializing container with label: "+label+"\n"+ex.getMessage());
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException("Error occured while initializing container from labe: "+label+"\n"+"\nSQL: "+sqlE);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
       
    }
    
     public static ArrayList findContainerLabelsForProcess(int process_code, int param_id) throws    BecDatabaseException
    {
        //define isolatetracking id statuses for the process
        String istr_status = null;
        String sql = null;
        String sqlCheckVector = null;
        switch (process_code)
        {
            case Constants.PROCESS_SELECT_PLATES_FOR_END_READS:
            {
                sql = "select distinct label,containerid from containerheader where containerid in "
                + " (select containerid from sample where sampleid in "
                + " (select sampleid from isolatetracking where status in ("+IsolateTrackingEngine.PROCESS_STATUS_SUBMITTED_FOR_ER+")))";
                sqlCheckVector = "select vectorid from cloningstrategy where strategyid =("
        +" select configid from processconfig where configtype="+ Spec.CLONINGSTRATEGY_SPEC_INT +" and processid = "
+" (select processid from process_object where objecttype="+ Constants.PROCESS_OBJECT_TYPE_CONTAINER +"  and objectid=";
                break;
            }
            case Constants.PROCESS_RUN_ISOLATE_RUNKER:
            {
                
                 sql = "select distinct label,containerid from containerheader where status <> "+CONTAINER_STATUS_FINISHED+" and containerid in "
+ " (select containerid from sample where sampleid in "
+ " (select sampleid from isolatetracking where status not in  ("
+ IsolateTrackingEngine.PROCESS_STATUS_SUBMITTED_FOR_ER 
+ ","+IsolateTrackingEngine.PROCESS_STATUS_SUBMITTED_FOR_FULLSEQUENCING 
+ ","+IsolateTrackingEngine.PROCESS_STATUS_ER_INITIATED 
+ ","+IsolateTrackingEngine.PROCESS_STATUS_ER_PHRED_RUN  + ")))";
                break;
            }
        }
        if (sql == null) return null;
        ArrayList container_labels = new ArrayList();
        
        String label = null;
        ResultSet rs = null;ResultSet rsCheckParam = null;
        Container container = null;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next())
            {
                label =  rs.getString("label");
                if (process_code ==  Constants.PROCESS_SELECT_PLATES_FOR_END_READS)
                {
                   // sqlCheckVector = sqlCheckVector +rs.getInt("containerid") + "))";
                    rsCheckParam = t.executeQuery(sqlCheckVector +rs.getInt("containerid") + "))");
                    if ( rsCheckParam.next() )
                    {
                        if (param_id == rsCheckParam.getInt("vectorid") ) container_labels.add(   label  );
                    }
                }
                else if(process_code ==  Constants.PROCESS_RUN_ISOLATE_RUNKER)
                {
                    container_labels.add(   label  );
                }
            }
            return container_labels;
        } catch (Exception sqlE)
        {
            throw new BecDatabaseException("Error occured searching for containers for the process\nSQL: "+sqlE);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
            if ( rsCheckParam != null) DatabaseTransaction.closeResultSet(rsCheckParam);
        }
       
    }
    
     
    public static ArrayList findAllContainerLabels() throws    BecDatabaseException
    {
        //define isolatetracking id statuses for the process
        String sql = null;
     
      sql = "select  label from containerheader order by label";
        
        ArrayList container_labels = new ArrayList();
        
        String label = null;
        ResultSet rs = null;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
        
            while(rs.next())
            {
                
                container_labels.add(   rs.getString("label")  );
            }
            return container_labels;
        } catch (Exception sqlE)
        {
            throw new BecDatabaseException("Error occured searching for containers for the process\nSQL: "+sqlE);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
            
        }
       
    }
    
    public int          getId()    {        return m_id;    }
    public int          getStatus()    {        return m_status;}
    public String       getLabel()    {        return m_label;    }
    public String      getType()    {        return m_type;    }
    public ArrayList getSamples()    {        return m_samples;    } 
    

    public void             addSample(Sample sample)    {        m_samples.add(sample);    }
    public void             setLabel(String label)    {        m_label = label;    }
    public void             setStatus(int status)    {        m_status = status;    }
    public void             setSamples(ArrayList arr)    {         m_samples = arr;  }
   


      
   
    /**
     * Get the Sample object at a certain position.
     *
     * @param position The position that the sample located. Based on 1.
     *
     * @return A Sample object.
     *
     * @exception The BecUtilException when the samples is null.
     */
    public Sample getSample(int position) throws BecUtilException
    {
        if (m_samples != null)
        {
            return (Sample)m_samples.get(position-1);
        }
        else
        {
            throw new BecUtilException("samples not exists at position: "+position);
        }
    }
    
   
    public int getCloningStrategyId()throws BecDatabaseException
    {
        if (m_cloning_strategy_id!=-1) return m_cloning_strategy_id;
        else
        {
            String sql = "select configid from processconfig where CONFIGTYPE = 6 and processid = "
+"(select processid from process_object where objectid="+m_id+" and objecttype=0)";
            RowSet rs = null;
          
            try
            {
                DatabaseTransaction t = DatabaseTransaction.getInstance();
                rs = t.executeQuery(sql);

                while(rs.next())
                {

                    m_cloning_strategy_id = rs.getInt("configid");
                }
                return m_cloning_strategy_id;
           } catch (Exception sqlE)
            {
                throw new BecDatabaseException("Error occured while getting cloning strategy for container with id: "+m_id+"\n"+sqlE.getMessage()+"\nSQL: "+sql);
            } finally
            {
                DatabaseTransaction.closeResultSet(rs);
               
            }
        }
        
    }
    
    
    public static int getCloningStrategyId(int id)throws BecDatabaseException
    {
        int cloning_strategy = -1;
        String sql = "select configid from processconfig where CONFIGTYPE = 6 and processid = "
+"(select processid from process_object where objectid="+id+" and objecttype=0)";
            RowSet rs = null;
          
            try
            {
                DatabaseTransaction t = DatabaseTransaction.getInstance();
                rs = t.executeQuery(sql);

                while(rs.next())
                {

                    cloning_strategy = rs.getInt("configid");
                    
                }
                return cloning_strategy;
           } catch (Exception sqlE)
            {
                throw new BecDatabaseException("Error occured while getting cloning strategy for container with id: "+id+"\n"+sqlE.getMessage()+"\nSQL: "+sql);
            } finally
            {
                DatabaseTransaction.closeResultSet(rs);
               
            }
     
    }
    
    
    public static CloningStrategy getCloningStrategy(int id)throws BecDatabaseException
    {
        int cloning_strategy = getCloningStrategyId(id);
        return  CloningStrategy.getById(cloning_strategy);
    }
    /**
     * Get the data from Sample table.
     *
     * @exception BecDatabaseException.
     */
    public void restoreSample() throws BecDatabaseException
    {
        
        m_samples.clear();
        
        String sql = "select sampleid, position, sampletype from sample where containerid="+m_id+" order by POSITION";
        
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        CachedRowSet crs = t.executeQuery(sql);
        
        try
        {
            while(crs.next())
            {
                
                int sampleid = crs.getInt("SAMPLEID");
                String sampletype = crs.getString("SAMPLETYPE");
                int position = crs.getInt("position");
                Sample s = new Sample(  sampleid, sampletype, position, m_id);
                m_samples.add(s);
            }
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException("Error occured while initializing sample\n"+sqlE+"\nSQL: "+sql);
        } finally
        {
           
            DatabaseTransaction.closeResultSet(crs);
        }
         
    }
    
     /**
     * Get the data from Sample table.
     *
     * @exception BecDatabaseException.
     */
    public void restoreSampleIsolate(boolean isConstructInfo, boolean isRefSequenceInfo) throws BecDatabaseException
    {
        
        m_samples.clear();
        String sql = null;
        if (!isRefSequenceInfo && !isConstructInfo)
        {
         sql = "select s.sampleid as sampleid, position, ASSEMBLY_STATUS, constructid, sampletype,rank,score, status, "
        +" iso.isolatetrackingid as isolatetrackingid,id,flexconstructid,flexsampleid,flexsequencingplateid,"
        +" flexsequenceid,flexcloneid from flexinfo f, isolatetracking iso, sample s  "
        +" where f.isolatetrackingid=iso.isolatetrackingid and iso.sampleid=s.sampleid "
        +" and s.sampleid in ( select sampleid from sample where containerid = "+m_id+") order by POSITION";
        }
        if (isRefSequenceInfo)
        {
            sql="select s.sampleid as sampleid, position, iso.ASSEMBLY_STATUS as ASSEMBLY_STATUS,  iso.constructid as constructid, sampletype,rank,score, status,refsequenceid,  "
        +" iso.isolatetrackingid as isolatetrackingid,id,flexconstructid,flexsampleid,flexsequencingplateid, "
        +"  flexsequenceid,flexcloneid from flexinfo f, isolatetracking iso, sample s, sequencingconstruct c  "
        +"  where f.isolatetrackingid=iso.isolatetrackingid and iso.sampleid=s.sampleid "
+" and c.constructid(+)=iso.constructid  and s.sampleid in ( select sampleid from sample where containerid = "+m_id+" ) order by POSITION";
        }
        
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        CachedRowSet crs = t.executeQuery(sql);
         Sample s = null; IsolateTrackingEngine isolatetracking = null;FlexInfo fl = null;
        int refseqid = -1;
         try
        {
            while(crs.next())
            {
                
               
                int position = crs.getInt("position");
                 int sampleid = crs.getInt("sampleid");
                String sampletype = crs.getString("sampletype");
                s = new Sample(  sampleid, sampletype, position, m_id);
                if (isRefSequenceInfo && s.getType().equals("ISOLATE"))
                {
                    refseqid = crs.getInt("refsequenceid");
                    s.setRefSequenceId(refseqid);
                }
                // create isolate tracking for sample
                if ( crs.getInt("isolatetrackingid") != -1)
                {
                    int isolatetracking_id = crs.getInt("isolatetrackingid");
                    isolatetracking = new IsolateTrackingEngine();
                    isolatetracking.setRank(crs.getInt("rank")) ;// results of the end read analysis
                    isolatetracking.setScore(crs.getInt("score"));// results of the end read analysis
                    isolatetracking.setStatus(crs.getInt("status"));
                    isolatetracking.setSampleId(sampleid);
                    isolatetracking.setId(isolatetracking_id);
                    isolatetracking.setAssemblyStatus(crs.getInt("ASSEMBLY_STATUS"));
                    isolatetracking.setFlexInfoId(crs.getInt("id") );// sample id of the first sample of this isolate
                    isolatetracking.setConstructId(crs.getInt("constructid") );// identifies the agar; several (four) isolates will have the same id
    //create flex info for isolate tracxking
                     fl = new FlexInfo();
                    fl.setId ( crs.getInt("id"));
                    fl.setIsolateTrackingId ( isolatetracking_id );
                     fl.setFlexSampleId ( crs.getInt("flexsampleid"));
                     fl.setFlexConstructId ( crs.getInt("flexconstructid"));
                     fl.setFlexPlateId ( crs.getInt("flexsequencingplateid"));
                     fl.setFlexSequenceId ( crs.getInt("flexsequenceid"));
                    fl.setFlexCloneId ( crs.getInt("flexcloneid")) ;

                    isolatetracking.setFlexInfo(fl);
                    s.setIsolaterTrackingEngine(isolatetracking);
                    m_samples.add(s);
                  //  System.out.println(s.getId());
                }
            }
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException("Error occured while initializing sample\n"+sqlE+"\nSQL: "+sql);
        } finally
        {
            
            DatabaseTransaction.closeResultSet(crs);
        }
         
    }
    public  static ArrayList restoreUISamples(Container container)throws BecDatabaseException
    {
        //get sample data
       ArrayList results = new ArrayList();
        if (container == null) return results;
        container.restoreSampleIsolate(false,false);
        //fill in clone info
        UICloneSample clone = null; Sample sample = null;
        for (int clone_count = 0; clone_count < container.getSamples().size(); clone_count++)
        {
            sample = (Sample) container.getSamples().get(clone_count);
            clone = new UICloneSample();
            clone.setPlateLabel (container.getLabel() ); 
            clone.setPosition (sample.getPosition()); 
            clone.setSampleType (sample.getType()); 
            clone.setCloneId ( sample.getIsolateTrackingEngine().getFlexInfo().getFlexCloneId()); 
            clone.setCloneStatus (sample.getIsolateTrackingEngine().getStatus() ); 
            clone.setConstructId (sample.getIsolateTrackingEngine().getConstructId()); 
            clone.setIsolateTrackingId(sample.getIsolateTrackingEngine().getId()); 
            clone.setRank(sample.getIsolateTrackingEngine().getRank());
            clone.setSampleId(sample.getId());
            clone.setRefSequenceId(sample.getRefSequenceId());
            results.add(clone);
        }
        return results;
    }
    
     /**
     * Get the data from Sample table.
     *
     * @exception BecDatabaseException.
     */
    public void restoreSampleIsolateNoFlexInfo() throws BecDatabaseException
    {
        
        m_samples.clear();
        
        String sql = "select s.sampleid as sampleid, position,constructid, sampletype,rank,score, status, ASSEMBLY_STATUS ,"
        +" iso.isolatetrackingid as isolatetrackingid from  isolatetracking iso, sample s  "
        +" where  iso.sampleid=s.sampleid "
        +" and s.sampleid in ( select sampleid from sample where containerid = "+m_id+") order by POSITION";
        
   
        
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        CachedRowSet crs = t.executeQuery(sql);
         Sample s = null; IsolateTrackingEngine isolatetracking = null;FlexInfo fl = null;
        try
        {
            while(crs.next())
            {
                
               
                int position = crs.getInt("position");
                 int sampleid = crs.getInt("sampleid");
                String sampletype = crs.getString("sampletype");
                s = new Sample(  sampleid, sampletype, position, m_id);
                // create isolate tracking for sample
                if ( crs.getInt("isolatetrackingid") != -1)
                {
                    int isolatetracking_id = crs.getInt("isolatetrackingid");
                    isolatetracking = new IsolateTrackingEngine();
                    isolatetracking.setRank(crs.getInt("rank")) ;// results of the end read analysis
                    isolatetracking.setScore(crs.getInt("score"));// results of the end read analysis
                    isolatetracking.setStatus(crs.getInt("status"));
                    isolatetracking.setAssemblyStatus(crs.getInt("ASSEMBLY_STATUS"));
                    isolatetracking.setSampleId(sampleid);
                    isolatetracking.setId(isolatetracking_id);
                   // sample id of the first sample of this isolate
                    isolatetracking.setConstructId(crs.getInt("constructid") );// identifies the agar; several (four) isolates will have the same id
                    s.setIsolaterTrackingEngine(isolatetracking);
                    m_samples.add(s);
                  //  System.out.println(s.getId());
                }
            }
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException("Error occured while initializing sample\n"+sqlE+"\nSQL: "+sql);
        } finally
        {
            
            DatabaseTransaction.closeResultSet(crs);
        }
         
    }
    
    
     /**
     * Get the data from Sample table.
     *
     * @exception BecDatabaseException.
     */
    public void restoreSampleWithResult(int[] result_types, boolean isConstruct_id) throws BecDatabaseException
    {
        
        m_samples.clear();
        String sql =  null;
        if (isConstruct_id)
        {
             sql = "select s.sampleid as sampleid, position, constructid, sampletype "
            +"  from  isolatetracking iso, sample s  where iso.sampleid=s.sampleid "
            +" and s.sampleid in ( select sampleid from sample where containerid = "+m_id+") order by POSITION";
        }
        else
        {
             sql = "select sampleid , position, sampletype  from   sample   where sampleid in ( select sampleid from sample where containerid = "+m_id+") order by POSITION";
        }
   
        
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        CachedRowSet crs = null; 
        RowSet rs = null;RowSet rs1 = null;
        String res_id = null; String res_value = null;int construct_id = -1;
        Result result = null;
         Sample s = null; IsolateTrackingEngine isolatetracking = null;FlexInfo fl = null;
        try
        {
            crs = t.executeQuery(sql);
            
            while(crs.next())
            {
                int position = crs.getInt("position");
                 int sampleid = crs.getInt("sampleid");
                String sampletype = crs.getString("sampletype");
                 s = new Sample(  sampleid, sampletype, position, m_id);
                if (isConstruct_id)
                {
                     construct_id = crs.getInt("constructid");
                     s.setConstructId(construct_id);
                }
               
                
              //get results
                 
                res_id = "select resultid,resultvalueid, resulttype from result where sampleid = "+sampleid+" and resulttype in ("+Algorithms.convertArrayToString(result_types,",")+")";
                rs = t.executeQuery(res_id);
            
                while(rs.next())
                {
                    int value_id = rs.getInt("resultvalueid");
                    int result_type = rs.getInt("resulttype");
                    int result_id = rs.getInt("resultid");
                    switch (result_type)
                    {
                        case Result.RESULT_TYPE_ENDREAD_FORWARD:
                        case Result. RESULT_TYPE_ENDREAD_FORWARD_PASS:
                        
                         case Result. RESULT_TYPE_ENDREAD_REVERSE_PASS : 
                        case Result. RESULT_TYPE_ENDREAD_REVERSE:
                        {
                            Read read = Read.getReadById(value_id);
                            result = new Result(result_id,-1, s.getId(), read, result_type, value_id);
                            s.addResult(result);
                            break;
                            
                        }
                         case Result. RESULT_TYPE_ENDREAD_FORWARD_FAIL:
                         case Result. RESULT_TYPE_ENDREAD_REVERSE_FAIL : 
                       
                        {
                             result = new Result(result_id,-1, s.getId(), null, result_type, -1);
                            s.addResult(result);
                            break;
                            
                        }
                        case Result.RESULT_TYPE_OLIGO_CALCULATION:
                        {
                            break;
                        }

                        case Result.RESULT_TYPE_ASSEMBLED_SEQUENCE_PASS :
                        {
                            break;
                        }
                        case Result.RESULT_TYPE_ASSEMBLED_SEQUENCE_FAIL :
                        {
                            break;
                        }
                        case Result.RESULT_TYPE_FINAL_CLONE_SEQUENCE :
                        {
                            break;
                        }
                    }
                }
                
                
                m_samples.add(s);
                  //  System.out.println(s.getId());
            }
            	Sample sampleq=null;
		Read readq  = null;
		//System.out.println(this.getSamples().size());
    for (int count = 0; count < this.getSamples().size(); count ++)
    {
	
		sampleq = (Sample)this.getSamples().get(count);
	//System.out.println(sampleq.getPosition() +" ");
		if ( sampleq.getResults() != null && sampleq.getResults().size() > 0)
		{
		
			readq = (Read) ((Result)sampleq.getResults().get(0)).getValueObject();
			//if ( readq != null) System.out.println(" read id "+ readq.getId() +" ");
		}
		
		 if ( sampleq.getConstructId()!= -1)
		{
		  //System.out.println(sampleq.getConstructId() );
		}
		
		}
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException("Error occured while initializing sample\n"+sqlE+"\nSQL: "+sql);
        } finally
        {
            
            DatabaseTransaction.closeResultSet(crs);
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeResultSet(rs1);
        }
         
    }
    
    public void restoreSampleWithResultId(int[] result_types, boolean isConstruct_id) throws BecDatabaseException
    {
        
        m_samples.clear();
        String sql =  null;
        if (isConstruct_id)
        {
             sql = "select s.sampleid as sampleid, position, constructid, sampletype "
            +"  from  isolatetracking iso, sample s  where iso.sampleid=s.sampleid "
            +" and s.sampleid in ( select sampleid from sample where containerid = "+m_id+") order by POSITION";
        }
        else
        {
             sql = "select sampleid , position, sampletype  from   sample   where sampleid in ( select sampleid from sample where containerid = "+m_id+") order by POSITION";
        }
   
        
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        CachedRowSet crs = null; 
        RowSet rs = null;RowSet rs1 = null;
        String res_id = null; String res_value = null;int construct_id = -1;
        Result result = null;
         Sample s = null; IsolateTrackingEngine isolatetracking = null;FlexInfo fl = null;
        try
        {
            crs = t.executeQuery(sql);
            
            while(crs.next())
            {
                int position = crs.getInt("position");
                 int sampleid = crs.getInt("sampleid");
                String sampletype = crs.getString("sampletype");
                 s = new Sample(  sampleid, sampletype, position, m_id);
                if (isConstruct_id)
                {
                     construct_id = crs.getInt("constructid");
                     s.setConstructId(construct_id);
                }
               
                
              //get results
               res_id = "  select r.resultid as resultid,resultvalueid, resulttype , readid,readsequenceid,score, trimmedstart,trimmedend ,localpath, basename "
+" from result r, readinfo read,resultfilereference f, filereference fr "
+" where sampleid = "+sampleid+" and read.resultid=r.resultid "
+" and r.resultid=f.resultid and f.filereferenceid=fr.filereferenceid and resulttype in ("+Algorithms.convertArrayToString(result_types,",")+")";
              
                rs = t.executeQuery(res_id);
            
                while(rs.next())
                {
                    int value_id = rs.getInt("resultvalueid");
                    int result_type = rs.getInt("resulttype");
                    int result_id = rs.getInt("resultid");
                    switch (result_type)
                    {
                        case Result.RESULT_TYPE_ENDREAD_FORWARD:
                        case Result. RESULT_TYPE_ENDREAD_FORWARD_PASS:
                        
                         case Result. RESULT_TYPE_ENDREAD_REVERSE_PASS : 
                        case Result. RESULT_TYPE_ENDREAD_REVERSE:
                        {
                            Read read = new Read();//.getReadById(value_id);
                            read.setId(rs.getInt("readid"));
                            read.setScore(rs.getInt("score"));
                            read.setSequenceId(rs.getInt("readsequenceid"));
                            read.setTrimEnd(rs.getInt("trimmedend"));
                            read.setTrimStart(rs.getInt("trimmedstart"));
                            read.setTraceFileBaseName( rs.getString("basename"));
                            read.setTraceFileName( rs.getString("localpath"));
                            result = new Result(result_id,-1, s.getId(), read, result_type, value_id);
                            s.addResult(result);
                            break;
                            
                        }
                         case Result. RESULT_TYPE_ENDREAD_FORWARD_FAIL:
                         case Result. RESULT_TYPE_ENDREAD_REVERSE_FAIL : 
                       
                        {
                             result = new Result(result_id,-1, s.getId(), null, result_type, -1);
                            s.addResult(result);
                            break;
                            
                        }
                        case Result.RESULT_TYPE_OLIGO_CALCULATION:
                        {
                            break;
                        }

                        case Result.RESULT_TYPE_ASSEMBLED_SEQUENCE_PASS :
                        {
                            break;
                        }
                        case Result.RESULT_TYPE_ASSEMBLED_SEQUENCE_FAIL :
                        {
                            break;
                        }
                        case Result.RESULT_TYPE_FINAL_CLONE_SEQUENCE :
                        {
                            break;
                        }
                    }
                }
                
                
                m_samples.add(s);
                  //  System.out.println(s.getId());
            }
            	
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException("Error occured while initializing sample\n"+sqlE+"\nSQL: "+sql);
        } finally
        {
            
            DatabaseTransaction.closeResultSet(crs);
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeResultSet(rs1);
        }
         
    }
    
    /**
     * Insert the container record into database.
     *
     * @param conn <code>Connection</code> to use for insert.
     * @exception BecDatabaseException.
     */
    public void insert(Connection conn) throws BecDatabaseException
    {
        String sql = 	"insert into containerheader " +
        "(containerid, containertype,  label, status) "+
        "values ("+m_id+",'"+m_type+"','"+m_label+"',"+m_status+")";
        Sample s = null;
        DatabaseTransaction.executeUpdate(sql,conn);
         
        //foreach sample, insert record into containercell and sample table
        for (int ind = 0; ind < m_samples.size(); ind++)
        {
            s = (Sample)m_samples.get(ind);
            s.insert(conn);
        }
    }
    
   
    
    
    
    /**
     * Update the location record to the database.
     *
     * @param locationid The new locationid to be updated to.
     * @param c The connection used for database update.
     * @exception The BecDatabaseException.
     */
    public void updateStatus(int status, Connection c) throws BecDatabaseException
    {
        String sql = "update containerheader "+
        " set status="+ status +
        " where containerid="+m_id;
        m_status = status;
        DatabaseTransaction.executeUpdate(sql, c);
    }
     
    
    
   
    // function returns araay of containers of type == container type
    // that have status one in container statuses
    //  mode - true  returns platesets of containers  with the same status if applicable
    //            array of elements (master plate - er foraward - er reverse)
    //  mode - false only containers of requested type
    //            array of elements (master plate)
    
    //user - if user not null - only containers submitted by user
    //other wise all containers regardless of user
    public static ArrayList getContainersBySubmitter(User user, 
                            String container_type , 
                            int[] container_status, 
                            boolean mode) throws BecDatabaseException
    {
        ArrayList containers = new ArrayList();
        
        String status = ""; 
        for (int count = 0; count < container_status.length; count ++)
        {
            status += container_status[count] + ",";
        }
        
        if (user == null && ! mode)
        {
          // return getContainersBySubmitter(   container_type ,    status    );
        }
        else if(user == null && mode)
         {
           // return getContainersBySubmitter(   status   );
         }
        
        else if (user != null && ! mode)
        {
          // return getContainersBySubmitter(   container_type ,    status , user.getId()   );
            
        }
        else if(user != null && mode)
         {
           //  return getContainersBySubmitter(       status , user.getId()   );
            
         }
        return null;
    }
    
       //function checkes wether at least one sample has result of type end-read attached
        //returns true if yes
   public boolean checkForResultTypes(int[] result_types) throws BecDatabaseException
   {
       String sql = "";
       for (int ind = 0; ind < result_types.length; ind++)
       {
           sql +=result_types[ind];
           if (ind != result_types.length - 1) sql +=",";
       }
  
       sql = "select count(*) from  result where sampleid in "
       + " (select sampleid  from sample where containerid = " + m_id
       + " ) and resulttype in ( "+ sql + ")";
       
        
        CachedRowSet crs = null;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            crs = t.executeQuery(sql);
            crs.next();
            if(crs.next())
            {
                return true;
            }
            return false;
        } catch (Exception e)
        {
            throw new BecDatabaseException("Error while checking for result types for container with id: "+m_id+"\n"+e+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(crs);
        }
        
       
    }
   
    /**
     * Static method to find the end read oligos for the container
     *
     * @param mgc The rearrayed MGC container.
     * @return The DNA template plate. Return null if not found.
     */
      public static Oligo[] findEndReadsOligos(int container_id)throws BecDatabaseException
      {
        String sql = "select position, type,orientation, p.primerid as primerid, name, sequence, tm, "
                    +" leaderlength,leadersequence from vectorprimer v, commonprimer p where p.primerid=v.primerid "
                    +" and p.primerid in (select configid from processconfig where "
                    +"configtype = "+Spec.VECTORPRIMER_SPEC_INT +" and processid in "
                    +"(select processid from process_object where objecttype=1 and objectid = "
                    + "(select min(resultid) from result where sampleid in(select sampleid from sample where containerid="+container_id
                    +") and resulttype in ("+Read.TYPE_ENDREAD_REVERSE+","+Read.TYPE_ENDREAD_FORWARD+"))))";
 
        DatabaseTransaction t = null;
        CachedRowSet crs = null;
        Container c = null;
        Oligo forward = null;
        Oligo reverse = null;
        Oligo[] oligos = new Oligo[2];
        try {
            t = DatabaseTransaction.getInstance();
            crs = t.executeQuery(sql);
            while (crs.next())
            {
                int position = crs.getInt("position");
                
                if (position == Oligo.POSITION_FORWARD)
                {
                    forward = new Oligo();
                    forward.setId(crs.getInt("primerid") );
                    forward.setType(crs.getInt("type"));//primer type: 5p-pcr, 5p-universal, 5p-full_set_n ?
                    forward.setPosition(position); // for full sequencing, start of the prime regarding sequence start
                    forward.setName(crs.getString("name") );
                    forward.setSequence(crs.getString("sequence"));
                    forward.setOrientation(crs.getInt("orientation")) ;
                    forward.setLeaderLength(crs.getInt("leaderlength"));
                    forward.setLeaderSequence(crs.getString("leadersequence"));
                     oligos[0] = forward;
                }
                else  if (position == Oligo.POSITION_REVERSE)
                {
                    reverse = new Oligo();
                    reverse.setId(crs.getInt("primerid") );
                    reverse.setType(crs.getInt("type"));//primer type: 5p-pcr, 5p-universal, 5p-full_set_n ?
                    reverse.setPosition(position); // for full sequencing, start of the prime regarding sequence start
                    reverse.setName(crs.getString("name") );
                    reverse.setSequence(crs.getString("sequence"));
                    reverse.setOrientation(crs.getInt("orientation")) ;
                    reverse.setLeaderLength(crs.getInt("leaderlength"));
                    reverse.setLeaderSequence(crs.getString("leadersequence"));
                    oligos[1] = reverse;
                }
            }
           
            return oligos;
            
        } catch (Exception ex)
        {
            System.out.println(ex);
            throw new  BecDatabaseException("Cannot get common oligos for the plate"+sql+ex.getMessage());
        } finally 
        {
            DatabaseTransaction.closeResultSet(crs);
        }
 
        
    }
     
      public static ArrayList getProcessHistoryItems(String label)throws Exception
      
      {
          
          Container cont = findContainerDescriptionFromLabel(label);
          if ( cont != null)
            return getProcessHistory( label );
          else return null;
      }
    
      private static ArrayList getProcessHistory(String label)throws Exception
      
      {
          ItemHistory history = (ItemHistory)edu.harvard.med.hip.bec.util_objects.ProcessHistory.getProcessHistory(Constants.ITEM_TYPE_PLATE_LABELS, label).get(0);
          if ( history.getStatus() == ItemHistory.HISTORY_PROCESSED)
              return history.getHistory();
          else
              throw new Exception( (String)history.getHistory().get(0));
       }
      
      
      
      
      //get all labels for the project by project code
      
      public static String getPlateLabelsForProject(String project_code)
      {
          StringBuffer result = new StringBuffer();
          String sql = "Select label from containerheader where label like '"+project_code +"%'";
          CachedRowSet crs = null;
            try
            {
                DatabaseTransaction t = DatabaseTransaction.getInstance();
                crs = t.executeQuery(sql);
            
                while(crs.next())
                {
                    result.append( crs.getString("LABEL") +" ");
                }
             } 
            catch (Exception sqlE)
            {
             } 
          finally
            {
                DatabaseTransaction.closeResultSet(crs);
            }
          return result.toString();
      }
      
      //well conversion
      
    //------------------------   
      //convert well nomenculature from A10 to int
      //---------------------------------------
      
    public static final boolean isValidNamingPlateType(int v)
    {
        if ( v == PLATE_TYPE_96_A1_H12 || v==PLATE_TYPE_96_A1_L8  || v ==PLATE_TYPE_384_A1_P24 )
            return true;
        return false;
    }
    public static int convertPositionFrom_alphanumeric_to_int(String position)
    {
        switch ( BecProperties.getInstance().getPlateTypePositionNaming())
        {
            case  PLATE_TYPE_96_A1_H12 : return convertWellFrom_A1_H12_to_int(position);
            case PLATE_TYPE_96_A1_L8 : return 0;
            case PLATE_TYPE_384_A1_P24 : return 0;
            default : return 0;
        }

    }
   
     public static String convertPositionFrom_int_to_alphanumeric(int position)
    {
        switch ( BecProperties.getInstance().getPlateTypePositionNaming())
        {
            case  PLATE_TYPE_96_A1_H12 : return convertWellFrom_int_to_A1_H12(position);
            case PLATE_TYPE_96_A1_L8 : return "";
            case PLATE_TYPE_384_A1_P24 : return "";
            default : return "";
        }

    }
     
     public static String convertWellFrom_int_to_A1_H12( int position) 
    {
        if (position > 96 || position < 1 ) return "";
        int column = (int) position / 8  +1 ;   
       
        int a_value = (int) 'A';
        int row_value = position % 8;
        char rowname = (char) (a_value + row_value - 1);
        if (row_value == 0 ) { rowname='H'; column--;}
        if (column < 10)
            return ""+rowname+"0"+column;
        else
            return ""+rowname + column;
     }
    
     public static int convertWellFrom_A1_H12_to_int(String well)
    {
        int a_value = (int) 'a';
     
        well = well.toLowerCase();
        int row = (int)well.charAt(0);
        if (row > (a_value + 7) || row < a_value) return -1;
        int column = Integer.parseInt(well.substring(1));
        int row_value =  row - a_value + 1;
     
        return (column - 1) * 8 +  row_value ;       
  
    }
      
      
    //**************************************************************//
    //				Test				//
    //**************************************************************//
    
    // These test cases also include tests for Sample class.
    public static void main(String args[]) throws Exception
    {
       
        try
        {
              BecProperties sysProps =  BecProperties.getInstance( BecProperties.PATH);
            sysProps.verifyApplicationSettings();
        
         int position = 1;
       System.out.println(  position+" "+      Container.convertPositionFrom_int_to_alphanumeric(position)    );
        position = 96;
       System.out.println(  position+" "+         Container.convertPositionFrom_int_to_alphanumeric( position) );
         position = 8;
       System.out.println(  position+" "+         Container.convertPositionFrom_int_to_alphanumeric( position) );
           position = 89;
       System.out.println(  position+" "+         Container.convertPositionFrom_int_to_alphanumeric( position) );
        position = 52;
       System.out.println(  position+" "+         Container.convertPositionFrom_int_to_alphanumeric( position) );
        position = 64;
       System.out.println(  position+" "+         Container.convertPositionFrom_int_to_alphanumeric( position) );
       position = 41;
       System.out.println(  position+" "+         Container.convertPositionFrom_int_to_alphanumeric( position) );
       
        }
        catch(Exception e)
        {
        }
        System.exit(0);
     
    }
}
