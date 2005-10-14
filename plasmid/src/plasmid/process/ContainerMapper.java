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

    public List mapContainer(String destContainerType) throws Exception {
        WorklistGenerator g = new WorklistGenerator(worklist);
        List destLabels = new ArrayList(g.getDestContainerLabels());
        List containers = new ArrayList();
        for(int i=0; i<destLabels.size(); i++) {
            String label = (String)destLabels.get(i);
            Container c = new Container();
            c.setContainerid(i);
            c.setLabel(label);
            c.setType(destContainerType);
            containers.add(c);
        }
        
        for(int i=0; i<worklist.size(); i++) {
            SampleLineage sl = (SampleLineage)worklist.get(i);
            Sample from = sl.getSampleFrom();
            Sample to = sl.getSampleTo();
            to.setStatus(Sample.GOOD);
            to.setSampleid(i);
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
            if(c.getLabel().equals(s.getContainerlabel())) {
                s.setContainerid(c.getContainerid());
                c.addSample(s);
                return true;
            }
        }
         
        return false;
    }
}
