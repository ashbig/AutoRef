/*
 * CloningStrategy.java
 *
 * Created on June 17, 2003, 1:35 PM
 */

package edu.harvard.med.hip.flex.core;

import edu.harvard.med.hip.flex.workflow.*;
import static edu.harvard.med.hip.flex.workflow.Workflow.WORKFLOW_TYPE;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.util.*;
import java.util.*;
import java.sql.*;

/**
 *
 * @author  dzuo
 */
public class CloningStrategy {
    public static final int YEAST_GATEWAY = 1;
    public static final int HUMAN_GATEWAY = 2;
    public static final int PSEUDOMONAS_GATEWAY = 3;
    public static final int CREATOR = 4;
    public static final int YEAST_GATEWAY_EXPRESSION = 5;
    public static final int HUMAN_GATEWAY_EXPRESSION = 6;
    public static final int PSEUDOMONAS_GATEWAY_EXPRESSION = 7;
    public static final int YP_GATEWAY = 8;
    public static final int CREATOR_EXPRESSION_JP1520 = 10;
    public static final int GATEWAY_PGW = 14;
    public static final int GATEWAY_PGW_2 = 23;
    public static final int CREATOR_EXPRESSION_PLP_DS_3xFlag = 15;
    public static final int CREATOR_EXPRESSION_PLP_DS_3xMyc = 16;
    public static final int GATEWAY_pCITE_GST = 17;
    public static final int GATEWAY_pDEST17 = 19;
    public static final int GATEWAY_EXPRESSION_pBY011 = 12;
    public static final int CREATOR_EXPRESSION_pLDNT7_nFLAG = 22;
    public static final int GATEWAY_EXPRESSION_pDEST_GST = 25;
    public static final int GATEWAY_EXPRESSION_pENTR223_pCITE_GST = 29;
    public static final int GATEWAY_EXPRESSION_pLENTI62_V5_Dest = 59;

    protected int id;
    protected String name;
    protected CloneVector clonevector;
    protected CloneLinker linker5p;
    protected CloneLinker linker3p;
    protected String type;
    
    /** Creates a new instance of CloningStrategy */
    public CloningStrategy() {
    }
    
    public CloningStrategy(CloningStrategy c) {
        if(c != null) {
            this.id = c.getId();
            this.name = c.getName();
            this.clonevector = new CloneVector(c.getClonevector());
            this.linker5p = new CloneLinker(c.getLinker5p());
            this.linker3p = new CloneLinker(c.getLinker3p());
            this.type = c.getType();
        }
    }
    
    public CloningStrategy(int id, String name, CloneVector clonevector, CloneLinker linker5p, CloneLinker linker3p) {
        this.id = id;
        this.name = name;
        this.clonevector = clonevector;
        this.linker5p = linker5p;
        this.linker3p = linker3p;
    }
    public CloningStrategy(int id, String name, CloneVector clonevector, 
            CloneLinker linker5p, CloneLinker linker3p, String type) {
        this.id = id;
        this.name = name;
        this.clonevector = clonevector;
        this.linker5p = linker5p;
        this.linker3p = linker3p;
        this.type = type;
    }
    public CloningStrategy(int id) {
        this.id = id;
    }
    
    public String toString()
    {
        return "Strategy name:" + name + " vector name: "+ clonevector.getName()
                +" linker 5p name: "+ linker5p.getName() +
                " linker 3p name: " + linker3p.getName() +" type: "+type;
     }
    
    public int getId() {return id;}
    public String getName() {return name;}
    public CloneVector getClonevector() {return clonevector;}
    public CloneLinker getLinker5p() {return linker5p;}
    public CloneLinker getLinker3p() {return linker3p;}
    public String getType() {return type;}
    
    
    public void             setName(String v) { name = v;}
    public void             setClonevector(CloneVector v) { clonevector  =v;}
    public void             setLinker5p(CloneLinker v) { linker5p = v;}
    public void             setLinker3p(CloneLinker v) { linker3p = v;}
    public void             setType(String v) { type = v;}

    public static CloningStrategy findStrategyByName(String name) {
        String sql = "Select * from cloningstrategy where strategyname='"+name+"'";
        DatabaseTransaction t = null;
        ResultSet rs = null;
        CloningStrategy s = null;
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            if(rs.next()) {
                int id = rs.getInt("STRATEGYID");
                int linkerid5p = rs.getInt("LINKERID_5P");
                int linkerid3p = rs.getInt("LINKERID_3P");
                String vectorname = rs.getString("VECTORNAME");
                String type = rs.getString("TYPE");
                s = new CloningStrategy(id, name, new CloneVector(vectorname), new CloneLinker(linkerid5p), new CloneLinker(linkerid3p));
            }
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        
        return s;
    }
    
    public static CloningStrategy findStrategyById(int strategyid) throws FlexDatabaseException {
        String sql = "Select * from cloningstrategy where strategyid="+strategyid;
        DatabaseTransaction t = null;
        ResultSet rs = null;
        CloningStrategy s = null;
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            if(rs.next()) {
                String name = rs.getString("STRATEGYNAME");
                int linkerid5p = rs.getInt("LINKERID_5P");
                int linkerid3p = rs.getInt("LINKERID_3P");
                String vectorname = rs.getString("VECTORNAME");
                String type = rs.getString("TYPE");
                s = new CloningStrategy(strategyid, name, new CloneVector(vectorname), new CloneLinker(linkerid5p), new CloneLinker(linkerid3p));
            }
        } catch (Exception ex) {
            System.out.println(ex);
            throw new FlexDatabaseException(ex);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        
        return s;
    }
    
    public static CloningStrategy findStrategyByVectorAndLinker(String vectorname, int linkerid5p, int linkerid3p) throws FlexDatabaseException {
        String sql = "Select * from cloningstrategy"+
        " where linkerid_5p=?"+
        " and linkerid_3p=?"+
        " and vectorname=?";
        DatabaseTransaction t = null;
        Connection c = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        CloningStrategy s = null;
        try {
            t = DatabaseTransaction.getInstance();
            c = t.requestConnection();
            stmt = c.prepareStatement(sql);
            stmt.setInt(1, linkerid5p);
            stmt.setInt(2, linkerid3p);
            stmt.setString(3, vectorname);
            rs = t.executeQuery(stmt);
            if(rs.next()) {
                int strategyid = rs.getInt("STRATEGYID");
                String name = rs.getString("STRATEGYNAME");
                String type = rs.getString("TYPE");
                s = new CloningStrategy(strategyid, name, new CloneVector(vectorname), new CloneLinker(linkerid5p), new CloneLinker(linkerid3p));
            }
        } catch (Exception ex) {
            System.out.println(ex);
            throw new FlexDatabaseException(ex);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeConnection(c);
        }
        
        return s;
    }
    
    
     public static int findStrategyByVectorAndLinker(String vectorname, String linkerid5p_name,
             String linkerid3p_name) throws FlexDatabaseException 
     {
        String sql = "Select * from cloningstrategy"+
        " where linkerid_5p=(select linkerid from linker where linkername = ?)"+
        " and linkerid_3p=(select linkerid from linker where linkername = ?)"+
        " and vectorname=?";
        DatabaseTransaction t = null;
        Connection c = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int result = -1;
        try 
        {
            t = DatabaseTransaction.getInstance();
            c = t.requestConnection();
            stmt = c.prepareStatement(sql);
            stmt.setString(1, linkerid5p_name);
            stmt.setString(2, linkerid3p_name);
            stmt.setString(3, vectorname);
            rs = t.executeQuery(stmt);
            if(rs.next())
            {
                result =  rs.getInt("STRATEGYID");
              //  String name = rs.getString("STRATEGYNAME");
              //  String type = rs.getString("TYPE");
               }
            return result;
        } 
        catch (Exception ex) 
        {
            System.out.println(ex);
            throw new FlexDatabaseException(ex);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeConnection(c);
        }
    
    }
    
    public static List<CloningStrategy> getAllCloningStrategies() 
    {
        String sql = "select strategyid, strategyname, vectorname, linkerid_5p, "
+"(select linkername from linker where linkerid=linkerid_5p) as linker_5p_name, linkerid_3p, "
+" (select linkername from linker where linkerid=linkerid_3p) as linker_3p_name, type "
 +" from cloningstrategy order by strategyid";
        return getAllCloningStrategies(sql, false,false);
    }
    
    public static List<CloningStrategy> getAllCloningStrategies(boolean isLinkerSequence) 
    {
        String sql = "select strategyid, strategyname, vectorname, linkerid_5p, "
+"(select linkername from linker where linkerid=linkerid_5p) as linker_5p_name,(select linkersequence from linker where linkerid=linkerid_5p) as linker_5p_sequence, linkerid_3p, "
+" (select linkername from linker where linkerid=linkerid_3p) as linker_3p_name, (select linkersequence from linker where linkerid=linkerid_3p) as linker_3p_sequence, type "
 +" from cloningstrategy order by strategyid";
        return getAllCloningStrategies(sql, isLinkerSequence, false);
    }
     public static List<CloningStrategy> getAllCloningStrategies(boolean isLinkerSequence,boolean isVectorDetails) 
    {
        String sql = "select strategyid, strategyname, vectorname, linkerid_5p, "
+"(select linkername from linker where linkerid=linkerid_5p) as linker_5p_name,(select linkersequence from linker where linkerid=linkerid_5p) as linker_5p_sequence, linkerid_3p, "
+" (select linkername from linker where linkerid=linkerid_3p) as linker_3p_name, (select linkersequence from linker where linkerid=linkerid_3p) as linker_3p_sequence, type "
 +" from cloningstrategy order by strategyid";
        return getAllCloningStrategies(sql, isLinkerSequence, isVectorDetails);
    }
    
    
    
     public static List<CloningStrategy> getAllCloningStrategies(String sql, boolean isLinkerSequence, boolean isVectorDetails) 
    {
       
        List<CloningStrategy> l = new ArrayList<CloningStrategy>();
        DatabaseTransaction t = null;String lsequence_5p = ""; String lsequence_3p = "";
        ResultSet rs = null;
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while (rs.next()) 
            {
                if ( isLinkerSequence )
                {
                    lsequence_5p=rs.getString("linker_5p_sequence");
                    lsequence_3p=rs.getString("linker_3p_sequence");
                }
                CloningStrategy c = new CloningStrategy(rs.getInt("strategyid"), 
                        rs.getString("strategyname"),
                        new CloneVector(rs.getString("vectorname")), 
                        new CloneLinker(rs.getInt("linkerid_5p"), rs.getString("linker_5p_name"),lsequence_5p),
                        new CloneLinker(rs.getInt("linkerid_3p"), rs.getString("linker_3p_name"),lsequence_3p),
                        rs.getString("type"));
                if ( isVectorDetails)
                {
                    CloneVector vector = c.getClonevector();
                    vector.restore( vector.getName());
                    c.setClonevector(vector);
                }
                l.add(c);
            }
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        return l;
    }
    
    public static List getAllDestStrategy()
    {
        String sql = "select * from cloningstrategy where type='destination plasmid'";
        List l = new ArrayList();
        DatabaseTransaction t = null;
        ResultSet rs = null;
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while (rs.next()) {
                int strategyid = rs.getInt(1);
                String strategyname = rs.getString(2);
                int linkerid5p = rs.getInt(3);
                int linkerid3p = rs.getInt(4);
                String vector = rs.getString(5);
                CloningStrategy c = new CloningStrategy(strategyid, strategyname, new CloneVector(vector), new CloneLinker(linkerid5p), new CloneLinker(linkerid3p));
                l.add(c);
            }
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        return l;
    }
    
    public static int getStrategyid(int projectid, int workflowid ) throws Exception
    {
        Workflow cur_wf =new Workflow(workflowid);
        if(cur_wf != null )
        {
            
            String return_value ;
            String key =""+ projectid+ProjectWorkflowProtocolInfo.PWP_SEPARATOR+workflowid+ProjectWorkflowProtocolInfo.PWP_SEPARATOR
                    +"-1"+ProjectWorkflowProtocolInfo.PWP_SEPARATOR+"CLONING_STRATEGY_ID";
            return_value= ProjectWorkflowProtocolInfo.getInstance().getPWPProperties().get( key);
//System.out.println("ll "+key +" "+return_value);  
            if (return_value==null)
            {
               key ="-1"+ProjectWorkflowProtocolInfo.PWP_SEPARATOR+workflowid+ProjectWorkflowProtocolInfo.PWP_SEPARATOR
                        +"-1"+ProjectWorkflowProtocolInfo.PWP_SEPARATOR+"CLONING_STRATEGY_ID";
               return_value =  ProjectWorkflowProtocolInfo.getInstance().getPWPProperties().get(key);
            }
//System.out.println("ll "+key +" "+return_value);  

             if (return_value!=null)
            {
                try {return Integer.parseInt(return_value);}catch(Exception e){ return 0;}
            }
          
        }
        
        return 0;
    }
    
    
    public static void            insertCloningStrategies(Collection cloning_strategies,Connection conn) 
            throws Exception
    {
        String sql_strategy = "insert into cloningstrategy (strategyid, strategyname, linkerid_5p, linkerid_3p, vectorname, type   )"+
        " values(strategyid.nextval,?,(select linkerid from linker where linkername=?),(select linkerid from linker where linkername=?),?,?)";
  
       PreparedStatement stmt_strategy = null;
       CloningStrategy  cl_strategy = null;
       String temp = null;
        try {
            stmt_strategy = conn.prepareStatement(sql_strategy);
            Iterator iter = cloning_strategies.iterator();
            while(iter.hasNext())
            {
                cl_strategy = (CloningStrategy) iter.next();
                temp = (cl_strategy.getName() == null) ? "strategy "+ cl_strategy.getClonevector().getName()
                : cl_strategy.getName();
                if ( temp.length() > 149) temp = temp.substring(0,149);
               /*sql_strategy = "insert into cloningstrategy (strategyid, strategyname, linkerid_5p, linkerid_3p, vectorname, type   )"+
        " values(strategyid.nextval,'"+temp
 +"',(select linkerid from linker where linkername='"+cl_strategy.getLinker5p().getName()
 +"'),(select linkerid from linker where linkername='"+cl_strategy.getLinker3p().getName()+"'),'"
  + cl_strategy.getClonevector().getName()+ "','"+cl_strategy.getType()+"')";
  DatabaseTransaction.executeUpdate(sql_strategy,conn);
  /**/
                stmt_strategy.setString(1, temp);
                stmt_strategy.setString(2, cl_strategy.getLinker5p().getName() );
                stmt_strategy.setString(3, cl_strategy.getLinker3p().getName() );
                stmt_strategy.setString(4, cl_strategy.getClonevector().getName() );
                stmt_strategy.setString(5, cl_strategy.getType());
                
                DatabaseTransaction.executeUpdate(stmt_strategy);
             }
           
        } catch (Exception ex) {
            throw new Exception("Error occured while inserting cloning strategy info. \n "+ex.getMessage());
            
        }
         finally
         {
              DatabaseTransaction.closeStatement(stmt_strategy);
         }
      
    }
    
    public  void            insert(Connection conn) 
            throws FlexDatabaseException
    {
        if (id == -1)
            id = FlexIDGenerator.getID("strategyid");
        String sql = "insert into cloningstrategy (strategyid, strategyname, linkerid_5p, linkerid_3p, vectorname, type   )"+
        " values("+id+",'"+this.name+"',"+this.getLinker5p().getId()+","
                +this.getLinker3p().getId()+",'"+this.getClonevector().getName()
                +"','"+this.getType()+"')";
  
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
           
        } catch (SQLException sqlE) {
            throw new FlexDatabaseException(sqlE.getMessage()+"\nSQL: "+sql);
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }
      
    }
    
   
    
     
}
