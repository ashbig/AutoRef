/*
 * Container384ToSlideMapper.java
 *
 * Created on April 26, 2007, 3:29 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package process;

import Algorithm.Plate384ToGalFileMapping;
import transfer.ProgrammappingTO;
import java.io.InputStream;
import transfer.ContainercellTO;
import transfer.ContainerheaderTO;
import transfer.SlidecellTO;

/**
 *
 * @author dzuo
 */
public class Container384ToSlideMapper extends ContainerMapper {
    
    /** Creates a new instance of Container384ToSlideMapper */
    public Container384ToSlideMapper() {
        super();
    }
    
    public Container384ToSlideMapper(InputStream file) {
        super();
        setAlgorithm(new Plate384ToGalFileMapping(file));
    }
    
    public String getLabware() {
        return ContainerheaderTO.getLABWARE_SLIDE();
    }
    
    public boolean isValid() {
        return true;
    }
    
    public ContainercellTO getContainercellmapTO(ProgrammappingTO m) {
        return new SlidecellTO(m.getDestpos(),m.getDestwellx(),m.getDestwelly(),ContainercellTO.TYPE_EMPTY,-1,-1,m.getDestblocknum(),m.getDestblockrow(),m.getDestblockcol(),m.getDestblockposx(),m.getDestblockposy(),m.getDestblockwellx(),m.getDestblockwelly());
    }
    
    public void addToContainerLineages(ContainerheaderTO containerFrom, ContainerheaderTO containerTo) {}
}