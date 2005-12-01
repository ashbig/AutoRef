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
    
    public DirectMappingCalculator(List src, List dest, String sType) {
        super(src, dest, sType);
    }
    
    public List calculateMapping() {
        List mappingList = new ArrayList();
        
        for(int i=0; i<srcContainers.size(); i++) {
            Container src = (Container)srcContainers.get(i);
            Container dest = (Container)destContainers.get(i);
            List l = calculateMappingForOneContainer(src, dest);
            mappingList.addAll(l);
        }
        
        return mappingList;
    }
    
    public List calculateMappingForOneContainer(Container src, Container dest) {
        List l = new ArrayList();
        for(int i=0; i<src.getSize(); i++) {
            Sample s = src.getSample(i+1);
            s.setContainerType(src.getType());
            Sample sample = new Sample();
            sample.setContainerid(dest.getContainerid());
            sample.setContainerlabel(dest.getLabel());
            sample.setContainerType(dest.getType());
            sample.setCloneid(s.getCloneid());
            sample.setPositions(s.getPosition());
            sample.setStatus(s.getStatus());
            if(s.getType().equals(Sample.EMPTY))
                sample.setType(Sample.EMPTY);
            else {
                if(s.getResult() != null) {
                    if(Sample.isResultPass(s.getType(), s.getResult())) {
                        sample.setType(destSampleType);
                    } else {
                        sample.setType(Sample.EMPTY);
                    }
                } else {
                    sample.setType(destSampleType);
                }
            }
            l.add(new SampleLineage(s,sample));
        }
        
        return l;
    }
    
    public boolean isMappingValid() {
        if(srcContainers == null || destContainers == null)
            return false;
        
        if(srcContainers.size() != destContainers.size())
            return false;
        
        int srcCapacity = 0;
        for(int i=0; i<srcContainers.size(); i++) {
            Container c = (Container)srcContainers.get(i);
            if(i == 0) {
                srcCapacity = c.getCapacity();
            }
            
            if(srcCapacity != c.getCapacity())
                return false;
        }
        
        int destCapacity = 0;
        for(int i=0; i<destContainers.size(); i++) {
            Container c = (Container)destContainers.get(i);
            if(i == 0) {
                destCapacity = c.getCapacity();
            }
            
            if(destCapacity != c.getCapacity())
                return false;
        }
        
        if(srcCapacity == 0 || destCapacity == 0)
            return false;
        
        if(srcCapacity != destCapacity)
            return false;
        
        return true;
    }    
}
