/*
 * CloningStrategy.java
 *
 * Created on June 17, 2003, 1:35 PM
 */

package edu.harvard.med.hip.flex.core;

import edu.harvard.med.hip.flex.workflow.*;
import edu.harvard.med.hip.flex.database.*;
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
    
    public CloningStrategy(int id) {
        this.id = id;
    }
    
    public int getId() {return id;}
    public String getName() {return name;}
    public CloneVector getClonevector() {return clonevector;}
    public CloneLinker getLinker5p() {return linker5p;}
    public CloneLinker getLinker3p() {return linker3p;}
    public String getType() {return type;}
    
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
    
    public static List getAllDestStrategy() {
        List l = new ArrayList();
        String sql = "select * from cloningstrategy where type='destination plasmid'";
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
    
    public static int getStrategyid(int projectid, int workflowid) {
        if(workflowid == Workflow.CREATOR_WORKFLOW || workflowid == Workflow.DNA_TEMPLATE_CREATOR
        || workflowid == Workflow.MGC_CREATOR_WORKFLOW || workflowid == Workflow.CONVERT_CLOSE_TO_FUSION
        || workflowid == Workflow.CONVERT_FUSION_TO_CLOSE) 
            return CREATOR;
        
        if(workflowid == Workflow.STANDARD_WORKFLOW || workflowid == Workflow.PSEUDOMONAS_WORKFLOW
        || workflowid == Workflow.MGC_GATEWAY_WORKFLOW || workflowid == Workflow.GATEWAY_WORKFLOW
        || workflowid == Workflow.MGC_GATEWAY_CLOSED) {
            if(projectid == Project.HUMAN || projectid == Project.BREASTCANCER || projectid == Project.KINASE) 
                return HUMAN_GATEWAY;
            if(projectid == Project.PSEUDOMONAS)
                return PSEUDOMONAS_GATEWAY;
            if(projectid == Project.YP || projectid == Project.YEAST || projectid == Project.FT || projectid == Project.AVENTIS)
                return YP_GATEWAY;
        }
        
        return 0;
    }
}
