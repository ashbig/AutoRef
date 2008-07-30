/*
 * Container96To96Mapper.java
 *
 * Created on October 23, 2007, 4:35 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package process;

import Algorithm.DirectProgramMapping;

/**
 *
 * @author dzuo
 */
public class Container96To96Mapper extends ContainerMapper {
    
    /** Creates a new instance of Container96To96Mapper */
    public Container96To96Mapper() {
        super();
        setAlgorithm(new DirectProgramMapping(8, 12));
    }
    
    public boolean isValid() {
        return (srcContainers.size() == destContainers.size());
    }
}
