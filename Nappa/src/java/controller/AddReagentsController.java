/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.ContainerDAO;
import dao.ReagentDAO;
import database.DatabaseTransaction;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import transfer.ContainercellTO;
import transfer.ReagentTO;
import transfer.SampleTO;

/**
 *
 * @author DZuo
 */
public class AddReagentsController {

    public List<ReagentTO> getControlSamples(Collection <SampleTO> ss) throws ControllerException {
        List<ReagentTO> samples = new ArrayList<ReagentTO>();
        try {
            for (SampleTO sample : ss) {
                if (sample.getCell().isEmptycell()) {
                    if (!sample.getNewReagent().equals(ReagentTO.NOT_SELECTED)) {
                        int reagentid = Integer.parseInt(sample.getNewReagent());
                        ReagentTO reagent = new ReagentTO(reagentid, sample);
                        samples.add(reagent);
                        //System.out.println("AddReagentsController:getControlSamples:addSample:"+reagent.getReagentid());
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ControllerException(ex.getMessage());
        }
        return samples;
    }
    
    public void persistControlSamples(List<ReagentTO> samples) throws ControllerException {
        List<ContainercellTO> cells = new ArrayList<ContainercellTO>();
        for(ReagentTO s:samples) {
            cells.add(s.getSample().getCell());
        }
        
        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            ContainerDAO dao = new ContainerDAO(conn);
            dao.addReagents(samples);
            dao.updateCelltype(cells, ReagentTO.getTYPE_CONTROL());
            DatabaseTransaction.commit(conn);
        } catch (Exception ex) {
            DatabaseTransaction.rollback(conn);
            ex.printStackTrace();
            throw new ControllerException(ex.getMessage());
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
    }
    
    public boolean isReagentExist(String name) throws ControllerException {
        if(name==null || name.trim().length()==0)
            return false;
        ReagentTO reagent = null;
        try {
            reagent = ReagentDAO.getReagent(name.trim());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ControllerException("Cannot check reagent name.\n"+ex.getMessage());
        }
        
        if(reagent==null)
            return false;
        
        return true;
    }
    
    public void addReagent(ReagentTO reagent) throws ControllerException {
        List<ReagentTO> reagents = new ArrayList<ReagentTO>();
        reagents.add(reagent);
        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            ReagentDAO dao = new ReagentDAO(conn);
            dao.addReagents(reagents);
            DatabaseTransaction.commit(conn);
        } catch (Exception ex) {
            DatabaseTransaction.rollback(conn);
            ex.printStackTrace();
            throw new ControllerException("Error occured while adding reagent to the database.\n"+ex.getMessage());
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
    }
}
