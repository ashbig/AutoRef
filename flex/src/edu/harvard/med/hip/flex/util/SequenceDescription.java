/*
 * SequenceDescription.java
 * used for sequence rearray 
 * contains description of sequence, its position on plate and plate it belongs to
 *
 * Created on June 7, 2002, 10:17 AM
 */

package edu.harvard.med.hip.flex.util;

/**
 *
 * @author  HTaycher
 */
public class SequenceDescription
{
    
   
    private int             m_seq_id = -1;
    private int             m_cds_length = -1;
    
    //sample parameters
    private int             m_image_id = -1;
    private int             m_org_position = -1;
    //initial MGC container
    private ContainerDescription          m_org_container = null;
    
    
    public SequenceDescription(int id, int pos, ContainerDescription cont, int imageid)
    {
        m_seq_id = id;
        m_org_position = pos;
        m_image_id = imageid;
        m_org_container = cont;
    }
    
    public void                         setCdsLength(int l){ m_cds_length = l;}
    
    public int                          getId(){ return m_seq_id;}
    public int                          getPosition(){ return m_org_position;}
    public ContainerDescription         getContainerDescription(){ return m_org_container;}
    public int                          getImageId(){ return m_image_id;}
    public int                          getCdsLength(){ return m_cds_length;}
    public int                          getContainerId(){ return m_org_container.getId();}
    

}
