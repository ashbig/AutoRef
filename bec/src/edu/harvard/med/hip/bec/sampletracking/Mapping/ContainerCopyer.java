/*
 * ContainerCopyer.java
 *
 * Created on April 2, 2003, 2:15 PM
 */

package edu.harvard.med.hip.bec.sampletracking.mapping;

import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.sampletracking.objects.*;
import edu.harvard.med.hip.bec.database.*;
import java.util.*;
/**
 *
 * @author  htaycher
 */
public class ContainerCopyer 
{
    //container label is prefix + number + sufix
    private String          m_prefix = null;
    private String          m_number = null;
    private ArrayList       m_suffixes = null;
    private String          m_sampletype = null;
    
    private ArrayList       m_sampleLineageSet = null;
    private String          m_container_type = null;
    private int             m_execution_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    
    private ArrayList       m_rearray_file_entries = null;
    private ArrayList       m_new_containers = null;
    
    /** Creates a new instance of ContainerCopyer */
    public ContainerCopyer()
    {
    }
    
    /**
     * Creates new containers based on given containers and protocol.
     *
     * @param containers Source containers for mapping.
     * @param protocol The protocol for destination containers.
     * @param project The project used for mapping.
     * @param workflow The workflow related to the mapping container.
     * @return The new containers.
     * @exception FlexDatabaseException.
     */
    public ArrayList doMapping(ArrayList org_containers, boolean mode_write_rearrayFile) throws BecDatabaseException
    {
        
        ArrayList m_new_containers = new ArrayList();
        String newBarcode = null;
         m_new_containers = new ArrayList();
         m_sampleLineageSet= new ArrayList();
         m_rearray_file_entries= new ArrayList();
     
        
        for (int ind = 0; ind < org_containers.size(); ind++)
        {
            Container container = (Container)org_containers.get(ind);
            container.restoreSample();
            for (int ind_num = 0; ind_num < m_suffixes.size(); ind_num++)
            {
                newBarcode = m_prefix +  m_number + (String)m_suffixes.get(ind_num);
                Container newContainer = new Container(BecIDGenerator.BEC_OBJECT_ID_NOTSET,
                                                      m_container_type, null, 
                                                      newBarcode, 
                                                      -1,
                                                      container.getThreadid());
                
                mappingSamples(container, newContainer,  mode_write_rearrayFile);
                m_new_containers.add(newContainer);
            }
        }
        
        return m_new_containers;
    }
    
    
    public ArrayList doMapping(Container container, String label, boolean mode_write_rearrayFile) throws BecDatabaseException
    {
        
        if (label == null || label.equals("")) throw new BecDatabaseException("Wrong container label");
         m_new_containers = new ArrayList();
         m_sampleLineageSet= new ArrayList();
         m_rearray_file_entries= new ArrayList();
     
    
        container.restoreSample();
        for (int ind_num = 0; ind_num < m_suffixes.size(); ind_num++)
        {
            
            Container newContainer = new Container(BecIDGenerator.BEC_OBJECT_ID_NOTSET,
                                                  m_container_type, null, 
                                                  label, 
                                                  -1,
                                                  container.getThreadid());

            mappingSamples(container, newContainer,  mode_write_rearrayFile);
            m_new_containers.add(newContainer);
        }

        
        return m_new_containers;
    }
    
    // Creates the new samples from the samples of the previous plate.
    //if request for robot file come create fileentries
    protected void mappingSamples(Container container, Container newContainer,
                            boolean mode_write_rearrayFile)
                            throws BecDatabaseException
    {
        ArrayList oldSamples = container.getSamples();
        String type = null;
        
       
            
        for (int ind = 0; ind < oldSamples.size(); ind++)
       {
            Sample s = (Sample)oldSamples.get(ind);
            if(Sample.CONTROL_POSITIVE.equals(s.getType()))
            {
                type = Sample.CONTROL_POSITIVE;
            } 
            else if(Sample.CONTROL_NEGATIVE.equals(s.getType()))
            {
                type = Sample.CONTROL_NEGATIVE;
            } 
            else if(Sample.EMPTY.equals(s.getType()))
            {
                type = Sample.EMPTY;
            }
            else 
            {
                type = m_sampletype;
            }
            
            Sample newSample = new Sample(BecIDGenerator.BEC_OBJECT_ID_NOTSET, type, s.getPosition(), newContainer.getId(), s.getIsolateTrackingid(), s.getOligoid());
            newContainer.addSample(newSample);
            if (mode_write_rearrayFile)
                  m_rearray_file_entries.add(new RearrayFileEntry(container.getLabel(),s.getPosition(),  newContainer.getLabel(), newSample.getPosition()));
           
            if (m_execution_id != BecIDGenerator.BEC_OBJECT_ID_NOTSET)
                 m_sampleLineageSet.add(new SampleLineage(m_execution_id, s.getId(), newSample.getId()));
            else
                m_sampleLineageSet.add(new SampleLineage(s.getId(), newSample.getId()));
        }
    }
    /**
     * Return the sample lineage.
     *
     * @return The sample lineage as a Vector.
     */
    public ArrayList getSampleLineageSet()    {return m_sampleLineageSet;    }
    public ArrayList getRearrayFileEntries(){ return m_rearray_file_entries;}
     public ArrayList getNewContainers(){ return m_new_containers;}
    
    public void setDestinationContainerWellNumber(int well_number)    { }
    public void setSampleType(String type)    {   m_sampletype = type; }
    public void setLabelPrefix(String prefix)    {  m_prefix = prefix;  }
    public void setLavelSuffics(ArrayList suffixes)    {m_suffixes=suffixes;}
    public void setLabelNumber(String num)    {  m_number = num; }
     public void setExecutionId(int v){m_execution_id = v;}
       
    public void setContainerType(String v)    {  m_container_type = v;  }    
    
    
}
