/*
 * PrimerDesignerRunner.java
 *
 * Created on October 27, 2003, 5:11 PM
 */

package edu.harvard.med.hip.bec.action_runners;


import java.sql.*;
import java.io.*;

import  edu.harvard.med.hip.bec.programs.primer3.*;
import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.programs.needle.*;
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.coreobjects.oligo.*;
import edu.harvard.med.hip.bec.coreobjects.feature.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.form.*;
import edu.harvard.med.hip.bec.user.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.Constants;
import edu.harvard.med.hip.bec.sampletracking.mapping.*;
import edu.harvard.med.hip.bec.sampletracking.objects.*;
import edu.harvard.med.hip.bec.programs.assembler.*;
import edu.harvard.med.hip.bec.util_objects.*;
import edu.harvard.med.hip.bec.ui_objects.*;
  import java.util.*;
  import edu.harvard.med.hip.utility.*;
/**
 *
 * @author  HTaycher
 */
public class PrimerDesignerRunner extends ProcessRunner 
{
    
    private int                     m_spec_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    private Primer3Spec             m_spec = null;
    private boolean                 m_isTryMode = false;
   
    /** Creates a new instance of PolymorphismFinderRunner */
    public void         setSpecId(int v){m_spec_id = v;}
    public void         setIsTryMode(boolean isTryMode){m_isTryMode=isTryMode;}
    public void run()
    {
         int id = -1; int process_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
         Connection conn = null;
         OligoCalculation oligo_calculation = null;
         String sql = "";
         Statement stmt = null;
         PreparedStatement pst_check_oligo_cloning = null;
          PreparedStatement pst_insert_process_object = null;
           FileWriter reportFileWriter = null;
        try
        {
            conn = DatabaseTransaction.getInstance().requestConnection();
            pst_insert_process_object = conn.prepareStatement("insert into process_object (processid,objectid,objecttype) values(?,?,"+Constants.PROCESS_OBJECT_TYPE_REFSEQUENCE+")");
            pst_check_oligo_cloning = conn.prepareStatement("select sequenceid from oligo_calculation where sequenceid = ? and primer3configid = "+m_spec_id);
            //get primer spec
            if ( !getPrimer3Spec()  ) return;
            //gett refsequence ids 
            sql = getQueryString(-1);
            if ( sql == null)        { return ; }
            ArrayList  ids =     getRefSequenceIds(sql);
            if ( ids == null || ids.size() <1 ) return;
            //create process
            ArrayList specs =new ArrayList();
            specs.add(m_spec);
            process_id = Request.createProcessHistory( conn, ProcessDefinition.RUN_DESIGN_OLIGO,specs,m_user) ;
           
            if ( m_isTryMode )
            {
                File reportFile = new File(FILE_PATH + "primer3Report.txt");
                m_file_list_reports.add(reportFile);
                reportFileWriter =  new FileWriter(reportFile);
                
            }
            for (int index =  0;  index < ids.size(); index++)
             {
                synchronized(this)
                {
                    try
                        {

                            id = ((Integer) ids.get(index)).intValue();
                            if (  isPrimersDesignedForRefsequence(id, pst_check_oligo_cloning) )
                            {
                                m_error_messages.add("Primers for refsequence with id "+id +"and this spec already designed.");
                                if ( !m_isTryMode  ) continue;
                            }
                        //get reference sequence to process
                            BaseSequence refsequence = getRefsequence(id);
                        //run primer3 with specified spec
                            Primer3Wrapper primer3 = new Primer3Wrapper(m_spec,  refsequence);
                            ArrayList oligo_calculations = primer3.run();
     
                            if ( primer3.getFailedSequences() != null && primer3.getFailedSequences().size() >0)
                                m_error_messages.addAll(primer3.getFailedSequences());
                            if ( oligo_calculations == null || oligo_calculations.size() < 1)
                            {
                                m_error_messages.add("Cannot design primers for refsequence with id: "+refsequence.getId());
                                continue;
                            }
                            oligo_calculation = (OligoCalculation)oligo_calculations.get(0);

                            if ( ! m_isTryMode )
                                oligo_calculation.insert(conn);
                            else
                            {
                                
                                StringBuffer buf = new StringBuffer();
                                buf.append(Constants.LINE_SEPARATOR );
                                buf.append("RefSequence Id: "+oligo_calculation.getSequenceId()+Constants.LINE_SEPARATOR);
                                buf.append("Primer3 Spec Id: "+oligo_calculation.getPrimer3SpecId()+Constants.LINE_SEPARATOR);
                                for (int oligo_index = 0; oligo_index < oligo_calculation.getOligos().size();oligo_index ++)
                                {
                                    buf.append( ((Oligo)oligo_calculation.getOligos().get(oligo_index)).geneSpecificOligotoString()+Constants.LINE_SEPARATOR);
                                }
                                reportFileWriter.write( buf.toString() );
                                reportFileWriter.flush();
                               
                            }
                            //insert process_object
                            pst_insert_process_object.setInt(1,process_id);
                            pst_insert_process_object.setInt(2, id);
                            DatabaseTransaction.executeUpdate(pst_insert_process_object);
                            conn.commit();
                        }
                        catch(Exception e)
                        {
                            m_error_messages.add(e.getMessage());
                        }
                    }
                
             }
        }
        catch(Exception e)
        {
            m_error_messages.add(e.getMessage());
        }
        finally
        {
            if ( m_isTryMode )
            {
                try {reportFileWriter.close();}catch(Exception e){ try { reportFileWriter.close();}catch(Exception n){} }
            }
        
            sendEMails();
        }

            //request->process->process_config|| process_object(refsequence)
    }
    
    
    
    //------------------------------------------------------------------
    private boolean getPrimer3Spec()
    {  //get primer spec
        try
        {
            m_spec = (Primer3Spec)Spec.getSpecById(m_spec_id);
            if (m_spec != null)            return true;
            return false;
        }
        catch(Exception e)
        {
            m_error_messages.add("Cannot extract primer3 configuration with id "+m_spec_id);
            return false;
        }
    }
    
    
    private BaseSequence getRefsequence(int id) throws Exception
    {
  
        String sequence_text = BaseSequence.getSequenceInfo(id, BaseSequence.SEQUENCE_INFO_TEXT);
        BaseSequence refsequence = new BaseSequence();
        refsequence.setText(sequence_text);
        refsequence.setId(id);
        return refsequence;
        
    }
    
    
    private String      getQueryString(int rownum)
    {
        ArrayList items = Algorithms.splitString( m_items);
        switch ( m_items_type)
        {
            case  Constants.ITEM_TYPE_CLONEID :
            {
                return "select distinct refsequenceid from sequencingconstruct where constructid in "
                +" (select constructid from isolatetracking where isolatetrackingid in "
                +" (select isolatetrackingid from flexinfo where flexcloneid in ("+Algorithms.convertStringArrayToString(items,"," )+")))";
            }
            case  Constants.ITEM_TYPE_PLATE_LABELS :
            {
                StringBuffer plate_names = new StringBuffer();
                for (int index = 0; index < items.size(); index++)
                {
                    plate_names.append( "'");
                    plate_names.append((String)items.get(index));
                    plate_names.append("'");
                    if ( index != items.size()-1 ) plate_names.append(",");
                }
                 return "select distinct refsequenceid from sequencingconstruct where constructid in "
                +"  (select constructid from isolatetracking where sampleid in "
                +"  (select sampleid from sample where containerid in (select containerid from "
                +" containerheader where label in ("+plate_names.toString()+"))))";
            }
            case  Constants.ITEM_TYPE_BECSEQUENCE_ID :
            {
                return "select distinct refsequenceid from assembledsequence where sequenceid in  ("+Algorithms.convertStringArrayToString(items,"," )+")";
            }
            case  Constants.ITEM_TYPE_FLEXSEQUENCE_ID :
            {
                return "select distinct refsequenceid from sequencingconstruct where constructid in "
                +" (select constructid from isolatetracking where isolatetrackingid in "
                +" (select isolatetrackingid from flexinfo where flexsequenceid in ("+Algorithms.convertStringArrayToString(items,"," )+")))";
            }
        }
        return null;
    }
    
    private int         getRefSequenceId(String sql) throws Exception
    {
        int id = -1;
        //get sequence id
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        ResultSet rs = null;
        try
        {
            rs = t.executeQuery(sql);
            if(rs.next())
            {
                id = rs.getInt("refsequenceid");
            }
            return id;
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException("Error occured while restoring sequence with id "+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
        
    }
    
    
    private ArrayList         getRefSequenceIds(String sql) 
    {
        ArrayList ids = new ArrayList();
        //get sequence id
        DatabaseTransaction t = null;
        ResultSet rs = null;
        try
        {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while(rs.next())
            {
                ids.add( new Integer( rs.getInt("refsequenceid")) );
            }
            
        } 
        catch (Exception sqlE)
        {
            m_error_messages.add("Error occured while restoring sequence with id "+sqlE+"\nSQL: "+sql);
        } 
        finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
        return ids;
    }
    
    private boolean             isPrimersDesignedForRefsequence
                            (int refsequenceid, 
                            PreparedStatement pst_check_oligo_cloning)
                            throws Exception
    {
       
        ResultSet rs = null;
        boolean result = true;
        DatabaseTransaction t =null;
        try
        {
            t = DatabaseTransaction.getInstance();
            pst_check_oligo_cloning.setInt(1, refsequenceid);
            rs = t.executeQuery(pst_check_oligo_cloning);
            if(rs.next())
            {
                return true;
            }
            else
            {
                return false;
            }
        } 
        catch (Exception sqlE)
        {
           throw new Exception("Error occured while restoring sequence with id "+refsequenceid);
        } 
        finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
        
    }
    
    
     public static void main(String args[]) 
     
    {
       // InputStream input = new InputStream();
        PrimerDesignerRunner input = null;
        User user  = null;
        try
        {
            input = new PrimerDesignerRunner();
            user = AccessManager.getInstance().getUser("htaycher1","htaycher");
            input.setItems("PGS000121-1");
            input.setItemsType( Constants.ITEM_TYPE_PLATE_LABELS);
            input.setUser(user);
            input.setSpecId(3);
            input.run();
        }
        catch(Exception e){}
     
        
       
        System.exit(0);
     }
    
}