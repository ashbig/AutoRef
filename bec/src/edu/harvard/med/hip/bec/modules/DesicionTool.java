/*
 * DesicionTool.java
 *
 * Created on September 10, 2003, 12:02 PM
 */

package edu.harvard.med.hip.bec.modules;

import java.util.*;

import java.sql.*;
import java.io.*;


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
    
    public void setPlateName(String plate_name)   { m_plate_label = plate_name;}
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
        Container container =  container = Container.findContainerDescriptionFromLabel(m_plate_label);
        
        container.restoreSampleIsolate(false,false);
        //fill in clone info
        CloneSample clone = null; Sample sample = null;
        for (int clone_count = 0; clone_count < container.getSamples().size(); clone_count++)
        {
            sample = (Sample) container.getSamples().get(clone_count);
            clone = new CloneSample();
            clone.setPlateLabel (container.getLabel() ); 
            clone.setPosition (sample.getPosition()); 
            clone.setSampleType (sample.getType()); 
            clone.setCloneId ( sample.getIsolateTrackingEngine().getFlexInfo().getFlexCloneId()); 
            clone.setCloneStatus (sample.getIsolateTrackingEngine().getStatus() ); 
            clone.setConstructId (sample.getIsolateTrackingEngine().getConstructId()); 
//get info for the most relevant sequence 
            CloneSequence clseq = CloneSequence.getByIsolateTrackingId(sample.getIsolateTrackingEngine().getId(), BaseSequence.CLONE_SEQUENCE_STATUS_ASSEMBLED);
            clone.setSequenceId (clseq.getId()); 
            clone.setSequenceAnalisysStatus (clseq.getCloneSequenceStatus()); 
            clone.setSequenceType (clseq.getCloneSequenceType()); 
            
            m_clones.add(clone);
        }
    }
    
    
    private void runCloneList()
    {
    }
}
