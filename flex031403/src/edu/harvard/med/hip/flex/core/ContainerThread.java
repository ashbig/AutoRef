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
 * $Revision: 1.1 $
 * $Date: 2003-03-07 17:44:53 $
 * $Author: dzuo $
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
 * @author     $Author: dzuo $
 * @version    $Revision: 1.1 $ $Date: 2003-03-07 17:44:53 $
 */

public class ContainerThread extends Thread {
    
    //The thread id
    protected int id;
    
    
    /**
     * Builds a thread.  Should use a find or create method to get
     * a thread object.
     *
     * @param threadid The id of the plateset.'
     * @param containerList the List ThreadElements
     */
    protected ContainerThread(int threadid, List containerList) {
        this.id = threadid;
        this.elementList = containerList;
    }
    
    
    
    /**
     * Finds a thread by the container id. used for MGC plates because they do not have 
     *thread
     *
     * @param threadid The plate set id to use when looking for the thread.
     */
    public static ContainerThread findMGCContainerThread(String label) throws FlexDatabaseException,
    FlexCoreException{
        List containerList = new LinkedList();
        ContainerThread thread = new ContainerThread(-1, containerList);
        //construct all labels from label 'MGS00000n', "MDN000000n',"MLI00000n'
        String cont_number = label.substring(3);
        String all_labels= "'MGS"+cont_number+"','MLI"+cont_number+"','MDN"+cont_number+"'";
        String sql =
        "SELECT DISTINCT " +
        "c.containerid, pp.processname, pe.processdate from " +
        "containerheader c,  processprotocol pp, "+
        "processexecution pe,processobject po " +
        "where "+
        "c.containerid = po.containerid " +
        "AND pe.executionid = po.executionid AND "+
        "pe.protocolid = pp.protocolid "+
        "AND po.INPUTOUTPUTFLAG IN('O', 'B') AND c.label in ( " +
        all_labels  +   ") order by pe.processdate DESC";
        
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
            //add mgc container
            List cl =  Container.findContainersFromView("MGC"+cont_number);
            if (cl.size() > 0 )
            {
                Container mgc_cont = (Container)cl.get(0);
                ThreadElement curElem =
                new ThreadElement(thread,null,mgc_cont);
                containerList.add(0,curElem);
            }
            
        } catch (SQLException sqlE) {
            FlexDatabaseException e =
            new FlexDatabaseException("SQL: " + sql +"\n" + sqlE);
            throw e;
        }
        
        return thread;
    }
    
    /**
     * Finds a thread by the thread id.
     *
     * @param threadid The plate set id to use when looking for the thread.
     */
    public static ContainerThread findContainerThread1(int threadid) throws FlexDatabaseException,
    FlexCoreException{
        List containerList = new LinkedList();
        ContainerThread thread = new ContainerThread(threadid, containerList);
        String sql =
        "SELECT DISTINCT c.containerid,  pp.processname,  pe.processdate " +
        " from containerheader c,  processprotocol pp, "+
        "processexecution pe,processobject po " +
        "where "+
        "c.containerid = po.containerid AND pe.executionid = po.executionid AND "+
        "pe.protocolid = pp.protocolid "+
        "AND po.INPUTOUTPUTFLAG IN('O', 'B') AND c.threadid = " +
        threadid +
        " order by pe.processdate DESC";
        
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
    
      public static ContainerThread findContainerThread(int threadid) throws FlexDatabaseException,
    FlexCoreException{
        List containerList = new LinkedList();
        ContainerThread thread = new ContainerThread(threadid, containerList);
        String sql =
        "SELECT DISTINCT CONTAINERID,  PROCESSNAME , processdate "+
        " from CONTAINER_THREAD where THREADID =" + threadid + " order by processdate DESC";
        
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
      System.out.println("MGC000001".substring(0,3));
        ContainerThread thread = ContainerThread.findMGCContainerThread("MGC000001");
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
       /*
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
        **/
        System.exit(0);
    }
    
} // End class Thread


/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */
