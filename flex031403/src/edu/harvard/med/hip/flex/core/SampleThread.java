/*
 * File : SampleThread.java
 * Classes : SampleThread
 *
 * Description :
 *
 * Represents a complete sample thread (lineage).
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
 * Revision history (Started on July 26, 2001) :
 *
 *    Add entries here when updating the code. Remember to date and insert
 *    your 3 letters initials.
 *
 *    07-26-2001 : JMM - Class created. 
 *
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
*/


package edu.harvard.med.hip.flex.core;

import java.sql.*;
import java.util.*;

import edu.harvard.med.hip.flex.core.Thread;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.process.Process;

/**
 * Represents a sample thread.  
 * 
 *A sample thread is a the linage a sample
 * goes through as it is mapped from container to container. 
 *
 * @author     $Author: dzuo $
 * @version    $Revision: 1.1 $ $Date: 2003-03-07 17:44:51 $
 */

public class SampleThread extends Thread {

    /**
     * Protected constructor.
     *
     * @param elementList The list of thread elements in this sample thread.
     */
    protected SampleThread(List elementList) {
        this.elementList = elementList;
    }
    
    
    /**
     * Finds the sample thread given the id of a sample.
     *
     * @param sampleId The id of the sample to find the linage for.
     *
     * @return SampleThread for the sampleId.
     *
     * @exception FlexDatabaseException When a problem with the database occurs.
     * @exception FlexCoreException When there is a problem creating on of 
     *      core object, this should never happen here.
     * @exception FlexProcessException When there is a problem creating a 
     *      process object, this should never happen here.
     */
    public static Thread findBySampleId(int sampleId) 
    throws FlexDatabaseException, FlexCoreException, FlexProcessException {
        Thread retThread =null;
       /*
        SELECT l.sampleid_to, pe.executionid, c.containerid
        FROM 	processexecution pe, 
	containercell cc,
	containerheader c,
	(SELECT sampleid_to, executionid FROM samplelineage sl
            CONNECT BY PRIOR sl.sampleid_to = sl.sampleid_from
            START WITH sl.sampleid_to = 149330
            UNION
            SELECT sampleid_to, executionid
	    FROM samplelineage sl
            CONNECT BY PRIOR sl.sampleid_from = sl.sampleid_to
            START WITH sl.sampleid_to = 149330 ) l
        WHERE
	l.executionid = pe.executionid AND l.sampleid_to = cc.sampleid
	AND cc.containerid = c.containerid
        */
        
        /*
         SELECT l.sampleid_to, pe.executionid, c.containerid
        FROM  processexecution pe,
 containercell cc,
 containerheader c,
(
	SELECT sampleid_to, executionid 
	FROM (select * from samplelineage where sampleid_to<>sampleid_from) sl
            CONNECT BY PRIOR sl.sampleid_to = sl.sampleid_from
            START WITH sl.sampleid_to = 165124
        UNION
	SELECT sampleid_to, executionid
     	FROM (select * from samplelineage where sampleid_to<>sampleid_from) sl
            CONNECT BY PRIOR sl.sampleid_from = sl.sampleid_to
            START WITH sl.sampleid_to = 165124 
        UNION 
	SELECT sampleid_to, executionid 
	FROM samplelineage where sampleid_to=sampleid_from
) l
WHERE
 l.executionid = pe.executionid AND l.sampleid_to = cc.sampleid
 AND cc.containerid = c.containerid
ORDER BY pe.processdate desc
         */
   /*     String sql = 
            "SELECT l.sampleid_to, pe.executionid, c.containerid "+
            "FROM  processexecution pe, "+
            "containercell cc, "+
            "containerheader c, "+
            "( "+
            "SELECT sampleid_to, executionid "+
            "FROM (select * from samplelineage where sampleid_to<>sampleid_from) sl " +
            "CONNECT BY PRIOR sl.sampleid_to = sl.sampleid_from "+
            "START WITH sl.sampleid_to = "+ sampleId + " " +
            "UNION "+
            "SELECT sampleid_to, executionid "+
            "FROM (select * from samplelineage where sampleid_to<>sampleid_from) sl "+
            "CONNECT BY PRIOR sl.sampleid_from = sl.sampleid_to "+
            "START WITH sl.sampleid_to = " + sampleId +  " "+
            "UNION "+
            "SELECT sampleid_to, executionid "+
            "FROM samplelineage where sampleid_to=sampleid_from "+
            ") l "+
             "WHERE "+
            "l.executionid = pe.executionid AND l.sampleid_to = cc.sampleid "+
            "AND cc.containerid = c.containerid "+
            "ORDER BY pe.processdate desc ";
    */
        String sql = "SELECT DISTINCT s.sampleid, pe.executionid, pe.processdate " +
                     "FROM sample s, samplelineage sl, processexecution pe "+
                     "WHERE s.constructid = "+
                        "( "+
                            "Select constructid from sample " +
                            "WHERE sampleid = " + sampleId + " " +
                         ") AND s.sampleid=sl.sampleid_to " +
                     " AND "+
                     "sl.executionid = pe.executionid "+
                     "ORDER BY pe.processdate desc";
                     retThread = createThread(sql);
        return retThread;
    }

    
    /**
     * Helper method to run the sql query and actually create the thread.
     *
     * @param sql The sql to find the thread with.
     *
     * @exception FlexDatabaseException When there is a problem with the
     *      database
     * @exception FlexCoreException When there is a problem creating one of the 
     *      core object, this should never happen here.
     * @exception FlexProcessException When there is a problem creating a
     *      process object, this should never happen here.
     */
    private static Thread createThread(String sql) 
    throws FlexDatabaseException, FlexCoreException, FlexProcessException {
        List sampleList = new LinkedList();
        SampleThread thread = new SampleThread(sampleList);
        ResultSet rs = null;
        try {
            rs = DatabaseTransaction.getInstance().executeQuery(sql);
            while(rs.next()) {
                
                Sample sample = new Sample(rs.getInt("SAMPLEID"));
                
                Process curProcess = 
                    Process.findProcess(rs.getInt("EXECUTIONID"));
                
                ThreadElement curElem =
                    new ThreadElement(thread,curProcess,sample);
                sampleList.add(curElem);
            }
            
        } catch (SQLException sqlE) {
            FlexDatabaseException e =
            new FlexDatabaseException("SQL: " + sql +"\n" + sqlE);
            throw e;
        }
        return thread;
    }
    
} // End class SampleThread


/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
*/
