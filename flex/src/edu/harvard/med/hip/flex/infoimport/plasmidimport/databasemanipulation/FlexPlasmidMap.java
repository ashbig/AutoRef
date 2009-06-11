/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.infoimport.plasmidimport.databasemanipulation;

import java.sql.*;
import edu.harvard.med.hip.flex.database.*;
import java.util.*;
import static edu.harvard.med.hip.flex.infoimport.plasmidimport.PlasmidImporterDefinitions.IMPORT_ACTIONS;

/**
 *
 * @author htaycher
 */
public class FlexPlasmidMap {
    
    private int  m_flexid;
    private String m_flexname;
    private int m_plasmid_id;
    private String m_plasmid_name;
    private String m_value1;
    private String m_value2;
    
    public FlexPlasmidMap(int f, String fn, int pi, String pn)
    {
        m_flexid = f; m_flexname =fn;  m_plasmid_id=pi;  m_plasmid_name=pn;
    }
    public FlexPlasmidMap(){}

    public int  getFlexId(){ return m_flexid;}
    public String getFlexName(){ return m_flexname;}
    public int getPlasmidId(){ return m_plasmid_id;}
    public String getPlasmidName(){ return m_plasmid_name;}
    public String   getValue1(){ return m_value1;}
    public String   getValue2(){ return m_value2;}
    
    
    public void     setFlexId(int v){  m_flexid=v;}
    public void     setFlexName(String v){  m_flexname=v;}
    public void     setPlasmidId(int v){  m_plasmid_id=v;}
    public void     setPlasmidName(String v){  m_plasmid_name=v;}
    public void     setValue1(String v){ m_value1=v;}
    public void     setValue2(String v){m_value2=v;}
    
     public List<FlexPlasmidMap> getAllMapItems(Connection conn, IMPORT_ACTIONS cur_process )throws Exception
    {
         return   getAllMapItems( conn,  cur_process ,  false);
    
     }
    public List<FlexPlasmidMap> getAllMapItems(Connection conn, IMPORT_ACTIONS cur_process , boolean internalCall)throws Exception
    {
            String sql=null;
           switch(cur_process)
           {
            case CONNECT_PLASMID_FLEX_VECTOR_NAMES_SUBMITTED:
            case CONNECT_PLASMID_FLEX_AUTHOR_SUBMITTED:
            case CONNECT_PLASMID_FLEX_AUTHOR_TYPE_SUBMITTED:
                 
               case CONNECT_PLASMID_FLEX_CLONEPROPERTY_NAMETYPE_SUBMITTED:
               case CONNECT_PLASMID_FLEX_CLONE_NAMETYPE_SUBMITTED:
            {
                sql="select flexid, flexname, plasmidId, plasmidname from FLEX_PLASMID_DEFINITION_MAP "
                        +" where maptype='"+cur_process.toString()+"' order by flexname";
                break;
            }
            case CONNECT_PLASMID_FLEX_SPECIES_SUBMITTED:
            {
                sql="select  flexid,flexname   ,plasmidId, value1, plasmidname || ' (' || value1 || ')' "
                        +" as plasmidname from FLEX_PLASMID_DEFINITION_MAP where maptype='"
                        +cur_process.toString()+"' order by flexname";
                 if ( internalCall)
                 {sql="select  flexid,flexname   ,plasmidId, value1, plasmidname  "
                        +"  from FLEX_PLASMID_DEFINITION_MAP where maptype='"
                        +cur_process.toString()+"' order by flexname";}
                 break;
            }
               case CONNECT_PLASMID_FLEX_NAMETYPE_SUBMITTED:
               {
                    sql="select flexid, flexname, plasmidId, plasmidname from FLEX_PLASMID_DEFINITION_MAP "
                        +" where maptype='"+cur_process.toString()+"' order by flexname";
                    if ( internalCall)
                   {sql="select  flexid,flexname   ,plasmidId, plasmidname, "
                            + " (select url from nametype where nametype=flexname ) as value1   "
                        +"   from FLEX_PLASMID_DEFINITION_MAP where maptype='"
                        +cur_process.toString()+"' order by flexname";}
                    
                    break;
               }
           
        }
           
        if (sql==null) return new ArrayList<FlexPlasmidMap>();
         return getAllMapItems( conn,  sql, cur_process,internalCall );
    }
            
            
    private   List<FlexPlasmidMap> getAllMapItems(Connection conn, String sql,
            IMPORT_ACTIONS cur_process ,boolean internalCall)throws Exception
    {
          List<FlexPlasmidMap> infos = new ArrayList<FlexPlasmidMap>();
         ResultSet rs = null;FlexPlasmidMap map_item;
          try
        {
             rs = DatabaseTransactionLocal.executeQuery(sql,conn);
            while(rs.next())
            {
                map_item = new FlexPlasmidMap(
                            rs.getInt("flexid"),
                            rs.getString("flexname"),
                            rs.getInt("plasmidid"),
                            rs.getString("plasmidname"));
          
                if (internalCall)
                {
                switch ( cur_process )
                {
                    case CONNECT_PLASMID_FLEX_SPECIES_SUBMITTED:
                    case CONNECT_PLASMID_FLEX_NAMETYPE_SUBMITTED:
                    {
                        map_item.setValue1(rs.getString("value1"));
                    }}
                }
                 infos.add(map_item);
                
            }
             return infos;
        } catch (Exception ex) {
            System.out.println("err "+ex.getMessage());
             throw new Exception ("Cannot get mapping information "+sql);
            
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
}
