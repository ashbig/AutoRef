/*
 * ConstructGenerator.java
 *
 * Created on May 31, 2001, 4:50 PM
 * @author  Wendy Mar
 * @version
 *
 * This class represents a process that generates three types of
 * oligos and two forms of constructs for a list of 94 sequence
 * objects. A DESIGN_CONSTRUCT protocl Process Execution record
 * is inserted in the ProcessExecution table. Also, one Process
 * Object input record for each input Flex sequence is inserted
 * in the ProcessObject table, one Process Object output records
 * for each new output construct is inserted in the ProcessObject
 * table.
 */

package edu.harvard.med.hip.flex.process;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.util.*;
import java.sql.*;
import java.util.*;


public class ConstructGenerator {
    private LinkedList seqList;
    private Connection conn;
    private LinkedList oligoPatternList;
    
    /** Creates new ConstructGenerator */
    public ConstructGenerator(LinkedList seqList) throws FlexDatabaseException {
        this.seqList = seqList;
        this.oligoPatternList = new LinkedList();
        try{
            this.conn = DatabaseTransaction.getInstance().requestConnection();
        } catch(FlexDatabaseException sqlex){
            throw new FlexDatabaseException(sqlex);
        }
    }
    
    /**
     * Precondition: there is a group of 94 sequences in the
     * same size class in the Queue table awaiting for
     * oligo primer calculation and construct generation.
     *
     * First, all three types of oligos are calculated.
     * Then, two new constructs are created, also the
     * pairID for each pair of open and closed constructs
     * from the same sequence are generated.
     *
     * @return A linkedlist of 94 OligoPattern objects
     */
    protected void getConstructs()throws FlexDatabaseException {
        Sequence seq = null;
        Oligo result_5p = null;
        Oligo result_3s = null;
        Oligo result_3op = null;
        OligoPattern pattern = null;
        Construct close = null;
        Construct open = null;
        int seqId = 0;
        int cdsLength = 0;
        int oligoID_5p = 0;
        int oligoID_3s = 0;
        int oligoID_3op = 0;
        int pairId = 0; //construct pairID
        int platesetId = 0; //oligo platesetID
        
        // insert design constructs protocl process execution
        //insertProcessExecution();
        NNPrimerCalculator pc = new NNPrimerCalculator();
        platesetId = setPlatesetId(); //create new plateset ID
        
        ListIterator iter = seqList.listIterator();
        
        while (iter.hasNext()) {
            seq = (Sequence) iter.next(); //retrieves one sequence from the list
            seqId = seq.getSeqID();
            cdsLength = seq.getCDSLength();
            
            // calculate all three types of oligos for each sequence
            // and insert oligo infor into the oligo table
            try{
                result_5p = pc.calculateFivepOligo(seq);
                result_5p.insertOligo(conn);
                result_3s = pc.calculateThreepCloseOligo(seq);
                result_3s.insertOligo(conn);
                result_3op = pc.calculateThreepOpenOligo(seq);
                result_3op.insertOligo(conn);
            } catch(FlexDatabaseException sqlex){
                throw new FlexDatabaseException(sqlex);
            }
            
            //get the group of three oligoIds
            oligoID_5p = result_5p.getOligoID();
            oligoID_3s = result_3s.getOligoID();
            oligoID_3op = result_3op.getOligoID();
            
            //create the OligoPattern object and store it in a linked list
            pattern = new OligoPattern(oligoID_5p, oligoID_3s, oligoID_3op, seqId, cdsLength);
            oligoPatternList.add(pattern);
            
            //generate construct pairID
            pairId = setPairId();
            
            // create two new constructs: open and close form and insert them
            // to the ConstructDesign table
            close = new Construct(seq,result_5p,result_3s, "Close", pairId,platesetId);
            open = new Construct(seq,result_5p,result_3op, "Open", pairId,platesetId);
            close.insert(conn);
            open.insert(conn);
            
            
        } //while
    } //getConstructs
    
    public LinkedList getOligoPatternList () {
        return oligoPatternList;
    }
    
    /**
     * set the oligo platesetId
     *
     * @return The platesetId
     */
    private int setPlatesetId() throws FlexDatabaseException {
        try{
            int   platesetId = FlexIDGenerator.getID("platesetid");
            return platesetId;
        }catch(FlexDatabaseException sqlex){
            throw new FlexDatabaseException(sqlex);
        }
    }
    
    private int setPairId() throws FlexDatabaseException {
        try{
            int    pairId = FlexIDGenerator.getID("constructpairid");
            return pairId;
        }catch(FlexDatabaseException sqlex){
            throw new FlexDatabaseException(sqlex);
        }
    }
    
} //ConstructGenerator
