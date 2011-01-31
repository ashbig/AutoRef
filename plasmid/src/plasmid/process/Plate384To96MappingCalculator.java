/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package plasmid.process;

import java.util.*;
import plasmid.coreobject.*;


/**
 *
 * @author Dongmei
 */
public class Plate384To96MappingCalculator extends MappingCalculator {
    public static final int ROWNUM = 16;
    public static final int COLNUM = 24;
    public static final int SRCROWNUM = 8;
    public static final int SPACE = 2;
    public static final int MULTIPLICATION = 4;
    
    /** Creates a new instance of Plate96To384MappingCalculator */
    public Plate384To96MappingCalculator() {
    }
    
    public Plate384To96MappingCalculator(List src, List dest, String sType) {
        super(src, dest, sType);
    }
    
    public List calculateMapping() throws Exception {
        List mappingList = new ArrayList();
        List dests = new ArrayList();
        
        for(int i=0; i<destContainers.size(); i++) {
            Container dest = (Container)destContainers.get(i);
            dests.add(dest);
            if((i+1)%MULTIPLICATION == 0 || i+1==destContainers.size()) {
                int index = (i+1)/MULTIPLICATION-1;
                if((i+1)%MULTIPLICATION != 0 && i+1 == destContainers.size()) {
                    index = destContainers.size()/MULTIPLICATION;
                }
                Container src = (Container)srcContainers.get(index);
                List l = calculateMappingForOneContainer(src, dests);
                mappingList.addAll(l);
            }
        }
        
        return mappingList;
    }
    
    public List calculateMappingForOneContainer(Container src, List dests) throws Exception {
        List l = new ArrayList();
   
        for(int i=0; i<dests.size(); i++) {
            Container dest = (Container)dests.get(i);
            for(int n=0; n<dest.getCapacity(); n++) {
                int destPos = n+1;
                int srcPos = destPos*SPACE-1+(i%SPACE)*ROWNUM+i/SPACE+(destPos-1)/SRCROWNUM*ROWNUM;
                Sample s = src.getSample(srcPos);
                s.setContainerlabel(src.getLabel());
                s.setContainerType(src.getType());
                System.out.println("srcpos="+srcPos+"/destPos="+destPos);
                Sample sample = new Sample();
                sample.setContainerid(dest.getContainerid());
                sample.setContainerlabel(dest.getLabel());
                sample.setContainerType(dest.getType());
                sample.setCloneid(s.getCloneid());
                sample.setPositions(destPos, 8, 12);
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
        
        if(destContainers.size()/MULTIPLICATION > srcContainers.size())
            return false;
        
        for(int i=0; i<srcContainers.size(); i++) {
            Container c = (Container)srcContainers.get(i);
            if(c.getCapacity() != 384)
                return false;
        }
        
        for(int i=0; i<destContainers.size(); i++) {
            Container c = (Container)destContainers.get(i);
            if(c.getCapacity() != 96)
                return false;
        }
        
        return true;
    }    
}
