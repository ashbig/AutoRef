/**
 * $Id: ProcessDefinition.java,v 1.15 2004-03-30 17:19:51 Elena Exp $
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
import sun.jdbc.rowset.*;
import java.sql.*;
import javax.sql.*;

/**
 * This class represents a process.
 */
public class ProcessDefinition
{
     public static final     String    RUN_UPLOAD_PLATE = "Upload new Plate";
    public static final     String    RUN_ENDREADS_SEQUENCING = "Run End Reads Sequencing";
    public static final     String    RUN_ENDREADS_WRAPPER = "Run End Reads Wrapper";
    public static final     String    RUN_ENDREADS_EVALUATION = "Run End Reads Evaluation";
    public static final     String    RUN_ENDREADS_CONFIRM_RANK = "Confirm clone rank";
    
    public static final     String    RUN_PREPARE_ASSEMBLY = "Prepare for sequence assembly";
    public static final     String    RUN_ASSEMBLY = "Run sequence assembly";
    public static final     String    RUN_ASSEMBLY_FROM_END_READS = "Run sequence assembly from end reads";
    public static final     String    RUN_SEQUENCE_SUBMISSION = "Run sequence data submission";
    
    public static final     String    RUN_DESIGN_OLIGO = "Run oligo design";
    public static final     String    RUN_OLIGO_APPROVAL = "Approve oligos";
    public static final     String    RUN_OLIGO_ORDER = "Run oligo order";
    public static final     String    RUN_OLIGO_ORDER_SEND = "Send oligo order";
    public static final     String    RUN_OLIGO_ORDER_RECIEVED = "Recieve oligo order";
    public static final     String    RUN_OLIGO_PLATE_USED_FOR_SEQUENCING = "Oligo plate used for sequencing";
    public static final     String    RUN_DISCREPANCY_FINDER = "Run discrepancy finder";
    
    
    private int             m_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
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
    
    
    public static int      ProcessIdFromProcessName(String name)throws BecDatabaseException
    {
        String sql = "select  processdefinitionid from processdefinition "+
            "where processname = '"+name +"'";
        CachedRowSet crs = null;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            crs = t.executeQuery(sql);
            if (crs.next())
                return crs.getInt("processdefinitionid");
            else 
                return -1;
        
        } catch (Exception sqlE)
        {
            throw new BecDatabaseException("Error occured while initializing processdefinition with name: "+name+"\n");
        } finally
        {
            DatabaseTransaction.closeResultSet(crs);
        }
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
