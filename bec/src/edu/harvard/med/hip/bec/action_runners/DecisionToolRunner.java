/*
 * DecisionTool.java
 *
 * Created on March 11, 2004, 4:30 PM
 */

package edu.harvard.med.hip.bec.action_runners;



import java.sql.*;
import java.io.*;

import edu.harvard.med.hip.bec.coreobjects.endreads.*;
import edu.harvard.med.hip.bec.coreobjects.feature.*;
import  edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.file.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.user.*;
import edu.harvard.med.hip.bec.Constants;

import edu.harvard.med.hip.bec.ui_objects.*;
  import java.util.*;
  import edu.harvard.med.hip.utility.*;
/**
 *
 * @author  HTaycher
 */
  public class DecisionToolRunner extends ProcessRunner 
{
  
    private ArrayList           m_clones = null;
    private FullSeqSpec         m_spec = null;
    private int                 m_spec_id = -1;
      
    public void                 setSpecId(int v){ m_spec_id = v;}
    public ArrayList            getClones(){ return m_clones;}
    public String               getTitle()    {return "Request for Desicion tool run";    }
    
    public void run()
    {
        // ArrayList file_list = new ArrayList();
        ArrayList items = new ArrayList();
        File report = null;
        try
        {
            m_spec = (FullSeqSpec)Spec.getSpecById(m_spec_id);
           items = Algorithms.splitString( m_items);
           ArrayList clones = getCloneInfo(items);
           processClones(clones);
           m_clones = clones;
           report = printReport(clones);
           m_file_list_reports.add(report);   
        }
        catch(Exception ex)
        {
            m_error_messages.add(ex.getMessage());
        }
        finally
        {
            sendEMails( getTitle() );
        }
        
    }
    
    
    //------------------------------------
   
     private ArrayList getCloneInfo( ArrayList items) throws BecDatabaseException
     {
        ArrayList clones = new ArrayList();String additional_id = null;
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
                 clone.setCloneId (rs.getInt("CLONEID"));
                
                 clone.setSampleType(rs.getString("SAMPLETYPE"));
                  if (clone.getSampleType().indexOf("CONTROL") != -1 )
                      clone.setSampleType("CONTROL");
                 clone.setSequenceId (rs.getInt("CLONESEQUENCEID"));
                 clone.setConstructId (rs.getInt("CONSTRUCTID"));
                 clone.setPlateLabel (rs.getString("LABEL"));
                 clone.setPosition (rs.getInt("POSITION"));
                 clone.setIsolateTrackingId (rs.getInt("ISOLATETRACKINGID"));
                clone.setCloneStatus(rs.getInt("CLONESTATUS"));
                 clone.setSequenceAnalisysStatus(rs.getInt("analysisstatus"));
                 clones.add(clone);
            }
            return clones;
            
        }
        catch(Exception e)
        {
          throw new BecDatabaseException("Cannot get data for clone "+e.getMessage() +"\n"+sql);
        }
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
                return "select LABEL, POSITION, flexcloneid  as CLONEID,sampletype,analysisstatus,i.status as clonestatus, "
   +"  a.SEQUENCEID as CLONESEQUENCEID,  i.CONSTRUCTID,  i.ISOLATETRACKINGID as ISOLATETRACKINGID  "
    +" from flexinfo f,isolatetracking i, sample s, containerheader c,assembledsequence a , "
     +" sequencingconstruct sc where f.isolatetrackingid=i.isolatetrackingid and i.sampleid=s.sampleid "
     +" and sc.constructid(+)=i.constructid and   s.containerid=c.containerid and a.isolatetrackingid(+) =i.isolatetrackingid "
      +" and s.containerid in (select containerid from containerheader where label in ("
   + plate_names.toString()+")) order by s.containerid,position";
 
            } 
            case Constants.ITEM_TYPE_CLONEID:
            {
                 return "select LABEL, POSITION, flexcloneid  as CLONEID,sampletype,analysisstatus,i.status as clonestatus,  "
   +"  a.SEQUENCEID as CLONESEQUENCEID,  i.CONSTRUCTID,  i.ISOLATETRACKINGID as ISOLATETRACKINGID  "
    +" from flexinfo f,isolatetracking i, sample s, containerheader c,assembledsequence a , "
     +" sequencingconstruct sc where f.isolatetrackingid=i.isolatetrackingid and i.sampleid=s.sampleid "
     +" and sc.constructid(+)=i.constructid and   s.containerid=c.containerid and a.isolatetrackingid(+) =i.isolatetrackingid "
      +" and flexcloneid in ("+Algorithms.convertStringArrayToString(items,"," )+") ";
            }
            default : return "";
        }
       
   
    }
   
    
    private void            processClones(ArrayList clones)
    {
         UICloneSample clone = null; CloneSequence clone_sequence =  null;
         int clone_quality = -1;
         for (int clone_count=0; clone_count < clones.size(); clone_count++)
         {
                 clone = (UICloneSample)clones.get(clone_count);
                 if ( clone.getSequenceId() == 0 ) continue;
                 try
                {
                    clone_sequence = new CloneSequence( clone.getSequenceId() );
                    if ( clone_sequence != null && !(clone_sequence.getStatus() == BaseSequence.CLONE_SEQUENCE_STATUS_ASSEMBLED 
                            ||clone_sequence.getStatus() == BaseSequence.CLONE_SEQUENCE_STATUS_NOMATCH         ) )
                    {
                        if ( clone_sequence.getDiscrepancies() == null || clone_sequence.getDiscrepancies().size() == 0)
                            clone_quality = BaseSequence.QUALITY_GOOD;
                        else
                        {
                            ArrayList discrepancy_descriptions = DiscrepancyDescription.assembleDiscrepancyDefinitions( clone_sequence.getDiscrepancies());
                            clone_quality =  DiscrepancyDescription.defineQuality( discrepancy_descriptions ,m_spec );
                        }
                        clone.setCloneQuality( clone_quality );
                    }
    
                 }
                catch(Exception e)
                {
                    m_error_messages.add("Cannot process clone "+clone.getCloneId()+"\n"+e.getMessage());
                }
                
         }
    }
    
    
    
    private File            printReport(ArrayList clones)
    {
        String title = " Clone Id\tPlate Label\tPosition\tSample Type\tClone Status\tClone Sequence Id\tClone Sequence Status\tQuality\n";
        FileWriter in = null; 
        File report = new File(Constants.getTemporaryFilesPath() + "DTReport"+System.currentTimeMillis()+".txt");
        try
        {
            in = new FileWriter(report);
            in.write(title);
            for (int clone_count =0; clone_count<clones.size(); clone_count++)
            {
                  in.write( getCloneEntry( (UICloneSample)clones.get(clone_count)  ));
            }
            in.flush();
            in.close();
            
        }
        catch(Exception e)
        {
            m_error_messages.add("Cannot create report file.");
        }
        return report;
    }
    
    private String getCloneEntry(UICloneSample clone)
    {
         /*Plate Position Sample 
          Type Clone Id 
          Clone Status 
          equence ID S
          equence Status 
           Quality */
        StringBuffer clone_info = new StringBuffer();
        clone_info.append( clone.getCloneId ()+"\t");
        clone_info.append( clone.getPlateLabel ()+"\t");
        clone_info.append( clone.getPosition ()+"\t");
        clone_info.append( clone.getSampleType() + "\t");
        clone_info.append( IsolateTrackingEngine.getStatusAsString(  clone.getCloneStatus()) +"\t");
        clone_info.append(  clone.getSequenceId()+"\t");
        clone_info.append( BaseSequence.getSequenceAnalyzedStatusAsString(clone.getSequenceAnalisysStatus ())+"\t");
         clone_info.append(BaseSequence.getSequenceQualityAsString(clone.getCloneQuality())+"\t");
                    
        return clone_info.toString()+"\n";
    }
    
    
   
    
    public static void main(String args[]) 
{
    DecisionToolRunner runner = new DecisionToolRunner();
      
    try
    {
         runner.setUser( AccessManager.getInstance().getUser("htaycher345","htaycher"));
         runner.setItems("582 ");
         runner.setItemsType( Constants.ITEM_TYPE_CLONEID);
                    runner.setSpecId(4);
                      runner.run();
                    ArrayList clone_data = runner.getClones();
           
       
    }catch(Exception e){}
    System.exit(0);
    }
    
  
    
}
