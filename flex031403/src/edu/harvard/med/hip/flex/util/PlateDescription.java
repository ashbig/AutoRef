/*
 * PlateDescription.java
 *
 * Created on June 20, 2002, 1:43 PM
 */

package edu.harvard.med.hip.flex.util;


import java.util.*;
/**
 *
 * @author  htaycher
 */
public class PlateDescription
{
    
    private LinkedList m_Sequences = null;
    private ArrayList m_Sequence_descriptions = null;
    private ArrayList m_container_descriptions = null;
    private boolean m_isReady = true;
    
    /** Creates a new instance of PlateDescription */
    public PlateDescription(LinkedList sequences, ArrayList sequence_descriptions                       )
    {
        m_Sequences = sequences;
        m_Sequence_descriptions = sequence_descriptions;
                m_container_descriptions = new ArrayList();
        for (int count = 0; count < sequence_descriptions.size(); count++ )
        {
            ContainerDescription cur_container = ((SequenceDescription) sequence_descriptions.get(count)).getContainerDescription();
            if (! m_container_descriptions.contains( cur_container)  )
            {
                m_container_descriptions.add(cur_container);
                if ( m_isReady )      m_isReady = cur_container.getStatus() ;
            }
        }
        
        
    }
    
    public PlateDescription(LinkedList sequences, ArrayList sequence_descriptions,
     boolean flag)
    {
        this( sequences, sequence_descriptions);
        m_isReady = flag;
    }
    
    public LinkedList           getSequences()    {  return m_Sequences  ;}
    public ArrayList            getSequenceDescriptions()    { return m_Sequence_descriptions;   }
    public boolean              getStatus()    {   return m_isReady ;}
    public int                  getNumberOfSequences()    {return m_Sequences.size()  ;}
    public String               getMarker()
    {
        SequenceDescription first =  (SequenceDescription)m_Sequence_descriptions.get(0);
        return first.getContainerDescription().getMarker()  ;
    }
    public ArrayList            getContainers()    { return m_container_descriptions;}
}
