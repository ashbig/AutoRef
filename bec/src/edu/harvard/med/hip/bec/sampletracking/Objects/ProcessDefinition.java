/**
 * $Id: ProcessDefinition.java,v 1.1 2003-03-27 17:45:42 Elena Exp $
 *
 * File     	: Process.java
 * Date     	: 04162001
 * describes actions available on the system
 * it is read only class: all available process set on system before delivery
 */
package edu.harvard.med.hip.bec.sampletracking.Objects;

import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.util.*;
import java.util.*;
import java.sql.*;

/**
 * This class represents a process.
 */
public class ProcessDefinition
{
    
    private int             m_id = -1;
    private String          m_process_code = null;
    private String          m_process_name = null;
    private ArrayList       m_spectype_ids = null;
    
    private static   ArrayList   m_process_definitions = null;
    

    public ProcessDefinition(int id) throws BecDatabaseException
    {
      
    }
    
    protected ProcessDefinition(int id, String code, String name, ArrayList ids) 
    {
       m_id = id;
       m_process_code = code;
       m_process_name = name;
       m_spectype_ids = ids;
    }
    
    
    
    
    /**
     * Finds all processes available on the system 
     
     */
    
  
    public static ArrayList findAllProcess()    throws BecDatabaseException,BecUtilException
    {
        if (m_process_definitions == null)
            
        {
            String sql = "Select * from process ";
            ResultSet rs = DatabaseTransaction.getInstance().executeQuery(sql);
            ArrayList res = new ArrayList();
            ProcessDefinition process= null;
            try
            {
                if(rs.next())
                {

                    //process = new ProcessDefinition();
                    res.add(process);

                } else
                {
                    throw new BecUtilException("No process definition found on the system");
                }
            } catch (SQLException sqlE)
            {
                throw new BecDatabaseException(sqlE);
            }
        }
        return m_process_definitions;
    }
    
   
  
    //******************************************************/
    //			Test				//
    //******************************************************//
    
    // This test also includes testing for ProcessObject.java and its subclasses.
    public static void main(String [] args)
    {
        
        
       
    }
}
