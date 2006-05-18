//Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->
/*
 * ResultParser.java
 *
 * Created on June 14, 2005, 12:25 PM
 */

package edu.harvard.med.hip.bec.programs.polymorphism_finder;


import java.util.*;
import java.io.*;
import java.sql.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.coreobjects.feature.*;
import edu.harvard.med.hip.bec.*;

/**
 *
 * @author  htaycher
 */
public class ResultParser
{
    
    
    
    /** Creates a new instance of ResultParser */
    public ResultParser() 
    {
    }
    
    
    public static void    parseResultFileAndSubmitInformation(String job_filename_dir)
    {
        Hashtable   results = null;
        ArrayList error_messages = new ArrayList();
       try
       {
            File output_dir = new File(job_filename_dir);
            File[] output_files = output_dir.listFiles();
            for (int count = 0; count < output_files.length; count++)
            {
                System.out.println("Start "+output_files[count].getAbsolutePath());
                results = parseResultFile(error_messages, output_files[count].getAbsolutePath());

                if ( results.size() > 0)
                    submitPolymorphismInformation(results, error_messages);
                 System.out.println("finished "+output_files[count].getAbsolutePath());
               
            }
         
       }
       catch(Exception e)
       {
           
       }
        finally
        {
            sendEMails("Polymorphism Finder run", error_messages);
        }
    }
    
    
    
    
    private static void             sendEMails(String title, ArrayList error_messages)
     {
         String message = null;
          
         try
         {
            if (error_messages != null && error_messages.size()>0)
            {
                String file_name = Constants.getTemporaryFilesPath() + File.separator + "ErrorMessages"+ System.currentTimeMillis()+".txt";
                Algorithms.writeArrayIntoFile( error_messages, false,  file_name) ;
                message = title+ Constants.LINE_SEPARATOR + "Please find error messages for your request in  attached file";
                Mailer.sendMessageWithAttachedFile(BecProperties.getInstance().getACEEmailAddress(), BecProperties.getInstance().getACEEmailAddress(),
                null,title, message ,                 new File(file_name) );
            }
            else
            {
                 Mailer.sendMessage      (  BecProperties.getInstance().getACEEmailAddress(), 
                    BecProperties.getInstance().getACEEmailAddress(),
                    null, title, message);
               
            }
     
        }
        catch(Exception e){   System.out.println(e.getMessage());  
    }
     }
     
     
    private static Hashtable   parseResultFile(ArrayList error_messages,  String job_filename ) throws BecUtilException
    {
        Hashtable info = new Hashtable();
         BufferedReader input = null; String line = null;
        ArrayList discr_data = null;
       String current_value = null;
       try
       {
            input = new BufferedReader(new FileReader(job_filename));
           // discrepancyId Id_type Id_value
           //discrepancyId “NODATA”
           String discr_hit = null;

           while ((line = input.readLine()) != null)
            {
                discr_data = Algorithms.splitString(line);
                current_value = (String)info.get(  discr_data.get(0));
              
                if (discr_data.size() == 3)
                {
                    discr_hit = (String) discr_data.get(1) + " "+ discr_data.get(2) +"|";
                }
                else if ( discr_data.size() == 2 && ((String)discr_data.get(1)).equalsIgnoreCase("NODATA"))
                {
                    discr_hit =  (String)discr_data.get(1)  ;
                }
                 
                if ( current_value == null)
                    info.put( (String) discr_data.get(0), discr_hit );
                else
                {
                    current_value += discr_hit;
                    info.put( (String) discr_data.get(0),current_value );
                }
             }
            input.close();
          return info;
       }
       catch(Exception e)
       {
           error_messages.add("Cannot parse polymorphism info file "+job_filename);
           throw new BecUtilException ("Cannot parse polymorphism info file");
       }
     }
    
    private static void       submitPolymorphismInformation(Hashtable discr_info, ArrayList error_messages)throws BecUtilException
    {
       // Polymorphism flag  yes/ no
        //Polymorphismid  GI1 | GI2 | GI3
        //Polymorphism date:   current date
        String discr_id = null; String discr_data = null;
            PreparedStatement  pst_update_discrepancy_no_polym = null;  
              PreparedStatement  pst_update_discrepancy_polym = null;  
      
        Connection conn = null;
         try
         {
                // conncection to use for transactions
                conn = DatabaseTransaction.getInstance().requestConnection();
                pst_update_discrepancy_polym = conn.prepareStatement("update discrepancy set polymflag = ?,polymid=?, polmdate=sysdate where discrepancyid=?");
                pst_update_discrepancy_no_polym = conn.prepareStatement("update discrepancy set polymflag = ?, polmdate=sysdate where discrepancyid=?");
              
                for ( Enumeration e = discr_info.keys(); e.hasMoreElements();)
                {
                     discr_id = (String) e.nextElement();
                     discr_data = (String) discr_info.get(discr_id);
                     discr_data = Algorithms.replaceString(discr_data, "NODATA", "");
                            
                     try
                     {
                         if ( discr_data.equalsIgnoreCase(""))
                         {
                             pst_update_discrepancy_no_polym.setInt(1, Mutation.FLAG_POLYM_NO);
                             pst_update_discrepancy_no_polym.setInt(2,  Integer.parseInt( discr_id));
                             System.out.println(discr_id +" NO DATA ");
                             DatabaseTransaction.executeUpdate(pst_update_discrepancy_no_polym);
                         
                         }
                         else
                         {
                              pst_update_discrepancy_polym.setInt(3, Integer.parseInt( discr_id));
                              pst_update_discrepancy_polym.setInt(1, Mutation.FLAG_POLYM_YES);
                              pst_update_discrepancy_polym.setString(2, discr_data);
                              System.out.println(discr_id +" "+discr_data);
                              DatabaseTransaction.executeUpdate(pst_update_discrepancy_polym);
                         
                         }
                           conn.commit();
                     }
                     catch(Exception e1)
                     {
                           DatabaseTransaction.rollback(conn);
                           error_messages.add("Cannot update discrepancy: discrepancy id: "+discr_id);
             
                     }
           
                }
                
         }
         catch(Exception e)
         {
              DatabaseTransaction.rollback(conn);
             if ( conn != null) DatabaseTransaction.closeConnection(conn);
              error_messages.add("Cannot open connection");
               throw new BecUtilException("Cannot open connection");
         }
    }
    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
            //BecProperties sysProps =  BecProperties.getInstance( BecProperties.PATH);
            //sysProps.verifyApplicationSettings();
            //DatabaseToApplicationDataLoader.loadDefinitionsFromDatabase();
String output_dir = null;

if (args.length > 0 ) output_dir = args[0];
else output_dir = "C:\\bio\\polymorphismfinder\\output_data";
     
        ResultParser.parseResultFileAndSubmitInformation(output_dir);
        // TODO code application logic here
    }
    
}
