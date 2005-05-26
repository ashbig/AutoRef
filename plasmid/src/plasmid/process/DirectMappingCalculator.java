/*
 * DirectMappingCalculator.java
 *
 * Created on May 26, 2005, 11:09 AM
 */

package plasmid.process;

import java.util.*;

import plasmid.coreobject.*;

/**
 *
 * @author  DZuo
 */
public class DirectMappingCalculator extends MappingCalculator {
    
    /** Creates a new instance of DirectMappingCalculator */
    public DirectMappingCalculator() {
    }
    
    public DirectMappingCalculator(List src, List dest, String type, String sType) {
        super(src, dest, type, sType);
    }
    
    public List calculateMapping() {
        List mappingList = new ArrayList();
        
        for(int i=0; i<srcContainers.size(); i++) {
            Container c = (Container)srcContainers.get(i);
            String label = (String)destContainers.get(i);
            List l = calculateMappingForOneContainer(c, label);
            mappingList.addAll(l);
        }
        
        return mappingList;
    }
    
    public List calculateMappingForOneContainer(Container c, String label) {
        List l = new ArrayList();
        for(int i=0; i<c.getSize(); i++) {
            Sample s = c.getSample(i+1);
            Sample sample = new Sample();
            sample.setContainerlabel(label);
            sample.setCloneid(s.getCloneid());
            sample.setPositions(s.getPosition());
            sample.setStatus(s.getStatus());
            if(s.getType().equals(Sample.EMPTY))
                sample.setType(Sample.EMPTY);
            else
                sample.setType(destSampleType);
            
            l.add(new SampleLineage(s,sample));
        }
        
        return l;
    }
    
    public boolean isMappingValid() {
        if(srcContainers == null || destContainerType == null)
            return false;
        
        if(srcContainers.size() != destContainers.size())
            return false;
        
        for(int i=0; i<srcContainers.size(); i++) {
            Container c = (Container)srcContainers.get(i);
            String type = c.getType();
            if(!destContainerType.equals(type))
                return false;
        }
        
        return true;
    }
    
}
