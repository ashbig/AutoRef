/*
 * DatabaseCommunicationsRunner.java
 *
 * Created on March 14, 2005, 11:03 AM
 */

package edu.harvard.med.hip.bec.action_runners;


import java.util.*;
import java.io.*;
import sun.jdbc.rowset.*;
import java.sql.*;

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import org.apache.xerces.parsers.SAXParser;


import edu.harvard.med.hip.bec.user.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.programs.parsers.*;
import edu.harvard.med.hip.bec.coreobjects.spec.*;

/**
 *
 * @author  htaycher
 */
public class DatabaseCommunicationsRunner  extends ProcessRunner
{
    private InputStream                 m_input_stream = null;
    private int                         m_process_type = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    
    
    public void     setProcessType(int i){ m_process_type = i;}
    public void     setInputStream(InputStream i){ m_input_stream = i;}
    public String getTitle() 
    {  
        switch (m_process_type)
        {
            case -Constants.PROCESS_ADD_NEW_VECTOR  :
            {
               return "Request for new vector submission";
            }
           case -Constants.PROCESS_SUBMIT_REFERENCE_SEQUENCES  :
           {
                return "Request for reference sequence submission";
             
           }
           case -Constants.PROCESS_SUBMIT_CLONE_COLLECTION  : 
           {
               return "Request for clone collection submission";
           }
            default : return "";
         }
    }
   
   public void run()
    {
         Connection  conn =null;
         try
         {
                conn = DatabaseTransaction.getInstance().requestConnection();
               switch (m_process_type)
                {
                    case -Constants.PROCESS_ADD_NEW_VECTOR  :
                    {
                        VectorInfoParser SAXHandler = new VectorInfoParser();
                        SAXParser parser = new SAXParser();
                        parser.setContentHandler(SAXHandler);
                        parser.setErrorHandler(SAXHandler);
                        parser.parse(new org.xml.sax.InputSource(m_input_stream));
                        ArrayList v= SAXHandler.getBioVectors();
                        for (int count = 0; count < v.size();count++)
                        {
                            ((BioVector)v.get(count)).insert(conn);
                        }
                        conn.commit();
                        break;
                    }
                   case -Constants.PROCESS_SUBMIT_REFERENCE_SEQUENCES  :
                   {
                          break;
                   }
                   case -Constants.PROCESS_SUBMIT_CLONE_COLLECTION  : 
                   {
                        break;
                   }
                 }
         } 
        catch(Exception e)  
        {
            m_error_messages.add(e.getMessage());
        }
        finally
        {
            sendEMails( getTitle() );
            DatabaseTransaction.closeConnection(conn);
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
    
    
}
