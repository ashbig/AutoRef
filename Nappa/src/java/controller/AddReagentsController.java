/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.ContainerDAO;
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
}
