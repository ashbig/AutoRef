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
 *
 * Revision:    07-01-2001  [wmar]
 *              removed the setPlatesetId method and the platsetId data member
 * Revision:    01-28-2002  [wmar]
 *              The 5p and 3p tag added to oligos are project specific
 */

package edu.harvard.med.hip.flex.process;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.util.*;
import edu.harvard.med.hip.flex.workflow.*;
import java.sql.*;
import java.util.*;


public class ConstructGenerator {
    private LinkedList seqList;
    private Connection conn = null;
    private LinkedList oligoPatternList;
    private LinkedList constructList;
    private Project project = null;
    private Workflow workflow = null;
    private Protocol m_protocol = null;
    
    /**
     * Constructor
     * Creates new ConstructGenerator
     */
    public ConstructGenerator(LinkedList seqList, Connection c) throws FlexDatabaseException {
        this.seqList = seqList;
        this.conn = c;
        this.oligoPatternList = new LinkedList();
        this.constructList = new LinkedList();
        m_protocol = new Protocol(Protocol.DESIGN_CONSTRUCTS);
    }
    
    /**
     * Constructor.
     *
     * @param seqList The list of sequences for construct design.
     * @param c The Connection object for database transaction.
     * @param project The project for the sequence construct design.
     * @param workflow The workflow for the sequence construct design.
     *
     * @return The ConstructGenerator object.
     */
    public ConstructGenerator(LinkedList seqList, Connection c, Project project,
    Workflow workflow) throws FlexDatabaseException {
        this(seqList,c );
        this.project = project;
        this.workflow = workflow;
        
    }
    
    /**
     * Constructor.
     *
     * @param seqList The list of sequences for construct design.
     * @param c The Connection object for database transaction.
     * @param project The project for the sequence construct design.
     * @param workflow The workflow for the sequence construct design.
     *
     * @return The ConstructGenerator object.
     */
    public ConstructGenerator(LinkedList seqList, Connection c, Project project,
    Workflow workflow, Protocol protocol) throws FlexDatabaseException {
        this(  seqList,  c,  project, workflow);
        m_protocol = protocol;
        this.oligoPatternList = new LinkedList();
        this.constructList = new LinkedList();
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
    protected void generateOligoAndConstructs()throws FlexDatabaseException {
        if(Project.PSEUDOMONAS == project.getId()) {
            generatePMOligoAndConstructs();
            return;
        }
        
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
        int count = 0;
        
        // insert design constructs protocl process execution
        // insertProcessExecution();
        NNPrimerCalculator pc = new NNPrimerCalculator();
        ListIterator iter = seqList.listIterator();
        
        while (iter.hasNext()) {
            seq = (Sequence) iter.next(); //retrieves one sequence from the list
            seqId = seq.getId();
            cdsLength = seq.getCDSLength();
            count++;
            
            //System.out.println("Calculate oligos for sequence count: " + count);
            //System.out.println("SequenceID: " + seqId + "   " + cdsLength);
            // calculate all three types of oligos for each sequence
            // and insert oligo info into the oligo table
            try{
                result_5p = pc.calculateFivepOligo(seq);
                result_5p.setTagSequence_5p(project, workflow);
                result_5p.insert(conn);
                
                //all 3p close oligos have universal stop from the add on tag
                result_3s = pc.calculateThreepOpenOligo(seq);
                result_3s.setTagSequence_3p_Close(project, workflow);
                result_3s.insert(conn);
                
                //all 3p fusion oligos mutated their stop codon to lysine: CAA
                result_3op = pc.calculateThreepOpenOligo(seq);
                result_3op.setTagSequence_3p_Fusion(project, workflow);
                result_3op.insert(conn);
            } catch(FlexDatabaseException sqlex){
                throw new FlexDatabaseException(sqlex);
            }
            
            //get the group of three oligoIds derived from the same sequence
            oligoID_5p = result_5p.getOligoID();
            oligoID_3s = result_3s.getOligoID();
            oligoID_3op = result_3op.getOligoID();
            
            //generate construct pairID: a open and a close construct derived from
            //the same sequence have the same pair id.
            pairId = setPairId(); //not working!!!
            
            // create two new constructs: open and close form and insert them
            // to the ConstructDesign table
            close = new Construct(seq,result_5p,result_3s, "CLOSED", pairId, project, workflow);
            open = new Construct(seq,result_5p,result_3op, "FUSION", pairId, project, workflow);
            constructList.add(close);
            constructList.add(open);
            //System.out.println("inserting constructs: ");
            close.insert(conn);
            open.insert(conn);
            //System.out.println("close construct ID: " + close.getId());
            //System.out.println("open construct ID: " + open.getId());
            
            //create the OligoPattern object and store it in a linked list
            pattern = new OligoPattern(oligoID_5p, oligoID_3s, oligoID_3op,
            result_5p.getTagOligoSequence(), result_3s.getTagOligoSequence(), result_3op.getTagOligoSequence(),
            close.getId(), open.getId(), cdsLength);
            oligoPatternList.add(pattern);
        } //while
    } //generateOligoAndConstructs

    public void generatePMOligoAndConstructs() throws FlexDatabaseException {
        Sequence seq = null;
        Oligo result_5p = null;
        Oligo result_3op = null;
        OligoPattern pattern = null;
        Construct open = null;
        int seqId = 0;
        int cdsLength = 0;
        int oligoID_5p = 0;
        int oligoID_3op = 0;
        int pairId = 0; //construct pairID
        int count = 0;
        
        // insert design constructs protocl process execution
        // insertProcessExecution();
        PMNNPrimerCalculator pc = new PMNNPrimerCalculator();
        ListIterator iter = seqList.listIterator();
        
        while (iter.hasNext()) {
            seq = (Sequence) iter.next(); //retrieves one sequence from the list
            seqId = seq.getId();
            cdsLength = seq.getCDSLength();
            count++;
            
            //System.out.println("Calculate oligos for sequence count: " + count);
            //System.out.println("SequenceID: " + seqId + "   " + cdsLength);
            // calculate all three types of oligos for each sequence
            // and insert oligo info into the oligo table
            try{
                result_5p = pc.calculateFivepOligo(seq);
                result_5p.setTagSequence_5p(project, workflow);
                result_5p.insert(conn);
                
                //all 3p fusion oligos mutated their stop codon to lysine: CAA
                result_3op = pc.calculateThreepOpenOligo(seq);
                result_3op.setTagSequence_3p_Fusion(project, workflow);
                result_3op.insert(conn);
            } catch(FlexDatabaseException sqlex){
                throw new FlexDatabaseException(sqlex);
            }
            
            //get the group of three oligoIds derived from the same sequence
            oligoID_5p = result_5p.getOligoID();
            oligoID_3op = result_3op.getOligoID();
            
            //generate construct pairID: a open and a close construct derived from
            //the same sequence have the same pair id.
            pairId = setPairId(); //not working!!!
            
            // create two new constructs: open and close form and insert them
            // to the ConstructDesign table
            open = new Construct(seq,result_5p,result_3op, "FUSION", pairId, project, workflow);
            constructList.add(open);
            //System.out.println("inserting constructs: ");
            open.insert(conn);
            //System.out.println("close construct ID: " + close.getId());
            //System.out.println("open construct ID: " + open.getId());
            
            //create the OligoPattern object and store it in a linked list
            pattern = new OligoPattern(oligoID_5p, -1, oligoID_3op,
            result_5p.getTagOligoSequence(), null, result_3op.getTagOligoSequence(),
            -1, open.getId(), cdsLength);
            oligoPatternList.add(pattern);
        } //while
    }   
    
    /**
     * @return The list of oligoPattern objects
     */
    public LinkedList getOligoPatternList() {
        return oligoPatternList;
    }
    
    /**
     * @return The list of newly generated constructs
     */
    public LinkedList getConstructList(){
        return this.constructList;
    }
    
    /**
     * Insert Process Execution record for Design Construct protocol
     * Insert one process object input record for each input sequence
     */
    protected void insertProcessInputOutput() throws FlexDatabaseException {
        Process process = null;
        
        Researcher r = new Researcher();
        String status = "S"; //SUCCESS
        
        int userId = r.getId("SYSTEM");
        r = new Researcher(userId);
        // insert process execution record into process execution table
        process = new Process(m_protocol,status,r, project, workflow);
        int executionId = process.getExecutionid();
        process.insert(conn);
        //System.out.println("Insert process execution for design constructs...");
        //System.out.println("Design Construct Execution ID: "+ process.getExecutionid());
        
        //insert sequence input process objects
        String ioFlag = "I";
        ListIterator iter = seqList.listIterator();
        Sequence seq = null;
        SequenceProcessObject spo = null;
        int seqId = -1;
        
        //System.out.println("Inserting sequence input object...");
        while (iter.hasNext()) {
            seq = (Sequence) iter.next(); //retrieves one sequence from the list
            seqId = seq.getId();
            spo = new SequenceProcessObject(seqId,executionId,ioFlag);
            spo.insert(conn);
        } //while
        
        //Insert one process output records for each output construct
        //(two per sequence)
        ListIterator iter_construct = constructList.listIterator();
        String ioType = "O";
        Construct construct = null;
        ConstructProcessObject cpo = null;
        int constructId = -1;
        //System.out.println("Inserting construct process output object...");
        while (iter.hasNext()){
            construct = (Construct)iter.next();
            constructId = construct.getId();
            cpo = new ConstructProcessObject(constructId,executionId,ioType);
            cpo.insert(conn);
        } //while
        
    }
    
    protected void insertConstructQueue() throws FlexDatabaseException {
        Protocol protocol = new Protocol(Protocol.DESIGN_CONSTRUCTS);
        Vector nextProtocols = workflow.getNextProtocol(protocol);
        
        
        ListIterator iter = constructList.listIterator();
        ConstructProcessQueue constructQueue = new ConstructProcessQueue();
        QueueItem queueItem = null;
        Construct construct = null;
        
        
        Iterator protocolIter = nextProtocols.iterator();
        while(protocolIter.hasNext()) {
            Protocol nextProtocol = (Protocol)protocolIter.next();
            
            //form a linkedlist of construct queue items
            LinkedList constructQueueItemList = new LinkedList();
            while (iter.hasNext()){
                construct = (Construct)iter.next();
                queueItem = new QueueItem(construct, nextProtocol, project, workflow);
                constructQueueItemList.add(queueItem);
            } //while
            
            //System.out.println("Adding constructs to queue...");
            constructQueue.addQueueItems(constructQueueItemList, conn);
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
