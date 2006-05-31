//Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->
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
public class DirectDatabaseCommunicationsAction extends BecAction
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
    public synchronized ActionForward becPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        
        ActionErrors errors = new ActionErrors();
        int forwardName = ((Seq_GetSpecForm)form).getForwardName();
       String title = null; String jsp_name = null;
         Connection conn = null;
      
       String sql = null; ArrayList display_items = null;
       User user = null;
         request.setAttribute(Constants.JSP_TITLE,getTitleForProcess(forwardName));
             request.setAttribute(Constants.JSP_CURRENT_LOCATION,getPageLocation(forwardName));
          
        try
        {
            DatabaseTransaction dt = DatabaseTransaction.getInstance();
            conn = dt.requestConnection();
     
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
                        case Constants.PROCESS_ADD_NEW_LINKER  : { jsp_name ="selecting_process"; prepareDisplayItems( forwardName,  request);break;}
                        case Constants.PROCESS_ADD_NAME_TYPE  : {jsp_name ="selecting_process"; prepareDisplayItems( forwardName,  request);break;}
                        case Constants.PROCESS_ADD_SPECIES_DEFINITION  : { jsp_name ="selecting_process";prepareDisplayItems( forwardName,  request); break;}
                        case Constants.PROCESS_ADD_PROJECT_DEFINITION  : { jsp_name ="selecting_process";prepareDisplayItems( forwardName,  request); break;}
                        case Constants.PROCESS_ADD_NEW_VECTOR  : { jsp_name ="selecting_process"; prepareDisplayItems( forwardName,  request); break;}
                        case Constants.PROCESS_SUBMIT_REFERENCE_SEQUENCES  :{ jsp_name ="selecting_process"; break;}
                        case Constants.PROCESS_SUBMIT_CLONE_SEQUENCES:{ jsp_name ="selecting_process"; break;}
                        case Constants.PROCESS_SUBMIT_CLONE_COLLECTION  : { jsp_name ="selecting_process"; break;}
                        case Constants.PROCESS_ADD_NEW_COMMON_PRIMER :{jsp_name ="selecting_process";prepareDisplayItems( forwardName,  request); break;}
                        case Constants.PROCESS_ADD_NEW_CLONINGSTRATEGY:
                        {
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
                             jsp_name ="selecting_process";
                              prepareDisplayItems( forwardName,  request);
                               break;
                         }
                   }
                   if ( forwardName== Constants.PROCESS_ADD_TRACE_FILE_NAME_FORMAT)
                       request.setAttribute("forwardName", new Integer(Constants.PROCESS_VERIFY_TRACE_FILE_FORMAT));
                   else
                       request.setAttribute("forwardName", new Integer(-forwardName));
                    
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
                case Constants.PROCESS_VERIFY_TRACE_FILE_FORMAT:
                {
                      //System.out.println("l");
                       TraceFileNameFormat format = buildTraceFileNameFormat(request);
                      
                       if( format == null) throw new Exception ("Format can not be created. Please check the data you have submitted.") ;
                       if ( isTraceFileFormatWithThisNameExist(format))
                       {
                           request.setAttribute(Constants.ADDITIONAL_JSP,"Trace File Name Format with this name already exists. <P>Format name: "+ format.getFormatName() );
                           return mapping.findForward("processing");
                       }
                       String identical_format_name =  isTraceFileFormatExist(  format) ;
                       if (identical_format_name != null)

                       {
                           request.setAttribute(Constants.ADDITIONAL_JSP,"Trace File Name Format already exists: <P>Format name: "+ identical_format_name );
                           return mapping.findForward("processing");
                       }
                       ArrayList trace_filename_parsing_result = null;
                       
                       try
                       {
                           try
                           {
                               format.isFormatDefinitionOK();
                                trace_filename_parsing_result = parseTraceFileNameForUI(format);
                                request.setAttribute("EXAMPLE_FILE_NAME_RESULT", trace_filename_parsing_result);
                           }
                           catch(Exception e)
                           {
                                   request.setAttribute("ERROR_MESSAGE", e.getMessage());
                           }
                           putFormatParametersIntoRequest(request);
                           request.setAttribute("forwardName",  new Integer(-Constants.PROCESS_ADD_TRACE_FILE_NAME_FORMAT));
                           return (mapping.findForward("format_verification"));
                       }
                       catch(Exception e)
                       {
                           request.setAttribute(Constants.ADDITIONAL_JSP,"Example trace file name cannot be parced using submitted format. <P>Please, verify your format parameters" );
                           
                           return (mapping.findForward("processing"));
                       }
                      
                       
                }
                case Constants.PROCESS_DELETE_TRACE_FILE_FORMAT:
                {
                    int format_id = -1;
                    String format_name = request.getParameter("FORMAT_NAME");
                       
                    if (request.getParameter("ID") != null && format_name != null && DatabaseToApplicationDataLoader.getTraceFileFormat(format_name) != null)
                    {
                         TraceFileNameFormat.deleteFormatById(conn, 0, request.getParameter("ID")); 
                        DatabaseToApplicationDataLoader.getTraceFileFormats().remove(format_name);
                        request.setAttribute(Constants.ADDITIONAL_JSP,"Trace file name format "+format_name +" has been deleted");
                    }
                    else
                    {
                        request.setAttribute(Constants.ADDITIONAL_JSP,"Trace file format "+format_name +" not found. Please, try to refresh you page.");
                    }
                    return mapping.findForward("processing");
                }
                case Constants.DISPLAY_TRACE_FILE_FORMAT_EXAMPLE:
                {
                    String format_name = request.getParameter("FORMAT_NAME");
                    if ( format_name != null && DatabaseToApplicationDataLoader.getTraceFileFormat(format_name) != null)
                    {
                         TraceFileNameFormat format = DatabaseToApplicationDataLoader.getTraceFileFormat(format_name);
                     
                       ArrayList trace_filename_parsing_result = parseTraceFileNameForUI(format);
                       request.setAttribute("EXAMPLE_FILE_NAME_RESULT", trace_filename_parsing_result);
                       request.setAttribute("FORMATNAME", format_name);
                       request.setAttribute("EXAMPLE_TRACE_FILE_NAME", format.getExampleFileName());
                       
                       return (mapping.findForward("display_format"));    }
                    else
                    {
                        request.setAttribute(Constants.ADDITIONAL_JSP,"Trace file format "+format_name +" not found. Please, try to refresh you page.");
                    }
                        
                }
                
            }
                              
            
        }
        catch (Exception e)
        {
            String error_message = e.getMessage();
            System.out.println(error_message);
            int message_start = (0 > error_message.lastIndexOf(":"))? 0 : error_message.lastIndexOf(":")+1;
             error_message = error_message.substring(message_start);
            System.out.println(error_message);
             errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.container.querry.parameter",  error_message));
            saveErrors(request,errors); 
            try
            {
                
                conn.rollback();
                request.setAttribute(Action.EXCEPTION_KEY, e);
                return (mapping.findForward("error"));
            } catch (Exception e1)
            {
                request.setAttribute(Action.EXCEPTION_KEY, e);
                return (mapping.findForward("error"));
            }
        }
        finally
        {
            if(conn != null)            DatabaseTransaction.closeConnection(conn);
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
                display_pagetitle = "Currently Available Annotation Types";
                display_title.add("Reference Sequence Annotation Type"); 
                sql = "Select nametype as item_1 from nametype order by nametype";
                 display_items = getDisplayItems(sql, display_title.size());
                break;
            }
            case Constants.PROCESS_ADD_SPECIES_DEFINITION  :
           case Constants.PROCESS_VIEW_ALL_SPECIES_DEFINITION:
            {  
                SpeciesDefinition sd = null;
                display_pagetitle="Currently Available Species Definitions";
                display_title.add("ACE Species ID");
                display_title.add("Taxomic Species Name");
                display_title.add("Species Specific Identifier");
                sql = "Select SPECIESID as item_1, SPECIESNAME as item_2, IDNAME  as item_3 from SPECIESDEFINITION order by SPECIESNAME";
                 display_items = getDisplayItems(sql, display_title.size());
                break;
            }
            case Constants.PROCESS_ADD_PROJECT_DEFINITION  : 
            
           case Constants.PROCESS_VIEW_ALL_PROJECT_DEFINITION  :      
            {     
                
                display_pagetitle = "Currently Available Project Definitions";
                display_title.add("Project Identifier");
                display_title.add("Project Name");
                display_title.add("Project Description");
                sql = "Select PROJECTID  as item_1, PROJECTNAME as item_2, PROJECTdescription  as item_3 from PROJECTDEFINITION order by PROJECTNAME";
                display_items = getDisplayItems(sql, display_title.size());
                break;
            }
            case Constants.PROCESS_ADD_NEW_LINKER  :
            {     
                
                display_pagetitle = "Currently Available Linkers";
                display_title.add("Linker Name");display_title.add("Linker Sequence");
                sql = "Select  name as item_1, sequence  as item_2 from linker order by name";
                display_items = getDisplayItems(sql, display_title.size());
                break;
            }
            case Constants.PROCESS_ADD_NEW_CONNECTION_VECTOR_LINKER:
            {
                display_pagetitle = "Currently Available Vector - Sequencing Primer Pairs";
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
                display_pagetitle = "Currently Available Sequencing Primers";
                display_title.add("Primer Name");
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
                display_pagetitle = "Currently Available Vectors";
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
               display_pagetitle = "Currently Available Cloning Strategies";
               display_title.add("ACE Cloning Startegy ID");
               display_title.add("Cloning Startegy Name");
               display_title.add("First Codon");display_title.add("Fusion Last Codon");
                display_title.add("Closed Last Codon");
                display_title.add("Vector");
                display_title.add("5' Linker");display_title.add("3' Linker");
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
                display_pagetitle = "Currently Available Trace File Name Formats";
               display_title.add("Format Name");//0
               display_title.add("File name reading direction");//0
               display_title.add("Plate Label Separator");//1
               display_title.add("Plate Label Column");//2
               display_title.add("Plate Label Start"); //3
               display_title.add("Plate Label Length");  //4
               display_title.add("Well Separator");     //5      
               display_title.add("Well Column"); //6
               display_title.add("Well Start");     //7 
               display_title.add("Well Length"); //8
               display_title.add("Direction Forward");  //9 
               display_title.add("Direction Reverse"); //10
               display_title.add("Direction Separator");//11
               display_title.add("Direction Column"); //12
               display_title.add("Direction Length");  //13
               display_title.add("Direction Start");  //14
               display_title.add("Example file name");
		display_title.add("Delete Format");
                
                TraceFileNameFormat format = null;
                ArrayList item = new ArrayList();
                String item_value = null;
                for (Enumeration e = DatabaseToApplicationDataLoader.getTraceFileFormats().keys() ; e.hasMoreElements() ;)
                {
                    format = (TraceFileNameFormat)DatabaseToApplicationDataLoader.getTraceFileFormats().get( e.nextElement() );
                    item = new ArrayList();
                    item.add(format.getFormatName());//"Format Name");//0
                    item_value = ( format.getFileNameReadingDirection()== TraceFileNameFormat.READING_LEFT_TO_RIGHT)?
                       "Left to Right" : "Right to Left";
                        item.add(item_value);
                    
                    item_value = ( format.getPlateSeparator()==null)? "Not Set":String.valueOf(format.getPlateSeparator());
                    item.add(item_value);//"Plate Label  Column");//2
                   
                    item_value = ( format.getPlateLabelColumn()==-1)? "Not Set":String.valueOf(format.getPlateLabelColumn());
                    item.add(item_value);//"File name reading direction");//0
                    
                    item_value = ( format.getPlateLabelStart()==-1)? "Not Set":String.valueOf(format.getPlateLabelStart());
                    item.add(item_value);//"Plate Label Start"); //3
                    
                    item_value = ( format.getPlateLabelLength()==-1)? "Not Set":String.valueOf(format.getPlateLabelLength());
                    item.add(item_value);//"Plate Label Length");  //4
                    
                    item_value = ( format.getPositionSeparator() == null)? "Not Set":String.valueOf(format.getPositionSeparator());
                    item.add(item_value);//"Well Separator");     //5      
                    item_value = ( format.getPositionColumn()==-1)? "Not Set":String.valueOf(format.getPositionColumn());
                    item.add(item_value);//"Well Column"); //6
                    item_value = ( format.getPositionStart()==-1)? "Not Set":String.valueOf(format.getPositionStart());
                    item.add(item_value);//"Well Start");     //7 
                    item_value = ( format.getPositionLength()==-1)? "Not Set":String.valueOf(format.getPositionLength());
                    item.add(item_value);//"Well Length"); //8
                    
                    item_value = ( format.getDirectionForward()==null)? "Not Set":String.valueOf(format.getDirectionForward());
                    item.add(item_value);//"Direction Forward");  //9 
                    item_value = ( format.getDirectionReverse()==null)? "Not Set":String.valueOf(format.getDirectionReverse());
                    item.add(item_value);//"Direction Reverse"); //10
                    item_value = ( format.getDirectionSeparator()== null)? "Not Set":String.valueOf(format.getDirectionSeparator());
                    item.add(item_value);//"Direction Separator");//11
                    item_value = ( format.getDirectionColumn() == -1)? "Not Set":String.valueOf(format.getDirectionColumn());
                    item.add(item_value);//"Direction Column"); //12
                    item_value = ( format.getDirectionLength()==-1)? "Not Set":String.valueOf(format.getDirectionLength());
                    item.add(item_value);//"Direction Length");  //13
                    item_value = ( format.getDirectionStart()==-1)? "Not Set":String.valueOf(format.getDirectionStart());
                    item.add(item_value);//"Direction Start");  //14
   
String anchor =  "<div align=center><A HREF='' onClick=\"window.open(\'"+BecProperties.getInstance().getProperty("JSP_REDIRECTION") +"DirectDatabaseCommunications.do?forwardName="+ Constants.DISPLAY_TRACE_FILE_FORMAT_EXAMPLE + "&FORMAT_NAME="+format.getFormatName()+"\',\'newWndNt\',\'width=300,height=100,menubar=no,location=no,scrollbars=yes,resizable=yes\');return false;\"> Example</a></div>";
                    item_value = ( format.getExampleFileName() == null)? "&nbsp":anchor;
                    item.add(item_value);//"Example file name");
item_value = "<div align=center><a href=\'"+ BecProperties.getInstance().getProperty("JSP_REDIRECTION") +"DirectDatabaseCommunications.do?ID="+ format.getId()+"&FORMAT_NAME="+format.getFormatName()+"&forwardName="  + Constants.PROCESS_DELETE_TRACE_FILE_FORMAT +"\' onclick=\"return confirm(\'Are you sure you want to delete format "+ format.getFormatName() +"?\');\">Delete </a></div>";
                    item.add(item_value);//"Delete Format");
                    display_items.add(item);
                }
               
                /*
                sql = "select FORMATNAME as item_1 , READING_DIRECTION as item_2, PLATE_SEPARATOR as item_3, PLATE_LABEL_COLUMN as item_4,PLATE_LABEL_START as item_5 ,PLATE_LABEL_LENGTH as item_6, "
                +" POSITION_SEPARATOR  as item_7, POSITION_COLUMN as item_8,POSITION_START  as item_9,POSITION_LENGTH as item_10,"
                +" DIRECTION_FORWARD as item_11  ,DIRECTION_REVERSE as item_12 ,"
                +" DIRECTION_SEPARATOR  as item_13, DIRECTION_COLUMN as item_14,DIRECTION_LENGTH as item_15 ,"
                +" DIRECTION_START item_16, EXAMPLE_FILE_NAME as item_17 from TRACEFILEFORMAT order by FORMATNAME";
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
                    
                 }*/
                 break;
            }
          
       }
        display_items.add(0, display_title);
        request.setAttribute("display_items", display_items);
        request.setAttribute("display_title", display_pagetitle);
    }
    
  private void putFormatParametersIntoRequest(HttpServletRequest request)
  {
       request.setAttribute("FORMATNAME", request.getParameter("FORMATNAME"));
       request.setAttribute("READING_DIRECTION",request.getParameter("READING_DIRECTION"));
       request.setAttribute("PLATE_SEPARATOR", request.getParameter("PLATE_SEPARATOR")); 
       request.setAttribute("POSITION_SEPARATOR", request.getParameter("POSITION_SEPARATOR"));
       request.setAttribute("DIRECTION_SEPARATOR", request.getParameter("DIRECTION_SEPARATOR"));
       request.setAttribute("PLATE_LABEL_COLUMN", request.getParameter("PLATE_LABEL_COLUMN"));
       request.setAttribute("PLATE_LABEL_START", request.getParameter("PLATE_LABEL_START"));    
        request.setAttribute("PLATE_LABEL_LENGTH", request.getParameter("PLATE_LABEL_LENGTH"));   
        request.setAttribute("POSITION_COLUMN", request.getParameter("POSITION_COLUMN"));   
        request.setAttribute("POSITION_START", request.getParameter("POSITION_START"));
        request.setAttribute("POSITION_LENGTH", request.getParameter("POSITION_LENGTH"));   
        request.setAttribute("DIRECTION_FORWARD", request.getParameter("DIRECTION_FORWARD"));  
        request.setAttribute("DIRECTION_REVERSE", request.getParameter("DIRECTION_REVERSE"));   
        request.setAttribute("DIRECTION_COLUMN", request.getParameter("DIRECTION_COLUMN"));   
        request.setAttribute("DIRECTION_LENGTH", request.getParameter("DIRECTION_LENGTH")); 
        request.setAttribute("DIRECTION_START", request.getParameter("DIRECTION_START"));   
        request.setAttribute("EXAMPLE_TRACE_FILE_NAME", request.getParameter("EXAMPLE_TRACE_FILE_NAME"));
  }
  
  
  
  public static ArrayList parseTraceFileNameForUI(TraceFileNameFormat format) throws Exception
  {
      ArrayList result = new ArrayList();
      SequencingFacilityFileName  fname = new SequencingFacilityFileName(format.getExampleFileName(), format );


      String[] item = {"Plate name", "Not Defined"};
      if (fname != null) { item[1]=fname.getPlateName();  }
      result.add(item); 
      item = new String[2]; item[0]="Well name";
      if (fname != null){ item[1]= fname.getWellName();  }
      result.add(item); item = new String[2];
      item[0]="Well number";
      if (fname != null){ item[1]= String.valueOf(fname.getWellNumber());}
      result.add(item); item = new String[2];
      item[0]="Direction"; item[1]="Not Defined";
      if (fname != null && fname.getOrientation() != null)   item[1]= fname.getOrientation();     
          result.add(item);
      return result;
  }
  
  
     private  void  processJobParsingFile(InputStream input, int forwardName,  HttpServletRequest request, User user)
     {
         switch (forwardName)
        {
            case -Constants.PROCESS_ADD_NEW_VECTOR  :
            
           case -Constants.PROCESS_SUBMIT_REFERENCE_SEQUENCES  :
           
           case -Constants.PROCESS_SUBMIT_CLONE_SEQUENCES:
           
           case -Constants.PROCESS_SUBMIT_CLONE_COLLECTION  : 
           {
               request.setAttribute(Constants.ADDITIONAL_JSP,"Report will be send to you by e-mail." );
              break;
           }
            
         }
           request.setAttribute(Constants.JSP_TITLE,getTitleForProcess(forwardName));
             
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
                  if ( !isLinkVectorPrimerExist(  vectorid ,  primerid , primerposition,  primerorientation ))
               {
                   sql = " insert into vectorprimer (vectorprimerid, vectorid,primerid,position,orientation,leaderlength)"
                   +" values(vectorid.nextval, "+ vectorid +","+primerid+","+primerposition+","+primerorientation+",65)"; 
                   DatabaseTransaction.executeUpdate(sql, conn);
                   request.setAttribute(Constants.ADDITIONAL_JSP,"Link between vector and sequencing primer added.");
              }
               else
               {
                    request.setAttribute(Constants.ADDITIONAL_JSP,"Link between vector and sequencing primer.");
               }
               break;
             }
           case -Constants.PROCESS_ADD_NEW_COMMON_PRIMER  : 
           {
               name = (String) request.getParameter("primername");
               sequence = (String)request.getParameter("sequence") ;
                 if( name != null) name = name.trim();
               if ( sequence != null) sequence=sequence.trim();
               if ( name == null || name.trim().length()< 1 )  throw new Exception ("Empty linker name") ;
               if( sequence == null || sequence.trim().length()< 1) 
                   sequence="NNNN" ;
               else
                   if ( !SequenceManipulation.isValidDNASequence( Algorithms.replaceChar(sequence, '-', '\u0000' )))throw new BecDatabaseException ("Wrong primer sequence. Please verify.") ;
             
               name = name.trim(); sequence = sequence.trim().toUpperCase();
               if ( !isCommonPrimerExist( name))
               {
                   sql = " insert into commonprimer (primerid, name,sequence,TM,type) values(oligoid.nextval,'"
                   +name + "','"+sequence +"',"+request.getParameter("tm")+","+request.getParameter("primertype") +")";
                   DatabaseTransaction.executeUpdate(sql, conn);
                    request.setAttribute(Constants.ADDITIONAL_JSP,"Added sequencing primer: <P>Name: "+name +"<p>Sequence: " +sequence);
               }
               else
               {
                    request.setAttribute(Constants.ADDITIONAL_JSP,"Sequencing primer already exists: <P>Name: "+name +"<p>Sequence: " +sequence);
               }
               break;
           }    
           case -Constants.PROCESS_ADD_NEW_LINKER  : 
           {
               name = (String)request.getParameter("linkername");
               sequence = (String)request.getParameter("linkersequence") ;
               if( name != null) name = name.trim();
               if ( sequence != null) sequence=sequence.trim();
               if ( name == null || name.trim().length()< 1 )       throw new Exception ("Empty linker name") ;
               if( sequence == null || sequence.trim().length()< 1)    throw new Exception ("Empty linker sequence") ;
               sequence= sequence.toUpperCase().trim();name= name.trim();
               boolean res = SequenceManipulation.isValidDNASequence( Algorithms.replaceChar(sequence, '-', '\u0000' ));
               System.out.println("Is sequence ok "+res);
               if ( !res)throw new Exception ("Wrong linker sequence. Please verify.") ;
             
               if ( ! isLinkerExist(name,  sequence))
               {
                   sql = " insert into linker (linkerid,sequence, name)  values (vectorid.nextval,'"+ sequence +"','"+ name +"')";
                   DatabaseTransaction.executeUpdate(sql, conn);
                    request.setAttribute(Constants.ADDITIONAL_JSP,"Added linker: <P>Linker name: "+name +"<p>Linker sequence: " +sequence);
               }
               else
               {
                    request.setAttribute(Constants.ADDITIONAL_JSP,"Linker already exists: <P>Linker name: "+name +"<p>Linker sequence: " +sequence );
               }
               break;
           }    
           case -Constants.PROCESS_ADD_NAME_TYPE  : 
           {
               // check that name type does not exists
               String name_type = (String) request.getParameter("nametype");
                if( name_type == null || name_type.trim().length()< 1) throw new Exception ("Empty Annotation Type") ;
               name_type =name_type.toUpperCase().trim();
             
               if ( ! isNameTypeExist( name_type) )
               {
                   sql = " insert into nametype values('"+ name_type +"')";
                   DatabaseTransaction.executeUpdate(sql, conn);
                   request.setAttribute(Constants.ADDITIONAL_JSP,"Added annotation type: "+ name_type );
               }
               else
               {
                  request.setAttribute(Constants.ADDITIONAL_JSP,"Annotation type: "+ name_type +" already exists in database." );
               }
                       
                break;
           }
           case -Constants.PROCESS_ADD_SPECIES_DEFINITION  : 
           {
               String speciesname = (String)request.getParameter("speciesname");
               String speciesid = (String)request.getParameter("speciesid");
               if( speciesname == null || speciesname.trim().length()< 1) throw new Exception ("Empty species name") ;
               
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
                        request.setAttribute(Constants.ADDITIONAL_JSP,"Added species definition.<P> Species name: "+speciesname+"<P> Species specific ID: "+ speciesid);
               DatabaseToApplicationDataLoader.addSpecies( new SpeciesDefinition(id , speciesname,speciesid));
               
               break;
           }
           case -Constants.PROCESS_ADD_PROJECT_DEFINITION  : 
           {
              // String pr_code = (String) request.getParameter("projectcode");
               String pr_name = ( String) request.getParameter(UI_Constants.PROJECT_NAME) ;
               String pr_description = (String) request.getParameter(UI_Constants.PROJECT_DESCRIPTION);
            //   if( pr_code == null || pr_code.trim().length()< 1) throw new Exception ("Project code not set") ;
             // pr_code =  pr_code.trim(); pr_name =  pr_name.trim();
                 
               if( pr_name == null || pr_name.trim().length()< 1) throw new Exception ("Project name not set") ;
               pr_description = pr_description.trim();
               if ( ! isProjectExist( pr_description, pr_name) )
               {
                   int project_id = BecIDGenerator.getID("projectid");
                   //sql = " insert into projectdefinition values (projectid.nextval, '"+pr_code+"','"+pr_name+"')";
                   sql = " insert into projectdefinition (projectid, projectcode, projectname,projectdescription) "
                   +" values ("+project_id+", '','"+pr_name+"','"+pr_description+"')";
                
                   DatabaseTransaction.executeUpdate(sql, conn);
                   request.setAttribute(Constants.ADDITIONAL_JSP,"Added project definition: <P>Project name: "+ pr_name +"; <P>Project description: "+pr_description);
                   ProjectDefinition project_definition = new ProjectDefinition(  project_id ,"" ,pr_name , pr_description  );
                   DatabaseToApplicationDataLoader.addProject(project_definition);
               
               }
               else
               {
                   request.setAttribute(Constants.ADDITIONAL_JSP,"Project already exists: <P>Project Name: "+ pr_name +"; <P>Project Description: "+pr_description);
               }
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
                         
                    if ( name == null) name ="";
                    if ( ! cs.isExist() )
                    {
                        cs.insert(conn);
                          request.setAttribute(Constants.ADDITIONAL_JSP,"Added cloning startegy: <P>Name: "+ cs.getName());
                    }
                    else
                    {
                        request.setAttribute(Constants.ADDITIONAL_JSP,"The cloning strategy with this parameters already exist. ");
                    }
                    break;
             }
            case -Constants.PROCESS_ADD_TRACE_FILE_NAME_FORMAT:
            {
                //System.out.println("l");
               TraceFileNameFormat format = buildTraceFileNameFormat(request);
               format.insert( conn);
               request.setAttribute(Constants.ADDITIONAL_JSP,"Added trace file name format: <P>Format name: "+ format.getFormatName() );
               DatabaseToApplicationDataLoader.addTraceFileFormat( format);
                       break;
            }
        }
        request.setAttribute(Constants.JSP_TITLE,getTitleForProcess(forwardName));
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
            throw new Exception("Error occured while extracting data"+sqlE.getMessage()+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
    
    private boolean isNameTypeExist(String  name_type) throws Exception
    {
        String sql = "select * from nametype where nametype='"+ name_type + "'";
        //System.out.println( sql +" "+isEntryExist( sql));
         return isEntryExist( sql);   
    }
     private boolean isCommonPrimerExist( String name) throws Exception
    {
        String sql = "select * from commonprimer where name='"+ name + "'";
         return isEntryExist( sql);   
    }
    
     private boolean isProjectExist(String  project_description, String project_name) throws Exception
    {
        String sql = "select * from projectdefinition where projectdescription ='"+ project_description + "' or projectname ='"+ project_name + "'";
        return isEntryExist( sql);   
    }
    
     private boolean isLinkVectorPrimerExist(int vectorid , int primerid , 
                        int primerposition, int primerorientation )throws Exception
     {
         String sql = "select * from vectorprimer where  vectorid="+vectorid+" and primerid ="+primerid+" and position ="+primerposition+" and orientation="+primerorientation;
         return isEntryExist( sql);  
     }
    private boolean isLinkerExist(String name,  String sequence)throws Exception
    {
        sequence = Algorithms.replaceChar(sequence,'-', '\u0000');
        String sql = "select * from linker where name ='"+ name + "' or sequence ='"+ sequence + "'";
        return isEntryExist( sql);   
    }
    private boolean isEntryExist(String sql)throws Exception
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
            throw new Exception("Cannot verify new entry "+sqlE.getMessage()+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
    
    
    private  TraceFileNameFormat  buildTraceFileNameFormat( HttpServletRequest request) throws Exception 
    {
        TraceFileNameFormat format = new TraceFileNameFormat();
       format.setFormatName ( (String)request.getParameter("FORMATNAME").trim());  //FORMAT_NAME> 
       format.setExampleFileName( (String)request.getParameter("EXAMPLE_TRACE_FILE_NAME").trim());   //DIRECTION_COLUMN>
      
       
       format.setFileNameReadingDirection( Integer.parseInt((String)request.getParameter("READING_DIRECTION")));
       format.setPlateSeparator ( (String)request.getParameter("PLATE_SEPARATOR").trim());//</SEPARATOR>
      
       format.setPositionSeparator ((String)request.getParameter("POSITION_SEPARATOR").trim());//</SEPARATOR>
       format.setDirectionSeparator ((String)request.getParameter("DIRECTION_SEPARATOR").trim());//</SEPARATOR>
        
       format.setDirectionForward ((String)request.getParameter("DIRECTION_FORWARD").trim());   //DIRECTION_FORWARD>
       format.setDirectionReverse ((String)request.getParameter("DIRECTION_REVERSE").trim());   //DIRECTION_REVERSE>
       
       format.setPlateLabelColumn ( Integer.parseInt((String)request.getParameter("PLATE_LABEL_COLUMN").trim()));   //DIRECTION_COLUMN>
       format.setPlateLabelStart ( Integer.parseInt((String)request.getParameter("PLATE_LABEL_START").trim()));   //DIRECTION_COLUMN>
       
       format.setPlateLabelLength ( Integer.parseInt((String)request.getParameter("PLATE_LABEL_LENGTH").trim()));   //DIRECTION_COLUMN>
       format.setPositionColumn ( Integer.parseInt((String)request.getParameter("POSITION_COLUMN").trim()));   //DIRECTION_COLUMN>
       format.setPositionStart ( Integer.parseInt((String)request.getParameter("POSITION_START").trim()));   //DIRECTION_COLUMN>
       format.setPositionLength ( Integer.parseInt((String)request.getParameter("POSITION_LENGTH").trim()));   //DIRECTION_COLUMN>
       format.setDirectionColumn ( Integer.parseInt( (String)request.getParameter("DIRECTION_COLUMN").trim()));   //DIRECTION_COLUMN>
       format.setDirectionLength ( Integer.parseInt((String)request.getParameter("DIRECTION_LENGTH").trim()));   //DIRECTION_COLUMN>
       format.setDirectionStart ( Integer.parseInt((String)request.getParameter("DIRECTION_START").trim()));   //DIRECTION_COLUMN>
      
        return format;
    }
              
    private boolean isTraceFileFormatWithThisNameExist( TraceFileNameFormat format)
    {
        // 
       
        if ( DatabaseToApplicationDataLoader.getTraceFileFormat(format.getFormatName()) != null)  return true;
        return false;
    }
    
     public static String isTraceFileFormatExist( TraceFileNameFormat format)
    {
        TraceFileNameFormat format_item = null;

        for(Enumeration e=  DatabaseToApplicationDataLoader.getTraceFileFormats().elements(); e.hasMoreElements();)
       {
            format_item = (TraceFileNameFormat)e.nextElement();
            if ( format_item.isSame(format)) return format_item.getFormatName();
             
       }
        return null;
    }
     
     private String         getTitleForProcess(int process_id)
     {
         switch(process_id)
         {
             case Constants.PROCESS_DELETE_TRACE_FILE_FORMAT:return "Deleting Trace File Names Format";
             case Constants.PROCESS_ADD_NEW_LINKER  :  return "Add Linker"; 
            case Constants.PROCESS_ADD_NAME_TYPE  :return"Add Annotation Type";
            case Constants.PROCESS_ADD_SPECIES_DEFINITION  : return"Add Species Definition"; 
            case Constants.PROCESS_ADD_PROJECT_DEFINITION  : return"Add Project Definition"; 
            case Constants.PROCESS_ADD_NEW_VECTOR  : return"Add Vector"; 
            case Constants.PROCESS_SUBMIT_REFERENCE_SEQUENCES  :return"Submit Reference Sequence Information"; 
            case Constants.PROCESS_SUBMIT_CLONE_SEQUENCES:return"Upload Clone Sequences";
            case Constants.PROCESS_SUBMIT_CLONE_COLLECTION  : return"Submit Clone Collection";
            case Constants.PROCESS_ADD_NEW_COMMON_PRIMER :return"Add Sequencing Primer";
            case Constants.PROCESS_ADD_NEW_CLONINGSTRATEGY:return"Add Cloning Strategy"; 
            case Constants.PROCESS_ADD_NEW_CONNECTION_VECTOR_LINKER:                        return"Link Vector with Sequencing Primer";
            case Constants.PROCESS_ADD_TRACE_FILE_NAME_FORMAT:                         return"Add Trace File Name Format";
            case Constants.PROCESS_VERIFY_TRACE_FILE_FORMAT: return "Verify New Trace File Name Format";
            case -Constants.PROCESS_ADD_NEW_VECTOR  :return "Request to Add Vector ";
            case -Constants.PROCESS_SUBMIT_REFERENCE_SEQUENCES  :return "Request for Reference Sequence Submission";
            case -Constants.PROCESS_SUBMIT_CLONE_SEQUENCES:return "Request for Clone Sequence Upload";
            case -Constants.PROCESS_SUBMIT_CLONE_COLLECTION  :return"Request for Clone Collection Submission";
            case -Constants.PROCESS_ADD_NEW_CONNECTION_VECTOR_LINKER:return"Request to Add Link between Vector and Sequencing Primer";
            case -Constants.PROCESS_ADD_NEW_COMMON_PRIMER  :return"Request to Add Sequencing Primer ";
            case -Constants.PROCESS_ADD_NEW_LINKER  : return"Request to Add Linker ";
            case -Constants.PROCESS_ADD_NAME_TYPE  :           return"Request to Add Annotation Type ";
            case -Constants.PROCESS_ADD_SPECIES_DEFINITION  :           return "Request to Add Species Definition";
            case -Constants.PROCESS_ADD_PROJECT_DEFINITION  : return "Request to Add Project Definition";
            case -Constants.PROCESS_ADD_NEW_CLONINGSTRATEGY:return "Request to Cloning Strategy";
            case -Constants.PROCESS_ADD_TRACE_FILE_NAME_FORMAT:return "Request to Add Trace File Name Format ";
             
             default: return "";  
         }
     }
     
      private String         getPageLocation(int process_id)
     {
         switch(process_id)
         {
             case Constants.PROCESS_DELETE_TRACE_FILE_FORMAT:return "Home > Trace Files > Create Name Format";
             case -Constants.PROCESS_ADD_NEW_LINKER  :
             case Constants.PROCESS_ADD_NEW_LINKER  :  return "Home > Cloning Project Settings > Add Linker"; 
           case -Constants.PROCESS_ADD_NAME_TYPE  : 
             case Constants.PROCESS_ADD_NAME_TYPE  :return"Home > Cloning Project Settings > Annotation Type";
            case -Constants.PROCESS_ADD_SPECIES_DEFINITION  :  
             case Constants.PROCESS_ADD_SPECIES_DEFINITION  : return"Home > Cloning Project Settings > Species Definition"; 
            case -Constants.PROCESS_ADD_PROJECT_DEFINITION  :
             case Constants.PROCESS_ADD_PROJECT_DEFINITION  : return"Home > Cloning Project Settings > Project Definition"; 
            case Constants.PROCESS_ADD_NEW_VECTOR  : return"Home > Cloning Project Settings > Vector Information"; 
            case Constants.PROCESS_SUBMIT_REFERENCE_SEQUENCES  :return"Home > Process > Upload Plate Information > Submit Reference Sequence Information"; 
            case Constants.PROCESS_SUBMIT_CLONE_SEQUENCES:return"Home > Process > Upload Clone Sequences";
            case Constants.PROCESS_SUBMIT_CLONE_COLLECTION  : return"Home > Process > Upload Plate Information > Submit Clone Collection";
            case Constants.PROCESS_ADD_NEW_COMMON_PRIMER :return"Home > Cloning Project Settings > Sequencing Primer";
           case -Constants.PROCESS_ADD_NEW_CLONINGSTRATEGY:
             case Constants.PROCESS_ADD_NEW_CLONINGSTRATEGY:return"Home > Cloning Project Settings > Add Cloning Strategy"; 
            case Constants.PROCESS_ADD_NEW_CONNECTION_VECTOR_LINKER: return"Home > Cloning Project Settings > Link Vector with Sequencing Primer";
            case -Constants.PROCESS_ADD_TRACE_FILE_NAME_FORMAT: 
             case Constants.PROCESS_ADD_TRACE_FILE_NAME_FORMAT: return"Home > Trace Files > Add Trace File Name Format";
            case Constants.PROCESS_VERIFY_TRACE_FILE_FORMAT: return "Home > Cloning Project Settings > Add Trace File Name Format";
            case -Constants.PROCESS_ADD_NEW_VECTOR  :return "Home > Cloning Project Settings > Add Vector"; 
            case -Constants.PROCESS_SUBMIT_REFERENCE_SEQUENCES  :return "Home > Process > Upload Plate Information > Submit Reference Sequence Information"; 
            case -Constants.PROCESS_SUBMIT_CLONE_SEQUENCES:return "Home > Process > Upload Clone Sequence";
            case -Constants.PROCESS_SUBMIT_CLONE_COLLECTION  :return"Home > Process > Upload Plate Information > Submit Clone Collection";
            case -Constants.PROCESS_ADD_NEW_CONNECTION_VECTOR_LINKER:return"Request to Add Connection between Vector and Sequencing Primer";
            case -Constants.PROCESS_ADD_NEW_COMMON_PRIMER  :return"Home > Cloning Project Settings > Sequencing Primer ";
              
             default: return "";  
         }
     }
    //99999999999999999999999999999999999999
     public static void main(String args[])
    {
        ArrayList trace_filename_parsing_result =null;
            
        try
        {
                BecProperties sysProps =  BecProperties.getInstance( BecProperties.PATH);
        sysProps.verifyApplicationSettings();
        edu.harvard.med.hip.bec.DatabaseToApplicationDataLoader.loadDefinitionsFromDatabase();
/*
        TraceFileNameFormat format = new TraceFileNameFormat();
format.setFormatName ( "abc");  //FORMAT_NAME> 
format.setFileNameReadingDirection( TraceFileNameFormat.READING_LEFT_TO_RIGHT);
format.setPlateSeparator ("-");//</SEPARATOR>
format.setPositionSeparator ("-");//</SEPARATOR>
//format.setDirectionSeparator ("");//</SEPARATOR>
format.setPlateLabelColumn ( 1);   //DIRECTION_COLUMN>

format.setPositionColumn ( 2);   //DIRECTION_COLUMN>
format.setPositionLength(3);
format.setPositionStart(1);

format.setDirectionForward ("F");   //DIRECTION_FORWARD>
format.setDirectionReverse ("R");   //DIRECTION_REVERSE>
format.setDirectionColumn ( -1);   //DIRECTION_COLUMN>
format.setExampleFileName( "KSG002372-G01_I.ab1");   //DIRECTION_COLUMN>
     
                               format.isFormatDefinitionOK();
                               */

        
        //TraceFileNameFormat format = DatabaseToApplicationDataLoader.getTraceFileFormat("test_format");
//trace_filename_parsing_result = parseTraceFileNameForUI(format);
                          
                                       
                                    
        
        }catch(Exception e)
        {System.out.println(e.getMessage());}    
        System.exit(0);
     }
}
