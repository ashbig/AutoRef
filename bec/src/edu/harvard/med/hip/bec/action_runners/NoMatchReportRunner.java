/*
 * NoMatchReportRunner.java
 *
 * Created on March 4, 2004, 1:35 PM
 */

package edu.harvard.med.hip.bec.action_runners;


import java.sql.*;
import java.io.*;

import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.file.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.user.*;
import edu.harvard.med.hip.bec.Constants;
import edu.harvard.med.hip.bec.sampletracking.mapping.*;
import edu.harvard.med.hip.bec.sampletracking.objects.*;
import edu.harvard.med.hip.bec.util_objects.*;
import edu.harvard.med.hip.bec.ui_objects.*;
  import java.util.*;
  import edu.harvard.med.hip.utility.*;
/**
 *
 * @author  HTaycher
 */
public class NoMatchReportRunner extends ProcessRunner 
{
    public void run()
    {
        // ArrayList file_list = new ArrayList();
        ArrayList items = new ArrayList();
        File report = null;
        try
        {
            //convert item into array
           items = Algorithms.splitString( m_items);
           ArrayList clones = getCloneInfo(items);
           processClones(clones);
           report = printReport(clones);
           m_file_list_reports.add(report);   
        }
        catch(Exception ex)
        {
            m_error_messages.add(ex.getMessage());
        }
        finally
        {
            sendEMails();
        }
        
    }
    
    
    //------------------------------------
   
     private ArrayList getCloneInfo( ArrayList items)
    {
        ArrayList clones = new ArrayList();
        String sql = null;
        UICloneSample clone = null;
        ResultSet rs = null;
        sql = constructQueryString(items);
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next())
            {
                 clone = new UICloneSample();
                 /*
                 clone.setPlateLabel (rs.getString("LABEL"));
                 clone.setPosition (rs.getInt("POSITION"));
                 clone.setSampleType (rs.getString("SAMPLETYPE"));
                 clone.setCloneId (rs.getInt("CLONEID"));
                 clone.setCloneStatus (rs.getInt("ISOLATESTATUS"));
                 clone.setSequenceId (rs.getInt("CLONESEQUENCEID"));
                 clone.setSequenceAnalisysStatus (rs.getInt("analysisSTATUS"));
                 clone.setSequenceType (rs.getInt("SEQUENCETYPE"));
                 clone.setConstructId (rs.getInt("CONSTRUCTID"));
                 clone.setIsolateTrackingId (rs.getInt("ISOLATETRACKINGID"));
                 clone.setRank(rs.getInt("RANK"));
                 clone.setSampleId(rs.getInt("SAMPLEID"));
                 clone.setScore(rs.getInt("SCORE"));
                 clone.setRefSequenceId(rs.getInt("REFSEQUENCEID"));
                 clone.setFLEXRefSequenceId(rs.getInt("FLEXSEQUENCEID"));
                 clone.setCloneSequenceCdsStart(rs.getInt("cloneseqcdsstart"));
                 clone.setCloneSequenceCdsStop(rs.getInt("clonesequencecdsstop"));
                 if ( m_dir_name )
                    clone.setTraceFilesDirectory( getTraceFilesDirName( clone.getSampleId() ));
                  **/
                 clones.add(clone);
            }
           
            
        }
        catch(Exception e)
        {
            m_error_messages.add("Cannot get data for clone "+e.getMessage() +"\n"+sql);
        }
       return clones;
    }
    
    
    private String constructQueryString( ArrayList items)
    {
        String sql = null;
        switch (m_items_type)
        {
            case Constants.ITEM_TYPE_PLATE_LABELS://plates
            {
                StringBuffer plate_names = new StringBuffer();
                for (int index = 0; index < items.size(); index++)
                {
                    plate_names.append( "'");
                    plate_names.append((String)items.get(index));
                    plate_names.append("'");
                    if (index == 3) break;
                    if ( index != items.size()-1 ) plate_names.append(",");
                }
                return "select FLEXSEQUENCEID,LABEL, POSITION,  SAMPLETYPE, s.SAMPLEID as SAMPLEID,flexcloneid  as CLONEID,"
     +" i.STATUS as IsolateStatus,  a.SEQUENCEID as CLONESEQUENCEID, a.cdsstart as cloneseqcdsstart, a.cdsstop as clonesequencecdsstop, analysisSTATUS,  SEQUENCETYPE, "
    +"sc.refsequenceid as refsequenceid,  i.CONSTRUCTID,  i.ISOLATETRACKINGID as ISOLATETRACKINGID, RANK, "
    +" i.SCORE as SCORE   from flexinfo f,isolatetracking i, sample s, containerheader c,assembledsequence a ,"
    +" sequencingconstruct sc where f.isolatetrackingid=i.isolatetrackingid and i.sampleid=s.sampleid "
    +" and sc.constructid(+)=i.constructid and   s.containerid=c.containerid and a.isolatetrackingid(+) =i.isolatetrackingid "
    +" and s.containerid in (select containerid from containerheader where label in ("
    +plate_names.toString()+")) order by s.containerid,position";
            } 
            case Constants.ITEM_TYPE_CLONEID:
            {
                 return "select FLEXSEQUENCEID,LABEL, POSITION,  SAMPLETYPE, s.SAMPLEID as SAMPLEID,flexcloneid  as CLONEID,"
        +" i.STATUS as IsolateStatus,  a.SEQUENCEID as CLONESEQUENCEID,  a.cdsstart as cloneseqcdsstart, a.cdsstop as clonesequencecdsstop,analysisSTATUS,  SEQUENCETYPE, "
        +"sc.refsequenceid as refsequenceid,  i.CONSTRUCTID,  i.ISOLATETRACKINGID as ISOLATETRACKINGID, RANK, "
        +" i.SCORE as SCORE   from flexinfo f,isolatetracking i, sample s, containerheader c,assembledsequence a ,"
        +" sequencingconstruct sc where rownum<300 and f.isolatetrackingid=i.isolatetrackingid and i.sampleid=s.sampleid "
        +" and sc.constructid(+)=i.constructid and   s.containerid=c.containerid and a.isolatetrackingid(+) =i.isolatetrackingid "
        +"  and flexcloneid in ("+Algorithms.convertStringArrayToString(items,"," )+") ";
            }
            default : return "";
        }
       
   
    }
   
    
    private void            processClones(ArrayList clones)
    {
    }
    
    private File            printReport(ArrayList clones)
    {
        return null;
    }
}
