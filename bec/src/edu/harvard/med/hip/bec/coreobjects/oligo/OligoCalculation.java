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
     public static final String   WRITE_PARAM_DELIM = "|";
     
     public static final int      TYPE_OF_OLIGO_CALCULATION_REFSEQUENCE = 1;
     public static final int      TYPE_OF_OLIGO_CALCULATION_STRETCH_COLLECTION = 2;
     public static final int      TYPE_OF_OLIGO_CALCULATION_ANY = 3;
   //--------------------------
    private ArrayList           m_oligos = null;
    private Primer3Spec         m_primer3_spec = null;
    private int                 m_primer3_spec_id = -1;
    private RefSequence         m_refsequence = null;
    private int                 m_refsequence_id = -1;
    private int                 m_id = -1;
    private java.util.Date      m_date = null;
    private int                 m_stretch_collection_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    private String              m_primer_design_for_stretches_additional_parameters = null;
    private Hashtable           i_stretch_calc_params = null;
    
    private int                 m_result_id = -1;
    
    public OligoCalculation(){}
    public OligoCalculation(int id, ArrayList oligos, int condid, int seqid, int resultid, int stretch_collection_id, String stretch_params) throws BecDatabaseException
    {
       m_oligos = oligos;
       m_primer3_spec_id = condid;
       m_refsequence_id = seqid;
      
       if (id == BecIDGenerator.BEC_OBJECT_ID_NOTSET)
            m_id = BecIDGenerator.getID("oligoid");
        else
            m_id = id;
      m_result_id = resultid;
      m_stretch_collection_id = stretch_collection_id ;
      m_primer_design_for_stretches_additional_parameters = stretch_params;
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
    public int              getStretchCollectioId(){ return m_stretch_collection_id;}
    public String           getStretchCollectionOligoCalculationParams(){ return m_primer_design_for_stretches_additional_parameters;}
    public Hashtable        getStretchCollectionOligoCalculationParamsAsHash()
    {
        String name = null;String name_value = null;
        String value = null;
        if ( i_stretch_calc_params != null ) return i_stretch_calc_params;
        if ( m_primer_design_for_stretches_additional_parameters == null ||
            m_primer_design_for_stretches_additional_parameters.length() == 0) return null;
        ArrayList params = Algorithms.splitString(m_primer_design_for_stretches_additional_parameters, WRITE_PARAM_DELIM);
        for (int count = 0; count < params.size(); count++)
        {
            name_value = (String)params.get(count);
            name = name_value.substring(0, name_value.indexOf("=") - 1);
            value = name_value.substring( name_value.indexOf("=") + 1);
            i_stretch_calc_params.put(name, value);
        }
        return i_stretch_calc_params;
    }
    
    
    public void     addOligos(ArrayList s)    {  if ( m_oligos == null) m_oligos = new ArrayList();  m_oligos.addAll( s);    }
    public void     addOligo(Oligo s)    {  if ( m_oligos == null) m_oligos = new ArrayList();  m_oligos.add( s);    }
    public void     setPrimer3Spec(Primer3Spec s)    {    m_primer3_spec = s;    }
    public void     setPrimer3SpecId(int s)    {    m_primer3_spec_id= s;    }
    public void     setSequence(RefSequence s)    {    m_refsequence = s;    }
    public void     setSequenceId(int s)    {    m_refsequence_id = s;    }
    public void      setDate(java.util.Date  v ){  m_date = v;}
    public void     setResultId(int v)    {   m_result_id = v;}
    protected void     setId(int v)    {   m_id = v;}
    public void         setStretchCollectionId(int v){ m_stretch_collection_id = v;}
   public void         setOligos(ArrayList v){  m_oligos = v;}
   public void        setStretchOligoCalculationParams(String s){ m_primer_design_for_stretches_additional_parameters=s;}
  
   
   /*
    //function gets all oligo calculations from db for the provided id
   public static ArrayList       getOligoCalculations(String item_ids,int item_type)            
            throws BecDatabaseException
   {
       return  getOligoCalculations( item_ids, item_type,TYPE_OF_OLIGO_CALCULATION_ANY, false);            
  
   }
    *
    **/
    public static ArrayList       getOligoCalculations(String item_ids,int item_type, 
            int type_of_oligo_calculation, boolean isLatestStretchCollection)            
            throws BecDatabaseException
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
                    oligo_calculations.addAll(getByCloneId(Integer.parseInt( (String)items.get(index) ), type_of_oligo_calculation, isLatestStretchCollection));
                    break;
                }
                case Constants.ITEM_TYPE_PLATE_LABELS :
                {   
                    oligo_calculations.addAll(getByPlateLabel((String) items.get( index),type_of_oligo_calculation, isLatestStretchCollection));
                    break;
                }
                case Constants.ITEM_TYPE_ACE_REF_SEQUENCE_ID :
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
        if (m_primer_design_for_stretches_additional_parameters!= null)
        {
             sql = "INSERT INTO oligo_calculation (oligocalculationid, sequenceid, primer3configid, dateadded,resultid, STRETCHCOLLECTIONID,STRETCHDEFPARAMS ) "+
            " VALUES("+ m_id+"," + m_refsequence_id +","+m_primer3_spec_id +",sysdate,"+m_result_id +","+ m_stretch_collection_id +",'"
            +m_primer_design_for_stretches_additional_parameters+"')";
        }
        else
        {
             sql = "INSERT INTO oligo_calculation (oligocalculationid, sequenceid, primer3configid, dateadded,resultid ) "+
            " VALUES("+ m_id+"," + m_refsequence_id +","+m_primer3_spec_id +",sysdate,"+m_result_id  +")";
        }
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
              System.out.println("insert "+sqlE.getMessage()); 
            throw new BecDatabaseException(sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeStatement(stmt);
        }
    } //insertOligo
    
   public static ArrayList getByOligoCalculationId(int oligocalcid)throws BecDatabaseException
    {
        String sql = "select  oligocalculationid, sequenceid, primer3configid, dateadded , STRETCHCOLLECTIONID, STRETCHDEFPARAMS "+
            " from oligo_calculation where oligocalculationid = "+oligocalcid;
        return getByRule(sql);
   }
    
    //can return sets calculated ander different configs for Primer3
   public static ArrayList getByRefSequenceId(int refsequenceid)throws BecDatabaseException
   {
        String sql = "select  oligocalculationid, sequenceid, primer3configid, dateadded , STRETCHCOLLECTIONID, STRETCHDEFPARAMS  "+
            " from oligo_calculation where sequenceid = "+refsequenceid +" order by oligocalculationid";
        return getByRule(sql);
   }
    //can return sets calculated ander different configs for Primer3
   public static ArrayList getByFlexSequenceId(int flexsequenceid)throws BecDatabaseException
   {
        String sql = "select  oligocalculationid, sequenceid, primer3configid, dateadded, STRETCHCOLLECTIONID,STRETCHDEFPARAMS    "
+" from  oligo_calculation where sequenceid = "
+" (select refsequenceid from sequencingconstruct where constructid =  (select constructid from isolatetracking where isolatetrackingid ="
+" (select max(isolatetrackingid) from flexinfo where flexsequenceid="+flexsequenceid+"))) order by oligocalculationid";
        return getByRule(sql);
   }
   
   
    //can return sets calculated ander different configs for Primer3
   public static ArrayList getByCloneId(int cloneid, int type_of_oligo_calculation, boolean isLatestStretchCollection)throws BecDatabaseException
   {
        String sql = null;
        if ( type_of_oligo_calculation == TYPE_OF_OLIGO_CALCULATION_REFSEQUENCE )
        {
            sql=         "select  oligocalculationid, sequenceid, primer3configid, dateadded , STRETCHCOLLECTIONID,STRETCHDEFPARAMS   "
+" from  oligo_calculation where STRETCHCOLLECTIONID is null and  sequenceid = "
+" (select refsequenceid from sequencingconstruct where constructid =  (select constructid from isolatetracking where isolatetrackingid ="
+" (select isolatetrackingid from flexinfo where flexcloneid="+cloneid+"))) order by oligocalculationid";

        }
        else if ( type_of_oligo_calculation ==    TYPE_OF_OLIGO_CALCULATION_STRETCH_COLLECTION )
        {
           sql=  "select  oligocalculationid, sequenceid, primer3configid, dateadded , STRETCHCOLLECTIONID,STRETCHDEFPARAMS   "
        +" from  oligo_calculation where STRETCHCOLLECTIONID in ";
               if ( isLatestStretchCollection )
               {
             sql +=" (select max(collectionid) from stretch_collection  where isolatetrackingid in "
            +" (select isolatetrackingid from flexinfo where flexcloneid="+cloneid+")) order by oligocalculationid";
               }
               else
               {
                   sql +=" (select collectionid from stretch_collection  where isolatetrackingid in "
            +" (select isolatetrackingid from flexinfo where flexcloneid="+cloneid+")) order by oligocalculationid";
               }
   
        }
        // does not use isLatestStretchCollection parameter, no invocation in current version
     else if ( type_of_oligo_calculation ==  TYPE_OF_OLIGO_CALCULATION_ANY )
     {
sql=         "select  oligocalculationid, sequenceid, primer3configid, dateadded , STRETCHCOLLECTIONID,STRETCHDEFPARAMS   "
+" from  oligo_calculation where sequenceid = "
+" (select refsequenceid from sequencingconstruct where constructid =  (select constructid from isolatetracking where isolatetrackingid ="
+" (select isolatetrackingid from flexinfo where flexcloneid="+cloneid+"))) order by oligocalculationid";

     }
        return getByRule(sql);
   }
     //can return sets calculated ander different configs for Primer3
   public static ArrayList getByPlateLabel(String label, int type_of_oligo_calculation, boolean isLatestStretchCollection)throws BecDatabaseException
   {
           String sql = null;
        if ( type_of_oligo_calculation == TYPE_OF_OLIGO_CALCULATION_REFSEQUENCE )
        {
       sql = "select  oligocalculationid, sequenceid, primer3configid, dateadded , STRETCHCOLLECTIONID,STRETCHDEFPARAMS    "
+" from  oligo_calculation where STRETCHCOLLECTIONID is null and sequenceid in "
+" (select refsequenceid from sequencingconstruct where constructid in  (select constructid from isolatetracking where sampleid in"
+" (select sampleid from sample  where containerid =(select containerid from containerheader where label ='"+label+"')))) order by oligocalculationid";
   
        }
        else if ( type_of_oligo_calculation ==    TYPE_OF_OLIGO_CALCULATION_STRETCH_COLLECTION )
        {
              sql = "select  oligocalculationid, sequenceid, primer3configid, dateadded , STRETCHCOLLECTIONID,STRETCHDEFPARAMS    "
            +" from  oligo_calculation where STRETCHCOLLECTIONID  in ";
               if ( isLatestStretchCollection )
               {
                   sql += " (select max(collectionid) from stretch_collection where isolatetrackingid in "
            +" (select isolatetrackingid from isolatetracking where sampleid in "
            +" (select sampleid from sample  where containerid in (select containerid from containerheader where label in ('"+label+"'))))) order by oligocalculationid";
               }
               else
               {
                   sql += " (select collectionid from stretch_collection where isolatetrackingid in "
            +" (select isolatetrackingid from isolatetracking where sampleid in "
            +" (select sampleid from sample  where containerid in (select containerid from containerheader where label in ('"+label+"'))))) order by oligocalculationid";
               }
        }
           
           
             // does not use isLatestStretchCollection parameter, no invocation in current version
    
     else if ( type_of_oligo_calculation ==  TYPE_OF_OLIGO_CALCULATION_ANY )
     {
  sql = "select  oligocalculationid, sequenceid, primer3configid, dateadded , STRETCHCOLLECTIONID,STRETCHDEFPARAMS    "
+" from  oligo_calculation where sequenceid in "
+" (select refsequenceid from sequencingconstruct where constructid in  (select constructid from isolatetracking where sampleid in"
+" (select sampleid from sample  where containerid in (select containerid from containerheader where label in ('"+label+"'))))) order by oligocalculationid";
    
     }
        
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
                oc.setStretchCollectionId(rs.getInt("STRETCHCOLLECTIONID"));
                oc.setStretchOligoCalculationParams( rs.getString("STRETCHDEFPARAMS"));
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
   
    public static ArrayList sortByRefSequenceIdPrimerSpec(ArrayList oligo_calculations)
    {
          //sort array by containerid and position
            Collections.sort(oligo_calculations, new Comparator() 
            {
                public int compare(Object o1, Object o2) 
                {
                    OligoCalculation cl1 = (OligoCalculation)o1;
                    OligoCalculation cl2 = (OligoCalculation)o2;
                    if (cl1.getSequenceId() != cl2.getSequenceId())
                        return cl1.getPrimer3SpecId() - cl2.getPrimer3SpecId();
                    else
                        return cl1.getSequenceId() - cl2.getSequenceId();
                 
                }
                /** Note: this comparator imposes orderings that are
                 * inconsistent with equals. */
                public boolean equals(java.lang.Object obj)
                {      return false;  }
                // compare
            } );
        
        return oligo_calculations;
    }
    
  //*********************************************************************
    
    public static void main(String [] args)
    {
        Connection c = null;
        int oligoid = 1;
        
        try
        {
             ArrayList oligo_calculations = new ArrayList();
             String item_ids = "140146    140130 ";
             ArrayList items = Algorithms.splitString(item_ids);
             ArrayList oligo_calculations_per_item = new ArrayList();
             for (int index = 0; index < items.size();index++)
             {
                oligo_calculations_per_item = OligoCalculation.getOligoCalculations((String)items.get(index),Constants.ITEM_TYPE_CLONEID, TYPE_OF_OLIGO_CALCULATION_REFSEQUENCE, true);
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




