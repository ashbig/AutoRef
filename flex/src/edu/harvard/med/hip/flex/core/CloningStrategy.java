/*
 * CloningStrategy.java
 *
 * Created on June 17, 2003, 1:35 PM
 */

package edu.harvard.med.hip.flex.core;

import edu.harvard.med.hip.flex.database.*;
import java.util.*;
import java.sql.*;

/**
 *
 * @author  dzuo
 */
public class CloningStrategy {
    protected int id;
    protected String name;
    protected CloneVector clonevector;
    protected CloneLinker linker5p;
    protected CloneLinker linker3p;
    protected String type;
    
    /** Creates a new instance of CloningStrategy */
    public CloningStrategy() {
    }
    
    public CloningStrategy(int id, String name, CloneVector clonevector, CloneLinker linker5p, CloneLinker linker3p) {
        this.id = id;
        this.name = name;
        this.clonevector = clonevector;
        this.linker5p = linker5p;
        this.linker3p = linker3p;
    }
    
    public int getId() {return id;}
    public String getName() {return name;}
    public CloneVector getClonevector() {return clonevector;}
    public CloneLinker getLinker5p() {return linker5p;}
    public CloneLinker getLinker3p() {return linker3p;}
    
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
}
