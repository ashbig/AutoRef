/*
 * File : ContainerThread.java
 * Classes : ContainerThread
 *
 * Description :
 *
 * A container thread is a set of plates (plate set) used in a single workflow
 *
 *
 * Author : Juan Munoz(jmunoz@3rdmill.com)
 *
 * See COPYRIGHT file for copyright information
 *
 *
 * The following information is used by CVS
 * $Revision: 1.2 $
 * $Date: 2001-07-09 16:00:56 $
 * $Author: jmunoz $
 *
 ******************************************************************************
 *
 * Revision history (Started on June 26, 2001) :
 *
 *    Add entries here when updating the code. Remember to date and insert
 *    your 3 letters initials.
 *
 *    Jun-26-2001 : JMM - Class created.
 *
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */


package edu.harvard.med.hip.flex.core;

import java.sql.*;
import java.sql.Date;

import java.text.*;

import java.util.*;

import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.process.Process;

/**
 * Represents the set of plates used in a single workflow.
 *
 * @author     $Author: jmunoz $
 * @version    $Revision: 1.2 $ $Date: 2001-07-09 16:00:56 $
 */

public class ContainerThread extends Thread {
    
    //The thread id
    protected int id;
    
    
    /**
     * Builds a thread.  Should use a find or create method to get
     * a thread object.
     *
     * @param plateSetId The id of the plateset.'
     * @param containerList the List ThreadElements
     */
    protected ContainerThread(int plateSetId, List containerList) {
        this.id = plateSetId;
        this.elementList = containerList;
    }
    
    
    
    
    
    /**
     * Finds a thread by the plateset id.
     *
     * @param plateSetId The plate set id to use when looking for the thread.
     */
    public static ContainerThread findContainerThread(int plateSetId) throws FlexDatabaseException,
    FlexCoreException{
        List containerList = new LinkedList();
        ContainerThread thread = new ContainerThread(plateSetId, containerList);
        String sql =
        "SELECT " +
        "c.containerid, c.containertype, l.locationtype, c.label," +
        "pp.processname, pe.processdate,pe.subprotocolname, " +
        "pe.researcherid, pe.extrainformation, pe.executionid" +
        " from " +
        "containerheader c, containerlocation l, processprotocol pp, "+
        "processexecution pe,processobject po " +
        "where "+
        "c.containerid = po.containerid AND c.locationid = l.locationid "+
        "AND pe.executionid = po.executionid AND "+
        "pe.protocolid = pp.protocolid "+
        "AND po.INPUTOUTPUTFLAG IN('O', 'B') AND c.platesetid = " +
        plateSetId +
        " order by pe.processdate";
        
        ResultSet rs = DatabaseTransaction.getInstance().executeQuery(sql);
        
        try {
            while(rs.next()) {
                Container curContainer =
                new Container(rs.getInt("CONTAINERID"));
                Protocol curProtocol =
                new Protocol(rs.getString("PROCESSNAME"));
                Process curProcess =
                Process.findCompleteProcess(curContainer, curProtocol);
                ThreadElement curElem =
                new ThreadElement(thread,curProcess,curContainer);
                containerList.add(curElem);
            }
            
        } catch (SQLException sqlE) {
            FlexDatabaseException e =
            new FlexDatabaseException("SQL: " + sql +"\n" + sqlE);
            throw e;
        }
        
        return thread;
    }
    
 
    
    /**
     * main method to do some testing
     */
    public static void main(String [] args) throws Exception{
        ContainerThread thread = ContainerThread.findContainerThread(1);
        Iterator iter = thread.getElements().iterator();
        while(iter.hasNext()) {
            ThreadElement curElem = 
                (ThreadElement)iter.next();
            Container curContainer = (Container) curElem.getObject();
            Process curProcess = curElem.getProcess();
            System.out.println("container: " + curContainer.getId());
            System.out.println("process: " + curProcess.getExecutionid());
            System.out.println("protocol: " + curProcess.getProtocol().getProcessname());
        }
        System.exit(0);
    }
    
} // End class Thread


/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */
