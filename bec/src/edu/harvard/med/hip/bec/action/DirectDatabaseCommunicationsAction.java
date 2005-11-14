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
import edu.harvard.med.hip.bec.bioutil.*;
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
import edu.harvard.med.hip.bec.sampletracking.mapping.*;
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
                case Constants.PROCESS_ADD_NEW_CLONINGSTRATEGY:
                 case Constants.PROCESS_ADD_TRACE_FILE_NAME_FORMAT:
                      case Constants.PROCESS_SUBMIT_CLONE_SEQUENCES:
              
              {
                    switch(forwardName)
                    {
                        case Constants.PROCESS_ADD_NEW_LINKER  : {title="Add  Linker"; jsp_name ="selecting_process"; prepareDisplayItems( forwardName,  request);break;}
                        case Constants.PROCESS_ADD_NAME_TYPE  : {title="Add  Name Type"; jsp_name ="selecting_process"; prepareDisplayItems( forwardName,  request);break;}
                        case Constants.PROCESS_ADD_SPECIES_DEFINITION  : {title="Add  Species Definition"; jsp_name ="selecting_process";prepareDisplayItems( forwardName,  request); break;}
                        case Constants.PROCESS_ADD_PROJECT_DEFINITION  : {title="Add  Project Definition"; jsp_name ="selecting_process";prepareDisplayItems( forwardName,  request); break;}
                        case Constants.PROCESS_ADD_NEW_VECTOR  : {title="Add  Vector"; jsp_name ="selecting_process"; prepareDisplayItems( forwardName,  request); break;}
                        case Constants.PROCESS_SUBMIT_REFERENCE_SEQUENCES  :{title="Submit Reference Sequence Information"; jsp_name ="selecting_process"; break;}
                        case Constants.PROCESS_SUBMIT_CLONE_SEQUENCES:{title="Submit Clone Sequence"; jsp_name ="selecting_process"; break;}
                        case Constants.PROCESS_SUBMIT_CLONE_COLLECTION  : {title="Submit Clone Collection"; jsp_name ="selecting_process"; break;}
                        case Constants.PROCESS_ADD_NEW_COMMON_PRIMER :{title="Add Common Primer"; jsp_name ="selecting_process";prepareDisplayItems( forwardName,  request); break;}
                        case Constants.PROCESS_ADD_NEW_CLONINGSTRATEGY:
                        {
                            title="Add Cloning Strategy"; 
                            jsp_name ="selecting_process";
                            prepareDisplayItems( forwardName,  request); 
                            ArrayList biolinkers = BioLinker.getAllLinkers();
                            ArrayList vectors = BioVector.getAllVectors();
                            request.setAttribute(Constants.VECTOR_COL_KEY, vectors);
                            request.setAttribute(Constants.LINKER_COL_KEY, biolinkers);
                            break;
                        }
                        case Constants.PROCESS_ADD_NEW_CONNECTION_VECTOR_LINKER:
                        {
                            title="Associate Vector with Common Primer";
                            jsp_name ="selecting_process";
                            prepareDisplayItems( forwardName,  request);
                            ArrayList vectors = BioVector.getAllVectors();
                            ArrayList primers = Oligo.getAllCommonPrimers();
                             request.setAttribute("vectors", vectors);
                              request.setAttribute("primers", primers);
                            break;
                        }
                         case Constants.PROCESS_ADD_TRACE_FILE_NAME_FORMAT:
                         {
                             title="Add Trace File Name Format";
                            jsp_name ="selecting_process";
                            prepareDisplayItems( forwardName,  request);
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
               case -Constants.PROCESS_ADD_NEW_CLONINGSTRATEGY:
                case -Constants.PROCESS_ADD_TRACE_FILE_NAME_FORMAT:
           
               {
                    Connection conn = DatabaseTransaction.getInstance().requestConnection();
                    processJob(forwardName, request, conn);
                    return mapping.findForward("processing");
               }
               case -Constants.PROCESS_ADD_NEW_VECTOR  : 
               case -Constants.PROCESS_SUBMIT_REFERENCE_SEQUENCES  :
               case - Constants.PROCESS_SUBMIT_CLONE_SEQUENCES:
               case -Constants.PROCESS_SUBMIT_CLONE_COLLECTION  : 
               {
                    FormFile requestFile = ((SubmitDataFileForm)form).getFileName();
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
       ArrayList items = null;
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
                 
                 // update vector type to readable 
                 for (int jj =  0; jj < display_items.size(); jj++ )
                 {
                     items = (ArrayList)display_items.get(jj);
                     int v = Integer.parseInt(String.valueOf(items.get(2)));
                     items.set(2, BioVector.getTypeAsString(  v));
                 }
                 break;
           }
           case Constants.PROCESS_ADD_NEW_CLONINGSTRATEGY:
           {
               display_pagetitle = "All available cloning strategies";
               display_title.add("Cloning Startegy Id");display_title.add("Cloning Startegy Name");
               display_title.add("Start Codon");display_title.add("Fusion Stop Codon");
                display_title.add("Closed Stop Codon");
                display_title.add("Vector");display_title.add("5' Linker");display_title.add("3' Linker");
                sql = "select strategyid as item_1, s.name as item_2, startcodon as item_3, fusionstopcodon as item_4, ";
sql += " closedstopcodon as item_5, v.vectorname as item_6, (select name from linker where linkerid = s.linker5id) as item_7,";
sql += " (select name from linker where linkerid = s.linker5id) as item_8";
sql += " from cloningstrategy s, vector v where v.vectorid=s.vectorid order by strategyid ";
                 display_items = getDisplayItems(sql, display_title.size());
                 for (int jj =  0; jj < display_items.size(); jj++ )
                 {
                     items = (ArrayList)display_items.get(jj);
                     if ( String.valueOf(items.get(2)).equalsIgnoreCase("NON") ) items.set(2, "Natural");
                     if ( String.valueOf(items.get(4)).equalsIgnoreCase("NON"))  items.set(4, "Natural");
                 }
                 break;
           }
            case Constants.PROCESS_ADD_TRACE_FILE_NAME_FORMAT:
            {
                display_pagetitle = "All available trace file name formats";
               display_title.add("Format Name");//0
               display_title.add("File name reading direction");//0
               display_title.add("Plate Label Separator");//1
               display_title.add("Plate Label  Column");//2
               display_title.add("Plate Label Start"); //3
               display_title.add("Plate Label Length");  //4
               display_title.add("Position Separator");     //5      
               display_title.add("Position Column"); //6
               display_title.add("Position Start");     //7 
               display_title.add("Position Length"); //8
               display_title.add("Direction Forward");  //9 
               display_title.add("Direction Reverse"); //10
               display_title.add("Direction Separator");//11
               display_title.add("Direction Column"); //12
               display_title.add("Direction Length");  //13
               display_title.add("Direction Start");  //14
               
                sql = "select FORMATNAME as item_1 , READING_DIRECTION as item_2, PLATE_SEPARATOR as item_3, PLATE_LABEL_COLUMN as item_4,PLATE_LABEL_START as item_5 ,PLATE_LABEL_LENGTH as item_6, "
                +" POSITION_SEPARATOR  as item_7, POSITION_COLUMN as item_8,POSITION_START  as item_9,POSITION_LENGTH as item_10,"
                +" DIRECTION_FORWARD as item_11  ,DIRECTION_REVERSE as item_12 ,"
                +" DIRECTION_SEPARATOR  as item_13, DIRECTION_COLUMN as item_14,DIRECTION_LENGTH as item_15 ,"
                +" DIRECTION_START item_16 from TRACEFILEFORMAT order by FORMATNAME";
                display_items = getDisplayItems(sql, display_title.size());
                for (int jj =  0; jj < display_items.size(); jj++ )
                 {
                     items = (ArrayList)display_items.get(jj);
                     for ( int ii = 0; ii < items.size(); ii++)
                     {
                         switch ( ii)
                         {
                             case 15: case 3: case 4: case 5:case 7:case 8: case 9:  case 13: case 14:
                             {
                                 if ( String.valueOf(items.get(ii)).equalsIgnoreCase("-1")  )
                                        items.set(ii, "Not set");
                                 break;
                             }
                             case 0: case 2: case 6:  case 10: case 11: case 12:
                             {
                                  if ( items.get(ii) == null || String.valueOf(items.get(ii)).equalsIgnoreCase("")  )
                                        items.set(ii, "Not set");
                                 break;
                             }
                             case 1:
                             {
                                 if ( String.valueOf(items.get(ii)).equals( String.valueOf(TraceFileNameFormat.READING_LEFT_TO_RIGHT)))
                                     items.set(ii, "Left to right");
                                 else
                                      items.set(ii, "Right to left");
                                  
                             }
                         }
                         
                     }
                    
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
               request.setAttribute(Constants.ADDITIONAL_JSP,"Report will be send to you by e-mail." );
               break;
            }
           case -Constants.PROCESS_SUBMIT_REFERENCE_SEQUENCES  :
           {
               request.setAttribute(Constants.JSP_TITLE,"Request for reference sequence submission");
               request.setAttribute(Constants.ADDITIONAL_JSP,"Report will be send to you by e-mail." );
               break;
           }
           case -Constants.PROCESS_SUBMIT_CLONE_SEQUENCES:
           {
               request.setAttribute(Constants.JSP_TITLE,"Request for clone sequence submission");
               request.setAttribute(Constants.ADDITIONAL_JSP,"Report will be send to you by e-mail." );
               break;
           }
           case -Constants.PROCESS_SUBMIT_CLONE_COLLECTION  : 
           {
               request.setAttribute(Constants.JSP_TITLE,"Request for clone collection submission");
               request.setAttribute(Constants.ADDITIONAL_JSP,"Report will be send to you by e-mail." );
              break;
           }
            
         }
        ProcessRunner runner = new DatabaseCommunicationsRunner();
        runner.setUser(user);
        ((DatabaseCommunicationsRunner)runner).setInputStream(input);
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
               name = (String) request.getParameter("primername");
               sequence = (String)request.getParameter("sequence") ;
               if ( name == null || name.trim().length()< 1 )  throw new BecDatabaseException ("Empty linker name") ;
               if( sequence == null || sequence.trim().length()< 1) sequence="NNNN" ;
               name = name.trim(); sequence = sequence.trim().toUpperCase();
               if ( !isCommonPrimerExist( name))
               {
                   sql = " insert into commonprimer (primerid, name,sequence,TM,type) values(oligoid.nextval,'"
                   +name + "','"+sequence +"',"+request.getParameter("tm")+","+request.getParameter("primertype") +")";
                   DatabaseTransaction.executeUpdate(sql, conn);
                    request.setAttribute(Constants.ADDITIONAL_JSP,"Added Common Primer: <P>Name: "+request.getParameter("name") +"<p>Sequence: " +request.getParameter("sequence"));
               }
               else
               {
                    request.setAttribute(Constants.ADDITIONAL_JSP,"Common Primer already exists: <P>Name: "+request.getParameter("name") +"<p>Sequence: " +request.getParameter("sequence"));
               }
               request.setAttribute(Constants.JSP_TITLE,"Request for new Common Primer addition");
               break;
           }    
           case -Constants.PROCESS_ADD_NEW_LINKER  : 
           {
               name = (String)request.getParameter("linkername");
               sequence = (String)request.getParameter("linkersequence") ;
               if ( name == null || name.trim().length()< 1 )       throw new BecDatabaseException ("Empty linker name") ;
               if( sequence == null || sequence.trim().length()< 1)    throw new BecDatabaseException ("Empty linker sequence") ;
               sequence= sequence.toUpperCase().trim();name= name.trim();
                if ( SequenceManipulation.isValidDNASequence( Algorithms.replaceChar(sequence, '-', '\u0000' )))throw new BecDatabaseException ("Wrong linker sequence. Please verify.") ;
             
               if ( ! isLinkerExist(name,  sequence))
               {
                   sql = " insert into linker (linkerid,sequence, name)  values (vectorid.nextval,'"+ sequence +"','"+ name +"')";
                   DatabaseTransaction.executeUpdate(sql, conn);
                    request.setAttribute(Constants.ADDITIONAL_JSP,"Added Linker: <P>Linker Name: "+name +"<p>Linker Sequence: " +sequence);
               }
               else
               {
                    request.setAttribute(Constants.ADDITIONAL_JSP,"Linker already exists: <P>Linker Name: "+name +"<p>Linker Sequence: " +sequence );
               }
               request.setAttribute(Constants.JSP_TITLE,"Request for new Linker addition");
               break;
           }    
           case -Constants.PROCESS_ADD_NAME_TYPE  : 
           {
               // check that name type does not exists
               String name_type = (String) request.getParameter("nametype");
                if( name_type == null || name_type.trim().length()< 1) throw new BecDatabaseException ("Empty name type") ;
               name_type =name_type.toUpperCase().trim();
             
               if ( ! isNameTypeExist( name_type) )
               {
                   sql = " insert into nametype values('"+ name_type +"')";
                   DatabaseTransaction.executeUpdate(sql, conn);
                   request.setAttribute(Constants.ADDITIONAL_JSP,"Added Name type: "+ name_type );
               }
               else
               {
                  request.setAttribute(Constants.ADDITIONAL_JSP,"Name type: "+ name_type +" already exists in database." );
               }
               request.setAttribute(Constants.JSP_TITLE,"Request for new name type addition");
              
               break;
           }
           case -Constants.PROCESS_ADD_SPECIES_DEFINITION  : 
           {
               String speciesname = (String)request.getParameter("speciesname");
               String speciesid = (String)request.getParameter("speciesid");
               if( speciesname == null || speciesname.trim().length()< 1) throw new BecDatabaseException ("Empty species name") ;
               
               speciesname = speciesname.trim();
               int id = BecIDGenerator.getID("speciesid");
               if( speciesid != null )
               {
                   speciesid=speciesid.toUpperCase().trim();
                   sql = " insert into SPECIESDEFINITION values("+ id +",'"+speciesname+"','"+speciesid+"')";
               }
               else
               {
                   sql = " insert into SPECIESDEFINITION values("+ id +",'"+speciesname+"')";
               }
               DatabaseTransaction.executeUpdate(sql, conn);
               request.setAttribute(Constants.JSP_TITLE,"Request for new Species Definition");
               request.setAttribute(Constants.ADDITIONAL_JSP,"Added Species Definition. Species Name: "+speciesname+" Species Specific ID: "+ speciesid);
               DatabaseToApplicationDataLoader.addSpecies( new SpeciesDefinition(id , speciesname,speciesid));
               
               break;
           }
           case -Constants.PROCESS_ADD_PROJECT_DEFINITION  : 
           {
               String pr_code = (String) request.getParameter("projectcode");
               String pr_name = ( String) request.getParameter("projectname") ;
               
               if( pr_code == null || pr_code.trim().length()< 1) throw new BecDatabaseException ("Project code not set") ;
               if( pr_name == null || pr_name.trim().length()< 1) throw new BecDatabaseException ("Project name not set") ;
               pr_code =  pr_code.trim(); pr_name =  pr_name.trim();
               if ( ! isProjectExist( pr_code, pr_name) )
               {
                   sql = " insert into projectdefinition values (projectid.nextval, '"+pr_code+"','"+pr_name+"')";
                   DatabaseTransaction.executeUpdate(sql, conn);
                   request.setAttribute(Constants.ADDITIONAL_JSP,"Added new Project Definition: <P>Project Name: "+ pr_name +"; Project Code: "+pr_code);
                   DatabaseToApplicationDataLoader.addProject(pr_name, pr_code);
               }
               else
               {
                   request.setAttribute(Constants.ADDITIONAL_JSP,"Project already exists: <P>Project Name: "+ pr_name +"; Project Code: "+pr_code);
               }
               request.setAttribute(Constants.JSP_TITLE,"Request for new Project Definition addition");
               break;
           }
            case -Constants.PROCESS_ADD_NEW_CLONINGSTRATEGY:
             {
                    name = (String)request.getParameter("csname");
                    String     start_codon =  (String)request.getParameter("start_codon");//get from form
                    String     fusion_stop_codon =  (String)request.getParameter("fusion_stop_codon");//get from form
                    String    closed_stop_codon =  (String)request.getParameter("closed_stop_codon");//get from form
                    
                    int     vectorid = Integer.parseInt( (String)request.getParameter(Constants.VECTOR_ID_KEY));//get from form
                    int     linker3id = Integer.parseInt( (String) request.getParameter("3LINKERID"));//get from form
                    int     linker5id = Integer.parseInt( (String)request.getParameter("5LINKERID"));//get from form
                    CloningStrategy cs = new CloningStrategy(-1,  vectorid, linker3id,  linker5id, 
                                    start_codon, fusion_stop_codon, closed_stop_codon, name);
                   request.setAttribute(Constants.JSP_TITLE,"Request for new Cloning Startegy addition");
                     
                    if ( name == null) name ="";
                    if ( ! cs.isExist() )
                    {
                        cs.insert(conn);
                          request.setAttribute(Constants.ADDITIONAL_JSP,"Added new Cloning Startegy: <P>Name: "+ cs.getName());
                    }
                    else
                    {
                         request.setAttribute(Constants.JSP_TITLE,"Request for new Project Definition addition");
                        request.setAttribute(Constants.ADDITIONAL_JSP,"The cloning strategy with this parameters already exist. ");
                    }
                    break;
             }
            case -Constants.PROCESS_ADD_TRACE_FILE_NAME_FORMAT:
            {
                //System.out.println("l");
               TraceFileNameFormat format = buildTraceFileNameFormat(request);
               if( format == null) throw new BecDatabaseException ("Format Name not set") ;
               if ( ! isTraceFileFormatExist(  format) )
               {
                   format.insert( conn);
                   request.setAttribute(Constants.ADDITIONAL_JSP,"Added new Trace File Name Format: <P>Format Name: "+ format.getFormatName() );
                   DatabaseToApplicationDataLoader.addTraceFileFormat( format);
               }
               else
               {
                   request.setAttribute(Constants.ADDITIONAL_JSP,"Trace File Name Format already exists: <P>Format Name: "+ format.getFormatName() );
               }
               request.setAttribute(Constants.JSP_TITLE,"Request for new Trace File Name Format addition");
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
                if (  number_of_columns > 0 ) {  if ( rs.getObject("item_1") != null) item.add(rs.getObject("item_1")); else item.add("");}
                if (  number_of_columns > 1){  if ( rs.getObject("item_2") != null)item.add(rs.getObject("item_2")); else item.add("");}
                if ( number_of_columns > 2){  if ( rs.getObject("item_3") != null) item.add(rs.getObject("item_3")); else item.add("");}
                if ( number_of_columns > 3) {  if ( rs.getObject("item_4") != null)item.add(rs.getObject("item_4")); else item.add("");}
                 if ( number_of_columns > 4) {  if ( rs.getObject("item_5") != null)item.add(rs.getObject("item_5")); else item.add("");}
                if ( number_of_columns > 5) {  if ( rs.getObject("item_6") != null)item.add(rs.getObject("item_6")); else item.add("");}
                if ( number_of_columns > 6) {  if ( rs.getObject("item_7") != null)item.add(rs.getObject("item_7")); else item.add("");}
                if ( number_of_columns > 7) {  if ( rs.getObject("item_8") != null)item.add(rs.getObject("item_8")); else item.add("");}
                if ( number_of_columns > 8) {  if ( rs.getObject("item_9") != null)item.add(rs.getObject("item_9")); else item.add("");}
               if ( number_of_columns > 9) {  if ( rs.getObject("item_10") != null)item.add(rs.getObject("item_10")); else item.add("");}
               if ( number_of_columns > 10) {  if ( rs.getObject("item_11") != null)item.add(rs.getObject("item_11")); else item.add("");}
               if ( number_of_columns > 11) {  if ( rs.getObject("item_12") != null)item.add(rs.getObject("item_12")); else item.add("");}
               if ( number_of_columns > 12) {  if ( rs.getObject("item_13") != null)item.add(rs.getObject("item_13")); else item.add("");}
               if ( number_of_columns > 13) {  if ( rs.getObject("item_14") != null)item.add(rs.getObject("item_14")); else item.add("");}
               if ( number_of_columns > 14) {  if ( rs.getObject("item_15") != null)item.add(rs.getObject("item_15")); else item.add("");}
               if ( number_of_columns > 15) {  if ( rs.getObject("item_16") != null)item.add(rs.getObject("item_16")); else item.add("");}
               if ( number_of_columns > 16) {  if ( rs.getObject("item_17") != null)item.add(rs.getObject("item_17")); else item.add("");}
               if ( number_of_columns > 17) {  if ( rs.getObject("item_18") != null)item.add(rs.getObject("item_18")); else item.add("");}
               if ( number_of_columns > 18) {  if ( rs.getObject("item_19") != null)item.add(rs.getObject("item_19")); else item.add("");}
               if ( number_of_columns > 19) {  if ( rs.getObject("item_20") != null)item.add(rs.getObject("item_20")); else item.add("");}
               
                
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
    
    private boolean isNameTypeExist(String  name_type) throws BecDatabaseException
    {
        String sql = "select * from nametype where nametype='"+ name_type + "'";
        //System.out.println( sql +" "+isEntryExist( sql));
         return isEntryExist( sql);   
    }
     private boolean isCommonPrimerExist( String name) throws BecDatabaseException
    {
        String sql = "select * from commonprimer where name='"+ name + "'";
         return isEntryExist( sql);   
    }
    
     private boolean isProjectExist(String  project_code, String project_name) throws BecDatabaseException
    {
        String sql = "select * from projectdefinition where projectcode ='"+ project_code + "' or projectname ='"+ project_name + "'";
        return isEntryExist( sql);   
    }
    
    private boolean isLinkerExist(String name,  String sequence)throws BecDatabaseException
    {
        sequence = Algorithms.replaceChar(sequence,'-', '\u0000');
        String sql = "select * from linker where name ='"+ name + "' or sequence ='"+ sequence + "'";
        return isEntryExist( sql);   
    }
    private boolean isEntryExist(String sql)throws BecDatabaseException
    {
        ResultSet rs = null; 
        try
        {
            rs = DatabaseTransaction.getInstance().executeQuery(sql);
            if(rs.next())
            {
                return true;
            }
            return false;
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException("Cannot verify new entry "+sqlE.getMessage()+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
    
    
    private  TraceFileNameFormat  buildTraceFileNameFormat( HttpServletRequest request)
    {
        TraceFileNameFormat format = new TraceFileNameFormat();
        String value = (String)request.getParameter("FORMATNAME");
        if ( value != null)        format.setFormatName ( value.trim());  //FORMAT_NAME> 
        format.setFileNameReadingDirection( Integer.parseInt((String)request.getParameter("READING_DIRECTION")));
        value = (String)request.getParameter("PLATE_SEPARATOR");  
        if ( value != null ) format.setPlateSeparator (value.trim());//</SEPARATOR>
        value = (String)request.getParameter("POSITION_SEPARATOR");  
        if ( value != null) format.setPositionSeparator (value.trim());//</SEPARATOR>
        value = (String)request.getParameter("DIRECTION_SEPARATOR"); 
        if ( value != null ) format.setDirectionSeparator (value.trim());//</SEPARATOR>
           value = (String)request.getParameter("PLATE_LABEL_COLUMN");  
        if ( value != null && value.trim().length() > 0) format.setPlateLabelColumn ( Integer.parseInt(value.trim()));   //DIRECTION_COLUMN>
      
       value = (String)request.getParameter("PLATE_LABEL_START");    
        if ( value != null && value.trim().length() > 0) format.setPlateLabelStart ( Integer.parseInt(value.trim()));   //DIRECTION_COLUMN>
       value = (String)request.getParameter("PLATE_LABEL_LENGTH");   
        if ( value != null && value.trim().length() > 0) format.setPlateLabelLength ( Integer.parseInt(value.trim()));   //DIRECTION_COLUMN>
       value = (String)request.getParameter("POSITION_COLUMN");   
        if ( value != null && value.trim().length() > 0) format.setPositionColumn ( Integer.parseInt(value.trim()));   //DIRECTION_COLUMN>
       value = (String)request.getParameter("POSITION_START");
        if ( value != null && value.trim().length() > 0) format.setPositionStart ( Integer.parseInt(value.trim()));   //DIRECTION_COLUMN>
       value = (String)request.getParameter("POSITION_LENGTH");   
        if ( value != null && value.trim().length() > 0) format.setPositionLength ( Integer.parseInt(value.trim()));   //DIRECTION_COLUMN>
        value = (String)request.getParameter("DIRECTION_FORWARD");  
        if ( value != null)         format.setDirectionForward (value.trim());   //DIRECTION_FORWARD>
         value = (String)request.getParameter("DIRECTION_REVERSE");   
        if ( value != null)     format.setDirectionReverse (value.trim());   //DIRECTION_REVERSE>
        value = (String)request.getParameter("DIRECTION_COLUMN");   
        if ( value != null && value.trim().length() > 0) format.setDirectionColumn ( Integer.parseInt(value.trim()));   //DIRECTION_COLUMN>
        value = (String)request.getParameter("DIRECTION_LENGTH"); 
        if ( value != null && value.trim().length() > 0) format.setDirectionLength ( Integer.parseInt(value.trim()));   //DIRECTION_COLUMN>
        value = (String)request.getParameter("DIRECTION_START");   
        if ( value != null && value.trim().length() > 0) format.setDirectionStart ( Integer.parseInt(value.trim()));   //DIRECTION_COLUMN>
        return format;
    }
              
    private boolean isTraceFileFormatExist( TraceFileNameFormat format)
    {
        // 
       
        if ( DatabaseToApplicationDataLoader.getTraceFileFormat(format.getFormatName()) != null)  return true;
        return false;
    }
    //99999999999999999999999999999999999999
     public static void main(String args[])
    {
        try
        {
            DirectDatabaseCommunicationsAction.prepareDisplayItems(Constants.PROCESS_ADD_TRACE_FILE_NAME_FORMAT, null);
    
    // ArrayList display_items = DirectDatabaseCommunicationsAction.getDisplayItems("Select nametype as item_1 from nametype", 1);
   //       display_items = DirectDatabaseCommunicationsAction.getDisplayItems("Select PROJECTID  as item_1, PROJECTNAME as item_2 PROJECTCODE  as item_3 from PROJECTDEFINITION ", 3);
                         
        
        }catch(Exception e){}    
     }
}
