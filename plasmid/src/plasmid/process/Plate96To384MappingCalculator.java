/*
 * Plate96To384MappingCalculator.java
 *
 * Created on May 10, 2007, 12:07 PM
 */

package plasmid.process;

import java.util.*;
import plasmid.coreobject.*;

/**
 *
 * @author  DZuo
 */
public class Plate96To384MappingCalculator extends MappingCalculator {
    public static final int ROWNUM = 16;
    public static final int COLNUM = 24;
    public static final int SRCROWNUM = 8;
    public static final int SPACE = 2;
    public static final int MULTIPLICATION = 4;
    
    /** Creates a new instance of Plate96To384MappingCalculator */
    public Plate96To384MappingCalculator() {
    }
    
    public Plate96To384MappingCalculator(List src, List dest, String sType) {
        super(src, dest, sType);
    }
    
    public List calculateMapping() throws Exception {
        List mappingList = new ArrayList();
        List srcs = new ArrayList();
        
        for(int i=0; i<srcContainers.size(); i++) {
            Container src = (Container)srcContainers.get(i);
            srcs.add(src);
            if((i+1)%MULTIPLICATION == 0 || i+1==srcContainers.size()) {
                int index = (i+1)/MULTIPLICATION-1;
                if((i+1)%MULTIPLICATION != 0 && i+1 == srcContainers.size()) {
                    index = srcContainers.size()/MULTIPLICATION;
                }
                Container dest = (Container)destContainers.get(index);
                List l = calculateMappingForOneContainer(srcs, dest);
                mappingList.addAll(l);
            }
        }
        
        return mappingList;
    }
    
    public List calculateMappingForOneContainer(List srcs, Container dest) throws Exception {
        List l = new ArrayList();
   
        for(int i=0; i<srcs.size(); i++) {
            Container src = (Container)srcs.get(i);
            for(int n=0; n<src.getSize(); n++) {
                Sample s = src.getSample(n+1);
                s.setContainerlabel(src.getLabel());
                s.setContainerType(src.getType());
                
                int pos = s.getPosition();
                int newPos = pos*SPACE-1+(i%SPACE)*ROWNUM+i/SPACE+(pos-1)/SRCROWNUM*ROWNUM;
                System.out.println("pos="+pos+"/newPos="+newPos);
                Sample sample = new Sample();
                sample.setContainerid(dest.getContainerid());
                sample.setContainerlabel(dest.getLabel());
                sample.setContainerType(dest.getType());
                sample.setCloneid(s.getCloneid());
                sample.setPositions(newPos, 16, 24);
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
                System.out.println(s.getContainerlabel()+"\t"+s.getPosition()+"\t"+sample.getContainerlabel()+"\t"+sample.getPosition());
                l.add(new SampleLineage(s,sample));
            }
        }
        return l;
    }
    
    public boolean isMappingValid() {
        if(srcContainers == null || destContainers == null)
            return false;
        
        if(srcContainers.size()/MULTIPLICATION > destContainers.size())
            return false;
        
        for(int i=0; i<srcContainers.size(); i++) {
            Container c = (Container)srcContainers.get(i);
            if(c.getCapacity() != 96)
                return false;
        }
        
        for(int i=0; i<destContainers.size(); i++) {
            Container c = (Container)destContainers.get(i);
            if(c.getCapacity() != 384)
                return false;
        }
        
        return true;
    }    
}
