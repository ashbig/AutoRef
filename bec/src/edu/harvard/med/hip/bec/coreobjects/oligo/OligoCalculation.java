package edu.harvard.med.hip.bec.coreobjects.oligo;

import  edu.harvard.med.hip.bec.util.*;
import  edu.harvard.med.hip.bec.*;
import  edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import java.sql.*;
import java.util.*;

/**
 * This class represents array of oligo pairs calculated for full sequencing process
 for gene sequence
 under predefined conditions.
 
 */
public class OligoCalculation
{
   
    private ArrayList           m_oligos = null;
    private Primer3Spec         m_primer3_spec = null;
    private int         m_primer3_spec_id = -1;
    private RefSequence        m_refsequence = null;
    private int                 m_refsequence_id = -1;
    private int                 m_id = -1;
    private java.util.Date      m_date = null;
   
    private int m_result_id = -1;
    
    public OligoCalculation(){}
    public OligoCalculation(int id, ArrayList oligos, int condid, int seqid, int resultid) throws BecDatabaseException
    {
       m_oligos = oligos;
       m_primer3_spec_id = condid;
       m_refsequence_id = seqid;
      
       if (id == BecIDGenerator.BEC_OBJECT_ID_NOTSET)
            m_id = BecIDGenerator.getID("oligoid");
        else
            m_id = id;
      m_result_id = resultid;
    }
    
  
       
    //////////////////////////////getters & setters ////////////////////////////
    public int     getId()    { return  m_id ;}
    public int     getPrimer3SpecId()    {   return m_primer3_spec_id;    }
    public int              getResultId(){ return m_result_id;}
    public RefSequence    getSequence()  throws BecDatabaseException 
    {   
        if (m_refsequence == null) m_refsequence = new RefSequence(m_refsequence_id);
        return m_refsequence;  
    }
    public int             getSequenceId(){ return m_refsequence_id; }
    public ArrayList        getOligos(){ return m_oligos;}
      public java.util.Date   getDate(){ return m_date;}
    
    
    
    
    
    public void     addOligo(Oligo s)    {  if ( m_oligos == null) m_oligos = new ArrayList();  m_oligos.add( s);    }
    public void     setPrimer3Spec(Primer3Spec s)    {    m_primer3_spec = s;    }
    public void     setPrimer3SpecId(int s)    {    m_primer3_spec_id= s;    }
    public void     setSequence(RefSequence s)    {    m_refsequence = s;    }
    public void     setSequenceId(int s)    {    m_refsequence_id = s;    }
    public void      setDate(java.util.Date  v ){  m_date = v;}
    public void     setResultId(int v)    {   m_result_id = v;}
    protected void     setId(int v)    {   m_id = v;}
   public void         setOligos(ArrayList v){  m_oligos = v;}
    
    //function gets all oligo calculations from db for the provided id
    public static ArrayList       getOligoCalculations(String item_ids,int item_type)                     throws BecDatabaseException
    {
        String sql = null;
        ArrayList oligo_calculations = new ArrayList();
        ArrayList items = Algorithms.splitString(item_ids);
        if (items == null || items.size() < 1) return oligo_calculations;
        for (int index = 0; index < items.size(); index++)
        {
            switch (item_type)
            {
                case  Constants.ITEM_TYPE_CLONEID:
                {
                    oligo_calculations.addAll(getByCloneId(Integer.parseInt( (String)items.get(index) )));
                    break;
                }
                case Constants.ITEM_TYPE_PLATE_LABELS :
                {   
                    oligo_calculations.addAll(getByPlateLabel((String) items.get( index)));
                    break;
                }
                case Constants.ITEM_TYPE_BECSEQUENCE_ID :
                {   
                    oligo_calculations.addAll(getByRefSequenceId(Integer.parseInt( (String)items.get( index))));
                    break;
                }
                case Constants.ITEM_TYPE_FLEXSEQUENCE_ID:
                {   
                    oligo_calculations.addAll(getByFlexSequenceId(Integer.parseInt( (String)items.get(index))));
                    break;
                }
            }
        }
        
        return oligo_calculations;
        
    }
   
    
   
   
    
    public void insert(Connection conn) throws BecDatabaseException
    {
        Statement stmt = null;
         String sql = null;
        try
        {
            if ( m_oligos == null || m_oligos.size() <1) return;
          if (m_id == BecIDGenerator.BEC_OBJECT_ID_NOTSET)
                 m_id = BecIDGenerator.getID("oligoid");
             sql = "INSERT INTO oligo_calculation (oligocalculationid, sequenceid, primer3configid, dateadded,resultid) "+
            " VALUES("+ m_id+"," + m_refsequence_id +","+m_primer3_spec_id +",sysdate,"+m_result_id +")";
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            for (int count = 0; count < m_oligos.size(); count++)
            {
               Oligo op = (Oligo)m_oligos.get(count);
               op.setOligoCalculationId( m_id);
               op.insert(conn);
            }
        } catch (Exception sqlE)
        {
              System.out.println("isert "+sqlE.getMessage()); 
            throw new BecDatabaseException(sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeStatement(stmt);
        }
    } //insertOligo
    
   public static ArrayList getByOligoCalculationId(int oligocalcid)throws BecDatabaseException
    {
        String sql = "select  oligocalculationid, sequenceid, primer3configid, dateadded "+
            " from oligo_calculation where oligocalculationid = "+oligocalcid;
        return getByRule(sql);
   }
    
    //can return sets calculated ander different configs for Primer3
   public static ArrayList getByRefSequenceId(int refsequenceid)throws BecDatabaseException
   {
        String sql = "select  oligocalculationid, sequenceid, primer3configid, dateadded  "+
            " from oligo_calculation where sequenceid = "+refsequenceid +" order by oligocalculationid";
        return getByRule(sql);
   }
    //can return sets calculated ander different configs for Primer3
   public static ArrayList getByFlexSequenceId(int flexsequenceid)throws BecDatabaseException
   {
        String sql = "select  oligocalculationid, sequenceid, primer3configid, dateadded   "
+" from  oligo_calculation where sequenceid = "
+" (select refsequenceid from sequencingconstruct where constructid =  (select constructid from isolatetracking where isolatetrackingid ="
+" (select isolatetrackingid from flexinfo where flexsequenceid="+flexsequenceid+"))) order by oligocalculationid";
        return getByRule(sql);
   }
   
   
    //can return sets calculated ander different configs for Primer3
   public static ArrayList getByCloneId(int cloneid)throws BecDatabaseException
   {
        String sql = "select  oligocalculationid, sequenceid, primer3configid, dateadded   "
+" from  oligo_calculation where sequenceid = "
+" (select refsequenceid from sequencingconstruct where constructid =  (select constructid from isolatetracking where isolatetrackingid ="
+" (select isolatetrackingid from flexinfo where flexcloneid="+cloneid+"))) order by oligocalculationid";
        return getByRule(sql);
   }
     //can return sets calculated ander different configs for Primer3
   public static ArrayList getByPlateLabel(String label)throws BecDatabaseException
   {
        String sql = "select  oligocalculationid, sequenceid, primer3configid, dateadded    "
+" from  oligo_calculation where sequenceid in "
+" (select refsequenceid from sequencingconstruct where constructid in  (select constructid from isolatetracking where sampleid in"
+" (select sampleid from sample  where containerid =(select containerid from containerheader where label ='"+label+"')))) order by oligocalculationid";
        return getByRule(sql);
   }
    
    private static ArrayList getByRule(String sql)throws BecDatabaseException
    {
        ArrayList res = new ArrayList();
        ResultSet rs = null;
         OligoCalculation oc = null;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);

            while(rs.next())
            {
                oc = new  OligoCalculation();
                oc.setPrimer3SpecId(rs.getInt("primer3configid"));
                oc.setSequenceId(rs.getInt("sequenceid"));
                oc.setDate(rs.getDate("dateadded"));
                oc.setId(rs.getInt("oligocalculationid"));
                oc.setOligos(Oligo.getByOligoCalculationId(oc.getId(), Oligo.STATUS_ANY));
                res.add(oc);
            }
            return res;
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException("Error occured while "+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }


    }
   
  //*********************************************************************
    
    public static void main(String [] args)
    {
        Connection c = null;
        int oligoid = 1;
        
        try
        {
             ArrayList oligo_calculations = new ArrayList();
             String item_ids = "9972\n9966\n9949\n9954\n581\n9990\n9938\n9929\n";
             ArrayList items = Algorithms.splitString(item_ids);
             ArrayList oligo_calculations_per_item = new ArrayList();
             for (int index = 0; index < items.size();index++)
             {
                oligo_calculations_per_item = OligoCalculation.getOligoCalculations((String)items.get(index),Constants.ITEM_TYPE_CLONEID);
                oligo_calculations.add( oligo_calculations_per_item);
             }
        }
        catch (BecDatabaseException exception)
        {
            System.out.println(exception.getMessage());
        }finally
        {
            DatabaseTransaction.closeConnection(c);
        }
    } //main
    
}




