/*
 * OligoPlater.java
 * @date    06052001
 * @author  Wendy Mar
 *
 * This class first sort a list of OligoPattern objects according to
 * the cds length of the sequence which the oligos are derived from.
 * Then, the oligo samples are arranged in a saw-tooth pattern on the
 * oligo plates. Three files for oligo ordering are generated.
 *
 * modified 062801 wmar:    added method to output threee oligo order files
 *
 */

package edu.harvard.med.hip.flex.process;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.util.*;
import edu.harvard.med.hip.flex.workflow.*;
import java.util.*;
import java.sql.*;
import java.io.*;


public class OligoPlater
{
    
    protected static final String filePath = "/tmp/";
    //protected static final String filePath = "H:\\Dev\\OligoOrder\\";
    
    protected static final int positiveControlPosition = 1;
    
    protected static final String PositiveControlSampleType = Sample.CONTROL_POSITIVE;
    protected static final String NegativeControlSampleType = Sample.CONTROL_NEGATIVE;
    protected static final String oligoFivePrefix = "OU";
    protected static final String oligoClosePrefix = "OC";
    protected static final String oligoFusionPrefix = "OF";
    
    protected LinkedList oligoPatternList = null;
    protected LinkedList constructList = null;
    protected Plateset plateset;
    protected Connection conn = null;
    protected Container container_5p = null;
    protected Container container_3s = null;
    protected Container container_3op = null;
    protected Process process = null;
    
    protected String plateOutFileName1 = null;
    protected String plateOutFileName2 = null;
    protected String plateOutFileName3 = null;
    protected FileWriter plateWriter_5p = null;
    protected FileWriter plateWriter_3s = null;
    protected FileWriter plateWriter_3op = null;
    protected LinkedList fileNameList = new LinkedList();
    
    private Project     project = null;
    private Workflow    workflow = null;
    private Protocol    m_protocol = null;
    private Vector      m_nextProtocols = null;
    private int         m_negativeControlPosition = 96;
    private int         m_totalWells = 94;
    
    private boolean     m_isReorderSequences = true;
    private boolean     m_isPseudomonas = false;
    private String      m_plateType = "96 WELL OLIGO PLATE";
    
    protected int       m_mgcContainerId = -1;
    
    /**
     * Creates new OligoPlater
     */
    public OligoPlater(LinkedList oligoPatternList, LinkedList constructList,
    Connection c, Project project, Workflow workflow)
    throws FlexDatabaseException, IOException
    {
        this.oligoPatternList = oligoPatternList;
        this.constructList = constructList;
        this.conn = c;
        this.project = project;
        this.workflow = workflow;
        if (project.getId() == Project.PSEUDOMONAS) m_isPseudomonas = true;
        m_protocol = new Protocol(Protocol.GENERATE_OLIGO_ORDERS);
        m_nextProtocols = workflow.getNextProtocol(m_protocol);
    }
    
    /**
     * Creates new OligoPlater
     */
    public OligoPlater(LinkedList oligoPatternList, LinkedList constructList,
    Connection c, Project project, Workflow workflow, int totalWells,
    String plateType)
    throws FlexDatabaseException, IOException
    {
        this( oligoPatternList,  constructList, c,  project,  workflow);
        m_totalWells = totalWells;
        m_plateType = plateType;
        m_negativeControlPosition = m_totalWells + 2;
    }
    
   
    
    public void setTotalWells(int wells)
    { m_totalWells = wells; m_negativeControlPosition = m_totalWells + 2;}
    public int getTotalWells()
    { return m_totalWells;}
    public void setPlateType(String  ptype)
    { m_plateType = ptype;}
    public String getPlateType()
    { return m_plateType;}
    public void setReorderRequest(boolean isReorder)
    { m_isReorderSequences = isReorder;}
    public boolean getReorderRequest()
    { return m_isReorderSequences;}
    
    public void setMgcContainerId(int mgcContainerId)throws FlexDatabaseException
    {
        m_mgcContainerId = mgcContainerId;
        if (m_mgcContainerId != -1)
        {
            //mgc project set different protocol
            m_protocol = new Protocol(Protocol.MGC_DESIGN_CONSTRUCTS);
            m_nextProtocols = new Vector();
            m_nextProtocols.add(new Protocol(Protocol.RECEIVE_OLIGO_PLATES)  );
        }
    }
    
    /**
     * This class generates three oligo plates, sort the samples on the plate.
     * The containerheader, sample and containercell tables are updated
     */
    protected void updateOligoPlates() throws FlexDatabaseException, IOException
    {
        container_5p.insert(conn);
        
        if( ! m_isPseudomonas)        {  container_3s.insert(conn);     }
        container_3op.insert(conn);
        plateset.insert(conn);
        //insert process output: oligo containers
        insertProcessOutput();
        //insert receive oligo queue
        insertReceiveOligoQueue();
    }
    
    
    /**
     * This method creates sets of three oligo order file objects
     * and store them in a list
     *
     */
    public LinkedList generateOligoOrderFiles()
    {
        LinkedList fileList = new LinkedList();
        //System.out.println("Total oligo order files generated: "+numFile);
        Iterator iter = fileNameList.iterator();
        
        while (iter.hasNext())
        {
            String fileName = (String)iter.next();
            File oligoFile = new File(fileName);
            fileList.add(oligoFile);
        } //while
        return fileList;
    }
    
    /**
     * Precondition: all three of the oligo plate headers are created
     * and inserted. all of the oligos are calculated and inserted.
     * First, the oligos are sorted by cds-length and arranged by
     * saw-tooth pattern based on cds-length.
     * Second, the oligo sample objecsts are generated and the sample
     * records are added to the container.
     *
     */
    protected void generateOligoOrder() throws FlexDatabaseException, IOException
    {
        String oligo5p = null;
        String oligo3s = null;
        String oligo3op = null;
        int current_well = 0;
        boolean isNewPlate = true;
        int negativeControl = 0;
        OligoPattern currentGene = null;
        LinkedList orderedOligos = new LinkedList();
        LinkedList plate_oligo = new LinkedList();
        //insert process input: construct design
        insertProcessInput();
        
        
        // Sort oligos per plate in ascending order of CDS length.
        if ( m_isReorderSequences )
        {
            // Removed this line because the sorting by cdslength is integrated into
            // the saw-tooth pattern sorting algorithm. See Algorithms class. -dzuo
            //Collections.sort(oligoPatternList, new GeneComparator());
            for (int count = 1; count <= oligoPatternList.size() ; count++)
            {
                plate_oligo.add( oligoPatternList.get(count-1));
                if ( count % m_totalWells == 0)//plate finished
                {
                    orderedOligos.addAll(Algorithms.rearangeSawToothPatternInOligoPattern(plate_oligo));
                    plate_oligo.clear();
                }
            }
            if ( plate_oligo.size() != 0)//if last plate is not full add it
                orderedOligos.addAll(Algorithms.rearangeSawToothPatternInOligoPattern(plate_oligo));
        }
        else
        {
            orderedOligos = new LinkedList(oligoPatternList);
        }
        
        // Loop over plates. It is possible to generate more than one sets of plates
        for (int geneCount = 0 ; geneCount < orderedOligos.size(); geneCount++)
        {
            if (isNewPlate)
            {
                generateOligoPlate();
                negativeControl = ( geneCount + m_totalWells <= oligoPatternList.size() ) ?
                m_negativeControlPosition: oligoPatternList.size() - geneCount + 2;
                
                generateControlSamples(negativeControl);
                prepareFilesForOligoOrder();
                current_well = 2;
                isNewPlate = false;
            }
            currentGene = (OligoPattern)oligoPatternList.get(geneCount);
            //System.out.println("The lower gene index is: "+ currentGeneIndex);
            outputGene( currentGene , current_well);
            current_well++;
            if (current_well == negativeControl )
            {
                isNewPlate = true;
            }
            //System.out.println("Update oligo plates.");
            if (isNewPlate  )
            {
                updateOligoPlates();
                plateWriter_5p.flush();
                plateWriter_5p.close();
                
                if( ! m_isPseudomonas )
                {
                    plateWriter_3s.flush();
                    plateWriter_3s.close();
                }
                
                plateWriter_3op.flush();
                plateWriter_3op.close();
            }
            
        } //outter while to fill oligo plates
        
    } // generateOligoOrder
    
    
    /*function process one gene (oligoPattern)
     */
    protected void outputGene(OligoPattern currentGene, int well)throws FlexDatabaseException, IOException
    {
        Sample oligoSample_5p = null;
        Sample oligoSample_3s = null;
        Sample oligoSample_3op = null;
        String cont = null;
        //System.out.println("The lower gene index is: "+ currentGeneIndex);
        
        // generate three oligo sample objects and insert into sample table
        // also a record for each oligo sample is inserted into containercell table
        oligoSample_5p = generateOligoSample(currentGene,"5p", container_5p.getId(), well);
        //System.out.println("PlateID: "+container_5p.getId() +"; " + "5p lower oligo well: "+ well + "; "
        //+ "sampleID: "+ oligoSample_5p.getId() +"; "+ "oligoID: "+oligoSample_5p.getOligoid()+"; " + currentGene.getCDSLength());
        container_5p.addSample(oligoSample_5p);
        cont = container_5p.getLabel()+"\t" + currentGene.getOligoId_5p()+"\t";
        cont += currentGene.getOligoseq_5p()+"\t" + oligoSample_5p.getPosition()+"\n";
        plateWriter_5p.write(cont);
        
        if(  ! m_isPseudomonas )
        {
            oligoSample_3s = generateOligoSample(currentGene,"3s", container_3s.getId(), well);
            //System.out.println("PlateID: "+container_3s.getId()+"; " + "3s lower oligo well: "+ well + "; "
            //+ "sampleID: "+ oligoSample_3s.getId() +"; "+oligoSample_3s.getConstructid()+"; " + currentGene.getCDSLength());
            container_3s.addSample(oligoSample_3s);
            cont = container_3s.getLabel()+"\t" + currentGene.getOligoId_3s()+"\t";
            cont += currentGene.getOligoseq_3s()+"\t" + oligoSample_3s.getPosition()+"\n";
            plateWriter_3s.write(cont);
        }
        
        oligoSample_3op = generateOligoSample(currentGene,"3op", container_3op.getId(), well);
        //System.out.println("PlateID: "+container_3op.getId()+"; " + "3op lower oligo well: "+ well + "; "
        //+ "sampleID: "+ oligoSample_3op.getId() +"; "+oligoSample_3op.getConstructid()+"; " +currentGene.getCDSLength());
        
        container_3op.addSample(oligoSample_3op);
        cont = container_3op.getLabel()+"\t" + currentGene.getOligoId_3op()+"\t";
        cont += currentGene.getOligoseq_3op()+"\t" + oligoSample_3op.getPosition()+"\n";
        plateWriter_3op.write(cont);
    }
    
    protected void createOligoFileHeader() throws IOException
    {
        plateWriter_5p.write("Label \t OligoID \t OligoSequence \t Well \n");
        
        if( ! m_isPseudomonas )
        {
            plateWriter_3s.write("Label \t OligoID \t OligoSequence \t Well \n");
        }
        
        plateWriter_3op.write("Label \t OligoID \t OligoSequence \t Well \n");
    }
    
    /*
     *Function prepare files for oligo order
     */
    protected void prepareFilesForOligoOrder() throws FlexDatabaseException, IOException
    {
        //prepare files for oligo order
        plateOutFileName1 = filePath + container_5p.getLabel();
        plateWriter_5p = new FileWriter(plateOutFileName1);
        fileNameList.add(plateOutFileName1);
        
        if( ! m_isPseudomonas )
        {
            plateOutFileName2 = filePath + container_3s.getLabel();
            plateWriter_3s = new FileWriter(plateOutFileName2);
            fileNameList.add(plateOutFileName2);
        }
        
        plateOutFileName3 = filePath + container_3op.getLabel();
        plateWriter_3op = new FileWriter(plateOutFileName3);
        fileNameList.add(plateOutFileName3);
        createOligoFileHeader();
        
    }
    /**
     * This class insert three oligo plates (containerheader table)
     * and one plateset record (plateset table)
     */
    protected void generateOligoPlate() throws FlexDatabaseException
    {
        Location location = null;
        
        String locationType = Location.UNAVAILABLE;
        String label_5p = "default";
        String label_3s = "default";
        String label_3op = "default";
        
        // get the oligo container location object
        location = new Location();
        int locationId = location.getId(locationType);
        location = new Location(locationId,locationType,"");
        
        // generate new container header object
        container_5p = new Container(m_plateType, location,label_5p);
        //System.out.println("Created the 5p oligo plate: "+ container_5p.getId());
        
        if(! m_isPseudomonas)
        {
            container_3s = new Container(m_plateType, location,label_3s);
            //System.out.println("Created the 3s oligo plate: "+ container_3s.getId());
        }
        
        container_3op = new Container(m_plateType, location,label_3op);
        //System.out.println("Created the 3op oligo plate: "+ container_3op.getId());
        
        if(m_isPseudomonas) {
            plateset = generatePlateset(container_5p.getId(), container_3op.getId(), -1, m_mgcContainerId);
        } else {
            plateset = generatePlateset(container_5p.getId(), container_3op.getId(),container_3s.getId(), m_mgcContainerId);
        }
        
        String projectCode = "";
        Workflow wf = project.getWorkflow(workflow);
        if(wf != null)
        {
            projectCode = ((ProjectWorkflow)wf).getCode();
        }
        int threadid = FlexIDGenerator.getID("threadid");
        label_5p = Container.getLabel(projectCode, oligoFivePrefix, threadid, null); //upstream
        
        if( ! m_isPseudomonas )
        {
            label_3s = Container.getLabel(projectCode, oligoClosePrefix, threadid, null); //closed
        }
        
        label_3op = Container.getLabel(projectCode, oligoFusionPrefix, threadid, null); //fusion
        container_5p.setLabel(label_5p);
        
        if( ! m_isPseudomonas)
        {
            container_3s.setLabel(label_3s);
        }
        
        container_3op.setLabel(label_3op);
        
        // Update the threadid for each container.
        container_5p.setThreadid(threadid);
        
        if( ! m_isPseudomonas )
        {
            container_3s.setThreadid(threadid);
        }
        
        container_3op.setThreadid(threadid);
        
        //System.out.println("5p oligo plate label: "+ label_5p);
        //System.out.println("3s oligo plate label: "+ label_3s);
        //System.out.println("3op oligo plate label: "+ label_3op);
        
    }
    
    protected Plateset generatePlateset(int fivepId, int threepOpenId, int threepClosedId, int containerId)
    {
        Plateset plateset = null;
        
        if( m_isPseudomonas )
        {
            plateset = new Plateset(fivepId, threepOpenId, -1);
        } 
        else
        {
            plateset = new Plateset(fivepId, threepOpenId, threepClosedId, containerId);
        }
        
        return plateset;
    }
    
    protected Sample generateOligoSample(OligoPattern currentOligo, String oligoType, int plateId, int wellId)
    throws FlexDatabaseException
    {
        int oligoId;
        int constructid;
        String sampleType = null;
        String status = "G";
        Sample sample = null;
        
        if (oligoType.equals("5p"))
        {
            oligoId = currentOligo.getOligoId_5p();
            sampleType = "OLIGO_5P";
            sample = new Sample(sampleType, wellId, plateId, oligoId, status);
        }
        else if (oligoType.equals("3s"))
        {
            oligoId = currentOligo.getOligoId_3s();
            constructid = currentOligo.getCloseConstructid();
            sampleType = "OLIGO_3C";
            sample = new Sample(sampleType, wellId, plateId, constructid, oligoId, status);
        }
        else
        {
            oligoId = currentOligo.getOligoId_3op();
            constructid = currentOligo.getOpenConstructid();
            sampleType = "OLIGO_3F";
            sample = new Sample(sampleType, wellId, plateId, constructid, oligoId, status);
        }
        
        return sample;
    } //generateOligoSample
    
    /**
     * This method adds one positive and one negative control to each of
     * the three oligo plates
     */
    protected void generateControlSamples(int negativeControl)throws FlexDatabaseException
    {
        Sample control_positive = null;
        Sample control_negative = null;
        //add positive control samples
        control_positive = new Sample(PositiveControlSampleType,positiveControlPosition,container_5p.getId());
        container_5p.addSample(control_positive);
        
        if( ! m_isPseudomonas )
        {
            control_positive = new Sample(PositiveControlSampleType,positiveControlPosition,container_3s.getId());
            container_3s.addSample(control_positive);
        }
        
        control_positive = new Sample(PositiveControlSampleType,positiveControlPosition,container_3op.getId());
        container_3op.addSample(control_positive);
        
        //add negative samples
        control_negative = new Sample(NegativeControlSampleType,negativeControl,container_5p.getId());
        container_5p.addSample(control_negative);
        
        if( ! m_isPseudomonas )
        {
            control_negative = new Sample(NegativeControlSampleType,negativeControl,container_3s.getId());
            container_3s.addSample(control_negative);
        }
        
        control_negative = new Sample(NegativeControlSampleType,negativeControl,container_3op.getId());
        container_3op.addSample(control_negative);
        
    }
    
    /**
     * insert process execution io for "generate oligo order" protocol
     */
    protected void insertProcessInput() throws FlexDatabaseException
    {
        //System.out.println("insert process input record...");
       
        Researcher r = new Researcher();
        String status = "S"; //SUCCESS
        String extraInfo = "";
        
        int userId = r.getId("SYSTEM");
        r = new Researcher(userId);
        
        // insert process execution record into process execution table
        process = new Process(m_protocol,status,r, project, workflow);
        
        //System.out.println("Generate oligo orders Execution ID: "+ process.getExecutionid());
        
        //Insert one process input records for each output construct design (two per sequence)
        ListIterator iter = constructList.listIterator();
        String ioType = "I";
        Construct construct = null;
        ConstructProcessObject cpo = null;
        int constructId = -1;
        
        while (iter.hasNext())
        {
            construct = (Construct)iter.next();
            constructId = construct.getId();
            cpo = new ConstructProcessObject(constructId,process.getExecutionid(),ioType);
            process.addProcessObject(cpo);
        } //while
        process.insert(conn);
        
    } //insertProcessInput
    
    /**
     * insert the output oligo plates to process object table
     */
    protected void insertProcessOutput() throws FlexDatabaseException
    {
        //System.out.println("insert process output record...");
        //insert the container output object
        String ioType = "O";
        ContainerProcessObject platepo_5p =
        new ContainerProcessObject(container_5p.getId(),process.getExecutionid(),ioType);
        platepo_5p.insert(conn);
        
        if( ! m_isPseudomonas )
        {
            ContainerProcessObject platepo_3s =
            new ContainerProcessObject(container_3s.getId(),process.getExecutionid(),ioType);
            platepo_3s.insert(conn);
        }
        
        ContainerProcessObject platepo_3op =
        new ContainerProcessObject(container_3op.getId(),process.getExecutionid(),ioType);
        platepo_3op.insert(conn);
    }
    
    /**
     * insert "receive oligo plates" queue record for each plate created
     */
    protected void insertReceiveOligoQueue() throws FlexDatabaseException
    {
        //System.out.println("insert receive oligo queue record...");
        QueueItem queueItem = null;
        ContainerProcessQueue containerQueue = new ContainerProcessQueue();
        
        Iterator iter = m_nextProtocols.iterator();
        while(iter.hasNext())
        {
            Protocol nextProtocol = (Protocol)iter.next();
            LinkedList containerQueueItemList = new LinkedList();
            
            queueItem = new QueueItem(container_5p, nextProtocol, project, workflow);
            containerQueueItemList.add(queueItem);
            
            if(! m_isPseudomonas)
            {
                queueItem = new QueueItem(container_3s, nextProtocol, project, workflow);
                containerQueueItemList.add(queueItem);
            }
            
            queueItem = new QueueItem(container_3op, nextProtocol, project, workflow);
            containerQueueItemList.add(queueItem);
            
            //System.out.println("Adding receive oligo plates to queue...");
            containerQueue.addQueueItems(containerQueueItemList, conn);
        }
    } //insertReceiveOligoQueue
    
    /**
     * delete the order oligos queue records referencing the two constructdesign
     */
    protected void removeOrderOligoQueue() throws FlexDatabaseException
    {
        
              
        String sql = "DELETE FROM queue\n" +
        "WHERE protocolid = "+ m_protocol.getId()  + "\n" +
        "AND constructid = ?" + "\n" +
        "AND workflowid = "+workflow.getId()+"\n"+
        "AND projectid = "+project.getId();
        PreparedStatement stmt = null;
        try
        {
            stmt = conn.prepareStatement(sql);
            ListIterator iter = constructList.listIterator();
            
            while (iter.hasNext())
            {
                Construct construct = (Construct) iter.next();
                int constructid = construct.getId();
                stmt.setInt(1, constructid);
                DatabaseTransaction.executeUpdate(stmt);
            }
            
        } catch(SQLException sqlE)
        {
            throw new FlexDatabaseException("Error occured while deleting constructs from queue\n"+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeStatement(stmt);
        }
    }
    
    
    /** Note: this comparator imposes orderings that are
     * inconsistent with equals. */
    class GeneComparator implements Comparator
    {
        public int compare(Object o1, Object o2)
        {
            int result = 0;
            OligoPattern lhs = (OligoPattern) o1;
            OligoPattern rhs = (OligoPattern) o2;
            int lhsLength = lhs.getCDSLength();
            int rhsLength = rhs.getCDSLength();
            result = lhsLength-rhsLength;
            return result;
        }
        
        public boolean equals(java.lang.Object obj)
        {
            return false;
        }
        // compare
        
    } //GeneComparator
    
    
    
}
