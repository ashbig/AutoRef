/*
 * MgcCOntainerForReaaray.java
 *
 * Created on June 6, 2002, 10:38 AM
 */

package edu.harvard.med.hip.flex.util;

import edu.harvard.med.hip.flex.core.*;
import java.util.*;


/*
 *
 * @author  htaycher
 * utility class that holds container that will go to rearray
 * and sequences from request that belong to this container
 */
public class MgcContainerForRearray
{
    Container m_Container = null;
    ArrayList    m_Sequences = null;
    
    public MgcContainerForRearray()
    {
        m_Sequences = new ArrayList();
    }
    public MgcContainerForRearray(Container cont, ArrayList seq)
    {
        m_Sequences = seq;
        m_Container = cont;
    }
    public Container    getContainer()    { return m_Container;}
    public ArrayList    getSequences()    { return m_Sequences;}
    public void         setContainer(Container cont)    {  m_Container = cont;}
    public void         setSequences(ArrayList seq)    {  m_Sequences = seq;}
    public void         addSequence(FlexSequence fc)    { m_Sequences.add(fc);}
    public int          numberOfSequences()    { return m_Sequences.size();}
    
    public String       getMarker()
    {
        if (m_Container instanceof MgcContainer) 
            return ((MgcContainer)m_Container).getMarker();
        else
            return null;
    }
    
    public ArrayList    getSequences(int first_sequence,   int number_of_sequences)
    {
        ArrayList res = new ArrayList();
        for(int count = first_sequence; count < number_of_sequences + first_sequence; count++)
        {
            res.add( m_Sequences.get(count));
        }
        return res;
    }

    
}
