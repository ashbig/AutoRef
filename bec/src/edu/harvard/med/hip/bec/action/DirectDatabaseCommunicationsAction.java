/*
 * DirectDatabaseCommunications.java
 *
 * Created on February 17, 2005, 2:14 PM
 */

package edu.harvard.med.hip.bec.action;

import java.util.*;

import java.sql.*;
import java.io.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;
import org.apache.struts.upload.*;

import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
//import edu.harvard.med.hip.bec.engine.*;
import edu.harvard.med.hip.bec.*;
import edu.harvard.med.hip.bec.coreobjects.oligo.*;
import edu.harvard.med.hip.bec.sampletracking.objects.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.form.*;
import edu.harvard.med.hip.bec.user.*;
import edu.harvard.med.hip.bec.Constants;
import edu.harvard.med.hip.bec.action_runners.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.util_objects.*;
/**
 *
 * @author  htaycher
 */
public class DirectDatabaseCommunicationsAction extends ResearcherAction
{
    
    /**
     * Does the real work for the perform method which must be overriden by the
     * Child classes.
     *
     * @param mapping The ActionMapping used to select this instance
     * @param actionForm The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */
    public ActionForward becPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        
        ActionErrors errors = new ActionErrors();
        int forwardName = ((Seq_GetSpecForm)form).getForwardName();
       String title = null; String jsp_name = null;
       String sql = null; ArrayList display_items = null;
       User user = null;
        try
        {
            if ( forwardName < 0 )
            {
                   user = (User)request.getSession().getAttribute(Constants.USER_KEY);
          
            }
            switch(forwardName)
            {
                  //settings database
               case Constants.PROCESS_ADD_NEW_LINKER  :
               case Constants.PROCESS_ADD_NAME_TYPE  : 
               case Constants.PROCESS_ADD_SPECIES_DEFINITION  :
               case Constants.PROCESS_ADD_PROJECT_DEFINITION  : 
                   
               case Constants.PROCESS_ADD_NEW_VECTOR  : 
               case Constants.PROCESS_SUBMIT_REFERENCE_SEQUENCES  :
               case Constants.PROCESS_SUBMIT_CLONE_COLLECTION  : 
                case Constants.PROCESS_ADD_NEW_CONNECTION_VECTOR_LINKER:
                case Constants.PROCESS_ADD_NEW_COMMON_PRIMER :

              {
                    switch(forwardName)
                    {
                        case Constants.PROCESS_ADD_NEW_LINKER  : {title="add  Linker"; jsp_name ="selecting_process"; prepareDisplayItems( forwardName,  request);break;}
                        case Constants.PROCESS_ADD_NAME_TYPE  : {title="add  Name Type"; jsp_name ="selecting_process"; prepareDisplayItems( forwardName,  request);break;}
                        case Constants.PROCESS_ADD_SPECIES_DEFINITION  : {title="add  Species Definition"; jsp_name ="selecting_process";prepareDisplayItems( forwardName,  request); break;}
                        case Constants.PROCESS_ADD_PROJECT_DEFINITION  : {title="add  Project Definition"; jsp_name ="selecting_process";prepareDisplayItems( forwardName,  request); break;}
                        case Constants.PROCESS_ADD_NEW_VECTOR  : {title="add  Vector"; jsp_name ="selecting_process"; prepareDisplayItems( forwardName,  request); break;}
                        case Constants.PROCESS_SUBMIT_REFERENCE_SEQUENCES  :{title="submit Reference Sequence Information"; jsp_name ="selecting_process"; break;}
                        case Constants.PROCESS_SUBMIT_CLONE_COLLECTION  : {title="submit Clone Collection"; jsp_name ="selecting_process"; break;}
                          case Constants.PROCESS_ADD_NEW_COMMON_PRIMER :{title="add Common Primer"; jsp_name ="selecting_process";prepareDisplayItems( forwardName,  request); break;}
                    
                        case Constants.PROCESS_ADD_NEW_CONNECTION_VECTOR_LINKER:
                        {
                            title="associate Vector with Common Primer";
                            jsp_name ="selecting_process";
                            prepareDisplayItems( forwardName,  request);
                            ArrayList vectors = BioVector.getAllVectors();
                            ArrayList primers = Oligo.getAllCommonPrimers();
                             request.setAttribute("vectors", vectors);
                              request.setAttribute("primers", primers);
                            break;
                        }
                      
                    
    
                    
                    }
                   request.setAttribute("forwardName", new Integer(-forwardName));
                    request.setAttribute(Constants.JSP_TITLE,title);
                    return (mapping.findForward(jsp_name));
               }
               
              
               
               //action       
               case -Constants.PROCESS_ADD_NEW_LINKER  : 
               case -Constants.PROCESS_ADD_NAME_TYPE  : 
               case -Constants.PROCESS_ADD_SPECIES_DEFINITION  : 
               case -Constants.PROCESS_ADD_PROJECT_DEFINITION  : 
               case -Constants.PROCESS_ADD_NEW_COMMON_PRIMER :
               case -Constants.PROCESS_ADD_NEW_CONNECTION_VECTOR_LINKER:
          
               {
                    Connection conn = DatabaseTransaction.getInstance().requestConnection();
                    processJob(forwardName, request, conn);
                    return mapping.findForward("processing");
               }
               case -Constants.PROCESS_ADD_NEW_VECTOR  : 
               case -Constants.PROCESS_SUBMIT_REFERENCE_SEQUENCES  :
               case -Constants.PROCESS_SUBMIT_CLONE_COLLECTION  : 
               {
                   System.out.println("here come");
                    FormFile requestFile = ((SubmitDataFileForm)form).getFileName();
                      System.out.println("here come "+requestFile == null);
                
                    if(requestFile == null)
                     {
                        errors.add("filename", new ActionError("error.query.nofile"));
                        saveErrors(request,errors);
                        return new ActionForward(mapping.getInput());
                    }
                     processJobParsingFile(requestFile.getInputStream(), forwardName, request, user);
                    return mapping.findForward("processing");
               }
               case Constants.PROCESS_VIEW_ALL_NAME_TYPE  : 
               case Constants.PROCESS_VIEW_ALL_SPECIES_DEFINITION  :
               case Constants.PROCESS_VIEW_ALL_PROJECT_DEFINITION  :       
               {
                     prepareDisplayItems( forwardName,  request);
                     return (mapping.findForward("display_items"));
               }
            
            }
        }
         catch (Exception e)
        {
            System.out.println(e.getMessage());
            request.setAttribute(Action.EXCEPTION_KEY, e);
            return (mapping.findForward("error"));
        }
        return (mapping.findForward("error"));
       
    }
    
    
 
  public static   void       prepareDisplayItems(int forwardName, HttpServletRequest request)throws Exception
    {
       ArrayList display_title = new ArrayList();
       ArrayList display_items = new ArrayList();
       ArrayList ditem = null;
       String sql = null; String display_pagetitle = null;
       switch (forwardName)
       {
            case Constants.PROCESS_ADD_NAME_TYPE  : 
           case Constants.PROCESS_VIEW_ALL_NAME_TYPE:
            { 
                display_pagetitle = "All available Name Types";
                display_title.add("Name Type"); 
                sql = "Select nametype as item_1 from nametype order by nametype";
                 display_items = getDisplayItems(sql, display_title.size());
                break;
            }
            case Constants.PROCESS_ADD_SPECIES_DEFINITION  :
           case Constants.PROCESS_VIEW_ALL_SPECIES_DEFINITION:
            {  
                SpeciesDefinition sd = null;
                display_pagetitle="All available Species Definitions";
                display_title.add("Species Id");display_title.add("Species Name");display_title.add("Species specific ORF Id");
                sql = "Select SPECIESID as item_1, SPECIESNAME as item_2, IDNAME  as item_3 from SPECIESDEFINITION order by SPECIESNAME";
                 display_items = getDisplayItems(sql, display_title.size());
                break;
            }
            case Constants.PROCESS_ADD_PROJECT_DEFINITION  : 
            
           case Constants.PROCESS_VIEW_ALL_PROJECT_DEFINITION  :      
            {     
                
                display_pagetitle = "All available Project Definitions";
                display_title.add("Project Id");display_title.add("Project Name");display_title.add("Project Code");
                sql = "Select PROJECTID  as item_1, PROJECTNAME as item_2, PROJECTCODE  as item_3 from PROJECTDEFINITION order by PROJECTNAME";
                display_items = getDisplayItems(sql, display_title.size());
                break;
            }
            case Constants.PROCESS_ADD_NEW_LINKER  :
            {     
                
                display_pagetitle = "All available Linkers";
                display_title.add("Linker Name");display_title.add("Linker Sequence");
                sql = "Select  name as item_1, sequence  as item_2 from linker order by name";
                display_items = getDisplayItems(sql, display_title.size());
                break;
            }
            case Constants.PROCESS_ADD_NEW_CONNECTION_VECTOR_LINKER:
            {
                display_pagetitle = "All available vector - common primer pairs";
                display_title.add("Vector Name");display_title.add("Primer Name");
                display_title.add("Primer Position");display_title.add("Primer Orientation");
               
                sql = "Select  v.vectorname  as item_1, c.name  as item_2, position as item_3, orientation as item_4 " +
                "from vectorprimer p, commonprimer c, vector v where v.vectorid=p.vectorid and c.primerid=p.primerid  order by item_1";
                display_items = getDisplayItems(sql, display_title.size());
                ArrayList items = null;
                 for (int jj =  0; jj < display_items.size(); jj++ )
                 {
                     items = (ArrayList)display_items.get(jj);
                     int v = Integer.parseInt(String.valueOf(items.get(3)));
                     items.set(3, Oligo.getOrientationAsString(  v));
                      items.set(2, items.get(2) + " prime");
                 }
                break;
            }
            case Constants.PROCESS_ADD_NEW_COMMON_PRIMER :
            {
                display_pagetitle = "All available Common Primers";
                display_title.add("Common Primer Name");
                display_title.add("Sequence");display_title.add("Tm");display_title.add("Type");
                sql = "Select  name as item_1, sequence  as item_2, tm as item_3, type as item_4 from commonprimer order by name";
                display_items = getDisplayItems(sql, display_title.size());
                ArrayList items = null;
                for (int jj =  0; jj < display_items.size(); jj++ )
                 {
                     items = (ArrayList)display_items.get(jj);
                     int v = Integer.parseInt(String.valueOf(items.get(3)));
                     items.set(3, Oligo.getTypeAsString(  v));
                    
                 }
                break;
            }
            
           case Constants.PROCESS_ADD_NEW_VECTOR:
           {
                display_pagetitle = "All available vectors";
               display_title.add("Vector Name");display_title.add("Vector Source");
               display_title.add("Vector Type");
                sql = "Select  vectorname as item_1, source  as item_2, vectortype as item_3 from vector order by vectorname";
                 display_items = getDisplayItems(sql, display_title.size());
                 ArrayList items = null;
                 // update vector type to readable 
                 for (int jj =  0; jj < display_items.size(); jj++ )
                 {
                     items = (ArrayList)display_items.get(jj);
                     int v = Integer.parseInt(String.valueOf(items.get(2)));
                     items.set(2, BioVector.getTypeAsString(  v));
                 }
                 break;
           }
          
       }
        display_items.add(0, display_title);
        request.setAttribute("display_items", display_items);
        request.setAttribute("display_title", display_pagetitle);
    }
    
     private  void  processJobParsingFile(InputStream input, int forwardName,  HttpServletRequest request, User user)
     {
         String title = "";
         switch (forwardName)
        {
            case -Constants.PROCESS_ADD_NEW_VECTOR  :
            {
               request.setAttribute(Constants.JSP_TITLE,"Request for new vector submission");
               request.setAttribute(Constants.ADDITIONAL_JSP,"" );
               break;
            }
           case -Constants.PROCESS_SUBMIT_REFERENCE_SEQUENCES  :
           {
               request.setAttribute(Constants.JSP_TITLE,"Request for reference sequence submission");
               request.setAttribute(Constants.ADDITIONAL_JSP,"" );
               break;
           }
           case -Constants.PROCESS_SUBMIT_CLONE_COLLECTION  : 
           {
               request.setAttribute(Constants.JSP_TITLE,"Request for clone collection submission");
               request.setAttribute(Constants.ADDITIONAL_JSP,"" );
              break;
           }
         }
         
        DatabaseCommunicationsRunner runner = new DatabaseCommunicationsRunner();
        runner.setUser(user);
        runner.setInputStream(input);
        runner.setProcessType( forwardName);
        Thread t = new Thread(runner);                    t.start();
     }
                   
    private  void processJob(int forwardName, HttpServletRequest request, Connection conn)throws Exception
    {
        String sql = null;
        String name = null; String sequence = null;
        switch (forwardName)
        {
             case -Constants.PROCESS_ADD_NEW_CONNECTION_VECTOR_LINKER:
             {
                int vectorid = Integer.parseInt(request.getParameter("vectorid"));
                int primerid = Integer.parseInt(request.getParameter("primerid")) ;
               int primerposition=Integer.parseInt(request.getParameter("primerposition")) ;
               int primerorientation = Integer.parseInt(request.getParameter("primerorientation")) ;
               sql = " insert into vectorprimer (vectorprimerid, vectorid,primerid,position,orientation,leaderlength)"
               +" values(vectorid.nextval, "+ vectorid +","+primerid+","+primerposition+","+primerorientation+",65)"; 
               DatabaseTransaction.executeUpdate(sql, conn);
               request.setAttribute(Constants.JSP_TITLE,"Request for new Connection between Vector and Common Primer");
               request.setAttribute(Constants.ADDITIONAL_JSP,"Connection between Vector and Common Primer added.");
                break;
             }
           case -Constants.PROCESS_ADD_NEW_COMMON_PRIMER  : 
           {
               name = request.getParameter("primername");
               sequence = request.getParameter("sequence") ;
               if ( name == null || name.trim().length()< 1 )
                   throw new BecDatabaseException ("Empty linker name") ;
               if( sequence == null || sequence.trim().length()< 1)
                   throw new BecDatabaseException ("Empty linker sequence") ;
               sql = " insert into commonprimer (primerid, name,sequence,TM,type) values(oligoid.nextval,'"
               +name + "','"+sequence +"',"+request.getParameter("tm")+","+request.getParameter("primertype") +")";
               DatabaseTransaction.executeUpdate(sql, conn);
               request.setAttribute(Constants.JSP_TITLE,"Request for new Common Primer addition");
               request.setAttribute(Constants.ADDITIONAL_JSP,"Added Common Primer: <P>Name: "+request.getParameter("name") +"<p>Sequence: " +request.getParameter("sequence"));
                break;
           }    
           case -Constants.PROCESS_ADD_NEW_LINKER  : 
           {
               name = request.getParameter("linkername");
               sequence = request.getParameter("linkersequence") ;
               if ( name == null || name.trim().length()< 1 )
                   throw new BecDatabaseException ("Empty linker name") ;
               if( sequence == null || sequence.trim().length()< 1)
                   throw new BecDatabaseException ("Empty linker sequence") ;
               sql = " insert into linker (linkerid,sequence, name) "
                +" values (vectorid.nextval,'"+ sequence +"','"+ name +"')";

               DatabaseTransaction.executeUpdate(sql, conn);
               request.setAttribute(Constants.JSP_TITLE,"Request for new Linker addition");
               request.setAttribute(Constants.ADDITIONAL_JSP,"Added Linker: <P>Linker Name: "+name +"<p>Linker Sequence: " +sequence);
                break;
           }    
           case -Constants.PROCESS_ADD_NAME_TYPE  : 
           {
               sql = " insert into nametype values('"+ request.getParameter("nametype").toUpperCase() +"')";
               DatabaseTransaction.executeUpdate(sql, conn);
               request.setAttribute(Constants.JSP_TITLE,"Request for new name type addition");
               request.setAttribute(Constants.ADDITIONAL_JSP,"Added Name type: "+ request.getParameter("nametype") );
               break;
           }
           case -Constants.PROCESS_ADD_SPECIES_DEFINITION  : 
           {
               String speciesname = request.getParameter("speciesname");
               String speciesid = request.getParameter("speciesid").toUpperCase();
               int id = BecIDGenerator.getID("speciesid");
               sql = " insert into SPECIESDEFINITION values("+ id +",'"+speciesname+"','"+speciesid+"')";
               DatabaseTransaction.executeUpdate(sql, conn);
               request.setAttribute(Constants.JSP_TITLE,"Request for new Species Definition");
               request.setAttribute(Constants.ADDITIONAL_JSP,"Added Species Definition. Species Name: "+speciesname+" Species Specific ID: "+ speciesid);
               DatabaseToApplicationDataLoader.addSpecies( new SpeciesDefinition(id , speciesname,speciesid));
               
               break;
           }
           case -Constants.PROCESS_ADD_PROJECT_DEFINITION  : 
           {
               String pr_code = String.valueOf( request.getParameter("projectcode").toUpperCase().charAt(0));
               String pr_name =request.getParameter("projectname") ;
               sql = " insert into projectdefinition values (projectid.nextval, '"+pr_code+"','"+pr_name+"')";
               DatabaseTransaction.executeUpdate(sql, conn);
               request.setAttribute(Constants.JSP_TITLE,"Request for new Project Definition addition");
               request.setAttribute(Constants.ADDITIONAL_JSP,"Added new Project Definition: <P>Project Name: "+ pr_name +"; Project Code: "+pr_code);
              DatabaseToApplicationDataLoader.addProject(pr_name, pr_code);
          
               break;
           }
           

        }
        conn.commit();
        
    }
    
    
    
    private   static ArrayList  getDisplayItems(String sql, int number_of_columns)throws Exception
    {
        ResultSet rs = null; ArrayList result = new ArrayList();
        ArrayList item = new ArrayList();
        try
        {
            rs = DatabaseTransaction.getInstance().executeQuery(sql);
            while(rs.next())
            {
                item = new ArrayList();
                if (  number_of_columns > 0 )                 {  if ( rs.getObject("item_1") != null) item.add(rs.getObject("item_1")); else item.add("");}
                if (  number_of_columns > 1){  if ( rs.getObject("item_2") != null)item.add(rs.getObject("item_2")); else item.add("");}
                if ( number_of_columns > 2){  if ( rs.getObject("item_3") != null) item.add(rs.getObject("item_3")); else item.add("");}
                if ( number_of_columns > 3) {  if ( rs.getObject("item_4") != null)item.add(rs.getObject("item_4")); else item.add("");}
                result.add(item);
            }
            
            return result;
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException("Error occured while extracting data"+sqlE.getMessage()+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
    
    
    
     public static void main(String args[])
    {
        try
        {
            DirectDatabaseCommunicationsAction.prepareDisplayItems(Constants.PROCESS_ADD_NEW_VECTOR, null);
    
    // ArrayList display_items = DirectDatabaseCommunicationsAction.getDisplayItems("Select nametype as item_1 from nametype", 1);
   //       display_items = DirectDatabaseCommunicationsAction.getDisplayItems("Select PROJECTID  as item_1, PROJECTNAME as item_2 PROJECTCODE  as item_3 from PROJECTDEFINITION ", 3);
                         
        
        }catch(Exception e){}    
     }
}
