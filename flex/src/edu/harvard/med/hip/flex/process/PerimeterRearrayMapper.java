/*
 * PerimeterRearrayMapper.java
 *
 * Created on February 17, 2004, 5:01 PM
 */

package edu.harvard.med.hip.flex.process;

import edu.harvard.med.hip.flex.core.*;
import java.util.*;
import edu.harvard.med.hip.flex.workflow.*;

/**
 *
 * @author  DZuo
 */
public class PerimeterRearrayMapper extends ManyToManyContainerMapper {
    public static final int [] EMPTY_WELL_LIST = {1,2,3,4,5,6,7,8,9,16,17,24,25,32,33,40,41,48,49,56,57,64,65,72,73,80,81,88,89,90,91,92,93,94,95,96};
    public static final int TOTAL_WELLS_DEST = 96;
    
    /** Creates a new instance of PerimeterRearrayMapper */
    public PerimeterRearrayMapper() {
        destWellIndex = 1;
        emptyWellList = EMPTY_WELL_LIST;
        totalWellsDest = TOTAL_WELLS_DEST;
    }
    
    public static void main(String args[]) {
        Container c1 = new Container(7983, null,null,"plate1");
        Container c2 = new Container(7984, null,null,"plate2");
        Container c3 = new Container(7985, null,null,"plate3");
        Vector containers = new Vector();
        containers.add(c1);
        containers.add(c2);
        containers.add(c3);
        Project project = null;
        Workflow workflow = null;
        Protocol protocol = null;
        try {
            project = new Project(Project.HUMAN);
            workflow = new Workflow(Workflow.EXPRESSION_WORKFLOW);
            protocol = new Protocol(Protocol.PERIMETER_REARRAY);
        } catch(Exception ex) {
            System.out.println(ex);
        }
        
        PerimeterRearrayMapper mapper = new PerimeterRearrayMapper();
        try {
            Vector newContainers = (mapper.doMapping(containers, protocol, project, workflow));
            for(int i=0; i<newContainers.size(); i++) {
                Container c = (Container)newContainers.get(i);
                System.out.println(c.getId());
            } 
            mapper.createRearrayFile();
            mapper.createWorklist("type 1", "type 2", 5);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        
        System.exit(0);
    }
}
