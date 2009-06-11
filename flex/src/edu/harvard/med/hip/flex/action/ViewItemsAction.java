/*
 * ViewItemsAction.java
 *
 * Created on August 9, 2007, 5:02 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.action;

import java.util.*;
import java.sql.*;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.util.*;
import edu.harvard.med.hip.flex.form.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.user.*;
import edu.harvard.med.hip.flex.Constants;
import edu.harvard.med.hip.flex.workflow.*;
import edu.harvard.med.hip.flex.infoimport.coreobjectsforimport.*;
import edu.harvard.med.hip.flex.infoimport.*;
import edu.harvard.med.hip.flex.core.growthcondition.*;
import static edu.harvard.med.hip.flex.infoimport.ConstantsImport.PROCESS_NTYPE;

import java.sql.*;
import javax.sql.*;
import sun.jdbc.rowset.*;

/**
 *
 * @author htaycher
 */
public class ViewItemsAction extends ResearcherAction {
    
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
    public ActionForward flexPerform(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException 
    {
        ActionErrors errors = new ActionErrors();
        
        String forwardName = ((ProjectWorkflowForm)form).getForwardName();        
              List items = null;
          String no_items_header = null;PROCESS_NTYPE cur_process =null;
          try{
           cur_process = PROCESS_NTYPE.valueOf(forwardName);
          }catch(Exception e){}
      Comparator genericComparator;
         String title= ""; 
         try 
        {
            if ( cur_process != null)
            {
                       System.out.println(cur_process.toString());
                  
                switch (cur_process)
                {
                    case VIEW_WORKFLOWS:
                    {
                        items = Workflow.getAllWorkflows();
                        title="Currently Available Workflows ";
                         if( items != null && items.size() > 0 )
                         {
                            genericComparator =  new BeanClassComparator("name");
                            Collections.sort(items, genericComparator);
                             request.setAttribute("workflows",items);
                            }
                       else
                       {
                         request.setAttribute(Constants.UI_TABLE_NO_DATA,"No workflows available");}
                       request.setAttribute(Constants.UI_PAGE_TITLE,title); 
                        return (mapping.findForward("view_items"));
                    }
                    case VIEW_BIOMATERIAL_CONDITION:
                    case VIEW_BIOMATERIAL:
                    case VIEW_BIOMATERIAL_COMBINATION :
                    case VIEW_GROWTHCONDITION :
                    case VIEW_VECTOR_GROWTH_CONDITON_TABLE:
                    {
                         items = getVeiwItems(cur_process);
                         System.out.println("items "+items.size());
                         if( items != null && items.size() > 0 )
                         {
                            genericComparator =  new BeanClassComparator("name");
                            Collections.sort(items, genericComparator);
                            request.setAttribute("display_items",items);
                              System.out.println("items "+items.size());
                      
                         }
                       else
                       {
                            request.setAttribute(Constants.UI_TABLE_NO_DATA,cur_process.getNoItemsTitle());
                       }
                       title=cur_process.getTitle();
                       request.setAttribute(Constants.UI_PAGE_TITLE,cur_process.getTitle() );
                       request.setAttribute("forwardName", forwardName);
                       return (mapping.findForward("view_growthcondition_items"));
            
                    }
                }
            }
              if(Constants.VIEW_VECTORS.equals(forwardName)) 
            {
               items = CloneVector.getAllVectors();
               if( items != null && items.size() > 0 )
               {
                     genericComparator =  new BeanClassComparator("name");
                     Collections.sort(items, genericComparator);
                   request.setAttribute("vectors",items);
               }
               else
                 request.setAttribute(Constants.UI_TABLE_NO_DATA,"No vectors available");
              
               title="Currently Available Vectors ";
            } 
            else  if(Constants.VIEW_LINKERS.equals(forwardName)) 
            {
               items = CloneLinker.getAllLinkers();
               if( items != null && items.size() > 0 )
              { 
                      genericComparator =  new BeanClassComparator("name");
                     Collections.sort(items, genericComparator);
                request.setAttribute("linkers",items);
               } 
               else
                  request.setAttribute(Constants.UI_TABLE_NO_DATA, "No linkers available");
              
               title="Currently Available Linkers ";
            } 
            else  if(Constants.VIEW_CLONINGSTRATEGIES.equals(forwardName)) 
            {
               items = CloningStrategy.getAllCloningStrategies();
               if( items != null && items.size() > 0 )
               {  
                      genericComparator =  new BeanClassComparator("name");
                     Collections.sort(items, genericComparator);
                    request.setAttribute("clstrategies",items);
               } 
               else
                   request.setAttribute(Constants.UI_TABLE_NO_DATA,   "No cloning strategies available");
              
               title = "Currently Available Cloning Strategies ";
            } 
           
             else  if(Constants.VIEW_NAMETYPE.equals(forwardName)) 
            {
                String tablename = (String) request.getAttribute("TABLENAME");
                if(tablename == null) tablename =  request.getParameter("TABLENAME");
                Nametype.TABLE_NAME_NAMETYPE cur_name_type = Nametype.TABLE_NAME_NAMETYPE.valueOf(tablename.toUpperCase());
           
                ArrayList nametypes = Nametype.getInfoFromNamesTable(cur_name_type);
                request.setAttribute("TABLENAME",tablename);
                request.setAttribute("nametypes",nametypes);
                return (mapping.findForward("view_nametypes"));
            } 
             else if (Constants.VIEW_AUTHOR.equals(forwardName))
             {
                String author_id_str =  (String)request.getParameter("ID");
                int author_id = Integer.parseInt(author_id_str);
                ImportAuthor author = ImportAuthor.restoreAuthor(author_id);
                if ( author != null) request.setAttribute("author",author);
                title="Author Information";
             }
            request.setAttribute(Constants.UI_PAGE_TITLE,title);
           
            request.setAttribute("forwardName",forwardName);
             return (mapping.findForward("view_items"));
       
        } catch (Exception e)
        {
            errors.add(ActionErrors.GLOBAL_ERROR,  new ActionError("Cannot show item",Constants.PROCESS_KEY));
         
            saveErrors(request,errors);
            request.setAttribute(Action.EXCEPTION_KEY, e);
            return (mapping.findForward("error"));
        }
    }
    
   public static List             getVeiwItems(PROCESS_NTYPE cur_process)
           throws Exception
   {
       List items = null;
       try
       {
       switch (cur_process)
       {
           case VIEW_BIOMATERIAL:
           {
               BioMaterial bm = new BioMaterial();
               return bm.findAll();
           }
           case VIEW_BIOMATERIAL_CONDITION:
           {
               BioMaterialCondition bc = new BioMaterialCondition();
               List<BioMaterialCondition> ll =  bc.findAll();
                return ll;
           }
            case VIEW_BIOMATERIAL_COMBINATION :
            {
                BioMaterialCombination bmc = new BioMaterialCombination();
                return bmc.findAll();
            }
            case VIEW_GROWTHCONDITION :
            {
                GrowthCondition gc = new GrowthCondition();
                return gc.findAll();
            }
            case VIEW_VECTOR_GROWTH_CONDITON_TABLE:
            default: return null;
       }
       }catch(Exception e)
       {
           System.out.println("Cannot get items"+e.getMessage());
           throw new Exception ("Cannot get items"+e.getMessage());
       }
        
   }
    
   
   public static void main(String args[]) 
     {
       try
       {
   
   List      gg=       getVeiwItems(PROCESS_NTYPE.VIEW_BIOMATERIAL_CONDITION);
   System.exit(0);
       }catch(Exception e)
       {
           ;
       }
   }
}
