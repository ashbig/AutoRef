/*
 * PlateCondensationMapper.java
 *
 * Created on November 23, 2005, 3:05 PM
 */

package edu.harvard.med.hip.flex.process;

import edu.harvard.med.hip.flex.core.*;
import java.util.*;

/**
 *
 * @author  DZuo
 */
public class PlateCondensationMapper {
    public static final int ROWNUM = 16;
    public static final int COLNUM = 24;
    public static final int SRCROWNUM = 8;
    public static final int SPACE = 2;
    
    private Vector sampleLineageSet;
    
    /** Creates a new instance of PlateCondensationMapper */
    public PlateCondensationMapper() {
    }
    
    public Vector getSampleLineageSet() {return sampleLineageSet;}
    
    public Container mapContainers(List containers, String newContainerType, String newBarcode) throws Exception {
        Location location = new Location(Location.UNAVAILABLE);
        Container newContainer = new Container(newContainerType, location, newBarcode);
        sampleLineageSet = new Vector();
        for(int i=0; i<containers.size(); i++) {
            Container c = (Container)containers.get(i);
            c.restoreSampleWithoutSeq();
            Vector samples = c.getSamples();
            for(int n=0; n<samples.size(); n++) {
                Sample s = (Sample)samples.get(n);
                int pos = s.getPosition();
                int newPos = pos*SPACE-1+(i%SPACE)*ROWNUM+i/SPACE+(pos-1)/SRCROWNUM*ROWNUM;
                //System.out.println("src pos: "+pos+"\t"+"dest pos: "+newPos);
                Sample newSample = new Sample(s.getType(), newPos, newContainer.getId(), s.getConstructid(), s.getOligoid(), Sample.GOOD);
                newSample.setCloneid(s.getCloneid());
                newContainer.addSample(newSample);
                sampleLineageSet.addElement(new SampleLineage(s.getId(), newSample.getId()));
            }
        }
        
        return newContainer;
    }
    
}
