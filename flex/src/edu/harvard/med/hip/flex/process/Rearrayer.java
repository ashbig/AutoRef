/*
 * Rearrayer.java
 *
 * Created on May 21, 2002, 11:28 AM
 */

package edu.harvard.med.hip.flex.process;

import java.util.*;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.util.*;
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
    private Hashtable m_sequence_container = null;//contains MgcContainers keyded by sequenceid
    //for fast lookup what container this sequence belong to
    
  
    /** Creates a new instance of Rearrayer */
    public Rearrayer(ArrayList sequences)
    {
        m_Sequences = sequences ;
        m_Containers = new ArrayList();
        m_sequence_container = new Hashtable();
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
    public ArrayList                        getSequences()
    { return m_Sequences;}
    public int                              getNumberOfRearrayedPlates(int numberOfWells)
    {
        if (numberOfWells <= 0) numberOfWells = 94;
        return (int)Math.ceil((double)m_Sequences.size() / numberOfWells);
    }
    
    public void                             setSequences(ArrayList seq )
    {  m_Sequences = seq;}
    
    public MgcContainer                     getContainerBySequenceId(int seq_id)
    {
        return (MgcContainer)m_sequence_container.get(new Integer(seq_id));
    }
    
    public MgcContainer                     getContainerBySequenceId(FlexSequence seq)
    {
        return (MgcContainer)m_sequence_container.get(new Integer(seq.getId()));
    }
    
    /*Function queries db for all Mgc containers with
     *mgc clones that have these sequences
     **/
    
    public void                             findMgcContainers(ArrayList sequences) throws Exception
    {
        m_Sequences = sequences;
        findMgcContainers();
     }
    
    public void findMgcContainers() throws Exception
    {
        if ( m_Sequences == null || m_Sequences.size() == 0) return;
        findMgcContainersFromDB();
        checkForGlycerolStock();
    }
    
    public void findOriginalMgcContainers() throws Exception
    {
        if ( m_Sequences == null || m_Sequences.size() == 0) return;
        findMgcContainersFromDB();
       
    }
    
    //function finds all containers that contain samples with these sequences.
    //function always gets the last inserted container that containes this sequence
    private void findMgcContainersFromDB() throws Exception
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
        ArrayList temp = new ArrayList(m_Sequences);
        
        //starting from first sequence
        
       while (! temp.isEmpty() )
        {
            current_sequence_id = ((FlexSequence)temp.get(0)).getId() ;
            mgc_container = MgcContainer.findMGCContainerFromSequenceID(current_sequence_id);
            mgc_container.restoreSample();
            mgc_container_in_rearray = new MgcContainerForRearray();
            mgc_container_in_rearray.setContainer(mgc_container);
            //check if any other sequence in request come from the same container
            for (int count = 0; count < mgc_container.getSamples().size(); count++)
            {
                MgcSample ms = (MgcSample)mgc_container.getSamples().get(count);
                Integer seq_key = new Integer(ms.getSequenceId());
                if (sequence_id.containsKey(seq_key ) )
                {
                    FlexSequence fs = (FlexSequence)sequence_id.get(seq_key);
                    mgc_container_in_rearray.addSequence( fs  );
                    temp.remove(fs);
                    if (temp.isEmpty()) continue;
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
            if ( current_mgc_container.getGlycerolContainerid() > 0)
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
    
    //not debuged yet
    public ArrayList getContainersOrderedByMarkerNumberOfSequences()
    {
        Collections.sort( m_Containers, new Comparator()
        {
            public int compare(Object cont1, Object cont2)
            {
                
                MgcContainerForRearray mrc1 =(MgcContainerForRearray) cont1;
                MgcContainerForRearray mrc2 = (MgcContainerForRearray) cont2;
                Container mc1 = mrc1.getContainer();
                Container mc2 = mrc2.getContainer();
                if ( !(mc1 instanceof MgcContainer) || !(mc2 instanceof MgcContainer)) return 0;
                
                int res = (((MgcContainer)mc1).getMarker().compareToIgnoreCase(((MgcContainer)mc2).getMarker())) ;
                if (res == 0)//same marker
                    return    -(mrc1.numberOfSequences() - mrc2.numberOfSequences());
                else
                        return res;
                
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
    //not debuged yet
    public ArrayList getSequencesOrderedByMarkerContainerSTP(int numberOfWells)
    {
        ArrayList orderedSequences = new ArrayList();
        ArrayList temp = new ArrayList();
        int first_sequence_index = 0;
        int last_sequence_index = 0;
        if (numberOfWells <= 0) numberOfWells = 94;
        getContainersOrderedByMarkerNumberOfSequences();
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
            m_Sequences.add( Algorithms.rearangeSawToothPatternInFlexSequence(temp));
            temp.clear();
            first_sequence_index = last_sequence_index  + 1;
        }//end of sequenceCount
        return m_Sequences;
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
    
    public String       getMarker()
    {
        if (m_Container instanceof MgcContainer) 
            return ((MgcContainer)m_Container).getMarker();
        else
            return null;
    }
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


//********************************testing**********************************
 
  public static void main(String args[])
    {
        try
        {
            Request m_Request = new Request(108);
          
             Rearrayer re = new Rearrayer( new ArrayList(m_Request.getSequences()) );
             //   ArrayList mgc_containers = null;
            //   re.findMgcContainers( );
            //mgc_containers = re.getContainers();
            re.findOriginalMgcContainers();
            ArrayList orderedSeq = re.getSequencesOrderedByMarkerContainerSTP(94);
            
            
        }catch(Exception e){}
  }
            
 
    
}
   