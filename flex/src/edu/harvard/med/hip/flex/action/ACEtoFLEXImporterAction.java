/*
 * ACEtoFLEXImporterAction.java
 *
 * Created on May 23, 2007, 12:53 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.action;

import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import org.apache.struts.upload.*;

import edu.harvard.med.hip.flex.form.*;
import edu.harvard.med.hip.flex.infoimport.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.workflow.*;
import edu.harvard.med.hip.flex.user.*;
import edu.harvard.med.hip.flex.Constants;
/**
 *
 * @author htaycher
 */
public class ACEtoFLEXImporterAction extends ResearcherAction
{
    public ActionForward flexPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        ActionErrors errors = new ActionErrors();
        FormFile mgcCloneFile = ((MgcCloneInfoImportForm)form).getMgcCloneFile();
        String fileName =  ((MgcCloneInfoImportForm)form).getFileName();
        InputStream input = null;
        try
        {
            input = mgcCloneFile.getInputStream();
            String clone_data = readFileIntoString(input);
          System.out.println(clone_data);
            String username = ((User)request.getSession().getAttribute(Constants.USER_KEY)).getUsername();
            System.out.println(username);
            ImportInformationRunner import_info = new ImportInformationRunner(clone_data, username);
            Thread t = new Thread(import_info);
            t.start();
            request.setAttribute("message",
            "Information is uploading. It can take some time based on number of clones. The e-mail notification will be sent to you upon completion.");
             request.setAttribute("title","Clone information transfer from ACE to FLEX");
            return mapping.findForward("proccessing");
        } catch (FileNotFoundException ex)
        {
            errors.add("aceImportCloneFile", new ActionError("flex.infoimport.file", ex.getMessage()));
            saveErrors(request,errors);
            return new ActionForward(mapping.getInput());
        } catch (IOException ex)
        {
            errors.add("aceImportCloneFile", new ActionError("flex.infoimport.file", ex.getMessage()));
            saveErrors(request,errors);
            return new ActionForward(mapping.getInput());
        }
        catch (Exception ex1)
        {
            errors.add("aceImportCloneFile", new ActionError("flex.infoimport.file", ex1.getMessage()));
            saveErrors(request,errors);
            return new ActionForward(mapping.getInput());
        }
        
       
        
    }
    public static String      readFileIntoString(InputStream input)throws Exception
        {
            
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            StringBuffer clone_ids = new StringBuffer();
            String line = null;
            try
            {
                while((line = in.readLine()) != null)
                {
                    clone_ids.append(line+" ");
                }
                 input.close();
                 return clone_ids.toString();
            }
            catch(Exception ex)
            {
                try  {input.close(); }catch(Exception ex1){}
                throw new Exception ("Cannot read submission file.");
            }
        }
       
    
    class ImportInformationRunner implements Runnable
    {
        private String      i_clone_data = null;
        private String      i_username = null;
        public ImportInformationRunner(String clone_data, String username)
        {
            i_clone_data = clone_data;
            i_username = username;
        }
        public void run()
        {
            User user = new User(i_username, "");
             // read file to populates clone_ids
            ImportRunner importer = new AceToFlexImporter();
            
            importer.setInputData(ConstantsImport.ITEM_TYPE_CLONEID, i_clone_data);//154431  154512  154516  154520  154454  154245  154532  154269");
            importer.setProcessType( ConstantsImport.PROCESS_DATA_TRANSFER_ACE_TO_FLEX );
            importer.setUser( user);
            importer.run();
           // isBusy = false;
        }
        
        
        
    }
      
    
    
    
   public static void main(String [] args)
    {
        try
        {
               System.out.print("here");
            InputStream input = new FileInputStream("c:\\tmp\\clone.txt");
            String clone_data = readFileIntoString(input);
            System.out.println(clone_data);
            String username = "htaycher";
            System.out.println(username);
            ImportRunner importer = new AceToFlexImporter();
            
            importer.setInputData(ConstantsImport.ITEM_TYPE_CLONEID, clone_data);//154431  154512  154516  154520  154454  154245  154532  154269");
            importer.setProcessType( ConstantsImport.PROCESS_DATA_TRANSFER_ACE_TO_FLEX );
            importer.setUser(new User(username, ""));
            importer.run();
      
           
        }catch(Exception ne){}
        System.exit(0);
      }
}



    

