/*
 * File : SequenceThread.java
 * Classes : SequenceThread
 *
 * Description :
 *
 *  Represents the process history of a sequence.
 *
 *
 * Author : Juan Munoz (jmunoz@3rdmill.com)
 *
 * See COPYRIGHT file for copyright information
 *
 *
 * The following information is used by CVS
 * $Revision: 1.1 $
 * $Date: 2003-03-07 17:44:51 $
 * $Author: dzuo $
 *
 ******************************************************************************
 *
 * Revision history (Started on July 2, 2001) :
 *
 *    Add entries here when updating the code. Remember to date and insert
 *    your 3 letters initials.
 *
 *    Jul-02-2001 : JMM - Class created.
 *
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */


package edu.harvard.med.hip.flex.core;

import java.util.*;
import java.util.List;
import java.sql.*;

import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.process.Process;

/**
 * Represents the processHistory of a sequence.
 *
 *
 * @author     $Author: dzuo $
 * @version    $Revision: 1.1 $ $Date: 2003-03-07 17:44:51 $
 */

public class SequenceThread extends Thread {
    
    /**
     * Constructor that takes in the list of elements.  Use a find method
     * to create a <code>Sequence Thread</code>
     *
     * @param elementList The list of thread elements (holding sequences) that
     *      make up this thread.
     */
    protected SequenceThread(List elementList) {
        this.elementList = elementList;
    }
    
    
    /**
     * Finds a <code>SequenceThread</code> based on the sequence name/value
     * pair.
     *
     * @param name The name to match to(gi, accession, unigene, etc..)
     * @param value The value to match to.
     *
     * @return The <code>SequenceThread</code> corresponding to the name
     * / value provided
     */
    public static SequenceThread findByName(String name, String value)
    throws FlexDatabaseException, FlexCoreException{
        name = DatabaseTransaction.prepareString(name);
        String sql=
        "SELECT DISTINCT "+
        "s.sampleid, s.sampletype, s.status_gb, c.containerid, "+
        "c.containertype, c.label, cc.position, pp.protocolid, "+
        "pp.processname, pe.processdate, pe.subprotocolname, "+
        "pe.researcherid, pe.extrainformation "+
        "FROM  " +
        "sample s, constructdesign cd, flexSequence fs, "+
        "containerheader c, containercell cc, samplelineage sl, "+
        "processexecution pe, processprotocol pp, name n "+
        "WHERE "+
        "cd.constructid=s.constructid AND fs.sequenceid=cd.sequenceid "+
        "AND cc.containerid=c.containerid AND "+
        "cc.containerid=s.containerid "+
        "AND cc.sampleid=s.sampleid AND sl.sampleid_to=s.sampleid "+
        "AND pe.executionid=sl.executionid AND "+
        "pp.protocolid=pe.protocolid "+
        "AND n.nametype='"+name+"' AND n.namevalue='"+value+"' "+
        "AND fs.sequenceid=n.sequenceid "+
        "ORDER BY pe.processdate DESC";
        
        return createThread(sql);
    }
    
    
    /**
     * finds a <code>SequenceThread</code> based on a flex sequence id.
     *
     * @param flexId The id of the flex sequence.
     *
     * @param the <code>SequenceThread</code> corresponding to the flex id
     *      provided.
     */
    public static SequenceThread findByFlexId(int flexId)
    throws FlexDatabaseException, FlexCoreException{
        String sql =
        "SELECT DISTINCT "+
        "s.sampleid, s.sampletype, s.status_gb, c.containerid, "+
        "c.containertype, c.label, cc.position, pp.protocolid, " +
        "pp.processname, pe.processdate, pe.subprotocolname, "+
        "pe.researcherid, pe.extrainformation "+
        "FROM "+
        "sample s, constructdesign cd, flexSequence fs, " +
        "containerheader c, containercell cc, samplelineage sl, "+
        "processexecution pe, processprotocol pp "+
        "WHERE "+
        " cd.constructid=s.constructid AND fs.sequenceid=cd.sequenceid "+
        "AND cc.containerid=c.containerid AND "+
        "cc.containerid=s.containerid "+
        "AND cc.sampleid=s.sampleid AND sl.sampleid_to=s.sampleid "+
        "AND pe.executionid=sl.executionid AND " +
        "pp.protocolid=pe.protocolid " +
        "AND fs.sequenceid= " + flexId +
        " ORDER BY pe.processdate DESC";
        
        return createThread(sql);
    }
    
    
    /**
     * Helper method that takes a sql statement and creates a SequenceThread
     *
     * @param sql statement
     */
    private static SequenceThread createThread(String sql)
    throws FlexDatabaseException, FlexCoreException{
        List seqList = new LinkedList();
        SequenceThread thread = new SequenceThread(seqList);
        ResultSet rs = null;
        try {
            rs = DatabaseTransaction.getInstance().executeQuery(sql);
            while(rs.next()) {
                
                Sample sample = new Sample(rs.getInt("SAMPLEID"));
                Container curContainer =
                new Container(rs.getInt("CONTAINERID"));
                Protocol curProtocol =
                new Protocol(rs.getString("PROCESSNAME"));
                
                Process curProcess =
                Process.findCompleteProcess(curContainer, curProtocol);
                
                ThreadElement curElem =
                new ThreadElement(thread,curProcess,sample);
                seqList.add(curElem);
            }
            
        } catch (SQLException sqlE) {
            FlexDatabaseException e =
            new FlexDatabaseException("SQL: " + sql +"\n" + sqlE);
            throw e;
        }
        
        return thread;
        
    }
    
    /**
     * Utility method to find all the name types.
     *
     * @return list of Strings representing the different name types
     */
    public static List getNameTypes() throws FlexDatabaseException {
        String sql = "SELECT DISTINCT nametype from name ORDER BY NAMETYPE";
        ResultSet rs = DatabaseTransaction.getInstance().executeQuery(sql);
        List typeList = new LinkedList();
        try {
            while(rs.next()) {
                typeList.add(rs.getString("NAMETYPE"));
            }
        } catch(SQLException sqlE) {
            throw new FlexDatabaseException(sqlE);
        }
        return typeList;
    }
    
    /**
     * main method to do some testing
     */
    public static void main(String [] args) throws Exception{
        SequenceThread thread = SequenceThread.findByFlexId(3624);
        Iterator iter = thread.getElements().iterator();
        while(iter.hasNext()) {
            ThreadElement curElem =
            (ThreadElement)iter.next();
            Sample sample = (Sample) curElem.getObject();
            Process curProcess = curElem.getProcess();
            System.out.println("sample: " + sample.getId());
            System.out.println("process: " + curProcess.getExecutionid());
            System.out.println("protocol: " + curProcess.getProtocol().getProcessname());
        }
        System.exit(0);
    }
} // End class SequenceThread


/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */
