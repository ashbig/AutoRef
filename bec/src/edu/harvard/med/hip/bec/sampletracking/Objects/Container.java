/**
 * $Id: Container.java,v 1.13 2003-08-29 18:41:26 Elena Exp $
 *
 * File     	: Container.java

 */
package edu.harvard.med.hip.bec.sampletracking.objects;

import java.util.*;
import java.math.BigDecimal;
import java.sql.*;
import javax.sql.*;

import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.file.*;
import edu.harvard.med.hip.bec.user.*;
import  edu.harvard.med.hip.bec.coreobjects.endreads.*;
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
   // public static final String TYPE_ER_REVERSE_CONTAINER = "ER_REVERSE_CONTAINER";
  //  public static final String TYPE_ER_FORWARD_CONTAINER = "ER_FORWARD_CONTAINER";
  //  public static final String TYPE_ER_CONTAINER = "ER_CONTAINER";
    public static final String TYPE_SEQUENCING_CONTAINER = "SEQUENCING_CONTAINER";
   
  
    
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
    
    
    
      
    /**
     * Constructor.
     *
     * @param type The container type.
     * @param Location The location of the container.
     * @param label The container label.
     * @param threadid The thread id of this container.
     *
     * @return The Container object.
     * @exception BecDatabaseException.
     */
    /*
    public Container(int id, String type, Location location, String label, 
                    int status, int threadid) throws BecDatabaseException
    {
        m_type = type;
        //m_location = location;
        m_status = status;
       // m_locationid = m_location.getId();
        m_label = label;
      //  m_threadid = threadid;
        if (id == BecIDGenerator.BEC_OBJECT_ID_NOTSET)
            m_id = BecIDGenerator.getID("containerid");
        else
            m_id = id;
    }
    
    public Container(int id, String type, int location, String label, 
                    int status, int threadid) throws BecDatabaseException
    {
        m_type = type;
        m_status = status;
        m_locationid = location;
        m_label = label;
        m_threadid = threadid;
        if (id == BecIDGenerator.BEC_OBJECT_ID_NOTSET)
            m_id = BecIDGenerator.getID("containerid");
        else
            m_id = id;
    }
     **/
    
    
    
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
        /*
        String sql = "select c.containerid as containerid, "+
        "c.containertype as containertype, "+
        "c.label as label, "+
        "c.locationid as locationid, "+
        "c.threadid as threadid, "+
        "l.locationtype as locationtype, "+
        "l.locationdescription as description\n"+
        "from containerheader c, containerlocation l\n"+
        "where c.locationid = l.locationid\n"+
        "and c.label = '"+ label+"'";
         **/
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
        } catch (NullPointerException ex)
        {
            throw new BecUtilException("Error occured while initializing container with label: "+label+"\n"+ex.getMessage());
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException("Error occured while initializing container from labe: "+label+"\n"+"\nSQL: "+sqlE);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
        return containerList;
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
    
  
    public int          getId()    {        return m_id;    }
    public int          getStatus()    {        return m_status;}
    public String       getLabel()    {        return m_label;    }
    public String      getType()    {        return m_type;    }
    public ArrayList getSamples()    {        return m_samples;    } 
    
    public void             addSample(Sample sample)    {        m_samples.add(sample);    }
    public void             setLabel(String label)    {        m_label = label;    }
    public void             setStatus(int status)    {        m_status = status;    }
    public void             setSamples(ArrayList arr)    {         m_samples = arr;  }
   
    public ArrayList getContainerHistory()    {        return null;    }
    public static ArrayList getContainerHistory(int containerid)    {        return null;    }
    
   
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
		System.out.println(this.getSamples().size());
    for (int count = 0; count < this.getSamples().size(); count ++)
    {
	
		sampleq = (Sample)this.getSamples().get(count);
	System.out.println(sampleq.getPosition() +" ");
		if ( sampleq.getResults() != null && sampleq.getResults().size() > 0)
		{
		
			readq = (Read) ((Result)sampleq.getResults().get(0)).getValueObject();
			if ( readq != null) System.out.println(" read id "+ readq.getId() +" ");
		}
		
		 if ( sampleq.getConstructId()!= -1)
		{
		  System.out.println(sampleq.getConstructId() );
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
            if (s.getType().equals("EMPTY"))
                System.out.print(s.getPosition()+" ");
            s.insert(conn);
            System.out.println("insert "+s.getPosition());
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
     
    
    
    /**
     * Update the location record to the database.
     *
     * @param locationid The new locationid to be updated to.
     * @param c The connection used for database update.
     * @exception The BecDatabaseException.
     */
    /*
    public void updateLocation(int locationid, Connection c) throws BecDatabaseException
    {
        String sql = "update containerheader "+
        " set locationid="+locationid+
        " where containerid="+m_id;
        
        DatabaseTransaction.executeUpdate(sql, c);
    }
    
   
  /*
    public String[] labelParsing()
    {
        String output[] = new String[2];
        char[] chars = m_label.toCharArray();
        for (int ind =0; ind < chars.length; ind ++)
        {
           
        }
        return output;
    }
    
    */
    
    // ***** statis methods **********************************
    
     /**
     * Update the location record to the database.
     *
     * @param locationid The new locationid to be updated to.
     * @param containerid The container id for container to be updated to.
     * @param c The connection used for database update.
     * @exception The BecDatabaseException.
     */
    /*
    public static void updateLocation(int locationid ,int containerid, Connection c) throws BecDatabaseException
    {
        String sql = "update containerheader "+
        " set locationid="+locationid+
        " where containerid="+containerid;
        
        DatabaseTransaction.executeUpdate(sql, c);
    }
    */
    
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
    /*
      // function returns array of containers of define type 
    // that have status one in container statuses
   
    public static ArrayList getContainersBySubmitter
                (  String container_type , String status)
                throws BecDatabaseException
    {
        ArrayList containers = new ArrayList();
        Container cont = null;
        
        String sql =  = "select  containerid,  threadid,  status, " +
            " containertype,  label,  locationid, "+
            " from containerheader where containertype = " + container_type 
            " and status in (" + status + ")";
       
        String type = null; int locationid = -1; String label = null; int status = -1;int id = -1;
        CachedRowSet crs = null;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            crs = t.executeQuery(sql);
            
            while(crs.next())
            {
                id = crs.getInt("CONTAINERID");
                type = crs.getString("CONTAINERTYPE");
                locationid = crs.getInt("LOCATIONID");
                label = crs.getString("LABEL");
                threadid = crs.getInt("THREADID");
                status = crs.getInt("");
                cont = new Container( id,  type,  locationid,  label,  status,  threadid);
                containers.add( cont);
            }
            return containers;
        } catch (Exception e)
        {
            throw new BecDatabaseException("Error occured while initializing container with id: "+id+"\n"+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(crs);
        }
        
        
    }
    
    
    
     // function returns platesets of containers  with the same status if applicable
    // that have status one in container statuses
   //            array of elements (master plate - er foraward - er reverse)
    
  
    public static ArrayList getContainersBySubmitter (   String status)   throws BecDatabaseException
    {
        ArrayList containers = new ArrayList();
        Container cont = null;
        
        String sql =  "select c.containerid as containerid, c.threadid as threadid, c.status as status, " +
            "c.containertype as containertype, c.label as label, c.locationid as locationid, "+
            " from containerheader c, "+
            "where status in (" + status + ")";
         }
     
        String type = null; int locationid = -1; String label = null; int status = -1;int id = -1;
        CachedRowSet crs = null;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            crs = t.executeQuery(sql);
            
            while(crs.next())
            {
                id = crs.getInt("CONTAINERID");
                type = crs.getString("CONTAINERTYPE");
                locationid = crs.getInt("LOCATIONID");
                label = crs.getString("LABEL");
                threadid = crs.getInt("THREADID");
                status = crs.getInt("");
                cont = new Container( id,  type,  locationid,  label,  status,  threadid);
                if (type ==
                containers_hash.put(new Integer(id), cont);
            }
           
            
            
            return containers;
        } catch (Exception e)
        {
            throw new BecDatabaseException("Error occured while initializing container with id: "+id+"\n"+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(crs);
        }
        
        
    }
    
    
    
       // function returns array of containers of define type 
    // that have status one in container statuses 
// initially submitted by user
   
    public static ArrayList getContainersBySubmitter (  String container_type , String status, int userid)
                throws BecDatabaseException
    {
        ArrayList containers = new ArrayList();
        Container cont = null;
        
        String status = ""; String sql = null;
        for (int count = 0; count < container_status.length; count ++)
        {
            status += container_status[count] + ",";
        }
        
        if (user == null && ! mode)
        {
             sql = "select c.containerid as containerid, c.threadid as threadid, c.status as status, " +
            "c.containertype as containertype, c.label as label, c.locationid as locationid, "+
            " from containerheader c, "+
            "where containertype = " + container_type 
            " and status in (" + status + ")";
        }
        else if(user == null && mode)
         {
             sql = "select c.containerid as containerid, c.threadid as threadid, c.status as status, " +
            "c.containertype as containertype, c.label as label, c.locationid as locationid, "+
            " from containerheader c, "+
            "where status in (" + status + ")";
         }
        /*
        else if (user != null && ! mode)
        {
            sql = "select c.containerid as containerid, c.threadid as threadid, c.status as status, " +
            "c.containertype as containertype, c.label as label, c.locationid as locationid, "+
            " from containerheader c, request r, process p, processobject po "+
            "where r.requestid = p.requestid and p.executionid = po.executionid and " +
            " po.objecttype = " + ProcessObject.OBJECT_TYPE_CONTAINER +
            " and c.containerid = po.objectid " +
            " and c.status in (" + status + ") and c.containertype = " + container_type "+
            " and r.resercherid = " + user.getId();
            
        }
        else if(user != null && mode)
         {
             sql = "select c.containerid as containerid, c.threadid as threadid, c.status as status, " +
            "c.containertype as containertype, c.label as label, c.locationid as locationid, "+
            " from containerheader c, request r, process p, processobject po "+
            "where r.requestid = p.requestid and p.executionid = po.executionid and " +
            " po.objecttype = " + ProcessObject.OBJECT_TYPE_CONTAINER +
            " and c.containerid = po.objectid " +
            " and status in (" + status + ") and r.resercherid = " + user.getId();
         }
        
        String type = null; int locationid = -1; String label = null; int status = -1;int id = -1;
        CachedRowSet crs = null;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            crs = t.executeQuery(sql);
            
            while(crs.next())
            {
                id = crs.getInt("CONTAINERID");
                type = crs.getString("CONTAINERTYPE");
                locationid = crs.getInt("LOCATIONID");
                label = crs.getString("LABEL");
                threadid = crs.getInt("THREADID");
                status = crs.getInt("");
                cont = new Container( id,  type,  locationid,  label,  status,  threadid);
                containers_hash.put(new Integer(id), cont);
            }
           
            
            
            return containers;
        } catch (Exception e)
        {
            throw new BecDatabaseException("Error occured while initializing container with id: "+id+"\n"+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(crs);
        }
        
        
    }
    
    
    
       // function returns array of containers of define type 
    // that have status one in container statuses
   
    public static ArrayList getContainersBySubmitter
                (  String container_type , String status)
                throws BecDatabaseException
    {
        ArrayList containers = new ArrayList();
        Container cont = null;
        
        String status = ""; String sql = null;
        for (int count = 0; count < container_status.length; count ++)
        {
            status += container_status[count] + ",";
        }
        
        if (user == null && ! mode)
        {
             sql = "select c.containerid as containerid, c.threadid as threadid, c.status as status, " +
            "c.containertype as containertype, c.label as label, c.locationid as locationid, "+
            " from containerheader c, "+
            "where containertype = " + container_type 
            " and status in (" + status + ")";
        }
        else if(user == null && mode)
         {
             sql = "select c.containerid as containerid, c.threadid as threadid, c.status as status, " +
            "c.containertype as containertype, c.label as label, c.locationid as locationid, "+
            " from containerheader c, "+
            "where status in (" + status + ")";
         }
        /*
        else if (user != null && ! mode)
        {
            sql = "select c.containerid as containerid, c.threadid as threadid, c.status as status, " +
            "c.containertype as containertype, c.label as label, c.locationid as locationid, "+
            " from containerheader c, request r, process p, processobject po "+
            "where r.requestid = p.requestid and p.executionid = po.executionid and " +
            " po.objecttype = " + ProcessObject.OBJECT_TYPE_CONTAINER +
            " and c.containerid = po.objectid " +
            " and c.status in (" + status + ") and c.containertype = " + container_type "+
            " and r.resercherid = " + user.getId();
            
        }
        else if(user != null && mode)
         {
             sql = "select c.containerid as containerid, c.threadid as threadid, c.status as status, " +
            "c.containertype as containertype, c.label as label, c.locationid as locationid, "+
            " from containerheader c, request r, process p, processobject po "+
            "where r.requestid = p.requestid and p.executionid = po.executionid and " +
            " po.objecttype = " + ProcessObject.OBJECT_TYPE_CONTAINER +
            " and c.containerid = po.objectid " +
            " and status in (" + status + ") and r.resercherid = " + user.getId();
         }
        
        String type = null; int locationid = -1; String label = null; int status = -1;int id = -1;
        CachedRowSet crs = null;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            crs = t.executeQuery(sql);
            
            while(crs.next())
            {
                id = crs.getInt("CONTAINERID");
                type = crs.getString("CONTAINERTYPE");
                locationid = crs.getInt("LOCATIONID");
                label = crs.getString("LABEL");
                threadid = crs.getInt("THREADID");
                status = crs.getInt("");
                cont = new Container( id,  type,  locationid,  label,  status,  threadid);
                containers_hash.put(new Integer(id), cont);
            }
           
            
            
            return containers;
        } catch (Exception e)
        {
            throw new BecDatabaseException("Error occured while initializing container with id: "+id+"\n"+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(crs);
        }
        
        
    }
    /**
     * Update the threadid record to the database.
     *
     * @param threadid The threadid to be updated to.
     * @param c The connection used for database update
     * @exception The BecDatabaseException.
     */
    
    /*
    public void updateThreadid(int threadid, Connection c) throws BecDatabaseException
    {
        String sql = "UPDATE containerheader\n"+
        " SET threadid="+threadid+
        " WHERE containerid="+m_id;
        
        DatabaseTransaction.executeUpdate(sql, c);
    } //updateThreadid
    */
   
    /**
     * Static method to find the next container from the previous one
     * for certain protocol.
     *
     * @param mgc The rearrayed MGC container.
     * @return The DNA template plate. Return null if not found.
     */
    
    /*
    public static Container findNextContainerFromPrevious(Container mgc, String protocolName)
    {
        String sql = "select c.containerid as containerid"+
        " from containerheader c, processobject o"+
        " where c.containerid = o.containerid"+
        " and o.inputoutputflag = '"+ProcessObject.OUTPUT+"'"+
        " and o.executionid in ("+
        " select executionid from processobject"+
        " where executionid in ("+
        " select e.executionid"+
        " from processexecution e, processprotocol p"+
        " where e.protocolid = p.protocolid"+
        " and p.processname='"+ protocolName +"'"+
        " ) and inputoutputflag = '"+ProcessObject.INPUT+"'"+
        " and containerid = "+mgc.getId()+
        " )  and c.locationid <> " + Location.CODE_DESTROYED;
        
        DatabaseTransaction t = null;
        CachedRowSet crs = null;
        Container c = null;
        try
        {
            t = DatabaseTransaction.getInstance();
            crs = t.executeQuery(sql);
            if(crs.next())
            {
                int containerid = crs.getInt("CONTAINERID");
                c = new Container(containerid);
            }
        } catch (Exception ex)
        {
            System.out.println(ex);
        } finally
        {
            DatabaseTransaction.closeResultSet(crs);
        }
        
        return c;
    }
    
  
    /**
     * Static method to find the next container from the previous one
     * for certain protocol.
     *
     * @param id The  container id.
     * @return The DNA template plate. Return null if not found.
     */
    
    /*
    public static Container findNextContainerFromPrevious(int id, String protocolName)
    {
        String sql = "select c.containerid as containerid "+
        " from containerheader c, processobject o"+
        " where c.containerid = o.containerid"+
        " and o.inputoutputflag = '"+ProcessObject.OUTPUT+"'"+
        " and o.executionid in ("+
        " select executionid from processobject"+
        " where executionid in ("+
        " select e.executionid"+
        " from processexecution e, processprotocol p"+
        " where e.protocolid = p.protocolid"+
        " and p.processname='"+ protocolName +"'"+
        " ) and inputoutputflag = '"+ProcessObject.INPUT+"'"+
        " and containerid = "+ id +
        " ) and c.locationid <> " + Location.CODE_DESTROYED;
        
        DatabaseTransaction t = null;
        CachedRowSet crs = null;
        Container c = null;
        try
        {
            t = DatabaseTransaction.getInstance();
            crs = t.executeQuery(sql);
            if(crs.next())
            {
                int containerid = crs.getInt("CONTAINERID");
                c = new Container(containerid);
            }
            
            
        } catch (Exception ex)
        {
            System.out.println(ex);
        } finally
        {
            DatabaseTransaction.closeResultSet(crs);
        }
        
        return c;
    }
    
    /**
     * Static method to find the DNA template plate from the rearrayed
     * MGC container.
     *
     * @param mgc The rearrayed MGC container.
     * @return The DNA template plate. Return null if not found.
     */
    /**    public static Container findTemplateFromRearrayedMgc(Container mgc) {
        String sql = "select c.containerid as containerid"+
                    " from containerheader c, processobject o"+
                    " where c.containerid = o.containerid"+
                    " and o.inputoutputflag = '"+ProcessObject.OUTPUT+"'"+
                    " and o.executionid in ("+
                    " select executionid from processobject"+
                    " where executionid in ("+
                    " select e.executionid"+
                    " from processexecution e, processprotocol p"+
                    " where e.protocolid = p.protocolid"+
                    " and p.processname='"+Protocol.CREATE_DNA_FROM_REARRAYED_CULTURE+"'"+
                    " ) and inputoutputflag = '"+ProcessObject.INPUT+"'"+
                    " and containerid = "+mgc.getId()+
                    " )";
 
        DatabaseTransaction t = null;
        CachedRowSet crs = null;
        Container c = null;
        try {
            t = DatabaseTransaction.getInstance();
            crs = t.executeQuery(sql);
            if(crs.next()) {
                int containerid = crs.getInt("CONTAINERID");
                c = new Container(containerid);
            }
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            DatabaseTransaction.closeResultSet(crs);
        }
 
        return c;
    }
     */
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
                    +" and vectorprimerid in (select configid from processconfig where "
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
                    forward.setStart(position); // for full sequencing, start of the prime regarding sequence start
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
                    reverse.setStart(position); // for full sequencing, start of the prime regarding sequence start
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
     
      public static ArrayList getProcessHistoryItems(String label)throws BecDatabaseException
      
      {
          
          Container cont = findContainerDescriptionFromLabel(label);
          if ( cont != null)
            return getProcessHistory( cont.getId());
          else return null;
      }
    
      private static ArrayList getProcessHistory(int containerid)throws BecDatabaseException
      
      {
          //for upload process
          String sql = "select p.EXECUTIONDATE as EXECUTIONDATE ,pd.processname as processname,p.processid as processid, "
+"(select username from userprofile where userid=( select researcherid from request where requestid = "
+" p.requestid)) as username,(select configtype from processconfig where processid= p.processid) as configtype, "
+"(select configid from processconfig where processid= p.processid) as configid from process p, processdefinition pd, userprofile "
+"where pd.processdefinitionid=p.processdefinitionid and processid= "
+"(select processid from process_object where objectid = "+containerid+" and objecttype=0) order by EXECUTIONDATE desc";

          ArrayList items = new ArrayList();
          ArrayList item = new ArrayList();
          DatabaseTransaction t = null;
          RowSet crs = null; RowSet crs1 = null;
         ProcessHistory pr_history = null;
        try
        {
             t = DatabaseTransaction.getInstance();
              crs = t.executeQuery(sql);
            while(crs.next())
            {
                pr_history= new ProcessHistory();
                pr_history.setId ( crs.getInt("processid") );
                pr_history.setName ( crs.getString("processname") );
                pr_history.setDate ( crs.getString("EXECUTIONDATE") );
                pr_history.setUsername ( crs.getString("username") );
                pr_history.addConfis (crs.getInt("configid"), crs.getInt("configtype"));
              
                items.add(pr_history);
               
            }
              
              //for all other first find process info
      sql="select p.processid as processid, p.EXECUTIONDATE as EXECUTIONDATE ,pd.processname as processname,(select username from userprofile where userid= "
+"( select researcherid from request where requestid = p.requestid)) as username "
+"from process p, processdefinition pd where pd.processdefinitionid=p.processdefinitionid and "
+"processid= (select processid from process_object where objectid in (select min(resultid) from result where sampleid in "
+"(select sampleid from sample where containerid="+containerid+")) and objecttype=1)";
              crs = t.executeQuery(sql);
            while(crs.next())
            {
                 pr_history= new ProcessHistory();
                pr_history.setId ( crs.getInt("processid") );
                pr_history.setName ( crs.getString("processname") );
                pr_history.setDate ( crs.getString("EXECUTIONDATE") );
                pr_history.setUsername ( crs.getString("username") );
                sql="select CONFIGID,   CONFIGTYPE from processconfig  where processid="+pr_history.getId();
                crs1 = t.executeQuery(sql);
                while(crs1.next())
                {
                    pr_history.addConfis (crs1.getInt("configid"), crs1.getInt("configtype"));
                }
                items.add(pr_history);
                              
            }
              
  sql="select p.processid as processid,p.EXECUTIONDATE as EXECUTIONDATE ,pd.processname as processname,(select username from userprofile where userid= "
+"( select researcherid from request where requestid ="
+" p.requestid)) as username from process p, processdefinition pd where pd.processdefinitionid=p.processdefinitionid and "
+"processid=(select processid from process_object where objectid in (select min(constructid) from isolatetracking where sampleid in "
+"(select sampleid from sample where containerid="+containerid+")) and objecttype=2)";
              crs = t.executeQuery(sql);
            while(crs.next())
            {
                 pr_history= new ProcessHistory();
                pr_history.setId ( crs.getInt("processid") );
                pr_history.setName ( crs.getString("processname") );
                pr_history.setDate ( crs.getString("EXECUTIONDATE") );
                pr_history.setUsername ( crs.getString("username") );
                sql="select CONFIGID,   CONFIGTYPE from processconfig  where processid="+pr_history.getId();
                crs1 = t.executeQuery(sql);
                while(crs1.next())
                {
                    pr_history.addConfis (crs1.getInt("configid"), crs1.getInt("configtype"));
                }
                items.add(pr_history);
                              
            }

            
            return items;
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException("Error occured while initializing container history\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(crs1);
            DatabaseTransaction.closeResultSet(crs);
        }
          
      }
    //**************************************************************//
    //				Test				//
    //**************************************************************//
    
    // These test cases also include tests for Sample class.
    public static void main(String args[]) throws Exception
    {
       ArrayList c  = null;Container container =null;
        try
        {
              container = Container.findContainerDescriptionFromLabel("PGS000121-1");
           
             container.restoreSampleIsolate(false,true);
             
             int i=container.getCloningStrategyId();
             
        }
        catch(Exception e)
        {
        }
        System.exit(0);
        /*
        System.out.println("\nCreate new container with label = CPL10.3");
        Container c = new Container("CPL10.3");
        c.restoreSample();
        System.out.println(c.getSamples());
        Iterator sampleIter = c.getSamples().iterator();
        while(sampleIter.hasNext()) {
            System.out.println("sample: " + sampleIter.next());
        }
         
        try{
            Container c = new Container(40); //containerid = 40
            LinkedList fList = c.getFileReferences();
            ListIterator iter = fList.listIterator();
            System.out.println("Get file references for container 40:");
            while (iter.hasNext()) {
                FileReference f = (FileReference) iter.next();
                System.out.print("FileReference ID: "+f.getId()+ "\n");
                System.out.println("FileType: "+f.getFileType());
                System.out.println("LocalPath: "+ f.getLocalPath());
                System.out.println("FileName: "+ f.getBaseName());
            } //while
         
        }catch(BecDatabaseException ex){
            System.out.println(ex.getMessage());
        }
         
         
        Location l = new Location(1, "testlocation", "testlocation");
        RowSet rs = null;
        try {
            Container plate = new Container("test", l, "Test1");
         
            int containerid = plate.getId();
            for(int i=1; i<=5; i++) {
                Sample s = new Sample("PCR", i, containerid);
                s.setConstructid(100);
                plate.addSample(s);
            }
         
            System.out.println("Plate ID:\t" + plate.getId());
            Location location = plate.getLocation();
            System.out.println("Plate Location:\t" + location.getType());
            System.out.println("Plate Type:\t" + plate.getType());
            System.out.println("Plate Label:\t" + plate.getLabel());
         
         
            ArrayList samples = plate.getSamples();
            Enumeration enum = samples.elements();
            while (enum.hasMoreElements()){
                Sample sample = (Sample)enum.nextElement();
                System.out.println("\tSample Container Id:\t"+sample.getContainerid());
                System.out.println("\tSample ID:\t"+sample.getId());
                System.out.println("\tSample Type:\t"+sample.getType());
                System.out.println("\tSample Position:\t"+sample.getPosition());
                System.out.println("\tSample is Control:\t"+sample.isControl());
                System.out.println("\tSample is empty:\t"+sample.isEmpty());
                System.out.println("\tSample construct id:\t"+sample.getConstructid());
                System.out.println("\tSample olig id:\t"+sample.getOligoid());
                System.out.println("\tSample status:\t"+sample.getStatus());
            }
         
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            Connection conn = t.requestConnection();
         
            //t.executeUpdate("insert into containerlocation values (10, 'testlocation', 'testlocation')", conn);
            //t.executeUpdate("insert into containertype values ('test')",conn);
            //t.executeUpdate("insert into flexstatus values ('test')",conn);
            //t.executeUpdate("insert into species values ('human')",conn);
            //t.executeUpdate("insert into flexsequence values (1, 'test', 'human', 10, 100,null,null,null,null)",conn);
            //t.executeUpdate("insert into constructtype values ('open')",conn);
            //t.executeUpdate("insert into oligo values (10, 'ATCG', 30, null)",conn);
            //t.executeUpdate("insert into oligo values (11, 'ATCG', 30, null)",conn);
            //t.executeUpdate("insert into cdnalibrary values (1000, 'test')",conn);
            //t.executeUpdate("insert into constructdesign values (100, 1, 10, 11, 'open', 'small',null,null)",conn);
            //t.executeUpdate("insert into sampletype values ('PCR')",conn);
            plate.insert(conn);
         
            System.out.println("Insert into containerheader:");
            rs = t.executeQuery("select * from containerheader where containerid="+containerid);
         
         
            while(rs.next()) {
                for(int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    String column = rs.getMetaData().getColumnName(i);
                    System.out.println(column + "\t" + rs.getObject(column));
                }
         
            }
         
            System.out.println("Insert into sample:");
            rs = t.executeQuery("select * from sample where containerid="+containerid);
         
            while(rs.next()) {
                for(int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    String column = rs.getMetaData().getColumnName(i);
                    System.out.println(column + "Sample:\t" + rs.getObject(column));
                }
            }
         
         
            System.out.println("\nCreate new container with containerid = "+containerid);
            Container p1 = new Container(10);
            System.out.println("Plate ID:\t" + p1.getId());
            System.out.println("Plate Location:\t" + p1.getLocation().getType());
            System.out.println("Plate Type:\t" + p1.getType());
            System.out.println("Plate Label:\t" + p1.getLabel());
         
         
         
            System.out.println("\nSamples on p1 after restore:");
            p1.restoreSample();
            ArrayList s1 = p1.getSamples();
            enum = s1.elements();
            while (enum.hasMoreElements()){
                Sample sample = (Sample)enum.nextElement();
                System.out.println("\tSample Container Id:\t"+sample.getContainerid());
                System.out.println("\tSample ID:\t"+sample.getId());
                System.out.println("\tSample Type:\t"+sample.getType());
                System.out.println("\tSample Position:\t"+sample.getPosition());
                System.out.println("\tSample is Control:\t"+sample.isControl());
                System.out.println("\tSample is empty:\t"+sample.isEmpty());
                System.out.println("\tSample construct id:\t"+sample.getConstructid());
                System.out.println("\tSample olig id:\t"+sample.getOligoid());
                System.out.println("\tSample status:\t"+sample.getStatus());
            }
         
         
         
            Container containTest = new Container(1288);
            Iterator fileIter = containTest.getFileReferences().iterator();
            System.out.println("Files for 1288");
            while(fileIter.hasNext()) {
                System.out.println(fileIter.next());
            }
         
            conn.rollback();
            conn.close();
         
            //System.out.println(Container.getLabel("Bp", 12, "f"));
        } catch (BecDatabaseException exception) {
            System.out.println(exception.getMessage());
        } catch (BecUtilException e) {
            System.out.println(e.getMessage());
        } catch (SQLException sqlE) {
            System.err.println(sqlE.getMessage());
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            System.exit(0);
        }
         */
    }
}
