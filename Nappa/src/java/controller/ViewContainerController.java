/*
 * ViewContainerController.java
 *
 * Created on October 22, 2007, 4:18 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package controller;

import java.util.Collection;
import process.ContainerManager;
import process.ProcessException;
import transfer.ContainerheaderTO;
import transfer.SampleTO;

/**
 *
 * @author dzuo
 */
public class ViewContainerController {
    
    /** Creates a new instance of ViewContainerController */
    public ViewContainerController() {
    }
    
    public ContainerheaderTO getContainer(String label) throws ControllerException {
        ContainerheaderTO container = null;
        try {
            container = ContainerManager.findContainer(label);
        } catch (ProcessException ex) {
            throw new ControllerException(ex.getMessage());
        }
        return container;
    }
    
    public static final void main(String args[]) {
        String label = "";
        ViewContainerController controller = new ViewContainerController();
        try {
            ContainerheaderTO c = controller.getContainer(label);
            System.out.println(c.getBarcode());
            Collection<SampleTO> samples = c.getSamples();
            for(SampleTO s:samples) {
                System.out.println(s.getName());
                System.out.println(s.getType());
                System.out.println(s.getPosition());
                System.out.println(s.getCell().getPosx());
                System.out.println(s.getCell().getPosy());
            }
        } catch (Exception ex) {
            System.out.println(ex);
            System.exit(1);
        }
        System.exit(0);
    }
}
