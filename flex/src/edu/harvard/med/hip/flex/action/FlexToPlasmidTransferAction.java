/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.action;

/**
 *
 * @author htaycher
 */
 
import java.io.*;
import java.sql.*;
import java.util.*;
import javax.crypto.NullCipher;
import javax.servlet.*;
import javax.servlet.http.*;
 
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.apache.struts.upload.*;
import org.apache.struts.util.MessageResources;

import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.util.*;
import edu.harvard.med.hip.flex.form.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.user.*;
import edu.harvard.med.hip.flex.Constants;
import edu.harvard.med.hip.flex.workflow.*;

import edu.harvard.med.hip.flex.infoimport.*;
import static edu.harvard.med.hip.flex.infoimport.ConstantsImport.ITEM_TYPE;

import static edu.harvard.med.hip.flex.infoimport.plasmidimport.PlasmidImporterDefinitions.PLASMID_TRANSFER_CLONE_STATUS;

import static edu.harvard.med.hip.flex.infoimport.plasmidimport.PlasmidImporterDefinitions.IMPORT_ACTIONS;
import static edu.harvard.med.hip.flex.workflow.Workflow.WORKFLOW_TYPE;
import edu.harvard.med.hip.flex.infoimport.bioinfo.*;
import edu.harvard.med.hip.flex.infoimport.file_mapping.*;
import edu.harvard.med.hip.flex.infoimport.plasmidimport.*;
//import edu.harvard.med.hip.flex.infoimport.plasmidimport.coreobject.*;
import edu.harvard.med.hip.flex.infoimport.plasmidimport.databasemanipulation.*;
 import edu.harvard.med.hip.flex.infoimport.plasmidimport.filemanipulation.*;
 

import plasmid.coreobject.CollectionInfo;
import plasmid.coreobject.GrowthCondition;
//import plasmid.process.QueryProcessManager;
/**
 *
 * @author  dzuo
 * @version
 */
public class FlexToPlasmidTransferAction extends ResearcherAction 
{
    
    /**
     * Process the specified HTTP request, and create the corresponding HTTP
     * response (or forward to another web component that will create it).
     * Return an <code>ActionForward</code> instance describing where and how
     * control should be forwarded, or <code>null</code> if the response has
     * already been completed.
     *
     * @param mapping The ActionMapping used to select this instance
     * @param actionForm The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */
    public synchronized ActionForward flexPerform(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException
    {
             
        ActionErrors errors = new ActionErrors();
        FlexToPlasmidTransferForm requestForm= ((FlexToPlasmidTransferForm)form);
        String forwardName = requestForm.getForwardName();
        IMPORT_ACTIONS cur_process = IMPORT_ACTIONS.valueOf(forwardName);
        String map_name= null;   
           User user = ((User)request.getSession().getAttribute(Constants.USER_KEY));
          
        try 
        {
             request.setAttribute("forwardName", String.valueOf(forwardName));
             FLEXtoPLASMIDImporter pi = new FLEXtoPLASMIDImporter();
             Connection plasmid_connection=null;
                    
             switch(cur_process) 
              {
                 case CREATE_SUBMISSION_FILES:
                 case TRANSFER_CLONE_INFORMATION :
                 {
                    
                    try
                    {
                          plasmid_connection = pi.getPLASMIDConnection();
                          collectDataForCreateFilesAction(  mapping,
                            request,errors, plasmid_connection);
                    }
                    catch(Exception e)
                    {
                         return (mapping.findForward("error"));
                    }
                    finally{if ( plasmid_connection!=null)plasmid_connection.close();}
                       return (mapping.findForward("create_submission_files"));
                 }
                 case UPLOAD_SUBMISSION_FILES :
                 {
                     return (mapping.findForward("transfer_data_to_plasmid_from_files"));
                 }
            
                 case CHANGE_CLONE_STATUS :
                 {
                     
                     return (mapping.findForward("update_clone_status"));
                 }
                 case CHANGE_CLONE_STATUS_SUBMITTED:
                 {
                     String items = requestForm.getItemids();
                      String new_status = requestForm.getCloneStatus();
                     boolean isCreateLogFile = requestForm.getIsCreateLogFile();
                    ItemsImporter imp = new ItemsImporter();
                    imp.setUser(user);
                      imp.setProcessType(ConstantsImport.PROCESS_NTYPE.CHANGE_CLONE_STATUS);
                      imp.setInputData(ITEM_TYPE.ITEM_TYPE_CLONEID,  items);
                     imp.isWriteLogFile(isCreateLogFile);
                     PLASMID_TRANSFER_CLONE_STATUS cs=PLASMID_TRANSFER_CLONE_STATUS.valueOf(new_status);
                     imp.setClonePlasmidTransferStatus(cs);
                     request.setAttribute("forwardName", String.valueOf(forwardName));
                     java.lang.Thread t = new java.lang.Thread(imp);                    t.start();
                   
                     return (mapping.findForward("update_clone_status_confirm"));
                 }
                 case CONNECT_PLASMID_FLEX_VECTOR_NAMES:
                 case CONNECT_PLASMID_FLEX_AUTHOR:
                 case CONNECT_PLASMID_FLEX_AUTHOR_TYPE:
                 case CONNECT_PLASMID_FLEX_SPECIES:
                 case CONNECT_PLASMID_FLEX_NAMETYPE:
                     case CONNECT_PLASMID_FLEX_CLONE_NAMETYPE:
                 case CONNECT_PLASMID_FLEX_CLONEPROPERTY_NAMETYPE:
                      
                 {
                     try
                    {
                          plasmid_connection = pi.getPLASMIDConnection();
                          getDataForMapping(  mapping,
                               request,errors, plasmid_connection,cur_process);
                          return (mapping.findForward("connect_flex_plasmid_items"));
                    }
                    catch(Exception e)
                    {
                         return (mapping.findForward("error"));
                    }
                    finally{if ( plasmid_connection!=null)plasmid_connection.close();}
                      
                 }
                 case CONNECT_PLASMID_FLEX_VECTOR_NAMES_SUBMITTED:
                 case CONNECT_PLASMID_FLEX_AUTHOR_SUBMITTED:
                 case CONNECT_PLASMID_FLEX_AUTHOR_TYPE_SUBMITTED:
                     case CONNECT_PLASMID_FLEX_SPECIES_SUBMITTED:
                 case CONNECT_PLASMID_FLEX_NAMETYPE_SUBMITTED:
                 case CONNECT_PLASMID_FLEX_CLONE_NAMETYPE_SUBMITTED:
                 case CONNECT_PLASMID_FLEX_CLONEPROPERTY_NAMETYPE_SUBMITTED:
                 {
                     try
                    {
                         submitMappingItem(  mapping,
                               request,errors, cur_process, form);
                         return (mapping.findForward("confirm_connect_flex_plasmid_items"));
                    }
                    catch(Exception e)
                    {
                         return (mapping.findForward("error"));
                    }
                    finally{}
                      
                 }
                 case DELETE_MAPPING_ITEM:
                 {
                    deleteMappingItem( mapping,
                            request,  errors,  cur_process,form);
                    return (mapping.findForward("confirm_connect_flex_plasmid_items"));
                 }
               }
             return (mapping.findForward("nothing"));    
             
        } 
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            request.setAttribute(Action.EXCEPTION_KEY, e);
            return (mapping.findForward("error"));
        }
    }
    //-------------------------------------------------------------
    private void                 updateCloneStatus(String items,
            String new_status, boolean isCreateLogFile)
                    
    {
        
    }
    
    private void                collectDataForCreateFilesAction(ActionMapping mapping,
        HttpServletRequest request,
        ActionErrors errors,
        Connection plasmid_connection)throws Exception
    {
            List<CollectionInfo> col;List<GrowthCondition> colGC;
            List <String> col_items;String sql;String[] v;
            int mode =0;String title="";
            try
            {
                col= getAllPlasmIDCollections(plasmid_connection);
                request.setAttribute("plasmidCollections", col);
                 mode=1;
                colGC= getAllPlasmIDGrowthConditions(plasmid_connection);
                request.setAttribute("plasmidGrowthConditions", colGC);
                 mode=2;
                sql="select restriction as item1 from restriction order by restriction";
                col_items = getAllItems(plasmid_connection,sql );
                v = new String[col_items.size()];
                v = col_items.toArray(v);
                request.setAttribute("plasmidRestrictions",v);
                  mode=3;
                 sql="select  type as item1 from hosttype order by type";
                col_items = getAllItems(plasmid_connection,sql );
                v = new String[col_items.size()];
                v = col_items.toArray(v);
                request.setAttribute("plasmidHostType", v);
                 mode=4;
                sql="select marker as item1 from marker order by marker";
                col_items = getAllItems(plasmid_connection,sql );
                v = new String[col_items.size()];
                v = col_items.toArray(v);
                request.setAttribute("plasmidMarkers", v);
                mode=5;
              /*  sql="select distinct authorname  as item1 from authorinfo";
                col_items = getAllItems(plasmid_connection,sql);
                v = new String[col_items.size()];
                v = col_items.toArray(v);
                request.setAttribute("authorInfos", v);
                mode=6;
                 sql="select distinct authortype from cloneauthor";
                col_items = getAllItems(plasmid_connection,sql);
                v = new String[col_items.size()];
                v = col_items.toArray(v);
                request.setAttribute("authorTypes", v);
                  /*   sql="select distinct 'Host strain: ' || hoststrain || '| Description: ' || description || '| Is in use: ' ||isinuse as item1 from host";
                col_items = getAllItems(plasmid_connection,sql);
                v = new String[col_items.size()];
                v = col_items.toArray(v);
                request.setAttribute("plasmidHost", v);
              System.out.println("got settings");*/
            }
            catch(Exception e)
            {
                switch(mode)
                {
                    case 0:{ title="Can not get clone collection definitions from PLASMID.";break;}
                    case 1:{title="Can not get growth conditions from PLASMID.";break;}
                    case 2:{title="Can not get restrictions from PLASMID.";break;}
                    case 3:{title="Can not get host type from PLASMID";break;}
                    case 4:{title="Can not get marker from PLASMID";break;}
                    case 7:{title="Can not get host from PLASMID";break;}
                    case 5:{title="Can not get author info from PLASMID";break;}
                    case 6:{title="Can not get author type from PLASMID";break;}
                }
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.general", title));
                saveErrors(request, errors);
            }
                   
                   
}
     private void               deleteMappingItem(ActionMapping mapping,
        HttpServletRequest request,
        ActionErrors errors, 
        IMPORT_ACTIONS cur_process,
                ActionForm form)throws Exception
    {
             String sql=null;  String err_title="";
             Connection conn=null;  
              try
            {
                  String plasmidname= request.getParameter("PLASMIDNAME");
            String flexname=request.getParameter("FLEXNAME");
            String maptype=request.getParameter("MAPTYPE");
            IMPORT_ACTIONS process = IMPORT_ACTIONS.valueOf(maptype);
      
                err_title=process.getMapTypeAsString()+ " FLEX item "+flexname +": PLASMID item: "+plasmidname;
                sql="delete from  FLEX_PLASMID_DEFINITION_MAP where FLEXname='"+flexname+
         "' and plasmidname='"+plasmidname+"' and maptype='"+maptype+"'";
     // System.out.println(sql);            
                 conn = DatabaseTransaction.getInstance().requestConnection();
                 DatabaseTransaction.executeUpdate(sql,conn);
                 conn.commit();
                 request.setAttribute("flexSelectedItem", flexname) ;
                    request.setAttribute("plasmidSelectedItem",plasmidname);
             }
           
            catch(FlexDatabaseException e)
            {
                System.out.println(e.getMessage()+err_title);
                conn.rollback();
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.general", err_title));
                saveErrors(request, errors);
            }
             finally
             {
                 if (conn != null) DatabaseTransaction.closeConnection(conn);
             }
             
        }
      
    
        private void               submitMappingItem(ActionMapping mapping,
        HttpServletRequest request,
        ActionErrors errors, 
        IMPORT_ACTIONS cur_process,
                ActionForm form)throws Exception
    {
            String sql=null;  String err_title="";
             Connection conn=null;String sql_plasmid =null;
             String fname = ((FlexToPlasmidTransferForm)form).getFlexSelectedItem();
             String pname = ((FlexToPlasmidTransferForm)form).getPlasmidSelectedItem();
             FLEXtoPLASMIDImporter pi = new FLEXtoPLASMIDImporter();
             request.setAttribute("flexSelectedItem", fname);
             request.setAttribute("plasmidSelectedItem", pname);
             Connection plasmid_connection=null;ResultSet rs=null;
             int pid = -1;
            try
            {
                switch(cur_process)
                {
                    case CONNECT_PLASMID_FLEX_VECTOR_NAMES_SUBMITTED:
                    {
                        err_title="submit mapping for vector";
                        sql="insert into FLEX_PLASMID_DEFINITION_MAP (maptype,FLEXID,FLEXNAME,PLASMidNAME,PLASMidID)"
+"values ( '"+cur_process.toString()+"',(select vectorid from vector where vectorname='"+fname 
+"'),'"+fname +"','"+pname +"',";
                        sql_plasmid="select vectorid as plasmidid  from vector where name='"+pname+"'";
                    
                        break;
                    }
                    case CONNECT_PLASMID_FLEX_AUTHOR_SUBMITTED:
                     {
                         err_title="Cannot submit mapping for author";
                         sql_plasmid="select authorid as plasmidid  from authorinfo where authorname='"+pname+"'";
                        
                        sql=" insert into  FLEX_PLASMID_DEFINITION_MAP (maptype,FLEXID,FLEXNAME,PLASMidNAME,PLASMidID)" 
                                +"values ('"+cur_process.toString()+"', (select authorid from authorinfo where authorname = '"+fname
                                +"'), '"+fname+"','"+pname+"',";
                        break;
                    }
                    case CONNECT_PLASMID_FLEX_SPECIES_SUBMITTED:
                    {
                        String plasmidsp = pname.substring(0, pname.indexOf("(")-1).trim();
                        String plasmidsptype= pname.substring(pname.indexOf("(")+1, pname.lastIndexOf(")")).trim();
                    err_title="Cannot submit mapping for species.";
   sql="insert into FLEX_PLASMID_DEFINITION_MAP (maptype,FLEXNAME,PLASMidname,value1)"
          +" values ('"+cur_process.toString()+"','"+fname+"','"+plasmidsp+"','"+plasmidsptype+"')";
  
                   break;
                         
                }
case CONNECT_PLASMID_FLEX_NAMETYPE_SUBMITTED:
       {
     err_title="Cannot submit mapping.";
   sql="insert into FLEX_PLASMID_DEFINITION_MAP (maptype,FLEXNAME,PLASMidname,value1)"
          +" values ('"+cur_process.toString()+"','"+fname+"','"+pname+"',"
          +"(select url from nametype where nametype='"+fname+"'))";
  
                   break;}
     case CONNECT_PLASMID_FLEX_AUTHOR_TYPE_SUBMITTED:
case CONNECT_PLASMID_FLEX_CLONE_NAMETYPE_SUBMITTED:
case CONNECT_PLASMID_FLEX_CLONEPROPERTY_NAMETYPE_SUBMITTED:
                    {
     err_title="Cannot submit mapping.";
   sql="insert into FLEX_PLASMID_DEFINITION_MAP (maptype,FLEXNAME,PLASMidname)"
          +" values ('"+cur_process.toString()+"','"+fname+"','"+pname+"')";
  
                   break;}
                 
                }
                  conn = DatabaseTransaction.getInstance().requestConnection();
                 if ( sql_plasmid != null)//get plasmid for item
                 {
                       plasmid_connection = pi.getPLASMIDConnection();
         
                      rs = DatabaseTransactionLocal.executeQuery(sql_plasmid,plasmid_connection);
                      if(rs.next()) {pid=rs.getInt("plasmidid");}
                      sql += pid +")";
                  }
                   DatabaseTransaction.executeUpdate(sql,conn);
                   conn.commit();
             }
           
            catch(FlexDatabaseException e)
            {
                System.out.println(e.getMessage());
                 if ( e.getMessage().indexOf("ORA-00001")  > -1 )
                {                    err_title+=": mapping already exists";                  
                }
                if ( conn != null)conn.rollback();
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.general", err_title));
                saveErrors(request, errors);
                
            }
             finally{
                 if ( rs != null)DatabaseTransaction.closeResultSet(rs);
                 if (conn != null)DatabaseTransaction.closeConnection(conn);
                 if ( plasmid_connection!=null)plasmid_connection.close();
                 
                   
             }
            }
                   
                   

     private void                getDataForMapping(ActionMapping mapping,
        HttpServletRequest request,
        ActionErrors errors,
        Connection plasmid_connection,
        IMPORT_ACTIONS cur_process)throws Exception
    {
            List <String> col_items;
            String sql_flex=null;String sql_plasmid=null;String sql_mappedItems=null;
            String[] v; String err_title="";Connection conn=null;
             try
            {
                switch(cur_process)
                {
                    case CONNECT_PLASMID_FLEX_VECTOR_NAMES:
                    {
                        err_title="Can not extract vector names";
                         sql_flex="select vectorname as item1 from vector order by vectorname";
                         sql_plasmid="select name as item1 from vector order by name";
                     //   sql_mappedItems="select FLEXVECTORID as flexid,FLEXVECTORNAME as flexname,"
                     //           +"PLASMVECTORID as plasmidId, PLASMVECTORNAME as plasmidname from FLEX_PLASMID_VECTOR order by flexname";
                        break;
                    }
                    case CONNECT_PLASMID_FLEX_AUTHOR:
                     {
                          err_title="Cannot extract author information";
                        sql_flex="select authorname as item1 from authorinfo order by authorname";
                         sql_plasmid="select authorname as item1 from authorinfo order by authorname";
                       //  sql_mappedItems="select  FLEXAUTHORID as flexid,FLEXAUTHORNAME as flexname,"
                       //         +"PLASMIDAUTHORID as plasmidId, PLASMIDAUTHORNAME as plasmidname from FLEX_PLASMID_AUTHOR order by flexname";
                       break;
                    }
                    case CONNECT_PLASMID_FLEX_AUTHOR_TYPE:
                        {
                          err_title="Cannot extract information for author type";
                        sql_plasmid="select distinct authortype as item1 from cloneauthor order by authortype";
                         sql_flex="select nametype as item1 from cloneauthortype order by nametype";
                       // sql_mappedItems="select -1 as flexid,FLEXCLONEAUTHOR as flexname,"
                       //         +"-1 as plasmidId, PLASMIDCLONEAUTHOR as plasmidname from FLEX_PLASMID_CLONEAUTHORTYPE order by flexname";
                         break;
                    }
                    case CONNECT_PLASMID_FLEX_SPECIES:
                    {
                        err_title="Cannot extract information for species";
                        sql_plasmid="select  genusspecies ||' (' ||   rEFSEQTYPE ||') 'as item1 from speciesrefseqtype order by genusspecies";
                         sql_flex="select genusspecies as item1 from species order by genusspecies";
                    //    sql_mappedItems="select -1 as flexid,FLEXSPECIES    as flexname,"
                        //        +"-1 as plasmidId, PLASMIDSPECIES || ' (' || PLASMIDREFSEQTYPE || ')' as plasmidname from FLEX_PLASMID_SPECIES_MAP order by flexname";
                      
                         break;
                    }
                    case CONNECT_PLASMID_FLEX_NAMETYPE:
                    {
                        err_title="Cannot extract information for reference sequence name type";
                        sql_plasmid="select  nametype as item1 from seqnametype order by nametype";
                         sql_flex="select nametype as item1 from nametype order by nametype";
                          break;
                    }
                    case CONNECT_PLASMID_FLEX_CLONEPROPERTY_NAMETYPE:
                    {
                        err_title="Cannot extract information for clone property name type";
                        sql_plasmid="select  propertytype as item1 from clonepropertytype order by propertytype";
                         sql_flex="select nametype as item1 from nametype order by nametype";
                          break;
                    }
                    case CONNECT_PLASMID_FLEX_CLONE_NAMETYPE:
                    {
                        err_title="Cannot extract information for clone name type";
                        sql_plasmid="select  nametype as item1 from cnametype order by nametype";
                         sql_flex="select nametype as item1 from nametype order by nametype";
                          break;
                    }
                    
                }
                conn = DatabaseTransaction.getInstance().requestConnection();
                col_items = getAllItems(conn,sql_flex);
                v = new String[col_items.size()];
                v = col_items.toArray(v);
                request.setAttribute("flexItems",v);
               col_items = getAllItems(plasmid_connection,sql_plasmid);
                v = new String[col_items.size()];
                v = col_items.toArray(v);
                request.setAttribute("plasmidItems",v);
               // col_items = getAllMapItems(conn,sql_mappedItems);
                FlexPlasmidMap map = new FlexPlasmidMap();
                List<FlexPlasmidMap> items = map.getAllMapItems(conn, cur_process.getNextProcess());
                request.setAttribute("mappedItems", items);
                 
              }
            catch(Exception e)
            {
                System.out.println("error message "+e.getMessage());
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.general", err_title));
                saveErrors(request, errors);
            }
              finally{if (conn != null)DatabaseTransaction.closeConnection(conn);}
                  
                   
}
    private   List<CollectionInfo> getAllPlasmIDCollections(Connection conn )throws Exception
    {
        String sql = "select name,description,price,status,restriction,memberprice"+
        " from collection order by name";
        
        List<CollectionInfo> infos = new ArrayList();
         ResultSet rs = null;
        try
        {
            rs = DatabaseTransactionLocal.executeQuery(sql,conn);
            while(rs.next()) {
                String name = rs.getString(1);
                String description = rs.getString(2);
                double price = rs.getDouble(3);
                String s = rs.getString(4);
                String restriction = rs.getString(5);
                double memberprice = rs.getDouble(6);
                CollectionInfo info = new CollectionInfo(name, description, price, memberprice, s, restriction);
                infos.add(info);
            }
            return infos;
        } catch (Exception ex) {
             throw new Exception ("Cannot get collection information from PLASMID");
            
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
    
    private   List<GrowthCondition> getAllPlasmIDGrowthConditions(Connection conn )throws Exception
    {
        String sql = "select * from growthcondition order by name";
        
        List<GrowthCondition> infos = new ArrayList();
         ResultSet rs = null;
        try
        {
            rs = DatabaseTransactionLocal.executeQuery(sql,conn);
            while(rs.next()) {
               
                GrowthCondition info = new GrowthCondition(rs.getInt("growthid"),
                        rs.getString("name"),
                        rs.getString("hosttype") ,
                        rs.getString("antibioticselection") ,
                        rs.getString("growthcondition") ,
                        rs.getString("comments")   ) ;
                infos.add(info);
            }
            return infos;
        } catch (Exception ex) {
             throw new Exception ("Cannot get collection information from PLASMID");
            
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
    
    private   List<String> getAllItems(Connection conn, String sql)throws Exception
    {
          List<String> infos = new ArrayList();
         ResultSet rs = null;
        try
        {
            rs = DatabaseTransactionLocal.executeQuery(sql,conn);
            while(rs.next())
            {
                infos.add(rs.getString("item1"));
                
            }
            return infos;
        } catch (Exception ex) {
             throw new Exception ("Cannot get information from PLASMID");
            
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
  /*  
      private   List getAllMapItems(Connection conn, String sql )throws Exception
    {
          List infos = new ArrayList();
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
                 infos.add(map_item);
                
            }
             return infos;
        } catch (Exception ex) {
             throw new Exception ("Cannot get information from PLASMID");
            
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
    */
    
    }