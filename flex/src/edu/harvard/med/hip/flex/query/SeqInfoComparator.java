/*
 * SeqInfoComparator.java
 *
 * Created on March 12, 2003, 5:41 PM
 */

package edu.harvard.med.hip.flex.query;
import java.util.Comparator;

/**
 *
 * @author  hweng
 */
public class SeqInfoComparator implements Comparator{
    
    public int compare(Object o1, Object o2){
        if( ((SequenceInfo)o1).getSequence().getSeqID() > ((SequenceInfo)o2).getSequence().getSeqID() )
            return 1;
        else if( ((SequenceInfo)o1).getSequence().getSeqID() < ((SequenceInfo)o2).getSequence().getSeqID() )
            return -1;
        else{
            if( ((SequenceInfo)o1).getConstruct().getId() > ((SequenceInfo)o2).getConstruct().getId() )
                return 1;
            else if ( ((SequenceInfo)o1).getConstruct().getId() < ((SequenceInfo)o2).getConstruct().getId() )
                return -1;
            else{
                if( ((SequenceInfo)o1).getSample().getId() > ((SequenceInfo)o2).getSample().getId() )
                    return 1;
                else if( ((SequenceInfo)o1).getSample().getId() < ((SequenceInfo)o2).getSample().getId() ) 
                    return -1;
                else
                    return 0;
            }
        }
    }
    
}
