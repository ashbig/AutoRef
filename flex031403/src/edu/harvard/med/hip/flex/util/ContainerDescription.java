/*
 * ContainerDescription.java
 * used for sequence rearray (MGC project)
 * contain description of container from rearray point of view:
 * library name, label & how many sequences from original MGC container go to experiment
 * Created on June 10, 2002, 12:34 PM
 */

package edu.harvard.med.hip.flex.util;

/**
 *
 * @author  HTaycher
 */
public class ContainerDescription
{
    
    private String              m_label = null;
    private String              m_marker = null;
    private int                 m_id = -1;
    private int                 m_glycerol_id = -1;
    private int                 m_culture_id = -1;
    private int                 m_number_of_sequences = -1;
    private boolean             m_isReady = false;
    
    /** Creates a new instance of ContainerDescription */
    public ContainerDescription(String l, String m, int id, int glycerol_id, int culture_id)
    {
        m_label = l;
        m_marker = m;
        m_id = id;
        m_glycerol_id = glycerol_id;
        m_culture_id = culture_id;
    }
    public ContainerDescription(String l, String m, int id, int glycerol_id,  int culture_id, int n)
    {
       this(l,m,id, glycerol_id,  culture_id);
       m_number_of_sequences = n;
    }
    
    public String toString()
    {
        return "Container description " + m_id + ";" + m_label + ";" + m_number_of_sequences;
    }
    
    public String           getLabel()    { return m_label;   }
    
    public int              getId()    {    return m_id;}
    public int              getNumberOfSequences()    { return m_number_of_sequences;   }
    public String           getMarker(){ return m_marker;}
    public boolean          getStatus(){ return m_isReady;}
    public int              getGlycerolId(){ return m_glycerol_id;}
    public int              getCultureId(){ return m_culture_id;}
    public String           getCultureLabel()  {  return "MLI"+m_label.substring(3);   }
    
    public void             setNumberOfSequences(int n  )    {m_number_of_sequences = n;    }
    //status shows if culture block for this mgc container exists on queue
    public void             setStatus(boolean s){ m_isReady = s;}
    
}
