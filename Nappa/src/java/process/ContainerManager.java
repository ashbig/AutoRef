/*
 * ContainerManager.java
 *
 * Created on May 1, 2007, 3:28 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package process;

import dao.ContainerDAO;
import dao.DaoException;
import java.util.ArrayList;
import java.util.Collection;
import transfer.ContainerheaderTO;
import transfer.SampleTO;
import util.*;

/**
 *
 * @author dzuo
 */
public class ContainerManager {
    
    /**
     * Creates a new instance of ContainerManager
     */
    public ContainerManager() {
    }
    
    public Collection findContainers(Collection labels, boolean isReagent) throws UtilException {
        ContainerDAO cdao = new ContainerDAO();
        Collection<ContainerheaderTO> c = null;
        try {
            c = cdao.getContainers(labels, isReagent);
        } catch (DaoException ex) {
            throw new UtilException("Error occured while trying to find containers.\n"+ex.getMessage());
        }
        
        return c;
    }
    
    public static Collection checkContainers(Collection labels, String status, boolean isContainertype, boolean isSample, boolean isReagent) throws UtilException {
        Collection<ContainerheaderTO> containers = null;
        try {
            containers = ContainerDAO.checkContainers(labels, status, isContainertype, isSample, isReagent);
        } catch (DaoException ex) {
            throw new UtilException(ex.getMessage());
        }
        
        Collection existinglabels = new ArrayList();
        for(ContainerheaderTO c:containers) {
            existinglabels.add(c.getBarcode());
        }
        labels.removeAll(existinglabels);
        
        return containers;
    }
    
    public static SampleTO findSample(Collection<ContainerheaderTO>containers, int pos) {
        SampleTO s = null;
        
        for(ContainerheaderTO c:containers) {
            if((s=c.getSample(pos)) != null)
                return s;
        }
        
        return s;
    }
    
    public static ContainerheaderTO findContainer(String label) throws ProcessException {
        ContainerDAO cdao = new ContainerDAO();
        ContainerheaderTO c = null;
        try {
            c = cdao.getContainer(label);
        } catch (DaoException ex) {
            throw new ProcessException("Error occured while trying to find containers.\n"+ex.getMessage());
        }
        
        return c;
    }
}
