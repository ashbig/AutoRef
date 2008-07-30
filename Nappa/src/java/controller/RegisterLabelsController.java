/*
 * RegisterLabelsController.java
 *
 * Created on October 18, 2007, 9:30 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package controller;

import dao.ContainerDAO;
import dao.ContainertypeDAO;
import dao.DaoException;
import database.DatabaseTransaction;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import transfer.ContainerheaderTO;
import transfer.ContainertypeTO;

/**
 *
 * @author dzuo
 */
public class RegisterLabelsController {
    
    /** Creates a new instance of RegisterLabelsController */
    public RegisterLabelsController() {
    }
    
    public void registerContainers(List<String> labels, ContainertypeTO type) throws ControllerException {
        Collection<ContainerheaderTO> containers = new ArrayList<ContainerheaderTO>();
        for(String l:labels) {
            ContainerheaderTO c = new ContainerheaderTO(0, l, type, ContainerheaderTO.getFORMAT_REGULAR(), ContainerheaderTO.getSTATUS_EMPTY(), ContainerheaderTO.getLOCATION_FREEZER(), null,0, null);
            containers.add(c);
        }
        
        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            ContainerDAO dao = new ContainerDAO(conn);
            dao.addContainers(containers);
            DatabaseTransaction.commit(conn);
        } catch (Exception ex) {
            DatabaseTransaction.rollback(conn);
            throw new ControllerException(ex.getMessage());
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
    }
    
    public Collection<ContainerheaderTO> checkContainers(List<String> labels) throws ControllerException {
        ContainerDAO dao = new ContainerDAO();
        try {
            Collection<ContainerheaderTO> existingContainers = dao.checkContainers(labels, false);
            return existingContainers;
        } catch (DaoException ex) {
            throw new ControllerException(ex.getMessage());
        }
    }
    
    public List<ContainertypeTO> getContainertypes() throws ControllerException {
        ContainertypeDAO dao = new ContainertypeDAO();
        try {
            List<ContainertypeTO> types = dao.getContainertypes();
            return types;
        } catch (DaoException ex) {
            throw new ControllerException(ex.getMessage());
        }
    }
    
    public static void main(String args[]) {
        List<String> labels = new ArrayList<String>();
        /**
        labels.add("HsxXG002541");
        labels.add("HsxXG002542");
        labels.add("HsxXG002543");
        labels.add("HsxXG002544");
        labels.add("HsxXG002545");
        labels.add("HsxXG002546");
        labels.add("HsxXG002547");
        labels.add("HsxXG002548");
        */
        //labels.add("HsxXG002552-7");
        //labels.add("HsxXG002552-8");
        
       //labels.add("Registration1");
        /**
        labels.add("HsxXG002541-1");
        labels.add("HsxXG002542-2");
        labels.add("HsxXG002543-3");
        labels.add("HsxXG002544-4");
        */
        
        labels.add("slide15");
        
        //ContainertypeTO type = new ContainertypeTO(ContainertypeTO.TYPE_PLATE_Costar96flatb, null, 8, 12);
        //ContainertypeTO type = new ContainertypeTO(ContainertypeTO.TYPE_PLATE384, null, 16, 24);
        ContainertypeTO type = new ContainertypeTO(ContainertypeTO.TYPE_SLIDE, null, 0,0);
        
        RegisterLabelsController controller = new RegisterLabelsController();
        try {
        Collection<ContainerheaderTO> containers = controller.checkContainers(labels);
        if(containers.size()>0) {
            System.out.println("The following labels exist in the system: ");
            for(ContainerheaderTO c:containers) {
                System.out.println(c.getBarcode());
            }
            System.exit(0);
        }
        controller.registerContainers(labels, type);
        } catch (Exception ex) {
            System.out.println(ex);
            System.exit(1);
        }
        System.exit(0);
    }
}
