/*
 * Container96To384Mapper.java
 *
 * Created on July 12, 2007, 3:23 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package process;

import Algorithm.Plate96To384FileMapping;
import java.io.InputStream;

/**
 *
 * @author dzuo
 */
public class Container96To384Mapper extends ContainerMapper {
    
    /** Creates a new instance of Container96To384Mapper */
    public Container96To384Mapper() {
    }
    
    public Container96To384Mapper(InputStream file) {
        super();
        setAlgorithm(new Plate96To384FileMapping(file));
    }
     /**  
    public ContainercellTO getContainercellmapTO(Programmapping m) {
        return new ContainercellTO(m.getDestpos(),m.getDestwellx(),m.getDestwelly(),ContainercellTO.getTYPE_CONTROL());
    }*/
    /**
    public String getLabware() {
        return ContainerheaderTO.getLABWARE_PLATE384();
    }
    **/
    public boolean isValid() {
        return (srcContainers.size() == destContainers.size()*4);
    }
}
