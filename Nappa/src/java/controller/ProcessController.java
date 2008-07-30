/*
 * ProcessController.java
 *
 * Created on April 25, 2007, 1:26 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package controller;

import core.Processobjectlineageinfo;
import dao.ProcessDAO;
import database.DatabaseTransaction;
import java.io.Serializable;
import java.sql.Connection;
import java.util.Date;
import java.util.List;
import transfer.ProcessexecutionTO;
import transfer.ProcessobjectTO;
import transfer.ProcessprotocolTO;
import transfer.ResearcherTO;

/**
 *
 * @author dzuo
 */
public abstract class ProcessController  {
    protected ProcessprotocolTO protocol;
    protected ResearcherTO who;
    protected Date when;
    protected String outcome;
    
    protected ProcessexecutionTO pe;
    
    /** Creates a new instance of ProcessController */
    public ProcessController() {
    }
    
    public void persistProcess() throws ControllerException {
        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
        } catch (Exception ex) {
            throw new ControllerException("Cannot get database connection: "+ex.getMessage());
        }
        
        try {
            persistSpecificProcess(conn);
            
            ProcessDAO dao = new ProcessDAO(conn);
            dao.addProcess(pe);
            
            DatabaseTransaction.commit(conn);
        } catch (Exception ex) {
            DatabaseTransaction.rollback(conn);
            ex.printStackTrace();
            throw new ControllerException("Cannot add process to the database.");
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
    }
    
    public void doProcess() throws ControllerException {
        setPe(new ProcessexecutionTO(getProtocol(), null, getWho(), ProcessexecutionTO.getOUTCOME_SUCCESS()));
        doSpecificProcess();
    }
    
    public static ProcessexecutionTO getProcess(int id, boolean isObject, boolean isLineage) throws ControllerException {
        ProcessDAO dao = new ProcessDAO();
        ProcessexecutionTO p = null;
        try {
            p = dao.getProcess(id, isObject, isLineage);
        } catch (Exception ex) {
            throw new ControllerException(ex.getMessage());
        }
        return p;
    }
    
    public abstract void doSpecificProcess() throws ControllerException;
    public abstract void persistSpecificProcess(Connection conn) throws ControllerException;
    
    public ProcessprotocolTO getProtocol() {
        return protocol;
    }
    
    public void setProtocol(ProcessprotocolTO protocol) {
        this.protocol = protocol;
    }
    
    public ResearcherTO getWho() {
        return who;
    }
    
    public void setWho(ResearcherTO who) {
        this.who = who;
    }
    
    public Date getWhen() {
        return when;
    }
    
    public void setWhen(Date when) {
        this.when = when;
    }
    
    public String getOutcome() {
        return outcome;
    }
    
    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }
    
    public ProcessexecutionTO getPe() {
        return pe;
    }
    
    public void setPe(ProcessexecutionTO pe) {
        this.pe = pe;
    }
    
    public static void main(String args[]) {
        try {
            ProcessexecutionTO p = ProcessController.getProcess(5, true, false);
            List<Processobjectlineageinfo> lineages = p.getObjectlineages();
            int order;
            for(Processobjectlineageinfo l:lineages) {
                List<ProcessobjectTO> fromList = l.getFrom();
                List<ProcessobjectTO> toList = l.getTo();
                
                order = -1;
                System.out.println("===========================");
                System.out.println("Input objects: ");
                for(ProcessobjectTO from:fromList) {
                    if(order != from.getOrder()) {
                        System.out.println("------------");
                        order = from.getOrder();
                    }
                    System.out.println("ID="+from.getObjectid());
                    System.out.println("Name="+from.getObjectname());
                    System.out.println("Type="+from.getObjecttype());
                }
                
                order = -1;
                System.out.println("Output objects: ");
                for(ProcessobjectTO to:toList) {
                    if(order != to.getOrder()) {
                        System.out.println("------------");
                        order = to.getOrder();
                    }
                    System.out.println("ID="+to.getObjectid());
                    System.out.println("Name="+to.getObjectname());
                    System.out.println("Type="+to.getObjecttype());
                }
            }
            System.out.println("===========================");
        } catch (Exception ex) {
            System.out.println(ex);
        }
        System.exit(0);
    }
}
