/**
 * $Id: ProcessDefinition.java,v 1.3 2003-04-16 17:49:40 Elena Exp $
 *
 * File     	: Process.java
 * Date     	: 04162001
 * describes actions available on the system
 * it is read only class: all available process set on system before delivery
 */
package edu.harvard.med.hip.bec.sampletracking.objects;

import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.util.*;
import java.util.*;
import java.sql.*;

/**
 * This class represents a process.
 */
public class ProcessDefinition
{
    public static final     String    RUN_ENDREADS_SEQUENCING = "Run End Reads Sequencing";
    public static final     String    RUN_ENDREADS_WRAPPER = "Run End Reads Wrapper";
    public static final     String    RUN_ENDREADS_EVALUATION = "Run End Reads Evaluation";
    public static final     String    RUN_ENDREADS_CONFIRM_RANK = "Confirm clone rank";
    
    private int             m_id = -1;
    private String          m_process_name = null;
    private ArrayList       m_spectype_ids = null;
    
   // public static final     int  END_READ_CONTAINER_CREATIONS = 0;
   // public static final     String  CODE_END_READ_CONTAINER_CREATIONS = "ER";
    
    private static   ArrayList   m_process_definitions = null;
    

    public ProcessDefinition(int id) throws BecDatabaseException
    {
      
    }
    
    public ProcessDefinition(String name) throws BecDatabaseException
    {
      
    }
    public ProcessDefinition(int id,  String name, ArrayList ids) 
    {
       m_id = id;
       m_process_name = name;
       m_spectype_ids = ids;
    }
    
    
    public int      ProcessIdFromProcessName(String name)
    {
        return 0;
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
