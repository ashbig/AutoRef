/*
 * ContainerMapper.java
 *
 * Created on May 26, 2005, 12:00 PM
 */

package plasmid.process;

import java.util.*;
import java.sql.*;

import plasmid.database.DatabaseManager.*;
import plasmid.database.*;
import plasmid.coreobject.*;

/**
 *
 * @author  DZuo
 */
public class ContainerMapper {
    private List worklist;
    
    /** Creates a new instance of ContainerMapper */
    public ContainerMapper() {
    }
    
    public ContainerMapper(List worklist) {
        this.worklist = worklist;
    }
    
    public List getWorklist() {return worklist;}
    public void setWorklist(List l) {this.worklist = l;}
    
    public List mapContainer() throws Exception {
        WorklistGenerator g = new WorklistGenerator(worklist);
        List containers = new ArrayList(g.getDestContainers());
    
        for(int i=0; i<worklist.size(); i++) {
            SampleLineage sl = (SampleLineage)worklist.get(i);
            Sample to = sl.getSampleTo();
            to.setStatus(Sample.GOOD);
            //to.setSampleid(0);
            if(!addToContainer(containers, to)) {
                throw new Exception("Cannot add sample "+to.getContainerlabel()+"/"+to.getPosition()+" to containers");
            }
        }
        
        return containers;
    }
    
    public boolean addToContainer(List containers, Sample s) {
        if(containers == null || s == null)
            return false;
        
        for(int i=0; i<containers.size(); i++) {
            Container c = (Container)containers.get(i);
            if(c.getContainerid() == s.getContainerid()) {
                s.setContainerid(c.getContainerid());
                c.addSample(s);
                return true;
            }
        }
        
        return false;
    }
    
    public List convertToTubes(Container container, Map mapping, boolean isWorking) throws Exception {
        if(mapping == null) {
            throw new Exception("Please provide the mapping list.");
        }
        
        if(container == null) {
            throw new Exception("container is null.");
        }
        
        List samples = container.getSamples();
        if(samples == null) {
            throw new Exception("sample list is null.");
        }
        
        List tubes = new ArrayList();
        
        for(int i=0; i<samples.size(); i++) {
            Sample s = (Sample)samples.get(i);
            int position = s.getPosition();
            String label = s.getContainerlabel();
            String barcode = (String)mapping.get((new Integer(position)).toString());
            if(barcode == null) {
                continue;
            }
            
            Container c = new Container(0, Container.TUBE, barcode, null, Location.BIOBANK, Container.getCapacity(Container.TUBE), Container.FILLED);
            s.setContainerlabel(barcode);
            s.setPositions(1);
            if(isWorking && s.getType().equals(Sample.GLYCEROL))
                s.setType(Sample.WORKING_GLYCEROL);
            c.addSample(s);
            tubes.add(c);
        }
        return tubes;
    }
    
    public Container convertToPlates(List tubes, Map mapping) throws Exception {
        if(mapping == null) {
            throw new Exception("Please provide the mapping list.");
        }
        
        if(tubes == null) {
            throw new Exception("container is null.");
        }
        
        Container container = new Container();
        container.setType(Container.MICRONIC96TUBEMP16);
        container.setCapacity(Container.getCapacity(Container.MICRONIC96TUBEMP16));
        Set keys = mapping.keySet();
        Iterator iter = keys.iterator();
        while(iter.hasNext()) {
            String position = (String)iter.next();
            String barcode = (String)mapping.get(position);
            
            if(TubeMap.NOTUBE.equals(barcode))
                continue;
            
            for(int i=0; i<tubes.size(); i++) {
                Container c = (Container)tubes.get(i);
                if(barcode.equals(c.getLabel())) {
                    Sample sample = c.getSample(1);
                    sample.setPositions(Integer.parseInt(position));
                    container.addSample(sample);
                    break;
                }
            }
        }
        
        return container;
    }
}
