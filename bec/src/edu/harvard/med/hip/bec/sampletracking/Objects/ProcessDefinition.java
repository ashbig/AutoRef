//Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->
/**
 * $Id: ProcessDefinition.java,v 1.20 2006-08-28 14:29:05 Elena Exp $
 *
 * File     	: Process.java
 * Date     	: 04162001
 * describes actions available on the system
 * it is read only class: all available process set on system before delivery
 */
package edu.harvard.med.hip.bec.sampletracking.objects;

import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.*;
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
    
    public static final     String    RUN_GAP_MAPPER_FOR_LOWQUALITY_SEQUENCE = "Run Gap Mapper to identify Low Confidence Regions";
    public static final     String    RUN_GAP_MAPPER = "Run Gap Mapper";
    
    public static final     String    RUN_UPDATE_FINAL_CLONE_STATUS = "Update final clone status";
  
    private int             m_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    private String          m_process_name = null;
    private ArrayList       m_spectype_ids = null;
    

    
   
    public ProcessDefinition(int id,  String name, ArrayList ids) 
    {
       m_id = id;
       m_process_name = name;
       m_spectype_ids = ids;
    }
    
    public int             getId(){ return m_id ;}
    public String          getName(){ return m_process_name ;}
    public ArrayList       getSpecs(){ return m_spectype_ids ;}
    
   
    public static int      ProcessIdFromProcessName(String name)throws BecDatabaseException
    {
        Hashtable def = DatabaseToApplicationDataLoader.getProcessDefinitions();
        if (def == null || def.size()== 0 )
            throw new BecDatabaseException("Process definitions are not loaded into the system");
        return ((ProcessDefinition)def.get(name)).getId();
    }
    
    
  
    //******************************************************/
    //			Test				//
    //******************************************************//
    
    // This test also includes testing for ProcessObject.java and its subclasses.
    public static void main(String [] args)
    {
        
        
       
    }
}
