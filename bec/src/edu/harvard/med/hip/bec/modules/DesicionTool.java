/*
 * DesicionTool.java
 *
 * Created on September 10, 2003, 12:02 PM
 */

package edu.harvard.med.hip.bec.modules;

import java.util.*;

import java.sql.*;
import java.io.*;
import edu.harvard.med.hip.bec.coreobjects.feature.*;

import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.modules.*;
import edu.harvard.med.hip.bec.action_runners.*;
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.coreobjects.endreads.*;
import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.coreobjects.oligo.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.form.*;
import edu.harvard.med.hip.bec.user.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.Constants;
import edu.harvard.med.hip.bec.sampletracking.mapping.*;
import edu.harvard.med.hip.bec.sampletracking.objects.*;
import edu.harvard.med.hip.bec.ui_objects.*;
/**
 *
 * @author  HTaycher
 */
public class DesicionTool
{
    private String              m_plate_label = null;
    private ArrayList           m_clone_ids = null;
    private ArrayList           m_clones = null;
    private FullSeqSpec         m_spec = null;
    /** Creates a new instance of DesicionTool */
    public DesicionTool()
    {
        m_clones = new ArrayList();
    }
    
    public void setPlateName(String plate_name)   { m_plate_label = plate_name.toUpperCase();}
    public void setCLoneIds(ArrayList clone_ids){m_clone_ids = clone_ids; }
    public void setSpecId(int bioeval_spec_id)throws BecDatabaseException{m_spec = (FullSeqSpec)Spec.getSpecById(bioeval_spec_id);}
    public  ArrayList getCloneData()    {        return m_clones;}
    
    public void run() throws BecDatabaseException
    {
        if (m_plate_label == null && m_clone_ids == null) return;
        if (m_plate_label != null) runContainer();
        if (m_clone_ids != null) runCloneList();
    }
    
    
    //*********************    private ********************
    private void runContainer()throws BecDatabaseException
    {
        //get sample data
        Container container =   Container.findContainerDescriptionFromLabel(m_plate_label);
         int[] sequence_analysis_status = {
                BaseSequence.CLONE_SEQUENCE_STATUS_ASSEMBLED ,
                BaseSequence.CLONE_SEQUENCE_STATUS_ANALIZED_YES_DISCREPANCIES,
                BaseSequence.CLONE_SEQUENCE_STATUS_ANALIZED_NO_DISCREPANCIES ,
                BaseSequence.CLONE_SEQUENCE_STATUS_NOMATCH ,
                BaseSequence.CLONE_SEQUENCE_STATUS_POLYMORPHISM_CLEARED ,
                BaseSequence.CLONE_SEQUENCE_STATUS_ANALYSIS_CONFIRMED };
       m_clones = container.restoreUISamples(container);
        //fill in clone info
        UICloneSample clone = null; 
        for (int clone_count = 0; clone_count < m_clones.size(); clone_count++)
        {
            clone = (UICloneSample) m_clones.get(clone_count);
            
//get info for the most relevant sequence 
            CloneSequence clone_sequence = UICloneSample.setCloneSequence(clone, sequence_analysis_status);
            if ( clone_sequence != null)clone.setCloneQuality( defineQuality( clone_sequence) );
        }
    }
    /*
    private CloneSequence setCloneSequence(UICloneSample clone_sample) throws BecDatabaseException
    {
         int[] sequence_analysis_status = {
                BaseSequence.CLONE_SEQUENCE_STATUS_ASSEMBLED ,
                BaseSequence.CLONE_SEQUENCE_STATUS_ANALIZED_YES_DISCREPANCIES,
                BaseSequence.CLONE_SEQUENCE_STATUS_ANALIZED_NO_DISCREPANCIES ,
                BaseSequence.CLONE_SEQUENCE_STATUS_NOMATCH ,
                BaseSequence.CLONE_SEQUENCE_STATUS_POLYMORPHISM_CLEARED ,
                BaseSequence.CLONE_SEQUENCE_STATUS_ANALYSIS_CONFIRMED };
            String clone_sequence_analysis_status = Algorithms.convertArrayToString(sequence_analysis_status, ",");
            int[] sequence_type = {BaseSequence.CLONE_SEQUENCE_TYPE_ASSEMBLED, BaseSequence.CLONE_SEQUENCE_TYPE_FINAL  };
            String clone_sequence_type = Algorithms.convertArrayToString(sequence_type, ",");
            CloneSequence clseq = CloneSequence.getOneByIsolateTrackingId( clone_sample.getIsolateTrackingId(), clone_sequence_analysis_status,clone_sequence_type);
            if (clseq != null)
            {
                clone_sample.setSequenceId (clseq.getId()); 
                clone_sample.setSequenceAnalisysStatus (clseq.getCloneSequenceStatus()); 
                clone_sample.setSequenceType (clseq.getCloneSequenceType()); 
            }
            return clseq;
    }
    
    */
    private int defineQuality( CloneSequence clone_sequence)throws BecDatabaseException
    {
        if ( clone_sequence.getDiscrepancies() == null || clone_sequence.getDiscrepancies().size() == 0)
            return BaseSequence.QUALITY_GOOD;
        ArrayList discrepancy_descriptions = DiscrepancyDescription.assembleDiscrepancyDefinitions( clone_sequence.getDiscrepancies());
    
        return  DiscrepancyDescription.defineQuality( discrepancy_descriptions ,m_spec );
    }
    
    private void runCloneList()
    {
    }
    
    
    
    //****************
     public static void main(String [] args) 
    {
        try
        {
               ArrayList spec_collection =  FullSeqSpec.getAllSpecNames() ;
                DesicionTool ds = new DesicionTool();
                ds.setSpecId(12);
                ds.setPlateName("YGS000361-1");
               
    
                ds.run();
                ArrayList clone_data = ds.getCloneData();
       
 //     System.out.println(clone_data.size());
        }
        catch(Exception e)
        {
        System.out.println(e.getMessage());
        }
        System.exit(0);
    }
}
