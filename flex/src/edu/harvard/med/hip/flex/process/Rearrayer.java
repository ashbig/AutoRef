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
    
    private ArrayList m_Sequences = null;//seq id
    private Hashtable m_sequences_by_key = null;
    private ArrayList m_sequence_descriptions = null;
    private ArrayList m_container_descriptions = null;
    
    private int       m_total_wells = 94;   
 
    
  
    /** Creates a new instance of Rearrayer */
    //@param list of Sequence objects
    public Rearrayer(ArrayList sequences)
    {
        m_Sequences = sequences ;
        m_sequence_descriptions = new ArrayList();
        m_container_descriptions = new ArrayList();
    }
    
    /** Creates a new instance of Rearrayer */
    //@param list of Sequence objects
    public Rearrayer(ArrayList sequences, int wells)
    {
        this(sequences);
        m_total_wells = wells;
    }
   
       
    public ArrayList getPlates(ArrayList messages) throws Exception
    {
        if ( m_Sequences == null || m_Sequences.size() == 0) return null;
        //create hashtable of sequences , key - sequence id
        Hashtable sequences = new Hashtable();
        ArrayList sequence_ids = new ArrayList();
        for (int count = 0; count < m_Sequences.size(); count++)
        {
            int seq_id = ((Sequence)m_Sequences.get(count)).getId();
            sequences.put(new Integer(seq_id), m_Sequences.get(count));
            sequence_ids.add(new Integer(seq_id));
        }
        m_sequences_by_key = sequences;
        // create sequence description for each gene in request
        findMgcContainersFromDB(sequence_ids, sequences);
        //check for culture block availability
        messages = checkForCultureBlocks();
        //sort sequences by marker/ number of sequences by plate
        sortSequencesByContainerDescription();
        return getArrangedPlates();
    }
    
 
 
    //function finds all containers that contain samples with these sequences.
    //function always gets the last inserted container that containes this sequence
    private void findMgcContainersFromDB(ArrayList sequence_ids, Hashtable sequences) throws Exception
    {
        int current_sequence_id = -1;
        MgcContainer mgc_container = null;
        SequenceDescription seqDesc = null;
        ContainerDescription contDesc = null;
        ArrayList mgc_container_in_rearray = null;
        int seq_count = -1;
        
        //starting from first sequence
        while (! sequence_ids.isEmpty() )
        {
            current_sequence_id =((Integer)sequence_ids.get(0)).intValue() ;
            mgc_container = MgcContainer.findMGCContainerFromSequenceID(current_sequence_id);
            mgc_container.restoreSample();
            seq_count = 0;
            contDesc = new ContainerDescription(mgc_container.getLabel(), 
                                                mgc_container.getMarker(),
                                                mgc_container.getId(),
                                                mgc_container.getGlycerolContainerid());
            //check if any other sequence in request come from the same container
            for (int count = 0; count < mgc_container.getSamples().size(); count++)
            {
                MgcSample ms = (MgcSample)mgc_container.getSamples().get(count);
                Integer seq_key = new Integer(ms.getSequenceId());
                if (sequences.containsKey(seq_key ) )
                {
                    seqDesc = new SequenceDescription(ms.getSequenceId(), ms.getPosition(), ms.getId(), contDesc, ms.getImageId());
                    seqDesc.setCdsLength( ((Sequence)sequences.get(seq_key)).getCDSLength()   );
                    m_sequence_descriptions.add( seqDesc );
                    sequence_ids.remove(seq_key);
                    seq_count++;
                    if (sequence_ids.isEmpty()) continue;
                }
                
            }
            contDesc.setNumberOfSequences(seq_count);
            m_container_descriptions.add(contDesc);
        }
        
    }

    /* 
      Function check availability of culture blocks requered to process request 
      print message for each plate that should be on queue but missing
    */
     private ArrayList  checkForCultureBlocks() throws Exception
     {
       int cont_id = -1;
       ContainerDescription cont_desc = null;
       ArrayList messages = new ArrayList();
       for (int container_count = 0; container_count < m_container_descriptions.size(); container_count++)
       {
           cont_desc = (ContainerDescription)m_container_descriptions.get(container_count);
           cont_id = cont_desc.getGlycerolId() == -1 ? cont_desc.getId(): cont_desc.getGlycerolId() ;
           if (  Container.findNextContainerFromPrevious(cont_id, Protocol.CREATE_CULTURE_FROM_MGC) != null)
                cont_desc.setStatus(true);
           else
           {
               cont_desc.setStatus(false);
               messages.add("Culture block from container " + cont_desc.getId() +" not available.");
           }
       }
       return messages;
     }
    

   
    
    /*function sort sequence descriptions by 
            MGC container marker / 
            number of mgc clone on original MGC container
     *     
    */
    private void sortSequencesByContainerDescription()
    {
        //sort sequences by container
        Collections.sort( m_sequence_descriptions, new Comparator()
        {
            public int compare(Object cont1, Object cont2)
            {
                
                ContainerDescription cont_1 =(ContainerDescription) ((SequenceDescription)cont1).getContainerDescription();
                ContainerDescription cont_2 = (ContainerDescription) ((SequenceDescription)cont2).getContainerDescription();
                         
                int res =  cont_1.getMarker().compareToIgnoreCase(cont_2.getMarker() ) ;
                if (res == 0)//same marker
                   return    -(cont_1.getNumberOfSequences() - cont_2.getNumberOfSequences());
                else
                   return res;
                
            }
        });
       
        
    }
    
    
     /*function sort sequence descriptions by sqw tooth pattern 
      * return array of sequence objects & array of sequence descriptions per plate
     *     
    */
    private ArrayList getArrangedPlates()
    {
        //plates collection
        ArrayList plates = new ArrayList();
        ArrayList plate = new ArrayList();
        //plate description
        LinkedList seq_objects = new LinkedList();
        ArrayList  seq_description = new ArrayList();
        int plate_start = 0;     
        int well_on_plate = 0;
        
        //sort sequences by container
        for (int seq_count = 0 ; seq_count < m_sequence_descriptions.size(); seq_count++)
        {
            //check if this sequence container is OK, if not clear arrays 
            //and increase counter to the start of next plate
            if ( ! ((SequenceDescription) m_sequence_descriptions.get(seq_count)).getContainerDescription().getStatus()  )
            {
                seq_description = new ArrayList();
                seq_count = plate_start + m_total_wells; 
                plate_start += m_total_wells;
                continue;
            }
            
            seq_description.add(    m_sequence_descriptions.get(seq_count)  );
            well_on_plate++;
                //new plate
            if (well_on_plate % m_total_wells == 0 || seq_count == m_sequence_descriptions.size() )
            {
                well_on_plate = 0;
                //sort description by saw-tooth pattern
                seq_description = Algorithms.rearangeSawToothPatternInSequenceDescription(seq_description);
                //create plate
                for (int count = 0 ; count < seq_description.size(); count++)
                {
                    seq_objects.add(  m_sequences_by_key.get(new Integer( ((SequenceDescription)seq_description.get(count)).getId()))  );
                }
                plate.add(seq_objects); plate.add(seq_description);
                plates.add(plate);
                plate = new ArrayList();
                seq_description = new ArrayList(); seq_objects = new LinkedList();
                plate_start = seq_count + 1;
            }
        }
       
        return plates;
    } 
   
    
    
    
 
//********************************testing**********************************
 
  public static void main(String args[])
    {
        try
        {
            Request m_Request = new Request(135);
          ArrayList seq_ids = new ArrayList();
            for (int count = 0; count < m_Request.getSequences().size(); count++)
            {
                Integer id = new Integer(((FlexSequence) m_Request.getSequences().get(count)).getId());
                seq_ids.add( id );
            }
           // Rearrayer re = new Rearrayer( seq_ids );
          //  ArrayList mgc_containers = null;
          //  re.findMgcContainers( );
          //  mgc_containers = re.getContainers();
           // re.findOriginalMgcContainers();
           // ArrayList orderedSeq = re.getSequencesOrderedByMarkerContainerSTP(94);
           // Algorithms.rearangeSawToothPatternInFlexSequence(new ArrayList(m_Request.getSequences()));
            
        }catch(Exception e){}
  }
            
 
    
}
   
   
 

