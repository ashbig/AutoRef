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
    
    
      private final static   String FILE_NAME_OUTPUT_DATA_FILE = "pl_output_data.txt";
  
    /** Creates a new instance of ResultParser */
    public ResultParser() 
    {
    }
    
    
    public static void    parseResultFileAndSubmitInformation()
    {
        Hashtable   results = null;
        ArrayList error_messages = new ArrayList();
       try
       {
            results = parseResultFile(error_messages);
            if ( results.size() > 0)
                submitPolymorphismInformation(results, error_messages);
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
                Mailer.sendMessageWithAttachedFile("hip_informatics@hms.harvard.edu", "hip_informatics@hms.harvard.edu",
                "hip_informatics@hms.harvard.edu",title, message , 
                new File(file_name) );
            }
            else
            {
                 Mailer.sendMessage      (  "hip_informatics@hms.harvard.edu", "hip_informatics@hms.harvard.edu",  null, title, message);
                     
            }
     
        }
        catch(Exception e){   System.out.println(e.getMessage());  
    }
     }
     
     
    private static Hashtable   parseResultFile(ArrayList error_messages) throws BecUtilException
    {
        Hashtable info = new Hashtable();
         BufferedReader input = null; String line = null;
        ArrayList discr_data = null;
       String current_value = null;
        String job_filename = edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("POLYORPHISM_FINDER_DATA_DIRECTORY") + java.io.File.separator + FILE_NAME_OUTPUT_DATA_FILE;
       try
       {
           File job_file = new File(job_filename);
           if ( ! job_file.exists() ) throw new BecUtilException("No file with Polymorphism information was detected.");
           input = new BufferedReader(new FileReader(job_filename));
           // discrepancyId Id_type Id_value
           //discrepancyId “NODATA”


           while ((line = input.readLine()) != null)
            {
                discr_data = Algorithms.splitString(line, " ");
                if (discr_data.size() == 2 && ((String)discr_data.get(1)).equalsIgnoreCase("NODATA"))
                {
                    info.put( (String) discr_data.get(0),"NODATA" );
                    continue;
                }
                if ( discr_data.size() != 3) continue;
                current_value = (String)info.get     (  discr_data.get(0));
                if ( current_value == null)
                    info.put( (String) discr_data.get(0), discr_data.get(1) + " "+ discr_data.get(2) +"|" );
                else
                {
                    current_value +=discr_data.get(1) + " "+ discr_data.get(2) +"|";
                    info.put( (String) discr_data.get(0),current_value );
         
                }
             }
            input.close();
          return info;
       }
       catch(Exception e)
       {
           error_messages.add("Cannot parse polymorphism info file");
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
                       try
                     {
                         if ( discr_data.equalsIgnoreCase("NODATA"))
                         {
                             pst_update_discrepancy_no_polym.setInt(1, Mutation.FLAG_POLYM_NO);
                             pst_update_discrepancy_no_polym.setInt(2,  Integer.parseInt( discr_id));
                             DatabaseTransaction.executeUpdate(pst_update_discrepancy_no_polym);
                         
                         }
                         else
                         {
                             pst_update_discrepancy_polym.setInt(3, Integer.parseInt( discr_id));
                              pst_update_discrepancy_polym.setInt(1, Mutation.FLAG_POLYM_YES);
                              pst_update_discrepancy_polym.setString(2, discr_data);
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
            BecProperties sysProps =  BecProperties.getInstance( BecProperties.PATH);
            sysProps.verifyApplicationSettings();
            DatabaseToApplicationDataLoader.loadDefinitionsFromDatabase();

        ResultParser.parseResultFileAndSubmitInformation();
        // TODO code application logic here
    }
    
}
