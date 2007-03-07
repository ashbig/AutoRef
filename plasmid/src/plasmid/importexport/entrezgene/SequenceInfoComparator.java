/*
 * SequenceInfoComparator.java
 *
 * Created on February 23, 2007, 10:19 AM
 */

package plasmid.importexport.entrezgene;

import java.util.*;

/**
 *
 * @author  DZuo
 */
public class SequenceInfoComparator implements Comparator {
    
    /** Creates a new instance of SequenceInfoComparator */
    public SequenceInfoComparator() {
    }
    
    public int compare(Object obj, Object obj1) {
        String acc1 = ((SequenceInfo)obj).getAccession();
        String acc2 = ((SequenceInfo)obj1).getAccession();
        String pacc1 = ((SequenceInfo)obj).getPaccession();
        String pacc2 = ((SequenceInfo)obj1).getPaccession();
        
        if(acc1==null && acc2==null && pacc1==null & pacc2==null)
            return 0;
        
        if(acc1 == null && pacc1 != null)
            if(acc2 == null && pacc1.equals(pacc2))
                return 0;
        
        if(pacc1 == null && acc1 != null)
            if(pacc2 == null && acc1.equals(acc2))
                return 0;
        
        if(acc1 != null && pacc1 != null)
          if(acc1.equals(acc2) && pacc1.equals(pacc2))
              return 0;
        
        return 1;
    }
    
}
