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
        ArrayList clones = new ArrayList();
        String  report_file_name =    null;
        ArrayList sql_groups_of_items = new ArrayList();
        m_clones = new ArrayList();
        File report = null;
        try
        {
            report_file_name =    Constants.getTemporaryFilesPath() + "DecisionToolReport"+ System.currentTimeMillis()+ ".txt";
            m_spec = (FullSeqSpec)Spec.getSpecById(m_spec_id);
           sql_groups_of_items =  prepareItemsListForSQL();
           for (int count = 0; count < sql_groups_of_items.size(); count++)
           {
               clones = UICloneSample.getCloneInfo( (String) sql_groups_of_items.get(count), m_items_type,true,  false);
                processClones(clones);
                 if ( clones != null && clones.size() > 0 )
                 {
                    printReport(report_file_name, clones, count);
                    m_clones.addAll(clones);
                 }
           }
           
           m_file_list_reports.add(new File(report_file_name));   
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
   
    
    
    private void            processClones(ArrayList clones)
    {
         UICloneSample clone = null; CloneSequence clone_sequence =  null;
         int clone_quality = BaseSequence.QUALITY_NOT_DEFINED;
         ArrayList discrepancy_descriptions = null;ArrayList contig_discrepancies = null;
         for (int clone_count=0; clone_count < clones.size(); clone_count++)
         {
                discrepancy_descriptions = new ArrayList();
                clone_quality = BaseSequence.QUALITY_NOT_DEFINED;
                 clone = (UICloneSample)clones.get(clone_count);
                 try
                 {
                    if ( clone.getSequenceId() > 1 ) //sequence exsists
                    {
                        clone_sequence = new CloneSequence( clone.getSequenceId() );
                        if ( clone_sequence != null && !(clone_sequence.getStatus() == BaseSequence.CLONE_SEQUENCE_STATUS_ASSEMBLED 
                            ||clone_sequence.getStatus() == BaseSequence.CLONE_SEQUENCE_STATUS_NOMATCH         ) )
                        {
                            if ( clone_sequence.getDiscrepancies() == null || clone_sequence.getDiscrepancies().size() == 0)
                                clone_quality = BaseSequence.QUALITY_GOOD;
                            else
                            {
                                discrepancy_descriptions = DiscrepancyDescription.assembleDiscrepancyDefinitions( clone_sequence.getDiscrepancies());
                                clone_quality =  DiscrepancyDescription.defineQuality( discrepancy_descriptions ,m_spec );
                                
                             }
                        }
                    }
                    else
                    {
                        StretchCollection str_coll = StretchCollection.getLastByCloneId( clone.getCloneId() );
                        if (str_coll != null && str_coll.getStretches() != null && str_coll.getStretches().size() > 0 )
                        {
                             Stretch contig = null;
                              for (int contig_count = 0; contig_count < str_coll.getStretches().size(); contig_count++)
                             {
                                   contig  = (Stretch)str_coll.getStretches().get(contig_count);
                                   if ( contig.getType() == Stretch.GAP_TYPE_CONTIG)
                                   {
                                        if ( contig.getStatus()== BaseSequence.CLONE_SEQUENCE_STATUS_ASSEMBLED )
                                        {
                                            clone.setSequenceAnalisysStatus (BaseSequence.CLONE_SEQUENCE_STATUS_ASSEMBLED );
                                            break;
                                        }
                                        contig_discrepancies = DiscrepancyDescription.assembleDiscrepancyDefinitions( contig.getSequence().getDiscrepancies());
                                        if ( contig_discrepancies != null && contig_discrepancies.size() > 0)
                                            discrepancy_descriptions.addAll(contig_discrepancies );
                                   }
                              }
                              clone_quality =  DiscrepancyDescription.defineQuality( discrepancy_descriptions ,m_spec );
                        
                              if ( discrepancy_descriptions != null && discrepancy_descriptions.size() > 0 )
                                        clone.setSequenceAnalisysStatus (BaseSequence.CLONE_SEQUENCE_STATUS_ANALIZED_YES_DISCREPANCIES);
                              else
                                       clone.setSequenceAnalisysStatus (BaseSequence.CLONE_SEQUENCE_STATUS_ANALIZED_NO_DISCREPANCIES);
                        }
                        else//reads only
                         {
                             ArrayList reads = Read.getReadByIsolateTrackingId(clone.getIsolateTrackingId());
                               
                             if (reads != null && reads.size() > 0)
                             {
                                 if ( reads.size() > 1 )
                                 {
                                      discrepancy_descriptions = DiscrepancyDescription.getDiscrepancyDescriptionsNoDuplicates(
                                         ((Read) reads.get(0)).getSequence().getDiscrepancies(),
                                          ((Read) reads.get(1)).getSequence().getDiscrepancies());
                                     
                                 }
                                 else
                                    discrepancy_descriptions = DiscrepancyDescription.assembleDiscrepancyDefinitions(
                                         ((Read) reads.get(0)).getSequence().getDiscrepancies() );
                                  if (discrepancy_descriptions == null ) 
                                        clone_quality = BaseSequence.QUALITY_GOOD;
                                  else
                                    clone_quality =  DiscrepancyDescription.defineQuality( discrepancy_descriptions ,m_spec );
                         
                             }
                             else
                                 clone_quality = BaseSequence.QUALITY_CANNOT_PROCESS_NO_DATA;
                             }
                        }
                 
                       clone.setCloneQuality( clone_quality );
                }
                catch(Exception e)
                {
                    clone.setCloneQuality(BaseSequence.QUALITY_CANNOT_PROCESS);
                    m_error_messages.add("Cannot process clone "+clone.getCloneId()+"\n"+e.getMessage());
                }
         
         }
    }
    
    
    
    private void            printReport(String report_file_name, ArrayList clones, int count)
    {
        String title = " Clone Id\tPlate Label\tPosition\tSample Type\tClone Sequence Id\tClone Sequence Status\tQuality\n";
        FileWriter in = null; 
        try
        {
            in =  new FileWriter(report_file_name, true);
            if(count == 0) in.write(title);
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
    //    clone_info.append( IsolateTrackingEngine.getStatusAsString(  clone.getCloneStatus()) +"\t");
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
         runner.setUser( AccessManager.getInstance().getUser("htaycher123","htaycher"));
         runner.setInputData(Constants.ITEM_TYPE_CLONEID, " 140346 139858	 ");
        // runner.setItems("582 ");
       //  runner.setItemsType( Constants.ITEM_TYPE_CLONEID);
                    runner.setSpecId(20);
                      runner.run();
                
       
    }catch(Exception e){}
    System.exit(0);
    }
    
  
    
}
