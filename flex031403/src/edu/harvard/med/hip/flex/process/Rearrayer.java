/*
 * Rearrayer.java
 *
 * Created on May 21, 2002, 11:28 AM
 */

package edu.harvard.med.hip.flex.process;

import java.util.*;
import edu.harvard.med.hip.flex.core.*;
/**
 *
 * @author  HTaycher
 */

/*class finds all containers that contains these sequences and
 *rearrange them in saw-tooth pattern
 */
public class Rearrayer
{
    
    private ArrayList m_Sequences = null;//FlexSequences
    private ArrayList m_Containers = null;
    
    /** Creates a new instance of Rearrayer */
    public Rearrayer()
    {}
    /** Creates a new instance of Rearrayer */
    public Rearrayer(ArrayList sequences)
    {
        m_Sequences = sequences;
    }
    
    /**
     * Return the all containers that contain  requested sequences.
     *
     * @return The ArrayList containers that contain  requested sequences.
     */
    public ArrayList getContainers()
    {
        ArrayList mgc_containers = new ArrayList();
        for (int count = 0; count < m_Containers.size(); count++)
        {
            mgc_containers.add( ((MgcContainerForRearray)m_Containers.get(count)).getContainer() );
        }
        return mgc_containers;
    }
    
    
    /**
     * Return all sequences.
     *
     * @return arrayList of all sequences.
     */
    public ArrayList getSequences()
    { return m_Sequences;}
    public int       getNumberOfRearrayedPlates(int numberOfWells)
    {
        if (numberOfWells <= 0) numberOfWells = 94;
        return (int)Math.ceil((double)m_Sequences.size() / numberOfWells);
    }
    
    public void setSequences(ArrayList seq )
    {  m_Sequences = seq;}
    
    
    
    /*Function queries db for all Mgc containers with
     *mgc clones that have these sequences
     **/
    
    public void findMgcContainers(ArrayList sequences) throws Exception
    {
        m_Sequences = sequences;
        findMgcContainers();
        checkForGlycerolStock();
    }
    
    //function finds all containers that contain samples with these sequences.
    
    private void findMgcContainers() throws Exception
    {
        int current_sequence_id = -1;
        MgcContainer mgc_container = null;
        FlexSequence fc = null;
        MgcContainerForRearray mgc_container_in_rearray = null;
        int seq_id = -1;
        
        //create hashtable of sequences , key - sequence id
        Hashtable sequence_id = new Hashtable();
        for (int count = 0; count < m_Sequences.size(); count++)
        {
            seq_id = ((FlexSequence)m_Sequences.get(count)).getId();
            sequence_id.put(new Integer(seq_id), m_Sequences.get(count));
        }
        
        //starting from first sequence
        Enumeration en = sequence_id.keys();
        while ( en.hasMoreElements() )
        {
            current_sequence_id = Integer.parseInt(     (String)en.nextElement()    ) ;
            mgc_container = MgcContainer.findMGCContainerFromSequenceID(current_sequence_id);
            mgc_container.restoreSample();
            mgc_container_in_rearray = new MgcContainerForRearray();
            mgc_container_in_rearray.setContainer(mgc_container);
            for (int count = 0; count < mgc_container.getSamples().size(); count++)
            {
                seq_id = ((MgcSample)mgc_container.getSamples().get(count)).getId();
                if (sequence_id.containsKey(new Integer(seq_id)) )//check if another sequnces belong to this container
                {
                    mgc_container_in_rearray.addSequence((FlexSequence) sequence_id.get(new Integer(seq_id))  );
                    sequence_id.remove( new Integer(seq_id ) );
                }
            }
            m_Containers.add(mgc_container_in_rearray);
        }
        
    }
    
    
    /* Fuction replaced Mgc container by glycerol stock container if
     * glycerol stock is available
     **/
    private void checkForGlycerolStock() throws Exception
    {
        MgcContainer current_mgc_container = null;
        MgcContainerForRearray current_mgc_container_for_rearray = null;
        
        for (int count = 0; count < m_Containers.size(); count++)
        {
            current_mgc_container_for_rearray = (MgcContainerForRearray)m_Containers.get(count);
            current_mgc_container = (MgcContainer) current_mgc_container_for_rearray.getContainer() ;
            if ( current_mgc_container.getGlycerolContainerid() != -1)
            {
                Container gly_container = new Container(current_mgc_container.getGlycerolContainerid()) ;
                current_mgc_container_for_rearray.setContainer(gly_container);
            }
        }
    }
    
    /*function sort containers by number of mgc clone sequences
     *that go to the experiment !!! method does not change order of sequences
     *@return arrayList of containers
     */
    public ArrayList getContainersOrderedByNumberOfSequences()
    {
        Collections.sort( m_Containers, new Comparator()
        {
            public int compare(Object cont1, Object cont2)
            {
                return ((MgcContainerForRearray)cont1).numberOfSequences() -
                ((MgcContainerForRearray)cont2).numberOfSequences();
            }
        });
        return m_Containers;
    }
    
    /*function orders sequences for experiment
     *first it orders containers, second it orders sequences in saw tooth patern
     * for plates with (numberOfWells) wells per plate
     * @param numberOfWells - how many wells per plate, default 94
     * @return list of ordered sequences
     **/
    
    public ArrayList getSequencesOrderedByContainerAndSawToothPattern(int numberOfWells)
    {
        ArrayList orderedSequences = new ArrayList();
        ArrayList temp = new ArrayList();
        int first_sequence_index = 0;
        int last_sequence_index = 0;
        if (numberOfWells <= 0) numberOfWells = 94;
        getContainersOrderedByNumberOfSequences();
        //rearange sequences acoording to container order
        for (int count = 0; count < m_Containers.size(); count++)
        {
            orderedSequences.addAll(((MgcContainerForRearray)m_Containers.get(count)).getSequences()  );
        }
        m_Sequences.clear();
        int sequencesCount = 0;
        while ( last_sequence_index  != ( orderedSequences.size() -1 )   )
        {
            //get sequences for one plate
            last_sequence_index = (last_sequence_index + numberOfWells )< (orderedSequences.size() -1 )?
            last_sequence_index + numberOfWells : orderedSequences.size() -1 ;
            
            for (int count = first_sequence_index ; count < last_sequence_index; count++)
            {
                temp.add( orderedSequences.get(count) );
            }
            //oreder by Saw-tooth , add to the returned list
            m_Sequences.add( rearangeSawToothPattern(temp));
            temp.clear();
            first_sequence_index = last_sequence_index  + 1;
        }//end of sequenceCount
        return m_Sequences;
    }
    
    
    
    public static ArrayList rearangeSawToothPattern(ArrayList sequences)
    {
        ArrayList result = new ArrayList();
        //sort array by cds length
        Collections.sort(sequences, new Comparator()
        {
            public int compare(Object o1, Object o2)
            {
                return ((FlexSequence) o1).getCdslength() - ((FlexSequence) o2).getCdslength();
            }
            /** Note: this comparator imposes orderings that are
             * inconsistent with equals. */
            public boolean equals(java.lang.Object obj)
            {      return false;  }
            // compare
        } );
        //get middle element
        int middle = (int)Math.ceil((double)sequences.size() / 2);
        for (int count = 0; count < middle; count++)
        {
            result.add(sequences.get(count));
            result.add(sequences.get(middle+count));
        }
        //ad last element 
        if (result.size() < sequences.size()) result.add(sequences.get(sequences.size() -1));
        return result;
    }
    
}
    /* inner class that holds container that will go to rearray
     * and sequences from request that belong to this container*/
class MgcContainerForRearray
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
    public Container getContainer()
    { return m_Container;}
    public ArrayList    getSequences()
    { return m_Sequences;}
    public void         setContainer(Container cont)
    {  m_Container = cont;}
    public void         setSequences(ArrayList seq)
    {  m_Sequences = seq;}
    public void         addSequence(FlexSequence fc)
    { m_Sequences.add(fc);}
    public int          numberOfSequences()
    { return m_Sequences.size();}
    public ArrayList    getNumberOfSequences(int first_sequence,
    int number_of_sequences)
    {
        ArrayList res = new ArrayList();
        for(int count = first_sequence; count < number_of_sequences + first_sequence; count++)
        {
            res.add( m_Sequences.get(count));
        }
        return res;
    }
}