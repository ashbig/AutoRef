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
   
    /** Creates a new instance of PolymorphismFinderRunner */
    public void         setSpecId(int v){m_spec_id = v;}
    public void run()
    {
         int id = -1; int process_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
         Connection conn = null;
         OligoCalculation oligo_calculation = null;
         String sql = "";
         Statement stmt = null;
         PreparedStatement pst_check_oligo_cloning = null;
          PreparedStatement pst_insert_process_object = null;
        try
        {
            conn = DatabaseTransaction.getInstance().requestConnection();
            pst_insert_process_object = conn.prepareStatement("insert into process_object (processid,objectid,objecttype) values(?,?,Constants.PROCESS_OBJECT_TYPE_REFSEQUENCE)");
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
            try
            {

                for (int index =  0;  index < ids.size(); index++)
                {
                    id = ((Integer) ids.get(index)).intValue();
                    if (isPrimersDesignedForRefsequence(id, pst_check_oligo_cloning) )
                    {
                        m_error_messages.add("Primers have been designed for refsequence with id "+id +" and this spec");
                        continue;
                    }
                //get reference sequence to process
                    BaseSequence refsequence = getRefsequence(id);
                //run primer3 with specified spec
                    Primer3Wrapper primer3 = new Primer3Wrapper(m_spec,  refsequence);
                    ArrayList oligo_calculations = primer3.run();
                    oligo_calculation = (OligoCalculation)oligo_calculations.get(0);
                    oligo_calculation.insert(conn);
                    //insert process_object
                    pst_insert_process_object.setInt(1,process_id);
                    pst_insert_process_object.setInt(2, id);
                    DatabaseTransaction.executeUpdate(pst_insert_process_object);
                    conn.commit();
                }
            }
            catch(Exception e)
            {
                m_error_messages.add(e.getMessage());
            }
        }
        catch(Exception e)
        {
            m_error_messages.add(e.getMessage());
        }
        finally
        {
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
            return true;
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
        String sql = null;
        ArrayList items = Algorithms.splitString( m_items);
        switch ( m_items_type)
        {
            case  Constants.ITEM_TYPE_CLONEID :
            {
                sql="select refsequenceid from sequencingconstruct where constructid in "
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
                sql="select refsequenceid from sequencingconstruct where constructid in "
                +"  (select constructid from isolatetracking where sampleid in "
                +"  (select sampleid from sample where containerid in (select containerid from "
                +" containerheader where label in ("+plate_names.toString()+"))))";
            }
            case  Constants.ITEM_TYPE_BECSEQUENCE_ID :
            {
                sql="select refsequenceid from assembledsequence where sequenceid in  ("+Algorithms.convertStringArrayToString(items,"," )+")";
            }
            case  Constants.ITEM_TYPE_FLEXSEQUENCE_ID :
            {
                sql="select refsequenceid from sequencingconstruct where constructid in "
                +" (select constructid from isolatetracking where isolatetrackingid in "
                +" (select isolatetrackingid from flexinfo where flexsequenceid in ("+Algorithms.convertStringArrayToString(items,"," )+")))";
            }
        }
        return sql;
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
                id = rs.getInt("sequenceid");
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
                ids.add( new Integer( rs.getInt("sequenceid")) );
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
    
    private boolean             isPrimersDesignedForRefsequence(int refsequenceid, PreparedStatement pst_check_oligo_cloning)
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
                result= true;
            }
            else
            {
                result= false;
            }
           
        } catch (Exception sqlE)
        {
            m_error_messages.add("Error occured while restoring sequence with id "+refsequenceid);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
         return result;
    }
    
    
    
    
}
